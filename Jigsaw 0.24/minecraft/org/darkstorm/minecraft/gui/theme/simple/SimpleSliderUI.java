package org.darkstorm.minecraft.gui.theme.simple;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import org.darkstorm.minecraft.gui.component.Container;
import org.darkstorm.minecraft.gui.component.Slider;
import org.darkstorm.minecraft.gui.theme.AbstractComponentUI;
import org.darkstorm.minecraft.gui.util.RenderUtil;
import org.lwjgl.input.Mouse;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class SimpleSliderUI extends AbstractComponentUI<Slider> {
	private SimpleTheme theme;
	private Color textColor;
	private Color displayColor;

	public SimpleSliderUI(SimpleTheme theme) {
		super(Slider.class);
		this.theme = theme;

		displayColor = Color.WHITE;
		textColor = Color.WHITE;
		foreground = new Color(50, 50, 240);
		background = new Color(50, 50, 240, 100);
	}

	@Override
	protected void renderComponent(Slider component) {
		translateComponent(component, false);
		GlStateManager.pushMatrix();
		glEnable(GL_BLEND);
		glDisable(GL_CULL_FACE);
		Rectangle area = component.getArea();
		int fontSize = theme.getFontRenderer().FONT_HEIGHT;
		FontRenderer fontRenderer = theme.getFontRenderer();
		fontRenderer.drawStringWithShadow(component.getText(), 2, 1, RenderUtil.toRGBA(textColor));
		String content = null;
		switch (component.getValueDisplay()) {
		case DECIMAL:
			content = String.format("%,.3f", component.getValue());
			break;
		case INTEGER:
			content = String.format("%,d", Long.valueOf(Math.round(component.getValue())));
			break;
		case PERCENTAGE:
			int percent = (int) Math.round((component.getValue() - component.getMinimumValue())
					/ (component.getMaximumValue() - component.getMinimumValue()) * 100D);
			content = String.format("%d%%", percent);
		default:
		}
		if (content != null) {
			String suffix = component.getContentSuffix();
			if (suffix != null && !suffix.trim().isEmpty())
				content = content.concat(" ").concat(suffix);
			fontRenderer.drawStringWithShadow(content, component.getWidth() - fontRenderer.getStringWidth(content) - 2, 0,
					RenderUtil.toRGBA(displayColor));
		}
		glDisable(GL_TEXTURE_2D);

		RenderUtil.setColor(new Color(0.7f, 0.3f, 0.3f, 1f));
		glLineWidth(2f);
		glBegin(GL_LINE_LOOP);
		{
			glVertex2d(0, fontSize + 2D);
			glVertex2d(area.width, fontSize + 2D);
			glVertex2d(area.width, area.height);
			glVertex2d(0, area.height);
		}
		glEnd();

		double sliderPercentage = (component.getValue() - component.getMinimumValue())
				/ (component.getMaximumValue() - component.getMinimumValue());
		RenderUtil.setColor(new Color(0.9f, 0.3f, 0.3f, 1f));
		glBegin(GL_QUADS);
		{
			glVertex2d(0, fontSize + 2D);
			glVertex2d(area.width * sliderPercentage, fontSize + 2D);
			glVertex2d(area.width * sliderPercentage, area.height);
			glVertex2d(0, area.height);
		}
		glEnd();

		GlStateManager.popMatrix();
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_CULL_FACE);
		glDisable(GL_BLEND);
		translateComponent(component, true);
	}

	@Override
	protected Dimension getDefaultComponentSize(Slider component) {
		return new Dimension(Math.max(this.theme.fontRenderer.getStringWidth(component.getText() + "99,9999_") + 1, 80),
				8 + theme.getFontRenderer().FONT_HEIGHT);
	}

	@Override
	protected Rectangle[] getInteractableComponentRegions(Slider component) {
		return new Rectangle[] { new Rectangle(0, theme.getFontRenderer().FONT_HEIGHT + 2, component.getWidth(),
				component.getHeight() - theme.getFontRenderer().FONT_HEIGHT) };
	}

	@Override
	protected void handleComponentInteraction(Slider component, Point location, int button) {
		if (getInteractableComponentRegions(component)[0].contains(location) && button == 0)
			if (Mouse.isButtonDown(button) && !component.isValueChanging())
				component.setValueChanging(true);
			else if (!Mouse.isButtonDown(button) && component.isValueChanging())
				component.setValueChanging(false);
	}

	@Override
	protected void handleComponentUpdate(Slider component) {
		if (component.isValueChanging()) {
			if (!Mouse.isButtonDown(0)) {
				component.setValueChanging(false);
				return;
			}
			Point mouse = RenderUtil.calculateMouseLocation();
			Container parent = component.getParent();
			if (parent != null)
				mouse.translate(-parent.getX(), -parent.getY());
			double percent = (double) mouse.x / (double) component.getWidth();
			double value = component.getMinimumValue()
					+ (percent * (component.getMaximumValue() - component.getMinimumValue()));
			component.setValue(value);
		}
	}
}
