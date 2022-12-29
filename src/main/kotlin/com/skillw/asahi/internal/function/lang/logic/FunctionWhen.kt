package com.skillw.asahi.internal.function.lang.logic

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.executor.IExecutor
import com.skillw.asahi.api.member.executor.IExecutor.Companion.executor
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction
import com.skillw.asahi.util.TypeUtils
import java.util.*

/**
 * @className FunctionAll
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */

@AsahiFunction
object FunctionWhen {

    val func = createFunction("when", "switch", namespaces = arrayOf("lang")) {
        val value = if (except("of")) quest<Any>() else null
        val pairs = LinkedList<Pair<IExecutor<Boolean>, IExecutor<Any>>>()
        except("{")
        while (except("case", "when")) {
            value?.let {
                val symbol = next()
                val other = quest<Any>()
                except("->")
                pairs.add(executor { TypeUtils.check(value, symbol, other) } to quest())
            } ?: kotlin.run {
                val bool = quest<Boolean>()
                except("->")
                val ifTrue = quest<Any>()
                pairs.add(bool to ifTrue)
            }
        }
        if (except("else", "default")) {
            except("->")
            val final = quest<Any>()
            pairs.add(executor { true } to final)
        }
        function {
            pairs.forEach {
                if (it.first.get()) {
                    return@function it.second.get()
                }
            }
        }
    }
}