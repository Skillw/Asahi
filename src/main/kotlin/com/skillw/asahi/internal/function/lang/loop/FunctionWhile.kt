package com.skillw.asahi.internal.function.lang.loop

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.context.ILoopContext
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction


@AsahiFunction
object FunctionWhile {

    val func = createFunction("while", namespaces = arrayOf("lang")) {
        val condition = quest<Boolean>()
        runLoop { loopOnce ->
            while (condition.get()) {
                when (loopOnce()) {
                    ILoopContext.Result.BREAK -> break
                    ILoopContext.Result.CONTINUE -> continue
                }
            }
        }
    }
}
