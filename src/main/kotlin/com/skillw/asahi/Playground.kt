package com.skillw.asahi

import com.skillw.asahi.api.AsahiAPI.compile
import com.skillw.asahi.internal.AsahiLoader
import taboolib.module.kether.KetherShell
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

/**
 * @className Playground
 *
 * @author Glom
 * @date 2023/8/6 19:47 Copyright 2023 user. All rights reserved.
 */
class Playground {
    companion object {
        @OptIn(ExperimentalTime::class)
        @JvmStatic
        fun main(args: Array<String>) {
            AsahiLoader.init("com.skillw.asahi")
            val scripts = """
        set text to join [ 1 1 2 3 4 ] by -
        repeat 3000 print &text
    """.trimIndent()
            val compiledAsahi = scripts.compile()
            val asahi = measureTime {
                compiledAsahi.eval()
            }
            KetherShell.eval(scripts)
            val kether = measureTime {
                KetherShell.eval(scripts).join()
            }
            println("Asahi: $asahi")
            println("Kether: $kether")
        }

    }
}