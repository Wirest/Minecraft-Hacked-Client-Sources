// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.ResourcePackListEntry;
import java.util.List;
import net.minecraft.client.Minecraft;

public abstract class GuiResourcePackList extends GuiListExtended
{
    protected final Minecraft mc;
    protected final List<ResourcePackListEntry> field_148204_l;
    
    public GuiResourcePackList(final Minecraft mcIn, final int p_i45055_2_, final int p_i45055_3_, final List<ResourcePackListEntry> p_i45055_4_) {
        super(mcIn, p_i45055_2_, p_i45055_3_, 32, p_i45055_3_ - 55 + 4, 36);
        this.mc = mcIn;
        this.field_148204_l = p_i45055_4_;
        this.field_148163_i = false;
        this.setHasListHeader(true, (int)(mcIn.fontRendererObj.FONT_HEIGHT * 1.5f));
    }
    
    @Override
    protected void drawListHeader(final int p_148129_1_, final int p_148129_2_, final Tessellator p_148129_3_) {
        final String s = new StringBuilder().append(EnumChatFormatting.UNDERLINE).append(EnumChatFormatting.BOLD).append(this.getListHeader()).toString();
        this.mc.fontRendererObj.drawString(s, p_148129_1_ + this.width / 2 - this.mc.fontRendererObj.getStringWidth(s) / 2, Math.min(this.top + 3, p_148129_2_), 16777215);
    }
    
    protected abstract String getListHeader();
    
    public List<ResourcePackListEntry> getList() {
        return this.field_148204_l;
    }
    
    @Override
    protected int getSize() {
        return this.getList().size();
    }
    
    @Override
    public ResourcePackListEntry getListEntry(final int index) {
        return this.getList().get(index);
    }
    
    @Override
    public int getListWidth() {
        return this.width;
    }
    
    @Override
    protected int getScrollBarX() {
        return this.right - 6;
    }
}
