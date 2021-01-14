package net.minecraft.entity;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.apache.commons.lang3.Validate;

public abstract class EntityHanging extends Entity {
    private int tickCounter1;
    protected BlockPos field_174861_a;
    public EnumFacing field_174860_b;
    private static final String __OBFID = "CL_00001546";

    public EntityHanging(World worldIn) {
        super(worldIn);
        setSize(0.5F, 0.5F);
    }

    public EntityHanging(World worldIn, BlockPos p_i45853_2_) {
        this(worldIn);
        field_174861_a = p_i45853_2_;
    }

    @Override
    protected void entityInit() {
    }

    protected void func_174859_a(EnumFacing p_174859_1_) {
        Validate.notNull(p_174859_1_);
        Validate.isTrue(p_174859_1_.getAxis().isHorizontal());
        field_174860_b = p_174859_1_;
        prevRotationYaw = rotationYaw = field_174860_b.getHorizontalIndex() * 90;
        func_174856_o();
    }

    private void func_174856_o() {
        if (field_174860_b != null) {
            double var1 = field_174861_a.getX() + 0.5D;
            double var3 = field_174861_a.getY() + 0.5D;
            double var5 = field_174861_a.getZ() + 0.5D;
            double var7 = 0.46875D;
            double var9 = func_174858_a(getWidthPixels());
            double var11 = func_174858_a(getHeightPixels());
            var1 -= field_174860_b.getFrontOffsetX() * 0.46875D;
            var5 -= field_174860_b.getFrontOffsetZ() * 0.46875D;
            var3 += var11;
            EnumFacing var13 = field_174860_b.rotateYCCW();
            var1 += var9 * var13.getFrontOffsetX();
            var5 += var9 * var13.getFrontOffsetZ();
            posX = var1;
            posY = var3;
            posZ = var5;
            double var14 = getWidthPixels();
            double var16 = getHeightPixels();
            double var18 = getWidthPixels();

            if (field_174860_b.getAxis() == EnumFacing.Axis.Z) {
                var18 = 1.0D;
            } else {
                var14 = 1.0D;
            }

            var14 /= 32.0D;
            var16 /= 32.0D;
            var18 /= 32.0D;
            func_174826_a(new AxisAlignedBB(var1 - var14, var3 - var16, var5 - var18, var1 + var14, var3 + var16, var5 + var18));
        }
    }

    private double func_174858_a(int p_174858_1_) {
        return p_174858_1_ % 32 == 0 ? 0.5D : 0.0D;
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        if (tickCounter1++ == 100 && !worldObj.isRemote) {
            tickCounter1 = 0;

            if (!isDead && !onValidSurface()) {
                setDead();
                onBroken((Entity) null);
            }
        }
    }

    /**
     * checks to make sure painting can be placed there
     */
    public boolean onValidSurface() {
        if (!worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox()).isEmpty()) {
            return false;
        } else {
            int var1 = Math.max(1, getWidthPixels() / 16);
            int var2 = Math.max(1, getHeightPixels() / 16);
            BlockPos var3 = field_174861_a.offset(field_174860_b.getOpposite());
            EnumFacing var4 = field_174860_b.rotateYCCW();

            for (int var5 = 0; var5 < var1; ++var5) {
                for (int var6 = 0; var6 < var2; ++var6) {
                    BlockPos var7 = var3.offset(var4, var5).offsetUp(var6);
                    Block var8 = worldObj.getBlockState(var7).getBlock();

                    if (!var8.getMaterial().isSolid() && !BlockRedstoneDiode.isRedstoneRepeaterBlockID(var8)) {
                        return false;
                    }
                }
            }

            List var9 = worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox());
            Iterator var10 = var9.iterator();
            Entity var11;

            do {
                if (!var10.hasNext()) {
                    return true;
                }

                var11 = (Entity) var10.next();
            } while (!(var11 instanceof EntityHanging));

            return false;
        }
    }

    /**
     * Returns true if other Entities should be prevented from moving through
     * this Entity.
     */
    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    /**
     * Called when a player attacks an entity. If this returns true the attack
     * will not happen.
     */
    @Override
    public boolean hitByEntity(Entity entityIn) {
        return entityIn instanceof EntityPlayer ? attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) entityIn), 0.0F) : false;
    }

    @Override
    public EnumFacing func_174811_aO() {
        return field_174860_b;
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (func_180431_b(source)) {
            return false;
        } else {
            if (!isDead && !worldObj.isRemote) {
                setDead();
                setBeenAttacked();
                onBroken(source.getEntity());
            }

            return true;
        }
    }

    /**
     * Tries to moves the entity by the passed in displacement. Args: x, y, z
     */
    @Override
    public void moveEntity(double x, double y, double z) {
        if (!worldObj.isRemote && !isDead && x * x + y * y + z * z > 0.0D) {
            setDead();
            onBroken((Entity) null);
        }
    }

    /**
     * Adds to the current velocity of the entity. Args: x, y, z
     */
    @Override
    public void addVelocity(double x, double y, double z) {
        if (!worldObj.isRemote && !isDead && x * x + y * y + z * z > 0.0D) {
            setDead();
            onBroken((Entity) null);
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        tagCompound.setByte("Facing", (byte) field_174860_b.getHorizontalIndex());
        tagCompound.setInteger("TileX", func_174857_n().getX());
        tagCompound.setInteger("TileY", func_174857_n().getY());
        tagCompound.setInteger("TileZ", func_174857_n().getZ());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        field_174861_a = new BlockPos(tagCompund.getInteger("TileX"), tagCompund.getInteger("TileY"), tagCompund.getInteger("TileZ"));
        EnumFacing var2;

        if (tagCompund.hasKey("Direction", 99)) {
            var2 = EnumFacing.getHorizontal(tagCompund.getByte("Direction"));
            field_174861_a = field_174861_a.offset(var2);
        } else if (tagCompund.hasKey("Facing", 99)) {
            var2 = EnumFacing.getHorizontal(tagCompund.getByte("Facing"));
        } else {
            var2 = EnumFacing.getHorizontal(tagCompund.getByte("Dir"));
        }

        func_174859_a(var2);
    }

    public abstract int getWidthPixels();

    public abstract int getHeightPixels();

    /**
     * Called when this entity is broken. Entity parameter may be null.
     */
    public abstract void onBroken(Entity p_110128_1_);

    @Override
    protected boolean shouldSetPosAfterLoading() {
        return false;
    }

    /**
     * Sets the x,y,z of the entity from the given parameters. Also seems to set
     * up a bounding box.
     */
    @Override
    public void setPosition(double x, double y, double z) {
        posX = x;
        posY = y;
        posZ = z;
        BlockPos var7 = field_174861_a;
        field_174861_a = new BlockPos(x, y, z);

        if (!field_174861_a.equals(var7)) {
            func_174856_o();
            isAirBorne = true;
        }
    }

    public BlockPos func_174857_n() {
        return field_174861_a;
    }
}
