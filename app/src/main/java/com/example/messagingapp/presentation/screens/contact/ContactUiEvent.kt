package com.example.messagingapp.presentation.screens.contact

sealed class ContactUiEvent {
    data class SearchChanged(val newText: String) : ContactUiEvent()
    data class SendMessage(val sender: String, val receiver: String, val text: String) : ContactUiEvent()
    data class LoadConversation(val loggedInUser: String, val contactUser: String) : ContactUiEvent()
    data class DeleteConversation(val user1: String, val user2: String) : ContactUiEvent()
}