package com.zinc.datastore.common

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommonDataStoreModule @Inject constructor(@ApplicationContext context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "common")
    private val commonDataStore = context.dataStore

    private val appVersionKey = stringPreferencesKey("appVersion")

    val loadAppVersion = commonDataStore.data.map { preferences ->
        preferences[appVersionKey] ?: ""
    }

    suspend fun setAppVersion(appVersion: String) {
        commonDataStore.edit { preferences ->
            preferences[appVersionKey] = appVersion
        }
    }
}