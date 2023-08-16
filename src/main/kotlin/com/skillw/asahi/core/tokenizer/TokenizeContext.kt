package com.skillw.asahi.core.tokenizer

import com.skillw.asahi.api.member.tokenizer.Token
import com.skillw.asahi.api.member.tokenizer.source.Script
import com.skillw.asahi.api.member.tokenizer.source.Source
import com.skillw.asahi.api.member.tokenizer.source.SourceHolder

/**
 * @className TokenizeContext
 *
 * @author Glom
 * @date 2023/8/13 18:51 Copyright 2023 user. All rights reserved.
 */
data class TokenizeContext(
    val script: Script = Script(),
    val tokens: ArrayList<Token> = ArrayList(),
    val builder: StringBuilder = java.lang.StringBuilder(),
) {
    fun add(token: Token) {
        tokens.add(token)
    }

    fun source(pair: Pair<Int, Int>): SourceHolder {
        return Source(script, pair)
    }

    infix fun Int.to(index: Int) = source(Pair(this, index))
}