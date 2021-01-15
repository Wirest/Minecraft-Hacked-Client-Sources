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
/*     */ import net.minecraft.entity.monster.EntitySilverfish;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockSilverfish extends Block
/*     */ {
/*  21 */   public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
/*     */   
/*     */   public BlockSilverfish()
/*     */   {
/*  25 */     super(Material.clay);
/*  26 */     setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.STONE));
/*  27 */     setHardness(0.0F);
/*  28 */     setCreativeTab(CreativeTabs.tabDecorations);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int quantityDropped(Random random)
/*     */   {
/*  36 */     return 0;
/*     */   }
/*     */   
/*     */   public static boolean canContainSilverfish(IBlockState blockState)
/*     */   {
/*  41 */     Block block = blockState.getBlock();
/*  42 */     return (blockState == Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE)) || (block == Blocks.cobblestone) || (block == Blocks.stonebrick);
/*     */   }
/*     */   
/*     */   protected ItemStack createStackedBlock(IBlockState state)
/*     */   {
/*  47 */     switch ((EnumType)state.getValue(VARIANT))
/*     */     {
/*     */     case COBBLESTONE: 
/*  50 */       return new ItemStack(Blocks.cobblestone);
/*     */     
/*     */     case CRACKED_STONEBRICK: 
/*  53 */       return new ItemStack(Blocks.stonebrick);
/*     */     
/*     */     case MOSSY_STONEBRICK: 
/*  56 */       return new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.EnumType.MOSSY.getMetadata());
/*     */     
/*     */     case STONE: 
/*  59 */       return new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.EnumType.CRACKED.getMetadata());
/*     */     
/*     */     case STONEBRICK: 
/*  62 */       return new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.EnumType.CHISELED.getMetadata());
/*     */     }
/*     */     
/*  65 */     return new ItemStack(Blocks.stone);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
/*     */   {
/*  74 */     if ((!worldIn.isRemote) && (worldIn.getGameRules().getBoolean("doTileDrops")))
/*     */     {
/*  76 */       EntitySilverfish entitysilverfish = new EntitySilverfish(worldIn);
/*  77 */       entitysilverfish.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
/*  78 */       worldIn.spawnEntityInWorld(entitysilverfish);
/*  79 */       entitysilverfish.spawnExplosionParticle();
/*     */     }
/*     */   }
/*     */   
/*     */   public int getDamageValue(World worldIn, BlockPos pos)
/*     */   {
/*  85 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*  86 */     return iblockstate.getBlock().getMetaFromState(iblockstate);
/*     */   }
/*     */   
/*     */ 
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
/*     */   {
/*     */     EnumType[] arrayOfEnumType;
/*     */     
/*  94 */     int j = (arrayOfEnumType = EnumType.values()).length; for (int i = 0; i < j; i++) { EnumType blocksilverfish$enumtype = arrayOfEnumType[i];
/*     */       
/*  96 */       list.add(new ItemStack(itemIn, 1, blocksilverfish$enumtype.getMetadata()));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 105 */     return getDefaultState().withProperty(VARIANT, EnumType.byMetadata(meta));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 113 */     return ((EnumType)state.getValue(VARIANT)).getMetadata();
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 118 */     return new BlockState(this, new IProperty[] { VARIANT });
/*     */   }
/*     */   
/*     */   public static abstract enum EnumType implements IStringSerializable
/*     */   {
/* 123 */     STONE(0, "stone"), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 130 */     COBBLESTONE(1, "cobblestone", "cobble"), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 137 */     STONEBRICK(2, "stone_brick", "brick"), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 144 */     MOSSY_STONEBRICK(3, "mossy_brick", "mossybrick"), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 151 */     CRACKED_STONEBRICK(4, "cracked_brick", "crackedbrick"), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 158 */     CHISELED_STONEBRICK(5, "chiseled_brick", "chiseledbrick");
/*     */     
/*     */ 
/*     */ 
/*     */     private static final EnumType[] META_LOOKUP;
/*     */     
/*     */     private final int meta;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final String unlocalizedName;
/*     */     
/*     */ 
/*     */     private EnumType(int meta, String name)
/*     */     {
/* 173 */       this(meta, name, name);
/*     */     }
/*     */     
/*     */     private EnumType(int meta, String name, String unlocalizedName)
/*     */     {
/* 178 */       this.meta = meta;
/* 179 */       this.name = name;
/* 180 */       this.unlocalizedName = unlocalizedName;
/*     */     }
/*     */     
/*     */     public int getMetadata()
/*     */     {
/* 185 */       return this.meta;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 190 */       return this.name;
/*     */     }
/*     */     
/*     */     public static EnumType byMetadata(int meta)
/*     */     {
/* 195 */       if ((meta < 0) || (meta >= META_LOOKUP.length))
/*     */       {
/* 197 */         meta = 0;
/*     */       }
/*     */       
/* 200 */       return META_LOOKUP[meta];
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 205 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getUnlocalizedName()
/*     */     {
/* 210 */       return this.unlocalizedName;
/*     */     }
/*     */     
/*     */ 
/*     */     public static EnumType forModelBlock(IBlockState model)
/*     */     {
/*     */       EnumType[] arrayOfEnumType;
/* 217 */       int j = (arrayOfEnumType = values()).length; for (int i = 0; i < j; i++) { EnumType blocksilverfish$enumtype = arrayOfEnumType[i];
/*     */         
/* 219 */         if (model == blocksilverfish$enumtype.getModelBlock())
/*     */         {
/* 221 */           return blocksilverfish$enumtype;
/*     */         }
/*     */       }
/*     */       
/* 225 */       return STONE;
/*     */     }
/*     */     
/*     */     static
/*     */     {
/* 166 */       META_LOOKUP = new EnumType[values().length];
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
/* 229 */       int j = (arrayOfEnumType = values()).length; for (int i = 0; i < j; i++) { EnumType blocksilverfish$enumtype = arrayOfEnumType[i];
/*     */         
/* 231 */         META_LOOKUP[blocksilverfish$enumtype.getMetadata()] = blocksilverfish$enumtype;
/*     */       }
/*     */     }
/*     */     
/*     */     public abstract IBlockState getModelBlock();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockSilverfish.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */