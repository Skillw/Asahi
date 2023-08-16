package com.skillw.asahi.api.member.ast.expr

import com.skillw.asahi.api.member.ast.Expression
import com.skillw.asahi.api.member.tokenizer.source.SourceHolder

/**
 * @className DefFunction
 *
 * @author Glom
 * @date 2023/8/15 16:26 Copyright 2023 user. All rights reserved.
 */
class DefVar(
    val name: String,
    val value: Expression,
    source: SourceHolder,
) :
    Expression, SourceHolder by source {
    override fun serialize() = linkedMapOf(
        "type" to "DefVar",
        "name" to name,
        "value" to value.serialize(),
    )
}