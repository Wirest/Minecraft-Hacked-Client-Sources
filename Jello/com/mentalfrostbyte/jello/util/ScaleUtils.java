package com.mentalfrostbyte.jello.util;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class ScaleUtils {

	public static float[] getScaledCoords(float x, float y, float width, float height, float scale, ScaledResolution sr){
		float x1 = x;
	    float y1 = y;
	    float x2 = x + width;
	    float y2 = y + height;
	    float sfX = (((-sr.getScaledWidth()/2)*scale) + sr.getScaledWidth()/2f);
	    float sfY = (((-sr.getScaledHeight()/2)*scale) + sr.getScaledHeight()/2f);
	    float x1S = ((sr.getScaledWidth()/2)-x1)/(sr.getScaledWidth()/2f);
	    float x2S = ((sr.getScaledWidth()/2)-x2)/(sr.getScaledWidth()/2f);
	    float y1S = ((sr.getScaledHeight()/2)-y1)/(sr.getScaledHeight()/2f);
	    float y2S = ((sr.getScaledHeight()/2)-y2)/(sr.getScaledHeight()/2f);
	    return new float[]{x1 + sfX*x1S, y1 + sfY*y1S, (x2 + sfX*x2S) - (x1 + sfX*x1S), (y2 + sfY*y2S) - (y1 + sfY*y1S)};
	}
	
	public static void preScale(float x, float y, float amount, ScaledResolution sr){
		GlStateManager.translate(x, y, 0);
	     GlStateManager.scale(amount, amount, 1);
	     GlStateManager.translate(-x, -y, 0);
	}
	
	public static void postScale(float x, float y, float amount, ScaledResolution sr){
		GlStateManager.translate(x, y, 0);
	     GlStateManager.scale(1/amount, 1/amount, 1);
	     GlStateManager.translate(-x, -y, 0);
	}
	
}
