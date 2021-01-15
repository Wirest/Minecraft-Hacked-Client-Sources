package info.spicyclient.fonts;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;

import net.minecraft.client.renderer.GlStateManager;

public class FontRenderer {
	
	public static UnicodeFont font = null;
	
	public static void setCurrentFont(UnicodeFont font) {
		
		FontRenderer.font = font;
		
	}
	
	public static void drawstring(String text, float x, float y, Color color) {
		
		GlStateManager.pushMatrix();
		GL11.glScaled(0.1, 0.1, 1);
		font.drawString(x * 10, (y - ((font.getHeight(text) / 10) / 2)) * 10, text, color);
		GL11.glScaled(10, 10, 1);
		GlStateManager.popMatrix();
		
	}
	
}
