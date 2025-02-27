package com.hhoangphuoc.diarybuddy.ui

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.hhoangphuoc.diarybuddy.R
import com.hhoangphuoc.diarybuddy.data.service.ApiKeyManager
import com.hhoangphuoc.diarybuddy.ui.theme.DiaryBuddyTheme

/**
 * Settings activity that allows users to configure app settings
 * Currently focused on managing the Gemini API key
 */
class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            DiaryBuddyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SettingsScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val apiKeyManager = remember { ApiKeyManager(context) }
    
    // Get the saved API key if it exists
    var apiKey by remember { mutableStateOf(apiKeyManager.getApiKey() ?: "") }
    var isApiKeyVisible by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.api_key_title),
                style = MaterialTheme.typography.titleLarge
            )
            
            OutlinedTextField(
                value = apiKey,
                onValueChange = { apiKey = it },
                label = { Text(stringResource(R.string.api_key_label)) },
                placeholder = { Text(stringResource(R.string.api_key_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (isApiKeyVisible) 
                    VisualTransformation.None 
                else 
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isApiKeyVisible = !isApiKeyVisible }) {
                        Icon(
                            imageVector = if (isApiKeyVisible) 
                                androidx.compose.material.icons.Icons.Default.Visibility 
                            else 
                                androidx.compose.material.icons.Icons.Default.VisibilityOff,
                            contentDescription = if (isApiKeyVisible) "Hide API key" else "Show API key"
                        )
                    }
                }
            )
            
            Text(
                text = stringResource(R.string.api_key_privacy_note),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Button(
                onClick = {
                    if (apiKey.isBlank()) {
                        Toast.makeText(context, "Please enter a valid API key", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    
                    isSaving = true
                    apiKeyManager.saveApiKey(apiKey)
                    Toast.makeText(context, "API key saved successfully", Toast.LENGTH_SHORT).show()
                    isSaving = false
                },
                modifier = Modifier.align(Alignment.End),
                enabled = !isSaving
            ) {
                Text(stringResource(R.string.save_button))
            }
            
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            
            Text(
                text = stringResource(R.string.api_key_instructions_title),
                style = MaterialTheme.typography.titleMedium
            )
            
            Text(
                text = stringResource(R.string.api_key_instructions),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
} 