package com.test.novel.di

import android.content.Context
import com.coder.vincent.sharp_retrofit.call_adapter.flow.FlowCallAdapterFactory
import com.test.novel.database.bookShelf.BookShelfDao
import com.test.novel.database.bookShelf.BookShelfDatabase
import com.test.novel.database.chapter.ChapterDao
import com.test.novel.database.chapter.ChapterDatabase
import com.test.novel.database.readHistory.SearchHistoryDao
import com.test.novel.database.readHistory.SearchHistoryDatabase
import com.test.novel.network.search.SearchService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val URL = "https://www.3bqg.cc/"
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun provideClient(@ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(3000L, TimeUnit.MILLISECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(interceptor = loggingInterceptor)
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val modifiedRequest = originalRequest.newBuilder()
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36 Edg/131.0.0.0")
                    .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                    .addHeader("Referer", "https://www.3bqg.cc/")
                    .addHeader("Cookie", "hm=b9af5c3381f232aaba36fbe0102f088d; hmt=1733053763")
                    .build()
                chain.proceed(modifiedRequest)
            }
            .build()
    }

    /**
     * 在目录中使用Hilt提供多个相对类型的实例对象，通过"@Named"注解进行区分
     * 也可以通过"@Qualifier"限定符定义新的注解进行区分*/

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(FlowCallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideSearchService(retrofit: Retrofit): SearchService {
        return retrofit.create(SearchService::class.java)
    }

    @Singleton
    @Provides
    fun provideBookShelfDao(@ApplicationContext context: Context): BookShelfDao {
        return BookShelfDatabase.getDatabase(context).bookShelfDao()
    }

    @Singleton
    @Provides
    fun provideSearchHistoryDao(@ApplicationContext context: Context): SearchHistoryDao {
        return SearchHistoryDatabase.getDatabase(context).searchHistoryDao()
    }

    @Singleton
    @Provides
    fun provideChapterDao(@ApplicationContext context: Context): ChapterDao {
        return ChapterDatabase.getDatabase(context).chapterDao()
    }


}