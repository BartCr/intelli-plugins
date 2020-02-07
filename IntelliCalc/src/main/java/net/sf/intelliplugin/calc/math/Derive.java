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

import java.util.ArrayList;
import java.util.List;

/**
 * A Java class for performing symbolic differentiation of a mathematical expression given as a string.
 * <p>
 * Example:
 * <pre>
 *   Derive d = new Derive();
 *   String ans[] = d.diff( "cos( x-y )" , "x;y" );
 *   System.out.println( ans[ 0 ] + " , " );
 *   System.out.println( ans[ 1 ] );
 * </pre>
 * This will print the following: -1*sin(x-y) , sin(x-y)
 *
 * @author Bart Cremers
 * @since 1.0
 */
public class Derive extends MathBase {

    /**
     * Constructor.
     */
    public Derive() {
        super();
    }

    /**
     * Vector that will contain a list of all variables in the argument expression after a call to diff(..)
     * <p/>
     * Access the variables with the accessor method getVariables
     */
    private final List<String> variables = new ArrayList<>(50);

    /**
     * Returns the first element in a prefix expression.
     *
     * @param str a prefix expression.
     * @return the first element in str.
     *         <p/>
     *         Ex:<br> If str is the expression ( exp x )<br>
     *         car( str ) => "exp"<br>
     *         If str is the expression ( * 3 x )<br>
     *         car( str ) => "*"
     *         <p/>
     *         Why is it called car ? :-)
     */
    private String car(String str) {
        int end = 0;
        int i = 2;
        int count = 0;

        if (str.charAt(2) == '(') {
            while (i < str.length()) {
                if (str.charAt(i) == '(') {
                    count++;
                } else if (str.charAt(i) == ')') {
                    count--;
                }

                if (count == 0) {
                    end = i;
                    break;
                }

                i++;
            }

            return (str.substring(2, end + 1));
        }

        return (str.substring(2, str.indexOf(" ", 2)));
    }

    /**
     * Returns the rest of a prefix expression.
     *
     * @param exp a prefix expression.
     * @return the rest of the prefix expression when the first element has been removed.
     *         <p/>
     *         Ex:<br> If str is the expression ( ^ x 3 )<br> cdr( str ) => "( x 3 )"<br> If str is the expression ( * 3 ( +
     *         2 y ) )<br> cdr( str ) => "( 3 ( + 2 y ) )"
     *         <p/>
     *         Why is it called cdr ? :-)
     */
    private String cdr(String exp) {

        return ('(' + exp.substring(car(exp).length() + 2));
    }

    /**
     * Returns the first argument from a prefix expression.
     *
     * @param str prefix expression.
     * @return the argument.
     *         <p/>
     *         Ex:<br> If str is the expression "( + 2 x )"<br> arg1( str ) => "2"<br> If str is the expression "( + ( * x y
     *         ) 5 )<br> arg1( str ) => "( * x y )"
     */
    private String arg1(String str) {
        return (car(cdr(str)));
    }

    /**
     * Returns the second argument from a prefix expression.
     *
     * @param str prefix expression.
     * @return the argument.
     *         <p/>
     *         Ex:<br> If str is the expression "( + 2 x )"<br> arg2( str ) => "x"<br> If str is the expression "( + 5 ( - x
     *         3 ) )<br> arg2( str ) => "( - x 3 )"
     */
    private String arg2(String str) {
        return (car(cdr(cdr(str))));
    }

    /**
     * Constructs a prefix expression: ( <i>op a b</i> )
     *
     * @param op operator.
     * @param a  first argument.
     * @param b  second argument.
     * @return a prefix expression
     */
    private String list(String op, String a, String b) {
        return "( " + op + " " + a + " " + b + " )";
    }

    /**
     * Constructs a prefix expression: ( <i>op a</i> )
     *
     * @param op operator.
     * @param a  argument.
     * @return a prefix expression
     */
    private String list(String op, String a) {
        return "( " + op + " " + a + " )";
    }

    /**
     * Checks to see if a and b are valid variables and equal.
     *
     * @param a string to check and compare with b.
     * @param b string to check and compare with a.
     * @return boolean, true or false.
     */
    private boolean isSameVariable(String a, String b) {
        return isVariable(a) && isVariable(b) && a.equalsIgnoreCase(b);
    }

    /**
     * Checks to see if b equals 1.0 .
     *
     * @param b string to check.
     * @return boolean, true or false.
     *         <p/>
     *         isConstant should be called before this method
     */
    private boolean isOne(String b) {
        return Double.parseDouble(b) == 1.0;
    }

    /**
     * Checks to see if b equals 0.0 .
     *
     * @param b string to check.
     * @return boolean, true or false.
     *         <p/>
     *         isConstant should be called before this method
     */
    private boolean isZero(String b) {
        return Double.parseDouble(b) == 0.0;
    }

    /**
     * Checks the first element in a prefix expression<br> too se if it's the operator '+'
     *
     * @param str prefix expression to check.
     * @return boolean, true or false.
     *         <p/>
     *         Ex:<br> if str is the expression ( + 3 x )<br> isSum( str ) => true<br> If str is the expression ( * 3 x
     *         )<br> isSum( str ) => false
     */
    private boolean isSum(String str) {
        return car(str).equals("+");
    }

    /**
     * Checks the first element in a prefix expression<br> too se if it's the operator '-'
     *
     * @param str prefix expression to check.
     * @return boolean, true or false.
     *         <p/>
     *         Ex:<br> if str is the expression ( - 3 x )<br> isSubtraction( str ) => true<br> If str is the expression ( *
     *         3 x )<br> isSubtraction( str ) => false
     */
    private boolean isSubtraction(String str) {
        return car(str).equals("-");
    }

    /**
     * Checks the first element in a prefix expression<br> too se if it's the operator '*'
     *
     * @param str prefix expression to check.
     * @return boolean, true or false.
     *         <p/>
     *         Ex:<br> if str is the expression ( * 3 x )<br> isProduct( str ) => true<br> If str is the expression ( - 3 x
     *         )<br> isProduct( str ) => false
     */
    private boolean isProduct(String str) {
        return car(str).equals("*");
    }

    /**
     * Checks the first element in a prefix expression<br> too se if it's the operator '/'
     *
     * @param str prefix expression to check.
     * @return boolean, true or false.
     *         <p/>
     *         Ex:<br> if str is the expression ( / 3 x )<br> isDivision( str ) => true<br> If str is the expression ( * 3 x
     *         )<br> isDivision( str ) => false
     */
    private boolean isDivision(String str) {
        return car(str).equals("/");
    }

    /**
     * Checks the first element in a prefix expression<br> too se if it's the operator 'sqrt'
     *
     * @param exp prefix expression to check.
     * @return boolean, true or false.
     *         <p/>
     *         Ex:<br> if str is the expression ( sqrt x )<br> isSquareRoot( str ) => true<br> If str is the expression ( *
     *         3 x )<br> isSquareRoot( str ) => false
     */
    private boolean isSquareRoot(String exp) {
        return car(exp).equals("sqrt");
    }

    /**
     * Checks the first element in a prefix expression<br> too se if it's the operator 'cos'
     *
     * @param exp prefix expression to check.
     * @return boolean, true or false.
     *         <p/>
     *         Ex:<br> if str is the expression ( cos x )<br> isCosine( str ) => true<br> If str is the expression ( * 3 x
     *         )<br> isCosine( str ) => false
     */
    private boolean isCosine(String exp) {
        return car(exp).equals("cos");
    }

    /**
     * Checks the first element in a prefix expression<br> too se if it's the operator 'sin'
     *
     * @param exp prefix expression to check.
     * @return boolean, true or false.
     *         <p/>
     *         Ex:<br> if str is the expression ( sin x )<br> isSine( str ) => true<br> If str is the expression ( * 3 x
     *         )<br> isSine( str ) => false
     */
    private boolean isSine(String exp) {
        return car(exp).equals("sin");
    }

    /**
     * Checks the first element in a prefix expression<br> too se if it's the operator 'tan'
     *
     * @param exp prefix expression to check.
     * @return boolean, true or false.
     *         <p/>
     *         Ex:<br> if str is the expression ( tan x )<br> isTan( str ) => true<br> If str is the expression ( * 3 x
     *         )<br> isTan( str ) => false
     */
    private boolean isTan(String exp) {
        return car(exp).equals("tan");
    }

    /**
     * Checks the first element in a prefix expression<br> too se if it's the operator 'atan'
     *
     * @param exp prefix expression to check.
     * @return boolean, true or false.
     *         <p/>
     *         Ex:<br> if str is the expression ( atan x )<br> isAtan( str ) => true<br> If str is the expression ( * 3 x
     *         )<br> isAtan( str ) => false
     */
    private boolean isAtan(String exp) {
        return car(exp).equals("atan");
    }

    /**
     * Checks the first element in a prefix expression<br> too se if it's the operator 'acos'
     *
     * @param exp prefix expression to check.
     * @return boolean, true or false.
     *         <p/>
     *         Ex:<br> if str is the expression ( acos x )<br> isAcos( str ) => true<br> If str is the expression ( * 3 x
     *         )<br> isAcos( str ) => false
     */
    private boolean isAcos(String exp) {
        return car(exp).equals("acos");
    }

    /**
     * Checks the first element in a prefix expression<br> too se if it's the operator 'asin'
     *
     * @param exp prefix expression to check.
     * @return boolean, true or false.
     *         <p/>
     *         Ex:<br> if str is the expression ( asin x )<br> isAsin( str ) => true<br> If str is the expression ( * 3 x
     *         )<br> isAsin( str ) => false
     */
    private boolean isAsin(String exp) {
        return car(exp).equals("asin");
    }

    /**
     * Checks the first element in a prefix expression<br> too se if it's the operator 'sinh'
     *
     * @param exp prefix expression to check.
     * @return boolean, true or false.
     *         <p/>
     *         Ex:<br> if str is the expression ( sinh x )<br> isSinHyp( str ) => true<br> If str is the expression ( * 3 x
     *         )<br> isSinHyp( str ) => false
     */
    private boolean isSinHyp(String exp) {
        return car(exp).equals("sinh");
    }

    /**
     * Checks the first element in a prefix expression<br> too se if it's the operator 'cosh'
     *
     * @param exp prefix expression to check.
     * @return boolean, true or false.
     *         <p/>
     *         Ex:<br> if str is the expression ( cosh x )<br> isCosHyp( str ) => true<br> If str is the expression ( * 3 x
     *         )<br> isCosHyp( str ) => false
     */
    private boolean isCosHyp(String exp) {
        return car(exp).equals("cosh");
    }

    /**
     * Checks the first element in a prefix expression<br> too se if it's the operator 'tanh'
     *
     * @param exp prefix expression to check.
     * @return boolean, true or false.
     *         <p/>
     *         Ex:<br> if str is the expression ( tanh x )<br> isTanHyp( str ) => true<br> If str is the expression ( * 3 x
     *         )<br> isTanHyp( str ) => false
     */
    private boolean isTanHyp(String exp) {
        return car(exp).equals("tanh");
    }

    /**
     * Checks the first element in a prefix expression<br> too se if it's the operator 'ln'
     *
     * @param exp prefix expression to check.
     * @return boolean, true or false.
     *         <p/>
     *         Ex:<br> if str is the expression ( ln x )<br> isLn( str ) => true<br> If str is the expression ( * 3 x )<br>
     *         isLn( str ) => false
     */
    private boolean isLn(String exp) {
        return car(exp).equals("ln");
    }

    /**
     * Checks the first element in a prefix expression<br> too se if it's the operator '^'
     *
     * @param exp prefix expression to check.
     * @return boolean, true or false.
     *         <p/>
     *         Ex:<br> if str is the expression ( ^ x 2 )<br> isPower( str ) => true<br> If str is the expression ( * 3 x
     *         )<br> isPower( str ) => false
     */
    private boolean isPower(String exp) {
        return car(exp).equals("^");
    }

    /**
     * Checks the first element in a prefix expression<br> too se if it's the operator 'exp'
     *
     * @param exp prefix expression to check.
     * @return boolean, true or false.
     *         <p/>
     *         Ex:<br> if str is the expression ( exp x )<br> isE( str ) => true<br> If str is the expression ( * 3 x )<br>
     *         isE( str ) => false
     */
    private boolean isE(String exp) {
        return car(exp).equals("exp");
    }

    /**
     * Checks the first element in a prefix expression<br> too se if it's the operator 'cotan'
     *
     * @param exp prefix expression to check.
     * @return boolean, true or false.
     *         <p/>
     *         Ex:<br> if str is the expression ( cotan x )<br> isCotan( str ) => true<br> If str is the expression ( * 3 x
     *         )<br> isCotan( str ) => false
     */
    private boolean isCotan(String exp) {
        return car(exp).equals("cotan");
    }

    /**
     * Checks the first element in a prefix expression<br> too se if it's the operator 'acotan'
     *
     * @param exp prefix expression to check.
     * @return boolean, true or false.
     *         <p/>
     *         Ex:<br> if str is the expression ( acotan x )<br> isAcotan( str ) => true<br> If str is the expression ( * 3
     *         x )<br> isAcotan( str ) => false
     */
    private boolean isAcotan(String exp) {
        return car(exp).equals("acotan");
    }

    /**
     * Constructs the prefix expression: ( exp <i>op</i> )
     *
     * @param op operator.
     * @return the prefix expression
     */
    private String makeE(String op) {
        if (!isConstant(op) && !isVariable(op) && isLn(op)) {
            return arg1(op);
        } else if (isConstant(op) && isZero(op)) {
            return "1";
        }

        return list("exp", op);
    }

    /**
     * Constructs the prefix expression: ( + <i>a b</i> )
     *
     * @param a  first argument.
     * @param b  second argument
     * @return the prefix expression
     */
    private String makeSum(String a, String b) {
        if (isConstant(a) && isConstant(b)) {
            return String.valueOf(Double.parseDouble(a) + Double.parseDouble(b));
        } else if (isConstant(a) && isZero(a)) {
            return b;
        } else if (isConstant(b) && isZero(b)) {
            return a;
        } else if (a.equals(b)) {
            return makeProduct("2", a);
        } else if (!isConstant(a) && !isVariable(a)) {
            if (isConstant(b)) {
                // error here
                return makeSumSimplifyConstant(a, b);
                //
            } else if (isVariable(b)) {
                return makeSumSimplifyVariable(a, b);
            } else {
                return makeSumSimplifyTwoExpressions(a, b);
            }
        } else if (!isConstant(b) && !isVariable(b)) {
            if (isConstant(a)) {
                return makeSumSimplifyConstant(b, a);
            } else if (isVariable(a)) {
                return makeSumSimplifyVariable(b, a);
            } else {
                return makeSumSimplifyTwoExpressions(b, a);
            }
        }

        return (list("+", a, b));
    }

    /**
     * Constructs the prefix expression: ( sqrt <i>a b</i> )
     *
     * @param a  argument.
     * @return the prefix expression
     */
    private String makeSquareRoot(String a) {
        if (isConstant(a) && isEven(Math.sqrt(Double.parseDouble(a)))) {
            return String.valueOf(Math.sqrt(Double.parseDouble(a)));
        } else if (!isConstant(a) && !isVariable(a) && isPower(a)) {
            if (isEven(Double.parseDouble(arg2(a)))) {
                return makePower(arg1(a), String.valueOf(Double.parseDouble(arg2(a)) / 2));
            }
        }

        return (list("sqrt", a));
    }

    /**
     * Constructs the prefix expression: ( * <i>a b</i> )
     *
     * @param a  first argument.
     * @param b  second argument
     * @return the prefix expression
     */
    private String makeProduct(String a, String b) {
        if (isConstant(a) && isConstant(b)) {
            return String.valueOf(Double.parseDouble(a) * Double.parseDouble(b));
        } else if (isConstant(a) && isZero(a)) {
            return "0";
        } else if (isConstant(a) && isOne(a)) {
            return b;
        } else if (isConstant(b) && isZero(b)) {
            return "0";
        } else if (isConstant(b) && isOne(b)) {
            return a;
        } else if (a.equals(b)) {
            return (makePower(a, "2"));
        } else if (!isConstant(a) && !isVariable(a) && !isConstant(b) && !isVariable(b)) {
            return (makeProductSimplifyTwoExp(b, a));
        } else if (!isConstant(a) && !isVariable(a)) {
            if (isConstant(b)) {
                return makeProductSimplifyConstant(a, b);
            } else if (isVariable(b)) {
                return makeProductSimplifyVariable(a, b);
            }
        } else if (!isConstant(b) && !isVariable(b)) {
            if (isConstant(a)) {
                return makeProductSimplifyConstant(b, a);
            } else if (isVariable(a)) {
                return makeProductSimplifyVariable(b, a);
            }
        }

        return (list("*", a, b));
    }

    /**
     * Constructs the prefix expression: ( / <i>a b</i> )
     *
     * @param a  first argument.
     * @param b  second argument
     * @return the prefix expression
     */
    private String makeDivision(String a, String b) {
        if (isConstant(a) && isConstant(b)) {
            double val = Double.parseDouble(a) / Double.parseDouble(b);
            if (Double.parseDouble(b) != 0 && isInteger(val)) {
                return String.valueOf(val);
            }
        } else if (isConstant(a) && isZero(a)) {
            return "0";
        } else if (isConstant(b) && isOne(b)) {
            return a;
        } else if (a.equals(b)) {
            return "1";
        } else if (!isConstant(a) && !isVariable(a)) {
            if (isSum(a) || isSubtraction(a)) {
                return (makeDivisionSimplifyExprThruVar(a, b));
            } else if (isDivision(a)) {
                if (isVariable(b) || isConstant(b)) {
                    return (makeDivision(arg1(a), makeProduct(b, arg2(a))));
                } else if (isDivision(b)) {
                    return (makeDivision(makeProduct(arg1(a), arg2(b)), makeProduct(arg2(a), arg1(b))));
                }
            }
        }

        return (list("/", a, b));
    }

