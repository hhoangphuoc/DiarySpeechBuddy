package com.hhoangphuoc.diarybuddy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PhotoFilter
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.hhoangphuoc.diarybuddy.domain.model.FeatureType
import com.hhoangphuoc.diarybuddy.ui.MainEvent

@Composable
fun ControlPanel(
    activeFeature: FeatureType,
    onEvent: (MainEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.9f))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FeatureButton(
            icon = Icons.Default.Book,
            isActive = activeFeature == FeatureType.CONTENT_READER,
            onClick = { onEvent(MainEvent.FeatureSelected(FeatureType.CONTENT_READER)) }
        )

        FeatureButton(
            icon = Icons.Default.Mic,
            isActive = activeFeature == FeatureType.VOICE_ASSISTANT,
            onClick = { onEvent(MainEvent.FeatureSelected(FeatureType.VOICE_ASSISTANT)) }
        )

        FeatureButton(
            icon = Icons.Default.PhotoFilter, //NOTE ICON
            isActive = activeFeature == FeatureType.NOTE_TAKING,
            onClick = { onEvent(MainEvent.FeatureSelected(FeatureType.NOTE_TAKING)) }
        )
    }
}

@Composable
private fun FeatureButton(
    icon: ImageVector,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val tint = if (isActive) Color(0xFF4285F4) else Color.Gray

    IconButton(
        onClick = onClick,
        modifier = Modifier.size(48.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(32.dp)
        )
    }
}