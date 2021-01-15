/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.ai.EntityAIControlledByPlayer;
/*    */ import net.minecraft.entity.passive.EntityPig;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemCarrotOnAStick extends Item
/*    */ {
/*    */   public ItemCarrotOnAStick()
/*    */   {
/* 14 */     setCreativeTab(CreativeTabs.tabTransport);
/* 15 */     setMaxStackSize(1);
/* 16 */     setMaxDamage(25);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isFull3D()
/*    */   {
/* 24 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean shouldRotateAroundWhenRendering()
/*    */   {
/* 33 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
/*    */   {
/* 41 */     if ((playerIn.isRiding()) && ((playerIn.ridingEntity instanceof EntityPig)))
/*    */     {
/* 43 */       EntityPig entitypig = (EntityPig)playerIn.ridingEntity;
/*    */       
/* 45 */       if ((entitypig.getAIControlledByPlayer().isControlledByPlayer()) && (itemStackIn.getMaxDamage() - itemStackIn.getMetadata() >= 7))
/*    */       {
/* 47 */         entitypig.getAIControlledByPlayer().boostSpeed();
/* 48 */         itemStackIn.damageItem(7, playerIn);
/*    */         
/* 50 */         if (itemStackIn.stackSize == 0)
/*    */         {
/* 52 */           ItemStack itemstack = new ItemStack(Items.fishing_rod);
/* 53 */           itemstack.setTagCompound(itemStackIn.getTagCompound());
/* 54 */           return itemstack;
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 59 */     playerIn.triggerAchievement(net.minecraft.stats.StatList.objectUseStats[Item.getIdFromItem(this)]);
/* 60 */     return itemStackIn;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemCarrotOnAStick.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */