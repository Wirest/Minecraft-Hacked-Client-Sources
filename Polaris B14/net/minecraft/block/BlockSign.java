/*    */ package net.minecraft.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.tileentity.TileEntity;
/*    */ import net.minecraft.tileentity.TileEntitySign;
/*    */ import net.minecraft.util.AxisAlignedBB;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.IBlockAccess;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockSign extends BlockContainer
/*    */ {
/*    */   protected BlockSign()
/*    */   {
/* 21 */     super(Material.wood);
/* 22 */     float f = 0.25F;
/* 23 */     float f1 = 1.0F;
/* 24 */     setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
/*    */   }
/*    */   
/*    */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
/*    */   {
/* 29 */     return null;
/*    */   }
/*    */   
/*    */   public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
/*    */   {
/* 34 */     setBlockBoundsBasedOnState(worldIn, pos);
/* 35 */     return super.getSelectedBoundingBox(worldIn, pos);
/*    */   }
/*    */   
/*    */   public boolean isFullCube()
/*    */   {
/* 40 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
/*    */   {
/* 45 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isOpaqueCube()
/*    */   {
/* 53 */     return false;
/*    */   }
/*    */   
/*    */   public boolean func_181623_g()
/*    */   {
/* 58 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public TileEntity createNewTileEntity(World worldIn, int meta)
/*    */   {
/* 66 */     return new TileEntitySign();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*    */   {
/* 74 */     return Items.sign;
/*    */   }
/*    */   
/*    */   public Item getItem(World worldIn, BlockPos pos)
/*    */   {
/* 79 */     return Items.sign;
/*    */   }
/*    */   
/*    */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
/*    */   {
/* 84 */     if (worldIn.isRemote)
/*    */     {
/* 86 */       return true;
/*    */     }
/*    */     
/*    */ 
/* 90 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/* 91 */     return (tileentity instanceof TileEntitySign) ? ((TileEntitySign)tileentity).executeCommand(playerIn) : false;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
/*    */   {
/* 97 */     return (!func_181087_e(worldIn, pos)) && (super.canPlaceBlockAt(worldIn, pos));
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockSign.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */