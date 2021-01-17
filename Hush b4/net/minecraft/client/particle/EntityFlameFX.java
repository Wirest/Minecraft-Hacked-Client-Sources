// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.world.World;

public class EntityFlameFX extends EntityFX
{
    private float flameScale;
    
    protected EntityFlameFX(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.motionX = this.motionX * 0.009999999776482582 + xSpeedIn;
        this.motionY = this.motionY * 0.009999999776482582 + ySpeedIn;
        this.motionZ = this.motionZ * 0.009999999776482582 + zSpeedIn;
        this.posX += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05f;
        this.posY += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05f;
        this.posZ += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05f;
        this.flameScale = this.particleScale;
        final float particleRed = 1.0f;
        this.particleBlue = particleRed;
        this.particleGreen = particleRed;
        this.particleRed = particleRed;
        this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2)) + 4;
        this.noClip = true;
        this.setParticleTextureIndex(48);
    }
    
    @Override
    public void renderParticle(final WorldRenderer worldRendererIn, final Entity entityIn, final float partialTicks, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
        final float f = (this.particleAge + partialTicks) / this.particleMaxAge;
        this.particleScale = this.flameScale * (1.0f - f * f * 0.5f);
        super.renderParticle(worldRendererIn, entityIn, partialTicks, p_180434_4_, p_180434_5_, p_180434_6_, p_180434_7_, p_180434_8_);
    }
    
    @Override
    public int getBrightnessForRender(final float partialTicks) {
        float f = (this.particleAge + partialTicks) / this.particleMaxAge;
        f = MathHelper.clamp_float(f, 0.0f, 1.0f);
        final int i = super.getBrightnessForRender(partialTicks);
        int j = i & 0xFF;
        final int k = i >> 16 & 0xFF;
        j += (int)(f * 15.0f * 16.0f);
        if (j > 240) {
            j = 240;
        }
        return j | k << 16;
    }
    
    @Override
    public float getBrightness(final float partialTicks) {
        float f = (this.particleAge + partialTicks) / this.particleMaxAge;
        f = MathHelper.clamp_float(f, 0.0f, 1.0f);
        final float f2 = super.getBrightness(partialTicks);
        return f2 * f + (1.0f - f);
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
        }
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9599999785423279;
        this.motionY *= 0.9599999785423279;
        this.motionZ *= 0.9599999785423279;
        if (this.onGround) {
            this.motionX *= 0.699999988079071;
            this.motionZ *= 0.699999988079071;
        }
    }
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public EntityFX getEntityFX(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new EntityFlameFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
