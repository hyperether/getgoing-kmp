package com.hyperether.getgoing_kmp.android.presentation.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hyperether.getgoing_kmp.android.presentation.navigation.Arguments.USER_ID
import com.hyperether.getgoing_kmp.android.presentation.scenes.details.DetailsScreen
import com.hyperether.getgoing_kmp.android.presentation.scenes.details.DetailsViewModel
import com.hyperether.getgoing_kmp.android.presentation.scenes.getgoing.GetGoingScreen
import com.hyperether.getgoing_kmp.android.presentation.scenes.getgoing.GetGoingViewModel
import com.hyperether.getgoing_kmp.android.presentation.scenes.profile.ProfileScreen
import com.hyperether.getgoing_kmp.android.presentation.scenes.profile.ProfileViewModel
import com.hyperether.getgoing_kmp.android.presentation.scenes.tracking.TrackingScreen
import com.hyperether.getgoing_kmp.android.presentation.scenes.tracking.TrackingViewModel
import com.hyperether.getgoing_kmp.android.util.ServiceUtil

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.GetGoingScreen.route) {
        lateinit var context: Context
        composable(route = Screen.GetGoingScreen.route) {
            context = LocalContext.current
            val viewModel: GetGoingViewModel = viewModel()
            GetGoingScreen(viewModel = viewModel,
                start = {
                    navController.navigate(
                        Screen.TrackingScreen.route.replace(
                            "{id}",
                            it.toString(),
                            true
                        )
                    )
                },
                navigateTo = { navController.navigate(it) }
            )
            if (ServiceUtil.isServiceActive(context)) {
                navController.navigate(Screen.TrackingScreen.route)
            }
        }

        composable(
            route = Screen.TrackingScreen.route,
            arguments = listOf(navArgument(name = "id") {
                nullable = true
                defaultValue = null
                type = NavType.StringType
            })
        ) {
            val viewModel: TrackingViewModel = viewModel()
            it.arguments?.getString("id")?.let {
                viewModel.setExercise(it.toInt())
            }

            TrackingScreen(viewModel = viewModel) {
                navController.navigateUp()
            }
        }

        composable(
            route = "${Screen.ProfileScreen.route}/{$USER_ID}",
            arguments = listOf(navArgument(USER_ID) { type = NavType.LongType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getLong(USER_ID)
            val viewModel: ProfileViewModel = viewModel()
            ProfileScreen(
                userId = userId!!,
                viewModel = viewModel,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            route = Screen.DetailsScreen.route,
            arguments = listOf(navArgument(name = "activityId") {
                nullable = true
                defaultValue = null
                type = NavType.StringType
            })
        ) {
            val viewModel: DetailsViewModel = viewModel()
            it.arguments?.getString("activityId")?.let {
                viewModel.setExercise(it.toInt())
            }

            DetailsScreen(viewModel = viewModel) {
                navController.navigateUp()
            }
        }
    }
}