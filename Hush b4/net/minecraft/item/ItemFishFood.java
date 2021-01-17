// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ItemFishFood extends ItemFood
{
    private final boolean cooked;
    
    public ItemFishFood(final boolean cooked) {
        super(0, 0.0f, false);
        this.cooked = cooked;
    }
    
    @Override
    public int getHealAmount(final ItemStack stack) {
        final FishType itemfishfood$fishtype = FishType.byItemStack(stack);
        return (this.cooked && itemfishfood$fishtype.canCook()) ? itemfishfood$fishtype.getCookedHealAmount() : itemfishfood$fishtype.getUncookedHealAmount();
    }
    
    @Override
    public float getSaturationModifier(final ItemStack stack) {
        final FishType itemfishfood$fishtype = FishType.byItemStack(stack);
        return (this.cooked && itemfishfood$fishtype.canCook()) ? itemfishfood$fishtype.getCookedSaturationModifier() : itemfishfood$fishtype.getUncookedSaturationModifier();
    }
    
    @Override
    public String getPotionEffect(final ItemStack stack) {
        return (FishType.byItemStack(stack) == FishType.PUFFERFISH) ? "+0-1+2+3+13&4-4" : null;
    }
    
    @Override
    protected void onFoodEaten(final ItemStack stack, final World worldIn, final EntityPlayer player) {
        final FishType itemfishfood$fishtype = FishType.byItemStack(stack);
        if (itemfishfood$fishtype == FishType.PUFFERFISH) {
            player.addPotionEffect(new PotionEffect(Potion.poison.id, 1200, 3));
            player.addPotionEffect(new PotionEffect(Potion.hunger.id, 300, 2));
            player.addPotionEffect(new PotionEffect(Potion.confusion.id, 300, 1));
        }
        super.onFoodEaten(stack, worldIn, player);
    }
    
    @Override
    public void getSubItems(final Item itemIn, final CreativeTabs tab, final List<ItemStack> subItems) {
        FishType[] values;
        for (int length = (values = FishType.values()).length, i = 0; i < length; ++i) {
            final FishType itemfishfood$fishtype = values[i];
            if (!this.cooked || itemfishfood$fishtype.canCook()) {
                subItems.add(new ItemStack(this, 1, itemfishfood$fishtype.getMetadata()));
            }
        }
    }
    
    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        final FishType itemfishfood$fishtype = FishType.byItemStack(stack);
        return String.valueOf(this.getUnlocalizedName()) + "." + itemfishfood$fishtype.getUnlocalizedName() + "." + ((this.cooked && itemfishfood$fishtype.canCook()) ? "cooked" : "raw");
    }
    
    public enum FishType
    {
        COD("COD", 0, 0, "cod", 2, 0.1f, 5, 0.6f), 
        SALMON("SALMON", 1, 1, "salmon", 2, 0.1f, 6, 0.8f), 
        CLOWNFISH("CLOWNFISH", 2, 2, "clownfish", 1, 0.1f), 
        PUFFERFISH("PUFFERFISH", 3, 3, "pufferfish", 1, 0.1f);
        
        private static final Map<Integer, FishType> META_LOOKUP;
        private final int meta;
        private final String unlocalizedName;
        private final int uncookedHealAmount;
        private final float uncookedSaturationModifier;
        private final int cookedHealAmount;
        private final float cookedSaturationModifier;
        private boolean cookable;
        
        static {
            META_LOOKUP = Maps.newHashMap();
            FishType[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final FishType itemfishfood$fishtype = values[i];
                FishType.META_LOOKUP.put(itemfishfood$fishtype.getMetadata(), itemfishfood$fishtype);
            }
        }
        
        private FishType(final String name, final int ordinal, final int meta, final String unlocalizedName, final int uncookedHeal, final float uncookedSaturation, final int cookedHeal, final float cookedSaturation) {
            this.cookable = false;
            this.meta = meta;
            this.unlocalizedName = unlocalizedName;
            this.uncookedHealAmount = uncookedHeal;
            this.uncookedSaturationModifier = uncookedSaturation;
            this.cookedHealAmount = cookedHeal;
            this.cookedSaturationModifier = cookedSaturation;
            this.cookable = true;
        }
        
        private FishType(final String name, final int ordinal, final int meta, final String unlocalizedName, final int uncookedHeal, final float uncookedSaturation) {
            this.cookable = false;
            this.meta = meta;
            this.unlocalizedName = unlocalizedName;
            this.uncookedHealAmount = uncookedHeal;
            this.uncookedSaturationModifier = uncookedSaturation;
            this.cookedHealAmount = 0;
            this.cookedSaturationModifier = 0.0f;
            this.cookable = false;
        }
        
        public int getMetadata() {
            return this.meta;
        }
        
        public String getUnlocalizedName() {
            return this.unlocalizedName;
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
        
        public boolean canCook() {
            return this.cookable;
        }
        
        public static FishType byMetadata(final int meta) {
            final FishType itemfishfood$fishtype = FishType.META_LOOKUP.get(meta);
            return (itemfishfood$fishtype == null) ? FishType.COD : itemfishfood$fishtype;
        }
        
        public static FishType byItemStack(final ItemStack stack) {
            return (stack.getItem() instanceof ItemFishFood) ? byMetadata(stack.getMetadata()) : FishType.COD;
        }
    }
}
