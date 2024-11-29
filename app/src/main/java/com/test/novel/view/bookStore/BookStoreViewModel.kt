package com.test.novel.view.bookStore

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.novel.utils.WebCrawler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BookStoreViewModel @Inject constructor() : ViewModel() {

    private val _bookStoreState = MutableStateFlow(BookStoreState())
    val bookStoreState = _bookStoreState.asStateFlow()

    private val _bookStoreIntent = MutableSharedFlow<BookStoreIntent>()
    private val bookStoreIntent = _bookStoreIntent.asSharedFlow()

    init {
        viewModelScope.launch {
            bookStoreIntent.collect{
                processIntent(it)
            }
        }
    }

    fun setIntent(intent: BookStoreIntent){
        viewModelScope.launch {
            _bookStoreIntent.emit(intent)
        }
    }

    private fun processIntent(intent: BookStoreIntent){
        viewModelScope.launch {
        when(intent) {
            is BookStoreIntent.Refresh -> {

            }

            is BookStoreIntent.LoadMore -> {

            }

            is BookStoreIntent.Search -> {

            }

            is BookStoreIntent.InitData -> {
                Log.d("TAG", "processIntent: ${_bookStoreState.value}")
                withContext(Dispatchers.IO) {
                    val hot = WebCrawler.fetchBQGTop()
                    _bookStoreState.value =
                        _bookStoreState.value.copy(rank = hot)
                }
            }
        }
        }
    }

}