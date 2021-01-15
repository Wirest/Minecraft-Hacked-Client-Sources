/*     */ package net.minecraft.village;
/*     */ 
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
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
/*     */ public class MerchantRecipe
/*     */ {
/*     */   private ItemStack itemToBuy;
/*     */   private ItemStack secondItemToBuy;
/*     */   private ItemStack itemToSell;
/*     */   private int toolUses;
/*     */   private int maxTradeUses;
/*     */   private boolean rewardsExp;
/*     */   
/*     */   public MerchantRecipe(NBTTagCompound tagCompound)
/*     */   {
/*  29 */     readFromTags(tagCompound);
/*     */   }
/*     */   
/*     */   public MerchantRecipe(ItemStack buy1, ItemStack buy2, ItemStack sell)
/*     */   {
/*  34 */     this(buy1, buy2, sell, 0, 7);
/*     */   }
/*     */   
/*     */   public MerchantRecipe(ItemStack buy1, ItemStack buy2, ItemStack sell, int toolUsesIn, int maxTradeUsesIn)
/*     */   {
/*  39 */     this.itemToBuy = buy1;
/*  40 */     this.secondItemToBuy = buy2;
/*  41 */     this.itemToSell = sell;
/*  42 */     this.toolUses = toolUsesIn;
/*  43 */     this.maxTradeUses = maxTradeUsesIn;
/*  44 */     this.rewardsExp = true;
/*     */   }
/*     */   
/*     */   public MerchantRecipe(ItemStack buy1, ItemStack sell)
/*     */   {
/*  49 */     this(buy1, null, sell);
/*     */   }
/*     */   
/*     */   public MerchantRecipe(ItemStack buy1, Item sellItem)
/*     */   {
/*  54 */     this(buy1, new ItemStack(sellItem));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getItemToBuy()
/*     */   {
/*  62 */     return this.itemToBuy;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getSecondItemToBuy()
/*     */   {
/*  70 */     return this.secondItemToBuy;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasSecondItemToBuy()
/*     */   {
/*  78 */     return this.secondItemToBuy != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getItemToSell()
/*     */   {
/*  86 */     return this.itemToSell;
/*     */   }
/*     */   
/*     */   public int getToolUses()
/*     */   {
/*  91 */     return this.toolUses;
/*     */   }
/*     */   
/*     */   public int getMaxTradeUses()
/*     */   {
/*  96 */     return this.maxTradeUses;
/*     */   }
/*     */   
/*     */   public void incrementToolUses()
/*     */   {
/* 101 */     this.toolUses += 1;
/*     */   }
/*     */   
/*     */   public void increaseMaxTradeUses(int increment)
/*     */   {
/* 106 */     this.maxTradeUses += increment;
/*     */   }
/*     */   
/*     */   public boolean isRecipeDisabled()
/*     */   {
/* 111 */     return this.toolUses >= this.maxTradeUses;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void compensateToolUses()
/*     */   {
/* 120 */     this.toolUses = this.maxTradeUses;
/*     */   }
/*     */   
/*     */   public boolean getRewardsExp()
/*     */   {
/* 125 */     return this.rewardsExp;
/*     */   }
/*     */   
/*     */   public void readFromTags(NBTTagCompound tagCompound)
/*     */   {
/* 130 */     NBTTagCompound nbttagcompound = tagCompound.getCompoundTag("buy");
/* 131 */     this.itemToBuy = ItemStack.loadItemStackFromNBT(nbttagcompound);
/* 132 */     NBTTagCompound nbttagcompound1 = tagCompound.getCompoundTag("sell");
/* 133 */     this.itemToSell = ItemStack.loadItemStackFromNBT(nbttagcompound1);
/*     */     
/* 135 */     if (tagCompound.hasKey("buyB", 10))
/*     */     {
/* 137 */       this.secondItemToBuy = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("buyB"));
/*     */     }
/*     */     
/* 140 */     if (tagCompound.hasKey("uses", 99))
/*     */     {
/* 142 */       this.toolUses = tagCompound.getInteger("uses");
/*     */     }
/*     */     
/* 145 */     if (tagCompound.hasKey("maxUses", 99))
/*     */     {
/* 147 */       this.maxTradeUses = tagCompound.getInteger("maxUses");
/*     */     }
/*     */     else
/*     */     {
/* 151 */       this.maxTradeUses = 7;
/*     */     }
/*     */     
/* 154 */     if (tagCompound.hasKey("rewardExp", 1))
/*     */     {
/* 156 */       this.rewardsExp = tagCompound.getBoolean("rewardExp");
/*     */     }
/*     */     else
/*     */     {
/* 160 */       this.rewardsExp = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public NBTTagCompound writeToTags()
/*     */   {
/* 166 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 167 */     nbttagcompound.setTag("buy", this.itemToBuy.writeToNBT(new NBTTagCompound()));
/* 168 */     nbttagcompound.setTag("sell", this.itemToSell.writeToNBT(new NBTTagCompound()));
/*     */     
/* 170 */     if (this.secondItemToBuy != null)
/*     */     {
/* 172 */       nbttagcompound.setTag("buyB", this.secondItemToBuy.writeToNBT(new NBTTagCompound()));
/*     */     }
/*     */     
/* 175 */     nbttagcompound.setInteger("uses", this.toolUses);
/* 176 */     nbttagcompound.setInteger("maxUses", this.maxTradeUses);
/* 177 */     nbttagcompound.setBoolean("rewardExp", this.rewardsExp);
/* 178 */     return nbttagcompound;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\village\MerchantRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */