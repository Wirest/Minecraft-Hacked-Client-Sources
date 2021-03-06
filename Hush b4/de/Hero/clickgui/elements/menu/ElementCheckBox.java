// 
// Decompiled by Procyon v0.5.36
// 

package de.Hero.clickgui.elements.menu;

import de.Hero.clickgui.util.FontUtil;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import de.Hero.clickgui.util.ColorUtil;
import de.Hero.settings.Setting;
import de.Hero.clickgui.elements.ModuleButton;
import de.Hero.clickgui.elements.Element;

public class ElementCheckBox extends Element
{
    public ElementCheckBox(final ModuleButton iparent, final Setting iset) {
        this.parent = iparent;
        this.set = iset;
        super.setup();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final Color temp = ColorUtil.getClickGUIColor();
        final int color = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 200).getRGB();
        Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -2130706432);
        FontUtil.drawString((this.set.getDisplay() == null) ? this.setstrg : this.set.getDisplay(), this.x + this.width - FontUtil.getStringWidth(this.setstrg), this.y + FontUtil.getFontHeight() / 2 - 0.5, 16777215);
        Gui.drawRect(this.x + 1.0, this.y + 2.0, this.x + 12.0, this.y + 13.0, this.set.getValBoolean() ? color : -16777216);
        if (this.isCheckHovered(mouseX, mouseY)) {
            Gui.drawRect(this.x + 1.0, this.y + 2.0, this.x + 12.0, this.y + 13.0, 1427181841);
        }
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (mouseButton == 0 && this.isCheckHovered(mouseX, mouseY)) {
            this.set.setValBoolean(!this.set.getValBoolean());
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    public boolean isCheckHovered(final int mouseX, final int mouseY) {
        return mouseX >= this.x + 1.0 && mouseX <= this.x + 12.0 && mouseY >= this.y + 2.0 && mouseY <= this.y + 13.0;
    }
}
