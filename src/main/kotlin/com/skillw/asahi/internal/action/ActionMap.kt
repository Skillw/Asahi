package com.skillw.asahi.internal.action

import com.skillw.asahi.api.annotation.AsahiAction
import com.skillw.asahi.api.member.action.BaseAction

/**
 * @className ActionMap
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AsahiAction
object ActionMap : BaseAction<MutableMap<*, *>>(MutableMap::class.java) {

    init {
        addExec("get") { map ->
            val key = parse<String>()
            map[key]
        }

        addExec("set") { map ->
            map as? MutableMap<String, Any?>? ?: error("MutableMap<String,Any?>")
            val key = parse<String>()
            val value = parse<Any>()
            map[key] = value
            value
        }

        addExec("put") { map ->
            map as? MutableMap<String, Any?>? ?: error("MutableMap<String,Any?>")
            val key = parse<String>()
            val value = parse<Any>()
            map[key] = value
            value
        }

        addExec("remove") { map ->
            val key = parse<String>()
            map.remove(key)
        }

        addExec("contains") { map ->
            val key = parse<String>()
            map.containsKey(key)
        }

        addExec("size") { map ->
            map.size
        }

        addExec("keys") { map ->
            map.keys
        }

        addExec("values") { map ->
            map.values
        }

        addExec("entries") { map ->
            map.entries
        }
    }

}