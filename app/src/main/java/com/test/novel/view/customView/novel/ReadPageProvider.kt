package com.test.novel.view.customView.novel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.test.novel.databinding.FragmentBookBriefBinding
import com.test.novel.databinding.FragmentPageBinding
import com.test.novel.model.BookBrief

class ReadPageProvider : PageProvider {

    private val pages = mutableListOf<PageType>()

    override fun getPageCount(): Int {
        return pages.size
    }

    override fun bindPage(pageView: View, pageIndex: Int) {
        if(pages[pageIndex] is PageType.NormalPage){
            val page = FragmentPageBinding.inflate(
                LayoutInflater.from(pageView.context),
                pageView as ViewGroup,
                true
            )
//            page.topic.layoutParams.let {
//                it as ConstraintLayout.LayoutParams
//            }.apply {
//                setMargins(0, 36, 0, 0)
//            }
            page.novelText.text = pages[pageIndex].content as String
            page.pageIndex.text = "${pageIndex + 1}/${pages.size}"
        }else{
            val page = FragmentBookBriefBinding.inflate(
                LayoutInflater.from(pageView.context),
                pageView as ViewGroup,
                true
            )
            val brief = pages[pageIndex].content as BookBrief
            Glide.with(pageView.context).load(brief.coverUrl).into(page.bookCover)
            page.bookName.text = brief.title
            page.author.text = brief.author
            page.introduction.text = brief.brief
            page.type.text = brief.type.joinToString("\u3000")
        }
    }

    override fun addPage(pageContent: PageType): Int {
        pages.add(pageContent)
        return pages.size - 1
    }


}

sealed class PageType(val content:Any){
    data class NormalPage(val text:String):PageType(text)
    data class CoverPage(val brief:BookBrief):PageType(brief)
}