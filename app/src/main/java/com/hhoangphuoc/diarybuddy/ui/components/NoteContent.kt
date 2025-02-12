package com.hhoangphuoc.diarybuddy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun NoteTakingContent(noteText: String) {
    var animatedText by remember { mutableStateOf("") }

    LaunchedEffect(noteText) {
        animatedText = ""
        noteText.forEachIndexed { index, _ ->
            animatedText = noteText.take(index + 1)
            delay(30) // Typing animation speed
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(8.dp))
    ) {
        Text(
            text = animatedText,
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )
    }
}