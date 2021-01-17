// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.Sys;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.lwjgl.input.Controllers;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.LWJGLException;
import java.util.Collection;
import java.util.Arrays;
import java.util.HashSet;
import org.lwjgl.LWJGLUtil;
import java.awt.event.ComponentListener;
import java.nio.ByteBuffer;
import java.awt.Canvas;

public final class Display
{
    private static final Thread shutdown_hook;
    private static final DisplayImplementation display_impl;
    private static final DisplayMode initial_mode;
    private static Canvas parent;
    private static DisplayMode current_mode;
    private static int x;
    private static ByteBuffer[] cached_icons;
    private static int y;
    private static int width;
    private static int height;
    private static String title;
    private static boolean fullscreen;
    private static int swap_interval;
    private static DrawableLWJGL drawable;
    private static boolean window_created;
    private static boolean parent_resized;
    private static boolean window_resized;
    private static boolean window_resizable;
    private static float r;
    private static float g;
    private static float b;
    private static final ComponentListener component_listener;
    
    public static Drawable getDrawable() {
        return Display.drawable;
    }
    
    private static DisplayImplementation createDisplayImplementation() {
        switch (LWJGLUtil.getPlatform()) {
            case 1: {
                return new LinuxDisplay();
            }
            case 3: {
                return new WindowsDisplay();
            }
            case 2: {
                return new MacOSXDisplay();
            }
            default: {
                throw new IllegalStateException("Unsupported platform");
            }
        }
    }
    
    private Display() {
    }
    
    public static DisplayMode[] getAvailableDisplayModes() throws LWJGLException {
        synchronized (GlobalLock.lock) {
            final DisplayMode[] unfilteredModes = Display.display_impl.getAvailableDisplayModes();
            if (unfilteredModes == null) {
                return new DisplayMode[0];
            }
            final HashSet<DisplayMode> modes = new HashSet<DisplayMode>(unfilteredModes.length);
            modes.addAll((Collection<?>)Arrays.asList(unfilteredModes));
            final DisplayMode[] filteredModes = new DisplayMode[modes.size()];
            modes.toArray(filteredModes);
            LWJGLUtil.log("Removed " + (unfilteredModes.length - filteredModes.length) + " duplicate displaymodes");
            return filteredModes;
        }
    }
    
    public static DisplayMode getDesktopDisplayMode() {
        return Display.initial_mode;
    }
    
    public static DisplayMode getDisplayMode() {
        return Display.current_mode;
    }
    
    public static void setDisplayMode(final DisplayMode mode) throws LWJGLException {
        synchronized (GlobalLock.lock) {
            if (mode == null) {
                throw new NullPointerException("mode must be non-null");
            }
            final boolean was_fullscreen = isFullscreen();
            Display.current_mode = mode;
            if (isCreated()) {
                destroyWindow();
                try {
                    if (was_fullscreen && !isFullscreen()) {
                        Display.display_impl.resetDisplayMode();
                    }
                    else if (isFullscreen()) {
                        switchDisplayMode();
                    }
                    createWindow();
                    makeCurrentAndSetSwapInterval();
                }
                catch (LWJGLException e) {
                    Display.drawable.destroy();
                    Display.display_impl.resetDisplayMode();
                    throw e;
                }
            }
        }
    }
    
    private static DisplayMode getEffectiveMode() {
        return (!isFullscreen() && Display.parent != null) ? new DisplayMode(Display.parent.getWidth(), Display.parent.getHeight()) : Display.current_mode;
    }
    
    private static int getWindowX() {
        if (isFullscreen() || Display.parent != null) {
            return 0;
        }
        if (Display.x == -1) {
            return Math.max(0, (Display.initial_mode.getWidth() - Display.current_mode.getWidth()) / 2);
        }
        return Display.x;
    }
    
    private static int getWindowY() {
        if (isFullscreen() || Display.parent != null) {
            return 0;
        }
        if (Display.y == -1) {
            return Math.max(0, (Display.initial_mode.getHeight() - Display.current_mode.getHeight()) / 2);
        }
        return Display.y;
    }
    
    private static void createWindow() throws LWJGLException {
        if (Display.window_created) {
            return;
        }
        final Canvas tmp_parent = isFullscreen() ? null : Display.parent;
        if (tmp_parent != null && !tmp_parent.isDisplayable()) {
            throw new LWJGLException("Parent.isDisplayable() must be true");
        }
        if (tmp_parent != null) {
            tmp_parent.addComponentListener(Display.component_listener);
        }
        final DisplayMode mode = getEffectiveMode();
        Display.display_impl.createWindow(Display.drawable, mode, tmp_parent, getWindowX(), getWindowY());
        Display.window_created = true;
        Display.width = getDisplayMode().getWidth();
        Display.height = getDisplayMode().getHeight();
        setTitle(Display.title);
        initControls();
        if (Display.cached_icons != null) {
            setIcon(Display.cached_icons);
        }
        else {
            setIcon(new ByteBuffer[] { LWJGLUtil.LWJGLIcon32x32, LWJGLUtil.LWJGLIcon16x16 });
        }
    }
    
    private static void releaseDrawable() {
        try {
            final Context context = Display.drawable.getContext();
            if (context != null && context.isCurrent()) {
                context.releaseCurrent();
                context.releaseDrawable();
            }
        }
        catch (LWJGLException e) {
            LWJGLUtil.log("Exception occurred while trying to release context: " + e);
        }
    }
    
    private static void destroyWindow() {
        if (!Display.window_created) {
            return;
        }
        if (Display.parent != null) {
            Display.parent.removeComponentListener(Display.component_listener);
        }
        releaseDrawable();
        if (Mouse.isCreated()) {
            Mouse.destroy();
        }
        if (Keyboard.isCreated()) {
            Keyboard.destroy();
        }
        Display.display_impl.destroyWindow();
        Display.window_created = false;
    }
    
    private static void switchDisplayMode() throws LWJGLException {
        if (!Display.current_mode.isFullscreenCapable()) {
            throw new IllegalStateException("Only modes acquired from getAvailableDisplayModes() can be used for fullscreen display");
        }
        Display.display_impl.switchDisplayMode(Display.current_mode);
    }
    
    public static void setDisplayConfiguration(final float gamma, final float brightness, final float contrast) throws LWJGLException {
        synchronized (GlobalLock.lock) {
            if (!isCreated()) {
                throw new LWJGLException("Display not yet created.");
            }
            if (brightness < -1.0f || brightness > 1.0f) {
                throw new IllegalArgumentException("Invalid brightness value");
            }
            if (contrast < 0.0f) {
                throw new IllegalArgumentException("Invalid contrast value");
            }
            final int rampSize = Display.display_impl.getGammaRampLength();
            if (rampSize == 0) {
                throw new LWJGLException("Display configuration not supported");
            }
            final FloatBuffer gammaRamp = BufferUtils.createFloatBuffer(rampSize);
            for (int i = 0; i < rampSize; ++i) {
                final float intensity = i / (float)(rampSize - 1);
                float rampEntry = (float)Math.pow(intensity, gamma);
                rampEntry += brightness;
                rampEntry = (rampEntry - 0.5f) * contrast + 0.5f;
                if (rampEntry > 1.0f) {
                    rampEntry = 1.0f;
                }
                else if (rampEntry < 0.0f) {
                    rampEntry = 0.0f;
                }
                gammaRamp.put(i, rampEntry);
            }
            Display.display_impl.setGammaRamp(gammaRamp);
            LWJGLUtil.log("Gamma set, gamma = " + gamma + ", brightness = " + brightness + ", contrast = " + contrast);
        }
    }
    
    public static void sync(final int fps) {
        Sync.sync(fps);
    }
    
    public static String getTitle() {
        synchronized (GlobalLock.lock) {
            return Display.title;
        }
    }
    
    public static Canvas getParent() {
        synchronized (GlobalLock.lock) {
            return Display.parent;
        }
    }
    
    public static void setParent(final Canvas parent) throws LWJGLException {
        synchronized (GlobalLock.lock) {
            if (Display.parent != parent) {
                Display.parent = parent;
                if (!isCreated()) {
                    return;
                }
                destroyWindow();
                try {
                    if (isFullscreen()) {
                        switchDisplayMode();
                    }
                    else {
                        Display.display_impl.resetDisplayMode();
                    }
                    createWindow();
                    makeCurrentAndSetSwapInterval();
                }
                catch (LWJGLException e) {
                    Display.drawable.destroy();
                    Display.display_impl.resetDisplayMode();
                    throw e;
                }
            }
        }
    }
    
