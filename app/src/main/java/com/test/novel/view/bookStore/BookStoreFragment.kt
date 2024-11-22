package com.test.novel.view.bookStore

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginTop
import com.test.novel.R
import com.test.novel.databinding.FragmentBookShelfBinding
import com.test.novel.databinding.FragmentBookStoreBinding

class BookStoreFragment : Fragment() {

    companion object {
        fun newInstance() = BookStoreFragment()
    }

    private val viewModel: BookStoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_book_store, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentBookStoreBinding.bind(view)
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.SearchBar)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, 0, 0, systemBars.bottom)
            constraintSet.setMargin(binding.searchBox.id,ConstraintSet.TOP,binding.searchBox.marginTop + systemBars.top)
            constraintSet.applyTo(binding.SearchBar)
            insets
        }
    }
}