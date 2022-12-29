package com.skillw.asahi.api.member.parser

import com.skillw.asahi.api.manager.AsahiManager
import com.skillw.asahi.api.member.executor.IExecutor
import com.skillw.asahi.api.member.reader.IAsahiReader

/**
 * @className BaseParser
 *
 * @author Glom
 * @date 2022/12/27 10:12 Copyright 2022 user. All rights reserved.
 */
abstract class BaseParser<R>(vararg val types: Class<*>) : IParser<R> {
    fun register() {
        AsahiManager.registerParser(this)
    }

    companion object {
        @JvmStatic
        fun <R> createParser(
            vararg types: Class<*>,
            parse: IAsahiReader.() -> IExecutor<R>,
        ): BaseParser<R> {
            return object : BaseParser<R>(*types) {
                override fun IAsahiReader.parse(): IExecutor<R> {
                    return parse()
                }

            }
        }

        @JvmStatic
        fun <R> regParser(
            vararg types: Class<*>,
            parse: IAsahiReader.() -> IExecutor<R>,
        ): BaseParser<R> {
            return createParser(*types, parse = parse).apply { register() }
        }
    }
}