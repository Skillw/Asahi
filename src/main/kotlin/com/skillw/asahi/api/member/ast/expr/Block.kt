package com.skillw.asahi.api.member.ast.expr

import com.skillw.asahi.api.member.ast.Expression
import com.skillw.asahi.api.member.ast.Invokable
import com.skillw.asahi.api.member.ast.invokable
import com.skillw.asahi.api.member.runtime.AsahiContext
import com.skillw.asahi.api.member.runtime.obj.AsahiObject
import com.skillw.asahi.api.member.tokenizer.source.NoSource
import com.skillw.asahi.api.member.tokenizer.source.SourceHolder

/**
 * @className Block
 *
 * @author Glom
 * @date 2023/8/15 11:45 Copyright 2023 user. All rights reserved.
 */
class Block(
    val body: Expression,
    source: SourceHolder,
) : Invokable, SourceHolder by source,
    Expression {
    override val source: Pair<Int, Int> = body.source

    constructor(body: AsahiContext.() -> AsahiObject) : this(invokable(body), NoSource)

    override fun serialize(): Map<String, Any> = linkedMapOf(
        "type" to "Block",
        "scripts" to script.scripts.joinToString("\n", prefix = "\n", postfix = "\n"),
        "body" to body.serialize(),
    )

    override fun AsahiContext.invoke(): AsahiObject {
        return body.eval()
    }


}