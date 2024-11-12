package com.test.novel.utils

import kotlinx.coroutines.*
import okhttp3.*
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

    // 发起网络请求并解析网页内容
    private suspend fun fetchHtml(url: String): String? {
        val request = Request.Builder()
            .header(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36 Edg/130.0.0.0"
            )
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

    // 解析HTML内容，提取正文
    private fun parseHtml(html: String): String {
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
            parseHtml(html)
        } else {
            println("无法从 $url 获取内容")
            ""
        }
    }
}


fun main() {
    // 替换为你需要爬取的实际 URL
    runBlocking {
        val time = measureTime {
            WebCrawler.fetchChapters(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20))
        }
        println(time)
    }

}


