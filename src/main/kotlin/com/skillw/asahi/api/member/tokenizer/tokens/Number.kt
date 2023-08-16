package com.skillw.asahi.api.member.tokenizer.tokens

import com.skillw.asahi.api.member.tokenizer.Token
import com.skillw.asahi.api.member.tokenizer.TokenType
import com.skillw.asahi.api.member.tokenizer.source.SourceHolder

/**
 * @className Name
 *
 * @author Glom
 * @date 2023/8/13 9:37 Copyright 2023 user. All rights reserved.
 */
data class Number(val number: Double, val holder: SourceHolder) : Token, SourceHolder by holder {
    override fun type() = TokenType.Number
    override fun toString(): String {
        return number.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Number
        return true
    }

    override fun hashCode(): Int {
        return type().hashCode()
    }
}