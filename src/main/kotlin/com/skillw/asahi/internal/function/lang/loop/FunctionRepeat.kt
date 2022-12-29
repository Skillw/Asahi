package com.skillw.asahi.internal.function.lang.loop

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.context.ILoopContext
import com.skillw.asahi.api.member.executor.IExecutor.Companion.executor
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction


@AsahiFunction
object FunctionRepeat {

    val func = createFunction("repeat", namespaces = arrayOf("lang")) {
        val time = quest<Int>()
        val indexName = if (except("with")) quest() else executor { "index" }
        runLoop { loopOnce ->
            val indexParam = indexName.get()
            for (i in 0 until time.get()) {
                context[indexParam] = i
                when (loopOnce()) {
                    ILoopContext.Result.BREAK -> break
                    ILoopContext.Result.CONTINUE -> continue
                }
            }
        }
    }
}
