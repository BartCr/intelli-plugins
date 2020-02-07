/*
 * Copyright (c) 2020 by Bart Cremers
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */
package net.sf.intelliplugin.calc.math;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bart Cremers
 * @since 1.0
 */
public abstract class MathBase {
    private static final int MAX_OPERATOR_LENGTH = 8;
    static final int INITIAL_STRING_BUFFER_LENGTH = 50;

    final Map<String, Operator> ops;

    protected MathBase() {
        ops = new HashMap<>(52);

        // Add all valid operators.
        // new Operator( operator, arguments, precedence )
        ops.put("^", new Operator("^", 2, 3));
        ops.put("+", new Operator("+", 2, 6));
        ops.put("-", new Operator("-", 2, 6));
        ops.put("/", new Operator("/", 2, 4));
        ops.put("*", new Operator("*", 2, 4));
        ops.put("cos", new Operator("cos", 1, 2));
        ops.put("sin", new Operator("sin", 1, 2));
        ops.put("exp", new Operator("exp", 1, 2));
        ops.put("ln", new Operator("ln", 1, 2));
        ops.put("tan", new Operator("tan", 1, 2));
        ops.put("acos", new Operator("acos", 1, 2));
        ops.put("asin", new Operator("asin", 1, 2));
        ops.put("atan", new Operator("atan", 1, 2));
        ops.put("cosh", new Operator("cosh", 1, 2));
        ops.put("sinh", new Operator("sinh", 1, 2));
        ops.put("tanh", new Operator("tanh", 1, 2));
        ops.put("sqrt", new Operator("sqrt", 1, 2));
        ops.put("cotan", new Operator("cotan", 1, 2));
        ops.put("fpart", new Operator("fpart", 1, 2));
        ops.put("acotan", new Operator("acotan", 1, 2));
        ops.put("round", new Operator("round", 1, 2));
        ops.put("ceil", new Operator("ceil", 1, 2));
        ops.put("floor", new Operator("floor", 1, 2));
        ops.put("fac", new Operator("fac", 1, 2));
        ops.put("sfac", new Operator("sfac", 1, 2));
        ops.put("abs", new Operator("abs", 1, 2));
        ops.put("log", new Operator("log", 2, 5));
        ops.put("deg2rad", new Operator("deg2rad", 1, 2));
        ops.put("deg2grad", new Operator("deg2grad", 1, 2));
        ops.put("rad2deg", new Operator("rad2deg", 1, 2));
        ops.put("rad2grad", new Operator("rad2grad", 1, 2));
        ops.put("grad2deg", new Operator("grad2deg", 1, 2));
        ops.put("grad2rad", new Operator("grad2rad", 1, 2));
        ops.put("%", new Operator("%", 2, 4));
        ops.put(">", new Operator(">", 2, 7));
        ops.put("<", new Operator("<", 2, 7));
        ops.put("&&", new Operator("&&", 2, 10));
        ops.put("==", new Operator("==", 2, 8));
        ops.put("!=", new Operator("!=", 2, 8));
        ops.put("||", new Operator("||", 2, 9));
        ops.put("!", new Operator("!", 1, 1));
        ops.put(">=", new Operator(">=", 2, 7));
        ops.put("<=", new Operator("<=", 2, 7));
    }

    /**
     * Checks if a character is an alphabetic character.
     *
     * @param ch character to check
     * @return true if the character is alphabetic, false otherwise.
     */
    private boolean isAlpha(char ch) {
        return ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z';
    }

