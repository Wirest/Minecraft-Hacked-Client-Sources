package com.mentalfrostbyte.jello.music.image;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
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
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;

import com.mentalfrostbyte.jello.main.Jello;
import com.mentalfrostbyte.jello.util.BlurUtil;
import com.mentalfrostbyte.jello.util.FontUtil;



public class Draw {

private static  Minecraft mc = Minecraft.getMinecraft();
	
public static void rectTextureMasked(float x, float y, float w, float h, Texture texture, int color, float mx, float my) {
	if (texture == null) {
		return;
	}
	
	float var11 = (float)(color >> 24 & 255) / 255.0F;
    float var6 = (float)(color >> 16 & 255) / 255.0F;
    float var7 = (float)(color >> 8 & 255) / 255.0F;
    float var8 = (float)(color & 255) / 255.0F;
    
	texture.bind();
	float tw = (46.153847f/texture.getTextureWidth())/(w/texture.getImageWidth());
	float th = (60.0f/texture.getTextureHeight())/(h/texture.getImageHeight());
	if(my != 0){
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
	GlStateManager.color(1, 1, 1, mx*(1-my));
	Gui.INSTANCE.drawModalRectWithCustomSizedTexture(x, y, 47/2f + 25/4f + 5*0 + 0.5f, 13f + 2.5f*0, w, h, w+69 + 10*0, h + 69 + 10*0);
	
	//BlurUtil.blurAreaBoarder(x - 2*my, y - 2*my, 81 - 4*(1-my) + 0.5f, 81 - 4*(1-my), 1 + (14*my), 0, 1);
	}else{
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
	GlStateManager.color(1, 1, 1, mx*(1-my));
	Gui.INSTANCE.drawModalRectWithCustomSizedTexture(x, y, 47/2f + 25/4f + 5*1, 13f + 3.5f*1, w, h, w+69 + 10*1, h + 69 + 10*1);
	
			
	}
}

public static void rectTextureMasked2(float x, float y, float w, float h, Texture texture, int color, float mx, float my) {
	if (texture == null) {
		return;
	}
	
	float var11 = (float)(color >> 24 & 255) / 255.0F;
    float var6 = (float)(color >> 16 & 255) / 255.0F;
    float var7 = (float)(color >> 8 & 255) / 255.0F;
    float var8 = (float)(color & 255) / 255.0F;
    
	texture.bind();
	float tw = (46.153847f/texture.getTextureWidth())/(w/texture.getImageWidth());
	float th = (60.0f/texture.getTextureHeight())/(h/texture.getImageHeight());
	
	Gui.INSTANCE.drawModalRectWithCustomSizedTexture(x, y, 47/2f + 25/4f - 7.5f, 9.5f + 2.5f*0, w, h, w+51, h + 51);
	
			
	
}

public static void rectTextureMaskedNotification(float x, float y, float w, float h, Texture texture, int color, float mx, float my) {
	if (texture == null) {
		return;
	}
	float var11 = (float)(color >> 24 & 255) / 255.0F;
    float var6 = (float)(color >> 16 & 255) / 255.0F;
    float var7 = (float)(color >> 8 & 255) / 255.0F;
    float var8 = (float)(color & 255) / 255.0F;
    
	texture.bind();
	float tw = (46.153847f/texture.getTextureWidth())/(w/texture.getImageWidth());
	float th = (60.0f/texture.getTextureHeight())/(h/texture.getImageHeight());
	
	Gui.INSTANCE.drawModalRectWithCustomSizedTexture(x, y, 27/2f + 25/4f - 7.5f - 1.5f, 4.5f + 2.5f*0, w, h, w+24, h + 24);
}

public static void rectTextureMasked3(float x, float y, float w, float h, Texture texture, int color, float mx, float my) {
	if (texture == null) {
		return;
	}
	
	float var11 = (float)(color >> 24 & 255) / 255.0F;
    float var6 = (float)(color >> 16 & 255) / 255.0F;
    float var7 = (float)(color >> 8 & 255) / 255.0F;
    float var8 = (float)(color & 255) / 255.0F;
    
	texture.bind();
	float tw = (46.153847f/texture.getTextureWidth())/(w/texture.getImageWidth());
	float th = (60.0f/texture.getTextureHeight())/(h/texture.getImageHeight());
	
	Gui.INSTANCE.drawModalRectWithCustomSizedTexture(x, y, 47/2f + 25/4f - 7.5f, 9.5f + 2.5f*0+ 125, w, h, w+51, h + 400);
	
			
	
}
	

	public static void rectTexture(float x, float y, float w, float h, Texture texture, int color) {
		if (texture == null) {
			return;
		}
		//TextureImpl.bindNone();
		//GlStateManager.color(0, 0, 0);
	//	GL11.glColor4f(0, 0, 0, 0);
		//x = Math.round(x);
		//w = Math.round(w);
		//y = Math.round(y);
		//h = Math.round(h);
		
		float var11 = (float)(color >> 24 & 255) / 255.0F;
        float var6 = (float)(color >> 16 & 255) / 255.0F;
        float var7 = (float)(color >> 8 & 255) / 255.0F;
        float var8 = (float)(color & 255) / 255.0F;
        
        //GlStateManager.enableBlend();
        //GlStateManager.disableTexture2D();
       // GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        //GlStateManager.color(var6, var7, var8, var11);
        
		//GL11.glEnable(GL11.GL_BLEND);
		//GL11.glEnable(GL11.GL_TEXTURE_2D);
		texture.bind();
		
		float tw = (w/texture.getTextureWidth())/(w/texture.getImageWidth());
		float th = (h/texture.getTextureHeight())/(h/texture.getImageHeight());
		
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(x, y);
		GL11.glTexCoord2f(0, th);
		GL11.glVertex2f(x, y+h);
		GL11.glTexCoord2f(tw, th);
		GL11.glVertex2f(x+w, y+h);
		GL11.glTexCoord2f(tw, 0);
		GL11.glVertex2f(x+w, y);
	GL11.glEnd();
		//GL11.glDisable(GL11.GL_TEXTURE_2D);
		//GL11.glDisable(GL11.GL_BLEND);
        //GlStateManager.enableTexture2D();
       // GlStateManager.disableBlend();
	}
	
	
	public static void rectTexture(float x, float y, float w, float h, Texture texture) {
		rectTexture(x, y, w, h, texture, -1);
	}
	
	public static void rectTextureMasked(float x, float y, float w, float h, Texture texture, float mx, float my) {
		rectTextureMasked(x, y, w, h, texture, -1, mx, my);
	}
	public static void rectTextureMasked2(float x, float y, float w, float h, Texture texture, float mx, float my) {
		rectTextureMasked2(x, y, w, h, texture, -1, mx, my);
	}
	public static void rectTextureMaskedNotification(float x, float y, float w, float h, Texture texture, float mx, float my) {
		rectTextureMaskedNotification(x, y, w, h, texture, -1, mx, my);
	}
	
	public static void rectTextureMaskedBanner(float x, float y, float w, float h, Texture texture, float mx, float my) {
		rectTextureMasked3(x, y, w, h, texture, -1, mx, my);
	}
	
}
