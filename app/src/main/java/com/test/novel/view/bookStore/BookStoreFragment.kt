package com.test.novel.view.bookStore

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.marginTop
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.novel.R
import com.test.novel.databinding.FragmentBookShelfBinding
import com.test.novel.databinding.FragmentBookStoreBinding
import com.test.novel.model.BookBrief
import com.test.novel.utils.SizeUtils
import kotlin.math.log

class BookStoreFragment : Fragment() {

    companion object {
        fun newInstance() = BookStoreFragment()
    }

    private val viewModel: BookStoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setIntent(BookStoreIntent.InitData)
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
        activity?.let {activity->
            WindowInsetsControllerCompat(activity.window, binding.root).apply {
                show(WindowInsetsCompat.Type.systemBars())
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            SizeUtils.statusBarHeight = systemBars.top
            SizeUtils.navigationBarHeight = systemBars.bottom
            v.setPadding(0, systemBars.top, 0, systemBars.bottom)
            insets
        }
        val navController = findNavController()
        val adapter = BookStoreAdapter(this, viewModel)
        binding.recycle.adapter = adapter
        binding.SearchBar.setOnClickListener {
            navController.navigate(R.id.SearchFragment)
        }
        binding.swipe.setOnRefreshListener {
            viewModel.setIntent(BookStoreIntent.InitData)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TAG", "onDestroy: ")
    }
}