// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources.model;

import net.minecraft.client.renderer.texture.IIconCreator;
import java.util.Deque;
import com.google.common.collect.Queues;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.renderer.block.model.BlockPart;
import java.util.Comparator;
import java.util.Collections;
import net.minecraft.init.Items;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import java.io.StringReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import java.io.Reader;
import java.io.InputStreamReader;
import com.google.common.base.Charsets;
import net.minecraft.client.resources.IResource;
import java.util.Iterator;
import java.util.Collection;
import com.google.common.collect.Lists;
import net.minecraft.util.IRegistry;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import com.google.common.collect.Sets;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.util.RegistrySimple;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.block.model.ModelBlockDefinition;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import com.google.common.base.Joiner;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import net.minecraft.util.ResourceLocation;
import java.util.Set;

public class ModelBakery
{
    private static final Set<ResourceLocation> LOCATIONS_BUILTIN_TEXTURES;
    private static final Logger LOGGER;
    protected static final ModelResourceLocation MODEL_MISSING;
    private static final Map<String, String> BUILT_IN_MODELS;
    private static final Joiner JOINER;
    private final IResourceManager resourceManager;
    private final Map<ResourceLocation, TextureAtlasSprite> sprites;
    private final Map<ResourceLocation, ModelBlock> models;
    private final Map<ModelResourceLocation, ModelBlockDefinition.Variants> variants;
    private final TextureMap textureMap;
    private final BlockModelShapes blockModelShapes;
    private final FaceBakery faceBakery;
    private final ItemModelGenerator itemModelGenerator;
    private RegistrySimple<ModelResourceLocation, IBakedModel> bakedRegistry;
    private static final ModelBlock MODEL_GENERATED;
    private static final ModelBlock MODEL_COMPASS;
    private static final ModelBlock MODEL_CLOCK;
    private static final ModelBlock MODEL_ENTITY;
    private Map<String, ResourceLocation> itemLocations;
    private final Map<ResourceLocation, ModelBlockDefinition> blockDefinitions;
    private Map<Item, List<String>> variantNames;
    
