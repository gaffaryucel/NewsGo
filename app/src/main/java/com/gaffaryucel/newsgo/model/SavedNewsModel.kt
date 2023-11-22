package com.gaffaryucel.newsgo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "saved_news")
data class SavedNewsModel(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "published_at") val publishedAt: String,
    @ColumnInfo(name = "source") val source: String,
    @ColumnInfo(name = "title")val title: String,
    @ColumnInfo(name = "url")  val url: String,
    @ColumnInfo(name = "urlToImage") val urlToImage: String
)
