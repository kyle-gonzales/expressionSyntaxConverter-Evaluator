package com.example.syntaxnotationconverter

class PostfixParser(val s : String) {
    private var isValid : Boolean  = true
    private val tokenizer = Tokenizer(s)
    private lateinit var lookAhead : Token
    private var operations = mapOf<String, String>("+" to "ADD", "-" to "SUBTRACT", "*" to "MULTIPLY", "/" to "DIVIDE", "^" to "POWER")


    fun isAccepted() : Boolean {
        lookAhead = tokenizer.getNextInput()

        expression()

        if (lookAhead.type == "END OF LINE") {
//            println("eol")
            return isValid
        } else {
            return false
        }
    }

    private fun expression() {
        val t = lookAhead.type
         if (t == "OPERAND") {
             eat() // consume the operand
             factor()
         } else {
//             println("hit expression")
             isValid = false
         }
    }
    private fun factor() {
        var t = lookAhead.type
        if (t == "OPERAND") {
            eat() // consume the operand
            factor()
            t = lookAhead.type
            if (t in operations.values) {
                eat() // consume the operation sign
                factor()
            } else {
//                println("hit factor 1")
                isValid = false
            }
        }
        // epsilon transition
    }
    private fun eat() : Token {
        val token = lookAhead
        lookAhead = tokenizer.getNextInput()
        return token
    }
}