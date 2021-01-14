package de.iotacb.client.gui.config;

import java.awt.Color;

import de.iotacb.client.Client;

public class ConfigSlot {
	
	private double x, y, width, height;
	
	private int slotId;
	
	private String configName, path, configLink;
	
	private boolean hovered;
	
	private GuiConfig screen;
	
	public ConfigSlot(double x, double y, double width, double height, int slotId, String name, String path, GuiConfig screen) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.slotId = slotId;
		this.configName = name;
		this.path = path;
		this.screen = screen;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getWidth() {
		return width;
	}
	
	public double getHeight() {
		return height;
	}
	
	public int getSlotId() {
		return slotId;
	}
	
	public String getPath() {
		return path;
	}
	
	public String getConfigName() {
		return configName;
	}
	
	public String getConfigLink() {
		return configLink;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public void setSlotId(int slotId) {
		this.slotId = slotId;
	}
	
	public void draw(int mouseX, int mouseY) {
		this.hovered = (mouseX > getX() && mouseX < getX() + getWidth()) && (mouseY > getY() && mouseY < getY() + getHeight());
		Color color = new Color(0, 0, 0, 200);
		
		if (hovered) {
			color = color.brighter();
		} else if (screen.selectedSlot == getSlotId()) {
			color = color.brighter().brighter();
			if (hovered) {
				color.darker();
			}
		}
		
		Client.RENDER2D.rect(getX(), getY(), getWidth(), getHeight(), color);
		Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow(getConfigName(), getX() + 10, getY() + 10, Color.white);

	}
	
}
