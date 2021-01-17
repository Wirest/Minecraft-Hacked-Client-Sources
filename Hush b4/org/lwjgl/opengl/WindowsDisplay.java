// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferUtils;
import java.lang.reflect.Method;
import org.lwjgl.input.Mouse;
import java.nio.Buffer;
import org.lwjgl.MemoryUtil;
import java.nio.FloatBuffer;
import java.awt.KeyboardFocusManager;
import org.lwjgl.LWJGLUtil;
import javax.swing.SwingUtilities;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import org.lwjgl.LWJGLException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.awt.event.FocusAdapter;
import java.awt.Canvas;
import java.nio.IntBuffer;

final class WindowsDisplay implements DisplayImplementation
{
    private static final int GAMMA_LENGTH = 256;
    private static final int WM_WINDOWPOSCHANGED = 71;
    private static final int WM_MOVE = 3;
    private static final int WM_CANCELMODE = 31;
    private static final int WM_MOUSEMOVE = 512;
    private static final int WM_LBUTTONDOWN = 513;
    private static final int WM_LBUTTONUP = 514;
    private static final int WM_LBUTTONDBLCLK = 515;
    private static final int WM_RBUTTONDOWN = 516;
    private static final int WM_RBUTTONUP = 517;
    private static final int WM_RBUTTONDBLCLK = 518;
    private static final int WM_MBUTTONDOWN = 519;
    private static final int WM_MBUTTONUP = 520;
    private static final int WM_MBUTTONDBLCLK = 521;
    private static final int WM_XBUTTONDOWN = 523;
    private static final int WM_XBUTTONUP = 524;
    private static final int WM_XBUTTONDBLCLK = 525;
    private static final int WM_MOUSEWHEEL = 522;
    private static final int WM_CAPTURECHANGED = 533;
    private static final int WM_MOUSELEAVE = 675;
    private static final int WM_ENTERSIZEMOVE = 561;
    private static final int WM_EXITSIZEMOVE = 562;
    private static final int WM_SIZING = 532;
    private static final int WM_KEYDOWN = 256;
    private static final int WM_KEYUP = 257;
    private static final int WM_SYSKEYUP = 261;
    private static final int WM_SYSKEYDOWN = 260;
    private static final int WM_SYSCHAR = 262;
    private static final int WM_CHAR = 258;
    private static final int WM_GETICON = 127;
    private static final int WM_SETICON = 128;
    private static final int WM_SETCURSOR = 32;
    private static final int WM_MOUSEACTIVATE = 33;
    private static final int WM_QUIT = 18;
    private static final int WM_SYSCOMMAND = 274;
    private static final int WM_PAINT = 15;
    private static final int WM_KILLFOCUS = 8;
    private static final int WM_SETFOCUS = 7;
    private static final int SC_SIZE = 61440;
    private static final int SC_MOVE = 61456;
    private static final int SC_MINIMIZE = 61472;
    private static final int SC_MAXIMIZE = 61488;
    private static final int SC_NEXTWINDOW = 61504;
    private static final int SC_PREVWINDOW = 61520;
    private static final int SC_CLOSE = 61536;
    private static final int SC_VSCROLL = 61552;
    private static final int SC_HSCROLL = 61568;
    private static final int SC_MOUSEMENU = 61584;
    private static final int SC_KEYMENU = 61696;
    private static final int SC_ARRANGE = 61712;
    private static final int SC_RESTORE = 61728;
    private static final int SC_TASKLIST = 61744;
    private static final int SC_SCREENSAVE = 61760;
    private static final int SC_HOTKEY = 61776;
    private static final int SC_DEFAULT = 61792;
    private static final int SC_MONITORPOWER = 61808;
    private static final int SC_CONTEXTHELP = 61824;
    private static final int SC_SEPARATOR = 61455;
    static final int SM_CXCURSOR = 13;
    static final int SM_CYCURSOR = 14;
    static final int SM_CMOUSEBUTTONS = 43;
    static final int SM_MOUSEWHEELPRESENT = 75;
    private static final int SIZE_RESTORED = 0;
    private static final int SIZE_MINIMIZED = 1;
    private static final int SIZE_MAXIMIZED = 2;
    private static final int WM_SIZE = 5;
    private static final int WM_ACTIVATE = 6;
    private static final int WA_INACTIVE = 0;
    private static final int WA_ACTIVE = 1;
    private static final int WA_CLICKACTIVE = 2;
    private static final int SW_NORMAL = 1;
    private static final int SW_SHOWMINNOACTIVE = 7;
    private static final int SW_SHOWDEFAULT = 10;
    private static final int SW_RESTORE = 9;
    private static final int SW_MAXIMIZE = 3;
    private static final int ICON_SMALL = 0;
    private static final int ICON_BIG = 1;
    private static final IntBuffer rect_buffer;
    private static final Rect rect;
    private static final long HWND_TOP = 0L;
    private static final long HWND_BOTTOM = 1L;
    private static final long HWND_TOPMOST = -1L;
    private static final long HWND_NOTOPMOST = -2L;
    private static final int SWP_NOSIZE = 1;
    private static final int SWP_NOMOVE = 2;
    private static final int SWP_NOZORDER = 4;
    private static final int SWP_FRAMECHANGED = 32;
    private static final int GWL_STYLE = -16;
    private static final int GWL_EXSTYLE = -20;
    private static final int WS_THICKFRAME = 262144;
    private static final int WS_MAXIMIZEBOX = 65536;
    private static final int HTCLIENT = 1;
    private static final int MK_XBUTTON1 = 32;
    private static final int MK_XBUTTON2 = 64;
    private static final int XBUTTON1 = 1;
    private static final int XBUTTON2 = 2;
    private static WindowsDisplay current_display;
    private static boolean cursor_clipped;
    private WindowsDisplayPeerInfo peer_info;
    private Object current_cursor;
    private static boolean hasParent;
    private Canvas parent;
    private long parent_hwnd;
    private FocusAdapter parent_focus_tracker;
    private AtomicBoolean parent_focused;
    private WindowsKeyboard keyboard;
    private WindowsMouse mouse;
    private boolean close_requested;
    private boolean is_dirty;
    private ByteBuffer current_gamma;
    private ByteBuffer saved_gamma;
    private DisplayMode current_mode;
    private boolean mode_set;
    private boolean isMinimized;
    private boolean isFocused;
    private boolean redoMakeContextCurrent;
    private boolean inAppActivate;
    private boolean resized;
    private boolean resizable;
    private int x;
    private int y;
    private int width;
    private int height;
    private long hwnd;
    private long hdc;
    private long small_icon;
    private long large_icon;
    private boolean iconsLoaded;
    private int captureMouse;
    private boolean mouseInside;
    
