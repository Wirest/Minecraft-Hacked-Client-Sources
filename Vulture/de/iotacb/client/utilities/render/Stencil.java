package de.iotacb.client.utilities.render;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;

public class Stencil {

	private static final Minecraft MC = Minecraft.getMinecraft();

	public void write(boolean renderClipLayer) {
		checkSetupFBO();
		GL11.glClearStencil(0);
		GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
		GL11.glEnable(GL11.GL_STENCIL_TEST);
		GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 65535);
		GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
		if (!renderClipLayer) {
			GlStateManager.colorMask(false, false, false, false);
		}
	}

	public void erase(boolean invert) {
		GL11.glStencilFunc(invert ? GL11.GL_EQUAL : GL11.GL_NOTEQUAL, 1, 65535);
		GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
		GlStateManager.colorMask(true, true, true, true);
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.0f);
	}

	public void dispose() {
		GL11.glDisable(GL11.GL_STENCIL_TEST);
		GlStateManager.disableAlpha();
		GlStateManager.disableBlend();
	}

	public void checkSetupFBO() {
		Framebuffer fbo = Minecraft.getMinecraft().getFramebuffer();
		if (fbo != null && fbo.depthBuffer > -1) {
			setupFBO(fbo);
			fbo.depthBuffer = -1;
		}
	}

	public void setupFBO(Framebuffer fbo) {
		EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
		int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
		EXTFramebufferObject.glBindRenderbufferEXT(36161, stencil_depth_buffer_ID);
		EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
		EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencil_depth_buffer_ID);
		EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencil_depth_buffer_ID);
	}

}
