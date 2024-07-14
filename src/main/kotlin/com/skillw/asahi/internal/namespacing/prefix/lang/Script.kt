package com.skillw.asahi.internal.namespacing.prefix.lang

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.quester
import com.skillw.asahi.api.script.linking.NativeFunction
import com.skillw.asahi.util.toArgs


private val funcRegex = Regex(".*?([a-zA-Z_$]+).*?\\((.*?)\\).*?")

@AsahiPrefix(["fun", "def"], "lang")
private fun `fun`() = prefixParser {
    val result = funcRegex.find(splitBeforeString("{"))!!
    val name = result.groups[1]?.value!!
    val params = result.groups[2]?.value!!.toArgs().filter { it.isNotEmpty() && it.isNotBlank() }.toTypedArray()
    val content = parseScript()
    result {
        NativeFunction.create(name, params, content).also { context().addInvoker(name, it) }
    }
}

@AsahiPrefix(["invoke", "call"], "lang")
private fun invoke() = prefixParser {
    val func = quest<String>()
    val paramsGetter = if (peek() == "[") quest<Array<Any>>() else quester { emptyArray() }
    result {
        val params = paramsGetter.get()
        invoke(func.get(), *params)
    }
}

@AsahiPrefix(["import"], "lang")
private fun import() = prefixParser {
    val paths = if (peek() == "[") quest() else quest<String>().quester { arrayOf(it) }
    result {
        import(*paths.get())
    }
}


