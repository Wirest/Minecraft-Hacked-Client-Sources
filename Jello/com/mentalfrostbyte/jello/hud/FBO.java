package com.mentalfrostbyte.jello.hud;

import java.awt.image.BufferedImage;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;

public class FBO
{
  private static boolean supported = false;
  private static boolean useARB = false;
  private boolean created;
  private boolean active;
  private int depthBuffer;
  private int frameBuffer;
  private /*bpq*/DynamicTexture texture;
  private int frameBufferWidth;
  private int frameBufferHeight;
  
  public static boolean detectFBOCapabilities()
  {
    ContextCapabilities capabilities = GLContext.getCapabilities();
    if (capabilities.GL_ARB_framebuffer_object)
    {
      supported = true;
      useARB = true;
      return true;
    }
    if (capabilities.GL_EXT_framebuffer_object)
    {
      supported = true;
      return true;
    }
    supported = false;
    return false;
  }
  
  public FBO()
  {
    detectFBOCapabilities();
  }
  
  public static boolean isSupported()
  {
    return supported;
  }
  
  public void begin(int width, int height)
  {
    if (!supported) {
      return;
    }
    if ((width < 1) || (height < 1)) {
      throw new IllegalArgumentException("Attempted to create an FBO with zero or negative size");
    }
    if ((this.created) && ((width != this.frameBufferWidth) || (height != this.frameBufferHeight))) {
      dispose();
    }
    if (!this.created)
    {
      this.created = true;
      this.frameBufferWidth = width;
      this.frameBufferHeight = height;
      
      BufferedImage textureImage = new BufferedImage(this.frameBufferWidth, this.frameBufferHeight, 1);
      this.texture = new DynamicTexture(textureImage);
      if (useARB)
      {
        this.frameBuffer = ARBFramebufferObject.glGenFramebuffers();
        this.depthBuffer = ARBFramebufferObject.glGenRenderbuffers();
        
        ARBFramebufferObject.glBindFramebuffer(36160, this.frameBuffer);
        ARBFramebufferObject.glFramebufferTexture2D(36160, 36064, 3553, this.texture.getGlTextureId(), 0);
        
        ARBFramebufferObject.glBindRenderbuffer(36161, this.depthBuffer);
        ARBFramebufferObject.glRenderbufferStorage(36161, 33190, this.frameBufferWidth, this.frameBufferHeight);
        ARBFramebufferObject.glFramebufferRenderbuffer(36160, 36096, 36161, this.depthBuffer);
        
        ARBFramebufferObject.glBindFramebuffer(36160, 0);
        ARBFramebufferObject.glBindRenderbuffer(36161, 0);
      }
      else
      {
        this.frameBuffer = EXTFramebufferObject.glGenFramebuffersEXT();
        this.depthBuffer = EXTFramebufferObject.glGenRenderbuffersEXT();
        
        EXTFramebufferObject.glBindFramebufferEXT(36160, this.frameBuffer);
        EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064, 3553, this.texture.getGlTextureId(), 0);
        
        EXTFramebufferObject.glBindRenderbufferEXT(36161, this.depthBuffer);
        EXTFramebufferObject.glRenderbufferStorageEXT(36161, 33190, this.frameBufferWidth, this.frameBufferHeight);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, this.depthBuffer);
        
        EXTFramebufferObject.glBindFramebufferEXT(36160, 0);
        EXTFramebufferObject.glBindRenderbufferEXT(36161, 0);
      }
    }
    bind();
  }
  
  public void bind()
  {
    if (!supported) {
      return;
    }
    if ((this.created) && (checkFBO()))
    {
      if (useARB)
      {
        ARBFramebufferObject.glBindFramebuffer(36160, this.frameBuffer);
        ARBFramebufferObject.glBindRenderbuffer(36161, this.depthBuffer);
      }
      else
      {
        EXTFramebufferObject.glBindFramebufferEXT(36160, this.frameBuffer);
        EXTFramebufferObject.glBindRenderbufferEXT(36161, this.depthBuffer);
      }
      GL11.glPushAttrib(2048);
      GL11.glViewport(0, 0, this.frameBufferWidth, this.frameBufferHeight);
      GL11.glClear(16384);
      this.active = true;
    }
  }
  
  public void end()
  {
    if ((supported) && (this.active))
    {
      if (useARB)
      {
        ARBFramebufferObject.glBindFramebuffer(36160, 0);
        ARBFramebufferObject.glBindRenderbuffer(36161, 0);
      }
      else
      {
        EXTFramebufferObject.glBindFramebufferEXT(36160, 0);
        EXTFramebufferObject.glBindRenderbufferEXT(36161, 0);
      }
      GL11.glPopAttrib();
      this.active = false;
    }
  }
  
  public void dispose()
  {
    if (!supported) {
      return;
    }
    end();
    if (this.texture != null) {
      GL11.glDeleteTextures(this.texture.getGlTextureId());
    }
    if (useARB)
    {
      ARBFramebufferObject.glDeleteRenderbuffers(this.depthBuffer);
      ARBFramebufferObject.glDeleteFramebuffers(this.frameBuffer);
    }
    else
    {
      EXTFramebufferObject.glDeleteRenderbuffersEXT(this.depthBuffer);
      EXTFramebufferObject.glDeleteFramebuffersEXT(this.frameBuffer);
    }
    this.depthBuffer = 0;
    this.texture = null;
    this.frameBuffer = 0;
    this.created = false;
  }
  
  private boolean checkFBO()
  {
    if (useARB)
    {
      ARBFramebufferObject.glBindFramebuffer(36160, this.frameBuffer);
      ARBFramebufferObject.glBindRenderbuffer(36161, this.depthBuffer);
    }
    else
    {
      EXTFramebufferObject.glBindFramebufferEXT(36160, this.frameBuffer);
      EXTFramebufferObject.glBindRenderbufferEXT(36161, this.depthBuffer);
    }
    int frameBufferStatus = useARB ? ARBFramebufferObject.glCheckFramebufferStatus(36160) : EXTFramebufferObject.glCheckFramebufferStatusEXT(36160);
    switch (frameBufferStatus)
    {
    case 36053: 
      return true;
    case 36054: 
    case 36055: 
    case 36057: 
    case 36058: 
    case 36059: 
    case 36060: 
      return false;
    }
    throw new RuntimeException("Unexpected reply from glCheckFramebufferStatus: " + frameBufferStatus);
  }
  
  public void draw(int x, int y, int x2, int y2, int z, float alpha)
  {
    draw(x, y, x2, y2, z, alpha, 0.0D, 0.0D, 1.0D, 1.0D);
  }
  
  public void draw(double x, double y, double x2, double y2, double z, float alpha, double u, double v, double u2, double v2)
  {
    if ((supported) && (this.created))
    {
      GL11.glEnable(3553);
      GL11.glBindTexture(3553, this.texture.getGlTextureId());
      GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
      
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer wr = tessellator.getWorldRenderer();
      wr.startDrawingQuads();
      wr.addVertexWithUV(x, y2, z, u, v);
      wr.addVertexWithUV(x2, y2, z, u2, v);
      wr.addVertexWithUV(x2, y, z, u2, v2);
      wr.addVertexWithUV(x, y, z, u, v2);
      wr.draw();
    }
  }
}
  