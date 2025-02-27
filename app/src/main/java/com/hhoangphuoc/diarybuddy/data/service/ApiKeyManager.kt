package com.hhoangphuoc.diarybuddy.data.service

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Manages the storage and retrieval of API keys
 * Uses EncryptedSharedPreferences for secure storage
 */
class ApiKeyManager(private val context: Context) {
    
    companion object {
        private const val PREFERENCES_FILE = "encrypted_api_keys"
        private const val GEMINI_API_KEY = "gemini_api_key"
    }
    
    // Create or get the master key for encryption
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    
    // Initialize encrypted shared preferences
    private val sharedPreferences: SharedPreferences by lazy {
        try {
            EncryptedSharedPreferences.create(
                context,
                PREFERENCES_FILE,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            // Fallback to regular SharedPreferences if encryption fails
            context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)
        }
    }
    
    /**
     * Save the Gemini API key to encrypted storage
     * @param apiKey The API key to save
     */
    fun saveApiKey(apiKey: String) {
        sharedPreferences.edit().putString(GEMINI_API_KEY, apiKey).apply()
    }
    
    /**
     * Retrieve the Gemini API key from encrypted storage
     * @return The API key, or null if not found
     */
    fun getApiKey(): String? {
        return sharedPreferences.getString(GEMINI_API_KEY, null)
    }
    
    /**
     * Check if an API key has been saved
     * @return True if an API key exists, false otherwise
     */
    fun hasApiKey(): Boolean {
        return !getApiKey().isNullOrBlank()
    }
    
    /**
     * Clear the saved API key
     */
    fun clearApiKey() {
        sharedPreferences.edit().remove(GEMINI_API_KEY).apply()
    }
} 