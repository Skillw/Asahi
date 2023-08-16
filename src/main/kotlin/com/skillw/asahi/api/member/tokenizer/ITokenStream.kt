package com.skillw.asahi.api.member.tokenizer

import com.skillw.asahi.api.member.error.UnexpectedException.Companion.unexpected
import com.skillw.asahi.api.member.tokenizer.source.ScriptHolder
import com.skillw.asahi.api.member.tokenizer.source.SourceHolder
import com.skillw.asahi.core.tokenizer.TokenStream

interface ITokenStream : ScriptHolder {
    fun current(): Token
    fun next(): Token
    fun hasPrevious(): Boolean
    fun hasNext(): Boolean
    fun previous(): Token
    fun peek(): Token
    fun peekPrevious(): Token
    fun iterator(): Iterator<Token>

    fun source(): SourceHolder

    fun <T : Token> next(type: Class<T>): T
    fun <T : Token> previous(type: Class<T>): T
    fun <T : Token> nextSafely(type: Class<T>): T?
    fun <T : Token> previousSafely(type: Class<T>): T?

    fun <T : Token> expect(type: Class<T>): Boolean
    fun <T : Token> expectPre(type: Class<T>): Boolean
    fun <T : Token> expectSafely(type: Class<T>): Boolean
    fun <T : Token> expectPreSafely(type: Class<T>): Boolean


    fun expect(type: TokenType): Boolean
    fun expectPre(type: TokenType): Boolean
    fun expectSafely(type: TokenType): Boolean
    fun expectPreSafely(type: TokenType): Boolean

    fun expect(literal: String): Boolean

    companion object {

        fun create(): ITokenStream {
            return TokenStream()
        }

        inline fun <reified T : Token> ITokenStream.nextToken(): T {
            val next = if (hasNext()) next() else null
            return next as? T? ?: unexpected(T::class.java.simpleName, next?.type()?.name)
        }


        inline fun <reified T : Token> ITokenStream.currentToken(): T {
            val current = current()
            return current as? T? ?: unexpected(T::class.java.simpleName, current.type().name)
        }

        inline fun <reified T : Token> ITokenStream.previousToken(): T {
            val pre = if (hasNext()) previous() else null
            return pre as? T? ?: unexpected(T::class.java.simpleName, pre?.type()?.name)
        }


        inline fun <reified T : Token> ITokenStream.nextSafely(): T? {
            val next = if (hasNext()) next() else null
            return next as? T?
        }

        inline fun <reified T : Token> ITokenStream.previousSafely(): T? {
            val pre = if (hasPrevious()) previous() else null
            return pre as? T?
        }


        inline fun <reified T : Token> ITokenStream.expect(): Boolean {
            return if (!expectSafely<T>()) unexpected(T::class.java.simpleName, next().type().name) else true
        }

        inline fun <reified T : Token> ITokenStream.expectPre(): Boolean {
            return if (!expectPreSafely<T>()) unexpected(
                T::class.java.simpleName,
                previous().type().name
            ) else true
        }


        inline fun <reified T : Token> ITokenStream.expectSafely(): Boolean {
            val next = if (hasNext()) next() else null
            return if (next is T) {
                true
            } else {
                previous()
                false
            }
        }

        inline fun <reified T : Token> ITokenStream.expectPreSafely(): Boolean {
            val pre = if (hasPrevious()) previous() else null
            return if (pre is T) {
                true
            } else {
                next()
                false
            }
        }

    }
}