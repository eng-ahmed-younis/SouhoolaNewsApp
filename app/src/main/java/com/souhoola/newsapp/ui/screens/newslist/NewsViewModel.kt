package com.souhoola.newsapp.ui.screens.newslist

import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.souhoola.newsapp.domain.use_case.GetTopHeadlinesUseCase
import com.souhoola.newsapp.domain.use_case.SearchNewsUseCase
import com.souhoola.newsapp.ui.screens.newslist.mvi.NewsListIntent
import com.souhoola.newsapp.ui.screens.newslist.mvi.NewsListState
import com.souhoola.newsapp.utils.dispatchers.DispatchersProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import java.util.Locale

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase,
    private val searchNewsUseCase: SearchNewsUseCase,
    private val dispatcher: DispatchersProvider
) : ViewModel() {

    val intentChannel = Channel<NewsListIntent>(Channel.UNLIMITED)


    private val _state = MutableStateFlow(NewsListState())
    val state: StateFlow<NewsListState> = _state.asStateFlow()

    init {
        processIntent()
    }

    @OptIn(FlowPreview::class)
    fun processIntent() {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect { intent->
                when (intent) {
                    is NewsListIntent.LoadArticles -> {
                        loadArticles()
                    }
                    is NewsListIntent.SearchNews -> {
                        updateState { it.copy(searchQuery = intent.query) }
                        loadArticles()
                    }
                    is NewsListIntent.SelectSortOption -> {
                        updateState { it.copy(selectedSortOption = intent.option) }
                        if (state.value.searchQuery.isNotBlank()) {
                            loadArticles()
                        }
                    }
                    is NewsListIntent.ClickArticle -> {
                        updateState { it.copy(articleToNavigateTo = intent.article) }
                    }
                    is NewsListIntent.ConnectionChange -> {
                        updateState { it.copy(isConnected = intent.isConnected) }
                    }
                    is NewsListIntent.ClearNavigation -> {
                        updateState { it.copy(articleToNavigateTo = null) }
                    }
                    is NewsListIntent.ClearSnackbar -> {
                        updateState { it.copy(showErrorSnackbar = null) }
                    }
                }
            }
        }

    }

    private fun updateState(update: (NewsListState) -> NewsListState) {
        _state.value = update(_state.value)
    }

    private fun loadArticles() {
        updateState { it.copy(isLoading = true, isError = false, errorMessage = null) }

        viewModelScope.launch (dispatcher.io){
            try {
                val articlesFlow = if (state.value.searchQuery.isBlank()) {
                    // Load top headlines if no search query
                    getTopHeadlinesUseCase.invoke(country = "us")
                } else {
                    // Search with query and sort option
                    searchNewsUseCase(
                        query = state.value.searchQuery,
                        sortOption = state.value.selectedSortOption
                    )
                }.cachedIn(viewModelScope)

                updateState {
                    it.copy(articles = articlesFlow, isLoading = false)
                }
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Unknown error occurred"
                updateState {
                    it.copy(
                        isError = true,
                        errorMessage = errorMsg,
                        isLoading = false,
                        showErrorSnackbar = errorMsg
                    )
                }
            }
        }
    }
}