/*    */ package net.minecraft.inventory;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.item.EntityItem;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class InventoryHelper
/*    */ {
/* 13 */   private static final Random RANDOM = new Random();
/*    */   
/*    */   public static void dropInventoryItems(World worldIn, BlockPos pos, IInventory p_180175_2_)
/*    */   {
/* 17 */     func_180174_a(worldIn, pos.getX(), pos.getY(), pos.getZ(), p_180175_2_);
/*    */   }
/*    */   
/*    */   public static void func_180176_a(World worldIn, Entity p_180176_1_, IInventory p_180176_2_)
/*    */   {
/* 22 */     func_180174_a(worldIn, p_180176_1_.posX, p_180176_1_.posY, p_180176_1_.posZ, p_180176_2_);
/*    */   }
/*    */   
/*    */   private static void func_180174_a(World worldIn, double x, double y, double z, IInventory p_180174_7_)
/*    */   {
/* 27 */     for (int i = 0; i < p_180174_7_.getSizeInventory(); i++)
/*    */     {
/* 29 */       ItemStack itemstack = p_180174_7_.getStackInSlot(i);
/*    */       
/* 31 */       if (itemstack != null)
/*    */       {
/* 33 */         spawnItemStack(worldIn, x, y, z, itemstack);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   private static void spawnItemStack(World worldIn, double x, double y, double z, ItemStack stack)
/*    */   {
/* 40 */     float f = RANDOM.nextFloat() * 0.8F + 0.1F;
/* 41 */     float f1 = RANDOM.nextFloat() * 0.8F + 0.1F;
/* 42 */     float f2 = RANDOM.nextFloat() * 0.8F + 0.1F;
/*    */     
/* 44 */     while (stack.stackSize > 0)
/*    */     {
/* 46 */       int i = RANDOM.nextInt(21) + 10;
/*    */       
/* 48 */       if (i > stack.stackSize)
/*    */       {
/* 50 */         i = stack.stackSize;
/*    */       }
/*    */       
/* 53 */       stack.stackSize -= i;
/* 54 */       EntityItem entityitem = new EntityItem(worldIn, x + f, y + f1, z + f2, new ItemStack(stack.getItem(), i, stack.getMetadata()));
/*    */       
/* 56 */       if (stack.hasTagCompound())
/*    */       {
/* 58 */         entityitem.getEntityItem().setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
/*    */       }
/*    */       
/* 61 */       float f3 = 0.05F;
/* 62 */       entityitem.motionX = (RANDOM.nextGaussian() * f3);
/* 63 */       entityitem.motionY = (RANDOM.nextGaussian() * f3 + 0.20000000298023224D);
/* 64 */       entityitem.motionZ = (RANDOM.nextGaussian() * f3);
/* 65 */       worldIn.spawnEntityInWorld(entityitem);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\inventory\InventoryHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */