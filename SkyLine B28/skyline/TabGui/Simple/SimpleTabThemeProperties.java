package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.TabGui.Simple;

import java.awt.Color;

import net.minecraft.util.EnumChatFormatting;

public class SimpleTabThemeProperties {

	private final Color selectedColor;
	private final Color textColor; 
	private final Color panelColor;
	private final float panelOutlineWidth;
	private final SimpleTabAlignment alignment;

	public SimpleTabThemeProperties(Color selectedColor, Color textColor, Color panelColor, float panelOutlineWidth, SimpleTabAlignment alignment) {
		this.selectedColor = selectedColor;
		this.textColor = textColor;
		this.panelColor = panelColor;
		this.panelOutlineWidth = panelOutlineWidth;
		this.alignment = alignment;
	}

	public Color getSelectedColor() {
		return Color.WHITE;
	}

	public Color getTextColor() {
		return Color.WHITE;
	}

	public Color getPanelColor() {
		return (new Color(21, 179, 193, 80));
	}

	public float getPanelOutlineWidth() {
		return panelOutlineWidth;
	}

	public SimpleTabAlignment getAlignment(){
		return this.alignment;
	}
	
	public enum SimpleTabAlignment {
		
		LEFT,
		CENTER
		
	}
	
}
