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

public class EntityLeashKnot extends EntityHanging {
    private static final String __OBFID = "CL_00001548";

    public EntityLeashKnot(World worldIn) {
        super(worldIn);
    }

    public EntityLeashKnot(World worldIn, BlockPos p_i45851_2_) {
        super(worldIn, p_i45851_2_);
        this.setPosition((double) p_i45851_2_.getX() + 0.5D, (double) p_i45851_2_.getY() + 0.5D, (double) p_i45851_2_.getZ() + 0.5D);
        float var3 = 0.125F;
        float var4 = 0.1875F;
        float var5 = 0.25F;
        this.func_174826_a(new AxisAlignedBB(this.posX - 0.1875D, this.posY - 0.25D + 0.125D, this.posZ - 0.1875D, this.posX + 0.1875D, this.posY + 0.25D + 0.125D, this.posZ + 0.1875D));
    }

    protected void entityInit() {
        super.entityInit();
    }

    public void func_174859_a(EnumFacing p_174859_1_) {
    }

    public int getWidthPixels() {
        return 9;
    }

    public int getHeightPixels() {
        return 9;
    }

    public float getEyeHeight() {
        return -0.0625F;
    }

    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight Args: distance
     */
    public boolean isInRangeToRenderDist(double distance) {
        return distance < 1024.0D;
    }

    /**
     * Called when this entity is broken. Entity parameter may be null.
     */
    public void onBroken(Entity p_110128_1_) {
    }

    /**
     * Either write this entity to the NBT tag given and return true, or return false without doing anything. If this
     * returns false the entity is not saved on disk. Ridden entities return false here as they are saved with their
     * rider.
     */
    public boolean writeToNBTOptional(NBTTagCompound tagCompund) {
        return false;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
    }

    /**
     * First layer of player interaction
     */
    public boolean interactFirst(EntityPlayer playerIn) {
        ItemStack var2 = playerIn.getHeldItem();
        boolean var3 = false;
        double var4;
        List var6;
        Iterator var7;
        EntityLiving var8;

        if (var2 != null && var2.getItem() == Items.lead && !this.worldObj.isRemote) {
            var4 = 7.0D;
            var6 = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(this.posX - var4, this.posY - var4, this.posZ - var4, this.posX + var4, this.posY + var4, this.posZ + var4));
            var7 = var6.iterator();

            while (var7.hasNext()) {
                var8 = (EntityLiving) var7.next();

                if (var8.getLeashed() && var8.getLeashedToEntity() == playerIn) {
                    var8.setLeashedToEntity(this, true);
                    var3 = true;
                }
            }
        }

        if (!this.worldObj.isRemote && !var3) {
            this.setDead();

            if (playerIn.capabilities.isCreativeMode) {
                var4 = 7.0D;
                var6 = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(this.posX - var4, this.posY - var4, this.posZ - var4, this.posX + var4, this.posY + var4, this.posZ + var4));
                var7 = var6.iterator();

                while (var7.hasNext()) {
                    var8 = (EntityLiving) var7.next();

                    if (var8.getLeashed() && var8.getLeashedToEntity() == this) {
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
    public boolean onValidSurface() {
        return this.worldObj.getBlockState(this.field_174861_a).getBlock() instanceof BlockFence;
    }

    public static EntityLeashKnot func_174862_a(World worldIn, BlockPos p_174862_1_) {
        EntityLeashKnot var2 = new EntityLeashKnot(worldIn, p_174862_1_);
        var2.forceSpawn = true;
        worldIn.spawnEntityInWorld(var2);
        return var2;
    }

    public static EntityLeashKnot func_174863_b(World worldIn, BlockPos p_174863_1_) {
        int var2 = p_174863_1_.getX();
        int var3 = p_174863_1_.getY();
        int var4 = p_174863_1_.getZ();
        List var5 = worldIn.getEntitiesWithinAABB(EntityLeashKnot.class, new AxisAlignedBB((double) var2 - 1.0D, (double) var3 - 1.0D, (double) var4 - 1.0D, (double) var2 + 1.0D, (double) var3 + 1.0D, (double) var4 + 1.0D));
        Iterator var6 = var5.iterator();
        EntityLeashKnot var7;

        do {
            if (!var6.hasNext()) {
                return null;
            }

            var7 = (EntityLeashKnot) var6.next();
        }
        while (!var7.func_174857_n().equals(p_174863_1_));

        return var7;
    }
}
