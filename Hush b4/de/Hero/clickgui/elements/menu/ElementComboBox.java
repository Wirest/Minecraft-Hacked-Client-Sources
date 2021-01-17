// 
// Decompiled by Procyon v0.5.36
// 

package de.Hero.clickgui.elements.menu;

import net.minecraft.client.Minecraft;
import me.nico.hush.Client;
import java.util.Iterator;
import de.Hero.clickgui.util.FontUtil;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import de.Hero.clickgui.util.ColorUtil;
import de.Hero.settings.Setting;
import de.Hero.clickgui.elements.ModuleButton;
import de.Hero.clickgui.elements.Element;

public class ElementComboBox extends Element
{
    public ElementComboBox(final ModuleButton iparent, final Setting iset) {
        this.parent = iparent;
        this.set = iset;
        super.setup();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final Color temp = ColorUtil.getClickGUIColor();
        final int color = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 150).getRGB();
        Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -2130706432);
        FontUtil.drawTotalCenteredString((this.set.getDisplay() == null) ? this.setstrg : this.set.getDisplay(), this.x + this.width / 2.0, this.y + 7.0, -1);
        final int clr1 = color;
        final int clr2 = temp.getRGB();
        Gui.drawRect(this.x, this.y + 14.0, this.x + this.width, this.y + 15.0, 1996488704);
        if (this.comboextended) {
            Gui.drawRect(this.x, this.y + 15.0, this.x + this.width, this.y + this.height, -2130706432);
            double ay = this.y + 15.0;
            for (final String sld : this.set.getOptions()) {
                final String elementtitle = String.valueOf(sld.substring(0, 1).toUpperCase()) + sld.substring(1, sld.length());
                FontUtil.drawCenteredString(elementtitle, this.x + this.width / 2.0, ay + 2.0, 16777215);
                if (sld.equalsIgnoreCase(this.set.getValString())) {
                    Gui.drawRect(this.x, ay, this.x + 1.5, ay + FontUtil.getFontHeight() + 2.0, clr1);
                }
                if (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= ay && mouseY < ay + FontUtil.getFontHeight() + 2.0) {
                    Gui.drawRect(this.x + this.width - 1.2, ay, this.x + this.width, ay + FontUtil.getFontHeight() + 2.0, clr2);
                }
                ay += FontUtil.getFontHeight() + 2;
            }
        }
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (mouseButton == 0) {
            if (this.isButtonHovered(mouseX, mouseY)) {
                this.comboextended = !this.comboextended;
                return true;
            }
            if (!this.comboextended) {
                return false;
            }
            double ay = this.y + 15.0;
            for (final String slcd : this.set.getOptions()) {
                if (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= ay && mouseY <= ay + FontUtil.getFontHeight() + 2.0) {
                    if (Client.instance.settingManager.getSettingByName("Sound").getValBoolean()) {
                        Minecraft.getMinecraft();
                        Minecraft.thePlayer.playSound("tile.piston.in", 20.0f, 20.0f);
                    }
                    if (this.clickgui != null && this.clickgui.setmgr != null) {
                        this.clickgui.setmgr.getSettingByName(this.set.getName()).setValString(slcd.toLowerCase());
                    }
                    return true;
                }
                ay += FontUtil.getFontHeight() + 2;
            }
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    public boolean isButtonHovered(final int mouseX, final int mouseY) {
        return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + 15.0;
    }
}
