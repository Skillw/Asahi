package com.skillw.asahi.api.member.executor

import com.skillw.asahi.api.member.context.AsahiContext
import java.util.*

/**
 * @className ExecutorCompound
 *
 * @author Glom
 * @date 2022/12/25 13:55 Copyright 2022 user. All rights reserved.
 */
open class ExecutorCompound : IExecutor<Any?> {
    private val execs = LinkedList<IExecutor<*>>()
    private var onEachFunc: AsahiContext.() -> Boolean = { context["@exit"] == true }
    fun onEach(onEachFunc: AsahiContext.() -> Boolean = { context["@exit"] == true }): ExecutorCompound {
        this.onEachFunc = onEachFunc
        return this
    }

    override fun AsahiContext.execute(): Any? {
        execs.run {
            var previous: Any? = null
            forEachIndexed { index, exec ->
                if (onEachFunc()) return previous
                exec.run().also {
                    if (index == lastIndex) return it
                    previous = it
                }
            }
        }
        return null
    }

    fun add(exec: IExecutor<*>) {
        execs.add(exec)
    }

    operator fun plusAssign(executor: IExecutor<Any>) {
        add(executor)
    }


}