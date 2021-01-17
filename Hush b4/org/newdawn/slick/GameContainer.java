// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick;

import org.lwjgl.opengl.Display;
import java.io.IOException;
import org.newdawn.slick.opengl.CursorLoader;
import org.lwjgl.input.Cursor;
import org.newdawn.slick.opengl.ImageData;
import org.lwjgl.Sys;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.ResourceLoader;
import java.util.Properties;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Pbuffer;
import org.lwjgl.opengl.PixelFormat;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.lwjgl.opengl.Drawable;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.gui.GUIContext;

public abstract class GameContainer implements GUIContext
{
    protected static SGL GL;
    protected static Drawable SHARED_DRAWABLE;
    protected long lastFrame;
    protected long lastFPS;
    protected int recordedFPS;
    protected int fps;
    protected boolean running;
    protected int width;
    protected int height;
    protected Game game;
    private Font defaultFont;
    private Graphics graphics;
    protected Input input;
    protected int targetFPS;
    private boolean showFPS;
    protected long minimumLogicInterval;
    protected long storedDelta;
    protected long maximumLogicInterval;
    protected Game lastGame;
    protected boolean clearEachFrame;
    protected boolean paused;
    protected boolean forceExit;
    protected boolean vsync;
    protected boolean smoothDeltas;
    protected int samples;
    protected boolean supportsMultiSample;
    protected boolean alwaysRender;
    protected static boolean stencil;
    
    static {
        GameContainer.GL = Renderer.get();
    }
    
    protected GameContainer(final Game game) {
        this.running = true;
        this.targetFPS = -1;
        this.showFPS = true;
        this.minimumLogicInterval = 1L;
        this.maximumLogicInterval = 0L;
        this.clearEachFrame = true;
        this.forceExit = true;
        this.game = game;
        this.lastFrame = this.getTime();
        getBuildVersion();
        Log.checkVerboseLogSetting();
    }
    
    public static void enableStencil() {
        GameContainer.stencil = true;
    }
    
    public void setDefaultFont(final Font font) {
        if (font != null) {
            this.defaultFont = font;
        }
        else {
            Log.warn("Please provide a non null font");
        }
    }
    
    public void setMultiSample(final int samples) {
        this.samples = samples;
    }
    
    public boolean supportsMultiSample() {
        return this.supportsMultiSample;
    }
    
    public int getSamples() {
        return this.samples;
    }
    
    public void setForceExit(final boolean forceExit) {
        this.forceExit = forceExit;
    }
    
    public void setSmoothDeltas(final boolean smoothDeltas) {
        this.smoothDeltas = smoothDeltas;
    }
    
    public boolean isFullscreen() {
        return false;
    }
    
    public float getAspectRatio() {
        return (float)(this.getWidth() / this.getHeight());
    }
    
    public void setFullscreen(final boolean fullscreen) throws SlickException {
    }
    
    public static void enableSharedContext() throws SlickException {
        try {
            GameContainer.SHARED_DRAWABLE = new Pbuffer(64, 64, new PixelFormat(8, 0, 0), null);
        }
        catch (LWJGLException e) {
            throw new SlickException("Unable to create the pbuffer used for shard context, buffers not supported", e);
        }
    }
    
    public static Drawable getSharedContext() {
        return GameContainer.SHARED_DRAWABLE;
    }
    
    public void setClearEachFrame(final boolean clear) {
        this.clearEachFrame = clear;
    }
    
    public void reinit() throws SlickException {
    }
    
    public void pause() {
        this.setPaused(true);
    }
    
    public void resume() {
        this.setPaused(false);
    }
    
    public boolean isPaused() {
        return this.paused;
    }
    
    public void setPaused(final boolean paused) {
        this.paused = paused;
    }
    
    public boolean getAlwaysRender() {
        return this.alwaysRender;
    }
    
    public void setAlwaysRender(final boolean alwaysRender) {
        this.alwaysRender = alwaysRender;
    }
    
