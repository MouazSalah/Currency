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
class DataStoreManager @Inject constructor(val context: Context, val gson: Gson) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "CURRENCY_PREF")

    fun setLastFetchDate(
        date: Long?, coroutineScope: CoroutineScope,
        callBack: (Boolean) -> Unit = {}
    ) {
        writePrefLong(
            PreferenceKeys.LAST_FETCH_RATES_DATE,
            date,
            coroutineScope, callBack
        )
    }

    fun getLastFetchDate(): Long? = readPrefLongBlocking(PreferenceKeys.LAST_FETCH_RATES_DATE)


    private fun writePrefLong(
        key: Preferences.Key<Long>,
        value: Long?,
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

    private fun readPrefLongBlocking(
        key: Preferences.Key<Long>,
        defaultValue: Long = 0L
    ): Long {
        val storedValue = runBlocking { context.dataStore.data.first()[key] }
        return try {
            storedValue?.toString()?.toLong() ?: defaultValue
        } catch (e: NumberFormatException) {
            defaultValue
        }
    }


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

    fun saveCurrenciesList(
        list: List<String>,
        coroutineScope: CoroutineScope,
        callBack: (Boolean) -> Unit = {}
    ) {
        val jsonString = gson.toJson(list)
        writePrefString(PreferenceKeys.CURRENCIES, jsonString, coroutineScope, callBack)
    }

    fun getCurrenciesList(): List<String> {
        val jsonString = readPrefStringBlocking(PreferenceKeys.CURRENCIES)
        return if (jsonString != null) {
            try {
                gson.fromJson(jsonString, Array<String>::class.java).toList()
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }


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
    val LAST_FETCH_RATES_DATE = longPreferencesKey("last_fetch_rates_date")
    val CURRENCIES = stringPreferencesKey("CURRENCIES")

}