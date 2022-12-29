package com.skillw.asahi.shell

import com.skillw.asahi.api.manager.AsahiManager.compile
import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.util.StringUtils.toStringWithNext
import java.util.*

object Shell {
    /**
     *     FIXME
     *
     * return
     *
     * print 1 return 1 print 2 run 1 2 Total: 0ms
     *
     * [ while, mapListOf ]
     */


    fun start() {
        var next = readlnOrNull()
        val list = LinkedList<String>()
        while (next != "stop") {
            when (next) {
                "run" -> {
                    val start = System.currentTimeMillis()
                    AsahiContext(namespaces = arrayOf("lang")).run {
                        list.toStringWithNext().compile().run()
                    }
                    val end = System.currentTimeMillis()
                    println("Total: ${end - start}ms")
                    list.clear()
                }

                "remove" -> {
                    list.removeLast()
                }

                else -> {
                    list.add(next + "\n")
                }
            }
            next = readlnOrNull()
        }
    }
}