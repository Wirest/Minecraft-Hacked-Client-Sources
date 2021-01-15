package info.spicyclient.notifications;

import info.spicyclient.chatCommands.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class Notification {
	
	public Notification(String title, String text, boolean showTimer, long timeOnScreen, Type type, Color color, int targetX, int targetY, int startingX, int startingY, int speed) {
		
		this.title = title;
		this.text = text;
		this.showTimer = showTimer;
		this.timeOnScreen = System.currentTimeMillis() + timeOnScreen;
		this.originalTime = timeOnScreen;
		this.originalTimeOnScreen = timeOnScreen;
		this.type = type;
		this.color = color;
		this.targetX = targetX;
		this.targetY = targetY;
		this.startingX = startingX;
		this.startingY = startingY;
		this.speed = speed;
		
	}
	
	public String title = "", text = "";
	public boolean showTimer;
	public long timeOnScreen;
	public long originalTimeOnScreen;
	public long originalTime;
	public final Type type;
	public Color color;
	public int targetX, targetY, startingX, startingY, speed;
	
	// So notifications don't get stuck
	public boolean leaving = false, left = false;
	
	// So notifications don't leave before they reach their location
	public boolean joined = false;
	
	// So notifications don't overlap
	public boolean setDefaultY = false;
	
	public void onRender(int Num) {
		
		Minecraft mc = Minecraft.getMinecraft();
		FontRenderer fr = mc.fontRendererObj;
		ScaledResolution sr = new ScaledResolution(mc);
		
		if (speed < 0) {
			speed *= -1;
		}
		
		if (speed == 0) {
			speed = 2;
		}
		
		if (System.currentTimeMillis() >= timeOnScreen) {
			leaving = true;
			targetY = sr.getScaledHeight() + 10;
		}
		
		if (!leaving && !left && setDefaultY) {
			targetY = sr.getScaledHeight() - 54 - (54 * Num);
		}
		
		int orgSpeed = speed;
		
		if ((targetX - startingX) < 0 && (startingX != targetX)) {
			
			if ((targetX - startingX) * -1 < speed) {
				speed = (targetX - startingX) * -1;
			}
			
		}else if ((startingX != targetX)) {
			
			if ((targetX - startingX) * 1 < speed) {
				speed = (targetX - startingX) * -1;
			}
			
		}
		
		if ((targetY - startingY) < 0 && (startingY != targetY)) {
			
			if ((targetY - startingY) * -1 < speed && speed >= (targetY - startingY) * -1) {
				speed = (targetY - startingY) * -1;
			}
			
		}else if ((startingY != targetY)) {
			
			if ((targetY - startingY) * -1 < speed && speed >= (targetY - startingY) * -1) {
				speed = (targetY - startingY) * -1;
			}
			
		}
		
		if (speed <= 0) {
			//speed = 2;
		}
		
		if (speed <= 0) {
			speed = orgSpeed;
		}
		
		if (startingX < targetX) {
			startingX += speed;
		}
		else if (startingX > targetX) {
			startingX -= speed;
		}
		
		if (startingY < targetY) {
			startingY += speed;
		}
		else if (startingY > targetY) {
			startingY -= speed;
		}
		
		if (startingX == targetX && startingY == targetY && !left && !leaving && !joined) {
			joined = true;
		}
		
		if (!joined) {
			this.timeOnScreen = System.currentTimeMillis() + originalTimeOnScreen;
		}
		
		double notWidth = 170, orgX = startingX;
		
		if ((fr.getStringWidth(title) * 1.1) + 45 >= 170 || (fr.getStringWidth(text) * 0.9) + 43.33 >= 170) {
			
			notWidth = (fr.getStringWidth(title) * 1.1) + 50;
			startingX -= (fr.getStringWidth(title) * 1.1) - 170 + 50;
			
			if ((fr.getStringWidth(text) * 0.9) + 43.33 >= notWidth) {
				notWidth = (fr.getStringWidth(text) * 0.9) + 50;
				startingX -= (fr.getStringWidth(text) * 0.9) - 170 + 72;
			}
			
		}
		
		Gui.drawRect(startingX, startingY, startingX + notWidth, startingY + 45, 0xff202225);
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(4, 4, 0);
		GlStateManager.scale(1.1, 1.1, 1);
		GlStateManager.translate(-4, -4, 0);
		fr.drawString(title, (int) ((startingX + 45) / 1.1), (int) ((int) ((startingY + 15 - (fr.FONT_HEIGHT / 2))) / 1.1), color.color);
		GlStateManager.popMatrix();
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(4, 4, 0);
		GlStateManager.scale(0.9, 0.9, 1);
		GlStateManager.translate(-4, -4, 0);
		fr.drawString(text, (float) ((startingX + 43.33) / 0.9), (float) ((float) ((startingY + 17 + (fr.FONT_HEIGHT))) / 0.9), color.color, false);
		GlStateManager.popMatrix();
		
		mc.getTextureManager().bindTexture(new ResourceLocation("spicy/notifications/" + type.filePrefix + color.fileSuffix + ".png"));
		int size = 30;
		Gui.drawModalRectWithCustomSizedTexture(startingX + 4, startingY + 5, 0, 0, size, size, size, size);
		
		if (showTimer && timeOnScreen - System.currentTimeMillis() > 0) {
			
			Gui.drawRect(startingX, startingY + 40, startingX + ((notWidth / originalTime) * ((timeOnScreen - System.currentTimeMillis()))), startingY + 45, color.color);
			
		}
		
		
		if (startingY == targetY && leaving) {
			left = true;
			NotificationManager.getNotificationManager().notifications.remove(this);
		}
		
		startingX = (int) orgX;
		speed = orgSpeed;
	}
	
}
