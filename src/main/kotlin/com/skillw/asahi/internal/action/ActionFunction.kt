package com.skillw.asahi.internal.action

import com.skillw.asahi.api.annotation.AsahiAction
import com.skillw.asahi.api.member.action.BaseAction
import com.skillw.asahi.api.member.function.ScriptFunction

/**
 * @className ActionArray
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AsahiAction
object ActionFunction : BaseAction<ScriptFunction>(ScriptFunction::class.java) {
    init {
        addExec("[") { obj ->
            val params = parse<Array<Any?>>()
            obj.invoke(*params)
        }
    }
}