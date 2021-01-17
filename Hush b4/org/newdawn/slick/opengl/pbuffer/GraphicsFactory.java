// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.opengl.pbuffer;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.SlickException;
import org.lwjgl.opengl.Pbuffer;
import org.lwjgl.opengl.GLContext;
import java.util.HashMap;

public class GraphicsFactory
{
    private static HashMap graphics;
    private static boolean pbuffer;
    private static boolean pbufferRT;
    private static boolean fbo;
    private static boolean init;
    
    static {
        GraphicsFactory.graphics = new HashMap();
        GraphicsFactory.pbuffer = true;
        GraphicsFactory.pbufferRT = true;
        GraphicsFactory.fbo = true;
        GraphicsFactory.init = false;
    }
    
    private static void init() throws SlickException {
        GraphicsFactory.init = true;
        if (GraphicsFactory.fbo) {
            GraphicsFactory.fbo = GLContext.getCapabilities().GL_EXT_framebuffer_object;
        }
        GraphicsFactory.pbuffer = ((Pbuffer.getCapabilities() & 0x1) != 0x0);
        GraphicsFactory.pbufferRT = ((Pbuffer.getCapabilities() & 0x2) != 0x0);
        if (!GraphicsFactory.fbo && !GraphicsFactory.pbuffer && !GraphicsFactory.pbufferRT) {
            throw new SlickException("Your OpenGL card does not support offscreen buffers and hence can't handle the dynamic images required for this application.");
        }
        Log.info("Offscreen Buffers FBO=" + GraphicsFactory.fbo + " PBUFFER=" + GraphicsFactory.pbuffer + " PBUFFERRT=" + GraphicsFactory.pbufferRT);
    }
    
    public static void setUseFBO(final boolean useFBO) {
        GraphicsFactory.fbo = useFBO;
    }
    
    public static boolean usingFBO() {
        return GraphicsFactory.fbo;
    }
    
    public static boolean usingPBuffer() {
        return !GraphicsFactory.fbo && GraphicsFactory.pbuffer;
    }
    
    public static Graphics getGraphicsForImage(final Image image) throws SlickException {
        Graphics g = GraphicsFactory.graphics.get(image.getTexture());
        if (g == null) {
            g = createGraphics(image);
            GraphicsFactory.graphics.put(image.getTexture(), g);
        }
        return g;
    }
    
    public static void releaseGraphicsForImage(final Image image) throws SlickException {
        final Graphics g = GraphicsFactory.graphics.remove(image.getTexture());
        if (g != null) {
            g.destroy();
        }
    }
    
    private static Graphics createGraphics(final Image image) throws SlickException {
        init();
        if (GraphicsFactory.fbo) {
            try {
                return new FBOGraphics(image);
            }
            catch (Exception e) {
                GraphicsFactory.fbo = false;
                Log.warn("FBO failed in use, falling back to PBuffer");
            }
        }
        if (!GraphicsFactory.pbuffer) {
            throw new SlickException("Failed to create offscreen buffer even though the card reports it's possible");
        }
        if (GraphicsFactory.pbufferRT) {
            return new PBufferGraphics(image);
        }
        return new PBufferUniqueGraphics(image);
    }
}
