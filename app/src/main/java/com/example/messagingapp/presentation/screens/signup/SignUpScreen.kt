package com.example.messagingapp.presentation.screens.signup

import PasswordRequirementItem
import com.example.messagingapp.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.messagingapp.presentation.components.LoginButton
import com.example.messagingapp.presentation.components.TextFields
import com.example.messagingapp.presentation.screens.AllScreens
import com.example.messagingapp.ui.theme.DarkBlue
import com.example.messagingapp.ui.theme.LightBlue
import com.example.messagingapp.ui.theme.mLightPurple

@Composable
fun SignUpScreen(viewModel: SignUpViewModel = viewModel(),
                 navController: NavController){
    val context = LocalContext.current

    if (viewModel.isSignUpSuccessful) {
        navController.navigate(AllScreens.LoginScreen.name) {
            popUpTo(AllScreens.SignUpScreen.name) {
                inclusive = true
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = LightBlue,
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 32.dp, start = 16.dp, end = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.splash),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(100.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Create Your Account",
                    fontSize = 24.sp,
                    color = DarkBlue,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Join Our App and start chatting securely.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TextFields(
                        value = viewModel.username,
                        onValueChange = viewModel::onUsernameChange,
                        label = "Username:",
                        placeholder = "Choose a username",
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = "username icon")
                        }
                    )
                    TextFields(
                        value = viewModel.email,
                        onValueChange = viewModel::onEmailChanged,
                        label = "Email address:",
                        placeholder = "example@gmail.com",
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = "email icon")
                        }
                    )

                    TextFields(
                        value = viewModel.password,
                        onValueChange = viewModel::onPasswordChanged,
                        label = "Password:",
                        placeholder = "Create a strong password",
                        isPassword = true,
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = "password icon")
                        }
                    )

                    TextFields(
                        value = viewModel.confirmPassword,
                        onValueChange = viewModel::onConfirmPasswordChanged,
                        label = "Confirm Password:",
                        placeholder = "Repeat your password",
                        isPassword = true,
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = "confirm password icon")
                        }
                    )

                    if (viewModel.password.isNotEmpty()) {
                        Column(
                            modifier = Modifier.padding(top = 6.dp)
                        ) {
                            PasswordRequirementItem(
                                label = "Minimum 8 characters",
                                fulfilled = viewModel.passwordRequirementsState.lengthOk
                            )
                            PasswordRequirementItem(
                                label = "At least one uppercase letter",
                                fulfilled = viewModel.passwordRequirementsState.hasUppercase
                            )
                            PasswordRequirementItem(
                                label = "At least one lowercase letter",
                                fulfilled = viewModel.passwordRequirementsState.hasLowercase
                            )
                            PasswordRequirementItem(
                                label = "At least one digit",
                                fulfilled = viewModel.passwordRequirementsState.hasDigit
                            )
                            PasswordRequirementItem(
                                label = "At least one special character",
                                fulfilled = viewModel.passwordRequirementsState.hasSpecialChar
                            )
                        }
                    }

                    LoginButton(
                        onClick = {
                            viewModel.onSignUpClicked(context)
                        },
                        text = "Sign Up",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(top = 6.dp)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    HorizontalDivider(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp),
                        color = mLightPurple
                    )
                    Text(
                        text = "Already have an account?",
                        color = Color.DarkGray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    TextButton(
                        onClick = {
                            navController.navigate(AllScreens.LoginScreen.name)
                        }
                    ) {
                        Text(
                            text = "Log in",
                            color = DarkBlue,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp),
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