package com.example.syntaxnotationconverter
/*
<expr> ::=  <term> <OP1>
<OP1> ::=  + <term> <OP1> | - <term> <OP1> | epsilon
<term> ::= <power> <OP2>
<OP2> ::= * <power> <OP2> | / <power> <OP2> | epsilon
<power> ::= <factor> ^ <power> | <factor> | epsilon
<factor> ::= (<expr>) | <digit>
<digit> ::= 0|1|2|3
 */
class InfixParser (val s : String) {
    private val tokenizer : Tokenizer = Tokenizer(s)
    private var isValid : Boolean = true
    private lateinit var lookAhead : Token

    public fun isAccepted(): Boolean {
        lookAhead = tokenizer.getNextInput()

        expression()

        if (lookAhead.type == "END OF LINE") {
//            print("eol")
            return isValid
        } else
            return false
    }

    private fun expression() {
        val t = lookAhead.type
        if (t == "OPERAND" || t == "(") {
            term()
            op1()
        } else {
//            print("hit expression")
            isValid = false
        }
    }
    private fun term() {
        val t = lookAhead.type
        if (t == "OPERAND" || t == "(") {
            power()
            op2()
        } else {
//            print("hit term")
            isValid = false
        }
    }

    private fun power() {
        val t = lookAhead.type
        if (t == "OPERAND" || t == "(") {
            factor()
            if (lookAhead.type == "POWER") {
                eat()
                power()
            }
            // epsilon transition
        } else {
//            print("hit power")
            isValid = false
        }
    }
    private fun factor() {
        var t = lookAhead.type
        if (t == "OPERAND") {
            eat()
        } else if (t == "(") {
            eat()
            expression()
            t = lookAhead.type
            if (t == ")") {
                eat()
            } else {
//                print("hit factor")
                isValid = false
            }
        } else {
//            print("hit factor2")
            isValid = false
        }

    }
    private fun op1() {
        var t = lookAhead.type
        if (t == "ADD" || t == "SUBTRACT") {
            eat()
            t = lookAhead.type
            if (t == "OPERAND" || t == "(") {
                term()
                op1()
            } else {
//                print("hit op1")
                isValid = false
            }
        }
    }
    private fun op2() {
        var t = lookAhead.type
        if (t == "MULTIPLY" || t == "DIVIDE") {
            eat()
            t = lookAhead.type
            if (t == "OPERAND" || t == "(") {
                power()
                op2()
            } else {
//                print("hit op2")
                isValid = false
            }
        }
    }

    private fun eat() : Token {
        val token = lookAhead
        lookAhead = tokenizer.getNextInput()
        return token
    }
}