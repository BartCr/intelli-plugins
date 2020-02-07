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

import com.intellij.ui.JBColor;
import net.sf.intelliplugin.calc.AngleSystem;
import net.sf.intelliplugin.calc.Mode;
import net.sf.intelliplugin.calc.NumeralSystem;
import net.sf.intelliplugin.calc.calculator.BigMath;
import net.sf.intelliplugin.calc.calculator.CalcEngine;
import net.sf.intelliplugin.calc.calculator.Operators;
import net.sf.intelliplugin.calc.ui.action.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Scientific implementation of the gui.
 *
 * @author Bart Cremers
 * @since 1.0
 */
public class ScientificCalculator extends AbstractCalculator implements ActionCallback {
    private JPanel contentPane;

    private JTextField tfOutput;

    private JButton btClear;
    private JButton btClearAll;
    private JButton btBackspace;

    private JButton btZero;
    private JButton btOne;
    private JButton btTwo;
    private JButton btThree;
    private JButton btFour;
    private JButton btFive;
    private JButton btSix;
    private JButton btSeven;
    private JButton btEight;
    private JButton btNine;
    private JButton btA;
    private JButton btB;
    private JButton btC;
    private JButton btD;
    private JButton btE;
    private JButton btF;
    private JButton btPoint;

    private JButton btPi;
    private JButton btEuler;

    private JButton btPlus;
    private JButton btMinus;
    private JButton btMultiply;
    private JButton btDivide;

    private JButton btEvaluate;
    private JButton btPercent;

    private JButton btPlusMin;

    private JButton btPow;
    private JButton btSquareRoot;
    private JButton btSquare;
    private JButton btReciprocal;
    private JButton btLog;
    private JButton btLn;
    private JButton btFactorial;

    private JButton btSin;
    private JButton btCos;
    private JButton btTan;
    private JButton btCotan;
    private JButton btAsin;
    private JButton btAcos;
    private JButton btAtan;
    private JButton btAcotan;
    private JButton btSinh;
    private JButton btCosh;
    private JButton btTanh;
    private JButton btCotanh;

    private JButton btOpenBracket;
    private JButton btCloseBracket;

    private JButton btToExpression;
    private JButton btToSimple;

    private JToggleButton tbBinary;
    private JToggleButton tbOctal;
    private JToggleButton tbDecimal;
    private JToggleButton tbHexadecimal;

    private JToggleButton tbDegrees;
    private JToggleButton tbRadians;
    private JToggleButton tbGradians;

    private final CalcEngine engine = CalcEngine.getInstance();

