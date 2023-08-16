package com.skillw.asahi.api.member.ast.expr

import com.skillw.asahi.api.member.ast.Expression
import com.skillw.asahi.api.member.ast.Invokable
import com.skillw.asahi.api.member.ast.serialize
import com.skillw.asahi.api.member.tokenizer.source.SourceHolder

/**
 * @className DefFunction
 *
 * @author Glom
 * @date 2023/8/15 16:26 Copyright 2023 user. All rights reserved.
 */
class DefFunction(
    val name: String,
    val params: Array<String>,
    val body: Invokable,
    source: SourceHolder,
) :
    Expression, SourceHolder by source {
    override fun serialize() = linkedMapOf(
        "type" to "DefFunction",
        "name" to name,
        "params" to params.toList(),
        "body" to body.serialize()
    )
}