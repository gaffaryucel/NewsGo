package com.gaffaryucel.newsgo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gaffaryucel.newsgo.dao.NewsDao
import com.gaffaryucel.newsgo.model.SavedNewsModel

@Database(entities = [SavedNewsModel::class], version = 1, exportSchema = false)
abstract class NewsDatabase  : RoomDatabase(){
    abstract fun newsDao(): NewsDao
}