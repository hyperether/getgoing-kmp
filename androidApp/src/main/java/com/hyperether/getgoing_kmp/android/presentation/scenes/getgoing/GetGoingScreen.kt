package com.hyperether.getgoing_kmp.android.presentation.scenes.getgoing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hyperether.getgoing_kmp.android.R
import com.hyperether.getgoing_kmp.android.presentation.mock.MockRepo
import com.hyperether.getgoing_kmp.android.presentation.navigation.Screen
import com.hyperether.getgoing_kmp.android.presentation.ui.components.BoldLargeText
import com.hyperether.getgoing_kmp.android.presentation.ui.components.EndlessListExercise
import com.hyperether.getgoing_kmp.android.presentation.ui.components.LastExercise
import com.hyperether.getgoing_kmp.android.presentation.ui.components.LinkWithIcon
import com.hyperether.getgoing_kmp.android.presentation.ui.components.Logo
import com.hyperether.getgoing_kmp.android.presentation.ui.components.PrimaryButton
import com.hyperether.getgoing_kmp.android.presentation.ui.components.Profile
import com.hyperether.getgoing_kmp.android.presentation.ui.components.ShapedColumn
import com.hyperether.getgoing_kmp.android.presentation.ui.theme.GetgoingkmpTheme
import com.hyperether.getgoing_kmp.android.util.ExerciseType

@Composable
fun GetGoingScreen(
    viewModel: GetGoingViewModel,
    start: (Int) -> Unit = {},
    navigateTo: (String) -> Unit = {}
) {
    LaunchedEffect(true) {
        viewModel.getLastRoute()
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ShapedColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Logo()
                    Profile(click = {
                        navigateTo("${Screen.ProfileScreen.route}/${viewModel.userId}")
                    })
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BoldLargeText(text = stringResource(R.string.last_exercise))
                    LinkWithIcon("View all", Icons.Default.KeyboardArrowRight)
                }

                LastExercise(
                    viewModel.lastRouteSelectedExercise.value,
                    viewModel.lastRouteDistance.value,
                    viewModel.lastRouteProgress.floatValue,
                    viewModel.lastRouteKcal.value,
                    viewModel.lastRouteDuration.value,
                    viewModel.lastRouteTimeProgress.floatValue
                )


                Column {
                    BoldLargeText(text = "Choose your exercise")
                    LinkWithIcon(text = "Can we burn our legs?")
                }


                EndlessListExercise(
                    list = ExerciseType.entries
                ) {
                    viewModel.selectExercise(it)
                }
            }

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = viewModel.exerciseState.value)

            PrimaryButton("Get ready") {
                start(viewModel.getSelectedExerciseId())
            }
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun GGSPreview() {
    GetgoingkmpTheme {
        GetGoingScreen(viewModel = GetGoingViewModel(MockRepo()))
    }
}