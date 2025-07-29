package com.example.messagingapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.messagingapp.ui.theme.DarkBlue

@Composable
fun NewBadge() {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(DarkBlue)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = "new",
            fontSize = 11.sp,
            color = Color.White,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
            fontWeight = FontWeight.Normal
        )
    }
}