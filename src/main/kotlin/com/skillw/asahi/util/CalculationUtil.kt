package com.skillw.asahi.util

import com.skillw.asahi.util.calculate.calculate
import java.math.BigDecimal

/**
 * 计算工具类
 *
 * @constructor Create empty Calculation utils
 */

internal fun calculate(input: String): BigDecimal {
    return input.calculate()
}