    public static int getBuildVersion() {
        try {
            final Properties props = new Properties();
            props.load(ResourceLoader.getResourceAsStream("version"));
            final int build = Integer.parseInt(props.getProperty("build"));
            Log.info("Slick Build #" + build);
            return build;
        }
        catch (Exception e) {
            Log.error("Unable to determine Slick build number");
            return -1;
        }
    }
    
    @Override
    public Font getDefaultFont() {
        return this.defaultFont;
    }
    
    public boolean isSoundOn() {
        return SoundStore.get().soundsOn();
    }
    
    public boolean isMusicOn() {
        return SoundStore.get().musicOn();
    }
    
    public void setMusicOn(final boolean on) {
        SoundStore.get().setMusicOn(on);
    }
    
    public void setSoundOn(final boolean on) {
        SoundStore.get().setSoundsOn(on);
    }
    
    public float getMusicVolume() {
        return SoundStore.get().getMusicVolume();
    }
    
    public float getSoundVolume() {
        return SoundStore.get().getSoundVolume();
    }
    
    public void setSoundVolume(final float volume) {
        SoundStore.get().setSoundVolume(volume);
    }
    
    public void setMusicVolume(final float volume) {
        SoundStore.get().setMusicVolume(volume);
    }
    
    @Override
    public abstract int getScreenWidth();
    
    @Override
    public abstract int getScreenHeight();
    
    @Override
    public int getWidth() {
        return this.width;
    }
    
    @Override
    public int getHeight() {
        return this.height;
    }
    
    public abstract void setIcon(final String p0) throws SlickException;
    
    public abstract void setIcons(final String[] p0) throws SlickException;
    
    @Override
    public long getTime() {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }
    
    public void sleep(final int milliseconds) {
        final long target = this.getTime() + milliseconds;
        while (this.getTime() < target) {
            try {
                Thread.sleep(1L);
            }
            catch (Exception ex) {}
        }
    }
    
    @Override
    public abstract void setMouseCursor(final String p0, final int p1, final int p2) throws SlickException;
    
    @Override
    public abstract void setMouseCursor(final ImageData p0, final int p1, final int p2) throws SlickException;
    
    public abstract void setMouseCursor(final Image p0, final int p1, final int p2) throws SlickException;
    
    @Override
    public abstract void setMouseCursor(final Cursor p0, final int p1, final int p2) throws SlickException;
    
    public void setAnimatedMouseCursor(final String ref, final int x, final int y, final int width, final int height, final int[] cursorDelays) throws SlickException {
        try {
            final Cursor cursor = CursorLoader.get().getAnimatedCursor(ref, x, y, width, height, cursorDelays);
            this.setMouseCursor(cursor, x, y);
        }
        catch (IOException e) {
            throw new SlickException("Failed to set mouse cursor", e);
        }
        catch (LWJGLException e2) {
            throw new SlickException("Failed to set mouse cursor", e2);
        }
    }
    
    @Override
    public abstract void setDefaultMouseCursor();
    
    @Override
    public Input getInput() {
        return this.input;
    }
    
    public int getFPS() {
        return this.recordedFPS;
    }
    
    public abstract void setMouseGrabbed(final boolean p0);
    
    public abstract boolean isMouseGrabbed();
    
    protected int getDelta() {
        final long time = this.getTime();
        final int delta = (int)(time - this.lastFrame);
        this.lastFrame = time;
        return delta;
    }
    
    protected void updateFPS() {
        if (this.getTime() - this.lastFPS > 1000L) {
            this.lastFPS = this.getTime();
            this.recordedFPS = this.fps;
            this.fps = 0;
        }
        ++this.fps;
    }
    
    public void setMinimumLogicUpdateInterval(final int interval) {
        this.minimumLogicInterval = interval;
    }
    
    public void setMaximumLogicUpdateInterval(final int interval) {
        this.maximumLogicInterval = interval;
    }
    
