package com.hyperether.getgoing_kmp.android.presentation.scenes.tracking

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.hyperether.getgoing_kmp.android.presentation.mock.MockRepo
import com.hyperether.getgoing_kmp.android.presentation.ui.components.CirceButtonContainer
import com.hyperether.getgoing_kmp.android.presentation.ui.components.GGShape
import com.hyperether.getgoing_kmp.android.presentation.ui.components.PlayButton
import com.hyperether.getgoing_kmp.android.presentation.ui.theme.GetgoingkmpTheme

@Composable
fun TrackingScreen(viewModel: TrackingViewModel) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {

    }

    Column(modifier = Modifier.fillMaxSize()) {
        val singapore = LatLng(1.35, 103.87)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(singapore, 10f)
        }
        Box(
            Modifier
                .weight(1f)
                .clip(GGShape()),
            contentAlignment = Alignment.BottomCenter
        ) {
            GoogleMap(
                cameraPositionState = cameraPositionState,
                modifier = Modifier.fillMaxSize()
            ) {
                Marker(
                    state = MarkerState(position = singapore),
                    title = "Singapore",
                    snippet = "Marker in Singapore"
                )
            }

            Box(modifier = Modifier.padding(bottom = 20.dp), contentAlignment = Alignment.Center) {
                CirceButtonContainer()
                PlayButton {}
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {

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