/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.properties.PropertyDirection;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumFacing.Plane;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockEndPortalFrame extends Block
/*     */ {
/*  22 */   public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
/*  23 */   public static final PropertyBool EYE = PropertyBool.create("eye");
/*     */   
/*     */   public BlockEndPortalFrame()
/*     */   {
/*  27 */     super(net.minecraft.block.material.Material.rock, MapColor.greenColor);
/*  28 */     setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(EYE, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/*  36 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBlockBoundsForItemRender()
/*     */   {
/*  44 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
/*     */   {
/*  52 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F);
/*  53 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*     */     
/*  55 */     if (((Boolean)worldIn.getBlockState(pos).getValue(EYE)).booleanValue())
/*     */     {
/*  57 */       setBlockBounds(0.3125F, 0.8125F, 0.3125F, 0.6875F, 1.0F, 0.6875F);
/*  58 */       super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*     */     }
/*     */     
/*  61 */     setBlockBoundsForItemRender();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*     */   {
/*  69 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
/*     */   {
/*  78 */     return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(EYE, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public boolean hasComparatorInputOverride()
/*     */   {
/*  83 */     return true;
/*     */   }
/*     */   
/*     */   public int getComparatorInputOverride(World worldIn, BlockPos pos)
/*     */   {
/*  88 */     return ((Boolean)worldIn.getBlockState(pos).getValue(EYE)).booleanValue() ? 15 : 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/*  96 */     return getDefaultState().withProperty(EYE, Boolean.valueOf((meta & 0x4) != 0)).withProperty(FACING, EnumFacing.getHorizontal(meta & 0x3));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 104 */     int i = 0;
/* 105 */     i |= ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
/*     */     
/* 107 */     if (((Boolean)state.getValue(EYE)).booleanValue())
/*     */     {
/* 109 */       i |= 0x4;
/*     */     }
/*     */     
/* 112 */     return i;
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 117 */     return new BlockState(this, new IProperty[] { FACING, EYE });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockEndPortalFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */