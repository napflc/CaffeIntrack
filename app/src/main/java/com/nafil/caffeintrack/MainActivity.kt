package com.nafil.caffeintrack

import android.R.attr.value
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nafil.caffeintrack.ui.theme.CaffeIntrackTheme
import kotlin.collections.listOf
import kotlin.coroutines.coroutineContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CaffeIntrackTheme {
                MainScreen()
            }
        }
    }
}

enum class AppScreen{
    LIST, INPUT
}
data class Coffee(val icon: ImageVector, val name: String, val mg: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var isMenuOpen by remember { mutableStateOf(false) }
    var currentScreen by remember { mutableStateOf(AppScreen.LIST) }
    var caffAmount by remember { mutableStateOf("") }
    var totalCaffeine by remember { mutableStateOf(0) }
    var isInputError by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {isMenuOpen = true}) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add caffeine"
                )
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Text(
                text = "Today's Caffeine"
            )
            Text(text = "$totalCaffeine")
        }
    }
    if (isMenuOpen) {
        val coffeeList = listOf(
            Coffee(icon = Icons.Default.LocalCafe, name = "Espresso", mg = 70),
            Coffee(icon = Icons.Default.LocalCafe, name = "Americano", mg = 43),
            Coffee(icon = Icons.Default.LocalCafe, name = "Latte", mg = 35)
        )

        ModalBottomSheet(
            onDismissRequest = {isMenuOpen = false},
            sheetState = sheetState
        ) {
            when (currentScreen) {
                AppScreen.LIST -> {
                    LazyColumn {
                        items(coffeeList) { coffee ->
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clickable { currentScreen = AppScreen.INPUT }
                            ) {
                                Icon(
                                    imageVector = coffee.icon,
                                    contentDescription = "Coffee Icon"
                                )
                                Column {
                                    Text(text = "${coffee.name}")
                                    Text(text = "${coffee.mg} mg")
                                }
                            }
                        }
                    }
                }
                AppScreen.INPUT -> {
                    Column(modifier = Modifier.padding(16.dp)) {
                        OutlinedTextField(
                            value = caffAmount,
                            onValueChange = {
                                caffAmount = it
                                isInputError = false },
                            label = { Text("Amount") },
                            isError = isInputError,
                            supportingText = {
                                if (isInputError) {
                                    Text("Input valid number")
                                }
                            }
                        )
                        Button(
                            onClick = {
                                val amount = caffAmount.toIntOrNull()
                                if (amount != null) {
                                    totalCaffeine += amount
                                    isMenuOpen = false
                                    currentScreen = AppScreen.LIST
                                    isInputError = false
                                    }
                                else {
                                    isInputError = true
                                    }
                                }
                            ) {
                            Text(text = "Submit")
                        }
                    }
                }
            }
        }
    }
}