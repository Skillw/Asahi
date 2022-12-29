package com.skillw.asahi.api.member.context


/**
 * @className AsahiContext
 *
 * @author Glom
 * @date 2022/12/24 15:05 Copyright 2022 user. All rights reserved.
 */
class AsahiLoopContext(
    override val label: String,
    override val parent: ILoopContext? = null,
    basic: AsahiContext,
) : AsahiContext(basic), ILoopContext {
    override var isBreak = false
    override var isContinue = false
    override val subLoops = HashSet<ILoopContext>()
    override fun searchLabel(label: String): ILoopContext {
        if (this.label == label) return this
        if (parent?.label == label) return parent
        return subLoops.firstOrNull { it.label == label } ?: error("No such Loop with the label $label")
    }

    companion object {
        fun AsahiContext.loopContext(label: String): AsahiLoopContext {
            val parent = if (this is ILoopContext) this else null
            return AsahiLoopContext(label, parent, this)
        }
    }
}