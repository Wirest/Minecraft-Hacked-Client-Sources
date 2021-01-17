package me.ihaq.iClient.gui.tabgui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.lwjgl.opengl.GL11;

import me.ihaq.iClient.Envy;
import me.ihaq.iClient.modules.Module;
import me.ihaq.iClient.modules.Module.Category;
import me.ihaq.iClient.ttf.MinecraftFontRenderer;
import me.ihaq.iClient.utils.Colors;
import me.ihaq.iClient.utils.R2DUtils;
import net.minecraft.client.Minecraft;

public class TabGUI {
	private ArrayList<String> category = new ArrayList();
	private ArrayList<String> modez = new ArrayList();
	private final MinecraftFontRenderer fr = Envy.FONT_MANAGER.hud;
	private final MinecraftFontRenderer frTitle = Envy.FONT_MANAGER.tabTitle;
	private int selectedTab;
	private int selectedMod;
	private int selectedOption;
	public int start = 30;
	public int start1 = this.start + 14;
	public int otherstart = 17;
	public int otherstart1 = this.otherstart + 14;
	public int end;
	public static int screen = 0;
	public static long lastUpdateTime;

	public TabGUI() {
		Category[] arrayOfModCategory;
		int j = (arrayOfModCategory = Category.values()).length;
		for (int i = 0; i < j; i++) {
			Category mc = arrayOfModCategory[i];
			this.category.add(
					mc.toString().substring(0, 1) + mc.toString().substring(1, mc.toString().length()).toLowerCase());
		}
	}

	public void drawTabGui() {
		int top = 18;
		int startx = 1;
		int endX = getLongestCatWidth();
		int otherstartx = endX + 2;
		int sliderColor = -3913426;
		int backgroundColor = -1795162112;
		int toggledColor = 0x90FFFFFF;
		int normalColor = 0x908B8C82;

		this.start = (this.selectedTab * 12 + top);
		this.start1 = (this.start + 14);
		this.otherstart = (this.selectedMod * 12 + this.start);
		this.otherstart1 = (this.otherstart + 14);
		int count = 0;
		R2DUtils.drawBorderRect(startx, top, endX, top + 3 + this.category.size() * 10 + 7, backgroundColor,
				backgroundColor, 1.0F);
		R2DUtils.drawBorderRect(startx, this.start, endX, this.start1, 203, Colors.getColor(), 1.0F);
		drawTopString();
		fr.drawString("FPS: \u00A7f" + Minecraft.getDebugFPS(), 2, top + 3 + this.category.size() * 10 + 10, Colors.getColor());
		int categoryCount = 0;
		for (String s : this.category) {
			fr.drawStringWithShadow(s, 4, top + 3 + categoryCount * 12, toggledColor);
			categoryCount++;	
		}
		if (screen == 1) {
			R2DUtils.drawBorderRect(otherstartx, this.start, otherstartx + getLongestModWidth() + 4,
					getModsForCategory().size() * 12 + 2 + this.start, backgroundColor, backgroundColor, 1.0F);
			R2DUtils.drawBorderRect(otherstartx, this.otherstart, otherstartx + getLongestModWidth() + 4,
					this.otherstart1, 203, Colors.getColor(), 1.0F);
			int modCount = 0;
			for (Module mod : getModsForCategory()) {
				String name = mod.getName();
				fr.drawStringWithShadow(name, otherstartx + 2, this.start + 3 + modCount * 12,
						mod.isToggled() ? toggledColor : normalColor);
				modCount++;
			}
		}
	}

	public void drawTopString() {
		int sliderColor = -65536;
		GL11.glPushMatrix();
		GL11.glScalef(1.0F, 1.0F, 1.0F);
		frTitle.drawString("E" + "\u00A7fnvy " + Envy.Client_Version, 2, 1, Colors.getColor());
		GL11.glPopMatrix();
	}

	public void down() {
		if (screen == 0) {
			if (this.selectedTab >= this.category.size() - 1) {
				this.selectedTab = -1;
			}
			this.selectedTab += 1;
		} else if (screen == 1) {
			if (this.selectedMod >= getModsForCategory().size() - 1) {
				this.selectedMod = -1;
			}
			this.selectedMod += 1;
		}
	}

	public void up() {
		if (screen == 0) {
			if (this.selectedTab <= 0) {
				this.selectedTab = this.category.size();
			}
			this.selectedTab -= 1;
		} else if (screen == 1) {
			if (this.selectedMod <= 0) {
				this.selectedMod = getModsForCategory().size();
			}
			this.selectedMod -= 1;
		}
	}

	public void left() {
		if (screen == 1) {
			screen = 0;
		}
		if (screen == 2) {
			this.selectedOption = 0;
			screen = 1;
		}
	}

	public void right() {
		if (screen == 1) {
			enter();
		} else if (screen == 0) {
			screen = 1;
			this.selectedMod = 0;
		}
	}

	public void enter() {
		if (screen == 1) {
			getModsForCategory().get(this.selectedMod).toggle();
		}
	}

	public Module getSelectedMod() {
		return getModsForCategory().get(this.selectedMod);
	}

	private ArrayList<Module> getModsForCategory() {
		ArrayList<Module> mods = new ArrayList();
		for (Module mod : Envy.MODULE_MANAGER.getModules()) {
			if (mod.getModCategory(Category.valueOf(this.category.get(this.selectedTab).toUpperCase()))) {
				mods.add(mod);
			}
		}
		return mods;
	}

	private int getLongestModWidth() {
		int longest = 0;
		for (Module mod : getModsForCategory()) {
			if (fr.getStringWidth(mod.getName()) > longest) {
				longest = fr.getStringWidth(" " + mod.getName());
			}
		}
		return longest;
	}
	
	private int getLongestCatWidth() {
		int longest = 0;
		for (Category cat : Category.values()) {
			if (fr.getStringWidth(cat.name()) > longest) {
				longest = fr.getStringWidth(" " + cat.name());
			}
		}
		return longest;
	}
	
	

}