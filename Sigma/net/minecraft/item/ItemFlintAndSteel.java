package net.minecraft.item;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemFlintAndSteel extends Item {
    private static final String __OBFID = "CL_00000035";

    public ItemFlintAndSteel() {
        maxStackSize = 1;
        setMaxDamage(64);
        setCreativeTab(CreativeTabs.tabTools);
    }

    /**
     * Called when a Block is right-clicked with this Item
     *
     * @param pos  The block being right-clicked
     * @param side The side being right-clicked
     */
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        pos = pos.offset(side);

        if (!playerIn.func_175151_a(pos, side, stack)) {
            return false;
        } else {
            if (worldIn.getBlockState(pos).getBlock().getMaterial() == Material.air) {
                worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "fire.ignite", 1.0F, Item.itemRand.nextFloat() * 0.4F + 0.8F);
                worldIn.setBlockState(pos, Blocks.fire.getDefaultState());
            }

            stack.damageItem(1, playerIn);
            return true;
        }
    }
}
