package com.test.novel.view.searchPage

import com.test.novel.database.readHistory.SearchHistory
import com.test.novel.network.search.SearchResult

data class SearchState(
    val searchHistory:List<SearchHistory> = listOf(),

    val searchResult:List<SearchResult> = listOf()
)

sealed class SearchIntent{
    data class Search(val keyword:String):SearchIntent()

    data object ClearHistory:SearchIntent()
}