package com.skillw.asahi.internal.parser.prefix.top

import com.skillw.asahi.api.annotation.AsahiTopParser
import com.skillw.asahi.api.member.lexer.AsahiLexer
import com.skillw.asahi.api.member.parser.prefix.TopPrefixParser
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.api.quester
import com.skillw.asahi.api.script.NativeFunction
import com.skillw.asahi.util.toArgs

@AsahiTopParser
internal object LambdaParser : TopPrefixParser<NativeFunction>("lambda", 0) {

    /**
     * TODO
     *
     * (变量1,变量2) -> { 处理 }
     *
     * (变量1,变量2) => { 处理 }
     */
    private val paramRegex = Regex("\\((.*?)(|\\))")
    override fun AsahiLexer.canParse(token: String): Boolean {
        return key.startsWith("(") || key.startsWith("[") || key.matches(paramRegex)
    }

    override fun AsahiLexer.parse(token: String): Quester<NativeFunction> {
        previous()
        val phrase = splitBeforeString("{")
        val params = paramRegex.find(phrase)!!.groups[0]!!.value.toArgs().filter { it.isNotEmpty() && it.isNotBlank() }
            .toTypedArray()
        val content = parseScript()
        val key = (params.hashCode() + content.hashCode()).toString()
        return quester {
            NativeFunction.create(key, params, content)
        }
    }

}