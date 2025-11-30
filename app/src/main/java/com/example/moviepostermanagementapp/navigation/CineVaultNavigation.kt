package com.example.moviepostermanagementapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.moviepostermanagementapp.ui.screens.AddContentScreen
import com.example.moviepostermanagementapp.ui.screens.ContentDetailScreen
import com.example.moviepostermanagementapp.ui.screens.MainScreen

@Composable
fun CineVaultNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(
                onNavigateToAddContent = {
                    navController.navigate("add_content")
                },
                onNavigateToContentDetail = { contentId ->
                    navController.navigate("content_detail/$contentId")
                }
            )
        }
        
        composable("add_content") {
            AddContentScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("content_detail/{contentId}") { backStackEntry ->
            val contentId = backStackEntry.arguments?.getString("contentId") ?: ""
            ContentDetailScreen(
                contentId = contentId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
