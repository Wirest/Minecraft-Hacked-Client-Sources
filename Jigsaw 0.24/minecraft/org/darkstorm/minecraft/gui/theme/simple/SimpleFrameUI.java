package org.darkstorm.minecraft.gui.theme.simple;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
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

import org.darkstorm.minecraft.gui.component.Button;
import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.component.Frame;
import org.darkstorm.minecraft.gui.layout.Constraint;
import org.darkstorm.minecraft.gui.theme.AbstractComponentUI;
import org.darkstorm.minecraft.gui.util.RenderUtil;
import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;

public class SimpleFrameUI extends AbstractComponentUI<Frame> {
	private final SimpleTheme theme;

	SimpleFrameUI(SimpleTheme theme) {
		super(Frame.class);
		this.theme = theme;

		foreground = Color.WHITE;
		background = new Color(0, 0, 0, 0.5f);
	}

	@Override
	protected void renderComponent(Frame component) {
		boolean searched = true;
		boolean searching = Jigsaw.getSearchBar() != null && !Jigsaw.getSearch().isEmpty();
		if (searching) {
			// searched =
			// component.getTitle().toUpperCase().indexOf(Jigsaw.getSearch().toUpperCase())
			// != -1;
			for (Component comp : component.getChildren()) {
				if (comp instanceof Button) {
					Button btn = (Button) comp;
					if (btn.getText().toUpperCase().indexOf(Jigsaw.getSearch().toUpperCase()) != -1) {
						searched = true;
						break;
					}
					searched = false;
				}
			}
		}
		Rectangle area = new Rectangle(component.getArea());
		int fontHeight = theme.getFontRenderer().FONT_HEIGHT;
		translateComponent(component, false);
		glEnable(GL_BLEND);
		glDisable(GL_CULL_FACE);
		glDisable(GL_TEXTURE_2D);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		// Draw frame background
		if (component.isMinimized())
			area.height = fontHeight + 4;

		RenderUtil.setColor(new Color(0f, 0f, 0f, 0.5f));
		// Background
		if (!component.isMinimized()) {
			glBegin(GL_QUADS);
			{
				glVertex2d(1, fontHeight + 4);
				glVertex2d(area.width - 1, fontHeight + 4);
				glVertex2d(area.width - 1, area.height - 2);
				glVertex2d(1, area.height - 2);
			}
			glEnd();
		}
		if (!component.isMinimized()) {
			RenderUtil.setColor(ClientSettings.framebgColor1);
			glLineWidth(4.0f);
			// TODO Jigsaw niiice
			// glBegin(GL_LINE_LOOP);
			// {
			// glVertex2d(0 + 1, theme.getFontRenderer().FONT_HEIGHT + 3);
			// glVertex2d(area.width - 1, theme.getFontRenderer().FONT_HEIGHT +
			// 3);
			// glVertex2d(area.width - 1, area.height - 1);
			// glVertex2d(0 + 1, area.height - 1);
			// }
			// glEnd();

			// glBegin(GL_LINES);
			// {
			// glVertex2d(2, theme.getFontRenderer().FONT_HEIGHT + 5);
			// glVertex2d(area.width - 2, theme.getFontRenderer().FONT_HEIGHT +
			// 5);
			// }
			// glEnd();
		}

		// frame head
		RenderUtil.setColor(searching ? (searched ? new Color(0.3f, 0.75f, 0.3f, 1f) : ClientSettings.frameHeadColor)
				: ClientSettings.frameHeadColor);

		glBegin(GL_QUADS);
		{
			glVertex2d(0, 0);
			glVertex2d(area.width, 0);
			glVertex2d(area.width, theme.getFontRenderer().FONT_HEIGHT + 4);
			glVertex2d(0, theme.getFontRenderer().FONT_HEIGHT + 4);
		}
		glEnd();

		// Draw controls
		int offset = component.getWidth() - 2;
		Point mouse = RenderUtil.calculateMouseLocation();
		Component parent = component;
		while (parent != null) {
			mouse.x -= parent.getX();
			mouse.y -= parent.getY();
			parent = parent.getParent();
		}
		if (component.getArea().contains(mouse)) {

		}

		boolean[] checks = new boolean[] { component.isClosable(), component.isPinnable(), component.isMinimizable() };
		boolean[] overlays = new boolean[] { false, component.isPinned(), component.isMinimized() };
		for (int i = 0; i < checks.length; i++) {
			if (!checks[i])
				continue;
			RenderUtil.setColor(new Color(0.5f, 0.2f, 0.2f, 1f));
			glBegin(GL11.GL_TRIANGLE_FAN);
			{
				glVertex2d(offset - fontHeight, 2);
				glVertex2d(offset, 2);
				glVertex2d(offset - 3.5, fontHeight + 2);
			}
			glEnd();
			if (overlays[i]) {
				glColor4f(0.0f, 0.0f, 0.0f, 0.5f);
				glBegin(GL11.GL_TRIANGLE_FAN);
				{
					glVertex2d(offset - fontHeight, 2);
					glVertex2d(offset, 2);
					glVertex2d(offset - 3.5, fontHeight + 2);
				}
				glEnd();
			}
			if (mouse.x >= offset - fontHeight && mouse.x <= offset && mouse.y >= 2 && mouse.y <= fontHeight + 2) {
				glColor4f(0.0f, 0.0f, 0.0f, 0.3f);
				glBegin(GL11.GL_TRIANGLE_FAN);
				{
					glVertex2d(offset - fontHeight, 2);
					glVertex2d(offset, 2);
					glVertex2d(offset - 3.5, fontHeight + 2);
				}
				glEnd();
			}
			offset -= fontHeight + 2;
		}

		glEnable(GL_TEXTURE_2D);

		theme.getFontRenderer().drawString(component.getTitle(), 2, 2,
				RenderUtil.toRGBA(component.getForegroundColor()));
		glEnable(GL_CULL_FACE);
		glDisable(GL_BLEND);
		translateComponent(component, true);
	}

	@Override
	protected Rectangle getContainerChildRenderArea(Frame container) {
		Rectangle area = new Rectangle(container.getArea());
		area.x = 2;
		area.y = theme.getFontRenderer().FONT_HEIGHT + 6;
		area.width -= 4;
		area.height -= theme.getFontRenderer().FONT_HEIGHT + 8;
		return area;
	}

	@Override
	protected Dimension getDefaultComponentSize(Frame component) {
		Component[] children = component.getChildren();
		Rectangle[] areas = new Rectangle[children.length];
		Constraint[][] constraints = new Constraint[children.length][];
		for (int i = 0; i < children.length; i++) {
			Component child = children[i];
			Dimension size = child.getTheme().getUIForComponent(child).getDefaultSize(child);
			areas[i] = new Rectangle(0, 0, size.width, size.height);
			constraints[i] = component.getConstraints(child);
		}
		Dimension size = component.getLayoutManager().getOptimalPositionedSize(areas, constraints);
		size.width += 4;
		size.height += theme.getFontRenderer().FONT_HEIGHT + 8;
		return size;
	}

	@Override
	protected Rectangle[] getInteractableComponentRegions(Frame component) {
		return new Rectangle[] { new Rectangle(0, 0, component.getWidth(), theme.getFontRenderer().FONT_HEIGHT + 4) };
	}

	@Override
	protected void handleComponentInteraction(Frame component, Point location, int button) {
		if (button != 0)
			return;
		int offset = component.getWidth() - 2;
		int textHeight = theme.getFontRenderer().FONT_HEIGHT;
		if (component.isClosable()) {
			if (location.x >= offset - textHeight && location.x <= offset && location.y >= 2
					&& location.y <= textHeight + 2) {
				component.close();
				return;
			}
			offset -= textHeight + 2;
		}
		if (component.isPinnable()) {
			if (location.x >= offset - textHeight && location.x <= offset && location.y >= 2
					&& location.y <= textHeight + 2) {
				component.setPinned(!component.isPinned());
				return;
			}
			offset -= textHeight + 2;
		}
		if (component.isMinimizable()) {
			if (location.x >= offset - textHeight && location.x <= offset && location.y >= 2
					&& location.y <= textHeight + 2) {
				component.setMinimized(!component.isMinimized());
				return;
			}
			offset -= textHeight + 2;
		}
		if (location.x >= 0 && location.x <= offset && location.y >= 0 && location.y <= textHeight + 4) {
			component.setDragging(true);
			return;
		}
	}
}
