package com.mentalfrostbyte.jello.util;

import java.awt.Font;
import java.io.InputStream;

import com.mentalfrostbyte.jello.font.JelloFontRenderer;
import com.mentalfrostbyte.jello.jelloclickgui.JelloGui;
import com.mentalfrostbyte.jello.main.Jello;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

public class FontUtil {
	
	public static JelloFontRenderer getTextFieldFont(boolean password){
		return password ? jelloFontGui : mc.currentScreen instanceof JelloGui ? jelloFontGui : jelloFontAddAlt;
	}

	private static Font getJelloFont(float size, boolean bold) {
		Font font = null;
		try {
			InputStream is = Minecraft.getMinecraft().getResourceManager()
					.getResource(new ResourceLocation(bold ? "Jello/jellomedium.ttf": "Jello/jellolight.ttf")).getInputStream();
			font = Font.createFont(0, is);
			font = font.deriveFont(0, size);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error loading font");
			font = new Font("default", 0, +10);
		}
		return font;
	}
	
	private static Font getJelloFontRegular(int size) {
		Font font = null;
		try {
			InputStream is = Minecraft.getMinecraft().getResourceManager()
					.getResource(new ResourceLocation("Jello/jelloregular.ttf")).getInputStream();
			font = Font.createFont(0, is);
			font = font.deriveFont(0, size);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error loading font");
			font = new Font("default", 0, +10);
		}
		return font;
	}
	
	private static Font getJelloFontUltralight(int size) {
		Font font = null;
		try {
			InputStream is = Minecraft.getMinecraft().getResourceManager()
					.getResource(new ResourceLocation("Jello/jelloultralight.ttf")).getInputStream();
			font = Font.createFont(0, is);
			font = font.deriveFont(0, size);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error loading font");
			font = new Font("default", 0, +10);
		}
		return font;
	}
	
	public static Minecraft mc = Jello.core.mc;
	public static FontRenderer fr = mc.fontRendererObj;
	public static JelloFontRenderer jelloFont = JelloFontRenderer.createFontRenderer(getJelloFont((int)(20), false));
	public static JelloFontRenderer jelloFontScale = JelloFontRenderer.createFontRenderer(getJelloFont((int)((20*1.2)), false));
	public static JelloFontRenderer jelloFontAddAlt = JelloFontRenderer.createFontRenderer(getJelloFont((int)(24), false));
	public static JelloFontRenderer jelloFontGui = JelloFontRenderer.createFontRenderer(getJelloFont((int)(25), false));
	public static JelloFontRenderer jelloFontDuration = JelloFontRenderer.createFontRenderer(getJelloFont((int)(13), false));
	public static JelloFontRenderer jelloFontMusic = JelloFontRenderer.createFontRenderer(getJelloFont((float) (12f), false));
	public static JelloFontRenderer jelloFontAddAlt2 = JelloFontRenderer.createFontRenderer(getJelloFont((int)(35), false));
	public static JelloFontRenderer jelloFontAddAlt3 = JelloFontRenderer.createFontRenderer(getJelloFont((int)(19), false));
	public static JelloFontRenderer jelloFontRegular = JelloFontRenderer.createFontRenderer(getJelloFontRegular((int)(20)));
	public static JelloFontRenderer jelloFontRegularBig = JelloFontRenderer.createFontRenderer(getJelloFontRegular((int)(24)));
	public static JelloFontRenderer jelloFontBoldSmall = JelloFontRenderer.createFontRenderer(getJelloFont((int)(19), true));
	public static JelloFontRenderer jelloFontMarker = JelloFontRenderer.createFontRenderer(getJelloFont((int)(19), false));
	public static JelloFontRenderer jelloFontSmall = JelloFontRenderer.createFontRenderer(getJelloFont((int)(14), false));
	public static JelloFontRenderer jelloFontSmallPassword = JelloFontRenderer.createFontRenderer(getJelloFont((int)(16), false));
	public static JelloFontRenderer jelloFontBig = JelloFontRenderer.createFontRenderer(getJelloFont((int)(41), true));
	public static JelloFontRenderer jelloFontMedium = JelloFontRenderer.createFontRenderer(getJelloFont((int)(25), false));
	public static JelloFontRenderer font = JelloFontRenderer.createFontRenderer(getJelloFontRegular((int)(18)));
	public static JelloFontRenderer fontBig = JelloFontRenderer.createFontRenderer(getJelloFontRegular((int)(33)));
	public static JelloFontRenderer fontSmall = JelloFontRenderer.createFontRenderer(getJelloFontRegular((int)(14)));
	
}
