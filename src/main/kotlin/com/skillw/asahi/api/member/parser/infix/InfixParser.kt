package com.skillw.asahi.api.member.parser.infix

import com.skillw.asahi.api.member.lexer.AsahiLexer
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.internal.parser.infix.ActionParserImpl

//后缀动作解释器
abstract class InfixParser {

    companion object {
        @JvmStatic
        fun get(): InfixParser {
            return ActionParserImpl
        }
    }

    abstract val ignores: HashSet<String>

    //这里是 后缀动作 的编译(parse)
    //在这时，Asahi是不知道每个对象是什么类型的，只有一个Quester<Any?>
    protected abstract fun AsahiLexer.parse(getter: Quester<Any?>): Quester<Any?>

    fun parseAction(lexer: AsahiLexer, getter: Quester<*>): Quester<Any?> {
        return lexer.parse(getter as Quester<Any?>)
    }
}