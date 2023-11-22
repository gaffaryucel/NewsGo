package com.gaffaryucel.newsgo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaffaryucel.newsgo.model.Article
import com.gaffaryucel.newsgo.model.NewsModel
import com.gaffaryucel.newsgo.model.SavedNewsModel
import com.gaffaryucel.newsgo.model.Source
import com.gaffaryucel.newsgo.repo.NewsDaoRepoInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsDetailsViewModel : ViewModel() {
    fun createNewsItem(
        author: String?,
        content: String?,
        description: String?,
        publishedAt: String?,
        source: String?,
        title: String?,
        url: String?,
        urlToImage: String?
    ): Article {
        return Article(
            author = author ?: "unknown",
            content = content ?: "unknown",
            description = description ?: "unknown",
            publishedAt = publishedAt ?: "unknown",
            source = Source("unknown",source ?: "unknown"),
            title = title ?: "unknown",
            url = url ?: "unknown",
            urlToImage = urlToImage ?: "unknown",
        )
    }
}