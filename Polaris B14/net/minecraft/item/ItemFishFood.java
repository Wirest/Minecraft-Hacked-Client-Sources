/*     */ package net.minecraft.item;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ public class ItemFishFood
/*     */   extends ItemFood
/*     */ {
/*     */   private final boolean cooked;
/*     */   
/*     */   public ItemFishFood(boolean cooked)
/*     */   {
/*  20 */     super(0, 0.0F, false);
/*  21 */     this.cooked = cooked;
/*     */   }
/*     */   
/*     */   public int getHealAmount(ItemStack stack)
/*     */   {
/*  26 */     FishType itemfishfood$fishtype = FishType.byItemStack(stack);
/*  27 */     return (this.cooked) && (itemfishfood$fishtype.canCook()) ? itemfishfood$fishtype.getCookedHealAmount() : itemfishfood$fishtype.getUncookedHealAmount();
/*     */   }
/*     */   
/*     */   public float getSaturationModifier(ItemStack stack)
/*     */   {
/*  32 */     FishType itemfishfood$fishtype = FishType.byItemStack(stack);
/*  33 */     return (this.cooked) && (itemfishfood$fishtype.canCook()) ? itemfishfood$fishtype.getCookedSaturationModifier() : itemfishfood$fishtype.getUncookedSaturationModifier();
/*     */   }
/*     */   
/*     */   public String getPotionEffect(ItemStack stack)
/*     */   {
/*  38 */     return FishType.byItemStack(stack) == FishType.PUFFERFISH ? "+0-1+2+3+13&4-4" : null;
/*     */   }
/*     */   
/*     */   protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
/*     */   {
/*  43 */     FishType itemfishfood$fishtype = FishType.byItemStack(stack);
/*     */     
/*  45 */     if (itemfishfood$fishtype == FishType.PUFFERFISH)
/*     */     {
/*  47 */       player.addPotionEffect(new PotionEffect(Potion.poison.id, 1200, 3));
/*  48 */       player.addPotionEffect(new PotionEffect(Potion.hunger.id, 300, 2));
/*  49 */       player.addPotionEffect(new PotionEffect(Potion.confusion.id, 300, 1));
/*     */     }
/*     */     
/*  52 */     super.onFoodEaten(stack, worldIn, player);
/*     */   }
/*     */   
/*     */ 
/*     */   public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
/*     */   {
/*     */     FishType[] arrayOfFishType;
/*     */     
/*  60 */     int j = (arrayOfFishType = FishType.values()).length; for (int i = 0; i < j; i++) { FishType itemfishfood$fishtype = arrayOfFishType[i];
/*     */       
/*  62 */       if ((!this.cooked) || (itemfishfood$fishtype.canCook()))
/*     */       {
/*  64 */         subItems.add(new ItemStack(this, 1, itemfishfood$fishtype.getMetadata()));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUnlocalizedName(ItemStack stack)
/*     */   {
/*  75 */     FishType itemfishfood$fishtype = FishType.byItemStack(stack);
/*  76 */     return getUnlocalizedName() + "." + itemfishfood$fishtype.getUnlocalizedName() + "." + ((this.cooked) && (itemfishfood$fishtype.canCook()) ? "cooked" : "raw");
/*     */   }
/*     */   
/*     */   public static enum FishType
/*     */   {
/*  81 */     COD(0, "cod", 2, 0.1F, 5, 0.6F), 
/*  82 */     SALMON(1, "salmon", 2, 0.1F, 6, 0.8F), 
/*  83 */     CLOWNFISH(2, "clownfish", 1, 0.1F), 
/*  84 */     PUFFERFISH(3, "pufferfish", 1, 0.1F);
/*     */     
/*     */     private static final Map<Integer, FishType> META_LOOKUP;
/*     */     private final int meta;
/*     */     private final String unlocalizedName;
/*     */     private final int uncookedHealAmount;
/*     */     private final float uncookedSaturationModifier;
/*     */     private final int cookedHealAmount;
/*     */     private final float cookedSaturationModifier;
/*  93 */     private boolean cookable = false;
/*     */     
/*     */     private FishType(int meta, String unlocalizedName, int uncookedHeal, float uncookedSaturation, int cookedHeal, float cookedSaturation)
/*     */     {
/*  97 */       this.meta = meta;
/*  98 */       this.unlocalizedName = unlocalizedName;
/*  99 */       this.uncookedHealAmount = uncookedHeal;
/* 100 */       this.uncookedSaturationModifier = uncookedSaturation;
/* 101 */       this.cookedHealAmount = cookedHeal;
/* 102 */       this.cookedSaturationModifier = cookedSaturation;
/* 103 */       this.cookable = true;
/*     */     }
/*     */     
/*     */     private FishType(int meta, String unlocalizedName, int uncookedHeal, float uncookedSaturation)
/*     */     {
/* 108 */       this.meta = meta;
/* 109 */       this.unlocalizedName = unlocalizedName;
/* 110 */       this.uncookedHealAmount = uncookedHeal;
/* 111 */       this.uncookedSaturationModifier = uncookedSaturation;
/* 112 */       this.cookedHealAmount = 0;
/* 113 */       this.cookedSaturationModifier = 0.0F;
/* 114 */       this.cookable = false;
/*     */     }
/*     */     
/*     */     public int getMetadata()
/*     */     {
/* 119 */       return this.meta;
/*     */     }
/*     */     
/*     */     public String getUnlocalizedName()
/*     */     {
/* 124 */       return this.unlocalizedName;
/*     */     }
/*     */     
/*     */     public int getUncookedHealAmount()
/*     */     {
/* 129 */       return this.uncookedHealAmount;
/*     */     }
/*     */     
/*     */     public float getUncookedSaturationModifier()
/*     */     {
/* 134 */       return this.uncookedSaturationModifier;
/*     */     }
/*     */     
/*     */     public int getCookedHealAmount()
/*     */     {
/* 139 */       return this.cookedHealAmount;
/*     */     }
/*     */     
/*     */     public float getCookedSaturationModifier()
/*     */     {
/* 144 */       return this.cookedSaturationModifier;
/*     */     }
/*     */     
/*     */     public boolean canCook()
/*     */     {
/* 149 */       return this.cookable;
/*     */     }
/*     */     
/*     */     public static FishType byMetadata(int meta)
/*     */     {
/* 154 */       FishType itemfishfood$fishtype = (FishType)META_LOOKUP.get(Integer.valueOf(meta));
/* 155 */       return itemfishfood$fishtype == null ? COD : itemfishfood$fishtype;
/*     */     }
/*     */     
/*     */     public static FishType byItemStack(ItemStack stack)
/*     */     {
/* 160 */       return (stack.getItem() instanceof ItemFishFood) ? byMetadata(stack.getMetadata()) : COD;
/*     */     }
/*     */     
/*     */     static
/*     */     {
/*  86 */       META_LOOKUP = Maps.newHashMap();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       FishType[] arrayOfFishType;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 164 */       int j = (arrayOfFishType = values()).length; for (int i = 0; i < j; i++) { FishType itemfishfood$fishtype = arrayOfFishType[i];
/*     */         
/* 166 */         META_LOOKUP.put(Integer.valueOf(itemfishfood$fishtype.getMetadata()), itemfishfood$fishtype);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemFishFood.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */