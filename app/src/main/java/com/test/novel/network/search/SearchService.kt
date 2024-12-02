package com.test.novel.network.search

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchService {
    @GET("user/search.html")
    suspend fun search(@Query("q", encoded = true) keyWord: String):List<SearchResult>

}