package me.robbanrobbin.jigsaw.gui.custom.clickgui;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.awt.Point;

import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.gui.GuiJigsawKeyBind;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.utils.GuiUtils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.Minecraft;

public class KeybindButton extends Component {
	
	private Module mod;
	private String title;
	
	public KeybindButton(Module mod) {
		this.setTitle("Keybind");
		this.setMod(mod);
		setHeight(12);
	}

	@Override
	public void update() {
		
	}

	@Override
	public void draw() {
		GuiUtils.translate(this, false);
		Point mouse = GuiUtils.calculateMouseLocation();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		glDisable(GL_CULL_FACE);
		GL11.glColor4f(0.2f, 0.2f, 0.2f, 0.8f - Jigsaw.getClickGuiManager().getAlpha());
		glBegin(GL11.GL_QUADS);
		{
			glVertex2d(0, 0);
			glVertex2d(0, getHeight());
			glVertex2d(getWidth(), getHeight());
			glVertex2d(getWidth(), 0);
		}
		glEnd();
		GL11.glColor4f(0.3f, 0.3f, 0.3f, 0.8f - Jigsaw.getClickGuiManager().getAlpha());
		glBegin(GL11.GL_LINES);
		{
			glVertex2d(0, 0);
			glVertex2d(getWidth(), 0);
		}
		glEnd();
		GL11.glColor4f(0.1f, 0.1f, 0.1f, 0.8f - Jigsaw.getClickGuiManager().getAlpha());
		glBegin(GL11.GL_LINES);
		{
			glVertex2d(0, getHeight() - 0.5);
			glVertex2d(getWidth(), getHeight() - 0.5);
		}
		glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		if(mod != null && Jigsaw.getClickGuiManager().getAlpha() <= 0.5f) {
			fontRenderer.drawString(title, (int) (getWidth() / 2 - fontRenderer.getStringWidth(title) / 2), 0, 
					0xffffffff);
		}
		GL11.glDisable(GL11.GL_BLEND);
		glEnable(GL_CULL_FACE);
		GuiUtils.translate(this, true);
	}
	
	public Module getMod() {
		return mod;
	}
	
	public void setMod(Module mod) {
		this.mod = mod;
	}
	
	@Override
	public void onClicked(double x, double y, int button) {
		super.onClicked(x, y, button);
		if(button == 0) {
			Minecraft.getMinecraft()
			.displayGuiScreen(new GuiJigsawKeyBind(mod, Minecraft.getMinecraft().currentScreen));
		}
	}

	@Override
	public double getPreferedWidth() {
		return fontRenderer.getStringWidth(title) + 8;
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
