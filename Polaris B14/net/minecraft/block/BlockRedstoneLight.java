/*    */ package net.minecraft.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockRedstoneLight extends Block
/*    */ {
/*    */   private final boolean isOn;
/*    */   
/*    */   public BlockRedstoneLight(boolean isOn)
/*    */   {
/* 18 */     super(Material.redstoneLight);
/* 19 */     this.isOn = isOn;
/*    */     
/* 21 */     if (isOn)
/*    */     {
/* 23 */       setLightLevel(1.0F);
/*    */     }
/*    */   }
/*    */   
/*    */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
/*    */   {
/* 29 */     if (!worldIn.isRemote)
/*    */     {
/* 31 */       if ((this.isOn) && (!worldIn.isBlockPowered(pos)))
/*    */       {
/* 33 */         worldIn.setBlockState(pos, Blocks.redstone_lamp.getDefaultState(), 2);
/*    */       }
/* 35 */       else if ((!this.isOn) && (worldIn.isBlockPowered(pos)))
/*    */       {
/* 37 */         worldIn.setBlockState(pos, Blocks.lit_redstone_lamp.getDefaultState(), 2);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*    */   {
/* 47 */     if (!worldIn.isRemote)
/*    */     {
/* 49 */       if ((this.isOn) && (!worldIn.isBlockPowered(pos)))
/*    */       {
/* 51 */         worldIn.scheduleUpdate(pos, this, 4);
/*    */       }
/* 53 */       else if ((!this.isOn) && (worldIn.isBlockPowered(pos)))
/*    */       {
/* 55 */         worldIn.setBlockState(pos, Blocks.lit_redstone_lamp.getDefaultState(), 2);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*    */   {
/* 62 */     if (!worldIn.isRemote)
/*    */     {
/* 64 */       if ((this.isOn) && (!worldIn.isBlockPowered(pos)))
/*    */       {
/* 66 */         worldIn.setBlockState(pos, Blocks.redstone_lamp.getDefaultState(), 2);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*    */   {
/* 76 */     return Item.getItemFromBlock(Blocks.redstone_lamp);
/*    */   }
/*    */   
/*    */   public Item getItem(World worldIn, BlockPos pos)
/*    */   {
/* 81 */     return Item.getItemFromBlock(Blocks.redstone_lamp);
/*    */   }
/*    */   
/*    */   protected ItemStack createStackedBlock(IBlockState state)
/*    */   {
/* 86 */     return new ItemStack(Blocks.redstone_lamp);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockRedstoneLight.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */