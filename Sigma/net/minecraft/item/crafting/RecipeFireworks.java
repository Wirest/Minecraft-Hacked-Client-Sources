package net.minecraft.item.crafting;

import com.google.common.collect.Lists;

import java.util.ArrayList;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class RecipeFireworks implements IRecipe {
    private ItemStack field_92102_a;
    private static final String __OBFID = "CL_00000083";

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting p_77569_1_, World worldIn) {
        this.field_92102_a = null;
        int var3 = 0;
        int var4 = 0;
        int var5 = 0;
        int var6 = 0;
        int var7 = 0;
        int var8 = 0;

        for (int var9 = 0; var9 < p_77569_1_.getSizeInventory(); ++var9) {
            ItemStack var10 = p_77569_1_.getStackInSlot(var9);

            if (var10 != null) {
                if (var10.getItem() == Items.gunpowder) {
                    ++var4;
                } else if (var10.getItem() == Items.firework_charge) {
                    ++var6;
                } else if (var10.getItem() == Items.dye) {
                    ++var5;
                } else if (var10.getItem() == Items.paper) {
                    ++var3;
                } else if (var10.getItem() == Items.glowstone_dust) {
                    ++var7;
                } else if (var10.getItem() == Items.diamond) {
                    ++var7;
                } else if (var10.getItem() == Items.fire_charge) {
                    ++var8;
                } else if (var10.getItem() == Items.feather) {
                    ++var8;
                } else if (var10.getItem() == Items.gold_nugget) {
                    ++var8;
                } else {
                    if (var10.getItem() != Items.skull) {
                        return false;
                    }

                    ++var8;
                }
            }
        }

        var7 += var5 + var8;

        if (var4 <= 3 && var3 <= 1) {
            NBTTagCompound var16;
            NBTTagCompound var19;

            if (var4 >= 1 && var3 == 1 && var7 == 0) {
                this.field_92102_a = new ItemStack(Items.fireworks);

                if (var6 > 0) {
                    var16 = new NBTTagCompound();
                    var19 = new NBTTagCompound();
                    NBTTagList var23 = new NBTTagList();

                    for (int var24 = 0; var24 < p_77569_1_.getSizeInventory(); ++var24) {
                        ItemStack var26 = p_77569_1_.getStackInSlot(var24);

                        if (var26 != null && var26.getItem() == Items.firework_charge && var26.hasTagCompound() && var26.getTagCompound().hasKey("Explosion", 10)) {
                            var23.appendTag(var26.getTagCompound().getCompoundTag("Explosion"));
                        }
                    }

                    var19.setTag("Explosions", var23);
                    var19.setByte("Flight", (byte) var4);
                    var16.setTag("Fireworks", var19);
                    this.field_92102_a.setTagCompound(var16);
                }

                return true;
            } else if (var4 == 1 && var3 == 0 && var6 == 0 && var5 > 0 && var8 <= 1) {
                this.field_92102_a = new ItemStack(Items.firework_charge);
                var16 = new NBTTagCompound();
                var19 = new NBTTagCompound();
                byte var22 = 0;
                ArrayList var12 = Lists.newArrayList();

                for (int var13 = 0; var13 < p_77569_1_.getSizeInventory(); ++var13) {
                    ItemStack var14 = p_77569_1_.getStackInSlot(var13);

                    if (var14 != null) {
                        if (var14.getItem() == Items.dye) {
                            var12.add(Integer.valueOf(ItemDye.dyeColors[var14.getMetadata() & 15]));
                        } else if (var14.getItem() == Items.glowstone_dust) {
                            var19.setBoolean("Flicker", true);
                        } else if (var14.getItem() == Items.diamond) {
                            var19.setBoolean("Trail", true);
                        } else if (var14.getItem() == Items.fire_charge) {
                            var22 = 1;
                        } else if (var14.getItem() == Items.feather) {
                            var22 = 4;
                        } else if (var14.getItem() == Items.gold_nugget) {
                            var22 = 2;
                        } else if (var14.getItem() == Items.skull) {
                            var22 = 3;
                        }
                    }
                }

                int[] var25 = new int[var12.size()];

                for (int var27 = 0; var27 < var25.length; ++var27) {
                    var25[var27] = ((Integer) var12.get(var27)).intValue();
                }

                var19.setIntArray("Colors", var25);
                var19.setByte("Type", var22);
                var16.setTag("Explosion", var19);
                this.field_92102_a.setTagCompound(var16);
                return true;
            } else if (var4 == 0 && var3 == 0 && var6 == 1 && var5 > 0 && var5 == var7) {
                ArrayList var15 = Lists.newArrayList();

                for (int var17 = 0; var17 < p_77569_1_.getSizeInventory(); ++var17) {
                    ItemStack var11 = p_77569_1_.getStackInSlot(var17);

                    if (var11 != null) {
                        if (var11.getItem() == Items.dye) {
                            var15.add(Integer.valueOf(ItemDye.dyeColors[var11.getMetadata() & 15]));
                        } else if (var11.getItem() == Items.firework_charge) {
                            this.field_92102_a = var11.copy();
                            this.field_92102_a.stackSize = 1;
                        }
                    }
                }

                int[] var18 = new int[var15.size()];

                for (int var20 = 0; var20 < var18.length; ++var20) {
                    var18[var20] = ((Integer) var15.get(var20)).intValue();
                }

                if (this.field_92102_a != null && this.field_92102_a.hasTagCompound()) {
                    NBTTagCompound var21 = this.field_92102_a.getTagCompound().getCompoundTag("Explosion");

                    if (var21 == null) {
                        return false;
                    } else {
                        var21.setIntArray("FadeColors", var18);
                        return true;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
        return this.field_92102_a.copy();
    }

    /**
     * Returns the size of the recipe area
     */
    public int getRecipeSize() {
        return 10;
    }

    public ItemStack getRecipeOutput() {
        return this.field_92102_a;
    }

    public ItemStack[] func_179532_b(InventoryCrafting p_179532_1_) {
        ItemStack[] var2 = new ItemStack[p_179532_1_.getSizeInventory()];

        for (int var3 = 0; var3 < var2.length; ++var3) {
            ItemStack var4 = p_179532_1_.getStackInSlot(var3);

            if (var4 != null && var4.getItem().hasContainerItem()) {
                var2[var3] = new ItemStack(var4.getItem().getContainerItem());
            }
        }

        return var2;
    }
}