    /**
     * Constructs the prefix expression: ( - <i>a b</i> )
     *
     * @param a  first argument.
     * @param b  second argument
     * @return the prefix expression
     */
    private String makeSubtraction(String a, String b) {
        if (isConstant(a) && isConstant(b)) {
            return String.valueOf(Double.parseDouble(a) - Double.parseDouble(b));
        } else if (isConstant(a) && isZero(a)) {
            return makeProduct("-1", b);
        } else if (isConstant(b) && isZero(b)) {
            return a;
        } else if (a.equals(b)) {
            return "0";
        } else if (!isConstant(b) && !isVariable(b) && (isConstant(a) || isVariable(a))) {
            return makeSubtractionSimplifyConstantVariableArg1(a, b);
        } else if (!isConstant(a) && !isVariable(a) && (isConstant(b) || isVariable(b))) {
            return makeSubtractionSimplifyConstantVariableArg2(a, b);
        } else if (!isConstant(a) && !isVariable(a) && !isConstant(b) && !isVariable(b)) {
            return makeSubtractionSimplifyTwoExp(a, b);
        }

        return list("-", a, b);
    }

    /**
     * Constructs the prefix expression: ( ^ <i>a b</i> )
     *
     * @param a  first argument.
     * @param b  second argument
     * @return the prefix expression
     */
    private String makePower(String a, String b) {

        if (isConstant(a) && isConstant(b)) {
            if (isOne(a) || isZero(b)) {
                return "1";
            } else if (isOne(b)) {
                return a;
            } else {
                double pow = Math.pow(Double.parseDouble(a), Double.parseDouble(b));
                if (isInteger(pow)) {
                    return String.valueOf(pow);
                }
            }
        } else if (isConstant(b) && isZero(b)) {
            return "1";
        } else if (isConstant(b) && isOne(b)) {
            return a;
        } else if (!isConstant(a) && !isVariable(a) && isPower(a)) {
            if (isConstant(b) && isConstant(arg2(a))) {
                return makePower(arg1(a), makeProduct(arg2(a), b));
            }
        }

        return list("^", a, b);
    }

    /**
     * Constructs the prefix expression: ( sin <i>a</i> )
     *
     * @param a  argument.
     * @return the prefix expression
     */
    private String makeSine(String a) {
        if (!isVariable(a) && !isConstant(a) && isAsin(a)) {
            return arg1(a);
        } else if (!isVariable(a) && !isConstant(a) && isAcos(a)) {
            return makeSquareRoot(makeSubtraction("1", makePower(arg1(a), "2")));
        }

        return list("sin", a);
    }

    /**
     * Constructs the prefix expression: ( cos <i>a</i> )
     *
     * @param a  argument.
     * @return the prefix expression
     */
    private String makeCosine(String a) {
        if (!isVariable(a) && !isConstant(a) && isAcos(a)) {
            return arg1(a);
        } else if (!isVariable(a) && !isConstant(a) && isAsin(a)) {
            return makeSquareRoot(makeSubtraction("1", makePower(arg1(a), "2")));
        }

        return list("cos", a);
    }

    /**
     * Constructs the prefix expression: ( tan <i>a</i> )
     *
     * @param a  argument.
     * @return the prefix expression
     */
    private String makeTan(String a) {
        if (!isVariable(a) && !isConstant(a) && isAtan(a)) {
            return arg1(a);
        } else if (!isVariable(a) && !isConstant(a) && isAcotan(a)) {
            return makeDivision("1", arg1(a));
        }

        return list("tan", a);
    }

    /**
     * Constructs the prefix expression: ( ln <i>a</i> )
     *
     * @param a  argument.
     * @return the prefix expression
     */
    private String makeLn(String a) {
        if (!isVariable(a) && !isConstant(a) && isE(a)) {
            return (arg1(a));
        }

        return (list("ln", a));
    }

    /**
     * Constructs the prefix expression: ( sinh <i>a</i> )
     *
     * @param a  argument.
     * @return the prefix expression
     */
    private String makeSinHyp(String a) {
        return (list("sinh", a));
    }

    /**
     * Constructs the prefix expression: ( cosh <i>a</i> )
     *
     * @param a  argument.
     * @return the prefix expression
     */
    private String makeCosHyp(String a) {
        return (list("cosh", a));
    }

    /**
     * Constructs the prefix expression: ( tanh <i>a</i> )
     *
     * @param a  argument.
     * @return the prefix expression
     */
    private String makeTanHyp(String a) {
        return (list("tanh", a));
    }

    /**
     * Constructs the prefix expression: ( cotan <i>a</i> )
     *
     * @param a  argument.
     * @return the prefix expression
     */
    private String makeCotan(String a) {
        if (!isVariable(a) && !isConstant(a) && isAcotan(a)) {
            return (arg1(a));
        }

        return (list("cotan", a));
    }

    /**
     * Constructs the prefix expression: ( acotan <i>a</i> )
     *
     * @param a  argument.
     * @return the prefix expression
     */
    private String makeAcotan(String a) {
        if (!isVariable(a) && !isConstant(a) && isCotan(a)) {
            return (arg1(a));
        }

        return (list("acotan", a));
    }

    /**
     * Help method for makeSum.<br> makes some simplifications.<br> Ex: ( + 5  ( + 3 x ) ) => ( + 8 x )
     *
     * @param a  first argument.
     * @param b  second argument
     * @return a simplified expression
     */
    private String makeSumSimplifyConstant(String a, String b) {
        double tmp;
        if (isSum(a)) {
            if (isConstant(arg1(a))) {
                tmp = Double.parseDouble(b) + Double.parseDouble(arg1(a));
                if (tmp >= 0) {
                    return makeSum(String.valueOf(tmp), arg2(a));
                } else {
                    return makeSubtraction(arg2(a), String.valueOf(-1 * tmp));
                }
            } else if (isConstant(arg2(a))) {
                tmp = Double.parseDouble(b) + Double.parseDouble(arg2(a));
                if (tmp >= 0) {
                    return makeSum(String.valueOf(tmp), arg1(a));
                } else {
                    return makeSubtraction(arg1(a), String.valueOf(-1 * tmp));
                }
            }
        } else if (isSubtraction(a)) {
            if (isConstant(arg1(a))) {
                tmp = Double.parseDouble(b) + Double.parseDouble(arg1(a));
                // if( tmp >= 0 ){
                return makeSubtraction(String.valueOf(tmp), arg2(a));
                // }
                // else
                //{
                //  return( makeSubtraction( arg2(a) , ( new StringBuilder().append( -1 * tmp ) ).toString() ) );
                //}
            } else if (isConstant(arg2(a))) {
                tmp = Double.parseDouble(b) - Double.parseDouble(arg2(a));
                if (tmp >= 0) {
                    return makeSum(String.valueOf(tmp), arg1(a));
                } else {
                    return makeSubtraction(arg1(a), String.valueOf(-1 * tmp));
                }
            }
        }

        return list("+", a, b);
    }

    /**
     * Help method for makeSum.<br> makes some simplifications.<br> Ex: ( + x  ( + 3 x ) ) => ( + 3 ( * 2 x ) )<br>
     *
     * @param a  first argument.
     * @param b  second argument
     * @return a simplified expression
     */
    private String makeSumSimplifyVariable(String a, String b) {
        if (isSum(a)) {
            if (isVariable(arg1(a)) && isSameVariable(b, arg1(a))) {
                return makeSum(makeProduct("2", b), arg2(a));
            } else if (isVariable(arg2(a)) && isSameVariable(b, arg2(a))) {
                return makeSum(makeProduct("2", b), arg1(a));
            }
        } else if (isSubtraction(a)) {
            if (isVariable(arg1(a)) && isSameVariable(b, arg1(a))) {
                return makeSum(makeProduct("2", b), arg2(a));
            } else if (isVariable(arg2(a)) && isSameVariable(b, arg2(a))) {
                return arg1(a);
            }
        } else if (isProduct(a)) {
            if (isConstant(arg1(a)) && arg2(a).equals(b)) {
                return makeProduct(makeSum("1", arg1(a)), b);
            } else if (isConstant(arg2(a)) && arg1(a).equals(b)) {
                return makeProduct(makeSum("1", arg2(a)), b);
            }
        }

        return list("+", a, b);
    }

