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
object ActionBool : BaseAction<Boolean>(Boolean::class.java) {
    init {
        addExec("?") { bool ->
            if (bool) {
                val result = parse<Any>()
                except(":")
                skip()
                result
            } else {
                skip()
                except(":")
                parse<Any>()
            }
        }
    }
}