    protected void updateAndRender(int delta) throws SlickException {
        if (this.smoothDeltas && this.getFPS() != 0) {
            delta = 1000 / this.getFPS();
        }
        this.input.poll(this.width, this.height);
        Music.poll(delta);
        Label_0233: {
            if (!this.paused) {
                this.storedDelta += delta;
                if (this.storedDelta < this.minimumLogicInterval) {
                    break Label_0233;
                }
                try {
                    if (this.maximumLogicInterval == 0L) {
                        this.game.update(this, (int)this.storedDelta);
                        this.storedDelta = 0L;
                        break Label_0233;
                    }
                    final long cycles = this.storedDelta / this.maximumLogicInterval;
                    for (int i = 0; i < cycles; ++i) {
                        this.game.update(this, (int)this.maximumLogicInterval);
                    }
                    final int remainder = (int)(this.storedDelta % this.maximumLogicInterval);
                    if (remainder > this.minimumLogicInterval) {
                        this.game.update(this, (int)(remainder % this.maximumLogicInterval));
                        this.storedDelta = 0L;
                        break Label_0233;
                    }
                    this.storedDelta = remainder;
                    break Label_0233;
                }
                catch (Throwable e) {
                    Log.error(e);
                    throw new SlickException("Game.update() failure - check the game code.");
                }
            }
            this.game.update(this, 0);
        }
        if (this.hasFocus() || this.getAlwaysRender()) {
            if (this.clearEachFrame) {
                GameContainer.GL.glClear(16640);
            }
            GameContainer.GL.glLoadIdentity();
            this.graphics.resetTransform();
            this.graphics.resetFont();
            this.graphics.resetLineWidth();
            this.graphics.setAntiAlias(false);
            try {
                this.game.render(this, this.graphics);
            }
            catch (Throwable e) {
                Log.error(e);
                throw new SlickException("Game.render() failure - check the game code.");
            }
            this.graphics.resetTransform();
            if (this.showFPS) {
                this.defaultFont.drawString(10.0f, 10.0f, "FPS: " + this.recordedFPS);
            }
            GameContainer.GL.flush();
        }
        if (this.targetFPS != -1) {
            Display.sync(this.targetFPS);
        }
    }
    
    public void setUpdateOnlyWhenVisible(final boolean updateOnlyWhenVisible) {
    }
    
    public boolean isUpdatingOnlyWhenVisible() {
        return true;
    }
    
    protected void initGL() {
        Log.info("Starting display " + this.width + "x" + this.height);
        GameContainer.GL.initDisplay(this.width, this.height);
        if (this.input == null) {
            this.input = new Input(this.height);
        }
        this.input.init(this.height);
        if (this.game instanceof InputListener) {
            this.input.removeListener((InputListener)this.game);
            this.input.addListener((InputListener)this.game);
        }
        if (this.graphics != null) {
            this.graphics.setDimensions(this.getWidth(), this.getHeight());
        }
        this.lastGame = this.game;
    }
    
    protected void initSystem() throws SlickException {
        this.initGL();
        this.setMusicVolume(1.0f);
        this.setSoundVolume(1.0f);
        this.graphics = new Graphics(this.width, this.height);
        this.defaultFont = this.graphics.getFont();
    }
    
    protected void enterOrtho() {
        this.enterOrtho(this.width, this.height);
    }
    
    public void setShowFPS(final boolean show) {
        this.showFPS = show;
    }
    
    public boolean isShowingFPS() {
        return this.showFPS;
    }
    
    public void setTargetFrameRate(final int fps) {
        this.targetFPS = fps;
    }
    
    public void setVSync(final boolean vsync) {
        Display.setVSyncEnabled(this.vsync = vsync);
    }
    
    public boolean isVSyncRequested() {
        return this.vsync;
    }
    
    protected boolean running() {
        return this.running;
    }
    
    public void setVerbose(final boolean verbose) {
        Log.setVerbose(verbose);
    }
    
    public void exit() {
        this.running = false;
    }
    
    public abstract boolean hasFocus();
    
    public Graphics getGraphics() {
        return this.graphics;
    }
    
    protected void enterOrtho(final int xsize, final int ysize) {
        GameContainer.GL.enterOrtho(xsize, ysize);
    }
}
