package com.banquemisr.currency.ui.db

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(val context: Context, val gson: Gson)
{
    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "CURRENCY_PREF")

    fun setLastFetchDate(
        date: String?, coroutineScope: CoroutineScope,
        callBack: (Boolean) -> Unit = {}
    ) {
        writePrefString(
            PreferenceKeys.LAST_FETCH_RATES_DATE,
            date,
            coroutineScope, callBack
        )
    }

    fun getLastFetchDate(): String? = readPrefStringBlocking(PreferenceKeys.LAST_FETCH_RATES_DATE)

    private fun readPrefStringBlocking(
        key: Preferences.Key<String>,
        defaultValue: String? = null
    ): String? = runBlocking { context.dataStore.data.first() }[key] ?: defaultValue


    private fun writePrefString(
        key: Preferences.Key<String>,
        value: String?,
        coroutineScope: CoroutineScope,
        callBack: (Boolean) -> Unit = {}
    ) {
        coroutineScope.launch {
            context.dataStore.edit { settings ->
                if (value != null) {
                    settings[key] = value
                    callBack.invoke(true)
                } else
                    callBack.invoke(false)
            }
        }
    }

    //region clear pref
    fun clearAllData(scope: CoroutineScope) {
        scope.launch {
            context.dataStore.edit {
                it.clear()
            }
        }
    }

    fun clear(scope: CoroutineScope, key: Preferences.Key<*>) {
        scope.launch {
            context.dataStore.edit {
                if (it.contains(key))
                    it.remove(key)
            }
        }
    }
}

object PreferenceKeys {
    val LAST_FETCH_RATES_DATE = stringPreferencesKey("last_fetch_rates_date")
}