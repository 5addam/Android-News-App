package com.newsapp.api

import com.newsapp.data.Article

data class NewsResponse(
    val articles: List<Article>
)