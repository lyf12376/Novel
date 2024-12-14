package com.test.novel.utils

object Utils {
    fun chineseNumberToArabic(chineseNumber: String): Int {
        val numberMap = mapOf(
            '零' to 0, '一' to 1, '二' to 2, '三' to 3, '四' to 4,
            '五' to 5, '六' to 6, '七' to 7, '八' to 8, '九' to 9
        )
        val unitMap = mapOf(
            '十' to 10, '百' to 100, '千' to 1000
        )

        var result = 0
        var temp = 0
        var unit = 1

        for (char in chineseNumber.reversed()) {
            when {
                unitMap.containsKey(char) -> {
                    unit = unitMap[char]!!
                    if (temp == 0) temp = 1 // 处理 "十" 等情况
                    result += temp * unit
                    temp = 0
                }
                numberMap.containsKey(char) -> {
                    temp = numberMap[char]!!
                }
                else -> throw IllegalArgumentException("Unsupported character: $char")
            }
        }
        return result + temp * unit
    }

    fun arabicNumberToChinese(number: Int): String {
        val numberMap = mapOf(
            0 to '零', 1 to '一', 2 to '二', 3 to '三', 4 to '四',
            5 to '五', 6 to '六', 7 to '七', 8 to '八', 9 to '九'
        )
        val unitMap = mapOf(
            10 to '十', 100 to '百', 1000 to '千'
        )

        val result = StringBuilder()
        var remaining = number
        var unit = 1

        while (remaining > 0) {
            val digit = remaining % 10
            if (digit > 0) {
                if (unit > 1) result.insert(0, unitMap[unit])
                result.insert(0, numberMap[digit])
            }
            remaining /= 10
            unit *= 10
        }

        return result.toString()
    }
}