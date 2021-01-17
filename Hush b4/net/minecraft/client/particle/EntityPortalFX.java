// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.world.World;

public class EntityPortalFX extends EntityFX
{
    private float portalParticleScale;
    private double portalPosX;
    private double portalPosY;
    private double portalPosZ;
    
    protected EntityPortalFX(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.motionX = xSpeedIn;
        this.motionY = ySpeedIn;
        this.motionZ = zSpeedIn;
        this.posX = xCoordIn;
        this.portalPosX = xCoordIn;
        this.posY = yCoordIn;
        this.portalPosY = yCoordIn;
        this.posZ = zCoordIn;
        this.portalPosZ = zCoordIn;
        final float f = this.rand.nextFloat() * 0.6f + 0.4f;
        final float n = this.rand.nextFloat() * 0.2f + 0.5f;
        this.particleScale = n;
        this.portalParticleScale = n;
        final float particleRed = 1.0f * f;
        this.particleBlue = particleRed;
        this.particleGreen = particleRed;
        this.particleRed = particleRed;
        this.particleGreen *= 0.3f;
        this.particleRed *= 0.9f;
        this.particleMaxAge = (int)(Math.random() * 10.0) + 40;
        this.noClip = true;
        this.setParticleTextureIndex((int)(Math.random() * 8.0));
    }
    
    @Override
    public void renderParticle(final WorldRenderer worldRendererIn, final Entity entityIn, final float partialTicks, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
        float f = (this.particleAge + partialTicks) / this.particleMaxAge;
        f = 1.0f - f;
        f *= f;
        f = 1.0f - f;
        this.particleScale = this.portalParticleScale * f;
        super.renderParticle(worldRendererIn, entityIn, partialTicks, p_180434_4_, p_180434_5_, p_180434_6_, p_180434_7_, p_180434_8_);
    }
    
    @Override
    public int getBrightnessForRender(final float partialTicks) {
        final int i = super.getBrightnessForRender(partialTicks);
        float f = this.particleAge / (float)this.particleMaxAge;
        f *= f;
        f *= f;
        final int j = i & 0xFF;
        int k = i >> 16 & 0xFF;
        k += (int)(f * 15.0f * 16.0f);
        if (k > 240) {
            k = 240;
        }
        return j | k << 16;
    }
    
    @Override
    public float getBrightness(final float partialTicks) {
        final float f = super.getBrightness(partialTicks);
        float f2 = this.particleAge / (float)this.particleMaxAge;
        f2 *= f2 * f2 * f2;
        return f * (1.0f - f2) + f2;
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        float f = this.particleAge / (float)this.particleMaxAge;
        f = -f + f * f * 2.0f;
        f = 1.0f - f;
        this.posX = this.portalPosX + this.motionX * f;
        this.posY = this.portalPosY + this.motionY * f + (1.0f - f);
        this.posZ = this.portalPosZ + this.motionZ * f;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
        }
    }
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public EntityFX getEntityFX(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new EntityPortalFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
