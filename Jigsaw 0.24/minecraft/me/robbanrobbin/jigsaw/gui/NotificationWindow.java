package me.robbanrobbin.jigsaw.gui;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import java.awt.Font;

import org.darkstorm.minecraft.gui.font.UnicodeFontRenderer;
import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class NotificationWindow {
	
	public static UnicodeFontRenderer fontRenderer = new UnicodeFontRenderer(new Font("Corbel", Font.PLAIN, 20));
	
	public static UnicodeFontRenderer fontRenderer2 = new UnicodeFontRenderer(new Font("Corbel", Font.BOLD, 20));
	
	private int x;
	private int y;
	
	private int width;
	private int height;
	
	private int lifeTime = 0;
	
	private Notification notification;
	
	private int stringWidth = 0;
	
	public NotificationWindow(Notification notification) {
		this.notification = notification;
		stringWidth = fontRenderer.getStringWidth(this.notification.getText());
		if(this.getNotification().getLevel().getHeader().isEmpty()) {
			this.width = stringWidth + 11;
		}
		else {
			this.width = stringWidth + 15 + fontRenderer2.getStringWidth(this.getNotification().getLevel().getHeader());
		}
		
		this.height = 17;
	}
	
	public void update() {
		lifeTime++;
	}
	
	public Notification getNotification() {
		return notification;
	}
	
	public void draw() {
		glEnable(GL_BLEND);
		glDisable(GL_CULL_FACE);
		glDisable(GL_TEXTURE_2D);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		float alpha = 1;
		float f = getNotification().getText().length() * 3;
		if(lifeTime < 6) {
			alpha = ((float)lifeTime) / 5f;
		}
		if(lifeTime > f - 6) {
			alpha = 1 - (((float)lifeTime - (f - 6)) / 5f);
		}
		GL11.glColor4f(0.214f, 0.214f, 0.214f, alpha);
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex2d(x, y);
			GL11.glVertex2d(x + width, y);
			GL11.glVertex2d(x + width, y + height);
			GL11.glVertex2d(x, y + height);
		}
		GL11.glEnd();
		GL11.glColor4f(0.2f, 0.2f, 0.2f, alpha);
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex2d(x, y);
			GL11.glVertex2d(x + width, y);
			GL11.glVertex2d(x + width, y + height / 2);
			GL11.glVertex2d(x, y + height / 2);
		}
		GL11.glEnd();
		GL11.glLineWidth(4);
		GL11.glColor4f(0.1f, 0.1f, 0.1f, alpha);
		GL11.glBegin(GL11.GL_LINES);
		{
			GL11.glVertex2d(x + 1, y);
			GL11.glVertex2d(x + 1, y + height);
			GL11.glVertex2d(x + width - 1, y);
			GL11.glVertex2d(x + width - 1, y + height);
		}
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_LINES);
		{
			GL11.glVertex2d(x, y);
			GL11.glVertex2d(x + width, y);
			GL11.glVertex2d(x + width, y + height);
			GL11.glVertex2d(x, y + height);
		}
		GL11.glEnd();
		
		GL11.glColor4f(0.7f, 0.1f, 0.1f, alpha);
		GL11.glLineWidth(1);
		GL11.glBegin(GL11.GL_LINES);
		{
			GL11.glVertex2d(x + 2, y + height - 1.5);
			GL11.glVertex2d(x + width - 2, y + height - 1.5);
		}
		GL11.glEnd();
		
		if(alpha > 1) {
			alpha = 1;
		}
		if(alpha < 0) {
			alpha = 0;
		}
		Level l = getNotification().getLevel();
		if(l == Level.ERROR) {
			fontRenderer2.drawStringWithShadow(this.getNotification().getLevel().getHeader(), x + 4, y + 3, 
					new org.newdawn.slick.Color(0.8f, 0.2f, 0.2f, alpha));
		}
		if(l == Level.INFO) {
			fontRenderer2.drawStringWithShadow(this.getNotification().getLevel().getHeader(), x + 4, y + 3, 
					new org.newdawn.slick.Color(0.8f, 0.8f, 0.8f, alpha));
		}
		if(l == Level.WARNING) {
			fontRenderer2.drawStringWithShadow(this.getNotification().getLevel().getHeader(), x + 4, y + 3, 
					new org.newdawn.slick.Color(0.8f, 0.6f, 0.3f, alpha));
		}
		
		
		fontRenderer.drawStringWithShadow(notification.getText(), x + -(stringWidth - width) - 6, y + 3, new org.newdawn.slick.Color(1f, 1f, 1f, alpha));
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getLifeTime() {
		return lifeTime;
	}
	
}
