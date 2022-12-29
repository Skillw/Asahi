package com.skillw.asahi.internal.function.lang.define

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction
import com.skillw.asahi.api.member.function.BaseFunction.ILazy

@AsahiFunction
object FunctionSet {

    val func = createFunction("set", namespaces = arrayOf("lang")) {
        val key = next()
        when {
            except("ifndef") -> {
                function { if (containsKey(key)) return@function context[key] else null }
            }

            except("by") && except("lazy") -> {
                except("to", "=");

                val block = quest<Any>()
                function {
                    ILazy {
                        block.get().also { result -> context[key] = result }
                    }.also { context[key] = it }
                }
            }

            else -> {
                except("to", "=")
                val value = quest<Any?>()
                function { value.get()?.let { context[key] = it } ?: context.remove(key) }
            }
        }
    }
}