    /**
     * Checks to see if x is a valid variable.
     *
     * @param exp string to check.
     * @return boolean, true or false.
     */
    boolean isVariable(String exp) {
        int len = exp.length();

        if (isAllNumbers(exp)) {
            return false;
        }

        for (int i = 0; i < len; i++) {
            if (getOp(exp, i) != null || isAllowedSym(exp.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks to see if the char s is a valid character.
     *
     * @param s the char to check
     * @return true if the char is valid, false otherwise.
     */
    private boolean isAllowedSym(char s) {
        return (s == ')' || s == '(' || s == '.' || s == '>' || s == '<' || s == '&' || s == '=' || s == '|');
    }

    /**
     * Checks if the char ch is numeric
     *
     * @param ch the char to check
     * @return true is the char was numeric, false otherwise
     */
    private boolean isConstant(char ch) {
        return Character.isDigit(ch);
    }

    /**
     * Checks if the String exp is numeric
     *
     * @param exp String to check
     * @return true is the String was numeric, false otherwise
     */
    boolean isConstant(String exp) {
        try {
            if (Double.isNaN(Double.parseDouble(exp))) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * Checks to see if this String consists of only digits and punctuation. <br> The purpose of this method is to
     * provide a more secure method of checking if a String is numeric since some older JVM's seem to accept Strings
     * that start with digits as numeric when the isConstant( String ) method above is used.
     * <p/>
     * The reason why both these methods exist is that this method is slightly more inefficient and there are only a few
     * places where the closer more secure check is necessary.
     *
     * @param str the String to check
     * @return true if the String was numeric, false otherwise.
     */
    boolean isAllNumbers(String str) {
        char ch;
        int i = 0, l;
        boolean dot = false;

        ch = str.charAt(0);

        if (ch == '-' || ch == '+') {
            i = 1;
        }

        l = str.length();

        while (i < l) {
            ch = str.charAt(i);
            if (!(Character.isDigit(ch) || (ch == '.' && !dot))) {
                return false;
            }

            if (ch == '.') {
                dot = true;
            }

            i++;
        }

        return true;
    }

    /**
     * Checks to see if the String str is the name of a acceptable operator.
     *
     * @param str the String to check
     * @return true if it is an acceptable operator, false otherwise.
     */
    private boolean isOperator(String str) {
        return ops.containsKey(str);
    }

    /**
     * Checks to see if the operator name represented by str takes two arguments.
     *
     * @param str the String to check
     * @return true if the operator takes two arguments, false otherwise.
     */
    boolean isTwoArgOp(String str) {
        if (str == null) {
            return false;
        }
        Operator o = ops.get(str);
        return o != null && o.arguments() == 2;
    }

    /**
     * Checks to see if the double value a can be considered to be a mathematical integer.
     *
     * @param a the double value to check
     * @return true if the double value is an integer, false otherwise.
     */
    boolean isInteger(double a) {
        return a - (int) a == 0.0;
    }

    /**
     * Checks to see if the int value a can be considered to be even.
     *
     * @param a the int value to check
     * @return true if the int value is even, false otherwise.
     */
    boolean isEven(double a) {
        return isInteger(a / 2);
    }

    /**
     * Takes an operator out of an infix expression.<br>
     *
     * @param exp   infix expression.
     * @param index where to start search for the operator in exp.
     * @return the operator if one is found or null if not. Ex:<br> If str is "34+cos(2*x)"<br> getOp( str , 2 ) =>
     *         "+"<br> getOp( str , 3 ) => "cos"<br> getOp( str , 0 ) => null
     */
    String getOp(String exp, int index) {
        String tmp;
        int len = exp.length();

        for (int i = 0; i < MAX_OPERATOR_LENGTH; i++) {
            if (index >= 0 && (index + MAX_OPERATOR_LENGTH - i) <= len) {
                tmp = exp.substring(index, index + (MAX_OPERATOR_LENGTH - i));
                if (isOperator(tmp)) {
                    return tmp;
                }
            }
        }

        return null;
    }

    /**
     * Checks the syntax of exp. Throws an java.lang.Exception if the syntax is wrong.
     *
     * @param exp string to be examined
     *            <p/>
     *            <p/>
     *            Only letters a-z, numbers 0-9, the symbols '(' ')' '.' and<br> one character operators are allowed in
     *            exp.<br> Syntax will also check for non matching brackets and some other cases<br> for example: 3cos(x)
     *            and 3+*x etc..
     * @throws SyntaxException when the syntax is incorrect.
     */
    void syntax(String exp) throws SyntaxException {
        int i = 0;
        String op;
        String nop;

        if (!isMatchParenthesis(exp)) {
            throw new SyntaxException("Non matching brackets");
        }

        int l = exp.length();

        while (i < l) {
            try {
                if ((op = getOp(exp, i)) != null) {
                    nop = getOp(exp, op.length() + i);
                    if (isTwoArgOp(nop) && !nop.equals("+") && !nop.equals("-")) {
                        throw new SyntaxException("Syntax error near -> " + exp.substring(i));
                    }
                } else if (!isAlpha(exp.charAt(i)) && !isConstant(exp.charAt(i)) && !isAllowedSym(exp.charAt(i))) {
                    throw new SyntaxException("Syntax error near -> " + exp.substring(i));
                }
            } catch (StringIndexOutOfBoundsException e) {
                // just ignore.
            }

            i++;
        }
    }

    /**
     * matches parenthesis in exp.
     *
     * @param exp string expression to check.
     * @return boolean, true if brackets match false otherwise.
     */
    private boolean isMatchParenthesis(String exp) {
        int count = 0;
        int len = exp.length();

        for (int i = 0; i < len; i++) {

            if (exp.charAt(i) == '(') {
                count++;
            } else if (exp.charAt(i) == ')') {
                count--;
            }
        }

        return count == 0;
    }

    /**
     * Inserts the multiplication operator where needed.<br> This method adds limited juxta positioning support.
     * <p/>
     * Juxtaposition is supported in these type cases:
     * <p/>
     * case: variable jp one-arg-op , xCos(x)<br> case: const jp variable or one-arg-op, 2x, 2tan(x)<br> case: "const jp (
     * expr )" , 2(3+x)<br> case: ( expr ) jp variable or one-arg-op , (2-x)x , (2-x)sin(x)<br> case: var jp  ( expr ) ,
     * x(x+1) , x(1-sin(x))
     * <p/>
     * Note that this also puts extra limitations on variable names, they cannot<br> contain digits within them or at the
     * beginning, only at the end.
     *
     * @param exp the infix String expression to process
     * @return the processed infix expression
     */
    String putMultiplicationOperator(String exp) {
        int i = 0, p = 0;
        String op = null;
        StringBuilder str = new StringBuilder(exp);

        int l = exp.length();

        while (i < l) {
            try {

                if ((op = getOp(exp, i)) != null && !isTwoArgOp(op)
                    && isAlpha(exp.charAt(i - 1))) {
                    // case: variable jp one-arg-op , xcos(x)
                    str.insert(i + p, '*');
                    p++;
                } else if (isAlpha(exp.charAt(i)) && isConstant(exp.charAt(i - 1))) {
                    // case: const jp variable or one-arg-op , 2x , 2tan(x)
                    str.insert(i + p, '*');
                    p++;
                } else if (exp.charAt(i) == '(' && isConstant(exp.charAt(i - 1))) {
                    // case: "const jp ( expr )" , 2(3+x)
                    str.insert(i + p, '*');
                    p++;
                } else if (isAlpha(exp.charAt(i)) && exp.charAt(i - 1) == ')') {
                    // case: ( expr ) jp variable or one-arg-op , (2-x)x , (2-x)sin(x)
                    str.insert(i + p, '*');
                    p++;
                } else if (exp.charAt(i) == '(' && exp.charAt(i - 1) == ')') {
                    // case: ( expr ) jp  ( expr ) , (2-x)(x+1) , sin(x)(2-x)
                    str.insert(i + p, '*');
                    p++;
                } else if (exp.charAt(i) == '(' && isAlpha(exp.charAt(i - 1)) && backTrack(exp.substring(0, i))
                                                                                 == null) {
                    // case: var jp  ( expr ) , x(x+1) , x(1-sin(x))
                    str.insert(i + p, '*');
                    p++;
                }
            } catch (StringIndexOutOfBoundsException e) {
                // Ignore
            }

            if (op != null) {
                i += op.length();
            } else {
                i++;
            }

            op = null;
        }

        return str.toString();
    }

    /**
     * Searches the infix expression str backwards to see if there<br> is an operator present at the end of str.
     *
     * @param str string expression to check.
     * @return the operator, if any, or null.
     *         Ex:<br> backTrack(  "5+x" ) => null<br> backTrack(  "5^" ) => "^"
     *         The purpose with this method is to check for combinations like ^- or *-<br> in some methods that need to do
     *         that.
     */
    String backTrack(String str) {
        String op;

        try {
            for (int i = 0; i <= MAX_OPERATOR_LENGTH; i++) {
                if ((op = getOp(str, (str.length() - 1 - MAX_OPERATOR_LENGTH + i))) != null
                    && (str.length() - MAX_OPERATOR_LENGTH - 1 + i + op.length()) == str.length()) {

                    return op;
                }
            }
        } catch (Exception e) {
            // Ignore
        }

        return null;
    }

    /**
     * Adds support for "scientific notation" by replacing the E operator with *10^
     * <p/>
     * For example the value 1E-3 would be changed to 1*10^-3 which the parser will treat<br> as a normal expression.
     *
     * @param exp the infix String expression to process
     * @return the processed infix expression
     */
    String parseE(String exp) {
        int i, p, len;

        StringBuilder newStr = new StringBuilder(exp);

        i = p = 0;
        len = exp.length();

        while (i < len) {
            try {
                if (exp.charAt(i) == 'e' && Character.isDigit(exp.charAt(i - 1))) {
                    if (Character.isDigit(exp.charAt(i + 1)) || ((exp.charAt(i + 1) == '-' || exp.charAt(i + 1) == '+')
                                                                 && Character.isDigit(exp.charAt(i + 2)))) {
                        // replace the 'e'
                        newStr.setCharAt(i + p, '*');
                        // insert the rest
                        newStr.insert(i + p + 1, "10^");
                        p += 3; // buffer grows by 3 chars
                    }
                }
            } catch (Exception e) {
                // Ignore
            }
            i++;
        }

        return newStr.toString();
    }

}