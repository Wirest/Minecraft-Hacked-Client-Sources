// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLivingBase;

public class MobAppearance extends EntityFX
{
    private EntityLivingBase entity;
    
    protected MobAppearance(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0, 0.0, 0.0);
        final float particleRed = 1.0f;
        this.particleBlue = particleRed;
        this.particleGreen = particleRed;
        this.particleRed = particleRed;
        final double motionX = 0.0;
        this.motionZ = motionX;
        this.motionY = motionX;
        this.motionX = motionX;
        this.particleGravity = 0.0f;
        this.particleMaxAge = 30;
    }
    
    @Override
    public int getFXLayer() {
        return 3;
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.entity == null) {
            final EntityGuardian entityguardian = new EntityGuardian(this.worldObj);
            entityguardian.setElder();
            this.entity = entityguardian;
        }
    }
    
    @Override
    public void renderParticle(final WorldRenderer worldRendererIn, final Entity entityIn, final float partialTicks, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
        if (this.entity != null) {
            final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
            rendermanager.setRenderPosition(EntityFX.interpPosX, EntityFX.interpPosY, EntityFX.interpPosZ);
            final float f = 0.42553192f;
            final float f2 = (this.particleAge + partialTicks) / this.particleMaxAge;
            GlStateManager.depthMask(true);
            GlStateManager.enableBlend();
            GlStateManager.enableDepth();
            GlStateManager.blendFunc(770, 771);
            final float f3 = 240.0f;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f3, f3);
            GlStateManager.pushMatrix();
            final float f4 = 0.05f + 0.5f * MathHelper.sin(f2 * 3.1415927f);
            GlStateManager.color(1.0f, 1.0f, 1.0f, f4);
            GlStateManager.translate(0.0f, 1.8f, 0.0f);
            GlStateManager.rotate(180.0f - entityIn.rotationYaw, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(60.0f - 150.0f * f2 - entityIn.rotationPitch, 1.0f, 0.0f, 0.0f);
            GlStateManager.translate(0.0f, -0.4f, -1.5f);
            GlStateManager.scale(f, f, f);
            final EntityLivingBase entity = this.entity;
            final EntityLivingBase entity2 = this.entity;
            final float n = 0.0f;
            entity2.prevRotationYaw = n;
            entity.rotationYaw = n;
            final EntityLivingBase entity3 = this.entity;
            final EntityLivingBase entity4 = this.entity;
            final float n2 = 0.0f;
            entity4.prevRotationYawHead = n2;
            entity3.rotationYawHead = n2;
            rendermanager.renderEntityWithPosYaw(this.entity, 0.0, 0.0, 0.0, 0.0f, partialTicks);
            GlStateManager.popMatrix();
            GlStateManager.enableDepth();
        }
    }
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public EntityFX getEntityFX(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new MobAppearance(worldIn, xCoordIn, yCoordIn, zCoordIn);
        }
    }
}
