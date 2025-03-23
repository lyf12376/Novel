package com.test.novel.view.newNovelPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.novel.database.chapter.Chapter
import com.test.novel.database.chapter.ChapterDao
import com.test.novel.database.readHistory.ReadHistoryDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadViewModel @Inject constructor(
    private val chapterDao: ChapterDao,
    private val readHistoryDao: ReadHistoryDao
) : ViewModel() {

    private val _showBar = MutableStateFlow(false)
    val showBar = _showBar.asStateFlow()

    private val _chapters = MutableStateFlow(emptyList<Chapter>())
    val chapters = _chapters.asStateFlow()

    fun showOrHideBar(){
        _showBar.value = !_showBar.value
    }

    fun loadChapter(bookId:Int){
        viewModelScope.launch {
            chapterDao.getChapters(bookId).collect{
                _chapters.value = it
            }
        }
    }
}