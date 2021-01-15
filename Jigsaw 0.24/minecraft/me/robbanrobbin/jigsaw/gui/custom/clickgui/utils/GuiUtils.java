package me.robbanrobbin.jigsaw.gui.custom.clickgui.utils;

import java.awt.Point;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.gui.custom.clickgui.Component;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.Container;
import net.minecraft.client.Minecraft;

public class GuiUtils {
	
	public static float partialTicks = 1f;
	
	public static void translate(Component comp, boolean reverse) {
		Container parent = comp.getParent();
		if(parent != null) {
			GL11.glTranslated((reverse ? -parent.getX() : parent.getX()), (reverse ? -parent.getY() : parent.getY()), 0);
			Container parent2 = comp.getParent().getParent();
			if(parent2 != null) {
				GL11.glTranslated((reverse ? -parent2.getX() : parent2.getX()), (reverse ? -parent2.getY() : parent2.getY()), 0);
			}
		}
		GL11.glTranslated((reverse ? -comp.getX() : comp.getX()), (reverse ? -comp.getY() : comp.getY()), 0);
	}
	
	public static Point calculateMouseLocation() {
		Minecraft minecraft = Minecraft.getMinecraft();
		int scale = minecraft.gameSettings.guiScale;
		if (scale == 0)
			scale = 1000;
		int scaleFactor = 0;
		while (scaleFactor < scale && minecraft.displayWidth / (scaleFactor + 1) >= 320
				&& minecraft.displayHeight / (scaleFactor + 1) >= 240)
			scaleFactor++;
		scaleFactor = 2;
		return new Point(Mouse.getX() / scaleFactor,
				minecraft.displayHeight / scaleFactor - Mouse.getY() / scaleFactor - 1);
	}
	
}
