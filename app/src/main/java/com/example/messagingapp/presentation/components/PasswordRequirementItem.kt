import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PasswordRequirementItem(
    label: String,
    fulfilled: Boolean
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val color = if (fulfilled) Color.Green else Color.Red
        val icon = if (fulfilled) Icons.Filled.Check else Icons.Filled.Close

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.padding(end = 8.dp)
        )

        Text(
            text = label,
            color = color,
            fontSize = 14.sp
        )
    }
}
