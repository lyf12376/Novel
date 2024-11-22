package com.test.novel.view.bookShelfPage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.novel.database.bookShelf.BookInShelf
import com.test.novel.database.bookShelf.BookShelfDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookShelfViewModel @Inject constructor(
    private val bookShelfDao: BookShelfDao
) : ViewModel() {

    private val _state = MutableStateFlow(BookShelfState())
    val state = _state.asStateFlow()

    private val _intent = MutableSharedFlow<BookShelfIntent>()
    private val intent = _intent.asSharedFlow()

    init {
        viewModelScope.launch {
            bookShelfDao.getAllBooks().collect { books ->
                _state.value = _state.value.copy(bookInShelfList = books)
            }
        }

        viewModelScope.launch {
            intent.collect { intent ->
                processIntent(intent)
            }
        }
    }


    private fun processIntent(intent: BookShelfIntent) {
        viewModelScope.launch {
            when (intent) {

                BookShelfIntent.EnterDeleteMode -> {
                    _state.value = _state.value.copy(isDeleteMode = true)
                    Log.d("TAG", "processIntent: ${_state.value}")
                }

                is BookShelfIntent.OpenBook -> {
                    _state.value = _state.value.copy(openId = intent.id)
                }

                is BookShelfIntent.SelectBook -> {
                    _state.value =
                        _state.value.copy(deleteBooks = _state.value.deleteBooks + intent.id)
                    println(_state.value.deleteBooks)
                }

                is BookShelfIntent.UnSelectBook -> {
                    _state.value =
                        _state.value.copy(deleteBooks = _state.value.deleteBooks - intent.id)
                }

                BookShelfIntent.DeleteBooks -> {
                    try {
                        println(_state.value.deleteBooks)
                        bookShelfDao.deleteBooks(_state.value.deleteBooks)
                        _state.value =
                            _state.value.copy(isDeleteMode = false, deleteBooks = listOf())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                BookShelfIntent.Cancel -> {
                    _state.value = _state.value.copy(isDeleteMode = false, deleteBooks = listOf())
                }
            }
        }
    }

    fun sendIntent(intent: BookShelfIntent) {
        viewModelScope.launch {
            _intent.emit(intent)
        }
    }

}