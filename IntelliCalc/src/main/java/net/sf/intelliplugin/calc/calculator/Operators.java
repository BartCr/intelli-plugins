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

package net.sf.intelliplugin.calc.calculator;

import net.sf.intelliplugin.calc.calculator.operator.*;

public class Operators {
    /**
     * Addition operator.
     */
    public static final Operator ADD = new Add();
    /**
     * Subtraction operator.
     */
    public static final Operator SUBTRACT = new Subtract();
    /**
     * Multiplication operator.
     */
    public static final Operator MULTIPLY = new Multiply();
    /**
     * Division operator.
     */
    public static final Operator DIVIDE = new Divide();
    /**
     * Power operator.
     */
    public static final Operator POWER = new Power();
    /**
     * Reciprocal (1/x) operator.
     */
    public static final Operator RECIPROCAL = new Reciprocal();
    /**
     * Square root operator.
     */
    public static final Operator SQUARE_ROOT = new SquareRoot();
    /**
     * Square operator.
     */
    public static final Operator SQUARE = new Square();
    /**
     * Factorial operator.
     */
    public static final Operator FACTORIAL = new Factorial();
    /**
     * Natural logarithm (to base <i>e</i>) operator.
     */
    public static final Operator NATURAL_LOGARITHM = new NaturalLogarithm();
    /**
     * Logarithm (to base 10) operator.
     */
    public static final Operator LOGARITHM_10 = new Logarithm10();
    /**
     * Sine operator.
     */
    public static final Operator SINE = new Sine();
    /**
     * Cosine operator.
     */
    public static final Operator COSINE = new Cosine();
    /**
     * Tangent operator.
     */
    public static final Operator TANGENT = new Tangent();
    /**
     * Cotangent operator.
     */
    public static final Operator COTANGENT = new Cotangent();
    /**
     * Inverse sine operator.
     */
    public static final Operator INVERSE_SINE = new InverseSine();
    /**
     * Inverse cosine operator.
     */
    public static final Operator INVERSE_COSINE = new InverseCosine();
    /**
     * Inverse tangent operator.
     */
    public static final Operator INVERSE_TANGENT = new InverseTangent();
    /**
     * Inverse cotangent operator.
     */
    public static final Operator INVERSE_COTANGENT = new InverseCotangent();
    /**
     * Hyperbolic sine operator.
     */
    public static final Operator HYPERBOLIC_SINE = new HyperbolicSine();
    /**
     * Hyperbolic cosine operator.
     */
    public static final Operator HYPERBOLIC_COSINE = new HyperbolicCosine();
    /**
     * Hyperbolic tangent operator.
     */
    public static final Operator HYPERBOLIC_TANGENT = new HyperbolicTangent();
    /**
     * Hyperbolic cotangent operator.
     */
    public static final Operator HYPERBOLIC_COTANGENT = new HyperbolicCotangent();
}
