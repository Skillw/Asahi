package com.skillw.asahi.api.member.runtime.type

import com.skillw.asahi.api.member.ast.expr.Block
import com.skillw.asahi.api.member.runtime.AsahiContext
import com.skillw.asahi.api.member.runtime.FunctionHolder
import com.skillw.asahi.api.member.runtime.obj.AsahiObject
import com.skillw.asahi.api.member.runtime.obj.AsahiObject.Companion.toObject
import com.skillw.asahi.api.member.runtime.obj.NullObject
import com.skillw.asahi.api.member.runtime.obj.basic.Function
import com.skillw.asahi.api.member.tokenizer.source.Script
import com.skillw.asahi.api.member.tokenizer.source.ScriptHolder
import com.skillw.asahi.core.util.AsahiClassBean

/**
 * @className AsahiClass
 *
 * @author Glom
 * @date 2023/8/14 16:15 Copyright 2023 user. All rights reserved.
 */
abstract class AsahiClass(script: Script = Script(listOf("Unknown"))) : ScriptHolder by script, FunctionHolder {
    private val functions = HashMap<String, Function>()
    abstract fun name(): String
    abstract fun toJava(): Class<*>
    fun invoke(obj: AsahiContext?, name: String, vararg parameters: AsahiObject): AsahiObject {
        val function = functions[name]
        if (function != null) {
            return function.invoke(obj ?: AsahiContext.create(), *parameters)
        }
        if (obj !is AsahiObject) return NullObject
        val bean = AsahiClassBean.of(toJava())
        return bean.invoke(obj.toJava(), name, *parameters).toObject(obj)
    }

    override fun functions(): Map<String, Function> {
        return functions
    }

    override fun addFunction(function: Function) {
        functions[function.name] = function
    }

    fun extend(superType: AsahiClass) {
        superType.functions.forEach { (name, function) ->
            functions.putIfAbsent(name, function)
        }
    }

    override fun clone(): AsahiClass {
        return this
    }

    final override fun function(
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