@file:Suppress("UNCHECKED_CAST")

package com.skillw.asahi.api.member.util.prefix

import com.skillw.asahi.api.member.ast.Expression
import com.skillw.asahi.api.member.parser.IParseContext
import com.skillw.asahi.api.member.parser.ParserRegistry
import com.skillw.asahi.api.member.parser.prefix.PrefixParser
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

class Prefix {

    private val prefixes = HashMap<TokenKey, PrefixParser>()
    private val evaluators = HashMap<ExpressionKey, Evaluator<out Expression>>()
    fun parser(
        vararg tokens: Token,
        type: TokenType = TokenType.EOF,
        priority: Int = type.priority,
        parser: ParserEnv.() -> Expression,
    ): Prefix {
        val prefix = object : PrefixParser {
            override var priority: Int = priority
            override fun parse(context: IParseContext) =
                context.run { ParserEnv(priority, context).parser() }
        }
        tokens.forEach {
            prefixes[it.toKey()] = prefix
        }
        if (type != TokenType.EOF) {
            prefixes[type.toKey()] = prefix
        }
        return this
    }

    fun <E : Expression> evaluator(
        vararg tokens: Token,
        type: TokenType = TokenType.EOF,
        eval: AsahiContext.(E) -> AsahiObject,
    ): Prefix {
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

    fun <E : Expression> evaluator(type: Class<E>, eval: AsahiContext.(E) -> AsahiObject): Prefix {
        evaluators[type.toKey()] = object : Evaluator<E> {
            override fun E.evaluate(context: AsahiContext) = context.eval(this)
        }
        return this
    }

    fun register() {
        prefixes.forEach {
            ParserRegistry.register(it.key, it.value)
        }
        evaluators.forEach {
            EvaluatorRegistry.register(it.key, it.value)
        }
    }
}