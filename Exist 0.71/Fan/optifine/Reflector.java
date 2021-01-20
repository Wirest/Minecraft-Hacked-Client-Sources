package optifine;

import com.google.common.base.Optional;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import javax.vecmath.Matrix4f;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.model.ModelBanner;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBat;
import net.minecraft.client.model.ModelBlaze;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelDragon;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.model.ModelEnderMite;
import net.minecraft.client.model.ModelGhast;
import net.minecraft.client.model.ModelGuardian;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.model.ModelHumanoidHead;
import net.minecraft.client.model.ModelLeashKnot;
import net.minecraft.client.model.ModelMagmaCube;
import net.minecraft.client.model.ModelOcelot;
import net.minecraft.client.model.ModelRabbit;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSign;
import net.minecraft.client.model.ModelSilverfish;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.model.ModelSquid;
import net.minecraft.client.model.ModelWitch;
import net.minecraft.client.model.ModelWither;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderBoat;
import net.minecraft.client.renderer.entity.RenderLeashKnot;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderMinecart;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.tileentity.RenderEnderCrystal;
import net.minecraft.client.renderer.tileentity.RenderItemFrame;
import net.minecraft.client.renderer.tileentity.RenderWitherSkull;
import net.minecraft.client.renderer.tileentity.TileEntityBannerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityEnchantmentTableRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityEnderChestRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.property.IUnlistedProperty;

public class Reflector
{
    private static boolean logForge = logEntry("*** Reflector Forge ***");
    public static ReflectorClass ForgeBiome = new ReflectorClass(BiomeGenBase.class);
    public static ReflectorMethod ForgeBiome_getWaterColorMultiplier = new ReflectorMethod(ForgeBiome, "getWaterColorMultiplier");
    public static ReflectorClass ForgeBlock = new ReflectorClass(Block.class);
    public static ReflectorMethod ForgeBlock_addDestroyEffects = new ReflectorMethod(ForgeBlock, "addDestroyEffects");
    public static ReflectorMethod ForgeBlock_addHitEffects = new ReflectorMethod(ForgeBlock, "addHitEffects");
    public static ReflectorMethod ForgeBlock_canCreatureSpawn = new ReflectorMethod(ForgeBlock, "canCreatureSpawn");
    public static ReflectorMethod ForgeBlock_canRenderInLayer = new ReflectorMethod(ForgeBlock, "canRenderInLayer", new Class[] {EnumWorldBlockLayer.class});
    public static ReflectorMethod ForgeBlock_doesSideBlockRendering = new ReflectorMethod(ForgeBlock, "doesSideBlockRendering");
    public static ReflectorMethod ForgeBlock_getBedDirection = new ReflectorMethod(ForgeBlock, "getBedDirection");
    public static ReflectorMethod ForgeBlock_getExtendedState = new ReflectorMethod(ForgeBlock, "getExtendedState");
    public static ReflectorMethod ForgeBlock_getLightOpacity = new ReflectorMethod(ForgeBlock, "getLightOpacity");
    public static ReflectorMethod ForgeBlock_getLightValue = new ReflectorMethod(ForgeBlock, "getLightValue");
    public static ReflectorMethod ForgeBlock_hasTileEntity = new ReflectorMethod(ForgeBlock, "hasTileEntity", new Class[] {IBlockState.class});
    public static ReflectorMethod ForgeBlock_isAir = new ReflectorMethod(ForgeBlock, "isAir");
    public static ReflectorMethod ForgeBlock_isBed = new ReflectorMethod(ForgeBlock, "isBed");
    public static ReflectorMethod ForgeBlock_isBedFoot = new ReflectorMethod(ForgeBlock, "isBedFoot");
    public static ReflectorMethod ForgeBlock_isSideSolid = new ReflectorMethod(ForgeBlock, "isSideSolid");
    public static ReflectorClass ForgeEntity = new ReflectorClass(Entity.class);
    public static ReflectorMethod ForgeEntity_canRiderInteract = new ReflectorMethod(ForgeEntity, "canRiderInteract");
    public static ReflectorField ForgeEntity_captureDrops = new ReflectorField(ForgeEntity, "captureDrops");
    public static ReflectorField ForgeEntity_capturedDrops = new ReflectorField(ForgeEntity, "capturedDrops");
    public static ReflectorMethod ForgeEntity_shouldRenderInPass = new ReflectorMethod(ForgeEntity, "shouldRenderInPass");
    public static ReflectorMethod ForgeEntity_shouldRiderSit = new ReflectorMethod(ForgeEntity, "shouldRiderSit");
    public static ReflectorClass ForgeHooksClient = new ReflectorClass("net.minecraftforge.client.ForgeHooksClient");
    public static ReflectorMethod ForgeHooksClient_applyTransform = new ReflectorMethod(ForgeHooksClient, "applyTransform", new Class[] {Matrix4f.class, Optional.class});
    public static ReflectorMethod ForgeHooksClient_dispatchRenderLast = new ReflectorMethod(ForgeHooksClient, "dispatchRenderLast");
    public static ReflectorMethod ForgeHooksClient_drawScreen = new ReflectorMethod(ForgeHooksClient, "drawScreen");
    public static ReflectorMethod ForgeHooksClient_fillNormal = new ReflectorMethod(ForgeHooksClient, "fillNormal");
    public static ReflectorMethod ForgeHooksClient_handleCameraTransforms = new ReflectorMethod(ForgeHooksClient, "handleCameraTransforms");
    public static ReflectorMethod ForgeHooksClient_getArmorModel = new ReflectorMethod(ForgeHooksClient, "getArmorModel");
    public static ReflectorMethod ForgeHooksClient_getArmorTexture = new ReflectorMethod(ForgeHooksClient, "getArmorTexture");
    public static ReflectorMethod ForgeHooksClient_getFogDensity = new ReflectorMethod(ForgeHooksClient, "getFogDensity");
    public static ReflectorMethod ForgeHooksClient_getFOVModifier = new ReflectorMethod(ForgeHooksClient, "getFOVModifier");
    public static ReflectorMethod ForgeHooksClient_getMatrix = new ReflectorMethod(ForgeHooksClient, "getMatrix", new Class[] {ModelRotation.class});
    public static ReflectorMethod ForgeHooksClient_getOffsetFOV = new ReflectorMethod(ForgeHooksClient, "getOffsetFOV");
    public static ReflectorMethod ForgeHooksClient_loadEntityShader = new ReflectorMethod(ForgeHooksClient, "loadEntityShader");
    public static ReflectorMethod ForgeHooksClient_onDrawBlockHighlight = new ReflectorMethod(ForgeHooksClient, "onDrawBlockHighlight");
    public static ReflectorMethod ForgeHooksClient_onFogRender = new ReflectorMethod(ForgeHooksClient, "onFogRender");
    public static ReflectorMethod ForgeHooksClient_onTextureStitchedPre = new ReflectorMethod(ForgeHooksClient, "onTextureStitchedPre");
    public static ReflectorMethod ForgeHooksClient_onTextureStitchedPost = new ReflectorMethod(ForgeHooksClient, "onTextureStitchedPost");
    public static ReflectorMethod ForgeHooksClient_orientBedCamera = new ReflectorMethod(ForgeHooksClient, "orientBedCamera");
    public static ReflectorMethod ForgeHooksClient_putQuadColor = new ReflectorMethod(ForgeHooksClient, "putQuadColor");
    public static ReflectorMethod ForgeHooksClient_renderFirstPersonHand = new ReflectorMethod(ForgeHooksClient, "renderFirstPersonHand");
    public static ReflectorMethod ForgeHooksClient_renderMainMenu = new ReflectorMethod(ForgeHooksClient, "renderMainMenu");
    public static ReflectorMethod ForgeHooksClient_setRenderLayer = new ReflectorMethod(ForgeHooksClient, "setRenderLayer");
    public static ReflectorMethod ForgeHooksClient_setRenderPass = new ReflectorMethod(ForgeHooksClient, "setRenderPass");
    public static ReflectorMethod ForgeHooksClient_transform = new ReflectorMethod(ForgeHooksClient, "transform");
    public static ReflectorClass ForgeItem = new ReflectorClass(Item.class);
    public static ReflectorMethod ForgeItem_getDurabilityForDisplay = new ReflectorMethod(ForgeItem, "getDurabilityForDisplay");
    public static ReflectorMethod ForgeItem_getModel = new ReflectorMethod(ForgeItem, "getModel");
    public static ReflectorMethod ForgeItem_onEntitySwing = new ReflectorMethod(ForgeItem, "onEntitySwing");
    public static ReflectorMethod ForgeItem_shouldCauseReequipAnimation = new ReflectorMethod(ForgeItem, "shouldCauseReequipAnimation");
    public static ReflectorMethod ForgeItem_showDurabilityBar = new ReflectorMethod(ForgeItem, "showDurabilityBar");
    public static ReflectorClass ForgeItemRecord = new ReflectorClass(ItemRecord.class);
    public static ReflectorMethod ForgeItemRecord_getRecordResource = new ReflectorMethod(ForgeItemRecord, "getRecordResource", new Class[] {String.class});
    public static ReflectorClass ForgePotionEffect = new ReflectorClass(PotionEffect.class);
    public static ReflectorMethod ForgePotionEffect_isCurativeItem = new ReflectorMethod(ForgePotionEffect, "isCurativeItem");
    public static ReflectorClass ForgeTileEntity = new ReflectorClass(TileEntity.class);
    public static ReflectorMethod ForgeTileEntity_canRenderBreaking = new ReflectorMethod(ForgeTileEntity, "canRenderBreaking");
    public static ReflectorMethod ForgeTileEntity_getRenderBoundingBox = new ReflectorMethod(ForgeTileEntity, "getRenderBoundingBox");
    public static ReflectorMethod ForgeTileEntity_hasFastRenderer = new ReflectorMethod(ForgeTileEntity, "hasFastRenderer");
    public static ReflectorMethod ForgeTileEntity_shouldRenderInPass = new ReflectorMethod(ForgeTileEntity, "shouldRenderInPass");
    public static ReflectorClass ForgeTileEntityRendererDispatcher = new ReflectorClass(TileEntityRendererDispatcher.class);
    public static ReflectorMethod ForgeTileEntityRendererDispatcher_preDrawBatch = new ReflectorMethod(ForgeTileEntityRendererDispatcher, "preDrawBatch");
    public static ReflectorMethod ForgeTileEntityRendererDispatcher_drawBatch = new ReflectorMethod(ForgeTileEntityRendererDispatcher, "drawBatch");
    public static ReflectorClass ForgeVertexFormatElementEnumUseage = new ReflectorClass(VertexFormatElement.EnumUsage.class);
    public static ReflectorMethod ForgeVertexFormatElementEnumUseage_preDraw = new ReflectorMethod(ForgeVertexFormatElementEnumUseage, "preDraw");
    public static ReflectorMethod ForgeVertexFormatElementEnumUseage_postDraw = new ReflectorMethod(ForgeVertexFormatElementEnumUseage, "postDraw");
    public static ReflectorClass ForgeWorld = new ReflectorClass(World.class);
    public static ReflectorMethod ForgeWorld_countEntities = new ReflectorMethod(ForgeWorld, "countEntities", new Class[] {EnumCreatureType.class, Boolean.TYPE});
    public static ReflectorMethod ForgeWorld_getPerWorldStorage = new ReflectorMethod(ForgeWorld, "getPerWorldStorage");
    public static ReflectorClass ForgeWorldProvider = new ReflectorClass(WorldProvider.class);
    public static ReflectorMethod ForgeWorldProvider_getCloudRenderer = new ReflectorMethod(ForgeWorldProvider, "getCloudRenderer");
    public static ReflectorMethod ForgeWorldProvider_getSkyRenderer = new ReflectorMethod(ForgeWorldProvider, "getSkyRenderer");
    public static ReflectorMethod ForgeWorldProvider_getWeatherRenderer = new ReflectorMethod(ForgeWorldProvider, "getWeatherRenderer");
    private static boolean logVanilla = logEntry("*** Reflector Vanilla ***");
    public static ReflectorClass ChunkProviderClient = new ReflectorClass(ChunkProviderClient.class);
    public static ReflectorField ChunkProviderClient_chunkMapping = new ReflectorField(ChunkProviderClient, LongHashMap.class);
    public static ReflectorClass GuiMainMenu = new ReflectorClass(GuiMainMenu.class);
    public static ReflectorField GuiMainMenu_splashText = new ReflectorField(GuiMainMenu, String.class);
    public static ReflectorClass Minecraft = new ReflectorClass(Minecraft.class);
    public static ReflectorField Minecraft_defaultResourcePack = new ReflectorField(Minecraft, DefaultResourcePack.class);
    public static ReflectorClass ModelHumanoidHead = new ReflectorClass(ModelHumanoidHead.class);
    public static ReflectorField ModelHumanoidHead_head = new ReflectorField(ModelHumanoidHead, ModelRenderer.class);
    public static ReflectorClass ModelBat = new ReflectorClass(ModelBat.class);
    public static ReflectorFields ModelBat_ModelRenderers = new ReflectorFields(ModelBat, ModelRenderer.class, 6);
    public static ReflectorClass ModelBlaze = new ReflectorClass(ModelBlaze.class);
    public static ReflectorField ModelBlaze_blazeHead = new ReflectorField(ModelBlaze, ModelRenderer.class);
    public static ReflectorField ModelBlaze_blazeSticks = new ReflectorField(ModelBlaze, ModelRenderer[].class);
    public static ReflectorClass ModelDragon = new ReflectorClass(ModelDragon.class);
    public static ReflectorFields ModelDragon_ModelRenderers = new ReflectorFields(ModelDragon, ModelRenderer.class, 12);
    public static ReflectorClass ModelEnderCrystal = new ReflectorClass(ModelEnderCrystal.class);
    public static ReflectorFields ModelEnderCrystal_ModelRenderers = new ReflectorFields(ModelEnderCrystal, ModelRenderer.class, 3);
    public static ReflectorClass RenderEnderCrystal = new ReflectorClass(RenderEnderCrystal.class);
    public static ReflectorField RenderEnderCrystal_modelEnderCrystal = new ReflectorField(RenderEnderCrystal, ModelBase.class, 0);
    public static ReflectorClass ModelEnderMite = new ReflectorClass(ModelEnderMite.class);
    public static ReflectorField ModelEnderMite_bodyParts = new ReflectorField(ModelEnderMite, ModelRenderer[].class);
    public static ReflectorClass ModelGhast = new ReflectorClass(ModelGhast.class);
    public static ReflectorField ModelGhast_body = new ReflectorField(ModelGhast, ModelRenderer.class);
    public static ReflectorField ModelGhast_tentacles = new ReflectorField(ModelGhast, ModelRenderer[].class);
    public static ReflectorClass ModelGuardian = new ReflectorClass(ModelGuardian.class);
    public static ReflectorField ModelGuardian_body = new ReflectorField(ModelGuardian, ModelRenderer.class, 0);
    public static ReflectorField ModelGuardian_eye = new ReflectorField(ModelGuardian, ModelRenderer.class, 1);
    public static ReflectorField ModelGuardian_spines = new ReflectorField(ModelGuardian, ModelRenderer[].class, 0);
    public static ReflectorField ModelGuardian_tail = new ReflectorField(ModelGuardian, ModelRenderer[].class, 1);
    public static ReflectorClass ModelHorse = new ReflectorClass(ModelHorse.class);
    public static ReflectorFields ModelHorse_ModelRenderers = new ReflectorFields(ModelHorse, ModelRenderer.class, 39);
    public static ReflectorClass RenderLeashKnot = new ReflectorClass(RenderLeashKnot.class);
    public static ReflectorField RenderLeashKnot_leashKnotModel = new ReflectorField(RenderLeashKnot, ModelLeashKnot.class);
    public static ReflectorClass ModelMagmaCube = new ReflectorClass(ModelMagmaCube.class);
    public static ReflectorField ModelMagmaCube_core = new ReflectorField(ModelMagmaCube, ModelRenderer.class);
    public static ReflectorField ModelMagmaCube_segments = new ReflectorField(ModelMagmaCube, ModelRenderer[].class);
    public static ReflectorClass ModelOcelot = new ReflectorClass(ModelOcelot.class);
    public static ReflectorFields ModelOcelot_ModelRenderers = new ReflectorFields(ModelOcelot, ModelRenderer.class, 8);
    public static ReflectorClass ModelRabbit = new ReflectorClass(ModelRabbit.class);
    public static ReflectorFields ModelRabbit_renderers = new ReflectorFields(ModelRabbit, ModelRenderer.class, 12);
    public static ReflectorClass ModelSilverfish = new ReflectorClass(ModelSilverfish.class);
    public static ReflectorField ModelSilverfish_bodyParts = new ReflectorField(ModelSilverfish, ModelRenderer[].class, 0);
    public static ReflectorField ModelSilverfish_wingParts = new ReflectorField(ModelSilverfish, ModelRenderer[].class, 1);
    public static ReflectorClass ModelSlime = new ReflectorClass(ModelSlime.class);
    public static ReflectorFields ModelSlime_ModelRenderers = new ReflectorFields(ModelSlime, ModelRenderer.class, 4);
    public static ReflectorClass ModelSquid = new ReflectorClass(ModelSquid.class);
    public static ReflectorField ModelSquid_body = new ReflectorField(ModelSquid, ModelRenderer.class);
    public static ReflectorField ModelSquid_tentacles = new ReflectorField(ModelSquid, ModelRenderer[].class);
    public static ReflectorClass ModelWitch = new ReflectorClass(ModelWitch.class);
    public static ReflectorField ModelWitch_mole = new ReflectorField(ModelWitch, ModelRenderer.class, 0);
    public static ReflectorField ModelWitch_hat = new ReflectorField(ModelWitch, ModelRenderer.class, 1);
    public static ReflectorClass ModelWither = new ReflectorClass(ModelWither.class);
    public static ReflectorField ModelWither_bodyParts = new ReflectorField(ModelWither, ModelRenderer[].class, 0);
    public static ReflectorField ModelWither_heads = new ReflectorField(ModelWither, ModelRenderer[].class, 1);
    public static ReflectorClass ModelWolf = new ReflectorClass(ModelWolf.class);
    public static ReflectorField ModelWolf_tail = new ReflectorField(ModelWolf, ModelRenderer.class, 6);
    public static ReflectorField ModelWolf_mane = new ReflectorField(ModelWolf, ModelRenderer.class, 7);
    public static ReflectorClass OptiFineClassTransformer = new ReflectorClass("optifine.OptiFineClassTransformer");
    public static ReflectorField OptiFineClassTransformer_instance = new ReflectorField(OptiFineClassTransformer, "instance");
    public static ReflectorMethod OptiFineClassTransformer_getOptiFineResource = new ReflectorMethod(OptiFineClassTransformer, "getOptiFineResource");
    public static ReflectorClass RenderBoat = new ReflectorClass(RenderBoat.class);
    public static ReflectorField RenderBoat_modelBoat = new ReflectorField(RenderBoat, ModelBase.class);
    public static ReflectorClass RenderMinecart = new ReflectorClass(RenderMinecart.class);
    public static ReflectorField RenderMinecart_modelMinecart = new ReflectorField(RenderMinecart, ModelBase.class);
    public static ReflectorClass RenderWitherSkull = new ReflectorClass(RenderWitherSkull.class);
    public static ReflectorField RenderWitherSkull_model = new ReflectorField(RenderWitherSkull, ModelSkeletonHead.class);
    public static ReflectorClass ResourcePackRepository = new ReflectorClass(ResourcePackRepository.class);
    public static ReflectorField ResourcePackRepository_repositoryEntries = new ReflectorField(ResourcePackRepository, List.class, 1);
    public static ReflectorClass TileEntityBannerRenderer = new ReflectorClass(TileEntityBannerRenderer.class);
    public static ReflectorField TileEntityBannerRenderer_bannerModel = new ReflectorField(TileEntityBannerRenderer, ModelBanner.class);
    public static ReflectorClass TileEntityChestRenderer = new ReflectorClass(TileEntityChestRenderer.class);
    public static ReflectorField TileEntityChestRenderer_simpleChest = new ReflectorField(TileEntityChestRenderer, ModelChest.class, 0);
    public static ReflectorField TileEntityChestRenderer_largeChest = new ReflectorField(TileEntityChestRenderer, ModelChest.class, 1);
    public static ReflectorClass TileEntityEnchantmentTableRenderer = new ReflectorClass(TileEntityEnchantmentTableRenderer.class);
    public static ReflectorField TileEntityEnchantmentTableRenderer_modelBook = new ReflectorField(TileEntityEnchantmentTableRenderer, ModelBook.class);
    public static ReflectorClass TileEntityEnderChestRenderer = new ReflectorClass(TileEntityEnderChestRenderer.class);
    public static ReflectorField TileEntityEnderChestRenderer_modelChest = new ReflectorField(TileEntityEnderChestRenderer, ModelChest.class);
    public static ReflectorClass TileEntitySignRenderer = new ReflectorClass(TileEntitySignRenderer.class);
    public static ReflectorField TileEntitySignRenderer_model = new ReflectorField(TileEntitySignRenderer, ModelSign.class);
    public static ReflectorClass TileEntitySkullRenderer = new ReflectorClass(TileEntitySkullRenderer.class);
    public static ReflectorField TileEntitySkullRenderer_skeletonHead = new ReflectorField(TileEntitySkullRenderer, ModelSkeletonHead.class, 0);
    public static ReflectorField TileEntitySkullRenderer_humanoidHead = new ReflectorField(TileEntitySkullRenderer, ModelSkeletonHead.class, 1);

    public static void callVoid(ReflectorMethod p_callVoid_0_, Object... p_callVoid_1_)
    {
        try
        {
            Method method = p_callVoid_0_.getTargetMethod();

            if (method == null)
            {
                return;
            }

            method.invoke((Object)null, p_callVoid_1_);
        }
        catch (Throwable throwable)
        {
            handleException(throwable, (Object)null, p_callVoid_0_, p_callVoid_1_);
        }
    }

    public static boolean callBoolean(ReflectorMethod p_callBoolean_0_, Object... p_callBoolean_1_)
    {
        try
        {
            Method method = p_callBoolean_0_.getTargetMethod();

            if (method == null)
            {
                return false;
            }
            else
            {
                return (Boolean)method.invoke((Object)null, p_callBoolean_1_);
            }
        }
        catch (Throwable throwable)
        {
            handleException(throwable, (Object)null, p_callBoolean_0_, p_callBoolean_1_);
            return false;
        }
    }

    public static int callInt(ReflectorMethod p_callInt_0_, Object... p_callInt_1_)
    {
        try
        {
            Method method = p_callInt_0_.getTargetMethod();

            if (method == null)
            {
                return 0;
            }
            else
            {
                return (Integer)method.invoke((Object)null, p_callInt_1_);
            }
        }
        catch (Throwable throwable)
        {
            handleException(throwable, (Object)null, p_callInt_0_, p_callInt_1_);
            return 0;
        }
    }

    public static float callFloat(ReflectorMethod p_callFloat_0_, Object... p_callFloat_1_)
    {
        try
        {
            Method method = p_callFloat_0_.getTargetMethod();

            if (method == null)
            {
                return 0.0F;
            }
            else
            {
                return (Float)method.invoke((Object)null, p_callFloat_1_);
            }
        }
        catch (Throwable throwable)
        {
            handleException(throwable, (Object)null, p_callFloat_0_, p_callFloat_1_);
            return 0.0F;
        }
    }

    public static double callDouble(ReflectorMethod p_callDouble_0_, Object... p_callDouble_1_)
    {
        try
        {
            Method method = p_callDouble_0_.getTargetMethod();

            if (method == null)
            {
                return 0.0D;
            }
            else
            {
                return (Double)method.invoke((Object)null, p_callDouble_1_);
            }
        }
        catch (Throwable throwable)
        {
            handleException(throwable, (Object)null, p_callDouble_0_, p_callDouble_1_);
            return 0.0D;
        }
    }

    public static String callString(ReflectorMethod p_callString_0_, Object... p_callString_1_)
    {
        try
        {
            Method method = p_callString_0_.getTargetMethod();

            if (method == null)
            {
                return null;
            }
            else
            {
                return (String)method.invoke((Object)null, p_callString_1_);
            }
        }
        catch (Throwable throwable)
        {
            handleException(throwable, (Object)null, p_callString_0_, p_callString_1_);
            return null;
        }
    }