    public static void setFullscreen(final boolean fullscreen) throws LWJGLException {
        setDisplayModeAndFullscreenInternal(fullscreen, Display.current_mode);
    }
    
    public static void setDisplayModeAndFullscreen(final DisplayMode mode) throws LWJGLException {
        setDisplayModeAndFullscreenInternal(mode.isFullscreenCapable(), mode);
    }
    
    private static void setDisplayModeAndFullscreenInternal(final boolean fullscreen, final DisplayMode mode) throws LWJGLException {
        synchronized (GlobalLock.lock) {
            if (mode == null) {
                throw new NullPointerException("mode must be non-null");
            }
            final DisplayMode old_mode = Display.current_mode;
            Display.current_mode = mode;
            final boolean was_fullscreen = isFullscreen();
            Display.fullscreen = fullscreen;
            if (was_fullscreen != isFullscreen() || !mode.equals(old_mode)) {
                if (!isCreated()) {
                    return;
                }
                destroyWindow();
                try {
                    if (isFullscreen()) {
                        switchDisplayMode();
                    }
                    else {
                        Display.display_impl.resetDisplayMode();
                    }
                    createWindow();
                    makeCurrentAndSetSwapInterval();
                }
                catch (LWJGLException e) {
                    Display.drawable.destroy();
                    Display.display_impl.resetDisplayMode();
                    throw e;
                }
            }
        }
    }
    
    public static boolean isFullscreen() {
        synchronized (GlobalLock.lock) {
            return Display.fullscreen && Display.current_mode.isFullscreenCapable();
        }
    }
    
    public static void setTitle(String newTitle) {
        synchronized (GlobalLock.lock) {
            if (newTitle == null) {
                newTitle = "";
            }
            Display.title = newTitle;
            if (isCreated()) {
                Display.display_impl.setTitle(Display.title);
            }
        }
    }
    
    public static boolean isCloseRequested() {
        synchronized (GlobalLock.lock) {
            if (!isCreated()) {
                throw new IllegalStateException("Cannot determine close requested state of uncreated window");
            }
            return Display.display_impl.isCloseRequested();
        }
    }
    
    public static boolean isVisible() {
        synchronized (GlobalLock.lock) {
            if (!isCreated()) {
                throw new IllegalStateException("Cannot determine minimized state of uncreated window");
            }
            return Display.display_impl.isVisible();
        }
    }
    
    public static boolean isActive() {
        synchronized (GlobalLock.lock) {
            if (!isCreated()) {
                throw new IllegalStateException("Cannot determine focused state of uncreated window");
            }
            return Display.display_impl.isActive();
        }
    }
    
    public static boolean isDirty() {
        synchronized (GlobalLock.lock) {
            if (!isCreated()) {
                throw new IllegalStateException("Cannot determine dirty state of uncreated window");
            }
            return Display.display_impl.isDirty();
        }
    }
    
    public static void processMessages() {
        synchronized (GlobalLock.lock) {
            if (!isCreated()) {
                throw new IllegalStateException("Display not created");
            }
            Display.display_impl.update();
        }
        pollDevices();
    }
    
    public static void swapBuffers() throws LWJGLException {
        synchronized (GlobalLock.lock) {
            if (!isCreated()) {
                throw new IllegalStateException("Display not created");
            }
            if (LWJGLUtil.DEBUG) {
                Display.drawable.checkGLError();
            }
            Display.drawable.swapBuffers();
        }
    }
    
    public static void update() {
        update(true);
    }
    
    public static void update(final boolean processMessages) {
        synchronized (GlobalLock.lock) {
            if (!isCreated()) {
                throw new IllegalStateException("Display not created");
            }
            Label_0060: {
                if (!Display.display_impl.isVisible()) {
                    if (!Display.display_impl.isDirty()) {
                        break Label_0060;
                    }
                }
                try {
                    swapBuffers();
                }
                catch (LWJGLException e) {
                    throw new RuntimeException(e);
                }
            }
            Display.window_resized = (!isFullscreen() && Display.parent == null && Display.display_impl.wasResized());
            if (Display.window_resized) {
                Display.width = Display.display_impl.getWidth();
                Display.height = Display.display_impl.getHeight();
            }
            if (Display.parent_resized) {
                reshape();
                Display.parent_resized = false;
                Display.window_resized = true;
            }
            if (processMessages) {
                processMessages();
            }
        }
    }
    
