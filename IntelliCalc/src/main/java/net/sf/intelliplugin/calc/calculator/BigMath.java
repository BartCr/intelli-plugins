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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Math helper class for performing math functions on {@link java.math.BigDecimal}.
 *
 * @author Bart Cremers
 * @since 2.0
 */
public class BigMath {

    /**
     * Eulers number
     */
    public static final BigDecimal EULER = BigDecimal.valueOf(Math.E);

    /**
     * Pi
     */
    public static final BigDecimal PI = BigDecimal.valueOf(Math.PI);

    /**
     * Not-a-Number
     */
    public static final BigDecimal NaN = null; // TODO: BigDecimal.valueOf(Double.NaN);

    /**
     * Positive infinity
     */
    public static final BigDecimal POSITIVE_INFINITY = null; // TODO:BigDecimal.valueOf(Double.POSITIVE_INFINITY);

    /**
     * Value representing boolean <code>false</code>.
     */
    public static final BigDecimal FALSE = BigDecimal.ZERO;

    /**
     * Value representing boolean <code>true</code>.
     */
    public static final BigDecimal TRUE = BigDecimal.ONE;

    /**
     * The value -2 with a scale of 0.
     */
    public static final BigDecimal MINUS_TWO = BigDecimal.valueOf(-2);

    /**
     * The value -1 with a scale of 0.
     */
    public static final BigDecimal MINUS_ONE = BigDecimal.valueOf(-1);

    /**
     * The value 0 with a scale of 0.
     */
    public static final BigDecimal ZERO = BigDecimal.ZERO;

    /**
     * The value 0.5 with a scale of 1.
     */
    public static final BigDecimal ONE_HALF = BigDecimal.valueOf(0.5);

    /**
     * The value 1 with a scale of 0.
     */
    public static final BigDecimal ONE = BigDecimal.ONE;

    /**
     * The value 2 with a scale of 0.
     */
    public static final BigDecimal TWO = BigDecimal.valueOf(2);

    /**
     * The value 8 with a scale of 0.
     */
    public static final BigDecimal EIGHT = BigDecimal.valueOf(8);

    /**
     * The value 10 with a scale of 0.
     */
    public static final BigDecimal TEN = BigDecimal.TEN;

    /**
     * The value 16 with a scale of 0.
     */
    public static final BigDecimal SIXTEEN = BigDecimal.valueOf(16);

    /**
     * The value 100 with a scale of 0.
     */
    public static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    /**
     * The value 180 with a scale of 0.
     */
    public static final BigDecimal ONE_EIGHTY = BigDecimal.valueOf(180);

    /**
     * The value 200 with a scale of 0.
     */
    public static final BigDecimal TWO_HUNDRED = BigDecimal.valueOf(200);

    private static final int MAX_ITERATIONS = 50;
    private static final int SCALE = 50;
    private static final MathContext MC = new MathContext(SCALE, RoundingMode.HALF_EVEN);

    /**
     * Convert a double to a BigDecimal
     *
     * @param value the double to convert
     * @return a BigDecimal with the double value.
     */
    private static BigDecimal toBigDecimal(double value) {
        return BigDecimal.valueOf(value);
    }

    /**
     * Returns Euler's number <i>e</i> raised to the power of a <code>double</code> value.
     *
     * @param a the exponent to raise <i>e</i> to
     * @return the value <i>e</i><sup><code>a</code></sup>, where <i>e</i> is the base of the natural logarithms.
     */
    public static BigDecimal exp(BigDecimal a) {
        return toBigDecimal(Math.exp(a.doubleValue()));
    }

    /**
     * Returns the value of the first argument raised to the power of the second argument.
     *
     * @param a the base
     * @param b the exponent
     * @return the value a<sup>b</sup>
     */
    public static BigDecimal pow(BigDecimal a, BigDecimal b) {
        return toBigDecimal(Math.pow(a.doubleValue(), b.doubleValue()));
    }

    /**
     * @param a the number
     * @return the log to base 10
     */
    public static BigDecimal log10(BigDecimal a) {
        return log(a).divide(log(TEN), MC);
    }

