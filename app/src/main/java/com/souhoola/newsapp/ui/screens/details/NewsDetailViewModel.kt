package com.souhoola.newsapp.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souhoola.newsapp.domain.model.Article
import com.souhoola.newsapp.ui.screens.details.mvi.NewsDetailIntent
import com.souhoola.newsapp.ui.screens.details.mvi.NewsDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class NewsDetailViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(NewsDetailState())
    val state: StateFlow<NewsDetailState> = _state.asStateFlow()

    val intentChannel = Channel<NewsDetailIntent>(Channel.UNLIMITED)

    init {
        processIntent()
    }

    private fun updateState(update: (NewsDetailState) -> NewsDetailState) {
        _state.value = update(_state.value)
    }

    private fun processIntent() {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect { intent ->
                when (intent) {
                    is NewsDetailIntent.LoadArticle -> {
                        updateState {
                            it.copy(
                                article = intent.article,
                                isLoading = false,
                                error = null
                            )
                        }
                    }

                    is NewsDetailIntent.ReadFullArticle -> {
                        updateState { it.copy(openUrl = intent.article.url) }
                    }

                    is NewsDetailIntent.ShareArticle -> {
                        updateState { it.copy(shareArticle = intent.article) }
                    }

                    is NewsDetailIntent.ClearOpenUrl -> {
                        updateState { it.copy(openUrl = null) }
                    }

                    is NewsDetailIntent.ClearShareArticle -> {
                        updateState { it.copy(shareArticle = null) }
                    }

                }
            }
        }
    }
}
