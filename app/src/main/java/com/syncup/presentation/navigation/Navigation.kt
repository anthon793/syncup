package com.syncup.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.syncup.presentation.screen.*

@Composable
fun SyncUpNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(onSplashComplete = {
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            })
        }

        composable("login") {
            LoginScreen(onLoginSuccess = {
                navController.navigate("main") {
                    popUpTo("login") { inclusive = true }
                }
            })
        }

        composable("main") {
            MainScreen(
                onNavigateToProject = { projectId ->
                    navController.navigate("project/$projectId")
                },
                onNavigateToInvite = {
                    navController.navigate("invite")
                },
                onNavigateToCreateMilestone = {
                    navController.navigate("create_milestone")
                },
                onNavigateToProfile = {
                    navController.navigate("profile")
                },
                onNavigateToProjectManagement = {
                    navController.navigate("project_management")
                },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }

        composable("invite") {
            InviteTeammateScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("create_milestone") {
            CreateMilestoneScreen(
                onBackClick = { navController.popBackStack() },
                onCreateMilestone = { }
            )
        }

        composable("profile") {
            EnhancedProfileScreen(
                onBackClick = { navController.popBackStack() },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "project/{projectId}",
            arguments = listOf(navArgument("projectId") { type = NavType.StringType })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId") ?: return@composable
            ProjectBoardScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable("project_management") {
            ProjectManagementScreen()
        }
    }
}
