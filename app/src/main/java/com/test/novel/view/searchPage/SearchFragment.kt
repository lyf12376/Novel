package com.test.novel.view.searchPage

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.test.novel.R
import com.test.novel.databinding.FragmentSearchBinding
import com.test.novel.utils.SizeUtils.statusBarHeight
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSearchBinding.bind(view)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, statusBarHeight, systemBars.right, 0)
            insets
        }
        val navController = findNavController()
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment, SearchHistoryFragment())
            .commit()
        binding.back.setOnClickListener {
            navController.popBackStack()
        }
        binding.searchText.setOnClickListener {
            viewModel.sendIntent(SearchIntent.Search(binding.searchInput.text.toString()))
            gotoSearch()
        }
        binding.searchInput.setOnEditorActionListener { _, _, _ ->
            viewModel.sendIntent(SearchIntent.Search(binding.searchInput.text.toString()))
            gotoSearch()
            true
        }
    }

    private fun gotoSearch(){
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment, SearchResultFragment())
            .commit()
    }

}