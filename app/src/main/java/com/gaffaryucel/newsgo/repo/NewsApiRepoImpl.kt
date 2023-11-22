package com.gaffaryucel.newsgo.repo

import com.gaffaryucel.newsgo.model.NewsModel
import com.gaffaryucel.newsgo.service.NewsApiService
import io.reactivex.Single
import javax.inject.Inject

class NewsApiRepoImpl @Inject constructor(private val newsService: NewsApiService) : NewsApiRepoInterface {
    override fun searchNews(query: String, fromDate: String, sortBy: String, apiKey: String): Single<NewsModel> {
        return newsService.searchNews(query = query, fromDate = fromDate, sortBy = sortBy, apiKey = apiKey)
    }

    override fun search(
        query: String,
        sortBy: String,
        apiKey: String
    ): Single<NewsModel> {
        return newsService.search(query = query, sortBy = sortBy, apiKey = apiKey)
    }

    override fun topHeadlinesBySource(source: String, apiKey: String): Single<NewsModel> {
        return newsService.topHeadlinesBySource(source = source, apiKey = apiKey)
    }

    override fun breakingNewsByCountry(country: String, apiKey: String): Single<NewsModel> {
        return newsService.breakingNewsByCountry(country = country, apiKey = apiKey)
    }

    override fun filteredSearch(
        query: String,
        fromDate: String,
        sortBy: String,
        source: String,
        apiKey: String
    ): Single<NewsModel> {
        return newsService.filteredSearch(query, fromDate, sortBy, source, apiKey)
    }

}