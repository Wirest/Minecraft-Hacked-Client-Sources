// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import net.minecraft.client.renderer.texture.Stitcher;

public class StitcherException extends RuntimeException
{
    private final Stitcher.Holder holder;
    
    public StitcherException(final Stitcher.Holder p_i2344_1_, final String p_i2344_2_) {
        super(p_i2344_2_);
        this.holder = p_i2344_1_;
    }
}
