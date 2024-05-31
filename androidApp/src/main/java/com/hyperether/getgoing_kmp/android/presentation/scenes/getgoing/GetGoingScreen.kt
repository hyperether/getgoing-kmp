package com.hyperether.getgoing_kmp.android.presentation.scenes.getgoing

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hyperether.getgoing_kmp.android.R
import com.hyperether.getgoing_kmp.android.presentation.mock.MockRepo
import com.hyperether.getgoing_kmp.android.presentation.navigation.Screen
import com.hyperether.getgoing_kmp.android.presentation.ui.components.BoldLargeText
import com.hyperether.getgoing_kmp.android.presentation.ui.components.ButtonTextIcon
import com.hyperether.getgoing_kmp.android.presentation.ui.components.EndlessListExercise
import com.hyperether.getgoing_kmp.android.presentation.ui.components.LastExercise
import com.hyperether.getgoing_kmp.android.presentation.ui.components.Logo
import com.hyperether.getgoing_kmp.android.presentation.ui.components.PrimaryButton
import com.hyperether.getgoing_kmp.android.presentation.ui.components.Profile
import com.hyperether.getgoing_kmp.android.presentation.ui.components.ShapedColumn
import com.hyperether.getgoing_kmp.android.presentation.ui.theme.GetgoingkmpTheme
import com.hyperether.getgoing_kmp.android.util.ExerciseType
import com.hyperether.getgoing_kmp.android.util.UserUtil.getUserId
import com.hyperether.getgoing_kmp.android.util.UserUtil.isUserCreated

@Composable
fun GetGoingScreen(
    viewModel: GetGoingViewModel,
    start: (Int) -> Unit = {},
    navigateTo: (String) -> Unit = {}
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ShapedColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Logo()
                Profile(click = {
                    val userId = getUserId(viewModel.user)
                    navigateTo("${Screen.ProfileScreen.route}/${userId}")
                })
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BoldLargeText(text = stringResource(R.string.last_exercise))
                ButtonTextIcon("View all", Icons.Default.KeyboardArrowRight)
            }

            LastExercise()


            Column {
                BoldLargeText(text = "Choose your exercise")
                ButtonTextIcon(text = "Can we burn our legs?")
            }


            EndlessListExercise(
                list = ExerciseType.entries
            ) {
                viewModel.selectExercise(it)
            }

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = viewModel.exerciseState.value)

            PrimaryButton("Get ready") {
                if (isUserCreated(viewModel.user)) {
                    start(viewModel.getSelectedExerciseId())
                } else {
                    Toast.makeText(
                        context,
                        "Please finish user creation.",
                        Toast.LENGTH_LONG
                    ).show()
                    val userId = getUserId(viewModel.user)
                    navigateTo("${Screen.ProfileScreen.route}/${userId}")
                }
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