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
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.world.ColorizerGrass;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ 
/*     */ public class BlockTallGrass extends BlockBush implements IGrowable
/*     */ {
/*  26 */   public static final PropertyEnum<EnumType> TYPE = PropertyEnum.create("type", EnumType.class);
/*     */   
/*     */   protected BlockTallGrass()
/*     */   {
/*  30 */     super(Material.vine);
/*  31 */     setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumType.DEAD_BUSH));
/*  32 */     float f = 0.4F;
/*  33 */     setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.8F, 0.5F + f);
/*     */   }
/*     */   
/*     */   public int getBlockColor()
/*     */   {
/*  38 */     return ColorizerGrass.getGrassColor(0.5D, 1.0D);
/*     */   }
/*     */   
/*     */   public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  43 */     return canPlaceBlockOn(worldIn.getBlockState(pos.down()).getBlock());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isReplaceable(World worldIn, BlockPos pos)
/*     */   {
/*  51 */     return true;
/*     */   }
/*     */   
/*     */   public int getRenderColor(IBlockState state)
/*     */   {
/*  56 */     if (state.getBlock() != this)
/*     */     {
/*  58 */       return super.getRenderColor(state);
/*     */     }
/*     */     
/*     */ 
/*  62 */     EnumType blocktallgrass$enumtype = (EnumType)state.getValue(TYPE);
/*  63 */     return blocktallgrass$enumtype == EnumType.DEAD_BUSH ? 16777215 : ColorizerGrass.getGrassColor(0.5D, 1.0D);
/*     */   }
/*     */   
/*     */ 
/*     */   public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
/*     */   {
/*  69 */     return worldIn.getBiomeGenForCoords(pos).getGrassColorAtPos(pos);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*     */   {
/*  77 */     return rand.nextInt(8) == 0 ? Items.wheat_seeds : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int quantityDroppedWithBonus(int fortune, Random random)
/*     */   {
/*  85 */     return 1 + random.nextInt(fortune * 2 + 1);
/*     */   }
/*     */   
/*     */   public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te)
/*     */   {
/*  90 */     if ((!worldIn.isRemote) && (player.getCurrentEquippedItem() != null) && (player.getCurrentEquippedItem().getItem() == Items.shears))
/*     */     {
/*  92 */       player.triggerAchievement(net.minecraft.stats.StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
/*  93 */       spawnAsEntity(worldIn, pos, new ItemStack(Blocks.tallgrass, 1, ((EnumType)state.getValue(TYPE)).getMeta()));
/*     */     }
/*     */     else
/*     */     {
/*  97 */       super.harvestBlock(worldIn, player, pos, state, te);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getDamageValue(World worldIn, BlockPos pos)
/*     */   {
/* 103 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/* 104 */     return iblockstate.getBlock().getMetaFromState(iblockstate);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
/*     */   {
/* 112 */     for (int i = 1; i < 3; i++)
/*     */     {
/* 114 */       list.add(new ItemStack(itemIn, 1, i));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
/*     */   {
/* 123 */     return state.getValue(TYPE) != EnumType.DEAD_BUSH;
/*     */   }
/*     */   
/*     */   public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
/*     */   {
/* 128 */     return true;
/*     */   }
/*     */   
/*     */   public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
/*     */   {
/* 133 */     BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = BlockDoublePlant.EnumPlantType.GRASS;
/*     */     
/* 135 */     if (state.getValue(TYPE) == EnumType.FERN)
/*     */     {
/* 137 */       blockdoubleplant$enumplanttype = BlockDoublePlant.EnumPlantType.FERN;
/*     */     }
/*     */     
/* 140 */     if (Blocks.double_plant.canPlaceBlockAt(worldIn, pos))
/*     */     {
/* 142 */       Blocks.double_plant.placeAt(worldIn, pos, blockdoubleplant$enumplanttype, 2);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 151 */     return getDefaultState().withProperty(TYPE, EnumType.byMetadata(meta));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 159 */     return ((EnumType)state.getValue(TYPE)).getMeta();
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 164 */     return new BlockState(this, new IProperty[] { TYPE });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Block.EnumOffsetType getOffsetType()
/*     */   {
/* 172 */     return Block.EnumOffsetType.XYZ;
/*     */   }
/*     */   
/*     */   public static enum EnumType implements IStringSerializable
/*     */   {
/* 177 */     DEAD_BUSH(0, "dead_bush"), 
/* 178 */     GRASS(1, "tall_grass"), 
/* 179 */     FERN(2, "fern");
/*     */     
/*     */     private static final EnumType[] META_LOOKUP;
/*     */     private final int meta;
/*     */     private final String name;
/*     */     
/*     */     private EnumType(int meta, String name)
/*     */     {
/* 187 */       this.meta = meta;
/* 188 */       this.name = name;
/*     */     }
/*     */     
/*     */     public int getMeta()
/*     */     {
/* 193 */       return this.meta;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 198 */       return this.name;
/*     */     }
/*     */     
/*     */     public static EnumType byMetadata(int meta)
/*     */     {
/* 203 */       if ((meta < 0) || (meta >= META_LOOKUP.length))
/*     */       {
/* 205 */         meta = 0;
/*     */       }
/*     */       
/* 208 */       return META_LOOKUP[meta];
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 213 */       return this.name;
/*     */     }
/*     */     
/*     */     static
/*     */     {
/* 181 */       META_LOOKUP = new EnumType[values().length];
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
/* 217 */       int j = (arrayOfEnumType = values()).length; for (int i = 0; i < j; i++) { EnumType blocktallgrass$enumtype = arrayOfEnumType[i];
/*     */         
/* 219 */         META_LOOKUP[blocktallgrass$enumtype.getMeta()] = blocktallgrass$enumtype;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockTallGrass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */