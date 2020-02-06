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
package net.sf.intelliplugin.zipchanges;

import com.intellij.openapi.ui.TextFieldWithBrowseButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * @author Bart Cremers
 * @since 26-okt-2007
 */
public class ZipChangesConfigurationPanel implements ActionListener {

    private TextFieldWithBrowseButton fileName;
    private JPanel contentPane;

    private void createUIComponents() {
        fileName = new TextFieldWithBrowseButton();
        fileName.addActionListener(this);
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    public void setFileName(File file) {
        fileName.setText(file.getAbsolutePath());
    }

    public String getFileName() {
        return fileName.getText();
    }

    public void actionPerformed(ActionEvent actionevent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(fileName.getText()));
        if (chooser.showSaveDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
            fileName.setText(chooser.getSelectedFile().getPath());
        }
    }
}
