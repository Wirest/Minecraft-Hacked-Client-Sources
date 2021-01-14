package de.iotacb.client.gui.click.elements.values;

import java.awt.Color;
import java.util.List;

import de.iotacb.client.Client;
import de.iotacb.client.gui.click.ClickConfig;
import de.iotacb.client.gui.click.Element;
import de.iotacb.client.gui.click.ElementValue;
import de.iotacb.client.gui.click.elements.ElementModule;
import de.iotacb.client.utilities.render.Render2D;
import de.iotacb.client.utilities.render.color.BetterColor;

public class ElementCombo extends ElementValue {
	
	private boolean extended;
	
	public ElementCombo(double posX, double posY, double width, double height, Element parent) {
		super(posX, posY, width, height, parent);
	}

	@Override
	public void updateElement(int mouseX, int mouseY) {
	}

	@Override
	public void drawElement(int mouseX, int mouseY) {
		if (isFirst()) {
			if (isExtended()) {
				Client.RENDER2D.rect(getPosX(), getPosY(), getWidestWidth(), getHeight(), isHovered(mouseX, mouseY) ? isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.darker() : ClickConfig.MODULE_BACKGROUND_LIGHT.darker() : isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK : ClickConfig.MODULE_BACKGROUND_LIGHT);
			} else {
//				Client.RENDER2D.roundedRectRightTop(getPosX(), getPosY(), getWidestWidth(), getHeight(), 10, isHovered(mouseX, mouseY) ? isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.darker() : ClickConfig.MODULE_BACKGROUND_LIGHT.darker() : isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK : ClickConfig.MODULE_BACKGROUND_LIGHT, Client.INSTANCE.getClientColor());
				Client.RENDER2D.gradientSideways(getPosX(), getPosY(), getWidestWidth(), getHeight(), isHovered(mouseX, mouseY) ? isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.darker() : ClickConfig.MODULE_BACKGROUND_LIGHT.darker() : isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK : ClickConfig.MODULE_BACKGROUND_LIGHT, Client.INSTANCE.getClientColor());
			}
		} else {
			if (!isLast()) {
				if (isExtended()) {
					Client.RENDER2D.rect(getPosX(), getPosY(), getWidestWidth(), getHeight(), isHovered(mouseX, mouseY) ? isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.darker() : ClickConfig.MODULE_BACKGROUND_LIGHT.darker() : isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK : ClickConfig.MODULE_BACKGROUND_LIGHT);
				} else {
					Client.RENDER2D.gradientSideways(getPosX(), getPosY(), getWidestWidth(), getHeight(), isHovered(mouseX, mouseY) ? isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.darker() : ClickConfig.MODULE_BACKGROUND_LIGHT.darker() : isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK : ClickConfig.MODULE_BACKGROUND_LIGHT, Client.INSTANCE.getClientColor());
				}
			}
		}
		if (isExtended()) {
			double posYOffset = 0;
			final int size = getValue().getComboValues().size();
			for (int i = 0; i < size; i++) {
				String mode = getValue().getComboValues().get(i);
				if (i == 0) {
//					Client.RENDER2D.roundedRectRightTop(getPosX() + getWidestWidth(), getPosY() + posYOffset, getWidth(), getHeight(), 10, isHovered(mouseX, mouseY, getPosX() + getWidth(), getPosY() + posYOffset) ? isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.darker() : ClickConfig.MODULE_BACKGROUND_LIGHT.darker() : isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK : ClickConfig.MODULE_BACKGROUND_LIGHT, Client.INSTANCE.getClientColor());
					Client.RENDER2D.gradientSideways(getPosX() + getWidestWidth(), getPosY() + posYOffset, getWidth(), getHeight(), isHovered(mouseX, mouseY, getPosX() + getWidth(), getPosY() + posYOffset) ? isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.darker() : ClickConfig.MODULE_BACKGROUND_LIGHT.darker() : isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK : ClickConfig.MODULE_BACKGROUND_LIGHT, Client.INSTANCE.getClientColor());
				} else {
					if (size > 1) {
						if (size == 2 && size > 2) {
//							Client.RENDER2D.roundedRectRight(getPosX() + getWidestWidth(), getPosY() + posYOffset, getWidth(), getHeight(), 10, isHovered(mouseX, mouseY, getPosX() + getWidth(), getPosY() + posYOffset) ? isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.darker() : ClickConfig.MODULE_BACKGROUND_LIGHT.darker() : isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK : ClickConfig.MODULE_BACKGROUND_LIGHT, Client.INSTANCE.getClientColor());
							Client.RENDER2D.gradientSideways(getPosX() + getWidestWidth(), getPosY() + posYOffset, getWidth(), getHeight(), isHovered(mouseX, mouseY, getPosX() + getWidth(), getPosY() + posYOffset) ? isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.darker() : ClickConfig.MODULE_BACKGROUND_LIGHT.darker() : isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK : ClickConfig.MODULE_BACKGROUND_LIGHT, Client.INSTANCE.getClientColor());
						} else {
							if (i == size - 1) {
//								Client.RENDER2D.roundedRectRightBottom(getPosX() + getWidestWidth(), getPosY() + posYOffset, getWidth(), getHeight(), 10, isHovered(mouseX, mouseY, getPosX() + getWidth(), getPosY() + posYOffset) ? isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.darker() : ClickConfig.MODULE_BACKGROUND_LIGHT.darker() : isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK : ClickConfig.MODULE_BACKGROUND_LIGHT, Client.INSTANCE.getClientColor());
								Client.RENDER2D.gradientSideways(getPosX() + getWidestWidth(), getPosY() + posYOffset, getWidth(), getHeight(), isHovered(mouseX, mouseY, getPosX() + getWidth(), getPosY() + posYOffset) ? isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.darker() : ClickConfig.MODULE_BACKGROUND_LIGHT.darker() : isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK : ClickConfig.MODULE_BACKGROUND_LIGHT, Client.INSTANCE.getClientColor());
							} else {
								Client.RENDER2D.gradientSideways(getPosX() + getWidestWidth(), getPosY() + posYOffset, getWidth(), getHeight(), isHovered(mouseX, mouseY, getPosX() + getWidth(), getPosY() + posYOffset) ? isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.darker() : ClickConfig.MODULE_BACKGROUND_LIGHT.darker() : isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK : ClickConfig.MODULE_BACKGROUND_LIGHT, Client.INSTANCE.getClientColor());
							}
						}
					}
				}
				getFontRenderer().drawStringWithShadow(mode, getPosX() + getWidestWidth() + 5, getPosY() + posYOffset + getFontRenderer().getHeight(mode) / 2, getValue().getComboValue().equalsIgnoreCase(mode) ? Client.INSTANCE.getClientColor().addColoring(40) : isDarkMode() ? ClickConfig.MODULE_TITLE_DARK : ClickConfig.MODULE_TITLE_LIGHT);
				posYOffset += getHeight();
			}
		}
		final String text = "Mode: " + getValue().getComboValue().trim();
		getFontRenderer().drawStringWithShadow(text, getPosX() + 5, getPosY() + getHeight() / 2 - getFontRenderer().getHeight(text) / 2, isDarkMode() ? ClickConfig.MODULE_TITLE_DARK : ClickConfig.MODULE_TITLE_LIGHT);
	}

