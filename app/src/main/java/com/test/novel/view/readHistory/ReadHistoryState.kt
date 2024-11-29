package com.test.novel.view.readHistory

import com.test.novel.model.ReadHistory

data class ReadHistoryState (

    val readHistoryToday: List<ReadHistory> = emptyList(),

    val readHistoryYesterday: List<ReadHistory> = emptyList(),

    val readHistoryEarlier: List<ReadHistory> = emptyList(),
)

sealed class ReadHistoryIntent {
    data class AddToBookShelf(val bookId:Int) : ReadHistoryIntent()
}