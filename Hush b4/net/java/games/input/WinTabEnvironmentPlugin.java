// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.util.ArrayList;
import java.security.AccessController;
import java.io.File;
import java.security.PrivilegedAction;
import java.util.List;
import net.java.games.util.plugins.Plugin;

public class WinTabEnvironmentPlugin extends ControllerEnvironment implements Plugin
{
    private static boolean supported;
    private final Controller[] controllers;
    private final List active_devices;
    private final WinTabContext winTabContext;
    
    static void loadLibrary(final String lib_name) {
        AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction() {
            public final Object run() {
                try {
                    final String lib_path = System.getProperty("net.java.games.input.librarypath");
                    if (lib_path != null) {
                        System.load(lib_path + File.separator + System.mapLibraryName(lib_name));
                    }
                    else {
                        System.loadLibrary(lib_name);
                    }
                }
                catch (UnsatisfiedLinkError e) {
                    e.printStackTrace();
                    WinTabEnvironmentPlugin.supported = false;
                }
                return null;
            }
        });
    }
    
    static String getPrivilegedProperty(final String property) {
        return AccessController.doPrivileged((PrivilegedAction<String>)new PrivilegedAction() {
            public Object run() {
                return System.getProperty(property);
            }
        });
    }
    
    static String getPrivilegedProperty(final String property, final String default_value) {
        return AccessController.doPrivileged((PrivilegedAction<String>)new PrivilegedAction() {
            public Object run() {
                return System.getProperty(property, default_value);
            }
        });
    }
    
    public WinTabEnvironmentPlugin() {
        this.active_devices = new ArrayList();
        if (this.isSupported()) {
            DummyWindow window = null;
            WinTabContext winTabContext = null;
            Controller[] controllers = new Controller[0];
            try {
                window = new DummyWindow();
                winTabContext = new WinTabContext(window);
                try {
                    winTabContext.open();
                    controllers = winTabContext.getControllers();
                }
                catch (Exception e) {
                    window.destroy();
                    throw e;
                }
            }
            catch (Exception e) {
                ControllerEnvironment.logln("Failed to enumerate devices: " + e.getMessage());
                e.printStackTrace();
            }
            this.controllers = controllers;
            this.winTabContext = winTabContext;
            AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction() {
                public final Object run() {
                    Runtime.getRuntime().addShutdownHook(new ShutdownHook());
                    return null;
                }
            });
        }
        else {
            this.winTabContext = null;
            this.controllers = new Controller[0];
        }
    }
    
    public boolean isSupported() {
        return WinTabEnvironmentPlugin.supported;
    }
    
    public Controller[] getControllers() {
        return this.controllers;
    }
    
    static {
        WinTabEnvironmentPlugin.supported = false;
        final String osName = getPrivilegedProperty("os.name", "").trim();
        if (osName.startsWith("Windows")) {
            WinTabEnvironmentPlugin.supported = true;
            loadLibrary("jinput-wintab");
        }
    }
    
    private final class ShutdownHook extends Thread
    {
        public final void run() {
            for (int i = 0; i < WinTabEnvironmentPlugin.this.active_devices.size(); ++i) {}
            WinTabEnvironmentPlugin.this.winTabContext.close();
        }
    }
}
