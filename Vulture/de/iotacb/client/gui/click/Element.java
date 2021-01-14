package de.iotacb.client.gui.click;

import de.iotacb.client.Client;
import de.iotacb.client.module.modules.render.ClickGui;
import de.iotacb.client.utilities.render.font.TTFFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

public abstract class Element {
	
	private double posX, posY;
	private double width, height;
	
	private boolean hovered;
	
	private Element parent;
	
	private static final Minecraft MC = Minecraft.getMinecraft();
	
	public Element(double posX, double posY, double width, double height, Element parent) {
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		this.initElement();
		
		this.parent = parent;
	}
	
	public void initElement() {}
	
	public abstract void updateElement(int mouseX, int mouseY);
	
	public abstract void drawElement(int mouseX, int mouseY);
	
	public abstract void clickElement(int mouseX, int mouseY, int mouseButton);
	
	public double getPosX() {
		return posX;
	}
	
	public double getPosY() {
		return posY;
	}
	
	public double getWidth() {
		return width;
	}
	
	public double getHeight() {
		return height;
	}
	
	public final Minecraft getMc() {
		return MC;
	}
	
	public final Element getParent() {
		return parent;
	}
	
	public final boolean isHovered(int mouseX, int mouseY) {
		return (mouseX > getPosX() && mouseX < getPosX() + getWidth()) && (mouseY > getPosY() && mouseY < getPosY() + getHeight());
	}
	
	public final boolean isHovered(int mouseX, int mouseY, double posX, double posY, double width, double height) {
		return (mouseX > posX && mouseX < posX + width) && (mouseY > posY && mouseY < posY + height);
	}
	
	public final void setPosX(double posX) {
		this.posX = posX;
	}
	
	public final void setPosY(double posY) {
		this.posY = posY;
	}
	
	public final void setWidth(double width) {
		this.width = width;
	}
	
	public final void setHeight(double height) {
		this.height = height;
	}
	
	public final void setParent(Element parent) {
		this.parent = parent;
	}
	
	public void setPosition(double posX, double posY) {
		this.posX = posX;
		this.posY = posY;
	}
	
	public final void setHovered(boolean hovered) {
		this.hovered = hovered;
	}
	
	public final TTFFontRenderer getFontRenderer() {
		return Client.INSTANCE.getFontManager().getDefaultFont();
	}
	
	public final boolean isDarkMode() {
		return Client.INSTANCE.getModuleManager().getModuleByClass(ClickGui.class).getValueByName("ClickGuiTheme").getComboValue().equalsIgnoreCase("Dark");
	}

}