    /**
     * @param a the number
     * @return the base 10 antilog
     */
    public static BigDecimal antilog10(BigDecimal a) {
        return pow(TEN, a);
    }

    /**
     * @param a the number
     * @return the log to base e
     */
    public static BigDecimal log(BigDecimal a) {
        return toBigDecimal(Math.log(a.doubleValue()));
    }

    /**
     * @param a the number
     * @return the base e antilog
     */
    public static BigDecimal antilog(BigDecimal a) {
        return exp(a);
    }

    /**
     * @param a the number
     * @return the log to base 2
     */
    public static BigDecimal log2(BigDecimal a) {
        return log(a).divide(TWO, MC);
    }

    /**
     * @param a the number
     * @return the base 2 antilog
     */
    public static BigDecimal antilog2(BigDecimal a) {
        return pow(TWO, a);
    }

    /**
     * @param a the number
     * @param b the base
     * @return the log to base b.
     */
    public static BigDecimal log10(BigDecimal a, BigDecimal b) {
        return log(a).divide(log(b), MC);
    }

    /**
     * @param a the number
     * @return the square
     */
    public static BigDecimal square(BigDecimal a) {
        return a.multiply(a, MC);
    }

    /**
     * Calculates the factorial of a. Note that rounding may make this an approximation.
     *
     * @param a the number
     * @return the factorial of a
     * @throws IllegalArgumentException if a is not a positive integer value
     */
    public static BigDecimal factorial(BigDecimal a) {
        if (isNegative(a) || !isInteger(a)) {
            throw new IllegalArgumentException("a must be a positive integer");
        }
        BigDecimal f = BigDecimal.ONE;

        int nn = a.intValue();
        for (int i = 1; i <= nn; i++) {
            f = f.multiply(BigDecimal.valueOf(i), MC);
        }
        return f;
    }

    /**
     * Calculates the log to base e of the factorial of a. Note that rounding may make this an approximation.
     *
     * @param a the number
     * @return the log to base e of the factorial of a
     * @throws IllegalArgumentException if a is not a positive integer value
     */
    public static BigDecimal logFactorial(BigDecimal a) {
        if (isNegative(a) || !isInteger(a)) {
            throw new IllegalArgumentException("a must be a positive integer");
        }
        BigDecimal f = BigDecimal.ZERO;
        int nn = a.intValue();
        for (int i = 2; i <= nn; i++) {
            f = f.add(log(BigDecimal.valueOf(i)), MC);
        }
        return f;
    }

    /**
     * Calculates the sine of the given angle (in radians).
     *
     * @param a the number
     * @return the sine
     */
    public static BigDecimal sin(BigDecimal a) {
        return toBigDecimal(Math.sin(a.doubleValue()));
    }

    /**
     * @param a the number
     * @return the inverse sine
     * @throws IllegalArgumentException if a &lt; -1 or a &gt; 1
     */
    public static BigDecimal asin(BigDecimal a) {
        if (a.compareTo(MINUS_ONE) < 0 || a.compareTo(ONE) > 0) {
            throw new IllegalArgumentException("asin argument (" + a + ") must be >= -1.0 and <= 1.0");
        }
        return toBigDecimal(Math.asin(a.doubleValue()));
    }

    /**
     * Calculates the cosine of the given angle (in radians).
     *
     * @param a the number
     * @return the cosine
     */
    public static BigDecimal cos(BigDecimal a) {
        return toBigDecimal(Math.cos(a.doubleValue()));
    }

    /**
     * @param a the number
     * @return the inverse cosine
     * @throws IllegalArgumentException if a &lt; -1 or a &gt; 1
     */
    public static BigDecimal acos(BigDecimal a) {
        if (a.compareTo(MINUS_ONE) < 0 || a.compareTo(ONE) > 0) {
            throw new IllegalArgumentException("acos argument (" + a + ") must be >= -1.0 and <= 1.0");
        }
        return toBigDecimal(Math.acos(a.doubleValue()));
    }

    /**
     * Calculates the tangent of the given angle (in radians)
     *
     * @param a the number
     * @return the tangent
     */
    public static BigDecimal tan(BigDecimal a) {
        return toBigDecimal(Math.tan(a.doubleValue()));
    }

    /**
     * @param a the number
     * @return the inverse tangent
     */
    public static BigDecimal atan(BigDecimal a) {
        return toBigDecimal(Math.atan(a.doubleValue()));
    }

    /**
     * @param a the numerator
     * @param b the denominator
     * @return the inverse tangent
     */
    public static BigDecimal atan2(BigDecimal a, BigDecimal b) {
        return toBigDecimal(Math.atan2(a.doubleValue(), b.doubleValue()));
    }

    /**
     * @param a the number
     * @return the cotangent
     */
    public static BigDecimal cotan(BigDecimal a) {
        return ONE.divide(tan(a), MC);
    }

    /**
     * @param a the number
     * @return the inverse cotangent
     */
    public static BigDecimal acotan(BigDecimal a) {
        return toBigDecimal(Math.atan(ONE.divide(a, MC).doubleValue()));
    }

    /**
     * @param a the numerator
     * @param b the denominator
     * @return the inverse cotangent
     */
    public static BigDecimal acot2(BigDecimal a, BigDecimal b) {
        return toBigDecimal(Math.atan2(b.doubleValue(), a.doubleValue()));
    }

    /**
     * @param a the number
     * @return the secant
     */
    public static BigDecimal sec(BigDecimal a) {
        return ONE.divide(cos(a), MC);
    }

    /**
     * @param a the number
     * @return the inverse secant
     * @throws IllegalArgumentException if a &lt; -1 or a &gt; 1
     */
    public static BigDecimal asec(BigDecimal a) {
        if (a.compareTo(MINUS_ONE) < 0 || a.compareTo(ONE) > 0) {
            throw new IllegalArgumentException("asec argument (" + a + ") must be >= -1.0 and <= 1.0");
        }
        return toBigDecimal(Math.acos(ONE.divide(a, MC).doubleValue()));
    }

    /**
     * @param a the number
     * @return the cosecant
     */
    public static BigDecimal csc(BigDecimal a) {
        return ONE.divide(sin(a), MC);
    }

    /**
     * @param a the number
     * @return the inverse cosecant
     * @throws IllegalArgumentException if a &lt; -1 or a &gt; 1
     */
    public static BigDecimal acsc(BigDecimal a) {
        if (a.compareTo(MINUS_ONE) < 0 || a.compareTo(ONE) > 0) {
            throw new IllegalArgumentException("acsc argument (" + a + ") must be >= -1.0 and <= 1.0");
        }
        return asin(ONE.divide(a, MC));
    }

    /**
     * @param a the number
     * @return the exsecant
     */
    public static BigDecimal exsec(BigDecimal a) {
        return ONE.divide(cos(a), MC).subtract(ONE, MC);
    }

    /**
     * @param a the number
     * @return the inverse exsecant
     * @throws IllegalArgumentException if a &lt; -2 or a &gt; 0
     */
    public static BigDecimal aexsec(BigDecimal a) {
        if (a.compareTo(MINUS_TWO) < 0 || a.compareTo(ZERO) > 0) {
            throw new IllegalArgumentException("aexsec argument (" + a + ") must be >= -2 and <= 0");
        }
        return asin(ONE.divide(ONE.add(a, MC), MC));
    }

    /**
     * @param a the number
     * @return the versine
     */
    public static BigDecimal vers(BigDecimal a) {
        return ONE.subtract(cos(a), MC);
    }

    /**
     * @param a the number
     * @return the inverse versine
     * @throws IllegalArgumentException if a &lt; 0 or a &gt; 2
     */
    public static BigDecimal avers(BigDecimal a) {
        if (a.compareTo(ZERO) < 0 || a.compareTo(TWO) > 0) {
            throw new IllegalArgumentException("avers argument (" + a + ") must be <= 2 and >= 0");
        }
        return acos(ONE.subtract(a, MC));
    }

