package com.mentalfrostbyte.jello.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class Clip {

private static final Minecraft mc = Minecraft.getMinecraft();
	
	public static void startClip(float x1, float y1, float x2, float y2) {
		float temp;
		if (y1 > y2) {
			temp = y2;
			y2 = y1;
			y1 = temp;
		}
		
		GL11.glScissor((int)x1, (int)(Display.getHeight()-y2), (int)(x2-x1), (int)(y2-y1));
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
	}
	
	public static void endClip() {
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}
	
}
