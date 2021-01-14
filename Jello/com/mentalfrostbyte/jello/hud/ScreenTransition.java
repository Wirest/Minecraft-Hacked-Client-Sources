package com.mentalfrostbyte.jello.hud;

import org.lwjgl.opengl.GL11;

public abstract class ScreenTransition
{
  public abstract void render(FBO paramFBO1, FBO paramFBO2, int paramInt1, int paramInt2, float paramFloat);
  
  public abstract String getName();
  
  public abstract boolean isHighMotion();
  
  public abstract int getTransitionTime();
  
  public abstract TransitionType getTransitionType();
  
  public static enum TransitionType
  {
    Linear,  Sine,  Cosine,  Smooth;
    
    private TransitionType() {}
    
    public float interpolate(float pct)
    {
      if (this == Sine) {
        return (float)Math.min(1.0D, Math.sin(3.141592653589793D * Math.min(0.5F, pct)));
      }
      if (this == Cosine) {
        return (float)Math.min(1.0D, 1.0D - Math.cos(3.141592653589793D * Math.min(0.5F, pct)));
      }
      if (this == Smooth) {
        return (float)Math.min(1.0D, (-Math.cos(6.283185307179586D * Math.min(0.5F, pct)) + 1.0D) * 0.5D);
      }
      return Math.min(1.0F, pct);
    }
  }
  
  public static final void drawFBO(FBO fbo, int x, int y, int x2, int y2, int z, float alpha)
  {
    drawFBO(fbo, x, y, x2, y2, z, alpha, true);
  }
  
  public static final void drawFBO(FBO fbo, int x, int y, int x2, int y2, int z, float alpha, boolean blend)
  {
    drawFBO(fbo, x, y, x2, y2, z, alpha, blend, 0.0D, 0.0D, 1.0D, 1.0D);
  }
  
  public static void drawFBO(FBO fbo, double x, double y, double x2, double y2, double z, float alpha, boolean blend, double u, double v, double u2, double v2)
  {
    if (blend)
    {
      GL11.glEnable(3042);
      GL11.glEnable(3008);
      GL11.glAlphaFunc(516, 0.0F);
      GL11.glBlendFunc(770, 771);
    }
    else
    {
      GL11.glDisable(3042);
      GL11.glDisable(3008);
    }
    GL11.glShadeModel(7424);
    
    fbo.draw(x, y, x2, y2, z, alpha, u, v, u2, v2);
    
    GL11.glEnable(3008);
    GL11.glAlphaFunc(516, 0.1F);
    GL11.glDisable(3042);
  }
}