    static {
        LOCATIONS_BUILTIN_TEXTURES = Sets.newHashSet(new ResourceLocation("blocks/water_flow"), new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/lava_flow"), new ResourceLocation("blocks/lava_still"), new ResourceLocation("blocks/destroy_stage_0"), new ResourceLocation("blocks/destroy_stage_1"), new ResourceLocation("blocks/destroy_stage_2"), new ResourceLocation("blocks/destroy_stage_3"), new ResourceLocation("blocks/destroy_stage_4"), new ResourceLocation("blocks/destroy_stage_5"), new ResourceLocation("blocks/destroy_stage_6"), new ResourceLocation("blocks/destroy_stage_7"), new ResourceLocation("blocks/destroy_stage_8"), new ResourceLocation("blocks/destroy_stage_9"), new ResourceLocation("items/empty_armor_slot_helmet"), new ResourceLocation("items/empty_armor_slot_chestplate"), new ResourceLocation("items/empty_armor_slot_leggings"), new ResourceLocation("items/empty_armor_slot_boots"));
        LOGGER = LogManager.getLogger();
        MODEL_MISSING = new ModelResourceLocation("builtin/missing", "missing");
        BUILT_IN_MODELS = Maps.newHashMap();
        JOINER = Joiner.on(" -> ");
        MODEL_GENERATED = ModelBlock.deserialize("{\"elements\":[{  \"from\": [0, 0, 0],   \"to\": [16, 16, 16],   \"faces\": {       \"down\": {\"uv\": [0, 0, 16, 16], \"texture\":\"\"}   }}]}");
        MODEL_COMPASS = ModelBlock.deserialize("{\"elements\":[{  \"from\": [0, 0, 0],   \"to\": [16, 16, 16],   \"faces\": {       \"down\": {\"uv\": [0, 0, 16, 16], \"texture\":\"\"}   }}]}");
        MODEL_CLOCK = ModelBlock.deserialize("{\"elements\":[{  \"from\": [0, 0, 0],   \"to\": [16, 16, 16],   \"faces\": {       \"down\": {\"uv\": [0, 0, 16, 16], \"texture\":\"\"}   }}]}");
        MODEL_ENTITY = ModelBlock.deserialize("{\"elements\":[{  \"from\": [0, 0, 0],   \"to\": [16, 16, 16],   \"faces\": {       \"down\": {\"uv\": [0, 0, 16, 16], \"texture\":\"\"}   }}]}");
        ModelBakery.BUILT_IN_MODELS.put("missing", "{ \"textures\": {   \"particle\": \"missingno\",   \"missingno\": \"missingno\"}, \"elements\": [ {     \"from\": [ 0, 0, 0 ],     \"to\": [ 16, 16, 16 ],     \"faces\": {         \"down\":  { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"down\", \"texture\": \"#missingno\" },         \"up\":    { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"up\", \"texture\": \"#missingno\" },         \"north\": { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"north\", \"texture\": \"#missingno\" },         \"south\": { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"south\", \"texture\": \"#missingno\" },         \"west\":  { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"west\", \"texture\": \"#missingno\" },         \"east\":  { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"east\", \"texture\": \"#missingno\" }    }}]}");
        ModelBakery.MODEL_GENERATED.name = "generation marker";
        ModelBakery.MODEL_COMPASS.name = "compass generation marker";
        ModelBakery.MODEL_CLOCK.name = "class generation marker";
        ModelBakery.MODEL_ENTITY.name = "block entity marker";
    }
    
    public ModelBakery(final IResourceManager p_i46085_1_, final TextureMap p_i46085_2_, final BlockModelShapes p_i46085_3_) {
        this.sprites = (Map<ResourceLocation, TextureAtlasSprite>)Maps.newHashMap();
        this.models = (Map<ResourceLocation, ModelBlock>)Maps.newLinkedHashMap();
        this.variants = (Map<ModelResourceLocation, ModelBlockDefinition.Variants>)Maps.newLinkedHashMap();
        this.faceBakery = new FaceBakery();
        this.itemModelGenerator = new ItemModelGenerator();
        this.bakedRegistry = new RegistrySimple<ModelResourceLocation, IBakedModel>();
        this.itemLocations = (Map<String, ResourceLocation>)Maps.newLinkedHashMap();
        this.blockDefinitions = (Map<ResourceLocation, ModelBlockDefinition>)Maps.newHashMap();
        this.variantNames = (Map<Item, List<String>>)Maps.newIdentityHashMap();
        this.resourceManager = p_i46085_1_;
        this.textureMap = p_i46085_2_;
        this.blockModelShapes = p_i46085_3_;
    }
    
    public IRegistry<ModelResourceLocation, IBakedModel> setupModelRegistry() {
        this.loadVariantItemModels();
        this.loadModelsCheck();
        this.loadSprites();
        this.bakeItemModels();
        this.bakeBlockModels();
        return this.bakedRegistry;
    }
    
    private void loadVariantItemModels() {
        this.loadVariants(this.blockModelShapes.getBlockStateMapper().putAllStateModelLocations().values());
        this.variants.put(ModelBakery.MODEL_MISSING, new ModelBlockDefinition.Variants(ModelBakery.MODEL_MISSING.getVariant(), Lists.newArrayList(new ModelBlockDefinition.Variant(new ResourceLocation(ModelBakery.MODEL_MISSING.getResourcePath()), ModelRotation.X0_Y0, false, 1))));
        final ResourceLocation resourcelocation = new ResourceLocation("item_frame");
        final ModelBlockDefinition modelblockdefinition = this.getModelBlockDefinition(resourcelocation);
        this.registerVariant(modelblockdefinition, new ModelResourceLocation(resourcelocation, "normal"));
        this.registerVariant(modelblockdefinition, new ModelResourceLocation(resourcelocation, "map"));
        this.loadVariantModels();
        this.loadItemModels();
    }
    
    private void loadVariants(final Collection<ModelResourceLocation> p_177591_1_) {
        for (final ModelResourceLocation modelresourcelocation : p_177591_1_) {
            try {
                final ModelBlockDefinition modelblockdefinition = this.getModelBlockDefinition(modelresourcelocation);
                try {
                    this.registerVariant(modelblockdefinition, modelresourcelocation);
                }
                catch (Exception var6) {
                    ModelBakery.LOGGER.warn("Unable to load variant: " + modelresourcelocation.getVariant() + " from " + modelresourcelocation);
                }
            }
            catch (Exception exception) {
                ModelBakery.LOGGER.warn("Unable to load definition " + modelresourcelocation, exception);
            }
        }
    }
    
    private void registerVariant(final ModelBlockDefinition p_177569_1_, final ModelResourceLocation p_177569_2_) {
        this.variants.put(p_177569_2_, p_177569_1_.getVariants(p_177569_2_.getVariant()));
    }
    
    private ModelBlockDefinition getModelBlockDefinition(final ResourceLocation p_177586_1_) {
        final ResourceLocation resourcelocation = this.getBlockStateLocation(p_177586_1_);
        ModelBlockDefinition modelblockdefinition = this.blockDefinitions.get(resourcelocation);
        if (modelblockdefinition == null) {
            final List<ModelBlockDefinition> list = (List<ModelBlockDefinition>)Lists.newArrayList();
            try {
                for (final IResource iresource : this.resourceManager.getAllResources(resourcelocation)) {
                    InputStream inputstream = null;
                    try {
                        inputstream = iresource.getInputStream();
                        final ModelBlockDefinition modelblockdefinition2 = ModelBlockDefinition.parseFromReader(new InputStreamReader(inputstream, Charsets.UTF_8));
                        list.add(modelblockdefinition2);
                    }
                    catch (Exception exception) {
                        throw new RuntimeException("Encountered an exception when loading model definition of '" + p_177586_1_ + "' from: '" + iresource.getResourceLocation() + "' in resourcepack: '" + iresource.getResourcePackName() + "'", exception);
                    }
                    finally {
                        IOUtils.closeQuietly(inputstream);
                    }
                    IOUtils.closeQuietly(inputstream);
                }
            }
            catch (IOException ioexception) {
                throw new RuntimeException("Encountered an exception when loading model definition of model " + resourcelocation.toString(), ioexception);
            }
            modelblockdefinition = new ModelBlockDefinition(list);
            this.blockDefinitions.put(resourcelocation, modelblockdefinition);
        }
        return modelblockdefinition;
    }
    
    private ResourceLocation getBlockStateLocation(final ResourceLocation p_177584_1_) {
        return new ResourceLocation(p_177584_1_.getResourceDomain(), "blockstates/" + p_177584_1_.getResourcePath() + ".json");
    }
    
    private void loadVariantModels() {
        for (final ModelResourceLocation modelresourcelocation : this.variants.keySet()) {
            for (final ModelBlockDefinition.Variant modelblockdefinition$variant : this.variants.get(modelresourcelocation).getVariants()) {
                final ResourceLocation resourcelocation = modelblockdefinition$variant.getModelLocation();
                if (this.models.get(resourcelocation) == null) {
                    try {
                        final ModelBlock modelblock = this.loadModel(resourcelocation);
                        this.models.put(resourcelocation, modelblock);
                    }
                    catch (Exception exception) {
                        ModelBakery.LOGGER.warn("Unable to load block model: '" + resourcelocation + "' for variant: '" + modelresourcelocation + "'", exception);
                    }
                }
            }
        }
    }
    
    private ModelBlock loadModel(final ResourceLocation p_177594_1_) throws IOException {
        final String s = p_177594_1_.getResourcePath();
        if ("builtin/generated".equals(s)) {
            return ModelBakery.MODEL_GENERATED;
        }
        if ("builtin/compass".equals(s)) {
            return ModelBakery.MODEL_COMPASS;
        }
        if ("builtin/clock".equals(s)) {
            return ModelBakery.MODEL_CLOCK;
        }
        if ("builtin/entity".equals(s)) {
            return ModelBakery.MODEL_ENTITY;
        }
        Reader reader;
        if (s.startsWith("builtin/")) {
            final String s2 = s.substring("builtin/".length());
            final String s3 = ModelBakery.BUILT_IN_MODELS.get(s2);
            if (s3 == null) {
                throw new FileNotFoundException(p_177594_1_.toString());
            }
            reader = new StringReader(s3);
        }
        else {
            final IResource iresource = this.resourceManager.getResource(this.getModelLocation(p_177594_1_));
            reader = new InputStreamReader(iresource.getInputStream(), Charsets.UTF_8);
        }
        ModelBlock modelblock2;
        try {
            final ModelBlock modelblock = ModelBlock.deserialize(reader);
            modelblock.name = p_177594_1_.toString();
            modelblock2 = modelblock;
        }
        finally {
            reader.close();
        }
        reader.close();
        return modelblock2;
    }
    
    private ResourceLocation getModelLocation(final ResourceLocation p_177580_1_) {
        return new ResourceLocation(p_177580_1_.getResourceDomain(), "models/" + p_177580_1_.getResourcePath() + ".json");
    }
    
    private void loadItemModels() {
        this.registerVariantNames();
        for (final Item item : Item.itemRegistry) {
            for (final String s : this.getVariantNames(item)) {
                final ResourceLocation resourcelocation = this.getItemLocation(s);
                this.itemLocations.put(s, resourcelocation);
                if (this.models.get(resourcelocation) == null) {
                    try {
                        final ModelBlock modelblock = this.loadModel(resourcelocation);
                        this.models.put(resourcelocation, modelblock);
                    }
                    catch (Exception exception) {
                        ModelBakery.LOGGER.warn("Unable to load item model: '" + resourcelocation + "' for item: '" + Item.itemRegistry.getNameForObject(item) + "'", exception);
                    }
                }
            }
        }
    }
    
    private void registerVariantNames() {
        this.variantNames.put(Item.getItemFromBlock(Blocks.stone), Lists.newArrayList("stone", "granite", "granite_smooth", "diorite", "diorite_smooth", "andesite", "andesite_smooth"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.dirt), Lists.newArrayList("dirt", "coarse_dirt", "podzol"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.planks), Lists.newArrayList("oak_planks", "spruce_planks", "birch_planks", "jungle_planks", "acacia_planks", "dark_oak_planks"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.sapling), Lists.newArrayList("oak_sapling", "spruce_sapling", "birch_sapling", "jungle_sapling", "acacia_sapling", "dark_oak_sapling"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.sand), Lists.newArrayList("sand", "red_sand"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.log), Lists.newArrayList("oak_log", "spruce_log", "birch_log", "jungle_log"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.leaves), Lists.newArrayList("oak_leaves", "spruce_leaves", "birch_leaves", "jungle_leaves"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.sponge), Lists.newArrayList("sponge", "sponge_wet"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.sandstone), Lists.newArrayList("sandstone", "chiseled_sandstone", "smooth_sandstone"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.red_sandstone), Lists.newArrayList("red_sandstone", "chiseled_red_sandstone", "smooth_red_sandstone"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.tallgrass), Lists.newArrayList("dead_bush", "tall_grass", "fern"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.deadbush), Lists.newArrayList("dead_bush"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.wool), Lists.newArrayList("black_wool", "red_wool", "green_wool", "brown_wool", "blue_wool", "purple_wool", "cyan_wool", "silver_wool", "gray_wool", "pink_wool", "lime_wool", "yellow_wool", "light_blue_wool", "magenta_wool", "orange_wool", "white_wool"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.yellow_flower), Lists.newArrayList("dandelion"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.red_flower), Lists.newArrayList("poppy", "blue_orchid", "allium", "houstonia", "red_tulip", "orange_tulip", "white_tulip", "pink_tulip", "oxeye_daisy"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.stone_slab), Lists.newArrayList("stone_slab", "sandstone_slab", "cobblestone_slab", "brick_slab", "stone_brick_slab", "nether_brick_slab", "quartz_slab"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.stone_slab2), Lists.newArrayList("red_sandstone_slab"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.stained_glass), Lists.newArrayList("black_stained_glass", "red_stained_glass", "green_stained_glass", "brown_stained_glass", "blue_stained_glass", "purple_stained_glass", "cyan_stained_glass", "silver_stained_glass", "gray_stained_glass", "pink_stained_glass", "lime_stained_glass", "yellow_stained_glass", "light_blue_stained_glass", "magenta_stained_glass", "orange_stained_glass", "white_stained_glass"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.monster_egg), Lists.newArrayList("stone_monster_egg", "cobblestone_monster_egg", "stone_brick_monster_egg", "mossy_brick_monster_egg", "cracked_brick_monster_egg", "chiseled_brick_monster_egg"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.stonebrick), Lists.newArrayList("stonebrick", "mossy_stonebrick", "cracked_stonebrick", "chiseled_stonebrick"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.wooden_slab), Lists.newArrayList("oak_slab", "spruce_slab", "birch_slab", "jungle_slab", "acacia_slab", "dark_oak_slab"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.cobblestone_wall), Lists.newArrayList("cobblestone_wall", "mossy_cobblestone_wall"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.anvil), Lists.newArrayList("anvil_intact", "anvil_slightly_damaged", "anvil_very_damaged"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.quartz_block), Lists.newArrayList("quartz_block", "chiseled_quartz_block", "quartz_column"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.stained_hardened_clay), Lists.newArrayList("black_stained_hardened_clay", "red_stained_hardened_clay", "green_stained_hardened_clay", "brown_stained_hardened_clay", "blue_stained_hardened_clay", "purple_stained_hardened_clay", "cyan_stained_hardened_clay", "silver_stained_hardened_clay", "gray_stained_hardened_clay", "pink_stained_hardened_clay", "lime_stained_hardened_clay", "yellow_stained_hardened_clay", "light_blue_stained_hardened_clay", "magenta_stained_hardened_clay", "orange_stained_hardened_clay", "white_stained_hardened_clay"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.stained_glass_pane), Lists.newArrayList("black_stained_glass_pane", "red_stained_glass_pane", "green_stained_glass_pane", "brown_stained_glass_pane", "blue_stained_glass_pane", "purple_stained_glass_pane", "cyan_stained_glass_pane", "silver_stained_glass_pane", "gray_stained_glass_pane", "pink_stained_glass_pane", "lime_stained_glass_pane", "yellow_stained_glass_pane", "light_blue_stained_glass_pane", "magenta_stained_glass_pane", "orange_stained_glass_pane", "white_stained_glass_pane"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.leaves2), Lists.newArrayList("acacia_leaves", "dark_oak_leaves"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.log2), Lists.newArrayList("acacia_log", "dark_oak_log"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.prismarine), Lists.newArrayList("prismarine", "prismarine_bricks", "dark_prismarine"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.carpet), Lists.newArrayList("black_carpet", "red_carpet", "green_carpet", "brown_carpet", "blue_carpet", "purple_carpet", "cyan_carpet", "silver_carpet", "gray_carpet", "pink_carpet", "lime_carpet", "yellow_carpet", "light_blue_carpet", "magenta_carpet", "orange_carpet", "white_carpet"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.double_plant), Lists.newArrayList("sunflower", "syringa", "double_grass", "double_fern", "double_rose", "paeonia"));
        this.variantNames.put(Items.bow, Lists.newArrayList("bow", "bow_pulling_0", "bow_pulling_1", "bow_pulling_2"));
        this.variantNames.put(Items.coal, Lists.newArrayList("coal", "charcoal"));
        this.variantNames.put(Items.fishing_rod, Lists.newArrayList("fishing_rod", "fishing_rod_cast"));
        this.variantNames.put(Items.fish, Lists.newArrayList("cod", "salmon", "clownfish", "pufferfish"));
        this.variantNames.put(Items.cooked_fish, Lists.newArrayList("cooked_cod", "cooked_salmon"));
        this.variantNames.put(Items.dye, Lists.newArrayList("dye_black", "dye_red", "dye_green", "dye_brown", "dye_blue", "dye_purple", "dye_cyan", "dye_silver", "dye_gray", "dye_pink", "dye_lime", "dye_yellow", "dye_light_blue", "dye_magenta", "dye_orange", "dye_white"));
        this.variantNames.put(Items.potionitem, Lists.newArrayList("bottle_drinkable", "bottle_splash"));
        this.variantNames.put(Items.skull, Lists.newArrayList("skull_skeleton", "skull_wither", "skull_zombie", "skull_char", "skull_creeper"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.oak_fence_gate), Lists.newArrayList("oak_fence_gate"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.oak_fence), Lists.newArrayList("oak_fence"));
        this.variantNames.put(Items.oak_door, Lists.newArrayList("oak_door"));
    }
    
    private List<String> getVariantNames(final Item p_177596_1_) {
        List<String> list = this.variantNames.get(p_177596_1_);
        if (list == null) {
            list = Collections.singletonList(Item.itemRegistry.getNameForObject(p_177596_1_).toString());
        }
        return list;
    }
    
    private ResourceLocation getItemLocation(final String p_177583_1_) {
        final ResourceLocation resourcelocation = new ResourceLocation(p_177583_1_);
        return new ResourceLocation(resourcelocation.getResourceDomain(), "item/" + resourcelocation.getResourcePath());
    }
    
    private void bakeBlockModels() {
        for (final ModelResourceLocation modelresourcelocation : this.variants.keySet()) {
            final WeightedBakedModel.Builder weightedbakedmodel$builder = new WeightedBakedModel.Builder();
            int i = 0;
            for (final ModelBlockDefinition.Variant modelblockdefinition$variant : this.variants.get(modelresourcelocation).getVariants()) {
                final ModelBlock modelblock = this.models.get(modelblockdefinition$variant.getModelLocation());
                if (modelblock != null && modelblock.isResolved()) {
                    ++i;
                    weightedbakedmodel$builder.add(this.bakeModel(modelblock, modelblockdefinition$variant.getRotation(), modelblockdefinition$variant.isUvLocked()), modelblockdefinition$variant.getWeight());
                }
                else {
                    ModelBakery.LOGGER.warn("Missing model for: " + modelresourcelocation);
                }
            }
            if (i == 0) {
                ModelBakery.LOGGER.warn("No weighted models for: " + modelresourcelocation);
            }
            else if (i == 1) {
                this.bakedRegistry.putObject(modelresourcelocation, weightedbakedmodel$builder.first());
            }
            else {
                this.bakedRegistry.putObject(modelresourcelocation, weightedbakedmodel$builder.build());
            }
        }
        for (final Map.Entry<String, ResourceLocation> entry : this.itemLocations.entrySet()) {
            final ResourceLocation resourcelocation = entry.getValue();
            final ModelResourceLocation modelresourcelocation2 = new ModelResourceLocation(entry.getKey(), "inventory");
            final ModelBlock modelblock2 = this.models.get(resourcelocation);
            if (modelblock2 != null && modelblock2.isResolved()) {
                if (this.isCustomRenderer(modelblock2)) {
                    this.bakedRegistry.putObject(modelresourcelocation2, new BuiltInModel(modelblock2.func_181682_g()));
                }
                else {
                    this.bakedRegistry.putObject(modelresourcelocation2, this.bakeModel(modelblock2, ModelRotation.X0_Y0, false));
                }
            }
            else {
                ModelBakery.LOGGER.warn("Missing model for: " + resourcelocation);
            }
        }
    }
    
    private Set<ResourceLocation> getVariantsTextureLocations() {
        final Set<ResourceLocation> set = (Set<ResourceLocation>)Sets.newHashSet();
        final List<ModelResourceLocation> list = (List<ModelResourceLocation>)Lists.newArrayList((Iterable<?>)this.variants.keySet());
        Collections.sort(list, new Comparator<ModelResourceLocation>() {
            @Override
            public int compare(final ModelResourceLocation p_compare_1_, final ModelResourceLocation p_compare_2_) {
                return p_compare_1_.toString().compareTo(p_compare_2_.toString());
            }
        });
        for (final ModelResourceLocation modelresourcelocation : list) {
            final ModelBlockDefinition.Variants modelblockdefinition$variants = this.variants.get(modelresourcelocation);
            for (final ModelBlockDefinition.Variant modelblockdefinition$variant : modelblockdefinition$variants.getVariants()) {
                final ModelBlock modelblock = this.models.get(modelblockdefinition$variant.getModelLocation());
                if (modelblock == null) {
                    ModelBakery.LOGGER.warn("Missing model for: " + modelresourcelocation);
                }
                else {
                    set.addAll(this.getTextureLocations(modelblock));
                }
            }
        }
        set.addAll(ModelBakery.LOCATIONS_BUILTIN_TEXTURES);
        return set;
    }
    
    private IBakedModel bakeModel(final ModelBlock modelBlockIn, final ModelRotation modelRotationIn, final boolean uvLocked) {
        final TextureAtlasSprite textureatlassprite = this.sprites.get(new ResourceLocation(modelBlockIn.resolveTextureName("particle")));
        final SimpleBakedModel.Builder simplebakedmodel$builder = new SimpleBakedModel.Builder(modelBlockIn).setTexture(textureatlassprite);
        for (final BlockPart blockpart : modelBlockIn.getElements()) {
            for (final EnumFacing enumfacing : blockpart.mapFaces.keySet()) {
                final BlockPartFace blockpartface = blockpart.mapFaces.get(enumfacing);
                final TextureAtlasSprite textureatlassprite2 = this.sprites.get(new ResourceLocation(modelBlockIn.resolveTextureName(blockpartface.texture)));
                if (blockpartface.cullFace == null) {
                    simplebakedmodel$builder.addGeneralQuad(this.makeBakedQuad(blockpart, blockpartface, textureatlassprite2, enumfacing, modelRotationIn, uvLocked));
                }
                else {
                    simplebakedmodel$builder.addFaceQuad(modelRotationIn.rotateFace(blockpartface.cullFace), this.makeBakedQuad(blockpart, blockpartface, textureatlassprite2, enumfacing, modelRotationIn, uvLocked));
                }
            }
        }
        return simplebakedmodel$builder.makeBakedModel();
    }
    
    private BakedQuad makeBakedQuad(final BlockPart p_177589_1_, final BlockPartFace p_177589_2_, final TextureAtlasSprite p_177589_3_, final EnumFacing p_177589_4_, final ModelRotation p_177589_5_, final boolean p_177589_6_) {
        return this.faceBakery.makeBakedQuad(p_177589_1_.positionFrom, p_177589_1_.positionTo, p_177589_2_, p_177589_3_, p_177589_4_, p_177589_5_, p_177589_1_.partRotation, p_177589_6_, p_177589_1_.shade);
    }
    
    private void loadModelsCheck() {
        this.loadModels();
        for (final ModelBlock modelblock : this.models.values()) {
            modelblock.getParentFromMap(this.models);
        }
        ModelBlock.checkModelHierarchy(this.models);
    }
    
    private void loadModels() {
        final Deque<ResourceLocation> deque = (Deque<ResourceLocation>)Queues.newArrayDeque();
        final Set<ResourceLocation> set = (Set<ResourceLocation>)Sets.newHashSet();
        for (final ResourceLocation resourcelocation : this.models.keySet()) {
            set.add(resourcelocation);
            final ResourceLocation resourcelocation2 = this.models.get(resourcelocation).getParentLocation();
            if (resourcelocation2 != null) {
                deque.add(resourcelocation2);
            }
        }
        while (!deque.isEmpty()) {
            final ResourceLocation resourcelocation3 = deque.pop();
            try {
                if (this.models.get(resourcelocation3) != null) {
                    continue;
                }
                final ModelBlock modelblock = this.loadModel(resourcelocation3);
                this.models.put(resourcelocation3, modelblock);
                final ResourceLocation resourcelocation4 = modelblock.getParentLocation();
                if (resourcelocation4 != null && !set.contains(resourcelocation4)) {
                    deque.add(resourcelocation4);
                }
            }
            catch (Exception exception) {
                ModelBakery.LOGGER.warn("In parent chain: " + ModelBakery.JOINER.join(this.getParentPath(resourcelocation3)) + "; unable to load model: '" + resourcelocation3 + "'", exception);
            }
            set.add(resourcelocation3);
        }
    }
    
    private List<ResourceLocation> getParentPath(final ResourceLocation p_177573_1_) {
        final List<ResourceLocation> list = Lists.newArrayList(p_177573_1_);
        ResourceLocation resourcelocation = p_177573_1_;
        while ((resourcelocation = this.getParentLocation(resourcelocation)) != null) {
            list.add(0, resourcelocation);
        }
        return list;
    }
    
    private ResourceLocation getParentLocation(final ResourceLocation p_177576_1_) {
        for (final Map.Entry<ResourceLocation, ModelBlock> entry : this.models.entrySet()) {
            final ModelBlock modelblock = entry.getValue();
            if (modelblock != null && p_177576_1_.equals(modelblock.getParentLocation())) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    private Set<ResourceLocation> getTextureLocations(final ModelBlock p_177585_1_) {
        final Set<ResourceLocation> set = (Set<ResourceLocation>)Sets.newHashSet();
        for (final BlockPart blockpart : p_177585_1_.getElements()) {
            for (final BlockPartFace blockpartface : blockpart.mapFaces.values()) {
                final ResourceLocation resourcelocation = new ResourceLocation(p_177585_1_.resolveTextureName(blockpartface.texture));
                set.add(resourcelocation);
            }
        }
        set.add(new ResourceLocation(p_177585_1_.resolveTextureName("particle")));
        return set;
    }
    
    private void loadSprites() {
        final Set<ResourceLocation> set = this.getVariantsTextureLocations();
        set.addAll(this.getItemsTextureLocations());
        set.remove(TextureMap.LOCATION_MISSING_TEXTURE);
        final IIconCreator iiconcreator = new IIconCreator() {
            @Override
            public void registerSprites(final TextureMap iconRegistry) {
                for (final ResourceLocation resourcelocation : set) {
                    final TextureAtlasSprite textureatlassprite = iconRegistry.registerSprite(resourcelocation);
                    ModelBakery.this.sprites.put(resourcelocation, textureatlassprite);
                }
            }
        };
        this.textureMap.loadSprites(this.resourceManager, iiconcreator);
        this.sprites.put(new ResourceLocation("missingno"), this.textureMap.getMissingSprite());
    }
    
    private Set<ResourceLocation> getItemsTextureLocations() {
        final Set<ResourceLocation> set = (Set<ResourceLocation>)Sets.newHashSet();
        for (final ResourceLocation resourcelocation : this.itemLocations.values()) {
            final ModelBlock modelblock = this.models.get(resourcelocation);
            if (modelblock != null) {
                set.add(new ResourceLocation(modelblock.resolveTextureName("particle")));
                if (this.hasItemModel(modelblock)) {
                    for (final String s : ItemModelGenerator.LAYERS) {
                        final ResourceLocation resourcelocation2 = new ResourceLocation(modelblock.resolveTextureName(s));
                        if (modelblock.getRootModel() == ModelBakery.MODEL_COMPASS && !TextureMap.LOCATION_MISSING_TEXTURE.equals(resourcelocation2)) {
                            TextureAtlasSprite.setLocationNameCompass(resourcelocation2.toString());
                        }
                        else if (modelblock.getRootModel() == ModelBakery.MODEL_CLOCK && !TextureMap.LOCATION_MISSING_TEXTURE.equals(resourcelocation2)) {
                            TextureAtlasSprite.setLocationNameClock(resourcelocation2.toString());
                        }
                        set.add(resourcelocation2);
                    }
                }
                else {
                    if (this.isCustomRenderer(modelblock)) {
                        continue;
                    }
                    for (final BlockPart blockpart : modelblock.getElements()) {
                        for (final BlockPartFace blockpartface : blockpart.mapFaces.values()) {
                            final ResourceLocation resourcelocation3 = new ResourceLocation(modelblock.resolveTextureName(blockpartface.texture));
                            set.add(resourcelocation3);
                        }
                    }
                }
            }
        }
        return set;
    }
    
    private boolean hasItemModel(final ModelBlock p_177581_1_) {
        if (p_177581_1_ == null) {
            return false;
        }
        final ModelBlock modelblock = p_177581_1_.getRootModel();
        return modelblock == ModelBakery.MODEL_GENERATED || modelblock == ModelBakery.MODEL_COMPASS || modelblock == ModelBakery.MODEL_CLOCK;
    }
    
    private boolean isCustomRenderer(final ModelBlock p_177587_1_) {
        if (p_177587_1_ == null) {
            return false;
        }
        final ModelBlock modelblock = p_177587_1_.getRootModel();
        return modelblock == ModelBakery.MODEL_ENTITY;
    }
    
    private void bakeItemModels() {
        for (final ResourceLocation resourcelocation : this.itemLocations.values()) {
            final ModelBlock modelblock = this.models.get(resourcelocation);
            if (this.hasItemModel(modelblock)) {
                final ModelBlock modelblock2 = this.makeItemModel(modelblock);
                if (modelblock2 != null) {
                    modelblock2.name = resourcelocation.toString();
                }
                this.models.put(resourcelocation, modelblock2);
            }
            else {
                if (!this.isCustomRenderer(modelblock)) {
                    continue;
                }
                this.models.put(resourcelocation, modelblock);
            }
        }
        for (final TextureAtlasSprite textureatlassprite : this.sprites.values()) {
            if (!textureatlassprite.hasAnimationMetadata()) {
                textureatlassprite.clearFramesTextureData();
            }
        }
    }
    
    private ModelBlock makeItemModel(final ModelBlock p_177582_1_) {
        return this.itemModelGenerator.makeItemModel(this.textureMap, p_177582_1_);
    }
}