    public static Object call(ReflectorMethod p_call_0_, Object... p_call_1_)
    {
        try
        {
            Method method = p_call_0_.getTargetMethod();

            if (method == null)
            {
                return null;
            }
            else
            {
                return method.invoke((Object)null, p_call_1_);
            }
        }
        catch (Throwable throwable)
        {
            handleException(throwable, (Object)null, p_call_0_, p_call_1_);
            return null;
        }
    }

    public static void callVoid(Object p_callVoid_0_, ReflectorMethod p_callVoid_1_, Object... p_callVoid_2_)
    {
        try
        {
            if (p_callVoid_0_ == null)
            {
                return;
            }

            Method method = p_callVoid_1_.getTargetMethod();

            if (method == null)
            {
                return;
            }

            method.invoke(p_callVoid_0_, p_callVoid_2_);
        }
        catch (Throwable throwable)
        {
            handleException(throwable, p_callVoid_0_, p_callVoid_1_, p_callVoid_2_);
        }
    }

    public static boolean callBoolean(Object p_callBoolean_0_, ReflectorMethod p_callBoolean_1_, Object... p_callBoolean_2_)
    {
        try
        {
            Method method = p_callBoolean_1_.getTargetMethod();

            if (method == null)
            {
                return false;
            }
            else
            {
                return (Boolean)method.invoke(p_callBoolean_0_, p_callBoolean_2_);
            }
        }
        catch (Throwable throwable)
        {
            handleException(throwable, p_callBoolean_0_, p_callBoolean_1_, p_callBoolean_2_);
            return false;
        }
    }

