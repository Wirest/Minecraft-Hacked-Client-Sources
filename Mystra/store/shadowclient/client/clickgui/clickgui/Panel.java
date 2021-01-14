package store.shadowclient.client.clickgui.clickgui;

import java.awt.Color;
import java.util.ArrayList;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.clickgui.elements.ModuleButton;
import store.shadowclient.client.clickgui.clickgui.util.FontUtil;
import net.minecraft.client.gui.Gui;

public class Panel {
	public String title;
	public double x;
	public double y;
	private double x2;
	private double y2;
	public double width;
	public double height;
	public boolean dragging;
	public boolean extended;
	public boolean visible;
	public ArrayList<ModuleButton> Elements = new ArrayList<>();
	public ClickGUI clickgui;

	/*
	 * Konstrukor
	 */
	public Panel(String ititle, double ix, double iy, double iwidth, double iheight, boolean iextended, ClickGUI parent) {
		this.title = ititle;
		this.x = ix;
		this.y = iy;
		this.width = iwidth;
		this.height = iheight;
		this.extended = iextended;
		this.dragging = false;
		this.visible = true;
		this.clickgui = parent;
		setup();
	}

	/*
	 * Wird in ClickGUI berschrieben, sodass auch ModuleButtons hinzugefgt werden knnen :3
	 */
	public void setup() {}

	/*
	 * Rendern des Elements.
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (!this.visible)
			return;

		if (this.dragging) {
			x = x2 + mouseX;
			y = y2 + mouseY;
		}

		//Color temp = ColorUtil.getClickGUIColor().darker();
		//int outlineColor = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 170).getRGB();

		Gui.drawRect(x, y, x + width, y + height, 0xff121212);
		String themepanel = Shadow.instance.settingsManager.getSettingByName("Theme").getValString();
		if(Shadow.instance.settingsManager.getSettingByName("Design").getValString().equalsIgnoreCase("Shadow")){
			if(themepanel.equalsIgnoreCase("Shadow")) {
				Gui.drawRect(x, y + 1, x + 80, y - 1, rainbow(0)); // UPPER LINE
				Gui.drawRect(x, y + 13, x + 80, y + height, rainbow(0)); // UNDER LINE
				Gui.drawRect(x + 2, y, x, y + height, rainbow(0)); //LEFT LINE
				Gui.drawRect(x + 78, y, x + 80, y + height, rainbow(0)); // RIGHT LINE
				Gui.drawRect(x + 14 + width / 2, y + 12 - 2.3, x - 12 + width, y - 7 + height - 3, rainbow(counter));
				FontUtil.drawStringWithShadow(title, x + 4, y + height / 2 - FontUtil.getFontHeight()/2, 0xffefefef);
			}
			
			if(themepanel.equalsIgnoreCase("Cheese")) {
				Gui.drawRect(x, y + 1, x + 80, y - 1,0xFFFFFF00); // UPPER LINE
				Gui.drawRect(x, y + 13, x + 80, y + height, 0xFFFFFF00); // UNDER LINE
				Gui.drawRect(x + 2, y, x, y + height, 0xFFFFFF00); //LEFT LINE
				Gui.drawRect(x + 78, y, x + 80, y + height, 0xFFFFFF00); // RIGHT LINE
				Gui.drawRect(x + 14 + width / 2, y + 12 - 2.3, x - 12 + width, y - 7 + height - 3, 0xFFFFFF00);
				FontUtil.drawStringWithShadow(title, x + 4, y + height / 2 - FontUtil.getFontHeight()/2, 0xffefefef);
			}
		}

		if (this.extended && !Elements.isEmpty()) {
			
			double startY = y + height;
			
			int epanelcolor = Shadow.instance.settingsManager.getSettingByName("Design").getValString().equalsIgnoreCase("Shadow") ? 0x99232323 : Shadow.instance.settingsManager.getSettingByName("Design").getValString().equalsIgnoreCase("New") ? 0x99232323 : 0; ;
			
			for (ModuleButton et : Elements) {
				Gui.drawRect(x, startY, x + width, startY + et.height + 1, epanelcolor);
				et.x = x + 2;
				et.y = startY;
				et.width = width + - 4;
				et.drawScreen(mouseX, mouseY, partialTicks);
				startY += et.height + 1;
			}
			Gui.drawRect(x, startY + 1, x + width, startY + 1, epanelcolor);
		}
	}

	/*
	 * Zum Bewegen und Extenden des Panels
	 * usw.
	 */
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (!this.visible) {
			return false;
		}
		if (mouseButton == 0 && isHovered(mouseX, mouseY)) {
			x2 = this.x - mouseX;
			y2 = this.y - mouseY;
			dragging = true;
			return true;
		} else if (mouseButton == 1 && isHovered(mouseX, mouseY)) {
			extended = !extended;
			return true;
		} else if (extended) {
			for (ModuleButton et : Elements) {
				if (et.mouseClicked(mouseX, mouseY, mouseButton)) {
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * Damit das Panel auch losgelassen werden kann
	 */
	public void mouseReleased(int mouseX, int mouseY, int state) {
		if (!this.visible) {
			return;
		}
		if (state == 0) {
			this.dragging = false;
		}
	}

	/*
	 * HoverCheck
	 */
	public boolean isHovered(int mouseX, int mouseY) {
		return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
	}

	int counter = 0;

	public static int rainbow(int delay) {
	      double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
	      rainbowState %= 360;
	      return Color.getHSBColor((float) (rainbowState / 360.0f), 0.8f, 0.7f).getRGB();
	}
}
