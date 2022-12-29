package com.skillw.asahi.util

import jdk.nashorn.api.scripting.NashornScriptEngineFactory
import java.math.BigDecimal

object CalculationUtils {
    private val calEngine by lazy {
        NashornScriptEngineFactory().scriptEngine
    }

    @JvmStatic
    fun calculate(input: String): BigDecimal {
        val result = kotlin.runCatching { calEngine.eval(input.filter { it != ' ' }) }.getOrNull()
        val optional = Coerce.asDouble(result)
        return if (optional.isPresent)
            BigDecimal.valueOf(optional.get())
        else {
            println("Wrong calculation formula! $input");
            BigDecimal.valueOf(0.0)
        }
    }
}