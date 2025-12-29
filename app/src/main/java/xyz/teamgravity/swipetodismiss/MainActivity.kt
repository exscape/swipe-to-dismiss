package xyz.teamgravity.swipetodismiss

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import xyz.teamgravity.swipetodismiss.ui.theme.SwipeToDismissTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SwipeToDismissTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val items = remember {
                        mutableStateListOf(
                            "Kotlin",
                            "Java",
                            "C++",
                            "C#",
                            "JavaScript"
                        )
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = items,
                            key = { it } // Use a unique key in practice!
                        ) { item ->
                            SwipeToDelete(
                                item = item,
                                modifier = Modifier.animateItem(),
                                onDelete = { deletedItem ->
                                    items -= deletedItem
                                },
                            ) { currentItem ->
                                Text(
                                    text = currentItem,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.background)
                                        .padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun <T> SwipeToDelete(
        item: T,
        modifier: Modifier = Modifier,
        onDelete: (T) -> Unit,
        content: @Composable (T) -> Unit
    ) {
        val state = rememberSwipeToDismissBoxState()
        val currentOnDelete by rememberUpdatedState(onDelete)

        LaunchedEffect(state.settledValue) {
            if (state.settledValue == SwipeToDismissBoxValue.EndToStart) {
                currentOnDelete(item)
                state.reset()
            }
        }

        SwipeToDismissBox(
            modifier = modifier,
            state = state,
            enableDismissFromStartToEnd = false,
            enableDismissFromEndToStart = true,
            backgroundContent = {
                val color = if (state.dismissDirection == SwipeToDismissBoxValue.EndToStart) Color.Red else Color.Transparent
                Box(
                    contentAlignment = Alignment.CenterEnd,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete item",
                        tint = Color.White
                    )
                }
            },
            content = {
                content(item)
            }
        )
    }
}