package store.shadowclient.client.clickgui.clickgui.elements.menu;

import java.awt.Color;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.clickgui.elements.Element;
import store.shadowclient.client.clickgui.clickgui.elements.ModuleButton;
import store.shadowclient.client.clickgui.clickgui.util.FontUtil;
import store.shadowclient.client.clickgui.settings.Setting;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;

public class ElementSlider extends Element {
	public boolean dragging;

	/*
	 * Konstrukor
	 */
	public ElementSlider(ModuleButton iparent, Setting iset) {
		parent = iparent;
		set = iset;
		dragging = false;
		super.setup();
	}

	/*
	 * Rendern des Elements
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		String themeslider = Shadow.instance.settingsManager.getSettingByName("Theme").getValString();
		String displayval = "" + Math.round(set.getValDouble() * 100D)/ 100D;
		boolean hoveredORdragged = isSliderHovered(mouseX, mouseY) || dragging;

		//Color temp = ColorUtil.getClickGUIColor();
		//int color = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), hoveredORdragged ? 250 : 200).getRGB();
		//int color2 = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), hoveredORdragged ? 255 : 230).getRGB();
		
		//selected = iset.getValDouble() / iset.getMax();
		double percentBar = (set.getValDouble() - set.getMin())/(set.getMax() - set.getMin());

		/*
		 * Slider Box
		 */
		Gui.drawRect(x, y, x + width, y + height, 0xff121212);

		/*
		 * Text
		 */
		FontUtil.drawString(setstrg, x + 1, y + 2, 0xffffffff);
		FontUtil.drawString(displayval, x + width - FontUtil.getStringWidth(displayval), y + 2, 0xffffffff);

		/*
		 * Slider
		 */
		if(themeslider.equalsIgnoreCase("Shadow")) {
			Gui.drawRect(x, y + 12, x + width, y + 13.5, 0xffc192e8);
		}
		if(themeslider.equalsIgnoreCase("Cheese")) {
			Gui.drawRect(x, y + 12, x + width, y + 13.5, -1);
		}
		
		if(themeslider.equalsIgnoreCase("Shadow")) {
			//Gui.drawRect(x, y + 12, x + (percentBar * width), y + 13.5, rainbow(0));
			Gui.drawRect(x, y + 12, x + (percentBar * width), y + 13.5, 0xff810081);
		}
		
		if(themeslider.equalsIgnoreCase("Cheese")) {
			Gui.drawRect(x, y + 12, x + (percentBar * width), y + 13.5, 0xFFFFFF00);
		}

		if(percentBar > 0 && percentBar < 1)
			if(themeslider.equalsIgnoreCase("Shadow")) {
				//Gui.drawRect(x + (percentBar Shadow width)-1, y + 12, x + Math.min((percentBar * width), width), y + 13.5, rainbow(0));
				Gui.drawRect(x + (percentBar * width)+3, y + 14, x + Math.min((percentBar * width), width), y + 11.5, 0xff810081);
			}
			if(themeslider.equalsIgnoreCase("Cheese")) {
				Gui.drawRect(x + (percentBar * width)-1, y + 12, x + Math.min((percentBar * width), width), y + 13.5, 0xFFFFFF00);
			}

		/*
		 * Neue Value berechnen, wenn dragging
		 */
		if (this.dragging) {
			double diff = set.getMax() - set.getMin();
			double val = set.getMin() + (MathHelper.clamp_double((mouseX - x) / width, 0, 1)) * diff;
			set.setValDouble(val); //Die Value im Setting updaten
		}
	}

	/*
	 * 'true' oder 'false' bedeutet hat der Nutzer damit interagiert und
	 * sollen alle anderen Versuche der Interaktion abgebrochen werden?
	 */
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == 0 && isSliderHovered(mouseX, mouseY)) {
			this.dragging = true;
			return true;
		}

		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	/*
	 * Wenn die Maus losgelassen wird soll aufgehrt werden die Slidervalue zu verndern
	 */
	public void mouseReleased(int mouseX, int mouseY, int state) {
		this.dragging = false;
	}

	/*
	 * Einfacher HoverCheck, bentigt damit dragging auf true gesetzt werden kann
	 */
	public boolean isSliderHovered(int mouseX, int mouseY) {
		return mouseX >= x && mouseX <= x + width && mouseY >= y + 11 && mouseY <= y + 14;
	}
	
	public static int rainbow(int delay) {
	      double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
	      rainbowState %= 360;
	      return Color.getHSBColor((float) (rainbowState / 360.0f), 0.8f, 0.7f).getRGB();
	}
}