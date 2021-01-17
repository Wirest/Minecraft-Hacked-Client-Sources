// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import java.util.Random;

public class InventoryHelper
{
    private static final Random RANDOM;
    
    static {
        RANDOM = new Random();
    }
    
    public static void dropInventoryItems(final World worldIn, final BlockPos pos, final IInventory p_180175_2_) {
        func_180174_a(worldIn, pos.getX(), pos.getY(), pos.getZ(), p_180175_2_);
    }
    
    public static void func_180176_a(final World worldIn, final Entity p_180176_1_, final IInventory p_180176_2_) {
        func_180174_a(worldIn, p_180176_1_.posX, p_180176_1_.posY, p_180176_1_.posZ, p_180176_2_);
    }
    
    private static void func_180174_a(final World worldIn, final double x, final double y, final double z, final IInventory p_180174_7_) {
        for (int i = 0; i < p_180174_7_.getSizeInventory(); ++i) {
            final ItemStack itemstack = p_180174_7_.getStackInSlot(i);
            if (itemstack != null) {
                spawnItemStack(worldIn, x, y, z, itemstack);
            }
        }
    }
    
    private static void spawnItemStack(final World worldIn, final double x, final double y, final double z, final ItemStack stack) {
        final float f = InventoryHelper.RANDOM.nextFloat() * 0.8f + 0.1f;
        final float f2 = InventoryHelper.RANDOM.nextFloat() * 0.8f + 0.1f;
        final float f3 = InventoryHelper.RANDOM.nextFloat() * 0.8f + 0.1f;
        while (stack.stackSize > 0) {
            int i = InventoryHelper.RANDOM.nextInt(21) + 10;
            if (i > stack.stackSize) {
                i = stack.stackSize;
            }
            stack.stackSize -= i;
            final EntityItem entityitem = new EntityItem(worldIn, x + f, y + f2, z + f3, new ItemStack(stack.getItem(), i, stack.getMetadata()));
            if (stack.hasTagCompound()) {
                entityitem.getEntityItem().setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
            }
            final float f4 = 0.05f;
            entityitem.motionX = InventoryHelper.RANDOM.nextGaussian() * f4;
            entityitem.motionY = InventoryHelper.RANDOM.nextGaussian() * f4 + 0.20000000298023224;
            entityitem.motionZ = InventoryHelper.RANDOM.nextGaussian() * f4;
            worldIn.spawnEntityInWorld(entityitem);
        }
    }
}
