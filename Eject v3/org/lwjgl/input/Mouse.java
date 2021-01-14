package org.lwjgl.input;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.InputImplementation;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

public class Mouse {
    public static final int EVENT_SIZE = 22;
    private static final Map<String, Integer> buttonMap = new HashMap(16);
    private static final int BUFFER_SIZE = 50;
    private static final boolean emulateCursorAnimation = (LWJGLUtil.getPlatform() == 3) || (LWJGLUtil.getPlatform() == 2);
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
    private static int buttonCount = -1;
    private static boolean hasWheel;
    private static Cursor currentCursor;
    private static String[] buttonName;
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
    private static boolean isGrabbed;
    private static InputImplementation implementation;
    private static boolean clipMouseCoordinatesToWindow = !getPrivilegedBoolean("org.lwjgl.input.Mouse.allowNegativeMouseCoords");

    public static Cursor getNativeCursor() {
        // Byte code:
        //   0: getstatic 65	org/lwjgl/input/OpenGLPackageAccess:global_lock	Ljava/lang/Object;
        //   3: dup
        //   4: astore_0
        //   5: monitorenter
        //   6: getstatic 67	org/lwjgl/input/Mouse:currentCursor	Lorg/lwjgl/input/Cursor;
        //   9: aload_0
        //   10: monitorexit
        //   11: areturn
        //   12: astore_1
        //   13: aload_0
        //   14: monitorexit
        //   15: aload_1
        //   16: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   4	10	0	Ljava/lang/Object;	Object
        //   12	4	1	localObject1	Object
        // Exception table:
        //   from	to	target	type
        //   6	11	12	finally
        //   12	15	12	finally
    }

    public static Cursor setNativeCursor(Cursor paramCursor)
            throws LWJGLException {
        synchronized (OpenGLPackageAccess.global_lock) {
            if (Cursor.getCapabilities() >> 1 == 0) {
                throw new IllegalStateException("Mouse doesn't support native cursors");
            }
            Cursor localCursor = currentCursor;
            currentCursor = paramCursor;
            if (isCreated()) {
                if (currentCursor != null) {
                    implementation.setNativeCursor(currentCursor.getHandle());
                    currentCursor.setTimeout();
                } else {
                    implementation.setNativeCursor(null);
                }
            }
            return localCursor;
        }
    }

    public static boolean isClipMouseCoordinatesToWindow() {
        return clipMouseCoordinatesToWindow;
    }

    public static void setClipMouseCoordinatesToWindow(boolean paramBoolean) {
        clipMouseCoordinatesToWindow = paramBoolean;
    }

    public static void setCursorPosition(int paramInt1, int paramInt2) {
        synchronized (OpenGLPackageAccess.global_lock) {
            if (!isCreated()) {
                throw new IllegalStateException("Mouse is not created");
            }
            x = event_x = paramInt1;
            y = event_y = paramInt2;
            if ((!isGrabbed()) && (Cursor.getCapabilities() >> 1 != 0)) {
                implementation.setCursorPosition(x, y);
            } else {
                grab_x = paramInt1;
                grab_y = paramInt2;
            }
        }
    }

    private static void initialize() {
        Sys.initialize();
        buttonName = new String[16];
        for (int i = 0; i < 16; i++) {
            buttonName[i] = ("BUTTON" + i);
            buttonMap.put(buttonName[i], Integer.valueOf(i));
        }
        initialized = true;
    }

    private static void resetMouse() {
        dx = dy = dwheel = 0;
        readBuffer.position(readBuffer.limit());
    }

    static InputImplementation getImplementation() {
        return implementation;
    }

    private static void create(InputImplementation paramInputImplementation)
            throws LWJGLException {
        if (created) {
            return;
        }
        if (!initialized) {
            initialize();
        }
        implementation = paramInputImplementation;
        implementation.createMouse();
        hasWheel = implementation.hasWheel();
        created = true;
        buttonCount = implementation.getButtonCount();
        buttons = BufferUtils.createByteBuffer(buttonCount);
        coord_buffer = BufferUtils.createIntBuffer(3);
        if ((currentCursor != null) && (implementation.getNativeCursorCapabilities() != 0)) {
            setNativeCursor(currentCursor);
        }
        readBuffer = ByteBuffer.allocate(1100);
        readBuffer.limit(0);
        setGrabbed(isGrabbed);
    }

