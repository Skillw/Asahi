package com.skillw.asahi.api.script

import com.skillw.asahi.api.manager.AsahiManager.compile
import com.skillw.asahi.api.member.context.AsahiContext
import java.io.Reader
import javax.script.*

/**
 * @className AsahiEngine
 *
 * @author Glom
 * @date 2022/12/24 14:02 Copyright 2022 user. All rights reserved.
 */
class AsahiEngine(private val factory: AsahiEngineFactory) : AbstractScriptEngine(), Compilable, Invocable {
    val context = AsahiContext()

    override fun eval(script: String, context: ScriptContext): Any? {
        return (context as AsahiContext).run { script.compile().run() }
    }

    override fun eval(reader: Reader, context: ScriptContext): Any? {
        return eval(reader.readText(), context)
    }

    override fun eval(script: String): Any? {
        return (context as AsahiContext).run { script.compile().run() }
    }

    override fun eval(reader: Reader): Any? {
        return eval(reader.readText())
    }

    override fun createBindings(): Bindings {
        return AsahiContext()
    }

    override fun getFactory(): ScriptEngineFactory {
        return factory
    }

    override fun compile(script: String): CompiledScript {
        return AsahiCompiledScript(script.compile(), this)
    }

    override fun compile(script: Reader): CompiledScript {
        return compile(script.readText())
    }

    override fun invokeMethod(thiz: Any, name: String, vararg args: Any): Any? {
        return (context as AsahiContext).invoke(name, *args)
    }

    override fun invokeFunction(name: String, vararg args: Any): Any? {
        return (context as AsahiContext).invoke(name, *args)
    }

    override fun <T : Any?> getInterface(clasz: Class<T>?): T {
        TODO("Not yet implemented")
    }

    override fun <T : Any?> getInterface(thiz: Any?, clasz: Class<T>?): T {
        TODO("Not yet implemented")
    }
}