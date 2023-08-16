package com.skillw.asahi.api.member.runtime

import com.skillw.asahi.api.member.runtime.obj.AsahiObject
import com.skillw.asahi.api.member.runtime.obj.basic.Function

/**
 * @className FunctionHolder
 *
 * @author Glom
 * @date 2023/8/15 11:07 Copyright 2023 user. All rights reserved.
 */
interface FunctionHolder {
    fun functions(): Map<String, Function>
    fun addFunction(function: Function)
    fun clone(): FunctionHolder
    fun function(name: String, params: Array<String>, body: AsahiContext.() -> AsahiObject): Function
}