// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.util.Iterator;
import java.util.logging.Logger;
import java.util.ArrayList;

public abstract class ControllerEnvironment
{
    private static ControllerEnvironment defaultEnvironment;
    protected final ArrayList controllerListeners;
    
    static void logln(final String msg) {
        log(msg + "\n");
    }
    
    static void log(final String msg) {
        Logger.getLogger(ControllerEnvironment.class.getName()).info(msg);
    }
    
    protected ControllerEnvironment() {
        this.controllerListeners = new ArrayList();
    }
    
    public abstract Controller[] getControllers();
    
    public void addControllerListener(final ControllerListener l) {
        assert l != null;
        this.controllerListeners.add(l);
    }
    
    public abstract boolean isSupported();
    
    public void removeControllerListener(final ControllerListener l) {
        assert l != null;
        this.controllerListeners.remove(l);
    }
    
    protected void fireControllerAdded(final Controller c) {
        final ControllerEvent ev = new ControllerEvent(c);
        final Iterator it = this.controllerListeners.iterator();
        while (it.hasNext()) {
            it.next().controllerAdded(ev);
        }
    }
    
    protected void fireControllerRemoved(final Controller c) {
        final ControllerEvent ev = new ControllerEvent(c);
        final Iterator it = this.controllerListeners.iterator();
        while (it.hasNext()) {
            it.next().controllerRemoved(ev);
        }
    }
    
    public static ControllerEnvironment getDefaultEnvironment() {
        return ControllerEnvironment.defaultEnvironment;
    }
    
    static {
        ControllerEnvironment.defaultEnvironment = new DefaultControllerEnvironment();
    }
}
