package net.minecraft.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.StatCollector;

public class ItemFireworkCharge extends Item {
    private static final String __OBFID = "CL_00000030";

    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        if (renderPass != 1) {
            return super.getColorFromItemStack(stack, renderPass);
        } else {
            NBTBase var3 = func_150903_a(stack, "Colors");

            if (!(var3 instanceof NBTTagIntArray)) {
                return 9079434;
            } else {
                NBTTagIntArray var4 = (NBTTagIntArray) var3;
                int[] var5 = var4.getIntArray();

                if (var5.length == 1) {
                    return var5[0];
                } else {
                    int var6 = 0;
                    int var7 = 0;
                    int var8 = 0;
                    int[] var9 = var5;
                    int var10 = var5.length;

                    for (int var11 = 0; var11 < var10; ++var11) {
                        int var12 = var9[var11];
                        var6 += (var12 & 16711680) >> 16;
                        var7 += (var12 & 65280) >> 8;
                        var8 += (var12 & 255) >> 0;
                    }

                    var6 /= var5.length;
                    var7 /= var5.length;
                    var8 /= var5.length;
                    return var6 << 16 | var7 << 8 | var8;
                }
            }
        }
    }

    public static NBTBase func_150903_a(ItemStack p_150903_0_, String p_150903_1_) {
        if (p_150903_0_.hasTagCompound()) {
            NBTTagCompound var2 = p_150903_0_.getTagCompound().getCompoundTag("Explosion");

            if (var2 != null) {
                return var2.getTag(p_150903_1_);
            }
        }

        return null;
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     *
     * @param tooltip  All lines to display in the Item's tooltip. This is a List of Strings.
     * @param advanced Whether the setting "Advanced tooltips" is enabled
     */
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
        if (stack.hasTagCompound()) {
            NBTTagCompound var5 = stack.getTagCompound().getCompoundTag("Explosion");

            if (var5 != null) {
                func_150902_a(var5, tooltip);
            }
        }
    }

    public static void func_150902_a(NBTTagCompound p_150902_0_, List p_150902_1_) {
        byte var2 = p_150902_0_.getByte("Type");

        if (var2 >= 0 && var2 <= 4) {
            p_150902_1_.add(StatCollector.translateToLocal("item.fireworksCharge.type." + var2).trim());
        } else {
            p_150902_1_.add(StatCollector.translateToLocal("item.fireworksCharge.type").trim());
        }

        int[] var3 = p_150902_0_.getIntArray("Colors");
        int var8;
        int var9;

        if (var3.length > 0) {
            boolean var4 = true;
            String var5 = "";
            int[] var6 = var3;
            int var7 = var3.length;

            for (var8 = 0; var8 < var7; ++var8) {
                var9 = var6[var8];

                if (!var4) {
                    var5 = var5 + ", ";
                }

                var4 = false;
                boolean var10 = false;

                for (int var11 = 0; var11 < ItemDye.dyeColors.length; ++var11) {
                    if (var9 == ItemDye.dyeColors[var11]) {
                        var10 = true;
                        var5 = var5 + StatCollector.translateToLocal("item.fireworksCharge." + EnumDyeColor.func_176766_a(var11).func_176762_d());
                        break;
                    }
                }

                if (!var10) {
                    var5 = var5 + StatCollector.translateToLocal("item.fireworksCharge.customColor");
                }
            }

            p_150902_1_.add(var5);
        }

        int[] var13 = p_150902_0_.getIntArray("FadeColors");
        boolean var14;

        if (var13.length > 0) {
            var14 = true;
            String var15 = StatCollector.translateToLocal("item.fireworksCharge.fadeTo") + " ";
            int[] var17 = var13;
            var8 = var13.length;

            for (var9 = 0; var9 < var8; ++var9) {
                int var18 = var17[var9];

                if (!var14) {
                    var15 = var15 + ", ";
                }

                var14 = false;
                boolean var19 = false;

                for (int var12 = 0; var12 < 16; ++var12) {
                    if (var18 == ItemDye.dyeColors[var12]) {
                        var19 = true;
                        var15 = var15 + StatCollector.translateToLocal("item.fireworksCharge." + EnumDyeColor.func_176766_a(var12).func_176762_d());
                        break;
                    }
                }

                if (!var19) {
                    var15 = var15 + StatCollector.translateToLocal("item.fireworksCharge.customColor");
                }
            }

            p_150902_1_.add(var15);
        }

        var14 = p_150902_0_.getBoolean("Trail");

        if (var14) {
            p_150902_1_.add(StatCollector.translateToLocal("item.fireworksCharge.trail"));
        }

        boolean var16 = p_150902_0_.getBoolean("Flicker");

        if (var16) {
            p_150902_1_.add(StatCollector.translateToLocal("item.fireworksCharge.flicker"));
        }
    }
}
