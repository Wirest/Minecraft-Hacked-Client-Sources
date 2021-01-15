// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.theme.simple;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import org.darkstorm.minecraft.gui.component.ProgressBar;
import org.darkstorm.minecraft.gui.theme.AbstractComponentUI;
import org.darkstorm.minecraft.gui.util.RenderUtil;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;

public class SimpleProgressBarUI extends AbstractComponentUI<ProgressBar> {
	private SimpleTheme theme;

	public SimpleProgressBarUI(final SimpleTheme theme) {
		super(ProgressBar.class);
		this.theme = theme;
		this.foreground = Color.white;
		this.background = new Color(128, 128, 128, 192);
	}

	@Override
	protected void renderComponent(final ProgressBar component) {
		final Rectangle area = component.getArea();
		final int fontSize = this.theme.getFontRenderer().FONT_HEIGHT;
		final FontRenderer fontRenderer = this.theme.getFontRenderer();
		this.translateComponent(component, false);
		GL11.glEnable(3042);
		GL11.glDisable(2884);
		GL11.glDisable(3553);
		RenderUtil.setColor(component.getBackgroundColor());
		GL11.glLineWidth(0.9f);
		GL11.glBegin(2);
		GL11.glVertex2d(0.0, 0.0);
		GL11.glVertex2d(area.width, 0.0);
		GL11.glVertex2d(area.width, area.height);
		GL11.glVertex2d(0.0, area.height);
		GL11.glEnd();
		final double barPercentage = (component.getValue() - component.getMinimumValue())
				/ (component.getMaximumValue() - component.getMinimumValue());
		RenderUtil.setColor(component.getForegroundColor());
		GL11.glBegin(7);
		GL11.glVertex2d(0.0, 0.0);
		GL11.glVertex2d(area.width * barPercentage, 0.0);
		GL11.glVertex2d(area.width * barPercentage, area.height);
		GL11.glVertex2d(0.0, area.height);
		GL11.glEnd();
		GL11.glEnable(3553);
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
			GL11.glBlendFunc(775, 769);
			fontRenderer.drawString(content, component.getWidth() / 2 - fontRenderer.getStringWidth(content) / 2,
					component.getHeight() / 2 - fontSize / 2, RenderUtil.toRGBA(component.getForegroundColor()));
			GL11.glBlendFunc(770, 771);
		}
		GL11.glEnable(2884);
		GL11.glDisable(3042);
		this.translateComponent(component, true);
	}

	@Override
	protected Dimension getDefaultComponentSize(final ProgressBar component) {
		return new Dimension(100, 4 + this.theme.getFontRenderer().FONT_HEIGHT);
	}

	@Override
	protected Rectangle[] getInteractableComponentRegions(final ProgressBar component) {
		return new Rectangle[0];
	}
}
