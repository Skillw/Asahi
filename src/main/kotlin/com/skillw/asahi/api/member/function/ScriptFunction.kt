package com.skillw.asahi.api.member.function

import com.skillw.asahi.api.manager.AsahiManager.compile
import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.executor.ExecutorCompound

/**
 * @className ScriptFunction
 *
 * @author Glom
 * @date 2022/12/28 21:24 Copyright 2022 user. All rights reserved.
 */
class ScriptFunction constructor(
    val key: String,
    val paramNames: Array<String>,
    val exec: ExecutorCompound,
    val context: AsahiContext,
) {
    constructor(key: String, paramNames: Array<String>, content: String, context: AsahiContext) : this(
        key,
        paramNames,
        content.compile(),
        context
    )

    val typeCount = paramNames.size

    fun invoke(vararg params: Any?): Any? {
        if (params.size != typeCount) {
            throw IllegalArgumentException("Wrong params count! (${params.size} / $typeCount)")
        }
        val invokeContext = AsahiContext(context)
        paramNames.forEachIndexed { index, name ->
            invokeContext[name] = params[index] ?: "null"
        }
        return invokeContext.run { exec.run() }.also {
            invokeContext.forEach { key, value ->
                if (key !in paramNames && context.containsKey(key)) context[key] = value
            }
        }

    }
}