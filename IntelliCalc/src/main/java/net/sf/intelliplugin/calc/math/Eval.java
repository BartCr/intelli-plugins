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

import net.sf.intelliplugin.calc.calculator.BigMath;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * This class evaluates a mathematical expression given as a String to a double value.
 * <p/>
 * Example:
 * <pre>
 * import net.sf.intelliplugin.calc.math.*;
 * <p/>
 * public class Test {
 * <p/>
 *     public static void main( String args[] ) {
 *         Eval e = new Eval();
 * <p/>
 *         try {
 *             System.out.println( e.eval( "cos(x+y)" , "x=pi;y=2.34" ) );
 *         } catch( Exception ex ) {
 *             // handle
 *         }
 *     }
 * }
 * </pre>
 * The example above uses the eval( String, String ) method but for efficiency you should<br> as a rule use the eval(
 * String, Hashtable ) method.
 *
 * @author Bart Cremers
 * @since 1.0
 */
public class Eval extends MathBase {
    private final Map<String, BigDecimal> spConst;
    private Map<String, String> storedValues;
    private final Map<String, Node> trees;

    private final MathContext DEFAULT_MATH_CONTEXT = new MathContext(20, RoundingMode.HALF_EVEN);

    /**
     * Constructs a Eval object
     */
    public Eval() {
        spConst = new HashMap<>(12);

        spConst.put("euler", BigMath.EULER);
        spConst.put("pi", BigMath.PI);
        spConst.put("nan", BigMath.NaN);
        spConst.put("infinity", BigMath.POSITIVE_INFINITY);
        spConst.put("true", BigMath.TRUE);
        spConst.put("false", BigMath.FALSE);

        trees = new HashMap<>(101);
    }

    /**
     * This methods will parse out all spaces in the String str. <br>
     * <p/>
     * ( I wish there was a native effective method to do this since this need often<br> appears when programming in
     * Java. )
     *
     * @param str the String to process
     * @return the String stripped of all spaces
     */
    private String skipSpaces(String str) {
        int i = 0;
        int len = str.length();
        StringBuilder nStr = new StringBuilder(len);

        while (i < len) {
            if (str.charAt(i) != ' ') {
                nStr.append(str.charAt(i));
            }
            i++;
        }

        return nStr.toString();
    }

    /**
     * Matches an opening left parenthesis.
     *
     * @param exp   the String to search in
     * @param index the index of the opening let parenthesis
     * @return the index of the matching closing right parenthesis
     */
    private int match(String exp, int index) {
        int len = exp.length();
        int i = index;
        int count = 0;

        while (i < len) {
            if (exp.charAt(i) == '(') {
                count++;
            } else if (exp.charAt(i) == ')') {
                count--;
            }

            if (count == 0) {
                return i;
            }

            i++;
        }

        return index;
    }

    /**
     * Parses an infix String expression and creates a parse tree of Node's.
     * <p/>
     * <p/>
     * <p/>
     * This is the heart of the parser, it takes a normal expression and creates<br> a data structure we can easily
     * recurse when evaluating.
     * <p/>
     * <p/>
     * <p/>
     * The data structure is then parsed by the toValue method.
     *
     * @param exp the infix String expression
     * @return a tree data structure of Node objects representing the expression
     * @throws MathException if the expression can not be parsed.
     */
    private Node parse(String exp) throws MathException {
        Node tree = null;

        String fArg;
        String sArg;
        String fop;
        int ma;
        int i = 0;

        int len = exp.length();

        if (len == 0) {
            throw new MathException("Wrong number of arguments to operator");
        } else if (exp.charAt(0) == '(' && ((ma = match(exp, 0)) == (len - 1))) {
            return parse(exp.substring(1, ma));
        } else if (isVariable(exp)) {
            return new Node(exp);
        } else if (isAllNumbers(exp)) { // this is really the only place where isAllNumbers matters.
            return new Node(new BigDecimal(exp));
        }

        while (i < len) {
            if ((fop = getOp(exp, i)) == null) {
                fArg = arg(null, exp, i);
                fop = getOp(exp, i + fArg.length());

                if (fop == null) {
                    throw new MathException("Missing operator");
                }

                if (isTwoArgOp(fop)) {
                    sArg = arg(fop, exp, i + fArg.length() + fop.length());
                    if (sArg.equals("")) {
                        throw new MathException("Wrong number of arguments to operator " + fop);
                    }
                    tree = new Node(fop, parse(fArg), parse(sArg));
                    i += fArg.length() + fop.length() + sArg.length();
                } else {
                    if (fArg.equals("")) {
                        throw new MathException("Wrong number of arguments to operator " + fop);
                    }
                    tree = new Node(fop, parse(fArg));
                    i += fArg.length() + fop.length();
                }
            } else {
                if (isTwoArgOp(fop)) {
                    fArg = arg(fop, exp, i + fop.length());
                    if (fArg.equals("")) {
                        throw new MathException("Wrong number of arguments to operator " + fop);
                    }
                    if (tree == null) {
                        if (fop.equals("+") || fop.equals("-")) {
                            tree = new Node(BigMath.ZERO);
                        } else {
                            throw new MathException("Wrong number of arguments to operator " + fop);
                        }
                    }
                    tree = new Node(fop, tree, parse(fArg));
                    i += fArg.length() + fop.length();
                } else {
                    fArg = arg(fop, exp, i + fop.length());
                    if (fArg.equals("")) {
                        throw new MathException("Wrong number of arguments to operator " + fop);
                    }
                    tree = new Node(fop, parse(fArg));
                    i += fArg.length() + fop.length();
                }
            }
        }

        return tree;
    }

