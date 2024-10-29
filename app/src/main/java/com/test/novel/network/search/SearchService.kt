package com.test.novel.network.search

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchService {
    @GET("https://www.3bqg.cc/user/search.html")
    suspend fun searchNovel(@Query("q") keyWord: String):List<SearchResult>

}