    WindowsDisplay() {
        this.captureMouse = -1;
        WindowsDisplay.current_display = this;
    }
    
    public void createWindow(final DrawableLWJGL drawable, final DisplayMode mode, final Canvas parent, final int x, final int y) throws LWJGLException {
        this.parent = parent;
        WindowsDisplay.hasParent = (parent != null);
        this.parent_hwnd = ((parent != null) ? getHwnd(parent) : 0L);
        this.hwnd = nCreateWindow(x, y, mode.getWidth(), mode.getHeight(), Display.isFullscreen() || isUndecorated(), parent != null, this.parent_hwnd);
        if (Display.isResizable() && parent == null) {
            this.setResizable(true);
        }
        if (this.hwnd == 0L) {
            throw new LWJGLException("Failed to create window");
        }
        this.hdc = getDC(this.hwnd);
        if (this.hdc == 0L) {
            nDestroyWindow(this.hwnd);
            throw new LWJGLException("Failed to get dc");
        }
        try {
            if (drawable instanceof DrawableGL) {
                final int format = WindowsPeerInfo.choosePixelFormat(this.getHdc(), 0, 0, (PixelFormat)drawable.getPixelFormat(), null, true, true, false, true);
                WindowsPeerInfo.setPixelFormat(this.getHdc(), format);
            }
            else {
                this.peer_info = new WindowsDisplayPeerInfo(true);
                ((DrawableGLES)drawable).initialize(this.hwnd, this.hdc, 4, (org.lwjgl.opengles.PixelFormat)drawable.getPixelFormat());
            }
            this.peer_info.initDC(this.getHwnd(), this.getHdc());
            showWindow(this.getHwnd(), 10);
            this.updateWidthAndHeight();
            if (parent == null) {
                setForegroundWindow(this.getHwnd());
            }
            else {
                this.parent_focused = new AtomicBoolean(false);
                parent.addFocusListener(this.parent_focus_tracker = new FocusAdapter() {
                    @Override
                    public void focusGained(final FocusEvent e) {
                        WindowsDisplay.this.parent_focused.set(true);
                        WindowsDisplay.this.clearAWTFocus();
                    }
                });
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        WindowsDisplay.this.clearAWTFocus();
                    }
                });
            }
            this.grabFocus();
        }
        catch (LWJGLException e) {
            nReleaseDC(this.hwnd, this.hdc);
            nDestroyWindow(this.hwnd);
            throw e;
        }
    }
    
    private void updateWidthAndHeight() {
        getClientRect(this.hwnd, WindowsDisplay.rect_buffer);
        WindowsDisplay.rect.copyFromBuffer(WindowsDisplay.rect_buffer);
        this.width = WindowsDisplay.rect.right - WindowsDisplay.rect.left;
        this.height = WindowsDisplay.rect.bottom - WindowsDisplay.rect.top;
    }
    
    private static native long nCreateWindow(final int p0, final int p1, final int p2, final int p3, final boolean p4, final boolean p5, final long p6) throws LWJGLException;
    
    private static boolean isUndecorated() {
        return Display.getPrivilegedBoolean("org.lwjgl.opengl.Window.undecorated");
    }
    
    private static long getHwnd(final Canvas parent) throws LWJGLException {
        final AWTCanvasImplementation awt_impl = AWTGLCanvas.createImplementation();
        final WindowsPeerInfo parent_peer_info = (WindowsPeerInfo)awt_impl.createPeerInfo(parent, null, null);
        final ByteBuffer parent_peer_info_handle = parent_peer_info.lockAndGetHandle();
        try {
            return parent_peer_info.getHwnd();
        }
        finally {
            parent_peer_info.unlock();
        }
    }
    
    public void destroyWindow() {
        if (this.parent != null) {
            this.parent.removeFocusListener(this.parent_focus_tracker);
            this.parent_focus_tracker = null;
        }
        nReleaseDC(this.hwnd, this.hdc);
        nDestroyWindow(this.hwnd);
        this.freeLargeIcon();
        this.freeSmallIcon();
        resetCursorClipping();
        this.close_requested = false;
        this.is_dirty = false;
        this.isMinimized = false;
        this.isFocused = false;
        this.redoMakeContextCurrent = false;
        this.mouseInside = false;
    }
    
    private static native void nReleaseDC(final long p0, final long p1);
    
    private static native void nDestroyWindow(final long p0);
    
    static void resetCursorClipping() {
        if (WindowsDisplay.cursor_clipped) {
            try {
                clipCursor(null);
            }
            catch (LWJGLException e) {
                LWJGLUtil.log("Failed to reset cursor clipping: " + e);
            }
            WindowsDisplay.cursor_clipped = false;
        }
    }
    
    private static void getGlobalClientRect(final long hwnd, final Rect rect) {
        WindowsDisplay.rect_buffer.put(0, 0).put(1, 0);
        clientToScreen(hwnd, WindowsDisplay.rect_buffer);
        final int offset_x = WindowsDisplay.rect_buffer.get(0);
        final int offset_y = WindowsDisplay.rect_buffer.get(1);
        getClientRect(hwnd, WindowsDisplay.rect_buffer);
        rect.copyFromBuffer(WindowsDisplay.rect_buffer);
        rect.offset(offset_x, offset_y);
    }
    
    static void setupCursorClipping(final long hwnd) throws LWJGLException {
        WindowsDisplay.cursor_clipped = true;
        getGlobalClientRect(hwnd, WindowsDisplay.rect);
        WindowsDisplay.rect.copyToBuffer(WindowsDisplay.rect_buffer);
        clipCursor(WindowsDisplay.rect_buffer);
    }
    
    private static native void clipCursor(final IntBuffer p0) throws LWJGLException;
    
    public void switchDisplayMode(final DisplayMode mode) throws LWJGLException {
        nSwitchDisplayMode(mode);
        this.current_mode = mode;
        this.mode_set = true;
    }
    
    private static native void nSwitchDisplayMode(final DisplayMode p0) throws LWJGLException;
    
    private void appActivate(final boolean active, final long millis) {
        if (this.inAppActivate) {
            return;
        }
        this.inAppActivate = true;
        this.isFocused = active;
        if (active) {
            if (Display.isFullscreen()) {
                this.restoreDisplayMode();
            }
            if (this.parent == null) {
                setForegroundWindow(this.getHwnd());
            }
            setFocus(this.getHwnd());
            this.redoMakeContextCurrent = true;
        }
        else {
            if (this.keyboard != null) {
                this.keyboard.releaseAll(millis);
            }
            if (Display.isFullscreen()) {
                showWindow(this.getHwnd(), 7);
                this.resetDisplayMode();
            }
        }
        this.updateCursor();
        this.inAppActivate = false;
    }
    
    private static native void showWindow(final long p0, final int p1);
    
    private static native void setForegroundWindow(final long p0);
    
    private static native void setFocus(final long p0);
    
    private void clearAWTFocus() {
        this.parent.setFocusable(false);
        this.parent.setFocusable(true);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
    }
    
    private void grabFocus() {
        if (this.parent == null) {
            setFocus(this.getHwnd());
        }
        else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    WindowsDisplay.this.parent.requestFocus();
                }
            });
        }
    }
    
    private void restoreDisplayMode() {
        try {
            this.doSetGammaRamp(this.current_gamma);
        }
        catch (LWJGLException e) {
            LWJGLUtil.log("Failed to restore gamma: " + e.getMessage());
        }
        if (!this.mode_set) {
            this.mode_set = true;
            try {
                nSwitchDisplayMode(this.current_mode);
            }
            catch (LWJGLException e) {
                LWJGLUtil.log("Failed to restore display mode: " + e.getMessage());
            }
        }
    }
    
    public void resetDisplayMode() {
        try {
            this.doSetGammaRamp(this.saved_gamma);
        }
        catch (LWJGLException e) {
            LWJGLUtil.log("Failed to reset gamma ramp: " + e.getMessage());
        }
        this.current_gamma = this.saved_gamma;
        if (this.mode_set) {
            this.mode_set = false;
            nResetDisplayMode();
        }
        resetCursorClipping();
    }
    
    private static native void nResetDisplayMode();
    
    public int getGammaRampLength() {
        return 256;
    }
    
    public void setGammaRamp(final FloatBuffer gammaRamp) throws LWJGLException {
        this.doSetGammaRamp(convertToNativeRamp(gammaRamp));
    }
    
    private static native ByteBuffer convertToNativeRamp(final FloatBuffer p0) throws LWJGLException;
    
    private static native ByteBuffer getCurrentGammaRamp() throws LWJGLException;
    
    private void doSetGammaRamp(final ByteBuffer native_gamma) throws LWJGLException {
        nSetGammaRamp(native_gamma);
        this.current_gamma = native_gamma;
    }
    
    private static native void nSetGammaRamp(final ByteBuffer p0) throws LWJGLException;
    
    public String getAdapter() {
        try {
            final String maxObjNo = WindowsRegistry.queryRegistrationKey(3, "HARDWARE\\DeviceMap\\Video", "MaxObjectNumber");
            final int maxObjectNumber = maxObjNo.charAt(0);
            String vga_driver_value = "";
            for (int i = 0; i < maxObjectNumber; ++i) {
                final String adapter_string = WindowsRegistry.queryRegistrationKey(3, "HARDWARE\\DeviceMap\\Video", "\\Device\\Video" + i);
                final String root_key = "\\registry\\machine\\";
                if (adapter_string.toLowerCase().startsWith(root_key)) {
                    final String driver_value = WindowsRegistry.queryRegistrationKey(3, adapter_string.substring(root_key.length()), "InstalledDisplayDrivers");
                    if (driver_value.toUpperCase().startsWith("VGA")) {
                        vga_driver_value = driver_value;
                    }
                    else if (!driver_value.toUpperCase().startsWith("RDP") && !driver_value.toUpperCase().startsWith("NMNDD")) {
                        return driver_value;
                    }
                }
            }
            if (!vga_driver_value.equals("")) {
                return vga_driver_value;
            }
        }
        catch (LWJGLException e) {
            LWJGLUtil.log("Exception occurred while querying registry: " + e);
        }
        return null;
    }
    
    public String getVersion() {
        final String driver = this.getAdapter();
        if (driver != null) {
            final String[] drivers = driver.split(",");
            if (drivers.length > 0) {
                final WindowsFileVersion version = this.nGetVersion(drivers[0] + ".dll");
                if (version != null) {
                    return version.toString();
                }
            }
        }
        return null;
    }
    
    private native WindowsFileVersion nGetVersion(final String p0);
    
    public DisplayMode init() throws LWJGLException {
        final ByteBuffer currentGammaRamp = getCurrentGammaRamp();
        this.saved_gamma = currentGammaRamp;
        this.current_gamma = currentGammaRamp;
        return this.current_mode = getCurrentDisplayMode();
    }
    
    private static native DisplayMode getCurrentDisplayMode() throws LWJGLException;
    
    public void setTitle(final String title) {
        final ByteBuffer buffer = MemoryUtil.encodeUTF16(title);
        nSetTitle(this.hwnd, MemoryUtil.getAddress0(buffer));
    }
    
    private static native void nSetTitle(final long p0, final long p1);
    
    public boolean isCloseRequested() {
        final boolean saved = this.close_requested;
        this.close_requested = false;
        return saved;
    }
    
    public boolean isVisible() {
        return !this.isMinimized;
    }
    
    public boolean isActive() {
        return this.isFocused;
    }
    
    public boolean isDirty() {
        final boolean saved = this.is_dirty;
        this.is_dirty = false;
        return saved;
    }
    
    public PeerInfo createPeerInfo(final PixelFormat pixel_format, final ContextAttribs attribs) throws LWJGLException {
        return this.peer_info = new WindowsDisplayPeerInfo(false);
    }
    
    public void update() {
        nUpdate();
        if (!this.isFocused && this.parent != null && this.parent_focused.compareAndSet(true, false)) {
            setFocus(this.getHwnd());
        }
        if (this.redoMakeContextCurrent) {
            this.redoMakeContextCurrent = false;
            try {
                final Context context = ((DrawableLWJGL)Display.getDrawable()).getContext();
                if (context != null && context.isCurrent()) {
                    context.makeCurrent();
                }
            }
            catch (LWJGLException e) {
                LWJGLUtil.log("Exception occurred while trying to make context current: " + e);
            }
        }
    }
    
    private static native void nUpdate();
    
    public void reshape(final int x, final int y, final int width, final int height) {
        nReshape(this.getHwnd(), x, y, width, height, Display.isFullscreen() || isUndecorated(), this.parent != null);
    }
    
    private static native void nReshape(final long p0, final int p1, final int p2, final int p3, final int p4, final boolean p5, final boolean p6);
    
    public native DisplayMode[] getAvailableDisplayModes() throws LWJGLException;
    
    public boolean hasWheel() {
        return this.mouse.hasWheel();
    }
    
    public int getButtonCount() {
        return this.mouse.getButtonCount();
    }
    
    public void createMouse() throws LWJGLException {
        this.mouse = new WindowsMouse(this.getHwnd());
    }
    
    public void destroyMouse() {
        if (this.mouse != null) {
            this.mouse.destroy();
        }
        this.mouse = null;
    }
    
    public void pollMouse(final IntBuffer coord_buffer, final ByteBuffer buttons) {
        this.mouse.poll(coord_buffer, buttons, this);
    }
    
    public void readMouse(final ByteBuffer buffer) {
        this.mouse.read(buffer);
    }
    
    public void grabMouse(final boolean grab) {
        this.mouse.grab(grab);
        this.updateCursor();
    }
    
    public int getNativeCursorCapabilities() {
        return 1;
    }
    
    public void setCursorPosition(final int x, final int y) {
        getGlobalClientRect(this.getHwnd(), WindowsDisplay.rect);
        final int transformed_x = WindowsDisplay.rect.left + x;
        final int transformed_y = WindowsDisplay.rect.bottom - 1 - y;
        nSetCursorPosition(transformed_x, transformed_y);
        this.setMousePosition(x, y);
    }
    
    private static native void nSetCursorPosition(final int p0, final int p1);
    
    public void setNativeCursor(final Object handle) throws LWJGLException {
        this.current_cursor = handle;
        this.updateCursor();
    }
    
    private void updateCursor() {
        try {
            if (this.mouse != null && this.shouldGrab()) {
                centerCursor(this.hwnd);
                nSetNativeCursor(this.getHwnd(), this.mouse.getBlankCursor());
            }
            else {
                nSetNativeCursor(this.getHwnd(), this.current_cursor);
            }
        }
        catch (LWJGLException e) {
            LWJGLUtil.log("Failed to update cursor: " + e);
        }
        this.updateClipping();
    }
    
    static native void nSetNativeCursor(final long p0, final Object p1) throws LWJGLException;
    
    public int getMinCursorSize() {
        return getSystemMetrics(13);
    }
    
    public int getMaxCursorSize() {
        return getSystemMetrics(13);
    }
    
    static native int getSystemMetrics(final int p0);
    
    private static native long getDllInstance();
    
    private long getHwnd() {
        return this.hwnd;
    }
    
    private long getHdc() {
        return this.hdc;
    }
    
    private static native long getDC(final long p0);
    
    private static native long getDesktopWindow();
    
    private static native long getForegroundWindow();
    
    static void centerCursor(final long hwnd) {
        if (getForegroundWindow() != hwnd && !WindowsDisplay.hasParent) {
            return;
        }
        getGlobalClientRect(hwnd, WindowsDisplay.rect);
        final int local_offset_x = WindowsDisplay.rect.left;
        final int local_offset_y = WindowsDisplay.rect.top;
        final int center_x = (WindowsDisplay.rect.left + WindowsDisplay.rect.right) / 2;
        final int center_y = (WindowsDisplay.rect.top + WindowsDisplay.rect.bottom) / 2;
        nSetCursorPosition(center_x, center_y);
        final int local_x = center_x - local_offset_x;
        final int local_y = center_y - local_offset_y;
        if (WindowsDisplay.current_display != null) {
            WindowsDisplay.current_display.setMousePosition(local_x, transformY(hwnd, local_y));
        }
    }
    
    private void setMousePosition(final int x, final int y) {
        if (this.mouse != null) {
            this.mouse.setPosition(x, y);
        }
    }
    
    public void createKeyboard() throws LWJGLException {
        this.keyboard = new WindowsKeyboard();
    }
    
    public void destroyKeyboard() {
        this.keyboard = null;
    }
    
    public void pollKeyboard(final ByteBuffer keyDownBuffer) {
        this.keyboard.poll(keyDownBuffer);
    }
    
    public void readKeyboard(final ByteBuffer buffer) {
        this.keyboard.read(buffer);
    }
    
    public static native ByteBuffer nCreateCursor(final int p0, final int p1, final int p2, final int p3, final int p4, final IntBuffer p5, final int p6, final IntBuffer p7, final int p8) throws LWJGLException;
    
    public Object createCursor(final int width, final int height, final int xHotspot, final int yHotspot, final int numImages, final IntBuffer images, final IntBuffer delays) throws LWJGLException {
        return doCreateCursor(width, height, xHotspot, yHotspot, numImages, images, delays);
    }
    
    static Object doCreateCursor(final int width, final int height, final int xHotspot, final int yHotspot, final int numImages, final IntBuffer images, final IntBuffer delays) throws LWJGLException {
        return nCreateCursor(width, height, xHotspot, yHotspot, numImages, images, images.position(), delays, (delays != null) ? delays.position() : -1);
    }
    
    public void destroyCursor(final Object cursorHandle) {
        doDestroyCursor(cursorHandle);
    }
    
    static native void doDestroyCursor(final Object p0);
    
    public int getPbufferCapabilities() {
        try {
            return this.nGetPbufferCapabilities(new PixelFormat(0, 0, 0, 0, 0, 0, 0, 0, false));
        }
        catch (LWJGLException e) {
            LWJGLUtil.log("Exception occurred while determining pbuffer capabilities: " + e);
            return 0;
        }
    }
    
    private native int nGetPbufferCapabilities(final PixelFormat p0) throws LWJGLException;
    
    public boolean isBufferLost(final PeerInfo handle) {
        return ((WindowsPbufferPeerInfo)handle).isBufferLost();
    }
    
    public PeerInfo createPbuffer(final int width, final int height, final PixelFormat pixel_format, final ContextAttribs attribs, final IntBuffer pixelFormatCaps, final IntBuffer pBufferAttribs) throws LWJGLException {
        return new WindowsPbufferPeerInfo(width, height, pixel_format, pixelFormatCaps, pBufferAttribs);
    }
    
    public void setPbufferAttrib(final PeerInfo handle, final int attrib, final int value) {
        ((WindowsPbufferPeerInfo)handle).setPbufferAttrib(attrib, value);
    }
    
    public void bindTexImageToPbuffer(final PeerInfo handle, final int buffer) {
        ((WindowsPbufferPeerInfo)handle).bindTexImageToPbuffer(buffer);
    }
    
    public void releaseTexImageFromPbuffer(final PeerInfo handle, final int buffer) {
        ((WindowsPbufferPeerInfo)handle).releaseTexImageFromPbuffer(buffer);
    }
    
    private void freeSmallIcon() {
        if (this.small_icon != 0L) {
            destroyIcon(this.small_icon);
            this.small_icon = 0L;
        }
    }
    
    private void freeLargeIcon() {
        if (this.large_icon != 0L) {
            destroyIcon(this.large_icon);
            this.large_icon = 0L;
        }
    }
    
    public int setIcon(final ByteBuffer[] icons) {
        boolean done_small = false;
        boolean done_large = false;
        int used = 0;
        final int small_icon_size = 16;
        final int large_icon_size = 32;
        for (final ByteBuffer icon : icons) {
            final int size = icon.limit() / 4;
            if ((int)Math.sqrt(size) == small_icon_size && !done_small) {
                final long small_new_icon = createIcon(small_icon_size, small_icon_size, icon.asIntBuffer());
                sendMessage(this.hwnd, 128L, 0L, small_new_icon);
                this.freeSmallIcon();
                this.small_icon = small_new_icon;
                ++used;
                done_small = true;
            }
            if ((int)Math.sqrt(size) == large_icon_size && !done_large) {
                final long large_new_icon = createIcon(large_icon_size, large_icon_size, icon.asIntBuffer());
                sendMessage(this.hwnd, 128L, 1L, large_new_icon);
                this.freeLargeIcon();
                this.large_icon = large_new_icon;
                ++used;
                done_large = true;
                this.iconsLoaded = false;
                final long time = System.nanoTime();
                final long MAX_WAIT = 500000000L;
                while (true) {
                    nUpdate();
                    if (this.iconsLoaded) {
                        break;
                    }
                    if (MAX_WAIT < System.nanoTime() - time) {
                        break;
                    }
                    Thread.yield();
                }
            }
        }
        return used;
    }
    
    private static native long createIcon(final int p0, final int p1, final IntBuffer p2);
    
    private static native void destroyIcon(final long p0);
    
    private static native long sendMessage(final long p0, final long p1, final long p2, final long p3);
    
    private static native long setWindowLongPtr(final long p0, final int p1, final long p2);
    
    private static native long getWindowLongPtr(final long p0, final int p1);
    
    private static native boolean setWindowPos(final long p0, final long p1, final int p2, final int p3, final int p4, final int p5, final long p6);
    
    private void handleMouseButton(final int button, final int state, final long millis) {
        if (this.mouse != null) {
            this.mouse.handleMouseButton((byte)button, (byte)state, millis);
            if (this.captureMouse == -1 && button != -1 && state == 1) {
                this.captureMouse = button;
                nSetCapture(this.hwnd);
            }
            if (this.captureMouse != -1 && button == this.captureMouse && state == 0) {
                this.captureMouse = -1;
                nReleaseCapture();
            }
        }
    }
    
    private boolean shouldGrab() {
        return !this.isMinimized && this.isFocused && Mouse.isGrabbed();
    }
    
    private static native long nSetCapture(final long p0);
    
    private static native boolean nReleaseCapture();
    
    private void handleMouseScrolled(final int amount, final long millis) {
        if (this.mouse != null) {
            this.mouse.handleMouseScrolled(amount, millis);
        }
    }
    
    private static native void getClientRect(final long p0, final IntBuffer p1);
    
    private void handleChar(final long wParam, final long lParam, final long millis) {
        final byte previous_state = (byte)(lParam >>> 30 & 0x1L);
        final byte state = (byte)(1L - (lParam >>> 31 & 0x1L));
        final boolean repeat = state == previous_state;
        if (this.keyboard != null) {
            this.keyboard.handleChar((int)(wParam & 0xFFFFL), millis, repeat);
        }
    }
    
    private void handleKeyButton(final long wParam, final long lParam, final long millis) {
        if (this.keyboard == null) {
            return;
        }
        final byte previous_state = (byte)(lParam >>> 30 & 0x1L);
        final byte state = (byte)(1L - (lParam >>> 31 & 0x1L));
        final boolean repeat = state == previous_state;
        final byte extended = (byte)(lParam >>> 24 & 0x1L);
        final int scan_code = (int)(lParam >>> 16 & 0xFFL);
        this.keyboard.handleKey((int)wParam, scan_code, extended != 0, state, millis, repeat);
    }
    
    private static int transformY(final long hwnd, final int y) {
        getClientRect(hwnd, WindowsDisplay.rect_buffer);
        WindowsDisplay.rect.copyFromBuffer(WindowsDisplay.rect_buffer);
        return WindowsDisplay.rect.bottom - WindowsDisplay.rect.top - 1 - y;
    }
    
    private static native void clientToScreen(final long p0, final IntBuffer p1);
    
    private static native void setWindowProc(final Method p0);
    
    private static long handleMessage(final long hwnd, final int msg, final long wParam, final long lParam, final long millis) {
        if (WindowsDisplay.current_display != null) {
            return WindowsDisplay.current_display.doHandleMessage(hwnd, msg, wParam, lParam, millis);
        }
        return defWindowProc(hwnd, msg, wParam, lParam);
    }
    
    private static native long defWindowProc(final long p0, final int p1, final long p2, final long p3);
    
    private void updateClipping() {
        Label_0093: {
            if ((Display.isFullscreen() || (this.mouse != null && this.mouse.isGrabbed())) && !this.isMinimized && this.isFocused) {
                if (getForegroundWindow() != this.getHwnd()) {
                    if (!WindowsDisplay.hasParent) {
                        break Label_0093;
                    }
                }
                try {
                    setupCursorClipping(this.getHwnd());
                }
                catch (LWJGLException e) {
                    LWJGLUtil.log("setupCursorClipping failed: " + e.getMessage());
                }
                return;
            }
        }
        resetCursorClipping();
    }
    
    private void setMinimized(final boolean m) {
        if (m != this.isMinimized) {
            this.isMinimized = m;
            this.updateClipping();
        }
    }
    
    private long doHandleMessage(final long hwnd, final int msg, final long wParam, final long lParam, final long millis) {
        if (this.parent != null && !this.isFocused) {
            switch (msg) {
                case 513:
                case 516:
                case 519:
                case 523: {
                    sendMessage(this.parent_hwnd, msg, wParam, lParam);
                    break;
                }
            }
        }
        Label_0919: {
            switch (msg) {
                case 6: {
                    return 0L;
                }
                case 5: {
                    switch ((int)wParam) {
                        case 0:
                        case 2: {
                            this.resized = true;
                            this.updateWidthAndHeight();
                            this.setMinimized(false);
                            break;
                        }
                        case 1: {
                            this.setMinimized(true);
                            break;
                        }
                    }
                    break;
                }
                case 532: {
                    this.resized = true;
                    this.updateWidthAndHeight();
                    break;
                }
                case 8: {
                    this.appActivate(false, millis);
                    return 0L;
                }
                case 7: {
                    this.appActivate(true, millis);
                    return 0L;
                }
                case 33: {
                    if (this.parent != null) {
                        if (!this.isFocused) {
                            this.grabFocus();
                        }
                        return 3L;
                    }
                    break;
                }
                case 512: {
                    if (this.mouse != null) {
                        final int xPos = (short)(lParam & 0xFFFFL);
                        final int yPos = transformY(this.getHwnd(), (short)(lParam >>> 16));
                        this.mouse.handleMouseMoved(xPos, yPos, millis);
                    }
                    if (!this.mouseInside) {
                        this.mouseInside = true;
                        this.updateCursor();
                        this.nTrackMouseEvent(hwnd);
                    }
                    return 0L;
                }
                case 522: {
                    final int dwheel = (short)(wParam >> 16 & 0xFFFFL);
                    this.handleMouseScrolled(dwheel, millis);
                    return 0L;
                }
                case 513: {
                    this.handleMouseButton(0, 1, millis);
                    return 0L;
                }
                case 514: {
                    this.handleMouseButton(0, 0, millis);
                    return 0L;
                }
                case 516: {
                    this.handleMouseButton(1, 1, millis);
                    return 0L;
                }
                case 517: {
                    this.handleMouseButton(1, 0, millis);
                    return 0L;
                }
                case 519: {
                    this.handleMouseButton(2, 1, millis);
                    return 0L;
                }
                case 520: {
                    this.handleMouseButton(2, 0, millis);
                    return 0L;
                }
                case 524: {
                    if (wParam >> 16 == 1L) {
                        this.handleMouseButton(3, 0, millis);
                    }
                    else {
                        this.handleMouseButton(4, 0, millis);
                    }
                    return 1L;
                }
                case 523: {
                    if ((wParam & 0xFFL) == 0x20L) {
                        this.handleMouseButton(3, 1, millis);
                    }
                    else {
                        this.handleMouseButton(4, 1, millis);
                    }
                    return 1L;
                }
                case 258:
                case 262: {
                    this.handleChar(wParam, lParam, millis);
                    return 0L;
                }
                case 261: {
                    if (wParam == 18L || wParam == 121L) {
                        this.handleKeyButton(wParam, lParam, millis);
                        return 0L;
                    }
                }
                case 257: {
                    if (wParam == 44L && this.keyboard != null && !this.keyboard.isKeyDown(183)) {
                        long fake_lparam = lParam & 0x7FFFFFFFL;
                        fake_lparam &= 0xFFFFFFFFBFFFFFFFL;
                        this.handleKeyButton(wParam, fake_lparam, millis);
                    }
                }
                case 256:
                case 260: {
                    this.handleKeyButton(wParam, lParam, millis);
                    break;
                }
                case 18: {
                    this.close_requested = true;
                    return 0L;
                }
                case 274: {
                    switch ((int)(wParam & 0xFFF0L)) {
                        case 61760:
                        case 61808: {
                            return 0L;
                        }
                        case 61536: {
                            this.close_requested = true;
                            return 0L;
                        }
                        default: {
                            break Label_0919;
                        }
                    }
                    break;
                }
                case 15: {
                    this.is_dirty = true;
                    break;
                }
                case 675: {
                    this.mouseInside = false;
                    break;
                }
                case 31: {
                    nReleaseCapture();
                }
                case 533: {
                    if (this.captureMouse != -1) {
                        this.handleMouseButton(this.captureMouse, 0, millis);
                        this.captureMouse = -1;
                    }
                    return 0L;
                }
                case 71: {
                    if (this.getWindowRect(hwnd, WindowsDisplay.rect_buffer)) {
                        WindowsDisplay.rect.copyFromBuffer(WindowsDisplay.rect_buffer);
                        this.x = WindowsDisplay.rect.left;
                        this.y = WindowsDisplay.rect.top;
                        break;
                    }
                    LWJGLUtil.log("WM_WINDOWPOSCHANGED: Unable to get window rect");
                    break;
                }
                case 127: {
                    this.iconsLoaded = true;
                    break;
                }
            }
        }
        return defWindowProc(hwnd, msg, wParam, lParam);
    }
    
    private native boolean getWindowRect(final long p0, final IntBuffer p1);
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    private native boolean nTrackMouseEvent(final long p0);
    
    public boolean isInsideWindow() {
        return this.mouseInside;
    }
    
    public void setResizable(final boolean resizable) {
        if (this.resizable == resizable) {
            return;
        }
        this.resized = false;
        this.resizable = resizable;
        int style = (int)getWindowLongPtr(this.hwnd, -16);
        final int styleex = (int)getWindowLongPtr(this.hwnd, -20);
        setWindowLongPtr(this.hwnd, -16, style = ((resizable && !Display.isFullscreen()) ? (style | 0x50000) : (style & 0xFFFAFFFF)));
        getGlobalClientRect(this.hwnd, WindowsDisplay.rect);
        WindowsDisplay.rect.copyToBuffer(WindowsDisplay.rect_buffer);
        this.adjustWindowRectEx(WindowsDisplay.rect_buffer, style, false, styleex);
        WindowsDisplay.rect.copyFromBuffer(WindowsDisplay.rect_buffer);
        setWindowPos(this.hwnd, 0L, WindowsDisplay.rect.left, WindowsDisplay.rect.top, WindowsDisplay.rect.right - WindowsDisplay.rect.left, WindowsDisplay.rect.bottom - WindowsDisplay.rect.top, 36L);
        this.updateWidthAndHeight();
    }
    
    private native boolean adjustWindowRectEx(final IntBuffer p0, final int p1, final boolean p2, final int p3);
    
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
        rect_buffer = BufferUtils.createIntBuffer(4);
        rect = new Rect();
        try {
            final Method windowProc = WindowsDisplay.class.getDeclaredMethod("handleMessage", Long.TYPE, Integer.TYPE, Long.TYPE, Long.TYPE, Long.TYPE);
            setWindowProc(windowProc);
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static final class Rect
    {
        public int left;
        public int top;
        public int right;
        public int bottom;
        
        public void copyToBuffer(final IntBuffer buffer) {
            buffer.put(0, this.left).put(1, this.top).put(2, this.right).put(3, this.bottom);
        }
        
        public void copyFromBuffer(final IntBuffer buffer) {
            this.left = buffer.get(0);
            this.top = buffer.get(1);
            this.right = buffer.get(2);
            this.bottom = buffer.get(3);
        }
        
        public void offset(final int offset_x, final int offset_y) {
            this.left += offset_x;
            this.top += offset_y;
            this.right += offset_x;
            this.bottom += offset_y;
        }
        
        public static void intersect(final Rect r1, final Rect r2, final Rect dst) {
            dst.left = Math.max(r1.left, r2.left);
            dst.top = Math.max(r1.top, r2.top);
            dst.right = Math.min(r1.right, r2.right);
            dst.bottom = Math.min(r1.bottom, r2.bottom);
        }
        
        @Override
        public String toString() {
            return "Rect: left = " + this.left + " top = " + this.top + " right = " + this.right + " bottom = " + this.bottom + ", width: " + (this.right - this.left) + ", height: " + (this.bottom - this.top);
        }
    }
}
