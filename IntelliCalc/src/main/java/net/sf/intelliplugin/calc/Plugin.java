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
package net.sf.intelliplugin.calc;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import net.sf.intelliplugin.calc.ui.PluginGui;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Project component implementation for the IntelliCalc plugin.
 *
 * @author Bart Cremers
 * @since 1.0
 */
@State(name = Plugin.NAME, storages = { @Storage(Plugin.NAME + ".xml")})
public class Plugin implements PersistentStateComponent<PluginState> {

    public static final String MODE_PROPERTY = Plugin.class.getName() + ".mode";
    public static final String NUMERAL_SYSTEM_PROPERTY = Plugin.class.getName() + ".system.numeral";
    public static final String ANGLE_SYSTEM_PROPERTY = Plugin.class.getName() + ".system.angle";

    /**
     * The Plugin name.
     */
    @NonNls
    public static final String NAME = "IntelliCalc";

    /**
     * The Plugin version.
     */
    @NonNls
    public static final String VERSION = "2.0";

    private final PluginGui pluginGui;

    private PluginState state = new PluginState();

    public Plugin(PluginGui pluginGui) {
        this.pluginGui = pluginGui;
    }

    @Nullable
    @Override
    public PluginState getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull PluginState state) {
        this.state = state;
    }

    public JComponent getGUI() {
        return pluginGui;
    }
}
