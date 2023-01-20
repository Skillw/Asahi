package com.skillw.asahi.api.member.parser.prefix.namespacing

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.lexer.AsahiLexer
import com.skillw.asahi.api.member.quest.Quester

/**
 * @className FunctionCompiler
 *
 * @author Glom
 * @date 2022/12/27 19:31 Copyright 2022 user. All rights reserved.
 */
class PrefixParser<R> private constructor(
    reader: AsahiLexer,
) : AsahiLexer by reader {
    companion object {
        fun <R> AsahiLexer.parser(): PrefixParser<R> {
            return PrefixParser(this)
        }
    }

    fun <R> result(exec: AsahiContext.() -> R): Quester<R> {
        return Quester { exec() }
    }
}