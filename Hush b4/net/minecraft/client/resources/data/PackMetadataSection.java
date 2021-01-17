// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources.data;

import net.minecraft.util.IChatComponent;

public class PackMetadataSection implements IMetadataSection
{
    private final IChatComponent packDescription;
    private final int packFormat;
    
    public PackMetadataSection(final IChatComponent p_i1034_1_, final int p_i1034_2_) {
        this.packDescription = p_i1034_1_;
        this.packFormat = p_i1034_2_;
    }
    
    public IChatComponent getPackDescription() {
        return this.packDescription;
    }
    
    public int getPackFormat() {
        return this.packFormat;
    }
}
