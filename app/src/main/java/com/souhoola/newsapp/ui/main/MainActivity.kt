package com.souhoola.newsapp.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.souhoola.newsapp.ui.navigation.NewsNavigation
import com.souhoola.newsapp.ui.theme.SouhoolaNewsAppTheme
import kotlinx.serialization.json.Json


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var json: Json

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SouhoolaNewsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NewsNavigation(
                        navController = navController,
                        json = remember { json },
                        scope = rememberCoroutineScope()
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true , showSystemUi = true)
@Composable
fun GreetingPreview() {
    SouhoolaNewsAppTheme {
        val navController = rememberNavController()
        NewsNavigation(navController, Json, rememberCoroutineScope())
    }
}