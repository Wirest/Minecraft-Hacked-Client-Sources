package org.darkstorm.minecraft.gui.theme.simple;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import org.darkstorm.minecraft.gui.component.Button;
import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.component.basic.BasicButton;
import org.darkstorm.minecraft.gui.theme.AbstractComponentUI;
import org.darkstorm.minecraft.gui.util.RenderUtil;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.Minecraft;

public class SimpleButtonUI extends AbstractComponentUI<Button> {
	private final SimpleTheme theme;

	SimpleButtonUI(SimpleTheme theme) {
		super(Button.class);
		this.theme = theme;

		foreground = Color.WHITE;
		background = new Color(128, 128, 128, 128 + 128 / 2);
	}

	@Override
	protected void renderComponent(Button button) {
		boolean searched = true;
		boolean searching = Jigsaw.getSearchBar() != null && !Jigsaw.getSearch().isEmpty();
		if (searching) {
			searched = button.getText().toUpperCase().indexOf(Jigsaw.getSearch().toUpperCase()) != -1;
		}
		translateComponent(button, false);
		Rectangle area = button.getArea();

		glEnable(GL_BLEND);
		glDisable(GL_CULL_FACE);

		glDisable(GL_TEXTURE_2D);

		RenderUtil.setColor(searching ? (searched ? new Color(50, 200, 50, 200) : button.getBackgroundColor())
				: button.getBackgroundColor());
//		if(button instanceof BasicButton) {
//			BasicButton bscButton = (BasicButton)button;
//			if(!Jigsaw.isModAllowed(bscButton.getModule())) {
//				RenderUtil.setColor(searching ? (searched ? new Color(50, 200, 50, 200) : new Color(100, 50, 50, 200))
//						: new Color(100, 50, 50, 200));
//			}
//		}
		
		glBegin(GL_QUADS);
		{
			glVertex2d(0 + 0, 0 + 0);
			glVertex2d(area.width - 0, 0 + 0);
			glVertex2d(area.width - 0, area.height - 0);
			glVertex2d(0 + 0, area.height - 0);
		}
		glEnd();
		Point mouse = RenderUtil.calculateMouseLocation();
		mouse.translate(0, 1);
		Component parent = button.getParent();
		while (parent != null) {
			mouse.x -= parent.getX();
			mouse.y -= parent.getY();
			parent = parent.getParent();
		}
		glEnable(GL_TEXTURE_2D);
		if (button instanceof BasicButton) {
			BasicButton basicBtn = (BasicButton) button;
			Module module = basicBtn.getModule();
			if (module != null) {
				if ((module.getModSettings() != null || module.getModes().length > 1) && !module.settingsDisplayed) {
					int x = basicBtn.getWidth();
					if (module.isToggled()) {
						glColor4f(1f, 0.4f, 0.4f, 1f);
					} else {
						glColor4f(1f, 0.3f, 0.3f, 1f);
					}
					glDisable(GL11.GL_BLEND);
					glDisable(GL_TEXTURE_2D);
					GL11.glLineWidth(3);
					glBegin(GL11.GL_LINES);
					{
						int i = 0;
						glVertex2d(x, i);
						glVertex2d(x, i + basicBtn.getHeight());
					}
					glEnd();
					glEnable(GL_TEXTURE_2D);
					glEnable(GL11.GL_BLEND);

				}
				glEnable(GL11.GL_BLEND);
				glDisable(GL_TEXTURE_2D);
				GL11.glLineWidth(1);
				glColor4f(1f, 0.4f, 0.4f, 0.2f);
				glBegin(GL11.GL_LINES);
				{
					glVertex2d(0, button.getHeight() - 0.5);
					glVertex2d(button.getWidth(), button.getHeight() - 0.5);
				}
				glEnd();
				glColor4f(1f, 0.4f, 0.4f, 0.15f);
				if (module.isToggled()) {
					glBegin(GL_QUADS);
					{
						glVertex2d(0 + 0, 0 + 0);
						glVertex2d(area.width - 0, 0 + 0);
						glVertex2d(area.width - 0, area.height - 0);
						glVertex2d(0 + 0, area.height - 0);
					}
					glEnd();
				}
				glEnable(GL_TEXTURE_2D);
			}
			if (area.contains(mouse)) {
				glDisable(GL_TEXTURE_2D);
				// TODO Jigsaw tooltips
				if (basicBtn.getModule() != null && basicBtn.getModule().description != "") {
					glColor4f(0.5f, 0.2f, 0.2f, 0.9f);
					GL11.glTranslated(0, 0, 1);
					GL11.glEnable(GL11.GL_DEPTH_TEST);
					int mousey = mouse.y - basicBtn.getY() - 10;
					int mousex = mouse.x;
					int realMouseX = RenderUtil.calculateMouseLocation().x;
					int testWidth = (theme.getFontRenderer().getStringWidth(basicBtn.getModule().description) + 28);
					if(realMouseX > Minecraft.getMinecraft().displayWidth / 2 - testWidth) {
						mousex = mousex - realMouseX + (Minecraft.getMinecraft().displayWidth / 2) - testWidth;
					}
					glBegin(GL_QUADS);
					{
						int width = mousex + 3
								+ theme.getFontRenderer().getStringWidth(basicBtn.getModule().description) + 2;
						int height = mousey + theme.getFontRenderer().FONT_HEIGHT;
						glVertex2d(mousex + 20, mousey - 1);
						glVertex2d(width + 20, mousey - 1);
						glVertex2d(width + 20, height);
						glVertex2d(mousex + 20, height);
					}
					glEnd();
					glColor4f(0, 0, 0, 1);
					GL11.glLineWidth(2);
					glBegin(GL11.GL_LINE_LOOP);
					{
						int width = mousex + 3
								+ theme.getFontRenderer().getStringWidth(basicBtn.getModule().description) + 2;
						int height = mousey + theme.getFontRenderer().FONT_HEIGHT;
						glVertex2d(mousex + 20, mousey - 1);
						glVertex2d(width + 20, mousey - 1);
						glVertex2d(width + 20, height);
						glVertex2d(mousex + 20, height);
					}
					glEnd();
					theme.getFontRenderer().drawStringWithShadow(basicBtn.getModule().description, mousex + 22, mousey - 1,
							RenderUtil.toRGBA(Color.WHITE));
					GL11.glTranslated(0, 0, -1);
				}
				glEnable(GL_TEXTURE_2D);
			}
		}
		if (area.contains(mouse)) {
			glDisable(GL_TEXTURE_2D);
			glColor4f(0.0f, 0.0f, 0.0f, (Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) ? 0.5f : 0.3f);
			glBegin(GL_QUADS);
			{
				glVertex2d(0, 0);
				glVertex2d(area.width, 0);
				glVertex2d(area.width, area.height);
				glVertex2d(0, area.height);
			}
			glEnd();
			glDisable(GL11.GL_BLEND);
			glDisable(GL_TEXTURE_2D);
			glColor4f(1f, 0.4f, 0.4f, 1f);
			GL11.glLineWidth(1);
			glBegin(GL11.GL_LINES);
			{
				glVertex2d(0, button.getHeight() - 0.5);
				glVertex2d(button.getWidth(), button.getHeight() - 0.5);
			}
			glEnd();
			glBegin(GL11.GL_LINES);
			{
				glVertex2d(0, 0 - 0.5);
				glVertex2d(button.getWidth(), 0 - 0.5);
			}
			glEnd();
			glEnable(GL_TEXTURE_2D);
			glEnable(GL11.GL_BLEND);
		}
		String text = button.getText();
		boolean allowed = true;
//		if(button instanceof BasicButton) {
//			BasicButton bscButton = (BasicButton)button;
//			allowed = Jigsaw.isModAllowed(bscButton.getModule());
//		}
		if(allowed) {
			if(button.getText().equals("Change Keybind")) {
				theme.getFontRenderer().drawString(text, area.width / 2 - theme.getFontRenderer().getStringWidth(text) / 2,
						area.height / 2 - theme.getFontRenderer().FONT_HEIGHT / 2, RenderUtil.toRGBA(searching
								? (searched ? button.getForegroundColor() : Color.GRAY) : button.getForegroundColor()));
			}
			else {
				theme.getFontRenderer().drawStringWithShadow(text, area.width / 2 - theme.getFontRenderer().getStringWidth(text) / 2,
						area.height / 2 - theme.getFontRenderer().FONT_HEIGHT / 2, RenderUtil.toRGBA(searching
								? (searched ? button.getForegroundColor() : Color.GRAY) : button.getForegroundColor()));
			}
		}
		else {
			if(button.getText().equals("Change Keybind")) {
				theme.getFontRenderer().drawString(text, area.width / 2 - theme.getFontRenderer().getStringWidth(text) / 2,
						area.height / 2 - theme.getFontRenderer().FONT_HEIGHT / 2, 0xffdd6060);
			}
			else {
				theme.getFontRenderer().drawStringWithShadow(text, area.width / 2 - theme.getFontRenderer().getStringWidth(text) / 2,
						area.height / 2 - theme.getFontRenderer().FONT_HEIGHT / 2, 0xffdda0a0);
			}
		}
		
		
		glEnable(GL_CULL_FACE);
		glDisable(GL_BLEND);
		translateComponent(button, true);
	}

	@Override
	protected Dimension getDefaultComponentSize(Button component) {
		return new Dimension(theme.getFontRenderer().getStringWidth(component.getText()) + 4,
				theme.getFontRenderer().FONT_HEIGHT + 1);
	}

	@Override
	protected Rectangle[] getInteractableComponentRegions(Button component) {
		return new Rectangle[] { new Rectangle(0, 0, component.getWidth(), component.getHeight()) };
	}

	@Override
	protected void handleComponentInteraction(Button component, Point location, int button) {
		if (location.x <= component.getWidth() && location.y <= component.getHeight() && button == 0) {
			component.press();
		}
		if (location.x <= component.getWidth() && location.y <= component.getHeight() && button == 1) {
			component.rightPress();
		}

	}
}