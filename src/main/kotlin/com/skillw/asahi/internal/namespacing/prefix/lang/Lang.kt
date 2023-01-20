package com.skillw.asahi.internal.namespacing.prefix.lang

import com.skillw.asahi.api.AsahiAPI.compile
import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.quester


/**
 * @className Lang
 *
 * $list
 *
 * @author Glom
 * @date 2023/1/9 22:55 Copyright 2023 user. All rights reserved.
 */
@AsahiPrefix(["null"], "lang")
private fun `null`() = prefixParser {
    result { null }
}

@AsahiPrefix(["pass"], "lang")
private fun pass() = prefixParser {
    result { "" }
}

@AsahiPrefix(["true"], "lang")
private fun `true`() = prefixParser {
    result { true }
}

@AsahiPrefix(["false"], "lang")
private fun `false`() = prefixParser {
    result { false }
}

@AsahiPrefix(["return"], "lang")
private fun `return`() = prefixParser {
    val value = if (expect(";")) quester { Unit } else quest<Any?>()
    result { exit(); value.get() }
}

@AsahiPrefix(["exit", "stop"], "lang")
private fun exit() = prefixParser {
    result { exit() }
}

@AsahiPrefix(["{"], "lang")
private fun block() = prefixParser {
    val script = splitTill("{", "}", true).compile(*namespaceNames())
    result { script.run() }
}

@AsahiPrefix(["using"], "lang")
private fun using() = prefixParser {
    val name = next()
    addNamespaces(name)
    result { name }
}

