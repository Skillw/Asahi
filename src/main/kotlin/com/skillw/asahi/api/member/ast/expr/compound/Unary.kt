package com.skillw.asahi.api.member.ast.expr.compound

import com.skillw.asahi.api.member.ast.Expression
import com.skillw.asahi.api.member.ast.TokenBased
import com.skillw.asahi.api.member.tokenizer.Token
import com.skillw.asahi.api.member.tokenizer.source.SourceHolder

/**
 * @className Unary
 *
 * @author Glom
 * @date 2023/8/13 11:38 Copyright 2023 user. All rights reserved.
 */
abstract class Unary(
    source: SourceHolder,
    override val token: Token,
    val right: Expression,
) : TokenBased, SourceHolder by source {
    override fun sized() = right.sized()
    override fun serialize() = linkedMapOf(
        "operator" to token.toString(),
        "right" to right.serialize(),
    )
}