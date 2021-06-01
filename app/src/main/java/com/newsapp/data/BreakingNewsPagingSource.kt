package com.newsapp.data

import androidx.paging.PagingSource
import com.newsapp.api.NewsApi
import retrofit2.HttpException
import java.io.IOException

private const val ARTICLE_STARTING_PAGE_INDEX = 1

class BreakingNewsPagingSource(
    private val newsApi: NewsApi,
    private val country: String
) : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key ?: ARTICLE_STARTING_PAGE_INDEX

        return try {
            val response = newsApi.getBreakingNews(country, position, params.loadSize)
            val articles = response.articles

            LoadResult.Page(
                data = articles,
                prevKey = if (position == ARTICLE_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (articles.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }

    }

}