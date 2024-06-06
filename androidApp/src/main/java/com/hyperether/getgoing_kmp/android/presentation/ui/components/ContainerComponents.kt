package com.hyperether.getgoing_kmp.android.presentation.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyperether.getgoing_kmp.android.R
import com.hyperether.getgoing_kmp.android.presentation.ui.theme.GetgoingkmpTheme
import com.hyperether.getgoing_kmp.android.util.ExerciseType
import com.hyperether.getgoing_kmp.repository.room.Route


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

@Composable
fun GoalProgress(
    bgColor: Color = MaterialTheme.colorScheme.secondary,
    color: Color = MaterialTheme.colorScheme.primary,
    canvasSize: Dp = 240.dp,
    progress: Float = 0f
) {
    var expanded by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        expanded = true
    }
    val p by animateFloatAsState(
        targetValue = if (expanded) progress else 0f,
        animationSpec = tween(
            durationMillis = 1000 // Set the duration to 3 seconds for a slow expansion
        )
    )

    Canvas(
        modifier = Modifier
            .size(240.dp, 200.dp)
    ) {
        val strokeWidthPx = 12.dp.toPx()
        val arcSize = Size(240.dp.toPx(), 240.dp.toPx()).width - strokeWidthPx

        drawArc(
            color = bgColor,
            30f,
            -240f,
            useCenter = false,
            size = Size(arcSize, arcSize),
            style = Stroke(strokeWidthPx, cap = StrokeCap.Round),
            topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2)
        )

        var angle = p * 250f
        if (angle > 250f) {
            angle = 250f
        }
        drawArc(
            color = color,
            -210f,
            angle,
            useCenter = false,
            size = Size(arcSize, arcSize),
            style = Stroke(strokeWidthPx, cap = StrokeCap.Round),
            topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2)
        )
    }
}

@Composable
fun GraphView(
    mergedRoutes: List<Route>,
    goal: Float,
    animated: Boolean = true,
    selected: (Long) -> Unit
) {
    if (mergedRoutes.isNotEmpty())
        Column(Modifier.padding(top = 24.dp)) {
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(text = "Goal")
                Text(
                    text = "___________________________________________________________________________________",
                    style = TextStyle(color = MaterialTheme.colorScheme.primary),
                    maxLines = 1,
                    modifier = Modifier
                        .weight(1f)
                        .offset(y = 2.dp)
                )
                Text(text = "5.00km")
            }

            var selectedId by remember {
                mutableLongStateOf(mergedRoutes.last().id)
            }
            val scrollState = rememberLazyListState(mergedRoutes.size - 1)

            LazyRow(
                Modifier
                    .height(160.dp)
                    .padding(start = 30.dp, end = 30.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                state = scrollState
            ) {
                items(mergedRoutes) {
                    var expanded by remember { mutableStateOf(false) }
                    var x = 130.dp * (it.length.toFloat() / goal)
                    if (x > 130.dp) {
                        x = 130.dp
                    }

                    LaunchedEffect(Unit) {
                        expanded = true
                    }

                    val height by animateDpAsState(
                        targetValue = if (expanded) x else 0.dp,
                        animationSpec = tween(
                            durationMillis = 1000 // Set the duration to 3 seconds for a slow expansion
                        )
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.noRippleClickable {
                            selectedId = it.id
                            selected(it.id)
                        }) {
                        Box(
                            modifier = Modifier
                                .height(130.dp)
                                .width(12.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .height(if (animated) height else x)
                                    .width(12.dp)
                                    .clip(RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp))
                                    .background(
                                        if (it.id == selectedId) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.primary.copy(
                                                alpha = 0.2f
                                            )
                                        }
                                    )
                            )
                        }

                        Text(text = it.date.slice(0..5))
                    }
                }
            }
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
                Box(modifier = Modifier.size(300.dp)) {
                    GoalProgress()
                }
            }

            LastExercise(
                ExerciseType.RUNNING, "20m", 0.3f, "34", "10:23:23", 0.2f
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainer),
                contentAlignment = Alignment.Center
            ) {
                GraphView(
                    listOf(
                        Route(0, 350, 400.0, 1000.0, "05.04.2024. 09:26:45", 0.0, 0.0, 0, 2000),
                        Route(1, 350, 400.0, 2000.0, "06.04.2024. 09:26:45", 0.0, 0.0, 0, 3000),
                        Route(2, 350, 400.0, 3000.0, "07.04.2024. 09:26:45", 0.0, 0.0, 0, 2000),
                        Route(3, 350, 400.0, 4000.0, "08.04.2024. 09:26:45", 0.0, 0.0, 0, 5000),
                        Route(4, 350, 400.0, 3000.0, "05.04.2024. 09:26:45", 0.0, 0.0, 0, 2000),
                        Route(5, 350, 400.0, 9000.0, "06.04.2024. 09:26:45", 0.0, 0.0, 0, 3000),
                        Route(6, 350, 400.0, 3000.0, "07.04.2024. 09:26:45", 0.0, 0.0, 0, 2000),
                        Route(7, 350, 400.0, 5000.0, "08.04.2024. 09:26:45", 0.0, 0.0, 0, 5000)
                    ),
                    5000f,
                    true,
                    {}
                )
            }
        }
    }
}