package com.test.novel

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.test.novel.databinding.ActivityMain2Binding
import com.test.novel.utils.SizeUtils

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT) // light causes internally enforce the navigation bar to be fully transparent
        )

        // 初始化 binding
        val binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root) // 使用 binding.root 设置内容视图

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
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
　　“三百年前，东凰大帝一统东方神州，下令天下诸侯创建武府学宫，兴盛武道，青州学宫便是在此背景下创立。”叶伏天回应道，当然他所说的是正史记载，在家族中他所看到的野史中还有另一个名字存在，然而，那禁忌之名，却决不允许被提及。"""
        binding.custom.text = text
        binding.custom.post {
            println( binding.custom.getLineCountCus())
            val layout = binding.custom.layout
            println(layout.lineCount)
            for (i in 0 until layout.lineCount){
                println("line $i: ${text.substring(layout.getLineStart(i),layout.getLineEnd(i))}")
                println("line $i: ${binding.custom.getLineText(i)}")
            }
        }
    }
}