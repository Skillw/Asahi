package com.skillw.asahi.util


internal fun MutableMap<String, Any>.putDeep(key: String, value: Any): Any? {
    var map: MutableMap<String, Any>? = this
    var list: MutableList<Any>? = null
    val keys = key.split(".")
    val lastIndex = keys.lastIndex
    for (i in keys.indices) {
        val keyStr = keys[i]

        if (i == lastIndex) {
            map?.put(keyStr, value) ?: list?.set(keyStr.toInt(), value)
            break
        }

        when (val obj = map?.get(keyStr) ?: keyStr.toIntOrNull()?.let { list?.getOrNull(it) }) {
            is Map<*, *> -> {
                map = obj as MutableMap<String, Any>
                list = null
            }

            is List<*> -> {
                list = obj as MutableList<Any>?
                map = null
            }

            null -> {
                map?.let {
                    HashMap<String, Any>().also { newMap ->
                        it[keyStr] = newMap
                        map = newMap
                    }
                }
                list?.let {
                    val index = keyStr.toInt()
                    ArrayList<Any>().also { newList ->
                        it[index] = newList
                        list = newList
                    }
                }
            }

            else -> {
                return null
            }
        }
    }
    return null
}

internal fun MutableMap<String, Any>.getDeep(key: String): Any? {
    var map: MutableMap<String, Any>? = this
    var list: MutableList<Any>? = null
    val keys = key.split(".")
    val lastIndex = keys.lastIndex
    for (i in keys.indices) {
        val keyStr = keys[i]
        val obj = map?.get(keyStr) ?: keyStr.toIntOrNull()?.let { list?.getOrNull(it) }
        if (i == lastIndex) return obj
        when (obj) {
            is Map<*, *> -> {
                map = obj as MutableMap<String, Any>
                list = null
            }

            is List<*> -> {
                list = obj as MutableList<Any>?
                map = null
            }

            else -> {
                return null
            }
        }
    }
    return null
}