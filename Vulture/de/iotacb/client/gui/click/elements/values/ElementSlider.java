package de.iotacb.client.gui.click.elements.values;

import java.awt.Color;

import org.lwjgl.input.Mouse;

import de.iotacb.client.Client;
import de.iotacb.client.gui.click.ClickConfig;
import de.iotacb.client.gui.click.Element;
import de.iotacb.client.gui.click.ElementValue;
import de.iotacb.client.gui.click.elements.ElementModule;
import de.iotacb.client.utilities.render.Render2D;
import de.iotacb.client.utilities.render.color.BetterColor;

public class ElementSlider extends ElementValue {
	
	private boolean dragging;
	private double startMouseX, startMouseY;

	public ElementSlider(double posX, double posY, double width, double height, Element parent) {
		super(posX, posY, width, height, parent);
	}

	@Override
	public void updateElement(int mouseX, int mouseY) {
		if (!Mouse.isButtonDown(0)) dragging = false;
		if (dragging) {
			double min = getValue().getMinMax().getMin();
			double max = getValue().getMinMax().getMax();
			double increment = getValue().getMinMax().getIncrement();
			double absoluteValue = mouseX - (getPosX() + 1.0F);
			double percentage = absoluteValue / (getWidth() - 2.0F);
			percentage = Math.min(Math.max(0, percentage), 1.0);
			double valueRel = (max - min) * percentage;
			double value = min + valueRel;
			value = Math.round(value * (1.0 / increment)) / (1.0 / increment);
			getValue().setNumberValue(value);
		}
	}

	@Override
	public void drawElement(int mouseX, int mouseY) {
		final int alpha = getValue().isEnabled() ? 255 : 100;
		if (isFirst()) {
			Client.RENDER2D.gradientSideways(getPosX(), getPosY(), getWidth(), getHeight(), isHovered(mouseX, mouseY) ? isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.darker().setAlpha(alpha) : ClickConfig.MODULE_BACKGROUND_LIGHT.darker().setAlpha(alpha) : isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.setAlpha(alpha) : ClickConfig.MODULE_BACKGROUND_LIGHT.setAlpha(alpha), Client.INSTANCE.getClientColor());
//			Client.RENDER2D.roundedRectRightTop(getPosX(), getPosY(), getWidth(), getHeight(), 10, isHovered(mouseX, mouseY) ? isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.darker().setAlpha(alpha) : ClickConfig.MODULE_BACKGROUND_LIGHT.darker().setAlpha(alpha) : isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.setAlpha(alpha) : ClickConfig.MODULE_BACKGROUND_LIGHT.setAlpha(alpha), Client.INSTANCE.getClientColor());
		} else {
			Client.RENDER2D.gradientSideways(getPosX(), getPosY(), getWidth(), getHeight(), isHovered(mouseX, mouseY) ? isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.darker().setAlpha(alpha) : ClickConfig.MODULE_BACKGROUND_LIGHT.darker().setAlpha(alpha) : isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.setAlpha(alpha) : ClickConfig.MODULE_BACKGROUND_LIGHT.setAlpha(alpha), Client.INSTANCE.getClientColor());
		}
		Client.RENDER2D.rect(getPosX(), getPosY() + getHeight() / 4 - 1, getWidth(), getHeight() / 2 + 2, new BetterColor(20, 20, 20).setAlpha(alpha));
		final double sliderWidth = (getValue().getNumberValue() / getValue().getMinMax().getMax()) * getWidth();
		Client.RENDER2D.rect(getPosX(), getPosY() + getHeight() / 4, sliderWidth, getHeight() / 2, isHovered(mouseX, mouseY) ? new BetterColor(Client.INSTANCE.getClientColor().addColoring(40)).setAlpha(getValue().isEnabled() ? 200 : 150) : Client.INSTANCE.getClientColor().setAlpha(alpha));
		final String value = getValue().getValueName().substring(((ElementModule)getParent()).getModule().getName().length());
		final String text = ""+getValue().getNumberValue();
		getFontRenderer().drawStringWithShadow(value, getPosX() + 5, getPosY() + getHeight() / 2 - getFontRenderer().getHeight(value) / 2, isDarkMode() ? ClickConfig.MODULE_TITLE_DARK.setAlpha(alpha) : ClickConfig.MODULE_TITLE_LIGHT.setAlpha(alpha));
		getFontRenderer().drawStringWithShadow(text, getPosX() + getWidth() - getFontRenderer().getWidth(text) - 5, getPosY() + getHeight() / 2 - getFontRenderer().getHeight(text) / 2, isDarkMode() ? ClickConfig.MODULE_TITLE_DARK.setAlpha(alpha) : ClickConfig.MODULE_TITLE_LIGHT.setAlpha(alpha));
	}

	@Override
	public void clickElement(int mouseX, int mouseY, int mouseButton) {
		if (isHovered(mouseX, mouseY)) {
			if (mouseButton == 0) {
				this.startMouseX = mouseX;
				this.startMouseY = mouseY;
				this.dragging = true;
			}
		}
	}
	
	public final boolean isDragging() {
		return dragging;
	}
	
	public final void setDragging(boolean dragging) {
		this.dragging = dragging;
	}

}
