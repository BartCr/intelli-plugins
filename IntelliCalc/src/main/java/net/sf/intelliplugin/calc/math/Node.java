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

import java.math.BigDecimal;

/**
 * This class represent a Node in the internal parse tree of the Eval class.
 *
 * @author Bart Cremers
 * @since 1.0
 */
public final class Node {

    private String operator = "";
    private Node arg1 = null;
    private Node arg2 = null;
    private int args = 0;
    private final NodeType type;
    private BigDecimal value = null; 
    private String variable = "";

    /**
     * Creates a Node containing the specified Operator and arguments. <br> This will automatically mark this Node as a
     * {@link NodeType#EXPRESSION}.
     *
     * @param operator the String operator
     * @param arg1     the first argument
     * @param arg2     the second argument
     */
    public Node(String operator, Node arg1, Node arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.operator = operator;
        this.args = 2;
        this.type = NodeType.EXPRESSION;
    }

    /**
     * Creates a Node containing the specified Operator and argument. <br> This will automatically mark this Node as a
     * {@link NodeType#EXPRESSION}.
     *
     * @param operator the String operator
     * @param arg1     the first argument
     */
    public Node(String operator, Node arg1) {
        this.arg1 = arg1;
        this.operator = operator;
        this.args = 1;
        this.type = NodeType.EXPRESSION;
    }

    /**
     * Creates a Node containing the specified variable. <br> This will automatically mark this Node as a {@link
     * NodeType#VARIABLE}.
     *
     * @param variable the string variable
     */
    public Node(String variable) {
        this.variable = variable;
        this.type = NodeType.VARIABLE;
    }

    /**
     * Creates a Node containing the specified value. <br> This will automatically mark this Node as a {@link
     * NodeType#CONSTANT}.
     *
     * @param value the value
     */
    public Node(BigDecimal value) {
        this.value = value;
        this.type = NodeType.CONSTANT;
    }

    /**
     * @return the operator of this Node
     */
    public String getOperator() {
        return this.operator;
    }

    /**
     * @return the value of this Node
     */
    public BigDecimal getValue() {
        return this.value;
    }

    /**
     * @return the variable of this Node
     */
    public String getVariable() {
        return this.variable;
    }

    /**
     * @return the number of arguments this Node has
     */
    public int arguments() {
        return this.args;
    }

    /**
     * @return the type of this Node
     * @see NodeType
     */
    public NodeType getType() {
        return this.type;
    }

    /**
     * @return the first argument of this Node
     */
    public Node arg1() {
        return this.arg1;
    }

    /**
     * @return the second argument of this Node
     */
    public Node arg2() {
        return this.arg2;
	}
}