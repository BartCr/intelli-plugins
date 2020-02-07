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

/**
 * This class represents an operator, like for example "+", "cos", "sin"<br> the Operator is used only in the list of
 * acceptable Operators and not in the<br> internal parse tree.
 *
 * @author Bart Cremers
 * @since 1.0
 */
final class Operator {

    private final String op;
    private final int args;
    private final int precedence;

    /**
     * Creates an Operator with the specified name, arguments and precedence
     *
     * @param operator   the name of the operator
     * @param arguments  the arguments for the operator
     * @param precedence the operator precedence
     */
    public Operator(String operator, int arguments, int precedence) {
        this.op = operator;
        this.args = arguments;
        this.precedence = precedence;
    }

    /**
     * @return the precedence for this Operator.
     */
    public int precedence() {
        return (this.precedence);
    }

    /**
     * @return the number of arguments this Operator can take.
     */
    public int arguments() {
        return( this.args );
	}

	public String op() { return op; }
}
