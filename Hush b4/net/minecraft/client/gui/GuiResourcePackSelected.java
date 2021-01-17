// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.ResourcePackListEntry;
import java.util.List;
import net.minecraft.client.Minecraft;

public class GuiResourcePackSelected extends GuiResourcePackList
{
    public GuiResourcePackSelected(final Minecraft mcIn, final int p_i45056_2_, final int p_i45056_3_, final List<ResourcePackListEntry> p_i45056_4_) {
        super(mcIn, p_i45056_2_, p_i45056_3_, p_i45056_4_);
    }
    
    @Override
    protected String getListHeader() {
        return I18n.format("resourcePack.selected.title", new Object[0]);
    }
}