    /**
     * Parses the infix expression exp for arguments to the operator operator.
     *
     * @param operator the operator we are interested in
     * @param exp      the infix String expression
     * @param index    the index to start the search from
     * @return the argument to the operator
     */
    private String arg(String operator, String exp, int index) {
        String op;
        StringBuilder str = new StringBuilder(INITIAL_STRING_BUFFER_LENGTH);

        int i = index;
        int ma;
        int len = exp.length();

        int precedence;
        if (operator == null) {
            precedence = -1;
        } else {
            precedence = ops.get(operator).precedence();
        }

        while (i < len) {
            if (exp.charAt(i) == '(') {
                ma = match(exp, i);
                str.append(exp, i, ma + 1);
                i = ma + 1;
            } else if ((op = getOp(exp, i)) != null) {
                if (str.length() != 0 && !isTwoArgOp(backTrack(str.toString())) && ops.get(op).precedence() >= precedence) {
                    return str.toString();
                }
                str.append(op);
                i += op.length();
            } else {
                str.append(exp.charAt(i));
                i++;
            }
        }

        return str.toString();
    }

    /**
     * Parses the data structure created by the parse method.
     * <p/>
     * <p/>
     * <p/>
     * This is where the actual evaluation of the expression is made,<br> the tree structure created by the parse method
     * is recursed and evaluated<br> to a double value.
     *
     * @param tree Node representing a tree data structure
     * @return a double value
     * @throws MathException if the string value for the given key could not be acquired for some reason.
     */
    private BigDecimal toValue(Node tree) throws MathException {
        Node arg1, arg2;
        String op, tmp;

        if (tree.getType() == NodeType.CONSTANT) {
            return (tree.getValue());
        } else if (tree.getType() == NodeType.VARIABLE) {
            tmp = tree.getVariable();

            // check if PI, Euler....etc
            if (spConst.containsKey(tmp)) {
                return spConst.get(tmp);
            }

            // normal variable, get value
            tmp = get(tmp);
            if (isConstant(tmp)) {
                return new BigDecimal(tmp);
            } else {
                syntax(tmp);
                return toValue(parse(putMultiplicationOperator(parseE(tmp))));
            }
        }

        op = tree.getOperator();
        arg1 = tree.arg1();

        if (tree.arguments() == 2) {
            arg2 = tree.arg2();

            switch (op) {
                case "+":
                    return toValue(arg1).add(toValue(arg2));
                case "-":
                    return toValue(arg1).subtract(toValue(arg2));
                case "*":
                    return toValue(arg1).multiply(toValue(arg2));
                case "/":
                    return toValue(arg1).divide(toValue(arg2), DEFAULT_MATH_CONTEXT);
                case "^":
                    return BigDecimal.valueOf(StrictMath.pow(toValue(arg1).doubleValue(), toValue(arg2).doubleValue()));
                case "log":
                    return BigDecimal.valueOf(StrictMath.log(toValue(arg1).doubleValue()))
                                     .divide(BigDecimal.valueOf(StrictMath.log(toValue(arg2).doubleValue())),
                                             DEFAULT_MATH_CONTEXT);
                case "%":
                    return toValue(arg1).remainder(toValue(arg2));
                case "==":
                    return toValue(arg1).compareTo(toValue(arg2)) == 0 ? BigMath.TRUE : BigMath.FALSE;
                case "!=":
                    return toValue(arg1).compareTo(toValue(arg2)) != 0 ? BigMath.TRUE : BigMath.FALSE;
                case "<":
                    return toValue(arg1).compareTo(toValue(arg2)) < 0 ? BigMath.TRUE : BigMath.FALSE;
                case ">":
                    return toValue(arg1).compareTo(toValue(arg2)) > 0 ? BigMath.TRUE : BigMath.FALSE;
                case "&&":
                    return toValue(arg1).compareTo(BigMath.TRUE) == 0 && toValue(arg2).compareTo(BigMath.TRUE) == 0
                            ? BigMath.TRUE : BigMath.FALSE;
                case "||":
                    return toValue(arg1).compareTo(BigMath.TRUE) == 0 || toValue(arg2).compareTo(BigMath.TRUE) == 0
                            ? BigMath.TRUE : BigMath.FALSE;
                case ">=":
                    return toValue(arg1).compareTo(toValue(arg2)) >= 0 ? BigMath.TRUE : BigMath.FALSE;
                case "<=":
                    return toValue(arg1).compareTo(toValue(arg2)) <= 0 ? BigMath.TRUE : BigMath.FALSE;
            }
        } else {
            // TODO : Refactor to use BigMath
            switch (op) {
                case "sqrt":
                    return BigMath.sqrt(toValue(arg1));
                case "sin":
                    return BigDecimal.valueOf(StrictMath.sin(toValue(arg1).doubleValue()));
                case "cos":
                    return BigDecimal.valueOf(StrictMath.cos(toValue(arg1).doubleValue()));
                case "tan":
                    return BigDecimal.valueOf(StrictMath.tan(toValue(arg1).doubleValue()));
                case "asin":
                    return BigDecimal.valueOf(StrictMath.asin(toValue(arg1).doubleValue()));
                case "acos":
                    return BigDecimal.valueOf(StrictMath.acos(toValue(arg1).doubleValue()));
                case "atan":
                    return BigDecimal.valueOf(StrictMath.atan(toValue(arg1).doubleValue()));
                case "ln":
                    return BigDecimal.valueOf(StrictMath.log(toValue(arg1).doubleValue()));
                case "exp":
                    return BigMath.exp(toValue(arg1));
                case "cotan":
                    return BigMath.cotan(toValue(arg1));
                case "acotan":
                    return BigMath.acotan(toValue(arg1));
                case "ceil":
                    return BigDecimal.valueOf(StrictMath.ceil(toValue(arg1).doubleValue()));
                case "round":
                    return BigDecimal.valueOf(StrictMath.round(toValue(arg1).doubleValue()));
                case "floor":
                    return BigDecimal.valueOf(StrictMath.floor(toValue(arg1).doubleValue()));
                case "fac":
                    return BigMath.fac(toValue(arg1));
                case "abs":
                    return toValue(arg1).abs();
                case "fpart":
                    return BigMath.fpart(toValue(arg1));
                case "sfac":
                    return BigMath.sfac(toValue(arg1));
                case "sinh":
                    return BigDecimal.valueOf(StrictMath.sinh(toValue(arg1).doubleValue()));
                case "cosh":
                    return BigDecimal.valueOf(StrictMath.cosh(toValue(arg1).doubleValue()));
                case "tanh":
                    return BigDecimal.valueOf(StrictMath.tanh(toValue(arg1).doubleValue()));
                case "!":
                    return !(toValue(arg1).compareTo(BigMath.TRUE) == 0.0) ? BigMath.TRUE : BigMath.FALSE;
                case "deg2rad":
                    return toValue(arg1).multiply(BigMath.PI).divide(BigMath.ONE_EIGHTY, DEFAULT_MATH_CONTEXT);
                case "deg2grad":
                    return toValue(arg1).multiply(BigMath.TWO_HUNDRED).divide(BigMath.ONE_EIGHTY, DEFAULT_MATH_CONTEXT);
                case "rad2deg":
                    return toValue(arg1).multiply(BigMath.ONE_EIGHTY).divide(BigMath.PI, DEFAULT_MATH_CONTEXT);
                case "rad2grad":
                    return toValue(arg1).multiply(BigMath.PI).divide(BigMath.PI, DEFAULT_MATH_CONTEXT);
                case "grad2deg":
                    return toValue(arg1).multiply(BigMath.ONE_EIGHTY).divide(BigMath.TWO_HUNDRED, DEFAULT_MATH_CONTEXT);
                case "grad2rad":
                    return toValue(arg1).multiply(BigMath.PI).divide(BigMath.TWO_HUNDRED, DEFAULT_MATH_CONTEXT);
            }
        }

        throw new MathException("Unknown operator");
    }

