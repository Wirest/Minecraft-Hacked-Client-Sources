// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.input;

import java.util.HashMap;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.Display;
import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.InputImplementation;
import java.util.Map;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;

public class Mouse
{
    public static final int EVENT_SIZE = 22;
    private static boolean created;
    private static ByteBuffer buttons;
    private static int x;
    private static int y;
    private static int absolute_x;
    private static int absolute_y;
    private static IntBuffer coord_buffer;
    private static int dx;
    private static int dy;
    private static int dwheel;
    private static int buttonCount;
    private static boolean hasWheel;
    private static Cursor currentCursor;
    private static String[] buttonName;
    private static final Map<String, Integer> buttonMap;
    private static boolean initialized;
    private static ByteBuffer readBuffer;
    private static int eventButton;
    private static boolean eventState;
    private static int event_dx;
    private static int event_dy;
    private static int event_dwheel;
    private static int event_x;
    private static int event_y;
    private static long event_nanos;
    private static int grab_x;
    private static int grab_y;
    private static int last_event_raw_x;
    private static int last_event_raw_y;
    private static final int BUFFER_SIZE = 50;
    private static boolean isGrabbed;
    private static InputImplementation implementation;
    private static final boolean emulateCursorAnimation;
    private static boolean clipMouseCoordinatesToWindow;
    
    private Mouse() {
    }
    
    public static Cursor getNativeCursor() {
        synchronized (OpenGLPackageAccess.global_lock) {
            return Mouse.currentCursor;
        }
    }
    
    public static Cursor setNativeCursor(final Cursor cursor) throws LWJGLException {
        synchronized (OpenGLPackageAccess.global_lock) {
            if ((Cursor.getCapabilities() & 0x1) == 0x0) {
                throw new IllegalStateException("Mouse doesn't support native cursors");
            }
            final Cursor oldCursor = Mouse.currentCursor;
            Mouse.currentCursor = cursor;
            if (isCreated()) {
                if (Mouse.currentCursor != null) {
                    Mouse.implementation.setNativeCursor(Mouse.currentCursor.getHandle());
                    Mouse.currentCursor.setTimeout();
                }
                else {
                    Mouse.implementation.setNativeCursor(null);
                }
            }
            return oldCursor;
        }
    }
    
    public static boolean isClipMouseCoordinatesToWindow() {
        return Mouse.clipMouseCoordinatesToWindow;
    }
    
    public static void setClipMouseCoordinatesToWindow(final boolean clip) {
        Mouse.clipMouseCoordinatesToWindow = clip;
    }
    
    public static void setCursorPosition(final int new_x, final int new_y) {
        synchronized (OpenGLPackageAccess.global_lock) {
            if (!isCreated()) {
                throw new IllegalStateException("Mouse is not created");
            }
            Mouse.event_x = new_x;
            Mouse.x = new_x;
            Mouse.event_y = new_y;
            Mouse.y = new_y;
            if (!isGrabbed() && (Cursor.getCapabilities() & 0x1) != 0x0) {
                Mouse.implementation.setCursorPosition(Mouse.x, Mouse.y);
            }
            else {
                Mouse.grab_x = new_x;
                Mouse.grab_y = new_y;
            }
        }
    }
    
    private static void initialize() {
        Sys.initialize();
        Mouse.buttonName = new String[16];
        for (int i = 0; i < 16; ++i) {
            Mouse.buttonName[i] = "BUTTON" + i;
            Mouse.buttonMap.put(Mouse.buttonName[i], i);
        }
        Mouse.initialized = true;
    }
    
    private static void resetMouse() {
        Mouse.dx = (Mouse.dy = (Mouse.dwheel = 0));
        Mouse.readBuffer.position(Mouse.readBuffer.limit());
    }
    
    static InputImplementation getImplementation() {
        return Mouse.implementation;
    }
    
    private static void create(final InputImplementation impl) throws LWJGLException {
        if (Mouse.created) {
            return;
        }
        if (!Mouse.initialized) {
            initialize();
        }
        (Mouse.implementation = impl).createMouse();
        Mouse.hasWheel = Mouse.implementation.hasWheel();
        Mouse.created = true;
        Mouse.buttonCount = Mouse.implementation.getButtonCount();
        Mouse.buttons = BufferUtils.createByteBuffer(Mouse.buttonCount);
        Mouse.coord_buffer = BufferUtils.createIntBuffer(3);
        if (Mouse.currentCursor != null && Mouse.implementation.getNativeCursorCapabilities() != 0) {
            setNativeCursor(Mouse.currentCursor);
        }
        (Mouse.readBuffer = ByteBuffer.allocate(1100)).limit(0);
        setGrabbed(Mouse.isGrabbed);
    }
    
