// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.security.AccessController;
import java.io.File;
import java.security.PrivilegedAction;
import net.java.games.util.plugins.Plugin;

public final class RawInputEnvironmentPlugin extends ControllerEnvironment implements Plugin
{
    private static boolean supported;
    private final Controller[] controllers;
    
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
                    RawInputEnvironmentPlugin.supported = false;
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
    
    public RawInputEnvironmentPlugin() {
        Controller[] controllers = new Controller[0];
        if (this.isSupported()) {
            try {
                final RawInputEventQueue queue = new RawInputEventQueue();
                controllers = this.enumControllers(queue);
            }
            catch (IOException e) {
                ControllerEnvironment.logln("Failed to enumerate devices: " + e.getMessage());
            }
        }
        this.controllers = controllers;
    }
    
    public final Controller[] getControllers() {
        return this.controllers;
    }
    
    private static final SetupAPIDevice lookupSetupAPIDevice(String device_name, final List setupapi_devices) {
        device_name = device_name.replaceAll("#", "\\\\").toUpperCase();
        for (int i = 0; i < setupapi_devices.size(); ++i) {
            final SetupAPIDevice device = setupapi_devices.get(i);
            if (device_name.indexOf(device.getInstanceId().toUpperCase()) != -1) {
                return device;
            }
        }
        return null;
    }
    
    private static final void createControllersFromDevices(final RawInputEventQueue queue, final List controllers, final List devices, final List setupapi_devices) throws IOException {
        final List active_devices = new ArrayList();
        for (int i = 0; i < devices.size(); ++i) {
            final RawDevice device = devices.get(i);
            final SetupAPIDevice setupapi_device = lookupSetupAPIDevice(device.getName(), setupapi_devices);
            if (setupapi_device != null) {
                final RawDeviceInfo info = device.getInfo();
                final Controller controller = info.createControllerFromDevice(device, setupapi_device);
                if (controller != null) {
                    controllers.add(controller);
                    active_devices.add(device);
                }
            }
        }
        queue.start(active_devices);
    }
    
    private static final native void enumerateDevices(final RawInputEventQueue p0, final List p1) throws IOException;
    
    private final Controller[] enumControllers(final RawInputEventQueue queue) throws IOException {
        final List controllers = new ArrayList();
        final List devices = new ArrayList();
        enumerateDevices(queue, devices);
        final List setupapi_devices = enumSetupAPIDevices();
        createControllersFromDevices(queue, controllers, devices, setupapi_devices);
        final Controller[] controllers_array = new Controller[controllers.size()];
        controllers.toArray(controllers_array);
        return controllers_array;
    }
    
    public boolean isSupported() {
        return RawInputEnvironmentPlugin.supported;
    }
    
    private static final List enumSetupAPIDevices() throws IOException {
        final List devices = new ArrayList();
        nEnumSetupAPIDevices(getKeyboardClassGUID(), devices);
        nEnumSetupAPIDevices(getMouseClassGUID(), devices);
        return devices;
    }
    
    private static final native void nEnumSetupAPIDevices(final byte[] p0, final List p1) throws IOException;
    
    private static final native byte[] getKeyboardClassGUID();
    
    private static final native byte[] getMouseClassGUID();
    
    static {
        RawInputEnvironmentPlugin.supported = false;
        final String osName = getPrivilegedProperty("os.name", "").trim();
        if (osName.startsWith("Windows")) {
            RawInputEnvironmentPlugin.supported = true;
            if ("x86".equals(getPrivilegedProperty("os.arch"))) {
                loadLibrary("jinput-raw");
            }
            else {
                loadLibrary("jinput-raw_64");
            }
        }
    }
}
