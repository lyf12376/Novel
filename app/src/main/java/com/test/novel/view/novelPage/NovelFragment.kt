package com.test.novel.view.novelPage

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.test.novel.R
import com.test.novel.databinding.FragmentPageBinding
import com.test.novel.databinding.NovelReadBinding
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "novelId"

/**
 * A simple [Fragment] subclass.
 * Use the [NovelFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NovelFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: Int? = null
    private var param2: String? = null
    private lateinit var novelFragmentViewModel: NovelFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        novelFragmentViewModel = ViewModelProvider(this).get(NovelFragmentViewModel::class.java)
        arguments?.let { bundle ->
            param1 = bundle.getInt(ARG_PARAM1)
            param1?.let { novelFragmentViewModel.init(it) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.novel_read, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var statusBarHeight = 0
        var navigationBarHeight = 0
        val binding = NovelReadBinding.bind(view)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            if (statusBarHeight == 0) {
                statusBarHeight = systemBars.top
//                println("statusBarHeight: $statusBarHeight")
            }
            if (navigationBarHeight == 0) {
                navigationBarHeight = systemBars.bottom
            }
            v.setPadding(systemBars.left, statusBarHeight, systemBars.right, 0)
            insets
        }

//        activity?.let {
//            WindowInsetsControllerCompat(it.window, binding.root).apply {
//                hide(WindowInsetsCompat.Type.systemBars())
//            }
//        }
        val novelFrameLayout = binding.novelFrame
        val text = """${"\u3000\u3000"}神州历9999年秋，东海，青州城。

　　青州学宫，青州城圣地，青州城豪门贵族以及宗门世家内半数以上的强者，都从青州学宫走出。

　　因而，青州城之人皆以能够入学宫中修行为荣，旦有机会踏入学宫，必刻苦求学。

　　然而，似乎并非所有人都有此觉悟。

　　此时在青州学宫的一间学舍中，便有一位少年正趴在桌上熟睡。

　　讲堂之上，一身穿青衣长裙的少女也注意到了这一幕，俏脸上不由浮现一抹怒意，迈开脚步朝着正在睡梦中的少年走去。

　　秦伊，十七岁，青州学院正式弟子，外门弟子讲师，容颜美貌，身材火爆。

　　学舍中，一双双眼睛随着秦伊的动人身姿一起移动着，哪怕是生气，秦伊迈出的步伐依旧优雅。

　　“这家伙，竟然又在秦师姐的讲堂上睡觉。”似乎这才注意到那熟睡的身姿，周围许多少年都有些无语，显然，这已经不是第一次了。

　　“以秦师姐的容貌和身材，哪怕是看着她也足以令人赏心悦目，那家伙脑子里究竟装的什么。”

　　在诸多讲师当中，秦伊绝对是人气最高的讲师，没有之一，至于原因，只要看到她便能明白，不知多少人将之奉为女神，她的讲堂，从来都是将学舍挤满为止。

　　在秦伊的讲堂上睡觉？这简直是对女神的亵渎。

　　秦伊的步伐很轻，走到少年的身边之时没有发出一点声响，她站在桌前，看着眼前那酣睡中的面孔，她的美丽容颜上布满了寒霜。

　　“叶伏天。”一道轻柔的声音传出，不过却并非是从秦伊口中喊出的，而是来自叶伏天的身后。

　　似乎是在睡梦中听到有人喊自己，叶伏天的身子动了动，双手撑着脑袋，悠悠的睁开眼睛，朦胧的目光下，映入眼帘是起伏的峰峦。

　　“好大。”叶伏天情不自禁的低语了一声，他的声音很轻，像是在自言自语，然而在此刻安静的环境中，这声音依旧显得格外的突兀，只一瞬间，许多道目光凝固在了空气中，随即又化作愤怒。

　　“他竟然敢……公然轻薄秦师姐？”

　　“这厚颜无耻的家伙，混蛋。”一道道愤怒的目光像是化作利剑，使得叶伏天打了个冷颤，像是感觉不对劲，他的目光顺着那诱人之地往上移动，随后便看到了一张精致如玉却满是怒火的脸庞。

　　“额……”叶伏天一脸黑线，怎么是秦伊？喊他的人不是晴雪吗？

　　回头看了一眼，便见到一位十五岁的清纯少女正对着他怒目而视。

　　叶伏天扫了一眼少女，随即暗骂一声，被害惨了，难怪尺寸不对。

　　“秦师姐，我……”叶伏天刚想解释。

　　“叶伏天。”秦伊冷漠的将他打断，道：“青州学宫是在什么背景下创立？”

　　很显然，秦伊是要回避刚才的尴尬，转移话题，但她此刻的怒火，叶伏天却能够清楚的感受到，他甚至隐隐感觉到从秦伊身上流动出一缕缕剑意，锋利刺骨，刺痛着他的每一寸肌肤。

　　“三百年前，东凰大帝一统东方神州，下令天下诸侯创建武府学宫，兴盛武道，青州学宫便是在此背景下创立。”叶伏天回应道，当然他所说的是正史记载，在家族中他所看到的野史中还有另一个名字存在，然而，那禁忌之名，却决不允许被提及。""".trimIndent()
        val adapter = PageFragmentAdapter(
            this,
            listOf(text)
        )
        binding.novelText.adapter = adapter
        novelFrameLayout.apply {
            // 定义点击事件的处理函数
            val leftClick = { binding.novelText.currentItem -= 1 }
            val middleClick = {
                novelFragmentViewModel.showOrHideBar()
            }
            val rightClick = { binding.novelText.currentItem += 1 }
            // 设置点击事件列表
            this.clickList = listOf(leftClick, middleClick, rightClick)
        }
        val topBar = binding.topBar
        val bottomBar = binding.bottomBar
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                novelFragmentViewModel.showBar.collect { showBar ->
                    if (showBar) {
                        topBar.visibility = View.VISIBLE
                        topBar.animate()
                            .translationY(0F)
                            .setDuration(500) // 修改持续时间为500毫秒
                            .setInterpolator(AccelerateDecelerateInterpolator()) // 使用加速减速插值器
                            .start()
                        bottomBar.animate()
                            .translationY(0F)
                            .setDuration(500) // 修改持续时间为500毫秒
                            .setInterpolator(AccelerateDecelerateInterpolator()) // 使用加速减速插值器
                            .start()
                        activity?.let {
                            WindowInsetsControllerCompat(it.window, binding.root).apply {
                                show(WindowInsetsCompat.Type.systemBars())
                            }
                        }
                    } else {
                        topBar.animate()
                            .translationY((-topBar.height).toFloat())
                            .setDuration(500) // 修改持续时间为500毫秒
                            .setInterpolator(AccelerateDecelerateInterpolator()) // 使用加速减速插值器
                            .withEndAction { topBar.visibility = View.GONE }
                            .start()
                        bottomBar.animate()
                            .translationY(bottomBar.height.toFloat() + navigationBarHeight)
                            .setDuration(500) // 修改持续时间为500毫秒
                            .setInterpolator(AccelerateDecelerateInterpolator()) // 使用加速减速插值器
                            .start()
                        activity?.let {
                            WindowInsetsControllerCompat(it.window, binding.root).apply {
                                hide(WindowInsetsCompat.Type.systemBars())
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NovelFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NovelFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}