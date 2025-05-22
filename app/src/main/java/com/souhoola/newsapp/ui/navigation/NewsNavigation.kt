package com.souhoola.newsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.souhoola.newsapp.domain.model.Article
import com.souhoola.newsapp.ui.screens.details.NewsDetailScreen
import com.souhoola.newsapp.ui.screens.details.NewsDetailViewModel
import com.souhoola.newsapp.ui.screens.details.mvi.NewsDetailIntent
import com.souhoola.newsapp.ui.screens.newslist.NewsListScreen
import com.souhoola.newsapp.ui.screens.newslist.NewsViewModel
import com.souhoola.newsapp.ui.screens.newslist.mvi.NewsListIntent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder

@Composable
fun NewsNavigation(
    navController: NavHostController,
    json: Json,
    scope: CoroutineScope
) {
    NavHost(
        navController = navController,
        startDestination = Screens.NewsList.route
    ) {

        composable(Screens.NewsList.route) {
            val viewModel: NewsViewModel = hiltViewModel()
            val state = viewModel.state.collectAsState()

            LaunchedEffect(state.value.articleToNavigateTo) {
                state.value.articleToNavigateTo?.let { article ->
                    val encoded = java.net.URLEncoder.encode(Json.encodeToString(article), "UTF-8")
                    navController.navigate(Screens.NewsDetail.createRoute(encoded))
                    // Clear navigation state immediately after navigation
                    viewModel.intentChannel.send(NewsListIntent.ClearNavigation)
                }
            }

            NewsListScreen(
                state = state,
                onIntent = { intent ->
                    scope.launch {
                        viewModel.intentChannel.send(intent)
                    }
                }
            )
        }

        composable(
            route = Screens.NewsDetail.route,
            arguments = listOf(navArgument("articleJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val viewModel:  NewsDetailViewModel = hiltViewModel()
            val encodedJson = backStackEntry.arguments?.getString("articleJson") ?: ""
            val decodedJson = URLDecoder.decode(encodedJson, "UTF-8")
            val article = json.decodeFromString<Article>(decodedJson)

            LaunchedEffect (Unit) {
                viewModel.intentChannel.send(NewsDetailIntent.LoadArticle(article))
            }

            NewsDetailScreen(
                state = viewModel.state.collectAsState(),
                onIntent = {
                    scope.launch {
                        viewModel.intentChannel.send(it)
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}