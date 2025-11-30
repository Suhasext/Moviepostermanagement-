package com.example.moviepostermanagementapp

import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.moviepostermanagementapp.navigation.CineVaultNavigation
import com.example.moviepostermanagementapp.ui.theme.MoviePosterManagementAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        enableEdgeToEdge()

        setContent {
            MoviePosterManagementAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Transparent
                ) {
                    CineVaultApp()
                }
            }
        }

        splashScreen.setOnExitAnimationListener { splashScreenView ->
            val iconView = splashScreenView.iconView ?: return@setOnExitAnimationListener

            iconView.animate().apply {
                scaleX(0f)
                scaleY(0f)
                alpha(0f)
                duration = 800L
                interpolator = DecelerateInterpolator()
                withEndAction {
                    splashScreenView.remove()
                }
                start()
            }
        }
    }
}

@Composable
fun CineVaultApp() {
    val navController = rememberNavController()
    CineVaultNavigation(navController = navController)
}
