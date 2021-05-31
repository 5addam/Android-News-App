package com.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.newsapp.R
import com.newsapp.databinding.FragmentArticleBinding
import com.newsapp.ui.MainActivity
import com.newsapp.ui.NewsViewModel

class ArticleFragment : Fragment(R.layout.fragment_article) {
    lateinit var viewModel: NewsViewModel

    private val args by navArgs<ArticleFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentArticleBinding.bind(view)

        viewModel = (activity as MainActivity).viewModel

        val article = args.article

        binding.apply {
            webView.apply {
                webViewClient = WebViewClient()
                loadUrl(article.url)
            }
            fab.setOnClickListener {
                viewModel.saveArticle(article)
                Snackbar.make(view, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}