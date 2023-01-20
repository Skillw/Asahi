package com.skillw.asahi.internal.parser.prefix.top

import com.skillw.asahi.api.annotation.AsahiTopParser
import com.skillw.asahi.api.member.context.InfixContext
import com.skillw.asahi.api.member.lexer.AsahiLexer
import com.skillw.asahi.api.member.parser.prefix.TopPrefixParser
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.api.member.quest.VarBeanQuester
import com.skillw.asahi.api.quester
import com.skillw.asahi.internal.parser.infix.ActionParserImpl
import com.skillw.asahi.util.fastGet
import com.skillw.asahi.util.fastInvoke
import com.skillw.asahi.util.fastSet
import java.util.*

@AsahiTopParser
internal object VarBeanParser : TopPrefixParser<Any?>("var-bean", 1) {
    private val varPrefixes = arrayOf('@')
    override fun AsahiLexer.canParse(token: String): Boolean {
        return token.first() in varPrefixes
    }

    private val paramsTokens = arrayOf("[", "[]", "(", "()")

    private val linkingTokens = hashSetOf("to", "=", "with", "of", "in", "at", "around", "over")

    override fun AsahiLexer.parse(token: String): Quester<Any?>? {
        val variable = VarParser.parseWith(this, token)
        if (peek() == "\n" || peek() == ";" || variable == null) return variable
        val tokens = LinkedList<String>()
        while (hasNext() && peek() !in ActionParserImpl.ignores) {
            tokens += next()
        }
        val actionReader = AsahiLexer.of(tokens)
        val context = quester { InfixContext(context(), actionReader) }
        return VarBeanQuester {
            //获取对象
            //这时候才知道对象的类型
            var obj = variable.get() ?: return@VarBeanQuester null
            //获取上下文
            context.get().run {
                reset()
                while (hasNext()) {
                    if (action == "@NONE")
                        action = next()
                    obj = (if (peek() in linkingTokens) {
                        next()
                        val any = parse<Any?>()
                        obj.fastSet(action, any)
                        any
                    } else if (peek() in paramsTokens) {
                        val params = if (peek() == "[]" || peek() == "()") {
                            next()
                            emptyArray()
                        } else parseArray()
                        obj.fastInvoke<Any?>(action, *params)
                    } else {
                        obj.fastGet(action)
                    }) ?: return@run "null"
                    action = "@NONE"
                }
                obj
            }
        }
    }

}