    /**
     * @param pluginGui the parent gui
     */
    public ScientificCalculator(PluginGui pluginGui) {
        super(pluginGui);

        tfOutput.setEditable(false);
        tfOutput.setBackground(JBColor.WHITE);
        tfOutput.setBorder(BorderFactory.createLoweredBevelBorder());

        EventHandler eventHandler = new EventHandler();
        btToSimple.addActionListener(eventHandler);
        btToExpression.addActionListener(eventHandler);

        btClear.addActionListener(eventHandler);
        btClearAll.addActionListener(eventHandler);
        btBackspace.addActionListener(eventHandler);

        btEvaluate.addActionListener(eventHandler);
        btPercent.addActionListener(eventHandler);
        btPlusMin.addActionListener(eventHandler);

        btEuler.addActionListener(new ActionEventHandler(new ConstantAction(BigMath.EULER), this));
        btPi.addActionListener(new ActionEventHandler(new ConstantAction(BigMath.PI), this));

        btPlus.addActionListener(new ActionEventHandler(new OperatorAction(Operators.ADD), this));
        btMinus.addActionListener(new ActionEventHandler(new OperatorAction(Operators.SUBTRACT), this));
        btDivide.addActionListener(new ActionEventHandler(new OperatorAction(Operators.DIVIDE), this));
        btMultiply.addActionListener(new ActionEventHandler(new OperatorAction(Operators.MULTIPLY), this));

        btPow.addActionListener(new ActionEventHandler(new OperatorAction(Operators.POWER), this));
        btReciprocal.addActionListener(new ActionEventHandler(new OperatorAction(Operators.RECIPROCAL), this));
        btSquareRoot.addActionListener(new ActionEventHandler(new OperatorAction(Operators.SQUARE_ROOT), this));
        btSquare.addActionListener(new ActionEventHandler(new OperatorAction(Operators.SQUARE), this));
        btFactorial.addActionListener(new ActionEventHandler(new OperatorAction(Operators.FACTORIAL), this));
        btLog.addActionListener(new ActionEventHandler(new OperatorAction(Operators.LOGARITHM_10), this));
        btLn.addActionListener(new ActionEventHandler(new OperatorAction(Operators.NATURAL_LOGARITHM), this));
        
        btPoint.addActionListener(new ActionEventHandler(new InputAction('.'), this));
        btZero.addActionListener(new ActionEventHandler(new InputAction('0'), this));
        btOne.addActionListener(new ActionEventHandler(new InputAction('1'), this));
        btTwo.addActionListener(new ActionEventHandler(new InputAction('2'), this));
        btThree.addActionListener(new ActionEventHandler(new InputAction('3'), this));
        btFour.addActionListener(new ActionEventHandler(new InputAction('4'), this));
        btFive.addActionListener(new ActionEventHandler(new InputAction('5'), this));
        btSix.addActionListener(new ActionEventHandler(new InputAction('6'), this));
        btSeven.addActionListener(new ActionEventHandler(new InputAction('7'), this));
        btEight.addActionListener(new ActionEventHandler(new InputAction('8'), this));
        btNine.addActionListener(new ActionEventHandler(new InputAction('9'), this));
        btA.addActionListener(new ActionEventHandler(new InputAction('A'), this));
        btB.addActionListener(new ActionEventHandler(new InputAction('B'), this));
        btC.addActionListener(new ActionEventHandler(new InputAction('C'), this));
        btD.addActionListener(new ActionEventHandler(new InputAction('D'), this));
        btE.addActionListener(new ActionEventHandler(new InputAction('E'), this));
        btF.addActionListener(new ActionEventHandler(new InputAction('F'), this));

        btSin.addActionListener(new ActionEventHandler(new OperatorAction(Operators.SINE), this));
        btCos.addActionListener(new ActionEventHandler(new OperatorAction(Operators.COSINE), this));
        btTan.addActionListener(new ActionEventHandler(new OperatorAction(Operators.TANGENT), this));
        btCotan.addActionListener(new ActionEventHandler(new OperatorAction(Operators.COTANGENT), this));
        btAsin.addActionListener(new ActionEventHandler(new OperatorAction(Operators.INVERSE_SINE), this));
        btAcos.addActionListener(new ActionEventHandler(new OperatorAction(Operators.INVERSE_COSINE), this));
        btAtan.addActionListener(new ActionEventHandler(new OperatorAction(Operators.INVERSE_TANGENT), this));
        btAcotan.addActionListener(new ActionEventHandler(new OperatorAction(Operators.INVERSE_COTANGENT), this));
        btSinh.addActionListener(new ActionEventHandler(new OperatorAction(Operators.HYPERBOLIC_SINE), this));
        btCosh.addActionListener(new ActionEventHandler(new OperatorAction(Operators.HYPERBOLIC_COSINE), this));
        btTanh.addActionListener(new ActionEventHandler(new OperatorAction(Operators.HYPERBOLIC_TANGENT), this));
        btCotanh.addActionListener(new ActionEventHandler(new OperatorAction(Operators.HYPERBOLIC_COTANGENT), this));

        NumeralSystemHandler numeralSystemHandler = new NumeralSystemHandler();
        tbBinary.addActionListener(numeralSystemHandler);
        tbOctal.addActionListener(numeralSystemHandler);
        tbDecimal.addActionListener(numeralSystemHandler);
        tbHexadecimal.addActionListener(numeralSystemHandler);

        AngleSystemHandler angleSystemHandler = new AngleSystemHandler();
        tbDegrees.addActionListener(angleSystemHandler);
        tbGradians.addActionListener(angleSystemHandler);
        tbRadians.addActionListener(angleSystemHandler);
        
        KeyboardButtonHandler keyHandler = new KeyboardButtonHandler(contentPane);
        keyHandler.addButton('/', btDivide);
        keyHandler.addButton('*', btMultiply);
        keyHandler.addButton('+', btPlus);
        keyHandler.addButton('-', btMinus);
        keyHandler.addButton('.', btPoint);
        keyHandler.addButton('0', btZero);
        keyHandler.addButton('1', btOne);
        keyHandler.addButton('2', btTwo);
        keyHandler.addButton('3', btThree);
        keyHandler.addButton('4', btFour);
        keyHandler.addButton('5', btFive);
        keyHandler.addButton('6', btSix);
        keyHandler.addButton('7', btSeven);
        keyHandler.addButton('8', btEight);
        keyHandler.addButton('9', btNine);
        keyHandler.addButton('A', btA);
        keyHandler.addButton('B', btB);
        keyHandler.addButton('C', btC);
        keyHandler.addButton('D', btD);
        keyHandler.addButton('E', btE);
        keyHandler.addButton('F', btF);
        keyHandler.addButton('a', btA);
        keyHandler.addButton('b', btB);
        keyHandler.addButton('c', btC);
        keyHandler.addButton('d', btD);
        keyHandler.addButton('e', btE);
        keyHandler.addButton('f', btF);
        keyHandler.addButton('R', btReciprocal);
        keyHandler.addButton('r', btReciprocal);
        keyHandler.addButton('S', btSin);
        keyHandler.addButton('s', btSin);
        keyHandler.addButton('C', btCos);
        keyHandler.addButton('c', btCos);
        keyHandler.addButton('T', btTan);
        keyHandler.addButton('t', btTan);
        keyHandler.addButton(KeyStroke.getKeyStroke("F9"), btPlusMin);
        keyHandler.addButton('=', btEvaluate);
        keyHandler.addButton(KeyStroke.getKeyStroke("ENTER"), btEvaluate);
        keyHandler.addButton('%', btPercent);
        keyHandler.addButton(KeyStroke.getKeyStroke("ESCAPE"), btClear);
        keyHandler.addButton(KeyStroke.getKeyStroke("DELETE"), btClearAll);
        keyHandler.addButton(KeyStroke.getKeyStroke("BACK_SPACE"), btBackspace);

        executeAfterAction();

        NumeralSystem numeralSystem = pluginGui != null ? pluginGui.getNumeralSystem() : NumeralSystem.DECIMAL;
        AngleSystem angleSystem = pluginGui != null ? pluginGui.getAngleSystem() : AngleSystem.DEGREES;

        CalcEngine.getInstance().setNumeralSystem(numeralSystem);
        CalcEngine.getInstance().setAngleSystem(angleSystem);

        updateGuiForSystem(numeralSystem);
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    public void executeAfterAction() {
        tfOutput.setText(engine.getDisplayValue(20));
    }

    protected void propertyChanged(String property) {
        if (NUMERAL_SYSTEM.equals(property)) {
            switch (getNumeralSystem()) {
                case BINARY:
                    tbBinary.setSelected(true);
                    break;
                case OCTAL:
                    tbOctal.setSelected(true);
                    break;
                case DECIMAL:
                    tbDecimal.setSelected(true);
                    break;
                case HEXADECIMAL:
                    tbHexadecimal.setSelected(true);
                    break;

            }
        } else if (ANGLE_SYSTEM.equals(property)) {
            switch (getAngleSystem()) {
                case DEGREES:
                    tbDegrees.setSelected(true);
                    break;
                case RADIANS:
                    tbRadians.setSelected(true);
                    break;
                case GRADIANS:
                    tbGradians.setSelected(true);
                    break;
            }
        }
    }

    /**
     * Action handler for the buttons.
     */
    private class EventHandler implements ActionListener {

        @SuppressWarnings({"ObjectEquality"})
        public void actionPerformed(ActionEvent actionEvent) {
            Object source = actionEvent.getSource();

            if (btToSimple == source) {
                getPluginGui().setMode(Mode.SIMPLE);
            } else if (btToExpression == source) {
                getPluginGui().setMode(Mode.EXPRESSION);
            } else if (btPercent == source) {
                engine.evaluate(true);
            } else if (btEvaluate == source) {
                engine.evaluate(false);
            } else if (btPlusMin == source) {
                engine.negateCurrentValue();
            } else if (btBackspace == source) {
                engine.backspace();
            } else if (btClear == source) {
                engine.clear();
            } else if (btClearAll == source) {
                engine.clearAll();
            }

            executeAfterAction();
        }
    }

    /**
     * Action handler for switching the numeral system.
     */
    private class NumeralSystemHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            NumeralSystem numeralSystem;
            if (tbBinary.isSelected()) {
                numeralSystem = NumeralSystem.BINARY;
            } else if (tbOctal.isSelected()) {
                numeralSystem = NumeralSystem.OCTAL;
            } else if (tbDecimal.isSelected()) {
                numeralSystem = NumeralSystem.DECIMAL;
            } else if (tbHexadecimal.isSelected()) {
                numeralSystem = NumeralSystem.HEXADECIMAL;
            } else {
                throw new IllegalStateException("Can't deselect all buttons.");
            }

            if (getPluginGui() != null) {
                getPluginGui().setNumeralSystem(numeralSystem);
            }
            CalcEngine.getInstance().setNumeralSystem(numeralSystem);
            updateGuiForSystem(numeralSystem);
            executeAfterAction();
        }
    }

    /**
     * Action handler for switching the angle system.
     */
    private class AngleSystemHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            AngleSystem angleSystem;
            if (tbDegrees.isSelected()) {
                angleSystem = AngleSystem.DEGREES;
            } else if (tbRadians.isSelected()) {
                angleSystem = AngleSystem.RADIANS;
            } else if (tbGradians.isSelected()) {
                angleSystem = AngleSystem.GRADIANS;
            } else {
                throw new IllegalStateException("Can't deselect all buttons.");
            }

            if (getPluginGui() != null) {
                getPluginGui().setAngleSystem(angleSystem);
            }
            CalcEngine.getInstance().setAngleSystem(angleSystem);
            executeAfterAction();
        }
    }

    /**
     * Update the gui for the system. Enabling and disabling specific buttons.
     *
     * @param system the system to use for decision
     */
    private void updateGuiForSystem(NumeralSystem system) {
        btTwo.setEnabled(system.getBase() > 2);
        btThree.setEnabled(system.getBase() > 3);
        btFour.setEnabled(system.getBase() > 4);
        btFive.setEnabled(system.getBase() > 5);
        btSix.setEnabled(system.getBase() > 6);
        btSeven.setEnabled(system.getBase() > 7);
        btEight.setEnabled(system.getBase() > 8);
        btNine.setEnabled(system.getBase() > 9);
        btA.setEnabled(system.getBase() > 10);
        btB.setEnabled(system.getBase() > 11);
        btC.setEnabled(system.getBase() > 12);
        btD.setEnabled(system.getBase() > 13);
        btE.setEnabled(system.getBase() > 14);
        btF.setEnabled(system.getBase() > 15);
        btPoint.setEnabled(system == NumeralSystem.DECIMAL);

        btSin.setEnabled(system == NumeralSystem.DECIMAL);
        btCos.setEnabled(system == NumeralSystem.DECIMAL);
        btTan.setEnabled(system == NumeralSystem.DECIMAL);
        btCotan.setEnabled(system == NumeralSystem.DECIMAL);
        btAsin.setEnabled(system == NumeralSystem.DECIMAL);
        btAcos.setEnabled(system == NumeralSystem.DECIMAL);
        btAtan.setEnabled(system == NumeralSystem.DECIMAL);
        btAcotan.setEnabled(system == NumeralSystem.DECIMAL);
        btSinh.setEnabled(system == NumeralSystem.DECIMAL);
        btCosh.setEnabled(system == NumeralSystem.DECIMAL);
        btTanh.setEnabled(system == NumeralSystem.DECIMAL);
        btCotanh.setEnabled(system == NumeralSystem.DECIMAL);

        tbDegrees.setVisible(system == NumeralSystem.DECIMAL);
        tbGradians.setVisible(system == NumeralSystem.DECIMAL);
        tbRadians.setVisible(system == NumeralSystem.DECIMAL);

        btPi.setEnabled(system == NumeralSystem.DECIMAL);
        btEuler.setEnabled(system == NumeralSystem.DECIMAL);
        btReciprocal.setEnabled(system == NumeralSystem.DECIMAL);
        btSquare.setEnabled(system == NumeralSystem.DECIMAL);
        btSquareRoot.setEnabled(system == NumeralSystem.DECIMAL);
        btPow.setEnabled(system == NumeralSystem.DECIMAL);
        btLog.setEnabled(system == NumeralSystem.DECIMAL);
        btLn.setEnabled(system == NumeralSystem.DECIMAL);
        btFactorial.setEnabled(system == NumeralSystem.DECIMAL);
    }

    /**
     * Standard main method to allow testing outside IntelliJ IDEA.
     *
     * @param args No arguments expected.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ScientificCalculator simpleCalculator = new ScientificCalculator(null);
            JFrame frame = new JFrame();
            frame.add(simpleCalculator.getContentPane());
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
