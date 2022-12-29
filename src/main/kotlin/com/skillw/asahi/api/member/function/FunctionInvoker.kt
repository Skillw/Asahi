package com.skillw.asahi.api.member.function

import com.skillw.asahi.api.member.reader.IAsahiReader

/**
 * @className FunctionCreator
 *
 * @author Glom
 * @date 2022/12/27 19:31 Copyright 2022 user. All rights reserved.
 */
class FunctionInvoker<R>(
    creator: FunctionCreator<R>,
    reader: IAsahiReader,
) : FunctionCreator<R> by creator, IAsahiReader by reader