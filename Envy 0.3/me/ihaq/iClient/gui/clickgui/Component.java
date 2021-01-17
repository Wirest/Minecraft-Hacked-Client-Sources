package me.ihaq.iClient.gui.clickgui;

import java.io.IOException;

import net.minecraft.client.gui.GuiScreen;

public abstract class Component {
	
	protected int x, y;

	protected int absx, absy;

	protected int width, height;

	protected int renderWidth, renderHeight;

	protected Component parent;

	protected boolean isVisible;

	public abstract void draw(int mouseX, int mouseY, float partialTicks, int parX, int parY, GuiScreen screen);

	public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);

	public abstract void mouseReleased(int mouseX, int mouseY, int mouseButton);

	public abstract void keyTyped(char typedChar, int keyCode) throws IOException;
	
}
