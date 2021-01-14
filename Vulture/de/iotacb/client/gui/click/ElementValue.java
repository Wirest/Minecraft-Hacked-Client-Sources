package de.iotacb.client.gui.click;

import de.iotacb.client.utilities.values.Value;

public abstract class ElementValue extends Element {
	
	double posXOffset;
	double posYOffset;
	
	Value value;
	
	boolean isFirst, isLast, isLonely;

	public ElementValue(double posX, double posY, double width, double height, Element parent) {
		super(posX, posY, width, height, parent);
	}

	@Override
	public abstract void updateElement(int mouseX, int mouseY);

	@Override
	public abstract void drawElement(int mouseX, int mouseY);

	@Override
	public abstract void clickElement(int mouseX, int mouseY, int mouseButton);

	public final double getPosXOffset() {
		return posXOffset;
	}
	
	public final double getPosYOffset() {
		return posYOffset;
	}
	
	@Override
	public final double getPosX() {
		return super.getPosX() + getPosXOffset();
	}
	
	@Override
	public final double getPosY() {
		return super.getPosY() + getPosYOffset();
	}
	
	public final Value getValue() {
		return value;
	}
	
	public final boolean isFirst() {
		return isFirst;
	}
	
	public final boolean isLast() {
		return isLast;
	}
	
	public final boolean isLonely() {
		return isLonely;
	}
	
	public final void setPosXOffset(double posXOffset) {
		this.posXOffset = posXOffset;
	}
	
	public final void setPosYOffset(double posYOffset) {
		this.posYOffset = posYOffset;
	}
	
	public final void setPosOffsets(double posXOffset, double posYOffset) {
		this.posXOffset = posXOffset;
		this.posYOffset = posYOffset;
	}
	
	public final void setValue(Value value) {
		this.value = value;
	}
	
	public final void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}
	
	public final void setLast(boolean isLast) {
		this.isLast = isLast;
	}
	
	public final void setLonely(boolean isLonely) {
		this.isLonely = isLonely;
	}
	
}
