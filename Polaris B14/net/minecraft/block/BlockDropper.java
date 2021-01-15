/*    */ package net.minecraft.block;
/*    */ 
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.dispenser.IBehaviorDispenseItem;
/*    */ import net.minecraft.inventory.IInventory;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.tileentity.TileEntity;
/*    */ import net.minecraft.tileentity.TileEntityDispenser;
/*    */ import net.minecraft.tileentity.TileEntityDropper;
/*    */ import net.minecraft.tileentity.TileEntityHopper;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockDropper extends BlockDispenser
/*    */ {
/* 17 */   private final IBehaviorDispenseItem dropBehavior = new net.minecraft.dispenser.BehaviorDefaultDispenseItem();
/*    */   
/*    */   protected IBehaviorDispenseItem getBehavior(ItemStack stack)
/*    */   {
/* 21 */     return this.dropBehavior;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public TileEntity createNewTileEntity(World worldIn, int meta)
/*    */   {
/* 29 */     return new TileEntityDropper();
/*    */   }
/*    */   
/*    */   protected void dispense(World worldIn, BlockPos pos)
/*    */   {
/* 34 */     BlockSourceImpl blocksourceimpl = new BlockSourceImpl(worldIn, pos);
/* 35 */     TileEntityDispenser tileentitydispenser = (TileEntityDispenser)blocksourceimpl.getBlockTileEntity();
/*    */     
/* 37 */     if (tileentitydispenser != null)
/*    */     {
/* 39 */       int i = tileentitydispenser.getDispenseSlot();
/*    */       
/* 41 */       if (i < 0)
/*    */       {
/* 43 */         worldIn.playAuxSFX(1001, pos, 0);
/*    */       }
/*    */       else
/*    */       {
/* 47 */         ItemStack itemstack = tileentitydispenser.getStackInSlot(i);
/*    */         
/* 49 */         if (itemstack != null)
/*    */         {
/* 51 */           EnumFacing enumfacing = (EnumFacing)worldIn.getBlockState(pos).getValue(FACING);
/* 52 */           BlockPos blockpos = pos.offset(enumfacing);
/* 53 */           IInventory iinventory = TileEntityHopper.getInventoryAtPosition(worldIn, blockpos.getX(), blockpos.getY(), blockpos.getZ());
/*    */           
/*    */           ItemStack itemstack1;
/* 56 */           if (iinventory == null)
/*    */           {
/* 58 */             ItemStack itemstack1 = this.dropBehavior.dispense(blocksourceimpl, itemstack);
/*    */             
/* 60 */             if ((itemstack1 != null) && (itemstack1.stackSize <= 0))
/*    */             {
/* 62 */               itemstack1 = null;
/*    */             }
/*    */           }
/*    */           else
/*    */           {
/* 67 */             itemstack1 = TileEntityHopper.putStackInInventoryAllSlots(iinventory, itemstack.copy().splitStack(1), enumfacing.getOpposite());
/*    */             
/* 69 */             if (itemstack1 == null)
/*    */             {
/* 71 */               itemstack1 = itemstack.copy();
/*    */               
/* 73 */               if (--itemstack1.stackSize <= 0)
/*    */               {
/* 75 */                 itemstack1 = null;
/*    */               }
/*    */             }
/*    */             else
/*    */             {
/* 80 */               itemstack1 = itemstack.copy();
/*    */             }
/*    */           }
/*    */           
/* 84 */           tileentitydispenser.setInventorySlotContents(i, itemstack1);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockDropper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */