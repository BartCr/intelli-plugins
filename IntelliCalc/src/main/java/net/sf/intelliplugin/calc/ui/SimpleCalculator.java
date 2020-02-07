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
import net.sf.intelliplugin.calc.calculator.CalcEngine;
import net.sf.intelliplugin.calc.calculator.Operators;
import net.sf.intelliplugin.calc.ui.action.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Simple implementation of the gui.
 * 
 * @author Bart Cremers
 * @since 1.0
 */
public class SimpleCalculator extends AbstractCalculator implements ActionCallback {
    private JPanel contentPane;
    private JTextField tfOutput;
    private JButton btDivide;
    private JButton btClear;
    private JButton btClearAll;
    private JButton btMultiply;
    private JButton btSix;
    private JButton btFive;
    private JButton btFour;
    private JButton btMinus;
    private JButton btThree;
    private JButton btTwo;
    private JButton btOne;
    private JButton btPlus;
    private JButton btEvaluate;
    private JButton btZero;
    private JButton btPoint;
    private JButton btNine;
    private JButton btEight;
    private JButton btSeven;
    private JButton btPercent;
    private JButton btOneDivX;
    private JButton btPlusMin;
    private JButton btBackspace;

    private JButton btToExpression;
    private JButton btToScientific;

    private final CalcEngine engine = CalcEngine.getInstance();

    /**
     * @param pluginGui the parent gui
     */
    public SimpleCalculator(PluginGui pluginGui) {
        super(pluginGui);

        tfOutput.setEditable(false);
        tfOutput.setBackground(JBColor.WHITE);
        tfOutput.setBorder(BorderFactory.createLoweredBevelBorder());
        
        EventHandler eventHandler = new EventHandler();
        btToScientific.addActionListener(eventHandler);
        btToExpression.addActionListener(eventHandler);

        btClear.addActionListener(eventHandler);
        btClearAll.addActionListener(eventHandler);

        btEvaluate.addActionListener(eventHandler);
        btPercent.addActionListener(eventHandler);
        btPlusMin.addActionListener(eventHandler);
        btBackspace.addActionListener(eventHandler);

        btPlus.addActionListener(new ActionEventHandler(new OperatorAction(Operators.ADD), this));
        btMinus.addActionListener(new ActionEventHandler(new OperatorAction(Operators.SUBTRACT), this));
        btDivide.addActionListener(new ActionEventHandler(new OperatorAction(Operators.DIVIDE), this));
        btMultiply.addActionListener(new ActionEventHandler(new OperatorAction(Operators.MULTIPLY), this));

        btOneDivX.addActionListener(new ActionEventHandler(new OperatorAction(Operators.RECIPROCAL), this));

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
        keyHandler.addButton('R', btOneDivX);
        keyHandler.addButton(KeyStroke.getKeyStroke("F9"), btPlusMin);
        keyHandler.addButton('=', btEvaluate);
        keyHandler.addButton(KeyStroke.getKeyStroke("ENTER"), btEvaluate);
        keyHandler.addButton('%', btPercent);
        keyHandler.addButton(KeyStroke.getKeyStroke("ESCAPE"), btClear);
        keyHandler.addButton(KeyStroke.getKeyStroke("DELETE"), btClearAll);
        keyHandler.addButton(KeyStroke.getKeyStroke("BACK_SPACE"), btBackspace);

        executeAfterAction();

        pluginGui.setNumeralSystem(NumeralSystem.DECIMAL);
        pluginGui.setAngleSystem(AngleSystem.DEGREES);

        CalcEngine.getInstance().setNumeralSystem(pluginGui.getNumeralSystem());
        CalcEngine.getInstance().setAngleSystem(pluginGui.getAngleSystem());
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    public void executeAfterAction() {
        tfOutput.setText(engine.getDisplayValue(20));
    }

    protected void propertyChanged(String property) {
    }

    /**
     * Action handler for the buttons.
     */
    private class EventHandler implements ActionListener {

        @SuppressWarnings({"ObjectEquality"})
        public void actionPerformed(ActionEvent actionEvent) {
            Object source = actionEvent.getSource();

            if (btToScientific == source) {
                getPluginGui().setMode(Mode.SCIENTIFIC);
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
     * Standard main method to allow testing outside IntelliJ IDEA.
     *
     * @param args No arguments expected.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimpleCalculator simpleCalculator = new SimpleCalculator(null);
            JFrame frame = new JFrame();
            frame.add(simpleCalculator.getContentPane());
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
