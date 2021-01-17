/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package Blizzard.UI.GuiDrag;

import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import Blizzard.Category.Category;
import Blizzard.Mod.Mod;
import Blizzard.Mod.ModManager;
import Blizzard.Mod.mods.fight.KillAura;
import Blizzard.Mod.mods.render.ESP;

public class DragGUI
extends GuiScreen {
    public static boolean isModeDrag = false;
    public static boolean isModeDrag2 = false;
    public static boolean isExtended = false;
    public static int mX = 0;
    public static int mY = 0;
    public static int mX2 = 2;
    public static int mY2 = 18;
    public static String Settings = "None";

    public static void drawSexyRect(double x, double y, double x1, double y1, int color, int color2) {
        Gui.drawRect((int)x, (int)y, (int)x1, (int)y1, color);
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        Gui.drawRect((int)x * 2 - 1, (int)y * 2, (int)x * 2, (int)y1 * 2 - 1, color2);
        Gui.drawRect((int)x * 2, (int)y * 2 - 1, (int)x1 * 2, (int)y * 2, color2);
        Gui.drawRect((int)x1 * 2, (int)y * 2, (int)x1 * 2 + 1, (int)y1 * 2 - 1, color2);
        Gui.drawRect((int)x * 2, (int)y1 * 2 - 1, (int)x1 * 2, (int)y1 * 2, color2);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
    }

    public static void dragGUI(FontRenderer f) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        int bottom = scaledResolution.getScaledHeight();
        f.drawString("\u00a77Left Click mod to toggle the mod", 3, bottom - 30, -1);
        f.drawString("\u00a77Right Click mod to view settings", 3, bottom - 20, -1);
        DragGUI.drawSexyRect(0 + mX, 0 + mY, 125 + mX, 13 + mY, -1157627904, -16777216);
        f.drawString(isModeDrag ? "\u00a7aMods" : "\u00a7dMods", 3 + mX, 3 + mY, -1);
        DragGUI.drawSexyRect(0 + mX2, 0 + mY2, 125 + mX2, 13 + mY2, -1157627904, -16777216);
        f.drawString(isModeDrag2 ? "\u00a7aSettings: " + Settings : "\u00a7dSettings: " + Settings, 3 + mX2, 3 + mY2, -1);
        if (isExtended) {
            int ClickY = mY + 15;
            for (Mod m : ModManager.activeMods) {
                if (m.getCategory() == Category.NONE) continue;
                DragGUI.drawSexyRect(0 + mX, 0 + ClickY, 125 + mX, 13 + ClickY, -1157627904, -16777216);
                f.drawString(m.isToggled() ? "\u00a7b" + m.getName() : "\u00a7c" + m.getName(), 3 + mX, 3 + ClickY, -1);
                ClickY += 13;
            }
            if (Settings == "KillAura") {
                DragGUI.drawSexyRect(0 + mX2, 15 + mY2, 125 + mX2, 28 + mY2, -1157627904, -16777216);
                f.drawString("\u00a7bAPS: " + KillAura.cps, 3 + mX2, 18 + mY2, -1);
                DragGUI.drawSexyRect(0 + mX2, 28 + mY2, 125 + mX2, 41 + mY2, -1157627904, -16777216);
                f.drawString("\u00a7bReach: " + KillAura.reach, 3 + mX2, 31 + mY2, -1);
            }
            if (Settings == "ESP") {
                DragGUI.drawSexyRect(0 + mX2, 15 + mY2, 125 + mX2, 28 + mY2, -1157627904, -16777216);
                f.drawString(ESP.Players ? "\u00a7bPlayers" : "\u00a7cPlayers", 3 + mX2, 18 + mY2, -1);
                DragGUI.drawSexyRect(0 + mX2, 28 + mY2, 125 + mX2, 41 + mY2, -1157627904, -16777216);
                f.drawString(ESP.Mobs ? "\u00a7bMobs" : "\u00a7cMobs", 3 + mX2, 31 + mY2, -1);
                DragGUI.drawSexyRect(0 + mX2, 41 + mY2, 125 + mX2, 54 + mY2, -1157627904, -16777216);
                f.drawString(ESP.Animals ? "\u00a7bAnimals" : "\u00a7cAnimals", 3 + mX2, 44 + mY2, -1);
            }
        }
    }

    public void mouseMovedOrUp(int i, int j, int k) {
        if (k == 0) {
            isModeDrag = false;
        }
        if (k == 0) {
            isModeDrag2 = false;
        }
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        DragGUI.dragGUI(this.fontRendererObj);
        this.modeDrag(i, j);
        super.drawScreen(i, j, f);
    }

    @Override
    public void mouseClicked(int i, int j, int k) throws IOException {
        int ModCheck;
        if (k == 1) {
            if (0 + mX < i && 125 + mX > i && 0 + mY < j && 13 + mY > j) {
                if (isExtended) {
                    isExtended = false;
                    return;
                }
                isExtended = true;
                return;
            }
            if (isExtended && 0 + mX < i && 125 + mX > i && j > 14 + mY) {
                ModCheck = 0;
                for (Mod m : ModManager.activeMods) {
                    if (j > mY + 15 + ModCheck * 13 && j < 13 + mY + 15 + ModCheck * 13) {
                        Settings = m.getName();
                        return;
                    }
                    ++ModCheck;
                }
            }
        }
        if (k == 0) {
            if (0 + mX < i && 125 + mX > i && 0 + mY < j && 13 + mY > j && !isModeDrag2) {
                if (isModeDrag) {
                    isModeDrag = false;
                    return;
                }
                isModeDrag = true;
                return;
            }
            if (0 + mX2 < i && 125 + mX2 > i && 0 + mY2 < j && 13 + mY2 > j && !isModeDrag) {
                if (isModeDrag2) {
                    isModeDrag2 = false;
                    return;
                }
                isModeDrag2 = true;
                return;
            }
            if (isExtended && 0 + mX < i && 125 + mX > i && j > 14 + mY) {
                ModCheck = 0;
                for (Mod m : ModManager.activeMods) {
                    if (j > mY + 15 + ModCheck * 13 && j < 13 + mY + 15 + ModCheck * 13) {
                        m.toggle();
                        return;
                    }
                    ++ModCheck;
                }
            }
            if (0 + mX2 < i && 125 + mX2 > i && j > 14 + mY) {
                if (Settings == "KillAura") {
                    if (j > 15 + mY2 && j < 28 + mY2) {
                        if (KillAura.cps == 20.0) {
                            KillAura.cps = 1.0;
                            return;
                        }
                        KillAura.cps += 1.0;
                    }
                    if (j > 28 + mY2 && j < 41 + mY2) {
                        if (KillAura.reach == 7.0) {
                            KillAura.reach = 1.0;
                            return;
                        }
                        KillAura.reach += 1.0;
                    }
                }
                if (Settings == "ESP") {
                    if (j > 15 + mY2 && j < 28 + mY2) {
                        if (ESP.Players) {
                            ESP.Players = false;
                            return;
                        }
                        ESP.Players = true;
                        return;
                    }
                    if (j > 28 + mY2 && j < 41 + mY2) {
                        if (ESP.Mobs) {
                            ESP.Mobs = false;
                            return;
                        }
                        ESP.Mobs = true;
                        return;
                    }
                    if (j > 41 + mY2 && j < 54 + mY2) {
                        if (ESP.Animals) {
                            ESP.Animals = false;
                            return;
                        }
                        ESP.Animals = true;
                        return;
                    }
                }
            }
        }
        super.mouseClicked(i, j, k);
    }

    public void modeDrag(int i, int j) {
        if (isModeDrag) {
            mX = i - 50;
            mY = j - 5;
        }
        if (isModeDrag2) {
            mX2 = i - 50;
            mY2 = j - 5;
        }
    }
}

