package com.hyperether.getgoing_kmp.android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hyperether.getgoing_kmp.android.presentation.scenes.getgoing.GetGoingScreen
import com.hyperether.getgoing_kmp.android.presentation.scenes.getgoing.GetGoingViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.GetGoingScreen.route) {

        composable(route = Screen.GetGoingScreen.route) {
            val viewModel: GetGoingViewModel = viewModel()
            GetGoingScreen(viewModel = viewModel)
        }

    }
}