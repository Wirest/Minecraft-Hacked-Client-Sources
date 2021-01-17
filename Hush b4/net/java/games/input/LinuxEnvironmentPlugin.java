// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.util.Arrays;
import java.util.Comparator;
import java.io.FilenameFilter;
import java.util.Collection;
import java.util.ArrayList;
import java.io.IOException;
import java.security.AccessController;
import java.io.File;
import java.security.PrivilegedAction;
import java.util.List;
import net.java.games.util.plugins.Plugin;

public final class LinuxEnvironmentPlugin extends ControllerEnvironment implements Plugin
{
    private static final String LIBNAME = "jinput-linux";
    private static final String POSTFIX64BIT = "64";
    private static boolean supported;
    private final Controller[] controllers;
    private final List devices;
    private static final LinuxDeviceThread device_thread;
    
    static void loadLibrary(final String lib_name) {
        AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction() {
            public final Object run() {
                final String lib_path = System.getProperty("net.java.games.input.librarypath");
                try {
                    if (lib_path != null) {
                        System.load(lib_path + File.separator + System.mapLibraryName(lib_name));
                    }
                    else {
                        System.loadLibrary(lib_name);
                    }
                }
                catch (UnsatisfiedLinkError e) {
                    ControllerEnvironment.logln("Failed to load library: " + e.getMessage());
                    e.printStackTrace();
                    LinuxEnvironmentPlugin.supported = false;
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
    
    public static final Object execute(final LinuxDeviceTask task) throws IOException {
        return LinuxEnvironmentPlugin.device_thread.execute(task);
    }
    
    public LinuxEnvironmentPlugin() {
        this.devices = new ArrayList();
        if (this.isSupported()) {
            this.controllers = this.enumerateControllers();
            ControllerEnvironment.logln("Linux plugin claims to have found " + this.controllers.length + " controllers");
            AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction() {
                public final Object run() {
                    Runtime.getRuntime().addShutdownHook(new ShutdownHook());
                    return null;
                }
            });
        }
        else {
            this.controllers = new Controller[0];
        }
    }
    
    public final Controller[] getControllers() {
        return this.controllers;
    }
    
    private static final Component[] createComponents(final List event_components, final LinuxEventDevice device) {
        final LinuxEventComponent[][] povs = new LinuxEventComponent[4][2];
        final List components = new ArrayList();
        for (int i = 0; i < event_components.size(); ++i) {
            final LinuxEventComponent event_component = event_components.get(i);
            final Component.Identifier identifier = event_component.getIdentifier();
            if (identifier == Component.Identifier.Axis.POV) {
                final int native_code = event_component.getDescriptor().getCode();
                switch (native_code) {
                    case 16: {
                        povs[0][0] = event_component;
                        break;
                    }
                    case 17: {
                        povs[0][1] = event_component;
                        break;
                    }
                    case 18: {
                        povs[1][0] = event_component;
                        break;
                    }
                    case 19: {
                        povs[1][1] = event_component;
                        break;
                    }
                    case 20: {
                        povs[2][0] = event_component;
                        break;
                    }
                    case 21: {
                        povs[2][1] = event_component;
                        break;
                    }
                    case 22: {
                        povs[3][0] = event_component;
                        break;
                    }
                    case 23: {
                        povs[3][1] = event_component;
                        break;
                    }
                    default: {
                        ControllerEnvironment.logln("Unknown POV instance: " + native_code);
                        break;
                    }
                }
            }
            else if (identifier != null) {
                final LinuxComponent component = new LinuxComponent(event_component);
                components.add(component);
                device.registerComponent(event_component.getDescriptor(), component);
            }
        }
        for (int i = 0; i < povs.length; ++i) {
            final LinuxEventComponent x = povs[i][0];
            final LinuxEventComponent y = povs[i][1];
            if (x != null && y != null) {
                final LinuxComponent controller_component = new LinuxPOV(x, y);
                components.add(controller_component);
                device.registerComponent(x.getDescriptor(), controller_component);
                device.registerComponent(y.getDescriptor(), controller_component);
            }
        }
        final Component[] components_array = new Component[components.size()];
        components.toArray(components_array);
        return components_array;
    }
    
    private static final Mouse createMouseFromDevice(final LinuxEventDevice device, final Component[] components) throws IOException {
        final Mouse mouse = new LinuxMouse(device, components, new Controller[0], device.getRumblers());
        if (mouse.getX() != null && mouse.getY() != null && mouse.getPrimaryButton() != null) {
            return mouse;
        }
        return null;
    }
    
    private static final Keyboard createKeyboardFromDevice(final LinuxEventDevice device, final Component[] components) throws IOException {
        final Keyboard keyboard = new LinuxKeyboard(device, components, new Controller[0], device.getRumblers());
        return keyboard;
    }
    
    private static final Controller createJoystickFromDevice(final LinuxEventDevice device, final Component[] components, final Controller.Type type) throws IOException {
        final Controller joystick = new LinuxAbstractController(device, components, new Controller[0], device.getRumblers(), type);
        return joystick;
    }
    
    private static final Controller createControllerFromDevice(final LinuxEventDevice device) throws IOException {
        final List event_components = device.getComponents();
        final Component[] components = createComponents(event_components, device);
        final Controller.Type type = device.getType();
        if (type == Controller.Type.MOUSE) {
            return createMouseFromDevice(device, components);
        }
        if (type == Controller.Type.KEYBOARD) {
            return createKeyboardFromDevice(device, components);
        }
        if (type == Controller.Type.STICK || type == Controller.Type.GAMEPAD) {
            return createJoystickFromDevice(device, components, type);
        }
        return null;
    }
    
    private final Controller[] enumerateControllers() {
        final List controllers = new ArrayList();
        final List eventControllers = new ArrayList();
        final List jsControllers = new ArrayList();
        this.enumerateEventControllers(eventControllers);
        this.enumerateJoystickControllers(jsControllers);
        for (int i = 0; i < eventControllers.size(); ++i) {
            for (int j = 0; j < jsControllers.size(); ++j) {
                final Controller evController = eventControllers.get(i);
                final Controller jsController = jsControllers.get(j);
                if (evController.getName().equals(jsController.getName())) {
                    final Component[] evComponents = evController.getComponents();
                    final Component[] jsComponents = jsController.getComponents();
                    if (evComponents.length == jsComponents.length) {
                        boolean foundADifference = false;
                        for (int k = 0; k < evComponents.length; ++k) {
                            if (evComponents[k].getIdentifier() != jsComponents[k].getIdentifier()) {
                                foundADifference = true;
                            }
                        }
                        if (!foundADifference) {
                            controllers.add(new LinuxCombinedController(eventControllers.remove(i), jsControllers.remove(j)));
                            --i;
                            --j;
                            break;
                        }
                    }
                }
            }
        }
        controllers.addAll(eventControllers);
        controllers.addAll(jsControllers);
        final Controller[] controllers_array = new Controller[controllers.size()];
        controllers.toArray(controllers_array);
        return controllers_array;
    }
    
    private static final Component.Identifier.Button getButtonIdentifier(final int index) {
        switch (index) {
            case 0: {
                return Component.Identifier.Button._0;
            }
            case 1: {
                return Component.Identifier.Button._1;
            }
            case 2: {
                return Component.Identifier.Button._2;
            }
            case 3: {
                return Component.Identifier.Button._3;
            }
            case 4: {
                return Component.Identifier.Button._4;
            }
            case 5: {
                return Component.Identifier.Button._5;
            }
            case 6: {
                return Component.Identifier.Button._6;
            }
            case 7: {
                return Component.Identifier.Button._7;
            }
            case 8: {
                return Component.Identifier.Button._8;
            }
            case 9: {
                return Component.Identifier.Button._9;
            }
            case 10: {
                return Component.Identifier.Button._10;
            }
            case 11: {
                return Component.Identifier.Button._11;
            }
            case 12: {
                return Component.Identifier.Button._12;
            }
            case 13: {
                return Component.Identifier.Button._13;
            }
            case 14: {
                return Component.Identifier.Button._14;
            }
            case 15: {
                return Component.Identifier.Button._15;
            }
            case 16: {
                return Component.Identifier.Button._16;
            }
            case 17: {
                return Component.Identifier.Button._17;
            }
            case 18: {
                return Component.Identifier.Button._18;
            }
            case 19: {
                return Component.Identifier.Button._19;
            }
            case 20: {
                return Component.Identifier.Button._20;
            }
            case 21: {
                return Component.Identifier.Button._21;
            }
            case 22: {
                return Component.Identifier.Button._22;
            }
            case 23: {
                return Component.Identifier.Button._23;
            }
            case 24: {
                return Component.Identifier.Button._24;
            }
            case 25: {
                return Component.Identifier.Button._25;
            }
            case 26: {
                return Component.Identifier.Button._26;
            }
            case 27: {
                return Component.Identifier.Button._27;
            }
            case 28: {
                return Component.Identifier.Button._28;
            }
            case 29: {
                return Component.Identifier.Button._29;
            }
            case 30: {
                return Component.Identifier.Button._30;
            }
            case 31: {
                return Component.Identifier.Button._31;
            }
            default: {
                return null;
            }
        }
    }
    
    private static final Controller createJoystickFromJoystickDevice(final LinuxJoystickDevice device) {
        final List components = new ArrayList();
        final byte[] axisMap = device.getAxisMap();
        final char[] buttonMap = device.getButtonMap();
        final LinuxJoystickAxis[] hatBits = new LinuxJoystickAxis[6];
        for (int i = 0; i < device.getNumButtons(); ++i) {
            final Component.Identifier button_id = LinuxNativeTypesMap.getButtonID(buttonMap[i]);
            if (button_id != null) {
                final LinuxJoystickButton button = new LinuxJoystickButton(button_id);
                device.registerButton(i, button);
                components.add(button);
            }
        }
        for (int i = 0; i < device.getNumAxes(); ++i) {
            final Component.Identifier.Axis axis_id = (Component.Identifier.Axis)LinuxNativeTypesMap.getAbsAxisID(axisMap[i]);
            LinuxJoystickAxis axis = new LinuxJoystickAxis(axis_id);
            device.registerAxis(i, axis);
            if (axisMap[i] == 16) {
                hatBits[0] = axis;
            }
            else if (axisMap[i] == 17) {
                hatBits[1] = axis;
                axis = new LinuxJoystickPOV(Component.Identifier.Axis.POV, hatBits[0], hatBits[1]);
                device.registerPOV((LinuxJoystickPOV)axis);
                components.add(axis);
            }
            else if (axisMap[i] == 18) {
                hatBits[2] = axis;
            }
            else if (axisMap[i] == 19) {
                hatBits[3] = axis;
                axis = new LinuxJoystickPOV(Component.Identifier.Axis.POV, hatBits[2], hatBits[3]);
                device.registerPOV((LinuxJoystickPOV)axis);
                components.add(axis);
            }
            else if (axisMap[i] == 20) {
                hatBits[4] = axis;
            }
            else if (axisMap[i] == 21) {
                hatBits[5] = axis;
                axis = new LinuxJoystickPOV(Component.Identifier.Axis.POV, hatBits[4], hatBits[5]);
                device.registerPOV((LinuxJoystickPOV)axis);
                components.add(axis);
            }
            else {
                components.add(axis);
            }
        }
        return new LinuxJoystickAbstractController(device, components.toArray(new Component[0]), new Controller[0], new Rumbler[0]);
    }
    
    private final void enumerateJoystickControllers(final List controllers) {
        File[] joystick_device_files = enumerateJoystickDeviceFiles("/dev/input");
        if (joystick_device_files == null || joystick_device_files.length == 0) {
            joystick_device_files = enumerateJoystickDeviceFiles("/dev");
            if (joystick_device_files == null) {
                return;
            }
        }
        for (int i = 0; i < joystick_device_files.length; ++i) {
            final File event_file = joystick_device_files[i];
            try {
                final String path = getAbsolutePathPrivileged(event_file);
                final LinuxJoystickDevice device = new LinuxJoystickDevice(path);
                final Controller controller = createJoystickFromJoystickDevice(device);
                if (controller != null) {
                    controllers.add(controller);
                    this.devices.add(device);
                }
                else {
                    device.close();
                }
            }
            catch (IOException e) {
                ControllerEnvironment.logln("Failed to open device (" + event_file + "): " + e.getMessage());
            }
        }
    }
    
    private static final File[] enumerateJoystickDeviceFiles(final String dev_path) {
        final File dev = new File(dev_path);
        return listFilesPrivileged(dev, new FilenameFilter() {
            public final boolean accept(final File dir, final String name) {
                return name.startsWith("js");
            }
        });
    }
    
    private static String getAbsolutePathPrivileged(final File file) {
        return AccessController.doPrivileged((PrivilegedAction<String>)new PrivilegedAction() {
            public Object run() {
                return file.getAbsolutePath();
            }
        });
    }
    
    private static File[] listFilesPrivileged(final File dir, final FilenameFilter filter) {
        return AccessController.doPrivileged((PrivilegedAction<File[]>)new PrivilegedAction() {
            public Object run() {
                final File[] files = dir.listFiles(filter);
                Arrays.sort(files, new Comparator() {
                    public int compare(final Object f1, final Object f2) {
                        return ((File)f1).getName().compareTo(((File)f2).getName());
                    }
                });
                return files;
            }
        });
    }
    
    private final void enumerateEventControllers(final List controllers) {
        final File dev = new File("/dev/input");
        final File[] event_device_files = listFilesPrivileged(dev, new FilenameFilter() {
            public final boolean accept(final File dir, final String name) {
                return name.startsWith("event");
            }
        });
        if (event_device_files == null) {
            return;
        }
        for (int i = 0; i < event_device_files.length; ++i) {
            final File event_file = event_device_files[i];
            try {
                final String path = getAbsolutePathPrivileged(event_file);
                final LinuxEventDevice device = new LinuxEventDevice(path);
                try {
                    final Controller controller = createControllerFromDevice(device);
                    if (controller != null) {
                        controllers.add(controller);
                        this.devices.add(device);
                    }
                    else {
                        device.close();
                    }
                }
                catch (IOException e) {
                    ControllerEnvironment.logln("Failed to create Controller: " + e.getMessage());
                    device.close();
                }
            }
            catch (IOException e2) {
                ControllerEnvironment.logln("Failed to open device (" + event_file + "): " + e2.getMessage());
            }
        }
    }
    
    public boolean isSupported() {
        return LinuxEnvironmentPlugin.supported;
    }
    
    static {
        LinuxEnvironmentPlugin.supported = false;
        device_thread = new LinuxDeviceThread();
        final String osName = getPrivilegedProperty("os.name", "").trim();
        if (osName.equals("Linux")) {
            LinuxEnvironmentPlugin.supported = true;
            if ("i386".equals(getPrivilegedProperty("os.arch"))) {
                loadLibrary("jinput-linux");
            }
            else {
                loadLibrary("jinput-linux64");
            }
        }
    }
    
    private final class ShutdownHook extends Thread
    {
        public final void run() {
            for (int i = 0; i < LinuxEnvironmentPlugin.this.devices.size(); ++i) {
                try {
                    final LinuxDevice device = LinuxEnvironmentPlugin.this.devices.get(i);
                    device.close();
                }
                catch (IOException e) {
                    ControllerEnvironment.logln("Failed to close device: " + e.getMessage());
                }
            }
        }
    }
}
