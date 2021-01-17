// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityLavaFX extends EntityFX
{
    private float lavaParticleScale;
    
    protected EntityLavaFX(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0, 0.0, 0.0);
        this.motionX *= 0.800000011920929;
        this.motionY *= 0.800000011920929;
        this.motionZ *= 0.800000011920929;
        this.motionY = this.rand.nextFloat() * 0.4f + 0.05f;
        final float particleRed = 1.0f;
        this.particleBlue = particleRed;
        this.particleGreen = particleRed;
        this.particleRed = particleRed;
        this.particleScale *= this.rand.nextFloat() * 2.0f + 0.2f;
        this.lavaParticleScale = this.particleScale;
        this.particleMaxAge = (int)(16.0 / (Math.random() * 0.8 + 0.2));
        this.noClip = false;
        this.setParticleTextureIndex(49);
    }
    
    @Override
    public int getBrightnessForRender(final float partialTicks) {
        float f = (this.particleAge + partialTicks) / this.particleMaxAge;
        f = MathHelper.clamp_float(f, 0.0f, 1.0f);
        final int i = super.getBrightnessForRender(partialTicks);
        final int j = 240;
        final int k = i >> 16 & 0xFF;
        return j | k << 16;
    }
    
    @Override
    public float getBrightness(final float partialTicks) {
        return 1.0f;
    }
    
    @Override
    public void renderParticle(final WorldRenderer worldRendererIn, final Entity entityIn, final float partialTicks, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
        final float f = (this.particleAge + partialTicks) / this.particleMaxAge;
        this.particleScale = this.lavaParticleScale * (1.0f - f * f);
        super.renderParticle(worldRendererIn, entityIn, partialTicks, p_180434_4_, p_180434_5_, p_180434_6_, p_180434_7_, p_180434_8_);
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
        }
        final float f = this.particleAge / (float)this.particleMaxAge;
        if (this.rand.nextFloat() > f) {
            this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ, new int[0]);
        }
        this.motionY -= 0.03;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9990000128746033;
        this.motionY *= 0.9990000128746033;
        this.motionZ *= 0.9990000128746033;
        if (this.onGround) {
            this.motionX *= 0.699999988079071;
            this.motionZ *= 0.699999988079071;
        }
    }
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public EntityFX getEntityFX(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new EntityLavaFX(worldIn, xCoordIn, yCoordIn, zCoordIn);
        }
    }
}
