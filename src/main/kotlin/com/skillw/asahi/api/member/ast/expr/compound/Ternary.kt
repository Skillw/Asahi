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
abstract class Ternary(
    source: SourceHolder,
    override val token: Token,
    val left: Expression,
    val middle: Expression,
    val right: Expression,
) : TokenBased, SourceHolder by source {
    override fun sized() = left.sized() && middle.sized() && right.sized()
    override fun serialize() = linkedMapOf(
        "operator" to token.toString(),
        "left" to left.serialize(),
        "middle" to middle.serialize(),
        "right" to right.serialize(),
    )
}