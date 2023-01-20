package com.skillw.asahi.internal.namespacing.infix.linking

import com.skillw.asahi.api.annotation.AsahiInfix
import com.skillw.asahi.api.member.parser.infix.namespacing.BaseInfix

/**
 * @className ActionString
 *
 * @author Glom
 * @date 2022年12月13日14点47分 Copyright 2022 user. All rights reserved.
 */
@AsahiInfix
object ActionString : BaseInfix<String>(String::class.java) {
    init {
        infix("[") { key ->
            if (!hasNativeFunction(key)) return@infix null
            val params = parse<Array<Any?>>()
            invoke(key, *params)
        }
        infix("(") { key ->
            if (!hasNativeFunction(key)) return@infix null
            val params = parse<Array<Any?>>()
            invoke(key, *params)
        }
    }
}