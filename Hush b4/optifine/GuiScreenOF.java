// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.client.gui.GuiVideoSettings;
import java.util.List;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiScreenOF extends GuiScreen
{
    protected void actionPerformedRightClick(final GuiButton p_actionPerformedRightClick_1_) throws IOException {
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 1) {
            final GuiButton guibutton = getSelectedButton(this.buttonList, mouseX, mouseY);
            if (guibutton != null && guibutton.enabled) {
                guibutton.playPressSound(this.mc.getSoundHandler());
                this.actionPerformedRightClick(guibutton);
            }
        }
    }
    
    public static GuiButton getSelectedButton(final List<GuiButton> p_getSelectedButton_0_, final int p_getSelectedButton_1_, final int p_getSelectedButton_2_) {
        for (int i = 0; i < p_getSelectedButton_0_.size(); ++i) {
            final GuiButton guibutton = p_getSelectedButton_0_.get(i);
            if (guibutton.visible) {
                final int j = GuiVideoSettings.getButtonWidth(guibutton);
                final int k = GuiVideoSettings.getButtonHeight(guibutton);
                if (p_getSelectedButton_1_ >= guibutton.xPosition && p_getSelectedButton_2_ >= guibutton.yPosition && p_getSelectedButton_1_ < guibutton.xPosition + j && p_getSelectedButton_2_ < guibutton.yPosition + k) {
                    return guibutton;
                }
            }
        }
        return null;
    }
}
