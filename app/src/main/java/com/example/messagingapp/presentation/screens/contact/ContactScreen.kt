package com.example.messagingapp.presentation.screens.contact

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.messagingapp.presentation.components.ContactInputField
import com.example.messagingapp.presentation.components.ContactTopBar
import com.example.messagingapp.presentation.components.MessageBubble
import com.example.messagingapp.ui.theme.WhiteDirty
import kotlinx.coroutines.launch

@Composable
fun ContactScreen(
    navController: NavController,
    loggedInUsername: String,
    contactUsername: String,
    viewModel: ContactViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var messageText by remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.onEvent(ContactUiEvent.LoadConversation(loggedInUsername, contactUsername))
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to clear the message history?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onEvent(ContactUiEvent.DeleteConversation(loggedInUsername, contactUsername))
                    showDialog.value = false
                }) { Text("Yes") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) { Text("No") }
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().imePadding(),
        topBar = {
            ContactTopBar(
                contactUsername = contactUsername,
                onBackClick = { navController.popBackStack() },
                searchText = state.searchText,
                onSearchTextChange = {
                    viewModel.onEvent(ContactUiEvent.SearchChanged(it))
                },
                onClearConvClick = {
                    showDialog.value = true
                }
            )
        },
        containerColor = WhiteDirty
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                reverseLayout = true,
                state = listState,
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(state.filteredMessages) { message ->
                    MessageBubble(
                        messageText = message.messageText,
                        isSentByMe = message.senderUsername == loggedInUsername,
                        timestamp = message.timestamp,
                        isReadStatus = message.isRead
                    )
                }
            }

            ContactInputField(
                messageText = messageText,
                onMessageChange = { messageText = it },
                onSend = {
                    if (messageText.isNotBlank()) {
                        viewModel.onEvent(
                            ContactUiEvent.SendMessage(
                                sender = loggedInUsername,
                                receiver = contactUsername,
                                text = messageText
                            )
                        )
                        messageText = ""
                        scope.launch {
                            listState.scrollToItem(0)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
