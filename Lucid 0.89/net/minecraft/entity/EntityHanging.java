package net.minecraft.entity;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.Validate;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract class EntityHanging extends Entity
{
    private int tickCounter1;
    protected BlockPos hangingPosition;
    public EnumFacing facingDirection;

    public EntityHanging(World worldIn)
    {
        super(worldIn);
        this.setSize(0.5F, 0.5F);
    }

    public EntityHanging(World worldIn, BlockPos hangingPositionIn)
    {
        this(worldIn);
        this.hangingPosition = hangingPositionIn;
    }

    @Override
	protected void entityInit() {}

    protected void func_174859_a(EnumFacing facingDirectionIn)
    {
        Validate.notNull(facingDirectionIn);
        Validate.isTrue(facingDirectionIn.getAxis().isHorizontal());
        this.facingDirection = facingDirectionIn;
        this.prevRotationYaw = this.rotationYaw = this.facingDirection.getHorizontalIndex() * 90;
        this.func_174856_o();
    }

    private void func_174856_o()
    {
        if (this.facingDirection != null)
        {
            double var1 = this.hangingPosition.getX() + 0.5D;
            double var3 = this.hangingPosition.getY() + 0.5D;
            double var5 = this.hangingPosition.getZ() + 0.5D;
            double var9 = this.func_174858_a(this.getWidthPixels());
            double var11 = this.func_174858_a(this.getHeightPixels());
            var1 -= this.facingDirection.getFrontOffsetX() * 0.46875D;
            var5 -= this.facingDirection.getFrontOffsetZ() * 0.46875D;
            var3 += var11;
            EnumFacing var13 = this.facingDirection.rotateYCCW();
            var1 += var9 * var13.getFrontOffsetX();
            var5 += var9 * var13.getFrontOffsetZ();
            this.posX = var1;
            this.posY = var3;
            this.posZ = var5;
            double var14 = this.getWidthPixels();
            double var16 = this.getHeightPixels();
            double var18 = this.getWidthPixels();

            if (this.facingDirection.getAxis() == EnumFacing.Axis.Z)
            {
                var18 = 1.0D;
            }
            else
            {
                var14 = 1.0D;
            }

            var14 /= 32.0D;
            var16 /= 32.0D;
            var18 /= 32.0D;
            this.setEntityBoundingBox(new AxisAlignedBB(var1 - var14, var3 - var16, var5 - var18, var1 + var14, var3 + var16, var5 + var18));
        }
    }

    private double func_174858_a(int p_174858_1_)
    {
        return p_174858_1_ % 32 == 0 ? 0.5D : 0.0D;
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
	public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.tickCounter1++ == 100 && !this.worldObj.isRemote)
        {
            this.tickCounter1 = 0;

            if (!this.isDead && !this.onValidSurface())
            {
                this.setDead();
                this.onBroken((Entity)null);
            }
        }
    }

    /**
     * checks to make sure painting can be placed there
     */
    public boolean onValidSurface()
    {
        if (!this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty())
        {
            return false;
        }
        else
        {
            int var1 = Math.max(1, this.getWidthPixels() / 16);
            int var2 = Math.max(1, this.getHeightPixels() / 16);
            BlockPos var3 = this.hangingPosition.offset(this.facingDirection.getOpposite());
            EnumFacing var4 = this.facingDirection.rotateYCCW();

            for (int var5 = 0; var5 < var1; ++var5)
            {
                for (int var6 = 0; var6 < var2; ++var6)
                {
                    BlockPos var7 = var3.offset(var4, var5).up(var6);
                    Block var8 = this.worldObj.getBlockState(var7).getBlock();

                    if (!var8.getMaterial().isSolid() && !BlockRedstoneDiode.isRedstoneRepeaterBlockID(var8))
                    {
                        return false;
                    }
                }
            }

            List var9 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox());
            Iterator var10 = var9.iterator();
            Entity var11;

            do
            {
                if (!var10.hasNext())
                {
                    return true;
                }

                var11 = (Entity)var10.next();
            }
            while (!(var11 instanceof EntityHanging));

            return false;
        }
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    @Override
	public boolean canBeCollidedWith()
    {
        return true;
    }

    /**
     * Called when a player attacks an entity. If this returns true the attack will not happen.
     */
    @Override
	public boolean hitByEntity(Entity entityIn)
    {
        return entityIn instanceof EntityPlayer ? this.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)entityIn), 0.0F) : false;
    }

    @Override
	public EnumFacing getHorizontalFacing()
    {
        return this.facingDirection;
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
	public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable(source))
        {
            return false;
        }
        else
        {
            if (!this.isDead && !this.worldObj.isRemote)
            {
                this.setDead();
                this.setBeenAttacked();
                this.onBroken(source.getEntity());
            }

            return true;
        }
    }

    /**
     * Tries to moves the entity by the passed in displacement. Args: x, y, z
     */
    @Override
	public void moveEntity(double x, double y, double z)
    {
        if (!this.worldObj.isRemote && !this.isDead && x * x + y * y + z * z > 0.0D)
        {
            this.setDead();
            this.onBroken((Entity)null);
        }
    }

    /**
     * Adds to the current velocity of the entity. Args: x, y, z
     */
    @Override
	public void addVelocity(double x, double y, double z)
    {
        if (!this.worldObj.isRemote && !this.isDead && x * x + y * y + z * z > 0.0D)
        {
            this.setDead();
            this.onBroken((Entity)null);
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
	public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setByte("Facing", (byte)this.facingDirection.getHorizontalIndex());
        tagCompound.setInteger("TileX", this.getHangingPosition().getX());
        tagCompound.setInteger("TileY", this.getHangingPosition().getY());
        tagCompound.setInteger("TileZ", this.getHangingPosition().getZ());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
	public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        this.hangingPosition = new BlockPos(tagCompund.getInteger("TileX"), tagCompund.getInteger("TileY"), tagCompund.getInteger("TileZ"));
        EnumFacing var2;

        if (tagCompund.hasKey("Direction", 99))
        {
            var2 = EnumFacing.getHorizontal(tagCompund.getByte("Direction"));
            this.hangingPosition = this.hangingPosition.offset(var2);
        }
        else if (tagCompund.hasKey("Facing", 99))
        {
            var2 = EnumFacing.getHorizontal(tagCompund.getByte("Facing"));
        }
        else
        {
            var2 = EnumFacing.getHorizontal(tagCompund.getByte("Dir"));
        }

        this.func_174859_a(var2);
    }

    public abstract int getWidthPixels();

    public abstract int getHeightPixels();

    /**
     * Called when this entity is broken. Entity parameter may be null.
     */
    public abstract void onBroken(Entity var1);

    @Override
	protected boolean shouldSetPosAfterLoading()
    {
        return false;
    }

    /**
     * Sets the x,y,z of the entity from the given parameters. Also seems to set up a bounding box.
     */
    @Override
	public void setPosition(double x, double y, double z)
    {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        BlockPos var7 = this.hangingPosition;
        this.hangingPosition = new BlockPos(x, y, z);

        if (!this.hangingPosition.equals(var7))
        {
            this.func_174856_o();
            this.isAirBorne = true;
        }
    }

    public BlockPos getHangingPosition()
    {
        return this.hangingPosition;
    }
}
