package com.test.novel.view.novelPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NovelFragmentViewModel @Inject constructor():ViewModel(){
    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _bookId = MutableStateFlow(0)
    val bookId = _bookId.asStateFlow()

    private val _author = MutableStateFlow("")
    val author = _author.asStateFlow()

    private val _content = MutableStateFlow(emptyList<String>())
    val content = _content.asStateFlow()
    
    private val _currentIndex = MutableStateFlow(0)
    val currentIndex = _currentIndex.asStateFlow()

    private val _showBar = MutableStateFlow(true)
    val showBar = _showBar.asStateFlow()

    private var hideBarJob: Job? = null

    fun init(bookId: Int) {
        _bookId.value = bookId
        viewModelScope.launch {
            withContext(Dispatchers.IO){

            }
        }
    }

    fun showOrHideBar() {
        if (_showBar.value) {
            _showBar.value = false
            hideBarJob?.cancel() // 取消已经存在的协程
        } else {
            _showBar.value = true
            hideBarJob?.cancel() // 取消已经存在的协程
            hideBarJob = viewModelScope.launch {
                delay(3000)
                _showBar.value = false
            }
        }
    }


}