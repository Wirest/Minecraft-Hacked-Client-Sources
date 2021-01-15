/*    */ package net.minecraft.dispenser;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.BlockDispenser;
/*    */ import net.minecraft.entity.item.EntityItem;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.util.EnumFacing.Axis;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BehaviorDefaultDispenseItem
/*    */   implements IBehaviorDispenseItem
/*    */ {
/*    */   public final ItemStack dispense(IBlockSource source, ItemStack stack)
/*    */   {
/* 16 */     ItemStack itemstack = dispenseStack(source, stack);
/* 17 */     playDispenseSound(source);
/* 18 */     spawnDispenseParticles(source, BlockDispenser.getFacing(source.getBlockMetadata()));
/* 19 */     return itemstack;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
/*    */   {
/* 27 */     EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockMetadata());
/* 28 */     IPosition iposition = BlockDispenser.getDispensePosition(source);
/* 29 */     ItemStack itemstack = stack.splitStack(1);
/* 30 */     doDispense(source.getWorld(), itemstack, 6, enumfacing, iposition);
/* 31 */     return stack;
/*    */   }
/*    */   
/*    */   public static void doDispense(World worldIn, ItemStack stack, int speed, EnumFacing facing, IPosition position)
/*    */   {
/* 36 */     double d0 = position.getX();
/* 37 */     double d1 = position.getY();
/* 38 */     double d2 = position.getZ();
/*    */     
/* 40 */     if (facing.getAxis() == EnumFacing.Axis.Y)
/*    */     {
/* 42 */       d1 -= 0.125D;
/*    */     }
/*    */     else
/*    */     {
/* 46 */       d1 -= 0.15625D;
/*    */     }
/*    */     
/* 49 */     EntityItem entityitem = new EntityItem(worldIn, d0, d1, d2, stack);
/* 50 */     double d3 = worldIn.rand.nextDouble() * 0.1D + 0.2D;
/* 51 */     entityitem.motionX = (facing.getFrontOffsetX() * d3);
/* 52 */     entityitem.motionY = 0.20000000298023224D;
/* 53 */     entityitem.motionZ = (facing.getFrontOffsetZ() * d3);
/* 54 */     entityitem.motionX += worldIn.rand.nextGaussian() * 0.007499999832361937D * speed;
/* 55 */     entityitem.motionY += worldIn.rand.nextGaussian() * 0.007499999832361937D * speed;
/* 56 */     entityitem.motionZ += worldIn.rand.nextGaussian() * 0.007499999832361937D * speed;
/* 57 */     worldIn.spawnEntityInWorld(entityitem);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void playDispenseSound(IBlockSource source)
/*    */   {
/* 65 */     source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void spawnDispenseParticles(IBlockSource source, EnumFacing facingIn)
/*    */   {
/* 73 */     source.getWorld().playAuxSFX(2000, source.getBlockPos(), func_82488_a(facingIn));
/*    */   }
/*    */   
/*    */   private int func_82488_a(EnumFacing facingIn)
/*    */   {
/* 78 */     return facingIn.getFrontOffsetX() + 1 + (facingIn.getFrontOffsetZ() + 1) * 3;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\dispenser\BehaviorDefaultDispenseItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */