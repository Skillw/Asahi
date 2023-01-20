package com.skillw.asahi.internal.namespacing.prefix.lang

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.lazyQuester
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest

/**
 * @className Variable
 *
 * @author Glom
 * @date 2023/1/13 19:09 Copyright 2023 user. All rights reserved.
 */
@AsahiPrefix(["set"], "lang")
private fun set() = prefixParser {
    val key = next()
    when {
        except("ifndef") -> {
            result { if (containsKey(key)) return@result context()[key] else null }
        }

        except("by") && except("lazy") -> {
            except("to", "=");

            val block = quest<Quester<*>>()
            result {
                lazyQuester {
                    block.get().also { result -> context()[key] = result }
                }.also { context()[key] = it }
            }
        }

        else -> {
            except("to", "=")
            val value = quest<Any?>()
            result {
                value.get()?.let {
                    context()[key] = it;
                } ?: context().remove(key)
            }
        }
    }
}

@AsahiPrefix(["has"], "lang")
private fun has() = prefixParser {
    val key = quest<String>()
    result { containsKey(key.get()) }
}