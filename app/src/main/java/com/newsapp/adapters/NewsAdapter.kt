package com.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.newsapp.R
import com.newsapp.data.Article
import com.newsapp.databinding.ItemArticleBinding
import java.util.*

class NewsAdapter(private val listener: OnItemClickListener) :
    PagingDataAdapter<Article, NewsAdapter.ArticleViewHolder>(ARTICLE_COMPARATOR) {

//    val differ = AsyncListDiffer(this, ARTICLE_COMPARATOR)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(
            LayoutInflater.from(
                parent.context
            ),
            parent,
            false
        )
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null)
            holder.bind(currentItem)
    }

//    override fun getItemCount(): Int {
//        return differ.currentList.size
//    }

    inner class ArticleViewHolder(private val binding: ItemArticleBinding) :
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
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .placeholder(R.drawable.ic_image)
                    .into(ivArticleImage)

                tvSource.text = article.source.name
                tvTitle.text = article.title
                tvDescription.text = article.description
                val publishAt = article.publishedAt.split("T")
                tvPublishedAt.text = publishAt[0]
                tvPublishTime.text = publishAt[1].removeSuffix("Z")



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