package com.mentalfrostbyte.jello.hud;

import java.awt.Color;
import java.util.List;

import com.google.common.collect.Lists;
import com.mentalfrostbyte.jello.main.Jello;
import com.mentalfrostbyte.jello.util.FontUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class Compass {

	public float innerWidth;
	public float outerWidth;
	public boolean shadow;
	public float scale;
	public int accuracy;
	
	public List<Degree> degrees = Lists.newArrayList();
	
	public Compass(float i, float o, float s, int a, boolean sh){
		innerWidth = i;
		outerWidth = o;
		scale = s;
		accuracy = a;
		shadow = sh;
		
		
		degrees.add(new Degree("N", 1));
		degrees.add(new Degree("195", 2));
		degrees.add(new Degree("210", 2));
		degrees.add(new Degree("NE", 3));
		degrees.add(new Degree("240", 2));
		degrees.add(new Degree("255", 2));
		degrees.add(new Degree("E", 1));
		degrees.add(new Degree("285", 2));
		degrees.add(new Degree("300", 2));
		degrees.add(new Degree("SE", 3));
		degrees.add(new Degree("330", 2));
		degrees.add(new Degree("345", 2));
		degrees.add(new Degree("S", 1));
		degrees.add(new Degree("15", 2));
		degrees.add(new Degree("30", 2));
		degrees.add(new Degree("SW", 3));
		degrees.add(new Degree("60", 2));
		degrees.add(new Degree("75", 2));
		degrees.add(new Degree("W", 1));
		degrees.add(new Degree("105", 2));
		degrees.add(new Degree("120", 2));
		degrees.add(new Degree("NW", 3));
		degrees.add(new Degree("150", 2));
		degrees.add(new Degree("165", 2));
	}
	
	public void draw(ScaledResolution sr){
		preRender();
		if(shadow){
			GlStateManager.enableBlend();
			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/shadow.png"));
		    Gui.drawModalRectWithCustomSizedTexture((float) (sr.getScaledWidth() / 2 - 325.5f/2), -8, 0.0F, 0.0F, 325.5f, 76.5D, 325.5F, 76.5D);
		}
		
		float center = sr.getScaledWidth()/2;
		
		int count = 0;
		int cardinals = 0;
		int subCardinals = 0;
		int markers = 0;
		float offset = 0;
		float yaaahhrewindTime = (Minecraft.getMinecraft().thePlayer.rotationYaw % 360)*2 + 360*3;
		
		for(Degree d : degrees){
			
			float location = center + ( count*30 ) - yaaahhrewindTime;
			float completeLocation = (float) (d.type == 1 ? (location - FontUtil.jelloFontBig.getStringWidth(d.text)/2) : d.type == 2 ? (location - FontUtil.jelloFontMarker.getStringWidth(d.text)/2) : (location - FontUtil.jelloFontMedium.getStringWidth(d.text)/2));
			
			int opacity = opacity(sr, completeLocation);
			
			if(d.type == 1 && opacity != 16777215){
				GlStateManager.color(1, 1, 1, 1);
				FontUtil.jelloFontBig.drawNoBSString(d.text, completeLocation, -75 + 100, opacity(sr, completeLocation));
				cardinals++;
			}
			
			if(d.type == 2 && opacity != 16777215){
				GlStateManager.color(1, 1, 1, 1);
				Gui.drawFloatRect(location-0.5f, -75 + 100 + 4, location+0.5f, -75 + 105 + 4, opacity(sr, completeLocation));
				GlStateManager.color(1, 1, 1, 1);
				FontUtil.jelloFontMarker.drawNoBSString(d.text, completeLocation, -75 + 105 + 3.5f + 4, opacity(sr, completeLocation));
				markers++;
			}
			
			if(d.type == 3 && opacity != 16777215){
				GlStateManager.color(1, 1, 1, 1);
				FontUtil.jelloFontMedium.drawNoBSString(d.text, completeLocation, -75 + 100 + FontUtil.jelloFontBig.getHeight()/2 - FontUtil.jelloFontMedium.getHeight()/2, opacity(sr, completeLocation));
				subCardinals++;
			}
			
			count++;
		}
		for(Degree d : degrees){
			
			float location = center + ( count*30 ) - yaaahhrewindTime;
			float completeLocation = (float) (d.type == 1 ? (location - FontUtil.jelloFontBig.getStringWidth(d.text)/2) : d.type == 2 ? (location - FontUtil.jelloFontMarker.getStringWidth(d.text)/2) : (location - FontUtil.jelloFontMedium.getStringWidth(d.text)/2));
			
			if(d.type == 1){
				GlStateManager.color(1, 1, 1, 1);
				FontUtil.jelloFontBig.drawNoBSString(d.text, completeLocation, -75 + 100, opacity(sr, completeLocation));
				cardinals++;
			}
			
			if(d.type == 2){
				GlStateManager.color(1, 1, 1, 1);
				Gui.drawFloatRect(location-0.5f, -75 + 100 + 4, location+0.5f, -75 + 105 + 4, opacity(sr, completeLocation));
				GlStateManager.color(1, 1, 1, 1);
				FontUtil.jelloFontMarker.drawNoBSString(d.text, completeLocation, -75 + 105 + 3.5f + 4, opacity(sr, completeLocation));
				markers++;
			}
			
			if(d.type == 3){
				GlStateManager.color(1, 1, 1, 1);
				FontUtil.jelloFontMedium.drawNoBSString(d.text, completeLocation, -75 + 100 + FontUtil.jelloFontBig.getHeight()/2 - FontUtil.jelloFontMedium.getHeight()/2, opacity(sr, completeLocation));
				subCardinals++;
			}
			
			count++;
		}
		for(Degree d : degrees){
			
			float location = center + ( count*30 ) - yaaahhrewindTime;
			float completeLocation = (float) (d.type == 1 ? (location - FontUtil.jelloFontBig.getStringWidth(d.text)/2) : d.type == 2 ? (location - FontUtil.jelloFontMarker.getStringWidth(d.text)/2) : (location - FontUtil.jelloFontMedium.getStringWidth(d.text)/2));
			
			if(d.type == 1){
				GlStateManager.color(1, 1, 1, 1);
				FontUtil.jelloFontBig.drawNoBSString(d.text, completeLocation, -75 + 100, opacity(sr, completeLocation));
				cardinals++;
			}
			
			if(d.type == 2){
				GlStateManager.color(1, 1, 1, 1);
				Gui.drawFloatRect(location-0.5f, -75 + 100 + 4, location+0.5f, -75 + 105 + 4, opacity(sr, completeLocation));
				GlStateManager.color(1, 1, 1, 1);
				FontUtil.jelloFontMarker.drawNoBSString(d.text, completeLocation, -75 + 105 + 3.5f + 4, opacity(sr, completeLocation));
				markers++;
			}
			
			if(d.type == 3){
				GlStateManager.color(1, 1, 1, 1);
				FontUtil.jelloFontMedium.drawNoBSString(d.text, completeLocation, -75 + 100 + FontUtil.jelloFontBig.getHeight()/2 - FontUtil.jelloFontMedium.getHeight()/2, opacity(sr, completeLocation));
				subCardinals++;
			}
			
			count++;
		}
		
	}
	
	public void preRender(){
		GlStateManager.disableAlpha();
	    GlStateManager.enableBlend();
	}
	
	public int opacity(ScaledResolution sr, float offset){
		int op = 0;
		float offs = 255-Math.abs(sr.getScaledWidth()/2 - offset)*1.8f;
		Color c = new Color((int)255, 255, 255, (int)Math.min(Math.max(0, offs), 255));
		
		return c.getRGB();
	}
	
}
