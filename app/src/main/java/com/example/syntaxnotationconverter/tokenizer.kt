package com.example.syntaxnotationconverter


data class Token(val type: String, val value: String?) //value is null because of EOF token

// Tokenizer for Problem 1 and Problem 2
class Tokenizer (s : String) {
    private var s = s.trim()
    private var cursor : Int = 0
    private var operations = mapOf<String, String>("+" to "ADD", "-" to "SUBTRACT", "*" to "MULTIPLY", "/" to "DIVIDE", "^" to "POWER")
    private var operandIsVar = false
    private var operandIsInt = false

    fun hasNextInput() : Boolean {
//        println("cursor: $cursor")
        return cursor < s.length
    }

    fun getNextInput() : Token {
        if (! hasNextInput()) {
            return Token("END OF LINE", null)
        }

        val str = s.slice(cursor until s.length)

        if (str[0].isWhitespace()) {
            cursor++
            return getNextInput()
        }
        if (str[0].isLetter() && !operandIsInt) {
            operandIsVar = true
            val value = str[0].toString()
            cursor++
            return Token("OPERAND", value)
        }
        if (str[0].isDigit() && !operandIsVar) {
            operandIsInt = true
            var value = ""
            while (hasNextInput() && s[cursor].isDigit()) {
                value += s[cursor++]
            }
            return Token("OPERAND", value)
        }
        if(str[0].toString() in operations.keys) {
            val key = str[0].toString()
            cursor++
            return Token(operations[key]!!, key)
        }
        if (str[0].toString() == "(") {
            cursor++
            return Token("(", "(")
        }
        if (str[0].toString() == ")") {
            cursor++
            return Token(")", ")")
        }
        else {
            throw Error("Invalid Syntax:  \'${str[0]}\'")
        }
    }
}
