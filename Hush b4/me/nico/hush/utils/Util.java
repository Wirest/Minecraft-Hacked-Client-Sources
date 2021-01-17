// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.utils;

import org.lwjgl.opengl.EXTFramebufferObject;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.Minecraft;

public class Util
{
    public static void checkSetupFBO() {
        final Framebuffer fbo = Minecraft.getMinecraft().getFramebuffer();
        if (fbo != null && fbo.depthBuffer > -1) {
            setupFBO(fbo);
            fbo.depthBuffer = -1;
        }
    }
    
    public static void setupFBO(final Framebuffer fbo) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
        final int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindRenderbufferEXT(36161, stencil_depth_buffer_ID);
        final int target = 36161;
        final int internalformat = 34041;
        Minecraft.getMinecraft();
        final int displayWidth = Minecraft.displayWidth;
        Minecraft.getMinecraft();
        EXTFramebufferObject.glRenderbufferStorageEXT(target, internalformat, displayWidth, Minecraft.displayHeight);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencil_depth_buffer_ID);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencil_depth_buffer_ID);
    }
}
