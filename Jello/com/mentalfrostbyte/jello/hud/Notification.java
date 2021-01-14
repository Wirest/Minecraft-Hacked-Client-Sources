package com.mentalfrostbyte.jello.hud;

import java.awt.Color;

import org.newdawn.slick.opengl.TextureImpl;

import com.mentalfrostbyte.jello.main.Jello;
import com.mentalfrostbyte.jello.music.image.TextureImage;
import com.mentalfrostbyte.jello.util.FontUtil;
import com.mentalfrostbyte.jello.util.TimerUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class Notification {

	private String text;
	private String secondText;
	private int time;
	private TimerUtil timer = new TimerUtil();
	private boolean isDone;
	private boolean peaked;
	
	private float progress;
	private float lastProgress;
	
	public TextureImage ti;
	
	public Notification(String text, String secondText, int time){
		this.text = text;
		this.secondText = secondText;
		this.time = time;
		timer.reset();
		isDone = false;
		progress = 0;
		lastProgress = 0;
		peaked = false;
	}
	
	public Notification(String text, String secondText, int time, TextureImage texture){
		ti = texture;
		this.text = text;
		this.secondText = secondText;
		this.time = time;
		timer.reset();
		isDone = false;
		progress = 0;
		lastProgress = 0;
		peaked = false;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSecondText() {
		return secondText;
	}

	public void setSecondText(String secondText) {
		this.secondText = secondText;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public TimerUtil getTimer() {
		return timer;
	}

	public void setTimer(TimerUtil timer) {
		this.timer = timer;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}
	
	public void onTick(){
		lastProgress = progress;
		if(progress < 34 && !peaked){
			//progress++;
			progress += (((34)-progress)/(3))+2;
		}else if(!peaked){
			peaked = true;
		}
		
		if(progress > 0 && peaked && timer.hasTimeElapsed(this.getTime() + 500, false)){
			//progress--;
			progress -= (progress/(3))+2;
		}else if(progress <= 1 && peaked){
			timer.reset();
			isDone = true;
		}
	}
	public float smoothTrans(double current, double last){
		return (float) (current * Minecraft.getMinecraft().timer.renderPartialTicks + (last * (1.0f - Minecraft.getMinecraft().timer.renderPartialTicks)));
	}
	public void renderNotification(){
		ScaledResolution sr = new ScaledResolution(Jello.core.mc, Jello.core.mc.displayWidth, Jello.core.mc.displayHeight);
		
		float animation = this.smoothTrans(progress, lastProgress);
		
		if(!isDone){
			GlStateManager.enableBlend();
			GlStateManager.disableAlpha();
			GlStateManager.color(1, 1, 1, 1);
		Gui.INSTANCE.drawGradientRect(sr.getScaledWidth() - 179 - animation*5.264f + 179+ 4.5f + 3 + .5f, 0+ 4.5f  + .5f, sr.getScaledWidth() - 179 - animation*5.264f + 179 + 179 - 4.5f  + 3 + .5f, 41 - 4.5f  + .5f, (new Color(Jello.getInGameGUI().smoothAnimation(Jello.getInGameGUI().nRed, Jello.getInGameGUI().lastnRed), Jello.getInGameGUI().smoothAnimation(Jello.getInGameGUI().nGreen, Jello.getInGameGUI().lastnGreen), Jello.getInGameGUI().smoothAnimation(Jello.getInGameGUI().nBlue, Jello.getInGameGUI().lastnBlue), 255)).getRGB(), (new Color(Jello.getInGameGUI().smoothAnimation(Jello.getInGameGUI().nbRed, Jello.getInGameGUI().lastnbRed), Jello.getInGameGUI().smoothAnimation(Jello.getInGameGUI().nbGreen, Jello.getInGameGUI().lastnbGreen), Jello.getInGameGUI().smoothAnimation(Jello.getInGameGUI().nbBlue, Jello.getInGameGUI().lastnbBlue), 255)).getRGB());
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();

		
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/notification.png"));
		Gui.drawModalRectWithCustomSizedTexture(sr.getScaledWidth() - 179 - animation*5.264f + 179 + 3 + .5f, 0 + .5f, 0, 0, 179, 41, 179, 41);
        //Gui.drawFloatRect(0, 0, sr.getScaledWidth(), animation, 0xdd000000);
		GlStateManager.color(1, 1, 1, 1);
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/warning.png"));
		Gui.drawModalRectWithCustomSizedTexture(sr.getScaledWidth() - 179 - animation*5.264f + 179 + 26/2f + 3 + .5f, 27/2f + .5f, 0, 0, 26/2f, 27/2f, 26/2f, 27/2f);
		
		if(ti != null){
			TextureImpl.bindNone();
			ti.rectTextureMaskedNotification(sr.getScaledWidth() - 179 - animation*5.264f + 179 + 26/2f - 8 + 2.5f + 3, 27/2f - 9 + 3f, 27, 27, 1, 0.001f);
		}
		
		FontUtil.jelloFont.drawString(text, sr.getScaledWidth() - 179 - animation*5.264f + 179 + 4.5f + 32.5f + 3 + 4, 4.5f + 7.5f + 1, 0xccffffff);
		FontUtil.jelloFontSmall.drawString(secondText, sr.getScaledWidth() - 179 - animation*5.264f + 179 + 4.5f + 32.5f + 3 + 4.5f, 4.5f + 19.5f + 1.5f, 0xaaffffff);
		}
		
	}

	public float getProgress() {
		return this.smoothTrans(progress, lastProgress);
	}

	public void setProgress(float progress) {
		this.progress = progress;
	}
	
	
}
