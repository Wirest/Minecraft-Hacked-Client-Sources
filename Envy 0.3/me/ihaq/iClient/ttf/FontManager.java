package me.ihaq.iClient.ttf;

import java.awt.*;

public class FontManager {
	
	
	public MinecraftFontRenderer hud = null;
	public MinecraftFontRenderer arraylist = null;
	public MinecraftFontRenderer mainMenu = null;
	public MinecraftFontRenderer chat = null;
	public MinecraftFontRenderer title = null;
	public MinecraftFontRenderer tabTitle = null;

	private static String fontName = "Audiowide";

	public void loadFonts() {
		hud = new MinecraftFontRenderer(new Font(fontName, Font.PLAIN, 22), true, true);
		tabTitle = new MinecraftFontRenderer(new Font(fontName, Font.PLAIN, 30), true, true);
		arraylist = new MinecraftFontRenderer(new Font(fontName, Font.PLAIN, 18), true, true);
		mainMenu = new MinecraftFontRenderer(new Font(fontName, Font.PLAIN, 50), true, true);
		chat = new MinecraftFontRenderer(new Font("Verdana", Font.PLAIN, 17), true, true);
		title = new MinecraftFontRenderer(new Font(fontName, Font.PLAIN, 90), true, true);
	}

	public static String getFontName() {
		return fontName;
	}

	public static void setFontName(String fontName) {
		FontManager.fontName = fontName;
	}
}
