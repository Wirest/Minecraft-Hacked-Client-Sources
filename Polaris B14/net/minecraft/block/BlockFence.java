/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemLead;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockFence
/*     */   extends Block
/*     */ {
/*  24 */   public static final PropertyBool NORTH = PropertyBool.create("north");
/*     */   
/*     */ 
/*  27 */   public static final PropertyBool EAST = PropertyBool.create("east");
/*     */   
/*     */ 
/*  30 */   public static final PropertyBool SOUTH = PropertyBool.create("south");
/*     */   
/*     */ 
/*  33 */   public static final PropertyBool WEST = PropertyBool.create("west");
/*     */   
/*     */   public BlockFence(Material materialIn)
/*     */   {
/*  37 */     this(materialIn, materialIn.getMaterialMapColor());
/*     */   }
/*     */   
/*     */   public BlockFence(Material p_i46395_1_, MapColor p_i46395_2_)
/*     */   {
/*  42 */     super(p_i46395_1_, p_i46395_2_);
/*  43 */     setDefaultState(this.blockState.getBaseState().withProperty(NORTH, Boolean.valueOf(false)).withProperty(EAST, Boolean.valueOf(false)).withProperty(SOUTH, Boolean.valueOf(false)).withProperty(WEST, Boolean.valueOf(false)));
/*  44 */     setCreativeTab(CreativeTabs.tabDecorations);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
/*     */   {
/*  52 */     boolean flag = canConnectTo(worldIn, pos.north());
/*  53 */     boolean flag1 = canConnectTo(worldIn, pos.south());
/*  54 */     boolean flag2 = canConnectTo(worldIn, pos.west());
/*  55 */     boolean flag3 = canConnectTo(worldIn, pos.east());
/*  56 */     float f = 0.375F;
/*  57 */     float f1 = 0.625F;
/*  58 */     float f2 = 0.375F;
/*  59 */     float f3 = 0.625F;
/*     */     
/*  61 */     if (flag)
/*     */     {
/*  63 */       f2 = 0.0F;
/*     */     }
/*     */     
/*  66 */     if (flag1)
/*     */     {
/*  68 */       f3 = 1.0F;
/*     */     }
/*     */     
/*  71 */     if ((flag) || (flag1))
/*     */     {
/*  73 */       setBlockBounds(f, 0.0F, f2, f1, 1.5F, f3);
/*  74 */       super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*     */     }
/*     */     
/*  77 */     f2 = 0.375F;
/*  78 */     f3 = 0.625F;
/*     */     
/*  80 */     if (flag2)
/*     */     {
/*  82 */       f = 0.0F;
/*     */     }
/*     */     
/*  85 */     if (flag3)
/*     */     {
/*  87 */       f1 = 1.0F;
/*     */     }
/*     */     
/*  90 */     if ((flag2) || (flag3) || ((!flag) && (!flag1)))
/*     */     {
/*  92 */       setBlockBounds(f, 0.0F, f2, f1, 1.5F, f3);
/*  93 */       super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*     */     }
/*     */     
/*  96 */     if (flag)
/*     */     {
/*  98 */       f2 = 0.0F;
/*     */     }
/*     */     
/* 101 */     if (flag1)
/*     */     {
/* 103 */       f3 = 1.0F;
/*     */     }
/*     */     
/* 106 */     setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
/*     */   }
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/* 111 */     boolean flag = canConnectTo(worldIn, pos.north());
/* 112 */     boolean flag1 = canConnectTo(worldIn, pos.south());
/* 113 */     boolean flag2 = canConnectTo(worldIn, pos.west());
/* 114 */     boolean flag3 = canConnectTo(worldIn, pos.east());
/* 115 */     float f = 0.375F;
/* 116 */     float f1 = 0.625F;
/* 117 */     float f2 = 0.375F;
/* 118 */     float f3 = 0.625F;
/*     */     
/* 120 */     if (flag)
/*     */     {
/* 122 */       f2 = 0.0F;
/*     */     }
/*     */     
/* 125 */     if (flag1)
/*     */     {
/* 127 */       f3 = 1.0F;
/*     */     }
/*     */     
/* 130 */     if (flag2)
/*     */     {
/* 132 */       f = 0.0F;
/*     */     }
/*     */     
/* 135 */     if (flag3)
/*     */     {
/* 137 */       f1 = 1.0F;
/*     */     }
/*     */     
/* 140 */     setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/* 148 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/* 153 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/* 158 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canConnectTo(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/* 163 */     Block block = worldIn.getBlockState(pos).getBlock();
/* 164 */     return block != Blocks.barrier;
/*     */   }
/*     */   
/*     */   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
/*     */   {
/* 169 */     return true;
/*     */   }
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
/*     */   {
/* 174 */     return worldIn.isRemote ? true : ItemLead.attachToFence(playerIn, worldIn, pos);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 182 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
/*     */   {
/* 191 */     return state.withProperty(NORTH, Boolean.valueOf(canConnectTo(worldIn, pos.north()))).withProperty(EAST, Boolean.valueOf(canConnectTo(worldIn, pos.east()))).withProperty(SOUTH, Boolean.valueOf(canConnectTo(worldIn, pos.south()))).withProperty(WEST, Boolean.valueOf(canConnectTo(worldIn, pos.west())));
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 196 */     return new BlockState(this, new IProperty[] { NORTH, EAST, WEST, SOUTH });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockFence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */