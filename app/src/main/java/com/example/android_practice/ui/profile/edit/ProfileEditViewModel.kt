package com.example.android_practice.ui.profile.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_practice.domain.error.InvalidReminderTimeException
import com.example.android_practice.domain.error.InvalidUrlException
import com.example.android_practice.domain.model.CameraTarget
import com.example.android_practice.domain.model.ReminderTime
import com.example.android_practice.domain.model.UserProfile
import com.example.android_practice.domain.usecase.CancelReminderUseCase
import com.example.android_practice.domain.usecase.DiscardAvatarUseCase
import com.example.android_practice.domain.usecase.ImportAvatarUseCase
import com.example.android_practice.domain.usecase.ObserveProfileUseCase
import com.example.android_practice.domain.usecase.PrepareCameraAvatarUseCase
import com.example.android_practice.domain.usecase.SaveProfileUseCase
import com.example.android_practice.domain.usecase.ScheduleReminderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SnackbarActionType {
    APP_NOTIFICATION_SETTINGS,
    EXACT_ALARM_SETTINGS
}

sealed interface ProfileEditEvent {
    object RequestNotificationPermission : ProfileEditEvent
    object SaveSuccess : ProfileEditEvent
    data class ShowSnackbar(
        val message: String,
        val actionLabel: String? = null,
        val actionType: SnackbarActionType? = null
    ) : ProfileEditEvent
}

