package com.skillw.asahi.api.member.runtime.type

import java.util.concurrent.ConcurrentHashMap

/**
 * @className ObjectClass
 *
 * @author Glom
 * @date 2023/8/14 23:07 Copyright 2023 user. All rights reserved.
 */
class JavaClass private constructor(val java: Class<*>) : AsahiClass() {
    override fun name() = "JavaClass"
    override fun toJava() = java

    companion object {
        private val cache = ConcurrentHashMap<Class<*>, JavaClass>()
        fun of(java: Class<*>): JavaClass {
            return cache.computeIfAbsent(java) {
                JavaClass(java)
            }
        }
    }
}