    /**
     * @param a the number
     * @return the coversine
     */
    public static BigDecimal covers(BigDecimal a) {
        return ONE.subtract(sin(a), MC);
    }

    /**
     * @param a the number
     * @return the inverse coversine
     * @throws IllegalArgumentException if a &lt; 0 or a &gt; 2
     */
    public static BigDecimal acovers(BigDecimal a) {
        if (a.compareTo(ZERO) < 0 || a.compareTo(TWO) > 0) {
            throw new IllegalArgumentException("acovers argument (" + a + ") must be <= 2 and >= 0");
        }
        return asin(ONE.subtract(a, MC));
    }

    /**
     * @param a the number
     * @return the haversine
     */
    public static BigDecimal hav(BigDecimal a) {
        return ONE_HALF.multiply(vers(a), MC);
    }

    /**
     * @param a the number
     * @return the inverse haversine
     * @throws IllegalArgumentException if a &lt; 0 or a &gt; 1
     */
    public static BigDecimal ahav(BigDecimal a) {
        if (a.compareTo(ZERO) < 0 || a.compareTo(ONE) > 0) {
            throw new IllegalArgumentException("ahav argument (" + a + ") must be >= 0 and <= 1");
        }
        return ONE_HALF.multiply(vers(a), MC);
    }

    /**
     * @param a the number
     * @return the sinus cardinalis
     */
    public static BigDecimal sinc(BigDecimal a) {
        return sin(a).divide(a, MC);
    }

    /**
     * @param a the number
     * @return the hyperbolic sine
     */
    public static BigDecimal sinh(BigDecimal a) {
        return ONE_HALF.multiply(exp(a), MC).subtract(exp(a.negate()), MC);
    }

    /**
     * @param a the number
     * @return the inverse hyperbolic sine
     */
    public static BigDecimal asinh(BigDecimal a) {
        BigDecimal sgn = ONE;

        if (a.compareTo(ZERO) < 0) {
            sgn = MINUS_ONE;
            a = a.negate();
        }

        return sgn.multiply(log(a.add(sqrt(a.multiply(a, MC).add(ONE, MC)), MC)), MC);
    }

    /**
     * @param a the number
     * @return the hyperbolic cosine
     */
    public static BigDecimal cosh(BigDecimal a) {
        return ONE_HALF.multiply(exp(a).add(exp(a.negate()), MC), MC);
    }

    /**
     * @param a the number
     * @return the inverse hyperbolic cosine
     * @throws IllegalArgumentException if a &lt; 1
     */
    public static BigDecimal acosh(BigDecimal a) {
        if (a.compareTo(ONE) < 0) {
            throw new IllegalArgumentException("acosh real number argument (" + a + ") must be >= 1");
        }
        return log(a.add(sqrt(a.multiply(a, MC).subtract(ONE, MC)), MC));
    }

    /**
     * @param a the number
     * @return the hyperbolic tangent
     */
    public static BigDecimal tanh(BigDecimal a) {
        return sinh(a).divide(cosh(a), MC);
    }

    /**
     * @param a the number
     * @return the inverse hyperbolic tangent
     * @throws IllegalArgumentException if a &lt; -1 or a &gt; 1
     */
    public static BigDecimal atanh(BigDecimal a) {
        BigDecimal sgn = ONE;

        if (a.compareTo(ZERO) < 0) {
            sgn = MINUS_ONE;
            a = a.negate();
        }

        if (a.compareTo(ONE) > 0) {
            throw new IllegalArgumentException("atanh real number argument (" + a.multiply(sgn)
                                               + ") must be >= -1 and <= 1");
        }

        return ONE_HALF.multiply(sgn, MC).multiply(log(ONE.add(a)), MC).subtract(log(ONE.subtract(a, MC)), MC);
    }

    /**
     * @param a the number
     * @return the hyperbolic cotangent
     */
    public static BigDecimal cotanh(BigDecimal a) {
        return ONE.divide(tanh(a), MC);
    }

