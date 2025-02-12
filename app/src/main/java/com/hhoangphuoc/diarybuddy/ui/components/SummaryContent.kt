package com.hhoangphuoc.diarybuddy.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SummaryContent(summary: String) {
    val scrollState = rememberScrollState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 500.dp)
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Text(
            text = summary,
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(24.dp),
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = 24.sp
        )
    }
}
