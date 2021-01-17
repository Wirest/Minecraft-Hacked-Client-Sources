// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import java.util.Iterator;
import java.io.IOException;
import java.util.Collection;
import net.minecraft.client.resources.I18n;
import com.google.common.collect.Lists;
import java.util.List;

public class GuiYesNo extends GuiScreen
{
    protected GuiYesNoCallback parentScreen;
    protected String messageLine1;
    private String messageLine2;
    private final List<String> field_175298_s;
    protected String confirmButtonText;
    protected String cancelButtonText;
    protected int parentButtonClickedId;
    private int ticksUntilEnable;
    
    public GuiYesNo(final GuiYesNoCallback p_i1082_1_, final String p_i1082_2_, final String p_i1082_3_, final int p_i1082_4_) {
        this.field_175298_s = (List<String>)Lists.newArrayList();
        this.parentScreen = p_i1082_1_;
        this.messageLine1 = p_i1082_2_;
        this.messageLine2 = p_i1082_3_;
        this.parentButtonClickedId = p_i1082_4_;
        this.confirmButtonText = I18n.format("gui.yes", new Object[0]);
        this.cancelButtonText = I18n.format("gui.no", new Object[0]);
    }
    
    public GuiYesNo(final GuiYesNoCallback p_i1083_1_, final String p_i1083_2_, final String p_i1083_3_, final String p_i1083_4_, final String p_i1083_5_, final int p_i1083_6_) {
        this.field_175298_s = (List<String>)Lists.newArrayList();
        this.parentScreen = p_i1083_1_;
        this.messageLine1 = p_i1083_2_;
        this.messageLine2 = p_i1083_3_;
        this.confirmButtonText = p_i1083_4_;
        this.cancelButtonText = p_i1083_5_;
        this.parentButtonClickedId = p_i1083_6_;
    }
    
    @Override
    public void initGui() {
        this.buttonList.add(new GuiOptionButton(0, this.width / 2 - 155, this.height / 6 + 96, this.confirmButtonText));
        this.buttonList.add(new GuiOptionButton(1, this.width / 2 - 155 + 160, this.height / 6 + 96, this.cancelButtonText));
        this.field_175298_s.clear();
        this.field_175298_s.addAll(this.fontRendererObj.listFormattedStringToWidth(this.messageLine2, this.width - 50));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        this.parentScreen.confirmClicked(button.id == 0, this.parentButtonClickedId);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.messageLine1, this.width / 2, 70, 16777215);
        int i = 90;
        for (final String s : this.field_175298_s) {
            this.drawCenteredString(this.fontRendererObj, s, this.width / 2, i, 16777215);
            i += this.fontRendererObj.FONT_HEIGHT;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void setButtonDelay(final int p_146350_1_) {
        this.ticksUntilEnable = p_146350_1_;
        for (final GuiButton guibutton : this.buttonList) {
            guibutton.enabled = false;
        }
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        final int ticksUntilEnable = this.ticksUntilEnable - 1;
        this.ticksUntilEnable = ticksUntilEnable;
        if (ticksUntilEnable == 0) {
            for (final GuiButton guibutton : this.buttonList) {
                guibutton.enabled = true;
            }
        }
    }
}
