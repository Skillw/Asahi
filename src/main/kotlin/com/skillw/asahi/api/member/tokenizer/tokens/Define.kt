package com.skillw.asahi.api.member.tokenizer.tokens

import com.skillw.asahi.api.member.tokenizer.Token
import com.skillw.asahi.api.member.tokenizer.TokenType
import com.skillw.asahi.api.member.tokenizer.source.SourceHolder

/**
 * @className Define
 *
 * @author Glom
 * @date 2023/8/13 8:57 Copyright 2023 user. All rights reserved.
 */
data class Define(val holder: SourceHolder) : Token, SourceHolder by holder {
    override fun type() = TokenType.Define

    override fun toString(): String {
        return "Define"
    }

    override fun equals(other: Any?): Boolean {
        return other is Define
    }

    override fun hashCode(): Int {
        return type().hashCode()
    }
}