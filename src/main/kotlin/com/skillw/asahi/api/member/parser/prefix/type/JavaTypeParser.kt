package com.skillw.asahi.api.member.parser.prefix.type

import com.skillw.asahi.api.member.lexer.AsahiLexer
import com.skillw.asahi.api.member.lexer.JavaLexer
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.api.quester


/**
 * @className JavaTypeParser
 *
 * @author Glom
 * @date 2022/12/25 13:39 Copyright 2022 user. All rights reserved.
 */
abstract class JavaTypeParser<R>(
    vararg types: Class<*>,
) : TypeParser<R>(*types) {


    override fun AsahiLexer.parse(): Quester<R> {
        return JavaLexer(this).compile()
    }

    protected abstract fun JavaLexer.compile(): Quester<R>

    override fun parseWith(lexer: AsahiLexer): Quester<R> {
        val parser = JavaLexer(lexer).compile()
        return quester {
            parser.get()
        }
    }
}