package com.example.messagingapp.presentation.screens.login

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.messagingapp.R
import com.example.messagingapp.presentation.components.LoginButton
import com.example.messagingapp.presentation.components.TextFields
import com.example.messagingapp.presentation.screens.AllScreens
import com.example.messagingapp.ui.theme.DarkBlue
import com.example.messagingapp.ui.theme.LightBlue
import com.example.messagingapp.ui.theme.mLightPurple

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navController: NavController
) {
    val state = viewModel.uiState
    val context = LocalContext.current

    LaunchedEffect(true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is LoginViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    if (state.isLoginSuccessful) {
        navController.navigate("${AllScreens.MainHostScreen.name}/${Uri.encode(state.username)}") {
            popUpTo(AllScreens.LoginScreen.name) { inclusive = true }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = LightBlue
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.padding(top = 72.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.splash),
                        contentDescription = "Main Icon",
                        modifier = Modifier.size(120.dp)
                    )
                    Column {
                        Text(
                            text = "MessageApp",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkBlue
                        )
                        Text(
                            text = "Secure. Private. Yours.",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                Text(
                    text = "Welcome! Please Sign in to continue",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 20.dp)
                )

                Spacer(modifier = Modifier.height(26.dp))

                TextFields(
                    value = state.username,
                    onValueChange = { viewModel.onEvent(LoginUiEvent.UsernameChanged(it)) },
                    label = "Username",
                    placeholder = "Type your name:",
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "username icon")
                    },
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                TextFields(
                    value = state.password,
                    onValueChange = { viewModel.onEvent(LoginUiEvent.PasswordChanged(it)) },
                    label = "Password:",
                    placeholder = "Type your password:",
                    isPassword = true,
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = "password icon")
                    },
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                LoginButton(
                    onClick = {
                        viewModel.onEvent(LoginUiEvent.LoginClicked)
                    },
                    text = "Login",
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                        .height(48.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f).height(1.dp),
                        color = mLightPurple
                    )
                    Text(
                        text = "New to the app?",
                        color = Color.DarkGray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    TextButton(onClick = {
                        navController.navigate(AllScreens.SignUpScreen.name)
                    }) {
                        Text(
                            text = "Sign Up",
                            color = DarkBlue,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier.weight(1f).height(1.dp),
                        color = mLightPurple,
                    )
                }
            }

            Text(
                text = "By proceeding, you confirm that you accept our Terms of Service and Privacy Policy.",
                color = mLightPurple,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp, start = 24.dp, end = 24.dp)
            )
        }
    }
}

