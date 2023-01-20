package com.skillw.asahi.api.member.parser.prefix.namespacing

import com.skillw.asahi.api.member.lexer.JavaLexer
import com.skillw.asahi.api.member.quest.Quester


/**
 * @className BaseJavaFunction
 *
 * @author Glom
 * @date 2022/12/25 13:39 Copyright 2022 user. All rights reserved.
 */
abstract class BaseJavaPrefix<R> : PrefixCreator<R> {
    protected abstract fun JavaLexer.parse(): Quester<R>
    override fun PrefixParser<R>.parse(): Quester<R> {
        return JavaLexer(this).parse()
    }
}