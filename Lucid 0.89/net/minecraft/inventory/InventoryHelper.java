package net.minecraft.inventory;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class InventoryHelper
{
    private static final Random RANDOM = new Random();

    public static void dropInventoryItems(World worldIn, BlockPos pos, IInventory p_180175_2_)
    {
        func_180174_a(worldIn, pos.getX(), pos.getY(), pos.getZ(), p_180175_2_);
    }

    public static void func_180176_a(World worldIn, Entity p_180176_1_, IInventory p_180176_2_)
    {
        func_180174_a(worldIn, p_180176_1_.posX, p_180176_1_.posY, p_180176_1_.posZ, p_180176_2_);
    }

    private static void func_180174_a(World worldIn, double x, double y, double z, IInventory p_180174_7_)
    {
        for (int var8 = 0; var8 < p_180174_7_.getSizeInventory(); ++var8)
        {
            ItemStack var9 = p_180174_7_.getStackInSlot(var8);

            if (var9 != null)
            {
                spawnItemStack(worldIn, x, y, z, var9);
            }
        }
    }

    private static void spawnItemStack(World worldIn, double x, double y, double z, ItemStack stack)
    {
        float var8 = RANDOM.nextFloat() * 0.8F + 0.1F;
        float var9 = RANDOM.nextFloat() * 0.8F + 0.1F;
        float var10 = RANDOM.nextFloat() * 0.8F + 0.1F;

        while (stack.stackSize > 0)
        {
            int var11 = RANDOM.nextInt(21) + 10;

            if (var11 > stack.stackSize)
            {
                var11 = stack.stackSize;
            }

            stack.stackSize -= var11;
            EntityItem var12 = new EntityItem(worldIn, x + var8, y + var9, z + var10, new ItemStack(stack.getItem(), var11, stack.getMetadata()));

            if (stack.hasTagCompound())
            {
                var12.getEntityItem().setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
            }

            float var13 = 0.05F;
            var12.motionX = RANDOM.nextGaussian() * var13;
            var12.motionY = RANDOM.nextGaussian() * var13 + 0.20000000298023224D;
            var12.motionZ = RANDOM.nextGaussian() * var13;
            worldIn.spawnEntityInWorld(var12);
        }
    }
}
