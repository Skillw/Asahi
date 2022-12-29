package com.skillw.asahi.internal.action

import com.skillw.asahi.api.annotation.AsahiAction
import com.skillw.asahi.api.member.action.BaseAction

/**
 * @className ActionList
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AsahiAction
object ActionList : BaseAction<MutableList<*>>(MutableList::class.java) {

    init {
        addExec("get") { obj ->
            except("at")
            val index = parse<Int>()
            obj[index]
        }

        addExec("add") { obj ->
            obj as? MutableList<Any?>? ?: error("MutableList")
            val value = parse<Any>()
            obj.add(value)
        }

        addExec("remove") { obj ->
            if (except("at")) {
                return@addExec obj.removeAt(parse<Int>())
            }
            val value = parse<Any>()
            obj.remove(value)
        }

        addExec("clear") { obj ->
            obj.clear()
        }

        addExec("set") { obj ->
            obj as? MutableList<Any?>? ?: error("MutableList")
            val index = parse<Int>()
            val value = parse<Any>()
            obj[index] = value
            return@addExec value
        }

        addExec("size") { obj ->
            obj.size
        }

        addExec("contains") { obj ->
            val value = parse<Any>()
            obj.contains(value)
        }

        addExec("isEmpty") { obj ->
            obj.isEmpty()
        }

        addExec("toArray") { obj ->
            obj.toTypedArray()
        }

        addExec("toString") { obj ->
            obj.toString()
        }

        addExec("merge") { obj ->
            except("by")
            val by = parse<String>()
            obj.joinToString { by.replace("\\n", "\n") }
        }
    }
}