// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.util.MathHelper;
import net.minecraft.stats.StatList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.BlockFence;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityList;
import net.minecraft.util.StatCollector;
import net.minecraft.creativetab.CreativeTabs;

public class ItemMonsterPlacer extends Item
{
    public ItemMonsterPlacer() {
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabs.tabMisc);
    }
    
    @Override
    public String getItemStackDisplayName(final ItemStack stack) {
        String s = new StringBuilder().append(StatCollector.translateToLocal(String.valueOf(this.getUnlocalizedName()) + ".name")).toString().trim();
        final String s2 = EntityList.getStringFromID(stack.getMetadata());
        if (s2 != null) {
            s = String.valueOf(s) + " " + StatCollector.translateToLocal("entity." + s2 + ".name");
        }
        return s;
    }
    
    @Override
    public int getColorFromItemStack(final ItemStack stack, final int renderPass) {
        final EntityList.EntityEggInfo entitylist$entityegginfo = EntityList.entityEggs.get(stack.getMetadata());
        return (entitylist$entityegginfo != null) ? ((renderPass == 0) ? entitylist$entityegginfo.primaryColor : entitylist$entityegginfo.secondaryColor) : 16777215;
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        if (!playerIn.canPlayerEdit(pos.offset(side), side, stack)) {
            return false;
        }
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getBlock() == Blocks.mob_spawner) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntityMobSpawner) {
                final MobSpawnerBaseLogic mobspawnerbaselogic = ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic();
                mobspawnerbaselogic.setEntityName(EntityList.getStringFromID(stack.getMetadata()));
                tileentity.markDirty();
                worldIn.markBlockForUpdate(pos);
                if (!playerIn.capabilities.isCreativeMode) {
                    --stack.stackSize;
                }
                return true;
            }
        }
        pos = pos.offset(side);
        double d0 = 0.0;
        if (side == EnumFacing.UP && iblockstate instanceof BlockFence) {
            d0 = 0.5;
        }
        final Entity entity = spawnCreature(worldIn, stack.getMetadata(), pos.getX() + 0.5, pos.getY() + d0, pos.getZ() + 0.5);
        if (entity != null) {
            if (entity instanceof EntityLivingBase && stack.hasDisplayName()) {
                entity.setCustomNameTag(stack.getDisplayName());
            }
            if (!playerIn.capabilities.isCreativeMode) {
                --stack.stackSize;
            }
        }
        return true;
    }
    
    @Override
    public ItemStack onItemRightClick(final ItemStack itemStackIn, final World worldIn, final EntityPlayer playerIn) {
        if (worldIn.isRemote) {
            return itemStackIn;
        }
        final MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(worldIn, playerIn, true);
        if (movingobjectposition == null) {
            return itemStackIn;
        }
        if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            final BlockPos blockpos = movingobjectposition.getBlockPos();
            if (!worldIn.isBlockModifiable(playerIn, blockpos)) {
                return itemStackIn;
            }
            if (!playerIn.canPlayerEdit(blockpos, movingobjectposition.sideHit, itemStackIn)) {
                return itemStackIn;
            }
            if (worldIn.getBlockState(blockpos).getBlock() instanceof BlockLiquid) {
                final Entity entity = spawnCreature(worldIn, itemStackIn.getMetadata(), blockpos.getX() + 0.5, blockpos.getY() + 0.5, blockpos.getZ() + 0.5);
                if (entity != null) {
                    if (entity instanceof EntityLivingBase && itemStackIn.hasDisplayName()) {
                        ((EntityLiving)entity).setCustomNameTag(itemStackIn.getDisplayName());
                    }
                    if (!playerIn.capabilities.isCreativeMode) {
                        --itemStackIn.stackSize;
                    }
                    playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
                }
            }
        }
        return itemStackIn;
    }
    
    public static Entity spawnCreature(final World worldIn, final int entityID, final double x, final double y, final double z) {
        if (!EntityList.entityEggs.containsKey(entityID)) {
            return null;
        }
        Entity entity = null;
        for (int i = 0; i < 1; ++i) {
            entity = EntityList.createEntityByID(entityID, worldIn);
            if (entity instanceof EntityLivingBase) {
                final EntityLiving entityliving = (EntityLiving)entity;
                entity.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(worldIn.rand.nextFloat() * 360.0f), 0.0f);
                entityliving.rotationYawHead = entityliving.rotationYaw;
                entityliving.renderYawOffset = entityliving.rotationYaw;
                entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), null);
                worldIn.spawnEntityInWorld(entity);
                entityliving.playLivingSound();
            }
        }
        return entity;
    }
    
    @Override
    public void getSubItems(final Item itemIn, final CreativeTabs tab, final List<ItemStack> subItems) {
        for (final EntityList.EntityEggInfo entitylist$entityegginfo : EntityList.entityEggs.values()) {
            subItems.add(new ItemStack(itemIn, 1, entitylist$entityegginfo.spawnedID));
        }
    }
}