    /**
     * Retrieves a value stored in the Hashtable containing all variable=value pairs.
     * <p/>
     * The hashtable is set by the eval( String, Hashtable ) method so this method retrieves<br> values inserted by the
     * user of this class. Please note that no processing has been made<br> on these values, they may have incorrect
     * syntax or casing.
     *
     * @param key the name of the variable we want the value for
     * @return the value stored in the Hashtable or null if none.
     * @throws MathException if the string value for the given key could not be acquired for some reason.
     */
    private String get(String key) throws MathException {
        String val = storedValues.get(key);

        if (val == null) {
            throw new MathException("No value associated with " + key);
        }

        return val;
    }

    /**
     * Evaluates the infix expression exp using the values in the map.
     * <p/>
     * This is one of the publicly available methods of the class, it is an entry point into the parser and the method
     * you want to use when evaluating a String expression.
     * <p/>
     * Example:
     * <pre>
     *   import net.sf.intelliplugin.calc.math.*;
     *   import java.util.Hashtable;
     * <p/>
     *   public class Test {
     *       public static void main(String[] args) {
     *           Eval e = new Eval();
     *           Map map = new HashMap&lt;String, String&gt;();
     *           try {
     *               map.put("x", "pi");
     *               map.put("y", "2.34");
     * <p/>
     *               System.out.println(e.eval("cos(x+y)", h));
     *           } catch (Exception ex) {}
     *       }
     *   }
     * </pre>
     *
     * @param exp the infix String expression to evaluate.
     * @param tbl Hashtable with variable value pairs
     * @return a BigDecimal value
     * @throws MathException when evaluation of the expression fails
     */
    public synchronized BigDecimal eval(String exp, Map<String, String> tbl) throws MathException {
        if (exp == null || exp.equals("")) {
            throw new MathException("First argument to method eval is null or empty string");
        } else if (tbl == null) {
            return eval(exp);
        }

        this.storedValues = tbl;
        String tmp = skipSpaces(exp.toLowerCase());

        try {
            BigDecimal ans;
            if (trees.containsKey(tmp)) {
                ans = toValue(trees.get(tmp));
            } else {
                syntax(tmp);

                Node tree = parse(putMultiplicationOperator(parseE(tmp)));

                ans = toValue(tree);

                trees.put(tmp, tree);
            }

            return ans;
        } catch (Exception e) {
            throw new MathException("Evaluation failed", e);
        }
    }

