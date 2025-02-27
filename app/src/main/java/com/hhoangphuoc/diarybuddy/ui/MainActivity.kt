package com.hhoangphuoc.diarybuddy.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.hhoangphuoc.diarybuddy.R
import com.hhoangphuoc.diarybuddy.data.service.ApiKeyManager
import com.hhoangphuoc.diarybuddy.ui.components.GlassmorphismScreen
import com.hhoangphuoc.diarybuddy.ui.theme.DiaryBuddyTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    
    // Permission launcher
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.entries.all { it.value }
        if (allGranted) {
            // All permissions granted, initialize the app
            viewModel.handleEvent(MainEvent.Activate)
        } else {
            // Handle permission denial
            // You might want to show a dialog explaining why permissions are needed
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Make the app edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        // Check if API key is set
        val apiKeyManager = ApiKeyManager(applicationContext)
        val isApiKeySet = apiKeyManager.hasApiKey()
        
        // If API key is not set, open settings activity
        if (!isApiKeySet) {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        
        // Set up the UI
        setContent {
            DiaryBuddyTheme {
                val state by viewModel.state.collectAsState()
                var showMenu by remember { mutableStateOf(false) }
                
                Box(modifier = Modifier.fillMaxSize()) {
                    when (state) {
                        is MainState.Active -> {
                            GlassmorphismScreen(
                                state = state as MainState.Active,
                                onEvent = viewModel::handleEvent
                            )
                        }
                        MainState.Hidden -> {
                            // Nothing to show when hidden
                        }
                    }
                    
                    // Menu button in the top-right corner
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                    ) {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Menu"
                            )
                        }
                        
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Settings") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = null
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                                    startActivity(intent)
                                }
                            )
                            
                            DropdownMenuItem(
                                text = { Text("Test Gemini API") },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                        contentDescription = null
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    val intent = Intent(this@MainActivity, GeminiTestActivity::class.java)
                                    startActivity(intent)
                                }
                            )
                        }
                    }
                    
                    // Add floating action buttons in a column
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                    ) {
                        // Settings button
                        FloatingActionButton(
                            onClick = {
                                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                                startActivity(intent)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings"
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Test Gemini API button
                        FloatingActionButton(
                            onClick = {
                                val intent = Intent(this@MainActivity, GeminiTestActivity::class.java)
                                startActivity(intent)
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                contentDescription = "Test Gemini API"
                            )
                        }
                    }
                }
            }
        }
        
        // Check and request permissions
        checkAndRequestPermissions()
    }
    
    private fun checkAndRequestPermissions() {
        val requiredPermissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        
        val permissionsToRequest = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()
        
        if (permissionsToRequest.isEmpty()) {
            // All permissions already granted
            viewModel.handleEvent(MainEvent.Activate)
        } else {
            // Request permissions
            requestPermissionLauncher.launch(permissionsToRequest)
        }
    }
    
    override fun onResume() {
        super.onResume()
        // Keep screen on while app is in foreground
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
    
    override fun onPause() {
        super.onPause()
        // Remove screen on flag when app is in background
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}



//UI RENDERED CONTENT


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    DiaryBuddyTheme {
////        Greeting("Android")
//        HomePage()
//    }
//}
