package com.skillw.asahi.api.member.parser

import com.skillw.asahi.api.member.executor.IExecutor
import com.skillw.asahi.api.member.reader.IAsahiReader

/**
 * @className IFunction
 *
 * @author Glom
 * @date 2022/12/24 14:35 Copyright 2022 user. All rights reserved.
 */
fun interface IParser<R> {
    /**
     * 解析文本，返回IExecutor
     *
     * @return 执行器
     */
    fun IAsahiReader.parse(): IExecutor<R>
}