    /**
     * Help method for makeSum.<br> makes some simplifications.<br> Ex: ( + ( + 2 x )  ( + 2 x ) ) => ( + 4 ( * 2 x ) )
     *
     * @param a  first argument.
     * @param b  second argument
     * @return a simplified expression
     */
    private String makeSumSimplifyTwoExpressions(String a, String b) {
        if (isSum(a) && isSum(b)) {
            if (arg1(a).equals(arg1(b))) {
                return makeSum(makeProduct("2", arg1(a)), makeSum(arg2(a), arg2(b)));
            } else if (arg2(a).equals(arg2(b))) {
                return makeSum(makeProduct("2", arg2(a)), makeSum(arg1(a), arg1(b)));
            } else if (arg1(a).equals(arg2(b))) {
                return makeSum(makeProduct("2", arg1(a)), makeSum(arg2(a), arg1(b)));
            } else if (arg2(a).equals(arg1(b))) {
                return makeSum(makeProduct("2", arg1(b)), makeSum(arg1(a), arg2(b)));
            }
        } else if (isSum(a) && isSubtraction(b)) {
            if (arg1(a).equals(arg1(b))) {
                return makeSum(makeProduct("2", arg1(a)), makeSubtraction(arg2(a), arg2(b)));
            } else if (arg1(a).equals(arg2(b))) {
                return makeSum(arg2(a), arg1(b));
            } else if (arg2(a).equals(arg1(b))) {
                return makeSum(makeProduct("2", arg1(b)), makeSubtraction(arg1(a), arg2(b)));
            } else if (arg2(a).equals(arg2(b))) {
                return makeSum(arg1(a), arg1(b));
            }
        } else if (isSum(a) && isProduct(b)) {
            if (isConstant(arg1(b))) {
                if (arg1(a).equals(arg2(b))) {
                    return makeSum(arg2(a), makeProduct(makeSum("1", arg1(b)), arg1(a)));
                } else if (arg2(a).equals(arg2(b))) {
                    return makeSum(arg1(a), makeProduct(makeSum("1", arg1(b)), arg2(a)));
                }
            } else if (isConstant(arg2(b))) {
                if (arg1(a).equals(arg1(b))) {
                    return makeSum(arg2(a), makeProduct(makeSum("1", arg2(b)), arg1(a)));
                } else if (arg2(a).equals(arg1(b))) {
                    return makeSum(arg1(a), makeProduct(makeSum("1", arg2(b)), arg2(a)));
                }
            }
        } else if (isSubtraction(a) && isSum(b)) {
            return makeSumSimplifyTwoExpressions(b, a);
        } else if (isSubtraction(a) && isSubtraction(b)) {
            if (arg1(a).equals(arg1(b))) {
                return makeSubtraction(makeProduct("2", arg1(a)), makeSum(arg2(a), arg2(b)));
            } else if (arg1(a).equals(arg2(b))) {
                return makeSubtraction(arg1(b), arg2(a));
            } else if (arg2(a).equals(arg1(b))) {
                return makeSubtraction(arg1(a), arg2(b));
            } else if (arg2(a).equals(arg2(b))) {
                return makeSubtraction(makeSum(arg1(a), arg1(b)), makeProduct("2", arg2(a)));
            }
        } else if (isSubtraction(a) && isProduct(b)) {
            if (isConstant(arg1(b))) {
                if (arg1(a).equals(arg2(b))) {
                    return makeSubtraction(makeProduct(makeSum("1", arg1(b)), arg1(a)), arg2(a));
                } else if (arg2(a).equals(arg2(b))) {
                    return makeSum(makeProduct(makeSubtraction(arg1(b), "1"), arg2(a)), arg1(a));
                }
            } else if (isConstant(arg2(b))) {
                if (arg1(a).equals(arg1(b))) {
                    return makeSubtraction(makeProduct(makeSum("1", arg2(b)), arg1(a)), arg2(a));
                } else if (arg2(a).equals(arg1(b))) {
                    return makeSum(makeProduct(makeSubtraction(arg2(b), "1"), arg2(a)), arg1(a));
                }
            }
        }

        return list("+", a, b);
    }

    /**
     * Help method for makeProduct.<br> makes some simplifications.<br> Ex: ( * ( + 3 x ) x ) => ( + ( * 3 x ) ( * x x ) )
     *
     * @param a  first argument.
     * @param b  second argument
     * @return a simplified expression
     */
    private String makeProductSimplifyVariable(String a, String b) {
        if (isSum(a)) {
            return makeSum(makeProduct(b, arg1(a)), makeProduct(b, arg2(a)));
        } else if (isSubtraction(a)) {
            return makeSubtraction(makeProduct(b, arg1(a)), makeProduct(b, arg2(a)));
        } else if (isPower(a)) {
            if (b.equals(arg1(a))) {
                return makePower(b, makeSum("1", arg2(a)));
            }
        }

        return list("*", b, a);
    }

    /**
     * Help method for makeProduct.<br> makes some simplifications.<br> Ex: ( * ( + 3 x ) ( - 3 x ) ) => ( - ( ^ 3 2 ) ( ^ x
     * 2 ) )
     *
     * @param a  first argument.
     * @param b  second argument
     * @return a simplified expression
     */
    private String makeProductSimplifyTwoExp(String a, String b) {
        if (isSum(a)
            && isSubtraction(b)
            && arg1(a).equals(arg1(b))
            && arg2(a).equals(arg2(b))) {
            return makeSubtraction(makePower(arg1(a), "2"), makePower(arg2(a), "2"));
        } else if (isSum(a)
                   && isSubtraction(b)
                   && arg1(a).equals(arg2(b))
                   && arg2(a).equals(arg1(b))) {
            return makeSubtraction(makePower(arg2(a), "2"), makePower(arg1(a), "2"));
        } else if (isSubtraction(a)
                   && isSum(b)
                   && arg1(a).equals(arg1(b))
                   && arg2(a).equals(arg2(b))) {
            return makeSubtraction(makePower(arg1(a), "2"), makePower(arg2(a), "2"));
        } else if (isSubtraction(a)
                   && isSum(b)
                   && arg1(a).equals(arg2(b))
                   && arg2(a).equals(arg1(b))) {
            return makeSubtraction(makePower(arg1(a), "2"), makePower(arg2(a), "2"));
        }

        return (list("*", a, b));
    }

    /**
     * Help method for makeProduct.<br> makes some simplifications.<br> Ex: ( * ( + 3 x ) 5 ) => ( + 15 ( * 5 x ) )
     *
     * @param a  first argument.
     * @param b  second argument
     * @return a simplified expression
     */
    private String makeProductSimplifyConstant(String a, String b) {
        double temp = Double.parseDouble(b);

        if (isSum(a)) {
            if (temp < 0) {
                return makeSubtraction(makeProduct(b, arg1(a)), makeProduct(String.valueOf(-1 * temp), arg2(a)));
            } else if (temp > 0) {
                return makeSum(makeProduct(b, arg1(a)), makeProduct(b, arg2(a)));
            } else {
                return "0";
            }
        } else if (isSubtraction(a)) {
            if (temp > 0) {
                return makeSubtraction(makeProduct(b, arg1(a)), makeProduct(b, arg2(a)));
            } else if (temp < 0) {
                return makeSum(makeProduct(b, arg1(a)), makeProduct(String.valueOf(-1 * temp), arg2(a)));
            } else {
                return "0";
            }
        } else if (isProduct(a)) {
            if (isConstant(arg1(a))) {
                return makeProduct(String.valueOf(temp * Double.parseDouble(arg1(a))), arg2(a));
            } else if (isConstant(arg2(a))) {
                return makeProduct(String.valueOf(temp * Double.parseDouble(arg2(a))), arg1(a));
            }
        }

        return list("*", b, a);
    }

    /**
     * Help method for makeDivision.<br> makes some simplifications.<br> Ex: ( / ( + 3 x ) x ) => ( + ( / 3 x ) 1 )
     *
     * @param a  first argument.
     * @param b  second argument
     * @return a simplified expression
     */
    private String makeDivisionSimplifyExprThruVar(String a, String b) {
        if (b.equals(arg1(a))) {
            if (isSum(a)) {
                return makeSum("1", makeDivision(arg2(a), b));
            } else if (isSubtraction(a)) {
                return makeSubtraction("1", makeDivision(arg2(a), b));
            } else if (isProduct(a)) {
                return makeDivision(arg2(a), b);
            }
        } else if (b.equals(arg2(a))) {
            if (isSum(a)) {
                return makeSum(makeDivision(arg1(a), b), "1");
            } else if (isSubtraction(a)) {
                return makeSubtraction(makeDivision(arg1(a), b), "1");
            } else if (isProduct(a)) {
                return makeDivision(arg1(a), b);
            }
        }

        return list("/", a, b);
    }

    /**
     * Help method for makeSubtraction.<br> makes some simplifications.<br> Ex: ( - ( + 5 x ) 2 ) => ( + 3 x )
     *
     * @param a  first argument.
     * @param b  second argument
     * @return a simplified expression
     */
    private String makeSubtractionSimplifyConstantVariableArg2(String a, String b) {
        if (isConstant(b)) {
            if (isSum(a)) {
                if (isConstant(arg1(a))) {
                    return makeSum(String.valueOf(Double.parseDouble(arg1(a)) - Double.parseDouble(b)), arg2(a));
                } else if (isConstant(arg2(a))) {
                    return makeSum(String.valueOf(Double.parseDouble(arg2(a)) - Double.parseDouble(b)), arg1(a));
                }
            } else if (isSubtraction(a)) {
                if (isConstant(arg1(a))) {
                    return makeSubtraction(String.valueOf(Double.parseDouble(arg1(a)) - Double.parseDouble(b)), arg2(a));
                } else if (isConstant(arg2(a))) {
                    return makeSubtraction(arg1(a), String.valueOf(Double.parseDouble(arg2(a)) - Double.parseDouble(b)));
                }
            }
        } else if (isVariable(b)) {
            if (isSum(a)) {
                if (isVariable(arg1(a)) && isSameVariable(b, arg1(a))) {
                    return arg2(a);
                } else if (isVariable(arg2(a)) && isSameVariable(b, arg2(a))) {
                    return arg1(a);
                }
            } else if (isSubtraction(a)) {
                if (isVariable(arg1(a)) && isSameVariable(b, arg1(a))) {
                    return makeProduct("-1", arg2(a));
                } else if (isVariable(arg2(a)) && isSameVariable(b, arg2(a))) {
                    return makeSubtraction(arg1(a), makeProduct("2", arg2(a)));
                }
            } else if (isProduct(a)) {
                if (isConstant(arg1(a)) && arg2(a).equals(b)) {
                    return makeProduct(makeSubtraction(arg1(a), "1"), b);
                } else if (isConstant(arg2(a)) && arg1(a).equals(b)) {
                    return makeProduct(makeSubtraction(arg2(a), "1"), b);
                }
            }
        }

        return list("-", a, b);
    }

