// 
// Decompiled by Procyon v0.5.36
// 

package de.Hero.clickgui.elements;

import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import java.io.IOException;
import org.lwjgl.input.Keyboard;
import de.Hero.clickgui.util.FontUtil;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import de.Hero.clickgui.util.ColorUtil;
import java.util.Iterator;
import de.Hero.clickgui.elements.menu.ElementComboBox;
import de.Hero.clickgui.elements.menu.ElementSlider;
import de.Hero.clickgui.elements.menu.ElementCheckBox;
import de.Hero.settings.Setting;
import me.nico.hush.Client;
import net.minecraft.client.Minecraft;
import de.Hero.clickgui.Panel;
import java.util.ArrayList;
import me.nico.hush.modules.Module;

public class ModuleButton
{
    public Module mod;
    public ArrayList<Element> menuelements;
    public Panel parent;
    public double x;
    public double y;
    public double width;
    public double height;
    public boolean extended;
    public boolean listening;
    
    public ModuleButton(final Module imod, final Panel pl) {
        this.extended = false;
        this.listening = false;
        this.mod = imod;
        this.height = Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 2;
        this.parent = pl;
        this.menuelements = new ArrayList<Element>();
        if (Client.instance.settingManager.getSettingsByMod(imod) != null) {
            for (final Setting s : Client.instance.settingManager.getSettingsByMod(imod)) {
                if (s.isCheck()) {
                    this.menuelements.add(new ElementCheckBox(this, s));
                }
                else if (s.isSlider()) {
                    this.menuelements.add(new ElementSlider(this, s));
                }
                else {
                    if (!s.isCombo()) {
                        continue;
                    }
                    this.menuelements.add(new ElementComboBox(this, s));
                }
            }
        }
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final Color temp = ColorUtil.getClickGUIColor();
        final int color = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 150).getRGB();
        int textcolor = -5263441;
        if (this.mod.isEnabled()) {
            Gui.drawRect(this.x - 2.0, this.y, this.x + this.width + 2.0, this.y + this.height + 1.0, color);
            textcolor = -1052689;
        }
        if (this.isHovered(mouseX, mouseY)) {
            Gui.drawRect(this.x - 2.0, this.y, this.x + this.width + 2.0, this.y + this.height + 1.0, 1427181841);
        }
        FontUtil.drawTotalCenteredStringWithShadow(this.mod.getName(), this.x + this.width / 2.0, this.y + 1.0 + this.height / 2.0, textcolor);
    }
    
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (!this.isHovered(mouseX, mouseY)) {
            return false;
        }
        if (mouseButton == 0) {
            this.mod.toggle();
            if (Client.instance.settingManager.getSettingByName("Sound").getValBoolean()) {
                Minecraft.getMinecraft();
                Minecraft.thePlayer.playSound("random.click", 0.5f, 0.5f);
            }
        }
        else if (mouseButton == 1) {
            if (this.menuelements != null && this.menuelements.size() > 0) {
                final boolean b = !this.extended;
                Client.instance.clickGUI.closeAllSettings();
                this.extended = b;
                if (Client.instance.settingManager.getSettingByName("Sound").getValBoolean()) {
                    if (this.extended) {
                        Minecraft.getMinecraft();
                        Minecraft.thePlayer.playSound("tile.piston.out", 1.0f, 1.0f);
                    }
                    else {
                        Minecraft.getMinecraft();
                        Minecraft.thePlayer.playSound("tile.piston.in", 1.0f, 1.0f);
                    }
                }
            }
        }
        else if (mouseButton == 2) {
            this.listening = true;
        }
        return true;
    }
    
    public boolean keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (this.listening) {
            if (keyCode != 1) {
                final int key = keyCode;
                this.mod.setKeyBind(key);
                messageWithPrefix("§7The Module §f" + this.mod.getDisplayname() + "§r§7 was placed on the §f" + Keyboard.getKeyName(keyCode) + " §7Button.");
                Client.instance.FileManager().saveKeyBinds();
            }
            else {
                messageWithPrefix("§7The Keybind for §f" + this.mod.getDisplayname() + "§r§7 has been removed.");
                this.mod.setKeyBind(0);
                Client.instance.FileManager().saveKeyBinds();
            }
            this.listening = false;
            return true;
        }
        return false;
    }
    
    public boolean isHovered(final int mouseX, final int mouseY) {
        return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;
    }
    
    public static void messageWithoutPrefix(final String msg) {
        final Object chat = new ChatComponentText(msg);
        if (msg != null) {
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage((IChatComponent)chat);
        }
    }
    
    public static void messageWithPrefix(final String msg) {
        messageWithoutPrefix(String.valueOf(Client.instance.ClientPrefix) + msg);
    }
}
