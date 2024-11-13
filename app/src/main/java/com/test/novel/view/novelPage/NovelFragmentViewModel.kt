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
                    val pagesCount = _state.value.pageCount
                    intent.pages.forEach{
                        pageState ->
                        pagesCount.replaceOrAddUntilToIndex(pageState.chapterIndex,1)
                    }
                    _state.value = _state.value.copy(pages = intent.pages)
                }

                is BookIntent.AddPages -> {
                    val pagesCount = _state.value.pageCount
                    val newList = _state.value.pages.toMutableList()
                    var o = 0
                    _state.value.pageCount.run {
                        forEachIndexed { index, i ->
                            println("$index $i")
                            if (index < intent.pageState[0].chapterIndex) {
                                o += i
                            } else {
                                return@run
                            }
                        }
                    }
                    pagesCount.replaceOrAddUntilToIndex(intent.pageState[0].chapterIndex,intent.pageState.size)
                    newList[o] = intent.pageState[0]
                    newList.addAll(o+1, intent.pageState.subList(1, intent.pageState.size))
                    Log.e("TAG", "processIntent: $newList", )
                    _state.value = _state.value.copy(pages = newList, pageCount = pagesCount)
                    Log.d("TAG", "processIntent: ${_state.value.pages.size}")
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

fun MutableList<Int>.replaceOrAddUntilToIndex(index: Int, element: Int) {
    if (index < size) {
        this[index] = element
    } else {
        while (index > size) {
            add(0)
        }
        add(element)
    }
}


