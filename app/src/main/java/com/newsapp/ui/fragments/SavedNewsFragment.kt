package com.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.newsapp.R
import com.newsapp.adapters.SavedNewsAdapter
import com.newsapp.data.Article
import com.newsapp.databinding.FragmentSavedNewsBinding
import com.newsapp.ui.MainActivity
import com.newsapp.ui.NewsViewModel

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news),
    SavedNewsAdapter.OnItemClickListener {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: SavedNewsAdapter


    private var _binding: FragmentSavedNewsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSavedNewsBinding.bind(view)

        viewModel = (activity as MainActivity).viewModel
        initRecyclerView()

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val article = newsAdapter.getArticle(position)
//                val article = newsAdapter.differ.currentList[position]
                if (article != null) {
                    viewModel.deleteArticle(article)
                    Snackbar.make(view, "Successfully deleted article", Snackbar.LENGTH_LONG)
                        .apply {
                            setAction("Undo") {
                                viewModel.saveArticle(article)
                            }
                            show()
                        }
                }
            }

        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }
        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer { articles ->
            newsAdapter.differ.submitList(articles)
        })
    }

    private fun initRecyclerView() {
        newsAdapter = SavedNewsAdapter(this)
        binding.apply {
            rvSavedNews.apply {
                adapter = newsAdapter
                layoutManager = LinearLayoutManager(activity)
            }

        }

    }

    override fun onItemClick(article: Article) {
        val action =
            SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(article)
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}