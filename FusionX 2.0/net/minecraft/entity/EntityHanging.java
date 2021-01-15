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

public abstract class EntityHanging extends Entity
{
    private int tickCounter1;
    protected BlockPos field_174861_a;
    public EnumFacing field_174860_b;
    private static final String __OBFID = "CL_00001546";

    public EntityHanging(World worldIn)
    {
        super(worldIn);
        this.setSize(0.5F, 0.5F);
    }

    public EntityHanging(World worldIn, BlockPos p_i45853_2_)
    {
        this(worldIn);
        this.field_174861_a = p_i45853_2_;
    }

    protected void entityInit() {}

    protected void func_174859_a(EnumFacing p_174859_1_)
    {
        Validate.notNull(p_174859_1_);
        Validate.isTrue(p_174859_1_.getAxis().isHorizontal());
        this.field_174860_b = p_174859_1_;
        this.prevRotationYaw = this.rotationYaw = (float)(this.field_174860_b.getHorizontalIndex() * 90);
        this.func_174856_o();
    }

    private void func_174856_o()
    {
        if (this.field_174860_b != null)
        {
            double var1 = (double)this.field_174861_a.getX() + 0.5D;
            double var3 = (double)this.field_174861_a.getY() + 0.5D;
            double var5 = (double)this.field_174861_a.getZ() + 0.5D;
            double var7 = 0.46875D;
            double var9 = this.func_174858_a(this.getWidthPixels());
            double var11 = this.func_174858_a(this.getHeightPixels());
            var1 -= (double)this.field_174860_b.getFrontOffsetX() * 0.46875D;
            var5 -= (double)this.field_174860_b.getFrontOffsetZ() * 0.46875D;
            var3 += var11;
            EnumFacing var13 = this.field_174860_b.rotateYCCW();
            var1 += var9 * (double)var13.getFrontOffsetX();
            var5 += var9 * (double)var13.getFrontOffsetZ();
            this.posX = var1;
            this.posY = var3;
            this.posZ = var5;
            double var14 = (double)this.getWidthPixels();
            double var16 = (double)this.getHeightPixels();
            double var18 = (double)this.getWidthPixels();

            if (this.field_174860_b.getAxis() == EnumFacing.Axis.Z)
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
            this.func_174826_a(new AxisAlignedBB(var1 - var14, var3 - var16, var5 - var18, var1 + var14, var3 + var16, var5 + var18));
        }
    }

    private double func_174858_a(int p_174858_1_)
    {
        return p_174858_1_ % 32 == 0 ? 0.5D : 0.0D;
    }

    /**
     * Called to update the entity's position/logic.
     */
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
            BlockPos var3 = this.field_174861_a.offset(this.field_174860_b.getOpposite());
            EnumFacing var4 = this.field_174860_b.rotateYCCW();

            for (int var5 = 0; var5 < var1; ++var5)
            {
                for (int var6 = 0; var6 < var2; ++var6)
                {
                    BlockPos var7 = var3.offset(var4, var5).offsetUp(var6);
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
    public boolean canBeCollidedWith()
    {
        return true;
    }

    /**
     * Called when a player attacks an entity. If this returns true the attack will not happen.
     */
    public boolean hitByEntity(Entity entityIn)
    {
        return entityIn instanceof EntityPlayer ? this.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)entityIn), 0.0F) : false;
    }

    public EnumFacing func_174811_aO()
    {
        return this.field_174860_b;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.func_180431_b(source))
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
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setByte("Facing", (byte)this.field_174860_b.getHorizontalIndex());
        tagCompound.setInteger("TileX", this.func_174857_n().getX());
        tagCompound.setInteger("TileY", this.func_174857_n().getY());
        tagCompound.setInteger("TileZ", this.func_174857_n().getZ());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        this.field_174861_a = new BlockPos(tagCompund.getInteger("TileX"), tagCompund.getInteger("TileY"), tagCompund.getInteger("TileZ"));
        EnumFacing var2;

        if (tagCompund.hasKey("Direction", 99))
        {
            var2 = EnumFacing.getHorizontal(tagCompund.getByte("Direction"));
            this.field_174861_a = this.field_174861_a.offset(var2);
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

    protected boolean shouldSetPosAfterLoading()
    {
        return false;
    }

    /**
     * Sets the x,y,z of the entity from the given parameters. Also seems to set up a bounding box.
     */
    public void setPosition(double x, double y, double z)
    {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        BlockPos var7 = this.field_174861_a;
        this.field_174861_a = new BlockPos(x, y, z);

        if (!this.field_174861_a.equals(var7))
        {
            this.func_174856_o();
            this.isAirBorne = true;
        }
    }

    public BlockPos func_174857_n()
    {
        return this.field_174861_a;
    }
}
