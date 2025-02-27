package com.hhoangphuoc.diarybuddy.domain.usecase

import com.hhoangphuoc.diarybuddy.domain.repository.AIRepository

/**
 * Use case for processing user queries
 * This is part of the domain layer in the MVI architecture
 */
class ProcessQueryUseCase(
    private val aiRepository: AIRepository
) {
    /**
     * Execute the use case to process a query
     * @param query The user's query
     * @param context Additional context information (e.g., screen content)
     * @return The AI-generated response
     */
    suspend fun execute(query: String, context: String): String {
        return aiRepository.processQuery(query, context)
    }
    
    /**
     * Execute the use case to summarize content
     * @param content The content to summarize
     * @return A summary of the content
     */
    suspend fun summarizeContent(content: String): String {
        return aiRepository.summarizeContent(content)
    }
}