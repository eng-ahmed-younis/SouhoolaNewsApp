package com.souhoola.newsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.souhoola.newsapp.domain.model.Article
import com.souhoola.newsapp.domain.model.Source
import com.souhoola.newsapp.ui.screens.details.NewsDetailScreen
import com.souhoola.newsapp.ui.screens.details.NewsDetailViewModel
import com.souhoola.newsapp.ui.screens.details.NewsDetailViewModelFactory
import com.souhoola.newsapp.ui.screens.newslist.NewsListScreen
import com.souhoola.newsapp.ui.screens.newslist.NewsViewModel
import com.souhoola.newsapp.ui.screens.newslist.mvi.NewsListIntent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun NewsNavigation(navController: NavHostController, scope: CoroutineScope) {
    NavHost(navController = navController, startDestination = Screens.NewsList) {

        composable<Screens.NewsList> {
            val viewModel: NewsViewModel = hiltViewModel()
            val state = viewModel.state.collectAsState()

            LaunchedEffect(state.value.articleToNavigateTo) {
                state.value.articleToNavigateTo?.let { article ->
                    navController.navigate(Screens.NewsDetail(
                        id = article.source.id,
                        name = article.source.name,
                        author = article.author,
                        title = article.title,
                        description = article.description,
                        url = article.url,
                        urlToImage = article.urlToImage,
                        publishedAt = article.publishedAt,
                        content = article.content
                    ))
                    viewModel.intentChannel.send(NewsListIntent.ClearNavigation)
                }
            }

            NewsListScreen(
                state = state,
                onIntent = { intent -> scope.launch { viewModel.intentChannel.send(intent) } }
            )
        }

        composable<Screens.NewsDetail> { backStackEntry ->
            val params = backStackEntry.toRoute<Screens.NewsDetail>()

            val article = Article(
                source = Source(id = params.id, name = params.name),
                author = params.author,
                title = params.title,
                description = params.description,
                url = params.url,
                urlToImage = params.urlToImage,
                publishedAt = params.publishedAt,
                content = params.content
            )

            val viewModel: NewsDetailViewModel =
                hiltViewModel<NewsDetailViewModel, NewsDetailViewModelFactory> {
                    it.create(article)
                }

            NewsDetailScreen(
                state = viewModel.state.collectAsState(),
                onIntent = { scope.launch { viewModel.intentChannel.send(it) } },
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
