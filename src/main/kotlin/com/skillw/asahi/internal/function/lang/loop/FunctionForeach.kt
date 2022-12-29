package com.skillw.asahi.internal.function.lang.loop

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.context.ILoopContext
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction


@AsahiFunction
object FunctionForeach {


    val func = createFunction("foreach", namespaces = arrayOf("lang")) {
        val paramName = next()
        except("in")
        val getter = quest<Any>()
        runLoop { loopOnce ->
            when (val obj = getter.get()) {
                is Array<*> -> {
                    for (item in obj) {
                        context[paramName] = item ?: continue
                        when (loopOnce()) {
                            ILoopContext.Result.BREAK -> break
                            ILoopContext.Result.CONTINUE -> continue
                        }
                    }
                }

                is Collection<*> -> {
                    for (item in obj) {
                        context[paramName] = item ?: continue
                        when (loopOnce()) {
                            ILoopContext.Result.BREAK -> break
                            ILoopContext.Result.CONTINUE -> continue
                        }
                    }
                }
            }
        }
    }
}
