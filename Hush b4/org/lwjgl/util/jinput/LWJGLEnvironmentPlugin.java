// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.jinput;

import net.java.games.input.Controller;
import net.java.games.util.plugins.Plugin;
import net.java.games.input.ControllerEnvironment;

public class LWJGLEnvironmentPlugin extends ControllerEnvironment implements Plugin
{
    private final Controller[] controllers;
    
    public LWJGLEnvironmentPlugin() {
        this.controllers = new Controller[] { new LWJGLKeyboard(), new LWJGLMouse() };
    }
    
    @Override
    public Controller[] getControllers() {
        return this.controllers;
    }
    
    @Override
    public boolean isSupported() {
        return true;
    }
}
