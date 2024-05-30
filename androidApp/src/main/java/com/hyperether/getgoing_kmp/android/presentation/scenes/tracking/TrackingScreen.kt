package com.hyperether.getgoing_kmp.android.presentation.scenes.tracking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.hyperether.getgoing_kmp.android.presentation.mock.MockRepo
import com.hyperether.getgoing_kmp.android.presentation.ui.components.BackButton
import com.hyperether.getgoing_kmp.android.presentation.ui.components.BoldLargeText
import com.hyperether.getgoing_kmp.android.presentation.ui.components.CirceButtonContainer
import com.hyperether.getgoing_kmp.android.presentation.ui.components.GGShape
import com.hyperether.getgoing_kmp.android.presentation.ui.components.LargeText
import com.hyperether.getgoing_kmp.android.presentation.ui.components.PlayButton
import com.hyperether.getgoing_kmp.android.presentation.ui.theme.GetgoingkmpTheme

@Composable
fun TrackingScreen(viewModel: TrackingViewModel, onBack: () -> Unit = {}) {

    Column(modifier = Modifier.fillMaxSize()) {
        val cameraPositionState = rememberCameraPositionState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(color = MaterialTheme.colorScheme.background),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton {
                onBack()
            }
            LargeText(text = "Running")
        }
        Box(
            Modifier
                .weight(1f)
                .clip(GGShape()),
            contentAlignment = Alignment.BottomCenter
        ) {
            viewModel.locationState.value.let {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 20f)
            }

            GoogleMap(
                cameraPositionState = cameraPositionState,
                modifier = Modifier.fillMaxSize(),
                properties = MapProperties(isMyLocationEnabled = true),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    myLocationButtonEnabled = false
                )
            ) {

                Polyline(
                    points = viewModel.listOfGreenPoly.value, color = Color.Green
                )

                Polyline(
                    points = viewModel.listOfYellowPoly.value, color = Color.Yellow
                )

                Polyline(
                    points = viewModel.listOfOrangePoly.value, color = Color(255, 128, 0)
                )

                Polyline(
                    points = viewModel.listOfRedPoly.value, color = Color.Red
                )

            }

            Box(modifier = Modifier.padding(bottom = 20.dp), contentAlignment = Alignment.Center) {
                CirceButtonContainer()
                PlayButton(viewModel.trackingStarted.value) {
                    if (!viewModel.trackingStarted.value)
                        viewModel.startTracking()
                    else {
                        viewModel.stopTracking()
                    }
                }
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BoldLargeText(text = viewModel.distanceState.value)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Duration")
                    Text(text = viewModel.durationState.value)
                }

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Speed")
                    Text(text = viewModel.velocityState.value)
                }

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Calories")
                    Text(text = viewModel.caloriesState.value)
                }

            }

        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TrackingPreview() {
    GetgoingkmpTheme {
        TrackingScreen(viewModel = TrackingViewModel(MockRepo()))
    }
}