// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.world.World;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntitySpecialRenderer<T extends TileEntity>
{
    protected static final ResourceLocation[] DESTROY_STAGES;
    protected TileEntityRendererDispatcher rendererDispatcher;
    
    static {
        DESTROY_STAGES = new ResourceLocation[] { new ResourceLocation("textures/blocks/destroy_stage_0.png"), new ResourceLocation("textures/blocks/destroy_stage_1.png"), new ResourceLocation("textures/blocks/destroy_stage_2.png"), new ResourceLocation("textures/blocks/destroy_stage_3.png"), new ResourceLocation("textures/blocks/destroy_stage_4.png"), new ResourceLocation("textures/blocks/destroy_stage_5.png"), new ResourceLocation("textures/blocks/destroy_stage_6.png"), new ResourceLocation("textures/blocks/destroy_stage_7.png"), new ResourceLocation("textures/blocks/destroy_stage_8.png"), new ResourceLocation("textures/blocks/destroy_stage_9.png") };
    }
    
    public abstract void renderTileEntityAt(final T p0, final double p1, final double p2, final double p3, final float p4, final int p5);
    
    protected void bindTexture(final ResourceLocation location) {
        final TextureManager texturemanager = this.rendererDispatcher.renderEngine;
        if (texturemanager != null) {
            texturemanager.bindTexture(location);
        }
    }
    
    protected World getWorld() {
        return this.rendererDispatcher.worldObj;
    }
    
    public void setRendererDispatcher(final TileEntityRendererDispatcher rendererDispatcherIn) {
        this.rendererDispatcher = rendererDispatcherIn;
    }
    
    public FontRenderer getFontRenderer() {
        return this.rendererDispatcher.getFontRenderer();
    }
    
    public boolean func_181055_a() {
        return false;
    }
}
