package com.skillw.asahi.api.member.tokenizer.tokens

import com.skillw.asahi.api.member.tokenizer.Token
import com.skillw.asahi.api.member.tokenizer.TokenType
import com.skillw.asahi.api.member.tokenizer.source.SourceHolder

/**
 * @className InvokeFunc
 *
 * @author Glom
 * @date 2023/8/13 8:59 Copyright 2023 user. All rights reserved.
 */
data class CallVar(val key: String, val holder: SourceHolder) : Token, SourceHolder by holder {
    override fun type() = TokenType.CallVar
    override fun toString(): String {
        return "\$$key"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CallVar) return false

        return key == other.key
    }

    override fun hashCode(): Int {
        return toString().hashCode()
    }

}