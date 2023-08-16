package com.skillw.asahi.api.member.ast.expr.compound

import com.skillw.asahi.api.member.ast.Expression
import com.skillw.asahi.api.member.ast.None
import com.skillw.asahi.api.member.ast.TokenBased
import com.skillw.asahi.api.member.tokenizer.Token
import com.skillw.asahi.api.member.tokenizer.source.SourceHolder

/**
 * @className PrefixExpression
 *
 * @author Glom
 * @date 2023/8/14 15:26 Copyright 2023 user. All rights reserved.
 */
abstract class Binary(
    source: SourceHolder,
    val left: Expression = None(source),
    override val token: Token,
    val right: Expression = None(source),
) : TokenBased, SourceHolder by source {
    override fun sized() = left.sized() && right.sized()
    override fun serialize() = linkedMapOf(
        "operator" to token.toString(),
        "left" to left.serialize(),
        "right" to right.serialize()
    )
}