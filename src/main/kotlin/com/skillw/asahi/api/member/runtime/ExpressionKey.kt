package com.skillw.asahi.api.member.runtime

import com.skillw.asahi.api.member.ast.Expression
import com.skillw.asahi.api.member.ast.Invokable
import com.skillw.asahi.api.member.ast.TokenBased
import com.skillw.asahi.api.member.ast.expr.Sized
import com.skillw.asahi.api.member.tokenizer.TokenKey

/**
 * @className ExpressionKey
 *
 * @author Glom
 * @date 2023/8/14 19:21 Copyright 2023 user. All rights reserved.
 */
class ExpressionKey private constructor(private val hash: Int, private val display: String) {
    private constructor(expr: Expression) : this(
        when (expr) {
            is TokenBased -> expr.key().hashCode()
            is Sized -> Sized::class.java.hashCode()
            is Invokable -> Invokable::class.java.hashCode()
            else -> expr::class.java.hashCode()
        },
        when (expr) {
            is TokenBased -> expr.key().toString()
            is Sized -> Sized::class.java.simpleName
            is Invokable -> Invokable::class.java.simpleName
            else -> expr::class.java.simpleName
        }
    )

    private constructor(tokenKey: TokenKey) : this(tokenKey.hashCode(), tokenKey.toString())

    private constructor(exprClass: Class<out Expression>) : this(exprClass.hashCode(), exprClass.simpleName)

    override fun toString(): String {
        return display
    }

    override fun equals(other: Any?): Boolean {
        return other is ExpressionKey && hash == other.hash
    }

    override fun hashCode(): Int {
        return hash
    }

    companion object {
        fun Expression.toKey(): ExpressionKey = ExpressionKey(this)
        fun TokenKey.toKey(): ExpressionKey = ExpressionKey(this)
        fun Class<out Expression>.toKey(): ExpressionKey = ExpressionKey(this)
    }
}