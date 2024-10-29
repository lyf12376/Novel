package com.test.novel.view.novelPage

import android.os.Bundle
import android.text.TextPaint
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.novel.R
import com.test.novel.databinding.FragmentPageBinding

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
        novelText.post {
            val width = binding.novelText.width - binding.novelText.paddingLeft - binding.novelText.paddingRight
            val textSize = binding.novelText.textSize

            val example = "对开发者来说放弃 Google Play 其实也不算是太大的问题，毕竟这是开源软件又不需要通过 Google Play 付费购买。\n" +
                    "\n" +
                    "所以这也是为什么说谷歌审核只是导火索，说到底还是开发者已经累了，另一位 Syncthing 主要维护者称该应用长期处于支持状态也就是能用但已经没有添加新功能。\n" +
                    "\n" +
                    "最终结果就是开发者彻底放弃这个项目不再开发，尽管目前还能用但不会再修复 BUG 以及适配后续的 Android 新版本，可能会对用户的使用产生影响。\n" +
                    "\n" +
                    "最后同类软件还有 SyncTrayzor 不过也处于非活跃开发阶段，所以不知道是否会有开发者接手 Syncthing 的开发让这款应用能够继续使用。"
            // 获取 Paint 对象以测量字符宽度
            novelText.text = example
            val textPaint: TextPaint = binding.novelText.paint

            val exampleCharWidth = textPaint.measureText(example)  // 假设以汉字测量字符宽度
            println("exampleCharWidth: $exampleCharWidth")
            // 计算每行最多能容纳的字符数
            val maxCharsPerLine = (width / exampleCharWidth).toInt()

            println(novelText.lineCount)
            println("TextView宽度: $width")
            println("字体大小: $textSize px")
            println("每行最多字符数: $maxCharsPerLine")
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