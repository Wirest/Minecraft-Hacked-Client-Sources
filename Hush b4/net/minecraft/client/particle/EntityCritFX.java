// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.world.World;

public class EntityCritFX extends EntitySmokeFX
{
    protected EntityCritFX(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double p_i1201_8_, final double p_i1201_10_, final double p_i1201_12_) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, p_i1201_8_, p_i1201_10_, p_i1201_12_, 2.5f);
    }
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public EntityFX getEntityFX(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new EntityCritFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