    /**
     * @param a the number
     * @return the inverse hyperbolic cotangent
     * @throws IllegalArgumentException if a &lt; -1 or a &gt; 1
     */
    public static BigDecimal acoth(BigDecimal a) {
        BigDecimal sgn = ONE;

        if (a.compareTo(ZERO) < 0) {
            sgn = MINUS_ONE;
            a = a.negate();
        }

        if (a.compareTo(ONE) > 0) {
            throw new IllegalArgumentException("acoth real number argument (" + a.multiply(sgn)
                                               + ") must be >= -1 and <= 1");
        }

        return ONE_HALF.multiply(sgn, MC).multiply(log(ONE.add(a, MC)), MC).subtract(log(a.subtract(ONE, MC)), MC);
    }

    /**
     * @param a the number
     * @return the hyperbolic secant
     */
    public static BigDecimal sech(BigDecimal a) {
        return ONE.divide(cosh(a), MC);
    }

    /**
     * @param a the number
     * @return the inverse hyperbolic secant
     * @throws IllegalArgumentException if a &lt; 0 or a &gt; 1
     */
    public static BigDecimal asech(BigDecimal a) {
        if (a.compareTo(ONE) > 0 || a.compareTo(ZERO) < 0) {
            throw new IllegalArgumentException("asech real number argument (" + a + ") must be >= 0 and <= 1");
        }
        return ONE_HALF.multiply(log(ONE.divide(a, MC).add(sqrt(ONE.divide(square(a).subtract(ONE, MC), MC)), MC)), MC);
    }

    /**
     * @param a the number
     * @return the hyperbolic cosecant
     */
    public static BigDecimal csch(BigDecimal a) {
        return ONE.divide(sinh(a), MC);
    }

    /**
     * @param a the number
     * @return the inverse hyperbolic cosecant
     */
    public static BigDecimal acsch(BigDecimal a) {
        BigDecimal sgn = ONE;

        if (a.compareTo(ZERO) < 0) {
            sgn = MINUS_ONE;
            a = a.negate();
        }
        return ONE_HALF.multiply(sgn, MC).multiply(log(ONE.divide(a, MC).add(sqrt(ONE.divide(square(a).add(ONE, MC), MC)), MC)), MC);
    }

    /**
     * @param a the number
     * @return an initial approximation for square root calculation
     */
    private static BigDecimal getInitialApproximation(BigDecimal a) {
        BigInteger integerPart = a.toBigInteger();
        int length = integerPart.toString().length();
        if ((length % 2) == 0) {
            length--;
        }
        length /= 2;
        return ONE.movePointRight(length);
    }

    /**
     * @param a the number
     * @return the square root
     */
    public static BigDecimal sqrt(BigInteger a) {
        return sqrt(new BigDecimal(a));
    }

