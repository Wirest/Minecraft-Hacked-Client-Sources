/*     */ package net.minecraft.client.resources.model;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Queues;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.renderer.BlockModelShapes;
/*     */ import net.minecraft.client.renderer.block.model.BakedQuad;
/*     */ import net.minecraft.client.renderer.block.model.BlockPart;
/*     */ import net.minecraft.client.renderer.block.model.BlockPartFace;
/*     */ import net.minecraft.client.renderer.block.model.FaceBakery;
/*     */ import net.minecraft.client.renderer.block.model.ItemModelGenerator;
/*     */ import net.minecraft.client.renderer.block.model.ModelBlock;
/*     */ import net.minecraft.client.renderer.block.model.ModelBlockDefinition;
/*     */ import net.minecraft.client.renderer.block.model.ModelBlockDefinition.Variant;
/*     */ import net.minecraft.client.renderer.block.model.ModelBlockDefinition.Variants;
/*     */ import net.minecraft.client.renderer.block.statemap.BlockStateMapper;
/*     */ import net.minecraft.client.renderer.texture.IIconCreator;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.TextureMap;
/*     */ import net.minecraft.client.resources.IResourceManager;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.IRegistry;
/*     */ import net.minecraft.util.RegistryNamespaced;
/*     */ import net.minecraft.util.RegistrySimple;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModelBakery
/*     */ {
/*  49 */   private static final Set<ResourceLocation> LOCATIONS_BUILTIN_TEXTURES = Sets.newHashSet(new ResourceLocation[] { new ResourceLocation("blocks/water_flow"), new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/lava_flow"), new ResourceLocation("blocks/lava_still"), new ResourceLocation("blocks/destroy_stage_0"), new ResourceLocation("blocks/destroy_stage_1"), new ResourceLocation("blocks/destroy_stage_2"), new ResourceLocation("blocks/destroy_stage_3"), new ResourceLocation("blocks/destroy_stage_4"), new ResourceLocation("blocks/destroy_stage_5"), new ResourceLocation("blocks/destroy_stage_6"), new ResourceLocation("blocks/destroy_stage_7"), new ResourceLocation("blocks/destroy_stage_8"), new ResourceLocation("blocks/destroy_stage_9"), new ResourceLocation("items/empty_armor_slot_helmet"), new ResourceLocation("items/empty_armor_slot_chestplate"), new ResourceLocation("items/empty_armor_slot_leggings"), new ResourceLocation("items/empty_armor_slot_boots") });
/*  50 */   private static final Logger LOGGER = LogManager.getLogger();
/*  51 */   protected static final ModelResourceLocation MODEL_MISSING = new ModelResourceLocation("builtin/missing", "missing");
/*  52 */   private static final Map<String, String> BUILT_IN_MODELS = Maps.newHashMap();
/*  53 */   private static final Joiner JOINER = Joiner.on(" -> ");
/*     */   private final IResourceManager resourceManager;
/*  55 */   private final Map<ResourceLocation, TextureAtlasSprite> sprites = Maps.newHashMap();
/*  56 */   private final Map<ResourceLocation, ModelBlock> models = Maps.newLinkedHashMap();
/*  57 */   private final Map<ModelResourceLocation, ModelBlockDefinition.Variants> variants = Maps.newLinkedHashMap();
/*     */   private final TextureMap textureMap;
/*     */   private final BlockModelShapes blockModelShapes;
/*  60 */   private final FaceBakery faceBakery = new FaceBakery();
/*  61 */   private final ItemModelGenerator itemModelGenerator = new ItemModelGenerator();
/*  62 */   private RegistrySimple<ModelResourceLocation, IBakedModel> bakedRegistry = new RegistrySimple();
/*  63 */   private static final ModelBlock MODEL_GENERATED = ModelBlock.deserialize("{\"elements\":[{  \"from\": [0, 0, 0],   \"to\": [16, 16, 16],   \"faces\": {       \"down\": {\"uv\": [0, 0, 16, 16], \"texture\":\"\"}   }}]}");
/*  64 */   private static final ModelBlock MODEL_COMPASS = ModelBlock.deserialize("{\"elements\":[{  \"from\": [0, 0, 0],   \"to\": [16, 16, 16],   \"faces\": {       \"down\": {\"uv\": [0, 0, 16, 16], \"texture\":\"\"}   }}]}");
/*  65 */   private static final ModelBlock MODEL_CLOCK = ModelBlock.deserialize("{\"elements\":[{  \"from\": [0, 0, 0],   \"to\": [16, 16, 16],   \"faces\": {       \"down\": {\"uv\": [0, 0, 16, 16], \"texture\":\"\"}   }}]}");
/*  66 */   private static final ModelBlock MODEL_ENTITY = ModelBlock.deserialize("{\"elements\":[{  \"from\": [0, 0, 0],   \"to\": [16, 16, 16],   \"faces\": {       \"down\": {\"uv\": [0, 0, 16, 16], \"texture\":\"\"}   }}]}");
/*  67 */   private Map<String, ResourceLocation> itemLocations = Maps.newLinkedHashMap();
/*  68 */   private final Map<ResourceLocation, ModelBlockDefinition> blockDefinitions = Maps.newHashMap();
/*  69 */   private Map<Item, List<String>> variantNames = Maps.newIdentityHashMap();
/*     */   
/*     */   public ModelBakery(IResourceManager p_i46085_1_, TextureMap p_i46085_2_, BlockModelShapes p_i46085_3_)
/*     */   {
/*  73 */     this.resourceManager = p_i46085_1_;
/*  74 */     this.textureMap = p_i46085_2_;
/*  75 */     this.blockModelShapes = p_i46085_3_;
/*     */   }
/*     */   
/*     */   public IRegistry<ModelResourceLocation, IBakedModel> setupModelRegistry()
/*     */   {
/*  80 */     loadVariantItemModels();
/*  81 */     loadModelsCheck();
/*  82 */     loadSprites();
/*  83 */     bakeItemModels();
/*  84 */     bakeBlockModels();
/*  85 */     return this.bakedRegistry;
/*     */   }
/*     */   
/*     */   private void loadVariantItemModels()
/*     */   {
/*  90 */     loadVariants(this.blockModelShapes.getBlockStateMapper().putAllStateModelLocations().values());
/*  91 */     this.variants.put(MODEL_MISSING, new ModelBlockDefinition.Variants(MODEL_MISSING.getVariant(), Lists.newArrayList(new ModelBlockDefinition.Variant[] { new ModelBlockDefinition.Variant(new ResourceLocation(MODEL_MISSING.getResourcePath()), ModelRotation.X0_Y0, false, 1) })));
/*  92 */     ResourceLocation resourcelocation = new ResourceLocation("item_frame");
/*  93 */     ModelBlockDefinition modelblockdefinition = getModelBlockDefinition(resourcelocation);
/*  94 */     registerVariant(modelblockdefinition, new ModelResourceLocation(resourcelocation, "normal"));
/*  95 */     registerVariant(modelblockdefinition, new ModelResourceLocation(resourcelocation, "map"));
/*  96 */     loadVariantModels();
/*  97 */     loadItemModels();
/*     */   }
/*     */   
/*     */   private void loadVariants(Collection<ModelResourceLocation> p_177591_1_)
/*     */   {
/* 102 */     Iterator localIterator = p_177591_1_.iterator(); break label109; for (;;) { ModelResourceLocation modelresourcelocation = (ModelResourceLocation)localIterator.next();
/*     */       
/*     */       try
/*     */       {
/* 106 */         ModelBlockDefinition modelblockdefinition = getModelBlockDefinition(modelresourcelocation);
/*     */         
/*     */         try
/*     */         {
/* 110 */           registerVariant(modelblockdefinition, modelresourcelocation);
/*     */         }
/*     */         catch (Exception var6)
/*     */         {
/* 114 */           LOGGER.warn("Unable to load variant: " + modelresourcelocation.getVariant() + " from " + modelresourcelocation);
/*     */         }
/* 102 */         if (localIterator.hasNext()) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       catch (Exception exception)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 119 */         LOGGER.warn("Unable to load definition " + modelresourcelocation, exception);
/*     */       }
/*     */     }
/*     */     label109:
/*     */   }
/*     */   
/*     */   private void registerVariant(ModelBlockDefinition p_177569_1_, ModelResourceLocation p_177569_2_) {
/* 126 */     this.variants.put(p_177569_2_, p_177569_1_.getVariants(p_177569_2_.getVariant()));
/*     */   }
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
/*     */   private ResourceLocation getBlockStateLocation(ResourceLocation p_177584_1_)
/*     */   {
/* 174 */     return new ResourceLocation(p_177584_1_.getResourceDomain(), "blockstates/" + p_177584_1_.getResourcePath() + ".json");
/*     */   }
/*     */   
/*     */   private void loadVariantModels() {
/*     */     Iterator localIterator2;
/* 179 */     for (Iterator localIterator1 = this.variants.keySet().iterator(); localIterator1.hasNext(); 
/*     */         
/* 181 */         localIterator2.hasNext())
/*     */     {
/* 179 */       ModelResourceLocation modelresourcelocation = (ModelResourceLocation)localIterator1.next();
/*     */       
/* 181 */       localIterator2 = ((ModelBlockDefinition.Variants)this.variants.get(modelresourcelocation)).getVariants().iterator(); continue;ModelBlockDefinition.Variant modelblockdefinition$variant = (ModelBlockDefinition.Variant)localIterator2.next();
/*     */       
/* 183 */       ResourceLocation resourcelocation = modelblockdefinition$variant.getModelLocation();
/*     */       
/* 185 */       if (this.models.get(resourcelocation) == null)
/*     */       {
/*     */         try
/*     */         {
/* 189 */           ModelBlock modelblock = loadModel(resourcelocation);
/* 190 */           this.models.put(resourcelocation, modelblock);
/*     */         }
/*     */         catch (Exception exception)
/*     */         {
/* 194 */           LOGGER.warn("Unable to load block model: '" + resourcelocation + "' for variant: '" + modelresourcelocation + "'", exception);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
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
/*     */   private ResourceLocation getModelLocation(ResourceLocation p_177580_1_)
/*     */   {
/* 262 */     return new ResourceLocation(p_177580_1_.getResourceDomain(), "models/" + p_177580_1_.getResourcePath() + ".json");
/*     */   }
/*     */   
/*     */   private void loadItemModels()
/*     */   {
/* 267 */     registerVariantNames();
/*     */     Iterator localIterator2;
/* 269 */     for (Iterator localIterator1 = Item.itemRegistry.iterator(); localIterator1.hasNext(); 
/*     */         
/* 271 */         localIterator2.hasNext())
/*     */     {
/* 269 */       Item item = (Item)localIterator1.next();
/*     */       
/* 271 */       localIterator2 = getVariantNames(item).iterator(); continue;String s = (String)localIterator2.next();
/*     */       
/* 273 */       ResourceLocation resourcelocation = getItemLocation(s);
/* 274 */       this.itemLocations.put(s, resourcelocation);
/*     */       
/* 276 */       if (this.models.get(resourcelocation) == null)
/*     */       {
/*     */         try
/*     */         {
/* 280 */           ModelBlock modelblock = loadModel(resourcelocation);
/* 281 */           this.models.put(resourcelocation, modelblock);
/*     */         }
/*     */         catch (Exception exception)
/*     */         {
/* 285 */           LOGGER.warn("Unable to load item model: '" + resourcelocation + "' for item: '" + Item.itemRegistry.getNameForObject(item) + "'", exception);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void registerVariantNames()
/*     */   {
/* 294 */     this.variantNames.put(Item.getItemFromBlock(Blocks.stone), Lists.newArrayList(new String[] { "stone", "granite", "granite_smooth", "diorite", "diorite_smooth", "andesite", "andesite_smooth" }));
/* 295 */     this.variantNames.put(Item.getItemFromBlock(Blocks.dirt), Lists.newArrayList(new String[] { "dirt", "coarse_dirt", "podzol" }));
/* 296 */     this.variantNames.put(Item.getItemFromBlock(Blocks.planks), Lists.newArrayList(new String[] { "oak_planks", "spruce_planks", "birch_planks", "jungle_planks", "acacia_planks", "dark_oak_planks" }));
/* 297 */     this.variantNames.put(Item.getItemFromBlock(Blocks.sapling), Lists.newArrayList(new String[] { "oak_sapling", "spruce_sapling", "birch_sapling", "jungle_sapling", "acacia_sapling", "dark_oak_sapling" }));
/* 298 */     this.variantNames.put(Item.getItemFromBlock(Blocks.sand), Lists.newArrayList(new String[] { "sand", "red_sand" }));
/* 299 */     this.variantNames.put(Item.getItemFromBlock(Blocks.log), Lists.newArrayList(new String[] { "oak_log", "spruce_log", "birch_log", "jungle_log" }));
/* 300 */     this.variantNames.put(Item.getItemFromBlock(Blocks.leaves), Lists.newArrayList(new String[] { "oak_leaves", "spruce_leaves", "birch_leaves", "jungle_leaves" }));
/* 301 */     this.variantNames.put(Item.getItemFromBlock(Blocks.sponge), Lists.newArrayList(new String[] { "sponge", "sponge_wet" }));
/* 302 */     this.variantNames.put(Item.getItemFromBlock(Blocks.sandstone), Lists.newArrayList(new String[] { "sandstone", "chiseled_sandstone", "smooth_sandstone" }));
/* 303 */     this.variantNames.put(Item.getItemFromBlock(Blocks.red_sandstone), Lists.newArrayList(new String[] { "red_sandstone", "chiseled_red_sandstone", "smooth_red_sandstone" }));
/* 304 */     this.variantNames.put(Item.getItemFromBlock(Blocks.tallgrass), Lists.newArrayList(new String[] { "dead_bush", "tall_grass", "fern" }));
/* 305 */     this.variantNames.put(Item.getItemFromBlock(Blocks.deadbush), Lists.newArrayList(new String[] { "dead_bush" }));
/* 306 */     this.variantNames.put(Item.getItemFromBlock(Blocks.wool), Lists.newArrayList(new String[] { "black_wool", "red_wool", "green_wool", "brown_wool", "blue_wool", "purple_wool", "cyan_wool", "silver_wool", "gray_wool", "pink_wool", "lime_wool", "yellow_wool", "light_blue_wool", "magenta_wool", "orange_wool", "white_wool" }));
/* 307 */     this.variantNames.put(Item.getItemFromBlock(Blocks.yellow_flower), Lists.newArrayList(new String[] { "dandelion" }));
/* 308 */     this.variantNames.put(Item.getItemFromBlock(Blocks.red_flower), Lists.newArrayList(new String[] { "poppy", "blue_orchid", "allium", "houstonia", "red_tulip", "orange_tulip", "white_tulip", "pink_tulip", "oxeye_daisy" }));
/* 309 */     this.variantNames.put(Item.getItemFromBlock(Blocks.stone_slab), Lists.newArrayList(new String[] { "stone_slab", "sandstone_slab", "cobblestone_slab", "brick_slab", "stone_brick_slab", "nether_brick_slab", "quartz_slab" }));
/* 310 */     this.variantNames.put(Item.getItemFromBlock(Blocks.stone_slab2), Lists.newArrayList(new String[] { "red_sandstone_slab" }));
/* 311 */     this.variantNames.put(Item.getItemFromBlock(Blocks.stained_glass), Lists.newArrayList(new String[] { "black_stained_glass", "red_stained_glass", "green_stained_glass", "brown_stained_glass", "blue_stained_glass", "purple_stained_glass", "cyan_stained_glass", "silver_stained_glass", "gray_stained_glass", "pink_stained_glass", "lime_stained_glass", "yellow_stained_glass", "light_blue_stained_glass", "magenta_stained_glass", "orange_stained_glass", "white_stained_glass" }));
/* 312 */     this.variantNames.put(Item.getItemFromBlock(Blocks.monster_egg), Lists.newArrayList(new String[] { "stone_monster_egg", "cobblestone_monster_egg", "stone_brick_monster_egg", "mossy_brick_monster_egg", "cracked_brick_monster_egg", "chiseled_brick_monster_egg" }));
/* 313 */     this.variantNames.put(Item.getItemFromBlock(Blocks.stonebrick), Lists.newArrayList(new String[] { "stonebrick", "mossy_stonebrick", "cracked_stonebrick", "chiseled_stonebrick" }));
/* 314 */     this.variantNames.put(Item.getItemFromBlock(Blocks.wooden_slab), Lists.newArrayList(new String[] { "oak_slab", "spruce_slab", "birch_slab", "jungle_slab", "acacia_slab", "dark_oak_slab" }));
/* 315 */     this.variantNames.put(Item.getItemFromBlock(Blocks.cobblestone_wall), Lists.newArrayList(new String[] { "cobblestone_wall", "mossy_cobblestone_wall" }));
/* 316 */     this.variantNames.put(Item.getItemFromBlock(Blocks.anvil), Lists.newArrayList(new String[] { "anvil_intact", "anvil_slightly_damaged", "anvil_very_damaged" }));
/* 317 */     this.variantNames.put(Item.getItemFromBlock(Blocks.quartz_block), Lists.newArrayList(new String[] { "quartz_block", "chiseled_quartz_block", "quartz_column" }));
/* 318 */     this.variantNames.put(Item.getItemFromBlock(Blocks.stained_hardened_clay), Lists.newArrayList(new String[] { "black_stained_hardened_clay", "red_stained_hardened_clay", "green_stained_hardened_clay", "brown_stained_hardened_clay", "blue_stained_hardened_clay", "purple_stained_hardened_clay", "cyan_stained_hardened_clay", "silver_stained_hardened_clay", "gray_stained_hardened_clay", "pink_stained_hardened_clay", "lime_stained_hardened_clay", "yellow_stained_hardened_clay", "light_blue_stained_hardened_clay", "magenta_stained_hardened_clay", "orange_stained_hardened_clay", "white_stained_hardened_clay" }));
/* 319 */     this.variantNames.put(Item.getItemFromBlock(Blocks.stained_glass_pane), Lists.newArrayList(new String[] { "black_stained_glass_pane", "red_stained_glass_pane", "green_stained_glass_pane", "brown_stained_glass_pane", "blue_stained_glass_pane", "purple_stained_glass_pane", "cyan_stained_glass_pane", "silver_stained_glass_pane", "gray_stained_glass_pane", "pink_stained_glass_pane", "lime_stained_glass_pane", "yellow_stained_glass_pane", "light_blue_stained_glass_pane", "magenta_stained_glass_pane", "orange_stained_glass_pane", "white_stained_glass_pane" }));
/* 320 */     this.variantNames.put(Item.getItemFromBlock(Blocks.leaves2), Lists.newArrayList(new String[] { "acacia_leaves", "dark_oak_leaves" }));
/* 321 */     this.variantNames.put(Item.getItemFromBlock(Blocks.log2), Lists.newArrayList(new String[] { "acacia_log", "dark_oak_log" }));
/* 322 */     this.variantNames.put(Item.getItemFromBlock(Blocks.prismarine), Lists.newArrayList(new String[] { "prismarine", "prismarine_bricks", "dark_prismarine" }));
/* 323 */     this.variantNames.put(Item.getItemFromBlock(Blocks.carpet), Lists.newArrayList(new String[] { "black_carpet", "red_carpet", "green_carpet", "brown_carpet", "blue_carpet", "purple_carpet", "cyan_carpet", "silver_carpet", "gray_carpet", "pink_carpet", "lime_carpet", "yellow_carpet", "light_blue_carpet", "magenta_carpet", "orange_carpet", "white_carpet" }));
/* 324 */     this.variantNames.put(Item.getItemFromBlock(Blocks.double_plant), Lists.newArrayList(new String[] { "sunflower", "syringa", "double_grass", "double_fern", "double_rose", "paeonia" }));
/* 325 */     this.variantNames.put(Items.bow, Lists.newArrayList(new String[] { "bow", "bow_pulling_0", "bow_pulling_1", "bow_pulling_2" }));
/* 326 */     this.variantNames.put(Items.coal, Lists.newArrayList(new String[] { "coal", "charcoal" }));
/* 327 */     this.variantNames.put(Items.fishing_rod, Lists.newArrayList(new String[] { "fishing_rod", "fishing_rod_cast" }));
/* 328 */     this.variantNames.put(Items.fish, Lists.newArrayList(new String[] { "cod", "salmon", "clownfish", "pufferfish" }));
/* 329 */     this.variantNames.put(Items.cooked_fish, Lists.newArrayList(new String[] { "cooked_cod", "cooked_salmon" }));
/* 330 */     this.variantNames.put(Items.dye, Lists.newArrayList(new String[] { "dye_black", "dye_red", "dye_green", "dye_brown", "dye_blue", "dye_purple", "dye_cyan", "dye_silver", "dye_gray", "dye_pink", "dye_lime", "dye_yellow", "dye_light_blue", "dye_magenta", "dye_orange", "dye_white" }));
/* 331 */     this.variantNames.put(Items.potionitem, Lists.newArrayList(new String[] { "bottle_drinkable", "bottle_splash" }));
/* 332 */     this.variantNames.put(Items.skull, Lists.newArrayList(new String[] { "skull_skeleton", "skull_wither", "skull_zombie", "skull_char", "skull_creeper" }));
/* 333 */     this.variantNames.put(Item.getItemFromBlock(Blocks.oak_fence_gate), Lists.newArrayList(new String[] { "oak_fence_gate" }));
/* 334 */     this.variantNames.put(Item.getItemFromBlock(Blocks.oak_fence), Lists.newArrayList(new String[] { "oak_fence" }));
/* 335 */     this.variantNames.put(Items.oak_door, Lists.newArrayList(new String[] { "oak_door" }));
/*     */   }
/*     */   
/*     */   private List<String> getVariantNames(Item p_177596_1_)
/*     */   {
/* 340 */     List<String> list = (List)this.variantNames.get(p_177596_1_);
/*     */     
/* 342 */     if (list == null)
/*     */     {
/* 344 */       list = Collections.singletonList(((ResourceLocation)Item.itemRegistry.getNameForObject(p_177596_1_)).toString());
/*     */     }
/*     */     
/* 347 */     return list;
/*     */   }
/*     */   
/*     */   private ResourceLocation getItemLocation(String p_177583_1_)
/*     */   {
/* 352 */     ResourceLocation resourcelocation = new ResourceLocation(p_177583_1_);
/* 353 */     return new ResourceLocation(resourcelocation.getResourceDomain(), "item/" + resourcelocation.getResourcePath());
/*     */   }
/*     */   
/*     */   private void bakeBlockModels()
/*     */   {
/* 358 */     for (ModelResourceLocation modelresourcelocation : this.variants.keySet())
/*     */     {
/* 360 */       WeightedBakedModel.Builder weightedbakedmodel$builder = new WeightedBakedModel.Builder();
/* 361 */       int i = 0;
/*     */       
/* 363 */       for (ModelBlockDefinition.Variant modelblockdefinition$variant : ((ModelBlockDefinition.Variants)this.variants.get(modelresourcelocation)).getVariants())
/*     */       {
/* 365 */         ModelBlock modelblock = (ModelBlock)this.models.get(modelblockdefinition$variant.getModelLocation());
/*     */         
/* 367 */         if ((modelblock != null) && (modelblock.isResolved()))
/*     */         {
/* 369 */           i++;
/* 370 */           weightedbakedmodel$builder.add(bakeModel(modelblock, modelblockdefinition$variant.getRotation(), modelblockdefinition$variant.isUvLocked()), modelblockdefinition$variant.getWeight());
/*     */         }
/*     */         else
/*     */         {
/* 374 */           LOGGER.warn("Missing model for: " + modelresourcelocation);
/*     */         }
/*     */       }
/*     */       
/* 378 */       if (i == 0)
/*     */       {
/* 380 */         LOGGER.warn("No weighted models for: " + modelresourcelocation);
/*     */       }
/* 382 */       else if (i == 1)
/*     */       {
/* 384 */         this.bakedRegistry.putObject(modelresourcelocation, weightedbakedmodel$builder.first());
/*     */       }
/*     */       else
/*     */       {
/* 388 */         this.bakedRegistry.putObject(modelresourcelocation, weightedbakedmodel$builder.build());
/*     */       }
/*     */     }
/*     */     
/* 392 */     for (Map.Entry<String, ResourceLocation> entry : this.itemLocations.entrySet())
/*     */     {
/* 394 */       ResourceLocation resourcelocation = (ResourceLocation)entry.getValue();
/* 395 */       ModelResourceLocation modelresourcelocation1 = new ModelResourceLocation((String)entry.getKey(), "inventory");
/* 396 */       ModelBlock modelblock1 = (ModelBlock)this.models.get(resourcelocation);
/*     */       
/* 398 */       if ((modelblock1 != null) && (modelblock1.isResolved()))
/*     */       {
/* 400 */         if (isCustomRenderer(modelblock1))
/*     */         {
/* 402 */           this.bakedRegistry.putObject(modelresourcelocation1, new BuiltInModel(modelblock1.func_181682_g()));
/*     */         }
/*     */         else
/*     */         {
/* 406 */           this.bakedRegistry.putObject(modelresourcelocation1, bakeModel(modelblock1, ModelRotation.X0_Y0, false));
/*     */         }
/*     */         
/*     */       }
/*     */       else {
/* 411 */         LOGGER.warn("Missing model for: " + resourcelocation);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private Set<ResourceLocation> getVariantsTextureLocations()
/*     */   {
/* 418 */     Set<ResourceLocation> set = Sets.newHashSet();
/* 419 */     List<ModelResourceLocation> list = Lists.newArrayList(this.variants.keySet());
/* 420 */     Collections.sort(list, new Comparator()
/*     */     {
/*     */       public int compare(ModelResourceLocation p_compare_1_, ModelResourceLocation p_compare_2_)
/*     */       {
/* 424 */         return p_compare_1_.toString().compareTo(p_compare_2_.toString());
/*     */       }
/*     */     });
/*     */     Iterator localIterator2;
/* 428 */     for (Iterator localIterator1 = list.iterator(); localIterator1.hasNext(); 
/*     */         
/*     */ 
/*     */ 
/* 432 */         localIterator2.hasNext())
/*     */     {
/* 428 */       ModelResourceLocation modelresourcelocation = (ModelResourceLocation)localIterator1.next();
/*     */       
/* 430 */       ModelBlockDefinition.Variants modelblockdefinition$variants = (ModelBlockDefinition.Variants)this.variants.get(modelresourcelocation);
/*     */       
/* 432 */       localIterator2 = modelblockdefinition$variants.getVariants().iterator(); continue;ModelBlockDefinition.Variant modelblockdefinition$variant = (ModelBlockDefinition.Variant)localIterator2.next();
/*     */       
/* 434 */       ModelBlock modelblock = (ModelBlock)this.models.get(modelblockdefinition$variant.getModelLocation());
/*     */       
/* 436 */       if (modelblock == null)
/*     */       {
/* 438 */         LOGGER.warn("Missing model for: " + modelresourcelocation);
/*     */       }
/*     */       else
/*     */       {
/* 442 */         set.addAll(getTextureLocations(modelblock));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 447 */     set.addAll(LOCATIONS_BUILTIN_TEXTURES);
/* 448 */     return set;
/*     */   }
/*     */   
/*     */   private IBakedModel bakeModel(ModelBlock modelBlockIn, ModelRotation modelRotationIn, boolean uvLocked)
/*     */   {
/* 453 */     TextureAtlasSprite textureatlassprite = (TextureAtlasSprite)this.sprites.get(new ResourceLocation(modelBlockIn.resolveTextureName("particle")));
/* 454 */     SimpleBakedModel.Builder simplebakedmodel$builder = new SimpleBakedModel.Builder(modelBlockIn).setTexture(textureatlassprite);
/*     */     Iterator localIterator2;
/* 456 */     for (Iterator localIterator1 = modelBlockIn.getElements().iterator(); localIterator1.hasNext(); 
/*     */         
/* 458 */         localIterator2.hasNext())
/*     */     {
/* 456 */       BlockPart blockpart = (BlockPart)localIterator1.next();
/*     */       
/* 458 */       localIterator2 = blockpart.mapFaces.keySet().iterator(); continue;EnumFacing enumfacing = (EnumFacing)localIterator2.next();
/*     */       
/* 460 */       BlockPartFace blockpartface = (BlockPartFace)blockpart.mapFaces.get(enumfacing);
/* 461 */       TextureAtlasSprite textureatlassprite1 = (TextureAtlasSprite)this.sprites.get(new ResourceLocation(modelBlockIn.resolveTextureName(blockpartface.texture)));
/*     */       
/* 463 */       if (blockpartface.cullFace == null)
/*     */       {
/* 465 */         simplebakedmodel$builder.addGeneralQuad(makeBakedQuad(blockpart, blockpartface, textureatlassprite1, enumfacing, modelRotationIn, uvLocked));
/*     */       }
/*     */       else
/*     */       {
/* 469 */         simplebakedmodel$builder.addFaceQuad(modelRotationIn.rotateFace(blockpartface.cullFace), makeBakedQuad(blockpart, blockpartface, textureatlassprite1, enumfacing, modelRotationIn, uvLocked));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 474 */     return simplebakedmodel$builder.makeBakedModel();
/*     */   }
/*     */   
/*     */   private BakedQuad makeBakedQuad(BlockPart p_177589_1_, BlockPartFace p_177589_2_, TextureAtlasSprite p_177589_3_, EnumFacing p_177589_4_, ModelRotation p_177589_5_, boolean p_177589_6_)
/*     */   {
/* 479 */     return this.faceBakery.makeBakedQuad(p_177589_1_.positionFrom, p_177589_1_.positionTo, p_177589_2_, p_177589_3_, p_177589_4_, p_177589_5_, p_177589_1_.partRotation, p_177589_6_, p_177589_1_.shade);
/*     */   }
/*     */   
/*     */   private void loadModelsCheck()
/*     */   {
/* 484 */     loadModels();
/*     */     
/* 486 */     for (ModelBlock modelblock : this.models.values())
/*     */     {
/* 488 */       modelblock.getParentFromMap(this.models);
/*     */     }
/*     */     
/* 491 */     ModelBlock.checkModelHierarchy(this.models);
/*     */   }
/*     */   
/*     */   private void loadModels()
/*     */   {
/* 496 */     Deque<ResourceLocation> deque = Queues.newArrayDeque();
/* 497 */     Set<ResourceLocation> set = Sets.newHashSet();
/*     */     
/* 499 */     for (ResourceLocation resourcelocation : this.models.keySet())
/*     */     {
/* 501 */       set.add(resourcelocation);
/* 502 */       ResourceLocation resourcelocation1 = ((ModelBlock)this.models.get(resourcelocation)).getParentLocation();
/*     */       
/* 504 */       if (resourcelocation1 != null)
/*     */       {
/* 506 */         deque.add(resourcelocation1);
/*     */       }
/*     */     }
/*     */     
/* 510 */     while (!deque.isEmpty())
/*     */     {
/* 512 */       ResourceLocation resourcelocation2 = (ResourceLocation)deque.pop();
/*     */       
/*     */       try
/*     */       {
/* 516 */         if (this.models.get(resourcelocation2) != null) {
/*     */           continue;
/*     */         }
/*     */         
/*     */ 
/* 521 */         ModelBlock modelblock = loadModel(resourcelocation2);
/* 522 */         this.models.put(resourcelocation2, modelblock);
/* 523 */         ResourceLocation resourcelocation3 = modelblock.getParentLocation();
/*     */         
/* 525 */         if ((resourcelocation3 != null) && (!set.contains(resourcelocation3)))
/*     */         {
/* 527 */           deque.add(resourcelocation3);
/*     */         }
/*     */       }
/*     */       catch (Exception exception)
/*     */       {
/* 532 */         LOGGER.warn("In parent chain: " + JOINER.join(getParentPath(resourcelocation2)) + "; unable to load model: '" + resourcelocation2 + "'", exception);
/*     */       }
/*     */       
/* 535 */       set.add(resourcelocation2);
/*     */     }
/*     */   }
/*     */   
/*     */   private List<ResourceLocation> getParentPath(ResourceLocation p_177573_1_)
/*     */   {
/* 541 */     List<ResourceLocation> list = Lists.newArrayList(new ResourceLocation[] { p_177573_1_ });
/* 542 */     ResourceLocation resourcelocation = p_177573_1_;
/*     */     
/* 544 */     while ((resourcelocation = getParentLocation(resourcelocation)) != null)
/*     */     {
/* 546 */       list.add(0, resourcelocation);
/*     */     }
/*     */     
/* 549 */     return list;
/*     */   }
/*     */   
/*     */   private ResourceLocation getParentLocation(ResourceLocation p_177576_1_)
/*     */   {
/* 554 */     for (Map.Entry<ResourceLocation, ModelBlock> entry : this.models.entrySet())
/*     */     {
/* 556 */       ModelBlock modelblock = (ModelBlock)entry.getValue();
/*     */       
/* 558 */       if ((modelblock != null) && (p_177576_1_.equals(modelblock.getParentLocation())))
/*     */       {
/* 560 */         return (ResourceLocation)entry.getKey();
/*     */       }
/*     */     }
/*     */     
/* 564 */     return null;
/*     */   }
/*     */   
/*     */   private Set<ResourceLocation> getTextureLocations(ModelBlock p_177585_1_)
/*     */   {
/* 569 */     Set<ResourceLocation> set = Sets.newHashSet();
/*     */     Iterator localIterator2;
/* 571 */     for (Iterator localIterator1 = p_177585_1_.getElements().iterator(); localIterator1.hasNext(); 
/*     */         
/* 573 */         localIterator2.hasNext())
/*     */     {
/* 571 */       BlockPart blockpart = (BlockPart)localIterator1.next();
/*     */       
/* 573 */       localIterator2 = blockpart.mapFaces.values().iterator(); continue;BlockPartFace blockpartface = (BlockPartFace)localIterator2.next();
/*     */       
/* 575 */       ResourceLocation resourcelocation = new ResourceLocation(p_177585_1_.resolveTextureName(blockpartface.texture));
/* 576 */       set.add(resourcelocation);
/*     */     }
/*     */     
/*     */ 
/* 580 */     set.add(new ResourceLocation(p_177585_1_.resolveTextureName("particle")));
/* 581 */     return set;
/*     */   }
/*     */   
/*     */   private void loadSprites()
/*     */   {
/* 586 */     final Set<ResourceLocation> set = getVariantsTextureLocations();
/* 587 */     set.addAll(getItemsTextureLocations());
/* 588 */     set.remove(TextureMap.LOCATION_MISSING_TEXTURE);
/* 589 */     IIconCreator iiconcreator = new IIconCreator()
/*     */     {
/*     */       public void registerSprites(TextureMap iconRegistry)
/*     */       {
/* 593 */         for (ResourceLocation resourcelocation : set)
/*     */         {
/* 595 */           TextureAtlasSprite textureatlassprite = iconRegistry.registerSprite(resourcelocation);
/* 596 */           ModelBakery.this.sprites.put(resourcelocation, textureatlassprite);
/*     */         }
/*     */       }
/* 599 */     };
/* 600 */     this.textureMap.loadSprites(this.resourceManager, iiconcreator);
/* 601 */     this.sprites.put(new ResourceLocation("missingno"), this.textureMap.getMissingSprite());
/*     */   }
/*     */   
/*     */   private Set<ResourceLocation> getItemsTextureLocations()
/*     */   {
/* 606 */     Set<ResourceLocation> set = Sets.newHashSet();
/*     */     
/* 608 */     for (ResourceLocation resourcelocation : this.itemLocations.values())
/*     */     {
/* 610 */       ModelBlock modelblock = (ModelBlock)this.models.get(resourcelocation);
/*     */       
/* 612 */       if (modelblock != null)
/*     */       {
/* 614 */         set.add(new ResourceLocation(modelblock.resolveTextureName("particle")));
/*     */         
/* 616 */         if (hasItemModel(modelblock))
/*     */         {
/* 618 */           for (String s : ItemModelGenerator.LAYERS)
/*     */           {
/* 620 */             ResourceLocation resourcelocation2 = new ResourceLocation(modelblock.resolveTextureName(s));
/*     */             
/* 622 */             if ((modelblock.getRootModel() == MODEL_COMPASS) && (!TextureMap.LOCATION_MISSING_TEXTURE.equals(resourcelocation2)))
/*     */             {
/* 624 */               TextureAtlasSprite.setLocationNameCompass(resourcelocation2.toString());
/*     */             }
/* 626 */             else if ((modelblock.getRootModel() == MODEL_CLOCK) && (!TextureMap.LOCATION_MISSING_TEXTURE.equals(resourcelocation2)))
/*     */             {
/* 628 */               TextureAtlasSprite.setLocationNameClock(resourcelocation2.toString());
/*     */             }
/*     */             
/* 631 */             set.add(resourcelocation2);
/*     */           }
/*     */         }
/* 634 */         else if (!isCustomRenderer(modelblock)) {
/*     */           Iterator localIterator3;
/* 636 */           for (??? = modelblock.getElements().iterator(); ???.hasNext(); 
/*     */               
/* 638 */               localIterator3.hasNext())
/*     */           {
/* 636 */             BlockPart blockpart = (BlockPart)???.next();
/*     */             
/* 638 */             localIterator3 = blockpart.mapFaces.values().iterator(); continue;BlockPartFace blockpartface = (BlockPartFace)localIterator3.next();
/*     */             
/* 640 */             ResourceLocation resourcelocation1 = new ResourceLocation(modelblock.resolveTextureName(blockpartface.texture));
/* 641 */             set.add(resourcelocation1);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 648 */     return set;
/*     */   }
/*     */   
/*     */   private boolean hasItemModel(ModelBlock p_177581_1_)
/*     */   {
/* 653 */     if (p_177581_1_ == null)
/*     */     {
/* 655 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 659 */     ModelBlock modelblock = p_177581_1_.getRootModel();
/* 660 */     return (modelblock == MODEL_GENERATED) || (modelblock == MODEL_COMPASS) || (modelblock == MODEL_CLOCK);
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean isCustomRenderer(ModelBlock p_177587_1_)
/*     */   {
/* 666 */     if (p_177587_1_ == null)
/*     */     {
/* 668 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 672 */     ModelBlock modelblock = p_177587_1_.getRootModel();
/* 673 */     return modelblock == MODEL_ENTITY;
/*     */   }
/*     */   
/*     */ 
/*     */   private void bakeItemModels()
/*     */   {
/* 679 */     for (ResourceLocation resourcelocation : this.itemLocations.values())
/*     */     {
/* 681 */       ModelBlock modelblock = (ModelBlock)this.models.get(resourcelocation);
/*     */       
/* 683 */       if (hasItemModel(modelblock))
/*     */       {
/* 685 */         ModelBlock modelblock1 = makeItemModel(modelblock);
/*     */         
/* 687 */         if (modelblock1 != null)
/*     */         {
/* 689 */           modelblock1.name = resourcelocation.toString();
/*     */         }
/*     */         
/* 692 */         this.models.put(resourcelocation, modelblock1);
/*     */       }
/* 694 */       else if (isCustomRenderer(modelblock))
/*     */       {
/* 696 */         this.models.put(resourcelocation, modelblock);
/*     */       }
/*     */     }
/*     */     
/* 700 */     for (TextureAtlasSprite textureatlassprite : this.sprites.values())
/*     */     {
/* 702 */       if (!textureatlassprite.hasAnimationMetadata())
/*     */       {
/* 704 */         textureatlassprite.clearFramesTextureData();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private ModelBlock makeItemModel(ModelBlock p_177582_1_)
/*     */   {
/* 711 */     return this.itemModelGenerator.makeItemModel(this.textureMap, p_177582_1_);
/*     */   }
/*     */   
/*     */   static
/*     */   {
/* 716 */     BUILT_IN_MODELS.put("missing", "{ \"textures\": {   \"particle\": \"missingno\",   \"missingno\": \"missingno\"}, \"elements\": [ {     \"from\": [ 0, 0, 0 ],     \"to\": [ 16, 16, 16 ],     \"faces\": {         \"down\":  { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"down\", \"texture\": \"#missingno\" },         \"up\":    { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"up\", \"texture\": \"#missingno\" },         \"north\": { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"north\", \"texture\": \"#missingno\" },         \"south\": { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"south\", \"texture\": \"#missingno\" },         \"west\":  { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"west\", \"texture\": \"#missingno\" },         \"east\":  { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"east\", \"texture\": \"#missingno\" }    }}]}");
/* 717 */     MODEL_GENERATED.name = "generation marker";
/* 718 */     MODEL_COMPASS.name = "compass generation marker";
/* 719 */     MODEL_CLOCK.name = "class generation marker";
/* 720 */     MODEL_ENTITY.name = "block entity marker";
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   private ModelBlockDefinition getModelBlockDefinition(ResourceLocation p_177586_1_)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: invokespecial 398	net/minecraft/client/resources/model/ModelBakery:getBlockStateLocation	(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/util/ResourceLocation;
/*     */     //   5: astore_2
/*     */     //   6: aload_0
/*     */     //   7: getfield 228	net/minecraft/client/resources/model/ModelBakery:blockDefinitions	Ljava/util/Map;
/*     */     //   10: aload_2
/*     */     //   11: invokeinterface 402 2 0
/*     */     //   16: checkcast 14	net/minecraft/client/renderer/block/model/ModelBlockDefinition
/*     */     //   19: astore_3
/*     */     //   20: aload_3
/*     */     //   21: ifnonnull +231 -> 252
/*     */     //   24: invokestatic 405	com/google/common/collect/Lists:newArrayList	()Ljava/util/ArrayList;
/*     */     //   27: astore 4
/*     */     //   29: aload_0
/*     */     //   30: getfield 236	net/minecraft/client/resources/model/ModelBakery:resourceManager	Lnet/minecraft/client/resources/IResourceManager;
/*     */     //   33: aload_2
/*     */     //   34: invokeinterface 411 2 0
/*     */     //   39: invokeinterface 414 1 0
/*     */     //   44: astore 6
/*     */     //   46: goto +139 -> 185
/*     */     //   49: aload 6
/*     */     //   51: invokeinterface 348 1 0
/*     */     //   56: checkcast 416	net/minecraft/client/resources/IResource
/*     */     //   59: astore 5
/*     */     //   61: aconst_null
/*     */     //   62: astore 7
/*     */     //   64: aload 5
/*     */     //   66: invokeinterface 420 1 0
/*     */     //   71: astore 7
/*     */     //   73: new 422	java/io/InputStreamReader
/*     */     //   76: dup
/*     */     //   77: aload 7
/*     */     //   79: getstatic 428	com/google/common/base/Charsets:UTF_8	Ljava/nio/charset/Charset;
/*     */     //   82: invokespecial 431	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;Ljava/nio/charset/Charset;)V
/*     */     //   85: invokestatic 435	net/minecraft/client/renderer/block/model/ModelBlockDefinition:parseFromReader	(Ljava/io/Reader;)Lnet/minecraft/client/renderer/block/model/ModelBlockDefinition;
/*     */     //   88: astore 8
/*     */     //   90: aload 4
/*     */     //   92: aload 8
/*     */     //   94: invokeinterface 439 2 0
/*     */     //   99: pop
/*     */     //   100: goto +80 -> 180
/*     */     //   103: astore 8
/*     */     //   105: new 443	java/lang/RuntimeException
/*     */     //   108: dup
/*     */     //   109: new 350	java/lang/StringBuilder
/*     */     //   112: dup
/*     */     //   113: ldc_w 445
/*     */     //   116: invokespecial 353	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   119: aload_1
/*     */     //   120: invokevirtual 362	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   123: ldc_w 447
/*     */     //   126: invokevirtual 357	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   129: aload 5
/*     */     //   131: invokeinterface 451 1 0
/*     */     //   136: invokevirtual 362	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   139: ldc_w 453
/*     */     //   142: invokevirtual 357	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   145: aload 5
/*     */     //   147: invokeinterface 456 1 0
/*     */     //   152: invokevirtual 357	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   155: ldc_w 458
/*     */     //   158: invokevirtual 357	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   161: invokevirtual 365	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   164: aload 8
/*     */     //   166: invokespecial 460	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   169: athrow
/*     */     //   170: astore 9
/*     */     //   172: aload 7
/*     */     //   174: invokestatic 468	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
/*     */     //   177: aload 9
/*     */     //   179: athrow
/*     */     //   180: aload 7
/*     */     //   182: invokestatic 468	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
/*     */     //   185: aload 6
/*     */     //   187: invokeinterface 379 1 0
/*     */     //   192: ifne -143 -> 49
/*     */     //   195: goto +35 -> 230
/*     */     //   198: astore 5
/*     */     //   200: new 443	java/lang/RuntimeException
/*     */     //   203: dup
/*     */     //   204: new 350	java/lang/StringBuilder
/*     */     //   207: dup
/*     */     //   208: ldc_w 470
/*     */     //   211: invokespecial 353	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   214: aload_2
/*     */     //   215: invokevirtual 471	net/minecraft/util/ResourceLocation:toString	()Ljava/lang/String;
/*     */     //   218: invokevirtual 357	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   221: invokevirtual 365	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   224: aload 5
/*     */     //   226: invokespecial 460	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   229: athrow
/*     */     //   230: new 14	net/minecraft/client/renderer/block/model/ModelBlockDefinition
/*     */     //   233: dup
/*     */     //   234: aload 4
/*     */     //   236: invokespecial 474	net/minecraft/client/renderer/block/model/ModelBlockDefinition:<init>	(Ljava/util/List;)V
/*     */     //   239: astore_3
/*     */     //   240: aload_0
/*     */     //   241: getfield 228	net/minecraft/client/resources/model/ModelBakery:blockDefinitions	Ljava/util/Map;
/*     */     //   244: aload_2
/*     */     //   245: aload_3
/*     */     //   246: invokeinterface 184 3 0
/*     */     //   251: pop
/*     */     //   252: aload_3
/*     */     //   253: areturn
/*     */     // Line number table:
/*     */     //   Java source line #131	-> byte code offset #0
/*     */     //   Java source line #132	-> byte code offset #6
/*     */     //   Java source line #134	-> byte code offset #20
/*     */     //   Java source line #136	-> byte code offset #24
/*     */     //   Java source line #140	-> byte code offset #29
/*     */     //   Java source line #142	-> byte code offset #61
/*     */     //   Java source line #146	-> byte code offset #64
/*     */     //   Java source line #147	-> byte code offset #73
/*     */     //   Java source line #148	-> byte code offset #90
/*     */     //   Java source line #149	-> byte code offset #100
/*     */     //   Java source line #150	-> byte code offset #103
/*     */     //   Java source line #152	-> byte code offset #105
/*     */     //   Java source line #155	-> byte code offset #170
/*     */     //   Java source line #156	-> byte code offset #172
/*     */     //   Java source line #157	-> byte code offset #177
/*     */     //   Java source line #156	-> byte code offset #180
/*     */     //   Java source line #140	-> byte code offset #185
/*     */     //   Java source line #159	-> byte code offset #195
/*     */     //   Java source line #160	-> byte code offset #198
/*     */     //   Java source line #162	-> byte code offset #200
/*     */     //   Java source line #165	-> byte code offset #230
/*     */     //   Java source line #166	-> byte code offset #240
/*     */     //   Java source line #169	-> byte code offset #252
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	254	0	this	ModelBakery
/*     */     //   0	254	1	p_177586_1_	ResourceLocation
/*     */     //   5	240	2	resourcelocation	ResourceLocation
/*     */     //   19	234	3	modelblockdefinition	ModelBlockDefinition
/*     */     //   27	208	4	list	List<ModelBlockDefinition>
/*     */     //   59	87	5	iresource	net.minecraft.client.resources.IResource
/*     */     //   198	27	5	ioexception	java.io.IOException
/*     */     //   44	142	6	localIterator	Iterator
/*     */     //   62	119	7	inputstream	java.io.InputStream
/*     */     //   88	5	8	modelblockdefinition1	ModelBlockDefinition
/*     */     //   103	62	8	exception	Exception
/*     */     //   170	8	9	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   64	100	103	java/lang/Exception
/*     */     //   64	170	170	finally
/*     */     //   29	195	198	java/io/IOException
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   private ModelBlock loadModel(ResourceLocation p_177594_1_)
/*     */     throws java.io.IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: invokevirtual 491	net/minecraft/util/ResourceLocation:getResourcePath	()Ljava/lang/String;
/*     */     //   4: astore_2
/*     */     //   5: ldc_w 521
/*     */     //   8: aload_2
/*     */     //   9: invokevirtual 526	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   12: ifeq +7 -> 19
/*     */     //   15: getstatic 172	net/minecraft/client/resources/model/ModelBakery:MODEL_GENERATED	Lnet/minecraft/client/renderer/block/model/ModelBlock;
/*     */     //   18: areturn
/*     */     //   19: ldc_w 528
/*     */     //   22: aload_2
/*     */     //   23: invokevirtual 526	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   26: ifeq +7 -> 33
/*     */     //   29: getstatic 174	net/minecraft/client/resources/model/ModelBakery:MODEL_COMPASS	Lnet/minecraft/client/renderer/block/model/ModelBlock;
/*     */     //   32: areturn
/*     */     //   33: ldc_w 530
/*     */     //   36: aload_2
/*     */     //   37: invokevirtual 526	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   40: ifeq +7 -> 47
/*     */     //   43: getstatic 176	net/minecraft/client/resources/model/ModelBakery:MODEL_CLOCK	Lnet/minecraft/client/renderer/block/model/ModelBlock;
/*     */     //   46: areturn
/*     */     //   47: ldc_w 532
/*     */     //   50: aload_2
/*     */     //   51: invokevirtual 526	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   54: ifeq +7 -> 61
/*     */     //   57: getstatic 178	net/minecraft/client/resources/model/ModelBakery:MODEL_ENTITY	Lnet/minecraft/client/renderer/block/model/ModelBlock;
/*     */     //   60: areturn
/*     */     //   61: aload_2
/*     */     //   62: ldc_w 534
/*     */     //   65: invokevirtual 538	java/lang/String:startsWith	(Ljava/lang/String;)Z
/*     */     //   68: ifeq +60 -> 128
/*     */     //   71: aload_2
/*     */     //   72: ldc_w 534
/*     */     //   75: invokevirtual 542	java/lang/String:length	()I
/*     */     //   78: invokevirtual 546	java/lang/String:substring	(I)Ljava/lang/String;
/*     */     //   81: astore 4
/*     */     //   83: getstatic 152	net/minecraft/client/resources/model/ModelBakery:BUILT_IN_MODELS	Ljava/util/Map;
/*     */     //   86: aload 4
/*     */     //   88: invokeinterface 402 2 0
/*     */     //   93: checkcast 523	java/lang/String
/*     */     //   96: astore 5
/*     */     //   98: aload 5
/*     */     //   100: ifnonnull +15 -> 115
/*     */     //   103: new 548	java/io/FileNotFoundException
/*     */     //   106: dup
/*     */     //   107: aload_1
/*     */     //   108: invokevirtual 471	net/minecraft/util/ResourceLocation:toString	()Ljava/lang/String;
/*     */     //   111: invokespecial 549	java/io/FileNotFoundException:<init>	(Ljava/lang/String;)V
/*     */     //   114: athrow
/*     */     //   115: new 551	java/io/StringReader
/*     */     //   118: dup
/*     */     //   119: aload 5
/*     */     //   121: invokespecial 552	java/io/StringReader:<init>	(Ljava/lang/String;)V
/*     */     //   124: astore_3
/*     */     //   125: goto +37 -> 162
/*     */     //   128: aload_0
/*     */     //   129: getfield 236	net/minecraft/client/resources/model/ModelBakery:resourceManager	Lnet/minecraft/client/resources/IResourceManager;
/*     */     //   132: aload_0
/*     */     //   133: aload_1
/*     */     //   134: invokespecial 554	net/minecraft/client/resources/model/ModelBakery:getModelLocation	(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/util/ResourceLocation;
/*     */     //   137: invokeinterface 558 2 0
/*     */     //   142: astore 4
/*     */     //   144: new 422	java/io/InputStreamReader
/*     */     //   147: dup
/*     */     //   148: aload 4
/*     */     //   150: invokeinterface 420 1 0
/*     */     //   155: getstatic 428	com/google/common/base/Charsets:UTF_8	Ljava/nio/charset/Charset;
/*     */     //   158: invokespecial 431	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;Ljava/nio/charset/Charset;)V
/*     */     //   161: astore_3
/*     */     //   162: aload_3
/*     */     //   163: invokestatic 563	net/minecraft/client/renderer/block/model/ModelBlock:deserialize	(Ljava/io/Reader;)Lnet/minecraft/client/renderer/block/model/ModelBlock;
/*     */     //   166: astore 5
/*     */     //   168: aload 5
/*     */     //   170: aload_1
/*     */     //   171: invokevirtual 471	net/minecraft/util/ResourceLocation:toString	()Ljava/lang/String;
/*     */     //   174: putfield 190	net/minecraft/client/renderer/block/model/ModelBlock:name	Ljava/lang/String;
/*     */     //   177: aload 5
/*     */     //   179: astore 4
/*     */     //   181: goto +12 -> 193
/*     */     //   184: astore 6
/*     */     //   186: aload_3
/*     */     //   187: invokevirtual 566	java/io/Reader:close	()V
/*     */     //   190: aload 6
/*     */     //   192: athrow
/*     */     //   193: aload_3
/*     */     //   194: invokevirtual 566	java/io/Reader:close	()V
/*     */     //   197: aload 4
/*     */     //   199: areturn
/*     */     // Line number table:
/*     */     //   Java source line #203	-> byte code offset #0
/*     */     //   Java source line #205	-> byte code offset #5
/*     */     //   Java source line #207	-> byte code offset #15
/*     */     //   Java source line #209	-> byte code offset #19
/*     */     //   Java source line #211	-> byte code offset #29
/*     */     //   Java source line #213	-> byte code offset #33
/*     */     //   Java source line #215	-> byte code offset #43
/*     */     //   Java source line #217	-> byte code offset #47
/*     */     //   Java source line #219	-> byte code offset #57
/*     */     //   Java source line #225	-> byte code offset #61
/*     */     //   Java source line #227	-> byte code offset #71
/*     */     //   Java source line #228	-> byte code offset #83
/*     */     //   Java source line #230	-> byte code offset #98
/*     */     //   Java source line #232	-> byte code offset #103
/*     */     //   Java source line #235	-> byte code offset #115
/*     */     //   Java source line #236	-> byte code offset #125
/*     */     //   Java source line #239	-> byte code offset #128
/*     */     //   Java source line #240	-> byte code offset #144
/*     */     //   Java source line #247	-> byte code offset #162
/*     */     //   Java source line #248	-> byte code offset #168
/*     */     //   Java source line #249	-> byte code offset #177
/*     */     //   Java source line #250	-> byte code offset #181
/*     */     //   Java source line #252	-> byte code offset #184
/*     */     //   Java source line #253	-> byte code offset #186
/*     */     //   Java source line #254	-> byte code offset #190
/*     */     //   Java source line #253	-> byte code offset #193
/*     */     //   Java source line #256	-> byte code offset #197
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	200	0	this	ModelBakery
/*     */     //   0	200	1	p_177594_1_	ResourceLocation
/*     */     //   4	68	2	s	String
/*     */     //   124	2	3	reader	java.io.Reader
/*     */     //   161	33	3	reader	java.io.Reader
/*     */     //   81	6	4	s1	String
/*     */     //   142	7	4	iresource	net.minecraft.client.resources.IResource
/*     */     //   179	3	4	modelblock1	ModelBlock
/*     */     //   193	5	4	modelblock1	ModelBlock
/*     */     //   96	24	5	s2	String
/*     */     //   166	12	5	modelblock	ModelBlock
/*     */     //   184	7	6	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   162	184	184	finally
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\model\ModelBakery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */