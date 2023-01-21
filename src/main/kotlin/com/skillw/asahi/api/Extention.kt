package com.skillw.asahi.api

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.lexer.AsahiLexer
import com.skillw.asahi.api.member.parser.infix.InfixParser
import com.skillw.asahi.api.member.parser.prefix.TopPrefixParser.Companion.tryLangParse
import com.skillw.asahi.api.member.parser.prefix.namespacing.PrefixCreator
import com.skillw.asahi.api.member.parser.prefix.namespacing.PrefixParser
import com.skillw.asahi.api.member.parser.prefix.type.TypeParser
import com.skillw.asahi.api.member.quest.LazyQuester
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.api.member.quest.VarBeanQuester
import com.skillw.asahi.api.script.AsahiScriptException
import com.skillw.asahi.util.cast
import com.skillw.asahi.util.castSafely

/**
 * @className ReaderExt
 *
 * @author Glom
 * @date 2023/1/13 20:40 Copyright 2023 user. All rights reserved.
 */

/**
 * Quest
 *
 * @param R
 * @return
 */
@Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
inline fun <reified R> AsahiLexer.questSafely(): Quester<R?> {
    val token = next()
    var getter =
        tryLangParse(token)
            ?: if (AsahiManager.hasParser(R::class.java)) {
                AsahiManager.getParser(R::class.java)?.parseWith(this)
            } else quester { token.cast() }
    getter ?: error("Cannot quest $token")
    getter = if (getter !is VarBeanQuester) InfixParser.get().parseAction(this, getter) else getter
    val index = currentIndex()
    return object : Quester<R?> {
        override fun AsahiContext.execute(): R? {
            return runCatching { getter.get().castSafely<R>() }.run {
                if (isSuccess) getOrThrow()
                else exceptionOrNull().let {
                    if (it is AsahiScriptException) throw it
                    else throw AsahiScriptException(info("Error occurred", index), it)
                }
            }
        }

        override fun toString(): String {
            return getter.toString()
        }
    }
}

@Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
inline fun <reified R> AsahiLexer.quest(): Quester<R> {
    return questSafely<R>() as Quester<R>
}

fun <R> quester(quest: AsahiContext.() -> R): Quester<R> {
    return Quester { quest() }
}

fun <T, R> Quester<T>.quester(quest: AsahiContext.(T) -> R): Quester<R> {
    return Quester { quest(get()) }
}


fun <R> lazyQuester(quest: AsahiContext.() -> R): LazyQuester<R> {
    return LazyQuester { quest() }
}

fun <R> prefixParser(
    compileFunc: PrefixParser<R>.() -> Quester<R>,
): PrefixCreator<R> {
    return object : PrefixCreator<R> {
        override fun PrefixParser<R>.parse(): Quester<R> {
            return compileFunc()
        }
    }
}

fun <R> typeParser(
    vararg types: Class<*>,
    quest: AsahiLexer.() -> Quester<R>,
): TypeParser<R> {
    return object : TypeParser<R>(*types) {
        override fun AsahiLexer.parse() = quest()
    }
}



