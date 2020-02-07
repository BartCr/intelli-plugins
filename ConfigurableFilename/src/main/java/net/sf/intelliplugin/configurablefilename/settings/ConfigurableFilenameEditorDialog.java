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

package net.sf.intelliplugin.configurablefilename.settings;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.util.ui.JBUI;
import net.sf.intelliplugin.configurablefilename.messages.MessageBundle;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class ConfigurableFilenameEditorDialog extends DialogWrapper {

    private final JTextField nameField = new JTextField(15);
    private final JTextField extensionField = new JTextField(5);
    private final JTextField templateField = new JTextField(30);

    public ConfigurableFilenameEditorDialog(JComponent parent, String title) {
        super(parent, true);
        setTitle(title);
        init();
    }

    @Override
    public void show() {
        super.show();
        nameField.requestFocus();
    }

    public ConfigurableFilename getData() {
        ConfigurableFilename filename = new ConfigurableFilename();

        filename.setType(convertString(nameField.getText()));
        filename.setDefaultExtension(convertString(extensionField.getText()));
        filename.setTemplate(convertString(templateField.getText()));

        return filename;
    }

    public void setData(ConfigurableFilename filename) {
        nameField.setText(filename.getType());
        extensionField.setText(filename.getDefaultExtension());
        templateField.setText(filename.getTemplate());
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return panelBuilder()
                // name
                .gridX(0)
                .gridY(0)
                .anchor(GridBagConstraints.WEST)
                .insets(JBUI.insetsTop(5))
                .add(new JLabel(MessageBundle.message("dialog.name.label")))

                .gridX(1)
                .gridY(0)
                .weightX(1)
                .insets(JBUI.insets(5, 10, 0, 0))
                .fill(GridBagConstraints.HORIZONTAL)
                .anchor(GridBagConstraints.WEST)
                .add(nameField)

                // extension
                .gridX(0)
                .gridY(1)
                .anchor(GridBagConstraints.WEST)
                .insets(JBUI.insetsTop(5))
                .add(new JLabel(MessageBundle.message("dialog.extension.label")))

                .gridX(1)
                .gridY(1)
                .insets(JBUI.insets(5, 10, 0, 0))
                .anchor(GridBagConstraints.WEST)
                .add(extensionField)

                // template
                .gridX(0)
                .gridY(2)
                .anchor(GridBagConstraints.WEST)
                .insets(JBUI.insetsTop(5))
                .add(new JLabel(MessageBundle.message("dialog.template.label")))

                .gridX(1)
                .gridY(2)
                .weightX(1)
                .insets(JBUI.insets(5, 10, 0, 0))
                .fill(GridBagConstraints.HORIZONTAL)
                .anchor(GridBagConstraints.WEST)
                .add(templateField)

                .build();
    }

    private String convertString(String s) {
        if (s != null && s.trim().isEmpty()) return null;
        return s;
    }

    private static PanelBuilder panelBuilder() {
        return new PanelBuilder();
    }

    @SuppressWarnings({"SameParameterValue", "unused"})
    private static class PanelBuilder {
        private JPanel panel;

        private int gridX = -1;
        private int gridY = -1;
        private int gridWidth = 1;
        private int gridHeight = 1;
        private double weightX = 0.0D;
        private double weightY = 0.0D;
        private int anchor = GridBagConstraints.CENTER;
        private int fill = 0;
        private Insets insets = JBUI.emptyInsets();
        private int iPadX = 0;
        private int iPadY = 0;

        private PanelBuilder() {
            this.panel = new JPanel(new GridBagLayout());
        }

        private PanelBuilder gridX(int gridX) {
            this.gridX = gridX;
            return this;
        }

        private PanelBuilder gridY(int gridY) {
            this.gridY = gridY;
            return this;
        }

        private PanelBuilder anchor(int anchor) {
            this.anchor = anchor;
            return this;
        }

        private PanelBuilder insets(Insets insets) {
            this.insets = insets;
            return this;
        }

        private PanelBuilder weightX(double weightX) {
            this.weightX = weightX;
            return this;
        }

        private PanelBuilder weightY(double weightY) {
            this.weightY = weightY;
            return this;
        }

        private PanelBuilder fill(int fill) {
            this.fill = fill;
            return this;
        }

        private PanelBuilder add(JComponent component) {
            GridBagConstraints constr = new GridBagConstraints(gridX, gridY, gridWidth, gridHeight, weightX, weightY,
                                                               anchor, fill, insets, iPadX, iPadY);
            panel.add(component, constr);
            reset();
            return this;
        }

        private JPanel build() {
            return panel;
        }

        private void reset() {
            gridX = -1;
            gridY = -1;
            gridWidth = 1;
            gridHeight = 1;
            weightX = 0.0D;
            weightY = 0.0D;
            anchor = GridBagConstraints.CENTER;
            fill = 0;
            insets = JBUI.emptyInsets();
            iPadX = 0;
            iPadY = 0;
        }
    }
}