    public static void create() throws LWJGLException {
        synchronized (OpenGLPackageAccess.global_lock) {
            if (!Display.isCreated()) {
                throw new IllegalStateException("Display must be created.");
            }
            create(OpenGLPackageAccess.createImplementation());
        }
    }
    
    public static boolean isCreated() {
        synchronized (OpenGLPackageAccess.global_lock) {
            return Mouse.created;
        }
    }
    
    public static void destroy() {
        synchronized (OpenGLPackageAccess.global_lock) {
            if (!Mouse.created) {
                return;
            }
            Mouse.created = false;
            Mouse.buttons = null;
            Mouse.coord_buffer = null;
            Mouse.implementation.destroyMouse();
        }
    }
    
    public static void poll() {
        synchronized (OpenGLPackageAccess.global_lock) {
            if (!Mouse.created) {
                throw new IllegalStateException("Mouse must be created before you can poll it");
            }
            Mouse.implementation.pollMouse(Mouse.coord_buffer, Mouse.buttons);
            final int poll_coord1 = Mouse.coord_buffer.get(0);
            final int poll_coord2 = Mouse.coord_buffer.get(1);
            final int poll_dwheel = Mouse.coord_buffer.get(2);
            if (isGrabbed()) {
                Mouse.dx += poll_coord1;
                Mouse.dy += poll_coord2;
                Mouse.x += poll_coord1;
                Mouse.y += poll_coord2;
                Mouse.absolute_x += poll_coord1;
                Mouse.absolute_y += poll_coord2;
            }
            else {
                Mouse.dx = poll_coord1 - Mouse.absolute_x;
                Mouse.dy = poll_coord2 - Mouse.absolute_y;
                Mouse.absolute_x = (Mouse.x = poll_coord1);
                Mouse.absolute_y = (Mouse.y = poll_coord2);
            }
            if (Mouse.clipMouseCoordinatesToWindow) {
                Mouse.x = Math.min(Display.getWidth() - 1, Math.max(0, Mouse.x));
                Mouse.y = Math.min(Display.getHeight() - 1, Math.max(0, Mouse.y));
            }
            Mouse.dwheel += poll_dwheel;
            read();
        }
    }
    
    private static void read() {
        Mouse.readBuffer.compact();
        Mouse.implementation.readMouse(Mouse.readBuffer);
        Mouse.readBuffer.flip();
    }
    
    public static boolean isButtonDown(final int button) {
        synchronized (OpenGLPackageAccess.global_lock) {
            if (!Mouse.created) {
                throw new IllegalStateException("Mouse must be created before you can poll the button state");
            }
            return button < Mouse.buttonCount && button >= 0 && Mouse.buttons.get(button) == 1;
        }
    }
    
    public static String getButtonName(final int button) {
        synchronized (OpenGLPackageAccess.global_lock) {
            if (button >= Mouse.buttonName.length || button < 0) {
                return null;
            }
            return Mouse.buttonName[button];
        }
    }
    
    public static int getButtonIndex(final String buttonName) {
        synchronized (OpenGLPackageAccess.global_lock) {
            final Integer ret = Mouse.buttonMap.get(buttonName);
            if (ret == null) {
                return -1;
            }
            return ret;
        }
    }
    
    public static boolean next() {
        synchronized (OpenGLPackageAccess.global_lock) {
            if (!Mouse.created) {
                throw new IllegalStateException("Mouse must be created before you can read events");
            }
            if (Mouse.readBuffer.hasRemaining()) {
                Mouse.eventButton = Mouse.readBuffer.get();
                Mouse.eventState = (Mouse.readBuffer.get() != 0);
                if (isGrabbed()) {
                    Mouse.event_dx = Mouse.readBuffer.getInt();
                    Mouse.event_dy = Mouse.readBuffer.getInt();
                    Mouse.event_x += Mouse.event_dx;
                    Mouse.event_y += Mouse.event_dy;
                    Mouse.last_event_raw_x = Mouse.event_x;
                    Mouse.last_event_raw_y = Mouse.event_y;
                }
                else {
                    final int new_event_x = Mouse.readBuffer.getInt();
                    final int new_event_y = Mouse.readBuffer.getInt();
                    Mouse.event_dx = new_event_x - Mouse.last_event_raw_x;
                    Mouse.event_dy = new_event_y - Mouse.last_event_raw_y;
                    Mouse.event_x = new_event_x;
                    Mouse.event_y = new_event_y;
                    Mouse.last_event_raw_x = new_event_x;
                    Mouse.last_event_raw_y = new_event_y;
                }
                if (Mouse.clipMouseCoordinatesToWindow) {
                    Mouse.event_x = Math.min(Display.getWidth() - 1, Math.max(0, Mouse.event_x));
                    Mouse.event_y = Math.min(Display.getHeight() - 1, Math.max(0, Mouse.event_y));
                }
                Mouse.event_dwheel = Mouse.readBuffer.getInt();
                Mouse.event_nanos = Mouse.readBuffer.getLong();
                return true;
            }
            return false;
        }
    }
    
