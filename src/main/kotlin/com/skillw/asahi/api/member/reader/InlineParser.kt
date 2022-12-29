package com.skillw.asahi.api.member.reader

import com.skillw.asahi.api.manager.AsahiManager.compile
import com.skillw.asahi.api.member.context.AsahiContext

/**
 * @className InlineParser
 *
 * @author Glom
 * @date 2022/12/27 9:12 Copyright 2022 user. All rights reserved.
 */
class InlineParser(val text: String) {
    //          待替换的字符串 -> Asahi段内容
    private val beingReplaced = LinkedHashMap<String, String>()

    init {
        //是否在内联脚本中
        var inScript = false
        //下个字符是否转义
        var escape = false
        //缓存内联脚本字符串
        val cache = StringBuilder()
        //{ } 嵌套计数器
        var count = 0

        //将字符加入缓存
        fun push(char: Char) {
            cache.append(char)
        }

        fun pull() {
            cache.deleteAt(cache.lastIndex)
        }

        //一段内联脚本读完，将内联脚本加入map
        fun finish() {
            val script = cache.toString()
            beingReplaced["{$script}"] = script
            cache.clear()
        }

        text.forEachIndexed loop@{ index, char ->
            //如果在内联脚本里，则压入字符
            if (inScript) push(char)
            when (char) {
                //转义
                '\\' -> {
                    //如果不转义，且在内联脚本里，就删除此次压入
                    if (!escape && inScript) {
                        pull()
                    }
                    //下个字符转义
                    escape = true
                }
                //内联脚本段开始
                '{' -> {
                    when {
                        //如果转义直接跳过
                        escape -> return@loop
                        //如果在内联脚本段 则嵌套计数器+1
                        inScript -> count++
                        //开始内联函数段
                        else -> inScript = true
                    }
                }
                //内联脚本段结束
                '}' -> {
                    when {
                        //转义则跳过
                        escape -> return@loop
                        //如果嵌套计数器不为0
                        count != 0 -> count--
                        //结束内联函数段
                        else -> {
                            pull()
                            inScript = false
                            finish()
                        }
                    }
                }
            }
        }
    }

    fun replace(context: AsahiContext): String {
        var cache = text
        context.run {
            beingReplaced.forEach { (origin, script) ->
                cache = cache.replaceFirst(origin, script.compile().run().toString())
            }
        }
        return cache
    }
}