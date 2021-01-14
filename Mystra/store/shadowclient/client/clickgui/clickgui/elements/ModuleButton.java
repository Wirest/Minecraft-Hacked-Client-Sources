package store.shadowclient.client.clickgui.clickgui.elements;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.clickgui.Panel;
import store.shadowclient.client.clickgui.clickgui.elements.menu.ElementCheckBox;
import store.shadowclient.client.clickgui.clickgui.elements.menu.ElementComboBox;
import store.shadowclient.client.clickgui.clickgui.elements.menu.ElementSlider;
import store.shadowclient.client.clickgui.clickgui.util.FontUtil;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class ModuleButton {
	public Module mod;
	public ArrayList<Element> menuelements;
	public Panel parent;
	public double x;
	public double y;
	public double width;
	public double height;
	public boolean extended = false;
	public boolean listening = false;

	/*
	 * Konstrukor
	 */
	public ModuleButton(Module imod, Panel pl) {
		mod = imod;
		height = Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 2;
		parent = pl;
		menuelements = new ArrayList<>();
		/*
		 * Settings wurden zuvor in eine ArrayList eingetragen
		 * dieses SettingSystem hat 3 Konstruktoren je nach
		 *  verwendetem Konstruktor ndert sich die Value
		 *  bei .isCheck() usw. so kann man ganz einfach ohne
		 *  irgendeinen Aufwand bestimmen welches Element
		 *  fr ein Setting bentigt wird :>
		 */
		if (Shadow.instance.settingsManager.getSettingsByMod(imod) != null)
			for (Setting s : Shadow.instance.settingsManager.getSettingsByMod(imod)) {
				if (s.isCheck()) {
					menuelements.add(new ElementCheckBox(this, s));
				} else if (s.isSlider()) {
					menuelements.add(new ElementSlider(this, s));
				} else if (s.isCombo()) {
					menuelements.add(new ElementComboBox(this, s));
				}
			}
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		//Color temp = ColorUtil.getClickGUIColor();
		//int color = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 150).getRGB();
		String thememodulebutton = Shadow.instance.settingsManager.getSettingByName("Theme").getValString();
		int textcolor = 0xffafafaf;
		if (mod.isToggled()) {
			if(thememodulebutton.equalsIgnoreCase("Shadow")) {
				Gui.drawRect(x - 2, y, x + width + 2, y + height + 1, 0xf00000ff);
				textcolor = -1;
			}
			
			if(thememodulebutton.equalsIgnoreCase("Cheese")) {
				Gui.drawRect(x - 2, y, x + width + 2, y + height + 1, 0xFFFFFF00);
				textcolor = -1;
			}
		}

		if (isHovered(mouseX, mouseY)) {
			Gui.drawRect(x - 2, y, x + width + 2, y + height + 1, 0xff121212);
		}

		FontUtil.drawTotalCenteredStringWithShadow(mod.getName(), x + width / 2, y + 1 + height / 2, textcolor);
	}

	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (!isHovered(mouseX, mouseY))
			return false;

		if (mouseButton == 0) {
			mod.toggle();

			if(Shadow.instance.settingsManager.getSettingByName("Sound").getValBoolean())
			Minecraft.getMinecraft().thePlayer.playSound("random.click", 0.5f, 0.5f);
		} else if (mouseButton == 1) {

			if (menuelements != null && menuelements.size() > 0) {
				boolean b = !this.extended;
				Shadow.instance.clickGui.closeAllSettings();
				this.extended = b;

				if(Shadow.instance.settingsManager.getSettingByName("Sound").getValBoolean())
				if(extended)Minecraft.getMinecraft().thePlayer.playSound("tile.piston.out", 1f, 1f);else Minecraft.getMinecraft().thePlayer.playSound("tile.piston.in", 1f, 1f);
			}
		} else if (mouseButton == 2) {

			listening = true;
		}
		return true;
	}

	public boolean keyTyped(char typedChar, int keyCode) throws IOException {

		if (listening) {
			if (keyCode != Keyboard.KEY_ESCAPE) {
				//Client.sendChatMessage("Bound '" + mod.getName() + "'" + " to '" + Keyboard.getKeyName(keyCode) + "'");
				mod.setKey(keyCode);
			} else {
				//Client.sendChatMessage("Unbound '" + mod.getName() + "'");
				mod.setKey(Keyboard.KEY_NONE);
			}
			listening = false;
			return true;
		}
		return false;
	}

	public boolean isHovered(int mouseX, int mouseY) {
		return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
	}
}
