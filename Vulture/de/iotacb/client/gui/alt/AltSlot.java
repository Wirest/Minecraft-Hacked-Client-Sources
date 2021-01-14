package de.iotacb.client.gui.alt;

import java.awt.Color;

import de.iotacb.client.Client;

public class AltSlot {
	
	private double x, y, width, height;
	
	private int slotId;
	
	private String email, password;
	
	private boolean hovered;
	
	private GuiAltManager screen;
	
	public AltSlot(double x, double y, double width, double height, int slotId, String email, String password, GuiAltManager screen) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.slotId = slotId;
		this.email = email;
		this.password = password;
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
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getType() {
		if (getEmail().endsWith("@alt.com") && (getPassword().isEmpty() || getPassword().equalsIgnoreCase("thealtening"))) {
			return "TA";
		} else if (getEmail().endsWith("=") && getPassword().isEmpty()) {
			return "V";
		} else if (getEmail().contains("@") && !getPassword().isEmpty()) {
			return "M";
		} else {
			return "C";
		}
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
		Color color = new Color(20, 20, 20, 200);
		
		if (hovered) {
			color = color.brighter();
		} else if (screen.selectedSlot == getSlotId()) {
			color = color.brighter().brighter();
			if (hovered) {
				color.darker();
			}
		}
		
		Client.RENDER2D.rect(getX(), getY(), getWidth(), getHeight(), color);
		Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow(getEmail(), getX() + 10, getY() + 10 +  (password.isEmpty() ? 5 : 0), Color.white);
		
		final String password = getPassword().replaceAll(".", "*");
		
		Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow(password, getX() + 10, getY() + getHeight() - Client.INSTANCE.getFontManager().getDefaultFont().getHeight(password) - 10, Color.white);

		Client.INSTANCE.getFontManager().getBigFont().drawStringWithShadow(getType(), getX() + getWidth() - Client.INSTANCE.getFontManager().getBigFont().getWidth(getType()) - 10, getY() + getHeight() - Client.INSTANCE.getFontManager().getBigFont().getHeight(getType()) - 10, Color.white);
	}
}
