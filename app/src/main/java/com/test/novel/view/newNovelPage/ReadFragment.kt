package com.test.novel.view.newNovelPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.novel.R
import com.test.novel.model.BookBrief
import com.test.novel.view.novelPage.NovelFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json

private const val BOOK_BRIEF = "bookBrief"
@AndroidEntryPoint
class ReadFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_read, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(bookBrief: BookBrief) =
            ReadFragment().apply {
                arguments = Bundle().apply {
                    putString(BOOK_BRIEF, Json.encodeToString(BookBrief.serializer(), bookBrief))
                }
            }
    }
}