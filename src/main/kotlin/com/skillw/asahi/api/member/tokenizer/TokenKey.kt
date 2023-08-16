package com.skillw.asahi.api.member.tokenizer

/**
 * @className TokenKey
 *
 * @author Glom
 * @date 2023/8/14 18:01 Copyright 2023 user. All rights reserved.
 */
class TokenKey private constructor(val source: Any) {

    val priority: Int
        get() = if (source is TokenType) source.priority else -1

    override fun equals(other: Any?): Boolean {
        return other is TokenKey && other.hashCode() == hashCode()
    }

    override fun hashCode(): Int {
        return source.hashCode()
    }

    override fun toString(): String {
        return source.toString()
    }

    companion object {
        fun Token.toKey(): TokenKey = TokenKey(this)
        fun TokenType.toKey(): TokenKey = TokenKey(this)
    }
}