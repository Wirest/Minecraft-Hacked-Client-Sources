package store.shadowclient.client.clickgui.clickgui.elements.menu;

import java.awt.Color;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.clickgui.elements.Element;
import store.shadowclient.client.clickgui.clickgui.elements.ModuleButton;
import store.shadowclient.client.clickgui.clickgui.util.FontUtil;
import store.shadowclient.client.clickgui.settings.Setting;
import net.minecraft.client.gui.Gui;

public class ElementCheckBox extends Element {
	/*
	 * Constructor
	 */
	public ElementCheckBox(ModuleButton iparent, Setting iset) {
		parent = iparent;
		set = iset;
		super.setup();
	}

	/*
	 * Render Elements
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		//Color temp = ColorUtil.getClickGUIColor();
		//int color = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 200).getRGB();

		/*
		 *Render the box and border
		 */
		Gui.drawRect(x, y, x + width, y + height, 0xff121212);

		/*
		 * Render titel and box
		 */
		String themecheckbox = Shadow.instance.settingsManager.getSettingByName("Theme").getValString();
		if(themecheckbox.equalsIgnoreCase("Shadow")) {
			/*FontUtil.drawString(setstrg, x + width - FontUtil.getStringWidth(setstrg), y + FontUtil.getFontHeight() / 2 - 0.5, 0xffffffff);
			Gui.drawRect(x + 1, y + 2, x + 12, y + 13, set.getValBoolean() ? rainbow(0) : 0xff000000);*/
			FontUtil.drawString(setstrg, x + width - FontUtil.getStringWidth(setstrg), y + FontUtil.getFontHeight() / 2 - 0.5, 0xffffffff);
			Gui.drawRect(x + 1, y + 2, x + 12, y + 13, set.getValBoolean() ? 0xff810081 : 0xff000000);
		}
		if(themecheckbox.equalsIgnoreCase("Cheese")) {
			FontUtil.drawString(setstrg, x + width - FontUtil.getStringWidth(setstrg), y + FontUtil.getFontHeight() / 2 - 0.5, 0xffffffff);
			Gui.drawRect(x + 1, y + 2, x + 12, y + 13, set.getValBoolean() ? 0xFFFFFF00 : 0xff000000);
		}
		if (isCheckHovered(mouseX, mouseY))
			Gui.drawRect(x + 1, y + 2, x + 12, y + 13, 0x55111111);
	}

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
	
	public static int rainbow(int delay) {
	      double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
	      rainbowState %= 360;
	      return Color.getHSBColor((float) (rainbowState / 360.0f), 0.8f, 0.7f).getRGB();
	}
}
