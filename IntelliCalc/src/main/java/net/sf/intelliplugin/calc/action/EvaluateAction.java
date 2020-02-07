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
package net.sf.intelliplugin.calc.action;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import net.sf.intelliplugin.calc.Mode;
import net.sf.intelliplugin.calc.Plugin;
import net.sf.intelliplugin.calc.ui.ExpressionCalculator;
import net.sf.intelliplugin.calc.ui.PluginGui;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Editor action allowing to evaluate a selected expression.
 *
 * @author Bart Cremers
 * @since 1.0
 */
@SuppressWarnings({"WeakerAccess"})
public class EvaluateAction extends EditorAction {
    public EvaluateAction() {
        super(new Handler());
    }

    @Override
    public void update(Editor editor, Presentation presentation, DataContext dataContext) {
        super.update(editor, presentation, dataContext);
        presentation.setEnabled(editor.getSelectionModel().hasSelection());
    }

    private static class Handler extends EditorActionHandler {
        @Override
        protected void doExecute(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {
            final String text = editor.getSelectionModel().getSelectedText();
            if (text != null) {
                Project prj = dataContext.getData(PlatformDataKeys.PROJECT);
                if (prj != null) {
                    final ToolWindow window = ToolWindowManager.getInstance(prj).getToolWindow(Plugin.NAME);
                    window.show(() -> {
                        Content content = window.getContentManager().getContent(0);
                        PluginGui pluginGui;
                        if (content != null) {
                            pluginGui = (PluginGui) content.getComponent();
                            pluginGui.setMode(Mode.EXPRESSION);
                            ((ExpressionCalculator) pluginGui.getCalculator()).evaluate(text);
                        }
                    });
                }
            }
        }
    }
}
