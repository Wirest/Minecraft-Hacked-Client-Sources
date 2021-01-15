/*     */ package net.minecraft.village;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.nbt.NBTUtil;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ 
/*     */ 
/*     */ public class MerchantRecipeList
/*     */   extends ArrayList<MerchantRecipe>
/*     */ {
/*     */   public MerchantRecipeList() {}
/*     */   
/*     */   public MerchantRecipeList(NBTTagCompound compound)
/*     */   {
/*  19 */     readRecipiesFromTags(compound);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MerchantRecipe canRecipeBeUsed(ItemStack p_77203_1_, ItemStack p_77203_2_, int p_77203_3_)
/*     */   {
/*  27 */     if ((p_77203_3_ > 0) && (p_77203_3_ < size()))
/*     */     {
/*  29 */       MerchantRecipe merchantrecipe1 = (MerchantRecipe)get(p_77203_3_);
/*  30 */       return (!func_181078_a(p_77203_1_, merchantrecipe1.getItemToBuy())) || (((p_77203_2_ == null) && (!merchantrecipe1.hasSecondItemToBuy())) || ((!merchantrecipe1.hasSecondItemToBuy()) || (!func_181078_a(p_77203_2_, merchantrecipe1.getSecondItemToBuy())) || (p_77203_1_.stackSize < merchantrecipe1.getItemToBuy().stackSize) || ((merchantrecipe1.hasSecondItemToBuy()) && (p_77203_2_.stackSize < merchantrecipe1.getSecondItemToBuy().stackSize)))) ? null : merchantrecipe1;
/*     */     }
/*     */     
/*     */ 
/*  34 */     for (int i = 0; i < size(); i++)
/*     */     {
/*  36 */       MerchantRecipe merchantrecipe = (MerchantRecipe)get(i);
/*     */       
/*  38 */       if ((func_181078_a(p_77203_1_, merchantrecipe.getItemToBuy())) && (p_77203_1_.stackSize >= merchantrecipe.getItemToBuy().stackSize) && (((!merchantrecipe.hasSecondItemToBuy()) && (p_77203_2_ == null)) || ((merchantrecipe.hasSecondItemToBuy()) && (func_181078_a(p_77203_2_, merchantrecipe.getSecondItemToBuy())) && (p_77203_2_.stackSize >= merchantrecipe.getSecondItemToBuy().stackSize))))
/*     */       {
/*  40 */         return merchantrecipe;
/*     */       }
/*     */     }
/*     */     
/*  44 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean func_181078_a(ItemStack p_181078_1_, ItemStack p_181078_2_)
/*     */   {
/*  50 */     return (ItemStack.areItemsEqual(p_181078_1_, p_181078_2_)) && ((!p_181078_2_.hasTagCompound()) || ((p_181078_1_.hasTagCompound()) && (NBTUtil.func_181123_a(p_181078_2_.getTagCompound(), p_181078_1_.getTagCompound(), false))));
/*     */   }
/*     */   
/*     */   public void writeToBuf(PacketBuffer buffer)
/*     */   {
/*  55 */     buffer.writeByte((byte)(size() & 0xFF));
/*     */     
/*  57 */     for (int i = 0; i < size(); i++)
/*     */     {
/*  59 */       MerchantRecipe merchantrecipe = (MerchantRecipe)get(i);
/*  60 */       buffer.writeItemStackToBuffer(merchantrecipe.getItemToBuy());
/*  61 */       buffer.writeItemStackToBuffer(merchantrecipe.getItemToSell());
/*  62 */       ItemStack itemstack = merchantrecipe.getSecondItemToBuy();
/*  63 */       buffer.writeBoolean(itemstack != null);
/*     */       
/*  65 */       if (itemstack != null)
/*     */       {
/*  67 */         buffer.writeItemStackToBuffer(itemstack);
/*     */       }
/*     */       
/*  70 */       buffer.writeBoolean(merchantrecipe.isRecipeDisabled());
/*  71 */       buffer.writeInt(merchantrecipe.getToolUses());
/*  72 */       buffer.writeInt(merchantrecipe.getMaxTradeUses());
/*     */     }
/*     */   }
/*     */   
/*     */   public static MerchantRecipeList readFromBuf(PacketBuffer buffer) throws IOException
/*     */   {
/*  78 */     MerchantRecipeList merchantrecipelist = new MerchantRecipeList();
/*  79 */     int i = buffer.readByte() & 0xFF;
/*     */     
/*  81 */     for (int j = 0; j < i; j++)
/*     */     {
/*  83 */       ItemStack itemstack = buffer.readItemStackFromBuffer();
/*  84 */       ItemStack itemstack1 = buffer.readItemStackFromBuffer();
/*  85 */       ItemStack itemstack2 = null;
/*     */       
/*  87 */       if (buffer.readBoolean())
/*     */       {
/*  89 */         itemstack2 = buffer.readItemStackFromBuffer();
/*     */       }
/*     */       
/*  92 */       boolean flag = buffer.readBoolean();
/*  93 */       int k = buffer.readInt();
/*  94 */       int l = buffer.readInt();
/*  95 */       MerchantRecipe merchantrecipe = new MerchantRecipe(itemstack, itemstack2, itemstack1, k, l);
/*     */       
/*  97 */       if (flag)
/*     */       {
/*  99 */         merchantrecipe.compensateToolUses();
/*     */       }
/*     */       
/* 102 */       merchantrecipelist.add(merchantrecipe);
/*     */     }
/*     */     
/* 105 */     return merchantrecipelist;
/*     */   }
/*     */   
/*     */   public void readRecipiesFromTags(NBTTagCompound compound)
/*     */   {
/* 110 */     NBTTagList nbttaglist = compound.getTagList("Recipes", 10);
/*     */     
/* 112 */     for (int i = 0; i < nbttaglist.tagCount(); i++)
/*     */     {
/* 114 */       NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/* 115 */       add(new MerchantRecipe(nbttagcompound));
/*     */     }
/*     */   }
/*     */   
/*     */   public NBTTagCompound getRecipiesAsTags()
/*     */   {
/* 121 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 122 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/* 124 */     for (int i = 0; i < size(); i++)
/*     */     {
/* 126 */       MerchantRecipe merchantrecipe = (MerchantRecipe)get(i);
/* 127 */       nbttaglist.appendTag(merchantrecipe.writeToTags());
/*     */     }
/*     */     
/* 130 */     nbttagcompound.setTag("Recipes", nbttaglist);
/* 131 */     return nbttagcompound;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\village\MerchantRecipeList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */