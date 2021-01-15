/*    */ package net.minecraft.dispenser;
/*    */ 
/*    */ import net.minecraft.block.BlockDispenser;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.IProjectile;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class BehaviorProjectileDispense
/*    */   extends BehaviorDefaultDispenseItem
/*    */ {
/*    */   public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
/*    */   {
/* 17 */     World world = source.getWorld();
/* 18 */     IPosition iposition = BlockDispenser.getDispensePosition(source);
/* 19 */     EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockMetadata());
/* 20 */     IProjectile iprojectile = getProjectileEntity(world, iposition);
/* 21 */     iprojectile.setThrowableHeading(enumfacing.getFrontOffsetX(), enumfacing.getFrontOffsetY() + 0.1F, enumfacing.getFrontOffsetZ(), func_82500_b(), func_82498_a());
/* 22 */     world.spawnEntityInWorld((Entity)iprojectile);
/* 23 */     stack.splitStack(1);
/* 24 */     return stack;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void playDispenseSound(IBlockSource source)
/*    */   {
/* 32 */     source.getWorld().playAuxSFX(1002, source.getBlockPos(), 0);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected abstract IProjectile getProjectileEntity(World paramWorld, IPosition paramIPosition);
/*    */   
/*    */ 
/*    */   protected float func_82498_a()
/*    */   {
/* 42 */     return 6.0F;
/*    */   }
/*    */   
/*    */   protected float func_82500_b()
/*    */   {
/* 47 */     return 1.1F;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\dispenser\BehaviorProjectileDispense.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */