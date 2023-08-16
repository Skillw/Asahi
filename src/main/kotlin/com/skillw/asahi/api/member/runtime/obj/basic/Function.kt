package com.skillw.asahi.api.member.runtime.obj.basic

import com.skillw.asahi.api.member.ast.Invokable
import com.skillw.asahi.api.member.runtime.AsahiContext
import com.skillw.asahi.api.member.runtime.Var
import com.skillw.asahi.api.member.runtime.obj.AsahiObject
import com.skillw.asahi.api.member.runtime.type.FunctionClass
import com.skillw.asahi.api.member.tokenizer.source.ScriptHolder

/**
 * @className Function
 *
 * @author Glom
 * @date 2023/8/13 12:39 Copyright 2023 user. All rights reserved.
 */
class Function(val name: String, vararg val params: String, val body: Invokable, private val base: AsahiContext) :
    AsahiObject(FunctionClass, body),
    Invokable by body {
    fun invoke(context: AsahiContext, vararg params: AsahiObject): AsahiObject {
        val runContext = context.combine(base).apply {
            this@Function.params.forEachIndexed { index, key ->
                variables()[key] = Var(key, params[index])
            }
        }
        return body.run { runContext.invoke() }
    }


    override val script: ScriptHolder
        get() = body.script
    override val source: Pair<Int, Int>
        get() = body.source

    override fun genError(message: String): String {
        return body.genError(message)
    }

    override fun serialize(): Map<String, Any> {
        return body.serialize()
    }
}