    /**
     * Help method for makeSubtraction.<br> makes some simplifications.<br> Ex: ( - 5 ( + 2 x ) ) => ( - 3 x )
     *
     * @param a  first argument.
     * @param b  second argument
     * @return a simplified expression
     */
    private String makeSubtractionSimplifyConstantVariableArg1(String a, String b) {
        if (isConstant(a)) {
            if (isSum(b)) {
                if (isConstant(arg1(b))) {
                    return makeSubtraction(String.valueOf(Double.parseDouble(a) - Double.parseDouble(
                            arg1(b))), arg2(b));
                } else if (isConstant(arg2(b))) {
                    return makeSubtraction(String.valueOf(Double.parseDouble(a) - Double.parseDouble(
                            arg2(b))), arg1(b));
                }
            } else if (isSubtraction(b)) {
                if (isConstant(arg1(b))) {
                    return makeSum(String.valueOf(Double.parseDouble(a) - Double.parseDouble(arg1(b))), arg2(b));
                } else if (isConstant(arg2(b))) {
                    return makeSubtraction(String.valueOf(Double.parseDouble(a) + Double.parseDouble(
                            arg2(b))), arg1(b));
                }
            }
        } else if (isVariable(a)) {
            if (isSum(b)) {
                if (isVariable(arg1(b)) && isSameVariable(a, arg1(b))) {
                    return makeProduct("-1", arg2(b));
                } else if (isVariable(arg2(b)) && isSameVariable(a, arg2(b))) {
                    return makeProduct("-1", arg1(b));
                }
            } else if (isSubtraction(b)) {
                if (isVariable(arg1(b)) && isSameVariable(a, arg1(b))) {
                    return arg2(b);
                } else if (isVariable(arg2(b)) && isSameVariable(a, arg2(b))) {
                    return makeSubtraction(arg1(b), makeProduct("2", arg2(b)));
                }
            } else if (isProduct(b)) {
                if (isConstant(arg1(b)) && arg2(b).equals(a)) {
                    return makeProduct(makeSubtraction("1", arg1(b)), a);
                } else if (isConstant(arg2(b)) && arg1(b).equals(a)) {
                    return makeProduct(makeSubtraction("1", arg2(b)), a);
                }
            }
        }
        return list("-", a, b);
    }

    /**
     * Help method for makeSubtraction.<br> makes some simplifications.<br> Ex: ( - ( + 5 x ) ( + 2 x ) ) => ( - 5 0 )
     *
     * @param a  first argument.
     * @param b  second argument
     * @return a simplified expression
     */
    private String makeSubtractionSimplifyTwoExp(String a, String b) {
        if (isSum(a) && isSum(b)) {
            if (arg1(a).equals(arg1(b))) {
                return makeSubtraction(arg2(a), arg2(b));
            } else if (arg1(a).equals(arg2(b))) {
                return makeSubtraction(arg2(a), arg1(b));
            } else if (arg2(a).equals(arg1(b))) {
                return makeSubtraction(arg1(a), arg2(b));
            } else if (arg2(a).equals(arg2(b))) {
                return makeSubtraction(arg1(a), arg1(b));
            }
        } else if (isSum(a) && isSubtraction(b)) {
            if (arg1(a).equals(arg1(b))) {
                return makeSum(arg2(a), arg2(b));
            } else if (arg1(a).equals(arg2(b))) {
                return makeSum(makeProduct("2", arg1(a)), makeSubtraction(arg2(a), arg1(b)));
            } else if (arg2(a).equals(arg1(b))) {
                return makeSum(arg1(a), arg2(b));
            } else if (arg2(a).equals(arg2(b))) {
                return makeSum(makeProduct("2", arg2(a)), makeSubtraction(arg1(a), arg1(b)));
            }
        } else if (isSum(a) && isProduct(b)) {
            if (isConstant(arg1(b))) {
                if (arg1(a).equals(arg2(b))) {
                    return makeSum(makeProduct(makeSubtraction("1", arg1(b)), arg1(a)), arg2(a));
                } else if (arg2(a).equals(arg2(b))) {
                    return makeSum(makeProduct(makeSubtraction("1", arg1(b)), arg2(a)), arg1(a));
                }
            } else if (isConstant(arg2(b))) {
                if (arg1(a).equals(arg1(b))) {
                    return makeSum(makeProduct(makeSubtraction("1", arg2(b)), arg1(a)), arg2(a));
                } else if (arg2(a).equals(arg1(b))) {
                    return makeSum(makeProduct(makeSubtraction("1", arg2(b)), arg2(a)), arg1(a));
                }
            }
        } else if (isSubtraction(a) && isSum(b)) {
            if (arg1(a).equals(arg1(b))) {
                return makeSubtraction(makeProduct("-1", arg2(a)), arg2(b));
            } else if (arg1(a).equals(arg2(b))) {
                return makeSubtraction(makeProduct("-1", arg2(a)), arg1(b));
            } else if (arg2(a).equals(arg1(b))) {
                return makeSubtraction(makeSubtraction(arg1(a), arg2(b)), makeProduct("2", arg1(b)));
            } else if (arg2(a).equals(arg2(b))) {
                return makeSubtraction(makeSubtraction(arg1(a), arg1(b)), makeProduct("2", arg2(a)));
            }
        } else if (isSubtraction(a) && isSubtraction(b)) {
            if (arg1(a).equals(arg1(b))) {
                return makeSubtraction(arg2(b), arg2(a));
            } else if (arg1(a).equals(arg2(b))) {
                return makeSubtraction(makeProduct("2", arg1(a)), makeSum(arg2(a), arg1(b)));
            } else if (arg2(a).equals(arg1(b))) {
                return makeSubtraction(makeSum(arg1(a), arg2(b)), makeProduct("2", arg1(b)));
            } else if (arg2(a).equals(arg2(b))) {
                return makeSubtraction(arg1(a), arg1(b));
            }
        } else if (isSubtraction(a) && isProduct(b)) {
            if (isConstant(arg1(b))) {
                if (arg1(a).equals(arg2(b))) {
                    return makeSubtraction(makeProduct(makeSubtraction("1", arg1(b)), arg1(a)), arg2(a));
                } else if (arg2(a).equals(arg2(b))) {
                    return makeSubtraction(makeProduct(makeSubtraction("-1", arg1(b)), arg2(a)), arg1(a));
                }
            } else if (isConstant(arg2(b))) {
                if (arg1(a).equals(arg1(b))) {
                    return makeSubtraction(makeProduct(makeSubtraction("1", arg2(b)), arg1(a)), arg2(a));
                } else if (arg2(a).equals(arg1(b))) {
                    return makeSubtraction(makeProduct(makeSubtraction("-1", arg2(b)), arg2(a)), arg1(a));
                }
            }
        }

        return list("-", a, b);
    }

    /**
     * Differentiates a prefix expression in the form ( <i>operator arg1 arg2</i> )<br> in regards to the variable
     * vary.<br>
     *
     * @param exp  string expression with prefix notation to derive.
     * @param var string representing the variable.
     * @return derived prefix expression.
     */
    private String derive(String exp, String var) {
        if (isConstant(exp)) {
            return "0";
        } else if (isVariable(exp)) {
            return deriveVariable(exp, var);
        } else if (isSum(exp)) {
            return deriveSum(exp, var);
        } else if (isSubtraction(exp)) {
            return deriveSubtraction(exp, var);
        } else if (isProduct(exp)) {
            return deriveProduct(exp, var);
        } else if (isDivision(exp)) {
            return deriveDivision(exp, var);
        } else if (isSquareRoot(exp)) {
            return deriveSqrt(exp, var);
        } else if (isSine(exp)) {
            return deriveSin(exp, var);
        } else if (isCosine(exp)) {
            return deriveCos(exp, var);
        } else if (isTan(exp)) {
            return deriveTan(exp, var);
        } else if (isPower(exp)) {
            return derivePower(exp, var);
        } else if (isLn(exp)) {
            return deriveLn(exp, var);
        } else if (isE(exp)) {
            return deriveE(exp, var);
        } else if (isAtan(exp)) {
            return deriveAtan(exp, var);
        } else if (isAsin(exp)) {
            return deriveAsin(exp, var);
        } else if (isAcos(exp)) {
            return deriveAcos(exp, var);
        } else if (isSinHyp(exp)) {
            return deriveSinHyp(exp, var);
        } else if (isCosHyp(exp)) {
            return deriveCosHyp(exp, var);
        } else if (isTanHyp(exp)) {
            return deriveTanHyp(exp, var);
        } else if (isCotan(exp)) {
            return deriveCotan(exp, var);
        } else if (isAcotan(exp)) {
            return deriveAcotan(exp, var);
        } else {
            return "";
        }
    }

    /**
     * Derives the prefix expression ( acotan <i>arg1 arg2</i> ).
     *
     * @param exp  string expression with prefix notation to derive.
     * @param var string representing the variable.
     * @return derived prefix expression.
     */
    private String deriveAcotan(String exp, String var) {
        return (makeDivision(makeProduct("-1", derive(arg1(exp), var)),
                             makeSum("1", makePower(arg1(exp), "2"))));
    }

    /**
     * Derives the prefix expression ( cotan <i>arg1 arg2</i> ).
     *
     * @param exp  string expression with prefix notation to derive.
     * @param var string representing the variable.
     * @return derived prefix expression.
     */
    private String deriveCotan(String exp, String var) {
        return (makeProduct(derive(arg1(exp), var),
                            makeSubtraction("-1", makePower(makeCotan(arg1(exp)), "2"))));
    }

    /**
     * Derives the prefix expression ( tanh <i>arg1 arg2</i> ).
     *
     * @param exp  string expression with prefix notation to derive.
     * @param var string representing the variable.
     * @return derived prefix expression.
     */
    private String deriveTanHyp(String exp, String var) {
        return (makeProduct(derive(arg1(exp), var),
                            makeSubtraction("1", makePower(makeTanHyp(arg1(exp)), "2"))));
    }

    /**
     * Derives the prefix expression ( cosh <i>arg1 arg2</i> ).
     *
     * @param exp  string expression with prefix notation to derive.
     * @param var string representing the variable.
     * @return derived prefix expression.
     */
    private String deriveCosHyp(String exp, String var) {
        return (makeProduct(derive(arg1(exp), var), makeSinHyp(arg1(exp))));
    }

    /**
     * Derives the prefix expression ( sinh <i>arg1 arg2</i> ).
     *
     * @param exp  string expression with prefix notation to derive.
     * @param var string representing the variable.
     * @return derived prefix expression.
     */
    private String deriveSinHyp(String exp, String var) {
        return (makeProduct(derive(arg1(exp), var), makeCosHyp(arg1(exp))));
    }

    /**
     * Derives the prefix expression ( acos <i>arg1 arg2</i> ).
     *
     * @param exp  string expression with prefix notation to derive.
     * @param var string representing the variable.
     * @return derived prefix expression.
     */
    private String deriveAcos(String exp, String var) {
        return (makeProduct("-1", makeDivision(derive(arg1(exp), var),
                                               makeSquareRoot(makeSubtraction("1", makePower(arg1(exp), "2"))))));
    }

    /**
     * Derives the prefix expression ( asin <i>arg1 arg2</i> ).
     *
     * @param exp  string expression with prefix notation to derive.
     * @param var string representing the variable.
     * @return derived prefix expression.
     */
    private String deriveAsin(String exp, String var) {
        return (makeDivision(derive(arg1(exp), var),
                             makeSquareRoot(makeSubtraction("1", makePower(arg1(exp), "2")))));
    }

    /**
     * Derives the prefix expression ( atan <i>arg1 arg2</i> ).
     *
     * @param exp  string expression with prefix notation to derive.
     * @param var string representing the variable.
     * @return derived prefix expression.
     */
    private String deriveAtan(String exp, String var) {
        return (makeDivision(derive(arg1(exp), var),
                             makeSum("1", makePower(arg1(exp), "2"))));
    }

    /**
     * Derives the prefix expression ( exp <i>arg1 arg2</i> ).
     *
     * @param exp  string expression with prefix notation to derive.
     * @param var string representing the variable.
     * @return derived prefix expression.
     */
    private String deriveE(String exp, String var) {
        return (makeProduct(derive(arg1(exp), var), makeE(arg1(exp))));
    }

    /**
     * Derives the prefix expression ( ln <i>arg1 arg2</i> ).
     *
     * @param exp  string expression with prefix notation to derive.
     * @param var string representing the variable.
     * @return derived prefix expression.
     */
    private String deriveLn(String exp, String var) {
        return (makeProduct(derive(arg1(exp), var), makeDivision("1", arg1(exp))));
    }

    /**
     * Derives the prefix expression ( tan <i>arg1 arg2</i> ).
     *
     * @param exp  string expression with prefix notation to derive.
     * @param var string representing the variable.
     * @return derived prefix expression.
     */
    private String deriveTan(String exp, String var) {
        return (makeProduct(derive(arg1(exp), var),
                            makeSum("1", makePower(makeTan(arg1(exp)), "2"))));
    }

    /**
     * Derives the prefix expression ( sin <i>arg1 arg2</i> ).
     *
     * @param exp  string expression with prefix notation to derive.
     * @param var string representing the variable.
     * @return derived prefix expression.
     */
    private String deriveSin(String exp, String var) {
        return (makeProduct(derive(arg1(exp), var),
                            makeCosine(arg1(exp))));
    }

    /**
     * Derives the prefix expression ( cos <i>arg1 arg2</i> ).
     *
     * @param exp  string expression with prefix notation to derive.
     * @param var string representing the variable.
     * @return derived prefix expression.
     */
    private String deriveCos(String exp, String var) {
        return (makeProduct(derive(arg1(exp), var),
                            makeProduct("-1", makeSine(arg1(exp)))));
    }

    /**
     * Derives the prefix expression ( sqrt <i>arg1 arg2</i> ).
     *
     * @param exp  string expression with prefix notation to derive.
     * @param var string representing the variable.
     * @return derived prefix expression.
     */
    private String deriveSqrt(String exp, String var) {
        return (makeDivision(derive(arg1(exp), var),
                             makeProduct("2", makeSquareRoot(arg1(exp)))));
    }

    /**
     * Derives the prefix expression ( / <i>arg1 arg2</i> ).
     *
     * @param exp  string expression with prefix notation to derive.
     * @param var string representing the variable.
     * @return derived prefix expression.
     */
    private String deriveDivision(String exp, String var) {
        return (makeDivision(makeSubtraction(makeProduct(arg2(exp), derive(arg1(exp), var)),
                                             makeProduct(arg1(exp), derive(arg2(exp), var))), makePower(arg2(exp),
                                                                                                         "2")));
    }

    /**
     * Derives the prefix expression ( * <i>arg1 arg2</i> ).
     *
     * @param exp  string expression with prefix notation to derive.
     * @param var string representing the variable.
     * @return derived prefix expression.
     */
    private String deriveProduct(String exp, String var) {
        return (makeSum(makeProduct(arg1(exp), derive(arg2(exp), var)),
                        makeProduct(derive(arg1(exp), var), arg2(exp))));
    }

    /**
     * Derives the prefix expression ( - <i>arg1 arg2</i> ).
     *
     * @param exp  string expression with prefix notation to derive.
     * @param var string representing the variable.
     * @return derived prefix expression.
     */
    private String deriveSubtraction(String exp, String var) {
        return (makeSubtraction(derive(arg1(exp), var), derive(arg2(exp), var)));
    }

    /**
     * Derives the prefix expression ( + <i>arg1 arg2</i> ).
     *
     * @param exp  string expression with prefix notation to derive.
     * @param var string representing the variable.
     * @return derived prefix expression.
     */
    private String deriveSum(String exp, String var) {
        return (makeSum(derive(arg1(exp), var), derive(arg2(exp), var)));
    }

    /**
     * Derives the prefix expression ( ^ <i>arg1 arg2</i> ).
     *
     * @param exp  string expression with prefix notation to derive.
     * @param var string representing the variable.
     * @return derived prefix expression.
     */
    private String derivePower(String exp, String var) {
        if (!isConstant(arg1(exp))) {
            if (isConstant(arg2(exp))) {
                return (makeProduct(derive(arg1(exp), var), makeProduct(arg2(exp),
                                                                         makePower(arg1(exp), makeSubtraction(arg2(exp),
                                                                                                              "1")))));
            } else {
                return (makeProduct(exp, makeSum(makeProduct(derive(arg2(exp), var), makeLn(arg1(exp))),
                                                 makeProduct(derive(makeLn(arg1(exp)), var), arg2(exp)))));
            }
        } else {
            return (makeProduct(makeProduct(makeLn(arg1(exp)), derive(arg2(exp), var)), exp));
        }
    }

    /**
     * Derives a variable.
     *
     * @param exp  the variable to derive.
     * @param var string representing the current variable.
     * @return 1 or 0 depending on whether exp.equals(var) or not.
     */
    private String deriveVariable(String exp, String var) {
        if (isSameVariable(exp, var)) {
            return "1";
        } else {
            return "0";
        }
    }

