package com.skillw.asahi.api.member.runtime.type

import com.skillw.asahi.api.member.runtime.obj.basic.Function

/**
 * @className ObjectClass
 *
 * @author Glom
 * @date 2023/8/14 23:07 Copyright 2023 user. All rights reserved.
 */
object FunctionClass : AsahiClass() {
    override fun name(): String = "FunctionClass"
    override fun toJava(): Class<*> = Function::class.java
}