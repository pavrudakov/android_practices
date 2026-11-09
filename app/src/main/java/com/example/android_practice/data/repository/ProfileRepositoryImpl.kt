package com.example.android_practice.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.android_practice.data.di.ProfileDataStore
import com.example.android_practice.domain.model.ReminderTime
import com.example.android_practice.domain.model.UserProfile
import com.example.android_practice.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    @ProfileDataStore private val dataStore: DataStore<Preferences>
) : ProfileRepository {

    private object PreferencesKeys {
        val KEY_FULL_NAME = stringPreferencesKey("full_name")
        val KEY_POSITION = stringPreferencesKey("position")
        val KEY_AVATAR_URI = stringPreferencesKey("avatar_uri")
        val KEY_RESUME_URL = stringPreferencesKey("resume_url")
        val KEY_REMINDER_TIME = stringPreferencesKey("reminder_time")
    }

    override val profile: Flow<UserProfile> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val rawTime = preferences[PreferencesKeys.KEY_REMINDER_TIME]
            val reminderTime = rawTime?.let { ReminderTime.parse(it) }

            UserProfile(
                fullName = preferences[PreferencesKeys.KEY_FULL_NAME] ?: "",
                position = preferences[PreferencesKeys.KEY_POSITION] ?: "",
                avatarUri = preferences[PreferencesKeys.KEY_AVATAR_URI]?.takeIf { it.isNotBlank() },
                resumeUrl = preferences[PreferencesKeys.KEY_RESUME_URL] ?: "",
                reminderTime = reminderTime
            )
        }

    override suspend fun save(profile: UserProfile) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_FULL_NAME] = profile.fullName
            preferences[PreferencesKeys.KEY_POSITION] = profile.position
            if (profile.avatarUri != null) {
                preferences[PreferencesKeys.KEY_AVATAR_URI] = profile.avatarUri
            } else {
                preferences.remove(PreferencesKeys.KEY_AVATAR_URI)
            }
            preferences[PreferencesKeys.KEY_RESUME_URL] = profile.resumeUrl
            if (profile.reminderTime != null) {
                preferences[PreferencesKeys.KEY_REMINDER_TIME] = profile.reminderTime.format()
            } else {
                preferences.remove(PreferencesKeys.KEY_REMINDER_TIME)
            }
        }
    }
}
