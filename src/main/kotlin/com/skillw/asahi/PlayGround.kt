package com.skillw.asahi

import com.skillw.asahi.api.member.ast.Expression
import com.skillw.asahi.api.member.ast.Invokable
import com.skillw.asahi.api.member.ast.expr.Sized
import com.skillw.asahi.api.member.ast.expr.compound.Binary
import com.skillw.asahi.api.member.ast.expr.compound.binary
import com.skillw.asahi.api.member.ast.parse
import com.skillw.asahi.api.member.parser.ParserRegistry.parse
import com.skillw.asahi.api.member.runtime.AsahiContext
import com.skillw.asahi.api.member.runtime.Evaluator
import com.skillw.asahi.api.member.runtime.EvaluatorRegistry
import com.skillw.asahi.api.member.runtime.obj.AsahiObject
import com.skillw.asahi.api.member.runtime.type.NumberClass
import com.skillw.asahi.api.member.tokenizer.ITokenStream.Companion.currentToken
import com.skillw.asahi.api.member.tokenizer.TokenType
import com.skillw.asahi.api.member.tokenizer.tokens.Operator
import com.skillw.asahi.api.member.util.infix
import com.skillw.asahi.core.inject.AsahiLoader
import com.skillw.asahi.core.tokenizer.AsahiTokenizer
import com.skillw.asahi.util.toJson
import kotlin.time.measureTime

/**
 * @className PlayerGround
 *
 * @author Glom
 * @date 2023/8/13 9:48 Copyright 2023 user. All rights reserved.
 */


private fun binaryCreator(type: TokenType) = infix {
    parser(type = type) { left ->
        val token = currentToken<Operator>()
        val right = parse(priority)
        binary(token, left, right)
    }

    evaluator<Binary>(type = type) {
        val left = it.left.eval<AsahiObject>()
        val operator = it.token.cast<Operator>().type.display
        val right = it.right.eval<AsahiObject>()
        left.invoke(operator, right)
    }
}

fun load() {
    TokenType.entries
        .filter { it.infix }
        .forEach {
            binaryCreator(it).create().register()
        }
}

fun main() {
    NumberClass.function(".", params = arrayOf("name")) {
        val name = variables()["name"]?.toJava().toString()
        variables()[name] ?: functions()[name] ?: NumberClass.invoke(this, name)
    }
    EvaluatorRegistry.register(Sized::class.java, object : Evaluator<Sized> {
        override fun Sized.evaluate(context: AsahiContext): AsahiObject = eval()
    } as Evaluator<in Expression>)
    EvaluatorRegistry.register(Invokable::class.java, object : Evaluator<Invokable> {
        override fun Invokable.evaluate(context: AsahiContext): AsahiObject = context.invoke()
    } as Evaluator<in Expression>)
    load()
    AsahiLoader.init("com.skillw.asahi")
    println("===============")
    val scripts = """
       1 + 1
    """.trimIndent()
    val tokens = AsahiTokenizer.tokenize(scripts)
    println("===============")
    val ast = parse(tokens)
    println(ast.serialize().toJson())
    println("===============")
    val context = AsahiContext.create(tokens)
    measureTime {
        println(EvaluatorRegistry.eval(ast, context))
    }.also {
        println(it)
    }
}