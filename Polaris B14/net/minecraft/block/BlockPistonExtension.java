/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.properties.PropertyDirection;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockPistonExtension extends Block
/*     */ {
/*  25 */   public static final PropertyDirection FACING = PropertyDirection.create("facing");
/*  26 */   public static final PropertyEnum<EnumPistonType> TYPE = PropertyEnum.create("type", EnumPistonType.class);
/*  27 */   public static final PropertyBool SHORT = PropertyBool.create("short");
/*     */   
/*     */   public BlockPistonExtension()
/*     */   {
/*  31 */     super(net.minecraft.block.material.Material.piston);
/*  32 */     setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TYPE, EnumPistonType.DEFAULT).withProperty(SHORT, Boolean.valueOf(false)));
/*  33 */     setStepSound(soundTypePiston);
/*  34 */     setHardness(0.5F);
/*     */   }
/*     */   
/*     */   public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
/*     */   {
/*  39 */     if (player.capabilities.isCreativeMode)
/*     */     {
/*  41 */       EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
/*     */       
/*  43 */       if (enumfacing != null)
/*     */       {
/*  45 */         BlockPos blockpos = pos.offset(enumfacing.getOpposite());
/*  46 */         Block block = worldIn.getBlockState(blockpos).getBlock();
/*     */         
/*  48 */         if ((block == Blocks.piston) || (block == Blocks.sticky_piston))
/*     */         {
/*  50 */           worldIn.setBlockToAir(blockpos);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  55 */     super.onBlockHarvested(worldIn, pos, state, player);
/*     */   }
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  60 */     super.breakBlock(worldIn, pos, state);
/*  61 */     EnumFacing enumfacing = ((EnumFacing)state.getValue(FACING)).getOpposite();
/*  62 */     pos = pos.offset(enumfacing);
/*  63 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */     
/*  65 */     if (((iblockstate.getBlock() == Blocks.piston) || (iblockstate.getBlock() == Blocks.sticky_piston)) && (((Boolean)iblockstate.getValue(BlockPistonBase.EXTENDED)).booleanValue()))
/*     */     {
/*  67 */       iblockstate.getBlock().dropBlockAsItem(worldIn, pos, iblockstate, 0);
/*  68 */       worldIn.setBlockToAir(pos);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/*  77 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/*  82 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
/*     */   {
/*  87 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
/*     */   {
/*  95 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int quantityDropped(Random random)
/*     */   {
/* 103 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
/*     */   {
/* 111 */     applyHeadBounds(state);
/* 112 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/* 113 */     applyCoreBounds(state);
/* 114 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/* 115 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   private void applyCoreBounds(IBlockState state)
/*     */   {
/* 120 */     float f = 0.25F;
/* 121 */     float f1 = 0.375F;
/* 122 */     float f2 = 0.625F;
/* 123 */     float f3 = 0.25F;
/* 124 */     float f4 = 0.75F;
/*     */     
/* 126 */     switch ((EnumFacing)state.getValue(FACING))
/*     */     {
/*     */     case DOWN: 
/* 129 */       setBlockBounds(0.375F, 0.25F, 0.375F, 0.625F, 1.0F, 0.625F);
/* 130 */       break;
/*     */     
/*     */     case EAST: 
/* 133 */       setBlockBounds(0.375F, 0.0F, 0.375F, 0.625F, 0.75F, 0.625F);
/* 134 */       break;
/*     */     
/*     */     case NORTH: 
/* 137 */       setBlockBounds(0.25F, 0.375F, 0.25F, 0.75F, 0.625F, 1.0F);
/* 138 */       break;
/*     */     
/*     */     case SOUTH: 
/* 141 */       setBlockBounds(0.25F, 0.375F, 0.0F, 0.75F, 0.625F, 0.75F);
/* 142 */       break;
/*     */     
/*     */     case UP: 
/* 145 */       setBlockBounds(0.375F, 0.25F, 0.25F, 0.625F, 0.75F, 1.0F);
/* 146 */       break;
/*     */     
/*     */     case WEST: 
/* 149 */       setBlockBounds(0.0F, 0.375F, 0.25F, 0.75F, 0.625F, 0.75F);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/* 155 */     applyHeadBounds(worldIn.getBlockState(pos));
/*     */   }
/*     */   
/*     */   public void applyHeadBounds(IBlockState state)
/*     */   {
/* 160 */     float f = 0.25F;
/* 161 */     EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
/*     */     
/* 163 */     if (enumfacing != null)
/*     */     {
/* 165 */       switch (enumfacing)
/*     */       {
/*     */       case DOWN: 
/* 168 */         setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
/* 169 */         break;
/*     */       
/*     */       case EAST: 
/* 172 */         setBlockBounds(0.0F, 0.75F, 0.0F, 1.0F, 1.0F, 1.0F);
/* 173 */         break;
/*     */       
/*     */       case NORTH: 
/* 176 */         setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
/* 177 */         break;
/*     */       
/*     */       case SOUTH: 
/* 180 */         setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
/* 181 */         break;
/*     */       
/*     */       case UP: 
/* 184 */         setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
/* 185 */         break;
/*     */       
/*     */       case WEST: 
/* 188 */         setBlockBounds(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */   {
/* 198 */     EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
/* 199 */     BlockPos blockpos = pos.offset(enumfacing.getOpposite());
/* 200 */     IBlockState iblockstate = worldIn.getBlockState(blockpos);
/*     */     
/* 202 */     if ((iblockstate.getBlock() != Blocks.piston) && (iblockstate.getBlock() != Blocks.sticky_piston))
/*     */     {
/* 204 */       worldIn.setBlockToAir(pos);
/*     */     }
/*     */     else
/*     */     {
/* 208 */       iblockstate.getBlock().onNeighborBlockChange(worldIn, blockpos, iblockstate, neighborBlock);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
/*     */   {
/* 214 */     return true;
/*     */   }
/*     */   
/*     */   public static EnumFacing getFacing(int meta)
/*     */   {
/* 219 */     int i = meta & 0x7;
/* 220 */     return i > 5 ? null : EnumFacing.getFront(i);
/*     */   }
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos)
/*     */   {
/* 225 */     return worldIn.getBlockState(pos).getValue(TYPE) == EnumPistonType.STICKY ? Item.getItemFromBlock(Blocks.sticky_piston) : Item.getItemFromBlock(Blocks.piston);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 233 */     return getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(TYPE, (meta & 0x8) > 0 ? EnumPistonType.STICKY : EnumPistonType.DEFAULT);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 241 */     int i = 0;
/* 242 */     i |= ((EnumFacing)state.getValue(FACING)).getIndex();
/*     */     
/* 244 */     if (state.getValue(TYPE) == EnumPistonType.STICKY)
/*     */     {
/* 246 */       i |= 0x8;
/*     */     }
/*     */     
/* 249 */     return i;
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 254 */     return new BlockState(this, new IProperty[] { FACING, TYPE, SHORT });
/*     */   }
/*     */   
/*     */   public static enum EnumPistonType implements IStringSerializable
/*     */   {
/* 259 */     DEFAULT("normal"), 
/* 260 */     STICKY("sticky");
/*     */     
/*     */     private final String VARIANT;
/*     */     
/*     */     private EnumPistonType(String name)
/*     */     {
/* 266 */       this.VARIANT = name;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 271 */       return this.VARIANT;
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 276 */       return this.VARIANT;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockPistonExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */