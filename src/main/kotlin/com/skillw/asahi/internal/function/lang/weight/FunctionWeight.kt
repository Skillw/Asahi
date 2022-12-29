package com.skillw.asahi.internal.function.lang.weight

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.executor.IExecutor
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction
import com.skillw.asahi.util.RandomList
import java.util.*


@AsahiFunction
object FunctionWeight {

    val func = createFunction("weight", namespaces = arrayOf("lang")) {
        except("[")
        val list = LinkedList<Pair<IExecutor<Int>, IExecutor<Any>>>()
        do {
            val weight = quest<Int>()
            except("to", "=", ":")
            val value = quest<Any>()
            list += weight to value
            except(",")
        } while (!except("]"))
        function {
            val randomList = RandomList<Any>()
            list.map { it.second.get() to it.first.get() }.forEach { (value, weight) ->
                randomList.add(value, weight)
            }
            return@function randomList.random()!!
        }
    }
}



