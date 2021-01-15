package net.minecraft.entity;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.BlockFence;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class EntityLeashKnot extends EntityHanging
{

    public EntityLeashKnot(World worldIn)
    {
        super(worldIn);
    }

    public EntityLeashKnot(World worldIn, BlockPos hangingPositionIn)
    {
        super(worldIn, hangingPositionIn);
        this.setPosition(hangingPositionIn.getX() + 0.5D, hangingPositionIn.getY() + 0.5D, hangingPositionIn.getZ() + 0.5D);
        this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.1875D, this.posY - 0.25D + 0.125D, this.posZ - 0.1875D, this.posX + 0.1875D, this.posY + 0.25D + 0.125D, this.posZ + 0.1875D));
    }

    @Override
	protected void entityInit()
    {
        super.entityInit();
    }

    @Override
	public void func_174859_a(EnumFacing facingDirectionIn) {}

    @Override
	public int getWidthPixels()
    {
        return 9;
    }

    @Override
	public int getHeightPixels()
    {
        return 9;
    }

    @Override
	public float getEyeHeight()
    {
        return -0.0625F;
    }

    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight Args: distance
     */
    @Override
	public boolean isInRangeToRenderDist(double distance)
    {
        return distance < 1024.0D;
    }

    /**
     * Called when this entity is broken. Entity parameter may be null.
     */
    @Override
	public void onBroken(Entity brokenEntity) {}

    /**
     * Either write this entity to the NBT tag given and return true, or return false without doing anything. If this
     * returns false the entity is not saved on disk. Ridden entities return false here as they are saved with their
     * rider.
     */
    @Override
	public boolean writeToNBTOptional(NBTTagCompound tagCompund)
    {
        return false;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
	public void writeEntityToNBT(NBTTagCompound tagCompound) {}

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
	public void readEntityFromNBT(NBTTagCompound tagCompund) {}

    /**
     * First layer of player interaction
     */
    @Override
	public boolean interactFirst(EntityPlayer playerIn)
    {
        ItemStack var2 = playerIn.getHeldItem();
        boolean var3 = false;
        double var4;
        List var6;
        Iterator var7;
        EntityLiving var8;

        if (var2 != null && var2.getItem() == Items.lead && !this.worldObj.isRemote)
        {
            var4 = 7.0D;
            var6 = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(this.posX - var4, this.posY - var4, this.posZ - var4, this.posX + var4, this.posY + var4, this.posZ + var4));
            var7 = var6.iterator();

            while (var7.hasNext())
            {
                var8 = (EntityLiving)var7.next();

                if (var8.getLeashed() && var8.getLeashedToEntity() == playerIn)
                {
                    var8.setLeashedToEntity(this, true);
                    var3 = true;
                }
            }
        }

        if (!this.worldObj.isRemote && !var3)
        {
            this.setDead();

            if (playerIn.capabilities.isCreativeMode)
            {
                var4 = 7.0D;
                var6 = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(this.posX - var4, this.posY - var4, this.posZ - var4, this.posX + var4, this.posY + var4, this.posZ + var4));
                var7 = var6.iterator();

                while (var7.hasNext())
                {
                    var8 = (EntityLiving)var7.next();

                    if (var8.getLeashed() && var8.getLeashedToEntity() == this)
                    {
                        var8.clearLeashed(true, false);
                    }
                }
            }
        }

        return true;
    }

    /**
     * checks to make sure painting can be placed there
     */
    @Override
	public boolean onValidSurface()
    {
        return this.worldObj.getBlockState(this.hangingPosition).getBlock() instanceof BlockFence;
    }

    public static EntityLeashKnot createKnot(World worldIn, BlockPos fence)
    {
        EntityLeashKnot var2 = new EntityLeashKnot(worldIn, fence);
        var2.forceSpawn = true;
        worldIn.spawnEntityInWorld(var2);
        return var2;
    }

    public static EntityLeashKnot getKnotForPosition(World worldIn, BlockPos pos)
    {
        int var2 = pos.getX();
        int var3 = pos.getY();
        int var4 = pos.getZ();
        List var5 = worldIn.getEntitiesWithinAABB(EntityLeashKnot.class, new AxisAlignedBB(var2 - 1.0D, var3 - 1.0D, var4 - 1.0D, var2 + 1.0D, var3 + 1.0D, var4 + 1.0D));
        Iterator var6 = var5.iterator();
        EntityLeashKnot var7;

        do
        {
            if (!var6.hasNext())
            {
                return null;
            }

            var7 = (EntityLeashKnot)var6.next();
        }
        while (!var7.getHangingPosition().equals(pos));

        return var7;
    }
}
