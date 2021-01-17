// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.world.World;

public class EntityEnchantmentTableParticleFX extends EntityFX
{
    private float field_70565_a;
    private double coordX;
    private double coordY;
    private double coordZ;
    
    protected EntityEnchantmentTableParticleFX(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.motionX = xSpeedIn;
        this.motionY = ySpeedIn;
        this.motionZ = zSpeedIn;
        this.coordX = xCoordIn;
        this.coordY = yCoordIn;
        this.coordZ = zCoordIn;
        final double n = xCoordIn + xSpeedIn;
        this.prevPosX = n;
        this.posX = n;
        final double n2 = yCoordIn + ySpeedIn;
        this.prevPosY = n2;
        this.posY = n2;
        final double n3 = zCoordIn + zSpeedIn;
        this.prevPosZ = n3;
        this.posZ = n3;
        final float f = this.rand.nextFloat() * 0.6f + 0.4f;
        final float n4 = this.rand.nextFloat() * 0.5f + 0.2f;
        this.particleScale = n4;
        this.field_70565_a = n4;
        final float particleRed = 1.0f * f;
        this.particleBlue = particleRed;
        this.particleGreen = particleRed;
        this.particleRed = particleRed;
        this.particleGreen *= 0.9f;
        this.particleRed *= 0.9f;
        this.particleMaxAge = (int)(Math.random() * 10.0) + 30;
        this.noClip = true;
        this.setParticleTextureIndex((int)(Math.random() * 26.0 + 1.0 + 224.0));
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
        f2 *= f2;
        f2 *= f2;
        return f * (1.0f - f2) + f2;
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        float f = this.particleAge / (float)this.particleMaxAge;
        f = 1.0f - f;
        float f2 = 1.0f - f;
        f2 *= f2;
        f2 *= f2;
        this.posX = this.coordX + this.motionX * f;
        this.posY = this.coordY + this.motionY * f - f2 * 1.2f;
        this.posZ = this.coordZ + this.motionZ * f;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
        }
    }
    
    public static class EnchantmentTable implements IParticleFactory
    {
        @Override
        public EntityFX getEntityFX(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new EntityEnchantmentTableParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
