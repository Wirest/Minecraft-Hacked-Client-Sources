package me.robbanrobbin.jigsaw.client.gui.tab;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLSync;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class TabGuiTitle extends TabGuiItem {
	
	public String title = null;
	
	public TabGuiContainer nextContainer;
	
	public boolean hasnextContainer() {
		return nextContainer != null;
	}

	@Override
	public int getWidth() {
		return TabGuiItem.fontRenderer.getStringWidth(title) + 10;
	}

	@Override
	public int getHeigth() {
		return TabGuiItem.fontRenderer.FONT_HEIGHT - 3;
	}
	
	@Override
	public void update() {
		super.update();
		if(nextContainer == null) {
			return;
		}
		nextContainer.x = this.x + this.parent.width + 2;
		nextContainer.y = this.y;
		nextContainer.update();
	}
	
	@Override
	public void setValues() {
		super.setValues();
		if(nextContainer != null) {
			nextContainer.setValues();
			return;
		}
	}
	
	@Override
	public void render() {
		super.render();
		int drawWidth = parent.width - 2;
		if(TabGui.novitex) {
			drawWidth = width - 10;
		}
		GlStateManager.enableAlpha();
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableCull();
		
		GL11.glColor4f(0.2f, 0.2f, 0.2f, 0.1f);
		if(selected) {
			GL11.glColor4f(0.7f, 0.2f, 0.2f, 1f);
		}
		if(TabGui.novitex) {
			GL11.glColor4f(0.7f, 0.2f, 0.2f, 0.5f);
			if(selected) {
				GL11.glColor4f(0.7f, 0.2f, 0.2f, 0.8f);
			}
		}
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex2d(x, y);
			GL11.glVertex2d(x, y + height);
			GL11.glVertex2d(x + drawWidth, y + height);
			GL11.glVertex2d(x + drawWidth, y);
		}
		GL11.glEnd();
		GL11.glColor4f(0f, 0f, 0f, 1f);
		GL11.glLineWidth(0.5f);
		if(!TabGui.novitex) {
			if(selected) {
				GL11.glTranslated(0, 0, 10);
				GL11.glBegin(GL11.GL_LINE_LOOP);
				{
					GL11.glVertex2d(x, y);
					GL11.glVertex2d(x, y + height);
					GL11.glVertex2d(x + drawWidth, y + height);
					GL11.glVertex2d(x + drawWidth, y);
				}
				GL11.glEnd();
				GL11.glTranslated(0, 0, -10);
			}
		}
		GlStateManager.enableBlend();
		GlStateManager.enableTexture2D();
		GL11.glColor4f(1f, 1f, 1f, 1f);
		boolean toggled = true;
		if(Jigsaw.isModuleName(title) && !Jigsaw.getModuleByName(title).isToggled()) {
			toggled = false;
		}
		if(TabGui.novitex) {
			TabGuiItem.fontRenderer.drawString(title, x + 5, y - 3, toggled ? 0xffffff : 0xbfbfbf);
		}
		else {
			TabGuiItem.fontRenderer.drawStringWithShadow(title, x + 1, y - 3, toggled ? 0xffffff : 0xbfbfbf);
		}
		
		if(maximised && hasnextContainer()) {
			nextContainer.render();
		}
		if(maximised && !hasnextContainer()) {
			Jigsaw.getModuleByName(this.title).toggle();
			maximised = false;
		}
	}
	
}
