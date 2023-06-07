package com.ece452.pillmaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ece452.pillmaster.reminder.ReminderScreen
import com.ece452.pillmaster.ui.theme.PillMasterTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // TODO - Use https://developer.android.com/reference/android/window/SplashScreen to create the splash.
    // TODO - Create App Icon resource and register it in AndroidManifest.xml
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PillMasterTheme {
                PillMasterApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PillMasterApp() {
    // Inflate the reminder screen as an example for now.
    // TODO - This reminder screen is an example.
    // ReminderScreen()

    // TODO !!! This is the entry composable.
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    Scaffold() { innerPadding ->
        PillMasterNavHost(
            nC = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PillMasterTheme {
        Greeting("Android")
    }
}