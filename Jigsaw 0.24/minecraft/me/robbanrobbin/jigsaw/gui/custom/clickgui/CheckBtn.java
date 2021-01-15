package me.robbanrobbin.jigsaw.gui.custom.clickgui;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.awt.Point;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.utils.GuiUtils;
import me.robbanrobbin.jigsaw.module.Module;

public abstract class CheckBtn extends Component {
	
	private String title;
	private boolean toggled = false;

	@Override
	public void update() {
//		if (mod.getKeyboardKey() == Keyboard.KEY_NONE) {
//			this.setTitle(mod.getName());
//		} else {
//			this.setTitle(mod.getName() + "[" + Keyboard.getKeyName(mod.getKeyboardKey()) + "]");
//		}
	}

	@Override
	public void draw() {
		GuiUtils.translate(this, false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		glDisable(GL_CULL_FACE);
		GL11.glColor4f(0.2f, 0.2f, 0.2f, 0.8f - Jigsaw.getClickGuiManager().getAlpha());
		if(isToggled()) {
			GL11.glColor4f(0.7f, 0.25f, 0.25f, 0.8f - Jigsaw.getClickGuiManager().getAlpha());
		}
		glBegin(GL11.GL_QUADS);
		{
			glVertex2d(0, 1);
			glVertex2d(0, getHeight() - 1);
			glVertex2d(0 + 10, getHeight() - 1);
			glVertex2d(0 + 10, 1);
		}
		glEnd();
		GL11.glColor4f(0.3f, 0.3f, 0.3f, 0.8f - Jigsaw.getClickGuiManager().getAlpha());
		glBegin(GL11.GL_LINES);
		{
			glVertex2d(0, getHeight() - 1.5);
			glVertex2d(10, getHeight() - 1.5);
		}
		glEnd();
		GL11.glColor4f(0.1f, 0.1f, 0.1f, 0.8f - Jigsaw.getClickGuiManager().getAlpha());
		glBegin(GL11.GL_LINES);
		{
			glVertex2d(0, 1);
			glVertex2d(10, 1);
		}
		glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		if(Jigsaw.getClickGuiManager().getAlpha() <= 0.5f) {
			fontRenderer.drawStringWithShadow(title, 11, 1, 0xffffffff);
		}
		GL11.glDisable(GL11.GL_BLEND);
		glEnable(GL_CULL_FACE);
		GuiUtils.translate(this, true);
	}
	
	public void setToggled(boolean toggled) {
		this.toggled = toggled;
	}
	
	public boolean isToggled() {
		return toggled;
	}

	@Override
	public double getPreferedWidth() {
		return fontRenderer.getStringWidth(title) + 12;
	}

	@Override
	public double getPreferedHeight() {
		return fontRenderer.FONT_HEIGHT + 2;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
}
