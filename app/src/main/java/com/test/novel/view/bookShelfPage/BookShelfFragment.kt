package com.test.novel.view.bookShelfPage

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import android.util.Size
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.test.novel.R
import com.test.novel.databinding.FragmentBookShelfBinding
import com.test.novel.utils.SizeUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
@AndroidEntryPoint
class BookShelfFragment : Fragment() {

    companion object {
        fun newInstance() = BookShelfFragment()
    }

    private val viewModel: BookShelfViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            v.setPadding(0, SizeUtils.statusBarHeight, 0, 0)
            insets
        }

        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.root)

        val bookShelf = binding.bookShelfRecyclerView
        bookShelf.adapter = BookAdapter(this,viewModel)

        val deleteBar = binding.deleteBar

        val cancelDelete = binding.cancel
        val confirmDelete = binding.confirm
        cancelDelete.setOnClickListener {
            viewModel.sendIntent(BookShelfIntent.Cancel)
        }

        confirmDelete.setOnClickListener {
            viewModel.sendIntent(BookShelfIntent.DeleteBooks)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.state.map { it.isDeleteMode }
                    .distinctUntilChanged()
                    .collect { isDeleteMode ->
                        val topAnchor = if (isDeleteMode) deleteBar.id else binding.main.id
                        constraintSet.connect(bookShelf.id, ConstraintSet.TOP, topAnchor, if (topAnchor == deleteBar.id) ConstraintSet.BOTTOM else ConstraintSet.TOP)
                        constraintSet.applyTo(binding.root)
                        deleteBar.visibility = if (isDeleteMode) View.VISIBLE else View.INVISIBLE
                    }

            }
        }
    }
}


//viewModel.itemSelected.collect {
//    when (it) {
//        0 -> {
//            // 动画改变文字大小
//            ValueAnimator.ofFloat(24f, 28f).apply {
//                duration = 300 // 动画持续时间
//                addUpdateListener { animator ->
//                    binding.bookShelfText.textSize = animator.animatedValue as Float
//                }
//            }.start()
//            ValueAnimator.ofFloat(28f, 24f).apply {
//                duration = 300
//                addUpdateListener { animator ->
//                    binding.readHistoryText.textSize = animator.animatedValue as Float
//                }
//            }.start()
//            // 动画改变文字颜色
//            ValueAnimator.ofArgb("#B6B6B6".toColorInt(), Color.BLACK).apply {
//                duration = 300
//                addUpdateListener { animator ->
//                    binding.bookShelfText.setTextColor(animator.animatedValue as Int)
//                }
//            }.start()
//            ValueAnimator.ofArgb(Color.BLACK, "#B6B6B6".toColorInt()).apply {
//                duration = 300
//                addUpdateListener { animator ->
//                    binding.readHistoryText.setTextColor(animator.animatedValue as Int)
//                }
//            }.start()
//        }
//        1 -> {
//            ValueAnimator.ofFloat(28f, 24f).apply {
//                duration = 300
//                addUpdateListener { animator ->
//                    binding.bookShelfText.textSize = animator.animatedValue as Float
//                }
//            }.start()
//            ValueAnimator.ofFloat(24f, 28f).apply {
//                duration = 300
//                addUpdateListener { animator ->
//                    binding.readHistoryText.textSize = animator.animatedValue as Float
//                }
//            }.start()
//            ValueAnimator.ofArgb(Color.BLACK, "#B6B6B6".toColorInt()).apply {
//                duration = 300
//                addUpdateListener { animator ->
//                    binding.bookShelfText.setTextColor(animator.animatedValue as Int)
//                }
//            }.start()
//            ValueAnimator.ofArgb("#B6B6B6".toColorInt(), Color.BLACK).apply {
//                duration = 300
//                addUpdateListener { animator ->
//                    binding.readHistoryText.setTextColor(animator.animatedValue as Int)
//                }
//            }.start()
//        }
//    }
//}
