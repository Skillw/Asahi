package com.skillw.asahi.api.member.tokenizer.source

/**
 * @className SourceHolder
 *
 * @author Glom
 * @date 2023/8/14 18:57 Copyright 2023 user. All rights reserved.
 */
interface SourceHolder {
    val script: ScriptHolder
    val source: Pair<Int, Int>
    fun genError(message: String = ""): String
}