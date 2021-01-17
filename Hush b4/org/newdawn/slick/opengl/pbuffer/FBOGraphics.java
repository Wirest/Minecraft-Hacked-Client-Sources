// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.opengl.pbuffer;

import org.newdawn.slick.opengl.SlickCallable;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import java.nio.IntBuffer;
import org.newdawn.slick.opengl.InternalTextureLoader;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.newdawn.slick.SlickException;
import org.lwjgl.opengl.GLContext;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.Image;
import org.newdawn.slick.Graphics;

public class FBOGraphics extends Graphics
{
    private Image image;
    private int FBO;
    private boolean valid;
    
    public FBOGraphics(final Image image) throws SlickException {
        super(image.getTexture().getTextureWidth(), image.getTexture().getTextureHeight());
        this.valid = true;
        this.image = image;
        Log.debug("Creating FBO " + image.getWidth() + "x" + image.getHeight());
        final boolean FBOEnabled = GLContext.getCapabilities().GL_EXT_framebuffer_object;
        if (!FBOEnabled) {
            throw new SlickException("Your OpenGL card does not support FBO and hence can't handle the dynamic images required for this application.");
        }
        this.init();
    }
    
    private void completeCheck() throws SlickException {
        final int framebuffer = EXTFramebufferObject.glCheckFramebufferStatusEXT(36160);
        switch (framebuffer) {
            case 36053: {}
            case 36054: {
                throw new SlickException("FrameBuffer: " + this.FBO + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT exception");
            }
            case 36055: {
                throw new SlickException("FrameBuffer: " + this.FBO + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT exception");
            }
            case 36057: {
                throw new SlickException("FrameBuffer: " + this.FBO + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT exception");
            }
            case 36059: {
                throw new SlickException("FrameBuffer: " + this.FBO + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT exception");
            }
            case 36058: {
                throw new SlickException("FrameBuffer: " + this.FBO + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT exception");
            }
            case 36060: {
                throw new SlickException("FrameBuffer: " + this.FBO + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT exception");
            }
            default: {
                throw new SlickException("Unexpected reply from glCheckFramebufferStatusEXT: " + framebuffer);
            }
        }
    }
    
    private void init() throws SlickException {
        final IntBuffer buffer = BufferUtils.createIntBuffer(1);
        EXTFramebufferObject.glGenFramebuffersEXT(buffer);
        this.FBO = buffer.get();
        try {
            final Texture tex = InternalTextureLoader.get().createTexture(this.image.getWidth(), this.image.getHeight(), this.image.getFilter());
            EXTFramebufferObject.glBindFramebufferEXT(36160, this.FBO);
            EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064, 3553, tex.getTextureID(), 0);
            this.completeCheck();
            this.unbind();
            this.clear();
            this.flush();
            this.drawImage(this.image, 0.0f, 0.0f);
            this.image.setTexture(tex);
        }
        catch (Exception e) {
            throw new SlickException("Failed to create new texture for FBO");
        }
    }
    
    private void bind() {
        EXTFramebufferObject.glBindFramebufferEXT(36160, this.FBO);
        GL11.glReadBuffer(36064);
    }
    
    private void unbind() {
        EXTFramebufferObject.glBindFramebufferEXT(36160, 0);
        GL11.glReadBuffer(1029);
    }
    
    @Override
    protected void disable() {
        FBOGraphics.GL.flush();
        this.unbind();
        GL11.glPopClientAttrib();
        GL11.glPopAttrib();
        GL11.glMatrixMode(5888);
        GL11.glPopMatrix();
        GL11.glMatrixMode(5889);
        GL11.glPopMatrix();
        GL11.glMatrixMode(5888);
        SlickCallable.leaveSafeBlock();
    }
    
    @Override
    protected void enable() {
        if (!this.valid) {
            throw new RuntimeException("Attempt to use a destroy()ed offscreen graphics context.");
        }
        SlickCallable.enterSafeBlock();
        GL11.glPushAttrib(1048575);
        GL11.glPushClientAttrib(-1);
        GL11.glMatrixMode(5889);
        GL11.glPushMatrix();
        GL11.glMatrixMode(5888);
        GL11.glPushMatrix();
        this.bind();
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
        final IntBuffer buffer = BufferUtils.createIntBuffer(1);
        buffer.put(this.FBO);
        buffer.flip();
        EXTFramebufferObject.glDeleteFramebuffersEXT(buffer);
        this.valid = false;
    }
    
    @Override
    public void flush() {
        super.flush();
        this.image.flushPixelData();
    }
}
