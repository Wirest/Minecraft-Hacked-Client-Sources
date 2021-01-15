/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.util.StatCollector;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockWall
/*     */   extends Block
/*     */ {
/*  24 */   public static final PropertyBool UP = PropertyBool.create("up");
/*  25 */   public static final PropertyBool NORTH = PropertyBool.create("north");
/*  26 */   public static final PropertyBool EAST = PropertyBool.create("east");
/*  27 */   public static final PropertyBool SOUTH = PropertyBool.create("south");
/*  28 */   public static final PropertyBool WEST = PropertyBool.create("west");
/*  29 */   public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
/*     */   
/*     */   public BlockWall(Block modelBlock)
/*     */   {
/*  33 */     super(modelBlock.blockMaterial);
/*  34 */     setDefaultState(this.blockState.getBaseState().withProperty(UP, Boolean.valueOf(false)).withProperty(NORTH, Boolean.valueOf(false)).withProperty(EAST, Boolean.valueOf(false)).withProperty(SOUTH, Boolean.valueOf(false)).withProperty(WEST, Boolean.valueOf(false)).withProperty(VARIANT, EnumType.NORMAL));
/*  35 */     setHardness(modelBlock.blockHardness);
/*  36 */     setResistance(modelBlock.blockResistance / 3.0F);
/*  37 */     setStepSound(modelBlock.stepSound);
/*  38 */     setCreativeTab(CreativeTabs.tabBlock);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getLocalizedName()
/*     */   {
/*  46 */     return StatCollector.translateToLocal(getUnlocalizedName() + "." + EnumType.NORMAL.getUnlocalizedName() + ".name");
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/*  51 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  56 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/*  64 */     return false;
/*     */   }
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  69 */     boolean flag = canConnectTo(worldIn, pos.north());
/*  70 */     boolean flag1 = canConnectTo(worldIn, pos.south());
/*  71 */     boolean flag2 = canConnectTo(worldIn, pos.west());
/*  72 */     boolean flag3 = canConnectTo(worldIn, pos.east());
/*  73 */     float f = 0.25F;
/*  74 */     float f1 = 0.75F;
/*  75 */     float f2 = 0.25F;
/*  76 */     float f3 = 0.75F;
/*  77 */     float f4 = 1.0F;
/*     */     
/*  79 */     if (flag)
/*     */     {
/*  81 */       f2 = 0.0F;
/*     */     }
/*     */     
/*  84 */     if (flag1)
/*     */     {
/*  86 */       f3 = 1.0F;
/*     */     }
/*     */     
/*  89 */     if (flag2)
/*     */     {
/*  91 */       f = 0.0F;
/*     */     }
/*     */     
/*  94 */     if (flag3)
/*     */     {
/*  96 */       f1 = 1.0F;
/*     */     }
/*     */     
/*  99 */     if ((flag) && (flag1) && (!flag2) && (!flag3))
/*     */     {
/* 101 */       f4 = 0.8125F;
/* 102 */       f = 0.3125F;
/* 103 */       f1 = 0.6875F;
/*     */     }
/* 105 */     else if ((!flag) && (!flag1) && (flag2) && (flag3))
/*     */     {
/* 107 */       f4 = 0.8125F;
/* 108 */       f2 = 0.3125F;
/* 109 */       f3 = 0.6875F;
/*     */     }
/*     */     
/* 112 */     setBlockBounds(f, 0.0F, f2, f1, f4, f3);
/*     */   }
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 117 */     setBlockBoundsBasedOnState(worldIn, pos);
/* 118 */     this.maxY = 1.5D;
/* 119 */     return super.getCollisionBoundingBox(worldIn, pos, state);
/*     */   }
/*     */   
/*     */   public boolean canConnectTo(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/* 124 */     Block block = worldIn.getBlockState(pos).getBlock();
/* 125 */     return block != Blocks.barrier;
/*     */   }
/*     */   
/*     */ 
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
/*     */   {
/*     */     EnumType[] arrayOfEnumType;
/*     */     
/* 133 */     int j = (arrayOfEnumType = EnumType.values()).length; for (int i = 0; i < j; i++) { EnumType blockwall$enumtype = arrayOfEnumType[i];
/*     */       
/* 135 */       list.add(new ItemStack(itemIn, 1, blockwall$enumtype.getMetadata()));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int damageDropped(IBlockState state)
/*     */   {
/* 145 */     return ((EnumType)state.getValue(VARIANT)).getMetadata();
/*     */   }
/*     */   
/*     */   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
/*     */   {
/* 150 */     return side == EnumFacing.DOWN ? super.shouldSideBeRendered(worldIn, pos, side) : true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 158 */     return getDefaultState().withProperty(VARIANT, EnumType.byMetadata(meta));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 166 */     return ((EnumType)state.getValue(VARIANT)).getMetadata();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
/*     */   {
/* 175 */     return state.withProperty(UP, Boolean.valueOf(!worldIn.isAirBlock(pos.up()))).withProperty(NORTH, Boolean.valueOf(canConnectTo(worldIn, pos.north()))).withProperty(EAST, Boolean.valueOf(canConnectTo(worldIn, pos.east()))).withProperty(SOUTH, Boolean.valueOf(canConnectTo(worldIn, pos.south()))).withProperty(WEST, Boolean.valueOf(canConnectTo(worldIn, pos.west())));
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 180 */     return new BlockState(this, new IProperty[] { UP, NORTH, EAST, WEST, SOUTH, VARIANT });
/*     */   }
/*     */   
/*     */   public static enum EnumType implements IStringSerializable
/*     */   {
/* 185 */     NORMAL(0, "cobblestone", "normal"), 
/* 186 */     MOSSY(1, "mossy_cobblestone", "mossy");
/*     */     
/*     */     private static final EnumType[] META_LOOKUP;
/*     */     private final int meta;
/*     */     private final String name;
/*     */     private String unlocalizedName;
/*     */     
/*     */     private EnumType(int meta, String name, String unlocalizedName)
/*     */     {
/* 195 */       this.meta = meta;
/* 196 */       this.name = name;
/* 197 */       this.unlocalizedName = unlocalizedName;
/*     */     }
/*     */     
/*     */     public int getMetadata()
/*     */     {
/* 202 */       return this.meta;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 207 */       return this.name;
/*     */     }
/*     */     
/*     */     public static EnumType byMetadata(int meta)
/*     */     {
/* 212 */       if ((meta < 0) || (meta >= META_LOOKUP.length))
/*     */       {
/* 214 */         meta = 0;
/*     */       }
/*     */       
/* 217 */       return META_LOOKUP[meta];
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 222 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getUnlocalizedName()
/*     */     {
/* 227 */       return this.unlocalizedName;
/*     */     }
/*     */     
/*     */     static
/*     */     {
/* 188 */       META_LOOKUP = new EnumType[values().length];
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       EnumType[] arrayOfEnumType;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 231 */       int j = (arrayOfEnumType = values()).length; for (int i = 0; i < j; i++) { EnumType blockwall$enumtype = arrayOfEnumType[i];
/*     */         
/* 233 */         META_LOOKUP[blockwall$enumtype.getMetadata()] = blockwall$enumtype;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockWall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */