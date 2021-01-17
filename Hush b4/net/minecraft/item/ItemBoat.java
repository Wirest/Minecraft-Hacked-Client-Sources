// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.util.BlockPos;
import net.minecraft.util.AxisAlignedBB;
import java.util.List;
import net.minecraft.stats.StatList;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.init.Blocks;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;

public class ItemBoat extends Item
{
    public ItemBoat() {
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabTransport);
    }
    
    @Override
    public ItemStack onItemRightClick(final ItemStack itemStackIn, final World worldIn, final EntityPlayer playerIn) {
        final float f = 1.0f;
        final float f2 = playerIn.prevRotationPitch + (playerIn.rotationPitch - playerIn.prevRotationPitch) * f;
        final float f3 = playerIn.prevRotationYaw + (playerIn.rotationYaw - playerIn.prevRotationYaw) * f;
        final double d0 = playerIn.prevPosX + (playerIn.posX - playerIn.prevPosX) * f;
        final double d2 = playerIn.prevPosY + (playerIn.posY - playerIn.prevPosY) * f + playerIn.getEyeHeight();
        final double d3 = playerIn.prevPosZ + (playerIn.posZ - playerIn.prevPosZ) * f;
        final Vec3 vec3 = new Vec3(d0, d2, d3);
        final float f4 = MathHelper.cos(-f3 * 0.017453292f - 3.1415927f);
        final float f5 = MathHelper.sin(-f3 * 0.017453292f - 3.1415927f);
        final float f6 = -MathHelper.cos(-f2 * 0.017453292f);
        final float f7 = MathHelper.sin(-f2 * 0.017453292f);
        final float f8 = f5 * f6;
        final float f9 = f4 * f6;
        final double d4 = 5.0;
        final Vec3 vec4 = vec3.addVector(f8 * d4, f7 * d4, f9 * d4);
        final MovingObjectPosition movingobjectposition = worldIn.rayTraceBlocks(vec3, vec4, true);
        if (movingobjectposition == null) {
            return itemStackIn;
        }
        final Vec3 vec5 = playerIn.getLook(f);
        boolean flag = false;
        final float f10 = 1.0f;
        final List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(playerIn, playerIn.getEntityBoundingBox().addCoord(vec5.xCoord * d4, vec5.yCoord * d4, vec5.zCoord * d4).expand(f10, f10, f10));
        for (int i = 0; i < list.size(); ++i) {
            final Entity entity = list.get(i);
            if (entity.canBeCollidedWith()) {
                final float f11 = entity.getCollisionBorderSize();
                final AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand(f11, f11, f11);
                if (axisalignedbb.isVecInside(vec3)) {
                    flag = true;
                }
            }
        }
        if (flag) {
            return itemStackIn;
        }
        if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos blockpos = movingobjectposition.getBlockPos();
            if (worldIn.getBlockState(blockpos).getBlock() == Blocks.snow_layer) {
                blockpos = blockpos.down();
            }
            final EntityBoat entityboat = new EntityBoat(worldIn, blockpos.getX() + 0.5f, blockpos.getY() + 1.0f, blockpos.getZ() + 0.5f);
            entityboat.rotationYaw = (float)(((MathHelper.floor_double(playerIn.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3) - 1) * 90);
            if (!worldIn.getCollidingBoundingBoxes(entityboat, entityboat.getEntityBoundingBox().expand(-0.1, -0.1, -0.1)).isEmpty()) {
                return itemStackIn;
            }
            if (!worldIn.isRemote) {
                worldIn.spawnEntityInWorld(entityboat);
            }
            if (!playerIn.capabilities.isCreativeMode) {
                --itemStackIn.stackSize;
            }
            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        }
        return itemStackIn;
    }
}
