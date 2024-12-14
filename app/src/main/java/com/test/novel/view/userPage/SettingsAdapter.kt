package com.test.novel.view.userPage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.novel.R
import com.test.novel.databinding.UserItemBinding
import com.test.novel.utils.file.FileComponent

class SettingsAdapter(fileComponent: FileComponent) : RecyclerView.Adapter<SettingsAdapter.UserViewHolder>() {

    private val settingsList = listOf(
        Pair(R.drawable.read_history, "阅读历史"),
        Pair(R.drawable.download, "下载记录"),
        Pair(R.drawable.upload, "上传本地书籍"),
        Pair(R.drawable.note, "笔记"),
        Pair(R.drawable.settings, "设置"),
    )

    private val click = listOf(
        {
            // 阅读历史

        },
        {
            // 下载记录
        },
        {
            // 上传本地书籍
            fileComponent.selectDocument()
        },
        {
            // 笔记
        },
        {
            // 设置

        }
    )

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = UserItemBinding.bind(view)
        val icon = binding.icon
        val text = binding.text
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val setting = settingsList[position]
        holder.icon.setImageResource(setting.first)
        holder.text.text = setting.second
        holder.itemView.setOnClickListener {
            click[position]()
        }
    }

    override fun getItemCount(): Int {
        return settingsList.size
    }

}