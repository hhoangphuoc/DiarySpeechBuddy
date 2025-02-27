package com.hhoangphuoc.diarybuddy.data.service

import android.content.Context
import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerationConfig
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Service that handles interactions with Google's Gemini API
 * Uses the Google AI SDK to communicate with the Gemini model
 */
class GenerativeAIService(private val context: Context) {
    private val TAG = "GenerativeAIService"
    
    // Get the API key from the ApiKeyManager
    private val apiKeyManager = ApiKeyManager(context)
    
    // Initialize the Gemini model lazily to ensure we have the latest API key
    private val generativeModel by lazy {
        val apiKey = apiKeyManager.getApiKey() ?: ""
        if (apiKey.isBlank()) {
            Log.e(TAG, "No API key found. Please set your Gemini API key in Settings.")
        }
        
        GenerativeModel(
            modelName = "gemini-pro",
            apiKey = apiKey,
            generationConfig = GenerationConfig(
                temperature = 0.7f,
                topK = 40,
                topP = 0.95f,
                maxOutputTokens = 1024
            )
        )
    }
    
    /**
     * Check if the API key is set
     * @return True if the API key is set, false otherwise
     */
    fun isApiKeySet(): Boolean {
        return apiKeyManager.hasApiKey()
    }
    
    /**
     * Generate a response to a user query using Gemini API
     * @param query The user's query
     * @param context Additional context information (e.g., screen content)
     * @return The AI-generated response
     */
    suspend fun generateResponse(query: String, context: String = ""): String {
        return withContext(Dispatchers.IO) {
            try {
                if (!isApiKeySet()) {
                    return@withContext "Please set your Gemini API key in Settings before using this feature."
                }
                
                // Create the prompt with context if available
                val prompt = if (context.isNotEmpty()) {
                    "Context: $context\nUser query: $query\nResponse:"
                } else {
                    "User query: $query\nResponse:"
                }
                
                // Create content for the model
                val inputContent = content {
                    text(prompt)
                }
                
                // Generate content using the Gemini model
                val response = generativeModel.generateContent(inputContent)
                
                // Extract and return the text from the response
                response.text ?: "Sorry, I couldn't generate a response."
            } catch (e: Exception) {
                Log.e(TAG, "Error generating response: ${e.message}", e)
                "Sorry, I couldn't process your request. Please try again. Error: ${e.message}"
            }
        }
    }
    
    /**
     * Summarize content using Gemini API
     * @param content The content to summarize
     * @return A summary of the content
     */
    suspend fun summarizeContent(content: String): String {
        return generateResponse(
            query = "Summarize the following content concisely and highlight the key points:",
            context = content
        )
    }
}