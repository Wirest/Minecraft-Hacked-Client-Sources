// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.util.Iterator;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.IOException;
import java.util.List;
import java.nio.ByteOrder;
import org.lwjgl.BufferUtils;
import java.nio.IntBuffer;
import org.lwjgl.MemoryUtil;
import java.nio.FloatBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.lwjgl.opengles.PixelFormat;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.LWJGLException;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.Canvas;
import java.nio.ByteBuffer;

final class LinuxDisplay implements DisplayImplementation
{
    public static final int CurrentTime = 0;
    public static final int GrabSuccess = 0;
    public static final int AutoRepeatModeOff = 0;
    public static final int AutoRepeatModeOn = 1;
    public static final int AutoRepeatModeDefault = 2;
    public static final int None = 0;
    private static final int KeyPressMask = 1;
    private static final int KeyReleaseMask = 2;
    private static final int ButtonPressMask = 4;
    private static final int ButtonReleaseMask = 8;
    private static final int NotifyAncestor = 0;
    private static final int NotifyNonlinear = 3;
    private static final int NotifyPointer = 5;
    private static final int NotifyPointerRoot = 6;
    private static final int NotifyDetailNone = 7;
    private static final int SetModeInsert = 0;
    private static final int SaveSetRoot = 1;
    private static final int SaveSetUnmap = 1;
    private static final int X_SetInputFocus = 42;
    private static final int FULLSCREEN_LEGACY = 1;
    private static final int FULLSCREEN_NETWM = 2;
    private static final int WINDOWED = 3;
    private static int current_window_mode;
    private static final int XRANDR = 10;
    private static final int XF86VIDMODE = 11;
    private static final int NONE = 12;
    private static long display;
    private static long current_window;
    private static long saved_error_handler;
    private static int display_connection_usage_count;
    private final LinuxEvent event_buffer;
    private final LinuxEvent tmp_event_buffer;
    private int current_displaymode_extension;
    private long delete_atom;
    private PeerInfo peer_info;
    private ByteBuffer saved_gamma;
    private ByteBuffer current_gamma;
    private DisplayMode saved_mode;
    private DisplayMode current_mode;
    private boolean keyboard_grabbed;
    private boolean pointer_grabbed;
    private boolean input_released;
    private boolean grab;
    private boolean focused;
    private boolean minimized;
    private boolean dirty;
    private boolean close_requested;
    private long current_cursor;
    private long blank_cursor;
    private boolean mouseInside;
    private boolean resizable;
    private boolean resized;
    private int window_x;
    private int window_y;
    private int window_width;
    private int window_height;
    private Canvas parent;
    private long parent_window;
    private static boolean xembedded;
    private long parent_proxy_focus_window;
    private boolean parent_focused;
    private boolean parent_focus_changed;
    private long last_window_focus;
    private LinuxKeyboard keyboard;
    private LinuxMouse mouse;
    private String wm_class;
    private final FocusListener focus_listener;
    
    LinuxDisplay() {
        this.event_buffer = new LinuxEvent();
        this.tmp_event_buffer = new LinuxEvent();
        this.current_displaymode_extension = 12;
        this.mouseInside = true;
        this.last_window_focus = 0L;
        this.focus_listener = new FocusListener() {
            public void focusGained(final FocusEvent e) {
                synchronized (GlobalLock.lock) {
                    LinuxDisplay.this.parent_focused = true;
                    LinuxDisplay.this.parent_focus_changed = true;
                }
            }
            
            public void focusLost(final FocusEvent e) {
                synchronized (GlobalLock.lock) {
                    LinuxDisplay.this.parent_focused = false;
                    LinuxDisplay.this.parent_focus_changed = true;
                }
            }
        };
    }
    
    private static ByteBuffer getCurrentGammaRamp() throws LWJGLException {
        lockAWT();
        try {
            incDisplay();
            try {
                if (isXF86VidModeSupported()) {
                    return nGetCurrentGammaRamp(getDisplay(), getDefaultScreen());
                }
                return null;
            }
            finally {
                decDisplay();
            }
        }
        finally {
            unlockAWT();
        }
    }
    
    private static native ByteBuffer nGetCurrentGammaRamp(final long p0, final int p1) throws LWJGLException;
    
    private static int getBestDisplayModeExtension() {
        int result;
        if (isXrandrSupported()) {
            LWJGLUtil.log("Using Xrandr for display mode switching");
            result = 10;
        }
        else if (isXF86VidModeSupported()) {
            LWJGLUtil.log("Using XF86VidMode for display mode switching");
            result = 11;
        }
        else {
            LWJGLUtil.log("No display mode extensions available");
            result = 12;
        }
        return result;
    }
    
    private static boolean isXrandrSupported() {
        if (Display.getPrivilegedBoolean("LWJGL_DISABLE_XRANDR")) {
            return false;
        }
        lockAWT();
        try {
            incDisplay();
            try {
                return nIsXrandrSupported(getDisplay());
            }
            finally {
                decDisplay();
            }
        }
        catch (LWJGLException e) {
            LWJGLUtil.log("Got exception while querying Xrandr support: " + e);
            return false;
        }
        finally {
            unlockAWT();
        }
    }
    
    private static native boolean nIsXrandrSupported(final long p0) throws LWJGLException;
    
    private static boolean isXF86VidModeSupported() {
        lockAWT();
        try {
            incDisplay();
            try {
                return nIsXF86VidModeSupported(getDisplay());
            }
            finally {
                decDisplay();
            }
        }
        catch (LWJGLException e) {
            LWJGLUtil.log("Got exception while querying XF86VM support: " + e);
            return false;
        }
        finally {
            unlockAWT();
        }
    }
    
    private static native boolean nIsXF86VidModeSupported(final long p0) throws LWJGLException;
    
    private static boolean isNetWMFullscreenSupported() throws LWJGLException {
        if (Display.getPrivilegedBoolean("LWJGL_DISABLE_NETWM")) {
            return false;
        }
        lockAWT();
        try {
            incDisplay();
            try {
                return nIsNetWMFullscreenSupported(getDisplay(), getDefaultScreen());
            }
            finally {
                decDisplay();
            }
        }
        catch (LWJGLException e) {
            LWJGLUtil.log("Got exception while querying NetWM support: " + e);
            return false;
        }
        finally {
            unlockAWT();
        }
    }
    
    private static native boolean nIsNetWMFullscreenSupported(final long p0, final int p1) throws LWJGLException;
    
    static void lockAWT() {
        try {
            nLockAWT();
        }
        catch (LWJGLException e) {
            LWJGLUtil.log("Caught exception while locking AWT: " + e);
        }
    }
    
    private static native void nLockAWT() throws LWJGLException;
    
    static void unlockAWT() {
        try {
            nUnlockAWT();
        }
        catch (LWJGLException e) {
            LWJGLUtil.log("Caught exception while unlocking AWT: " + e);
        }
    }
    
    private static native void nUnlockAWT() throws LWJGLException;
    
    static void incDisplay() throws LWJGLException {
        if (LinuxDisplay.display_connection_usage_count == 0) {
            try {
                GLContext.loadOpenGLLibrary();
                org.lwjgl.opengles.GLContext.loadOpenGLLibrary();
            }
            catch (Throwable t) {}
            LinuxDisplay.saved_error_handler = setErrorHandler();
            LinuxDisplay.display = openDisplay();
        }
        ++LinuxDisplay.display_connection_usage_count;
    }
    
    private static native int callErrorHandler(final long p0, final long p1, final long p2);
    
    private static native long setErrorHandler();
    
    private static native long resetErrorHandler(final long p0);
    
    private static native void synchronize(final long p0, final boolean p1);
    
    private static int globalErrorHandler(final long display, final long event_ptr, final long error_display, final long serial, final long error_code, final long request_code, final long minor_code) throws LWJGLException {
        if (LinuxDisplay.xembedded && request_code == 42L) {
            return 0;
        }
        if (display == getDisplay()) {
            final String error_msg = getErrorText(display, error_code);
            throw new LWJGLException("X Error - disp: 0x" + Long.toHexString(error_display) + " serial: " + serial + " error: " + error_msg + " request_code: " + request_code + " minor_code: " + minor_code);
        }
        if (LinuxDisplay.saved_error_handler != 0L) {
            return callErrorHandler(LinuxDisplay.saved_error_handler, display, event_ptr);
        }
        return 0;
    }
    
    private static native String getErrorText(final long p0, final long p1);
    
    static void decDisplay() {
    }
    
    static native long openDisplay() throws LWJGLException;
    
    static native void closeDisplay(final long p0);
    
    private int getWindowMode(final boolean fullscreen) throws LWJGLException {
        if (!fullscreen) {
            return 3;
        }
        if (this.current_displaymode_extension == 10 && isNetWMFullscreenSupported()) {
            LWJGLUtil.log("Using NetWM for fullscreen window");
            return 2;
        }
        LWJGLUtil.log("Using legacy mode for fullscreen window");
        return 1;
    }
    
    static long getDisplay() {
        if (LinuxDisplay.display_connection_usage_count <= 0) {
            throw new InternalError("display_connection_usage_count = " + LinuxDisplay.display_connection_usage_count);
        }
        return LinuxDisplay.display;
    }
    
    static int getDefaultScreen() {
        return nGetDefaultScreen(getDisplay());
    }
    
    static native int nGetDefaultScreen(final long p0);
    
    static long getWindow() {
        return LinuxDisplay.current_window;
    }
    
    private void ungrabKeyboard() {
        if (this.keyboard_grabbed) {
            nUngrabKeyboard(getDisplay());
            this.keyboard_grabbed = false;
        }
    }
    
    static native int nUngrabKeyboard(final long p0);
    
    private void grabKeyboard() {
        if (!this.keyboard_grabbed) {
            final int res = nGrabKeyboard(getDisplay(), getWindow());
            if (res == 0) {
                this.keyboard_grabbed = true;
            }
        }
    }
    
    static native int nGrabKeyboard(final long p0, final long p1);
    
    private void grabPointer() {
        if (!this.pointer_grabbed) {
            final int result = nGrabPointer(getDisplay(), getWindow(), 0L);
            if (result == 0) {
                this.pointer_grabbed = true;
                if (isLegacyFullscreen()) {
                    nSetViewPort(getDisplay(), getWindow(), getDefaultScreen());
                }
            }
        }
    }
    
    static native int nGrabPointer(final long p0, final long p1, final long p2);
    
    private static native void nSetViewPort(final long p0, final long p1, final int p2);
    
    private void ungrabPointer() {
        if (this.pointer_grabbed) {
            this.pointer_grabbed = false;
            nUngrabPointer(getDisplay());
        }
    }
    
    static native int nUngrabPointer(final long p0);
    
    private static boolean isFullscreen() {
        return LinuxDisplay.current_window_mode == 1 || LinuxDisplay.current_window_mode == 2;
    }
    
    private boolean shouldGrab() {
        return !this.input_released && this.grab && this.mouse != null;
    }
    
    private void updatePointerGrab() {
        if (isFullscreen() || this.shouldGrab()) {
            this.grabPointer();
        }
        else {
            this.ungrabPointer();
        }
        this.updateCursor();
    }
    
    private void updateCursor() {
        long cursor;
        if (this.shouldGrab()) {
            cursor = this.blank_cursor;
        }
        else {
            cursor = this.current_cursor;
        }
        nDefineCursor(getDisplay(), getWindow(), cursor);
    }
    
    private static native void nDefineCursor(final long p0, final long p1, final long p2);
    
    private static boolean isLegacyFullscreen() {
        return LinuxDisplay.current_window_mode == 1;
    }
    
    private void updateKeyboardGrab() {
        if (isLegacyFullscreen()) {
            this.grabKeyboard();
        }
        else {
            this.ungrabKeyboard();
        }
    }
    
