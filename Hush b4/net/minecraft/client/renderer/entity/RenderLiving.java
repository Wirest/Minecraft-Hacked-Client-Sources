// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityHanging;
import net.minecraft.client.renderer.Tessellator;
import shadersmod.client.Shaders;
import optifine.Config;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLiving;

public abstract class RenderLiving<T extends EntityLiving> extends RendererLivingEntity<T>
{
    private static final String __OBFID = "CL_00001015";
    
    public RenderLiving(final RenderManager rendermanagerIn, final ModelBase modelbaseIn, final float shadowsizeIn) {
        super(rendermanagerIn, modelbaseIn, shadowsizeIn);
    }
    
    @Override
    protected boolean canRenderName(final T entity) {
        return super.canRenderName(entity) && (entity.getAlwaysRenderNameTagForRender() || (entity.hasCustomName() && entity == this.renderManager.pointedEntity));
    }
    
    @Override
    public boolean shouldRender(final T livingEntity, final ICamera camera, final double camX, final double camY, final double camZ) {
        if (super.shouldRender(livingEntity, camera, camX, camY, camZ)) {
            return true;
        }
        if (livingEntity.getLeashed() && livingEntity.getLeashedToEntity() != null) {
            final Entity entity = livingEntity.getLeashedToEntity();
            return camera.isBoundingBoxInFrustum(entity.getEntityBoundingBox());
        }
        return false;
    }
    
    @Override
    public void doRender(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        this.renderLeash(entity, x, y, z, entityYaw, partialTicks);
    }
    
    public void func_177105_a(final T entityLivingIn, final float partialTicks) {
        final int i = entityLivingIn.getBrightnessForRender(partialTicks);
        final int j = i % 65536;
        final int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0f, k / 1.0f);
    }
    
    private double interpolateValue(final double start, final double end, final double pct) {
        return start + (end - start) * pct;
    }
    
    protected void renderLeash(final T entityLivingIn, double x, double y, double z, final float entityYaw, final float partialTicks) {
        if (!Config.isShaders() || !Shaders.isShadowPass) {
            final Entity entity = entityLivingIn.getLeashedToEntity();
            if (entity != null) {
                y -= (1.6 - entityLivingIn.height) * 0.5;
                final Tessellator tessellator = Tessellator.getInstance();
                final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                final double d0 = this.interpolateValue(entity.prevRotationYaw, entity.rotationYaw, partialTicks * 0.5f) * 0.01745329238474369;
                final double d2 = this.interpolateValue(entity.prevRotationPitch, entity.rotationPitch, partialTicks * 0.5f) * 0.01745329238474369;
                double d3 = Math.cos(d0);
                double d4 = Math.sin(d0);
                double d5 = Math.sin(d2);
                if (entity instanceof EntityHanging) {
                    d3 = 0.0;
                    d4 = 0.0;
                    d5 = -1.0;
                }
                final double d6 = Math.cos(d2);
                final double d7 = this.interpolateValue(entity.prevPosX, entity.posX, partialTicks) - d3 * 0.7 - d4 * 0.5 * d6;
                final double d8 = this.interpolateValue(entity.prevPosY + entity.getEyeHeight() * 0.7, entity.posY + entity.getEyeHeight() * 0.7, partialTicks) - d5 * 0.5 - 0.25;
                final double d9 = this.interpolateValue(entity.prevPosZ, entity.posZ, partialTicks) - d4 * 0.7 + d3 * 0.5 * d6;
                final double d10 = this.interpolateValue(entityLivingIn.prevRenderYawOffset, entityLivingIn.renderYawOffset, partialTicks) * 0.01745329238474369 + 1.5707963267948966;
                d3 = Math.cos(d10) * entityLivingIn.width * 0.4;
                d4 = Math.sin(d10) * entityLivingIn.width * 0.4;
                final double d11 = this.interpolateValue(entityLivingIn.prevPosX, entityLivingIn.posX, partialTicks) + d3;
                final double d12 = this.interpolateValue(entityLivingIn.prevPosY, entityLivingIn.posY, partialTicks);
                final double d13 = this.interpolateValue(entityLivingIn.prevPosZ, entityLivingIn.posZ, partialTicks) + d4;
                x += d3;
                z += d4;
                final double d14 = (float)(d7 - d11);
                final double d15 = (float)(d8 - d12);
                final double d16 = (float)(d9 - d13);
                GlStateManager.disableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableCull();
                if (Config.isShaders()) {
                    Shaders.beginLeash();
                }
                final boolean flag = true;
                final double d17 = 0.025;
                worldrenderer.begin(5, DefaultVertexFormats.POSITION_COLOR);
                for (int i = 0; i <= 24; ++i) {
                    float f = 0.5f;
                    float f2 = 0.4f;
                    float f3 = 0.3f;
                    if (i % 2 == 0) {
                        f *= 0.7f;
                        f2 *= 0.7f;
                        f3 *= 0.7f;
                    }
                    final float f4 = i / 24.0f;
                    worldrenderer.pos(x + d14 * f4 + 0.0, y + d15 * (f4 * f4 + f4) * 0.5 + ((24.0f - i) / 18.0f + 0.125f), z + d16 * f4).color(f, f2, f3, 1.0f).endVertex();
                    worldrenderer.pos(x + d14 * f4 + 0.025, y + d15 * (f4 * f4 + f4) * 0.5 + ((24.0f - i) / 18.0f + 0.125f) + 0.025, z + d16 * f4).color(f, f2, f3, 1.0f).endVertex();
                }
                tessellator.draw();
                worldrenderer.begin(5, DefaultVertexFormats.POSITION_COLOR);
                for (int j = 0; j <= 24; ++j) {
                    float f5 = 0.5f;
                    float f6 = 0.4f;
                    float f7 = 0.3f;
                    if (j % 2 == 0) {
                        f5 *= 0.7f;
                        f6 *= 0.7f;
                        f7 *= 0.7f;
                    }
                    final float f8 = j / 24.0f;
                    worldrenderer.pos(x + d14 * f8 + 0.0, y + d15 * (f8 * f8 + f8) * 0.5 + ((24.0f - j) / 18.0f + 0.125f) + 0.025, z + d16 * f8).color(f5, f6, f7, 1.0f).endVertex();
                    worldrenderer.pos(x + d14 * f8 + 0.025, y + d15 * (f8 * f8 + f8) * 0.5 + ((24.0f - j) / 18.0f + 0.125f), z + d16 * f8 + 0.025).color(f5, f6, f7, 1.0f).endVertex();
                }
                tessellator.draw();
                if (Config.isShaders()) {
                    Shaders.endLeash();
                }
                GlStateManager.enableLighting();
                GlStateManager.enableTexture2D();
                GlStateManager.enableCull();
            }
        }
    }
}
