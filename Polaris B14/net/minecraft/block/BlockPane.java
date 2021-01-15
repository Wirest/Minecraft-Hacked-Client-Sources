/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockPane extends Block
/*     */ {
/*  23 */   public static final PropertyBool NORTH = PropertyBool.create("north");
/*  24 */   public static final PropertyBool EAST = PropertyBool.create("east");
/*  25 */   public static final PropertyBool SOUTH = PropertyBool.create("south");
/*  26 */   public static final PropertyBool WEST = PropertyBool.create("west");
/*     */   private final boolean canDrop;
/*     */   
/*     */   protected BlockPane(Material materialIn, boolean canDrop)
/*     */   {
/*  31 */     super(materialIn);
/*  32 */     setDefaultState(this.blockState.getBaseState().withProperty(NORTH, Boolean.valueOf(false)).withProperty(EAST, Boolean.valueOf(false)).withProperty(SOUTH, Boolean.valueOf(false)).withProperty(WEST, Boolean.valueOf(false)));
/*  33 */     this.canDrop = canDrop;
/*  34 */     setCreativeTab(CreativeTabs.tabDecorations);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  43 */     return state.withProperty(NORTH, Boolean.valueOf(canPaneConnectToBlock(worldIn.getBlockState(pos.north()).getBlock()))).withProperty(SOUTH, Boolean.valueOf(canPaneConnectToBlock(worldIn.getBlockState(pos.south()).getBlock()))).withProperty(WEST, Boolean.valueOf(canPaneConnectToBlock(worldIn.getBlockState(pos.west()).getBlock()))).withProperty(EAST, Boolean.valueOf(canPaneConnectToBlock(worldIn.getBlockState(pos.east()).getBlock())));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*     */   {
/*  51 */     return !this.canDrop ? null : super.getItemDropped(state, rand, fortune);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/*  59 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/*  64 */     return false;
/*     */   }
/*     */   
/*     */   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
/*     */   {
/*  69 */     return worldIn.getBlockState(pos).getBlock() == this ? false : super.shouldSideBeRendered(worldIn, pos, side);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
/*     */   {
/*  77 */     boolean flag = canPaneConnectToBlock(worldIn.getBlockState(pos.north()).getBlock());
/*  78 */     boolean flag1 = canPaneConnectToBlock(worldIn.getBlockState(pos.south()).getBlock());
/*  79 */     boolean flag2 = canPaneConnectToBlock(worldIn.getBlockState(pos.west()).getBlock());
/*  80 */     boolean flag3 = canPaneConnectToBlock(worldIn.getBlockState(pos.east()).getBlock());
/*     */     
/*  82 */     if (((!flag2) || (!flag3)) && ((flag2) || (flag3) || (flag) || (flag1)))
/*     */     {
/*  84 */       if (flag2)
/*     */       {
/*  86 */         setBlockBounds(0.0F, 0.0F, 0.4375F, 0.5F, 1.0F, 0.5625F);
/*  87 */         super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*     */       }
/*  89 */       else if (flag3)
/*     */       {
/*  91 */         setBlockBounds(0.5F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
/*  92 */         super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  97 */       setBlockBounds(0.0F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
/*  98 */       super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*     */     }
/*     */     
/* 101 */     if (((!flag) || (!flag1)) && ((flag2) || (flag3) || (flag) || (flag1)))
/*     */     {
/* 103 */       if (flag)
/*     */       {
/* 105 */         setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 0.5F);
/* 106 */         super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*     */       }
/* 108 */       else if (flag1)
/*     */       {
/* 110 */         setBlockBounds(0.4375F, 0.0F, 0.5F, 0.5625F, 1.0F, 1.0F);
/* 111 */         super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 116 */       setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 1.0F);
/* 117 */       super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBlockBoundsForItemRender()
/*     */   {
/* 126 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/* 131 */     float f = 0.4375F;
/* 132 */     float f1 = 0.5625F;
/* 133 */     float f2 = 0.4375F;
/* 134 */     float f3 = 0.5625F;
/* 135 */     boolean flag = canPaneConnectToBlock(worldIn.getBlockState(pos.north()).getBlock());
/* 136 */     boolean flag1 = canPaneConnectToBlock(worldIn.getBlockState(pos.south()).getBlock());
/* 137 */     boolean flag2 = canPaneConnectToBlock(worldIn.getBlockState(pos.west()).getBlock());
/* 138 */     boolean flag3 = canPaneConnectToBlock(worldIn.getBlockState(pos.east()).getBlock());
/*     */     
/* 140 */     if (((!flag2) || (!flag3)) && ((flag2) || (flag3) || (flag) || (flag1)))
/*     */     {
/* 142 */       if (flag2)
/*     */       {
/* 144 */         f = 0.0F;
/*     */       }
/* 146 */       else if (flag3)
/*     */       {
/* 148 */         f1 = 1.0F;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 153 */       f = 0.0F;
/* 154 */       f1 = 1.0F;
/*     */     }
/*     */     
/* 157 */     if (((!flag) || (!flag1)) && ((flag2) || (flag3) || (flag) || (flag1)))
/*     */     {
/* 159 */       if (flag)
/*     */       {
/* 161 */         f2 = 0.0F;
/*     */       }
/* 163 */       else if (flag1)
/*     */       {
/* 165 */         f3 = 1.0F;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 170 */       f2 = 0.0F;
/* 171 */       f3 = 1.0F;
/*     */     }
/*     */     
/* 174 */     setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
/*     */   }
/*     */   
/*     */   public final boolean canPaneConnectToBlock(Block blockIn)
/*     */   {
/* 179 */     return (blockIn.isFullBlock()) || (blockIn == this) || (blockIn == Blocks.glass) || (blockIn == Blocks.stained_glass) || (blockIn == Blocks.stained_glass_pane) || ((blockIn instanceof BlockPane));
/*     */   }
/*     */   
/*     */   protected boolean canSilkHarvest()
/*     */   {
/* 184 */     return true;
/*     */   }
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer()
/*     */   {
/* 189 */     return EnumWorldBlockLayer.CUTOUT_MIPPED;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 197 */     return 0;
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 202 */     return new BlockState(this, new IProperty[] { NORTH, EAST, WEST, SOUTH });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockPane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */