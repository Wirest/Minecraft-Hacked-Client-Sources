// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.Sys;
import java.awt.event.HierarchyEvent;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.Graphics;
import org.lwjgl.PointerBuffer;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import java.awt.event.HierarchyListener;
import java.awt.event.ComponentListener;
import java.awt.Canvas;

public class AWTGLCanvas extends Canvas implements DrawableLWJGL, ComponentListener, HierarchyListener
{
    private static final long serialVersionUID = 1L;
    private static final AWTCanvasImplementation implementation;
    private boolean update_context;
    private Object SYNC_LOCK;
    private final PixelFormat pixel_format;
    private final Drawable drawable;
    private final ContextAttribs attribs;
    private PeerInfo peer_info;
    private ContextGL context;
    private int reentry_count;
    private boolean first_run;
    
    static AWTCanvasImplementation createImplementation() {
        switch (LWJGLUtil.getPlatform()) {
            case 1: {
                return new LinuxCanvasImplementation();
            }
            case 3: {
                return new WindowsCanvasImplementation();
            }
            case 2: {
                return new MacOSXCanvasImplementation();
            }
            default: {
                throw new IllegalStateException("Unsupported platform");
            }
        }
    }
    
    private void setUpdate() {
        synchronized (this.SYNC_LOCK) {
            this.update_context = true;
        }
    }
    
    public void setPixelFormat(final PixelFormatLWJGL pf) throws LWJGLException {
        throw new UnsupportedOperationException();
    }
    
    public void setPixelFormat(final PixelFormatLWJGL pf, final ContextAttribs attribs) throws LWJGLException {
        throw new UnsupportedOperationException();
    }
    
    public PixelFormatLWJGL getPixelFormat() {
        return this.pixel_format;
    }
    
    public ContextGL getContext() {
        return this.context;
    }
    
    public ContextGL createSharedContext() throws LWJGLException {
        synchronized (this.SYNC_LOCK) {
            if (this.context == null) {
                throw new IllegalStateException("Canvas not yet displayable");
            }
            return new ContextGL(this.peer_info, this.context.getContextAttribs(), this.context);
        }
    }
    
    public void checkGLError() {
        Util.checkGLError();
    }
    
    public void initContext(final float r, final float g, final float b) {
        GL11.glClearColor(r, g, b, 0.0f);
        GL11.glClear(16384);
    }
    
    public AWTGLCanvas() throws LWJGLException {
        this(new PixelFormat());
    }
    
    public AWTGLCanvas(final PixelFormat pixel_format) throws LWJGLException {
        this(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice(), pixel_format);
    }
    
    public AWTGLCanvas(final GraphicsDevice device, final PixelFormat pixel_format) throws LWJGLException {
        this(device, pixel_format, null);
    }
    
    public AWTGLCanvas(final GraphicsDevice device, final PixelFormat pixel_format, final Drawable drawable) throws LWJGLException {
        this(device, pixel_format, drawable, null);
    }
    
    public AWTGLCanvas(final GraphicsDevice device, final PixelFormat pixel_format, final Drawable drawable, final ContextAttribs attribs) throws LWJGLException {
        super(AWTGLCanvas.implementation.findConfiguration(device, pixel_format));
        this.SYNC_LOCK = new Object();
        if (pixel_format == null) {
            throw new NullPointerException("Pixel format must be non-null");
        }
        this.addHierarchyListener(this);
        this.addComponentListener(this);
        this.drawable = drawable;
        this.pixel_format = pixel_format;
        this.attribs = attribs;
    }
    
    @Override
    public void addNotify() {
        super.addNotify();
    }
    
    @Override
    public void removeNotify() {
        synchronized (this.SYNC_LOCK) {
            this.destroy();
            super.removeNotify();
        }
    }
    
    public void setSwapInterval(final int swap_interval) {
        synchronized (this.SYNC_LOCK) {
            if (this.context == null) {
                throw new IllegalStateException("Canvas not yet displayable");
            }
            ContextGL.setSwapInterval(swap_interval);
        }
    }
    
    public void setVSyncEnabled(final boolean enabled) {
        this.setSwapInterval(enabled ? 1 : 0);
    }
    
    public void swapBuffers() throws LWJGLException {
        synchronized (this.SYNC_LOCK) {
            if (this.context == null) {
                throw new IllegalStateException("Canvas not yet displayable");
            }
            ContextGL.swapBuffers();
        }
    }
    
    public boolean isCurrent() throws LWJGLException {
        synchronized (this.SYNC_LOCK) {
            if (this.context == null) {
                throw new IllegalStateException("Canvas not yet displayable");
            }
            return this.context.isCurrent();
        }
    }
    
    public void makeCurrent() throws LWJGLException {
        synchronized (this.SYNC_LOCK) {
            if (this.context == null) {
                throw new IllegalStateException("Canvas not yet displayable");
            }
            this.context.makeCurrent();
        }
    }
    
    public void releaseContext() throws LWJGLException {
        synchronized (this.SYNC_LOCK) {
            if (this.context == null) {
                throw new IllegalStateException("Canvas not yet displayable");
            }
            if (this.context.isCurrent()) {
                this.context.releaseCurrent();
            }
        }
    }
    
    public final void destroy() {
        synchronized (this.SYNC_LOCK) {
            try {
                if (this.context != null) {
                    this.context.forceDestroy();
                    this.context = null;
                    this.reentry_count = 0;
                    this.peer_info.destroy();
                    this.peer_info = null;
                }
            }
            catch (LWJGLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public final void setCLSharingProperties(final PointerBuffer properties) throws LWJGLException {
        synchronized (this.SYNC_LOCK) {
            if (this.context == null) {
                throw new IllegalStateException("Canvas not yet displayable");
            }
            this.context.setCLSharingProperties(properties);
        }
    }
    
    protected void initGL() {
    }
    
    protected void paintGL() {
    }
    
    @Override
    public final void paint(final Graphics g) {
        LWJGLException exception = null;
        synchronized (this.SYNC_LOCK) {
            if (!this.isDisplayable()) {
                return;
            }
            try {
                if (this.peer_info == null) {
                    this.peer_info = AWTGLCanvas.implementation.createPeerInfo(this, this.pixel_format, this.attribs);
                }
                this.peer_info.lockAndGetHandle();
                try {
                    if (this.context == null) {
                        this.context = new ContextGL(this.peer_info, this.attribs, (this.drawable != null) ? ((ContextGL)((DrawableLWJGL)this.drawable).getContext()) : null);
                        this.first_run = true;
                    }
                    if (this.reentry_count == 0) {
                        this.context.makeCurrent();
                    }
                    ++this.reentry_count;
                    try {
                        if (this.update_context) {
                            this.context.update();
                            this.update_context = false;
                        }
                        if (this.first_run) {
                            this.first_run = false;
                            this.initGL();
                        }
                        this.paintGL();
                    }
                    finally {
                        --this.reentry_count;
                        if (this.reentry_count == 0) {
                            this.context.releaseCurrent();
                        }
                    }
                }
                finally {
                    this.peer_info.unlock();
                }
            }
            catch (LWJGLException e) {
                exception = e;
            }
        }
        if (exception != null) {
            this.exceptionOccurred(exception);
        }
    }
    
    protected void exceptionOccurred(final LWJGLException exception) {
        LWJGLUtil.log("Unhandled exception occurred, skipping paint(): " + exception);
    }
    
    @Override
    public void update(final Graphics g) {
        this.paint(g);
    }
    
    public void componentShown(final ComponentEvent e) {
    }
    
    public void componentHidden(final ComponentEvent e) {
    }
    
    public void componentResized(final ComponentEvent e) {
        this.setUpdate();
    }
    
    public void componentMoved(final ComponentEvent e) {
        this.setUpdate();
    }
    
    @Override
    public void setLocation(final int x, final int y) {
        super.setLocation(x, y);
        this.setUpdate();
    }
    
    @Override
    public void setLocation(final Point p) {
        super.setLocation(p);
        this.setUpdate();
    }
    
    @Override
    public void setSize(final Dimension d) {
        super.setSize(d);
        this.setUpdate();
    }
    
    @Override
    public void setSize(final int width, final int height) {
        super.setSize(width, height);
        this.setUpdate();
    }
    
    @Override
    public void setBounds(final int x, final int y, final int width, final int height) {
        super.setBounds(x, y, width, height);
        this.setUpdate();
    }
    
    public void hierarchyChanged(final HierarchyEvent e) {
        this.setUpdate();
    }
    
    static {
        Sys.initialize();
        implementation = createImplementation();
    }
}
