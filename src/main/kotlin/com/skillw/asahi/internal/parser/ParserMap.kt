package com.skillw.asahi.internal.parser

import com.skillw.asahi.api.annotation.AsahiParser
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.executor.IExecutor
import com.skillw.asahi.api.member.executor.IExecutor.Companion.executor
import com.skillw.asahi.api.member.parser.BaseParser.Companion.createParser
import java.util.*

@AsahiParser
object ParserMap {
    val parser = createParser(Map::class.java, MutableMap::class.java) {
        except("[")
        val list = LinkedList<Pair<IExecutor<String>, IExecutor<Any?>>>()
        do {
            val key = quest<String>()
            except("to", "=", ":")
            val value = quest<Any?>()
            list += key to value
            except(",")
        } while (!except("]"))
        executor {
            val map = LinkedHashMap<String, Any?>()
            list.forEach { (key, value) ->
                map[key.get()] = value.get()
            }
            map
        }
    }
}