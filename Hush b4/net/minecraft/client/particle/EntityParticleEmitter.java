// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.world.World;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.Entity;

public class EntityParticleEmitter extends EntityFX
{
    private Entity attachedEntity;
    private int age;
    private int lifetime;
    private EnumParticleTypes particleTypes;
    
    public EntityParticleEmitter(final World worldIn, final Entity p_i46279_2_, final EnumParticleTypes particleTypesIn) {
        super(worldIn, p_i46279_2_.posX, p_i46279_2_.getEntityBoundingBox().minY + p_i46279_2_.height / 2.0f, p_i46279_2_.posZ, p_i46279_2_.motionX, p_i46279_2_.motionY, p_i46279_2_.motionZ);
        this.attachedEntity = p_i46279_2_;
        this.lifetime = 3;
        this.particleTypes = particleTypesIn;
        this.onUpdate();
    }
    
    @Override
    public void renderParticle(final WorldRenderer worldRendererIn, final Entity entityIn, final float partialTicks, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
    }
    
    @Override
    public void onUpdate() {
        for (int i = 0; i < 16; ++i) {
            final double d0 = this.rand.nextFloat() * 2.0f - 1.0f;
            final double d2 = this.rand.nextFloat() * 2.0f - 1.0f;
            final double d3 = this.rand.nextFloat() * 2.0f - 1.0f;
            if (d0 * d0 + d2 * d2 + d3 * d3 <= 1.0) {
                final double d4 = this.attachedEntity.posX + d0 * this.attachedEntity.width / 4.0;
                final double d5 = this.attachedEntity.getEntityBoundingBox().minY + this.attachedEntity.height / 2.0f + d2 * this.attachedEntity.height / 4.0;
                final double d6 = this.attachedEntity.posZ + d3 * this.attachedEntity.width / 4.0;
                this.worldObj.spawnParticle(this.particleTypes, false, d4, d5, d6, d0, d2 + 0.2, d3, new int[0]);
            }
        }
        ++this.age;
        if (this.age >= this.lifetime) {
            this.setDead();
        }
    }
    
    @Override
    public int getFXLayer() {
        return 3;
    }
}
