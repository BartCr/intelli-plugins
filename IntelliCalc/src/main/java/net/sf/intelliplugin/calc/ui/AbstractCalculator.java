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
package net.sf.intelliplugin.calc.ui;

import net.sf.intelliplugin.calc.NumeralSystem;
import net.sf.intelliplugin.calc.AngleSystem;

import javax.swing.*;

/**
 * Abstract super class for a calculator visualization.
 *
 * @author Bart Cremers
 * @since 1.0
 */
public abstract class AbstractCalculator {

    /**
     * Property for the numeral system.
     */
    protected static final String NUMERAL_SYSTEM = "NUMERAL_SYSTEM";

    /**
     * Property for the angle system.
     */
    protected static final String ANGLE_SYSTEM = "ANGLE_SYSTEM";

    private final PluginGui pluginGui;
    private NumeralSystem numeralSystem = NumeralSystem.DECIMAL;
    private AngleSystem angleSystem = AngleSystem.DEGREES;

    /**
     * @param pluginGui the {@link PluginGui} which is parent for the calculator.
     */
    protected AbstractCalculator(PluginGui pluginGui) {
        this.pluginGui = pluginGui;
    }

    /**
     * @return the parent {@link PluginGui}
     */
    PluginGui getPluginGui() {
        return pluginGui;
    }

    /**
     * @param numeralSystem sets the new numeral system to use.
     */
    public void setNumeralSystem(NumeralSystem numeralSystem) {
        if (this.numeralSystem != numeralSystem) {
            this.numeralSystem = numeralSystem;
            propertyChanged(NUMERAL_SYSTEM);
        }
    }

    /**
     * @return the current numeral system
     */
    public NumeralSystem getNumeralSystem() {
        return numeralSystem;
    }

    /**
     * @param angleSystem sets the angle system to use
     */
    public void setAngleSystem(AngleSystem angleSystem) {
        if (this.angleSystem != angleSystem) {
            this.angleSystem = angleSystem;
            propertyChanged(ANGLE_SYSTEM);
        }
    }

    /**
     * @return the current angle system
     */
    public AngleSystem getAngleSystem() {
        return angleSystem;
    }

    /**
     * @return the root panel for the calculator gui
     */
    public abstract JPanel getContentPane();

    /**
     * Invoked when a property changes in the calculator.
     *
     * @param property the name of the changed property.
     * @see #setAngleSystem(net.sf.intelliplugin.calc.AngleSystem)
     * @see #setNumeralSystem(net.sf.intelliplugin.calc.NumeralSystem)
     */
    protected abstract void propertyChanged(String property);
}
