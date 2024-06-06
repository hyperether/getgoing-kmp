package com.hyperether.getgoing_kmp.android.presentation.navigation

sealed class Screen(val route: String) {
    object ActivitiesScreen : Screen("activities_route")
    object GetGoingScreen : Screen("getgoing_route")
    object ProfileScreen : Screen("profile_route")
    object DetailsScreen : Screen("details_route?id={id}")
    object TrackingScreen : Screen("tracking_route?activityId={activityId}")
}
