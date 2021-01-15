/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityEndPortal;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockEndPortal extends BlockContainer
/*     */ {
/*     */   protected BlockEndPortal(Material materialIn)
/*     */   {
/*  23 */     super(materialIn);
/*  24 */     setLightLevel(1.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TileEntity createNewTileEntity(World worldIn, int meta)
/*     */   {
/*  32 */     return new TileEntityEndPortal();
/*     */   }
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  37 */     float f = 0.0625F;
/*  38 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
/*     */   }
/*     */   
/*     */   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
/*     */   {
/*  43 */     return side == EnumFacing.DOWN ? super.shouldSideBeRendered(worldIn, pos, side) : false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/*  58 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/*  63 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int quantityDropped(Random random)
/*     */   {
/*  71 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
/*     */   {
/*  79 */     if ((entityIn.ridingEntity == null) && (entityIn.riddenByEntity == null) && (!worldIn.isRemote))
/*     */     {
/*  81 */       entityIn.travelToDimension(1);
/*     */     }
/*     */   }
/*     */   
/*     */   public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/*  87 */     double d0 = pos.getX() + rand.nextFloat();
/*  88 */     double d1 = pos.getY() + 0.8F;
/*  89 */     double d2 = pos.getZ() + rand.nextFloat();
/*  90 */     double d3 = 0.0D;
/*  91 */     double d4 = 0.0D;
/*  92 */     double d5 = 0.0D;
/*  93 */     worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5, new int[0]);
/*     */   }
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos)
/*     */   {
/*  98 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MapColor getMapColor(IBlockState state)
/*     */   {
/* 106 */     return MapColor.blackColor;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockEndPortal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */