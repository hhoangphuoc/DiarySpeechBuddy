package com.hhoangphuoc.diarybuddy

import android.app.Application
import com.hhoangphuoc.diarybuddy.data.repository.AIRepositoryImpl
import com.hhoangphuoc.diarybuddy.data.service.ApiKeyManager
import com.hhoangphuoc.diarybuddy.data.service.GenerativeAIService
import com.hhoangphuoc.diarybuddy.data.service.ScreenContentService
import com.hhoangphuoc.diarybuddy.domain.repository.AIRepository
import com.hhoangphuoc.diarybuddy.domain.usecase.ProcessQueryUseCase

/**
 * Main application class for DiaryBuddy
 * Initializes all the components needed for the app
 */
class DiaryBuddy : Application() {
    // Initialize ApiKeyManager (singleton)
    val apiKeyManager: ApiKeyManager by lazy {
        ApiKeyManager(applicationContext)
    }
    
    // Initialize GenerativeAIService (singleton)
    val generativeAiService: GenerativeAIService by lazy {
        GenerativeAIService(applicationContext)
    }

    // Initialize ScreenContentService (singleton)
    val screenContentService: ScreenContentService by lazy {
        ScreenContentService(applicationContext)
    }

    // Initialize AIRepository (singleton)
    val aiRepository: AIRepository by lazy {
        AIRepositoryImpl(generativeAiService)
    }

    // Initialize ProcessQueryUseCase (singleton)
    val processQueryUseCase: ProcessQueryUseCase by lazy {
        ProcessQueryUseCase(aiRepository)
    }
}