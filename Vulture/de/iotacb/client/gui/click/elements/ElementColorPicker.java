package de.iotacb.client.gui.click.elements;

import java.awt.Color;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import de.iotacb.client.Client;
import de.iotacb.client.gui.click.Element;
import de.iotacb.client.utilities.render.Render2D;
import de.iotacb.client.utilities.render.color.BetterColor;
import net.minecraft.util.MathHelper;

public class ElementColorPicker extends Element {

	private double hueBarWidth = 20;
	
	private double hueColor;
	
	private BetterColor color;
	
	private double hueBarDotX, lastHueBarDotX, hueBarDotY, lastHueBarDotY;
	private double colorFieldDotX, lastColorFieldDotX, colorFieldDotY, lastColorFieldDotY;
	
	private boolean draggingHue, draggingColor;
	
	public ElementColorPicker(double posX, double posY, double width, double height, Element parent) {
		super(posX, posY, width, height, parent);
		color = Client.INSTANCE.getClientColor();
	}

	@Override
	public void updateElement(int mouseX, int mouseY) {
	}

	@Override
	public void drawElement(int mouseX, int mouseY) {
		Client.RENDER2D.start();
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glBegin(GL11.GL_QUADS);
		{
			Client.RENDER2D.color(BetterColor.getHue(hueColor));
			GL11.glVertex2d(getPosX(), getPosY() + getHeight());
			Client.RENDER2D.color(Color.black);
			GL11.glVertex2d(getPosX() + getWidth() - hueBarWidth, getPosY() + getHeight());
			Client.RENDER2D.color(BetterColor.getHue(hueColor));
			GL11.glVertex2d(getPosX() + getWidth() - hueBarWidth, getPosY());
			Client.RENDER2D.color(Color.WHITE);
			GL11.glVertex2d(getPosX(), getPosY());
		}
		GL11.glEnd();
		GL11.glShadeModel(GL11.GL_FLAT);
		Client.RENDER2D.stop();
		
		for (int i = 0; i < 360; ++i) {
			Client.RENDER2D.rect(getPosX() + getWidth() - hueBarWidth, getHeight() * (i / 360.0F) + getPosY(), hueBarWidth, 1, BetterColor.getHue((float) i));
		}
		
		if (draggingHue && !draggingColor) {
			hueBarDotX = mouseX - 2.5;
			hueBarDotY = mouseY - 2.5;
			
			hueBarDotX = MathHelper.clamp_double(hueBarDotX, getPosX() + getWidth() - hueBarWidth, getPosX() + getWidth() - 5);
			hueBarDotY = MathHelper.clamp_double(hueBarDotY, getPosY(), getPosY() + getHeight() - 5);
			
			hueColor = ((hueBarDotY / getHeight()) - (getPosY() / getHeight())) * 360;
			
			lastHueBarDotX = hueBarDotX - getPosX();
			lastHueBarDotY = hueBarDotY - getPosY();
			draggingHue = true;
		}
		
		if (draggingColor && !draggingHue) {
			colorFieldDotX = mouseX - 2.5;
			colorFieldDotY = mouseY - 2.5;
			
			colorFieldDotX = MathHelper.clamp_double(colorFieldDotX, getPosX(), getPosX() + getWidth() - hueBarWidth - 5);
			colorFieldDotY = MathHelper.clamp_double(colorFieldDotY, getPosY(), getPosY() + getHeight() - 5);
			
			color = BetterColor.getHue((float) hueColor).addColoring((int) ((360 - (((colorFieldDotY / getHeight()) - (getPosY() / getHeight())) * 360)) - ((((colorFieldDotX / getWidth()) - (getPosX() / getWidth())) * 360))));
			
			lastColorFieldDotX = colorFieldDotX - getPosX();
			lastColorFieldDotY = colorFieldDotY - getPosY();
			draggingColor = true;
		}
		
		if (!Mouse.isButtonDown(0)) {
			if (draggingHue) {
				draggingHue = false;
			}
			
			if (draggingColor) {
				draggingColor = false;
			}
		}
		
		Client.RENDER2D.circle(hueBarDotX, hueBarDotY, 5, Color.white);
		Client.RENDER2D.circle(hueBarDotX, hueBarDotY, 5, false, Color.black);
		
		Client.RENDER2D.circle(colorFieldDotX, colorFieldDotY, 5, Color.white);
		Client.RENDER2D.circle(colorFieldDotX, colorFieldDotY, 5, false, Color.black);
		
		if (color != null) {
			Client.INSTANCE.setClientColor(color);
		}
	}

	@Override
	public final void clickElement(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == 0) {
			if (colorFieldHovered(mouseX, mouseY)) {
				draggingColor = true;
			}
			
			if (colorHueBarHovered(mouseX, mouseY)) {
				draggingHue = true;
			}
		}
	}
	
	private boolean colorFieldHovered(int mouseX, int mouseY) {
		return (mouseX > getPosX() && mouseX < getPosX() + getWidth() - hueBarWidth) && (mouseY > getPosY() && mouseY < getPosY() + getHeight());
	}
	
	private boolean colorHueBarHovered(int mouseX, int mouseY) {
		return (mouseX > getPosX() + getWidth() - hueBarWidth && mouseX < getPosX() + getWidth()) && (mouseY > getPosY() && mouseY < getPosY() + getHeight());
	}
	
	@Override
	public final void setPosition(double posX, double posY) {
		hueBarDotX = lastHueBarDotX + posX;
		hueBarDotY = lastHueBarDotY + posY;
		
		hueColor = ((hueBarDotY / getHeight()) - (getPosY() / getHeight())) * 360;
		
		colorFieldDotX = lastColorFieldDotX + posX;
		colorFieldDotY = lastColorFieldDotY + posY;
		super.setPosition(posX, posY);
	}
    
    public final double getHueBarDotX() {
		return hueBarDotX;
	}
    
    public final double getHueBarDotY() {
		return hueBarDotY;
	}
    
    public final double getColorFieldDotX() {
		return colorFieldDotX;
	}
    
    public final double getColorFieldDotY() {
		return colorFieldDotY;
	}
    
    public final void setDotPosition(double hueDotX, double hueDotY, double colorFieldDotX, double colorFieldDotY) {
    	this.hueBarDotX = hueDotX;
    	this.hueBarDotY = hueDotY;
		
    	this.colorFieldDotX = colorFieldDotX;
    	this.colorFieldDotY = colorFieldDotY;
    	
    	this.lastHueBarDotX = hueBarDotX - getPosX();
    	this.lastHueBarDotY = hueBarDotY - getPosY();
    	
    	this.lastColorFieldDotX = colorFieldDotX - getPosX();
    	this.lastColorFieldDotY = colorFieldDotY - getPosY();
    	
    	hueColor = ((hueBarDotY / getHeight()) - (getPosY() / getHeight())) * 360;
    	color = BetterColor.getHue((float) hueColor).addColoring((int) ((360 - (((colorFieldDotY / getHeight()) - (getPosY() / getHeight())) * 360)) - ((((colorFieldDotX / getWidth()) - (getPosX() / getWidth())) * 360))));
    }

}
