/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.BlockPos.MutableBlockPos;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.world.ColorizerFoliage;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeColorHelper;
/*     */ 
/*     */ public abstract class BlockLeaves extends BlockLeavesBase
/*     */ {
/*  21 */   public static final PropertyBool DECAYABLE = PropertyBool.create("decayable");
/*  22 */   public static final PropertyBool CHECK_DECAY = PropertyBool.create("check_decay");
/*     */   int[] surroundings;
/*     */   protected int iconIndex;
/*     */   protected boolean isTransparent;
/*     */   
/*     */   public BlockLeaves()
/*     */   {
/*  29 */     super(Material.leaves, false);
/*  30 */     setTickRandomly(true);
/*  31 */     setCreativeTab(CreativeTabs.tabDecorations);
/*  32 */     setHardness(0.2F);
/*  33 */     setLightOpacity(1);
/*  34 */     setStepSound(soundTypeGrass);
/*     */   }
/*     */   
/*     */   public int getBlockColor()
/*     */   {
/*  39 */     return ColorizerFoliage.getFoliageColor(0.5D, 1.0D);
/*     */   }
/*     */   
/*     */   public int getRenderColor(IBlockState state)
/*     */   {
/*  44 */     return ColorizerFoliage.getFoliageColorBasic();
/*     */   }
/*     */   
/*     */   public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
/*     */   {
/*  49 */     return BiomeColorHelper.getFoliageColorAtPos(worldIn, pos);
/*     */   }
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  54 */     int i = 1;
/*  55 */     int j = i + 1;
/*  56 */     int k = pos.getX();
/*  57 */     int l = pos.getY();
/*  58 */     int i1 = pos.getZ();
/*     */     
/*  60 */     if (worldIn.isAreaLoaded(new BlockPos(k - j, l - j, i1 - j), new BlockPos(k + j, l + j, i1 + j)))
/*     */     {
/*  62 */       for (int j1 = -i; j1 <= i; j1++)
/*     */       {
/*  64 */         for (int k1 = -i; k1 <= i; k1++)
/*     */         {
/*  66 */           for (int l1 = -i; l1 <= i; l1++)
/*     */           {
/*  68 */             BlockPos blockpos = pos.add(j1, k1, l1);
/*  69 */             IBlockState iblockstate = worldIn.getBlockState(blockpos);
/*     */             
/*  71 */             if ((iblockstate.getBlock().getMaterial() == Material.leaves) && (!((Boolean)iblockstate.getValue(CHECK_DECAY)).booleanValue()))
/*     */             {
/*  73 */               worldIn.setBlockState(blockpos, iblockstate.withProperty(CHECK_DECAY, Boolean.valueOf(true)), 4);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/*  83 */     if (!worldIn.isRemote)
/*     */     {
/*  85 */       if ((((Boolean)state.getValue(CHECK_DECAY)).booleanValue()) && (((Boolean)state.getValue(DECAYABLE)).booleanValue()))
/*     */       {
/*  87 */         int i = 4;
/*  88 */         int j = i + 1;
/*  89 */         int k = pos.getX();
/*  90 */         int l = pos.getY();
/*  91 */         int i1 = pos.getZ();
/*  92 */         int j1 = 32;
/*  93 */         int k1 = j1 * j1;
/*  94 */         int l1 = j1 / 2;
/*     */         
/*  96 */         if (this.surroundings == null)
/*     */         {
/*  98 */           this.surroundings = new int[j1 * j1 * j1];
/*     */         }
/*     */         
/* 101 */         if (worldIn.isAreaLoaded(new BlockPos(k - j, l - j, i1 - j), new BlockPos(k + j, l + j, i1 + j)))
/*     */         {
/* 103 */           BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*     */           
/* 105 */           for (int i2 = -i; i2 <= i; i2++)
/*     */           {
/* 107 */             for (int j2 = -i; j2 <= i; j2++)
/*     */             {
/* 109 */               for (int k2 = -i; k2 <= i; k2++)
/*     */               {
/* 111 */                 Block block = worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(k + i2, l + j2, i1 + k2)).getBlock();
/*     */                 
/* 113 */                 if ((block != Blocks.log) && (block != Blocks.log2))
/*     */                 {
/* 115 */                   if (block.getMaterial() == Material.leaves)
/*     */                   {
/* 117 */                     this.surroundings[((i2 + l1) * k1 + (j2 + l1) * j1 + k2 + l1)] = -2;
/*     */                   }
/*     */                   else
/*     */                   {
/* 121 */                     this.surroundings[((i2 + l1) * k1 + (j2 + l1) * j1 + k2 + l1)] = -1;
/*     */                   }
/*     */                   
/*     */                 }
/*     */                 else {
/* 126 */                   this.surroundings[((i2 + l1) * k1 + (j2 + l1) * j1 + k2 + l1)] = 0;
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */           
/* 132 */           for (int i3 = 1; i3 <= 4; i3++)
/*     */           {
/* 134 */             for (int j3 = -i; j3 <= i; j3++)
/*     */             {
/* 136 */               for (int k3 = -i; k3 <= i; k3++)
/*     */               {
/* 138 */                 for (int l3 = -i; l3 <= i; l3++)
/*     */                 {
/* 140 */                   if (this.surroundings[((j3 + l1) * k1 + (k3 + l1) * j1 + l3 + l1)] == i3 - 1)
/*     */                   {
/* 142 */                     if (this.surroundings[((j3 + l1 - 1) * k1 + (k3 + l1) * j1 + l3 + l1)] == -2)
/*     */                     {
/* 144 */                       this.surroundings[((j3 + l1 - 1) * k1 + (k3 + l1) * j1 + l3 + l1)] = i3;
/*     */                     }
/*     */                     
/* 147 */                     if (this.surroundings[((j3 + l1 + 1) * k1 + (k3 + l1) * j1 + l3 + l1)] == -2)
/*     */                     {
/* 149 */                       this.surroundings[((j3 + l1 + 1) * k1 + (k3 + l1) * j1 + l3 + l1)] = i3;
/*     */                     }
/*     */                     
/* 152 */                     if (this.surroundings[((j3 + l1) * k1 + (k3 + l1 - 1) * j1 + l3 + l1)] == -2)
/*     */                     {
/* 154 */                       this.surroundings[((j3 + l1) * k1 + (k3 + l1 - 1) * j1 + l3 + l1)] = i3;
/*     */                     }
/*     */                     
/* 157 */                     if (this.surroundings[((j3 + l1) * k1 + (k3 + l1 + 1) * j1 + l3 + l1)] == -2)
/*     */                     {
/* 159 */                       this.surroundings[((j3 + l1) * k1 + (k3 + l1 + 1) * j1 + l3 + l1)] = i3;
/*     */                     }
/*     */                     
/* 162 */                     if (this.surroundings[((j3 + l1) * k1 + (k3 + l1) * j1 + (l3 + l1 - 1))] == -2)
/*     */                     {
/* 164 */                       this.surroundings[((j3 + l1) * k1 + (k3 + l1) * j1 + (l3 + l1 - 1))] = i3;
/*     */                     }
/*     */                     
/* 167 */                     if (this.surroundings[((j3 + l1) * k1 + (k3 + l1) * j1 + l3 + l1 + 1)] == -2)
/*     */                     {
/* 169 */                       this.surroundings[((j3 + l1) * k1 + (k3 + l1) * j1 + l3 + l1 + 1)] = i3;
/*     */                     }
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 178 */         int l2 = this.surroundings[(l1 * k1 + l1 * j1 + l1)];
/*     */         
/* 180 */         if (l2 >= 0)
/*     */         {
/* 182 */           worldIn.setBlockState(pos, state.withProperty(CHECK_DECAY, Boolean.valueOf(false)), 4);
/*     */         }
/*     */         else
/*     */         {
/* 186 */           destroy(worldIn, pos);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/* 194 */     if ((worldIn.canLightningStrike(pos.up())) && (!World.doesBlockHaveSolidTopSurface(worldIn, pos.down())) && (rand.nextInt(15) == 1))
/*     */     {
/* 196 */       double d0 = pos.getX() + rand.nextFloat();
/* 197 */       double d1 = pos.getY() - 0.05D;
/* 198 */       double d2 = pos.getZ() + rand.nextFloat();
/* 199 */       worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   private void destroy(World worldIn, BlockPos pos)
/*     */   {
/* 205 */     dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
/* 206 */     worldIn.setBlockToAir(pos);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int quantityDropped(Random random)
/*     */   {
/* 214 */     return random.nextInt(20) == 0 ? 1 : 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*     */   {
/* 222 */     return Item.getItemFromBlock(Blocks.sapling);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
/*     */   {
/* 230 */     if (!worldIn.isRemote)
/*     */     {
/* 232 */       int i = getSaplingDropChance(state);
/*     */       
/* 234 */       if (fortune > 0)
/*     */       {
/* 236 */         i -= (2 << fortune);
/*     */         
/* 238 */         if (i < 10)
/*     */         {
/* 240 */           i = 10;
/*     */         }
/*     */       }
/*     */       
/* 244 */       if (worldIn.rand.nextInt(i) == 0)
/*     */       {
/* 246 */         Item item = getItemDropped(state, worldIn.rand, fortune);
/* 247 */         spawnAsEntity(worldIn, pos, new net.minecraft.item.ItemStack(item, 1, damageDropped(state)));
/*     */       }
/*     */       
/* 250 */       i = 200;
/*     */       
/* 252 */       if (fortune > 0)
/*     */       {
/* 254 */         i -= (10 << fortune);
/*     */         
/* 256 */         if (i < 40)
/*     */         {
/* 258 */           i = 40;
/*     */         }
/*     */       }
/*     */       
/* 262 */       dropApple(worldIn, pos, state, i);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance) {}
/*     */   
/*     */ 
/*     */   protected int getSaplingDropChance(IBlockState state)
/*     */   {
/* 272 */     return 20;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/* 280 */     return !this.fancyGraphics;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setGraphicsLevel(boolean fancy)
/*     */   {
/* 288 */     this.isTransparent = fancy;
/* 289 */     this.fancyGraphics = fancy;
/* 290 */     this.iconIndex = (fancy ? 0 : 1);
/*     */   }
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer()
/*     */   {
/* 295 */     return this.isTransparent ? EnumWorldBlockLayer.CUTOUT_MIPPED : EnumWorldBlockLayer.SOLID;
/*     */   }
/*     */   
/*     */   public boolean isVisuallyOpaque()
/*     */   {
/* 300 */     return false;
/*     */   }
/*     */   
/*     */   public abstract BlockPlanks.EnumType getWoodType(int paramInt);
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockLeaves.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */