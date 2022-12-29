package com.skillw.asahi.internal.action

import com.skillw.asahi.api.annotation.AsahiAction
import com.skillw.asahi.api.member.action.BaseAction

/**
 * @className ActionBool
 *
 * ActionPlayer
 *
 * @author Glom
 * @date 2022年12月13日14点47分 Copyright 2022 user. All rights reserved.
 */
@AsahiAction
object ActionString : BaseAction<String>(String::class.java) {
    init {
        addExec("[") { key ->
            if (!hasFunction(key)) return@addExec null
            val params = parse<Array<Any?>>()
            invoke(key, *params)
        }
        addExec("(") { key ->
            if (!hasFunction(key)) return@addExec null
            val params = parse<Array<Any?>>()
            invoke(key, *params)
        }
    }
}