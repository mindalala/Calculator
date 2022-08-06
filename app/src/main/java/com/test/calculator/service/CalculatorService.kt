package com.test.calculator.service

import android.text.TextUtils
import java.math.BigDecimal

class CalculatorService {

    val calculate = fun(firstNumber: String, symbol: String, secondNumber: String): String {
        return getFunctionBySymbol(symbol)(firstNumber, secondNumber)
    }

    val getNumberText = fun(target: String, input: String): String {
        if (!isNumberZero(target)) {
            return target + input
        }

        return if (isNumberZero(input)) {
            "0"
        } else {
            input
        }
    }

    private val getFunctionBySymbol = { symbol: String ->
        when (symbol) {
            "+" -> plus
            "-" -> subtract
            "*" -> multiply
            "/" -> divide
            else -> throw IllegalArgumentException()
        }
    }

    private val isNumberZero = { input: String ->
        TextUtils.equals(input, "0")
    }

    private val plus = { firstNumber: String, secondNumber: String ->
        firstNumber.toBigInteger().add(secondNumber.toBigInteger()).toString()
    }

    private val subtract = { firstNumber: String, secondNumber: String ->
        firstNumber.toBigInteger().subtract(secondNumber.toBigInteger()).toString()
    }

    private val multiply = { firstNumber: String, secondNumber: String ->
        firstNumber.toBigInteger().multiply(secondNumber.toBigInteger()).toString()
    }

    private val divide = { firstNumber: String, secondNumber: String ->
        val secondNumberDecimal = secondNumber.toBigDecimal()
        if (secondNumberDecimal == BigDecimal(0)) {
            "ERR"
        } else {
            firstNumber.toBigDecimal().divide(secondNumberDecimal, 4, BigDecimal.ROUND_HALF_UP)
                .toString()
        }
    }

}
