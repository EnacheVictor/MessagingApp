package com.example.messagingapp.presentation.screens.main

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.messagingapp.presentation.components.ContactItem
import com.example.messagingapp.presentation.components.MainTopBar
import com.example.messagingapp.presentation.screens.AllScreens



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    navController: NavController,
    loggedInUsername: String,
    viewModel: MainViewModel = hiltViewModel()
) {
    LaunchedEffect(loggedInUsername) {
        viewModel.setLoggedInUser(loggedInUsername)
        viewModel.loadOwnStatus(loggedInUsername)
    }

    val context = LocalContext.current

    val search by viewModel.search.collectAsState()
    val contacts by viewModel.filteredContacts.collectAsState()

    var showStatusDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            MainTopBar(
                title = "Chats",
                searchValue = search,
                onSearchChange = viewModel::onSearchChanged,
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
            modifier = Modifier.fillMaxSize().background(Color.White)
        ) {
            items(contacts, key = { it.username }) { user ->
                val dismissState = rememberDismissState(
                    confirmStateChange = {
                        if (it == DismissValue.DismissedToStart) {
                            viewModel.promptDeleteContact(user.username)
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
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .fillMaxSize()
                                .clip(MaterialTheme.shapes.medium)
                                .background(Color.Red),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Text("Delete", color = Color.White, modifier = Modifier.padding(end = 24.dp))
                        }
                    },
                    dismissContent = {
                        ContactItem(
                            username = user.username,
                            status = user.status,
                            isFavorite = user.isFavorite,
                            onClick = {
                                navController.navigate(
                                    "${AllScreens.ContactScreen.name}/${Uri.encode(loggedInUsername)}/${Uri.encode(user.username)}"
                                )
                            },
                            onToggleFavorite = {
                                viewModel.toggleFavorite(user.username)
                            }
                        )
                    }
                )
            }
        }

        if (viewModel.showDeleteDialog && viewModel.contactToDelete != null) {
            AlertDialog(
                onDismissRequest = { viewModel.cancelDelete() },
                confirmButton = {
                    TextButton(onClick = { viewModel.confirmDeleteContact() }) {
                        Text("Delete", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.cancelDelete() }) {
                        Text("Cancel")
                    }
                },
                title = { Text("Delete Contact") },
                text = { Text("Are you sure you want to delete ${viewModel.contactToDelete}?") }
            )
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
                                    viewModel.onStatusSelected(status, loggedInUsername, context)
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