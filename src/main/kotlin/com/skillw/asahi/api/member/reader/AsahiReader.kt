package com.skillw.asahi.api.member.reader

import com.skillw.asahi.internal.logger.Logger.error
import java.util.*


/**
 * AsahiReader
 *
 * @param origin
 * @constructor
 */
class AsahiReader : IAsahiReader {
    val tokens: ArrayList<String> = ArrayList()
    private var count = -1
    override val origin: String

    private constructor(origin: String) {
        this.origin = origin
        this.tokens.addAll(prepareTokens())
    }

    private constructor(tokens: Collection<String>) {
        this.tokens.addAll(tokens)
        this.origin = tokens.joinToString { " " }
    }

    companion object {
        @JvmStatic
        fun of(script: String): AsahiReader {
            return AsahiReader(script)
        }

        @JvmStatic
        fun of(tokens: Collection<String>): AsahiReader {
            return AsahiReader(tokens)
        }
    }


    /**
     * Except
     *
     * @param excepts
     * @return
     */
    override fun except(vararg excepts: String): Boolean {
        return excepts.any {
            (peek() == it).also { bool ->
                if (bool) {
                    next()
                    return@any true
                }
            }
        }
    }

    override fun prepareTokens(): ArrayList<String> {
        val tokens = ArrayList<String>()
        val cache = StringBuilder()
        fun push(char: Char) {
            cache.append(char)
        }

        fun addToken() {
            if (cache.isNotEmpty())
                tokens.add(cache.toString())
            cache.clear()
        }

        var string = false
        var escape = false
        var notes = false
        origin.forEach {
            if (notes) {
                //遇到换行符就退出注释模式
                if (it == '\n') {
                    notes = false
                }
                //如果在注释中，就跳过
                return@forEach
            }
            when (it) {
                //单行注释
                '#' -> {
                    if (string) {
                        push(it)
                        return@forEach
                    }
                    notes = true
                }

                //转义
                '\\' -> {
                    escape = true
                }

                //字符串
                '"', '\'' -> {
                    if (escape) {
                        push(it)
                        return@forEach
                    }
                    push(it)
                    if (string) {
                        addToken()
                    }
                    string = !string
                }

                //换行
                '\n' -> {
                    if (escape) {
                        push(it)
                        escape = false
                        return@forEach
                    }
                    if (string) {
                        push(it)
                        return@forEach
                    }
                    addToken()
                    tokens.add("\n")
                }

                //空格
                ' ' -> {
                    if (string) {
                        push(it)
                        return@forEach
                    }
                    addToken()
                }

                //常规字符
                else -> {
                    push(it)
                }
            }
        }
        if (cache.isNotEmpty() && cache.isNotBlank())
            addToken()
        val result = ArrayList<String>(tokens.size)
        tokens.forEachIndexed { index, token ->
            if (token == "\n" && tokens.getOrNull(index - 1) == "\n") {
                return@forEachIndexed
            }
            result.add(token)
        }
        return result
    }

    /**
     * Has next
     *
     * @return
     */
    override fun hasNext(): Boolean {
        val last = peek() == "\n" && count + 1 == tokens.lastIndex
        return count + 1 < tokens.size && !last
    }

    override fun current(): String {
        return tokens[count]
    }

    /**
     * Next
     *
     * @return
     */
    override fun next(): String {
        return tokens.getOrNull(++count).run { if (this == "\n") next() else this } ?: kotlin.run { count--;null }
        ?: error("Has no next")
    }

    override fun previous(): String? {
        if (count - 1 <= 0) {
            return null
        }
        return tokens[--count]
    }

    override fun currentIndex(): Int {
        return count
    }

    override fun peekNextIgnoreBlank(): String? {
        return tokens.getOrNull(count + 1)?.run {
            ifBlank {
                count++
                return peekNextIgnoreBlank().also { count-- }
            }
        }
    }

    override fun peek(): String? {
        return tokens.getOrNull(count + 1)
    }

    override fun skipTill(from: String, till: String): Boolean {
        var countIf = 0
        while (hasNext()) {
            when (next()) {
                from -> countIf++
                till -> if (--countIf <= 0) return true
                else -> {}
            }
        }
        return false
    }

    private fun trulyNext(): String = tokens[++count]

    override fun splitTill(from: String, to: String): String {
        var count = 0
        val builder = StringBuilder()
        while (hasNext()) {
            when (trulyNext()) {
                from ->
                    if (count++ == 0) continue

                to -> if (--count <= 0) return builder.toString()
            }
            builder.append(" ${current()}")
        }
        return builder.toString()
    }

    override fun reset() {
        count = -1
    }

}