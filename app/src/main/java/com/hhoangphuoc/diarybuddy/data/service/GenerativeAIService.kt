package com.hhoangphuoc.diarybuddy.data.service

import android.content.Context
import com.google.ai.client.generativeai.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.hhoangphuoc.diarybuddy.R

class GenerativeAIService (
    private val context: Context
){
    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    suspend fun generateResponse(query: String, context: String): Result<String> {
        return try {
            val prompt = "Context: $context\nQuery: $query\nResponse:"
            val inputContent = content { text(prompt) }
            val response = generativeModel.generateContent(inputContent)
            Result.success(response.text ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}