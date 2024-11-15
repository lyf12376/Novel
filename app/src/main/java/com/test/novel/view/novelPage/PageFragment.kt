package com.test.novel.view.novelPage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.test.novel.R
import com.test.novel.databinding.FragmentPageBinding
import com.test.novel.utils.SizeUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


private const val TITLE = "title"
private const val TEXT = "text"
private const val LOAD = "load"
private const val INDEX = "pageIndex"
private const val CHAPTER = "chapterIndex"


class PageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var text: String? = ""
    private var load: Boolean = false
    private var title:String? = ""
    private var pageIndex:Int = 0
    private var chapterIndex:Int = 0
    private lateinit var sharedViewModel: NovelFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel =
            ViewModelProvider(requireParentFragment())[NovelFragmentViewModel::class.java]
        arguments?.let {
            title = it.getString(TITLE)
            text = it.getString(TEXT)
            load = it.getBoolean(LOAD)
            pageIndex = it.getInt(INDEX)
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
        Log.d("TAG", "onViewCreated:")
        val binding = FragmentPageBinding.bind(view)
        val novelText = binding.novelText
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.root)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                sharedViewModel.state.collectLatest{
                    val page = it.pages[pageIndex]
                    load = page.load
                    title = page.title
                    chapterIndex = page.chapterIndex
                    if (page.text != text){
                        text = page.text
                        binding.title.text = title
                        novelText.text = text
                    }
                    if (!page.showTitle){
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
//                        novelText.logAll()
                        if (load)
                            return@post
                        var pageLines = SizeUtils.getPageLineCount(novelText)
//                        println("pageLines $pageLines")
                        val maxLines = novelText.getLineCountCus()
                        if (maxLines <= pageLines) {
                            novelText.text = example
                            return@post
                        }
//                        println("maxLines $maxLines")
                        var lineEndOffset = novelText.getLineEnd(pageLines-1)
                        val textShowInPage = example?.substring(0,lineEndOffset)
                        var nextStartLine = pageLines - 1
                        pageLines ++
                        var nextEndLine = nextStartLine + pageLines
                        novelText.text = textShowInPage
                        val pageList = mutableListOf(PageState(chapterIndex = chapterIndex,showTitle = true,title = title?:"",text = textShowInPage?:"",load = true))
                        while (lineEndOffset < example?.length!!){
                            if (nextEndLine > maxLines - 1){
                                val nextLineEndOffset = novelText.getLineEnd(maxLines - 1)
                                val nextTextShowInPage = example.substring(lineEndOffset,nextLineEndOffset)
//                                println("lastPage  $nextTextShowInPage")
                                pageList.add(PageState(chapterIndex = chapterIndex,title = "",text = nextTextShowInPage,load = true))
                                break
                            }
                            else {
                                val nextLineEndOffset = novelText.getLineEnd(nextEndLine)

                                val nextTextShowInPage =
                                    example.substring(lineEndOffset, nextLineEndOffset)
                                pageList.add(
                                    PageState(
                                        chapterIndex = chapterIndex,
                                        showTitle = false,
                                        title = title?:"",
                                        text = nextTextShowInPage,
                                        load = true
                                    )
                                )
                                lineEndOffset = nextLineEndOffset
                                nextEndLine += pageLines
                            }
                        }
                        sharedViewModel.sendIntent(BookIntent.AddPages(pageList))
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(pageState: PageState,pageIndex:Int) =
            PageFragment().apply {
                arguments = Bundle().apply {
                    putString(TITLE, pageState.title)
                    putString(TEXT, pageState.text)
                    putBoolean(LOAD,pageState.load)
                    putInt(INDEX,pageIndex)
                    putInt(CHAPTER,pageState.chapterIndex)
                }
            }
    }
}