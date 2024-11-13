package com.test.novel.view.novelPage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.novel.model.BookBrief
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NovelFragmentViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(BookState())
    val state = _state.asStateFlow()

    private val _intent = MutableSharedFlow<BookIntent>()
    private val intent = _intent.asSharedFlow()

    private var hideBarJob: Job? = null

    init {
        viewModelScope.launch {
            intent.collect {
                processIntent(it)
            }
        }
    }

    fun sendIntent(intent: BookIntent) {
        viewModelScope.launch {
            _intent.emit(intent)
        }
    }

    private fun processIntent(intent: BookIntent) {
        viewModelScope.launch {
            when (intent) {
                BookIntent.ShowBar -> showOrHideBar()

                is BookIntent.Init -> {
                    init(intent.bookBrief)
                }

                is BookIntent.SetContent -> {
                    _state.value = _state.value.copy(pages = intent.pages)
                }

                is BookIntent.AddPage -> {
                    println(intent)
                    val nowPage = _state.value.pages[intent.insertIndex].text.substring(0,intent.splitTextIndex)
                    val nextPage = _state.value.pages[intent.insertIndex].text.substring(intent.splitTextIndex)
                    val newList = _state.value.pages.toMutableList()
                    newList[intent.insertIndex] = newList[intent.insertIndex].copy(text = nowPage,load = true)
                    newList.add(intent.insertIndex + 1,PageState(text = nextPage,load = false))
                    _state.value = _state.value.copy(currentIndex = intent.insertIndex, pages = newList)
                    Log.d("TAG", "processIntent: ${_state.value}")
                }

                is BookIntent.SetCurrentIndex -> {
                    _state.value = _state.value.copy(currentIndex = intent.index)
                }
            }
        }
    }


        private fun init(bookBrief: BookBrief) {
            _state.value = _state.value.copy(
                bookId = bookBrief.bookId,
                title = bookBrief.title,
                author = bookBrief.author,
                brief = bookBrief.brief
            )
            viewModelScope.launch {
                withContext(Dispatchers.IO) {

                }
            }
        }

        private fun showOrHideBar() {
            if (_state.value.showBar) {
                _state.value = _state.value.copy(showBar = false)
                hideBarJob?.cancel() // 取消已经存在的协程
            } else {
                _state.value = _state.value.copy(showBar = true)
                hideBarJob?.cancel() // 取消已经存在的协程
                hideBarJob = viewModelScope.launch {
                    delay(3000)
                    _state.value = _state.value.copy(showBar = false)
                }
            }
        }


}


