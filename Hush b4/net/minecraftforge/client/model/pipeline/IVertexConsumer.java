// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraftforge.client.model.pipeline;

import net.minecraft.util.EnumFacing;
import net.minecraft.client.renderer.vertex.VertexFormat;

public interface IVertexConsumer
{
    VertexFormat getVertexFormat();
    
    void setQuadTint(final int p0);
    
    void setQuadOrientation(final EnumFacing p0);
    
    void setQuadColored();
    
    void put(final int p0, final float... p1);
}
