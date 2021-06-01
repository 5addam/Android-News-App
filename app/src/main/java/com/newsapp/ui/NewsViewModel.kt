package com.newsapp.ui

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.newsapp.api.NewsResponse
import com.newsapp.data.Article
import com.newsapp.repositories.NewsRepository
import kotlinx.coroutines.launch
import com.newsapp.util.Resource
import dagger.assisted.Assisted
import retrofit2.Response

class NewsViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

//    init {
//        getBreakingNews("us")
//    }

//    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
//        breakingNews.postValue(Resource.Loading())
//        val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
//        breakingNews.postValue(handleBreakingNewsResponse(response))
//    }

    private val currentCountry = MutableLiveData(DEFAULT_COUNTRY)
    val topHeadlines = currentCountry.switchMap { country ->
        newsRepository.getBreakingNews(country).cachedIn(viewModelScope)
    }

    fun getBreakingNews(countryCode: String) {
        currentCountry.value = countryCode
    }


    private val currentQuery = MutableLiveData(CURRENT_QUERY)
    val articles = currentQuery.switchMap { queryString ->
        newsRepository.searchNews(queryString).cachedIn(viewModelScope)
    }

    fun searchNews(query: String) {
        currentQuery.value = query
    }

//    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
//        if (response.isSuccessful) {
//            response.body()?.let { resultResponse ->
//                return Resource.Success(resultResponse)
//            }
//        }
//        return Resource.Error(response.message())
//    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    companion object {
        const val CURRENT_QUERY = ""
        const val DEFAULT_COUNTRY = "us"
    }
}