    static void pollDevices() {
        if (Mouse.isCreated()) {
            Mouse.poll();
            Mouse.updateCursor();
        }
        if (Keyboard.isCreated()) {
            Keyboard.poll();
        }
        if (Controllers.isCreated()) {
            Controllers.poll();
        }
    }
    
    public static void releaseContext() throws LWJGLException {
        Display.drawable.releaseContext();
    }
    
    public static boolean isCurrent() throws LWJGLException {
        return Display.drawable.isCurrent();
    }
    
    public static void makeCurrent() throws LWJGLException {
        Display.drawable.makeCurrent();
    }
    
    private static void removeShutdownHook() {
        AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Object>() {
            public Object run() {
                Runtime.getRuntime().removeShutdownHook(Display.shutdown_hook);
                return null;
            }
        });
    }
    
    private static void registerShutdownHook() {
        AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Object>() {
            public Object run() {
                Runtime.getRuntime().addShutdownHook(Display.shutdown_hook);
                return null;
            }
        });
    }
    
    public static void create() throws LWJGLException {
        create(new PixelFormat());
    }
    
    public static void create(final PixelFormat pixel_format) throws LWJGLException {
        synchronized (GlobalLock.lock) {
            create(pixel_format, null, null);
        }
    }
    
    public static void create(final PixelFormat pixel_format, final Drawable shared_drawable) throws LWJGLException {
        synchronized (GlobalLock.lock) {
            create(pixel_format, shared_drawable, null);
        }
    }
    
    public static void create(final PixelFormat pixel_format, final ContextAttribs attribs) throws LWJGLException {
        synchronized (GlobalLock.lock) {
            create(pixel_format, null, attribs);
        }
    }
    
    public static void create(final PixelFormat pixel_format, final Drawable shared_drawable, final ContextAttribs attribs) throws LWJGLException {
        synchronized (GlobalLock.lock) {
            if (isCreated()) {
                throw new IllegalStateException("Only one LWJGL context may be instantiated at any one time.");
            }
            if (pixel_format == null) {
                throw new NullPointerException("pixel_format cannot be null");
            }
            removeShutdownHook();
            registerShutdownHook();
            if (isFullscreen()) {
                switchDisplayMode();
            }
            final DrawableGL drawable = (DrawableGL)(Display.drawable = new DrawableGL() {
                @Override
                public void destroy() {
                    synchronized (GlobalLock.lock) {
                        if (!Display.isCreated()) {
                            return;
                        }
                        releaseDrawable();
                        super.destroy();
                        destroyWindow();
                        Display.x = (Display.y = -1);
                        Display.cached_icons = null;
                        reset();
                        removeShutdownHook();
                    }
                }
            });
            try {
                drawable.setPixelFormat(pixel_format, attribs);
                try {
                    createWindow();
                    try {
                        drawable.context = new ContextGL(drawable.peer_info, attribs, (shared_drawable != null) ? ((DrawableGL)shared_drawable).getContext() : null);
                        try {
                            makeCurrentAndSetSwapInterval();
                            initContext();
                        }
                        catch (LWJGLException e) {
                            drawable.destroy();
                            throw e;
                        }
                    }
                    catch (LWJGLException e) {
                        destroyWindow();
                        throw e;
                    }
                }
                catch (LWJGLException e) {
                    drawable.destroy();
                    throw e;
                }
            }
            catch (LWJGLException e) {
                Display.display_impl.resetDisplayMode();
                throw e;
            }
        }
    }
    
    public static void create(final PixelFormatLWJGL pixel_format) throws LWJGLException {
        synchronized (GlobalLock.lock) {
            create(pixel_format, null, null);
        }
    }
    
    public static void create(final PixelFormatLWJGL pixel_format, final Drawable shared_drawable) throws LWJGLException {
        synchronized (GlobalLock.lock) {
            create(pixel_format, shared_drawable, null);
        }
    }
    
    public static void create(final PixelFormatLWJGL pixel_format, final org.lwjgl.opengles.ContextAttribs attribs) throws LWJGLException {
        synchronized (GlobalLock.lock) {
            create(pixel_format, null, attribs);
        }
    }
    
    public static void create(final PixelFormatLWJGL pixel_format, final Drawable shared_drawable, final org.lwjgl.opengles.ContextAttribs attribs) throws LWJGLException {
        synchronized (GlobalLock.lock) {
            if (isCreated()) {
                throw new IllegalStateException("Only one LWJGL context may be instantiated at any one time.");
            }
            if (pixel_format == null) {
                throw new NullPointerException("pixel_format cannot be null");
            }
            removeShutdownHook();
            registerShutdownHook();
            if (isFullscreen()) {
                switchDisplayMode();
            }
            final DrawableGLES drawable = (DrawableGLES)(Display.drawable = new DrawableGLES() {
                public void setPixelFormat(final PixelFormatLWJGL pf, final ContextAttribs attribs) throws LWJGLException {
                    throw new UnsupportedOperationException();
                }
                
                @Override
                public void destroy() {
                    synchronized (GlobalLock.lock) {
                        if (!Display.isCreated()) {
                            return;
                        }
                        releaseDrawable();
                        super.destroy();
                        destroyWindow();
                        Display.x = (Display.y = -1);
                        Display.cached_icons = null;
                        reset();
                        removeShutdownHook();
                    }
                }
            });
            try {
                drawable.setPixelFormat(pixel_format);
                try {
                    createWindow();
                    try {
                        drawable.createContext(attribs, shared_drawable);
                        try {
                            makeCurrentAndSetSwapInterval();
                            initContext();
                        }
                        catch (LWJGLException e) {
                            drawable.destroy();
                            throw e;
                        }
                    }
                    catch (LWJGLException e) {
                        destroyWindow();
                        throw e;
                    }
                }
                catch (LWJGLException e) {
                    drawable.destroy();
                    throw e;
                }
            }
            catch (LWJGLException e) {
                Display.display_impl.resetDisplayMode();
                throw e;
            }
        }
    }
    
    public static void setInitialBackground(final float red, final float green, final float blue) {
        Display.r = red;
        Display.g = green;
        Display.b = blue;
    }
    
    private static void makeCurrentAndSetSwapInterval() throws LWJGLException {
        makeCurrent();
        try {
            Display.drawable.checkGLError();
        }
        catch (OpenGLException e) {
            LWJGLUtil.log("OpenGL error during context creation: " + e.getMessage());
        }
        setSwapInterval(Display.swap_interval);
    }
    
    private static void initContext() {
        Display.drawable.initContext(Display.r, Display.g, Display.b);
        update();
    }
    
    static DisplayImplementation getImplementation() {
        return Display.display_impl;
    }
    
    static boolean getPrivilegedBoolean(final String property_name) {
        return AccessController.doPrivileged((PrivilegedAction<Boolean>)new PrivilegedAction<Boolean>() {
            public Boolean run() {
                return Boolean.getBoolean(property_name);
            }
        });
    }
    
    static String getPrivilegedString(final String property_name) {
        return AccessController.doPrivileged((PrivilegedAction<String>)new PrivilegedAction<String>() {
            public String run() {
                return System.getProperty(property_name);
            }
        });
    }
    
    private static void initControls() {
        if (!getPrivilegedBoolean("org.lwjgl.opengl.Display.noinput")) {
            if (!Mouse.isCreated() && !getPrivilegedBoolean("org.lwjgl.opengl.Display.nomouse")) {
                try {
                    Mouse.create();
                }
                catch (LWJGLException e) {
                    if (LWJGLUtil.DEBUG) {
                        e.printStackTrace(System.err);
                    }
                    else {
                        LWJGLUtil.log("Failed to create Mouse: " + e);
                    }
                }
            }
            if (!Keyboard.isCreated() && !getPrivilegedBoolean("org.lwjgl.opengl.Display.nokeyboard")) {
                try {
                    Keyboard.create();
                }
                catch (LWJGLException e) {
                    if (LWJGLUtil.DEBUG) {
                        e.printStackTrace(System.err);
                    }
                    else {
                        LWJGLUtil.log("Failed to create Keyboard: " + e);
                    }
                }
            }
        }
    }
    
    public static void destroy() {
        if (isCreated()) {
            Display.drawable.destroy();
        }
    }
    
    private static void reset() {
        Display.display_impl.resetDisplayMode();
        Display.current_mode = Display.initial_mode;
    }
    
    public static boolean isCreated() {
        synchronized (GlobalLock.lock) {
            return Display.window_created;
        }
    }
    
    public static void setSwapInterval(final int value) {
        synchronized (GlobalLock.lock) {
            Display.swap_interval = value;
            if (isCreated()) {
                Display.drawable.setSwapInterval(Display.swap_interval);
            }
        }
    }
    
    public static void setVSyncEnabled(final boolean sync) {
        synchronized (GlobalLock.lock) {
            setSwapInterval(sync ? 1 : 0);
        }
    }
    
    public static void setLocation(final int new_x, final int new_y) {
        synchronized (GlobalLock.lock) {
            Display.x = new_x;
            Display.y = new_y;
            if (isCreated() && !isFullscreen()) {
                reshape();
            }
        }
    }
    
    private static void reshape() {
        final DisplayMode mode = getEffectiveMode();
        Display.display_impl.reshape(getWindowX(), getWindowY(), mode.getWidth(), mode.getHeight());
    }
    
    public static String getAdapter() {
        synchronized (GlobalLock.lock) {
            return Display.display_impl.getAdapter();
        }
    }
    
    public static String getVersion() {
        synchronized (GlobalLock.lock) {
            return Display.display_impl.getVersion();
        }
    }
    
    public static int setIcon(final ByteBuffer[] icons) {
        synchronized (GlobalLock.lock) {
            if (Display.cached_icons != icons) {
                Display.cached_icons = new ByteBuffer[icons.length];
                for (int i = 0; i < icons.length; ++i) {
                    Display.cached_icons[i] = BufferUtils.createByteBuffer(icons[i].capacity());
                    final int old_position = icons[i].position();
                    Display.cached_icons[i].put(icons[i]);
                    icons[i].position(old_position);
                    Display.cached_icons[i].flip();
                }
            }
            if (isCreated() && Display.parent == null) {
                return Display.display_impl.setIcon(Display.cached_icons);
            }
            return 0;
        }
    }
    
    public static void setResizable(final boolean resizable) {
        Display.window_resizable = resizable;
        if (isCreated()) {
            Display.display_impl.setResizable(resizable);
        }
    }
    
    public static boolean isResizable() {
        return Display.window_resizable;
    }
    
    public static boolean wasResized() {
        return Display.window_resized;
    }
    
    public static int getX() {
        if (isFullscreen()) {
            return 0;
        }
        if (Display.parent != null) {
            return Display.parent.getX();
        }
        return Display.display_impl.getX();
    }
    
    public static int getY() {
        if (isFullscreen()) {
            return 0;
        }
        if (Display.parent != null) {
            return Display.parent.getY();
        }
        return Display.display_impl.getY();
    }
    
    public static int getWidth() {
        if (isFullscreen()) {
            return getDisplayMode().getWidth();
        }
        if (Display.parent != null) {
            return Display.parent.getWidth();
        }
        return Display.width;
    }
    
    public static int getHeight() {
        if (isFullscreen()) {
            return getDisplayMode().getHeight();
        }
        if (Display.parent != null) {
            return Display.parent.getHeight();
        }
        return Display.height;
    }
    
    public static float getPixelScaleFactor() {
        return Display.display_impl.getPixelScaleFactor();
    }
    
    static {
        shutdown_hook = new Thread() {
            @Override
            public void run() {
                reset();
            }
        };
        Display.x = -1;
        Display.y = -1;
        Display.width = 0;
        Display.height = 0;
        Display.title = "Game";
        component_listener = new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                synchronized (GlobalLock.lock) {
                    Display.parent_resized = true;
                }
            }
        };
        Sys.initialize();
        display_impl = createDisplayImplementation();
        try {
            Display.current_mode = (initial_mode = Display.display_impl.init());
            LWJGLUtil.log("Initial mode: " + Display.initial_mode);
        }
        catch (LWJGLException e) {
            throw new RuntimeException(e);
        }
    }
}
