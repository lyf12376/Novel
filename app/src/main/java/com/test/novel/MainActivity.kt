package com.test.novel

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.test.novel.databinding.MainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonNull.content


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        // 设置视图
        val binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 初始化导航
        val bottomNavigationView = binding.bottomNavigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)

        // 动态控制底部导航栏显示
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val showBottomNav = when (destination.id) {
                R.id.BookStoreFragment, R.id.BookShelfFragment, R.id.UserFragment -> true
                else -> false
            }
            bottomNavigationView.visibility = if (showBottomNav) View.VISIBLE else View.GONE
        }

    }


}



