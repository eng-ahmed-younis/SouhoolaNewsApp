package com.souhoola.newsapp.ui.screens.newslist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.souhoola.newsapp.domain.model.Article
import com.souhoola.newsapp.domain.model.SortOption
import com.souhoola.newsapp.ui.screens.components.EmptyState
import com.souhoola.newsapp.ui.screens.components.ErrorState
import com.souhoola.newsapp.ui.screens.components.LoadingState
import com.souhoola.newsapp.ui.screens.components.NewsItem
import com.souhoola.newsapp.ui.screens.components.connectivityState
import com.souhoola.newsapp.ui.screens.newslist.mvi.NewsListIntent
import com.souhoola.newsapp.ui.screens.newslist.mvi.NewsListState
import com.souhoola.newsapp.utils.ConnectionState
import com.souhoola.newsapp.utils.NetworkConnectionObserver
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreen(
    state: State<NewsListState>,
    onIntent: (NewsListIntent) -> Unit,
) {
    val articles = state.value.articles.collectAsLazyPagingItems()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Key to track if this is the first composition
    var isInitialized by remember { mutableStateOf(false) }

    // Load articles only on first composition, not on recomposition
    LaunchedEffect(Unit) {
        if (!isInitialized) {
            onIntent(NewsListIntent.LoadArticles)
            isInitialized = true
        }
    }

    // Observe network connectivity
    val context = LocalContext.current
    val networkObserver = remember { NetworkConnectionObserver(context) }
    val connectionState by connectivityState(networkObserver)

    // Observe network changes
    LaunchedEffect(connectionState) {
        val isConnected = connectionState == ConnectionState.Available
        onIntent(NewsListIntent.ConnectionChange(isConnected))
    }

    // Handle snackbar state
    LaunchedEffect(state.value.showErrorSnackbar) {
        state.value.showErrorSnackbar?.let { errorMessage ->
            scope.launch {
                snackbarHostState.showSnackbar(errorMessage)
                onIntent(NewsListIntent.ClearSnackbar)
            }
        }
    }

    var sortMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("News App") },
                actions = {
                    IconButton(onClick = { sortMenuExpanded = true }) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = "Sort"
                        )
                    }

                    // Sort menu
                    DropdownMenu(
                        expanded = sortMenuExpanded,
                        onDismissRequest = { sortMenuExpanded = false }
                    ) {
                        SortOption.entries.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.displayName) },
                                onClick = {
                                    scope.launch {
                                        onIntent(NewsListIntent.SelectSortOption(option))
                                    }
                                    sortMenuExpanded = false
                                },
                                leadingIcon = {
                                    RadioButton(
                                        selected = state.value.selectedSortOption == option,
                                        onClick = null
                                    )
                                }
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Connection status banner
            if (!state.value.isConnected) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "No internet connection",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Search bar
            OutlinedTextField(
                value = state.value.searchQuery,
                onValueChange = {
                    scope.launch {
                        onIntent(NewsListIntent.SearchNews(it))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search news...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                singleLine = true
            )

            // Content area
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    // Error state
                    state.value.isError -> {
                        ErrorState(
                            message = state.value.errorMessage ?: "Something went wrong",
                            onRetry = {
                                scope.launch {
                                    onIntent(NewsListIntent.LoadArticles)
                                }
                            },
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    // Loading state
                    state.value.isLoading || (articles.loadState.refresh is LoadState.Loading && articles.itemCount == 0) -> {
                        LoadingState(modifier = Modifier.align(Alignment.Center))
                    }

                    // Empty state
                    articles.itemCount == 0 && articles.loadState.refresh !is LoadState.Loading -> {
                        EmptyState(modifier = Modifier.align(Alignment.Center))
                    }

                    // Content
                    else -> {
                        ArticlesList(
                            articles = articles,
                            onArticleClick = { article ->
                                scope.launch {
                                    onIntent(NewsListIntent.ClickArticle(article))
                                }
                            },
                            onRefresh = {
                                scope.launch {
                                    onIntent(NewsListIntent.LoadArticles)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArticlesList(
    articles: LazyPagingItems<Article>,
    onArticleClick: (Article) -> Unit,
    onRefresh: () -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = articles.loadState.refresh is LoadState.Loading,
        onRefresh = onRefresh
    )

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn {
            items(
                count = articles.itemCount,
                key = articles.itemKey { it.url }
            ) { index ->
                val article = articles[index]
                if (article != null) {
                    NewsItem(
                        article = article,
                        onArticleClick = onArticleClick
                    )
                }
            }

            // Footer loading indicator
            item {
                if (articles.loadState.append is LoadState.Loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            // Footer error
            item {
                if (articles.loadState.append is LoadState.Error) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Failed to load more")
                            Button(
                                onClick = { articles.retry() },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
            }
        }

        // Pull-to-refresh indicator
        PullRefreshIndicator(
            refreshing = articles.loadState.refresh is LoadState.Loading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}