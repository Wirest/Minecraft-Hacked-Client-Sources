// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferUtils;
import org.lwjgl.MemoryUtil;
import java.util.ArrayList;
import java.util.List;
import java.nio.FloatBuffer;
import java.awt.Component;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.LWJGLException;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;
import java.awt.Robot;
import java.awt.Canvas;

final class MacOSXDisplay implements DisplayImplementation
{
    private static final int PBUFFER_HANDLE_SIZE = 24;
    private static final int GAMMA_LENGTH = 256;
    private Canvas canvas;
    private Robot robot;
    private MacOSXMouseEventQueue mouse_queue;
    private KeyboardEventQueue keyboard_queue;
    private DisplayMode requested_mode;
    private MacOSXNativeMouse mouse;
    private MacOSXNativeKeyboard keyboard;
    private ByteBuffer window;
    private ByteBuffer context;
    private boolean skipViewportValue;
    private static final IntBuffer current_viewport;
    private boolean mouseInsideWindow;
    private boolean close_requested;
    private boolean native_mode;
    private boolean updateNativeCursor;
    private long currentNativeCursor;
    private boolean enableHighDPI;
    private float scaleFactor;
    
    MacOSXDisplay() {
        this.skipViewportValue = false;
        this.native_mode = true;
        this.updateNativeCursor = false;
        this.currentNativeCursor = 0L;
        this.enableHighDPI = false;
        this.scaleFactor = 1.0f;
    }
    
    private native ByteBuffer nCreateWindow(final int p0, final int p1, final int p2, final int p3, final boolean p4, final boolean p5, final boolean p6, final boolean p7, final boolean p8, final boolean p9, final ByteBuffer p10, final ByteBuffer p11) throws LWJGLException;
    
    private native Object nGetCurrentDisplayMode();
    
    private native void nGetDisplayModes(final Object p0);
    
    private native boolean nIsMiniaturized(final ByteBuffer p0);
    
    private native boolean nIsFocused(final ByteBuffer p0);
    
    private native void nSetResizable(final ByteBuffer p0, final boolean p1);
    
    private native void nResizeWindow(final ByteBuffer p0, final int p1, final int p2, final int p3, final int p4);
    
    private native boolean nWasResized(final ByteBuffer p0);
    
    private native int nGetX(final ByteBuffer p0);
    
    private native int nGetY(final ByteBuffer p0);
    
    private native int nGetWidth(final ByteBuffer p0);
    
    private native int nGetHeight(final ByteBuffer p0);
    
    private native boolean nIsNativeMode(final ByteBuffer p0);
    
    private static boolean isUndecorated() {
        return Display.getPrivilegedBoolean("org.lwjgl.opengl.Window.undecorated");
    }
    
    public void createWindow(final DrawableLWJGL drawable, final DisplayMode mode, final Canvas parent, final int x, final int y) throws LWJGLException {
        final boolean fullscreen = Display.isFullscreen();
        final boolean resizable = Display.isResizable();
        final boolean parented = parent != null && !fullscreen;
        final boolean enableFullscreenModeAPI = LWJGLUtil.isMacOSXEqualsOrBetterThan(10, 7) && parent == null && !Display.getPrivilegedBoolean("org.lwjgl.opengl.Display.disableOSXFullscreenModeAPI");
        this.enableHighDPI = (LWJGLUtil.isMacOSXEqualsOrBetterThan(10, 7) && parent == null && (Display.getPrivilegedBoolean("org.lwjgl.opengl.Display.enableHighDPI") || fullscreen));
        if (parented) {
            this.canvas = parent;
        }
        else {
            this.canvas = null;
        }
        this.close_requested = false;
        final DrawableGL gl_drawable = (DrawableGL)Display.getDrawable();
        final PeerInfo peer_info = gl_drawable.peer_info;
        final ByteBuffer peer_handle = peer_info.lockAndGetHandle();
        final ByteBuffer window_handle = parented ? ((MacOSXCanvasPeerInfo)peer_info).window_handle : this.window;
        try {
            this.window = this.nCreateWindow(x, y, mode.getWidth(), mode.getHeight(), fullscreen, isUndecorated(), resizable, parented, enableFullscreenModeAPI, this.enableHighDPI, peer_handle, window_handle);
            if (fullscreen) {
                this.skipViewportValue = true;
                MacOSXDisplay.current_viewport.put(2, mode.getWidth());
                MacOSXDisplay.current_viewport.put(3, mode.getHeight());
            }
            if (!(this.native_mode = this.nIsNativeMode(peer_handle))) {
                this.robot = AWTUtil.createRobot(this.canvas);
            }
        }
        catch (LWJGLException e) {
            this.destroyWindow();
            throw e;
        }
        finally {
            peer_info.unlock();
        }
    }
    
