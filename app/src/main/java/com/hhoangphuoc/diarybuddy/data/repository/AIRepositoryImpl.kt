package com.hhoangphuoc.diarybuddy.data.repository

import com.hhoangphuoc.diarybuddy.data.service.GenerativeAIService
import com.hhoangphuoc.diarybuddy.domain.repository.AIRepository

/**
 * Implementation of the AIRepository interface
 * Handles communication with the GenerativeAIService
 */
class AIRepositoryImpl(
    private val generativeAIService: GenerativeAIService
) : AIRepository {
    
    /**
     * Process a user query and generate a response
     * @param query The user's query
     * @param context Additional context information (e.g., screen content)
     * @return The AI-generated response
     */
    override suspend fun processQuery(query: String, context: String): String {
        return generativeAIService.generateResponse(query, context)
    }
    
    /**
     * Summarize screen content
     * @param content The screen content to summarize
     * @return A summary of the content
     */
    override suspend fun summarizeContent(content: String): String {
        return generativeAIService.summarizeContent(content)
    }
}