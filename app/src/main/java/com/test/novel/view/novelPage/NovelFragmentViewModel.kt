package com.test.novel.view.novelPage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.novel.database.chapter.ChapterDao
import com.test.novel.database.readHistory.ReadHistory
import com.test.novel.database.readHistory.ReadHistoryDao
import com.test.novel.model.BookBrief
import com.test.novel.utils.WebCrawler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NovelFragmentViewModel @Inject constructor(
    private val chapterDao: ChapterDao,
    private val readHistoryDao: ReadHistoryDao
) : ViewModel() {

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
                    val pagesCount = _state.value.pageCount.toMutableList()
                    intent.pages.forEach { pageState ->
                        ensurePageCountSize(pagesCount, pageState.chapterIndex)
                        pagesCount[pageState.chapterIndex - 1] = 1
                    }
                    _state.value = _state.value.copy(pages = intent.pages, pageCount = pagesCount)
                }

                is BookIntent.AddPages -> {
                    if (intent.pageState.isEmpty()) return@launch

                    val chapterIndex = intent.pageState[0].chapterIndex
                    val pagesCount = _state.value.pageCount.toMutableList()
                    ensurePageCountSize(pagesCount, chapterIndex)

                    // Update the page count for this chapter
                    pagesCount[chapterIndex - 1] = intent.pageState.size

                    // Find the offset for this chapter's first page
                    var offset = 0
                    for (i in 0 until chapterIndex - 1) {
                        offset += pagesCount.getOrElse(i) { 0 }
                    }

                    // Create new pages list with updated/added pages
                    val newList = _state.value.pages.toMutableList()

                    // Ensure the list has enough capacity
                    while (newList.size < offset + intent.pageState.size) {
                        newList.add(PageState(chapterIndex = 0, title = "", text = "", load = false))
                    }

                    // Replace or add the new pages
                    for (i in intent.pageState.indices) {
                        if (offset + i < newList.size) {
                            newList[offset + i] = intent.pageState[i]
                        } else {
                            newList.add(intent.pageState[i])
                        }
                    }

                    _state.value = _state.value.copy(pages = newList, pageCount = pagesCount)
                }

                is BookIntent.SetCurrentIndex -> {
                    _state.value = _state.value.copy(currentIndex = intent.index)
                }

                is BookIntent.GetContentFromLocal -> {
                    getLocalContent(intent.bookId)
                }
            }
        }
    }

    private fun ensurePageCountSize(pageCount: MutableList<Int>, chapterIndex: Int) {
        while (pageCount.size < chapterIndex) {
            pageCount.add(0)
        }
    }

    private fun init(bookBrief: BookBrief) {
        _state.value = _state.value.copy(
            bookId = bookBrief.bookId,
            title = bookBrief.title,
            author = bookBrief.author,
            brief = bookBrief.brief,
            coverUrl = bookBrief.coverUrl,
            type = bookBrief.type
        )

        sendIntent(BookIntent.GetContentFromLocal(bookBrief.bookId))
    }

    private fun showOrHideBar() {
        if (_state.value.currentIndex == 0)
            return
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

    private suspend fun getLocalContent(bookId: Int) {
        withContext(Dispatchers.IO) {
            val chapters = chapterDao.getChapters(bookId)
            chapters.collect { chapterList ->
                val pages = mutableListOf<PageState>()
                chapterList.forEachIndexed { index, chapter ->
                    pages.add(
                        PageState(
                            title = chapter.title,
                            showTitle = true,
                            chapterIndex = index + 1,
                            text = chapter.content,
                            load = false
                        )
                    )
                }
                sendIntent(BookIntent.SetContent(pages))
            }
        }
    }
}


