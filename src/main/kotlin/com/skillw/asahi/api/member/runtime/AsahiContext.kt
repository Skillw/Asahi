package com.skillw.asahi.api.member.runtime

import com.skillw.asahi.api.member.ast.Expression
import com.skillw.asahi.api.member.ast.Serializable
import com.skillw.asahi.api.member.runtime.obj.AsahiObject
import com.skillw.asahi.api.member.runtime.obj.basic.Function
import com.skillw.asahi.api.member.tokenizer.ITokenStream
import com.skillw.asahi.api.member.tokenizer.source.ScriptHolder
import com.skillw.asahi.core.runtime.AsahiContextImpl

/**
 * @className AsahiContext
 *
 * @author Glom
 * @date 2023/8/13 12:14 Copyright 2023 user. All rights reserved.
 */
interface AsahiContext : ScriptHolder, FunctionHolder, Serializable {
    fun variables(): HashMap<String, Var>
    fun Function.invoke(vararg params: Any?): Any? = invoke(this@AsahiContext, *params)
    fun <A : AsahiObject> Expression.eval(): A = EvaluatorRegistry.eval(this, this@AsahiContext) as A
    override fun clone(): AsahiContext
    fun combine(other: AsahiContext): AsahiContext


    companion object {
        fun create(tokens: ITokenStream = ITokenStream.create()): AsahiContext {
            return AsahiContextImpl(tokens)
        }
    }
}