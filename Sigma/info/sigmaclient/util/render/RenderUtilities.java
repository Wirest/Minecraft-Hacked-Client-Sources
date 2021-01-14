
package info.sigmaclient.util.render;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import info.sigmaclient.Client;
import info.sigmaclient.management.ColorManager;
import info.sigmaclient.module.impl.hud.TabGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class RenderUtilities {
    protected final static Minecraft mc = Minecraft.getMinecraft();
	public static void drawGradientRect(double x, double y, double x2, double y2, int col1, int col2) {
		float f = (col1 >> 24 & 0xFF) / 255.0F;
		float f1 = (col1 >> 16 & 0xFF) / 255.0F;
		float f2 = (col1 >> 8 & 0xFF) / 255.0F;
		float f3 = (col1 & 0xFF) / 255.0F;

		float f4 = (col2 >> 24 & 0xFF) / 255.0F;
		float f5 = (col2 >> 16 & 0xFF) / 255.0F;
		float f6 = (col2 >> 8 & 0xFF) / 255.0F;
		float f7 = (col2 & 0xFF) / 255.0F;

		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glShadeModel(7425);

		GL11.glPushMatrix();
		GL11.glBegin(7);
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y);

		GL11.glColor4f(f5, f6, f7, f4);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glEnd();
		GL11.glPopMatrix();

		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
		GL11.glShadeModel(7424);
		GL11.glColor3f(255, 255, 255);
	}
	public static void drawUnfilledRect(float x, float y, int width, int height, int c, double lWidth) {
		GL11.glPushMatrix();
		float f = (c >> 24 & 0xFF) / 255.0F;
		float f1 = (c >> 16 & 0xFF) / 255.0F;
		float f2 = (c >> 8 & 0xFF) / 255.0F;
		float f3 = (c & 0xFF) / 255.0F;
		
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glEnable(2848);
		GL11.glBlendFunc(770, 771);
		GL11.glColor4f(f1, f2, f3, f);
		
		Gui.drawRect(x, y+height, x+lWidth, y, c);
		Gui.drawRect(x + 1, y+lWidth, x + width + 1, y, c);
		Gui.drawRect(x+width, y+height, x+width+lWidth, y + 1, c);
		Gui.drawRect(x, y+height+lWidth, x+width + 1, y+height, c);
		GL11.glDisable(2848);
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glScalef(2.0F, 2.0F, 2.0F);
		GL11.glPopMatrix();
	}


	private static void drawBorderedRect(float left, float top, float right, float bottom, int borderColor, int color) {
		Gui.drawRect(left, top, right, bottom, color);
		Gui.drawRect(left, top, left, bottom, borderColor);
		Gui.drawRect(left, top, right, top, borderColor);
		Gui.drawRect(left, bottom, right, bottom, borderColor);
		Gui.drawRect(right, top, right, bottom, borderColor);
	}

	public static void drawBorderedCircle(float x, float y, float radius, int outsideC, int insideC) {
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848); 
		GL11.glPushMatrix(); 
		float scale = 0.1F; 
		GL11.glScalef(scale, scale, scale);
		x = (int) (x * (1.0F / scale)); 
		y = (int) (y * (1.0F / scale));
		radius *= 1.0F / scale; 
		drawCircle(x, y, radius, insideC);
		drawUnfilledCircle(x, y, radius, 1.5F, outsideC); 
		GL11.glScalef(1.0F / scale, 1.0F / scale, 1.0F / scale);
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042); 
		GL11.glDisable(2848);
		}
	 private static void drawUnfilledCircle(float x, float y, float radius, float lineWidth, int color) { 
		 float alpha = (color >> 24 & 0xFF) / 255.0F;
		 float red = (color >> 16 & 0xFF) / 255.0F; 
		 float green = (color >> 8 & 0xFF) / 255.0F;
		 float blue = (color & 0xFF) / 255.0F; 
		 GL11.glColor4f(red, green, blue, alpha);
		 GL11.glLineWidth(lineWidth); 
		 GL11.glEnable(2848);
		 GL11.glBegin(2); 
		 for (int i = 0; i <= 360; i++) {
			 GL11.glVertex2d(x + Math.sin(i * 3.141526D / 180.0D) * radius, y + Math.cos(i * 3.141526D / 180.0D) * radius); 
		 }
		 GL11.glEnd();
	     GL11.glDisable(2848);
			 
	 }
	 public static void drawCircle(float x, float y, float radius, int color) {
		 float alpha = (color >> 24 & 0xFF) / 255.0F; 
		 float red = (color >> 16 & 0xFF) / 255.0F;
		 float green = (color >> 8 & 0xFF) / 255.0F;
		 float blue = (color & 0xFF) / 255.0F;
		 GL11.glColor4f(red, green, blue, alpha);
		 GL11.glBegin(9); for (int i = 0; i <= 360; i++) {
			 GL11.glVertex2d(x + Math.sin(i * 3.141526D / 180.0D) * radius, y + Math.cos(i * 3.141526D / 180.0D) * radius);
		 }
		 GL11.glEnd();
	 }
	 	
	 public static void drawLeakedPvPKeyStrokes() {

		boolean forward = mc.gameSettings.keyBindForward.getIsKeyPressed();
		boolean back = mc.gameSettings.keyBindBack.getIsKeyPressed();
		boolean left = mc.gameSettings.keyBindLeft.getIsKeyPressed();
		boolean right = mc.gameSettings.keyBindRight.getIsKeyPressed();
		boolean jump = mc.gameSettings.keyBindJump.getIsKeyPressed();
		//TODO BACK
		int width = (int)Client.fm.getFont("SFB 11").getWidth(Keyboard.getKeyName(mc.gameSettings.keyBindBack.getKeyCode()));
		RenderUtilities.drawUnfilledRect(((float)ScaledResolution.getScaledWidth()) - 63f,
				((float)ScaledResolution.getScaledHeight()) - 55,
				19, 19, 
				Colors.getColor(200, 200, 200, 190),
				1); 
		Gui.drawRect(((float)ScaledResolution.getScaledWidth()) - 62f,
				((float)ScaledResolution.getScaledHeight()) - 54,
				((float)ScaledResolution.getScaledWidth()) - 44f,
				((float)ScaledResolution.getScaledHeight()) - 36,
				back ? 	Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 45 + 164) :
					Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 45 + 64));
		Client.fm.getFont("SFB 11").drawStringWithShadow(Keyboard.getKeyName(mc.gameSettings.keyBindBack.getKeyCode()),
				ScaledResolution.getScaledWidth() - (51 + (width/1.5f)), ScaledResolution.getScaledHeight() - 48,
				back ? Colors.getColor(255, 255, 255) : Colors.getColor(200, 200, 200));
		//TODO FORWARD
		width = (int)Client.fm.getFont("SFB 11").getWidth(Keyboard.getKeyName(mc.gameSettings.keyBindForward.getKeyCode()));
		RenderUtilities.drawUnfilledRect(((float)ScaledResolution.getScaledWidth()) - 63f,
				((float)ScaledResolution.getScaledHeight()) - 78,
				19, 19, 
				Colors.getColor(200, 200, 200, 190),
				1); 
		Gui.drawRect(((float)ScaledResolution.getScaledWidth()) - 62f,
				((float)ScaledResolution.getScaledHeight()) - 77,
				((float)ScaledResolution.getScaledWidth()) - 44f,
				((float)ScaledResolution.getScaledHeight()) - 59,
				forward ? 	Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 45 + 164) :
					Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 45 + 64));
		Client.fm.getFont("SFB 11").drawStringWithShadow(Keyboard.getKeyName(mc.gameSettings.keyBindForward.getKeyCode()),
				ScaledResolution.getScaledWidth() - (51 + (width/1.4f)), ScaledResolution.getScaledHeight() - 71,
				forward ? Colors.getColor(255, 255, 255) : Colors.getColor(200, 200, 200));
		//TODO LEFT
		width = (int)Client.fm.getFont("SFB 11").getWidth(Keyboard.getKeyName(mc.gameSettings.keyBindLeft.getKeyCode()));
		RenderUtilities.drawUnfilledRect(((float)ScaledResolution.getScaledWidth()) - 86f,
				((float)ScaledResolution.getScaledHeight()) - 55,
				19, 19, 
				Colors.getColor(200, 200, 200, 190),
				1); 
		Gui.drawRect(((float)ScaledResolution.getScaledWidth()) - 85f,
				((float)ScaledResolution.getScaledHeight()) - 54,
				((float)ScaledResolution.getScaledWidth()) - 67f,
				((float)ScaledResolution.getScaledHeight()) - 36,
				left ? 	Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 45 + 164) :
					Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 45 + 64));
		Client.fm.getFont("SFB 11").drawStringWithShadow(Keyboard.getKeyName(mc.gameSettings.keyBindLeft.getKeyCode()),
				ScaledResolution.getScaledWidth() - (74 + (width/1.4f)), ScaledResolution.getScaledHeight() - 48,
				left ? Colors.getColor(255, 255, 255) : Colors.getColor(200, 200, 200));
		//TODO RIGHT
		width = (int)Client.fm.getFont("SFB 11").getWidth(Keyboard.getKeyName(mc.gameSettings.keyBindRight.getKeyCode()));
		RenderUtilities.drawUnfilledRect(((float)ScaledResolution.getScaledWidth()) - 40f,
				((float)ScaledResolution.getScaledHeight()) - 55,
				19, 19, 
				Colors.getColor(200, 200, 200, 190),
				1); 
		Gui.drawRect(((float)ScaledResolution.getScaledWidth()) - 39f,
				((float)ScaledResolution.getScaledHeight()) - 54,
				((float)ScaledResolution.getScaledWidth()) - 21f,
				((float)ScaledResolution.getScaledHeight()) - 36,
				right ? 	Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 45 + 164) :
					Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 45 + 64));
		Client.fm.getFont("SFB 11").drawStringWithShadow(Keyboard.getKeyName(mc.gameSettings.keyBindRight.getKeyCode()),
				ScaledResolution.getScaledWidth() - (28 + (width/1.4f)), ScaledResolution.getScaledHeight() - 48,
				right ? Colors.getColor(255, 255, 255) : Colors.getColor(200, 200, 200));
		//TODO Jump
		RenderUtilities.drawUnfilledRect(((float)ScaledResolution.getScaledWidth()) - 71f,
				((float)ScaledResolution.getScaledHeight()) - 31,
				35, 16, 
				Colors.getColor(200, 200, 200, 190),
				1); 
		Gui.drawRect(((float)ScaledResolution.getScaledWidth()) - 70f,
				((float)ScaledResolution.getScaledHeight()) - 30,
				((float)ScaledResolution.getScaledWidth()) - 36f,
				((float)ScaledResolution.getScaledHeight()) - 15,
				jump ?	Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 45 + 164) :
					Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 45 + 64));
		Client.fm.getFont("SFB 9").drawStringWithShadow("Space",
				ScaledResolution.getScaledWidth() - 67, ScaledResolution.getScaledHeight() - 28,
				jump ? Colors.getColor(255, 255, 255) : Colors.getColor(200, 200, 200));
	
	}

}