    public static int callInt(Object p_callInt_0_, ReflectorMethod p_callInt_1_, Object... p_callInt_2_)
    {
        try
        {
            Method method = p_callInt_1_.getTargetMethod();

            if (method == null)
            {
                return 0;
            }
            else
            {
                return (Integer)method.invoke(p_callInt_0_, p_callInt_2_);
            }
        }
        catch (Throwable throwable)
        {
            handleException(throwable, p_callInt_0_, p_callInt_1_, p_callInt_2_);
            return 0;
        }
    }

    public static float callFloat(Object p_callFloat_0_, ReflectorMethod p_callFloat_1_, Object... p_callFloat_2_)
    {
        try
        {
            Method method = p_callFloat_1_.getTargetMethod();

            if (method == null)
            {
                return 0.0F;
            }
            else
            {
                return (Float)method.invoke(p_callFloat_0_, p_callFloat_2_);
            }
        }
        catch (Throwable throwable)
        {
            handleException(throwable, p_callFloat_0_, p_callFloat_1_, p_callFloat_2_);
            return 0.0F;
        }
    }

    public static double callDouble(Object p_callDouble_0_, ReflectorMethod p_callDouble_1_, Object... p_callDouble_2_)
    {
        try
        {
            Method method = p_callDouble_1_.getTargetMethod();

            if (method == null)
            {
                return 0.0D;
            }
            else
            {
                return (Double)method.invoke(p_callDouble_0_, p_callDouble_2_);
            }
        }
        catch (Throwable throwable)
        {
            handleException(throwable, p_callDouble_0_, p_callDouble_1_, p_callDouble_2_);
            return 0.0D;
        }
    }

    public static String callString(Object p_callString_0_, ReflectorMethod p_callString_1_, Object... p_callString_2_)
    {
        try
        {
            Method method = p_callString_1_.getTargetMethod();

            if (method == null)
            {
                return null;
            }
            else
            {
                return (String)method.invoke(p_callString_0_, p_callString_2_);
            }
        }
        catch (Throwable throwable)
        {
            handleException(throwable, p_callString_0_, p_callString_1_, p_callString_2_);
            return null;
        }
    }

    public static Object call(Object p_call_0_, ReflectorMethod p_call_1_, Object... p_call_2_)
    {
        try
        {
            Method method = p_call_1_.getTargetMethod();

            if (method == null)
            {
                return null;
            }
            else
            {
                return method.invoke(p_call_0_, p_call_2_);
            }
        }
        catch (Throwable throwable)
        {
            handleException(throwable, p_call_0_, p_call_1_, p_call_2_);
            return null;
        }
    }

    public static Object getFieldValue(ReflectorField p_getFieldValue_0_)
    {
        return getFieldValue((Object)null, p_getFieldValue_0_);
    }

    public static Object getFieldValue(Object p_getFieldValue_0_, ReflectorField p_getFieldValue_1_)
    {
        try
        {
            Field field = p_getFieldValue_1_.getTargetField();

            if (field == null)
            {
                return null;
            }
            else
            {
                return field.get(p_getFieldValue_0_);
            }
        }
        catch (Throwable throwable)
        {
            throwable.printStackTrace();
            return null;
        }
    }

    public static Object getFieldValue(ReflectorFields p_getFieldValue_0_, int p_getFieldValue_1_)
    {
        ReflectorField reflectorfield = p_getFieldValue_0_.getReflectorField(p_getFieldValue_1_);
        return reflectorfield == null ? null : getFieldValue(reflectorfield);
    }

    public static Object getFieldValue(Object p_getFieldValue_0_, ReflectorFields p_getFieldValue_1_, int p_getFieldValue_2_)
    {
        ReflectorField reflectorfield = p_getFieldValue_1_.getReflectorField(p_getFieldValue_2_);
        return reflectorfield == null ? null : getFieldValue(p_getFieldValue_0_, reflectorfield);
    }

    public static float getFieldValueFloat(Object p_getFieldValueFloat_0_, ReflectorField p_getFieldValueFloat_1_, float p_getFieldValueFloat_2_)
    {
        Object object = getFieldValue(p_getFieldValueFloat_0_, p_getFieldValueFloat_1_);

        if (!(object instanceof Float))
        {
            return p_getFieldValueFloat_2_;
        }
        else
        {
            return (Float)object;
        }
    }

    public static boolean setFieldValue(ReflectorField p_setFieldValue_0_, Object p_setFieldValue_1_)
    {
        return setFieldValue((Object)null, p_setFieldValue_0_, p_setFieldValue_1_);
    }

    public static boolean setFieldValue(Object p_setFieldValue_0_, ReflectorField p_setFieldValue_1_, Object p_setFieldValue_2_)
    {
        try
        {
            Field field = p_setFieldValue_1_.getTargetField();

            if (field == null)
            {
                return false;
            }
            else
            {
                field.set(p_setFieldValue_0_, p_setFieldValue_2_);
                return true;
            }
        }
        catch (Throwable throwable)
        {
            throwable.printStackTrace();
            return false;
        }
    }

    public static boolean postForgeBusEvent(ReflectorConstructor p_postForgeBusEvent_0_, Object... p_postForgeBusEvent_1_)
    {
        Object object = newInstance(p_postForgeBusEvent_0_, p_postForgeBusEvent_1_);
        return postForgeBusEvent(object);
    }

    public static boolean postForgeBusEvent(Object p_postForgeBusEvent_0_)
    {
        return false;
    }

    public static Object newInstance(ReflectorConstructor p_newInstance_0_, Object... p_newInstance_1_)
    {
        Constructor constructor = p_newInstance_0_.getTargetConstructor();

        if (constructor == null)
        {
            return null;
        }
        else
        {
            try
            {
                return constructor.newInstance(p_newInstance_1_);
            }
            catch (Throwable throwable)
            {
                handleException(throwable, p_newInstance_0_, p_newInstance_1_);
                return null;
            }
        }
    }

    public static boolean matchesTypes(Class[] p_matchesTypes_0_, Class[] p_matchesTypes_1_)
    {
        if (p_matchesTypes_0_.length != p_matchesTypes_1_.length)
        {
            return false;
        }
        else
        {
            for (int i = 0; i < p_matchesTypes_1_.length; ++i)
            {
                Class oclass = p_matchesTypes_0_[i];
                Class oclass1 = p_matchesTypes_1_[i];

                if (oclass != oclass1)
                {
                    return false;
                }
            }

            return true;
        }
    }

    private static void dbgCall(boolean p_dbgCall_0_, String p_dbgCall_1_, ReflectorMethod p_dbgCall_2_, Object[] p_dbgCall_3_, Object p_dbgCall_4_)
    {
        String s = p_dbgCall_2_.getTargetMethod().getDeclaringClass().getName();
        String s1 = p_dbgCall_2_.getTargetMethod().getName();
        String s2 = "";

        if (p_dbgCall_0_)
        {
            s2 = " static";
        }

        Config.dbg(p_dbgCall_1_ + s2 + " " + s + "." + s1 + "(" + Config.arrayToString(p_dbgCall_3_) + ") => " + p_dbgCall_4_);
    }

