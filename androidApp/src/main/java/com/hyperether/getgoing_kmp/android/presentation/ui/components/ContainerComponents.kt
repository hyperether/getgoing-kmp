package com.hyperether.getgoing_kmp.android.presentation.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyperether.getgoing_kmp.android.R
import com.hyperether.getgoing_kmp.android.presentation.ui.theme.GetgoingkmpTheme
import com.hyperether.getgoing_kmp.android.util.ExerciseType


@Composable
fun AppToolbar(
    titleId: Int,
    onNavigateBack: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackButton {
            onNavigateBack()
        }

        Text(
            text = stringResource(id = titleId),
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

@Composable
fun AppToolbarDynamic(
    title: String,
    onNavigateBack: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackButton {
            onNavigateBack()
        }

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

@Composable
fun LastExercise(
    exercise: ExerciseType,
    distance: String,
    exerciseProgress: Float,
    kcal: String,
    duration: String,
    timeProgress: Float
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.7f)
            .clip(RoundedCornerShape(12.dp))
            .background(color = MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ProgressWithIconAndText(
                exercise,
                distance,
                exerciseProgress
            )

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "kcal",
                    fontSize = 14.sp,
                    style = TextStyle.Default.copy(
                        fontSynthesis = FontSynthesis.None,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
                Text(
                    text = kcal,
                    fontSize = 16.sp,
                    style = TextStyle.Default.copy(
                        fontSynthesis = FontSynthesis.None,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }

            ProgressWithIconAndText(
                null,
                duration,
                timeProgress
            )
        }
    }
}

@Composable
fun ProgressWithIconAndText(
    type: ExerciseType? = null,
    textValue: String,
    progress: Float
) {
    Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.size(100.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        )
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.size(100.dp),
            color = MaterialTheme.colorScheme.onPrimary
        )
        Column(
            modifier = Modifier.size(100.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (type) {
                ExerciseType.RUNNING -> {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        painter = painterResource(id = R.drawable.ic_light_running_white),
                        contentDescription = "Activity icon",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                ExerciseType.WALKING -> {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        painter = painterResource(id = R.drawable.ic_walking_white),
                        contentDescription = "Activity icon",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                ExerciseType.CYCLING -> {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        painter = painterResource(id = R.drawable.ic_bicycling_white),
                        contentDescription = "Activity icon",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                null -> {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        painter = painterResource(id = R.drawable.ic_timer),
                        contentDescription = "Activity icon",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Text(
                text = textValue,
                fontSize = 16.sp,
                style = TextStyle.Default.copy(
                    fontSynthesis = FontSynthesis.None,
                    color = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = type?.value ?: "Time",
                fontSize = 14.sp,
                style = TextStyle.Default.copy(
                    fontSynthesis = FontSynthesis.None,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}

@Composable
fun CirceButtonContainer() {
    Box(contentAlignment = Alignment.Center) {
        Box(
            Modifier
                .size(115.dp)
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
        )

        Box(
            Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.surfaceContainerLow)
        )
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EndlessListExercise(list: List<ExerciseType>, selected: (ExerciseType) -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val pagerState = rememberPagerState(pageCount = { 50 })
        var scrolled by remember {
            mutableStateOf(false)
        }
        LaunchedEffect(key1 = true) {
            pagerState.scrollToPage(24)
            scrolled = true
        }

        CirceButtonContainer()

        CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
            HorizontalPager(
                modifier = Modifier.height(70.dp),
                pageSpacing = 15.dp,
                contentPadding = PaddingValues(horizontal = 120.dp),
                state = pagerState
            ) { index ->

                list.getOrNull(
                    index % (list.size)
                )?.let { item ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (scrolled) {
                            var size = 50.dp
                            var color = MaterialTheme.colorScheme.tertiary
                            if (index == pagerState.settledPage) {
                                size = 60.dp
                                color = MaterialTheme.colorScheme.primary
                                selected(list[pagerState.settledPage % (list.size)])
                            }
                            Icon(
                                painter = painterResource(id = item.image),
                                contentDescription = "Exercise item",
                                modifier = Modifier.size(size),
                                tint = color
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ShapedColumn(
    modifier: Modifier,
    verticalArrangement: Arrangement.Vertical,
    color: Color = MaterialTheme.colorScheme.primaryContainer,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .drawBehind {
                val path = generateGGPath(size)
                drawPath(path = path, color = color, style = Fill)
            },
        verticalArrangement = verticalArrangement
    ) {
        content()
    }
}


@Preview
@Composable
private fun ContainerComponentsPreview() {
    GetgoingkmpTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            EndlessListExercise(
                ExerciseType.entries,
                {}
            )

            ShapedColumn(Modifier, Arrangement.SpaceBetween) {
                Box(modifier = Modifier.size(300.dp))
            }

            LastExercise(
                ExerciseType.RUNNING, "20m", 0.3f, "34", "10:23:23", 0.2f
            )
        }
    }
}