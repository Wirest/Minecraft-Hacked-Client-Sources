package net.minecraft.item;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemFirework extends Item {
    private static final String __OBFID = "CL_00000031";

    /**
     * Called when a Block is right-clicked with this Item
     *
     * @param pos  The block being right-clicked
     * @param side The side being right-clicked
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            EntityFireworkRocket var9 = new EntityFireworkRocket(worldIn, (double) ((float) pos.getX() + hitX), (double) ((float) pos.getY() + hitY), (double) ((float) pos.getZ() + hitZ), stack);
            worldIn.spawnEntityInWorld(var9);

            if (!playerIn.capabilities.isCreativeMode) {
                --stack.stackSize;
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     *
     * @param tooltip  All lines to display in the Item's tooltip. This is a List of Strings.
     * @param advanced Whether the setting "Advanced tooltips" is enabled
     */
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
        if (stack.hasTagCompound()) {
            NBTTagCompound var5 = stack.getTagCompound().getCompoundTag("Fireworks");

            if (var5 != null) {
                if (var5.hasKey("Flight", 99)) {
                    tooltip.add(StatCollector.translateToLocal("item.fireworks.flight") + " " + var5.getByte("Flight"));
                }

                NBTTagList var6 = var5.getTagList("Explosions", 10);

                if (var6 != null && var6.tagCount() > 0) {
                    for (int var7 = 0; var7 < var6.tagCount(); ++var7) {
                        NBTTagCompound var8 = var6.getCompoundTagAt(var7);
                        ArrayList var9 = Lists.newArrayList();
                        ItemFireworkCharge.func_150902_a(var8, var9);

                        if (var9.size() > 0) {
                            for (int var10 = 1; var10 < var9.size(); ++var10) {
                                var9.set(var10, "  " + (String) var9.get(var10));
                            }

                            tooltip.addAll(var9);
                        }
                    }
                }
            }
        }
    }
}