    private static void dbgCallVoid(boolean p_dbgCallVoid_0_, String p_dbgCallVoid_1_, ReflectorMethod p_dbgCallVoid_2_, Object[] p_dbgCallVoid_3_)
    {
        String s = p_dbgCallVoid_2_.getTargetMethod().getDeclaringClass().getName();
        String s1 = p_dbgCallVoid_2_.getTargetMethod().getName();
        String s2 = "";

        if (p_dbgCallVoid_0_)
        {
            s2 = " static";
        }

        Config.dbg(p_dbgCallVoid_1_ + s2 + " " + s + "." + s1 + "(" + Config.arrayToString(p_dbgCallVoid_3_) + ")");
    }

    private static void dbgFieldValue(boolean p_dbgFieldValue_0_, String p_dbgFieldValue_1_, ReflectorField p_dbgFieldValue_2_, Object p_dbgFieldValue_3_)
    {
        String s = p_dbgFieldValue_2_.getTargetField().getDeclaringClass().getName();
        String s1 = p_dbgFieldValue_2_.getTargetField().getName();
        String s2 = "";

        if (p_dbgFieldValue_0_)
        {
            s2 = " static";
        }

        Config.dbg(p_dbgFieldValue_1_ + s2 + " " + s + "." + s1 + " => " + p_dbgFieldValue_3_);
    }

    private static void handleException(Throwable p_handleException_0_, Object p_handleException_1_, ReflectorMethod p_handleException_2_, Object[] p_handleException_3_)
    {
        if (p_handleException_0_ instanceof InvocationTargetException)
        {
            Throwable throwable = p_handleException_0_.getCause();

            if (throwable instanceof RuntimeException)
            {
                throw (RuntimeException)throwable;
            }
            else
            {
                p_handleException_0_.printStackTrace();
            }
        }
        else
        {
            if (p_handleException_0_ instanceof IllegalArgumentException)
            {
                Config.warn("*** IllegalArgumentException ***");
                Config.warn("Method: " + p_handleException_2_.getTargetMethod());
                Config.warn("Object: " + p_handleException_1_);
                Config.warn("Parameter classes: " + Config.arrayToString(getClasses(p_handleException_3_)));
                Config.warn("Parameters: " + Config.arrayToString(p_handleException_3_));
            }

            Config.warn("*** Exception outside of method ***");
            Config.warn("Method deactivated: " + p_handleException_2_.getTargetMethod());
            p_handleException_2_.deactivate();
            p_handleException_0_.printStackTrace();
        }
    }

    private static void handleException(Throwable p_handleException_0_, ReflectorConstructor p_handleException_1_, Object[] p_handleException_2_)
    {
        if (!(p_handleException_0_ instanceof InvocationTargetException)) {
            if (p_handleException_0_ instanceof IllegalArgumentException) {
                Config.warn("*** IllegalArgumentException ***");
                Config.warn("Constructor: " + p_handleException_1_.getTargetConstructor());
                Config.warn("Parameter classes: " + Config.arrayToString(getClasses(p_handleException_2_)));
                Config.warn("Parameters: " + Config.arrayToString(p_handleException_2_));
            }

            Config.warn("*** Exception outside of constructor ***");
            Config.warn("Constructor deactivated: " + p_handleException_1_.getTargetConstructor());
            p_handleException_1_.deactivate();
        }
        p_handleException_0_.printStackTrace();
    }

    private static Object[] getClasses(Object[] p_getClasses_0_)
    {
        if (p_getClasses_0_ == null)
        {
            return new Class[0];
        }
        else
        {
            Class[] aclass = new Class[p_getClasses_0_.length];

            for (int i = 0; i < aclass.length; ++i)
            {
                Object object = p_getClasses_0_[i];

                if (object != null)
                {
                    aclass[i] = object.getClass();
                }
            }

            return aclass;
        }
    }

    private static ReflectorField[] getReflectorFields(ReflectorClass p_getReflectorFields_0_, Class p_getReflectorFields_1_, int p_getReflectorFields_2_)
    {
        ReflectorField[] areflectorfield = new ReflectorField[p_getReflectorFields_2_];

        for (int i = 0; i < areflectorfield.length; ++i)
        {
            areflectorfield[i] = new ReflectorField(p_getReflectorFields_0_, p_getReflectorFields_1_, i);
        }

        return areflectorfield;
    }

    private static boolean logEntry(String p_logEntry_0_)
    {
        Config.dbg(p_logEntry_0_);
        return true;
    }
}
