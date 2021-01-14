package net.minecraft.item;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.world.World;

public class ItemFishFood extends ItemFood {
    /**
     * Indicates whether this fish is "cooked" or not.
     */
    private final boolean cooked;
    private static final String __OBFID = "CL_00000032";

    public ItemFishFood(boolean p_i45338_1_) {
        super(0, 0.0F, false);
        this.cooked = p_i45338_1_;
    }

    public int getHealAmount(ItemStack itemStackIn) {
        ItemFishFood.FishType var2 = ItemFishFood.FishType.getFishTypeForItemStack(itemStackIn);
        return this.cooked && var2.getCookable() ? var2.getCookedHealAmount() : var2.getUncookedHealAmount();
    }

    public float getSaturationModifier(ItemStack itemStackIn) {
        ItemFishFood.FishType var2 = ItemFishFood.FishType.getFishTypeForItemStack(itemStackIn);
        return this.cooked && var2.getCookable() ? var2.getCookedSaturationModifier() : var2.getUncookedSaturationModifier();
    }

    public String getPotionEffect(ItemStack stack) {
        return ItemFishFood.FishType.getFishTypeForItemStack(stack) == ItemFishFood.FishType.PUFFERFISH ? PotionHelper.field_151423_m : null;
    }

    protected void onFoodEaten(ItemStack p_77849_1_, World worldIn, EntityPlayer p_77849_3_) {
        ItemFishFood.FishType var4 = ItemFishFood.FishType.getFishTypeForItemStack(p_77849_1_);

        if (var4 == ItemFishFood.FishType.PUFFERFISH) {
            p_77849_3_.addPotionEffect(new PotionEffect(Potion.poison.id, 1200, 3));
            p_77849_3_.addPotionEffect(new PotionEffect(Potion.hunger.id, 300, 2));
            p_77849_3_.addPotionEffect(new PotionEffect(Potion.confusion.id, 300, 1));
        }

        super.onFoodEaten(p_77849_1_, worldIn, p_77849_3_);
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     *
     * @param subItems The List of sub-items. This is a List of ItemStacks.
     */
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
        ItemFishFood.FishType[] var4 = ItemFishFood.FishType.values();
        int var5 = var4.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            ItemFishFood.FishType var7 = var4[var6];

            if (!this.cooked || var7.getCookable()) {
                subItems.add(new ItemStack(this, 1, var7.getItemDamage()));
            }
        }
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack stack) {
        ItemFishFood.FishType var2 = ItemFishFood.FishType.getFishTypeForItemStack(stack);
        return this.getUnlocalizedName() + "." + var2.getUnlocalizedNamePart() + "." + (this.cooked && var2.getCookable() ? "cooked" : "raw");
    }

    public static enum FishType {
        COD("COD", 0, 0, "cod", 2, 0.1F, 5, 0.6F),
        SALMON("SALMON", 1, 1, "salmon", 2, 0.1F, 6, 0.8F),
        CLOWNFISH("CLOWNFISH", 2, 2, "clownfish", 1, 0.1F),
        PUFFERFISH("PUFFERFISH", 3, 3, "pufferfish", 1, 0.1F);
        private static final Map itemDamageToFishTypeMap = Maps.newHashMap();
        private final int itemDamage;
        private final String unlocalizedNamePart;
        private final int uncookedHealAmount;
        private final float uncookedSaturationModifier;
        private final int cookedHealAmount;
        private final float cookedSaturationModifier;
        private boolean cookable = false;

        private static final ItemFishFood.FishType[] $VALUES = new ItemFishFood.FishType[]{COD, SALMON, CLOWNFISH, PUFFERFISH};
        private static final String __OBFID = "CL_00000033";

        private FishType(String p_i45336_1_, int p_i45336_2_, int p_i45336_3_, String p_i45336_4_, int p_i45336_5_, float p_i45336_6_, int p_i45336_7_, float p_i45336_8_) {
            this.itemDamage = p_i45336_3_;
            this.unlocalizedNamePart = p_i45336_4_;
            this.uncookedHealAmount = p_i45336_5_;
            this.uncookedSaturationModifier = p_i45336_6_;
            this.cookedHealAmount = p_i45336_7_;
            this.cookedSaturationModifier = p_i45336_8_;
            this.cookable = true;
        }

        private FishType(String p_i45337_1_, int p_i45337_2_, int p_i45337_3_, String p_i45337_4_, int p_i45337_5_, float p_i45337_6_) {
            this.itemDamage = p_i45337_3_;
            this.unlocalizedNamePart = p_i45337_4_;
            this.uncookedHealAmount = p_i45337_5_;
            this.uncookedSaturationModifier = p_i45337_6_;
            this.cookedHealAmount = 0;
            this.cookedSaturationModifier = 0.0F;
            this.cookable = false;
        }

        public int getItemDamage() {
            return this.itemDamage;
        }

        public String getUnlocalizedNamePart() {
            return this.unlocalizedNamePart;
        }

        public int getUncookedHealAmount() {
            return this.uncookedHealAmount;
        }

        public float getUncookedSaturationModifier() {
            return this.uncookedSaturationModifier;
        }

        public int getCookedHealAmount() {
            return this.cookedHealAmount;
        }

        public float getCookedSaturationModifier() {
            return this.cookedSaturationModifier;
        }

        public boolean getCookable() {
            return this.cookable;
        }

        public static ItemFishFood.FishType getFishTypeForItemDamage(int p_150974_0_) {
            ItemFishFood.FishType var1 = (ItemFishFood.FishType) itemDamageToFishTypeMap.get(Integer.valueOf(p_150974_0_));
            return var1 == null ? COD : var1;
        }

        public static ItemFishFood.FishType getFishTypeForItemStack(ItemStack p_150978_0_) {
            return p_150978_0_.getItem() instanceof ItemFishFood ? getFishTypeForItemDamage(p_150978_0_.getMetadata()) : COD;
        }

        static {
            ItemFishFood.FishType[] var0 = values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2) {
                ItemFishFood.FishType var3 = var0[var2];
                itemDamageToFishTypeMap.put(Integer.valueOf(var3.getItemDamage()), var3);
            }
        }
    }
}
