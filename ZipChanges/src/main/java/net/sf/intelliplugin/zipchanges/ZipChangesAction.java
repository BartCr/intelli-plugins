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

import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.actions.VcsContext;
import com.intellij.openapi.vcs.changes.CommitExecutor;
import com.intellij.openapi.vcs.changes.actions.AbstractCommitChangesAction;
import org.jetbrains.annotations.NotNull;

public class ZipChangesAction extends AbstractCommitChangesAction {

    public ZipChangesAction() {
    }

    public String getActionName(@NotNull VcsContext vcsContext) {
        return "Zip Changes";
    }

    public CommitExecutor getExecutor(@NotNull Project project) {
        return ZipChangesCommitExecutor.getInstance(project);
    }

    @Override
    protected void update(@NotNull VcsContext vcsContext, @NotNull Presentation presentation) {
        super.update(vcsContext, presentation);
        if (presentation.isEnabled()) {

            if (vcsContext.getSelectedChanges() == null
                || vcsContext.getSelectedChanges().length == 0) {
                presentation.setEnabled(false);
            }
        }
    }
}





