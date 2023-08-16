package com.skillw.asahi.api.member.ast

import com.skillw.asahi.api.member.tokenizer.Token
import com.skillw.asahi.api.member.tokenizer.TokenKey.Companion.toKey
import com.skillw.asahi.api.member.tokenizer.tokens.Operator

/**
 * @className TokenBased
 *
 * @author Glom
 * @date 2023/8/14 18:58 Copyright 2023 user. All rights reserved.
 */
interface TokenBased : Expression {
    val token: Token
    fun key() = if (token is Operator) token.type().toKey() else token.toKey()
}