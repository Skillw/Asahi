package com.skillw.asahi.api.member.action

import com.skillw.asahi.api.manager.AsahiManager
import com.skillw.asahi.api.member.context.ActionContext


/**
 * @className BaseAction
 *
 * @author Glom
 * @date 2022年12月27日 Copyright 2022 user. All rights reserved.
 */
abstract class BaseAction<T : Any>(val type: Class<T>) {

    constructor(type: Class<T>, vararg pairs: Pair<String, ActionExecutor<T>>) : this(type) {
        actions.putAll(pairs)
    }

    //                           后缀动作    执行内容
    internal val actions = HashMap<String, ActionExecutor<T>>()

    //  动作上下文        执行动作
    fun ActionContext.action(obj: T): Any? {
        return actions[action]?.run { executor(obj) }
    }

    fun addExec(key: String, executor: ActionContext.(T) -> Any?): BaseAction<T> {
        actions[key] = ActionExecutor { executor(it) }
        return this
    }

    fun removeExec(key: String): BaseAction<T> {
        actions.remove(key)
        return this
    }

    fun register() {
        AsahiManager.registerAction(this)
    }

    companion object {

        @JvmStatic
        fun <T : Any> createAction(clazz: Class<T>, receiver: BaseAction<T>.() -> Unit): BaseAction<T> {
            return object : BaseAction<T>(clazz) {}.apply(receiver)
        }
    }
}