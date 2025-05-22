package com.souhoola.newsapp.data.mappers

import com.souhoola.newsapp.data.dto.ArticleDto
import com.souhoola.newsapp.data.dto.NewsResponseDto
import com.souhoola.newsapp.data.dto.SourceDto
import com.souhoola.newsapp.domain.model.Article
import com.souhoola.newsapp.domain.model.Source

fun SourceDto.toDomain(): Source =
    Source(
        id = this.id,
        name = this.name
    )

fun ArticleDto.toDomain(): Article =
    Article(
        source = this.source.toDomain(),
        author = this.author,
        title = this.title,
        description = this.description,
        url = this.url,
        urlToImage = this.urlToImage,
        publishedAt = this.publishedAt,
        content = this.content
    )

fun NewsResponseDto.toDomainArticles(): List<Article> =
    this.articles.map { it.toDomain() }

fun Source.toDto(): SourceDto =
    SourceDto(
        id = this.id,
        name = this.name
    )

fun Article.toDto(): ArticleDto =
    ArticleDto(
        source = this.source.toDto(),
        author = this.author,
        title = this.title,
        description = this.description,
        url = this.url,
        urlToImage = this.urlToImage,
        publishedAt = this.publishedAt,
        content = this.content
    )