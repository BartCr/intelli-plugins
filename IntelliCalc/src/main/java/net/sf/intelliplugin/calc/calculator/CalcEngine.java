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

import net.sf.intelliplugin.calc.AngleSystem;
import net.sf.intelliplugin.calc.NumeralSystem;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Calculator engine. This class tracks the input values and the operator.
 *
 * @author Bart Cremers
 * @since 2.0
 */
public class CalcEngine {

    /**
     * The singleton engine instance.
     */
    private static final CalcEngine INSTANCE = new CalcEngine();

    private static final DecimalFormat NORMAL_FORMAT = new DecimalFormat("###########0.##########");
    private static final DecimalFormat SCIENTIFIC_FORMAT = new DecimalFormat("##0.#####E0");

    /**
     * Is the in memory value.
     */
    private BigDecimal storedValue;

    /**
     * Is the current displayed value. This can easily be altered.
     */
    private BigDecimal currentValue;

    /**
     * Is the operator typed. Setting a new operator will execute a previous operator if present first.
     */
    private Operator currentOperator;

    /**
     * Keeps track of the current input. If input was active and a new operator is set it might be required to evaluate
     * the old operator first.
     */
    private boolean inputActive;

    /**
     * Can be set to specify the next character to append is a decimal character.
     */
    private boolean decimalInput;

    /**
     * Possible error message as result of an operation. When this is set, no other value will be displayed by the
     * engine.
     */
    private String errorMessage;

    /**
     * The {@link NumeralSystem} the engine works with.
     */
    private NumeralSystem numeralSystem = NumeralSystem.DECIMAL;

    /**
     * The {@link net.sf.intelliplugin.calc.AngleSystem} the engine works with.
     */
    private AngleSystem angleSystem = AngleSystem.DEGREES;

    private CalcEngine() {
        clearAll();
    }

    /**
     * @return the singleton CalcEngine instance
     */
    public static CalcEngine getInstance() {
        return INSTANCE;
    }

    /**
     * Clear all values in the engine.
     */
    @SuppressWarnings({"AssignmentToNull"})
    public void clearAll() {
        clear();
        storedValue = BigDecimal.ZERO;
        currentOperator = null;
    }

    /**
     * Clear the current value. The current operator and stored value are not touched.
     */
    @SuppressWarnings({"AssignmentToNull"})
    public void clear() {
        currentValue = BigDecimal.ZERO;
        inputActive = false;
        decimalInput = false;
        errorMessage = null;
    }

    /**
     * @param length the required maximum length for the output
     * @return the current value to display on the calculator formatted correctly
     */
    public String getDisplayValue(int length) {

        if (errorMessage != null) {
            return errorMessage;
        }
        String value;
        switch (numeralSystem) {
            case BINARY:
                value = Long.toBinaryString(currentValue.longValue());
                break;
            case OCTAL:
                value = Long.toOctalString(currentValue.longValue());
                break;
            case DECIMAL:
                value = NORMAL_FORMAT.format(currentValue);
//                if (value.length() > length) {
//                    value = SCIENTIFIC_FORMAT.format(currentValue);
//                }
                break;
            case HEXADECIMAL:
                value = Long.toHexString(currentValue.longValue());
                break;
            default:
                throw new IllegalArgumentException("Illegal Numeral System");
        }

        if (value.indexOf('.') < 0) {
            value += '.';
        }
        return value.toUpperCase();
    }

    /**
     * Evaluate the current entered expression.
     *
     * @param percentage denotes a percentage calculation instead of a normal calculation.
     */
    @SuppressWarnings({"AssignmentToNull"})
    public void evaluate(boolean percentage) {
        try {
            if (currentOperator != null) {
                if (currentOperator.getArgumentCount() == 2) {
                    storedValue = currentOperator.execute(percentage, storedValue, currentValue);
                } else {
                    if (currentOperator.isTrigonometric()) {
                        BigDecimal converted = convertToRadians(currentValue);
                        storedValue = currentOperator.execute(percentage, converted);
                    } else {
                        storedValue = currentOperator.execute(percentage, currentValue);
                    }
                }
                currentValue = storedValue;
            }
            inputActive = false;
            decimalInput = false;
            currentOperator = null;
        } catch (ArithmeticException e) {
            clearAll();
            errorMessage = e.getMessage();
        }
    }

    /**
     * Sets the numeral system the engine should use for displaying the values. Switching to another system from the
     * decimal system will clear all values if the current value has decimal input going.
     *
     * @param numeralSystem the {@link net.sf.intelliplugin.calc.NumeralSystem} to use.
     */
    public void setNumeralSystem(@NotNull NumeralSystem numeralSystem) {
        if (numeralSystem != NumeralSystem.DECIMAL && decimalInput) {
            clearAll();
        }
        this.numeralSystem = numeralSystem;
    }

    /**
     * Sets the angle system the engine should us for displaying the values. Setting the angle system is only possible
     * when the {@link net.sf.intelliplugin.calc.NumeralSystem#DECIMAL} is used.
     *
     * @param angleSystem the {@link net.sf.intelliplugin.calc.AngleSystem} to use.
     * @see #setNumeralSystem(net.sf.intelliplugin.calc.NumeralSystem)
     */
    public void setAngleSystem(AngleSystem angleSystem) {
        AngleSystem old = this.angleSystem;
        this.angleSystem = angleSystem;

        if (old != angleSystem) {
            switch (old) {
                case DEGREES:
                    switch(angleSystem) {
                        case RADIANS:
                            currentValue = BigMath.degreesToRadians(currentValue);
                            break;
                        case GRADIANS:
                            currentValue = BigMath.degreesToGradians(currentValue);
                            break;
                    }
                    break;
                case RADIANS:
                    switch(angleSystem) {
                        case DEGREES:
                            currentValue = BigMath.radiansToDegrees(currentValue);
                            break;
                        case GRADIANS:
                            currentValue = BigMath.radiansToGradians(currentValue);
                            break;
                    }
                    break;
                case GRADIANS:
                    switch(angleSystem) {
                        case DEGREES:
                            currentValue = BigMath.gradiansToDegrees(currentValue);
                            break;
                        case RADIANS:
                            currentValue = BigMath.gradiansToRadians(currentValue);
                            break;
                    }
                    break;
            }
        }
    }

