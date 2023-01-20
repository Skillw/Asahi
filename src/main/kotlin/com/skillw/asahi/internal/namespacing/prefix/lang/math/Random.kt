package com.skillw.asahi.internal.namespacing.prefix.lang.math

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import taboolib.common5.RandomList
import java.util.*

/**
 * @className Random
 *
 * @author Glom
 * @date 2023/1/14 0:31 Copyright 2023 user. All rights reserved.
 */

@AsahiPrefix(["random"], "lang")
fun random() = prefixParser {
    val x = quest<Double>()
    except("to")
    val y = quest<Double>()
    result { com.skillw.asahi.util.random(x.get(), y.get()) }
}

@AsahiPrefix(["randomInt"], "lang")
fun randomInt() = prefixParser {
    val x = quest<Int>()
    except("to")
    val y = quest<Int>()
    result { com.skillw.asahi.util.randomInt(x.get(), y.get()) }
}

@AsahiPrefix(["randomObj"], "lang")
fun randomObj() = prefixParser {
    val list = quest<List<Any>>()
    result { list.get().random() }
}

@AsahiPrefix(["weight"], "lang")
fun weight() = prefixParser {
    except("[")
    val list = LinkedList<Pair<Quester<Int>, Quester<Any>>>()
    do {
        val weight = quest<Int>()
        except("to", "=", ":")
        val value = quest<Any>()
        list += weight to value
        except(",")
    } while (!except("]"))
    val builder = peek() == "build"
    if (builder) next()
    result {
        val randomList = RandomList<Any>()
        list.map { it.second.get() to it.first.get() }.forEach { (value, weight) ->
            randomList.add(value, weight)
        }
        return@result if (!builder) randomList.random()!! else randomList
    }
}