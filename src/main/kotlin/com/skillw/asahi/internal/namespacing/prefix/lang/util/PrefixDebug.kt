package com.skillw.asahi.internal.namespacing.prefix.lang.util

import com.skillw.asahi.api.AsahiManager
import com.skillw.asahi.api.AsahiManager.topPrefixParsers
import com.skillw.asahi.api.AsahiManager.typeParsers
import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.util.MessageUtil.debug
import java.util.*

internal object PrefixDebug {

    private fun Collection<*>.debug() {
        val messages = LinkedList<String>()
        forEach {
            messages.add(it.toString())
        }
        messages.sort()
        messages.forEach(::debug)
    }

    @AsahiPrefix(["debug"], "lang")
    private fun debugFunc() = prefixParser {
        when (val top = next()) {

            "parsers" ->
                when (val parserType = next()) {
                    "token" -> result {
                        debug("Asahi Token Parsers: ")
                        topPrefixParsers.debug()
                    }

                    "type" -> result {
                        debug("Asahi Token Parsers: ")
                        typeParsers.values.debug()
                    }

                    else -> error("Unknown parser type $parserType")
                }

            "functions" ->
                when (peek()) {
                    "native" -> result {
                        debug("Asahi Native Functions: ")
                        functions.values.debug()
                    }

                    else -> result {
                        debug("Asahi Functions: ")
                        AsahiManager.namespaces.values.forEach {
                            debug("Namespace ${it.key}")
                            it.functions.values.debug()
                        }
                    }
                }

            "namespaces" -> when (peek()) {
                "current" -> result {
                    debug("Current namespaces: ${namespaceNames().toList()}")
                }

                "all" -> result {
                    AsahiManager.namespaces.values.debug()
                }

                else -> result {
                    debug("Asahi Functions: ")
                    AsahiManager.namespaces.values.forEach {
                        debug("Namespace ${it.key}")
                        it.functions.values.debug()
                    }
                }
            }

            "actions" ->
                result {
                    debug("Asahi Actions: ")
                    AsahiManager.namespaces.values.forEach {
                        debug("Namespace ${it.key}")
                        it.actions.values.debug()
                    }
                }

            "allActions" ->
                result {
                    debug("Asahi All Actions: ")
                    AsahiManager.namespaces.values.forEach {
                        debug("Namespace ${it.key}")
                        it.allActions.debug()
                    }
                }


            "info" ->
                when (val infoType = next()) {
                    "compile" -> {
                        info("Asahi Compile Info")
                        result { null }
                    }

                    "eval" -> result {
                        info("Asahi Eval Info")
                    }

                    else -> error("Unknown info type $infoType")
                }

            "on" ->
                when (val type = next()) {
                    "compile" -> {
                        debugOn()
                        result { null }
                    }

                    "eval" -> result {
                        debugOn()
                    }

                    else -> error("Unknown debug type $type")
                }

            "off" ->
                when (val type = next()) {
                    "compile" -> {
                        debugOff()
                        result { null }
                    }

                    "eval" -> result {
                        debugOff()
                        null
                    }

                    else -> error("Unknown debug type $type")
                }

            else -> {
                previous()
                val str = questString()
                result {
                    debug(str.get())
                }
            }
        }
    }

}