package com.example.syntaxnotationconverter


fun main() {

    //1. Evaluate an expression and convert it to the other syntax notations
    val s = "abc*def^/g*-h*+"
    convertNotation(s)

    //2. Evaluate the expressions

    val str = " ( 5 + 15 ) / (20 / 5)"

    println(eval(str))
}

fun convertNotation(s: String) {
    if (isSymbol(s[0])) {
        val parser = PrefixParser(s)
        if (parser.isAccepted()) {
            print("infix: ")
            println(prefixToInfix(s))
            print("postfix: ")
            println(prefixToPostfix(s))
        } else {
            println("Invalid Expression")
        }
    } else if (isSymbol(s[s.length - 1])) {
        val parser = PostfixParser(s)
        if (parser.isAccepted()) {
            print("infix: ")
            println(postfixToInfix(s))
            print("prefix: ")
            println(postfixToPrefix(s).filterNot{it.isWhitespace()})
        } else {
            println("Invalid Expression")
        }
    } else {
        val parser = InfixParser(s)
        if (parser.isAccepted()) {
            print("postfix: ")
            println(infixToPostfix(s))
            print("prefix: ")
            println(infixToPrefix(s).filterNot {it.isWhitespace()})
        } else {
            println("Invalid Expression")
        }
    }
}

fun isSymbol (s: Char) : Boolean {
    if (s in "+-/*^") return true
    return false
}
/*
"a+b*c-d"
"(A+B)*(C+D)"
"A*B+C*D"
"A+B+C+D"
"(A+B)*C"
"a*(b+c/d)"
"(a+(((b*c)-((d/(e^f))*g))*h))"
 */