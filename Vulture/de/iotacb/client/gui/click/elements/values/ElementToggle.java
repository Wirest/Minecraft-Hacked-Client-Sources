package de.iotacb.client.gui.click.elements.values;

import java.awt.Color;

import de.iotacb.client.Client;
import de.iotacb.client.gui.click.ClickConfig;
import de.iotacb.client.gui.click.Element;
import de.iotacb.client.gui.click.ElementValue;
import de.iotacb.client.gui.click.elements.ElementModule;
import de.iotacb.client.utilities.render.Render2D;
import de.iotacb.client.utilities.render.color.BetterColor;

public class ElementToggle extends ElementValue {

	public ElementToggle(double posX, double posY, double width, double height, Element parent) {
		super(posX, posY, width, height, parent);
	}

	@Override
	public void updateElement(int mouseX, int mouseY) {
		
	}

	@Override
	public void drawElement(int mouseX, int mouseY) {
		final int alpha = getValue().isEnabled() ? 255 : 100;
		if (isLonely()) {
//			Client.RENDER2D.roundedRectRight(getPosX(), getPosY(), getWidth(), getHeight(), 10, isHovered(mouseX, mouseY) ? isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.darker().setAlpha(alpha) : ClickConfig.MODULE_BACKGROUND_LIGHT.darker().setAlpha(alpha) : isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.setAlpha(alpha) : ClickConfig.MODULE_BACKGROUND_LIGHT.setAlpha(alpha), Client.INSTANCE.getClientColor().setAlpha(alpha));
			Client.RENDER2D.gradientSideways(getPosX(), getPosY(), getWidth(), getHeight(), isHovered(mouseX, mouseY) ? isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.darker().setAlpha(alpha) : ClickConfig.MODULE_BACKGROUND_LIGHT.darker().setAlpha(alpha) : isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.setAlpha(alpha) : ClickConfig.MODULE_BACKGROUND_LIGHT.setAlpha(alpha), Client.INSTANCE.getClientColor().setAlpha(alpha));
		} else {
			if (isFirst()) {
//				Client.RENDER2D.roundedRectRightTop(getPosX(), getPosY(), getWidth(), getHeight(), 10, isHovered(mouseX, mouseY) ? isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.darker().setAlpha(alpha) : ClickConfig.MODULE_BACKGROUND_LIGHT.darker().setAlpha(alpha) : isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.setAlpha(alpha) : ClickConfig.MODULE_BACKGROUND_LIGHT.setAlpha(alpha), Client.INSTANCE.getClientColor().setAlpha(alpha));
				Client.RENDER2D.gradientSideways(getPosX(), getPosY(), getWidth(), getHeight(), isHovered(mouseX, mouseY) ? isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.darker().setAlpha(alpha) : ClickConfig.MODULE_BACKGROUND_LIGHT.darker().setAlpha(alpha) : isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.setAlpha(alpha) : ClickConfig.MODULE_BACKGROUND_LIGHT.setAlpha(alpha), Client.INSTANCE.getClientColor().setAlpha(alpha));
			} else {
				if (isLast()) {
//					Client.RENDER2D.roundedRectRightBottom(getPosX(), getPosY(), getWidth(), getHeight(), 10, isHovered(mouseX, mouseY) ? isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.darker().setAlpha(alpha) : ClickConfig.MODULE_BACKGROUND_LIGHT.darker().setAlpha(alpha) : isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.setAlpha(alpha) : ClickConfig.MODULE_BACKGROUND_LIGHT.setAlpha(alpha), Client.INSTANCE.getClientColor().setAlpha(alpha));
					Client.RENDER2D.gradientSideways(getPosX(), getPosY(), getWidth(), getHeight(), isHovered(mouseX, mouseY) ? isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.darker().setAlpha(alpha) : ClickConfig.MODULE_BACKGROUND_LIGHT.darker().setAlpha(alpha) : isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.setAlpha(alpha) : ClickConfig.MODULE_BACKGROUND_LIGHT.setAlpha(alpha), Client.INSTANCE.getClientColor().setAlpha(alpha));
				} else {
					Client.RENDER2D.gradientSideways(getPosX(), getPosY(), getWidth(), getHeight(), isHovered(mouseX, mouseY) ? isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.darker().setAlpha(alpha) : ClickConfig.MODULE_BACKGROUND_LIGHT.darker().setAlpha(alpha) : isDarkMode() ? ClickConfig.MODULE_BACKGROUND_DARK.setAlpha(alpha) : ClickConfig.MODULE_BACKGROUND_LIGHT.setAlpha(alpha), Client.INSTANCE.getClientColor().setAlpha(alpha));
				}
			}
		}
		final String text = getValue().getValueName().substring(((ElementModule)getParent()).getModule().getName().length());
		getFontRenderer().drawStringWithShadow(text, getPosX() + 5, getPosY() + getHeight() / 2 - getFontRenderer().getHeight(text) / 2, getValue().getBooleanValue() ? Client.INSTANCE.getClientColor().addColoring(40).setAlpha(alpha) : isDarkMode() ? ClickConfig.MODULE_TITLE_DARK.setAlpha(alpha) : new Color(0, 0, 0));
	}

	@Override
	public void clickElement(int mouseX, int mouseY, int mouseButton) {
		if (isHovered(mouseX, mouseY)) {
			if (mouseButton == 0) {
				getValue().setBooleanValue(!getValue().getBooleanValue());
			}
		}
	}
}
