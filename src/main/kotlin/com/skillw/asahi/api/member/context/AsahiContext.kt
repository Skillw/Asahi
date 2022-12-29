package com.skillw.asahi.api.member.context

import com.skillw.asahi.api.member.executor.IExecutor
import com.skillw.asahi.api.member.function.ScriptFunction
import javax.script.Bindings

/**
 * @className AsahiContext
 *
 * @author Glom
 * @date 2022/12/24 15:05 Copyright 2022 user. All rights reserved.
 */
open class AsahiContext(
    val namespaces: Array<String> = arrayOf("common"),
    private val data: HashMap<String, Any> = HashMap(),
) :
    Bindings,
    MutableMap<String, Any> by data {
    constructor(other: AsahiContext) : this(other.namespaces, HashMap(other.data))

    internal val functions = HashMap<String, ScriptFunction>()

    fun hasFunction(key: String): Boolean {
        return functions.containsKey(key)
    }

    fun addFunction(function: ScriptFunction) {
        functions[function.key] = function
    }

    fun invoke(key: String, vararg params: Any?): Any? {
        return functions[key]?.invoke(*params)
    }

    val context by lazy(LazyThreadSafetyMode.NONE) { this }
    fun <R> IExecutor<R>.run(): R {
        return execute()
    }

    fun <R> IExecutor<R>.get(): R {
        return execute()
    }
}