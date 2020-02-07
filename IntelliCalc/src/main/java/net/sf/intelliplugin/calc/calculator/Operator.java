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

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * @author Bart Cremers
 * @since 2.0
 */
public abstract class Operator {
    protected static final MathContext DEFAULT_MATH_CONTEXT = new MathContext(20, RoundingMode.HALF_EVEN);

    private int argumentCount;
    private boolean trigonometric;

    /**
     * Construct a new non-trigonometric operator.
     *
     * @param argumentCount the number of arguments expected by the operator
     */
    protected Operator(int argumentCount) {
        this(argumentCount, false);
    }

    /**
     * Construct a new operator.
     *
     * @param argumentCount the number of arguments expected by the operator
     * @param trigonometric set to <code>true</code> if the operator is a trigonometric function
     * @throws IllegalArgumentException if the argumentCount specified is not <b>1</b> or <b>2</b>.
     */
    protected Operator(int argumentCount, boolean trigonometric) {
        if (argumentCount != 1 && argumentCount != 2) {
            throw new IllegalArgumentException("Expected an argument count of 1 or 2.");
        }
        this.argumentCount = argumentCount;
        this.trigonometric = trigonometric;
    }

    /**
     * @return the expected argument count for the operator
     */
    public final int getArgumentCount() {
        return argumentCount;
    }

    /**
     * Execute the operator.
     *
     * @param percentage perform percentage calculation or not
     * @param value      the value to perform the operation on
     * @return the calculated result of the operation
     * @throws IllegalArgumentException if this method is performed on an operator which expects 2 arguments
     */
    @NotNull
    public final BigDecimal execute(boolean percentage, @NotNull BigDecimal value) {
        if (argumentCount != 1) {
            throw new IllegalArgumentException("Expected a single argument for this operator: " + getClass());
        }
        if (percentage) {
            return doExecutePercentage(value);
        } else {
            return doExecute(value);
        }
    }

    /**
     * Execute the operator.
     *
     * @param percentage perform percentage calculation or not
     * @param leftVal    the left value for the operation
     * @param rightVal   the right value for the operation
     * @return the calculated result of the operation
     * @throws IllegalArgumentException if this method is performed on an operator which expects 1 argument
     */
    @NotNull
    public final BigDecimal execute(boolean percentage, @NotNull BigDecimal leftVal, @NotNull BigDecimal rightVal) {
        if (argumentCount != 2) {
            throw new IllegalArgumentException("Expected two arguments for this operator: " + getClass());
        }
        if (percentage) {
            return doExecutePercentage(leftVal, rightVal);
        } else {
            return doExecute(leftVal, rightVal);
        }
    }

    /**
     * Subclasses are required to implement this method to actually perform the calculation.
     *
     * @param values the values to use for the operation. Values are ordered left to right in the array.
     * @return the calculated result of the operation
     */
    @NotNull
    protected abstract BigDecimal doExecute(BigDecimal... values);

    /**
     * Subclasses can override this method to perform percentage calculation. This default implementation simple
     * returns {@link BigDecimal#ZERO}.
     *
     * @param values the values to use for the operation. Values are ordered left to right in the array.
     * @return the calculated result of the operation
     */
    @NotNull
    protected BigDecimal doExecutePercentage(BigDecimal... values) {
        return BigDecimal.ZERO;
    }

    /**
     * @return <code>true</code> if this is a trigonometric operator; <code>false</code> otherwise
     */
    public final boolean isTrigonometric() {
        return trigonometric;
    }
}
