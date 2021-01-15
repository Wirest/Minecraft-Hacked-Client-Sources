package info.spicyclient.ClickGUI;

import info.spicyclient.modules.Module;

public class Tab {
	
	public float x, y, offsetX, offsetY;
	public boolean extended = false;
	
	public String name;
	public Module.Category category;
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public float getOffsetX() {
		return offsetX;
	}
	
	public void setOffsetX(float offsetX) {
		this.offsetX = offsetX;
	}
	
	public float getOffsetY() {
		return offsetY;
	}
	
	public void setOffsetY(float offsetY) {
		this.offsetY = offsetY;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Module.Category getCategory() {
		return category;
	}
	
	public void setCategory(Module.Category category) {
		this.category = category;
	}
	
}
