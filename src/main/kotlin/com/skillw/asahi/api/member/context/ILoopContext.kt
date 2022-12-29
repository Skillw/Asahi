package com.skillw.asahi.api.member.context

interface ILoopContext {
    val label: String
    var isBreak: Boolean
    var isContinue: Boolean
    val parent: ILoopContext?
    val subLoops: HashSet<ILoopContext>
    fun searchLabel(label: String): ILoopContext
    enum class Result {
        BREAK, CONTINUE
    }
}