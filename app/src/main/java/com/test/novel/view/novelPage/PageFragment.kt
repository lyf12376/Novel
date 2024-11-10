package com.test.novel.view.novelPage

import android.os.Bundle
import android.text.TextPaint
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.test.novel.R
import com.test.novel.databinding.FragmentPageBinding
import com.test.novel.utils.SizeUtils
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "text"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var text: String? = ""
    private lateinit var sharedViewModel: NovelFragmentViewModel
//    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            text = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentPageBinding.bind(view)
        val novelText = binding.novelText
        sharedViewModel =
            ViewModelProvider(requireParentFragment())[NovelFragmentViewModel::class.java]

        novelText.post {
            val width = novelText.width - novelText.paddingLeft - novelText.paddingRight
            val height = novelText.height - novelText.paddingTop - novelText.paddingBottom
            val example = text
            // 获取 Paint 对象以测量字符宽度
            novelText.text = example
            val list = example?.split("\n")
            var pageText = ""
            val pageLines = SizeUtils.getPageLineCount(novelText)
            var nowLines = 0
            val textPaint = novelText.paint
            var pageSent = false

            list?.forEachIndexed { index, s ->
                if (pageSent) return@forEachIndexed

                val length = s.length
                var left = 0
                for (i in 0 until length) {
                    val textWidth = textPaint.measureText(s, left, i + 1)
                    if (i == length - 1 && textWidth > 0) {
                        nowLines++
                        pageText += s.substring(left, i + 1) + "\n"
                    }
                    if (textWidth >= width) {
                        pageText += s.substring(left, i) + "\n"
                        left = i
                        nowLines++
                    }
                    if (nowLines == pageLines) {
                        pageSent = true
                        if(pageLines == 1){
                            break
                        }
                        Log.d("TAG", "onViewCreated: sendIntent")
                        sharedViewModel.sendIntent(BookIntent.AddPage(rowIndex = index, columnIndex = i))
                        break
                    }
                }
            }
            novelText.text = pageText
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(text: String) =
            PageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, text)
                    Log.d("TAG", "newInstance: $text")
//                    putString(ARG_PARAM2, param2)
                }
            }
    }
}