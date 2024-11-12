package com.test.novel.view.novelPage

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.test.novel.R
import com.test.novel.databinding.FragmentPageBinding
import com.test.novel.utils.SizeUtils


private const val ARG_PARAM1 = "title"
private const val ARG_PARAM2 = "text"
private const val ARG_PARAM3 = "load"

class PageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var text: String? = ""
    private var load: Boolean = false
    private var title:String? = ""
    private lateinit var sharedViewModel: NovelFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_PARAM1)
            text = it.getString(ARG_PARAM2)
            load = it.getBoolean(ARG_PARAM3)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentPageBinding.bind(view)
        val novelText = binding.novelText
        sharedViewModel =
            ViewModelProvider(requireParentFragment())[NovelFragmentViewModel::class.java]
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.root)

        if (title == ""){
            binding.title.visibility = View.GONE
            constraintSet.connect(binding.novelText.id,ConstraintSet.TOP,binding.topic.id,ConstraintSet.BOTTOM)
        }else{
            binding.title.post {
                binding.title.text = title
            }
        }

        novelText.post {
            val example = text
            // 获取 Paint 对象以测量字符宽度
            novelText.text = example
            if (load)
                return@post
            val pageLines = SizeUtils.getPageLineCount(novelText)
            val layout= novelText.layout
            val maxLines = layout.lineCount
            if (maxLines <= pageLines) {
                novelText.text = example
                return@post
            }
            val lineEndOffset = layout.getLineEnd(pageLines-1)
            val textShowInPage = example?.substring(0,lineEndOffset)
            novelText.text = textShowInPage
            sharedViewModel.sendIntent(BookIntent.AddPage(lineEndOffset))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(pageState: PageState) =
            PageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, pageState.title)
                    putString(ARG_PARAM2, pageState.text)
                    putBoolean(ARG_PARAM3,pageState.load)
                    Log.d("TAG", "newInstance: $pageState")
                }
            }
    }
}