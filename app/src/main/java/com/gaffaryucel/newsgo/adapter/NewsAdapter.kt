package com.gaffaryucel.newsgo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.gaffaryucel.newsgo.R
import com.gaffaryucel.newsgo.databinding.RowNewsBinding
import com.gaffaryucel.newsgo.model.Article
import com.gaffaryucel.newsgo.model.SavedNewsModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class NewsAdapter(private val glide : RequestManager) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    var savedNewsTitleList = ArrayList<SavedNewsModel>()

    private val diffCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Article,
            newItem: Article
        ): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffCallback)

    var newsList : List<Article>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = newsList[position]
        var saved = false
        CoroutineScope(Dispatchers.Default).launch {
            savedNewsTitleList.forEach {
                if (item.title.equals(it.title)&&item.description.equals(it.description)){
                    println("equals")
                    saved = true
                    holder.binding.imageBorder.setImageResource(R.drawable.saved_corner)
                }
            }
        }
        holder.binding.saveImageView.setOnClickListener {
            if (saved){
                holder.binding.imageBorder.setImageResource(R.drawable.notsaved_corner)
                saved = false
                onClickDelete?.invoke(item)
            }else{
                holder.binding.imageBorder.setImageResource(R.drawable.saved_corner)
                saved = true
                onClickSave?.invoke(item)
            }
        }
        holder.itemView.setOnClickListener{
            onClick?.invoke(item)
        }
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    inner class ViewHolder(val binding: RowNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(news: Article) {
            glide.load(news.urlToImage)
                .into(binding.newsImageView)
            binding.apply {
                item = news
                executePendingBindings()
            }

        }
    }
    var onClickSave: ((Article) -> Unit)? = null
    var onClickDelete: ((Article) -> Unit)? = null
    var onClick: ((Article) -> Unit)? = null
}
