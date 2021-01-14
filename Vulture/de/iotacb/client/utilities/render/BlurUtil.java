package de.iotacb.client.utilities.render;

import java.awt.Color;
import java.io.IOException;

import org.lwjgl.opengl.GL11;

import com.google.gson.JsonSyntaxException;

import de.iotacb.client.Client;
import de.iotacb.client.module.modules.render.HUD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;

public class BlurUtil {

	private static final Minecraft MC = Minecraft.getMinecraft();
	private final ResourceLocation resourceLocation;
	private ShaderGroup shaderGroup;
	private Framebuffer framebuffer;
	
	private int lastFactor;
	private int lastWidth;
	private int lastHeight;
	
	public BlurUtil() {
		this.resourceLocation = new ResourceLocation("client/blur.json");
	}
	
	public void init() {
		try {
			this.shaderGroup = new ShaderGroup(MC.getTextureManager(), MC.getResourceManager(), MC.getFramebuffer(), resourceLocation);
			this.shaderGroup.createBindFramebuffers(MC.displayWidth, MC.displayHeight);
			this.framebuffer = shaderGroup.mainFramebuffer;
		} catch (JsonSyntaxException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setValues(int strength) {
		this.shaderGroup.getShaders().get(0).getShaderManager().getShaderUniform("Radius").set(strength);
		this.shaderGroup.getShaders().get(1).getShaderManager().getShaderUniform("Radius").set(strength);
		this.shaderGroup.getShaders().get(2).getShaderManager().getShaderUniform("Radius").set(strength);
		this.shaderGroup.getShaders().get(3).getShaderManager().getShaderUniform("Radius").set(strength);
	}
	
	public final void blur(int blurStrength) {
		if (!Client.INSTANCE.getModuleManager().getModuleByClass(HUD.class).getValueByName("HUDBlur").getBooleanValue()) return;
		final ScaledResolution scaledResolution = new ScaledResolution(MC);
		
		final int scaleFactor = scaledResolution.getScaleFactor();
		final int width = scaledResolution.getScaledWidth();
		final int height = scaledResolution.getScaledHeight();
		
		if (sizeHasChanged(scaleFactor, width, height) || framebuffer == null || shaderGroup == null) {
			init();
		}
		
		this.lastFactor = scaleFactor;
		this.lastWidth = width;
		this.lastHeight = height;
		
		setValues(blurStrength);
		framebuffer.bindFramebuffer(true);
		shaderGroup.loadShaderGroup(MC.timer.renderPartialTicks);
		MC.getFramebuffer().bindFramebuffer(true);
		GlStateManager.enableAlpha();
	}
	
	public final void blur(double x, double y, double areaWidth, double areaHeight, int blurStrength) {
		final HUD hud = (HUD) Client.INSTANCE.getModuleManager().getModuleByClass(HUD.class);
		if (!hud.getValueByName("HUDBlur").getBooleanValue()) return;
		final ScaledResolution scaledResolution = new ScaledResolution(MC);
		
		final int scaleFactor = scaledResolution.getScaleFactor();
		final int width = scaledResolution.getScaledWidth();
		final int height = scaledResolution.getScaledHeight();
		
		if (sizeHasChanged(scaleFactor, width, height) || framebuffer == null || shaderGroup == null) {
			init();
		}
		
		this.lastFactor = scaleFactor;
		this.lastWidth = width;
		this.lastHeight = height;
		
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		Client.RENDER2D.scissor(x, y + 1, areaWidth, areaHeight - 1);
		framebuffer.bindFramebuffer(true);
		shaderGroup.loadShaderGroup(MC.timer.renderPartialTicks);
		setValues((int) hud.getValueByName("HUDBlur strength").getNumberValue());
		MC.getFramebuffer().bindFramebuffer(false);
		
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		
		
	}
	
	private boolean sizeHasChanged(int scaleFactor, int width, int height) {
		return (lastFactor != scaleFactor || lastWidth != width || lastHeight != height);
	}
	
}
