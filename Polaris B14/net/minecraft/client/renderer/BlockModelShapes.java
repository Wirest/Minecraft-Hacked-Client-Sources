/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockBed;
/*     */ import net.minecraft.block.BlockCactus;
/*     */ import net.minecraft.block.BlockColored;
/*     */ import net.minecraft.block.BlockCommandBlock;
/*     */ import net.minecraft.block.BlockDirt;
/*     */ import net.minecraft.block.BlockDirt.DirtType;
/*     */ import net.minecraft.block.BlockDispenser;
/*     */ import net.minecraft.block.BlockDoor;
/*     */ import net.minecraft.block.BlockDoublePlant;
/*     */ import net.minecraft.block.BlockDropper;
/*     */ import net.minecraft.block.BlockFenceGate;
/*     */ import net.minecraft.block.BlockFire;
/*     */ import net.minecraft.block.BlockFlower;
/*     */ import net.minecraft.block.BlockFlowerPot;
/*     */ import net.minecraft.block.BlockJukebox;
/*     */ import net.minecraft.block.BlockLeaves;
/*     */ import net.minecraft.block.BlockOldLeaf;
/*     */ import net.minecraft.block.BlockOldLog;
/*     */ import net.minecraft.block.BlockPlanks;
/*     */ import net.minecraft.block.BlockPrismarine;
/*     */ import net.minecraft.block.BlockQuartz;
/*     */ import net.minecraft.block.BlockQuartz.EnumType;
/*     */ import net.minecraft.block.BlockRedSandstone;
/*     */ import net.minecraft.block.BlockRedstoneWire;
/*     */ import net.minecraft.block.BlockReed;
/*     */ import net.minecraft.block.BlockSand;
/*     */ import net.minecraft.block.BlockSilverfish;
/*     */ import net.minecraft.block.BlockStem;
/*     */ import net.minecraft.block.BlockStone;
/*     */ import net.minecraft.block.BlockStoneBrick;
/*     */ import net.minecraft.block.BlockStoneSlab;
/*     */ import net.minecraft.block.BlockStoneSlab.EnumType;
/*     */ import net.minecraft.block.BlockStoneSlabNew;
/*     */ import net.minecraft.block.BlockTNT;
/*     */ import net.minecraft.block.BlockTallGrass;
/*     */ import net.minecraft.block.BlockTripWire;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.renderer.block.statemap.BlockStateMapper;
/*     */ import net.minecraft.client.renderer.block.statemap.IStateMapper;
/*     */ import net.minecraft.client.renderer.block.statemap.StateMap.Builder;
/*     */ import net.minecraft.client.renderer.block.statemap.StateMapperBase;
/*     */ import net.minecraft.client.renderer.texture.TextureMap;
/*     */ import net.minecraft.client.resources.model.IBakedModel;
/*     */ import net.minecraft.client.resources.model.ModelManager;
/*     */ import net.minecraft.client.resources.model.ModelResourceLocation;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.RegistryNamespacedDefaultedByKey;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public class BlockModelShapes
/*     */ {
/*  61 */   private final Map<IBlockState, IBakedModel> bakedModelStore = Maps.newIdentityHashMap();
/*  62 */   private final BlockStateMapper blockStateMapper = new BlockStateMapper();
/*     */   private final ModelManager modelManager;
/*     */   
/*     */   public BlockModelShapes(ModelManager manager)
/*     */   {
/*  67 */     this.modelManager = manager;
/*  68 */     registerAllBlocks();
/*     */   }
/*     */   
/*     */   public BlockStateMapper getBlockStateMapper()
/*     */   {
/*  73 */     return this.blockStateMapper;
/*     */   }
/*     */   
/*     */   public net.minecraft.client.renderer.texture.TextureAtlasSprite getTexture(IBlockState state)
/*     */   {
/*  78 */     Block block = state.getBlock();
/*  79 */     IBakedModel ibakedmodel = getModelForState(state);
/*     */     
/*  81 */     if ((ibakedmodel == null) || (ibakedmodel == this.modelManager.getMissingModel()))
/*     */     {
/*  83 */       if ((block == Blocks.wall_sign) || (block == Blocks.standing_sign) || (block == Blocks.chest) || (block == Blocks.trapped_chest) || (block == Blocks.standing_banner) || (block == Blocks.wall_banner))
/*     */       {
/*  85 */         return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/planks_oak");
/*     */       }
/*     */       
/*  88 */       if (block == Blocks.ender_chest)
/*     */       {
/*  90 */         return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/obsidian");
/*     */       }
/*     */       
/*  93 */       if ((block == Blocks.flowing_lava) || (block == Blocks.lava))
/*     */       {
/*  95 */         return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/lava_still");
/*     */       }
/*     */       
/*  98 */       if ((block == Blocks.flowing_water) || (block == Blocks.water))
/*     */       {
/* 100 */         return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/water_still");
/*     */       }
/*     */       
/* 103 */       if (block == Blocks.skull)
/*     */       {
/* 105 */         return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/soul_sand");
/*     */       }
/*     */       
/* 108 */       if (block == Blocks.barrier)
/*     */       {
/* 110 */         return this.modelManager.getTextureMap().getAtlasSprite("minecraft:items/barrier");
/*     */       }
/*     */     }
/*     */     
/* 114 */     if (ibakedmodel == null)
/*     */     {
/* 116 */       ibakedmodel = this.modelManager.getMissingModel();
/*     */     }
/*     */     
/* 119 */     return ibakedmodel.getParticleTexture();
/*     */   }
/*     */   
/*     */   public IBakedModel getModelForState(IBlockState state)
/*     */   {
/* 124 */     IBakedModel ibakedmodel = (IBakedModel)this.bakedModelStore.get(state);
/*     */     
/* 126 */     if (ibakedmodel == null)
/*     */     {
/* 128 */       ibakedmodel = this.modelManager.getMissingModel();
/*     */     }
/*     */     
/* 131 */     return ibakedmodel;
/*     */   }
/*     */   
/*     */   public ModelManager getModelManager()
/*     */   {
/* 136 */     return this.modelManager;
/*     */   }
/*     */   
/*     */   public void reloadModels()
/*     */   {
/* 141 */     this.bakedModelStore.clear();
/*     */     
/* 143 */     for (Map.Entry<IBlockState, ModelResourceLocation> entry : this.blockStateMapper.putAllStateModelLocations().entrySet())
/*     */     {
/* 145 */       this.bakedModelStore.put((IBlockState)entry.getKey(), this.modelManager.getModel((ModelResourceLocation)entry.getValue()));
/*     */     }
/*     */   }
/*     */   
/*     */   public void registerBlockWithStateMapper(Block assoc, IStateMapper stateMapper)
/*     */   {
/* 151 */     this.blockStateMapper.registerBlockStateMapper(assoc, stateMapper);
/*     */   }
/*     */   
/*     */   public void registerBuiltInBlocks(Block... builtIns)
/*     */   {
/* 156 */     this.blockStateMapper.registerBuiltInBlocks(builtIns);
/*     */   }
/*     */   
/*     */   private void registerAllBlocks()
/*     */   {
/* 161 */     registerBuiltInBlocks(new Block[] { Blocks.air, Blocks.flowing_water, Blocks.water, Blocks.flowing_lava, Blocks.lava, Blocks.piston_extension, Blocks.chest, Blocks.ender_chest, Blocks.trapped_chest, Blocks.standing_sign, Blocks.skull, Blocks.end_portal, Blocks.barrier, Blocks.wall_sign, Blocks.wall_banner, Blocks.standing_banner });
/* 162 */     registerBlockWithStateMapper(Blocks.stone, new StateMap.Builder().withName(BlockStone.VARIANT).build());
/* 163 */     registerBlockWithStateMapper(Blocks.prismarine, new StateMap.Builder().withName(BlockPrismarine.VARIANT).build());
/* 164 */     registerBlockWithStateMapper(Blocks.leaves, new StateMap.Builder().withName(BlockOldLeaf.VARIANT).withSuffix("_leaves").ignore(new IProperty[] { BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE }).build());
/* 165 */     registerBlockWithStateMapper(Blocks.leaves2, new StateMap.Builder().withName(net.minecraft.block.BlockNewLeaf.VARIANT).withSuffix("_leaves").ignore(new IProperty[] { BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE }).build());
/* 166 */     registerBlockWithStateMapper(Blocks.cactus, new StateMap.Builder().ignore(new IProperty[] { BlockCactus.AGE }).build());
/* 167 */     registerBlockWithStateMapper(Blocks.reeds, new StateMap.Builder().ignore(new IProperty[] { BlockReed.AGE }).build());
/* 168 */     registerBlockWithStateMapper(Blocks.jukebox, new StateMap.Builder().ignore(new IProperty[] { BlockJukebox.HAS_RECORD }).build());
/* 169 */     registerBlockWithStateMapper(Blocks.command_block, new StateMap.Builder().ignore(new IProperty[] { BlockCommandBlock.TRIGGERED }).build());
/* 170 */     registerBlockWithStateMapper(Blocks.cobblestone_wall, new StateMap.Builder().withName(net.minecraft.block.BlockWall.VARIANT).withSuffix("_wall").build());
/* 171 */     registerBlockWithStateMapper(Blocks.double_plant, new StateMap.Builder().withName(BlockDoublePlant.VARIANT).ignore(new IProperty[] { BlockDoublePlant.field_181084_N }).build());
/* 172 */     registerBlockWithStateMapper(Blocks.oak_fence_gate, new StateMap.Builder().ignore(new IProperty[] { BlockFenceGate.POWERED }).build());
/* 173 */     registerBlockWithStateMapper(Blocks.spruce_fence_gate, new StateMap.Builder().ignore(new IProperty[] { BlockFenceGate.POWERED }).build());
/* 174 */     registerBlockWithStateMapper(Blocks.birch_fence_gate, new StateMap.Builder().ignore(new IProperty[] { BlockFenceGate.POWERED }).build());
/* 175 */     registerBlockWithStateMapper(Blocks.jungle_fence_gate, new StateMap.Builder().ignore(new IProperty[] { BlockFenceGate.POWERED }).build());
/* 176 */     registerBlockWithStateMapper(Blocks.dark_oak_fence_gate, new StateMap.Builder().ignore(new IProperty[] { BlockFenceGate.POWERED }).build());
/* 177 */     registerBlockWithStateMapper(Blocks.acacia_fence_gate, new StateMap.Builder().ignore(new IProperty[] { BlockFenceGate.POWERED }).build());
/* 178 */     registerBlockWithStateMapper(Blocks.tripwire, new StateMap.Builder().ignore(new IProperty[] { BlockTripWire.DISARMED, BlockTripWire.POWERED }).build());
/* 179 */     registerBlockWithStateMapper(Blocks.double_wooden_slab, new StateMap.Builder().withName(BlockPlanks.VARIANT).withSuffix("_double_slab").build());
/* 180 */     registerBlockWithStateMapper(Blocks.wooden_slab, new StateMap.Builder().withName(BlockPlanks.VARIANT).withSuffix("_slab").build());
/* 181 */     registerBlockWithStateMapper(Blocks.tnt, new StateMap.Builder().ignore(new IProperty[] { BlockTNT.EXPLODE }).build());
/* 182 */     registerBlockWithStateMapper(Blocks.fire, new StateMap.Builder().ignore(new IProperty[] { BlockFire.AGE }).build());
/* 183 */     registerBlockWithStateMapper(Blocks.redstone_wire, new StateMap.Builder().ignore(new IProperty[] { BlockRedstoneWire.POWER }).build());
/* 184 */     registerBlockWithStateMapper(Blocks.oak_door, new StateMap.Builder().ignore(new IProperty[] { BlockDoor.POWERED }).build());
/* 185 */     registerBlockWithStateMapper(Blocks.spruce_door, new StateMap.Builder().ignore(new IProperty[] { BlockDoor.POWERED }).build());
/* 186 */     registerBlockWithStateMapper(Blocks.birch_door, new StateMap.Builder().ignore(new IProperty[] { BlockDoor.POWERED }).build());
/* 187 */     registerBlockWithStateMapper(Blocks.jungle_door, new StateMap.Builder().ignore(new IProperty[] { BlockDoor.POWERED }).build());
/* 188 */     registerBlockWithStateMapper(Blocks.acacia_door, new StateMap.Builder().ignore(new IProperty[] { BlockDoor.POWERED }).build());
/* 189 */     registerBlockWithStateMapper(Blocks.dark_oak_door, new StateMap.Builder().ignore(new IProperty[] { BlockDoor.POWERED }).build());
/* 190 */     registerBlockWithStateMapper(Blocks.iron_door, new StateMap.Builder().ignore(new IProperty[] { BlockDoor.POWERED }).build());
/* 191 */     registerBlockWithStateMapper(Blocks.wool, new StateMap.Builder().withName(BlockColored.COLOR).withSuffix("_wool").build());
/* 192 */     registerBlockWithStateMapper(Blocks.carpet, new StateMap.Builder().withName(BlockColored.COLOR).withSuffix("_carpet").build());
/* 193 */     registerBlockWithStateMapper(Blocks.stained_hardened_clay, new StateMap.Builder().withName(BlockColored.COLOR).withSuffix("_stained_hardened_clay").build());
/* 194 */     registerBlockWithStateMapper(Blocks.stained_glass_pane, new StateMap.Builder().withName(BlockColored.COLOR).withSuffix("_stained_glass_pane").build());
/* 195 */     registerBlockWithStateMapper(Blocks.stained_glass, new StateMap.Builder().withName(BlockColored.COLOR).withSuffix("_stained_glass").build());
/* 196 */     registerBlockWithStateMapper(Blocks.sandstone, new StateMap.Builder().withName(net.minecraft.block.BlockSandStone.TYPE).build());
/* 197 */     registerBlockWithStateMapper(Blocks.red_sandstone, new StateMap.Builder().withName(BlockRedSandstone.TYPE).build());
/* 198 */     registerBlockWithStateMapper(Blocks.tallgrass, new StateMap.Builder().withName(BlockTallGrass.TYPE).build());
/* 199 */     registerBlockWithStateMapper(Blocks.bed, new StateMap.Builder().ignore(new IProperty[] { BlockBed.OCCUPIED }).build());
/* 200 */     registerBlockWithStateMapper(Blocks.yellow_flower, new StateMap.Builder().withName(Blocks.yellow_flower.getTypeProperty()).build());
/* 201 */     registerBlockWithStateMapper(Blocks.red_flower, new StateMap.Builder().withName(Blocks.red_flower.getTypeProperty()).build());
/* 202 */     registerBlockWithStateMapper(Blocks.stone_slab, new StateMap.Builder().withName(BlockStoneSlab.VARIANT).withSuffix("_slab").build());
/* 203 */     registerBlockWithStateMapper(Blocks.stone_slab2, new StateMap.Builder().withName(BlockStoneSlabNew.VARIANT).withSuffix("_slab").build());
/* 204 */     registerBlockWithStateMapper(Blocks.monster_egg, new StateMap.Builder().withName(BlockSilverfish.VARIANT).withSuffix("_monster_egg").build());
/* 205 */     registerBlockWithStateMapper(Blocks.stonebrick, new StateMap.Builder().withName(BlockStoneBrick.VARIANT).build());
/* 206 */     registerBlockWithStateMapper(Blocks.dispenser, new StateMap.Builder().ignore(new IProperty[] { BlockDispenser.TRIGGERED }).build());
/* 207 */     registerBlockWithStateMapper(Blocks.dropper, new StateMap.Builder().ignore(new IProperty[] { BlockDropper.TRIGGERED }).build());
/* 208 */     registerBlockWithStateMapper(Blocks.log, new StateMap.Builder().withName(BlockOldLog.VARIANT).withSuffix("_log").build());
/* 209 */     registerBlockWithStateMapper(Blocks.log2, new StateMap.Builder().withName(net.minecraft.block.BlockNewLog.VARIANT).withSuffix("_log").build());
/* 210 */     registerBlockWithStateMapper(Blocks.planks, new StateMap.Builder().withName(BlockPlanks.VARIANT).withSuffix("_planks").build());
/* 211 */     registerBlockWithStateMapper(Blocks.sapling, new StateMap.Builder().withName(net.minecraft.block.BlockSapling.TYPE).withSuffix("_sapling").build());
/* 212 */     registerBlockWithStateMapper(Blocks.sand, new StateMap.Builder().withName(BlockSand.VARIANT).build());
/* 213 */     registerBlockWithStateMapper(Blocks.hopper, new StateMap.Builder().ignore(new IProperty[] { net.minecraft.block.BlockHopper.ENABLED }).build());
/* 214 */     registerBlockWithStateMapper(Blocks.flower_pot, new StateMap.Builder().ignore(new IProperty[] { BlockFlowerPot.LEGACY_DATA }).build());
/* 215 */     registerBlockWithStateMapper(Blocks.quartz_block, new StateMapperBase()
/*     */     {
/*     */       protected ModelResourceLocation getModelResourceLocation(IBlockState state)
/*     */       {
/* 219 */         BlockQuartz.EnumType blockquartz$enumtype = (BlockQuartz.EnumType)state.getValue(BlockQuartz.VARIANT);
/*     */         
/* 221 */         switch (blockquartz$enumtype)
/*     */         {
/*     */         case CHISELED: 
/*     */         default: 
/* 225 */           return new ModelResourceLocation("quartz_block", "normal");
/*     */         
/*     */         case DEFAULT: 
/* 228 */           return new ModelResourceLocation("chiseled_quartz_block", "normal");
/*     */         
/*     */         case LINES_X: 
/* 231 */           return new ModelResourceLocation("quartz_column", "axis=y");
/*     */         
/*     */         case LINES_Y: 
/* 234 */           return new ModelResourceLocation("quartz_column", "axis=x");
/*     */         }
/*     */         
/* 237 */         return new ModelResourceLocation("quartz_column", "axis=z");
/*     */       }
/*     */       
/* 240 */     });
/* 241 */     registerBlockWithStateMapper(Blocks.deadbush, new StateMapperBase()
/*     */     {
/*     */       protected ModelResourceLocation getModelResourceLocation(IBlockState state)
/*     */       {
/* 245 */         return new ModelResourceLocation("dead_bush", "normal");
/*     */       }
/* 247 */     });
/* 248 */     registerBlockWithStateMapper(Blocks.pumpkin_stem, new StateMapperBase()
/*     */     {
/*     */       protected ModelResourceLocation getModelResourceLocation(IBlockState state)
/*     */       {
/* 252 */         Map<IProperty, Comparable> map = Maps.newLinkedHashMap(state.getProperties());
/*     */         
/* 254 */         if (state.getValue(BlockStem.FACING) != EnumFacing.UP)
/*     */         {
/* 256 */           map.remove(BlockStem.AGE);
/*     */         }
/*     */         
/* 259 */         return new ModelResourceLocation((ResourceLocation)Block.blockRegistry.getNameForObject(state.getBlock()), getPropertyString(map));
/*     */       }
/* 261 */     });
/* 262 */     registerBlockWithStateMapper(Blocks.melon_stem, new StateMapperBase()
/*     */     {
/*     */       protected ModelResourceLocation getModelResourceLocation(IBlockState state)
/*     */       {
/* 266 */         Map<IProperty, Comparable> map = Maps.newLinkedHashMap(state.getProperties());
/*     */         
/* 268 */         if (state.getValue(BlockStem.FACING) != EnumFacing.UP)
/*     */         {
/* 270 */           map.remove(BlockStem.AGE);
/*     */         }
/*     */         
/* 273 */         return new ModelResourceLocation((ResourceLocation)Block.blockRegistry.getNameForObject(state.getBlock()), getPropertyString(map));
/*     */       }
/* 275 */     });
/* 276 */     registerBlockWithStateMapper(Blocks.dirt, new StateMapperBase()
/*     */     {
/*     */       protected ModelResourceLocation getModelResourceLocation(IBlockState state)
/*     */       {
/* 280 */         Map<IProperty, Comparable> map = Maps.newLinkedHashMap(state.getProperties());
/* 281 */         String s = BlockDirt.VARIANT.getName((BlockDirt.DirtType)map.remove(BlockDirt.VARIANT));
/*     */         
/* 283 */         if (BlockDirt.DirtType.PODZOL != state.getValue(BlockDirt.VARIANT))
/*     */         {
/* 285 */           map.remove(BlockDirt.SNOWY);
/*     */         }
/*     */         
/* 288 */         return new ModelResourceLocation(s, getPropertyString(map));
/*     */       }
/* 290 */     });
/* 291 */     registerBlockWithStateMapper(Blocks.double_stone_slab, new StateMapperBase()
/*     */     {
/*     */       protected ModelResourceLocation getModelResourceLocation(IBlockState state)
/*     */       {
/* 295 */         Map<IProperty, Comparable> map = Maps.newLinkedHashMap(state.getProperties());
/* 296 */         String s = BlockStoneSlab.VARIANT.getName((BlockStoneSlab.EnumType)map.remove(BlockStoneSlab.VARIANT));
/* 297 */         map.remove(BlockStoneSlab.SEAMLESS);
/* 298 */         String s1 = ((Boolean)state.getValue(BlockStoneSlab.SEAMLESS)).booleanValue() ? "all" : "normal";
/* 299 */         return new ModelResourceLocation(s + "_double_slab", s1);
/*     */       }
/* 301 */     });
/* 302 */     registerBlockWithStateMapper(Blocks.double_stone_slab2, new StateMapperBase()
/*     */     {
/*     */       protected ModelResourceLocation getModelResourceLocation(IBlockState state)
/*     */       {
/* 306 */         Map<IProperty, Comparable> map = Maps.newLinkedHashMap(state.getProperties());
/* 307 */         String s = BlockStoneSlabNew.VARIANT.getName((net.minecraft.block.BlockStoneSlabNew.EnumType)map.remove(BlockStoneSlabNew.VARIANT));
/* 308 */         map.remove(BlockStoneSlab.SEAMLESS);
/* 309 */         String s1 = ((Boolean)state.getValue(BlockStoneSlabNew.SEAMLESS)).booleanValue() ? "all" : "normal";
/* 310 */         return new ModelResourceLocation(s + "_double_slab", s1);
/*     */       }
/*     */     });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\BlockModelShapes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */