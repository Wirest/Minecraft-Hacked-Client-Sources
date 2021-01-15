package org.darkstorm.minecraft.gui.theme.simple;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import org.darkstorm.minecraft.gui.component.CheckButton;
import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.theme.AbstractComponentUI;
import org.darkstorm.minecraft.gui.util.RenderUtil;
import org.lwjgl.input.Mouse;

public class SimpleCheckButtonUI extends AbstractComponentUI<CheckButton> {
	private final SimpleTheme theme;

	SimpleCheckButtonUI(SimpleTheme theme) {
		super(CheckButton.class);
		this.theme = theme;

		foreground = Color.WHITE;
		background = new Color(128, 128, 128, 128);
	}

	@Override
	protected void renderComponent(CheckButton button) {
		translateComponent(button, false);
		Rectangle area = button.getArea();
		glEnable(GL_BLEND);
		glDisable(GL_CULL_FACE);

		glDisable(GL_TEXTURE_2D);
		RenderUtil.setColor(button.getBackgroundColor());
		int size = area.height - 4;
		glBegin(GL_QUADS);
		{
			glVertex2d(2, 2);
			glVertex2d(size + 2, 2);
			glVertex2d(size + 2, size + 2);
			glVertex2d(2, size + 2);
		}
		glEnd();
		if (button.isSelected()) {
			glColor4f(1f, 0.3f, 0.3f, 0.5f);
			glBegin(GL_QUADS);
			{
				glVertex2d(2.5, 2.5);
				glVertex2d(size + 1.5, 2.5);
				glVertex2d(size + 1.5, size + 1.5);
				glVertex2d(2.5, size + 1.5);
			}
			glEnd();
		}
		glLineWidth(2.0f);
		glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
		glBegin(GL_LINE_LOOP);
		{
			glVertex2d(2, 2);
			glVertex2d(size + 2, 2);
			glVertex2d(size + 2, size + 2);
			glVertex2d(2 - 0, size + 2);
		}
		glEnd();
		Point mouse = RenderUtil.calculateMouseLocation();
		mouse.y += 1;
		Component parent = button.getParent();
		while (parent != null) {
			mouse.x -= parent.getX();
			mouse.y -= parent.getY();
			parent = parent.getParent();
		}
		if (area.contains(mouse)) {
			glColor4f(0.0f, 0.0f, 0.0f, Mouse.isButtonDown(0) ? 0.5f : 0.3f);
			glBegin(GL_QUADS);
			{
				glVertex2d(0, 0);
				glVertex2d(area.width, 0);
				glVertex2d(area.width, area.height);
				glVertex2d(0, area.height);
			}
			glEnd();
		}
		glEnable(GL_TEXTURE_2D);

		String text = button.getText();
		theme.getFontRenderer().drawStringWithShadow(text, size + 4, area.height / 2 - theme.getFontRenderer().FONT_HEIGHT / 2,
				RenderUtil.toRGBA(button.getForegroundColor()));

		glEnable(GL_CULL_FACE);
		glDisable(GL_BLEND);
		translateComponent(button, true);
	}

	@Override
	protected Dimension getDefaultComponentSize(CheckButton component) {
		return new Dimension(
				theme.getFontRenderer().getStringWidth(component.getText()) + theme.getFontRenderer().FONT_HEIGHT + 6,
				theme.getFontRenderer().FONT_HEIGHT + 4);
	}

	@Override
	protected Rectangle[] getInteractableComponentRegions(CheckButton component) {
		return new Rectangle[] { new Rectangle(0, 0, component.getWidth(), component.getHeight()) };
	}

	@Override
	protected void handleComponentInteraction(CheckButton component, Point location, int button) {
		if (location.x <= component.getWidth() && location.y <= component.getHeight() && button == 0)
			component.press();
	}
}
