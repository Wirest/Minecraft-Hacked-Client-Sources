// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.network.LanServerDetector;
import net.minecraft.client.Minecraft;

public class ServerListEntryLanDetected implements GuiListExtended.IGuiListEntry
{
    private final GuiMultiplayer field_148292_c;
    protected final Minecraft mc;
    protected final LanServerDetector.LanServer field_148291_b;
    private long field_148290_d;
    
    protected ServerListEntryLanDetected(final GuiMultiplayer p_i45046_1_, final LanServerDetector.LanServer p_i45046_2_) {
        this.field_148290_d = 0L;
        this.field_148292_c = p_i45046_1_;
        this.field_148291_b = p_i45046_2_;
        this.mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void drawEntry(final int slotIndex, final int x, final int y, final int listWidth, final int slotHeight, final int mouseX, final int mouseY, final boolean isSelected) {
        this.mc.fontRendererObj.drawString(I18n.format("lanServer.title", new Object[0]), x + 32 + 3, y + 1, 16777215);
        this.mc.fontRendererObj.drawString(this.field_148291_b.getServerMotd(), x + 32 + 3, y + 12, 8421504);
        if (this.mc.gameSettings.hideServerAddress) {
            this.mc.fontRendererObj.drawString(I18n.format("selectServer.hiddenAddress", new Object[0]), x + 32 + 3, y + 12 + 11, 3158064);
        }
        else {
            this.mc.fontRendererObj.drawString(this.field_148291_b.getServerIpPort(), x + 32 + 3, y + 12 + 11, 3158064);
        }
    }
    
    @Override
    public boolean mousePressed(final int slotIndex, final int p_148278_2_, final int p_148278_3_, final int p_148278_4_, final int p_148278_5_, final int p_148278_6_) {
        this.field_148292_c.selectServer(slotIndex);
        if (Minecraft.getSystemTime() - this.field_148290_d < 250L) {
            this.field_148292_c.connectToSelected();
        }
        this.field_148290_d = Minecraft.getSystemTime();
        return false;
    }
    
    @Override
    public void setSelected(final int p_178011_1_, final int p_178011_2_, final int p_178011_3_) {
    }
    
    @Override
    public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
    }
    
    public LanServerDetector.LanServer getLanServer() {
        return this.field_148291_b;
    }
}
