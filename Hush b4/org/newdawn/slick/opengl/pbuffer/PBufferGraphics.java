// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.opengl.pbuffer;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.SlickCallable;
import org.lwjgl.LWJGLException;
import org.newdawn.slick.opengl.Texture;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.Drawable;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.opengl.RenderTexture;
import org.newdawn.slick.opengl.InternalTextureLoader;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.Image;
import org.lwjgl.opengl.Pbuffer;
import org.newdawn.slick.Graphics;

public class PBufferGraphics extends Graphics
{
    private Pbuffer pbuffer;
    private Image image;
    
    public PBufferGraphics(final Image image) throws SlickException {
        super(image.getTexture().getTextureWidth(), image.getTexture().getTextureHeight());
        this.image = image;
        Log.debug("Creating pbuffer(rtt) " + image.getWidth() + "x" + image.getHeight());
        if ((Pbuffer.getCapabilities() & 0x1) == 0x0) {
            throw new SlickException("Your OpenGL card does not support PBuffers and hence can't handle the dynamic images required for this application.");
        }
        if ((Pbuffer.getCapabilities() & 0x2) == 0x0) {
            throw new SlickException("Your OpenGL card does not support Render-To-Texture and hence can't handle the dynamic images required for this application.");
        }
        this.init();
    }
    
    private void init() throws SlickException {
        try {
            final Texture tex = InternalTextureLoader.get().createTexture(this.image.getWidth(), this.image.getHeight(), this.image.getFilter());
            final RenderTexture rt = new RenderTexture(false, true, false, false, 8314, 0);
            (this.pbuffer = new Pbuffer(this.screenWidth, this.screenHeight, new PixelFormat(8, 0, 0), rt, null)).makeCurrent();
            this.initGL();
            PBufferGraphics.GL.glBindTexture(3553, tex.getTextureID());
            this.pbuffer.releaseTexImage(8323);
            this.image.draw(0.0f, 0.0f);
            this.image.setTexture(tex);
            Display.makeCurrent();
        }
        catch (Exception e) {
            Log.error(e);
            throw new SlickException("Failed to create PBuffer for dynamic image. OpenGL driver failure?");
        }
    }
    
    @Override
    protected void disable() {
        PBufferGraphics.GL.flush();
        PBufferGraphics.GL.glBindTexture(3553, this.image.getTexture().getTextureID());
        this.pbuffer.bindTexImage(8323);
        try {
            Display.makeCurrent();
        }
        catch (LWJGLException e) {
            Log.error(e);
        }
        SlickCallable.leaveSafeBlock();
    }
    
    @Override
    protected void enable() {
        SlickCallable.enterSafeBlock();
        try {
            if (this.pbuffer.isBufferLost()) {
                this.pbuffer.destroy();
                this.init();
            }
            this.pbuffer.makeCurrent();
        }
        catch (Exception e) {
            Log.error("Failed to recreate the PBuffer");
            throw new RuntimeException(e);
        }
        PBufferGraphics.GL.glBindTexture(3553, this.image.getTexture().getTextureID());
        this.pbuffer.releaseTexImage(8323);
        TextureImpl.unbind();
        this.initGL();
    }
    
    protected void initGL() {
        GL11.glEnable(3553);
        GL11.glShadeModel(7425);
        GL11.glDisable(2929);
        GL11.glDisable(2896);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glClearDepth(1.0);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glViewport(0, 0, this.screenWidth, this.screenHeight);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        this.enterOrtho();
    }
    
    protected void enterOrtho() {
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, this.screenWidth, 0.0, this.screenHeight, 1.0, -1.0);
        GL11.glMatrixMode(5888);
    }
    
    @Override
    public void destroy() {
        super.destroy();
        this.pbuffer.destroy();
    }
    
    @Override
    public void flush() {
        super.flush();
        this.image.flushPixelData();
    }
}
