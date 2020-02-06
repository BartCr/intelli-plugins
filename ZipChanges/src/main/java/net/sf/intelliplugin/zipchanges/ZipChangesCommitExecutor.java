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

import com.intellij.icons.AllIcons;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.util.ProgressIndicatorBase;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.FilePath;
import com.intellij.openapi.vcs.changes.*;
import com.intellij.openapi.vcs.changes.shelf.ShelveChangesManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.vcsUtil.VcsUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Bart Cremers
 * @since 26-okt-2007
 */
public class ZipChangesCommitExecutor implements CommitExecutor, ProjectComponent {

    private final Project project;
    private final ChangeListManager changeListManager;
    public String PATCH_PATH;

    public static ZipChangesCommitExecutor getInstance(Project project) {
        return project.getComponent(ZipChangesCommitExecutor.class);
    }

    public ZipChangesCommitExecutor(Project project, ChangeListManager changelistmanager) {
        PATCH_PATH = "";
        this.project = project;
        changeListManager = changelistmanager;
    }

    @NotNull
    public Icon getActionIcon() {
        return AllIcons.Nodes.Folder;
    }

    @NotNull
    public String getActionText() {
        return "Zip Changelist...";
    }

    public String getActionDescription() {
        return "Create a Zip File from the selected changes";
    }

    @NotNull
    @Override
    public CommitSession createCommitSession(@NotNull CommitContext commitContext) {
        return new ZipChangesCommitSession();
    }

    @NotNull
    public String getComponentName() {
        return "ZipChangesCommitExecutor";
    }

    private class ZipChangesCommitSession implements CommitSession {

        private final ZipChangesConfigurationPanel configurationPanel;

        private ZipChangesCommitSession() {
            this.configurationPanel = new ZipChangesConfigurationPanel();
        }

        public JComponent getAdditionalConfigurationUI(@NotNull Collection<Change> collection, String s) {
            if (PATCH_PATH.length() == 0) {
                VirtualFile projectDir = ProjectUtil.guessProjectDir(project);
                if (projectDir != null)
                    PATCH_PATH = projectDir.getPresentableUrl();
            }
            File file = ShelveChangesManager.suggestPatchName(project, s, new File(PATCH_PATH), "zip");
            if (!file.getName().endsWith(".zip")) {
                String path = file.getPath();
                if (path.lastIndexOf('.') >= 0) {
                    path = path.substring(0, path.lastIndexOf('.'));
                }
                file = new File(path + ".zip");
            }
            configurationPanel.setFileName(file);
            return configurationPanel.getContentPane();
        }

        public void execute(@NotNull Collection<Change> changes, String s) {

            try {
                Set<VirtualFile> vcsRoots = new HashSet<>();

                for (Change change : changes) {
                    if (change.getAfterRevision() != null) {
                        FilePath path = change.getAfterRevision().getFile();
                        VirtualFile vcsRoot = VcsUtil.getVcsRootFor(project, path);
                        vcsRoots.add(vcsRoot);
                    }
                }
                if (!vcsRoots.isEmpty()) {
                    Iterator<VirtualFile> fileIterator = vcsRoots.iterator();
                    VirtualFile root = fileIterator.next();

                    while (fileIterator.hasNext()) {
                        root = VfsUtil.getCommonAncestor(root, fileIterator.next());
                    }

                    if (root != null) {
                        ZipRunnable zipRunnable = new ZipRunnable(configurationPanel.getFileName(), root, changes);
                        ProgressManager.getInstance().runProcess(zipRunnable, zipRunnable);
                    } else {
                        SwingUtilities.invokeLater(
                                () -> Messages.showErrorDialog(project, "No common ancestor found for changes.", "Error"));
                    }
                } else {
                    SwingUtilities.invokeLater(() -> Messages.showErrorDialog(project, "No VCS roots found.", "Error"));
                }
            } catch (final Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> Messages.showErrorDialog(project, e.getMessage(), "Error"));
            }
        }
    }

    private class ZipRunnable extends ProgressIndicatorBase implements Runnable {

        private final String fileName;
        private final VirtualFile root;
        private final Collection<Change> changes;

        private ZipRunnable(String fileName, VirtualFile root, Collection<Change> changes) {
            this.fileName = fileName;
            this.root = root;
            this.changes = changes;
            setIndeterminate(true);
        }

        public void run() {
            try {
                File file = new File(fileName);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                ZipOutputStream out = new ZipOutputStream(new FileOutputStream(fileName));
                byte[] buf = new byte[1024];
                for (Change change : changes) {
                    if (change.getAfterRevision() != null) {
                        FilePath path = change.getAfterRevision().getFile();
                        String zipEntryPath = VfsUtil.getRelativePath(path.getVirtualFile(), root, '/');
                        out.putNextEntry(new ZipEntry(zipEntryPath));

                        FileInputStream in = new FileInputStream(path.getIOFile());

                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        out.closeEntry();
                        in.close();
                    }
                }
                out.close();
            } catch (final IOException e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> Messages.showErrorDialog(project, e.getMessage(), "Error"));
            }
        }
    }
}
