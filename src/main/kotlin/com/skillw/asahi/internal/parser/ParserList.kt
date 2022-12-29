package com.skillw.asahi.internal.parser

import com.skillw.asahi.api.annotation.AsahiParser
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.executor.IExecutor
import com.skillw.asahi.api.member.executor.IExecutor.Companion.executor
import com.skillw.asahi.api.member.parser.BaseParser.Companion.createParser
import java.util.*

@AsahiParser
object ParserList {
    val parser = createParser(List::class.java, MutableList::class.java) {
        except("[", "(")
        val list = LinkedList<IExecutor<Any?>>()
        do {
            except("*")
            list += quest()
            except(",")
        } while (!except("]", ")"))
        executor {
            val result = ArrayList<Any?>()
            list.forEach {
                when (val value = it.get()) {
                    is List<*> -> {
                        result.addAll(value)
                    }

                    is Array<*> -> {
                        result.addAll(value)
                    }

                    else -> result.add(value)
                }
            }
            result
        }
    }
}