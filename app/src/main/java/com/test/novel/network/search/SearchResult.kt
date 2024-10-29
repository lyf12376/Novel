package com.test.novel.network.search

import kotlinx.serialization.Serializable

@Serializable
data class SearchResult (
    val url_list: String,
    val url_img: String,
    val articlename: String,
    val author: String,
    val intro: String
)

//[
//{
//    "url_list": "/book/12436/",
//    "url_img": "https://www.3bqg.cc/bookimg/0/470.jpg",
//    "articlename": "伏天氏",
//    "author": "净无痕",
//    "intro": "　　东方神州，有人皇立道统，有圣贤宗门传道，有诸侯雄踞一方王国，诸强林立，神州动乱千万载，值此之时，一代天骄叶青帝及东凰"
//},
//{
//    "url_list": "/book/125869/",
//    "url_img": "https://www.3bqg.cc/bookimg/119/119327.jpg",
//    "articlename": "三伏天（公媳）1V1",
//    "author": "岁安",
//    "intro": "潮湿、燥热，盐城的三伏天。两个寂寞人，三个可怜人。"
//},
//{
//    "url_list": "/book/8218/",
//    "url_img": "https://www.3bqg.cc/bookimg/4/4688.jpg",
//    "articlename": "伏天氏叶伏天花解语",
//    "author": "净无痕",
//    "intro": "东方神州，有人皇立道统，有圣贤宗门传道，有诸侯雄踞一方王国，诸强林立，神州动乱千万载，值此之时，一代天骄叶青帝及东凰大帝"
//},
//{
//    "url_list": "/book/23035/",
//    "url_img": "https://www.3bqg.cc/bookimg/15/15681.jpg",
//    "articlename": "伏天氏起点",
//    "author": "净无痕",
//    "intro": "东方神州，有人皇立道统，有圣贤宗门传道，有诸侯雄踞一方王国，诸强林立，神州动乱千万载，执此之时，一代天骄叶青帝及东凰大帝"
//},
//{
//    "url_list": "/book/2863/",
//    "url_img": "https://www.3bqg.cc/bookimg/10/10043.jpg",
//    "articlename": "伏天记笔趣阁",
//    "author": "净无痕",
//    "intro": "东方神州，有人皇立道统，有圣贤宗门传道，有诸侯雄踞一方王国，诸强林立，神州动乱千万载，执此之时，一代天骄叶青帝及东凰大帝"
//},
//{
//    "url_list": "/book/8050/",
//    "url_img": "https://www.3bqg.cc/bookimg/4/4856.jpg",
//    "articlename": "叶伏天秦伊",
//    "author": "净无痕",
//    "intro": "东方神州，有人皇立道统，有圣贤宗门传道，有诸侯雄踞一方王国，诸强林立，神州动乱千万载，值此之时，一代天骄叶青帝及东凰大帝"
//},
//{
//    "url_list": "/book/4074/",
//    "url_img": "https://www.3bqg.cc/bookimg/8/8832.jpg",
//    "articlename": "伏天狂徒",
//    "author": "叶叶开",
//    "intro": "我等生来自由身，谁敢高高在上？少年自漠北提刀而出，十年蛰伏，一朝灿烂！江湖碗里三尺气概，乾坤壶中千古风流，我养一刀伏天地"
//},
//{
//    "url_list": "/book/13283/",
//    "url_img": "https://www.3bqg.cc/bookimg/25/25433.jpg",
//    "articlename": "叶伏天花解语",
//    "author": "净无痕",
//    "intro": "东方神州，有人皇立道统，有圣贤宗门传道，有诸侯雄踞一方王国，诸强林立，神州动乱千万载，值此之时，一代天骄叶青帝及东凰大帝"
//},
//{
//    "url_list": "/book/19351/",
//    "url_img": "https://www.3bqg.cc/bookimg/19/19365.jpg",
//    "articlename": "花解语叶伏天",
//    "author": "净无痕",
//    "intro": "东方神州，有人皇立道统，有圣贤宗门传道，有诸侯雄踞一方王国，诸强林立，神州动乱千万载，值此之时，一代天骄叶青帝及东凰大帝"
//},
//{
//    "url_list": "/book/18475/",
//    "url_img": "https://www.3bqg.cc/bookimg/20/20241.jpg",
//    "articlename": "极限伏天",
//    "author": "天茗",
//    "intro": "斗界五州大陆，气魂为先，铸世气魂，五龙四凤，中央麒麟。苍龙少主凌云废而后立，失去苍龙，却得玄龟。玄龟虽小，能力通天，玄龟"
//},
//{
//    "url_list": "/book/19352/",
//    "url_img": "https://www.3bqg.cc/bookimg/19/19364.jpg",
//    "articlename": "叶伏天余生",
//    "author": "净无痕",
//    "intro": "东方神州，有人皇立道统，有圣贤宗门传道，有诸侯雄踞一方王国，诸强林立，神州动乱千万载，值此之时，一代天骄叶青帝及东凰大帝"
//},
//{
//    "url_list": "/book/19353/",
//    "url_img": "https://www.3bqg.cc/bookimg/19/19363.jpg",
//    "articlename": "叶伏天是什么",
//    "author": "净无痕",
//    "intro": "东方神州，有人皇立道统，有圣贤宗门传道，有诸侯雄踞一方王国，诸强林立，神州动乱千万载，值此之时，一代天骄叶青帝及东凰大帝"
//},
//{
//    "url_list": "/book/51510/",
//    "url_img": "https://www.3bqg.cc/bookimg/38/38826.jpg",
//    "articlename": "伏天剑帝",
//    "author": "左岸咖啡店",
//    "intro": "十万年前，九位铸剑大师，铸造逆天神剑伏天，从此伏天一出，神魔皆退，万灵臣服。十万年后，神剑山庄少年，从卑微中崛起，寂灭中"
//},
//{
//    "url_list": "/book/1105/",
//    "url_img": "https://www.3bqg.cc/bookimg/11/11801.jpg",
//    "articlename": "伏天剑狂",
//    "author": "沏道",
//    "intro": "【18年最强玄幻！一剑红颜血千里，斩神天路骨成堆，燃爆你的热血！】女主很萝莉！“莫麟哥哥，人家好久没见你了，今晚我想留下"
//},
//{
//    "url_list": "/book/72737/",
//    "url_img": "https://www.3bqg.cc/bookimg/69/69219.jpg",
//    "articlename": "伏天",
//    "author": "捕梦者",
//    "intro": "第八卷名扬天下大结局（附带完本感言）“云皇万岁万岁万万岁——”山呼海啸般的朝拜之声在宫城之中响起，数百万名忠心耿耿的文臣"
//},
//{
//    "url_list": "/book/137751/",
//    "url_img": "https://www.3bqg.cc/bookimg/133/133255.jpg",
//    "articlename": "伏天剑尊",
//    "author": "残剑",
//    "intro": "曾逍遥万界，无敌寂寞，寰宇称王。为情而殇，仙路断，魂消亡，生死两茫茫。重生轮回，至尊归来。万般荣耀皆可弃，傲剑天下为伊狂"
//},
//{
//    "url_list": "/book/144905/",
//    "url_img": "https://www.3bqg.cc/bookimg/151/151911.jpg",
//    "articlename": "净无痕作品",
//    "author": "伏天氏",
//    "intro": "东方神州，有人皇立道统，有圣贤宗门传道，有诸侯雄踞一方王国，诸强林立，神州动乱千万载，值此之时，一代天骄叶青帝及东凰大帝"
//}
//]