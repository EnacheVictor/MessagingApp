package com.example.messagingapp.presentation.screens.favorites

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.messagingapp.presentation.components.FavoriteContactItem
import com.example.messagingapp.presentation.components.MainTopBar
import com.example.messagingapp.presentation.screens.AllScreens
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FavoritesScreen(
    loggedInUsername: String,
    navController: NavController,
    viewModel: FavoritesViewModel = hiltViewModel()
) {

    val search by viewModel.search.collectAsState()
    val favoriteItems by viewModel.filteredFavorites.collectAsState()
    var showStatusDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadFavoriteUsers(loggedInUsername)
    }

    Scaffold(
        topBar = {
            MainTopBar(
                title = "Favorites",
                searchValue = search,
                onSearchChange = {viewModel.onSearchChanged(it)},
                onSignOutClick = {
                    navController.navigate(AllScreens.LoginScreen.name) {
                        popUpTo(0)
                    }
                },
                onChooseStatusClick = {
                    showStatusDialog = true
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            items(favoriteItems, key = { it.username }) { item ->
                val dismissState = rememberDismissState(
                    confirmStateChange = {
                        if (it == DismissValue.DismissedToStart) {
                            viewModel.removeFavorite(item.username)
                            false
                        } else false
                    }
                )

                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.EndToStart),
                    background = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .clip(MaterialTheme.shapes.medium)
                                .background(Color.Red),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Text(
                                "Remove",
                                color = Color.White,
                                modifier = Modifier.padding(end = 24.dp)
                            )
                        }
                    },
                    dismissContent = {
                        FavoriteContactItem(
                            username = item.username,
                            lastMessageStatus = item.lastMessageStatus,
                            lastMessageText = item.lastMessageText,
                            onClick = {
                                navController.navigate(
                                    "${AllScreens.ContactScreen.name}/${Uri.encode(loggedInUsername)}/${Uri.encode(item.username)}"
                                )
                            }
                        )
                    }
                )
            }
        }

        if (showStatusDialog) {
            val statusOptions = listOf("😎 Available", "💬 Busy", "🚀 At work", "📵 DND", "😂 Chill mood")
            AlertDialog(
                onDismissRequest = { showStatusDialog = false },
                title = { Text("Choose your status") },
                text = {
                    Column {
                        statusOptions.forEach { status ->
                            TextButton(
                                onClick = {
                                    viewModel.onStatusSelected(status, loggedInUsername)
                                    showStatusDialog = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(status)
                            }
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(onClick = { showStatusDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}