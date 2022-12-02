package com.example.syntaxnotationconverter

/*
<expr> ::= +| - | * | / | ^ | <identifier>
<identifier> ::= \d+ | [a-zA-Z]
 */
class PrefixParser(val s : String) {
    private var isValid = true
    private val tokenizer = Tokenizer(s)
    private lateinit var lookAhead : Token

    private var operations = mapOf<String, String>("+" to "ADD", "-" to "SUBTRACT", "*" to "MULTIPLY", "/" to "DIVIDE", "^" to "POWER")

    fun isAccepted() : Boolean {
        lookAhead = tokenizer.getNextInput()
        expression()
        if (lookAhead.type == "END OF LINE") {
//            println("eol")
            return isValid
        } else
            return false
    }
    private fun expression() {
        val t = lookAhead
        if (t.type in operations.values) {
            eat() // consume operator
            expression()
            expression()
        } else if (t.type == "OPERAND")  {
            eat()
        } else {
//            println("hit expression")
            isValid = false
        }
    }
    private fun eat() :Token {
        val token = lookAhead
        lookAhead = tokenizer.getNextInput()
        return token
    }
}