    /**
     * Evaluates the infix expression exp using the variable=value pairs defined in the variables String.
     * <p/>
     * This is one of the publicly available methods of the class, it is an entry point into the parser and the method
     * you want to use when evaluating a String expression.
     * <p/>
     * Example:
     * <pre>
     *   import net.sf.intelliplugin.calc.math.*;
     * <p/>
     *   public class Test {
     *       public static void main(String[] args) {
     *           Eval e = new Eval();
     * <p/>
     *           try {
     *               System.out.println(e.eval("cos(x+y)", "x=pi;y=2.34"));
     *           } catch(Exception ex) {
     *               // handle
     *           }
     *       }
     *   }
     * </pre>
     * For efficiency, {@link #eval(String,Map)} method should be used instead of this method.
     *
     * @param exp       infix String expression to evaluate
     * @param variables semicolon delimited variable=value pairs.
     * @return a BigDecimal value
     * @throws MathException when evaluation of the expression fails
     */
    public synchronized BigDecimal eval(String exp, String variables) throws MathException {
        String temp = "";
        StringTokenizer tok;
        Map<String, String> map = new HashMap<>(1001);

        if (exp == null || exp.equals("")) {
            throw new MathException("First argument to method eval is null or empty string");
        } else if (variables == null || variables.equals("")) {
            return eval(exp);
        }

        try {
            variables = skipSpaces(variables.toLowerCase());
            tok = new StringTokenizer(variables, ";", false);
            while (tok.hasMoreTokens()) {
                temp = tok.nextToken();
                map.put(temp.substring(0, temp.indexOf("=")), temp.substring(temp.indexOf("=") + 1));
            }
        } catch (StringIndexOutOfBoundsException e) {
            throw new MathException("Syntax error ->" + variables);
        } catch (Exception e) {
            throw new MathException("Syntax error ->" + temp);
        }

        return eval(exp, map);
    }

    /**
     * Evaluates the infix expression exp.
     * <p/>
     * This method can only evaluate expressions that have no variables.
     * <p/>
     * This is one of the publicly available methods of the class, it is an entry point into<br> the parser and the
     * method you want to use when evaluating a String expression.
     * <p/>
     * Example:
     * <pre>
     *   import net.sf.intelliplugin.calc.math.*;
     * <p/>
     *   public class Test {
     *       public static void main(String[] args) {
     *           Eval e = new Eval();
     * <p/>
     *           try {
     *               System.out.println(e.eval("12.3+pi"));
     *           } catch(Exception ex) {
     *               // handle
     *           }
     *       }
     *   }
     * </pre>
     * Use one of the other eval methods if you need to have variables in the expression.
     *
     * @param exp the infix String expression to evaluate.
     * @return a BigDecimal value.
     * @throws MathException when evaluation of the expression fails
     */
    private synchronized BigDecimal eval(String exp) throws MathException {
        return eval(exp, new HashMap<>(0));
    }
}



