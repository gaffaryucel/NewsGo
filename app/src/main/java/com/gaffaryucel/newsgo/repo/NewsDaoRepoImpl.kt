package com.gaffaryucel.newsgo.repo

import androidx.lifecycle.LiveData
import com.gaffaryucel.newsgo.dao.NewsDao
import com.gaffaryucel.newsgo.model.SavedNewsModel
import javax.inject.Inject

class NewsDaoRepoImpl @Inject constructor(
    private val newsDao : NewsDao
) : NewsDaoRepoInterface {
    override fun getAllSavedNews(): LiveData<List<SavedNewsModel>> {
        return newsDao.getAllSavedNews()
    }

    override suspend fun insertNews(news: SavedNewsModel) {
        newsDao.insertNews(news)
    }

    override suspend fun deleteNews(news: SavedNewsModel) {
        newsDao.deleteNews(news)
    }

}