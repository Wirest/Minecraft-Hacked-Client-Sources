/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.passive.EntityPig;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ 
/*    */ public class ItemSaddle extends Item
/*    */ {
/*    */   public ItemSaddle()
/*    */   {
/* 12 */     this.maxStackSize = 1;
/* 13 */     setCreativeTab(CreativeTabs.tabTransport);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target)
/*    */   {
/* 21 */     if ((target instanceof EntityPig))
/*    */     {
/* 23 */       EntityPig entitypig = (EntityPig)target;
/*    */       
/* 25 */       if ((!entitypig.getSaddled()) && (!entitypig.isChild()))
/*    */       {
/* 27 */         entitypig.setSaddled(true);
/* 28 */         entitypig.worldObj.playSoundAtEntity(entitypig, "mob.horse.leather", 0.5F, 1.0F);
/* 29 */         stack.stackSize -= 1;
/*    */       }
/*    */       
/* 32 */       return true;
/*    */     }
/*    */     
/*    */ 
/* 36 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
/*    */   {
/* 46 */     itemInteractionForEntity(stack, null, target);
/* 47 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemSaddle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */