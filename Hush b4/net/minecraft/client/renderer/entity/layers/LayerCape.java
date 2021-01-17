// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;

public class LayerCape implements LayerRenderer
{
    private final RenderPlayer playerRenderer;
    private static final String __OBFID = "CL_00002425";
    
    public LayerCape(final RenderPlayer playerRendererIn) {
        this.playerRenderer = playerRendererIn;
    }
    
    public void doRenderLayer(final AbstractClientPlayer entitylivingbaseIn, final float p_177141_2_, final float p_177141_3_, final float partialTicks, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float scale) {
        if (entitylivingbaseIn.hasPlayerInfo() && !entitylivingbaseIn.isInvisible() && entitylivingbaseIn.isWearing(EnumPlayerModelParts.CAPE) && entitylivingbaseIn.getLocationCape() != null) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.playerRenderer.bindTexture(entitylivingbaseIn.getLocationCape());
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, 0.0f, 0.125f);
            final double d0 = entitylivingbaseIn.prevChasingPosX + (entitylivingbaseIn.chasingPosX - entitylivingbaseIn.prevChasingPosX) * partialTicks - (entitylivingbaseIn.prevPosX + (entitylivingbaseIn.posX - entitylivingbaseIn.prevPosX) * partialTicks);
            final double d2 = entitylivingbaseIn.prevChasingPosY + (entitylivingbaseIn.chasingPosY - entitylivingbaseIn.prevChasingPosY) * partialTicks - (entitylivingbaseIn.prevPosY + (entitylivingbaseIn.posY - entitylivingbaseIn.prevPosY) * partialTicks);
            final double d3 = entitylivingbaseIn.prevChasingPosZ + (entitylivingbaseIn.chasingPosZ - entitylivingbaseIn.prevChasingPosZ) * partialTicks - (entitylivingbaseIn.prevPosZ + (entitylivingbaseIn.posZ - entitylivingbaseIn.prevPosZ) * partialTicks);
            final float f = entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks;
            final double d4 = MathHelper.sin(f * 3.1415927f / 180.0f);
            final double d5 = -MathHelper.cos(f * 3.1415927f / 180.0f);
            float f2 = (float)d2 * 10.0f;
            f2 = MathHelper.clamp_float(f2, -6.0f, 32.0f);
            float f3 = (float)(d0 * d4 + d3 * d5) * 100.0f;
            final float f4 = (float)(d0 * d5 - d3 * d4) * 100.0f;
            if (f3 < 0.0f) {
                f3 = 0.0f;
            }
            if (f3 > 165.0f) {
                f3 = 165.0f;
            }
            final float f5 = entitylivingbaseIn.prevCameraYaw + (entitylivingbaseIn.cameraYaw - entitylivingbaseIn.prevCameraYaw) * partialTicks;
            f2 += MathHelper.sin((entitylivingbaseIn.prevDistanceWalkedModified + (entitylivingbaseIn.distanceWalkedModified - entitylivingbaseIn.prevDistanceWalkedModified) * partialTicks) * 6.0f) * 32.0f * f5;
            if (entitylivingbaseIn.isSneaking()) {
                f2 += 25.0f;
                GlStateManager.translate(0.0f, 0.142f, -0.0178f);
            }
            GlStateManager.rotate(6.0f + f3 / 2.0f + f2, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(f4 / 2.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate(-f4 / 2.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
            this.playerRenderer.getMainModel().renderCape(0.0625f);
            GlStateManager.popMatrix();
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
    
    @Override
    public void doRenderLayer(final EntityLivingBase entitylivingbaseIn, final float p_177141_2_, final float p_177141_3_, final float partialTicks, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float scale) {
        this.doRenderLayer((AbstractClientPlayer)entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale);
    }
}
