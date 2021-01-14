package com.mentalfrostbyte.jello.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class Stencil {
	
	public static Minecraft mc = Minecraft.getMinecraft();
	
	public static int nextColor;
	
    public static void write(boolean renderClipLayer) {
        Stencil.checkSetupFBO();
        GL11.glClearStencil((int)0);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glStencilFunc(GL11.GL_ALWAYS, (int)1, (int)65535);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
        if(!renderClipLayer){
        GlStateManager.colorMask(false, false, false, false);
        }
    }

    public static void erase(boolean invert) {
        GL11.glStencilFunc(invert ? GL11.GL_EQUAL : GL11.GL_NOTEQUAL, (int)1, (int)65535);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GL11.glAlphaFunc( GL11.GL_GREATER, 0.0f );
	 }
    

    public static void dispose() {
        GL11.glDisable(GL11.GL_STENCIL_TEST);
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
    }

    public static void checkSetupFBO() {
        Framebuffer fbo = Minecraft.getMinecraft().getFramebuffer();
        if (fbo != null && fbo.depthBuffer > -1) {
            Stencil.setupFBO(fbo);
            fbo.depthBuffer = -1;
        }
    }

    
    public static void setupFBO(Framebuffer fbo) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT((int)fbo.depthBuffer);
        int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindRenderbufferEXT((int)36161, (int)stencil_depth_buffer_ID);
        EXTFramebufferObject.glRenderbufferStorageEXT((int)36161, (int)34041, (int)Minecraft.getMinecraft().displayWidth, (int)Minecraft.getMinecraft().displayHeight);
        EXTFramebufferObject.glFramebufferRenderbufferEXT((int)36160, (int)36128, (int)36161, (int)stencil_depth_buffer_ID);
        EXTFramebufferObject.glFramebufferRenderbufferEXT((int)36160, (int)36096, (int)36161, (int)stencil_depth_buffer_ID);
    }
}

