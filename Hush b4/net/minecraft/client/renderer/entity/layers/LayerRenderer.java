// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.EntityLivingBase;

public interface LayerRenderer<E extends EntityLivingBase>
{
    void doRenderLayer(final E p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final float p7);
    
    boolean shouldCombineTextures();
}
