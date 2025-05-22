package com.souhoola.newsapp.ui.screens.details

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.souhoola.newsapp.ui.screens.details.mvi.NewsDetailIntent
import com.souhoola.newsapp.ui.screens.details.mvi.NewsDetailState
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailScreen(
    state: State<NewsDetailState>,
    onIntent: (NewsDetailIntent) -> Unit,
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val article = state.value.article

    // Handle URL opening
    LaunchedEffect(state.value.openUrl) {
        state.value.openUrl?.let { url ->
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            context.startActivity(intent)
            onIntent(NewsDetailIntent.ClearOpenUrl)
        }
    }

    // Handle article sharing
    LaunchedEffect(state.value.shareArticle) {
        state.value.shareArticle?.let { articleToShare ->
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "${articleToShare.title}\n${articleToShare.url}")
                type = "text/plain"
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share article"))
            onIntent(NewsDetailIntent.ClearShareArticle)
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Article Details",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (article == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                if (state.value.isLoading) {
                    CircularProgressIndicator()
                } else if (state.value.error != null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = state.value.error ?: "Unknown error",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { onNavigateBack() }) {
                            Text("Go Back")
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Article title
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Source and date
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = article.source.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Format date
                    val formattedDate = remember(article.publishedAt) {
                        try {
                            val instant = Instant.parse(article.publishedAt)
                            val localDateTime =
                                instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
                            DateTimeFormatter.ofLocalizedDateTime(
                                FormatStyle.MEDIUM,
                                FormatStyle.SHORT
                            )
                                .format(localDateTime)
                        } catch (e: Exception) {
                            article.publishedAt
                        }
                    }

                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Image
                article.urlToImage?.let { imageUrl ->
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = article.title,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .height(200.dp)
                    )
                }

                // Description
                article.description?.let { description ->
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // Content
                article.content?.let { content ->
                    Text(
                        text = content,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }

                // Author
                article.author?.let { author ->
                    Text(
                        text = "By $author",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }

                // Action buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { onIntent(NewsDetailIntent.ReadFullArticle(article)) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Read More")
                    }

                    OutlinedButton(
                        onClick = { onIntent(NewsDetailIntent.ShareArticle(article)) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Share")
                    }
                }
            }
        }
    }
}