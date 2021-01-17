// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.util.AxisAlignedBB;
import org.apache.commons.lang3.Validate;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;

public abstract class EntityHanging extends Entity
{
    private int tickCounter1;
    protected BlockPos hangingPosition;
    public EnumFacing facingDirection;
    
    public EntityHanging(final World worldIn) {
        super(worldIn);
        this.setSize(0.5f, 0.5f);
    }
    
    public EntityHanging(final World worldIn, final BlockPos hangingPositionIn) {
        this(worldIn);
        this.hangingPosition = hangingPositionIn;
    }
    
    @Override
    protected void entityInit() {
    }
    
    protected void updateFacingWithBoundingBox(final EnumFacing facingDirectionIn) {
        Validate.notNull(facingDirectionIn);
        Validate.isTrue(facingDirectionIn.getAxis().isHorizontal());
        this.facingDirection = facingDirectionIn;
        final float n = (float)(this.facingDirection.getHorizontalIndex() * 90);
        this.rotationYaw = n;
        this.prevRotationYaw = n;
        this.updateBoundingBox();
    }
    
    private void updateBoundingBox() {
        if (this.facingDirection != null) {
            double d0 = this.hangingPosition.getX() + 0.5;
            double d2 = this.hangingPosition.getY() + 0.5;
            double d3 = this.hangingPosition.getZ() + 0.5;
            final double d4 = 0.46875;
            final double d5 = this.func_174858_a(this.getWidthPixels());
            final double d6 = this.func_174858_a(this.getHeightPixels());
            d0 -= this.facingDirection.getFrontOffsetX() * 0.46875;
            d3 -= this.facingDirection.getFrontOffsetZ() * 0.46875;
            d2 += d6;
            final EnumFacing enumfacing = this.facingDirection.rotateYCCW();
            d0 += d5 * enumfacing.getFrontOffsetX();
            d3 += d5 * enumfacing.getFrontOffsetZ();
            this.posX = d0;
            this.posY = d2;
            this.posZ = d3;
            double d7 = this.getWidthPixels();
            double d8 = this.getHeightPixels();
            double d9 = this.getWidthPixels();
            if (this.facingDirection.getAxis() == EnumFacing.Axis.Z) {
                d9 = 1.0;
            }
            else {
                d7 = 1.0;
            }
            d7 /= 32.0;
            d8 /= 32.0;
            d9 /= 32.0;
            this.setEntityBoundingBox(new AxisAlignedBB(d0 - d7, d2 - d8, d3 - d9, d0 + d7, d2 + d8, d3 + d9));
        }
    }
    
    private double func_174858_a(final int p_174858_1_) {
        return (p_174858_1_ % 32 == 0) ? 0.5 : 0.0;
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.tickCounter1++ == 100 && !this.worldObj.isRemote) {
            this.tickCounter1 = 0;
            if (!this.isDead && !this.onValidSurface()) {
                this.setDead();
                this.onBroken(null);
            }
        }
    }
    
    public boolean onValidSurface() {
        if (!this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty()) {
            return false;
        }
        final int i = Math.max(1, this.getWidthPixels() / 16);
        final int j = Math.max(1, this.getHeightPixels() / 16);
        final BlockPos blockpos = this.hangingPosition.offset(this.facingDirection.getOpposite());
        final EnumFacing enumfacing = this.facingDirection.rotateYCCW();
        for (int k = 0; k < i; ++k) {
            for (int l = 0; l < j; ++l) {
                final BlockPos blockpos2 = blockpos.offset(enumfacing, k).up(l);
                final Block block = this.worldObj.getBlockState(blockpos2).getBlock();
                if (!block.getMaterial().isSolid() && !BlockRedstoneDiode.isRedstoneRepeaterBlockID(block)) {
                    return false;
                }
            }
        }
        for (final Entity entity : this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox())) {
            if (entity instanceof EntityHanging) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
    
    @Override
    public boolean hitByEntity(final Entity entityIn) {
        return entityIn instanceof EntityPlayer && this.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)entityIn), 0.0f);
    }
    
    @Override
    public EnumFacing getHorizontalFacing() {
        return this.facingDirection;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (!this.isDead && !this.worldObj.isRemote) {
            this.setDead();
            this.setBeenAttacked();
            this.onBroken(source.getEntity());
        }
        return true;
    }
    
    @Override
    public void moveEntity(final double x, final double y, final double z) {
        if (!this.worldObj.isRemote && !this.isDead && x * x + y * y + z * z > 0.0) {
            this.setDead();
            this.onBroken(null);
        }
    }
    
    @Override
    public void addVelocity(final double x, final double y, final double z) {
        if (!this.worldObj.isRemote && !this.isDead && x * x + y * y + z * z > 0.0) {
            this.setDead();
            this.onBroken(null);
        }
    }
    
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        tagCompound.setByte("Facing", (byte)this.facingDirection.getHorizontalIndex());
        tagCompound.setInteger("TileX", this.getHangingPosition().getX());
        tagCompound.setInteger("TileY", this.getHangingPosition().getY());
        tagCompound.setInteger("TileZ", this.getHangingPosition().getZ());
    }
    
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        this.hangingPosition = new BlockPos(tagCompund.getInteger("TileX"), tagCompund.getInteger("TileY"), tagCompund.getInteger("TileZ"));
        EnumFacing enumfacing;
        if (tagCompund.hasKey("Direction", 99)) {
            enumfacing = EnumFacing.getHorizontal(tagCompund.getByte("Direction"));
            this.hangingPosition = this.hangingPosition.offset(enumfacing);
        }
        else if (tagCompund.hasKey("Facing", 99)) {
            enumfacing = EnumFacing.getHorizontal(tagCompund.getByte("Facing"));
        }
        else {
            enumfacing = EnumFacing.getHorizontal(tagCompund.getByte("Dir"));
        }
        this.updateFacingWithBoundingBox(enumfacing);
    }
    
    public abstract int getWidthPixels();
    
    public abstract int getHeightPixels();
    
    public abstract void onBroken(final Entity p0);
    
    @Override
    protected boolean shouldSetPosAfterLoading() {
        return false;
    }
    
    @Override
    public void setPosition(final double x, final double y, final double z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        final BlockPos blockpos = this.hangingPosition;
        this.hangingPosition = new BlockPos(x, y, z);
        if (!this.hangingPosition.equals(blockpos)) {
            this.updateBoundingBox();
            this.isAirBorne = true;
        }
    }
    
    public BlockPos getHangingPosition() {
        return this.hangingPosition;
    }
}