    public void createWindow(final DrawableLWJGL drawable, final DisplayMode mode, final Canvas parent, int x, int y) throws LWJGLException {
        lockAWT();
        try {
            incDisplay();
            try {
                if (drawable instanceof DrawableGLES) {
                    this.peer_info = new LinuxDisplayPeerInfo();
                }
                final ByteBuffer handle = this.peer_info.lockAndGetHandle();
                try {
                    LinuxDisplay.current_window_mode = this.getWindowMode(Display.isFullscreen());
                    if (LinuxDisplay.current_window_mode != 3) {
                        Compiz.setLegacyFullscreenSupport(true);
                    }
                    final boolean undecorated = Display.getPrivilegedBoolean("org.lwjgl.opengl.Window.undecorated") || (LinuxDisplay.current_window_mode != 3 && Display.getPrivilegedBoolean("org.lwjgl.opengl.Window.undecorated_fs"));
                    this.parent = parent;
                    this.parent_window = ((parent != null) ? getHandle(parent) : getRootWindow(getDisplay(), getDefaultScreen()));
                    this.resizable = Display.isResizable();
                    this.resized = false;
                    this.window_x = x;
                    this.window_y = y;
                    this.window_width = mode.getWidth();
                    this.window_height = mode.getHeight();
                    if (mode.isFullscreenCapable() && this.current_displaymode_extension == 10) {
                        final XRandR.Screen primaryScreen = XRandR.DisplayModetoScreen(Display.getDisplayMode());
                        x = primaryScreen.xPos;
                        y = primaryScreen.yPos;
                    }
                    LinuxDisplay.current_window = nCreateWindow(getDisplay(), getDefaultScreen(), handle, mode, LinuxDisplay.current_window_mode, x, y, undecorated, this.parent_window, this.resizable);
                    this.wm_class = Display.getPrivilegedString("LWJGL_WM_CLASS");
                    if (this.wm_class == null) {
                        this.wm_class = Display.getTitle();
                    }
                    this.setClassHint(Display.getTitle(), this.wm_class);
                    mapRaised(getDisplay(), LinuxDisplay.current_window);
                    LinuxDisplay.xembedded = (parent != null && isAncestorXEmbedded(this.parent_window));
                    this.blank_cursor = createBlankCursor();
                    this.current_cursor = 0L;
                    this.focused = false;
                    this.input_released = false;
                    this.pointer_grabbed = false;
                    this.keyboard_grabbed = false;
                    this.close_requested = false;
                    this.grab = false;
                    this.minimized = false;
                    this.dirty = true;
                    if (drawable instanceof DrawableGLES) {
                        ((DrawableGLES)drawable).initialize(LinuxDisplay.current_window, getDisplay(), 4, (PixelFormat)drawable.getPixelFormat());
                    }
                    if (parent != null) {
                        parent.addFocusListener(this.focus_listener);
                        this.parent_focused = parent.isFocusOwner();
                        this.parent_focus_changed = true;
                    }
                }
                finally {
                    this.peer_info.unlock();
                }
            }
            catch (LWJGLException e) {
                decDisplay();
                throw e;
            }
        }
        finally {
            unlockAWT();
        }
    }
    
    private static native long nCreateWindow(final long p0, final int p1, final ByteBuffer p2, final DisplayMode p3, final int p4, final int p5, final int p6, final boolean p7, final long p8, final boolean p9) throws LWJGLException;
    
    private static native long getRootWindow(final long p0, final int p1);
    
    private static native boolean hasProperty(final long p0, final long p1, final long p2);
    
    private static native long getParentWindow(final long p0, final long p1) throws LWJGLException;
    
    private static native int getChildCount(final long p0, final long p1) throws LWJGLException;
    
    private static native void mapRaised(final long p0, final long p1);
    
    private static native void reparentWindow(final long p0, final long p1, final long p2, final int p3, final int p4);
    
    private static native long nGetInputFocus(final long p0) throws LWJGLException;
    
    private static native void nSetInputFocus(final long p0, final long p1, final long p2);
    
    private static native void nSetWindowSize(final long p0, final long p1, final int p2, final int p3, final boolean p4);
    
    private static native int nGetX(final long p0, final long p1);
    
    private static native int nGetY(final long p0, final long p1);
    
    private static native int nGetWidth(final long p0, final long p1);
    
    private static native int nGetHeight(final long p0, final long p1);
    
