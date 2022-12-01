package com.example.syntaxnotationconverter

import java.util.Stack

fun infixToPostfix(s : String) : String {
    val operatorStack = Stack<String>()
    val t = Tokenizer(s)
    var postfixExp = ""

    while (t.hasNextInput()) {
        val inputToken = t.getNextInput()
//        println(inputToken)

        if (inputToken.type == "OPERAND") {
            postfixExp += inputToken.value
        } else if (inputToken.type == "(") {
            operatorStack.push(inputToken.value)
        } else if (inputToken.type == ")") {
            while (operatorStack.peek() != "(") {
                postfixExp += operatorStack.pop() // append operators to answer
            }
            operatorStack.pop() // pop '('
        } else {
            while (!operatorStack.isEmpty() && inputToken.value?.let { getPrecedence(it) }!! <= getPrecedence(operatorStack.peek())) {
//                println(" input: '${inputToken.value} -> precedence: '${getPrecedence(inputToken.value)}\ntop of stack: ${operatorStack.peek()} -> precedence: ${getPrecedence(operatorStack.peek())}")

                if (getPrecedence(inputToken.value) < getPrecedence(operatorStack.peek())) {
                    postfixExp += operatorStack.pop()
                } else if (getPrecedence(inputToken.value) == getPrecedence(operatorStack.peek())) {
                    when (getAssociativity(inputToken.value)) {
                        "L to R" -> postfixExp += operatorStack.pop()
                        "R to L" -> operatorStack.push(inputToken.value)
                        else -> throw Error("Invalid Associativity")
                    }
                }
            }
            operatorStack.push(inputToken.value)
        }
    }
    while (! operatorStack.isEmpty()) {
        postfixExp += operatorStack.pop()
    }
    return postfixExp
}

fun infixToPrefix(s: String) : String {
    val reverse = s.reversed()
    val operatorStack = Stack<String>()
    val t = Tokenizer(reverse)
    var prefixExp = ""

    while (t.hasNextInput()) {
        val inputToken = t.getNextInput()
        //3println(inputToken)

        if (inputToken.type == "OPERAND") {
            prefixExp += inputToken.value + " "
        } else if (inputToken.type == ")") {
            operatorStack.push(inputToken.value)
        } else if (inputToken.type == "(") {
            while (operatorStack.peek() != ")") {
                prefixExp += operatorStack.pop() + ' '
            }
            operatorStack.pop()
        } else {
            while ((!operatorStack.isEmpty() && inputToken.value?.let { getPrecedence(it)}!! < getPrecedence(operatorStack.peek())) || (!operatorStack.isEmpty() && inputToken.value?.let { getPrecedence(it)}!! == getPrecedence(operatorStack.peek()) && getAssociativity(inputToken.value) == "R to L")) {
                prefixExp += operatorStack.pop() + " "
            }
            operatorStack.push(inputToken.value)
        }
    }

    while (!operatorStack.isEmpty()) {
        prefixExp += operatorStack.pop() + " "
    }
    return prefixExp.reversed()
}

fun postfixToInfix(s: String) : String {

    val operatorStack = Stack<String>()
    val t = Tokenizer(s)
    var infixExp = ""

    while (t.hasNextInput()) {
        val inputToken = t.getNextInput()

        if (inputToken.type == "OPERAND") {
            operatorStack.push(inputToken.value)
        } else if (inputToken.type == "(" || inputToken.type == ")") {
            continue
        } else {
            val a : String = operatorStack.pop()
            val b : String = operatorStack.pop()
            infixExp= "($b${inputToken.value}$a)"
            operatorStack.push(infixExp)
        }
    }
    return infixExp
}

fun prefixToInfix(s : String) : String {
    val operatorStack = Stack<String>()
    val reverse = s.reversed()
    val t = Tokenizer(reverse)
    var infixExp = ""

    while (t.hasNextInput()) {
        val inputToken = t.getNextInput()

        if (inputToken.type == "OPERAND") {
            operatorStack.push(inputToken.value)
        } else if (inputToken.type == "(" || inputToken.type == ")") {
            continue
        } else {
            val a : String = operatorStack.pop()
            val b : String = operatorStack.pop()
            infixExp = "($a${inputToken.value}$b)"
            operatorStack.push(infixExp)
        }
    }
    return infixExp
}

fun prefixToPostfix(s : String) : String {
    val x = prefixToInfix(s)
    return infixToPostfix(x)
}
fun postfixToPrefix(s : String) : String {
    val x = postfixToInfix(s)
    return infixToPrefix(x)
}

// helper functions

fun getPrecedence(op : String) : Int {
    return when (op) {
        "^" -> 3
        "/", "*" -> 2
        "+", "-" -> 1
        else -> -1 // parenthesis
    }
}

fun getAssociativity(op : String) : String {
    return when (op) {
        "+", "-", "*", "/" -> "L to R"
        "^" -> "R to L"
        "(", ")" -> "parentheses"
        else -> throw Error("Invalid Operator: '$op'")
    }
}