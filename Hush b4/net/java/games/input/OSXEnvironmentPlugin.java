// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.util.ArrayList;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.security.AccessController;
import java.io.File;
import java.security.PrivilegedAction;
import net.java.games.util.plugins.Plugin;

public final class OSXEnvironmentPlugin extends ControllerEnvironment implements Plugin
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
                    OSXEnvironmentPlugin.supported = false;
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
    
    private static final boolean isMacOSXEqualsOrBetterThan(final int major_required, final int minor_required) {
        final String os_version = System.getProperty("os.version");
        final StringTokenizer version_tokenizer = new StringTokenizer(os_version, ".");
        int major;
        int minor;
        try {
            final String major_str = version_tokenizer.nextToken();
            final String minor_str = version_tokenizer.nextToken();
            major = Integer.parseInt(major_str);
            minor = Integer.parseInt(minor_str);
        }
        catch (Exception e) {
            ControllerEnvironment.logln("Exception occurred while trying to determine OS version: " + e);
            return false;
        }
        return major > major_required || (major == major_required && minor >= minor_required);
    }
    
    public OSXEnvironmentPlugin() {
        if (this.isSupported()) {
            this.controllers = enumerateControllers();
        }
        else {
            this.controllers = new Controller[0];
        }
    }
    
    public final Controller[] getControllers() {
        return this.controllers;
    }
    
    public boolean isSupported() {
        return OSXEnvironmentPlugin.supported;
    }
    
    private static final void addElements(final OSXHIDQueue queue, final List elements, final List components, final boolean map_mouse_buttons) throws IOException {
        for (final OSXHIDElement element : elements) {
            Component.Identifier id = element.getIdentifier();
            if (id == null) {
                continue;
            }
            if (map_mouse_buttons) {
                if (id == Component.Identifier.Button._0) {
                    id = Component.Identifier.Button.LEFT;
                }
                else if (id == Component.Identifier.Button._1) {
                    id = Component.Identifier.Button.RIGHT;
                }
                else if (id == Component.Identifier.Button._2) {
                    id = Component.Identifier.Button.MIDDLE;
                }
            }
            final OSXComponent component = new OSXComponent(id, element);
            components.add(component);
            queue.addElement(element, component);
        }
    }
    
    private static final Keyboard createKeyboardFromDevice(final OSXHIDDevice device, final List elements) throws IOException {
        final List components = new ArrayList();
        final OSXHIDQueue queue = device.createQueue(32);
        try {
            addElements(queue, elements, components, false);
        }
        catch (IOException e) {
            queue.release();
            throw e;
        }
        final Component[] components_array = new Component[components.size()];
        components.toArray(components_array);
        final Keyboard keyboard = new OSXKeyboard(device, queue, components_array, new Controller[0], new Rumbler[0]);
        return keyboard;
    }
    
    private static final Mouse createMouseFromDevice(final OSXHIDDevice device, final List elements) throws IOException {
        final List components = new ArrayList();
        final OSXHIDQueue queue = device.createQueue(32);
        try {
            addElements(queue, elements, components, true);
        }
        catch (IOException e) {
            queue.release();
            throw e;
        }
        final Component[] components_array = new Component[components.size()];
        components.toArray(components_array);
        final Mouse mouse = new OSXMouse(device, queue, components_array, new Controller[0], new Rumbler[0]);
        if (mouse.getPrimaryButton() != null && mouse.getX() != null && mouse.getY() != null) {
            return mouse;
        }
        queue.release();
        return null;
    }
    
    private static final AbstractController createControllerFromDevice(final OSXHIDDevice device, final List elements, final Controller.Type type) throws IOException {
        final List components = new ArrayList();
        final OSXHIDQueue queue = device.createQueue(32);
        try {
            addElements(queue, elements, components, false);
        }
        catch (IOException e) {
            queue.release();
            throw e;
        }
        final Component[] components_array = new Component[components.size()];
        components.toArray(components_array);
        final AbstractController controller = new OSXAbstractController(device, queue, components_array, new Controller[0], new Rumbler[0], type);
        return controller;
    }
    
    private static final void createControllersFromDevice(final OSXHIDDevice device, final List controllers) throws IOException {
        final UsagePair usage_pair = device.getUsagePair();
        if (usage_pair == null) {
            return;
        }
        final List elements = device.getElements();
        if (usage_pair.getUsagePage() == UsagePage.GENERIC_DESKTOP && (usage_pair.getUsage() == GenericDesktopUsage.MOUSE || usage_pair.getUsage() == GenericDesktopUsage.POINTER)) {
            final Controller mouse = createMouseFromDevice(device, elements);
            if (mouse != null) {
                controllers.add(mouse);
            }
        }
        else if (usage_pair.getUsagePage() == UsagePage.GENERIC_DESKTOP && (usage_pair.getUsage() == GenericDesktopUsage.KEYBOARD || usage_pair.getUsage() == GenericDesktopUsage.KEYPAD)) {
            final Controller keyboard = createKeyboardFromDevice(device, elements);
            if (keyboard != null) {
                controllers.add(keyboard);
            }
        }
        else if (usage_pair.getUsagePage() == UsagePage.GENERIC_DESKTOP && usage_pair.getUsage() == GenericDesktopUsage.JOYSTICK) {
            final Controller joystick = createControllerFromDevice(device, elements, Controller.Type.STICK);
            if (joystick != null) {
                controllers.add(joystick);
            }
        }
        else if (usage_pair.getUsagePage() == UsagePage.GENERIC_DESKTOP && usage_pair.getUsage() == GenericDesktopUsage.MULTI_AXIS_CONTROLLER) {
            final Controller multiaxis = createControllerFromDevice(device, elements, Controller.Type.STICK);
            if (multiaxis != null) {
                controllers.add(multiaxis);
            }
        }
        else if (usage_pair.getUsagePage() == UsagePage.GENERIC_DESKTOP && usage_pair.getUsage() == GenericDesktopUsage.GAME_PAD) {
            final Controller game_pad = createControllerFromDevice(device, elements, Controller.Type.GAMEPAD);
            if (game_pad != null) {
                controllers.add(game_pad);
            }
        }
    }
    
    private static final Controller[] enumerateControllers() {
        final List controllers = new ArrayList();
        try {
            final OSXHIDDeviceIterator it = new OSXHIDDeviceIterator();
            try {
                while (true) {
                    try {
                        final OSXHIDDevice device = it.next();
                        if (device == null) {
                            break;
                        }
                        boolean device_used = false;
                        try {
                            final int old_size = controllers.size();
                            createControllersFromDevice(device, controllers);
                            device_used = (old_size != controllers.size());
                        }
                        catch (IOException e3) {
                            ControllerEnvironment.logln("Failed to create controllers from device: " + device.getProductName());
                        }
                        if (device_used) {
                            continue;
                        }
                        device.release();
                    }
                    catch (IOException e) {
                        ControllerEnvironment.logln("Failed to enumerate device: " + e.getMessage());
                    }
                }
            }
            finally {
                it.close();
            }
        }
        catch (IOException e2) {
            ControllerEnvironment.log("Failed to enumerate devices: " + e2.getMessage());
            return new Controller[0];
        }
        final Controller[] controllers_array = new Controller[controllers.size()];
        controllers.toArray(controllers_array);
        return controllers_array;
    }
    
    static {
        OSXEnvironmentPlugin.supported = false;
        final String osName = getPrivilegedProperty("os.name", "").trim();
        if (osName.equals("Mac OS X")) {
            OSXEnvironmentPlugin.supported = true;
            loadLibrary("jinput-osx");
        }
    }
}
