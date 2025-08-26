package com.example.messagingapp.presentation.screens.unread

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.messagingapp.model.network.SignalRClient
import com.example.messagingapp.presentation.components.MainTopBar
import com.example.messagingapp.presentation.components.UnreadContactItem
import com.example.messagingapp.presentation.screens.AllScreens
import com.example.messagingapp.ui.theme.DarkBlue
import com.example.messagingapp.ui.theme.WhiteDirty

@Composable
fun UnreadScreen(
    loggedInUsername: String,
    navController: NavController,
    viewModel: UnreadViewModel = hiltViewModel()
) {
    val state = viewModel.uiState
    val context = LocalContext.current
    var showStatusDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.onEvent(UnreadUiEvent.Init(loggedInUsername))
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UnreadViewModel.UiEvent.ShowToast ->
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        topBar = {
            MainTopBar(
                title = "Recents",
                searchValue = state.searchText,
                onSearchChange = { viewModel.onEvent(UnreadUiEvent.SearchChanged(it)) },
                onSignOutClick = {
                    SignalRClient.disconnect()
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
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            items(state.filteredUnreadItems) { item ->
                UnreadContactItem(
                    username = item.username,
                    lastMessage = item.lastMessage,
                    timestamp = item.timestamp,
                    onClick = {
                        navController.navigate("${AllScreens.ContactScreen.name}/${Uri.encode(loggedInUsername)}/${Uri.encode(item.username)}")
                    }
                )
            }
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
                                    viewModel.onEvent(UnreadUiEvent.StatusSelected(status))
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
                },
                containerColor = WhiteDirty,
                titleContentColor = DarkBlue
            )
        }
    }
}
