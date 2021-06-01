package com.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.newsapp.data.Article
import com.newsapp.databinding.ItemArticleBinding


class SearchNewsAdapter(private val listener: OnItemClickListener) :
    PagingDataAdapter<Article, SearchNewsAdapter.SearchArticleViewHolder>(ARTICLE_COMPARATOR) {

//    val differ = AsyncListDiffer(this, ARTICLE_COMPARATOR)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchArticleViewHolder {
        val binding = ItemArticleBinding.inflate(
            LayoutInflater.from(
                parent.context
            ),
            parent,
            false
        )
        return SearchArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchArticleViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null)
            holder.bind(currentItem)
    }

    inner class SearchArticleViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    val item = getItem(pos)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }

            }
        }

        fun bind(article: Article) {
            binding.apply {
                Glide.with(itemView)
                    .load(article.urlToImage)
                    .into(ivArticleImage)

                tvSource.text = article.source.name
                tvTitle.text = article.title
                tvDescription.text = article.description
                tvPublishedAt.text = article.publishedAt


            }

        }

    }

    interface OnItemClickListener {
        fun onItemClick(article: Article)
    }

    companion object {
        private val ARTICLE_COMPARATOR = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.url == newItem.url

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem == newItem

        }
    }

}