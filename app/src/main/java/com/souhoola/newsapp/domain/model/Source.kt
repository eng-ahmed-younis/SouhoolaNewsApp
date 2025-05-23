package com.souhoola.newsapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Source(
    val id: String? = null,
    val name: String = ""
)