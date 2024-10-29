package com.test.novel.view.novelPage

import android.os.Bundle
import android.text.TextPaint
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.test.novel.R
import com.test.novel.databinding.FragmentPageBinding
import com.test.novel.utils.SizeUtils

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
            val example = """${"\u3000\u3000"}神州历9999年秋，东海，青州城。
${"\u3000\u3000"}青州学宫，青州城圣地，青州城豪门贵族以及宗门世家内半数以上的强者，都从青州学宫走出。
${"\u3000\u3000"}因而，青州城之人皆以能够入学宫中修行为荣，旦有机会踏入学宫，必刻苦求学。
${"\u3000\u3000"}然而，似乎并非所有人都有此觉悟。
${"\u3000\u3000"}此时在青州学宫的一间学舍中，便有一位少年正趴在桌上熟睡。
${"\u3000\u3000"}讲堂之上，一身穿青衣长裙的少女也注意到了这一幕，俏脸上不由浮现一抹怒意，迈开脚步朝着正在睡梦中的少年走去。
${"\u3000\u3000"}秦伊，十七岁，青州学院正式弟子，外门弟子讲师，容颜美貌，身材火爆。
${"\u3000\u3000"}学舍中，一双双眼睛随着秦伊的动人身姿一起移动着，哪怕是生气，秦伊迈出的步伐依旧优雅。"""

            // 获取 Paint 对象以测量字符宽度
            novelText.text = example
            val list = example.split("\n")
            var pageText = ""
            val pageLines = SizeUtils.getPageLineCount(novelText)
            println(pageLines)
            var nowLines = 0
            val textPaint = novelText.paint
            var lastCharIndex = Pair(0, 0)
            list.forEachIndexed { index, s ->
                val length = s.length
                var left = 0
                for (i in 0 until length) {
                    val textWidth = textPaint.measureText(s, left, i + 1)
                    if (i == length - 1 && textWidth > 0) {
                        nowLines++
                        pageText += s.substring(left, i + 1) + "\n"
                        println(s.substring(left, i + 1))
                    }
                    if (textWidth >= width) {
                        println("$index $i $left")
                        println(s.substring(left, i))
                        pageText += s.substring(left, i) + "\n"
                        left = i
                        nowLines++
                    }
                    if (nowLines == pageLines) {
                        return@forEachIndexed
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