    /**
     * Will force the expression exp through the constructors again. The effect is some additional simplification.
     *
     * @param exp prefix expression to simplify
     * @return the simplified expression
     */
    private String simplify(String exp) {
        if (isConstant(exp)) {
            if (isInteger(Double.parseDouble(exp))) {
                return String.valueOf(Double.valueOf(exp).intValue());
            } else {
                return exp;
            }
        } else if (isVariable(exp)) {
            return exp;
        } else if (isSum(exp)) {
            return makeSum(simplify(arg1(exp)), simplify(arg2(exp)));
        } else if (isSubtraction(exp)) {
            return makeSubtraction(simplify(arg1(exp)), simplify(arg2(exp)));
        } else if (isProduct(exp)) {
            return makeProduct(simplify(arg1(exp)), simplify(arg2(exp)));
        } else if (isDivision(exp)) {
            return makeDivision(simplify(arg1(exp)), simplify(arg2(exp)));
        } else if (isSquareRoot(exp)) {
            return makeSquareRoot(simplify(arg1(exp)));
        } else if (isSine(exp)) {
            return makeSine(simplify(arg1(exp)));
        } else if (isCosine(exp)) {
            return makeCosine(simplify(arg1(exp)));
        } else if (isTan(exp)) {
            return makeTan(simplify(arg1(exp)));
        } else if (isPower(exp)) {
            return makePower(simplify(arg1(exp)), simplify(arg2(exp)));
        } else if (isLn(exp)) {
            return makeLn(simplify(arg1(exp)));
        } else if (isE(exp)) {
            return makeE(simplify(arg1(exp)));
        } else if (isSinHyp(exp)) {
            return makeSinHyp(simplify(arg1(exp)));
        } else if (isCosHyp(exp)) {
            return makeCosHyp(simplify(arg1(exp)));
        } else if (isTanHyp(exp)) {
            return makeTanHyp(simplify(arg1(exp)));
        } else if (isCotan(exp)) {
            return makeCotan(simplify(arg1(exp)));
        } else if (isAcotan(exp)) {
            return makeAcotan(simplify(arg1(exp)));
        }

        return exp;
    }

    /**
     * Will call simplify repeatedly with its argument. When the returned string from simplify is unchanged it is
     * returned. The effect is some additional simplification.
     *
     * @param exp prefix expression to simplify
     * @return the simplified expression
     */
    private String simplifyAsMuchAsPossible(String exp) {
        String lastTime = "";
        String now = exp;

        while (true) {
            now = simplify(now);
            if (lastTime.equalsIgnoreCase(now)) {
                break;
            }
            lastTime = now;
        }

        return now;
    }

    /**
     * returns the operator in a prefix expression
     *
     * @param exp prefix expression.
     * @return the operator.
     */
    private String firstOp(String exp) {
        return car(exp);
    }

    /**
     * Converts a prefix expression to a infix.
     *
     * @param exp string expression to convert.
     * @return a infix expression.
     *         Ex: ( + 5 x ) => 5+x
     *         NOTE: Some obvious improvements in efficiency could be made if all string appending is changed to use a
     *         StringBuilder
     */
    private String preToInfix(String exp) {
        if (isVariable(exp) || isConstant(exp)) {
            return exp;
        }

        String fop = firstOp(exp);
        String fArg = arg1(exp);
        String sArg;

        if (!isTwoArgOp(fop)) {
            return fop + '(' + preToInfix(fArg) + ')';
        } else {
            sArg = arg2(exp);

            if (isConstant(fArg) || isVariable(fArg)) {
                if (isConstant(sArg) || isVariable(sArg)) {
                    return fArg + fop + sArg;
                } else {
                    if (fop.equalsIgnoreCase("+")) {
                        return fArg + fop + preToInfix(sArg);
                    } else if (fop.equalsIgnoreCase("-") && (isDivision(sArg) || isProduct(sArg))) {
                        return fArg + fop + preToInfix(sArg);
                    } else if (fop.equalsIgnoreCase("*") && (isPower(sArg) || isProduct(sArg) || !isTwoArgOp(firstOp(
                            sArg)))) {
                        return fArg + fop + preToInfix(sArg);
                    } else if (isTwoArgOp(firstOp(sArg))) {
                        return fArg + fop + '(' + preToInfix(sArg) +
                                ')';
                    } else {
                        return fArg + fop + preToInfix(sArg);
                    }
                }
            } else if (isConstant(sArg) || isVariable(sArg)) {
                if (fop.equalsIgnoreCase("+") || fop.equalsIgnoreCase("-")) {
                    return preToInfix(fArg) + fop + sArg;
                } else if (isTwoArgOp(firstOp(fArg))) {
                    return '(' + preToInfix(fArg) + ')' + fop + sArg;
                } else {
                    return preToInfix(fArg) + fop + sArg;
                }
            } else {

                if (fop.equalsIgnoreCase("+")) {
                    return preToInfix(fArg) + fop + preToInfix(sArg);
                } else if (fop.equalsIgnoreCase("-")) {
                    if (isProduct(sArg) || isDivision(sArg)) {
                        return preToInfix(fArg) + fop + preToInfix(sArg);
                    } else {
                        return preToInfix(fArg) + fop + '(' + preToInfix(
                                sArg) +
                                ')';
                    }
                } else if (isTwoArgOp(firstOp(fArg)) && isTwoArgOp(firstOp(sArg))) {
                    return '(' + preToInfix(fArg) + ')' + fop + '(' +
                            preToInfix(sArg) + ')';
                } else if (isTwoArgOp(firstOp(fArg)) && !isTwoArgOp(firstOp(sArg))) {
                    return '(' + preToInfix(fArg) + ')' + fop +
                            preToInfix(sArg);
                } else if (isTwoArgOp(firstOp(sArg)) && !isTwoArgOp(firstOp(fArg))) {
                    return preToInfix(fArg) + fop + '(' + preToInfix(sArg) +
                            ')';
                } else {
                    return preToInfix(fArg) + fop + preToInfix(sArg);
                }
            }
        }
    }

    /**
     * Converts a infix expression to a prefix.
     *
     * @param exp string expression to convert.
     * @return a prefix expression.
     *         Ex: 5*x^2+3*x => ( + ( * 5 ( ^ x 2 ) ) ( * 3 x ) )
     *         The method also calls storeVars() with all variables found so they will be stored. Use the method getVars()
     *         to retrieve the list of variables.
     * @throws MathException when the expression is invalid
     */
    private String inToPrefix(String exp) throws MathException {

        int ma;
        if (exp.length() == 0) {
            throw new MathException("Wrong number of arguments to operator");
        } else if (isVariable(exp)) {
            // store variable name for use with diff(String exp)
            storeVars(exp);
            return exp;
        } else if (isAllNumbers(exp)) {
            return exp;
        } else if (exp.charAt(0) == '(' && ((ma = match(exp, 0)) == (exp.length() - 1))) {
            return inToPrefix(exp.substring(1, ma));
        }

        int i = 0;
        String fop;
        String str = "";
        String fArg;
        String sArg;
        while (i < exp.length()) {
            if ((fop = getOp(exp, i)) != null) {
                if (isTwoArgOp(fop)) {
                    if (fop.equalsIgnoreCase("+") || fop.equalsIgnoreCase("-")) {
                        if ("".equals(str)) {
                            str = "0";
                        }

                        fArg = argToPlusOrMinus(exp, i + 1);
                    } else if (fop.equalsIgnoreCase("*") || fop.equalsIgnoreCase("/")) {
                        if ("".equals(str)) {
                            throw new MathException("Wrong number of arguments to operator");
                        }

                        fArg = argToAnyOpExcept(exp, i + 1, "^");
                    } else {
                        if ("".equals(str)) {
                            throw new MathException("Wrong number of arguments to operator");
                        }

                        fArg = arg(exp, i + fop.length());
                    }

                    str = "( " + fop + " " + str + " " + inToPrefix(fArg) + " )";
                } else {

                    fArg = arg(exp, i + fop.length());
                    str += "( " + fop + " " + inToPrefix(fArg) + " )";
                }
                i += fop.length() + fArg.length();
            } else {

                fArg = arg(exp, i);
                fop = getOp(exp, i + fArg.length());

                if (fop == null) {
                    throw new MathException("Missing operator");
                }

                if (isTwoArgOp(fop)) {

                    if (fop.equalsIgnoreCase("+") || fop.equalsIgnoreCase("-")) {
                        sArg = argToPlusOrMinus(exp, i + 1 + fArg.length());
                    } else if (fop.equalsIgnoreCase("*") || fop.equalsIgnoreCase("/")) {
                        sArg = argToAnyOpExcept(exp, i + 1 + fArg.length(), "^");
                    } else {
                        sArg = arg(exp, i + fop.length() + fArg.length());
                    }

                    str += "( " + fop + " " + inToPrefix(fArg) + " " + inToPrefix(sArg) + " )";
                    i += fArg.length() + sArg.length() + fop.length();
                } else {
                    str += "( " + fop + " " + inToPrefix(fArg) + " )";
                    i += fop.length() + fArg.length();
                }
            }
        }

        return str;
    }

