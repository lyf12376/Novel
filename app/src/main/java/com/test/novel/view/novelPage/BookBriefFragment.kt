package com.test.novel.view.novelPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.test.novel.R
import com.test.novel.databinding.FragmentBookBriefBinding
import com.test.novel.model.BookBrief
import kotlinx.serialization.json.Json

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"


/**
 * A simple [Fragment] subclass.
 * Use the [BookBriefFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookBriefFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_book_brief, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bookBrief = Json.decodeFromString(BookBrief.serializer(), param1!!)
        val binding = FragmentBookBriefBinding.bind(view)
        binding.bookName.text = bookBrief.title
        binding.author.text = bookBrief.author
        binding.type.text = bookBrief.type.joinToString("    ")
        Glide.with(this).load(bookBrief.coverUrl).into(binding.bookCover)
        binding.introduction.text = bookBrief.brief
    }

    companion object {
        @JvmStatic
        fun newInstance(bookBrief: BookBrief) =
            BookBriefFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, Json.encodeToString(BookBrief.serializer(), bookBrief))
                }
            }
    }
}