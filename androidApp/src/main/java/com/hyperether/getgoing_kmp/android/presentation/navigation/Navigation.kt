package com.hyperether.getgoing_kmp.android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
                navController.navigate(Screen.TrackingScreen.route)
            }
        }

        composable(route = Screen.TrackingScreen.route) {
            val viewModel: TrackingViewModel = viewModel()
            TrackingScreen(viewModel = viewModel) {
                navController.popBackStack()
            }
        }

    }
}