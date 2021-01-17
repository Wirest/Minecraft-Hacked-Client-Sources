// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick;

import java.io.IOException;
import java.io.OutputStream;
import org.newdawn.slick.opengl.LoadableImageData;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.opengl.ImageIOImageData;
import org.newdawn.slick.opengl.TGAImageData;
import org.lwjgl.openal.AL;
import org.lwjgl.Sys;
import org.lwjgl.opengl.PixelFormat;
import org.newdawn.slick.openal.SoundStore;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import org.newdawn.slick.opengl.ImageData;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.CursorLoader;
import org.lwjgl.LWJGLException;
import org.newdawn.slick.opengl.InternalTextureLoader;
import java.security.AccessController;
import org.newdawn.slick.util.Log;
import org.lwjgl.opengl.Display;
import java.security.PrivilegedAction;
import org.lwjgl.opengl.DisplayMode;

public class AppGameContainer extends GameContainer
{
    protected DisplayMode originalDisplayMode;
    protected DisplayMode targetDisplayMode;
    protected boolean updateOnlyOnVisible;
    protected boolean alphaSupport;
    
    static {
        AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction() {
            @Override
            public Object run() {
                try {
                    Display.getDisplayMode();
                }
                catch (Exception e) {
                    Log.error(e);
                }
                return null;
            }
        });
    }
    
    public AppGameContainer(final Game game) throws SlickException {
        this(game, 640, 480, false);
    }
    
    public AppGameContainer(final Game game, final int width, final int height, final boolean fullscreen) throws SlickException {
        super(game);
        this.updateOnlyOnVisible = true;
        this.alphaSupport = false;
        this.originalDisplayMode = Display.getDisplayMode();
        this.setDisplayMode(width, height, fullscreen);
    }
    
    public boolean supportsAlphaInBackBuffer() {
        return this.alphaSupport;
    }
    
    public void setTitle(final String title) {
        Display.setTitle(title);
    }
    
    public void setDisplayMode(final int width, final int height, final boolean fullscreen) throws SlickException {
        if (this.width == width && this.height == height && this.isFullscreen() == fullscreen) {
            return;
        }
        try {
            this.targetDisplayMode = null;
            if (fullscreen) {
                final DisplayMode[] modes = Display.getAvailableDisplayModes();
                int freq = 0;
                for (int i = 0; i < modes.length; ++i) {
                    final DisplayMode current = modes[i];
                    if (current.getWidth() == width && current.getHeight() == height) {
                        if ((this.targetDisplayMode == null || current.getFrequency() >= freq) && (this.targetDisplayMode == null || current.getBitsPerPixel() > this.targetDisplayMode.getBitsPerPixel())) {
                            this.targetDisplayMode = current;
                            freq = this.targetDisplayMode.getFrequency();
                        }
                        if (current.getBitsPerPixel() == this.originalDisplayMode.getBitsPerPixel() && current.getFrequency() == this.originalDisplayMode.getFrequency()) {
                            this.targetDisplayMode = current;
                            break;
                        }
                    }
                }
            }
            else {
                this.targetDisplayMode = new DisplayMode(width, height);
            }
            if (this.targetDisplayMode == null) {
                throw new SlickException("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
            }
            this.width = width;
            this.height = height;
            Display.setDisplayMode(this.targetDisplayMode);
            Display.setFullscreen(fullscreen);
            if (Display.isCreated()) {
                this.initGL();
                this.enterOrtho();
            }
            if (this.targetDisplayMode.getBitsPerPixel() == 16) {
                InternalTextureLoader.get().set16BitMode();
            }
        }
        catch (LWJGLException e) {
            throw new SlickException("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen, e);
        }
        this.getDelta();
    }
    
    @Override
    public boolean isFullscreen() {
        return Display.isFullscreen();
    }
    
    @Override
    public void setFullscreen(final boolean fullscreen) throws SlickException {
        if (this.isFullscreen() == fullscreen) {
            return;
        }
        Label_0059: {
            if (!fullscreen) {
                try {
                    Display.setFullscreen(fullscreen);
                    break Label_0059;
                }
                catch (LWJGLException e) {
                    throw new SlickException("Unable to set fullscreen=" + fullscreen, e);
                }
            }
            this.setDisplayMode(this.width, this.height, fullscreen);
        }
        this.getDelta();
    }
    
    @Override
    public void setMouseCursor(final String ref, final int hotSpotX, final int hotSpotY) throws SlickException {
        try {
            final Cursor cursor = CursorLoader.get().getCursor(ref, hotSpotX, hotSpotY);
            Mouse.setNativeCursor(cursor);
        }
        catch (Throwable e) {
            Log.error("Failed to load and apply cursor.", e);
            throw new SlickException("Failed to set mouse cursor", e);
        }
    }
    
    @Override
    public void setMouseCursor(final ImageData data, final int hotSpotX, final int hotSpotY) throws SlickException {
        try {
            final Cursor cursor = CursorLoader.get().getCursor(data, hotSpotX, hotSpotY);
            Mouse.setNativeCursor(cursor);
        }
        catch (Throwable e) {
            Log.error("Failed to load and apply cursor.", e);
            throw new SlickException("Failed to set mouse cursor", e);
        }
    }
    
    @Override
    public void setMouseCursor(final Cursor cursor, final int hotSpotX, final int hotSpotY) throws SlickException {
        try {
            Mouse.setNativeCursor(cursor);
        }
        catch (Throwable e) {
            Log.error("Failed to load and apply cursor.", e);
            throw new SlickException("Failed to set mouse cursor", e);
        }
    }
    
    private int get2Fold(final int fold) {
        int ret;
        for (ret = 2; ret < fold; ret *= 2) {}
        return ret;
    }
    
    @Override
    public void setMouseCursor(final Image image, final int hotSpotX, final int hotSpotY) throws SlickException {
        try {
            final Image temp = new Image(this.get2Fold(image.getWidth()), this.get2Fold(image.getHeight()));
            final Graphics g = temp.getGraphics();
            final ByteBuffer buffer = BufferUtils.createByteBuffer(temp.getWidth() * temp.getHeight() * 4);
            g.drawImage(image.getFlippedCopy(false, true), 0.0f, 0.0f);
            g.flush();
            g.getArea(0, 0, temp.getWidth(), temp.getHeight(), buffer);
            final Cursor cursor = CursorLoader.get().getCursor(buffer, hotSpotX, hotSpotY, temp.getWidth(), image.getHeight());
            Mouse.setNativeCursor(cursor);
        }
        catch (Throwable e) {
            Log.error("Failed to load and apply cursor.", e);
            throw new SlickException("Failed to set mouse cursor", e);
        }
    }
    
    @Override
    public void reinit() throws SlickException {
        InternalTextureLoader.get().clear();
        SoundStore.get().clear();
        this.initSystem();
        this.enterOrtho();
        try {
            this.game.init(this);
        }
        catch (SlickException e) {
            Log.error(e);
            this.running = false;
        }
    }
    
    private void tryCreateDisplay(final PixelFormat format) throws LWJGLException {
        if (AppGameContainer.SHARED_DRAWABLE == null) {
            Display.create(format);
        }
        else {
            Display.create(format, AppGameContainer.SHARED_DRAWABLE);
        }
    }
    
    public void start() throws SlickException {
        try {
            this.setup();
            this.getDelta();
            while (this.running()) {
                this.gameLoop();
            }
        }
        finally {
            this.destroy();
        }
        this.destroy();
        if (this.forceExit) {
            System.exit(0);
        }
    }
    
    protected void setup() throws SlickException {
        if (this.targetDisplayMode == null) {
            this.setDisplayMode(640, 480, false);
        }
        Display.setTitle(this.game.getTitle());
        Log.info("LWJGL Version: " + Sys.getVersion());
        Log.info("OriginalDisplayMode: " + this.originalDisplayMode);
        Log.info("TargetDisplayMode: " + this.targetDisplayMode);
        AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction() {
            @Override
            public Object run() {
                try {
                    final PixelFormat format = new PixelFormat(8, 8, AppGameContainer.stencil ? 8 : 0, AppGameContainer.this.samples);
                    AppGameContainer.this.tryCreateDisplay(format);
                    AppGameContainer.this.supportsMultiSample = true;
                }
                catch (Exception e4) {
                    Display.destroy();
                    try {
                        final PixelFormat format2 = new PixelFormat(8, 8, AppGameContainer.stencil ? 8 : 0);
                        AppGameContainer.this.tryCreateDisplay(format2);
                        AppGameContainer.this.alphaSupport = false;
                    }
                    catch (Exception e5) {
                        Display.destroy();
                        try {
                            AppGameContainer.this.tryCreateDisplay(new PixelFormat());
                        }
                        catch (Exception e3) {
                            Log.error(e3);
                        }
                    }
                }
                return null;
            }
        });
        if (!Display.isCreated()) {
            throw new SlickException("Failed to initialise the LWJGL display");
        }
        this.initSystem();
        this.enterOrtho();
        try {
            this.getInput().initControllers();
        }
        catch (SlickException e) {
            Log.info("Controllers not available");
        }
        catch (Throwable e2) {
            Log.info("Controllers not available");
        }
        try {
            this.game.init(this);
        }
        catch (SlickException e) {
            Log.error(e);
            this.running = false;
        }
    }
    
    protected void gameLoop() throws SlickException {
        final int delta = this.getDelta();
        if (!Display.isVisible() && this.updateOnlyOnVisible) {
            try {
                Thread.sleep(100L);
            }
            catch (Exception ex) {}
        }
        else {
            try {
                this.updateAndRender(delta);
            }
            catch (SlickException e) {
                Log.error(e);
                this.running = false;
                return;
            }
        }
        this.updateFPS();
        Display.update();
        if (Display.isCloseRequested() && this.game.closeRequested()) {
            this.running = false;
        }
    }
    
    @Override
    public void setUpdateOnlyWhenVisible(final boolean updateOnlyWhenVisible) {
        this.updateOnlyOnVisible = updateOnlyWhenVisible;
    }
    
    @Override
    public boolean isUpdatingOnlyWhenVisible() {
        return this.updateOnlyOnVisible;
    }
    
    @Override
    public void setIcon(final String ref) throws SlickException {
        this.setIcons(new String[] { ref });
    }
    
    @Override
    public void setMouseGrabbed(final boolean grabbed) {
        Mouse.setGrabbed(grabbed);
    }
    
    @Override
    public boolean isMouseGrabbed() {
        return Mouse.isGrabbed();
    }
    
    @Override
    public boolean hasFocus() {
        return Display.isActive();
    }
    
    @Override
    public int getScreenHeight() {
        return this.originalDisplayMode.getHeight();
    }
    
    @Override
    public int getScreenWidth() {
        return this.originalDisplayMode.getWidth();
    }
    
    public void destroy() {
        Display.destroy();
        AL.destroy();
    }
    
    @Override
    public void setIcons(final String[] refs) throws SlickException {
        final ByteBuffer[] bufs = new ByteBuffer[refs.length];
        for (int i = 0; i < refs.length; ++i) {
            boolean flip = true;
            LoadableImageData data;
            if (refs[i].endsWith(".tga")) {
                data = new TGAImageData();
            }
            else {
                flip = false;
                data = new ImageIOImageData();
            }
            try {
                bufs[i] = data.loadImage(ResourceLoader.getResourceAsStream(refs[i]), flip, false, null);
            }
            catch (Exception e) {
                Log.error(e);
                throw new SlickException("Failed to set the icon");
            }
        }
        Display.setIcon(bufs);
    }
    
    @Override
    public void setDefaultMouseCursor() {
        try {
            Mouse.setNativeCursor(null);
        }
        catch (LWJGLException e) {
            Log.error("Failed to reset mouse cursor", e);
        }
    }
    
    private class NullOutputStream extends OutputStream
    {
        @Override
        public void write(final int b) throws IOException {
        }
    }
}
