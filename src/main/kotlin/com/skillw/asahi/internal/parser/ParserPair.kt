package com.skillw.asahi.internal.parser

import com.skillw.asahi.api.annotation.AsahiParser
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.executor.IExecutor.Companion.executor
import com.skillw.asahi.api.member.parser.BaseParser.Companion.createParser


@AsahiParser
object ParserPair {
    val parser = createParser(Pair::class.java) {
        val first = quest<Any?>()
        except("to", "=", ":")
        val second = quest<Any?>()
        executor { first.get() to second.get() }
    }
}