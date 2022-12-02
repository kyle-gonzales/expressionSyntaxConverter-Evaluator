package com.example.syntaxnotationconverter

import java.util.*
import kotlin.math.pow

// Evaluates an expression
fun eval(s: String) : Double {
    val s = s.trim()
    if (isSymbol(s[0])) {
        val parser = PrefixParser(s)
        if (parser.isAccepted()) {
            return evalPrefix(s)
        } else {
            println("Invalid Expression")
        }
    } else if (isSymbol(s[s.length - 1])) {
        val parser = PostfixParser(s)
        if (parser.isAccepted()) {
            return evalPostfix(s)
        } else {
            println("Invalid Expression")
        }
    } else {
        val parser = InfixParser(s)
        if (parser.isAccepted()) {
            return evalInfix(s)
        } else {
            println("Invalid Expression")
        }
    }
    throw Error("Invalid Expression")
}

private fun evalPostfix(s: String) : Double {
    val operatorStack = Stack<Double>()
    val t = Tokenizer(s)
    var result : Double = 0.0

    while (t.hasNextInput()) {
        val inputToken = t.getNextInput()

        if (inputToken.type == "OPERAND") {
            operatorStack.push(inputToken.value?.toDouble())
        } else if (inputToken.type == "(" || inputToken.type == ")") {
            continue
        } else {
            val a : Double = operatorStack.pop()
            val b : Double = operatorStack.pop()
            result = operation(b, a, inputToken.type)
            operatorStack.push(result)
        }
    }
    return result
}

private fun evalPrefix(s: String) : Double {
    val operatorStack = Stack<Double>()
    val str : List<String> = s.split(" ")
    val reverse = str.reversed().joinToString(" ")
    val t = Tokenizer(reverse)
    var result : Double = 0.0

    while (t.hasNextInput()) {
        val inputToken = t.getNextInput()
        if (inputToken.type == "OPERAND") {
            operatorStack.push(inputToken.value?.toDouble())
        } else if (inputToken.type == "(" || inputToken.type == ")") {
            continue
        } else {
            val a : Double = operatorStack.pop()
            val b : Double = operatorStack.pop()
            result = operation(a, b, inputToken.type)
            operatorStack.push(result)
        }
    }
    return result
}

private fun evalInfix(s : String) : Double {
    val prefix = infixToPrefix(s)
    return evalPrefix(prefix)
}

private fun operation(a : Double, b : Double, op : String) : Double {
    when (op) {
        "ADD" -> return a + b
        "SUBTRACT" -> return a - b
        "MULTIPLY" -> return a * b
        "DIVIDE" -> {
            if (b.toInt() == 0) {
                throw Error("Division By Zero Error")
            } else
                return a / b
        }
        "POWER" -> return a.pow(b)
        else -> throw Error("Invalid Operation")
    }
}

