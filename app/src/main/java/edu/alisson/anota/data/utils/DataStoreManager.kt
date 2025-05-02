package edu.alisson.anota.data.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext context: Context) {

    private val Context.dataStore by preferencesDataStore("user_prefs")
    private val dataStore = context.dataStore

    object Keys {
        val UID = stringPreferencesKey("user_uid")
    }

    suspend fun saveUid(uid: String) {
        dataStore.edit { prefs ->
            prefs[Keys.UID] = uid
        }
    }

    val uidFlow: Flow<String?> = dataStore.data.map { prefs ->
        prefs[Keys.UID]
    }

    suspend fun clearUid() {
        dataStore.edit { prefs ->
            prefs.remove(Keys.UID)
        }
    }
}
