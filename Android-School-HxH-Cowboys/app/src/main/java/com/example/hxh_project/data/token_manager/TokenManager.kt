package com.example.hxh_project.data.token_manager

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class TokenManager(
    private val sharedPreferences: SharedPreferences
) {
    fun saveToken(token: String?) {
        sharedPreferences.edit(commit = true) {
            putString(KEY_TOKEN, token)
        }
    }

    fun deleteToken() {
        sharedPreferences.edit(commit = true) {
            remove(KEY_TOKEN)
        }
    }

    fun getToken(): String? = sharedPreferences.getString(KEY_TOKEN, null)

    companion object {
        private const val KEY_TOKEN = "auth_token"
        private const val FILE_NAME_SESSION_PREF = "auth_shared_pref"

        fun tokenPreferences(context: Context): SharedPreferences {
            return EncryptedSharedPreferences.create(
                context,
                FILE_NAME_SESSION_PREF,
                MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
    }
}