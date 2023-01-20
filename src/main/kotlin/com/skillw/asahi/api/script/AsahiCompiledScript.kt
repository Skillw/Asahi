package com.skillw.asahi.api.script

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.quest.Quester
import javax.script.CompiledScript

abstract class AsahiCompiledScript(val engine: AsahiEngine) : CompiledScript(), Quester<Any?> {
    abstract fun isExit(isExit: AsahiContext.() -> Boolean = { isExit() }): AsahiCompiledScript
    abstract fun rawScript(): String

    @Throws(AsahiScriptException::class)
    abstract override fun AsahiContext.execute(): Any?
    abstract fun add(quester: Quester<Any?>)
}