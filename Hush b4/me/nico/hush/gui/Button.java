// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import de.Hero.clickgui.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class Button extends GuiButton
{
    private int fade;
    
    public Button(final int buttonId, final int x, final int y, final String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }
    
    public Button(final int buttonId, final int x, final int y, final int widtIn, final int heightIn, final String buttonText) {
        super(buttonId, x, y, widtIn, heightIn, buttonText);
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
            if (!this.hovered && this.fade != 100) {
                this.fade += 10;
            }
            final FontRenderer var4 = mc.fontRendererObj;
            Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, ColorUtil.getClickGUIColor().getRGB());
            this.drawCenteredString(var4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 16777215);
        }
    }
}
