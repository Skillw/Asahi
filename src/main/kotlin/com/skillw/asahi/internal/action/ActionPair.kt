package com.skillw.asahi.internal.action

import com.skillw.asahi.api.annotation.AsahiAction
import com.skillw.asahi.api.member.action.BaseAction

/**
 * @className ActionMap
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AsahiAction
object ActionPair : BaseAction<Pair<*, *>>(Pair::class.java) {
    init {
        addExec("key") { it.first }
        addExec("value") { it.second }
    }

}