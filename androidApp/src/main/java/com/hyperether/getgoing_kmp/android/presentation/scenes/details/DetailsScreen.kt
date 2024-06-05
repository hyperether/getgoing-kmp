package com.hyperether.getgoing_kmp.android.presentation.scenes.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hyperether.getgoing_kmp.android.presentation.mock.MockRepo
import com.hyperether.getgoing_kmp.android.presentation.ui.components.AppToolbarDynamic
import com.hyperether.getgoing_kmp.android.presentation.ui.components.GoalProgress
import com.hyperether.getgoing_kmp.android.presentation.ui.theme.GetgoingkmpTheme

@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel,
    navigateBack: () -> Unit = {}
) {

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        AppToolbarDynamic(title = viewModel.selectedExercise.value) {
            navigateBack()
        }

        GoalProgress(progress = 0.3f)
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