    public static void create()
            throws LWJGLException {
        synchronized (OpenGLPackageAccess.global_lock) {
            if (!Display.isCreated()) {
                throw new IllegalStateException("Display must be created.");
            }
            create(OpenGLPackageAccess.createImplementation());
        }
    }

    public static boolean isCreated() {
        // Byte code:
        //   0: getstatic 65	org/lwjgl/input/OpenGLPackageAccess:global_lock	Ljava/lang/Object;
        //   3: dup
        //   4: astore_0
        //   5: monitorenter
        //   6: getstatic 192	org/lwjgl/input/Mouse:created	Z
        //   9: aload_0
        //   10: monitorexit
        //   11: ireturn
        //   12: astore_1
        //   13: aload_0
        //   14: monitorexit
        //   15: aload_1
        //   16: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   4	10	0	Ljava/lang/Object;	Object
        //   12	4	1	localObject1	Object
        // Exception table:
        //   from	to	target	type
        //   6	11	12	finally
        //   12	15	12	finally
    }

    public static void destroy() {
        synchronized (OpenGLPackageAccess.global_lock) {
            if (!created) {
                return;
            }
            created = false;
            buttons = null;
            coord_buffer = null;
            implementation.destroyMouse();
        }
    }

    public static void poll() {
        synchronized (OpenGLPackageAccess.global_lock) {
            if (!created) {
                throw new IllegalStateException("Mouse must be created before you can poll it");
            }
            implementation.pollMouse(coord_buffer, buttons);
            int i = coord_buffer.get(0);
            int j = coord_buffer.get(1);
            int k = coord_buffer.get(2);
            if (isGrabbed()) {
                dx |= i;
                dy |= j;
                x |= i;
                y |= j;
                absolute_x |= i;
                absolute_y |= j;
            } else {
                dx = i - absolute_x;
                dy = j - absolute_y;
                absolute_x = x = i;
                absolute_y = y = j;
            }
            if (clipMouseCoordinatesToWindow) {
                x = Math.min(Display.getWidth() - 1, Math.max(0, x));
                y = Math.min(Display.getHeight() - 1, Math.max(0, y));
            }
            dwheel |= k;
            read();
        }
    }

    private static void read() {
        readBuffer.compact();
        implementation.readMouse(readBuffer);
        readBuffer.flip();
    }

    public static boolean isButtonDown(int paramInt) {
        synchronized (OpenGLPackageAccess.global_lock) {
            if (!created) {
                throw new IllegalStateException("Mouse must be created before you can poll the button state");
            }
            if ((paramInt >= buttonCount) || (paramInt < 0)) {
                return false;
            }
            return buttons.get(paramInt) == 1;
        }
    }

    public static String getButtonName(int paramInt) {
        synchronized (OpenGLPackageAccess.global_lock) {
            if ((paramInt >= buttonName.length) || (paramInt < 0)) {
                return null;
            }
            return buttonName[paramInt];
        }
    }

    public static int getButtonIndex(String paramString) {
        synchronized (OpenGLPackageAccess.global_lock) {
            Integer localInteger = (Integer) buttonMap.get(paramString);
            if (localInteger == null) {
                return -1;
            }
            return localInteger.intValue();
        }
    }

    public static boolean next() {
        synchronized (OpenGLPackageAccess.global_lock) {
            if (!created) {
                throw new IllegalStateException("Mouse must be created before you can read events");
            }
            if (readBuffer.hasRemaining()) {
                eventButton = readBuffer.get();
                eventState = readBuffer.get() != 0;
                if (isGrabbed()) {
                    event_dx = readBuffer.getInt();
                    event_dy = readBuffer.getInt();
                    event_x |= event_dx;
                    event_y |= event_dy;
                    last_event_raw_x = event_x;
                    last_event_raw_y = event_y;
                } else {
                    int i = readBuffer.getInt();
                    int j = readBuffer.getInt();
                    event_dx = i - last_event_raw_x;
                    event_dy = j - last_event_raw_y;
                    event_x = i;
                    event_y = j;
                    last_event_raw_x = i;
                    last_event_raw_y = j;
                }
                if (clipMouseCoordinatesToWindow) {
                    event_x = Math.min(Display.getWidth() - 1, Math.max(0, event_x));
                    event_y = Math.min(Display.getHeight() - 1, Math.max(0, event_y));
                }
                event_dwheel = readBuffer.getInt();
                event_nanos = readBuffer.getLong();
                return true;
            }
            return false;
        }
    }

    public static int getEventButton() {
        // Byte code:
        //   0: getstatic 65	org/lwjgl/input/OpenGLPackageAccess:global_lock	Ljava/lang/Object;
        //   3: dup
        //   4: astore_0
        //   5: monitorenter
        //   6: getstatic 323	org/lwjgl/input/Mouse:eventButton	I
        //   9: aload_0
        //   10: monitorexit
        //   11: ireturn
        //   12: astore_1
        //   13: aload_0
        //   14: monitorexit
        //   15: aload_1
        //   16: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   4	10	0	Ljava/lang/Object;	Object
        //   12	4	1	localObject1	Object
        // Exception table:
        //   from	to	target	type
        //   6	11	12	finally
        //   12	15	12	finally
    }

    public static boolean getEventButtonState() {
        // Byte code:
        //   0: getstatic 65	org/lwjgl/input/OpenGLPackageAccess:global_lock	Ljava/lang/Object;
        //   3: dup
        //   4: astore_0
        //   5: monitorenter
        //   6: getstatic 325	org/lwjgl/input/Mouse:eventState	Z
        //   9: aload_0
        //   10: monitorexit
        //   11: ireturn
        //   12: astore_1
        //   13: aload_0
        //   14: monitorexit
        //   15: aload_1
        //   16: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   4	10	0	Ljava/lang/Object;	Object
        //   12	4	1	localObject1	Object
        // Exception table:
        //   from	to	target	type
        //   6	11	12	finally
        //   12	15	12	finally
    }

    public static int getEventDX() {
        // Byte code:
        //   0: getstatic 65	org/lwjgl/input/OpenGLPackageAccess:global_lock	Ljava/lang/Object;
        //   3: dup
        //   4: astore_0
        //   5: monitorenter
        //   6: getstatic 330	org/lwjgl/input/Mouse:event_dx	I
        //   9: aload_0
        //   10: monitorexit
        //   11: ireturn
        //   12: astore_1
        //   13: aload_0
        //   14: monitorexit
        //   15: aload_1
        //   16: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   4	10	0	Ljava/lang/Object;	Object
        //   12	4	1	localObject1	Object
        // Exception table:
        //   from	to	target	type
        //   6	11	12	finally
        //   12	15	12	finally
    }

    public static int getEventDY() {
        // Byte code:
        //   0: getstatic 65	org/lwjgl/input/OpenGLPackageAccess:global_lock	Ljava/lang/Object;
        //   3: dup
        //   4: astore_0
        //   5: monitorenter
        //   6: getstatic 332	org/lwjgl/input/Mouse:event_dy	I
        //   9: aload_0
        //   10: monitorexit
        //   11: ireturn
        //   12: astore_1
        //   13: aload_0
        //   14: monitorexit
        //   15: aload_1
        //   16: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   4	10	0	Ljava/lang/Object;	Object
        //   12	4	1	localObject1	Object
        // Exception table:
        //   from	to	target	type
        //   6	11	12	finally
        //   12	15	12	finally
    }

    public static int getEventX() {
        // Byte code:
        //   0: getstatic 65	org/lwjgl/input/OpenGLPackageAccess:global_lock	Ljava/lang/Object;
        //   3: dup
        //   4: astore_0
        //   5: monitorenter
        //   6: getstatic 113	org/lwjgl/input/Mouse:event_x	I
        //   9: aload_0
        //   10: monitorexit
        //   11: ireturn
        //   12: astore_1
        //   13: aload_0
        //   14: monitorexit
        //   15: aload_1
        //   16: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   4	10	0	Ljava/lang/Object;	Object
        //   12	4	1	localObject1	Object
        // Exception table:
        //   from	to	target	type
        //   6	11	12	finally
        //   12	15	12	finally
    }

