/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeColorHelper;
/*     */ 
/*     */ public class BlockDoublePlant extends BlockBush implements IGrowable
/*     */ {
/*  28 */   public static final PropertyEnum<EnumPlantType> VARIANT = PropertyEnum.create("variant", EnumPlantType.class);
/*  29 */   public static final PropertyEnum<EnumBlockHalf> HALF = PropertyEnum.create("half", EnumBlockHalf.class);
/*  30 */   public static final PropertyEnum<EnumFacing> field_181084_N = BlockDirectional.FACING;
/*     */   
/*     */   public BlockDoublePlant()
/*     */   {
/*  34 */     super(Material.vine);
/*  35 */     setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumPlantType.SUNFLOWER).withProperty(HALF, EnumBlockHalf.LOWER).withProperty(field_181084_N, EnumFacing.NORTH));
/*  36 */     setHardness(0.0F);
/*  37 */     setStepSound(soundTypeGrass);
/*  38 */     setUnlocalizedName("doublePlant");
/*     */   }
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  43 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public EnumPlantType getVariant(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  48 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */     
/*  50 */     if (iblockstate.getBlock() == this)
/*     */     {
/*  52 */       iblockstate = getActualState(iblockstate, worldIn, pos);
/*  53 */       return (EnumPlantType)iblockstate.getValue(VARIANT);
/*     */     }
/*     */     
/*     */ 
/*  57 */     return EnumPlantType.FERN;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
/*     */   {
/*  63 */     return (super.canPlaceBlockAt(worldIn, pos)) && (worldIn.isAirBlock(pos.up()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isReplaceable(World worldIn, BlockPos pos)
/*     */   {
/*  71 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */     
/*  73 */     if (iblockstate.getBlock() != this)
/*     */     {
/*  75 */       return true;
/*     */     }
/*     */     
/*     */ 
/*  79 */     EnumPlantType blockdoubleplant$enumplanttype = (EnumPlantType)getActualState(iblockstate, worldIn, pos).getValue(VARIANT);
/*  80 */     return (blockdoubleplant$enumplanttype == EnumPlantType.FERN) || (blockdoubleplant$enumplanttype == EnumPlantType.GRASS);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  86 */     if (!canBlockStay(worldIn, pos, state))
/*     */     {
/*  88 */       boolean flag = state.getValue(HALF) == EnumBlockHalf.UPPER;
/*  89 */       BlockPos blockpos = flag ? pos : pos.up();
/*  90 */       BlockPos blockpos1 = flag ? pos.down() : pos;
/*  91 */       Block block = flag ? this : worldIn.getBlockState(blockpos).getBlock();
/*  92 */       Block block1 = flag ? worldIn.getBlockState(blockpos1).getBlock() : this;
/*     */       
/*  94 */       if (block == this)
/*     */       {
/*  96 */         worldIn.setBlockState(blockpos, Blocks.air.getDefaultState(), 2);
/*     */       }
/*     */       
/*  99 */       if (block1 == this)
/*     */       {
/* 101 */         worldIn.setBlockState(blockpos1, Blocks.air.getDefaultState(), 3);
/*     */         
/* 103 */         if (!flag)
/*     */         {
/* 105 */           dropBlockAsItem(worldIn, blockpos1, state, 0);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 113 */     if (state.getValue(HALF) == EnumBlockHalf.UPPER)
/*     */     {
/* 115 */       return worldIn.getBlockState(pos.down()).getBlock() == this;
/*     */     }
/*     */     
/*     */ 
/* 119 */     IBlockState iblockstate = worldIn.getBlockState(pos.up());
/* 120 */     return (iblockstate.getBlock() == this) && (super.canBlockStay(worldIn, pos, iblockstate));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*     */   {
/* 129 */     if (state.getValue(HALF) == EnumBlockHalf.UPPER)
/*     */     {
/* 131 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 135 */     EnumPlantType blockdoubleplant$enumplanttype = (EnumPlantType)state.getValue(VARIANT);
/* 136 */     return blockdoubleplant$enumplanttype == EnumPlantType.GRASS ? null : rand.nextInt(8) == 0 ? Items.wheat_seeds : blockdoubleplant$enumplanttype == EnumPlantType.FERN ? null : Item.getItemFromBlock(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int damageDropped(IBlockState state)
/*     */   {
/* 146 */     return (state.getValue(HALF) != EnumBlockHalf.UPPER) && (state.getValue(VARIANT) != EnumPlantType.GRASS) ? ((EnumPlantType)state.getValue(VARIANT)).getMeta() : 0;
/*     */   }
/*     */   
/*     */   public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
/*     */   {
/* 151 */     EnumPlantType blockdoubleplant$enumplanttype = getVariant(worldIn, pos);
/* 152 */     return (blockdoubleplant$enumplanttype != EnumPlantType.GRASS) && (blockdoubleplant$enumplanttype != EnumPlantType.FERN) ? 16777215 : BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
/*     */   }
/*     */   
/*     */   public void placeAt(World worldIn, BlockPos lowerPos, EnumPlantType variant, int flags)
/*     */   {
/* 157 */     worldIn.setBlockState(lowerPos, getDefaultState().withProperty(HALF, EnumBlockHalf.LOWER).withProperty(VARIANT, variant), flags);
/* 158 */     worldIn.setBlockState(lowerPos.up(), getDefaultState().withProperty(HALF, EnumBlockHalf.UPPER), flags);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
/*     */   {
/* 166 */     worldIn.setBlockState(pos.up(), getDefaultState().withProperty(HALF, EnumBlockHalf.UPPER), 2);
/*     */   }
/*     */   
/*     */   public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te)
/*     */   {
/* 171 */     if ((worldIn.isRemote) || (player.getCurrentEquippedItem() == null) || (player.getCurrentEquippedItem().getItem() != Items.shears) || (state.getValue(HALF) != EnumBlockHalf.LOWER) || (!onHarvest(worldIn, pos, state, player)))
/*     */     {
/* 173 */       super.harvestBlock(worldIn, player, pos, state, te);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
/*     */   {
/* 179 */     if (state.getValue(HALF) == EnumBlockHalf.UPPER)
/*     */     {
/* 181 */       if (worldIn.getBlockState(pos.down()).getBlock() == this)
/*     */       {
/* 183 */         if (!player.capabilities.isCreativeMode)
/*     */         {
/* 185 */           IBlockState iblockstate = worldIn.getBlockState(pos.down());
/* 186 */           EnumPlantType blockdoubleplant$enumplanttype = (EnumPlantType)iblockstate.getValue(VARIANT);
/*     */           
/* 188 */           if ((blockdoubleplant$enumplanttype != EnumPlantType.FERN) && (blockdoubleplant$enumplanttype != EnumPlantType.GRASS))
/*     */           {
/* 190 */             worldIn.destroyBlock(pos.down(), true);
/*     */           }
/* 192 */           else if (!worldIn.isRemote)
/*     */           {
/* 194 */             if ((player.getCurrentEquippedItem() != null) && (player.getCurrentEquippedItem().getItem() == Items.shears))
/*     */             {
/* 196 */               onHarvest(worldIn, pos, iblockstate, player);
/* 197 */               worldIn.setBlockToAir(pos.down());
/*     */             }
/*     */             else
/*     */             {
/* 201 */               worldIn.destroyBlock(pos.down(), true);
/*     */             }
/*     */             
/*     */           }
/*     */           else {
/* 206 */             worldIn.setBlockToAir(pos.down());
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 211 */           worldIn.setBlockToAir(pos.down());
/*     */         }
/*     */       }
/*     */     }
/* 215 */     else if ((player.capabilities.isCreativeMode) && (worldIn.getBlockState(pos.up()).getBlock() == this))
/*     */     {
/* 217 */       worldIn.setBlockState(pos.up(), Blocks.air.getDefaultState(), 2);
/*     */     }
/*     */     
/* 220 */     super.onBlockHarvested(worldIn, pos, state, player);
/*     */   }
/*     */   
/*     */   private boolean onHarvest(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
/*     */   {
/* 225 */     EnumPlantType blockdoubleplant$enumplanttype = (EnumPlantType)state.getValue(VARIANT);
/*     */     
/* 227 */     if ((blockdoubleplant$enumplanttype != EnumPlantType.FERN) && (blockdoubleplant$enumplanttype != EnumPlantType.GRASS))
/*     */     {
/* 229 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 233 */     player.triggerAchievement(net.minecraft.stats.StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
/* 234 */     int i = (blockdoubleplant$enumplanttype == EnumPlantType.GRASS ? BlockTallGrass.EnumType.GRASS : BlockTallGrass.EnumType.FERN).getMeta();
/* 235 */     spawnAsEntity(worldIn, pos, new ItemStack(Blocks.tallgrass, 2, i));
/* 236 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
/*     */   {
/*     */     EnumPlantType[] arrayOfEnumPlantType;
/*     */     
/* 245 */     int j = (arrayOfEnumPlantType = EnumPlantType.values()).length; for (int i = 0; i < j; i++) { EnumPlantType blockdoubleplant$enumplanttype = arrayOfEnumPlantType[i];
/*     */       
/* 247 */       list.add(new ItemStack(itemIn, 1, blockdoubleplant$enumplanttype.getMeta()));
/*     */     }
/*     */   }
/*     */   
/*     */   public int getDamageValue(World worldIn, BlockPos pos)
/*     */   {
/* 253 */     return getVariant(worldIn, pos).getMeta();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
/*     */   {
/* 261 */     EnumPlantType blockdoubleplant$enumplanttype = getVariant(worldIn, pos);
/* 262 */     return (blockdoubleplant$enumplanttype != EnumPlantType.GRASS) && (blockdoubleplant$enumplanttype != EnumPlantType.FERN);
/*     */   }
/*     */   
/*     */   public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
/*     */   {
/* 267 */     return true;
/*     */   }
/*     */   
/*     */   public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
/*     */   {
/* 272 */     spawnAsEntity(worldIn, pos, new ItemStack(this, 1, getVariant(worldIn, pos).getMeta()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 280 */     return (meta & 0x8) > 0 ? getDefaultState().withProperty(HALF, EnumBlockHalf.UPPER) : getDefaultState().withProperty(HALF, EnumBlockHalf.LOWER).withProperty(VARIANT, EnumPlantType.byMetadata(meta & 0x7));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
/*     */   {
/* 289 */     if (state.getValue(HALF) == EnumBlockHalf.UPPER)
/*     */     {
/* 291 */       IBlockState iblockstate = worldIn.getBlockState(pos.down());
/*     */       
/* 293 */       if (iblockstate.getBlock() == this)
/*     */       {
/* 295 */         state = state.withProperty(VARIANT, (EnumPlantType)iblockstate.getValue(VARIANT));
/*     */       }
/*     */     }
/*     */     
/* 299 */     return state;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 307 */     return state.getValue(HALF) == EnumBlockHalf.UPPER ? 0x8 | ((EnumFacing)state.getValue(field_181084_N)).getHorizontalIndex() : ((EnumPlantType)state.getValue(VARIANT)).getMeta();
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 312 */     return new BlockState(this, new IProperty[] { HALF, VARIANT, field_181084_N });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Block.EnumOffsetType getOffsetType()
/*     */   {
/* 320 */     return Block.EnumOffsetType.XZ;
/*     */   }
/*     */   
/*     */   public static enum EnumBlockHalf implements IStringSerializable
/*     */   {
/* 325 */     UPPER, 
/* 326 */     LOWER;
/*     */     
/*     */     public String toString()
/*     */     {
/* 330 */       return getName();
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 335 */       return this == UPPER ? "upper" : "lower";
/*     */     }
/*     */   }
/*     */   
/*     */   public static enum EnumPlantType implements IStringSerializable
/*     */   {
/* 341 */     SUNFLOWER(0, "sunflower"), 
/* 342 */     SYRINGA(1, "syringa"), 
/* 343 */     GRASS(2, "double_grass", "grass"), 
/* 344 */     FERN(3, "double_fern", "fern"), 
/* 345 */     ROSE(4, "double_rose", "rose"), 
/* 346 */     PAEONIA(5, "paeonia");
/*     */     
/*     */     private static final EnumPlantType[] META_LOOKUP;
/*     */     private final int meta;
/*     */     private final String name;
/*     */     private final String unlocalizedName;
/*     */     
/*     */     private EnumPlantType(int meta, String name)
/*     */     {
/* 355 */       this(meta, name, name);
/*     */     }
/*     */     
/*     */     private EnumPlantType(int meta, String name, String unlocalizedName)
/*     */     {
/* 360 */       this.meta = meta;
/* 361 */       this.name = name;
/* 362 */       this.unlocalizedName = unlocalizedName;
/*     */     }
/*     */     
/*     */     public int getMeta()
/*     */     {
/* 367 */       return this.meta;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 372 */       return this.name;
/*     */     }
/*     */     
/*     */     public static EnumPlantType byMetadata(int meta)
/*     */     {
/* 377 */       if ((meta < 0) || (meta >= META_LOOKUP.length))
/*     */       {
/* 379 */         meta = 0;
/*     */       }
/*     */       
/* 382 */       return META_LOOKUP[meta];
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 387 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getUnlocalizedName()
/*     */     {
/* 392 */       return this.unlocalizedName;
/*     */     }
/*     */     
/*     */     static
/*     */     {
/* 348 */       META_LOOKUP = new EnumPlantType[values().length];
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
/*     */ 
/*     */ 
/*     */       EnumPlantType[] arrayOfEnumPlantType;
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
/*     */ 
/*     */ 
/* 396 */       int j = (arrayOfEnumPlantType = values()).length; for (int i = 0; i < j; i++) { EnumPlantType blockdoubleplant$enumplanttype = arrayOfEnumPlantType[i];
/*     */         
/* 398 */         META_LOOKUP[blockdoubleplant$enumplanttype.getMeta()] = blockdoubleplant$enumplanttype;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockDoublePlant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */