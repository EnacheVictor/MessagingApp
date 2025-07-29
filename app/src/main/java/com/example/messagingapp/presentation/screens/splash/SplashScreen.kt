package com.example.messagingapp.presentation.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.messagingapp.presentation.screens.AllScreens
import kotlinx.coroutines.delay
import com.example.messagingapp.R
import com.example.messagingapp.ui.theme.DarkBlue
import com.example.messagingapp.ui.theme.LightBlue
import com.example.messagingapp.ui.theme.mOffWhite

@Preview
@Composable
fun SplashScreen(navController: NavController = NavController(context = LocalContext.current)){
    LaunchedEffect(true) {
        delay(2500L)
        navController.navigate(AllScreens.LoginScreen.name){
            popUpTo(AllScreens.SplashScreen.name){
                inclusive = true
            }
        }
    }
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(LightBlue, DarkBlue))
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 180.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.splash),
                contentDescription = "Splash icon",
                modifier = Modifier.size(140.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Welcome back!",
                color = Color.White.copy(alpha = 0.7f),
                style = MaterialTheme.typography.headlineMedium,
                fontStyle = FontStyle.Normal
            )
        }
        Text(
            text = "Message app still in develop phase",
            color = mOffWhite,
            style = MaterialTheme.typography.bodySmall,
            fontStyle = FontStyle.Italic,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 36.dp)
        )
        Text(
            text = "Version: v1.0.0",
            color = mOffWhite,
            style = MaterialTheme.typography.bodySmall,
            fontStyle = FontStyle.Italic,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(top = 12.dp, bottom = 12.dp)
        )
    }
}