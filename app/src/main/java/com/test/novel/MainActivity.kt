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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.test.novel.databinding.MainBinding
import com.test.novel.utils.SizeUtils
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
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            if (SizeUtils.statusBarHeight == 0 && SizeUtils.navigationBarHeight == 0) {
                SizeUtils.statusBarHeight = systemBars.top
                SizeUtils.navigationBarHeight = systemBars.bottom
            }
            insets
        }
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



