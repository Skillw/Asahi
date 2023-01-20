package com.skillw.asahi.api.member.quest

import com.skillw.asahi.api.member.context.AsahiContext

fun interface Quester<R> {
    /**
     * 在上下文中执行 (为什么Kotlin1.7.20的上下文约束还没有转正)
     *
     * @return R
     * @receiver AsahiContext
     */
    fun AsahiContext.execute(): R

    fun run(context: AsahiContext): R {
        return context.run { execute() }
    }

    fun get(context: AsahiContext): R {
        return context.execute()
    }

}