    private static boolean isAncestorXEmbedded(final long window) throws LWJGLException {
        final long xembed_atom = internAtom("_XEMBED_INFO", true);
        if (xembed_atom != 0L) {
            for (long w = window; w != 0L; w = getParentWindow(getDisplay(), w)) {
                if (hasProperty(getDisplay(), w, xembed_atom)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static long getHandle(final Canvas parent) throws LWJGLException {
        final AWTCanvasImplementation awt_impl = AWTGLCanvas.createImplementation();
        final LinuxPeerInfo parent_peer_info = (LinuxPeerInfo)awt_impl.createPeerInfo(parent, null, null);
        final ByteBuffer parent_peer_info_handle = parent_peer_info.lockAndGetHandle();
        try {
            return parent_peer_info.getDrawable();
        }
        finally {
            parent_peer_info.unlock();
        }
    }
    
    private void updateInputGrab() {
        this.updatePointerGrab();
        this.updateKeyboardGrab();
    }
    
    public void destroyWindow() {
        lockAWT();
        try {
            if (this.parent != null) {
                this.parent.removeFocusListener(this.focus_listener);
            }
            try {
                this.setNativeCursor(null);
            }
            catch (LWJGLException e) {
                LWJGLUtil.log("Failed to reset cursor: " + e.getMessage());
            }
            nDestroyCursor(getDisplay(), this.blank_cursor);
            this.blank_cursor = 0L;
            this.ungrabKeyboard();
            nDestroyWindow(getDisplay(), getWindow());
            decDisplay();
            if (LinuxDisplay.current_window_mode != 3) {
                Compiz.setLegacyFullscreenSupport(false);
            }
        }
        finally {
            unlockAWT();
        }
    }
    
    static native void nDestroyWindow(final long p0, final long p1);
    
    public void switchDisplayMode(final DisplayMode mode) throws LWJGLException {
        lockAWT();
        try {
            this.switchDisplayModeOnTmpDisplay(mode);
            this.current_mode = mode;
        }
        finally {
            unlockAWT();
        }
    }
    
    private void switchDisplayModeOnTmpDisplay(final DisplayMode mode) throws LWJGLException {
        if (this.current_displaymode_extension == 10) {
            XRandR.setConfiguration(false, XRandR.DisplayModetoScreen(mode));
        }
        else {
            incDisplay();
            try {
                nSwitchDisplayMode(getDisplay(), getDefaultScreen(), this.current_displaymode_extension, mode);
            }
            finally {
                decDisplay();
            }
        }
    }
    
    private static native void nSwitchDisplayMode(final long p0, final int p1, final int p2, final DisplayMode p3) throws LWJGLException;
    
    private static long internAtom(final String atom_name, final boolean only_if_exists) throws LWJGLException {
        incDisplay();
        try {
            return nInternAtom(getDisplay(), atom_name, only_if_exists);
        }
        finally {
            decDisplay();
        }
    }
    
    static native long nInternAtom(final long p0, final String p1, final boolean p2);
    
    public void resetDisplayMode() {
        lockAWT();
        try {
            if (this.current_displaymode_extension == 10) {
                AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Object>() {
                    public Object run() {
                        XRandR.restoreConfiguration();
                        return null;
                    }
                });
            }
            else {
                this.switchDisplayMode(this.saved_mode);
            }
            if (isXF86VidModeSupported()) {
                this.doSetGamma(this.saved_gamma);
            }
            Compiz.setLegacyFullscreenSupport(false);
        }
        catch (LWJGLException e) {
            LWJGLUtil.log("Caught exception while resetting mode: " + e);
        }
        finally {
            unlockAWT();
        }
    }
    
    public int getGammaRampLength() {
        if (!isXF86VidModeSupported()) {
            return 0;
        }
        lockAWT();
        try {
            incDisplay();
            try {
                return nGetGammaRampLength(getDisplay(), getDefaultScreen());
            }
            catch (LWJGLException e) {
                LWJGLUtil.log("Got exception while querying gamma length: " + e);
                return 0;
            }
            finally {
                decDisplay();
            }
        }
        catch (LWJGLException e) {
            LWJGLUtil.log("Failed to get gamma ramp length: " + e);
            return 0;
        }
        finally {
            unlockAWT();
        }
    }
    
    private static native int nGetGammaRampLength(final long p0, final int p1) throws LWJGLException;
    
    public void setGammaRamp(final FloatBuffer gammaRamp) throws LWJGLException {
        if (!isXF86VidModeSupported()) {
            throw new LWJGLException("No gamma ramp support (Missing XF86VM extension)");
        }
        this.doSetGamma(convertToNativeRamp(gammaRamp));
    }
    
    private void doSetGamma(final ByteBuffer native_gamma) throws LWJGLException {
        lockAWT();
        try {
            setGammaRampOnTmpDisplay(native_gamma);
            this.current_gamma = native_gamma;
        }
        finally {
            unlockAWT();
        }
    }
    
    private static void setGammaRampOnTmpDisplay(final ByteBuffer native_gamma) throws LWJGLException {
        incDisplay();
        try {
            nSetGammaRamp(getDisplay(), getDefaultScreen(), native_gamma);
        }
        finally {
            decDisplay();
        }
    }
    
    private static native void nSetGammaRamp(final long p0, final int p1, final ByteBuffer p2) throws LWJGLException;
    
    private static ByteBuffer convertToNativeRamp(final FloatBuffer ramp) throws LWJGLException {
        return nConvertToNativeRamp(ramp, ramp.position(), ramp.remaining());
    }
    
    private static native ByteBuffer nConvertToNativeRamp(final FloatBuffer p0, final int p1, final int p2) throws LWJGLException;
    
    public String getAdapter() {
        return null;
    }
    
    public String getVersion() {
        return null;
    }
    
    public DisplayMode init() throws LWJGLException {
        lockAWT();
        try {
            Compiz.init();
            this.delete_atom = internAtom("WM_DELETE_WINDOW", false);
            this.current_displaymode_extension = getBestDisplayModeExtension();
            if (this.current_displaymode_extension == 12) {
                throw new LWJGLException("No display mode extension is available");
            }
            final DisplayMode[] modes = this.getAvailableDisplayModes();
            if (modes == null || modes.length == 0) {
                throw new LWJGLException("No modes available");
            }
            switch (this.current_displaymode_extension) {
                case 10: {
                    this.saved_mode = AccessController.doPrivileged((PrivilegedAction<DisplayMode>)new PrivilegedAction<DisplayMode>() {
                        public DisplayMode run() {
                            XRandR.saveConfiguration();
                            return XRandR.ScreentoDisplayMode(XRandR.getConfiguration());
                        }
                    });
                    break;
                }
                case 11: {
                    this.saved_mode = modes[0];
                    break;
                }
                default: {
                    throw new LWJGLException("Unknown display mode extension: " + this.current_displaymode_extension);
                }
            }
            this.current_mode = this.saved_mode;
            this.saved_gamma = getCurrentGammaRamp();
            this.current_gamma = this.saved_gamma;
            return this.saved_mode;
        }
        finally {
            unlockAWT();
        }
    }
    
    private static DisplayMode getCurrentXRandrMode() throws LWJGLException {
        lockAWT();
        try {
            incDisplay();
            try {
                return nGetCurrentXRandrMode(getDisplay(), getDefaultScreen());
            }
            finally {
                decDisplay();
            }
        }
        finally {
            unlockAWT();
        }
    }
    
    private static native DisplayMode nGetCurrentXRandrMode(final long p0, final int p1) throws LWJGLException;
    
    public void setTitle(final String title) {
        lockAWT();
        try {
            final ByteBuffer titleText = MemoryUtil.encodeUTF8(title);
            nSetTitle(getDisplay(), getWindow(), MemoryUtil.getAddress(titleText), titleText.remaining() - 1);
        }
        finally {
            unlockAWT();
        }
    }
    
    private static native void nSetTitle(final long p0, final long p1, final long p2, final int p3);
    
    private void setClassHint(final String wm_name, final String wm_class) {
        lockAWT();
        try {
            final ByteBuffer nameText = MemoryUtil.encodeUTF8(wm_name);
            final ByteBuffer classText = MemoryUtil.encodeUTF8(wm_class);
            nSetClassHint(getDisplay(), getWindow(), MemoryUtil.getAddress(nameText), MemoryUtil.getAddress(classText));
        }
        finally {
            unlockAWT();
        }
    }
    
    private static native void nSetClassHint(final long p0, final long p1, final long p2, final long p3);
    
    public boolean isCloseRequested() {
        final boolean result = this.close_requested;
        this.close_requested = false;
        return result;
    }
    
    public boolean isVisible() {
        return !this.minimized;
    }
    
    public boolean isActive() {
        return this.focused || isLegacyFullscreen();
    }
    
    public boolean isDirty() {
        final boolean result = this.dirty;
        this.dirty = false;
        return result;
    }
    
    public PeerInfo createPeerInfo(final org.lwjgl.opengl.PixelFormat pixel_format, final ContextAttribs attribs) throws LWJGLException {
        return this.peer_info = new LinuxDisplayPeerInfo(pixel_format);
    }
    
    private void relayEventToParent(final LinuxEvent event_buffer, final int event_mask) {
        this.tmp_event_buffer.copyFrom(event_buffer);
        this.tmp_event_buffer.setWindow(this.parent_window);
        this.tmp_event_buffer.sendEvent(getDisplay(), this.parent_window, true, event_mask);
    }
    
    private void relayEventToParent(final LinuxEvent event_buffer) {
        if (this.parent == null) {
            return;
        }
        switch (event_buffer.getType()) {
            case 2: {
                this.relayEventToParent(event_buffer, 1);
                break;
            }
            case 3: {
                this.relayEventToParent(event_buffer, 1);
                break;
            }
            case 4: {
                if (LinuxDisplay.xembedded || !this.focused) {
                    this.relayEventToParent(event_buffer, 1);
                    break;
                }
                break;
            }
            case 5: {
                if (LinuxDisplay.xembedded || !this.focused) {
                    this.relayEventToParent(event_buffer, 1);
                    break;
                }
                break;
            }
        }
    }
    
    private void processEvents() {
        while (LinuxEvent.getPending(getDisplay()) > 0) {
            this.event_buffer.nextEvent(getDisplay());
            final long event_window = this.event_buffer.getWindow();
            this.relayEventToParent(this.event_buffer);
            if (event_window == getWindow() && !this.event_buffer.filterEvent(event_window) && (this.mouse == null || !this.mouse.filterEvent(this.grab, this.shouldWarpPointer(), this.event_buffer))) {
                if (this.keyboard != null && this.keyboard.filterEvent(this.event_buffer)) {
                    continue;
                }
                switch (this.event_buffer.getType()) {
                    case 9: {
                        this.setFocused(true, this.event_buffer.getFocusDetail());
                        continue;
                    }
                    case 10: {
                        this.setFocused(false, this.event_buffer.getFocusDetail());
                        continue;
                    }
                    case 33: {
                        if (this.event_buffer.getClientFormat() == 32 && this.event_buffer.getClientData(0) == this.delete_atom) {
                            this.close_requested = true;
                            continue;
                        }
                        continue;
                    }
                    case 19: {
                        this.dirty = true;
                        this.minimized = false;
                        continue;
                    }
                    case 18: {
                        this.dirty = true;
                        this.minimized = true;
                        continue;
                    }
                    case 12: {
                        this.dirty = true;
                        continue;
                    }
                    case 22: {
                        final int x = nGetX(getDisplay(), getWindow());
                        final int y = nGetY(getDisplay(), getWindow());
                        final int width = nGetWidth(getDisplay(), getWindow());
                        final int height = nGetHeight(getDisplay(), getWindow());
                        this.window_x = x;
                        this.window_y = y;
                        if (this.window_width != width || this.window_height != height) {
                            this.resized = true;
                            this.window_width = width;
                            this.window_height = height;
                            continue;
                        }
                        continue;
                    }
                    case 7: {
                        this.mouseInside = true;
                        continue;
                    }
                    case 8: {
                        this.mouseInside = false;
                        continue;
                    }
                }
            }
        }
    }
    
    public void update() {
        lockAWT();
        try {
            this.processEvents();
            this.checkInput();
        }
        finally {
            unlockAWT();
        }
    }
    
    public void reshape(final int x, final int y, final int width, final int height) {
        lockAWT();
        try {
            nReshape(getDisplay(), getWindow(), x, y, width, height);
        }
        finally {
            unlockAWT();
        }
    }
    
    private static native void nReshape(final long p0, final long p1, final int p2, final int p3, final int p4, final int p5);
    
    public DisplayMode[] getAvailableDisplayModes() throws LWJGLException {
        lockAWT();
        try {
            incDisplay();
            if (this.current_displaymode_extension == 10) {
                final DisplayMode[] nDisplayModes = nGetAvailableDisplayModes(getDisplay(), getDefaultScreen(), this.current_displaymode_extension);
                int bpp = 24;
                if (nDisplayModes.length > 0) {
                    bpp = nDisplayModes[0].getBitsPerPixel();
                }
                final XRandR.Screen[] resolutions = XRandR.getResolutions(XRandR.getScreenNames()[0]);
                final DisplayMode[] modes = new DisplayMode[resolutions.length];
                for (int i = 0; i < modes.length; ++i) {
                    modes[i] = new DisplayMode(resolutions[i].width, resolutions[i].height, bpp, resolutions[i].freq);
                }
                return modes;
            }
            try {
                final DisplayMode[] modes2 = nGetAvailableDisplayModes(getDisplay(), getDefaultScreen(), this.current_displaymode_extension);
                return modes2;
            }
            finally {
                decDisplay();
            }
        }
        finally {
            unlockAWT();
        }
    }
    
    private static native DisplayMode[] nGetAvailableDisplayModes(final long p0, final int p1, final int p2) throws LWJGLException;
    
    public boolean hasWheel() {
        return true;
    }
    
    public int getButtonCount() {
        return this.mouse.getButtonCount();
    }
    
    public void createMouse() throws LWJGLException {
        lockAWT();
        try {
            this.mouse = new LinuxMouse(getDisplay(), getWindow(), getWindow());
        }
        finally {
            unlockAWT();
        }
    }
    
    public void destroyMouse() {
        this.mouse = null;
        this.updateInputGrab();
    }
    
    public void pollMouse(final IntBuffer coord_buffer, final ByteBuffer buttons) {
        lockAWT();
        try {
            this.mouse.poll(this.grab, coord_buffer, buttons);
        }
        finally {
            unlockAWT();
        }
    }
    
    public void readMouse(final ByteBuffer buffer) {
        lockAWT();
        try {
            this.mouse.read(buffer);
        }
        finally {
            unlockAWT();
        }
    }
    
    public void setCursorPosition(final int x, final int y) {
        lockAWT();
        try {
            this.mouse.setCursorPosition(x, y);
        }
        finally {
            unlockAWT();
        }
    }
    
    private void checkInput() {
        if (this.parent == null) {
            return;
        }
        if (LinuxDisplay.xembedded) {
            final long current_focus_window = 0L;
            if (this.last_window_focus != current_focus_window || this.parent_focused != this.focused) {
                if (this.isParentWindowActive(current_focus_window)) {
                    if (this.parent_focused) {
                        nSetInputFocus(getDisplay(), LinuxDisplay.current_window, 0L);
                        this.last_window_focus = LinuxDisplay.current_window;
                        this.focused = true;
                    }
                    else {
                        nSetInputFocus(getDisplay(), this.parent_proxy_focus_window, 0L);
                        this.last_window_focus = this.parent_proxy_focus_window;
                        this.focused = false;
                    }
                }
                else {
                    this.last_window_focus = current_focus_window;
                    this.focused = false;
                }
            }
        }
        else if (this.parent_focus_changed && this.parent_focused) {
            this.setInputFocusUnsafe(getWindow());
            this.parent_focus_changed = false;
        }
    }
    
    private void setInputFocusUnsafe(final long window) {
        try {
            nSetInputFocus(getDisplay(), window, 0L);
            nSync(getDisplay(), false);
        }
        catch (LWJGLException e) {
            LWJGLUtil.log("Got exception while trying to focus: " + e);
        }
    }
    
    private static native void nSync(final long p0, final boolean p1) throws LWJGLException;
    
    private boolean isParentWindowActive(final long window) {
        try {
            if (window == LinuxDisplay.current_window) {
                return true;
            }
            if (getChildCount(getDisplay(), window) != 0) {
                return false;
            }
            final long parent_window = getParentWindow(getDisplay(), window);
            if (parent_window == 0L) {
                return false;
            }
            long w = LinuxDisplay.current_window;
            while (w != 0L) {
                w = getParentWindow(getDisplay(), w);
                if (w == parent_window) {
                    this.parent_proxy_focus_window = window;
                    return true;
                }
            }
        }
        catch (LWJGLException e) {
            LWJGLUtil.log("Failed to detect if parent window is active: " + e.getMessage());
            return true;
        }
        return false;
    }
    
    private void setFocused(final boolean got_focus, final int focus_detail) {
        if (this.focused == got_focus || focus_detail == 7 || focus_detail == 5 || focus_detail == 6 || LinuxDisplay.xembedded) {
            return;
        }
        this.focused = got_focus;
        if (this.focused) {
            this.acquireInput();
        }
        else {
            this.releaseInput();
        }
    }
    
    private void releaseInput() {
        if (isLegacyFullscreen() || this.input_released) {
            return;
        }
        if (this.keyboard != null) {
            this.keyboard.releaseAll();
        }
        this.input_released = true;
        this.updateInputGrab();
        if (LinuxDisplay.current_window_mode == 2) {
            nIconifyWindow(getDisplay(), getWindow(), getDefaultScreen());
            try {
                if (this.current_displaymode_extension == 10) {
                    AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Object>() {
                        public Object run() {
                            XRandR.restoreConfiguration();
                            return null;
                        }
                    });
                }
                else {
                    this.switchDisplayModeOnTmpDisplay(this.saved_mode);
                }
                setGammaRampOnTmpDisplay(this.saved_gamma);
            }
            catch (LWJGLException e) {
                LWJGLUtil.log("Failed to restore saved mode: " + e.getMessage());
            }
        }
    }
    
    private static native void nIconifyWindow(final long p0, final long p1, final int p2);
    
    private void acquireInput() {
        if (isLegacyFullscreen() || !this.input_released) {
            return;
        }
        this.input_released = false;
        this.updateInputGrab();
        if (LinuxDisplay.current_window_mode == 2) {
            try {
                this.switchDisplayModeOnTmpDisplay(this.current_mode);
                setGammaRampOnTmpDisplay(this.current_gamma);
            }
            catch (LWJGLException e) {
                LWJGLUtil.log("Failed to restore mode: " + e.getMessage());
            }
        }
    }
    
    public void grabMouse(final boolean new_grab) {
        lockAWT();
        try {
            if (new_grab != this.grab) {
                this.grab = new_grab;
                this.updateInputGrab();
                this.mouse.changeGrabbed(this.grab, this.shouldWarpPointer());
            }
        }
        finally {
            unlockAWT();
        }
    }
    
    private boolean shouldWarpPointer() {
        return this.pointer_grabbed && this.shouldGrab();
    }
    
    public int getNativeCursorCapabilities() {
        lockAWT();
        try {
            incDisplay();
            try {
                return nGetNativeCursorCapabilities(getDisplay());
            }
            finally {
                decDisplay();
            }
        }
        catch (LWJGLException e) {
            throw new RuntimeException(e);
        }
        finally {
            unlockAWT();
        }
    }
    
    private static native int nGetNativeCursorCapabilities(final long p0) throws LWJGLException;
    
    public void setNativeCursor(final Object handle) throws LWJGLException {
        this.current_cursor = getCursorHandle(handle);
        lockAWT();
        try {
            this.updateCursor();
        }
        finally {
            unlockAWT();
        }
    }
    
    public int getMinCursorSize() {
        lockAWT();
        try {
            incDisplay();
            try {
                return nGetMinCursorSize(getDisplay(), getWindow());
            }
            finally {
                decDisplay();
            }
        }
        catch (LWJGLException e) {
            LWJGLUtil.log("Exception occurred in getMinCursorSize: " + e);
            return 0;
        }
        finally {
            unlockAWT();
        }
    }
    
    private static native int nGetMinCursorSize(final long p0, final long p1);
    
    public int getMaxCursorSize() {
        lockAWT();
        try {
            incDisplay();
            try {
                return nGetMaxCursorSize(getDisplay(), getWindow());
            }
            finally {
                decDisplay();
            }
        }
        catch (LWJGLException e) {
            LWJGLUtil.log("Exception occurred in getMaxCursorSize: " + e);
            return 0;
        }
        finally {
            unlockAWT();
        }
    }
    
    private static native int nGetMaxCursorSize(final long p0, final long p1);
    
    public void createKeyboard() throws LWJGLException {
        lockAWT();
        try {
            this.keyboard = new LinuxKeyboard(getDisplay(), getWindow());
        }
        finally {
            unlockAWT();
        }
    }
    
    public void destroyKeyboard() {
        lockAWT();
        try {
            this.keyboard.destroy(getDisplay());
            this.keyboard = null;
        }
        finally {
            unlockAWT();
        }
    }
    
    public void pollKeyboard(final ByteBuffer keyDownBuffer) {
        lockAWT();
        try {
            this.keyboard.poll(keyDownBuffer);
        }
        finally {
            unlockAWT();
        }
    }
    
    public void readKeyboard(final ByteBuffer buffer) {
        lockAWT();
        try {
            this.keyboard.read(buffer);
        }
        finally {
            unlockAWT();
        }
    }
    
    private static native long nCreateCursor(final long p0, final int p1, final int p2, final int p3, final int p4, final int p5, final IntBuffer p6, final int p7, final IntBuffer p8, final int p9) throws LWJGLException;
    
    private static long createBlankCursor() {
        return nCreateBlankCursor(getDisplay(), getWindow());
    }
    
    static native long nCreateBlankCursor(final long p0, final long p1);
    
    public Object createCursor(final int width, final int height, final int xHotspot, final int yHotspot, final int numImages, final IntBuffer images, final IntBuffer delays) throws LWJGLException {
        lockAWT();
        try {
            incDisplay();
            try {
                final long cursor = nCreateCursor(getDisplay(), width, height, xHotspot, yHotspot, numImages, images, images.position(), delays, (delays != null) ? delays.position() : -1);
                return cursor;
            }
            catch (LWJGLException e) {
                decDisplay();
                throw e;
            }
        }
        finally {
            unlockAWT();
        }
    }
    
    private static long getCursorHandle(final Object cursor_handle) {
        return (long)((cursor_handle != null) ? cursor_handle : 0L);
    }
    
    public void destroyCursor(final Object cursorHandle) {
        lockAWT();
        try {
            nDestroyCursor(getDisplay(), getCursorHandle(cursorHandle));
            decDisplay();
        }
        finally {
            unlockAWT();
        }
    }
    
    static native void nDestroyCursor(final long p0, final long p1);
    
    public int getPbufferCapabilities() {
        lockAWT();
        try {
            incDisplay();
            try {
                return nGetPbufferCapabilities(getDisplay(), getDefaultScreen());
            }
            finally {
                decDisplay();
            }
        }
        catch (LWJGLException e) {
            LWJGLUtil.log("Exception occurred in getPbufferCapabilities: " + e);
            return 0;
        }
        finally {
            unlockAWT();
        }
    }
    
    private static native int nGetPbufferCapabilities(final long p0, final int p1);
    
    public boolean isBufferLost(final PeerInfo handle) {
        return false;
    }
    
    public PeerInfo createPbuffer(final int width, final int height, final org.lwjgl.opengl.PixelFormat pixel_format, final ContextAttribs attribs, final IntBuffer pixelFormatCaps, final IntBuffer pBufferAttribs) throws LWJGLException {
        return new LinuxPbufferPeerInfo(width, height, pixel_format);
    }
    
    public void setPbufferAttrib(final PeerInfo handle, final int attrib, final int value) {
        throw new UnsupportedOperationException();
    }
    
    public void bindTexImageToPbuffer(final PeerInfo handle, final int buffer) {
        throw new UnsupportedOperationException();
    }
    
    public void releaseTexImageFromPbuffer(final PeerInfo handle, final int buffer) {
        throw new UnsupportedOperationException();
    }
    
    private static ByteBuffer convertIcons(final ByteBuffer[] icons) {
        int bufferSize = 0;
        for (final ByteBuffer icon : icons) {
            final int size = icon.limit() / 4;
            final int dimension = (int)Math.sqrt(size);
            if (dimension > 0) {
                bufferSize += 8;
                bufferSize += dimension * dimension * 4;
            }
        }
        if (bufferSize == 0) {
            return null;
        }
        final ByteBuffer icon_argb = BufferUtils.createByteBuffer(bufferSize);
        icon_argb.order(ByteOrder.BIG_ENDIAN);
        for (final ByteBuffer icon2 : icons) {
            final int size2 = icon2.limit() / 4;
            final int dimension2 = (int)Math.sqrt(size2);
            icon_argb.putInt(dimension2);
            icon_argb.putInt(dimension2);
            for (int y = 0; y < dimension2; ++y) {
                for (int x = 0; x < dimension2; ++x) {
                    final byte r = icon2.get(x * 4 + y * dimension2 * 4);
                    final byte g = icon2.get(x * 4 + y * dimension2 * 4 + 1);
                    final byte b = icon2.get(x * 4 + y * dimension2 * 4 + 2);
                    final byte a = icon2.get(x * 4 + y * dimension2 * 4 + 3);
                    icon_argb.put(a);
                    icon_argb.put(r);
                    icon_argb.put(g);
                    icon_argb.put(b);
                }
            }
        }
        return icon_argb;
    }
    
    public int setIcon(final ByteBuffer[] icons) {
        lockAWT();
        try {
            incDisplay();
            try {
                final ByteBuffer icons_data = convertIcons(icons);
                if (icons_data == null) {
                    return 0;
                }
                nSetWindowIcon(getDisplay(), getWindow(), icons_data, icons_data.capacity());
                return icons.length;
            }
            finally {
                decDisplay();
            }
        }
        catch (LWJGLException e) {
            LWJGLUtil.log("Failed to set display icon: " + e);
            return 0;
        }
        finally {
            unlockAWT();
        }
    }
    
    private static native void nSetWindowIcon(final long p0, final long p1, final ByteBuffer p2, final int p3);
    
    public int getX() {
        return this.window_x;
    }
    
    public int getY() {
        return this.window_y;
    }
    
    public int getWidth() {
        return this.window_width;
    }
    
    public int getHeight() {
        return this.window_height;
    }
    
    public boolean isInsideWindow() {
        return this.mouseInside;
    }
    
    public void setResizable(final boolean resizable) {
        if (this.resizable == resizable) {
            return;
        }
        this.resizable = resizable;
        nSetWindowSize(getDisplay(), getWindow(), this.window_width, this.window_height, resizable);
    }
    
    public boolean wasResized() {
        if (this.resized) {
            this.resized = false;
            return true;
        }
        return false;
    }
    
    public float getPixelScaleFactor() {
        return 1.0f;
    }
    
    static {
        LinuxDisplay.current_window_mode = 3;
    }
    
    private static final class Compiz
    {
        private static boolean applyFix;
        private static Provider provider;
        
        static void init() {
            if (Display.getPrivilegedBoolean("org.lwjgl.opengl.Window.nocompiz_lfs")) {
                return;
            }
            AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Object>() {
                public Object run() {
                    try {
                        if (!isProcessActive("compiz")) {
                            return null;
                        }
                        Compiz.provider = null;
                        String providerName = null;
                        if (isProcessActive("dbus-daemon")) {
                            providerName = "Dbus";
                            Compiz.provider = new Provider() {
                                private static final String KEY = "/org/freedesktop/compiz/workarounds/allscreens/legacy_fullscreen";
                                
                                public boolean hasLegacyFullscreenSupport() throws LWJGLException {
                                    final List output = run(new String[] { "dbus-send", "--print-reply", "--type=method_call", "--dest=org.freedesktop.compiz", "/org/freedesktop/compiz/workarounds/allscreens/legacy_fullscreen", "org.freedesktop.compiz.get" });
                                    if (output == null || output.size() < 2) {
                                        throw new LWJGLException("Invalid Dbus reply.");
                                    }
                                    String line = output.get(0);
                                    if (!line.startsWith("method return")) {
                                        throw new LWJGLException("Invalid Dbus reply.");
                                    }
                                    line = output.get(1).trim();
                                    if (!line.startsWith("boolean") || line.length() < 12) {
                                        throw new LWJGLException("Invalid Dbus reply.");
                                    }
                                    return "true".equalsIgnoreCase(line.substring("boolean".length() + 1));
                                }
                                
                                public void setLegacyFullscreenSupport(final boolean state) throws LWJGLException {
                                    if (run(new String[] { "dbus-send", "--type=method_call", "--dest=org.freedesktop.compiz", "/org/freedesktop/compiz/workarounds/allscreens/legacy_fullscreen", "org.freedesktop.compiz.set", "boolean:" + Boolean.toString(state) }) == null) {
                                        throw new LWJGLException("Failed to apply Compiz LFS workaround.");
                                    }
                                }
                            };
                        }
                        else {
                            try {
                                Runtime.getRuntime().exec("gconftool");
                                providerName = "gconftool";
                                Compiz.provider = new Provider() {
                                    private static final String KEY = "/apps/compiz/plugins/workarounds/allscreens/options/legacy_fullscreen";
                                    
                                    public boolean hasLegacyFullscreenSupport() throws LWJGLException {
                                        final List output = run(new String[] { "gconftool", "-g", "/apps/compiz/plugins/workarounds/allscreens/options/legacy_fullscreen" });
                                        if (output == null || output.size() == 0) {
                                            throw new LWJGLException("Invalid gconftool reply.");
                                        }
                                        return Boolean.parseBoolean(output.get(0).trim());
                                    }
                                    
                                    public void setLegacyFullscreenSupport(final boolean state) throws LWJGLException {
                                        if (run(new String[] { "gconftool", "-s", "/apps/compiz/plugins/workarounds/allscreens/options/legacy_fullscreen", "-s", Boolean.toString(state), "-t", "bool" }) == null) {
                                            throw new LWJGLException("Failed to apply Compiz LFS workaround.");
                                        }
                                        if (state) {
                                            try {
                                                Thread.sleep(200L);
                                            }
                                            catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                };
                            }
                            catch (IOException ex) {}
                        }
                        if (Compiz.provider != null && !Compiz.provider.hasLegacyFullscreenSupport()) {
                            Compiz.applyFix = true;
                            LWJGLUtil.log("Using " + providerName + " to apply Compiz LFS workaround.");
                        }
                    }
                    catch (LWJGLException e) {
                        return e;
                    }
                    finally {
                        return null;
                    }
                }
            });
        }
        
        static void setLegacyFullscreenSupport(final boolean enabled) {
            if (!Compiz.applyFix) {
                return;
            }
            AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Object>() {
                public Object run() {
                    try {
                        Compiz.provider.setLegacyFullscreenSupport(enabled);
                    }
                    catch (LWJGLException e) {
                        LWJGLUtil.log("Failed to change Compiz Legacy Fullscreen Support. Reason: " + e.getMessage());
                    }
                    return null;
                }
            });
        }
        
        private static List<String> run(final String... command) throws LWJGLException {
            final List<String> output = new ArrayList<String>();
            try {
                final Process p = Runtime.getRuntime().exec(command);
                try {
                    final int exitValue = p.waitFor();
                    if (exitValue != 0) {
                        return null;
                    }
                }
                catch (InterruptedException e) {
                    throw new LWJGLException("Process interrupted.", e);
                }
                final BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    output.add(line);
                }
                br.close();
            }
            catch (IOException e2) {
                throw new LWJGLException("Process failed.", e2);
            }
            return output;
        }
        
        private static boolean isProcessActive(final String processName) throws LWJGLException {
            final List<String> output = run("ps", "-C", processName);
            if (output == null) {
                return false;
            }
            for (final String line : output) {
                if (line.contains(processName)) {
                    return true;
                }
            }
            return false;
        }
        
        private interface Provider
        {
            boolean hasLegacyFullscreenSupport() throws LWJGLException;
            
            void setLegacyFullscreenSupport(final boolean p0) throws LWJGLException;
        }
    }
}
