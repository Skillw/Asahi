package com.skillw.asahi.core.runtime

import com.skillw.asahi.api.member.ast.expr.Block
import com.skillw.asahi.api.member.ast.serialize
import com.skillw.asahi.api.member.runtime.AsahiContext
import com.skillw.asahi.api.member.runtime.Var
import com.skillw.asahi.api.member.runtime.obj.AsahiObject
import com.skillw.asahi.api.member.runtime.obj.basic.Function
import com.skillw.asahi.api.member.tokenizer.ITokenStream
import com.skillw.asahi.api.member.tokenizer.source.ScriptHolder

/**
 * @className AsahiContextImpl
 *
 * @author Glom
 * @date 2023/8/14 10:37 Copyright 2023 user. All rights reserved.
 */
class AsahiContextImpl(private val tokens: ITokenStream) : AsahiContext, ScriptHolder by tokens {
    private val variables = HashMap<String, Var>()
    private val functions = HashMap<String, Function>()
    override fun variables() = variables

    override fun functions() = functions
    override fun addFunction(function: Function) {
        functions[function.name] = function
    }

    override fun clone(): AsahiContext = AsahiContextImpl(tokens).apply {
        variables.putAll(variables.mapValues { it.value.clone() })
        functions.putAll(functions)
    }

    override fun combine(other: AsahiContext): AsahiContext {
        variables().putAll(other.variables().mapValues { it.value.clone() })
        functions().putAll(other.functions())
        return this
    }

    override fun serialize() = linkedMapOf(
        "functions" to functions.map { it.serialize() },
        "variables" to variables.map { it.serialize() }
    )


    override fun function(
        name: String,
        params: Array<String>,
        body: AsahiContext.() -> AsahiObject,
    ): Function {
        val block = Block(body)
        val function = Function(name, params = params, body = block, AsahiContext.create())
        addFunction(function)
        return function
    }
}