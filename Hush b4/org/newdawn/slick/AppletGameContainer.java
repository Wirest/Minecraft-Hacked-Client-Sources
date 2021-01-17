// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick;

import java.awt.GridLayout;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.awt.Label;
import java.awt.Font;
import java.awt.Color;
import java.awt.TextArea;
import java.awt.Panel;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.ImageData;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Cursor;
import org.newdawn.slick.opengl.CursorLoader;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.InternalTextureLoader;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.PixelFormat;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.util.Log;
import java.awt.Component;
import java.awt.Canvas;
import java.applet.Applet;

public class AppletGameContainer extends Applet
{
    protected ContainerPanel canvas;
    protected Container container;
    protected Canvas displayParent;
    protected Thread gameThread;
    protected boolean alphaSupport;
    
    public AppletGameContainer() {
        this.alphaSupport = true;
    }
    
    @Override
    public void destroy() {
        if (this.displayParent != null) {
            this.remove(this.displayParent);
        }
        super.destroy();
        Log.info("Clear up");
    }
    
    private void destroyLWJGL() {
        this.container.stopApplet();
        try {
            this.gameThread.join();
        }
        catch (InterruptedException e) {
            Log.error(e);
        }
    }
    
    @Override
    public void start() {
    }
    
    public void startLWJGL() {
        if (this.gameThread != null) {
            return;
        }
        (this.gameThread = new Thread() {
            @Override
            public void run() {
                try {
                    AppletGameContainer.this.canvas.start();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    if (Display.isCreated()) {
                        Display.destroy();
                    }
                    AppletGameContainer.this.displayParent.setVisible(false);
                    AppletGameContainer.this.add(new ConsolePanel(e));
                    AppletGameContainer.this.validate();
                }
            }
        }).start();
    }
    
    @Override
    public void stop() {
    }
    
    @Override
    public void init() {
        this.removeAll();
        this.setLayout(new BorderLayout());
        this.setIgnoreRepaint(true);
        try {
            final Game game = (Game)Class.forName(this.getParameter("game")).newInstance();
            this.container = new Container(game);
            this.canvas = new ContainerPanel(this.container);
            (this.displayParent = new Canvas() {
                @Override
                public final void addNotify() {
                    super.addNotify();
                    AppletGameContainer.this.startLWJGL();
                }
                
                @Override
                public final void removeNotify() {
                    AppletGameContainer.this.destroyLWJGL();
                    super.removeNotify();
                }
            }).setSize(this.getWidth(), this.getHeight());
            this.add(this.displayParent);
            this.displayParent.setFocusable(true);
            this.displayParent.requestFocus();
            this.displayParent.setIgnoreRepaint(true);
            this.setVisible(true);
        }
        catch (Exception e) {
            Log.error(e);
            throw new RuntimeException("Unable to create game container");
        }
    }
    
    public GameContainer getContainer() {
        return this.container;
    }
    
    public class ContainerPanel
    {
        private Container container;
        
        public ContainerPanel(final Container container) {
            this.container = container;
        }
        
        private void createDisplay() throws Exception {
            try {
                Display.create(new PixelFormat(8, 8, GameContainer.stencil ? 8 : 0));
                AppletGameContainer.this.alphaSupport = true;
            }
            catch (Exception e) {
                AppletGameContainer.this.alphaSupport = false;
                Display.destroy();
                Display.create();
            }
        }
        
        public void start() throws Exception {
            Display.setParent(AppletGameContainer.this.displayParent);
            Display.setVSyncEnabled(true);
            try {
                this.createDisplay();
            }
            catch (LWJGLException e) {
                e.printStackTrace();
                Thread.sleep(1000L);
                this.createDisplay();
            }
            this.initGL();
            AppletGameContainer.this.displayParent.requestFocus();
            this.container.runloop();
        }
        
        protected void initGL() {
            try {
                InternalTextureLoader.get().clear();
                SoundStore.get().clear();
                this.container.initApplet();
            }
            catch (Exception e) {
                Log.error(e);
                this.container.stopApplet();
            }
        }
    }
    
    public class Container extends GameContainer
    {
        public Container(final Game game) {
            super(game);
            this.width = AppletGameContainer.this.getWidth();
            this.height = AppletGameContainer.this.getHeight();
        }
        
        public void initApplet() throws SlickException {
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
            this.game.init(this);
            this.getDelta();
        }
        
        public boolean isRunning() {
            return this.running;
        }
        
