package com.hyperether.getgoing_kmp.android.presentation.scenes.activities

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hyperether.getgoing_kmp.android.presentation.ui.components.BoldLargeText
import com.hyperether.getgoing_kmp.android.presentation.ui.components.PrimaryButton
import com.hyperether.getgoing_kmp.android.presentation.ui.components.back
import com.hyperether.getgoing_kmp.android.presentation.ui.theme.GetgoingkmpTheme



@Composable
fun ActivitiesScreen(
    viewModel: ActivitiesViewModel
) {
    val meters = viewModel.meters.value
    val calories = viewModel.calories.value
    val walkedValue=viewModel.walkedValue.value

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)) {
        Spacer(modifier = Modifier.height(16.dp))
        ActivitiesTopBar()

        Spacer(modifier = Modifier.height(16.dp))
        ActivityProgress("Walking")
        ActivityProgress(activity = "Running")
        ActivityProgress(activity = "Cycling")

        GoalSection(meters, calories, viewModel)
        ActivityTimeEstimations(meters, viewModel)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            PrimaryButton("Save changes") {
                viewModel.saveChanges()
            }
        }
    }
}

@Composable
fun ActivityProgress(activity: String) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(activity, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
        Text("0km", style = MaterialTheme.typography.bodyMedium)
    }
    Divider()
    LinearProgressIndicator(
        progress = { 0.1f },
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun ActivitiesTopBar() {
    Row(verticalAlignment = Alignment.CenterVertically ) {
        back()
        Text("My activities", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(start = 16.dp))
    }
}

@Composable
fun GoalSection(meters: Int, calories: Int, viewModel: ActivitiesViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {


        Row ( modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            Column {
                BoldLargeText("MY GOAL")
                Spacer(modifier = Modifier.height(8.dp))
            }
            Column {
                Text("$meters meters", style = MaterialTheme.typography.bodyLarge)
                Text("About $calories kcal", style = MaterialTheme.typography.bodyLarge)
            }

        }
        Slider(
            value = meters.toFloat(),
            onValueChange = { newValue -> viewModel.setMeters(newValue.toInt()) },
            valueRange = 0f..10000f,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun ActivityTimeEstimations(meters: Int, viewModel: ActivitiesViewModel) {
    Row(modifier = Modifier.fillMaxWidth().padding(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    )
    {

        Row{
            Icon(Icons.Default.Star, contentDescription = "Walking")
            Spacer(modifier = Modifier.width(8.dp))
            Text("${viewModel.walkingTime(meters)} min", style = MaterialTheme.typography.bodyMedium)
        }
        Text(text = "or")
        Row {
            Icon(Icons.Default.Star, contentDescription = "Running")
            Spacer(modifier = Modifier.width(8.dp))
            Text("${viewModel.runningTime(meters)} min", style = MaterialTheme.typography.bodyMedium)
        }
        Text(text = "or")
        Row{
            Icon(Icons.Default.Star, contentDescription = "Cycling")
            Spacer(modifier = Modifier.width(8.dp))
            Text("${viewModel.cyclingTime(meters)} min", style = MaterialTheme.typography.bodyMedium)
        }

    }





//    Column(modifier = Modifier.padding(16.dp)) {
//        Row(modifier = Modifier.fillMaxWidth()) {
//            Icon(Icons.Default.Star, contentDescription = "Walking")
//            Spacer(modifier = Modifier.width(8.dp))
//            Text("${viewModel.walkingTime(meters)} min", style = MaterialTheme.typography.bodyMedium)
//        }
//        Row(modifier = Modifier.fillMaxWidth()) {
//            Icon(Icons.Default.Star, contentDescription = "Running")
//            Spacer(modifier = Modifier.width(8.dp))
//            Text("${viewModel.runningTime(meters)} min", style = MaterialTheme.typography.bodyMedium)
//        }
//        Row(modifier = Modifier.fillMaxWidth()) {
//            Icon(Icons.Default.Star, contentDescription = "Cycling")
//            Spacer(modifier = Modifier.width(8.dp))
//            Text("${viewModel.cyclingTime(meters)} min", style = MaterialTheme.typography.bodyMedium)
//        }
//    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewActivityScreen() {
    GetgoingkmpTheme {
        ActivitiesScreen(viewModel = ActivitiesViewModel())
    }
}
