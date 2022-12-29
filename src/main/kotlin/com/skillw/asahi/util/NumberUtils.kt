package com.skillw.asahi.util

import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.max
import kotlin.math.min

// For script coders
object NumberUtils {
    @JvmOverloads
    @JvmStatic
    fun Number.format(input: String = "#.##"): String {
        val decimalFormat = DecimalFormat(input)
        return decimalFormat.format(this)
    }

    /** 创建线程安全的随机数生成器 */
    fun random(): Random {
        return ThreadLocalRandom.current()
    }

    /**
     * 随机数判断
     *
     * @param v 0-1
     */
    fun random(v: Double): Boolean {
        return ThreadLocalRandom.current().nextDouble() <= v
    }

    /**
     * 随机生成整数
     *
     * @param v 最大值
     */
    fun random(v: Int): Int {
        return ThreadLocalRandom.current().nextInt(v)
    }

    /**
     * 随机生成整数
     *
     * @param num1 最小值
     * @param num2 最大值
     */
    fun random(num1: Int, num2: Int): Int {
        val min = min(num1, num2)
        val max = max(num1, num2)
        return ThreadLocalRandom.current().nextInt(min, max + 1)
    }

    /**
     * 随机生成浮点数
     *
     * @param num1 最小值
     * @param num2 最大值
     */
    fun random(num1: Double, num2: Double): Double {
        val min = min(num1, num2)
        val max = max(num1, num2)
        return if (min == max) max else ThreadLocalRandom.current().nextDouble(min, max)
    }

    /**
     * 随机生成浮点数
     *
     * @return 0-1
     */
    fun randomDouble(): Double {
        return random().nextDouble()
    }

    @JvmStatic
    fun randomInt(a: Int, b: Int): Int {
        return random(a, b)
    }

    @JvmStatic
    fun random(a: Number, b: Number, format: String = "#.##"): String {
        return random(a.toDouble(), b.toDouble()).format(format)
    }

    private val romanNum = HashMap<Char, Int>().apply {
        put('I', 1);
        put('V', 5);
        put('X', 10);
        put('L', 50);
        put('C', 100);
        put('D', 500);
        put('M', 1000);
    }

    fun Char.romanInt(): Int {
        return romanNum[this] ?: 0
    }

    @JvmStatic
    fun romanToInt(s: String): Int {
        //数字累加
        var ans = 0;
        for (i in s.indices) {

            //取出罗马字母所对应的数字
            val value = s[i].romanInt()
            //从左往右，直接与下一位进行比较；
            //如果小于右边，直接减去
            if (i < (s.length - 1) && value < s[i + 1].romanInt()) {
                ans -= value;
            } else {
                ans += value;
            }
        }
        return ans;
    }
}