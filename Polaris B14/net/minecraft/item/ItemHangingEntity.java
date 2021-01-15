/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.EntityHanging;
/*    */ import net.minecraft.entity.item.EntityItemFrame;
/*    */ import net.minecraft.entity.item.EntityPainting;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemHangingEntity extends Item
/*    */ {
/*    */   private final Class<? extends EntityHanging> hangingEntityClass;
/*    */   
/*    */   public ItemHangingEntity(Class<? extends EntityHanging> entityClass)
/*    */   {
/* 18 */     this.hangingEntityClass = entityClass;
/* 19 */     setCreativeTab(CreativeTabs.tabDecorations);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
/*    */   {
/* 27 */     if (side == EnumFacing.DOWN)
/*    */     {
/* 29 */       return false;
/*    */     }
/* 31 */     if (side == EnumFacing.UP)
/*    */     {
/* 33 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 37 */     BlockPos blockpos = pos.offset(side);
/*    */     
/* 39 */     if (!playerIn.canPlayerEdit(blockpos, side, stack))
/*    */     {
/* 41 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 45 */     EntityHanging entityhanging = createEntity(worldIn, blockpos, side);
/*    */     
/* 47 */     if ((entityhanging != null) && (entityhanging.onValidSurface()))
/*    */     {
/* 49 */       if (!worldIn.isRemote)
/*    */       {
/* 51 */         worldIn.spawnEntityInWorld(entityhanging);
/*    */       }
/*    */       
/* 54 */       stack.stackSize -= 1;
/*    */     }
/*    */     
/* 57 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   private EntityHanging createEntity(World worldIn, BlockPos pos, EnumFacing clickedSide)
/*    */   {
/* 64 */     return this.hangingEntityClass == EntityItemFrame.class ? new EntityItemFrame(worldIn, pos, clickedSide) : this.hangingEntityClass == EntityPainting.class ? new EntityPainting(worldIn, pos, clickedSide) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemHangingEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */