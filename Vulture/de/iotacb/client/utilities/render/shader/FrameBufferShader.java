package de.iotacb.client.utilities.render.shader;

import java.awt.Color;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.shader.Framebuffer;

public abstract class FrameBufferShader extends Shader {

	public FrameBufferShader(String fragementShaderName) {
		super(fragementShaderName);
	}
	
	private static Framebuffer framebuffer;
	
	public void renderShader(float partialTicks) {
		GlStateManager.enableAlpha();
		
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		
		makeFrameBuffer();
		framebuffer.framebufferClear();
		framebuffer.bindFramebuffer(true);
		getMc().entityRenderer.setupCameraTransform(partialTicks, 0);
	}
	
	public void clearShader() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		getMc().getFramebuffer().bindFramebuffer(true);
		
		getMc().entityRenderer.disableLightmap();
		RenderHelper.disableStandardItemLighting();
		
		startShader();
		getMc().entityRenderer.setupOverlayRendering();
		drawFrameBuffer();
		stopShader();
		
		getMc().entityRenderer.disableLightmap();
		
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
	}
	
	public void makeFrameBuffer() {
		if (framebuffer != null) framebuffer.deleteFramebuffer();
		
		framebuffer = new Framebuffer(getMc().displayWidth, getMc().displayHeight, true); // width, height, depth
	}
	
	public void drawFrameBuffer() {
		final ScaledResolution scaledResolution = new ScaledResolution(getMc());
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebuffer.framebufferTexture);
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2d(0, 1);
			GL11.glVertex2d(0, 0);
			GL11.glTexCoord2d(0, 0);
			GL11.glVertex2d(0, scaledResolution.getScaledHeight());
			GL11.glTexCoord2d(1, 0);
			GL11.glVertex2d(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
			GL11.glTexCoord2d(1, 1);
			GL11.glVertex2d(scaledResolution.getScaledWidth(), 0);
		}
		GL11.glEnd();
		GL20.glUseProgram(0);
	}

}
