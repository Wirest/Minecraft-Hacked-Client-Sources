package net.minecraft.item;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.world.World;

public class ItemFishFood extends ItemFood
{
    /** Indicates whether this fish is "cooked" or not. */
    private final boolean cooked;

    public ItemFishFood(boolean cooked)
    {
        super(0, 0.0F, false);
        this.cooked = cooked;
    }

    @Override
	public int getHealAmount(ItemStack stack)
    {
        ItemFishFood.FishType var2 = ItemFishFood.FishType.byItemStack(stack);
        return this.cooked && var2.canCook() ? var2.getCookedHealAmount() : var2.getUncookedHealAmount();
    }

    @Override
	public float getSaturationModifier(ItemStack stack)
    {
        ItemFishFood.FishType var2 = ItemFishFood.FishType.byItemStack(stack);
        return this.cooked && var2.canCook() ? var2.getCookedSaturationModifier() : var2.getUncookedSaturationModifier();
    }

    @Override
	public String getPotionEffect(ItemStack stack)
    {
        return ItemFishFood.FishType.byItemStack(stack) == ItemFishFood.FishType.PUFFERFISH ? PotionHelper.pufferfishEffect : null;
    }

    @Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
    {
        ItemFishFood.FishType var4 = ItemFishFood.FishType.byItemStack(stack);

        if (var4 == ItemFishFood.FishType.PUFFERFISH)
        {
            player.addPotionEffect(new PotionEffect(Potion.poison.id, 1200, 3));
            player.addPotionEffect(new PotionEffect(Potion.hunger.id, 300, 2));
            player.addPotionEffect(new PotionEffect(Potion.confusion.id, 300, 1));
        }

        super.onFoodEaten(stack, worldIn, player);
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     *  
     * @param subItems The List of sub-items. This is a List of ItemStacks.
     */
    @Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
    {
        ItemFishFood.FishType[] var4 = ItemFishFood.FishType.values();
        int var5 = var4.length;

        for (int var6 = 0; var6 < var5; ++var6)
        {
            ItemFishFood.FishType var7 = var4[var6];

            if (!this.cooked || var7.canCook())
            {
                subItems.add(new ItemStack(this, 1, var7.getMetadata()));
            }
        }
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    @Override
	public String getUnlocalizedName(ItemStack stack)
    {
        ItemFishFood.FishType var2 = ItemFishFood.FishType.byItemStack(stack);
        return this.getUnlocalizedName() + "." + var2.getUnlocalizedName() + "." + (this.cooked && var2.canCook() ? "cooked" : "raw");
    }

    public static enum FishType
    {
        COD("COD", 0, 0, "cod", 2, 0.1F, 5, 0.6F),
        SALMON("SALMON", 1, 1, "salmon", 2, 0.1F, 6, 0.8F),
        CLOWNFISH("CLOWNFISH", 2, 2, "clownfish", 1, 0.1F),
        PUFFERFISH("PUFFERFISH", 3, 3, "pufferfish", 1, 0.1F);
        private static final Map META_LOOKUP = Maps.newHashMap();
        private final int meta;
        private final String unlocalizedName;
        private final int uncookedHealAmount;
        private final float uncookedSaturationModifier;
        private final int cookedHealAmount;
        private final float cookedSaturationModifier;
        private boolean cookable = false; 

        private FishType(String p_i45336_1_, int p_i45336_2_, int meta, String unlocalizedName, int uncookedHeal, float uncookedSaturation, int cookedHeal, float cookedSaturation)
        {
            this.meta = meta;
            this.unlocalizedName = unlocalizedName;
            this.uncookedHealAmount = uncookedHeal;
            this.uncookedSaturationModifier = uncookedSaturation;
            this.cookedHealAmount = cookedHeal;
            this.cookedSaturationModifier = cookedSaturation;
            this.cookable = true;
        }

        private FishType(String p_i45337_1_, int p_i45337_2_, int meta, String unlocalizedName, int uncookedHeal, float uncookedSaturation)
        {
            this.meta = meta;
            this.unlocalizedName = unlocalizedName;
            this.uncookedHealAmount = uncookedHeal;
            this.uncookedSaturationModifier = uncookedSaturation;
            this.cookedHealAmount = 0;
            this.cookedSaturationModifier = 0.0F;
            this.cookable = false;
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public String getUnlocalizedName()
        {
            return this.unlocalizedName;
        }

        public int getUncookedHealAmount()
        {
            return this.uncookedHealAmount;
        }

        public float getUncookedSaturationModifier()
        {
            return this.uncookedSaturationModifier;
        }

        public int getCookedHealAmount()
        {
            return this.cookedHealAmount;
        }

        public float getCookedSaturationModifier()
        {
            return this.cookedSaturationModifier;
        }

        public boolean canCook()
        {
            return this.cookable;
        }

        public static ItemFishFood.FishType byMetadata(int meta)
        {
            ItemFishFood.FishType var1 = (ItemFishFood.FishType)META_LOOKUP.get(Integer.valueOf(meta));
            return var1 == null ? COD : var1;
        }

        public static ItemFishFood.FishType byItemStack(ItemStack stack)
        {
            return stack.getItem() instanceof ItemFishFood ? byMetadata(stack.getMetadata()) : COD;
        }

        static {
            ItemFishFood.FishType[] var0 = values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2)
            {
                ItemFishFood.FishType var3 = var0[var2];
                META_LOOKUP.put(Integer.valueOf(var3.getMetadata()), var3);
            }
        }
    }
}