    public static int getEventButton() {
        synchronized (OpenGLPackageAccess.global_lock) {
            return Mouse.eventButton;
        }
    }
    
    public static boolean getEventButtonState() {
        synchronized (OpenGLPackageAccess.global_lock) {
            return Mouse.eventState;
        }
    }
    
    public static int getEventDX() {
        synchronized (OpenGLPackageAccess.global_lock) {
            return Mouse.event_dx;
        }
    }
    
    public static int getEventDY() {
        synchronized (OpenGLPackageAccess.global_lock) {
            return Mouse.event_dy;
        }
    }
    
    public static int getEventX() {
        synchronized (OpenGLPackageAccess.global_lock) {
            return Mouse.event_x;
        }
    }
    
    public static int getEventY() {
        synchronized (OpenGLPackageAccess.global_lock) {
            return Mouse.event_y;
        }
    }
    
    public static int getEventDWheel() {
        synchronized (OpenGLPackageAccess.global_lock) {
            return Mouse.event_dwheel;
        }
    }
    
    public static long getEventNanoseconds() {
        synchronized (OpenGLPackageAccess.global_lock) {
            return Mouse.event_nanos;
        }
    }
    
    public static int getX() {
        synchronized (OpenGLPackageAccess.global_lock) {
            return Mouse.x;
        }
    }
    
    public static int getY() {
        synchronized (OpenGLPackageAccess.global_lock) {
            return Mouse.y;
        }
    }
    
    public static int getDX() {
        synchronized (OpenGLPackageAccess.global_lock) {
            final int result = Mouse.dx;
            Mouse.dx = 0;
            return result;
        }
    }
    
    public static int getDY() {
        synchronized (OpenGLPackageAccess.global_lock) {
            final int result = Mouse.dy;
            Mouse.dy = 0;
            return result;
        }
    }
    
    public static int getDWheel() {
        synchronized (OpenGLPackageAccess.global_lock) {
            final int result = Mouse.dwheel;
            Mouse.dwheel = 0;
            return result;
        }
    }
    
    public static int getButtonCount() {
        synchronized (OpenGLPackageAccess.global_lock) {
            return Mouse.buttonCount;
        }
    }
    
    public static boolean hasWheel() {
        synchronized (OpenGLPackageAccess.global_lock) {
            return Mouse.hasWheel;
        }
    }
    
    public static boolean isGrabbed() {
        synchronized (OpenGLPackageAccess.global_lock) {
            return Mouse.isGrabbed;
        }
    }
    
    public static void setGrabbed(final boolean grab) {
        synchronized (OpenGLPackageAccess.global_lock) {
            final boolean grabbed = Mouse.isGrabbed;
            Mouse.isGrabbed = grab;
            if (isCreated()) {
                if (grab && !grabbed) {
                    Mouse.grab_x = Mouse.x;
                    Mouse.grab_y = Mouse.y;
                }
                else if (!grab && grabbed && (Cursor.getCapabilities() & 0x1) != 0x0) {
                    Mouse.implementation.setCursorPosition(Mouse.grab_x, Mouse.grab_y);
                }
                Mouse.implementation.grabMouse(grab);
                poll();
                Mouse.event_x = Mouse.x;
                Mouse.event_y = Mouse.y;
                Mouse.last_event_raw_x = Mouse.x;
                Mouse.last_event_raw_y = Mouse.y;
                resetMouse();
            }
        }
    }
    
    public static void updateCursor() {
        synchronized (OpenGLPackageAccess.global_lock) {
            if (Mouse.emulateCursorAnimation && Mouse.currentCursor != null && Mouse.currentCursor.hasTimedOut() && isInsideWindow()) {
                Mouse.currentCursor.nextCursor();
                try {
                    setNativeCursor(Mouse.currentCursor);
                }
                catch (LWJGLException e) {
                    if (LWJGLUtil.DEBUG) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    static boolean getPrivilegedBoolean(final String property_name) {
        final Boolean value = AccessController.doPrivileged((PrivilegedAction<Boolean>)new PrivilegedAction<Boolean>() {
            public Boolean run() {
                return Boolean.getBoolean(property_name);
            }
        });
        return value;
    }
    
    public static boolean isInsideWindow() {
        return Mouse.implementation.isInsideWindow();
    }
    
    static {
        Mouse.buttonCount = -1;
        buttonMap = new HashMap<String, Integer>(16);
        emulateCursorAnimation = (LWJGLUtil.getPlatform() == 3 || LWJGLUtil.getPlatform() == 2);
        Mouse.clipMouseCoordinatesToWindow = !getPrivilegedBoolean("org.lwjgl.input.Mouse.allowNegativeMouseCoords");
    }
}
