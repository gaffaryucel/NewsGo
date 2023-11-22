package com.gaffaryucel.newsgo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.gaffaryucel.newsgo.R
import com.gaffaryucel.newsgo.databinding.RowNews2Binding
import com.gaffaryucel.newsgo.databinding.RowNewsBinding
import com.gaffaryucel.newsgo.model.Article
import com.gaffaryucel.newsgo.model.SavedNewsModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SavedNewsAdapter(private val glide: RequestManager) :
    RecyclerView.Adapter<SavedNewsAdapter.ViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<SavedNewsModel>() {
        override fun areItemsTheSame(oldItem: SavedNewsModel, newItem: SavedNewsModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: SavedNewsModel,
            newItem: SavedNewsModel
        ): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffCallback)

    var newsList : List<SavedNewsModel>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowNews2Binding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = newsList[position]
        var saved = true

        holder.itemView.setOnClickListener {
            onClick?.invoke(item)
        }
/*
            if (saved){
                       }else{
                holder.binding.saveIvIcon.setImageResource(R.drawable.saved_news_icon)
                saved = true
                onClickSave?.invoke(item)
            }

 */
        holder.binding.saveIvIcon.setOnClickListener {
            holder.binding.saveIvIcon.setImageResource(R.drawable.save_news_icon)
            saved = false
            onClickDelete?.invoke(item)
        }
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    inner class ViewHolder(val binding: RowNews2Binding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(news: SavedNewsModel) {
            glide.load(news.urlToImage)
                .into(binding.newImageView)
            binding.apply {
                item = news
                executePendingBindings()
            }
        }
    }

    var onClickSave: ((SavedNewsModel) -> Unit)? = null
    var onClickDelete: ((SavedNewsModel) -> Unit)? = null
    var onClick: ((SavedNewsModel) -> Unit)? = null
}