        public void stopApplet() {
            this.running = false;
        }
        
        @Override
        public int getScreenHeight() {
            return 0;
        }
        
        @Override
        public int getScreenWidth() {
            return 0;
        }
        
        public boolean supportsAlphaInBackBuffer() {
            return AppletGameContainer.this.alphaSupport;
        }
        
        @Override
        public boolean hasFocus() {
            return true;
        }
        
        public Applet getApplet() {
            return AppletGameContainer.this;
        }
        
        @Override
        public void setIcon(final String ref) throws SlickException {
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
                final Cursor cursor = CursorLoader.get().getCursor(buffer, hotSpotX, hotSpotY, temp.getWidth(), temp.getHeight());
                Mouse.setNativeCursor(cursor);
            }
            catch (Throwable e) {
                Log.error("Failed to load and apply cursor.", e);
                throw new SlickException("Failed to set mouse cursor", e);
            }
        }
        
        @Override
        public void setIcons(final String[] refs) throws SlickException {
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
        
        @Override
        public void setDefaultMouseCursor() {
        }
        
        @Override
        public boolean isFullscreen() {
            return Display.isFullscreen();
        }
        
        @Override
        public void setFullscreen(final boolean fullscreen) throws SlickException {
            if (fullscreen == this.isFullscreen()) {
                return;
            }
            try {
                if (fullscreen) {
                    final int screenWidth = Display.getDisplayMode().getWidth();
                    final int screenHeight = Display.getDisplayMode().getHeight();
                    final float gameAspectRatio = this.width / (float)this.height;
                    final float screenAspectRatio = screenWidth / (float)screenHeight;
                    int newWidth;
                    int newHeight;
                    if (gameAspectRatio >= screenAspectRatio) {
                        newWidth = screenWidth;
                        newHeight = (int)(this.height / (this.width / (float)screenWidth));
                    }
                    else {
                        newWidth = (int)(this.width / (this.height / (float)screenHeight));
                        newHeight = screenHeight;
                    }
                    final int xoffset = (screenWidth - newWidth) / 2;
                    final int yoffset = (screenHeight - newHeight) / 2;
                    GL11.glViewport(xoffset, yoffset, newWidth, newHeight);
                    this.enterOrtho();
                    this.getInput().setOffset(-xoffset * (float)this.width / newWidth, -yoffset * (float)this.height / newHeight);
                    this.getInput().setScale(this.width / (float)newWidth, this.height / (float)newHeight);
                    this.width = screenWidth;
                    this.height = screenHeight;
                    Display.setFullscreen(true);
                }
                else {
                    this.getInput().setOffset(0.0f, 0.0f);
                    this.getInput().setScale(1.0f, 1.0f);
                    this.width = AppletGameContainer.this.getWidth();
                    this.height = AppletGameContainer.this.getHeight();
                    GL11.glViewport(0, 0, this.width, this.height);
                    this.enterOrtho();
                    Display.setFullscreen(false);
                }
            }
            catch (LWJGLException e) {
                Log.error(e);
            }
        }
        
        public void runloop() throws Exception {
            while (this.running) {
                final int delta = this.getDelta();
                this.updateAndRender(delta);
                this.updateFPS();
                Display.update();
            }
            Display.destroy();
        }
    }
    
    public class ConsolePanel extends Panel
    {
        TextArea textArea;
        
        public ConsolePanel(final Exception e) {
            this.textArea = new TextArea();
            this.setLayout(new BorderLayout());
            this.setBackground(Color.black);
            this.setForeground(Color.white);
            final Font consoleFont = new Font("Arial", 1, 14);
            final Label slickLabel = new Label("SLICK CONSOLE", 1);
            slickLabel.setFont(consoleFont);
            this.add(slickLabel, "First");
            final StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            this.textArea.setText(sw.toString());
            this.textArea.setEditable(false);
            this.add(this.textArea, "Center");
            this.add(new Panel(), "Before");
            this.add(new Panel(), "After");
            final Panel bottomPanel = new Panel();
            bottomPanel.setLayout(new GridLayout(0, 1));
            final Label infoLabel1 = new Label("An error occured while running the applet.", 1);
            final Label infoLabel2 = new Label("Plese contact support to resolve this issue.", 1);
            infoLabel1.setFont(consoleFont);
            infoLabel2.setFont(consoleFont);
            bottomPanel.add(infoLabel1);
            bottomPanel.add(infoLabel2);
            this.add(bottomPanel, "Last");
        }
    }
}
