@file:Suppress("UNCHECKED_CAST")

package com.skillw.asahi.core.tokenizer

import com.skillw.asahi.api.member.error.UnexpectedException.Companion.unexpected
import com.skillw.asahi.api.member.tokenizer.ITokenStream
import com.skillw.asahi.api.member.tokenizer.ITokenStream.Companion.nextToken
import com.skillw.asahi.api.member.tokenizer.Token
import com.skillw.asahi.api.member.tokenizer.TokenType
import com.skillw.asahi.api.member.tokenizer.source.Script
import com.skillw.asahi.api.member.tokenizer.source.ScriptHolder
import com.skillw.asahi.api.member.tokenizer.source.Source
import com.skillw.asahi.api.member.tokenizer.source.SourceHolder
import com.skillw.asahi.api.member.tokenizer.tokens.EndOfFile
import com.skillw.asahi.api.member.tokenizer.tokens.Literal


/**
 * @className TokenStream
 *
 * @author Glom
 * @date 2023/8/13 9:00 Copyright 2023 user. All rights reserved.
 */
class TokenStream(
    private val tokens: ArrayList<Token> = ArrayList(),
    private val script: Script = Script(),
) :
    Iterable<Token>, ITokenStream, ScriptHolder by script {
    private var cursor: Int = 0
    override fun current(): Token = if (cursor in 0..tokens.size) tokens[cursor - 1] else EndOfFile(source())
    override fun next(): Token = if (hasNext()) tokens[cursor++] else EndOfFile(source())
    override fun hasPrevious() = cursor > 0
    override fun hasNext() = cursor + 1 < tokens.size
    override fun previous(): Token = if (hasPrevious()) tokens[--cursor] else EndOfFile(source())
    override fun peek(): Token = if (hasNext()) tokens[cursor] else EndOfFile(source())
    override fun peekPrevious(): Token = if (hasPrevious()) tokens[cursor - 1] else EndOfFile(source())
    override fun iterator(): Iterator<Token> {
        return object : Iterator<Token> {
            override fun hasNext(): Boolean = this@TokenStream.hasNext()
            override fun next(): Token = this@TokenStream.next()
        }
    }

    override fun source(): SourceHolder = Source(script, current().source)

    override fun expect(literal: String): Boolean {
        return if (nextToken<Literal>().string == literal) {
            true
        } else {
            previous()
            false
        }
    }

    override fun <T : Token> next(type: Class<T>): T {
        val next = previous()
        if (!type.isAssignableFrom(next::class.java)) {
            unexpected(type.simpleName, next.type()?.name)
        }
        return next as T
    }

    override fun <T : Token> previous(type: Class<T>): T {
        val pre = previous()
        if (!type.isAssignableFrom(pre::class.java)) {
            unexpected(type.simpleName, pre.type().name)
        }
        return pre as T
    }

    override fun <T : Token> nextSafely(type: Class<T>): T? {
        val next = next()
        return if (!type.isAssignableFrom(next::class.java)) {
            previous()
            null
        } else next as? T?
    }

    override fun <T : Token> previousSafely(type: Class<T>): T? {
        val pre = previous()
        return if (!type.isAssignableFrom(pre::class.java)) {
            next()
            null
        } else pre as? T?
    }


    override fun <T : Token> expect(type: Class<T>): Boolean {
        return if (expectSafely(type)) unexpected(type.simpleName, next().type().name)
        else true
    }

    override fun <T : Token> expectPre(type: Class<T>): Boolean {
        return if (expectPreSafely(type)) unexpected(type.simpleName, previous().type().name)
        else true
    }


    override fun <T : Token> expectSafely(type: Class<T>): Boolean {
        val next = next()
        return if (!type.isAssignableFrom(next::class.java)) {
            previous()
            false
        } else true
    }

    override fun <T : Token> expectPreSafely(type: Class<T>): Boolean {
        val pre = previous()
        return if (!type.isAssignableFrom(pre::class.java)) {
            next()
            false
        } else true
    }

    override fun expect(type: TokenType): Boolean {
        val next = next()
        return if (next.type() != type) unexpected(type.name, next.type().name) else true
    }

    override fun expectPre(type: TokenType): Boolean {
        val pre = previous()
        return if (pre.type() != type) unexpected(type.name, pre.type().name) else true
    }

    override fun expectSafely(type: TokenType): Boolean {
        val pre = previous()
        return if (pre.type() != type) {
            next()
            false
        } else true
    }

    override fun expectPreSafely(type: TokenType): Boolean {
        val pre = previous()
        return if (pre.type() != type) {
            next()
            false
        } else true
    }


    override fun toString(): String {
        return tokens.joinToString(" ")
    }
}