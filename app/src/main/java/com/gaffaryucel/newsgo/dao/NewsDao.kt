package com.gaffaryucel.newsgo.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gaffaryucel.newsgo.model.SavedNewsModel

@Dao
interface NewsDao {
    @Query("SELECT * FROM saved_news")
    fun getAllSavedNews(): LiveData<List<SavedNewsModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(news: SavedNewsModel)

    @Delete
    fun deleteNews(news: SavedNewsModel)
}
