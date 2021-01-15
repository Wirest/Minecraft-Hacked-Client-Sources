/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.inventory.IInventory;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.tileentity.TileEntityDispenser;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WeightedRandomChestContent
/*    */   extends WeightedRandom.Item
/*    */ {
/*    */   private ItemStack theItemId;
/*    */   private int minStackSize;
/*    */   private int maxStackSize;
/*    */   
/*    */   public WeightedRandomChestContent(Item p_i45311_1_, int p_i45311_2_, int minimumChance, int maximumChance, int itemWeightIn)
/*    */   {
/* 25 */     super(itemWeightIn);
/* 26 */     this.theItemId = new ItemStack(p_i45311_1_, 1, p_i45311_2_);
/* 27 */     this.minStackSize = minimumChance;
/* 28 */     this.maxStackSize = maximumChance;
/*    */   }
/*    */   
/*    */   public WeightedRandomChestContent(ItemStack stack, int minimumChance, int maximumChance, int itemWeightIn)
/*    */   {
/* 33 */     super(itemWeightIn);
/* 34 */     this.theItemId = stack;
/* 35 */     this.minStackSize = minimumChance;
/* 36 */     this.maxStackSize = maximumChance;
/*    */   }
/*    */   
/*    */   public static void generateChestContents(Random random, List<WeightedRandomChestContent> listIn, IInventory inv, int max)
/*    */   {
/* 41 */     for (int i = 0; i < max; i++)
/*    */     {
/* 43 */       WeightedRandomChestContent weightedrandomchestcontent = (WeightedRandomChestContent)WeightedRandom.getRandomItem(random, listIn);
/* 44 */       int j = weightedrandomchestcontent.minStackSize + random.nextInt(weightedrandomchestcontent.maxStackSize - weightedrandomchestcontent.minStackSize + 1);
/*    */       
/* 46 */       if (weightedrandomchestcontent.theItemId.getMaxStackSize() >= j)
/*    */       {
/* 48 */         ItemStack itemstack1 = weightedrandomchestcontent.theItemId.copy();
/* 49 */         itemstack1.stackSize = j;
/* 50 */         inv.setInventorySlotContents(random.nextInt(inv.getSizeInventory()), itemstack1);
/*    */       }
/*    */       else
/*    */       {
/* 54 */         for (int k = 0; k < j; k++)
/*    */         {
/* 56 */           ItemStack itemstack = weightedrandomchestcontent.theItemId.copy();
/* 57 */           itemstack.stackSize = 1;
/* 58 */           inv.setInventorySlotContents(random.nextInt(inv.getSizeInventory()), itemstack);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public static void generateDispenserContents(Random random, List<WeightedRandomChestContent> listIn, TileEntityDispenser dispenser, int max)
/*    */   {
/* 66 */     for (int i = 0; i < max; i++)
/*    */     {
/* 68 */       WeightedRandomChestContent weightedrandomchestcontent = (WeightedRandomChestContent)WeightedRandom.getRandomItem(random, listIn);
/* 69 */       int j = weightedrandomchestcontent.minStackSize + random.nextInt(weightedrandomchestcontent.maxStackSize - weightedrandomchestcontent.minStackSize + 1);
/*    */       
/* 71 */       if (weightedrandomchestcontent.theItemId.getMaxStackSize() >= j)
/*    */       {
/* 73 */         ItemStack itemstack1 = weightedrandomchestcontent.theItemId.copy();
/* 74 */         itemstack1.stackSize = j;
/* 75 */         dispenser.setInventorySlotContents(random.nextInt(dispenser.getSizeInventory()), itemstack1);
/*    */       }
/*    */       else
/*    */       {
/* 79 */         for (int k = 0; k < j; k++)
/*    */         {
/* 81 */           ItemStack itemstack = weightedrandomchestcontent.theItemId.copy();
/* 82 */           itemstack.stackSize = 1;
/* 83 */           dispenser.setInventorySlotContents(random.nextInt(dispenser.getSizeInventory()), itemstack);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public static List<WeightedRandomChestContent> func_177629_a(List<WeightedRandomChestContent> p_177629_0_, WeightedRandomChestContent... p_177629_1_)
/*    */   {
/* 91 */     List<WeightedRandomChestContent> list = Lists.newArrayList(p_177629_0_);
/* 92 */     Collections.addAll(list, p_177629_1_);
/* 93 */     return list;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\WeightedRandomChestContent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */