package net.minecraft.item;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemFishFood extends ItemFood {
   private final boolean cooked;

   public ItemFishFood(boolean cooked) {
      super(0, 0.0F, false);
      this.cooked = cooked;
   }

   public int getHealAmount(ItemStack stack) {
      ItemFishFood.FishType itemfishfood$fishtype = ItemFishFood.FishType.byItemStack(stack);
      return this.cooked && itemfishfood$fishtype.canCook() ? itemfishfood$fishtype.getCookedHealAmount() : itemfishfood$fishtype.getUncookedHealAmount();
   }

   public float getSaturationModifier(ItemStack stack) {
      ItemFishFood.FishType itemfishfood$fishtype = ItemFishFood.FishType.byItemStack(stack);
      return this.cooked && itemfishfood$fishtype.canCook() ? itemfishfood$fishtype.getCookedSaturationModifier() : itemfishfood$fishtype.getUncookedSaturationModifier();
   }

   public String getPotionEffect(ItemStack stack) {
      return ItemFishFood.FishType.byItemStack(stack) == ItemFishFood.FishType.PUFFERFISH ? "+0-1+2+3+13&4-4" : null;
   }

   protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
      ItemFishFood.FishType itemfishfood$fishtype = ItemFishFood.FishType.byItemStack(stack);
      if (itemfishfood$fishtype == ItemFishFood.FishType.PUFFERFISH) {
         player.addPotionEffect(new PotionEffect(Potion.poison.id, 1200, 3));
         player.addPotionEffect(new PotionEffect(Potion.hunger.id, 300, 2));
         player.addPotionEffect(new PotionEffect(Potion.confusion.id, 300, 1));
      }

      super.onFoodEaten(stack, worldIn, player);
   }

   public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
      ItemFishFood.FishType[] var4 = ItemFishFood.FishType.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         ItemFishFood.FishType itemfishfood$fishtype = var4[var6];
         if (!this.cooked || itemfishfood$fishtype.canCook()) {
            subItems.add(new ItemStack(this, 1, itemfishfood$fishtype.getMetadata()));
         }
      }

   }

   public String getUnlocalizedName(ItemStack stack) {
      ItemFishFood.FishType itemfishfood$fishtype = ItemFishFood.FishType.byItemStack(stack);
      return this.getUnlocalizedName() + "." + itemfishfood$fishtype.getUnlocalizedName() + "." + (this.cooked && itemfishfood$fishtype.canCook() ? "cooked" : "raw");
   }

   public static enum FishType {
      COD(0, "cod", 2, 0.1F, 5, 0.6F),
      SALMON(1, "salmon", 2, 0.1F, 6, 0.8F),
      CLOWNFISH(2, "clownfish", 1, 0.1F),
      PUFFERFISH(3, "pufferfish", 1, 0.1F);

      private static final Map META_LOOKUP = Maps.newHashMap();
      private final int meta;
      private final String unlocalizedName;
      private final int uncookedHealAmount;
      private final float uncookedSaturationModifier;
      private final int cookedHealAmount;
      private final float cookedSaturationModifier;
      private boolean cookable = false;

      private FishType(int meta, String unlocalizedName, int uncookedHeal, float uncookedSaturation, int cookedHeal, float cookedSaturation) {
         this.meta = meta;
         this.unlocalizedName = unlocalizedName;
         this.uncookedHealAmount = uncookedHeal;
         this.uncookedSaturationModifier = uncookedSaturation;
         this.cookedHealAmount = cookedHeal;
         this.cookedSaturationModifier = cookedSaturation;
         this.cookable = true;
      }

      private FishType(int meta, String unlocalizedName, int uncookedHeal, float uncookedSaturation) {
         this.meta = meta;
         this.unlocalizedName = unlocalizedName;
         this.uncookedHealAmount = uncookedHeal;
         this.uncookedSaturationModifier = uncookedSaturation;
         this.cookedHealAmount = 0;
         this.cookedSaturationModifier = 0.0F;
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

      public static ItemFishFood.FishType byMetadata(int meta) {
         ItemFishFood.FishType itemfishfood$fishtype = (ItemFishFood.FishType)META_LOOKUP.get(meta);
         return itemfishfood$fishtype == null ? COD : itemfishfood$fishtype;
      }

      public static ItemFishFood.FishType byItemStack(ItemStack stack) {
         return stack.getItem() instanceof ItemFishFood ? byMetadata(stack.getMetadata()) : COD;
      }

      static {
         ItemFishFood.FishType[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            ItemFishFood.FishType itemfishfood$fishtype = var0[var2];
            META_LOOKUP.put(itemfishfood$fishtype.getMetadata(), itemfishfood$fishtype);
         }

      }
   }
}
