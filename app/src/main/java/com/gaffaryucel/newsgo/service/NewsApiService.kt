package com.gaffaryucel.newsgo.service

import com.gaffaryucel.newsgo.model.NewsModel
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("/v2/everything")
    fun searchNews(
        @Query("q") query: String,
        @Query("from") fromDate: String,
        @Query("sortBy") sortBy: String,
        @Query("apiKey") apiKey: String
    ): Single<NewsModel>

    @GET("/v2/everything")
    fun search(
        @Query("q") query: String,
        @Query("sortBy") sortBy: String,
        @Query("apiKey") apiKey: String
    ): Single<NewsModel>

    @GET("/v2/top-headlines")
    fun topHeadlinesBySource(
        @Query("sources") source: String,
        @Query("apiKey") apiKey: String
    ): Single<NewsModel>

    @GET("/v2/top-headlines")
    fun breakingNewsByCountry(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String
    ): Single<NewsModel>

    //https://newsapi.org/v2/top-headlines?country=us&apiKey=6d9dac5e46ec42a581bcc949c414f6e5

    @GET("/v2/everything")
    fun filteredSearch(
        @Query("q") query: String,
        @Query("from") fromDate: String,
        @Query("sortBy") sortBy: String,
        @Query("sources") source: String,
        @Query("apiKey") apiKey: String
    ): Single<NewsModel>
}