    public static int getEventY() {
        // Byte code:
        //   0: getstatic 65	org/lwjgl/input/OpenGLPackageAccess:global_lock	Ljava/lang/Object;
        //   3: dup
        //   4: astore_0
        //   5: monitorenter
        //   6: getstatic 117	org/lwjgl/input/Mouse:event_y	I
        //   9: aload_0
        //   10: monitorexit
        //   11: ireturn
        //   12: astore_1
        //   13: aload_0
        //   14: monitorexit
        //   15: aload_1
        //   16: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   4	10	0	Ljava/lang/Object;	Object
        //   12	4	1	localObject1	Object
        // Exception table:
        //   from	to	target	type
        //   6	11	12	finally
        //   12	15	12	finally
    }

    public static int getEventDWheel() {
        // Byte code:
        //   0: getstatic 65	org/lwjgl/input/OpenGLPackageAccess:global_lock	Ljava/lang/Object;
        //   3: dup
        //   4: astore_0
        //   5: monitorenter
        //   6: getstatic 338	org/lwjgl/input/Mouse:event_dwheel	I
        //   9: aload_0
        //   10: monitorexit
        //   11: ireturn
        //   12: astore_1
        //   13: aload_0
        //   14: monitorexit
        //   15: aload_1
        //   16: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   4	10	0	Ljava/lang/Object;	Object
        //   12	4	1	localObject1	Object
        // Exception table:
        //   from	to	target	type
        //   6	11	12	finally
        //   12	15	12	finally
    }

    public static long getEventNanoseconds() {
        // Byte code:
        //   0: getstatic 65	org/lwjgl/input/OpenGLPackageAccess:global_lock	Ljava/lang/Object;
        //   3: dup
        //   4: astore_0
        //   5: monitorenter
        //   6: getstatic 344	org/lwjgl/input/Mouse:event_nanos	J
        //   9: aload_0
        //   10: monitorexit
        //   11: lreturn
        //   12: astore_1
        //   13: aload_0
        //   14: monitorexit
        //   15: aload_1
        //   16: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   4	10	0	Ljava/lang/Object;	Object
        //   12	4	1	localObject1	Object
        // Exception table:
        //   from	to	target	type
        //   6	11	12	finally
        //   12	15	12	finally
    }

    public static int getX() {
        // Byte code:
        //   0: getstatic 65	org/lwjgl/input/OpenGLPackageAccess:global_lock	Ljava/lang/Object;
        //   3: dup
        //   4: astore_0
        //   5: monitorenter
        //   6: getstatic 115	org/lwjgl/input/Mouse:x	I
        //   9: aload_0
        //   10: monitorexit
        //   11: ireturn
        //   12: astore_1
        //   13: aload_0
        //   14: monitorexit
        //   15: aload_1
        //   16: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   4	10	0	Ljava/lang/Object;	Object
        //   12	4	1	localObject1	Object
        // Exception table:
        //   from	to	target	type
        //   6	11	12	finally
        //   12	15	12	finally
    }

    public static int getY() {
        // Byte code:
        //   0: getstatic 65	org/lwjgl/input/OpenGLPackageAccess:global_lock	Ljava/lang/Object;
        //   3: dup
        //   4: astore_0
        //   5: monitorenter
        //   6: getstatic 119	org/lwjgl/input/Mouse:y	I
        //   9: aload_0
        //   10: monitorexit
        //   11: ireturn
        //   12: astore_1
        //   13: aload_0
        //   14: monitorexit
        //   15: aload_1
        //   16: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   4	10	0	Ljava/lang/Object;	Object
        //   12	4	1	localObject1	Object
        // Exception table:
        //   from	to	target	type
        //   6	11	12	finally
        //   12	15	12	finally
    }

    public static int getDX() {
        synchronized (OpenGLPackageAccess.global_lock) {
            int i = dx;
            dx = 0;
            return i;
        }
    }

    public static int getDY() {
        synchronized (OpenGLPackageAccess.global_lock) {
            int i = dy;
            dy = 0;
            return i;
        }
    }

