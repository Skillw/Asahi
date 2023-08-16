@file:Suppress("UNCHECKED_CAST")

package com.skillw.asahi.core.inject

import com.skillw.asahi.api.annotation.AsahiMember
import com.skillw.asahi.api.member.util.infix.InfixCreator
import com.skillw.asahi.api.member.util.prefix.PrefixCreator
import com.skillw.asahi.util.getClasses

object AsahiLoader {
    fun init(vararg paths: String) {
        paths.forEach { path ->
            getClasses(path).forEach { it.inject() }
        }
    }

    fun Class<*>.inject() {
        declaredMethods.forEach { method ->
            if (!method.isAnnotationPresent(AsahiMember::class.java)) return
            method.isAccessible = true
            when (method.returnType) {
                PrefixCreator::class.java -> {
                    val creator = method.invoke(null) as PrefixCreator
                    creator.create().register()
                }

                InfixCreator::class.java -> {
                    val creator = method.invoke(null) as InfixCreator
                    creator.create().register()
                }
            }
        }
    }
}