	@Override
	public void clickElement(int mouseX, int mouseY, int mouseButton) {
		if (isHovered(mouseX, mouseY)) {
			if (mouseButton == 1) {
				for (ElementValue value : ((ElementModule) getParent()).getValues()) {
					if (value instanceof ElementCombo) {
						if (value == this) continue;
						((ElementCombo) value).setExtended(false);
					}
				}
				if (getValue().getComboValues().size() > 1)
					setExtended(!isExtended());
			}
		}
		
		if (!isExtended()) return;
		
		double posYOffset = 0;
		for (String mode : getValue().getComboValues()) {
			if (isHovered(mouseX, mouseY, getPosX() + getWidestWidth(), getPosY() + posYOffset)) {
				if (mouseButton == 0) {
					getValue().setComboValue(mode);
				}
			}
			posYOffset += getHeight();
		}
	}
	
	double getWidestWidth() {
		double widestWidth = 100;
		for (String mode : getValue().getComboValues()) {
			double width = Client.INSTANCE.getFontManager().getDefaultFont().getWidth(mode);
			if (width > widestWidth) {
				widestWidth = width;
			}
		}
		return widestWidth;
	}
	
	public boolean isExtended() {
		return extended;
	}
	
	public void setExtended(boolean extended) {
		this.extended = extended;
	}
	
	boolean isHovered(int mouseX, int mouseY, double xPos, double yPos) {
		return isHovered(mouseX, mouseY, xPos, yPos, getWidestWidth(), getHeight());
	}

}
