// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Vec3;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.projectile.EntityFishHook;

public class RenderFish extends Render<EntityFishHook>
{
    private static final ResourceLocation FISH_PARTICLES;
    
    static {
        FISH_PARTICLES = new ResourceLocation("textures/particle/particles.png");
    }
    
    public RenderFish(final RenderManager renderManagerIn) {
        super(renderManagerIn);
    }
    
    @Override
    public void doRender(final EntityFishHook entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        this.bindEntityTexture(entity);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        final int i = 1;
        final int j = 2;
        final float f = 0.0625f;
        final float f2 = 0.125f;
        final float f3 = 0.125f;
        final float f4 = 0.1875f;
        final float f5 = 1.0f;
        final float f6 = 0.5f;
        final float f7 = 0.5f;
        GlStateManager.rotate(180.0f - RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-RenderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
        worldrenderer.pos(-0.5, -0.5, 0.0).tex(0.0625, 0.1875).normal(0.0f, 1.0f, 0.0f).endVertex();
        worldrenderer.pos(0.5, -0.5, 0.0).tex(0.125, 0.1875).normal(0.0f, 1.0f, 0.0f).endVertex();
        worldrenderer.pos(0.5, 0.5, 0.0).tex(0.125, 0.125).normal(0.0f, 1.0f, 0.0f).endVertex();
        worldrenderer.pos(-0.5, 0.5, 0.0).tex(0.0625, 0.125).normal(0.0f, 1.0f, 0.0f).endVertex();
        tessellator.draw();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        if (entity.angler != null) {
            final float f8 = entity.angler.getSwingProgress(partialTicks);
            final float f9 = MathHelper.sin(MathHelper.sqrt_float(f8) * 3.1415927f);
            Vec3 vec3 = new Vec3(-0.36, 0.03, 0.35);
            vec3 = vec3.rotatePitch(-(entity.angler.prevRotationPitch + (entity.angler.rotationPitch - entity.angler.prevRotationPitch) * partialTicks) * 3.1415927f / 180.0f);
            vec3 = vec3.rotateYaw(-(entity.angler.prevRotationYaw + (entity.angler.rotationYaw - entity.angler.prevRotationYaw) * partialTicks) * 3.1415927f / 180.0f);
            vec3 = vec3.rotateYaw(f9 * 0.5f);
            vec3 = vec3.rotatePitch(-f9 * 0.7f);
            double d0 = entity.angler.prevPosX + (entity.angler.posX - entity.angler.prevPosX) * partialTicks + vec3.xCoord;
            double d2 = entity.angler.prevPosY + (entity.angler.posY - entity.angler.prevPosY) * partialTicks + vec3.yCoord;
            double d3 = entity.angler.prevPosZ + (entity.angler.posZ - entity.angler.prevPosZ) * partialTicks + vec3.zCoord;
            double d4 = entity.angler.getEyeHeight();
            Label_0747: {
                if (this.renderManager.options == null || this.renderManager.options.thirdPersonView <= 0) {
                    final EntityPlayer angler = entity.angler;
                    Minecraft.getMinecraft();
                    if (angler == Minecraft.thePlayer) {
                        break Label_0747;
                    }
                }
                final float f10 = (entity.angler.prevRenderYawOffset + (entity.angler.renderYawOffset - entity.angler.prevRenderYawOffset) * partialTicks) * 3.1415927f / 180.0f;
                final double d5 = MathHelper.sin(f10);
                final double d6 = MathHelper.cos(f10);
                final double d7 = 0.35;
                final double d8 = 0.8;
                d0 = entity.angler.prevPosX + (entity.angler.posX - entity.angler.prevPosX) * partialTicks - d6 * 0.35 - d5 * 0.8;
                d2 = entity.angler.prevPosY + d4 + (entity.angler.posY - entity.angler.prevPosY) * partialTicks - 0.45;
                d3 = entity.angler.prevPosZ + (entity.angler.posZ - entity.angler.prevPosZ) * partialTicks - d5 * 0.35 + d6 * 0.8;
                d4 = (entity.angler.isSneaking() ? -0.1875 : 0.0);
            }
            final double d9 = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
            final double d10 = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + 0.25;
            final double d11 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
            final double d12 = (float)(d0 - d9);
            final double d13 = (float)(d2 - d10) + d4;
            final double d14 = (float)(d3 - d11);
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
            final int k = 16;
            for (int l = 0; l <= 16; ++l) {
                final float f11 = l / 16.0f;
                worldrenderer.pos(x + d12 * f11, y + d13 * (f11 * f11 + f11) * 0.5 + 0.25, z + d14 * f11).color(0, 0, 0, 255).endVertex();
            }
            tessellator.draw();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityFishHook entity) {
        return RenderFish.FISH_PARTICLES;
    }
}
