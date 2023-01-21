package com.skillw.asahi.api.script

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.internal.script.NativeFunctionImpl

abstract class NativeFunction(val key: String, val paramNames: Array<String>, val content: AsahiCompiledScript) {
    abstract fun invoke(context: AsahiContext, vararg params: Any?): Any?

    override fun toString(): String {
        return "Asahi NativeFunction $key { Params: ${paramNames.toList()} Content: ${content.rawScript()} }"
    }

    companion object {
        @JvmStatic
        fun create(
            key: String,
            paramNames: Array<String>,
            content: AsahiCompiledScript,
            initial: AsahiContext.() -> Unit = {},
        ): NativeFunction {
            return NativeFunctionImpl.create(key, paramNames, content, initial)
        }

        @JvmStatic
        fun deserialize(key: String, content: String, vararg namespaces: String): NativeFunction {
            return NativeFunctionImpl.deserialize(key, content, *namespaces)
        }

        @JvmStatic
        fun deserialize(key: String, map: Map<String, Any>, vararg namespaces: String): NativeFunction {
            return NativeFunctionImpl.deserialize(key, map, *namespaces)
        }
    }

    abstract val paramCount: Int
}