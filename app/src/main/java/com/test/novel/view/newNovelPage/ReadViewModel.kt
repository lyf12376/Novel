package com.test.novel.view.newNovelPage

import androidx.lifecycle.ViewModel
import com.test.novel.database.chapter.ChapterDao
import com.test.novel.database.readHistory.ReadHistoryDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReadViewModel @Inject constructor(
    private val chapterDao: ChapterDao,
    private val readHistoryDao: ReadHistoryDao
) : ViewModel() {

}