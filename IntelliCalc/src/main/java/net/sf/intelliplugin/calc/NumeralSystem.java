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

package net.sf.intelliplugin.calc;

/**
 * @author Bart Cremers
 * @since 2.0
 */
public enum NumeralSystem {
    BINARY(2),
    OCTAL(8),
    DECIMAL(10),
    HEXADECIMAL(16);

    private final int base;

    /**
     * @param base the base used for storing the numeral system configuration
     */
    NumeralSystem(int base) {
        this.base = base;
    }

    /**
     * @return the base for the numeral system
     */
    public int getBase() {
        return base;
    }

    /**
     * @return the string to use for storing the numeral system configuration
     */
    public String getBaseString() {
        return String.valueOf(base);
    }

    /**
     * @param base the name to lookup
     * @return the angle system from the given base. If no angle system is found with the given base, then
     *         {@link #DECIMAL} is returned.
     */
    public static NumeralSystem getNumeralSystem(String base) {
        for (NumeralSystem numeralSystem : values()) {
            if (numeralSystem.getBaseString().equals(base)) {
                return numeralSystem;
            }
        }
        return DECIMAL;
    }
}
