package com.skillw.asahi.internal.namespacing.prefix.lang.util

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.quester
import com.skillw.asahi.api.script.NativeFunction

internal object FunctionRegex {
    @AsahiPrefix(["regexOf"], "lang")
    private fun regexOf() = prefixParser {
        val regex = quest<String>()
        val options = if (except("with")) quest() else quester { emptySet<RegexOption>() }
        result { regex.get().toRegex(options.get()) }
    }

    @AsahiPrefix(["regex"], "lang")
    private fun regex() = prefixParser {
        val regex = if (except("of")) quest<String>().quester { it.toRegex() } else quester { selector() }
        when (val type = next()) {
            "find" -> {
                val input = quest<String>()
                val index = if (except("at")) quest() else quester { 0 }
                result {
                    regex.get().find(input.get(), index.get())
                }
            }

            "findAll" -> {
                val input = quest<String>()
                val index = if (except("at")) quest() else quester { 0 }
                result {
                    regex.get().find(input.get(), index.get())
                }
            }

            "matches" -> {
                val input = quest<String>()
                val index = if (except("at")) quest() else quester { 0 }
                result {
                    regex.get().matchesAt(input.get(), index.get())
                }
            }

            "replace" -> {
                val input = quest<String>()
                val replacement = if (except("with")) quest<NativeFunction>() else quest<String>()
                result {
                    when (val obj = replacement.get()) {
                        is String -> regex.get().replace(input.get(), obj)
                        is NativeFunction -> {
                            println(1)
                            regex.get().replace(input.get()) {
                                obj.invoke(this, it).toString()
                            }
                        }

                        else -> null
                    }
                }
            }

            else -> error("Unknown regex operate type $type")
        }
    }


}