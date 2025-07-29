package com.example.messagingapp.presentation.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Markunread
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.messagingapp.presentation.screens.favorites.FavoritesScreen
import com.example.messagingapp.presentation.screens.favorites.FavoritesViewModel
import com.example.messagingapp.presentation.screens.main.MainScreen
import com.example.messagingapp.ui.theme.DarkBlue
import com.example.messagingapp.ui.theme.LightBlue
import com.example.messagingapp.presentation.screens.unread.UnreadScreen


@Composable
fun MainHostScreen(
    loggedInUsername: String,
    navControllerRoot: NavHostController
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "all_chats",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("all_chats") {
                MainScreen(navControllerRoot, loggedInUsername)
            }
            composable("unread") {
                UnreadScreen(
                    loggedInUsername = loggedInUsername,
                    navController = navControllerRoot
                )
            }
            composable("favorites") {
                val viewModel: FavoritesViewModel = androidx.hilt.navigation.compose.hiltViewModel()
                FavoritesScreen(
                    loggedInUsername = loggedInUsername,
                    navController = navControllerRoot,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = currentRoute == "all_chats",
            onClick = {
                navController.navigate("all_chats") {
                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "All Chats") },
            label = { Text("Chats") },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = LightBlue,
                selectedIconColor = DarkBlue,
                selectedTextColor = DarkBlue,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )

        NavigationBarItem(
            selected = currentRoute == "unread",
            onClick = {
                navController.navigate("unread") {
                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(Icons.Default.Markunread, contentDescription = "Unread") },
            label = { Text("Recents") },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = LightBlue,
                selectedIconColor = DarkBlue,
                selectedTextColor = DarkBlue,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
        )
        )

        NavigationBarItem(
            selected = currentRoute == "favorites",
            onClick = {
                navController.navigate("favorites") {
                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(Icons.Default.Star, contentDescription = "Favorites") },
            label = { Text("Favorites")},
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = LightBlue,
                selectedIconColor = DarkBlue,
                selectedTextColor = DarkBlue,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )
    }
}
