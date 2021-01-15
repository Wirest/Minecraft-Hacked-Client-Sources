package info.spicyclient.fonts;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class FontManager {
	
	public static FontManager fontManager = null;
	
	private Font[] fonts = new Font[] {null};
	private UnicodeFont[] unicodeFonts = new UnicodeFont[] {null};
	
	private HashMap<String, Font> findFonts = new HashMap<String, Font>();
	private HashMap<String, UnicodeFont> findUnicodeFonts = new HashMap<String, UnicodeFont>();
	
	public FontManager() {
		
		if (!setupFonts()) {
			
			for (int i = 0; i < 10; i++) {
				
				System.err.println("One or more of the fonts failed to load, this may cause a crash!!!");
				
			}
			
		}else {
			System.out.println("Fonts successfully loaded");
		}
		
	}
	
	private boolean setupFonts() {
		
        fonts[0] = FontUtils.loadFontFile("/assets/minecraft/OpenSans-Semibold.ttf");
        unicodeFonts[0] = FontUtils.loadUnicodeFont(fonts[0], Font.PLAIN, 100);
        
        findFonts.put("opensans", fonts[0]);
        findUnicodeFonts.put("opensans", unicodeFonts[0]);
        
        // Makes sure none of the fonts are null
        for (Font f : fonts) {
        	
        	if (f == null) {
        		return false;
        	}
        	
        }
        
        // Makes sure none of the unicode fonts are null
        for (UnicodeFont uniF : unicodeFonts) {
        	
        	if (uniF == null) {
        		return false;
        	}
        	
        }
        
        return true;
	}
	
	public UnicodeFont getUniFont(String name) {
		return findUnicodeFonts.get(name);
	}
	
	public Font getFont(String name) {
		return findFonts.get(name);
	}
	
	public static FontManager getFontManager() {
		
		if (fontManager == null) {
			fontManager = new FontManager();
		}
		
		return fontManager;
		
	}
	
}
