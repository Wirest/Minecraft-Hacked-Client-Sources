// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.world.World;

public class EntityFishWakeFX extends EntityFX
{
    protected EntityFishWakeFX(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double p_i45073_8_, final double p_i45073_10_, final double p_i45073_12_) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0, 0.0, 0.0);
        this.motionX *= 0.30000001192092896;
        this.motionY = Math.random() * 0.20000000298023224 + 0.10000000149011612;
        this.motionZ *= 0.30000001192092896;
        this.particleRed = 1.0f;
        this.particleGreen = 1.0f;
        this.particleBlue = 1.0f;
        this.setParticleTextureIndex(19);
        this.setSize(0.01f, 0.01f);
        this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));
        this.particleGravity = 0.0f;
        this.motionX = p_i45073_8_;
        this.motionY = p_i45073_10_;
        this.motionZ = p_i45073_12_;
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= this.particleGravity;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863;
        this.motionY *= 0.9800000190734863;
        this.motionZ *= 0.9800000190734863;
        final int i = 60 - this.particleMaxAge;
        final float f = i * 0.001f;
        this.setSize(f, f);
        this.setParticleTextureIndex(19 + i % 4);
        if (this.particleMaxAge-- <= 0) {
            this.setDead();
        }
    }
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public EntityFX getEntityFX(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new EntityFishWakeFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
