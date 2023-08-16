package com.skillw.asahi.api.member.tokenizer.source

import com.skillw.asahi.api.member.ast.Expression
import com.skillw.asahi.api.member.error.EvaluateFailedException.Companion.evalFailed
import kotlin.math.max
import kotlin.math.min

/**
 * @className Script
 *
 * @author Glom
 * @date 2023/8/15 15:29 Copyright 2023 user. All rights reserved.
 */
class Script(override val scripts: List<String> = ArrayList()) : ScriptHolder {
    override fun genError(pair: Pair<Int, Int>, message: String): String {
        val (line, index) = pair
        return genError(line, index, message)
    }

    override fun genError(line: Int, index: Int, message: String): String {
        val lineStr = scripts[line]
        return "Error occurred at line $line, index $index ,\n $message : \n" +
                scripts.subList(line, min(scripts.size, line + 4)).toMutableList().also {
                    it.add(
                        min(it.size, line + 1),
                        lineStr.substring(0, max(lineStr.length, index - 1))
                            .run {
                                " ".repeat(realLength()) + "↑"
                            })
                }.joinToString("\n") + "\n"
    }

    /** 获取字符串的实际长度 坏黑提供的函数 */
    private fun String.realLength(): Int {
        val regex = "[\u3091-\uFFe5]".toRegex()
        return sumBy { if (it.toString().matches(regex)) 2 else 1 }
    }

    override fun Expression.error(message: String): Nothing {
        evalFailed(this)
    }
}