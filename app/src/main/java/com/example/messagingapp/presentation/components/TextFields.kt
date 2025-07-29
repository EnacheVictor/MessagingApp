package com.example.messagingapp.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Shape
import com.example.messagingapp.ui.theme.DarkBlue
import com.example.messagingapp.ui.theme.mLightGray


@Composable
fun TextFields(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    containerColor: Color = Color.White,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    isPassword: Boolean = false,
    leadingIcon: (@Composable (() -> Unit))? = null,
    maxLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = if (label.isNotBlank()) {
            { Text(label) }
        } else null,
        placeholder = { Text(placeholder, color = mLightGray) },
        singleLine = maxLines == 1,
        maxLines = maxLines,
        shape = shape,
        leadingIcon = leadingIcon,
        visualTransformation = if (isPassword) PasswordVisualTransformation()
        else VisualTransformation.None,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = DarkBlue,
            unfocusedIndicatorColor = Color.Gray,
            cursorColor = DarkBlue,
            focusedLabelColor = DarkBlue,
            unfocusedLabelColor = Color.Gray,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = containerColor,
            errorContainerColor = containerColor,
            focusedContainerColor = containerColor
        ),
        modifier = modifier.fillMaxWidth()
    )
}