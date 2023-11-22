package com.gaffaryucel.newsgo.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaffaryucel.newsgo.model.Article
import com.gaffaryucel.newsgo.model.NewsModel
import com.gaffaryucel.newsgo.model.SavedNewsModel
import com.gaffaryucel.newsgo.repo.NewsApiRepoImpl
import com.gaffaryucel.newsgo.repo.NewsApiRepoInterface
import com.gaffaryucel.newsgo.repo.NewsDaoRepoInterface
import com.gaffaryucel.newsgo.util.Resource
import com.gaffaryucel.newsgo.util.Util.API_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo : NewsApiRepoInterface,
    private val daoRepo : NewsDaoRepoInterface
): ViewModel() {

    val savedNews = daoRepo.getAllSavedNews()

    private val disposable = CompositeDisposable()

    private val _breakingNews = MutableLiveData<NewsModel>()
    val breakingNews : LiveData<NewsModel>
        get() = _breakingNews

    private val _sportNews = MutableLiveData<NewsModel>()
    val sportNews : LiveData<NewsModel>
        get() = _sportNews

    private val _technologyNews = MutableLiveData<NewsModel>()
    val technologyNews : LiveData<NewsModel>
        get() = _technologyNews

    private val _educationNews = MutableLiveData<NewsModel>()
    val educationNews: LiveData<NewsModel>
        get() = _educationNews

    private val _healthNews = MutableLiveData<NewsModel>()
    val healthNews: LiveData<NewsModel>
        get() = _healthNews

    private val _scienceNews = MutableLiveData<NewsModel>()
    val scienceNews: LiveData<NewsModel>
        get() = _scienceNews

    private val _searchResult = MutableLiveData<NewsModel>()
    val searchResult : LiveData<NewsModel>
        get() = _searchResult

    private val _loadingMessage = MutableLiveData<Resource<String>>()
    val loadingMessage : LiveData<Resource<String>>
        get() = _loadingMessage

     init {
         getBreakingNews()
     }
    fun getNews(category : String) = viewModelScope.launch{
        if (!category.equals("Breaking News")){
            _loadingMessage.value = Resource.loading(null)
            val single = repo.searchNews(category,"2023-30-10","popularity","6d9dac5e46ec42a581bcc949c414f6e5")
            disposable.add(
                single
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response ->
                        response?.let {
                            when (category) {
                                "Sport" -> {
                                    _sportNews.value = it
                                }
                                "Technology" -> {
                                    _technologyNews.value = it
                                }
                                "Education" -> {
                                    _educationNews.value = it
                                }
                                "Health" -> {
                                    _healthNews.value = it
                                }
                                "Science" -> {
                                    _scienceNews.value = it
                                }
                                else -> {
                                    _searchResult.value = it
                                }
                            }
                            _loadingMessage.value = Resource.success(null)
                        }
                    }, { error ->
                        error.localizedMessage?.let {
                            _loadingMessage.value = Resource.error(it,null)
                        }
                    })
            )
        }
    }
    private fun getBreakingNews() = viewModelScope.launch{
        _loadingMessage.value = Resource.loading(null)
        val single = repo.breakingNewsByCountry("us","6d9dac5e46ec42a581bcc949c414f6e5")
        disposable.add(
            single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    response?.let {
                        _breakingNews.value = it
                        _loadingMessage.value = Resource.success(null)
                    }
                }, { error ->
                    error.localizedMessage?.let {
                        _loadingMessage.value = Resource.error(it,null)
                    }
                })
        )
    }

    fun searchNews(query : String) = viewModelScope.launch{
        _loadingMessage.value = Resource.loading(null)
        //https://newsapi.org/v2/everything?q=Apple&from=2023-19-01&sortBy=popularity&apiKey=6d9dac5e46ec42a581bcc949c414f6e5
        val single = repo.search(query,"popularity ","6d9dac5e46ec42a581bcc949c414f6e5")
        disposable.add(
            single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    response?.let {
                        _searchResult.value = it
                        _loadingMessage.value = Resource.success(null)
                    }
                }, { error ->
                    error.localizedMessage?.let {
                        _loadingMessage.value = Resource.error(it,null)
                    }
                })
        )
    }

    fun filter(query : String,fromDate : String,sortBy : String,source : String) = viewModelScope.launch{
        _loadingMessage.value = Resource.loading(null)
        //https://newsapi.org/v2/everything?q=Apple&from=2023-19-01&sortBy=popularity&apiKey=6d9dac5e46ec42a581bcc949c414f6e5
        var newSource = ""
        var newsortBy = ""
        var newFromDate = ""
        if (!source.equals("select")) {
            newSource = source
        }
        if (!sortBy.equals("select")){
            newsortBy = sortBy
        }
        if (!fromDate.equals("Select From Date : ")){
            newFromDate = fromDate
        }
        val single = repo.filteredSearch(
            query = query,
            fromDate = fromDate,
            sortBy  = newsortBy,
            source = newSource,
            apiKey = API_KEY
        )
        disposable.add(
            single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    response?.let {
                        _searchResult.value = it
                        _loadingMessage.value = Resource.success(null)
                    }
                }, { error ->
                    error.localizedMessage?.let {
                        _loadingMessage.value = Resource.error(it,null)
                    }
                })
        )
    }


    fun saveNews(savedNewsModel : SavedNewsModel) = viewModelScope.launch{
        try {
            CoroutineScope(Dispatchers.IO).launch{
                daoRepo.insertNews(savedNewsModel)
            }
        }catch (e : Exception){
            println("error321"+ e.localizedMessage)
        }
    }
    fun deleteNews(savedNewsModel : SavedNewsModel) = viewModelScope.launch{
        try {
            CoroutineScope(Dispatchers.IO).launch{
                daoRepo.deleteNews(savedNewsModel)
            }
        }catch (e : Exception){
            println("error321"+ e.localizedMessage)
        }
    }

    fun convertArticleToSavedNewsModel(article: Article): SavedNewsModel {
        val sourceString = "${article.source.id} - ${article.source.name}"

        return SavedNewsModel(
            id = article.title+article.url,
            author = article.author ?: "unknown 1",
            content = article.content ?: "unknown 1",
            description = article.description ?: "unknown 1",
            publishedAt = article.publishedAt ?: "unknown 1",
            source = sourceString,
            title = article.title ?: "unknown 1",
            url = article.url ?: "unknown 1",
            urlToImage = article.urlToImage ?: "unknown 1"
        )
    }

}