package com.example.android_practice.ui.profile.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_practice.domain.error.InvalidUrlException
import com.example.android_practice.domain.model.CameraTarget
import com.example.android_practice.domain.model.UserProfile
import com.example.android_practice.domain.usecase.DiscardAvatarUseCase
import com.example.android_practice.domain.usecase.ImportAvatarUseCase
import com.example.android_practice.domain.usecase.ObserveProfileUseCase
import com.example.android_practice.domain.usecase.PrepareCameraAvatarUseCase
import com.example.android_practice.domain.usecase.SaveProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileEditState(
    val fullName: String = "",
    val position: String = "",
    val resumeUrl: String = "",
    val avatarUri: String? = null,
    val resumeUrlError: String? = null,
    val isSaving: Boolean = false,
    val isInitialized: Boolean = false
)

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val observeProfileUseCase: ObserveProfileUseCase,
    private val saveProfileUseCase: SaveProfileUseCase,
    private val importAvatarUseCase: ImportAvatarUseCase,
    private val prepareCameraAvatarUseCase: PrepareCameraAvatarUseCase,
    private val discardAvatarUseCase: DiscardAvatarUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileEditState())
    val state: StateFlow<ProfileEditState> = _state.asStateFlow()

    private var initialAvatarUri: String? = null
    private var pendingCameraTarget: CameraTarget?
        get() {
            val capture = savedStateHandle.get<String>("pending_cam_capture") ?: return null
            val stored = savedStateHandle.get<String>("pending_cam_stored") ?: return null
            return CameraTarget(capture, stored)
        }
        set(value) {
            savedStateHandle["pending_cam_capture"] = value?.captureUri
            savedStateHandle["pending_cam_stored"] = value?.storedUri
        }

    init {
        viewModelScope.launch {
            val initialProfile = observeProfileUseCase().first()
            if (!_state.value.isInitialized) {
                initialAvatarUri = initialProfile.avatarUri
                _state.value = ProfileEditState(
                    fullName = initialProfile.fullName,
                    position = initialProfile.position,
                    resumeUrl = initialProfile.resumeUrl,
                    avatarUri = initialProfile.avatarUri,
                    isInitialized = true
                )
            }
        }
    }

    fun onFullNameChange(value: String) {
        if (value.length <= 100) {
            _state.value = _state.value.copy(fullName = value)
        }
    }

    fun onPositionChange(value: String) {
        if (value.length <= 100) {
            _state.value = _state.value.copy(position = value)
        }
    }

    fun onResumeUrlChange(value: String) {
        _state.value = _state.value.copy(
            resumeUrl = value,
            resumeUrlError = null
        )
    }

    fun onGalleryImageSelected(sourceUri: String) {
        viewModelScope.launch {
            try {
                val storedUri = importAvatarUseCase(sourceUri)
                val oldAvatar = _state.value.avatarUri
                _state.value = _state.value.copy(avatarUri = storedUri)

                if (oldAvatar != null && oldAvatar != initialAvatarUri) {
                    discardAvatarUseCase(oldAvatar)
                }
            } catch (e: Exception) {
                // Ignore or handle
            }
        }
    }

    suspend fun prepareCameraCaptureUri(): String {
        val target = prepareCameraAvatarUseCase()
        pendingCameraTarget = target
        return target.captureUri
    }

    fun onCameraPhotoCaptured(success: Boolean) {
        val target = pendingCameraTarget ?: return
        pendingCameraTarget = null

        viewModelScope.launch {
            if (success) {
                val oldAvatar = _state.value.avatarUri
                _state.value = _state.value.copy(avatarUri = target.storedUri)

                if (oldAvatar != null && oldAvatar != initialAvatarUri) {
                    discardAvatarUseCase(oldAvatar)
                }
            } else {
                discardAvatarUseCase(target.storedUri)
            }
        }
    }

    fun onRemoveAvatar() {
        val oldAvatar = _state.value.avatarUri
        _state.value = _state.value.copy(avatarUri = null)

        if (oldAvatar != null && oldAvatar != initialAvatarUri) {
            viewModelScope.launch {
                discardAvatarUseCase(oldAvatar)
            }
        }
    }

    fun save(onSuccess: () -> Unit) {
        if (_state.value.isSaving) return

        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true, resumeUrlError = null)
            val currentState = _state.value

            val profileToSave = UserProfile(
                fullName = currentState.fullName,
                position = currentState.position,
                avatarUri = currentState.avatarUri,
                resumeUrl = currentState.resumeUrl
            )

            try {
                saveProfileUseCase(profileToSave)

                // Delete previous original avatar if it was replaced
                if (initialAvatarUri != null && initialAvatarUri != currentState.avatarUri) {
                    discardAvatarUseCase(initialAvatarUri!!)
                }

                onSuccess()
            } catch (e: InvalidUrlException) {
                _state.value = _state.value.copy(
                    isSaving = false,
                    resumeUrlError = e.message ?: "Некорректная ссылка"
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isSaving = false,
                    resumeUrlError = "Ошибка сохранения профиля"
                )
            }
        }
    }

    fun cancel() {
        val currentAvatar = _state.value.avatarUri
        if (currentAvatar != null && currentAvatar != initialAvatarUri) {
            viewModelScope.launch {
                discardAvatarUseCase(currentAvatar)
            }
        }
    }
}
