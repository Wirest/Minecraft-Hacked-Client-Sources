/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package Blizzard.UI.GuiDrag;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import Blizzard.Category.Category;
import Blizzard.Mod.Mod;
import Blizzard.Mod.ModManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class ClickGUI extends GuiScreen {
	public static ArrayList<Mod> Combat = new ArrayList();
	public static ArrayList<Mod> Render = new ArrayList();
	public static ArrayList<Mod> Movement = new ArrayList();
	public static ArrayList<Mod> Player = new ArrayList();
	public static ArrayList<Mod> Misc = new ArrayList();
	public static String mods = "None";

	public static void drawSexyRect(double x, double y, double x1, double y1, int color, int color2) {
		Gui.drawRect((int) x, (int) y, (int) x1, (int) y1, color);
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		Gui.drawRect((int) x * 2 - 1, (int) y * 2, (int) x * 2, (int) y1 * 2 - 1, color2);
		Gui.drawRect((int) x * 2, (int) y * 2 - 1, (int) x1 * 2, (int) y * 2, color2);
		Gui.drawRect((int) x1 * 2, (int) y * 2, (int) x1 * 2 + 1, (int) y1 * 2 - 1, color2);
		Gui.drawRect((int) x * 2, (int) y1 * 2 - 1, (int) x1 * 2, (int) y1 * 2, color2);
		GL11.glScalef(2.0f, 2.0f, 2.0f);
	}

	public ClickGUI() {
		Combat.clear();
		Render.clear();
		Movement.clear();
		Player.clear();
		Misc.clear();
		for (Mod m : ModManager.activeMods) {
			if (m.getCategory() == Category.COMBAT) {
				Combat.add(m);
			}
			if (m.getCategory() == Category.RENDER) {
				Render.add(m);
			}
			if (m.getCategory() == Category.MOVEMENT) {
				Movement.add(m);
			}
			if (m.getCategory() == Category.PLAYER) {
				Player.add(m);
			}
			if (m.getCategory() == Category.MISC) {
				Misc.add(m);
			}
		}
	}

	public static void dragGUI(FontRenderer f) {
		int yCount;
		ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft(),
				Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
		int bottom = scaledResolution.getScaledHeight();
		int right = scaledResolution.getScaledWidth();
		ClickGUI.drawSexyRect(10.0, 10.0, 100.0, 23.0, -1157627904, 255);
		f.drawString("Combat", 13, 13, 16777215);
		ClickGUI.drawSexyRect(10.0, 23.0, 100.0, 36.0, -1157627904, 255);
		f.drawString("Render", 13, 26, 16777215);
		ClickGUI.drawSexyRect(10.0, 36.0, 100.0, 49.0, -1157627904, 255);
		f.drawString("Movement", 13, 39, 16777215);
		ClickGUI.drawSexyRect(10.0, 49.0, 100.0, 62.0, -1157627904, 255);
		f.drawString("Player", 13, 52, 16777215);
		ClickGUI.drawSexyRect(10.0, 62.0, 100.0, 75.0, -1157627904, 255);
		f.drawString("World", 13, 65, 16777215);
		ClickGUI.drawSexyRect(10.0, 75.0, 100.0, 88.0, -1157627904, 255);
		f.drawString("Exploits", 13, 78, 16777215);
		if (mods == "Combat") {
			yCount = 10;
			for (Mod m : Combat) {
				ClickGUI.drawSexyRect(110.0, yCount, 220.0, yCount + 13, -1157627904, 255);
				f.drawString(m.getName(), 113, yCount + 3, 16777215);
				yCount += 13;
			}
		}
		if (mods == "Player") {
			yCount = 10;
			for (Mod m : Player) {
				ClickGUI.drawSexyRect(110.0, yCount, 220.0, yCount + 13, -1157627904, 255);
				f.drawString(m.getName(), 113, yCount + 3, 16777215);
				yCount += 13;
			}
		}
		if (mods == "Misc") {
			yCount = 10;
			for (Mod m : Misc) {
				ClickGUI.drawSexyRect(110.0, yCount, 220.0, yCount + 13, -1157627904, 255);
				f.drawString(m.getName(), 113, yCount + 3, 16777215);
				yCount += 13;
			}
		}
		if (mods == "Combat") {
			yCount = 10;
			for (Mod m : Combat) {
				ClickGUI.drawSexyRect(110.0, yCount, 220.0, yCount + 13, -1157627904, 255);
				f.drawString(m.getName(), 113, yCount + 3, 16777215);
				yCount += 13;
			}
		}
		if (mods == "Render") {
			yCount = 10;
			for (Mod m : Render) {
				if (m.getName() == "ClickGUI") {
					return;
				}
				ClickGUI.drawSexyRect(110.0, yCount, 220.0, yCount + 13, -1157627904, 255);
				f.drawString(m.getName(), 113, yCount + 3, 16777215);
				yCount += 13;
			}
		}
		if (mods == "Movement") {
			yCount = 10;
			for (Mod m : Movement) {
				ClickGUI.drawSexyRect(110.0, yCount, 220.0, yCount + 13, -1157627904, 255);
				f.drawString(m.getName(), 113, yCount + 3, 16777215);
				yCount += 13;
			}
		}
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		ClickGUI.dragGUI(this.fontRendererObj);
		super.drawScreen(i, j, f);
	}

	@Override
	public void mouseClicked(int i, int j, int k) throws IOException {
		if (k == 0) {
			if (i > 10 && i < 100 && j > 10 && 23 > j) {
				mods = "Combat";
			}
			if (i > 10 && i < 100 && j > 23 && 36 > j) {
				mods = "Render";
			}
			if (i > 10 && i < 100 && j > 36 && 49 > j) {
				mods = "Movement";
			}
			if (i > 10 && i < 100 && j > 49 && 62 > j) {
				mods = "Player";
			}
			if (i > 10 && i < 100 && j > 62 && 75 > j) {
				mods = "Misc";
			}
			if (i > 110 && 220 > i) {
				int ModCheck;
				if (mods == "Combat") {
					ModCheck = 0;
					for (Mod m : Combat) {
						if (10 + ModCheck * 13 < j && 23 + ModCheck * 13 > j) {
							m.toggle();
						}
						++ModCheck;
					}
				}
				if (mods == "Render") {
					ModCheck = 0;
					for (Mod m : Render) {
						if (10 + ModCheck * 13 < j && 23 + ModCheck * 13 > j) {
							if (m.getName() == "ClickGUI") {
								return;
							}
							m.toggle();
						}
						++ModCheck;
					}
				}
				if (mods == "Movement") {
					ModCheck = 0;
					for (Mod m : Movement) {
						if (10 + ModCheck * 13 < j && 23 + ModCheck * 13 > j) {
							m.toggle();
						}
						++ModCheck;
					}
				}
				if (mods == "Player") {
					ModCheck = 0;
					for (Mod m : Player) {
						if (10 + ModCheck * 13 < j && 23 + ModCheck * 13 > j) {
							m.toggle();
						}
						++ModCheck;
					}
				}
				if (mods == "Misc") {
					ModCheck = 0;
					for (Mod m : Misc) {
						if (10 + ModCheck * 13 < j && 23 + ModCheck * 13 > j) {
							m.toggle();
						}
						++ModCheck;
					}
				}
			}
		}
		super.mouseClicked(i, j, k);
	}
}
