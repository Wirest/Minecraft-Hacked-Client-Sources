// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item;
import net.minecraft.entity.Entity;

public class RenderSnowball<T extends Entity> extends Render<T>
{
    protected final Item field_177084_a;
    private final RenderItem field_177083_e;
    
    public RenderSnowball(final RenderManager renderManagerIn, final Item p_i46137_2_, final RenderItem p_i46137_3_) {
        super(renderManagerIn);
        this.field_177084_a = p_i46137_2_;
        this.field_177083_e = p_i46137_3_;
    }
    
    @Override
    public void doRender(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(RenderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        this.bindTexture(TextureMap.locationBlocksTexture);
        this.field_177083_e.func_181564_a(this.func_177082_d(entity), ItemCameraTransforms.TransformType.GROUND);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    public ItemStack func_177082_d(final T entityIn) {
        return new ItemStack(this.field_177084_a, 1, 0);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity entity) {
        return TextureMap.locationBlocksTexture;
    }
}
