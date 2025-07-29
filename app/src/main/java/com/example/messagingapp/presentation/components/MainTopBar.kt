package com.example.messagingapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.messagingapp.ui.theme.DarkBlue
import com.example.messagingapp.ui.theme.LightBlue
import com.example.messagingapp.ui.theme.WhiteDirty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    title: String,
    searchValue: String,
    onSearchChange: (String) -> Unit,
    onSignOutClick: () -> Unit,
    onChooseStatusClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    fontSize = 24.sp,
                    color = DarkBlue,
                    fontWeight = FontWeight.Bold
                )
            },
            windowInsets = WindowInsets(0),
            actions = {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menu",
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(WhiteDirty)
                ) {
                    DropdownMenuItem(
                        text = { Text("Choose Status", color = DarkBlue, fontWeight = FontWeight.SemiBold) },
                        onClick = {
                            expanded = false
                            onChooseStatusClick()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Sign Out", color = DarkBlue, fontWeight = FontWeight.SemiBold) },
                        onClick = {
                            expanded = false
                            onSignOutClick()
                        }
                    )
                }
            },
            navigationIcon = {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier
                        .padding(start = 12.dp, end = 8.dp)
                        .size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Icon",
                        tint = DarkBlue,
                        modifier = Modifier.padding(6.dp).clip(CircleShape)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = LightBlue,
                navigationIconContentColor = DarkBlue,
                actionIconContentColor = DarkBlue
            ),
            modifier = Modifier.background(LightBlue)
        )

        Surface(color = LightBlue, modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(bottom = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = WhiteDirty,
                    tonalElevation = 3.dp,
                    shadowElevation = 3.dp,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray),
                    modifier = Modifier.fillMaxWidth(0.9f).height(40.dp)
                ) {
                    BasicTextField(
                        value = searchValue,
                        onValueChange = onSearchChange,
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(
                            color = Color.Black,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        decorationBox = { innerTextField ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search Icon",
                                    tint = DarkBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(10.dp))
                                if (searchValue.isEmpty()) {
                                    Text("Search", color = Color.Gray, fontSize = 15.sp)
                                }
                                innerTextField()
                            }
                        }
                    )
                }
            }
        }
    }
}



