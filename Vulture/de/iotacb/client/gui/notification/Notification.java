package de.iotacb.client.gui.notification;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import de.iotacb.client.Client;
import de.iotacb.client.utilities.render.Render2D;
import de.iotacb.client.utilities.render.BlurUtil;
import de.iotacb.client.utilities.render.DeltaUtil;
import de.iotacb.client.utilities.render.animations.AnimationUtil;
import de.iotacb.client.utilities.render.animations.easings.Expo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class Notification {

	private final AnimationUtil animUtil;
	
	private double posX, posY, width, height, moveX;
	private int time;
	
	private boolean reverse, remove;
	
	private final String title, message;
	
	private final static Minecraft MC = Minecraft.getMinecraft();
	
	private final ResourceLocation SHADOW = new ResourceLocation("client/textures/shadow_minimap.png");
	
	public Notification(String title, String message) {
		this.title = title;
		this.message = message;
		this.animUtil = new AnimationUtil(Expo.class);
		final ScaledResolution sr = new ScaledResolution(MC);
		
		calculateSize();
		
		posX = sr.getScaledWidth();
		posY = sr.getScaledHeight() - height - 50;
	}
	
	public final void drawNotification(double yPos) {
		Client.RENDER2D.image(SHADOW, posX - moveX - 7, posY + yPos - 2.5, width + 14, height + 5);
		Client.RENDER2D.rect(posX - moveX, posY + yPos, width, height, new Color(20, 20, 20));
		Client.RENDER2D.rect(posX - moveX + (width * (animUtil.getProgression(0).getValue())), posY + yPos + height - 4, width, 4, Client.INSTANCE.getClientColor());
		Client.RENDER2D.push();
		Client.RENDER2D.color(Color.white);
		Client.RENDER2D.translate(posX - moveX + 5, posY + yPos + 5);
		Client.RENDER2D.scale(1.5, 1.5);
		MC.fontRendererObj.drawStringWithShadow(title, 0, 0, Client.INSTANCE.getClientColor().getRGB());
		Client.RENDER2D.pop();
		Client.RENDER2D.color(Color.white);
		Client.RENDER2D.push();
		Client.RENDER2D.translate(posX - moveX + 5, posY + yPos + 20);
		MC.fontRendererObj.drawStringWithShadow(message, 0, 0, Color.white.getRGB());
		Client.RENDER2D.pop();
	}
	
	public final void updateNotification() {
		moveX = MathHelper.clamp_double(moveX, 0, width);
		
		if (time > width / 4) {
			reverse = true;
			time = 0;
		}
		
		if (!reverse) {
			if (moveX < width) {
				moveX = animUtil.easeOut(0, 0, width, 1);
			}
			if (Math.round(moveX) == width) {
				time += 1 + (Client.DELTA_UTIL.deltaTime * .1F);
			}
		} else {
			if (moveX > 0) {
				moveX = width - animUtil.easeOut(1, 0, width, 1);
			}
			if (Math.round(moveX) == 0) remove = true;
		}
	}
	
	public final boolean isRemove() {
		return remove;
	}
	
	private void calculateSize() {
		final double titleWidth = MC.fontRendererObj.getStringWidth(title) * 2;
		final double messageWidth = MC.fontRendererObj.getStringWidth(message) + 20;
		final double height = MC.fontRendererObj.FONT_HEIGHT * 4;
		this.width = titleWidth > messageWidth ? titleWidth : messageWidth;
		this.height = height;
	}
	
}
