package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.render;

import java.awt.Font;

import net.minecraft.client.gui.FontRenderer;
import skyline.specc.cfont.MinecraftFontRenderer;

public class VitalFontRenderer {

	public static VitalFontRenderer createFontRenderer(FontObjectType type, Font font){
		VitalFontRenderer fontRenderer = null;

		switch(type){
			case CFONT:
				fontRenderer = new MinecraftFontRenderer(font, true, true);
				break;		
			case NAHR:
				fontRenderer = new MinecraftFontRenderer(font, true, true);
				break;
			case DEFAULT:
				fontRenderer = new DefaultFontRenderer();
				break;
			default:
				break;
		}
		return fontRenderer;
	}

	public enum FontObjectType {

		CFONT,
		NAHR,
		DEFAULT

	}
	
    public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color)
    {
        fontRendererIn.func_175063_a(text, (float)(x - fontRendererIn.getStringWidth(text) / 2), (float)y, color);
    }
    
    public void drawCenteredString(String text, int x, int y, int color) {
    	// This took way longer then it should have.
    	this.drawString(text, (float)(x - getStringWidth(text) / 2), y, color);
    }
    
	public int drawString(String text, float x, float y, int color) {
		text = text.replaceAll("§§", "");
		return 0;
	}

	public int drawStringWithShadow(String text, float x, float y, int color) {
		text = text.replaceAll("§§", "");
		return 0;
	}

	public int getStringWidth(String text) {
		text = text.replaceAll("§§", "");
		return 0;
	}

	public int getHeight() {
		return 0;
	}

	public static Font createFontFromFile(String name, int size){
		Font f;
		try {
			f = Font.createFont(Font.TRUETYPE_FONT, new Object().getClass().getResourceAsStream("/" + name + ".ttf"));
		} catch (Exception e) {
			return null;
		}
		
		f = f.deriveFont(Font.PLAIN, size); 

		return f;
	}

}