    /**
     * Returns an argument from the infix expression exp,<br> starting at index and ending at the beginning of<br> next
     * operator or at the end of exp. If '(' appears in the argument, the matching ')' will<br> be searched for.
     *
     * @param exp   infix expression.
     * @param index the index to start from
     * @return the argument.
     *         Ex:<br> If exp is the expression "2+3*cos(x+2)"<br> arg( exp , 0 ) => "2"<br> arg( exp , 4 ) => "cos(x+2)"
     */
    private String arg(String exp, int index) {
        String op;
        StringBuilder str = new StringBuilder();

        int i = index;
        int ma;

        while (i < exp.length()) {
            if (exp.charAt(i) == '(') {
                ma = match(exp, i);
                str.append(exp, i, ma + 1);
                i = ma + 1;
            } else if ((op = getOp(exp, i)) != null) {
                if (str.length() != 0 && !isTwoArgOp(backTrack(str.toString()))) {
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
     * Returns an argument from the infix expression exp,<br> starting at index and ending at the beginning of<br> next
     * operator, if that operator is not equal to except,<br> or ending at the end of exp.<br> If '(' appears in the
     * argument, the matching ')' will<br> be searched for.
     *
     * @param exp    infix expression.
     * @param index  the index to start at
     * @param except operator to exclude
     * @return the argument. Ex:<br> If exp is the expression "3*x^2+5"<br> argToAnyOpExcept( exp , 2 ) => "x^2"<br>
     */
    private String argToAnyOpExcept(String exp, int index, String except) {

        StringBuilder str = new StringBuilder();

        int i = index;
        int ma;
        String op;

        while (i < exp.length()) {
            if (exp.charAt(i) == '(') {
                ma = match(exp, i);
                str.append(exp, i, ma + 1);
                i = ma + 1;
            } else if ((op = getOp(exp, i)) != null) {
                if (str.length() != 0 && !isTwoArgOp(backTrack(str.toString())) && !op.equalsIgnoreCase(except)) {
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
     * Returns an argument from the infix expression exp,<br> starting at index and ending at the beginning of<br> any of
     * the operators '+' or '-' or at the end of exp.<br> If '(' appears in the argument, the matching ')' will<br> be
     * searched for.
     *
     * @param exp infix expression.
     * @param index the index to start at
     * @return the argument. Ex:<br> If exp is the expression "2+3*cos(x+2)"<br> argToPlusOrMinus( exp , 2 ) =>
     *         "3*cos(x+2)"<br> argToPlusOrMinus( exp , 0 ) => "2"
     */
    private String argToPlusOrMinus(String exp, int index) {
        int ma;
        int i = index;
        String str = "";

        while (i < exp.length()) {
            if (exp.charAt(i) == '(') {
                ma = match(exp, i);
                str += exp.substring(i, ma + 1);
                i = ma;
            } else if ((exp.charAt(i) == '+' || exp.charAt(i) == '-') && !"".equals(str)) {

                // backtracking. The end of str must not be a two arg op, case -1*-1

                if (isTwoArgOp(backTrack(str))) {
                    str += exp.charAt(i);
                } else {
                    return str;
                }
            } else {
                str += exp.charAt(i);
            }
            i++;
        }

        return str;
    }

    /**
     * Parses out all spaces in str.
     *
     * @param str the expression
     * @return the expression without the spaces
     */
    private String skipSpaces(String str) {
        StringBuilder newStr = new StringBuilder(100);
        int len = str.length();

        for (int i = 0; i < len; i++) {
            if (str.charAt(i) != ' ') {
                newStr.append(str.charAt(i));
            }
        }

        return newStr.toString();
    }

    /**
     * Parses out all ++ +- -+ --
     * An "easy" no good way to simplify expressions like x+-1*cos(x) This should instead be made inside all constructors.
     *
     * @param str the expression
     * @return the expression where the plus/minus signs are simplified
     */
    private String parseSigns(String str) {
        StringBuilder newStr = new StringBuilder(100);
        int i = 0;

        while (i < str.length()) {
            try {
                if (str.charAt(i) == '+' && str.charAt(i + 1) == '+') {
                    newStr.append('+');
                    i++;
                } else if (str.charAt(i) == '+' && str.charAt(i + 1) == '-') {
                    newStr.append('-');
                    i++;
                } else if (str.charAt(i) == '-' && str.charAt(i + 1) == '+') {
                    newStr.append('-');
                    i++;
                } else if (str.charAt(i) == '-' && str.charAt(i + 1) == '-') {
                    newStr.append('+');
                    i++;
                } else {
                    newStr.append(str.charAt(i));
                }
            } catch (Exception e) {
                // Ignore
            }

            i++;
        }
        return newStr.toString();
    }

    /**
     * matches brackets in exp.
     *
     * @param exp string expression to check.
     * @param index the index to start at
     * @return index of matching ")".
     */
    private int match(String exp, int index) {
        int i = index;
        int count = 0;

        while (i < exp.length()) {

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

    private int lastIndex = 0;

    /**
     * Extracts variables from a string, x;y;z
     *
     * @param str the string containing variables separated with ";"
     * @return each call returns the next variable from the string or null.
     * NOTE: and obvious improvement would be to use a StringTokenizer here.
     */
    private String findVariable(String str) {
        int thisIndex;
        String var;

        if (lastIndex >= str.length()) {
            lastIndex = 0;
            return null;
        }

        thisIndex = str.indexOf(";", lastIndex);

        if (thisIndex == -1) {
            var = str.substring(lastIndex);
            lastIndex = str.length();
            return (var);
        }

        var = str.substring(lastIndex, thisIndex);

        lastIndex = thisIndex + 1;

        return var;
    }

    /**
     * Doubles the array arr and copies everything in arr inTo the new array.
     *
     * @param arr array to double and copy.
     * @return a new string array, twice as big as arr and with the same elements.
     *         This method should not be called in normal cases.
     */
    private String[] doubleAndCopyArray(String[] arr) {
        int len = arr.length;

        String[] new_arr = new String[len * 2];

        System.arraycopy(arr, 0, new_arr, 0, len);

        return new_arr;
    }

    /**
     * Used to store a list of variables.
     *
     * @param var variable to store
     */
    private void storeVars(String var) {
        if (!variables.contains(var)) {
            variables.add(var);
        }
    }

    /**
     * Gets stored variables as a semi-colon delimited string.<br> Example:<br> <xmp> Derive d = new Derive(); d.diff(
     * "x+y+z" ); String vars = d.getVariables(); </xmp>
     * The variable vars will contain the string "x;y;z" , note that the variables appear<br> in the list in the same order
     * as they were found in the expression.
     *
     * @return semi-colon delimited string of variables found in the expression<br> after a call to any of the methods diff
     *         or an empty string if no call has yet been made.
     */
    private synchronized String getVariables() {
        StringBuilder tmp = new StringBuilder(50);
        boolean first = true;

        for (String variable : variables) {
            if (first) {
                tmp.append(variable);
            } else {
                tmp.append(';').append(variable);
            }
            first = false;
        }

        return tmp.toString();
    }

    /**
     * Clears the variable stored vars.
     */
    private void clearVars() {
        variables.clear();
    }

    /**
     * This method takes a mathematical expression with infix notation and performs symbolic differentiation in regards
     * to<br> the variables listed in <i>variables</i>.
     *
     * @param exp  string expression with infix notation to derive.
     * @param vars string representing the variables.
     * @return string array with the derivatives of exp.
     * @throws SyntaxException if the expression has invalid syntax
     */
    private synchronized String[] diff(String exp, String vars) throws SyntaxException {
        String tmpStr;
        String variable;
        String answer;
        String expression;
        String prefixExp;
        String derivePrefixExp;
        String storedVars;
        String[] ans = new String[100];
        int count = 0;

        if (exp == null || exp.equals("")) {
            throw new SyntaxException("Arguments null or empty string");
        }

        // clear all previously stored variables
        clearVars();

        expression = skipSpaces(exp);
        expression = expression.toLowerCase();
        expression = putMultiplicationOperator(parseE(expression));

        try {
            syntax(expression);

            prefixExp = inToPrefix(expression);
            //  System.out.println(prefixExp);
            prefixExp = simplifyAsMuchAsPossible(prefixExp);
            //  System.out.println(prefixExp);
            if (vars == null || vars.equals("")) {
                storedVars = getVariables();
                /*
                 * Default variable, used if no variables are given and no variables can be found in the expression.
                 * ( i.e the argument is a constant )
                 */
                String defaultVar = "x";
                tmpStr = (storedVars.equals("") ? defaultVar : storedVars);
                tmpStr = skipSpaces(tmpStr.toLowerCase());
            } else {
                tmpStr = skipSpaces(vars.toLowerCase());
            }

            while ((variable = findVariable(tmpStr)) != null) {
                syntax(variable);

                if (!isVariable(variable)) {
                    throw new java.lang.Exception("Not a valid variable " + variable);
                }

                derivePrefixExp = derive(prefixExp, variable);
                //System.out.println(derivePrefixExp);
                derivePrefixExp = simplifyAsMuchAsPossible(derivePrefixExp);
                // System.out.println(derivePrefixExp);
                answer = preToInfix(derivePrefixExp);
                //System.out.println(answer);
                if (count > (ans.length - 1)) {
                    ans = doubleAndCopyArray(ans);
                }

                ans[count] = parseSigns(answer);
                count++;
            }

            return ans;
        } catch (StringIndexOutOfBoundsException f) {
            throw new SyntaxException("Wrong number of arguments to operator");
        } catch (Exception f) {
            throw new SyntaxException(f.getMessage());
        }
    }

    /**
     * Takes an mathematical expression with infix notation and performs symbolic differentiation in regards to all<br> the
     * variables found in the expression.
     *
     * @param exp string expression with infix notation to derive.
     * @return string array with the derivatives of exp.
     * @throws SyntaxException if the expression has invalid syntax
     */
    public synchronized String[] diff(String exp) throws SyntaxException {
        return diff(exp, "");
    }
}
