package com.example.messagingapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.messagingapp.ui.theme.DarkBlue

@Composable
fun ContactInputField(
    messageText: String,
    onMessageChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .shadow(elevation = 2.dp, shape = CircleShape)
                .clip(CircleShape)
                .background(Color.White)
                .border(
                    width = 1.dp,
                    color = if (isFocused) DarkBlue else Color.LightGray,
                    shape = CircleShape
                )
                .padding(horizontal = 16.dp, vertical = 10.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (messageText.isEmpty()) {
                Text(
                    text = "Write a message...",
                    fontSize = 17.sp,
                    color = DarkBlue.copy(alpha = 0.5f)
                )
            }
            BasicTextField(
                value = messageText,
                onValueChange = onMessageChange,
                modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                maxLines = 3,
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 17.sp
                ),
                cursorBrush = SolidColor(Color.Black),
                interactionSource = interactionSource,
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = {
                if (messageText.isNotBlank()) {
                    onSend()
                }
            },
            modifier = Modifier
                .size(44.dp)
                .background(DarkBlue, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send",
                tint = Color.White,
                modifier = Modifier.size(22.dp).padding(bottom = 4.dp)
            )
        }
    }
}
