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
import net.sf.intelliplugin.calc.Mode;
import net.sf.intelliplugin.calc.math.Derive;
import net.sf.intelliplugin.calc.math.Eval;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Calculator implementation using expression evaluation.
 *
 * @author Bart Cremers
 * @since 2.0
 */
public class ExpressionCalculator extends AbstractCalculator {
    private JPanel contentPane;
    private JTextField tfInput;
    private JTextArea taOutput;
    private JButton btEvaluate;
    private JButton btToScientific;
    private JButton btToSimple;
    private JButton btClear;

    private final Eval eval;
    private final Derive derive;
    private final Map<String, String> values;

    /**
     * @param pluginGui the parent gui
     */
    public ExpressionCalculator(PluginGui pluginGui) {
        super(pluginGui);

        eval = new Eval();
        derive = new Derive();
        values = new HashMap<>();

        taOutput.setBackground(JBColor.WHITE);

        EventHandler eventHandler = new EventHandler();

        btClear.addActionListener(eventHandler);
        btEvaluate.addActionListener(eventHandler);
        btToScientific.addActionListener(eventHandler);
        btToSimple.addActionListener(eventHandler);
        tfInput.addActionListener(eventHandler);

        taOutput.setEditable(false);
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    protected void propertyChanged(String property) {
    }

    /**
     * Evaluate the text in the input textfield.
     */
    private void evaluate() {
        evaluate(tfInput.getText());
    }

    /**
     * Evaluate the given expression.
     *
     * @param expression the expression to evaluate
     */
    public void evaluate(String expression) {
        String temp;
        String tmp;
        int ind1;
        int ind2;

        try {
            tmp = prepare(expression.trim().toLowerCase());

            if (tmp.equals("")) {
                return;
            }

            append("> " + tmp);

            // check if is diff, set or clear() command.
            if ((ind1 = tmp.indexOf("diff(")) != -1) {
                temp = derive.diff(tmp.substring(ind1 + 5, tmp.lastIndexOf(")")))[0];

                append(temp);
                return;
            } else if ((ind1 = tmp.indexOf("set(")) != -1) {
                try {
                    ind2 = tmp.lastIndexOf(")");
                    temp = tmp.substring(ind1 + 4, ind2);
                    values.put(temp.substring(0, temp.indexOf("=")), temp.substring(temp.indexOf("=") + 1));
                    append("Value set, " + getValues());
                } catch (Exception ex) {
                    append("Syntax error, " + tmp);
                }
                return;
            } else if ((ind1 = tmp.indexOf("clear(")) != -1) {
                if ((ind2 = tmp.lastIndexOf(")")) == -1) {
                    append("Non matching brackets");
                    return;
                }
                temp = tmp.substring(ind1 + 6, ind2);
                values.remove(temp);
                append("Value cleared, " + getValues());
                return;
            } else if (tmp.contains("memory")) {
                append(getValues());
                return;
            }

            // normal, evaluate.
            append(String.valueOf(eval.eval(tmp, values)));
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            append(ex.getMessage());
        }
    }

    /**
     * @return a formatted string of all stored values
     */
    private String getValues() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String key : values.keySet()) {
            if (!first) {
                sb.append(";");
            }
            sb.append(key).append('=').append(values.get(key));
            first = false;
        }
        return sb.toString();
    }

    /**
     * Prepare the expression for evaluation.
     *
     * @param expression the expression to prepare
     * @return the expression prepared for evaluation.
     * @see #evaluate(String)
     */
    private String prepare(String expression) {
        StringBuilder nStr = new StringBuilder();
        int i = 0;
        int count = 0;

        while (i < expression.length()) {

            if (expression.charAt(i) == '(') {
                count++;
            } else if (expression.charAt(i) == ')') {
                count--;
            }

            if (expression.charAt(i) != ' ') {
                nStr.append(expression.charAt(i));
            }

            i++;
        }

        if (count != 0) {
            append("Non matching brackets, " + expression);
            return "";
        }

        return nStr.toString();
    }

    /**
     * Append the text to the output text area, inserting a new line after appending.
     * @param text the text to append
     */
    private void append(String text) {
        taOutput.append(text + "\n");
        tfInput.requestFocusInWindow();
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
            } else if (btToScientific == source) {
                getPluginGui().setMode(Mode.SCIENTIFIC);
            } else if (btEvaluate == source || tfInput == source) {
                evaluate();
            } else if (btClear == source) {
                taOutput.setText("");
            }
        }
    }

    /**
     * Standard main method to allow testing outside IntelliJ IDEA.
     *
     * @param args No arguments expected.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ExpressionCalculator simpleCalculator = new ExpressionCalculator(null);
            JFrame frame = new JFrame();
            frame.add(simpleCalculator.getContentPane());
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

}
