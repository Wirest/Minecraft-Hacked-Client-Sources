// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.util.Rotations;
import java.util.Random;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemArmorStand extends Item
{
    public ItemArmorStand() {
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (side == EnumFacing.DOWN) {
            return false;
        }
        final boolean flag = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
        final BlockPos blockpos = flag ? pos : pos.offset(side);
        if (!playerIn.canPlayerEdit(blockpos, side, stack)) {
            return false;
        }
        final BlockPos blockpos2 = blockpos.up();
        boolean flag2 = !worldIn.isAirBlock(blockpos) && !worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos);
        flag2 |= (!worldIn.isAirBlock(blockpos2) && !worldIn.getBlockState(blockpos2).getBlock().isReplaceable(worldIn, blockpos2));
        if (flag2) {
            return false;
        }
        final double d0 = blockpos.getX();
        final double d2 = blockpos.getY();
        final double d3 = blockpos.getZ();
        final List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.fromBounds(d0, d2, d3, d0 + 1.0, d2 + 2.0, d3 + 1.0));
        if (list.size() > 0) {
            return false;
        }
        if (!worldIn.isRemote) {
            worldIn.setBlockToAir(blockpos);
            worldIn.setBlockToAir(blockpos2);
            final EntityArmorStand entityarmorstand = new EntityArmorStand(worldIn, d0 + 0.5, d2, d3 + 0.5);
            final float f = MathHelper.floor_float((MathHelper.wrapAngleTo180_float(playerIn.rotationYaw - 180.0f) + 22.5f) / 45.0f) * 45.0f;
            entityarmorstand.setLocationAndAngles(d0 + 0.5, d2, d3 + 0.5, f, 0.0f);
            this.applyRandomRotations(entityarmorstand, worldIn.rand);
            final NBTTagCompound nbttagcompound = stack.getTagCompound();
            if (nbttagcompound != null && nbttagcompound.hasKey("EntityTag", 10)) {
                final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                entityarmorstand.writeToNBTOptional(nbttagcompound2);
                nbttagcompound2.merge(nbttagcompound.getCompoundTag("EntityTag"));
                entityarmorstand.readFromNBT(nbttagcompound2);
            }
            worldIn.spawnEntityInWorld(entityarmorstand);
        }
        --stack.stackSize;
        return true;
    }
    
    private void applyRandomRotations(final EntityArmorStand armorStand, final Random rand) {
        Rotations rotations = armorStand.getHeadRotation();
        float f = rand.nextFloat() * 5.0f;
        final float f2 = rand.nextFloat() * 20.0f - 10.0f;
        Rotations rotations2 = new Rotations(rotations.getX() + f, rotations.getY() + f2, rotations.getZ());
        armorStand.setHeadRotation(rotations2);
        rotations = armorStand.getBodyRotation();
        f = rand.nextFloat() * 10.0f - 5.0f;
        rotations2 = new Rotations(rotations.getX(), rotations.getY() + f, rotations.getZ());
        armorStand.setBodyRotation(rotations2);
    }
}
