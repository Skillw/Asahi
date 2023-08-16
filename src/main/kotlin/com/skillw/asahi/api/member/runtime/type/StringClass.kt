package com.skillw.asahi.api.member.runtime.type

/**
 * @className ObjectClass
 *
 * @author Glom
 * @date 2023/8/14 23:07 Copyright 2023 user. All rights reserved.
 */
object StringClass : AsahiClass() {
    override fun name(): String = "StringClass"
    override fun toJava(): Class<*> = String::class.java
}