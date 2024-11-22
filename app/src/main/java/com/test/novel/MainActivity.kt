package com.test.novel

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.test.novel.databinding.MainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        val binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = binding.bottomNavigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // 绑定 BottomNavigationView 与 NavController
        bottomNavigationView.setupWithNavController(navController)

        // 动态控制底部导航栏的显示与隐藏
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val showBottomNav = when (destination.id) {
                R.id.BookStoreFragment, R.id.BookShelfFragment, R.id.UserFragment -> true
                else -> false
            }
            bottomNavigationView.visibility = if (showBottomNav) View.VISIBLE else View.GONE
        }
    }
}

