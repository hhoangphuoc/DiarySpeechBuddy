package com.hhoangphuoc.diarybuddy.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.hhoangphuoc.diarybuddy.domain.model.FeatureType
import com.hhoangphuoc.diarybuddy.ui.MainEvent
import com.hhoangphuoc.diarybuddy.ui.MainState
import com.hhoangphuoc.diarybuddy.ui.theme.*

@Composable
fun GlassmorphismScreen(
    state: MainState.Active,
    onEvent: (MainEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Glassmorphism Background
        GlassmorphismBackground()

        // Main Content
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Feature Content (takes most of the screen)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                when(state.activeFeature) {
                    FeatureType.NOTE_TAKING -> NoteContent(
                        noteText = state.noteText,
                        isLoading = state.isLoading,
                        onSaveNote = { /* Implement save note functionality */ }
                    )
                    FeatureType.VOICE_ASSISTANT -> ChatContent(
                        messages = state.chatMessages,
                        isLoading = state.isLoading
                    )
                    FeatureType.CONTENT_READER -> SummaryContent(
                        summary = state.contentSummary,
                        isLoading = state.isLoading
                    )
                }
            }
            
            // Bottom Navigation Bar
            GlassmorphismBottomBar(
                activeFeature = state.activeFeature,
                onFeatureSelected = { onEvent(MainEvent.FeatureSelected(it)) },
                onStartListening = { onEvent(MainEvent.StartListening) },
                onStopListening = { onEvent(MainEvent.StopListening) },
                isListening = state.isListening
            )
        }
    }
}

@Composable
private fun GlassmorphismBackground() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    GradientStart,  // Light cyan pastel
                    GradientEnd     // Light purple pastel
                )
            )
        )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        PastelCyan.copy(alpha = 0.4f), 
                        PastelPink.copy(alpha = 0.3f)
                    ),
                    center = center,
                    radius = size.minDimension
                )
            )
        }
    }
}

@Composable
fun GlassmorphismBottomBar(
    activeFeature: FeatureType,
    onFeatureSelected: (FeatureType) -> Unit,
    onStartListening: () -> Unit,
    onStopListening: () -> Unit,
    isListening: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(28.dp),
        color = TransparentWhite,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = Icons.Default.Mic,
                label = "Assistant",
                isSelected = activeFeature == FeatureType.VOICE_ASSISTANT,
                onClick = { onFeatureSelected(FeatureType.VOICE_ASSISTANT) }
            )
            
            BottomNavItem(
                icon = Icons.Default.MenuBook,
                label = "Reader",
                isSelected = activeFeature == FeatureType.CONTENT_READER,
                onClick = { onFeatureSelected(FeatureType.CONTENT_READER) }
            )
            
            BottomNavItem(
                icon = Icons.Default.Note,
                label = "Notes",
                isSelected = activeFeature == FeatureType.NOTE_TAKING,
                onClick = { onFeatureSelected(FeatureType.NOTE_TAKING) }
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) IconActive else IconInactive,
            modifier = Modifier.size(24.dp)
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) IconActive else IconInactive
        )
    }
}