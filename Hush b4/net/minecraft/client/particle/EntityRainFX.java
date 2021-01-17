// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.MathHelper;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockLiquid;
import net.minecraft.world.IBlockAccess;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityRainFX extends EntityFX
{
    protected EntityRainFX(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0, 0.0, 0.0);
        this.motionX *= 0.30000001192092896;
        this.motionY = Math.random() * 0.20000000298023224 + 0.10000000149011612;
        this.motionZ *= 0.30000001192092896;
        this.particleRed = 1.0f;
        this.particleGreen = 1.0f;
        this.particleBlue = 1.0f;
        this.setParticleTextureIndex(19 + this.rand.nextInt(4));
        this.setSize(0.01f, 0.01f);
        this.particleGravity = 0.06f;
        this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));
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
        if (this.particleMaxAge-- <= 0) {
            this.setDead();
        }
        if (this.onGround) {
            if (Math.random() < 0.5) {
                this.setDead();
            }
            this.motionX *= 0.699999988079071;
            this.motionZ *= 0.699999988079071;
        }
        final BlockPos blockpos = new BlockPos(this);
        final IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
        final Block block = iblockstate.getBlock();
        block.setBlockBoundsBasedOnState(this.worldObj, blockpos);
        final Material material = iblockstate.getBlock().getMaterial();
        if (material.isLiquid() || material.isSolid()) {
            double d0 = 0.0;
            if (iblockstate.getBlock() instanceof BlockLiquid) {
                d0 = 1.0f - BlockLiquid.getLiquidHeightPercent(iblockstate.getValue((IProperty<Integer>)BlockLiquid.LEVEL));
            }
            else {
                d0 = block.getBlockBoundsMaxY();
            }
            final double d2 = MathHelper.floor_double(this.posY) + d0;
            if (this.posY < d2) {
                this.setDead();
            }
        }
    }
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public EntityFX getEntityFX(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new EntityRainFX(worldIn, xCoordIn, yCoordIn, zCoordIn);
        }
    }
}
