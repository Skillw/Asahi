package com.skillw.asahi.api.member.parser.infix.namespacing

import com.skillw.asahi.api.member.context.InfixContext


/**
 * @className ActionExecutor
 *
 * @author Glom
 * @date 2022年12月27日 8:36 Copyright 2022 user. All rights reserved.
 */
fun interface InfixExecutor<T : Any> {
    fun InfixContext.execute(obj: T): Any?

    @Suppress("UNCHECKED_CAST")
    fun run(context: InfixContext, obj: Any): Any? {
        return context.run { execute(obj as? T ?: return null) }
    }
}