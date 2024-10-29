package com.test.novel.view.bookShelfPage

import android.graphics.Color
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.test.novel.R
import com.test.novel.databinding.FragmentBookShelfBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BookShelfFragment : Fragment() {

    companion object {
        fun newInstance() = BookShelfFragment()
    }

    private val viewModel: BookShelfViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_book_shelf, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentBookShelfBinding.bind(view)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.itemSelected.collect {
                    when(it){
                        0 -> {
                            binding.bookShelf.textSize = 32f
                            binding.readHistory.textSize = 28f
                            binding.bookShelf.setTextColor(Color.BLACK)
                            binding.readHistory.setTextColor("#B6B6B6".toColorInt())
                        }
                        1 -> {
                            binding.bookShelf.textSize = 28f
                            binding.readHistory.textSize = 32f
                            binding.bookShelf.setTextColor("#B6B6B6".toColorInt())
                            binding.readHistory.setTextColor(Color.BLACK)
                        }
                    }
                }
            }
        }
        binding.bookShelfRecyclerView.adapter = BookAdapter(viewModel.bookList)
        binding.bookShelf.setOnClickListener {
            viewModel.selectItem(0)
        }
        binding.readHistory.setOnClickListener {
            viewModel.selectItem(1)
        }
    }
}