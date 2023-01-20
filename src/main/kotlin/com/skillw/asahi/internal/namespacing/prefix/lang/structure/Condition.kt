package com.skillw.asahi.internal.namespacing.prefix.lang.structure

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.quester
import java.util.*

/**
 * @className Condition
 *
 * @author Glom
 * @date 2023/1/14 0:21 Copyright 2023 user. All rights reserved.
 */
//单路
@AsahiPrefix(["if"], "lang")
private fun `if`() = prefixParser {
    val condition = condition("then")
    except("then")
    val ifTrue = quest<Any?>()
    val ifFalse = if (except("else")) quest<Any?>() else quester { }
    result {
        if (condition.get()) ifTrue.run()
        else ifFalse.run()
    }
}

//多路
@AsahiPrefix(["when", "switch"], "lang")
private fun `when`() = prefixParser {
    val value = if (except("of")) quest<Any>() else null
    val pairs = LinkedList<Pair<Quester<Boolean>, Quester<Any>>>()
    except("{")
    while (except("case", "when")) {
        value?.let {
            val condition = condition("->") {
                val symbol = next()
                val other = quest<Any>()
                quester { com.skillw.asahi.util.check(value, symbol, other) }
            }
            except("->")
            pairs.add(condition to quest())
        } ?: kotlin.run {
            val condition = condition("->")
            except("->")
            val ifTrue = quest<Any>()
            pairs.add(condition to ifTrue)
        }
    }
    if (except("else", "default")) {
        except("->")
        val final = quest<Any>()
        pairs.add(quester { true } to final)
    }
    result {
        pairs.forEach {
            if (it.first.get()) {
                return@result it.second.get()
            }
        }
    }
}