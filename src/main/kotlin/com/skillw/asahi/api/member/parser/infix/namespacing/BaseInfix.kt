package com.skillw.asahi.api.member.parser.infix.namespacing

import com.skillw.asahi.api.AsahiManager
import com.skillw.asahi.api.member.AsahiRegistrable
import com.skillw.asahi.api.member.context.InfixContext
import com.skillw.asahi.api.member.namespace.Namespacing


/**
 * @className BaseAction
 *
 * @author Glom
 * @date 2022年12月27日 Copyright 2022 user. All rights reserved.
 */
abstract class BaseInfix<T : Any>(override val key: Class<out T>, override val namespace: String = "common") :
    AsahiRegistrable<Class<out T>>, Namespacing {

    constructor(type: Class<T>, vararg pairs: Pair<String, InfixExecutor<T>>) : this(type) {
        actions.putAll(pairs)
    }

    //                           后缀动作    执行内容
    internal val actions = HashMap<String, InfixExecutor<T>>()

    //  动作上下文        执行动作
    fun InfixContext.action(obj: T): Any? {
        return actions[token]?.run { execute(obj) }
    }

    infix fun String.to(executor: InfixContext.(T) -> Any?) {
        infix(this, executor = executor)
    }

    infix fun Collection<String>.to(executor: InfixContext.(T) -> Any?) {
        infix(*this.toTypedArray(), executor = executor)
    }

    infix fun Array<String>.to(executor: InfixContext.(T) -> Any?) {
        infix(*this, executor = executor)
    }

    fun infix(vararg keys: String, executor: InfixContext.(T) -> Any?): BaseInfix<T> {
        keys.forEach { key ->
            actions[key] = InfixExecutor { executor(it) }
        }
        return this
    }

    infix fun `infix`(pair: Pair<String, InfixContext.(T) -> Any?>) {
        infix(pair.first, executor = pair.second)
    }

    fun removeExec(key: String): BaseInfix<T> {
        actions.remove(key)
        return this
    }

    override fun register() {
        AsahiManager.getNamespace(namespace).registerInfix(this)
    }

    fun putAll(other: BaseInfix<*>) {
        if (other.key != key && !other.key.isAssignableFrom(key)) return
        other.actions.forEach { (key, action) ->
            infix(key) {
                action.run(this, it)
            }
        }
    }

    override fun toString(): String {
        return "AsahiInfix { $key ${actions.keys} }"
    }

    companion object {
        //给Java用的
        @JvmStatic
        fun <T : Any> createInfix(type: Class<T>, namespace: String = "common"): BaseInfix<T> {
            return object : BaseInfix<T>(type, namespace) {}
        }

        @JvmStatic
        fun <T : Any> createInfix(
            type: Class<T>,
            namespace: String = "common",
            receiver: BaseInfix<T>.() -> Unit,
        ): BaseInfix<T> {
            return object : BaseInfix<T>(type, namespace) {}.apply(receiver)
        }

        @JvmStatic
        fun <T : Any> infix(
            type: Class<T>,
            vararg keys: String,
            namespace: String = "common",
            executor: InfixContext.(T) -> Any?,
        ) {
            AsahiManager.getNamespace(namespace).getInfix(type).infix(*keys) { executor(this, it) }
        }
    }
}