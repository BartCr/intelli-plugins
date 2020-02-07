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
package net.sf.intelliplugin.calc.ui.action;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * A handler class to simplify listening for KeyEvents on a base component and translating the KeyEvent to an
 * ActionEvent on a JButton.
 *
 * @author Bart Cremers
 * @since 2.0
 */
public class KeyboardButtonHandler implements AWTEventListener {
    private final Map<KeyStroke, JButton> strokeToButton = new HashMap<>();
    private final Map<Character, JButton> charToButton = new HashMap<>();

    private final Component baseComponent;

    /**
     * @param baseComponent the base component containing the buttons for which KeyEvents should be forwarded.
     */
    public KeyboardButtonHandler(Component baseComponent) {
        this.baseComponent = baseComponent;
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);
    }

    /**
     * Link the specified button to the stroke.
     *
     * @param stroke the stroke to listen for.
     * @param button the button to press whenever the stroke is typed.
     */
    public void addButton(KeyStroke stroke, JButton button) {
        strokeToButton.put(stroke, button);
    }

    /**
     * Link the specified button to the char.
     *
     * @param ch     the char to listen for
     * @param button the button to press whenever the char is typed.
     */
    public void addButton(char ch, JButton button) {
        charToButton.put(ch, button);
    }

    /**
     * {@inheritDoc}
     * <p>
     * If a button is linked to the key stroke that resulted in invoking this method, the button is clicked
     * programmatically.
     */
    public void eventDispatched(AWTEvent event) {
        if (isInBaseComponent(event.getSource())) {
            if (event.getID() == KeyEvent.KEY_PRESSED) {
                KeyEvent keyEvent = ((KeyEvent) event);
                KeyStroke stroke = KeyStroke.getKeyStrokeForEvent(keyEvent);

                JButton button = strokeToButton.get(stroke);
                if (button == null) {
                    button = charToButton.get(keyEvent.getKeyChar());
                }
                if (button != null) {
                    button.doClick();
                }
            }
        }
    }

    /**
     * Check if the given object is a child of the base component.
     *
     * @param source the object to check
     * @return <code>true</code> if the given object is a child component of the base component (or the base component
     *         itself); <code>false</code> otherwise
     */
    @SuppressWarnings({"ObjectEquality"})
    private boolean isInBaseComponent(Object source) {
        return source instanceof Component && (source == baseComponent || isInBaseComponent(
            ((Component) source).getParent()));
    }
}
