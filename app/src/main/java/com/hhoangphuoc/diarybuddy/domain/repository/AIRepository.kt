package com.hhoangphuoc.diarybuddy.domain.repository

/**
 * Repository interface for AI-related operations
 */
interface AIRepository {
    /**
     * Process a user query and generate a response
     * @param query The user's query
     * @param context Additional context information (e.g., screen content)
     * @return The AI-generated response
     */
    suspend fun processQuery(query: String, context: String): String
    
    /**
     * Summarize screen content
     * @param content The screen content to summarize
     * @return A summary of the content
     */
    suspend fun summarizeContent(content: String): String
}