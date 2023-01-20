package com.skillw.asahi.api.member.context

import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.api.script.NativeFunction
import com.skillw.asahi.internal.context.AsahiContextImpl
import java.util.concurrent.CompletableFuture
import javax.script.Bindings

interface AsahiContext : MutableMap<String, Any>, Bindings {
    val functions: HashMap<String, NativeFunction>
    fun putDeep(key: String, value: Any): Any?
    fun getDeep(key: String): Any?
    fun putAllIfExists(map: Map<String, Any>)
    fun hasNativeFunction(key: String): Boolean
    fun addFunction(function: NativeFunction)

    fun invoke(key: String, vararg params: Any?): Any?

    fun import(vararg paths: String)

    fun select(obj: Any)
    fun <R> selector(): R {
        return selectorSafely() ?: error("No Required Selector Selected!")
    }

    fun <R> AsahiContext.selectorSafely(): R? {
        return this["@selector"] as? R?
    }

    fun <R> Quester<R>.run(): R {
        return execute()
    }

    fun <R> Quester<R>.get(): R {
        return execute()
    }

    fun clone(): AsahiContext
    fun context(): AsahiContext {
        return this
    }

    fun onExit(exec: () -> Unit)

    fun <R> CompletableFuture<R>.autoCancelled(): CompletableFuture<R>

    fun exit()
    fun reset()
    fun isExit(): Boolean
    fun debugOn()
    fun debugOff()
    fun ifDebug(todo: () -> Unit)
    fun <R> R.ifDebug(todo: (R) -> Unit): R


    companion object {
        //给脚本用的 不用填参数
        @JvmStatic
        fun create(): AsahiContext {
            return AsahiContextImpl.create()
        }

        @JvmStatic
        fun create(
            data: MutableMap<String, Any> = HashMap(),
        ): AsahiContext {
            return AsahiContextImpl.create(data)
        }
    }
}