data class ProfileEditState(
    val fullName: String = "",
    val position: String = "",
    val resumeUrl: String = "",
    val reminderTimeText: String = "",
    val avatarUri: String? = null,
    val resumeUrlError: String? = null,
    val reminderTimeError: String? = null,
    val isReminderTimeTouched: Boolean = false,
    val isSaving: Boolean = false,
    val isInitialized: Boolean = false
) {
    val isReminderTimeValid: Boolean
        get() = reminderTimeText.isBlank() || ReminderTime.parse(reminderTimeText) != null
}

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val observeProfileUseCase: ObserveProfileUseCase,
    private val saveProfileUseCase: SaveProfileUseCase,
    private val scheduleReminderUseCase: ScheduleReminderUseCase,
    private val cancelReminderUseCase: CancelReminderUseCase,
    private val importAvatarUseCase: ImportAvatarUseCase,
    private val prepareCameraAvatarUseCase: PrepareCameraAvatarUseCase,
    private val discardAvatarUseCase: DiscardAvatarUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileEditState())
    val state: StateFlow<ProfileEditState> = _state.asStateFlow()

    private val _events = Channel<ProfileEditEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

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
                    reminderTimeText = initialProfile.reminderTime?.format() ?: "",
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

    fun onReminderTimeChange(value: String) {
        val currentText = _state.value.reminderTimeText
        val formatted = formatTimeInput(value, currentText)

        val isTouched = _state.value.isReminderTimeTouched
        val error = if (isTouched && formatted.isNotBlank() && ReminderTime.parse(formatted) == null) {
            "Некорректный формат времени"
        } else {
            null
        }

        _state.value = _state.value.copy(
            reminderTimeText = formatted,
            reminderTimeError = error
        )
    }

    fun onReminderTimeBlur() {
        val text = _state.value.reminderTimeText.trim()
        val error = if (text.isNotBlank() && ReminderTime.parse(text) == null) {
            "Некорректный формат времени"
        } else {
            null
        }
        _state.value = _state.value.copy(
            isReminderTimeTouched = true,
            reminderTimeError = error
        )
    }

    fun onReminderTimeSelected(hour: Int, minute: Int) {
        val time = ReminderTime(hour, minute)
        _state.value = _state.value.copy(
            reminderTimeText = time.format(),
            isReminderTimeTouched = true,
            reminderTimeError = null
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
                // Ignore
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

    fun onSaveClicked(hasNotificationPermission: Boolean, isPermissionCheckRequired: Boolean) {
        if (_state.value.isSaving) return

        val currentState = _state.value
        val rawTime = currentState.reminderTimeText.trim()

        if (rawTime.isNotBlank() && ReminderTime.parse(rawTime) == null) {
            _state.value = currentState.copy(
                isReminderTimeTouched = true,
                reminderTimeError = "Некорректный формат времени"
            )
            return
        }

        val parsedTime = if (rawTime.isBlank()) null else ReminderTime.parse(rawTime)

        if (parsedTime != null && isPermissionCheckRequired && !hasNotificationPermission) {
            viewModelScope.launch {
                _events.send(ProfileEditEvent.RequestNotificationPermission)
            }
        } else {
            performSave(permissionGranted = hasNotificationPermission)
        }
    }

    fun onNotificationPermissionResult(granted: Boolean) {
        performSave(permissionGranted = granted)
    }

    private fun performSave(permissionGranted: Boolean) {
        if (_state.value.isSaving) return

        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true, resumeUrlError = null, reminderTimeError = null)
            val currentState = _state.value
            val rawTime = currentState.reminderTimeText.trim()

            if (rawTime.isNotBlank() && ReminderTime.parse(rawTime) == null) {
                _state.value = currentState.copy(
                    isSaving = false,
                    isReminderTimeTouched = true,
                    reminderTimeError = "Некорректный формат времени"
                )
                return@launch
            }

            val parsedTime = if (rawTime.isBlank()) null else ReminderTime.parse(rawTime)

            val profileToSave = UserProfile(
                fullName = currentState.fullName,
                position = currentState.position,
                avatarUri = currentState.avatarUri,
                resumeUrl = currentState.resumeUrl,
                reminderTime = parsedTime
            )

            try {
                saveProfileUseCase(profileToSave)

                if (parsedTime != null) {
                    val scheduleResult = scheduleReminderUseCase(parsedTime, currentState.fullName)
                    val timeFormatted = parsedTime.format()
                    when {
                        !permissionGranted -> {
                            _events.send(
                                ProfileEditEvent.ShowSnackbar(
                                    message = "Уведомления отключены, напоминание не появится",
                                    actionLabel = "Настройки",
                                    actionType = SnackbarActionType.APP_NOTIFICATION_SETTINGS
                                )
                            )
                        }
                        !scheduleResult.isExact -> {
                            _events.send(
                                ProfileEditEvent.ShowSnackbar(
                                    message = "Точное время недоступно, напоминание может прийти позже",
                                    actionLabel = "Разрешить",
                                    actionType = SnackbarActionType.EXACT_ALARM_SETTINGS
                                )
                            )
                        }
                        scheduleResult.isNextDay -> {
                            _events.send(
                                ProfileEditEvent.ShowSnackbar(
                                    message = "Это время сегодня уже прошло, напоминание сработает завтра в $timeFormatted",
                                    actionLabel = null,
                                    actionType = null
                                )
                            )
                        }
                        else -> {
                            _events.send(ProfileEditEvent.SaveSuccess)
                        }
                    }
                } else {
                    cancelReminderUseCase()
                    _events.send(ProfileEditEvent.SaveSuccess)
                }

                if (initialAvatarUri != null && initialAvatarUri != currentState.avatarUri) {
                    discardAvatarUseCase(initialAvatarUri!!)
                }

                _state.value = _state.value.copy(isSaving = false)
            } catch (e: InvalidReminderTimeException) {
                _state.value = _state.value.copy(
                    isSaving = false,
                    isReminderTimeTouched = true,
                    reminderTimeError = e.message ?: "Некорректный формат времени"
                )
            } catch (e: InvalidUrlException) {
                _state.value = _state.value.copy(
                    isSaving = false,
                    resumeUrlError = e.message ?: "Некорректная ссылка"
                )
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _state.value = _state.value.copy(
                    isSaving = false,
                    reminderTimeError = "Ошибка сохранения профиля"
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

    companion object {
        fun formatTimeInput(input: String, previousText: String): String {
            if (input.isBlank()) return ""

            val cleaned = input.filter { it.isDigit() || it == ':' }

            // If user pressed backspace on "HH:"
            if (previousText.endsWith(":") && cleaned == previousText.dropLast(1)) {
                if (previousText.length == 3 && previousText.startsWith("0") && previousText[1] in '3'..'9') {
                    return ""
                }
                return cleaned
            }

            val digitsOnly = cleaned.filter { it.isDigit() }
            if (digitsOnly.isEmpty()) return ""

            if (cleaned.contains(":")) {
                val parts = cleaned.split(":")
                val rawHours = parts[0].filter { it.isDigit() }
                val hoursPart = if (rawHours.length == 1 && rawHours[0] in '3'..'9') {
                    "0${rawHours[0]}"
                } else {
                    rawHours.take(2)
                }
                val minutesPart = parts.getOrNull(1)?.filter { it.isDigit() }?.take(2) ?: ""
                return "$hoursPart:$minutesPart"
            } else {
                if (digitsOnly.length == 1 && digitsOnly[0] in '3'..'9') {
                    return "0${digitsOnly[0]}:"
                }

                return if (digitsOnly.length >= 2) {
                    val hours = digitsOnly.take(2)
                    val mins = digitsOnly.drop(2).take(2)
                    if (mins.isNotEmpty()) "$hours:$mins" else "$hours:"
                } else {
                    digitsOnly
                }
            }
        }
    }
}
