package com.test.novel

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.test.novel.databinding.MainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    //    private lateinit var binding: ActivityYourBinding
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT) // light causes internally enforce the navigation bar to be fully transparent
        )
        super.onCreate(savedInstanceState)
        // 使用 ViewBinding
        val binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
