package com.skillw.asahi.api.member.parser.prefix.namespacing

import com.skillw.asahi.api.member.lexer.AsahiLexer
import com.skillw.asahi.api.member.parser.prefix.namespacing.PrefixParser.Companion.parser
import com.skillw.asahi.api.member.quest.Quester

/**
 * @className PrefixCreator
 *
 * @author Glom
 * @date 2023/1/14 11:07 Copyright 2023 user. All rights reserved.
 */
interface PrefixCreator<R> {
    fun PrefixParser<R>.parse(): Quester<R>


    fun register(
        key: String,
        vararg alias: String,
        namespace: String = "common",
    ) {
        object : BasePrefix<R>(key, *alias, namespace = namespace) {
            override fun AsahiLexer.parse(): Quester<R> {
                return this@PrefixCreator.run { parser<R>().parse() }
            }
        }.register()
    }
}