package com.skillw.asahi.api.member.lexer

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.quest.Quester

/**
 * @className JavaFuncReader
 *
 * @author Glom
 * @date 2023/1/15 13:18 Copyright 2023 user. All rights reserved.
 */
class JavaLexer(reader: AsahiLexer) : AsahiLexer by reader {
    fun <R> questAs(): Quester<R> {
        return questObj() as Quester<R>
    }

    fun <R> result(exec: AsahiContext.() -> R): Quester<R> {
        return Quester { exec() }
    }
}