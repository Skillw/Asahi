package com.skillw.asahi.api.member.runtime.type

/**
 * @className ObjectClass
 *
 * @author Glom
 * @date 2023/8/14 23:07 Copyright 2023 user. All rights reserved.
 */
object ObjectClass : AsahiClass() {
    override fun name() = "ObjectClass"
    override fun toJava() = Any::class.java
}