    /**
     * @param a the number
     * @return the square root
     */
    public static BigDecimal sqrt(BigDecimal a) {

        // Make sure a is a positive number
        if (a.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal initialGuess = getInitialApproximation(a);
        BigDecimal lastGuess;
        BigDecimal guess = new BigDecimal(initialGuess.toString());

        // Iterate
        int iterations = 0;
        boolean more = true;

        BigDecimal error;
        while (more) {
            lastGuess = guess;
            guess = a.divide(guess, SCALE, RoundingMode.HALF_UP);
            guess = guess.add(lastGuess);
            guess = guess.divide(TWO, SCALE, RoundingMode.HALF_UP);
            error = a.subtract(guess.multiply(guess));
            if (++iterations >= MAX_ITERATIONS) {
                more = false;
            } else if (lastGuess.equals(guess)) {
                more = error.abs().compareTo(ONE) >= 0;
            }
        }
        return guess;
    }

    /**
     * Calculates the faculty of the value a.
     *
     * @param a the value to calculate the faculty for
     * @return the faculty of a
     */
    public static BigDecimal fac(BigDecimal a) {

        if (!isInteger(a)) {
            return NaN;
        } else if (a.compareTo(ZERO) < 0) {
            return NaN;
        } else if (a.compareTo(ONE) <= 1) {
            return ONE;
        }

        return a.multiply(fac(a.subtract(ONE, MC)), MC);
    }

    /**
     * Calculates the semi faculty of the value a.
     *
     * @param a the value to calculate the semi faculty for
     * @return the semi faculty of a
     */
    public static BigDecimal sfac(BigDecimal a) {

        if (!isInteger(a)) {
            return NaN;
        } else if (a.compareTo(ZERO) < 0) {
            return NaN;
        } else if (a.compareTo(ONE) <= 1) {
            return ONE;
        }

        return a.multiply(sfac(a.subtract(TWO, MC)), MC);
    }

    /**
     * Calculates the integer part of the value a.
     *
     * @param a the value to calculate the fpart for
     * @return the fpart of a
     */
    public static BigDecimal fpart(BigDecimal a) {
        if (a.compareTo(ZERO) >= 0) {
            return a.subtract(BigDecimal.valueOf(StrictMath.floor(a.doubleValue())), MC);
        } else {
            return a.subtract(BigDecimal.valueOf(StrictMath.ceil(a.doubleValue())), MC);
        }
    }

    /**
     * Check to see if the BigDecimal can be considered a positive a (>= 0).
     *
     * @param a the {@link java.math.BigDecimal} a to check
     * @return <code>true</code> if the a is positive, <code>false</code> otherwise.
     */
    private static boolean isNegative(BigDecimal a) {
        return a.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * Checks to see if the BigDecimal can be considered to be a mathematical integer.
     *
     * @param a the {@link java.math.BigDecimal} a to check
     * @return <code>true</code> if the a is an integer, <code>false</code> otherwise.
     */
    private static boolean isInteger(BigDecimal a) {
        return a.scale() == 0;
    }

    /**
     * Checks to see if the BigDecimal is an even number.
     *
     * @param a the number
     * @return <code>true if a is an even number, <code>false if a is an odd number
     * @throws IllegalArgumentException if a isn't an integer value
     */
    public static boolean isEven(BigDecimal a) {
        if (!isInteger(a)) {
            throw new IllegalArgumentException("the argument is not an integer");
        }
        return a.remainder(TWO, MC).compareTo(ZERO) == 0;
    }

    /**
     * Checks to see if the BigDecimal is an odd number.
     *
     * @param a the number
     * @return <code>true</code> if a is an odd number, <code>false</code> if a is an even number
     * @throws IllegalArgumentException if a isn't an integer value
     */
    public static boolean isOdd(BigDecimal a) {
        return !isEven(a);
    }

    /**
     * Convert gradians to degrees.
     *
     * @param a the angle in gradians
     * @return the angle in degrees
     */
    public static BigDecimal gradiansToDegrees(BigDecimal a) {
        return a.divide(TWO_HUNDRED, MC).multiply(ONE_EIGHTY, MC);
    }

    /**
     * Convert degrees to gradians.
     *
     * @param a the angel in degrees
     * @return the angle in gradians
     */
    public static BigDecimal degreesToGradians(BigDecimal a) {
        return a.divide(ONE_EIGHTY, MC).multiply(TWO_HUNDRED, MC);
    }

    /**
     * Convert degrees to radians.
     *
     * @param a the angel in degrees
     * @return the angle in radians
     */
    public static BigDecimal degreesToRadians(BigDecimal a) {
        return a.multiply(PI, MC).divide(ONE_EIGHTY, MC);
    }

    /**
     * Convert radians to degrees.
     *
     * @param a the angel in radians
     * @return the angle in degrees
     */
    public static BigDecimal radiansToDegrees(BigDecimal a) {
        return a.multiply(ONE_EIGHTY, MC).divide(PI, MC);
    }

    /**
     * Convert radians to gradians.
     *
     * @param a the angel in radians
     * @return the angle in gradians
     */
    public static BigDecimal radiansToGradians(BigDecimal a) {
        return a.multiply(TWO_HUNDRED, MC).divide(PI, MC);
    }

    /**
     * Convert gradians to radians.
     *
     * @param a the angel in gradians
     * @return the angle in radians
     */
    public static BigDecimal gradiansToRadians(BigDecimal a) {
        return a.multiply(PI, MC).divide(TWO_HUNDRED, MC);
    }
}