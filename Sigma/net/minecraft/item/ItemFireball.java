package net.minecraft.item;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemFireball extends Item {
    private static final String __OBFID = "CL_00000029";

    public ItemFireball() {
        setCreativeTab(CreativeTabs.tabMisc);
    }

    /**
     * Called when a Block is right-clicked with this Item
     *
     * @param pos  The block being right-clicked
     * @param side The side being right-clicked
     */
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        } else {
            pos = pos.offset(side);

            if (!playerIn.func_175151_a(pos, side, stack)) {
                return false;
            } else {
                if (worldIn.getBlockState(pos).getBlock().getMaterial() == Material.air) {
                    worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "item.fireCharge.use", 1.0F, (Item.itemRand.nextFloat() - Item.itemRand.nextFloat()) * 0.2F + 1.0F);
                    worldIn.setBlockState(pos, Blocks.fire.getDefaultState());
                }

                if (!playerIn.capabilities.isCreativeMode) {
                    --stack.stackSize;
                }

                return true;
            }
        }
    }
}
