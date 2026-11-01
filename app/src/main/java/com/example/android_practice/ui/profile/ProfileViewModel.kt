package com.example.android_practice.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_practice.domain.error.HttpStatusException
import com.example.android_practice.domain.error.InvalidUrlException
import com.example.android_practice.domain.error.NetworkUnavailableException
import com.example.android_practice.domain.error.NotAFileException
import com.example.android_practice.domain.error.ResourceNotFoundException
import com.example.android_practice.domain.model.UserProfile
import com.example.android_practice.domain.usecase.DownloadResumeUseCase
import com.example.android_practice.domain.usecase.ObserveProfileUseCase
import com.example.android_practice.ui.profile.model.ProfileUiModel
import com.example.android_practice.ui.profile.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ProfileEvent {
    data class OpenFile(val uri: String, val mimeType: String) : ProfileEvent
    data class ShowMessage(val message: String) : ProfileEvent
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val observeProfileUseCase: ObserveProfileUseCase,
    private val downloadResumeUseCase: DownloadResumeUseCase
) : ViewModel() {

    private val _uiModel = MutableStateFlow(ProfileUiModel())
    val uiModel: StateFlow<ProfileUiModel> = _uiModel.asStateFlow()

    private val _events = Channel<ProfileEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var currentProfile: UserProfile = UserProfile()

    init {
        viewModelScope.launch {
            observeProfileUseCase().collectLatest { profile ->
                currentProfile = profile
                _uiModel.value = profile.toUiModel(isDownloading = _uiModel.value.isDownloading)
            }
        }
    }

    fun downloadAndOpenResume() {
        if (_uiModel.value.isDownloading) return

        val url = currentProfile.resumeUrl
        if (url.isBlank()) {
            viewModelScope.launch {
                _events.send(ProfileEvent.ShowMessage("Ссылка на резюме не указана"))
            }
            return
        }

        viewModelScope.launch {
            _uiModel.value = currentProfile.toUiModel(isDownloading = true)
            try {
                val file = downloadResumeUseCase(url)
                _events.send(ProfileEvent.OpenFile(file.uri, file.mimeType))
            } catch (e: CancellationException) {
                throw e
            } catch (e: NetworkUnavailableException) {
                _events.send(ProfileEvent.ShowMessage("Нет подключения к интернету"))
            } catch (e: ResourceNotFoundException) {
                _events.send(ProfileEvent.ShowMessage("Файл по ссылке не найден (404)"))
            } catch (e: NotAFileException) {
                _events.send(ProfileEvent.ShowMessage("Ссылка ведёт на страницу, а не на файл"))
            } catch (e: InvalidUrlException) {
                _events.send(ProfileEvent.ShowMessage(e.message ?: "Некорректная ссылка"))
            } catch (e: HttpStatusException) {
                val message = when (e.code) {
                    400 -> "Некорректный запрос (400). Проверьте правильность ссылки"
                    403 -> "Доступ к файлу запрещён (403). Проверьте права доступа по ссылке"
                    401 -> "Требуется авторизация (401)"
                    404 -> "Файл по ссылке не найден (404)"
                    in 500..599 -> "Сервер временно недоступен (${e.code})"
                    else -> "Ошибка сервера (${e.code})"
                }
                _events.send(ProfileEvent.ShowMessage(message))
            } catch (e: Exception) {
                _events.send(ProfileEvent.ShowMessage("Не удалось скачать резюме"))
            } finally {
                _uiModel.value = currentProfile.toUiModel(isDownloading = false)
            }
        }
    }
}
