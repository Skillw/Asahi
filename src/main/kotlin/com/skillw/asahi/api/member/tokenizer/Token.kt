package com.skillw.asahi.api.member.tokenizer

import com.skillw.asahi.api.member.tokenizer.source.SourceHolder

/**
 * @className Token
 *
 * @author Glom
 * @date 2023/8/13 8:46 Copyright 2023 user. All rights reserved.
 */
interface Token : SourceHolder {
    fun type(): TokenType
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
    fun <T : Token> cast() = this as T
    fun <T : Token> castSafely() = this as? T
}