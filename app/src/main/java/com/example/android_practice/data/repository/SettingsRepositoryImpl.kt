package com.example.android_practice.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.android_practice.domain.model.FilterSettings
import com.example.android_practice.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private object PreferencesKeys {
        val KEY_STATE = stringPreferencesKey("by_state")
        val KEY_TYPE = stringPreferencesKey("by_type")
        val KEY_NAME = stringPreferencesKey("by_name")
    }

    override val settings: Flow<FilterSettings> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            FilterSettings(
                state = preferences[PreferencesKeys.KEY_STATE] ?: "",
                type = preferences[PreferencesKeys.KEY_TYPE] ?: "",
                name = preferences[PreferencesKeys.KEY_NAME] ?: ""
            )
        }

    override suspend fun save(settings: FilterSettings) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_STATE] = settings.state
            preferences[PreferencesKeys.KEY_TYPE] = settings.type
            preferences[PreferencesKeys.KEY_NAME] = settings.name
        }
    }

    override suspend fun reset() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.KEY_STATE)
            preferences.remove(PreferencesKeys.KEY_TYPE)
            preferences.remove(PreferencesKeys.KEY_NAME)
        }
    }
}
