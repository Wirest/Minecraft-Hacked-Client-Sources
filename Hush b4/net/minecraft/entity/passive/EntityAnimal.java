// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;

public abstract class EntityAnimal extends EntityAgeable implements IAnimals
{
    protected Block spawnableBlock;
    private int inLove;
    private EntityPlayer playerInLove;
    
    public EntityAnimal(final World worldIn) {
        super(worldIn);
        this.spawnableBlock = Blocks.grass;
    }
    
    @Override
    protected void updateAITasks() {
        if (this.getGrowingAge() != 0) {
            this.inLove = 0;
        }
        super.updateAITasks();
    }
    
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.getGrowingAge() != 0) {
            this.inLove = 0;
        }
        if (this.inLove > 0) {
            --this.inLove;
            if (this.inLove % 10 == 0) {
                final double d0 = this.rand.nextGaussian() * 0.02;
                final double d2 = this.rand.nextGaussian() * 0.02;
                final double d3 = this.rand.nextGaussian() * 0.02;
                this.worldObj.spawnParticle(EnumParticleTypes.HEART, this.posX + this.rand.nextFloat() * this.width * 2.0f - this.width, this.posY + 0.5 + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0f - this.width, d0, d2, d3, new int[0]);
            }
        }
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        this.inLove = 0;
        return super.attackEntityFrom(source, amount);
    }
    
    @Override
    public float getBlockPathWeight(final BlockPos pos) {
        return (this.worldObj.getBlockState(pos.down()).getBlock() == Blocks.grass) ? 10.0f : (this.worldObj.getLightBrightness(pos) - 0.5f);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("InLove", this.inLove);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.inLove = tagCompund.getInteger("InLove");
    }
    
    @Override
    public boolean getCanSpawnHere() {
        final int i = MathHelper.floor_double(this.posX);
        final int j = MathHelper.floor_double(this.getEntityBoundingBox().minY);
        final int k = MathHelper.floor_double(this.posZ);
        final BlockPos blockpos = new BlockPos(i, j, k);
        return this.worldObj.getBlockState(blockpos.down()).getBlock() == this.spawnableBlock && this.worldObj.getLight(blockpos) > 8 && super.getCanSpawnHere();
    }
    
    @Override
    public int getTalkInterval() {
        return 120;
    }
    
    @Override
    protected boolean canDespawn() {
        return false;
    }
    
    @Override
    protected int getExperiencePoints(final EntityPlayer player) {
        return 1 + this.worldObj.rand.nextInt(3);
    }
    
    public boolean isBreedingItem(final ItemStack stack) {
        return stack != null && stack.getItem() == Items.wheat;
    }
    
    @Override
    public boolean interact(final EntityPlayer player) {
        final ItemStack itemstack = player.inventory.getCurrentItem();
        if (itemstack != null) {
            if (this.isBreedingItem(itemstack) && this.getGrowingAge() == 0 && this.inLove <= 0) {
                this.consumeItemFromStack(player, itemstack);
                this.setInLove(player);
                return true;
            }
            if (this.isChild() && this.isBreedingItem(itemstack)) {
                this.consumeItemFromStack(player, itemstack);
                this.func_175501_a((int)(-this.getGrowingAge() / 20 * 0.1f), true);
                return true;
            }
        }
        return super.interact(player);
    }
    
    protected void consumeItemFromStack(final EntityPlayer player, final ItemStack stack) {
        if (!player.capabilities.isCreativeMode) {
            --stack.stackSize;
            if (stack.stackSize <= 0) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
            }
        }
    }
    
    public void setInLove(final EntityPlayer player) {
        this.inLove = 600;
        this.playerInLove = player;
        this.worldObj.setEntityState(this, (byte)18);
    }
    
    public EntityPlayer getPlayerInLove() {
        return this.playerInLove;
    }
    
    public boolean isInLove() {
        return this.inLove > 0;
    }
    
    public void resetInLove() {
        this.inLove = 0;
    }
    
    public boolean canMateWith(final EntityAnimal otherAnimal) {
        return otherAnimal != this && otherAnimal.getClass() == this.getClass() && (this.isInLove() && otherAnimal.isInLove());
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 18) {
            for (int i = 0; i < 7; ++i) {
                final double d0 = this.rand.nextGaussian() * 0.02;
                final double d2 = this.rand.nextGaussian() * 0.02;
                final double d3 = this.rand.nextGaussian() * 0.02;
                this.worldObj.spawnParticle(EnumParticleTypes.HEART, this.posX + this.rand.nextFloat() * this.width * 2.0f - this.width, this.posY + 0.5 + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0f - this.width, d0, d2, d3, new int[0]);
            }
        }
        else {
            super.handleStatusUpdate(id);
        }
    }
}
