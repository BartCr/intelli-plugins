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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Action handler allowing execution of a {@link CalcAction} and providing callback after execution to interested
 * objects.
 *
 * @author Bart Cremers
 * @since 2.0
 */
@SuppressWarnings({"ClassNamePrefixedWithPackageName"})
public class ActionEventHandler implements ActionListener {
    private final CalcAction calcAction;
    private final ActionCallback callback;

    /**
     * @param calcAction the action to execute whenever the handler receives an
     *                   {@link #actionPerformed(java.awt.event.ActionEvent)} from the AWT event mechanism
     * @param callback   the object to notify after successful execution of the action
     */
    public ActionEventHandler(@NotNull CalcAction calcAction, @Nullable ActionCallback callback) {
        this.calcAction = calcAction;
        this.callback = callback;
    }

    public void actionPerformed(ActionEvent e) {
        calcAction.executeAction();
        if (callback != null) {
            callback.executeAfterAction();
        }
    }
}
