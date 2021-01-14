package net.minecraft.client.renderer.entity.layers;

import java.util.Random;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.MathHelper;

public class LayerArrow implements LayerRenderer {
    private final RendererLivingEntity field_177168_a;
    private static final String __OBFID = "CL_00002426";

    public LayerArrow(RendererLivingEntity p_i46124_1_) {
        this.field_177168_a = p_i46124_1_;
    }

    public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_) {
        int var9 = p_177141_1_.getArrowCountInEntity();

        if (var9 > 0) {
            EntityArrow var10 = new EntityArrow(p_177141_1_.worldObj, p_177141_1_.posX, p_177141_1_.posY, p_177141_1_.posZ);
            Random var11 = new Random((long) p_177141_1_.getEntityId());
            RenderHelper.disableStandardItemLighting();

            for (int var12 = 0; var12 < var9; ++var12) {
                GlStateManager.pushMatrix();
                ModelRenderer var13 = this.field_177168_a.getMainModel().getRandomModelBox(var11);
                ModelBox var14 = (ModelBox) var13.cubeList.get(var11.nextInt(var13.cubeList.size()));
                var13.postRender(0.0625F);
                float var15 = var11.nextFloat();
                float var16 = var11.nextFloat();
                float var17 = var11.nextFloat();
                float var18 = (var14.posX1 + (var14.posX2 - var14.posX1) * var15) / 16.0F;
                float var19 = (var14.posY1 + (var14.posY2 - var14.posY1) * var16) / 16.0F;
                float var20 = (var14.posZ1 + (var14.posZ2 - var14.posZ1) * var17) / 16.0F;
                GlStateManager.translate(var18, var19, var20);
                var15 = var15 * 2.0F - 1.0F;
                var16 = var16 * 2.0F - 1.0F;
                var17 = var17 * 2.0F - 1.0F;
                var15 *= -1.0F;
                var16 *= -1.0F;
                var17 *= -1.0F;
                float var21 = MathHelper.sqrt_float(var15 * var15 + var17 * var17);
                var10.prevRotationYaw = var10.rotationYaw = (float) (Math.atan2((double) var15, (double) var17) * 180.0D / Math.PI);
                var10.prevRotationPitch = var10.rotationPitch = (float) (Math.atan2((double) var16, (double) var21) * 180.0D / Math.PI);
                double var22 = 0.0D;
                double var24 = 0.0D;
                double var26 = 0.0D;
                this.field_177168_a.func_177068_d().renderEntityWithPosYaw(var10, var22, var24, var26, 0.0F, p_177141_4_);
                GlStateManager.popMatrix();
            }

            RenderHelper.enableStandardItemLighting();
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
