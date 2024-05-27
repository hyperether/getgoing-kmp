package com.hyperether.getgoing_kmp.android.presentation.scenes.getgoing

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hyperether.getgoing_kmp.android.presentation.mock.MockRepo

@Composable
fun GetGoingScreen(viewModel: GetGoingViewModel) {

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun GGSPreview() {
    GetGoingScreen(viewModel = GetGoingViewModel(MockRepo()))
}