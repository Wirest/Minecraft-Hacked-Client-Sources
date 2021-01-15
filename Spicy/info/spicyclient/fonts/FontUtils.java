package info.spicyclient.fonts;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class FontUtils {
	
	public static Font loadFontFile(String fileLocation) {
		
		Class cls = null;
		try {
			cls = Class.forName("info.spicyclient.fonts.FontUtils");
		} catch (ClassNotFoundException e3) {
			e3.printStackTrace();
			System.err.println("Unable to load font - File location: " + fileLocation);
			return null;
		}
		
		InputStream fontIStream = cls.getResourceAsStream(fileLocation);
        Font font = null;
		try {
			font = Font.createFont(Font.PLAIN, fontIStream);
		} catch (FontFormatException | IOException e2) {
			e2.printStackTrace();
			System.err.println("Unable to load font - File location: " + fileLocation);
			return null;
		}
		
		return font;
		
	}
	
	public static UnicodeFont loadUnicodeFont(Font font, int style, int size) {
		
        font = font.deriveFont(style, size);
        
		UnicodeFont unicodeFont = new UnicodeFont(font);
		unicodeFont.addAsciiGlyphs();
		ColorEffect e = new ColorEffect();
		e.setColor(java.awt.Color.white);
		unicodeFont.getEffects().add(e);
		try {
			unicodeFont.loadGlyphs();
		} catch (SlickException e1) {
		    e1.printStackTrace();
		    System.err.println("Unable to convert font to unicode font - Font: " + font + " - Style: " + style + " - Size: " + size);
		}
		
		return unicodeFont;
		
	}
	
}