    public void doHandleQuit() {
        synchronized (this) {
            this.close_requested = true;
        }
    }
    
    public void mouseInsideWindow(final boolean inside) {
        synchronized (this) {
            this.mouseInsideWindow = inside;
        }
        this.updateNativeCursor = true;
    }
    
    public void setScaleFactor(final float scale) {
        synchronized (this) {
            this.scaleFactor = scale;
        }
    }
    
    public native void nDestroyCALayer(final ByteBuffer p0);
    
    public native void nDestroyWindow(final ByteBuffer p0);
    
    public void destroyWindow() {
        if (!this.native_mode) {
            final DrawableGL gl_drawable = (DrawableGL)Display.getDrawable();
            final PeerInfo peer_info = gl_drawable.peer_info;
            if (peer_info != null) {
                final ByteBuffer peer_handle = peer_info.getHandle();
                this.nDestroyCALayer(peer_handle);
            }
            this.robot = null;
        }
        this.nDestroyWindow(this.window);
    }
    
    public int getGammaRampLength() {
        return 256;
    }
    
    public native void setGammaRamp(final FloatBuffer p0) throws LWJGLException;
    
    public String getAdapter() {
        return null;
    }
    
    public String getVersion() {
        return null;
    }
    
    private static boolean equals(final DisplayMode mode1, final DisplayMode mode2) {
        return mode1.getWidth() == mode2.getWidth() && mode1.getHeight() == mode2.getHeight() && mode1.getBitsPerPixel() == mode2.getBitsPerPixel() && mode1.getFrequency() == mode2.getFrequency();
    }
    
    public void switchDisplayMode(final DisplayMode mode) throws LWJGLException {
        final DisplayMode[] arr$;
        final DisplayMode[] modes = arr$ = this.getAvailableDisplayModes();
        for (final DisplayMode available_mode : arr$) {
            if (equals(available_mode, mode)) {
                this.requested_mode = available_mode;
                return;
            }
        }
        throw new LWJGLException(mode + " is not supported");
    }
    
    public void resetDisplayMode() {
        this.requested_mode = null;
        this.restoreGamma();
    }
    
    private native void restoreGamma();
    
    public Object createDisplayMode(final int width, final int height, final int bitsPerPixel, final int refreshRate) {
        return new DisplayMode(width, height, bitsPerPixel, refreshRate);
    }
    
    public DisplayMode init() throws LWJGLException {
        return (DisplayMode)this.nGetCurrentDisplayMode();
    }
    
    public void addDisplayMode(final Object modesList, final int width, final int height, final int bitsPerPixel, final int refreshRate) {
        final List<DisplayMode> modes = (List<DisplayMode>)modesList;
        final DisplayMode displayMode = new DisplayMode(width, height, bitsPerPixel, refreshRate);
        modes.add(displayMode);
    }
    
    public DisplayMode[] getAvailableDisplayModes() throws LWJGLException {
        final List<DisplayMode> modes = new ArrayList<DisplayMode>();
        this.nGetDisplayModes(modes);
        modes.add(Display.getDesktopDisplayMode());
        return modes.toArray(new DisplayMode[modes.size()]);
    }
    
    private native void nSetTitle(final ByteBuffer p0, final ByteBuffer p1);
    
