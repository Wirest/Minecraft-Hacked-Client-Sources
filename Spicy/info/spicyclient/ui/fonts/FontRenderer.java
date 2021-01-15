package info.spicyclient.ui.fonts;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import info.spicyclient.SpicyClient;
import info.spicyclient.chatCommands.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class FontRenderer {
	
	public static UnicodeFont font;
	public static DecimalFormat format = new DecimalFormat("#.##");
	
	public static void drawString(float x, float y, String text) {
		
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.04, 0.04, 1);
		
		float offset = 0;
		for (char ch: text.toCharArray()) {
			font.drawString((x * 25) + offset + (font.getWidth(Character.toString(ch)) / 2), (y * 25) - (font.getHeight(text) / 2.8f), Character.toString(ch));
			offset += 144;
		}		
		
		GlStateManager.popMatrix();
		
	}
	
	public static void setupFonts() {
		
		Class cls = null;
		try {
			cls = Class.forName("info.spicyclient.ui.fonts.FontRenderer");
		} catch (ClassNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		
        //InputStream istream = cls.getResourceAsStream("/assets/minecraft/Jigsaw-Regular.otf");
		//InputStream istream = cls.getResourceAsStream("/assets/minecraft/OpenSans-Light.ttf");
		InputStream istream = cls.getResourceAsStream("/assets/minecraft/OpenSans-Semibold.ttf");
        Font myFont = null;
		try {
			myFont = Font.createFont(Font.PLAIN, istream);
		} catch (FontFormatException | IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        myFont = myFont.deriveFont(Font.PLAIN, 250);
		
		float size = 20.0F;
		UnicodeFont f = new UnicodeFont(myFont);
		f.addAsciiGlyphs();
		ColorEffect e = new ColorEffect();
		e.setColor(java.awt.Color.white);
		f.getEffects().add(e);
		try {
		    f.loadGlyphs();
		} catch (SlickException e1) {
		    e1.printStackTrace();
		}
		font = f;
		
	}
	
}
