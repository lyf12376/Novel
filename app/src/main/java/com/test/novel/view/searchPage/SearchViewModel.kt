package com.test.novel.view.searchPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.novel.database.readHistory.SearchHistory
import com.test.novel.database.readHistory.SearchHistoryDao
import com.test.novel.network.search.SearchService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchService: SearchService,
    private val searchHistoryDao: SearchHistoryDao
) : ViewModel() {

    private val _searchState = MutableStateFlow(SearchState())
    val searchState = _searchState.asStateFlow()

    private val _searchIntent = MutableSharedFlow<SearchIntent>()
    private val searchIntent = _searchIntent.asSharedFlow()

    init {
        viewModelScope.launch {
            searchHistoryDao.getAll().collect{
                _searchState.value = _searchState.value.copy(searchHistory = it)
            }
        }
        viewModelScope.launch {
            searchIntent.collect {
                processIntent(it)
            }
        }
    }

    fun sendIntent(intent: SearchIntent) {
        viewModelScope.launch {
            _searchIntent.emit(intent)
        }
    }

    private fun processIntent(intent: SearchIntent) {
        viewModelScope.launch {
            when (intent) {
                is SearchIntent.Search -> {
                    withContext(Dispatchers.IO) {
                        val keyword = URLEncoder.encode(intent.keyword, "UTF-8")
                        val searchResult = searchService.search(keyword)
                        println(searchResult)
                        _searchState.value = _searchState.value.copy(searchResult = searchResult)
                        searchHistoryDao.insert(SearchHistory(0,intent.keyword))
                    }
                }

                is SearchIntent.ClearHistory -> {
                    searchHistoryDao.deleteAll()
                }
            }
        }
    }
}