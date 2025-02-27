package com.hhoangphuoc.diarybuddy.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.hhoangphuoc.diarybuddy.DiaryBuddy
import com.hhoangphuoc.diarybuddy.data.service.ApiKeyManager
import com.hhoangphuoc.diarybuddy.ui.theme.DiaryBuddyTheme
import kotlinx.coroutines.launch

/**
 * Test activity to verify that the Gemini API integration is working correctly
 */
class GeminiTestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Get the application instance
        val application = application as DiaryBuddy
        
        // Get the ProcessQueryUseCase
        val processQueryUseCase = application.processQueryUseCase
        
        // Check if API key is set
        val apiKeyManager = ApiKeyManager(applicationContext)
        val isApiKeySet = apiKeyManager.hasApiKey()
        
        setContent {
            DiaryBuddyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (!isApiKeySet) {
                        // Show message and button to go to settings if API key is not set
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Gemini API Key Not Set",
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            
                            Text(
                                text = "Please set your Gemini API key in Settings before using this feature.",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(bottom = 32.dp)
                            )
                            
                            Button(
                                onClick = {
                                    val intent = Intent(this@GeminiTestActivity, SettingsActivity::class.java)
                                    startActivity(intent)
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Go to Settings")
                            }
                        }
                    } else {
                        // Show test UI if API key is set
                        var query by remember { mutableStateOf("") }
                        var response by remember { mutableStateOf("") }
                        var isLoading by remember { mutableStateOf(false) }
                        
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Gemini API Test",
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            
                            OutlinedTextField(
                                value = query,
                                onValueChange = { query = it },
                                label = { Text("Enter your query") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                            )
                            
                            Button(
                                onClick = {
                                    if (query.isNotEmpty()) {
                                        isLoading = true
                                        lifecycleScope.launch {
                                            try {
                                                val result = processQueryUseCase.execute(query, "")
                                                response = result
                                            } catch (e: Exception) {
                                                response = "Error: ${e.message}"
                                                Toast.makeText(
                                                    this@GeminiTestActivity,
                                                    "Error: ${e.message}",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            } finally {
                                                isLoading = false
                                            }
                                        }
                                    }
                                },
                                enabled = query.isNotEmpty() && !isLoading,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                            ) {
                                Text("Submit Query")
                            }
                            
                            Button(
                                onClick = {
                                    if (query.isNotEmpty()) {
                                        isLoading = true
                                        lifecycleScope.launch {
                                            try {
                                                val result = processQueryUseCase.summarizeContent(query)
                                                response = result
                                            } catch (e: Exception) {
                                                response = "Error: ${e.message}"
                                                Toast.makeText(
                                                    this@GeminiTestActivity,
                                                    "Error: ${e.message}",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            } finally {
                                                isLoading = false
                                            }
                                        }
                                    }
                                },
                                enabled = query.isNotEmpty() && !isLoading,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                            ) {
                                Text("Summarize Content")
                            }
                            
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                            
                            if (response.isNotEmpty()) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp)
                                ) {
                                    Text(
                                        text = response,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
} 