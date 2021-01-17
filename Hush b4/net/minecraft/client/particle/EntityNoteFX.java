// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityNoteFX extends EntityFX
{
    float noteParticleScale;
    
    protected EntityNoteFX(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double p_i46353_8_, final double p_i46353_10_, final double p_i46353_12_) {
        this(worldIn, xCoordIn, yCoordIn, zCoordIn, p_i46353_8_, p_i46353_10_, p_i46353_12_, 2.0f);
    }
    
    protected EntityNoteFX(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double p_i1217_8_, final double p_i1217_10_, final double p_i1217_12_, final float p_i1217_14_) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0, 0.0, 0.0);
        this.motionX *= 0.009999999776482582;
        this.motionY *= 0.009999999776482582;
        this.motionZ *= 0.009999999776482582;
        this.motionY += 0.2;
        this.particleRed = MathHelper.sin(((float)p_i1217_8_ + 0.0f) * 3.1415927f * 2.0f) * 0.65f + 0.35f;
        this.particleGreen = MathHelper.sin(((float)p_i1217_8_ + 0.33333334f) * 3.1415927f * 2.0f) * 0.65f + 0.35f;
        this.particleBlue = MathHelper.sin(((float)p_i1217_8_ + 0.6666667f) * 3.1415927f * 2.0f) * 0.65f + 0.35f;
        this.particleScale *= 0.75f;
        this.particleScale *= p_i1217_14_;
        this.noteParticleScale = this.particleScale;
        this.particleMaxAge = 6;
        this.noClip = false;
        this.setParticleTextureIndex(64);
    }
    
    @Override
    public void renderParticle(final WorldRenderer worldRendererIn, final Entity entityIn, final float partialTicks, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
        float f = (this.particleAge + partialTicks) / this.particleMaxAge * 32.0f;
        f = MathHelper.clamp_float(f, 0.0f, 1.0f);
        this.particleScale = this.noteParticleScale * f;
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
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        if (this.posY == this.prevPosY) {
            this.motionX *= 1.1;
            this.motionZ *= 1.1;
        }
        this.motionX *= 0.6600000262260437;
        this.motionY *= 0.6600000262260437;
        this.motionZ *= 0.6600000262260437;
        if (this.onGround) {
            this.motionX *= 0.699999988079071;
            this.motionZ *= 0.699999988079071;
        }
    }
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public EntityFX getEntityFX(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new EntityNoteFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
