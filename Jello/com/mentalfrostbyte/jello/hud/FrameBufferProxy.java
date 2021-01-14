package com.mentalfrostbyte.jello.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;

public class FrameBufferProxy
extends Framebuffer
{
private Framebuffer oldFramebuffer;
private FBO fbo;

public FrameBufferProxy()
{
  super(0, 0, false);
}

public void attach(Minecraft mc, FBO fbo)
{
  this.fbo = fbo;
  this.oldFramebuffer = mc.getFramebuffer();
  mc.framebufferMc = this;
}

public void release(Minecraft mc)
{
  mc.framebufferMc = oldFramebuffer;
}

public void a(int p_147613_1_, int p_147613_2_) {}

public void a(boolean p_147610_1_)
{
  this.fbo.bind();
}

public void e()
{
  this.fbo.end();
}
}
