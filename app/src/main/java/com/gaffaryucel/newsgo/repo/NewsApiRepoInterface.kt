package com.gaffaryucel.newsgo.repo

import com.gaffaryucel.newsgo.model.NewsModel
import io.reactivex.Single

interface NewsApiRepoInterface {
    fun searchNews(query: String, fromDate: String, sortBy: String, apiKey: String): Single<NewsModel>
    fun search(query: String, sortBy: String, apiKey: String): Single<NewsModel>
    fun topHeadlinesBySource(source: String, apiKey: String): Single<NewsModel>
    fun breakingNewsByCountry(country: String, apiKey: String): Single<NewsModel>
    fun filteredSearch(
        query: String,
        fromDate: String,
        sortBy: String,
        source: String,
        apiKey: String,
    ): Single<NewsModel>
}