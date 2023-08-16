package com.skillw.asahi.api.member.tokenizer.tokens

import com.skillw.asahi.api.member.tokenizer.Token
import com.skillw.asahi.api.member.tokenizer.TokenType
import com.skillw.asahi.api.member.tokenizer.source.SourceHolder

/**
 * @className Literal
 *
 * @author Glom
 * @date 2023/8/13 9:22 Copyright 2023 user. All rights reserved.
 */
data class Literal(val string: String, val holder: SourceHolder) : Token, SourceHolder by holder {
    override fun type() = TokenType.Literal

    override fun toString(): String {
        return string
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return when (other) {

            is Literal -> other.string == string

            else -> false
        }
    }

    override fun hashCode(): Int {
        return string.hashCode()
    }
}