    public static int getDWheel() {
        synchronized (OpenGLPackageAccess.global_lock) {
            int i = dwheel;
            dwheel = 0;
            return i;
        }
    }

    public static int getButtonCount() {
        // Byte code:
        //   0: getstatic 65	org/lwjgl/input/OpenGLPackageAccess:global_lock	Ljava/lang/Object;
        //   3: dup
        //   4: astore_0
        //   5: monitorenter
        //   6: getstatic 205	org/lwjgl/input/Mouse:buttonCount	I
        //   9: aload_0
        //   10: monitorexit
        //   11: ireturn
        //   12: astore_1
        //   13: aload_0
        //   14: monitorexit
        //   15: aload_1
        //   16: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   4	10	0	Ljava/lang/Object;	Object
        //   12	4	1	localObject1	Object
        // Exception table:
        //   from	to	target	type
        //   6	11	12	finally
        //   12	15	12	finally
    }

    public static boolean hasWheel() {
        // Byte code:
        //   0: getstatic 65	org/lwjgl/input/OpenGLPackageAccess:global_lock	Ljava/lang/Object;
        //   3: dup
        //   4: astore_0
        //   5: monitorenter
        //   6: getstatic 200	org/lwjgl/input/Mouse:hasWheel	Z
        //   9: aload_0
        //   10: monitorexit
        //   11: ireturn
        //   12: astore_1
        //   13: aload_0
        //   14: monitorexit
        //   15: aload_1
        //   16: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   4	10	0	Ljava/lang/Object;	Object
        //   12	4	1	localObject1	Object
        // Exception table:
        //   from	to	target	type
        //   6	11	12	finally
        //   12	15	12	finally
    }

    public static boolean isGrabbed() {
        // Byte code:
        //   0: getstatic 65	org/lwjgl/input/OpenGLPackageAccess:global_lock	Ljava/lang/Object;
        //   3: dup
        //   4: astore_0
        //   5: monitorenter
        //   6: getstatic 231	org/lwjgl/input/Mouse:isGrabbed	Z
        //   9: aload_0
        //   10: monitorexit
        //   11: ireturn
        //   12: astore_1
        //   13: aload_0
        //   14: monitorexit
        //   15: aload_1
        //   16: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   4	10	0	Ljava/lang/Object;	Object
        //   12	4	1	localObject1	Object
        // Exception table:
        //   from	to	target	type
        //   6	11	12	finally
        //   12	15	12	finally
    }

    public static void setGrabbed(boolean paramBoolean) {
        synchronized (OpenGLPackageAccess.global_lock) {
            boolean bool = isGrabbed;
            isGrabbed = paramBoolean;
            if (isCreated()) {
                if ((paramBoolean) && (!bool)) {
                    grab_x = x;
                    grab_y = y;
                } else if ((!paramBoolean) && (bool) && (Cursor.getCapabilities() >> 1 != 0)) {
                    implementation.setCursorPosition(grab_x, grab_y);
                }
                implementation.grabMouse(paramBoolean);
                poll();
                event_x = x;
                event_y = y;
                last_event_raw_x = x;
                last_event_raw_y = y;
                resetMouse();
            }
        }
    }

    public static void updateCursor() {
        synchronized (OpenGLPackageAccess.global_lock) {
            if ((emulateCursorAnimation) && (currentCursor != null) && (currentCursor.hasTimedOut()) && (isInsideWindow())) {
                currentCursor.nextCursor();
                try {
                    setNativeCursor(currentCursor);
                } catch (LWJGLException localLWJGLException) {
                    if (LWJGLUtil.DEBUG) {
                        localLWJGLException.printStackTrace();
                    }
                }
            }
        }
    }

    static boolean getPrivilegedBoolean(String paramString) {
        Boolean localBoolean = (Boolean) AccessController.doPrivileged(new PrivilegedAction() {
            public Boolean run() {
                return Boolean.valueOf(Boolean.getBoolean(this.val$property_name));
            }
        });
        return localBoolean.booleanValue();
    }

    public static boolean isInsideWindow() {
        return implementation.isInsideWindow();
    }
}