    /**
     * Sets a new operator for the calculation. This might result in the old operator being executed first.
     *
     * @param operator the new operator for the calculation.
     */
    public void setOperator(Operator operator) {
        if (errorMessage != null) {
            return;
        }
        if (inputActive && currentOperator != null && currentOperator.getArgumentCount() == 2) {
            evaluate(false);
        }
        currentOperator = operator;
        if (operator.getArgumentCount() == 1) {
            evaluate(false);
        } else {
            storedValue = currentValue;
        }
        inputActive = false;
    }

    /**
     * Append a character the the current value if possible.
     *
     * @param ch the character to append.
     */
    @SuppressWarnings({"AssignmentToNull"})
    public void appendChar(char ch) {
        switch (ch) {
            case'.':
                if (numeralSystem == NumeralSystem.DECIMAL) {
                    decimalInput = true;
                }
                break;
            case'0':
            case'1':
            case'2':
            case'3':
            case'4':
            case'5':
            case'6':
            case'7':
            case'8':
            case'9':
                int val = ch - 0x30;
                if (inputActive) {
                    if (!decimalInput) {
                        switch (numeralSystem) {
                            case BINARY:
                                currentValue =
                                    currentValue.multiply(BigMath.TWO).add(new BigDecimal(String.valueOf(val)));
                                break;
                            case OCTAL:
                                currentValue =
                                    currentValue.multiply(BigMath.EIGHT).add(new BigDecimal(String.valueOf(val)));
                                break;
                            case DECIMAL:
                                currentValue =
                                    currentValue.multiply(BigMath.TEN).add(new BigDecimal(String.valueOf(val)));
                                break;
                            case HEXADECIMAL:
                                currentValue =
                                    currentValue.multiply(BigMath.SIXTEEN).add(new BigDecimal(String.valueOf(val)));
                                break;
                        }
                    } else {
                        int scale = currentValue.scale();
                        for (int i = 0; i < scale + 1; i++) {
                            currentValue = currentValue.multiply(BigMath.TEN);
                        }

                        currentValue = currentValue.add(new BigDecimal(String.valueOf(val)));
                        for (int i = 0; i < scale + 1; i++) {
                            currentValue = currentValue.divide(BigMath.TEN, RoundingMode.UNNECESSARY);
                        }
                    }
                } else {
                    currentValue = new BigDecimal(String.valueOf(val));
                }
                break;
            case'A':
            case'B':
            case'C':
            case'D':
            case'E':
            case'F':
                val = ch - 0x37;
                currentValue =
                    currentValue.multiply(BigMath.SIXTEEN).add(new BigDecimal(String.valueOf(val)));
                break;
            default:
                throw new IllegalArgumentException("'" + ch + "' is not a legal character to append to the engine.");
        }
        errorMessage = null;
        inputActive = true;
    }

    /**
     * Negate the current value. This means the current value will become -(current value).
     */
    public void negateCurrentValue() {
        currentValue = currentValue.negate();
    }

    /**
     * Remove the last (right most) character of the current value.
     */
    public void backspace() {
        if (decimalInput) {
            int scale = currentValue.scale();
            if (scale == 0) {
                decimalInput = false;
            } else {
                currentValue = currentValue.divide(BigDecimal.TEN, scale, RoundingMode.FLOOR);
                currentValue = currentValue.movePointRight(1);
            }
        } else {
            int scale = currentValue.scale();

            switch (numeralSystem) {
                case BINARY:
                    currentValue =
                        currentValue.divide(BigMath.TWO, scale, RoundingMode.DOWN);
                    break;
                case OCTAL:
                    currentValue =
                        currentValue.divide(BigMath.EIGHT, scale, RoundingMode.DOWN);
                    break;
                case DECIMAL:
                    currentValue =
                        currentValue.divide(BigMath.TEN, scale, RoundingMode.DOWN);
                    break;
                case HEXADECIMAL:
                    currentValue =
                        currentValue.divide(BigMath.SIXTEEN, scale, RoundingMode.DOWN);
                    break;
            }
        }
    }

    /**
     * Convert the current value to radians.
     *
     * @param value the current value in either {@link AngleSystem#RADIANS}, {@link AngleSystem#DEGREES} or
     *              {@link AngleSystem#GRADIANS}.
     * @return the converted value
     * @throws IllegalStateException if there is no angle system stored in the engine.
     */
    private BigDecimal convertToRadians(BigDecimal value) {
        switch(angleSystem) {
            case RADIANS:
                return value;
            case DEGREES:
                return BigMath.degreesToRadians(value);
            case GRADIANS:
                return BigMath.gradiansToRadians(value);
            default:
                throw new IllegalStateException("Illegal configured angle system");
        }
    }

    /**
     * Overwrites the current value with a new constant value.
     *
     * @param constant the constant to set
     */
    public void setCurrentValue(BigDecimal constant) {
        currentValue = constant;
    }
}
