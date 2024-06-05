package com.hyperether.getgoing_kmp.android.presentation.scenes.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.android.material.color.MaterialColors
import com.hyperether.getgoing_kmp.android.presentation.scenes.profile.util.ProfileUtil.generateAgeList
import com.hyperether.getgoing_kmp.android.presentation.scenes.profile.util.ProfileUtil.generateHeightList
import com.hyperether.getgoing_kmp.android.presentation.scenes.profile.util.ProfileUtil.generateWeightList
import com.hyperether.getgoing_kmp.android.presentation.ui.components.ConfirmButton
import com.hyperether.getgoing_kmp.android.presentation.ui.components.DismissButton
import com.hyperether.getgoing_kmp.model.UserGender

data class Items(
    val value: Int
)

enum class DialogType {
    None, GenderDialog, AgeDialog, HeightDialog, WeightDialog
}

@Composable
fun EditGenderDialog(
    showDialog: Boolean,
    currentGender: UserGender?,
    onDismiss: () -> Unit = {},
    onConfirm: (UserGender) -> Unit = {}
) {
    var selectedOption by remember { mutableStateOf(currentGender) }

    LaunchedEffect(showDialog) {
        if (showDialog) {
            selectedOption = currentGender
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            title = {
                Text(
                    text = "Please select your gender:",
                    color = MaterialTheme.colorScheme.primary
                )
            },
            text = {
                Column {
                    UserGender.entries.forEach { gender ->
                        Row(
                            Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedOption == gender,
                                onClick = { selectedOption = gender }
                            )
                            Text(
                                text = gender.name,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedOption?.let(onConfirm)
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun EditAgeDialog(
    showDialog: Boolean,
    currentAge: Int,
    onDismiss: () -> Unit = {},
    onConfirm: (Int) -> Unit = {}
) {
    val items = generateAgeList()
    var selectedIndex by remember { mutableIntStateOf(items.indexOf(Items(currentAge))) }
    LaunchedEffect(showDialog) {
        if (showDialog) {
            selectedIndex = items.indexOf(Items(currentAge))
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            title = {
                Text(
                    text = "How old are you?",
                    color = MaterialTheme.colorScheme.primary
                )
            },
            text = {
                LargeDropdownMenu(
                    items = items,
                    selectedIndex = selectedIndex,
                    onItemSelected = { index, _ -> selectedIndex = index },
                )
            },
            confirmButton = {
                ConfirmButton(
                    onClick = { onConfirm(items[selectedIndex].value) }
                )
            },
            dismissButton = {
                DismissButton(
                    onClick = onDismiss
                )
            }
        )
    }
}

@Composable
fun EditHeightDialog(
    showDialog: Boolean,
    currentHeight: Int,
    onDismiss: () -> Unit = {},
    onConfirm: (Int) -> Unit = {}
) {
    val items = generateHeightList()
    var selectedIndex by remember { mutableIntStateOf(items.indexOf(Items(currentHeight))) }
    LaunchedEffect(showDialog) {
        if (showDialog) {
            selectedIndex = items.indexOf(Items(currentHeight))
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            title = {
                Text(
                    text = "Enter your height:",
                    color = MaterialTheme.colorScheme.primary
                )
            },
            text = {
                LargeDropdownMenu(
                    items = items,
                    selectedIndex = selectedIndex,
                    onItemSelected = { index, _ -> selectedIndex = index },
                )
            },
            confirmButton = {
                ConfirmButton(
                    onClick = { onConfirm(items[selectedIndex].value) }
                )
            },
            dismissButton = {
                DismissButton(
                    onClick = onDismiss
                )
            }
        )
    }
}

@Composable
fun EditWeightDialog(
    showDialog: Boolean,
    currentWeight: Int,
    onDismiss: () -> Unit = {},
    onConfirm: (Int) -> Unit = {}
) {
    val items = generateWeightList()
    var selectedIndex by remember { mutableIntStateOf(items.indexOf(Items(currentWeight))) }
    LaunchedEffect(showDialog) {
        if (showDialog) {
            selectedIndex = items.indexOf(Items(currentWeight))
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            title = {
                Text(
                    text = "Enter your weight:",
                    color = MaterialTheme.colorScheme.primary
                )
            },
            text = {
                LargeDropdownMenu(
                    items = items,
                    selectedIndex = selectedIndex,
                    onItemSelected = { index, _ -> selectedIndex = index },
                )
            },
            confirmButton = {
                ConfirmButton(
                    onClick = { onConfirm(items[selectedIndex].value) }
                )
            },
            dismissButton = {
                DismissButton(
                    onClick = onDismiss
                )
            }
        )
    }
}

@Composable
fun LargeDropdownMenu(
    items: List<Items>,
    selectedIndex: Int = -1,
    onItemSelected: (index: Int, item: Items) -> Unit,
    drawItem: @Composable (Items, Boolean, Boolean, () -> Unit) -> Unit = { item, selected, itemEnabled, onClick ->
        LargeDropdownMenuItem(
            text = item.value.toString(),
            selected = selected,
            enabled = itemEnabled,
            onClick = onClick,
        )
    },
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.height(IntrinsicSize.Min)) {
        TextField(
            readOnly = true,
            value = items[selectedIndex].value.toString(),
            onValueChange = {},
            trailingIcon = {
                val icon =
                    if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
                Icon(icon, "")
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.extraSmall)
                .clickable(enabled = true) { expanded = true },
            color = Color.Transparent,
        ) {}
    }

    if (expanded) {
        Dialog(
            onDismissRequest = { expanded = false },
        ) {
            Surface(
                modifier = Modifier
                    .sizeIn(
                        minWidth = 280.dp,
                        maxWidth = 560.dp,
                        maxHeight = 560.dp
                    )
                    .padding(20.dp),
                shape = RoundedCornerShape(12.dp),
            ) {
                val listState = rememberLazyListState()
                if (selectedIndex > -1) {
                    LaunchedEffect("ScrollToSelected") {
                        listState.scrollToItem(index = selectedIndex)
                    }
                }

                LazyColumn(modifier = Modifier.fillMaxWidth(), state = listState) {
                    itemsIndexed(items) { index, item ->
                        val selectedItem = index == selectedIndex
                        drawItem(
                            item,
                            selectedItem,
                            true
                        ) {
                            onItemSelected(index, item)
                            expanded = false
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LargeDropdownMenuItem(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val contentColor = when {
        !enabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = MaterialColors.ALPHA_DISABLED)
        selected -> MaterialTheme.colorScheme.primary.copy(alpha = MaterialColors.ALPHA_FULL)
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = MaterialColors.ALPHA_FULL)
    }

    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Box(modifier = Modifier
            .clickable(enabled) { onClick() }
            .fillMaxWidth()
            .padding(20.dp)) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}