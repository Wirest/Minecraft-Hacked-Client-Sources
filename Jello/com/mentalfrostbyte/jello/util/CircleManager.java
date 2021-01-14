package com.mentalfrostbyte.jello.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;

public class CircleManager {

	public List<Circle> circles = new ArrayList<Circle>();
	
	public void addCircle(double x, double y, double rad, double speed, int key) {
		circles.add(new Circle(x, y, rad, speed, key));
	}
	
	public void runCircle(Circle c){
		//if(c.progress < 2){
		
		
		c.lastProgress = c.progress;
		if((c.keyCode == Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode() || c.keyCode == Minecraft.getMinecraft().gameSettings.keyBindUseItem.getKeyCode()) ? false : c.progress > c.topRadius*0.67 && Keyboard.isKeyDown(c.keyCode))
			return;
		
		c.progress += (c.topRadius-c.progress)/(c.speed) + 0.01;
		if(c.progress >= c.topRadius){
			c.complete = true;
		}
		//}
	}
	
	public void runCircles(){
		List<Circle> completes = new ArrayList<Circle>();
		for(Circle c : circles){
			if(!c.complete){
			runCircle(c);
			}else{
				completes.add(c);
			}
		}
		synchronized(circles){
			circles.removeAll(completes);
		}
	}
	
	public void drawCircles(){
		for(Circle c : circles){
			if(!c.complete)
			drawCircle(c);
		}
	}
	
	public void drawCircle(Circle c){
    	float progress = (float) (c.progress * Minecraft.getMinecraft().timer.renderPartialTicks + (c.lastProgress * (1.0f - Minecraft.getMinecraft().timer.renderPartialTicks)));
    	if(!c.complete)
    		
    		RenderingUtil.drawBorderedCircle((int)c.x, (int)c.y, progress, new Color(1f, 1f, 1f, (1-Math.min(1f, Math.max(0f, (float)(progress/c.topRadius))))/2).getRGB(), new Color(1f, 1f, 1f, (1-Math.min(1f, Math.max(0f, (float)(progress/c.topRadius))))/2).getRGB());
		//RenderingUtil.drawCircle((int)c.x, (int)c.y, progress, new Color(1f, 1f, 1f, (1-Math.min(1f, Math.max(0f, (float)(progress/c.topRadius))))/2).getRGB(), new Color(1f, 1f, 1f, (1-Math.min(1f, Math.max(0f, (float)(progress/c.topRadius))))/2).getRGB());
	}
	
}

