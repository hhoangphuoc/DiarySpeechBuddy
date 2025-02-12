package com.hhoangphuoc.diarybuddy.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hhoangphuoc.diarybuddy.ui.AppState
import com.hhoangphuoc.diarybuddy.domain.model.FeatureType
import com.hhoangphuoc.diarybuddy.ui.MainEvent

@Composable
fun GlassmorphismScreen(
    state: AppState.Active,
    onEvent: (MainEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Glassmorphism Background
        GlassmorphismBackground()

        // Feature Content
        when(state.activeFeature) {
            FeatureType.NOTE_TAKING -> NoteTakingContent(state.noteText)
            FeatureType.VOICE_ASSISTANT -> ChatContent(state.chatMessages)
            FeatureType.CONTENT_READER -> SummaryContent(state.contentSummary)
        }

        // Control Panel
        ControlPanel(
            activeFeature = state.activeFeature,
            onFeatureSelected = { onEvent(MainEvent.FeatureSelected(it)) },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun GlassmorphismBackground() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
//                    Color(0xCCE3F2FD),  // 80% opacity
//                    Color(0xB3E8F5E9)    // 70% opacity
                    Color(0xFFEAFFFE),
                    Color(0xFFCDC9F1)
                )
            )
        )
        .blur(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x66B2EBF2), Color(0x4D4DD0E1)),
                    center = center,
                    radius = size.minDimension
                )
            )
        }
    }
}