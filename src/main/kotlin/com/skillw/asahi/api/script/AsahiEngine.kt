package com.skillw.asahi.api.script

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.internal.context.AsahiContextImpl
import com.skillw.asahi.internal.script.AsahiEngineImpl
import java.io.File
import javax.script.AbstractScriptEngine
import javax.script.Compilable
import javax.script.Invocable

abstract class AsahiEngine(context: AsahiContext) : AbstractScriptEngine(context), Compilable, Invocable {
    companion object {
        @JvmStatic
        fun create(
            factory: AsahiEngineFactory,
            context: AsahiContext = AsahiContextImpl.create(),
        ): AsahiEngine {
            return AsahiEngineImpl.create(factory, context)
        }
    }

    abstract fun compile(file: File, vararg namespaces: String): AsahiCompiledScript

    abstract override fun compile(script: String): AsahiCompiledScript
    abstract fun compile(script: String, vararg namespaces: String): AsahiCompiledScript
    abstract fun compile(tokens: Collection<String>, vararg namespaces: String): AsahiCompiledScript

    abstract fun context(): AsahiContext
    abstract fun global(): AsahiContext
}