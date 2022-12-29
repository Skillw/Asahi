package com.skillw.asahi.internal.function.lang.math

import com.skillw.asahi.api.annotation.AsahiFunction
import com.skillw.asahi.api.manager.AsahiManager.quest
import com.skillw.asahi.api.member.function.BaseFunction.Companion.createFunction
import com.skillw.asahi.api.member.reader.InlineParser
import com.skillw.asahi.util.CalculationUtils


@AsahiFunction
object FunctionCalculate {

    val func = createFunction("calculate", alias = arrayOf("calc"), namespaces = arrayOf("lang")) {
        val formulaGetter = quest<String>()
        function {
            val formula = InlineParser(formulaGetter.get()).replace(this)
            CalculationUtils.calculate(formula).toDouble()
        }
    }

}