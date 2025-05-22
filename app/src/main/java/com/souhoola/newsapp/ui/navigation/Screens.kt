package com.souhoola.newsapp.ui.navigation

import java.net.URLEncoder

sealed class Screens(val route: String) {
    object NewsList : Screens("news_list")
    object NewsDetail : Screens("news_detail/{articleJson}") {
        fun createRoute(articleJson: String): String {
            val encodedJson = URLEncoder.encode(articleJson, "UTF-8")
            return "news_detail/$encodedJson"
        }
    }
}
