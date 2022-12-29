package com.skillw.asahi.internal.logger

import com.skillw.asahi.api.member.reader.IAsahiReader

object Logger {
    fun IAsahiReader.error(message: String): Nothing {
        kotlin.error(
            "$message at : ${current()} in\n $origin"
        )
    }
}