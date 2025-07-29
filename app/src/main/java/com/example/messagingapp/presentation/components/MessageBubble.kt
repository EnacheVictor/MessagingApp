package com.example.messagingapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.messagingapp.ui.theme.DarkBlue
import com.example.messagingapp.ui.theme.mOffWhite
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MessageBubble(
    messageText: String,
    isSentByMe: Boolean,
    timestamp: Long,
    isReadStatus: Int
) {
    val backgroundColor = if (isSentByMe) DarkBlue.copy(alpha = 0.9f) else mOffWhite
    val textColor = if (isSentByMe) Color.White else Color.Black

    val bubbleShape = if (isSentByMe) {
        RoundedCornerShape(12.dp, 0.dp, 12.dp, 12.dp)
    } else {
        RoundedCornerShape(0.dp, 12.dp, 12.dp, 12.dp)
    }

    val timeString = remember(timestamp) {
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timestamp))
    }

    val readIndicator = when (isReadStatus) {
        0 -> "✓"
        1 -> "✓✓"
        2 -> "✓✓"
        else -> ""
    }

    val readColor = when (isReadStatus) {
        2 -> Color.White.copy(alpha = 0.8f)
        else -> Color.White.copy(alpha = 0.6f)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = if (isSentByMe) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(backgroundColor, shape = bubbleShape)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(text = messageText, color = textColor, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(1.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (isSentByMe) {
                    Text(
                        text = "$timeString  $readIndicator",
                        fontSize = 10.sp,
                        color = readColor
                    )
                } else {
                    Text(
                        text = timeString,
                        fontSize = 10.sp,
                        color = textColor.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}
