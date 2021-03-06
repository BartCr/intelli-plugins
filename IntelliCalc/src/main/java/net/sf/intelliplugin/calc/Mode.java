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
* @since 1.0
*/
public enum Mode {
    SIMPLE,
    SCIENTIFIC,
    EXPRESSION;

    /**
     * Get the Mode for a given ordinal. If there's no Mode for that ordinal {@link #SIMPLE} is returned.
     *
     * @param ordinal the ordinal to look up
     * @return the Mode attached to the given ordinal
     */
    public static Mode getMode(int ordinal) {
        for (Mode mode : values()) {
            if (mode.ordinal() == ordinal) {
                return mode;
            }
        }
        return SIMPLE;
    }
}
