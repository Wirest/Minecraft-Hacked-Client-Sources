// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import java.util.Random;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;

public class LayerArrow implements LayerRenderer<EntityLivingBase>
{
    private final RendererLivingEntity field_177168_a;
    
    public LayerArrow(final RendererLivingEntity p_i46124_1_) {
        this.field_177168_a = p_i46124_1_;
    }
    
    @Override
    public void doRenderLayer(final EntityLivingBase entitylivingbaseIn, final float p_177141_2_, final float p_177141_3_, final float partialTicks, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float scale) {
        final int i = entitylivingbaseIn.getArrowCountInEntity();
        if (i > 0) {
            final Entity entity = new EntityArrow(entitylivingbaseIn.worldObj, entitylivingbaseIn.posX, entitylivingbaseIn.posY, entitylivingbaseIn.posZ);
            final Random random = new Random(entitylivingbaseIn.getEntityId());
            RenderHelper.disableStandardItemLighting();
            for (int j = 0; j < i; ++j) {
                GlStateManager.pushMatrix();
                final ModelRenderer modelrenderer = this.field_177168_a.getMainModel().getRandomModelBox(random);
                final ModelBox modelbox = modelrenderer.cubeList.get(random.nextInt(modelrenderer.cubeList.size()));
                modelrenderer.postRender(0.0625f);
                float f = random.nextFloat();
                float f2 = random.nextFloat();
                float f3 = random.nextFloat();
                final float f4 = (modelbox.posX1 + (modelbox.posX2 - modelbox.posX1) * f) / 16.0f;
                final float f5 = (modelbox.posY1 + (modelbox.posY2 - modelbox.posY1) * f2) / 16.0f;
                final float f6 = (modelbox.posZ1 + (modelbox.posZ2 - modelbox.posZ1) * f3) / 16.0f;
                GlStateManager.translate(f4, f5, f6);
                f = f * 2.0f - 1.0f;
                f2 = f2 * 2.0f - 1.0f;
                f3 = f3 * 2.0f - 1.0f;
                f *= -1.0f;
                f2 *= -1.0f;
                f3 *= -1.0f;
                final float f7 = MathHelper.sqrt_float(f * f + f3 * f3);
                final Entity entity2 = entity;
                final Entity entity3 = entity;
                final float n = (float)(Math.atan2(f, f3) * 180.0 / 3.141592653589793);
                entity3.rotationYaw = n;
                entity2.prevRotationYaw = n;
                final Entity entity4 = entity;
                final Entity entity5 = entity;
                final float n2 = (float)(Math.atan2(f2, f7) * 180.0 / 3.141592653589793);
                entity5.rotationPitch = n2;
                entity4.prevRotationPitch = n2;
                final double d0 = 0.0;
                final double d2 = 0.0;
                final double d3 = 0.0;
                this.field_177168_a.getRenderManager().renderEntityWithPosYaw(entity, d0, d2, d3, 0.0f, partialTicks);
                GlStateManager.popMatrix();
            }
            RenderHelper.enableStandardItemLighting();
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
