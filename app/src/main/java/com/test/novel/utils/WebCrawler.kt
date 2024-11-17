package com.test.novel.utils

import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.internal.throwMissingFieldException
import kotlinx.serialization.json.Json
import okhttp3.*
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import kotlin.time.measureTime

object WebCrawler {

    // 配置 OkHttpClient，启用连接池
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .connectionPool(ConnectionPool(16, 1, TimeUnit.MINUTES)) // 连接池配置
        .build()

    // 发起网络请求并解析网页内容
    private fun fetchHtml(url: String): String? {
        val request = Request.Builder()
            .header(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36 Edg/130.0.0.0"
            )
            .addHeader("Cookie","e2=; e1=%7B%22l6%22%3A%22%22%2C%22l7%22%3A%22%22%2C%22l1%22%3A5%2C%22l3%22%3A%22%22%2C%22pid%22%3A%22qd_P_rank%22%2C%22eid%22%3A%22qd_C47%22%7D; newstatisticUUID=1722172888_1469407685; _csrfToken=8faea513-c53f-4417-aafd-70c2b429bbca; fu=485525092; _ga=GA1.1.1532030433.1722172889; supportwebp=true; x-waf-captcha-referer=; traffic_utm_referer=; _ga_FZMMH98S83=GS1.1.1731817396.3.0.1731817396.0.0.0; _ga_PFYW0QLV3P=GS1.1.1731817396.3.0.1731817396.0.0.0; w_tsfp=ltvuV0MF2utBvS0Q6qvhlUytHz8mdDA4h0wpEaR0f5thQLErU5mG0I57uc/2N3Pf5sxnvd7DsZoyJTLYCJI3dwMQTc6TId8ZjAqRxoBz3YdGBRYxEZjcUVRLJ7Jx5DUTdXhCNxS00jA8eIUd379yilkMsyN1zap3TO14fstJ019E6KDQmI5uDW3HlFWQRzaLbjcMcuqPr6g18L5a5WvasVz9fQ5xBelC1xHEhnsdW3sjtxHuJekMMEn4JcqrSqA=")
            .url(url)
            .build()

//        // 随机延迟，模拟用户请求，避免封禁
//        delay((500L..3000L).random())

        return client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                val contentType = response.header("Content-Type")
                val charset = if (contentType != null && contentType.contains("charset=")) {
                    contentType.substringAfter("charset=").uppercase()
                } else {
                    "UTF-8"
                }
                response.body?.byteStream()?.reader(Charset.forName(charset))?.readText()
            } else {
                null
            }
        }
    }


    // 定义网络请求的重试逻辑
    private suspend fun fetchHtmlWithRetry(url: String, retries: Int = 3): String? {
        var attempt = 0
        while (attempt < retries) {
            val result = fetchHtml(url)
            if (result != null) return result
            attempt++
            delay(1000L * attempt) // 指数级退避
        }
        return null
    }

    // 获取起点中文网排行榜
    private fun parseQDHtml(html: String): Rank {
        val doc: Document = Jsoup.parse(html)

        // 获取书名
        val bookName = doc.select(".book-mid-info h2 a[href]").text().split(" ")

        // 获取作者
        val author = doc.select(".book-mid-info .author .name").text().split(" ")

        // 获取分类
        val category = doc.select(".book-mid-info .author a").getOrNull(1)?.text()?.split(" ") ?: listOf("")

        // 获取子分类
        val subCategory = doc.select(".book-mid-info .author a.go-sub-type").text().split(" ")

        // 获取连载状态
        val status = doc.select(".book-mid-info .author span").text().split(" ")

        // 获取简介
        val intro = doc.select(".book-mid-info .intro").text().split(" ")

        // 获取最新章节
        val latestChapter = doc.select(".book-mid-info .update a").text().split(" ")

        // 获取最新更新时间
        val latestUpdateTime = doc.select(".book-mid-info .update span").text().split(" ")

        // 构造 Rank 对象
        return Rank(
            bookName = bookName,
            author = author,
            category = category,
            subCategory = subCategory,
            status = status,
            intro = intro,
            latestChapter = latestChapter,
            latestUpdateTime = latestUpdateTime
        )
    }
    @Serializable
    data class Rank(
        val bookName: List<String> = listOf(),
        val author: List<String> = listOf(),
        val category: List<String> = listOf(),
        val subCategory: List<String> = listOf(),
        val status: List<String> = listOf(),
        val intro: List<String> = listOf(),
        val latestChapter: List<String> = listOf(),
        val latestUpdateTime: List<String> = listOf()
    )




    // 解析HTML内容，提取正文
    private fun parseBQGHtml(html: String): String {
        val doc: Document = Jsoup.parse(html)
        val contentDiv = doc.selectFirst("#chaptercontent")
        val chapterName = doc.selectFirst("h1.wap_none")?.text()
        println(chapterName)

        return if (contentDiv != null) {
            contentDiv.text().replace("\\s+".toRegex(), "\n").split("<br><br>").dropLast(2).joinToString("\n")
        } else {
            println("没有找到正文内容")
            ""
        }
    }

    // 使用协程并发抓取多个章节
    suspend fun fetchChapters(chapters: List<Int>): List<String> = coroutineScope {
        chapters.map { chapter ->
            async {
                try {
                    fetchChapter(chapter)
                } catch (e: Exception) {
                    println("Error fetching chapter $chapter: ${e.message}")
                    ""
                }
            }
        }.awaitAll()
    }

    // 抓取单个章节
    suspend fun fetchChapter(chapter: Int): String {
        val url = "https://www.3bqg.cc/book/10376/${chapter}.html"
        val html = fetchHtmlWithRetry(url)
        return if (html != null) {
            parseBQGHtml(html)
        } else {
            println("无法从 $url 获取内容")
            ""
        }
    }

    suspend fun fetchQD():Rank{
        val url = "https://www.qidian.com/rank/readIndex"
        val html = fetchHtmlWithRetry(url)
        return if (html != null){
            parseQDHtml(html)
        }else{
            Rank()
        }
    }
}


fun main() {
    // 替换为你需要爬取的实际 URL
    runBlocking {
        val time = measureTime {
            println(Json.encodeToString(WebCrawler.fetchQD()))
        }
        println(time)
    }

}


