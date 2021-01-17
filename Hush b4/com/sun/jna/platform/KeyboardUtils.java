// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform;

import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.Platform;
import java.awt.HeadlessException;
import java.awt.GraphicsEnvironment;

public class KeyboardUtils
{
    static final NativeKeyboardUtils INSTANCE;
    
    public static boolean isPressed(final int keycode, final int location) {
        return KeyboardUtils.INSTANCE.isPressed(keycode, location);
    }
    
    public static boolean isPressed(final int keycode) {
        return KeyboardUtils.INSTANCE.isPressed(keycode);
    }
    
    static {
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException("KeyboardUtils requires a keyboard");
        }
        if (Platform.isWindows()) {
            INSTANCE = new W32KeyboardUtils();
        }
        else {
            if (Platform.isMac()) {
                INSTANCE = new MacKeyboardUtils();
                throw new UnsupportedOperationException("No support (yet) for " + System.getProperty("os.name"));
            }
            INSTANCE = new X11KeyboardUtils();
        }
    }
    
    private abstract static class NativeKeyboardUtils
    {
        public abstract boolean isPressed(final int p0, final int p1);
        
        public boolean isPressed(final int keycode) {
            return this.isPressed(keycode, 0);
        }
    }
    
    private static class W32KeyboardUtils extends NativeKeyboardUtils
    {
        private int toNative(final int code, final int loc) {
            if ((code >= 65 && code <= 90) || (code >= 48 && code <= 57)) {
                return code;
            }
            if (code == 16) {
                if ((loc & 0x3) != 0x0) {
                    return 161;
                }
                if ((loc & 0x2) != 0x0) {
                    return 160;
                }
                return 16;
            }
            else if (code == 17) {
                if ((loc & 0x3) != 0x0) {
                    return 163;
                }
                if ((loc & 0x2) != 0x0) {
                    return 162;
                }
                return 17;
            }
            else {
                if (code != 18) {
                    return 0;
                }
                if ((loc & 0x3) != 0x0) {
                    return 165;
                }
                if ((loc & 0x2) != 0x0) {
                    return 164;
                }
                return 18;
            }
        }
        
        @Override
        public boolean isPressed(final int keycode, final int location) {
            final User32 lib = User32.INSTANCE;
            return (lib.GetAsyncKeyState(this.toNative(keycode, location)) & 0x8000) != 0x0;
        }
    }
    
    private static class MacKeyboardUtils extends NativeKeyboardUtils
    {
        @Override
        public boolean isPressed(final int keycode, final int location) {
            return false;
        }
    }
    
    private static class X11KeyboardUtils extends NativeKeyboardUtils
    {
        private int toKeySym(final int code, final int location) {
            if (code >= 65 && code <= 90) {
                return 97 + (code - 65);
            }
            if (code >= 48 && code <= 57) {
                return 48 + (code - 48);
            }
            if (code == 16) {
                if ((location & 0x3) != 0x0) {
                    return 65505;
                }
                return 65505;
            }
            else if (code == 17) {
                if ((location & 0x3) != 0x0) {
                    return 65508;
                }
                return 65507;
            }
            else if (code == 18) {
                if ((location & 0x3) != 0x0) {
                    return 65514;
                }
                return 65513;
            }
            else {
                if (code != 157) {
                    return 0;
                }
                if ((location & 0x3) != 0x0) {
                    return 65512;
                }
                return 65511;
            }
        }
        
        @Override
        public boolean isPressed(final int keycode, final int location) {
            final X11 lib = X11.INSTANCE;
            final X11.Display dpy = lib.XOpenDisplay(null);
            if (dpy == null) {
                throw new Error("Can't open X Display");
            }
            try {
                final byte[] keys = new byte[32];
                lib.XQueryKeymap(dpy, keys);
                final int keysym = this.toKeySym(keycode, location);
                for (int code = 5; code < 256; ++code) {
                    final int idx = code / 8;
                    final int shift = code % 8;
                    if ((keys[idx] & 1 << shift) != 0x0) {
                        final int sym = lib.XKeycodeToKeysym(dpy, (byte)code, 0).intValue();
                        if (sym == keysym) {
                            return true;
                        }
                    }
                }
            }
            finally {
                lib.XCloseDisplay(dpy);
            }
            return false;
        }
    }
}
