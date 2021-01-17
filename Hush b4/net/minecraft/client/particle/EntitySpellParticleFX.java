// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.world.World;
import java.util.Random;

public class EntitySpellParticleFX extends EntityFX
{
    private static final Random RANDOM;
    private int baseSpellTextureIndex;
    
    static {
        RANDOM = new Random();
    }
    
    protected EntitySpellParticleFX(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double p_i1229_8_, final double p_i1229_10_, final double p_i1229_12_) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.5 - EntitySpellParticleFX.RANDOM.nextDouble(), p_i1229_10_, 0.5 - EntitySpellParticleFX.RANDOM.nextDouble());
        this.baseSpellTextureIndex = 128;
        this.motionY *= 0.20000000298023224;
        if (p_i1229_8_ == 0.0 && p_i1229_12_ == 0.0) {
            this.motionX *= 0.10000000149011612;
            this.motionZ *= 0.10000000149011612;
        }
        this.particleScale *= 0.75f;
        this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));
        this.noClip = false;
    }
    
    @Override
    public void renderParticle(final WorldRenderer worldRendererIn, final Entity entityIn, final float partialTicks, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
        float f = (this.particleAge + partialTicks) / this.particleMaxAge * 32.0f;
        f = MathHelper.clamp_float(f, 0.0f, 1.0f);
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
        this.setParticleTextureIndex(this.baseSpellTextureIndex + (7 - this.particleAge * 8 / this.particleMaxAge));
        this.motionY += 0.004;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        if (this.posY == this.prevPosY) {
            this.motionX *= 1.1;
            this.motionZ *= 1.1;
        }
        this.motionX *= 0.9599999785423279;
        this.motionY *= 0.9599999785423279;
        this.motionZ *= 0.9599999785423279;
        if (this.onGround) {
            this.motionX *= 0.699999988079071;
            this.motionZ *= 0.699999988079071;
        }
    }
    
    public void setBaseSpellTextureIndex(final int baseSpellTextureIndexIn) {
        this.baseSpellTextureIndex = baseSpellTextureIndexIn;
    }
    
    public static class AmbientMobFactory implements IParticleFactory
    {
        @Override
        public EntityFX getEntityFX(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            final EntityFX entityfx = new EntitySpellParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            entityfx.setAlphaF(0.15f);
            entityfx.setRBGColorF((float)xSpeedIn, (float)ySpeedIn, (float)zSpeedIn);
            return entityfx;
        }
    }
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public EntityFX getEntityFX(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new EntitySpellParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
    
    public static class InstantFactory implements IParticleFactory
    {
        @Override
        public EntityFX getEntityFX(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            final EntityFX entityfx = new EntitySpellParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            ((EntitySpellParticleFX)entityfx).setBaseSpellTextureIndex(144);
            return entityfx;
        }
    }
    
    public static class MobFactory implements IParticleFactory
    {
        @Override
        public EntityFX getEntityFX(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            final EntityFX entityfx = new EntitySpellParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            entityfx.setRBGColorF((float)xSpeedIn, (float)ySpeedIn, (float)zSpeedIn);
            return entityfx;
        }
    }
    
    public static class WitchFactory implements IParticleFactory
    {
        @Override
        public EntityFX getEntityFX(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            final EntityFX entityfx = new EntitySpellParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            ((EntitySpellParticleFX)entityfx).setBaseSpellTextureIndex(144);
            final float f = worldIn.rand.nextFloat() * 0.5f + 0.35f;
            entityfx.setRBGColorF(1.0f * f, 0.0f * f, 1.0f * f);
            return entityfx;
        }
    }
}
