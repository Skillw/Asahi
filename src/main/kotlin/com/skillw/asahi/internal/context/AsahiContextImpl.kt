package com.skillw.asahi.internal.context

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.script.AsahiEngineFactory
import com.skillw.asahi.api.script.NativeFunction
import com.skillw.asahi.util.getDeep
import com.skillw.asahi.util.putDeep
import com.skillw.asahi.util.safe
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.LinkedBlockingDeque


/**
 * @className AsahiContextImpl
 *
 * @author Glom
 * @date 2022/12/24 15:05 Copyright 2022 user. All rights reserved.
 */
internal class AsahiContextImpl private constructor(
    private val data: MutableMap<String, Any> = HashMap(),
) : AsahiContext, MutableMap<String, Any> by data {

    private constructor(other: AsahiContext) : this(HashMap()) {
        putAll(other)
    }

    private val onExit: Deque<() -> Unit> = LinkedBlockingDeque()

    override val functions = HashMap<String, NativeFunction>()

    override val entries: MutableSet<MutableMap.MutableEntry<String, Any>>
        get() = data.entries
    override val keys: MutableSet<String>
        get() = data.keys
    override val values: MutableCollection<Any>
        get() = data.values
    override val size: Int
        get() = data.size

    override fun get(key: String): Any? {
        return if (key.contains(".")) getDeep(key) else data[key]
    }

    override fun put(key: String, value: Any): Any? {
        return if (key.contains(".")) putDeep(key, value) else data[key] = value
    }

    override fun putDeep(key: String, value: Any): Any? {
        return data.putDeep(key, value)
    }

    override fun getDeep(key: String): Any? {
        return data.getDeep(key)
    }

    override fun putAll(from: Map<out String, Any>) {
        if (from is AsahiContext) {
            functions.putAll(from.functions)
        }
        data.putAll(from)
    }

    override fun putAllIfExists(map: Map<String, Any>) {
        map.forEach { (key, value) ->
            if (containsKey(key)) put(key, value)
        }
    }

    override fun hasNativeFunction(key: String): Boolean {
        return functions.containsKey(key)
    }

    override fun addFunction(function: NativeFunction) {
        functions[function.key] = function
    }

    override fun invoke(key: String, vararg params: Any?): Any? {
        val func = functions[key] ?: error("No such function called $key")
        return func.invoke(this, *params)
    }

    override fun import(vararg paths: String) {
        paths.forEach {
            AsahiEngineFactory.search(it)?.let { script ->
                putAll(script.engine.context())
            }
        }
    }

    override fun <R : Any> select(obj: R): R {
        put("@selector", obj)
        return obj
    }


    override fun clone(): AsahiContext {
        return AsahiContextImpl(this)
    }

    override fun onExit(exec: () -> Unit) {
        onExit.addFirst(exec)
    }

    override fun <R> CompletableFuture<R>.autoCancelled(): CompletableFuture<R> {
        onExit { cancel(true) }
        return this
    }

    private var exit = false
    override fun isExit(): Boolean {
        return exit
    }

    override fun exit() {
        while (onExit.isNotEmpty()) {
            safe { onExit.pollFirst()() }
        }
        exit = true
    }

    private var debug = false
    override fun debugOn() {
        debug = true
    }

    override fun debugOff() {
        debug = false
    }

    override fun ifDebug(todo: () -> Unit) {
        if (debug) todo()
    }

    override fun <R> R.ifDebug(todo: (R) -> Unit): R {
        if (debug) todo(this)
        return this
    }

    override fun <R> temp(key: String, value: Any, todo: () -> R): R {
        val origin = get(key)
        put(key, value)
        val result = todo()
        remove(key)
        origin?.let { put(key, it) }
        return result
    }

    override fun reset() {
        exit = false
    }

    override fun toString(): String {
        return "Variables: $data \n Functions: $functions"
    }

    companion object {
        //给脚本用的 不用填参数
        fun create(): AsahiContextImpl {
            return AsahiContextImpl()
        }

        fun create(
            data: MutableMap<String, Any> = HashMap(),
        ): AsahiContextImpl {
            return AsahiContextImpl(data)
        }
    }
}