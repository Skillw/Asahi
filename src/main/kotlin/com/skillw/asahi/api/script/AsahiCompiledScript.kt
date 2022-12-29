package com.skillw.asahi.api.script

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.executor.ExecutorCompound
import javax.script.CompiledScript
import javax.script.ScriptContext
import javax.script.ScriptEngine

/**
 * @className AsahiCompiledScript
 *
 * @author Glom
 * @date 2022/12/28 21:26 Copyright 2022 user. All rights reserved.
 */
class AsahiCompiledScript(val exec: ExecutorCompound, val engine: AsahiEngine) : CompiledScript() {

    override fun eval(context: ScriptContext): Any {
        return engine.context.putAll(context as AsahiContext)
    }

    override fun getEngine(): ScriptEngine {
        return engine
    }

}