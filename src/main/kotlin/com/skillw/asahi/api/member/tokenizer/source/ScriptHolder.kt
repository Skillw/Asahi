package com.skillw.asahi.api.member.tokenizer.source

import com.skillw.asahi.api.member.ast.Expression

/**
 * @className ScriptHolder
 *
 * @author Glom
 * @date 2023/8/13 22:12 Copyright 2023 user. All rights reserved.
 */
interface ScriptHolder {
    val scripts: List<String>
    fun genError(pair: Pair<Int, Int>, message: String = ""): String
    fun genError(line: Int, index: Int, message: String = ""): String
    fun Expression.error(message: String): Nothing
}