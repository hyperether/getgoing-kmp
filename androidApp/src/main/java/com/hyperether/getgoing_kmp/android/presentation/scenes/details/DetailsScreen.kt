package com.hyperether.getgoing_kmp.android.presentation.scenes.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hyperether.getgoing_kmp.android.presentation.mock.MockRepo
import com.hyperether.getgoing_kmp.android.presentation.ui.components.AppToolbarDynamic
import com.hyperether.getgoing_kmp.android.presentation.ui.components.BoldMediumText
import com.hyperether.getgoing_kmp.android.presentation.ui.components.GoalProgress
import com.hyperether.getgoing_kmp.android.presentation.ui.components.GraphView
import com.hyperether.getgoing_kmp.android.presentation.ui.components.RegularText
import com.hyperether.getgoing_kmp.android.presentation.ui.components.SmallText
import com.hyperether.getgoing_kmp.android.presentation.ui.theme.GetgoingkmpTheme

@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel,
    navigateBack: () -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppToolbarDynamic(title = viewModel.selectedExercise.value) {
            navigateBack()
        }

        GoalProgress(progress = viewModel.selectedRouteProgress.floatValue)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RegularText(text = "Calories")
                BoldMediumText(text = viewModel.selectedRouteCal.value)
                SmallText(text = "kcal")
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RegularText(text = "Distance")
                BoldMediumText(text = viewModel.selectedRouteDistance.value)
                SmallText(text = "km")
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RegularText(text = "Speed")
                BoldMediumText(text = viewModel.selectedRouteSpeed.value)
                SmallText(text = "m/s")
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 28.dp)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(MaterialTheme.colorScheme.secondary)
        ) {
            GraphView(
                viewModel.routes.value,
                60f
            ) {
                viewModel.selectRoute(it)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetailsScreenPreview() {
    GetgoingkmpTheme {
        val viewModel = DetailsViewModel(MockRepo())
        viewModel.setExercise(1)
        DetailsScreen(
            viewModel = viewModel
        )
    }
}