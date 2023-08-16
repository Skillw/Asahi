@file:Suppress("UNCHECKED_CAST")

package com.skillw.asahi.api.member.util.infix

import com.skillw.asahi.api.member.ast.Expression
import com.skillw.asahi.api.member.parser.IParseContext
import com.skillw.asahi.api.member.parser.ParserRegistry
import com.skillw.asahi.api.member.parser.infix.InfixParser
import com.skillw.asahi.api.member.runtime.AsahiContext
import com.skillw.asahi.api.member.runtime.Evaluator
import com.skillw.asahi.api.member.runtime.EvaluatorRegistry
import com.skillw.asahi.api.member.runtime.ExpressionKey
import com.skillw.asahi.api.member.runtime.ExpressionKey.Companion.toKey
import com.skillw.asahi.api.member.runtime.obj.AsahiObject
import com.skillw.asahi.api.member.tokenizer.Token
import com.skillw.asahi.api.member.tokenizer.TokenKey
import com.skillw.asahi.api.member.tokenizer.TokenKey.Companion.toKey
import com.skillw.asahi.api.member.tokenizer.TokenType
import com.skillw.asahi.api.member.util.ParserEnv

class Infix {

    private val infixes = HashMap<TokenKey, InfixParser>()
    private val evaluators = HashMap<ExpressionKey, Evaluator<out Expression>>()
    fun parser(
        vararg tokens: Token,
        type: TokenType = TokenType.EOF,
        priority: Int = type.priority,
        parser: ParserEnv.(Expression) -> Expression,
    ): Infix {
        val infix = object : InfixParser {
            override var priority: Int = priority
            override fun parse(context: IParseContext, left: Expression) =
                context.run { ParserEnv(priority, context).parser(left) }
        }
        tokens.forEach {
            infixes[it.toKey()] = infix
        }
        if (type != TokenType.EOF) {
            infixes[type.toKey()] = infix
        }
        return this
    }

    fun <E : Expression> evaluator(
        vararg tokens: Token,
        type: TokenType = TokenType.EOF,
        eval: AsahiContext.(E) -> AsahiObject,
    ): Infix {
        val evaluator = object : Evaluator<E> {
            override fun E.evaluate(context: AsahiContext) = context.eval(this)
        }
        tokens.forEach {
            evaluators[it.toKey().toKey()] = evaluator
        }
        if (type != TokenType.EOF) {
            evaluators[type.toKey().toKey()] = evaluator
        }
        return this
    }

    fun <E : Expression> evaluator(type: Class<E>, eval: AsahiContext.(E) -> AsahiObject): Infix {
        evaluators[type.toKey()] = object : Evaluator<E> {
            override fun E.evaluate(context: AsahiContext) = context.eval(this)
        }
        return this
    }

    fun register() {
        infixes.forEach {
            ParserRegistry.register(it.key, it.value)
        }
        evaluators.forEach {
            EvaluatorRegistry.register(it.key, it.value)
        }
    }
}