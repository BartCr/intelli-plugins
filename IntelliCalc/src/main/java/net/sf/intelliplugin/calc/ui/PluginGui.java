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

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowType;
import net.sf.intelliplugin.calc.AngleSystem;
import net.sf.intelliplugin.calc.Mode;
import net.sf.intelliplugin.calc.NumeralSystem;
import net.sf.intelliplugin.calc.Plugin;

import javax.swing.*;
import java.awt.*;

/**
 * The GUI for the calculator plugin.
 *
 * @author Bart Cremers
 * @since 1.0
 */
public class PluginGui extends JPanel {

    private AbstractCalculator calculator;

    private final ToolWindow toolWindow;

    /**
     * Construct a new gui for the project.
     *
     * @param toolWindow the tool window containing the UI.
     */
    public PluginGui(ToolWindow toolWindow) {
        super(new BorderLayout());
        this.toolWindow = toolWindow;

        calculator = createCalculator();
        add(calculator.getContentPane(), BorderLayout.CENTER);
    }

    /**
     * @return the configured numeral system
     */
    public NumeralSystem getNumeralSystem() {
        String value = PropertiesComponent.getInstance().getValue(Plugin.NUMERAL_SYSTEM_PROPERTY);
        return value != null ? NumeralSystem.valueOf(value) : NumeralSystem.DECIMAL;
    }

    /**
     * @param numeralSystem the new numeral system to set on the calculator
     */
    public void setNumeralSystem(NumeralSystem numeralSystem) {
        if (getNumeralSystem() != numeralSystem) {
            PropertiesComponent.getInstance().setValue(Plugin.NUMERAL_SYSTEM_PROPERTY, numeralSystem.name());
            calculator.setNumeralSystem(numeralSystem);
        }
    }

    /**
     * @return the configured angle system
     */
    public AngleSystem getAngleSystem() {
        String value = PropertiesComponent.getInstance().getValue(Plugin.ANGLE_SYSTEM_PROPERTY);
        return value != null ? AngleSystem.valueOf(value) : AngleSystem.DEGREES;
    }

    /**
     * @param angleSystem the new angle system to set on the calculator
     */
    public void setAngleSystem(AngleSystem angleSystem) {
        if (getAngleSystem() != angleSystem) {
            PropertiesComponent.getInstance().setValue(Plugin.ANGLE_SYSTEM_PROPERTY, angleSystem.name());
            calculator.setAngleSystem(angleSystem);
        }
    }

    /**
     * @return the current mode
     */
    public Mode getMode() {
        String value = PropertiesComponent.getInstance().getValue(Plugin.MODE_PROPERTY);
        return value != null ? Mode.valueOf(value) : Mode.SIMPLE;
    }

    /**
     * Changes the mode calculator mode. This results in the disposal of the old and construction of a new calculator.
     *
     * @param mode the new mode for the calculator.
     * @see #createCalculator()
     */
    public void setMode(Mode mode) {
        if (mode != getMode()) {
            PropertiesComponent.getInstance().setValue(Plugin.MODE_PROPERTY, mode.name());

            remove(calculator.getContentPane());

            calculator = createCalculator();

            add(calculator.getContentPane(), BorderLayout.CENTER);
            revalidate();

            if (toolWindow != null) {
                if (toolWindow.getType() == ToolWindowType.FLOATING) {
                    SwingUtilities.getWindowAncestor(this).pack();
                } /* TODO: else {
                Container parent = getParent();
                while (parent != null) {
                    System.out.println("parent = " + parent);
                    parent = parent.getParent();

                    if (parent instanceof ToolWindowsPane) {
                                                         
                    }
                }
            }*/
            }
        }
    }

    /**
     * @return the current calculator
     */
    public AbstractCalculator getCalculator() {
        return calculator;
    }

    /**
     * Create a new calculator based on the mode.
     *
     * @return a new, correctly configured calculator
     * @throws IllegalStateException if no mode is set
     */
    private AbstractCalculator createCalculator() {
        AbstractCalculator calculator;
        switch (getMode()) {
            case SIMPLE:
                calculator = new SimpleCalculator(this);
                break;
            case SCIENTIFIC:
                calculator = new ScientificCalculator(this);
                break;
            case EXPRESSION:
                calculator = new ExpressionCalculator(this);
                break;
            default:
                throw new IllegalStateException("IntelliCalc is not correctly configured.");
        }

        calculator.setAngleSystem(getAngleSystem());
        calculator.setNumeralSystem(getNumeralSystem());
        return calculator;
    }
}
