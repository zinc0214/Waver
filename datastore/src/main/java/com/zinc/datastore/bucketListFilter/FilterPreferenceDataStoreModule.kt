package com.zinc.datastore.bucketListFilter

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilterPreferenceDataStoreModule @Inject constructor(@ApplicationContext context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "filter")
    private val filterDataStore = context.dataStore

    private val isProgressPref = booleanPreferencesKey("isProgress")
    private val isSucceedPref = booleanPreferencesKey("isSucceed")
    private val orderUpdate = booleanPreferencesKey("orderUpdate")
    private val orderCreate = booleanPreferencesKey("orderCreate")
    private val showDday = booleanPreferencesKey("showDday")
    private val isDdayMinusPref = booleanPreferencesKey("isDdayMinus")
    private val isDdayPlusPref = booleanPreferencesKey("isDdayPlus")

    val loadIsProgress: Flow<Boolean> = filterDataStore.data.map { preferences ->
        preferences[isProgressPref] ?: true
    }

    suspend fun setProgress(isProgress: Boolean) {
        filterDataStore.edit { preferences ->
            preferences[isProgressPref] = isProgress
        }
    }

    val loadIsSucceed: Flow<Boolean> = filterDataStore.data.map { preferences ->
        preferences[isSucceedPref] ?: true
    }

    suspend fun setSucceed(isSucceed: Boolean) {
        filterDataStore.edit { preferences ->
            preferences[isSucceedPref] = isSucceed
        }
    }

    val loadOrderUpdate: Flow<Boolean> = filterDataStore.data.map { preferences ->
        preferences[orderUpdate] ?: true
    }

    suspend fun setOrderUpdate(isUpdateUse: Boolean) {
        filterDataStore.edit { preferences ->
            preferences[orderUpdate] = isUpdateUse
        }
    }

    val loadOrderCreate: Flow<Boolean> = filterDataStore.data.map { preferences ->
        preferences[orderCreate] ?: true
    }

    suspend fun setOrderCreate(isCreateUse: Boolean) {
        filterDataStore.edit { preferences ->
            preferences[orderCreate] = isCreateUse
        }
    }

    val loadShowDday: Flow<Boolean> = filterDataStore.data.map { preferences ->
        preferences[showDday] ?: true
    }

    suspend fun setShowDday(isDdayUse: Boolean) {
        filterDataStore.edit { preferences ->
            preferences[showDday] = isDdayUse
        }
    }

    val loadIsDdayMinus: Flow<Boolean> = filterDataStore.data.map { preferences ->
        preferences[isDdayMinusPref] ?: true
    }

    suspend fun setShowDdayMinus(isShowDdayMinus: Boolean) {
        filterDataStore.edit { preferences ->
            preferences[isDdayMinusPref] = isShowDdayMinus
        }
    }

    val loadIsPlusMinus: Flow<Boolean> = filterDataStore.data.map { preferences ->
        preferences[isDdayPlusPref] ?: true
    }

    suspend fun setShowDdayPlus(isShowDdayPlus: Boolean) {
        filterDataStore.edit { preferences ->
            preferences[isDdayPlusPref] = isShowDdayPlus
        }
    }
}