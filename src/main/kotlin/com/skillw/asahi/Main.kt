package com.skillw.asahi

import com.skillw.asahi.internal.inject.Register
import com.skillw.asahi.shell.Shell

fun main() {
    Register.registerAsahi()
    Shell.start()
}