    public void setTitle(final String title) {
        final ByteBuffer buffer = MemoryUtil.encodeUTF8(title);
        this.nSetTitle(this.window, buffer);
    }
    
    public boolean isCloseRequested() {
        final boolean result;
        synchronized (this) {
            result = this.close_requested;
            this.close_requested = false;
        }
        return result;
    }
    
    public boolean isVisible() {
        return true;
    }
    
    public boolean isActive() {
        if (this.native_mode) {
            return this.nIsFocused(this.window);
        }
        return Display.getParent().hasFocus();
    }
    
    public Canvas getCanvas() {
        return this.canvas;
    }
    
    public boolean isDirty() {
        return false;
    }
    
    public PeerInfo createPeerInfo(final PixelFormat pixel_format, final ContextAttribs attribs) throws LWJGLException {
        try {
            return new MacOSXDisplayPeerInfo(pixel_format, attribs, true);
        }
        catch (LWJGLException e) {
            return new MacOSXDisplayPeerInfo(pixel_format, attribs, false);
        }
    }
    
    public void update() {
        final boolean should_update = true;
        final DrawableGL drawable = (DrawableGL)Display.getDrawable();
        if (should_update) {
            if (this.skipViewportValue) {
                this.skipViewportValue = false;
            }
            else {
                GL11.glGetInteger(2978, MacOSXDisplay.current_viewport);
            }
            drawable.context.update();
            GL11.glViewport(MacOSXDisplay.current_viewport.get(0), MacOSXDisplay.current_viewport.get(1), MacOSXDisplay.current_viewport.get(2), MacOSXDisplay.current_viewport.get(3));
        }
        if (this.native_mode && this.updateNativeCursor) {
            this.updateNativeCursor = false;
            try {
                this.setNativeCursor(this.currentNativeCursor);
            }
            catch (LWJGLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void reshape(final int x, final int y, final int width, final int height) {
    }
    
    public boolean hasWheel() {
        return AWTUtil.hasWheel();
    }
    
    public int getButtonCount() {
        return AWTUtil.getButtonCount();
    }
    
    public void createMouse() throws LWJGLException {
        if (this.native_mode) {
            (this.mouse = new MacOSXNativeMouse(this, this.window)).register();
        }
        else {
            (this.mouse_queue = new MacOSXMouseEventQueue(this.canvas)).register();
        }
    }
    
    public void destroyMouse() {
        if (this.native_mode) {
            try {
                MacOSXNativeMouse.setCursor(0L);
            }
            catch (LWJGLException ex) {}
            this.grabMouse(false);
            if (this.mouse != null) {
                this.mouse.unregister();
            }
            this.mouse = null;
        }
        else {
            if (this.mouse_queue != null) {
                MacOSXMouseEventQueue.nGrabMouse(false);
                this.mouse_queue.unregister();
            }
            this.mouse_queue = null;
        }
    }
    
    public void pollMouse(final IntBuffer coord_buffer, final ByteBuffer buttons_buffer) {
        if (this.native_mode) {
            this.mouse.poll(coord_buffer, buttons_buffer);
        }
        else {
            this.mouse_queue.poll(coord_buffer, buttons_buffer);
        }
    }
    
    public void readMouse(final ByteBuffer buffer) {
        if (this.native_mode) {
            this.mouse.copyEvents(buffer);
        }
        else {
            this.mouse_queue.copyEvents(buffer);
        }
    }
    
    public void grabMouse(final boolean grab) {
        if (this.native_mode) {
            this.mouse.setGrabbed(grab);
        }
        else {
            this.mouse_queue.setGrabbed(grab);
        }
    }
    
    public int getNativeCursorCapabilities() {
        if (this.native_mode) {
            return 7;
        }
        return AWTUtil.getNativeCursorCapabilities();
    }
    
    public void setCursorPosition(final int x, final int y) {
        if (this.native_mode && this.mouse != null) {
            this.mouse.setCursorPosition(x, y);
        }
    }
    
    public void setNativeCursor(final Object handle) throws LWJGLException {
        if (this.native_mode) {
            this.currentNativeCursor = getCursorHandle(handle);
            if (Display.isCreated()) {
                if (this.mouseInsideWindow) {
                    MacOSXNativeMouse.setCursor(this.currentNativeCursor);
                }
                else {
                    MacOSXNativeMouse.setCursor(0L);
                }
            }
        }
    }
    
    public int getMinCursorSize() {
        return 1;
    }
    
    public int getMaxCursorSize() {
        final DisplayMode dm = Display.getDesktopDisplayMode();
        return Math.min(dm.getWidth(), dm.getHeight()) / 2;
    }
    
    public void createKeyboard() throws LWJGLException {
        if (this.native_mode) {
            (this.keyboard = new MacOSXNativeKeyboard(this.window)).register();
        }
        else {
            (this.keyboard_queue = new KeyboardEventQueue(this.canvas)).register();
        }
    }
    
    public void destroyKeyboard() {
        if (this.native_mode) {
            if (this.keyboard != null) {
                this.keyboard.unregister();
            }
            this.keyboard = null;
        }
        else {
            if (this.keyboard_queue != null) {
                this.keyboard_queue.unregister();
            }
            this.keyboard_queue = null;
        }
    }
    
    public void pollKeyboard(final ByteBuffer keyDownBuffer) {
        if (this.native_mode) {
            this.keyboard.poll(keyDownBuffer);
        }
        else {
            this.keyboard_queue.poll(keyDownBuffer);
        }
    }
    
    public void readKeyboard(final ByteBuffer buffer) {
        if (this.native_mode) {
            this.keyboard.copyEvents(buffer);
        }
        else {
            this.keyboard_queue.copyEvents(buffer);
        }
    }
    
    public Object createCursor(final int width, final int height, final int xHotspot, final int yHotspot, final int numImages, final IntBuffer images, final IntBuffer delays) throws LWJGLException {
        if (this.native_mode) {
            final long cursor = MacOSXNativeMouse.createCursor(width, height, xHotspot, yHotspot, numImages, images, delays);
            return cursor;
        }
        return AWTUtil.createCursor(width, height, xHotspot, yHotspot, numImages, images, delays);
    }
    
    public void destroyCursor(final Object cursor_handle) {
        final long handle = getCursorHandle(cursor_handle);
        if (this.currentNativeCursor == handle) {
            this.currentNativeCursor = 0L;
        }
        MacOSXNativeMouse.destroyCursor(handle);
    }
    
    private static long getCursorHandle(final Object cursor_handle) {
        return (long)((cursor_handle != null) ? cursor_handle : 0L);
    }
    
    public int getPbufferCapabilities() {
        return 1;
    }
    
    public boolean isBufferLost(final PeerInfo handle) {
        return false;
    }
    
    public PeerInfo createPbuffer(final int width, final int height, final PixelFormat pixel_format, final ContextAttribs attribs, final IntBuffer pixelFormatCaps, final IntBuffer pBufferAttribs) throws LWJGLException {
        return new MacOSXPbufferPeerInfo(width, height, pixel_format, attribs);
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
    
    public int setIcon(final ByteBuffer[] icons) {
        return 0;
    }
    
    public int getX() {
        return this.nGetX(this.window);
    }
    
    public int getY() {
        return this.nGetY(this.window);
    }
    
    public int getWidth() {
        return this.nGetWidth(this.window);
    }
    
    public int getHeight() {
        return this.nGetHeight(this.window);
    }
    
    public boolean isInsideWindow() {
        return this.mouseInsideWindow;
    }
    
    public void setResizable(final boolean resizable) {
        this.nSetResizable(this.window, resizable);
    }
    
    public boolean wasResized() {
        return this.nWasResized(this.window);
    }
    
    public float getPixelScaleFactor() {
        return (this.enableHighDPI && !Display.isFullscreen()) ? this.scaleFactor : 1.0f;
    }
    
    static {
        current_viewport = BufferUtils.createIntBuffer(16);
    }
}
