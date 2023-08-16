package com.skillw.asahi.api.member.ast

import com.skillw.asahi.api.member.ast.expr.Sized
import com.skillw.asahi.api.member.error.UnknownParserException.Companion.unknownParser
import com.skillw.asahi.api.member.parser.IParseContext
import com.skillw.asahi.api.member.parser.ParserRegistry.infix
import com.skillw.asahi.api.member.parser.ParserRegistry.prefix
import com.skillw.asahi.api.member.runtime.AsahiContext
import com.skillw.asahi.api.member.runtime.obj.AsahiObject
import com.skillw.asahi.api.member.runtime.obj.AsahiObject.Companion.toObject
import com.skillw.asahi.api.member.tokenizer.source.NoSource
import com.skillw.asahi.api.member.tokenizer.source.SourceHolder
import com.skillw.asahi.api.member.tokenizer.tokens.EndOfFile

/**
 * @className Extension
 *
 * @author Glom
 * @date 2023/8/14 0:53 Copyright 2023 user. All rights reserved.
 */

fun IParseContext.sized(value: () -> Any?): Sized =
    object : Sized, SourceHolder by source() {
        override fun eval(): AsahiObject = value().toObject(this)
        override fun serialize(): Map<String, Any> =
            eval().serialize() as? Map<String, Any>? ?: mapOf("value" to eval().toString())
    }

fun invokable(body: AsahiContext.() -> AsahiObject): Invokable = object : Invokable, SourceHolder by NoSource {
    override fun AsahiContext.invoke() = body()
    override fun serialize() = mapOf("" to "native invokable")
}

fun IParseContext.parse(priority: Int = 0): Expression {
    val token = next()
    val prefix = token.prefix() ?: unknownParser(token.toString())
    var left: Expression = prefix.parse()
    while (hasNext()) {
        if (peek() is EndOfFile) break
        val infix = peek().infix() ?: break
        next()
        if (infix.priority < priority) break
        left = infix.parse(left)
    }
    return left
}

fun Any?.serialize(): Any = if (this is Serializable) serialize() else toString()