package com.test.novel.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

object WebCrawler {
    private suspend fun fetchHtml(url: String): String? {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
//        .addInterceptor(loggingInterceptor)
            .build()

        val request = Request.Builder()
            .header(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36 Edg/130.0.0.0"
            )
            .url(url)
            .build()

        client.newCall(request).execute().use {  response->
            return if (response.isSuccessful) {
                val contentType = response.header("Content-Type")
                val charset = if (contentType != null && contentType.contains("charset=")) {
                    contentType.substringAfter("charset=").uppercase()
                } else {
                    "UTF-8"
                }
                println(charset)
                response.body?.byteStream()?.reader(Charset.forName(charset))?.readText()
            } else {
                null
            }
        }
    }

    private fun parseHtml(html: String):String {
        val doc: Document = Jsoup.parse(html)

        // 提取 id 为 "chaptercontent" 的 div
        val contentDiv = doc.selectFirst("#chaptercontent")
        val chapterName = doc.selectFirst("h1.wap_none")

        // 如果找到了这个 div，提取里面的纯文本
        if (contentDiv != null) {
            val textContent = contentDiv.html().replace(" ", "")
            val noSpacesString = textContent.replace("\\s+".toRegex(), "").split("<br><br>")
            
        } else {
            println("没有找到正文内容")
        }
    }

    suspend fun fetchChapter(chapter:Int):String {
        val url = "https://www.3bqg.cc/book/10376/${chapter}.html"
        val html = fetchHtml(url)
        if (html != null) {
            parseHtml(html)
        } else {
            println("无法从 $url 获取内容")
        }
    }
}

fun main() {
    // 替换为你需要爬取的实际 URL
    runBlocking { WebCrawler.fetchChapter(2) }

}


