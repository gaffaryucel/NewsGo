package com.gaffaryucel.newsgo.repo

import androidx.lifecycle.LiveData
import com.gaffaryucel.newsgo.model.SavedNewsModel

interface NewsDaoRepoInterface {
    fun getAllSavedNews(): LiveData<List<SavedNewsModel>>
    suspend fun insertNews(news: SavedNewsModel)
    suspend fun deleteNews(news: SavedNewsModel)
}