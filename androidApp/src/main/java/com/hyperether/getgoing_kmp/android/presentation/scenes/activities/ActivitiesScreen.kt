package com.hyperether.getgoing_kmp.android.presentation.scenes.activities

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hyperether.getgoing_kmp.android.R
import com.hyperether.getgoing_kmp.android.presentation.mock.MockRepo
import com.hyperether.getgoing_kmp.android.presentation.ui.components.AppToolbar
import com.hyperether.getgoing_kmp.android.presentation.ui.components.BoldMediumText
import com.hyperether.getgoing_kmp.android.presentation.ui.components.PrimaryButton
import com.hyperether.getgoing_kmp.android.presentation.ui.components.RegularText
import com.hyperether.getgoing_kmp.android.presentation.ui.theme.GetgoingkmpTheme
import com.hyperether.getgoing_kmp.android.util.ExerciseType
import com.hyperether.getgoing_kmp.android.util.TimeUtils
import com.hyperether.getgoing_kmp.repository.room.Route


@Composable
fun ActivitiesScreen(userId:Long,
    viewModel: ActivitiesViewModel,
    onBack: () -> Unit = {}
) {
    LaunchedEffect(key1 = userId) {
        viewModel.fetchUserById(userId)
        viewModel.fetchRouteList()
    }
    val context =LocalContext.current
    val meters = viewModel.meters
    val calories = viewModel.calories.value
    val routes = viewModel.routeList.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 8.dp,
                end = 8.dp,
                bottom = 16.dp,
                top = 16.dp
            )
    ) {
        AppToolbar(
            titleId = R.string.my_activities,
            onNavigateBack = { onBack() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ActivityProgress(activityName = stringResource(R.string.walking_lbl),ExerciseType.WALKING.id,routes,viewModel.meters.value)
        ActivityProgress(activityName = stringResource(R.string.running_lbl),ExerciseType.RUNNING.id,routes,viewModel.meters.value)
        ActivityProgress(activityName = stringResource(R.string.cycling_lbl),ExerciseType.CYCLING.id,routes,viewModel.meters.value)

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            GoalSection(meters, calories, viewModel)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ActivityTimeEstimations(meters.value)
                PrimaryButton(stringResource(R.string.save_changes_btn)) {
                    viewModel.saveChanges()
                    Toast.makeText(context,
                        context.getString(R.string.changes_saved_toast),Toast.LENGTH_LONG).show()
                }
            }

        }
    }
}

@Composable
fun ActivityProgress(activityName: String, activityID:Int, routes: List<Route>,goalMeters:Int) {
    var activitySum=0.0f

    for ( route in routes){
        if(route.activity_id==activityID){
            activitySum+=route.length.toFloat()
        }
    }

    val activityPercentage= ((activitySum*1000)/goalMeters)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.Start){
            RegularText(text =activityName )
        }
        Column(
            horizontalAlignment = Alignment.End){
            RegularText(text =activitySum.toString()+" km" )

        }
    }
    Divider()
    LinearProgressIndicator(
        progress = {activityPercentage },
        modifier = Modifier.fillMaxWidth(),
    )
}


@Composable
fun GoalSection(meters: MutableState<Int>, calories: Int, viewModel: ActivitiesViewModel) {
    var goalValue by remember { mutableStateOf(GoalValues.LOW) }
    goalValue=determineGoalLabel(meters.value)

    Column(modifier = Modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                BoldMediumText(stringResource(R.string.my_goal_lbl))
                Spacer(modifier = Modifier.height(8.dp))
            }


        }
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
            RegularText("${meters.value} meters")
            RegularText("About $calories kcal")
        }
        Slider(
            value = meters.value.toFloat(),
            onValueChange = { newValue ->
                viewModel.setMeters(newValue.toInt())
                if (newValue < 3333) {
                    goalValue = GoalValues.LOW
                } else if (newValue < 6666) {
                    goalValue = GoalValues.MEDIUM
                } else {
                    goalValue = GoalValues.HIGH
                }
            },
            valueRange = 0f..10000f,
            modifier = Modifier.padding(16.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GoalLabel(GoalValues.LOW,goalValue)
            RegularText("|")
            GoalLabel(GoalValues.MEDIUM,goalValue)
            RegularText("|")
            GoalLabel(GoalValues.HIGH,goalValue)
        }
    }
}

private fun determineGoalLabel(value: Int): GoalValues {
    if (value < 3333) {
        return GoalValues.LOW
    } else if (value < 6666) {
        return GoalValues.MEDIUM
    } else {
        return GoalValues.HIGH
    }
}

@Composable
fun GoalLabel(goalValue: GoalValues,currentGoal: GoalValues) {
    RegularText(
        text = goalValue.name,
        color =if (currentGoal==goalValue) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
    )
}
@Composable
fun ActivityTimeEstimations(meters: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    )
    {

        Row(verticalAlignment = Alignment.CenterVertically) {
            ActivityIcon(R.drawable.ic_walking_gray,"Walking")
            Spacer(modifier = Modifier.width(8.dp))
            RegularText(text ="${TimeUtils.getTimeEstimates(meters)[0]} min" )
        }
        Text(text = "or")
        Row(verticalAlignment = Alignment.CenterVertically) {
            ActivityIcon(R.drawable.ic_light_run,"Running")
            Spacer(modifier = Modifier.width(8.dp))
            RegularText(text = "${TimeUtils.getTimeEstimates(meters)[1]} min")
        }
        Text(text = "or")
        Row(verticalAlignment = Alignment.CenterVertically) {
            ActivityIcon(R.drawable.ic_light_cycling,"Cycling")
            Spacer(modifier = Modifier.width(8.dp))
            RegularText(text = "${TimeUtils.getTimeEstimates(meters)[2]} min")
        }

    }
}

@Composable
fun ActivityIcon(drawable: Int, description: String) {
    Icon(modifier = Modifier
        .width(20.dp)
        .height(24.dp), painter = painterResource(id = drawable) , contentDescription = description)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewActivityScreen() {
    GetgoingkmpTheme {
        ActivitiesScreen(viewModel = ActivitiesViewModel(MockRepo()), userId = 3)
    }
}

enum class GoalValues {
    LOW,
    MEDIUM,
    HIGH
}
