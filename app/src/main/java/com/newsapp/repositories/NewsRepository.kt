package com.newsapp.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.newsapp.data.Article
import com.newsapp.data.ArticlePagingSource
import com.newsapp.data.BreakingNewsPagingSource
import com.newsapp.db.ArticleDB
import com.newsapp.module.RetrofitInstance


class NewsRepository(
    val db: ArticleDB
) {
    fun getBreakingNews(countryCode: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { BreakingNewsPagingSource(RetrofitInstance.api, countryCode) }
        ).liveData
//        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    fun searchNews(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ArticlePagingSource(RetrofitInstance.api, query) }
        ).liveData

//    RetrofitInstance.api.searchNews(query, pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}