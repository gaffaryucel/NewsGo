package com.gaffaryucel.newsgo.di

import android.content.Context
import android.widget.ImageView
import androidx.room.Room
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.gaffaryucel.newsgo.R
import com.gaffaryucel.newsgo.dao.NewsDao
import com.gaffaryucel.newsgo.database.NewsDatabase
import com.gaffaryucel.newsgo.repo.FirebaseRepoImpl
import com.gaffaryucel.newsgo.repo.FirebaseRepoInterFace
import com.gaffaryucel.newsgo.repo.NewsApiRepoImpl
import com.gaffaryucel.newsgo.repo.NewsApiRepoInterface
import com.gaffaryucel.newsgo.repo.NewsDaoRepoImpl
import com.gaffaryucel.newsgo.repo.NewsDaoRepoInterface
import com.gaffaryucel.newsgo.service.NewsApiService
import com.gaffaryucel.newsgo.util.Util
import com.gaffaryucel.newsgo.util.Util.BASE_URL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNewsApiService(): NewsApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(NewsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsApiRepo(apiService: NewsApiService): NewsApiRepoInterface = NewsApiRepoImpl(apiService)

    @Provides
    @Singleton
    fun provideGlide(@ApplicationContext context: Context) : RequestManager  {
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        return Glide.with(context)
            .setDefaultRequestOptions(
                RequestOptions().placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.error)
                    .placeholder(circularProgressDrawable)
            )
    }

    @Singleton
    @Provides
    fun provideNewsDatabase(@ApplicationContext context: Context): NewsDatabase {
        return Room.databaseBuilder(
            context,
            NewsDatabase::class.java,
            "news_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideNewsDao(database: NewsDatabase): NewsDao {
        return database.newsDao()
    }

    @Singleton
    @Provides
    fun provideNewsRepository(newsDao: NewsDao): NewsDaoRepoInterface {
        return NewsDaoRepoImpl(newsDao)
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideStorage() = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFireStore() = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseRepo(auth: FirebaseAuth,firestore: FirebaseFirestore): FirebaseRepoInterFace {
        return FirebaseRepoImpl(auth,firestore)
    }
}
