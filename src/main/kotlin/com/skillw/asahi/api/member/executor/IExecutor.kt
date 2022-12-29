package com.skillw.asahi.api.member.executor

import com.skillw.asahi.api.member.context.AsahiContext

fun interface IExecutor<R> {
    fun AsahiContext.execute(): R

    companion object {
        @JvmStatic
        fun <R> executor(exec: AsahiContext.() -> R): IExecutor<R> {
            return IExecutor { exec() }
        }
    }
}