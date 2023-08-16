package com.skillw.asahi.util

/**
 * @className Run
 *
 * @author Glom
 * @date 2023/8/13 15:54 Copyright 2023 user. All rights reserved.
 */
fun <T> safe(run: () -> T): T? {
    return runCatching { run() }.run {
        if (isSuccess) getOrNull()
        else {
            exceptionOrNull()?.printStackTrace()
            null
        }
    }
}


fun <T> silent(run: () -> T): T? {
    return runCatching { run() }.getOrNull()
}
