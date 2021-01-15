// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.theme.simple;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import org.darkstorm.minecraft.gui.component.Container;
import org.darkstorm.minecraft.gui.component.Slider;
import org.darkstorm.minecraft.gui.theme.AbstractComponentUI;
import org.darkstorm.minecraft.gui.util.RenderUtil;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;

public class SimpleSliderUI extends AbstractComponentUI<Slider> {
	private SimpleTheme theme;

	public SimpleSliderUI(final SimpleTheme theme) {
		super(Slider.class);
		this.theme = theme;
		this.foreground = Color.white;
		this.background = new Color(128, 128, 128, 192);
	}

	@Override
	protected void renderComponent(final Slider component) {
		this.translateComponent(component, false);
		GL11.glEnable(3042);
		GL11.glDisable(2884);
		final Rectangle area = component.getArea();
		final int fontSize = this.theme.getFontRenderer().FONT_HEIGHT;
		final FontRenderer fontRenderer = this.theme.getFontRenderer();
		fontRenderer.drawString(component.getText(), 0, 0, RenderUtil.toRGBA(component.getForegroundColor()));
		String content = null;
		switch (component.getValueDisplay()) {
		case DECIMAL: {
			content = String.format("%,.3f", component.getValue());
			break;
		}
		case INTEGER: {
			content = String.format("%,d", Math.round(component.getValue()));
			break;
		}
		case PERCENTAGE: {
			final int percent = (int) Math.round((component.getValue() - component.getMinimumValue())
					/ (component.getMaximumValue() - component.getMinimumValue()) * 100.0);
			content = String.format("%d%%", percent);
			break;
		}
		default:
			break;
		}
		if (content != null) {
			final String suffix = component.getContentSuffix();
			if (suffix != null && !suffix.trim().isEmpty()) {
				content = content.concat(" ").concat(suffix);
			}
			fontRenderer.drawString(content, component.getWidth() - fontRenderer.getStringWidth(content), 0,
					RenderUtil.toRGBA(component.getForegroundColor()));
		}
		GL11.glDisable(3553);
		RenderUtil.setColor(component.getBackgroundColor());
		GL11.glLineWidth(0.9f);
		GL11.glBegin(2);
		GL11.glVertex2d(0.0, fontSize + 2.0);
		GL11.glVertex2d(area.width, fontSize + 2.0);
		GL11.glVertex2d(area.width, area.height);
		GL11.glVertex2d(0.0, area.height);
		GL11.glEnd();
		final double sliderPercentage = (component.getValue() - component.getMinimumValue())
				/ (component.getMaximumValue() - component.getMinimumValue());
		RenderUtil.setColor(component.getForegroundColor());
		GL11.glBegin(7);
		GL11.glVertex2d(0.0, fontSize + 2.0);
		GL11.glVertex2d(area.width * sliderPercentage, fontSize + 2.0);
		GL11.glVertex2d(area.width * sliderPercentage, area.height);
		GL11.glVertex2d(0.0, area.height);
		GL11.glEnd();
		GL11.glEnable(3553);
		this.translateComponent(component, true);
	}

	@Override
	protected Dimension getDefaultComponentSize(final Slider component) {
		return new Dimension(100, 8 + this.theme.getFontRenderer().FONT_HEIGHT);
	}

	@Override
	protected Rectangle[] getInteractableComponentRegions(final Slider component) {
		return new Rectangle[] { new Rectangle(0, this.theme.getFontRenderer().FONT_HEIGHT + 2, component.getWidth(),
				component.getHeight() - this.theme.getFontRenderer().FONT_HEIGHT) };
	}

	@Override
	protected void handleComponentInteraction(final Slider component, final Point location, final int button) {
		if (this.getInteractableComponentRegions(component)[0].contains(location) && button == 0) {
			if (Mouse.isButtonDown(button) && !component.isValueChanging()) {
				component.setValueChanging(true);
			} else if (!Mouse.isButtonDown(button) && component.isValueChanging()) {
				component.setValueChanging(false);
			}
		}
	}

	@Override
	protected void handleComponentUpdate(final Slider component) {
		if (component.isValueChanging()) {
			if (!Mouse.isButtonDown(0)) {
				component.setValueChanging(false);
				return;
			}
			final Point mouse = RenderUtil.calculateMouseLocation();
			final Container parent = component.getParent();
			if (parent != null) {
				mouse.translate(-parent.getX(), -parent.getY());
			}
			final double percent = mouse.x / component.getWidth();
			final double value = component.getMinimumValue()
					+ percent * (component.getMaximumValue() - component.getMinimumValue());
			component.setValue(value);
		}
	}
}
