package com.hyperether.getgoing_kmp.android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hyperether.getgoing_kmp.android.presentation.scenes.getgoing.GetGoingScreen
import com.hyperether.getgoing_kmp.android.presentation.scenes.getgoing.GetGoingViewModel
import com.hyperether.getgoing_kmp.android.presentation.scenes.tracking.TrackingScreen
import com.hyperether.getgoing_kmp.android.presentation.scenes.tracking.TrackingViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.GetGoingScreen.route) {

        composable(route = Screen.GetGoingScreen.route) {
            val viewModel: GetGoingViewModel = viewModel()
            GetGoingScreen(viewModel = viewModel) {
                navController.navigate(Screen.TrackingScreen.route.split("/")[0] + "/$it")
            }
        }

        composable(
            route = Screen.TrackingScreen.route,
            arguments = listOf(navArgument(name = "id") { type = NavType.IntType })
        ) {
            val viewModel: TrackingViewModel = viewModel()
            viewModel.setExercise(it.arguments?.getInt("id") ?: 0)
            TrackingScreen(viewModel = viewModel) {
                navController.popBackStack()
            }
        }

    }
}