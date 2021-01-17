// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.io.IOException;
import java.util.ArrayList;
import java.security.AccessController;
import java.io.File;
import java.security.PrivilegedAction;
import java.util.List;
import net.java.games.util.plugins.Plugin;

public final class DirectInputEnvironmentPlugin extends ControllerEnvironment implements Plugin
{
    private static boolean supported;
    private final Controller[] controllers;
    private final List active_devices;
    private final DummyWindow window;
    
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
                    DirectInputEnvironmentPlugin.supported = false;
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
    
    public DirectInputEnvironmentPlugin() {
        this.active_devices = new ArrayList();
        DummyWindow window = null;
        Controller[] controllers = new Controller[0];
        if (this.isSupported()) {
            try {
                window = new DummyWindow();
                try {
                    controllers = this.enumControllers(window);
                }
                catch (IOException e) {
                    window.destroy();
                    throw e;
                }
            }
            catch (IOException e) {
                ControllerEnvironment.logln("Failed to enumerate devices: " + e.getMessage());
            }
            this.window = window;
            this.controllers = controllers;
            AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction() {
                public final Object run() {
                    Runtime.getRuntime().addShutdownHook(new ShutdownHook());
                    return null;
                }
            });
        }
        else {
            this.window = null;
            this.controllers = controllers;
        }
    }
    
    public final Controller[] getControllers() {
        return this.controllers;
    }
    
    private final Component[] createComponents(final IDirectInputDevice device, final boolean map_mouse_buttons) {
        final List device_objects = device.getObjects();
        final List controller_components = new ArrayList();
        for (int i = 0; i < device_objects.size(); ++i) {
            final DIDeviceObject device_object = device_objects.get(i);
            Component.Identifier identifier = device_object.getIdentifier();
            if (identifier != null) {
                if (map_mouse_buttons && identifier instanceof Component.Identifier.Button) {
                    identifier = DIIdentifierMap.mapMouseButtonIdentifier((Component.Identifier.Button)identifier);
                }
                final DIComponent component = new DIComponent(identifier, device_object);
                controller_components.add(component);
                device.registerComponent(device_object, component);
            }
        }
        final Component[] components = new Component[controller_components.size()];
        controller_components.toArray(components);
        return components;
    }
    
    private final Mouse createMouseFromDevice(final IDirectInputDevice device) {
        final Component[] components = this.createComponents(device, true);
        final Mouse mouse = new DIMouse(device, components, new Controller[0], device.getRumblers());
        if (mouse.getX() != null && mouse.getY() != null && mouse.getPrimaryButton() != null) {
            return mouse;
        }
        return null;
    }
    
    private final AbstractController createControllerFromDevice(final IDirectInputDevice device, final Controller.Type type) {
        final Component[] components = this.createComponents(device, false);
        final AbstractController controller = new DIAbstractController(device, components, new Controller[0], device.getRumblers(), type);
        return controller;
    }
    
    private final Keyboard createKeyboardFromDevice(final IDirectInputDevice device) {
        final Component[] components = this.createComponents(device, false);
        return new DIKeyboard(device, components, new Controller[0], device.getRumblers());
    }
    
    private final Controller createControllerFromDevice(final IDirectInputDevice device) {
        switch (device.getType()) {
            case 18: {
                return this.createMouseFromDevice(device);
            }
            case 19: {
                return this.createKeyboardFromDevice(device);
            }
            case 21: {
                return this.createControllerFromDevice(device, Controller.Type.GAMEPAD);
            }
            case 22: {
                return this.createControllerFromDevice(device, Controller.Type.WHEEL);
            }
            case 20:
            case 23:
            case 24: {
                return this.createControllerFromDevice(device, Controller.Type.STICK);
            }
            default: {
                return this.createControllerFromDevice(device, Controller.Type.UNKNOWN);
            }
        }
    }
    
    private final Controller[] enumControllers(final DummyWindow window) throws IOException {
        final List controllers = new ArrayList();
        final IDirectInput dinput = new IDirectInput(window);
        try {
            final List devices = dinput.getDevices();
            for (int i = 0; i < devices.size(); ++i) {
                final IDirectInputDevice device = devices.get(i);
                final Controller controller = this.createControllerFromDevice(device);
                if (controller != null) {
                    controllers.add(controller);
                    this.active_devices.add(device);
                }
                else {
                    device.release();
                }
            }
        }
        finally {
            dinput.release();
        }
        final Controller[] controllers_array = new Controller[controllers.size()];
        controllers.toArray(controllers_array);
        return controllers_array;
    }
    
    public boolean isSupported() {
        return DirectInputEnvironmentPlugin.supported;
    }
    
    static {
        DirectInputEnvironmentPlugin.supported = false;
        final String osName = getPrivilegedProperty("os.name", "").trim();
        if (osName.startsWith("Windows")) {
            DirectInputEnvironmentPlugin.supported = true;
            if ("x86".equals(getPrivilegedProperty("os.arch"))) {
                loadLibrary("jinput-dx8");
            }
            else {
                loadLibrary("jinput-dx8_64");
            }
        }
    }
    
    private final class ShutdownHook extends Thread
    {
        public final void run() {
            for (int i = 0; i < DirectInputEnvironmentPlugin.this.active_devices.size(); ++i) {
                final IDirectInputDevice device = DirectInputEnvironmentPlugin.this.active_devices.get(i);
                device.release();
            }
        }
    }
}
