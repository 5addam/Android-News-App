package com.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsapp.R
import com.newsapp.adapters.NewsAdapter
import com.newsapp.data.Article
import com.newsapp.databinding.FragmentSearchNewsBinding
import com.newsapp.ui.MainActivity
import com.newsapp.ui.NewsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news),
    NewsAdapter.OnItemClickListener {
    private lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter


    private var _binding: FragmentSearchNewsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchNewsBinding.bind(view)

        viewModel = (activity as MainActivity).viewModel

        initViewData()

        viewModel.articles.observe(viewLifecycleOwner, Observer {
            newsAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        })

    }

    private fun initViewData() {
        newsAdapter = NewsAdapter(this)
        binding.apply {
            rvSearchNews.apply {
                adapter = newsAdapter
                layoutManager = LinearLayoutManager(activity)
            }
            var job: Job? = null
            etSearch.addTextChangedListener { editable ->
                job?.cancel()
                job = MainScope().launch {
                    delay(500L)
                    editable?.let {
                        if (editable.toString().isNotEmpty()) {
                            viewModel.searchNews(editable.toString())
                        }
                    }

                }
            }
            btnRetry.setOnClickListener {
                newsAdapter.retry()
            }
        }
        newsAdapter.addLoadStateListener { loadState ->
            binding.apply {
                paginationProgressBar.isVisible = loadState.source.refresh is LoadState.Loading
                rvSearchNews.isVisible = loadState.source.refresh is LoadState.NotLoading
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                txtError.isVisible = loadState.source.refresh is LoadState.Error

                //empty view
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    newsAdapter.itemCount < 1
                ) {
                    rvSearchNews.isVisible = false
                    txtViewEmpty.isVisible = true
                } else
                    txtViewEmpty.isVisible = false
            }

        }
    }


    companion object {
        private const val TAG = "SearchNewsFragment"
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(article: Article) {
        val action =
            SearchNewsFragmentDirections.actionSearchNewsFragmentToArticleFragment(article)
        findNavController().navigate(action)
    }


}