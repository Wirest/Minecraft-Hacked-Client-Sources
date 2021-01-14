package de.Hero.clickgui.elements.menu;

import java.awt.Color;

import net.minecraft.client.gui.Gui;
import de.Hero.clickgui.elements.Element;
import de.Hero.clickgui.elements.ModuleButton;
import de.Hero.clickgui.util.ColorUtil;
import de.Hero.clickgui.util.FontUtil;
import de.Hero.settings.Setting;

/**
 *  Made by HeroCode
 *  it's free to use
 *  but you have to credit me
 *
 *  @author HeroCode
 */
public class ElementCheckBox extends Element {
	/*
	 * Konstrukor
	 */
	public ElementCheckBox(ModuleButton iparent, Setting iset) {
		parent = iparent;
		set = iset;
		super.setup();
	}

	/*
	 * Rendern des Elements 
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		Color temp = ColorUtil.getClickGUIColor();
		int color = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 200).getRGB();
		
		/*
		 * Die Box und Umrandung rendern
		 */
		Gui.drawRect(x, y, x + width, y + height, 0xff1a1a1a);

		/*
		 * Titel und Checkbox rendern.
		 */
		FontUtil.drawString(setstrg, x + width - FontUtil.getStringWidth(setstrg), y + FontUtil.getFontHeight() / 2 - 0.5, 0xffffffff);
		Gui.drawRect(x + 1, y + 2, x + 12, y + 13, set.getValBoolean() ? color : 0xff000000);
		if (isCheckHovered(mouseX, mouseY))
			Gui.drawRect(x + 1, y + 2, x + 12, y + 13, 0x55111111);
	}

	/*
	 * 'true' oder 'false' bedeutet hat der Nutzer damit interagiert und
	 * sollen alle anderen Versuche der Interaktion abgebrochen werden?
	 */
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == 0 && isCheckHovered(mouseX, mouseY)) {
			set.setValBoolean(!set.getValBoolean());
			return true;
		}
		
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	/*
	 * Einfacher HoverCheck, bentigt damit die Value gendert werden kann
	 */
	public boolean isCheckHovered(int mouseX, int mouseY) {
		return mouseX >= x + 1 && mouseX <= x + 12 && mouseY >= y + 2 && mouseY <= y + 13;
	}
}
