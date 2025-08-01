package com.example.messagingapp.presentation.screens.main

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.foundation.lazy.items
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
    val state = viewModel.uiState
    val context = LocalContext.current
    var showStatusDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.onEvent(MainUiEvent.Init(loggedInUsername))
        viewModel.uiEvent.collect { event ->
            when (event) {
                is MainViewModel.UiEvent.ShowToast ->
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        topBar = {
            MainTopBar(
                title = "Chats",
                searchValue = state.searchText,
                onSearchChange = { viewModel.onEvent(MainUiEvent.SearchChanged(it)) },
                onSignOutClick = {
                    navController.navigate(AllScreens.LoginScreen.name) {
                        popUpTo(0)
                    }
                },
                onChooseStatusClick = { showStatusDialog = true }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier.fillMaxSize().background(Color.White)
        ) {
            items(state.filteredContacts, key = { it.username }) { user ->
                val dismissState = rememberDismissState(
                    confirmStateChange = {
                        if (it == DismissValue.DismissedToStart) {
                            viewModel.onEvent(MainUiEvent.PromptDelete(user.username))
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
                                    "${AllScreens.ContactScreen.name}/${Uri.encode(state.currentUsername)}/${Uri.encode(user.username)}"
                                )
                            },
                            onToggleFavorite = {
                                viewModel.onEvent(MainUiEvent.ToggleFavorite(user.username))
                            }
                        )
                    }
                )
            }
        }

        if (state.showDeleteDialog && state.contactToDelete != null) {
            AlertDialog(
                onDismissRequest = { viewModel.onEvent(MainUiEvent.CancelDelete) },
                confirmButton = {
                    TextButton(onClick = { viewModel.onEvent(MainUiEvent.ConfirmDelete) }) {
                        Text("Delete", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.onEvent(MainUiEvent.CancelDelete) }) {
                        Text("Cancel")
                    }
                },
                title = { Text("Delete Contact") },
                text = { Text("Are you sure you want to delete ${state.contactToDelete}?") }
            )
        }

        if (showStatusDialog) {
            val statusOptions = listOf("Available", "Busy", "At work", "DND", "Chill mood")
            AlertDialog(
                onDismissRequest = { showStatusDialog = false },
                title = { Text("Choose your status") },
                text = {
                    Column {
                        statusOptions.forEach { status ->
                            TextButton(
                                onClick = {
                                    viewModel.onEvent(MainUiEvent.StatusSelected(status))
                                    showStatusDialog = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) { Text(status) }
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
