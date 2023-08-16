package com.skillw.asahi.api.member.runtime.type

import com.skillw.asahi.api.member.runtime.AsahiContext
import com.skillw.asahi.api.member.runtime.obj.AsahiObject

/**
 * @className TypeHolder
 *
 * @author Glom
 * @date 2023/8/14 22:06 Copyright 2023 user. All rights reserved.
 */
interface TypeHolder : AsahiContext {
    val type: AsahiClass
    override fun clone(): TypeHolder
    fun invoke(name: String, vararg args: AsahiObject): AsahiObject
}