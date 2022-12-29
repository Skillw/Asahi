package com.skillw.asahi.internal.function.lang.loop

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.context.ILoopContext
import com.skillw.asahi.api.member.executor.IExecutor.Companion.executor
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction


@AsahiFunction
object FunctionContinue {

    val func = createFunction("continue", namespaces = arrayOf("lang")) {
        val labelGetter = if (except("the")) quest() else executor { (context as ILoopContext).label }
        function {
            if (this !is ILoopContext) return@function
            val label = labelGetter.get()
            searchLabel(label).apply {
                isContinue = true
                subLoops.forEach {
                    it.isBreak = true
                }
            }

        }
    }
}