package com.skillw.asahi.api.member.function

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.context.AsahiLoopContext
import com.skillw.asahi.api.member.context.ILoopContext
import com.skillw.asahi.api.member.reader.IAsahiReader

interface FunctionCreator<R> {
    fun <R> IAsahiReader.function(exec: AsahiContext.() -> R): BaseFunction.IFunction<R>
    fun IAsahiReader.runLoop(
        loop: AsahiLoopContext.(() -> ILoopContext.Result) -> Unit,
    ): BaseFunction.IFunction<Unit>
}
