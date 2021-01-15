/*     */ package optfine;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.renderer.EntityRenderer;
/*     */ import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EnumCreatureType;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemRecord;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.world.ChunkCoordIntPair;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldProvider;
/*     */ 
/*     */ public class Reflector
/*     */ {
/*  27 */   public static ReflectorClass ModLoader = new ReflectorClass("ModLoader");
/*  28 */   public static ReflectorMethod ModLoader_renderWorldBlock = new ReflectorMethod(ModLoader, "renderWorldBlock");
/*  29 */   public static ReflectorMethod ModLoader_renderInvBlock = new ReflectorMethod(ModLoader, "renderInvBlock");
/*  30 */   public static ReflectorMethod ModLoader_renderBlockIsItemFull3D = new ReflectorMethod(ModLoader, "renderBlockIsItemFull3D");
/*  31 */   public static ReflectorMethod ModLoader_registerServer = new ReflectorMethod(ModLoader, "registerServer");
/*  32 */   public static ReflectorMethod ModLoader_getCustomAnimationLogic = new ReflectorMethod(ModLoader, "getCustomAnimationLogic");
/*  33 */   public static ReflectorClass FMLRenderAccessLibrary = new ReflectorClass("net.minecraft.src.FMLRenderAccessLibrary");
/*  34 */   public static ReflectorMethod FMLRenderAccessLibrary_renderWorldBlock = new ReflectorMethod(FMLRenderAccessLibrary, "renderWorldBlock");
/*  35 */   public static ReflectorMethod FMLRenderAccessLibrary_renderInventoryBlock = new ReflectorMethod(FMLRenderAccessLibrary, "renderInventoryBlock");
/*  36 */   public static ReflectorMethod FMLRenderAccessLibrary_renderItemAsFull3DBlock = new ReflectorMethod(FMLRenderAccessLibrary, "renderItemAsFull3DBlock");
/*  37 */   public static ReflectorClass LightCache = new ReflectorClass("LightCache");
/*  38 */   public static ReflectorField LightCache_cache = new ReflectorField(LightCache, "cache");
/*  39 */   public static ReflectorMethod LightCache_clear = new ReflectorMethod(LightCache, "clear");
/*  40 */   public static ReflectorClass BlockCoord = new ReflectorClass("BlockCoord");
/*  41 */   public static ReflectorMethod BlockCoord_resetPool = new ReflectorMethod(BlockCoord, "resetPool");
/*  42 */   public static ReflectorClass MinecraftForge = new ReflectorClass("net.minecraftforge.common.MinecraftForge");
/*  43 */   public static ReflectorField MinecraftForge_EVENT_BUS = new ReflectorField(MinecraftForge, "EVENT_BUS");
/*  44 */   public static ReflectorClass ForgeHooks = new ReflectorClass("net.minecraftforge.common.ForgeHooks");
/*  45 */   public static ReflectorMethod ForgeHooks_onLivingSetAttackTarget = new ReflectorMethod(ForgeHooks, "onLivingSetAttackTarget");
/*  46 */   public static ReflectorMethod ForgeHooks_onLivingUpdate = new ReflectorMethod(ForgeHooks, "onLivingUpdate");
/*  47 */   public static ReflectorMethod ForgeHooks_onLivingAttack = new ReflectorMethod(ForgeHooks, "onLivingAttack");
/*  48 */   public static ReflectorMethod ForgeHooks_onLivingHurt = new ReflectorMethod(ForgeHooks, "onLivingHurt");
/*  49 */   public static ReflectorMethod ForgeHooks_onLivingDeath = new ReflectorMethod(ForgeHooks, "onLivingDeath");
/*  50 */   public static ReflectorMethod ForgeHooks_onLivingDrops = new ReflectorMethod(ForgeHooks, "onLivingDrops");
/*  51 */   public static ReflectorMethod ForgeHooks_onLivingFall = new ReflectorMethod(ForgeHooks, "onLivingFall");
/*  52 */   public static ReflectorMethod ForgeHooks_onLivingJump = new ReflectorMethod(ForgeHooks, "onLivingJump");
/*  53 */   public static ReflectorClass MinecraftForgeClient = new ReflectorClass("net.minecraftforge.client.MinecraftForgeClient");
/*  54 */   public static ReflectorMethod MinecraftForgeClient_getRenderPass = new ReflectorMethod(MinecraftForgeClient, "getRenderPass");
/*  55 */   public static ReflectorMethod MinecraftForgeClient_getItemRenderer = new ReflectorMethod(MinecraftForgeClient, "getItemRenderer");
/*  56 */   public static ReflectorClass ForgeHooksClient = new ReflectorClass("net.minecraftforge.client.ForgeHooksClient");
/*  57 */   public static ReflectorMethod ForgeHooksClient_onDrawBlockHighlight = new ReflectorMethod(ForgeHooksClient, "onDrawBlockHighlight");
/*  58 */   public static ReflectorMethod ForgeHooksClient_orientBedCamera = new ReflectorMethod(ForgeHooksClient, "orientBedCamera");
/*  59 */   public static ReflectorMethod ForgeHooksClient_dispatchRenderLast = new ReflectorMethod(ForgeHooksClient, "dispatchRenderLast");
/*  60 */   public static ReflectorMethod ForgeHooksClient_setRenderPass = new ReflectorMethod(ForgeHooksClient, "setRenderPass");
/*  61 */   public static ReflectorMethod ForgeHooksClient_onTextureStitchedPre = new ReflectorMethod(ForgeHooksClient, "onTextureStitchedPre");
/*  62 */   public static ReflectorMethod ForgeHooksClient_onTextureStitchedPost = new ReflectorMethod(ForgeHooksClient, "onTextureStitchedPost");
/*  63 */   public static ReflectorMethod ForgeHooksClient_renderFirstPersonHand = new ReflectorMethod(ForgeHooksClient, "renderFirstPersonHand");
/*  64 */   public static ReflectorMethod ForgeHooksClient_getOffsetFOV = new ReflectorMethod(ForgeHooksClient, "getOffsetFOV");
/*  65 */   public static ReflectorMethod ForgeHooksClient_drawScreen = new ReflectorMethod(ForgeHooksClient, "drawScreen");
/*  66 */   public static ReflectorMethod ForgeHooksClient_onFogRender = new ReflectorMethod(ForgeHooksClient, "onFogRender");
/*  67 */   public static ReflectorMethod ForgeHooksClient_setRenderLayer = new ReflectorMethod(ForgeHooksClient, "setRenderLayer");
/*  68 */   public static ReflectorClass FMLCommonHandler = new ReflectorClass("net.minecraftforge.fml.common.FMLCommonHandler");
/*  69 */   public static ReflectorMethod FMLCommonHandler_instance = new ReflectorMethod(FMLCommonHandler, "instance");
/*  70 */   public static ReflectorMethod FMLCommonHandler_handleServerStarting = new ReflectorMethod(FMLCommonHandler, "handleServerStarting");
/*  71 */   public static ReflectorMethod FMLCommonHandler_handleServerAboutToStart = new ReflectorMethod(FMLCommonHandler, "handleServerAboutToStart");
/*  72 */   public static ReflectorMethod FMLCommonHandler_enhanceCrashReport = new ReflectorMethod(FMLCommonHandler, "enhanceCrashReport");
/*  73 */   public static ReflectorMethod FMLCommonHandler_getBrandings = new ReflectorMethod(FMLCommonHandler, "getBrandings");
/*  74 */   public static ReflectorClass FMLClientHandler = new ReflectorClass("net.minecraftforge.fml.client.FMLClientHandler");
/*  75 */   public static ReflectorMethod FMLClientHandler_instance = new ReflectorMethod(FMLClientHandler, "instance");
/*  76 */   public static ReflectorMethod FMLClientHandler_isLoading = new ReflectorMethod(FMLClientHandler, "isLoading");
/*  77 */   public static ReflectorClass ItemRenderType = new ReflectorClass("net.minecraftforge.client.IItemRenderer$ItemRenderType");
/*  78 */   public static ReflectorField ItemRenderType_EQUIPPED = new ReflectorField(ItemRenderType, "EQUIPPED");
/*  79 */   public static ReflectorClass ForgeWorldProvider = new ReflectorClass(WorldProvider.class);
/*  80 */   public static ReflectorMethod ForgeWorldProvider_getSkyRenderer = new ReflectorMethod(ForgeWorldProvider, "getSkyRenderer");
/*  81 */   public static ReflectorMethod ForgeWorldProvider_getCloudRenderer = new ReflectorMethod(ForgeWorldProvider, "getCloudRenderer");
/*  82 */   public static ReflectorMethod ForgeWorldProvider_getWeatherRenderer = new ReflectorMethod(ForgeWorldProvider, "getWeatherRenderer");
/*  83 */   public static ReflectorClass ForgeWorld = new ReflectorClass(World.class);
/*  84 */   public static ReflectorMethod ForgeWorld_countEntities = new ReflectorMethod(ForgeWorld, "countEntities", new Class[] { EnumCreatureType.class, Boolean.TYPE });
/*  85 */   public static ReflectorMethod ForgeWorld_getPerWorldStorage = new ReflectorMethod(ForgeWorld, "getPerWorldStorage");
/*  86 */   public static ReflectorClass IRenderHandler = new ReflectorClass("net.minecraftforge.client.IRenderHandler");
/*  87 */   public static ReflectorMethod IRenderHandler_render = new ReflectorMethod(IRenderHandler, "render");
/*  88 */   public static ReflectorClass DimensionManager = new ReflectorClass("net.minecraftforge.common.DimensionManager");
/*  89 */   public static ReflectorMethod DimensionManager_getStaticDimensionIDs = new ReflectorMethod(DimensionManager, "getStaticDimensionIDs");
/*  90 */   public static ReflectorClass WorldEvent_Load = new ReflectorClass("net.minecraftforge.event.world.WorldEvent$Load");
/*  91 */   public static ReflectorConstructor WorldEvent_Load_Constructor = new ReflectorConstructor(WorldEvent_Load, new Class[] { World.class });
/*  92 */   public static ReflectorClass DrawScreenEvent_Pre = new ReflectorClass("net.minecraftforge.client.event.GuiScreenEvent$DrawScreenEvent$Pre");
/*  93 */   public static ReflectorConstructor DrawScreenEvent_Pre_Constructor = new ReflectorConstructor(DrawScreenEvent_Pre, new Class[] { GuiScreen.class, Integer.TYPE, Integer.TYPE, Float.TYPE });
/*  94 */   public static ReflectorClass DrawScreenEvent_Post = new ReflectorClass("net.minecraftforge.client.event.GuiScreenEvent$DrawScreenEvent$Post");
/*  95 */   public static ReflectorConstructor DrawScreenEvent_Post_Constructor = new ReflectorConstructor(DrawScreenEvent_Post, new Class[] { GuiScreen.class, Integer.TYPE, Integer.TYPE, Float.TYPE });
/*  96 */   public static ReflectorClass EntityViewRenderEvent_FogColors = new ReflectorClass("net.minecraftforge.client.event.EntityViewRenderEvent$FogColors");
/*  97 */   public static ReflectorConstructor EntityViewRenderEvent_FogColors_Constructor = new ReflectorConstructor(EntityViewRenderEvent_FogColors, new Class[] { EntityRenderer.class, Entity.class, Block.class, Double.TYPE, Float.TYPE, Float.TYPE, Float.TYPE });
/*  98 */   public static ReflectorField EntityViewRenderEvent_FogColors_red = new ReflectorField(EntityViewRenderEvent_FogColors, "red");
/*  99 */   public static ReflectorField EntityViewRenderEvent_FogColors_green = new ReflectorField(EntityViewRenderEvent_FogColors, "green");
/* 100 */   public static ReflectorField EntityViewRenderEvent_FogColors_blue = new ReflectorField(EntityViewRenderEvent_FogColors, "blue");
/* 101 */   public static ReflectorClass EntityViewRenderEvent_FogDensity = new ReflectorClass("net.minecraftforge.client.event.EntityViewRenderEvent$FogDensity");
/* 102 */   public static ReflectorConstructor EntityViewRenderEvent_FogDensity_Constructor = new ReflectorConstructor(EntityViewRenderEvent_FogDensity, new Class[] { EntityRenderer.class, Entity.class, Block.class, Double.TYPE, Float.TYPE });
/* 103 */   public static ReflectorField EntityViewRenderEvent_FogDensity_density = new ReflectorField(EntityViewRenderEvent_FogDensity, "density");
/* 104 */   public static ReflectorClass EntityViewRenderEvent_RenderFogEvent = new ReflectorClass("net.minecraftforge.client.event.EntityViewRenderEvent$RenderFogEvent");
/* 105 */   public static ReflectorConstructor EntityViewRenderEvent_RenderFogEvent_Constructor = new ReflectorConstructor(EntityViewRenderEvent_RenderFogEvent, new Class[] { EntityRenderer.class, Entity.class, Block.class, Double.TYPE, Integer.TYPE, Float.TYPE });
/* 106 */   public static ReflectorClass EventBus = new ReflectorClass("net.minecraftforge.fml.common.eventhandler.EventBus");
/* 107 */   public static ReflectorMethod EventBus_post = new ReflectorMethod(EventBus, "post");
/* 108 */   public static ReflectorClass Event_Result = new ReflectorClass("net.minecraftforge.fml.common.eventhandler.Event$Result");
/* 109 */   public static ReflectorField Event_Result_DENY = new ReflectorField(Event_Result, "DENY");
/* 110 */   public static ReflectorField Event_Result_ALLOW = new ReflectorField(Event_Result, "ALLOW");
/* 111 */   public static ReflectorField Event_Result_DEFAULT = new ReflectorField(Event_Result, "DEFAULT");
/* 112 */   public static ReflectorClass ForgeEventFactory = new ReflectorClass("net.minecraftforge.event.ForgeEventFactory");
/* 113 */   public static ReflectorMethod ForgeEventFactory_canEntitySpawn = new ReflectorMethod(ForgeEventFactory, "canEntitySpawn");
/* 114 */   public static ReflectorMethod ForgeEventFactory_canEntityDespawn = new ReflectorMethod(ForgeEventFactory, "canEntityDespawn");
/* 115 */   public static ReflectorClass ChunkWatchEvent_UnWatch = new ReflectorClass("net.minecraftforge.event.world.ChunkWatchEvent$UnWatch");
/* 116 */   public static ReflectorConstructor ChunkWatchEvent_UnWatch_Constructor = new ReflectorConstructor(ChunkWatchEvent_UnWatch, new Class[] { ChunkCoordIntPair.class, EntityPlayerMP.class });
/* 117 */   public static ReflectorClass ForgeBlock = new ReflectorClass(Block.class);
/* 118 */   public static ReflectorMethod ForgeBlock_getBedDirection = new ReflectorMethod(ForgeBlock, "getBedDirection");
/* 119 */   public static ReflectorMethod ForgeBlock_isBedFoot = new ReflectorMethod(ForgeBlock, "isBedFoot");
/* 120 */   public static ReflectorMethod ForgeBlock_hasTileEntity = new ReflectorMethod(ForgeBlock, "hasTileEntity", new Class[] { IBlockState.class });
/* 121 */   public static ReflectorMethod ForgeBlock_canCreatureSpawn = new ReflectorMethod(ForgeBlock, "canCreatureSpawn");
/* 122 */   public static ReflectorMethod ForgeBlock_addHitEffects = new ReflectorMethod(ForgeBlock, "addHitEffects");
/* 123 */   public static ReflectorMethod ForgeBlock_addDestroyEffects = new ReflectorMethod(ForgeBlock, "addDestroyEffects");
/* 124 */   public static ReflectorMethod ForgeBlock_isAir = new ReflectorMethod(ForgeBlock, "isAir");
/* 125 */   public static ReflectorMethod ForgeBlock_canRenderInLayer = new ReflectorMethod(ForgeBlock, "canRenderInLayer");
/* 126 */   public static ReflectorClass ForgeEntity = new ReflectorClass(Entity.class);
/* 127 */   public static ReflectorField ForgeEntity_captureDrops = new ReflectorField(ForgeEntity, "captureDrops");
/* 128 */   public static ReflectorField ForgeEntity_capturedDrops = new ReflectorField(ForgeEntity, "capturedDrops");
/* 129 */   public static ReflectorMethod ForgeEntity_shouldRenderInPass = new ReflectorMethod(ForgeEntity, "shouldRenderInPass");
/* 130 */   public static ReflectorMethod ForgeEntity_canRiderInteract = new ReflectorMethod(ForgeEntity, "canRiderInteract");
/* 131 */   public static ReflectorClass ForgeTileEntity = new ReflectorClass(TileEntity.class);
/* 132 */   public static ReflectorMethod ForgeTileEntity_shouldRenderInPass = new ReflectorMethod(ForgeTileEntity, "shouldRenderInPass");
/* 133 */   public static ReflectorMethod ForgeTileEntity_getRenderBoundingBox = new ReflectorMethod(ForgeTileEntity, "getRenderBoundingBox");
/* 134 */   public static ReflectorMethod ForgeTileEntity_canRenderBreaking = new ReflectorMethod(ForgeTileEntity, "canRenderBreaking");
/* 135 */   public static ReflectorClass ForgeItem = new ReflectorClass(Item.class);
/* 136 */   public static ReflectorMethod ForgeItem_onEntitySwing = new ReflectorMethod(ForgeItem, "onEntitySwing");
/* 137 */   public static ReflectorClass ForgePotionEffect = new ReflectorClass(PotionEffect.class);
/* 138 */   public static ReflectorMethod ForgePotionEffect_isCurativeItem = new ReflectorMethod(ForgePotionEffect, "isCurativeItem");
/* 139 */   public static ReflectorClass ForgeItemRecord = new ReflectorClass(ItemRecord.class);
/* 140 */   public static ReflectorMethod ForgeItemRecord_getRecordResource = new ReflectorMethod(ForgeItemRecord, "getRecordResource", new Class[] { String.class });
/* 141 */   public static ReflectorClass ForgeVertexFormatElementEnumUseage = new ReflectorClass(VertexFormatElement.EnumUsage.class);
/* 142 */   public static ReflectorMethod ForgeVertexFormatElementEnumUseage_preDraw = new ReflectorMethod(ForgeVertexFormatElementEnumUseage, "preDraw");
/* 143 */   public static ReflectorMethod ForgeVertexFormatElementEnumUseage_postDraw = new ReflectorMethod(ForgeVertexFormatElementEnumUseage, "postDraw");
/*     */   
/*     */   public static void callVoid(ReflectorMethod p_callVoid_0_, Object... p_callVoid_1_)
/*     */   {
/*     */     try
/*     */     {
/* 149 */       Method method = p_callVoid_0_.getTargetMethod();
/*     */       
/* 151 */       if (method == null)
/*     */       {
/* 153 */         return;
/*     */       }
/*     */       
/* 156 */       method.invoke(null, p_callVoid_1_);
/*     */     }
/*     */     catch (Throwable throwable)
/*     */     {
/* 160 */       handleException(throwable, null, p_callVoid_0_, p_callVoid_1_);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean callBoolean(ReflectorMethod p_callBoolean_0_, Object... p_callBoolean_1_)
/*     */   {
/*     */     try
/*     */     {
/* 168 */       Method method = p_callBoolean_0_.getTargetMethod();
/*     */       
/* 170 */       if (method == null)
/*     */       {
/* 172 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 176 */       Boolean obool = (Boolean)method.invoke(null, p_callBoolean_1_);
/* 177 */       return obool.booleanValue();
/*     */ 
/*     */     }
/*     */     catch (Throwable throwable)
/*     */     {
/* 182 */       handleException(throwable, null, p_callBoolean_0_, p_callBoolean_1_); }
/* 183 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public static int callInt(ReflectorMethod p_callInt_0_, Object... p_callInt_1_)
/*     */   {
/*     */     try
/*     */     {
/* 191 */       Method method = p_callInt_0_.getTargetMethod();
/*     */       
/* 193 */       if (method == null)
/*     */       {
/* 195 */         return 0;
/*     */       }
/*     */       
/*     */ 
/* 199 */       Integer integer = (Integer)method.invoke(null, p_callInt_1_);
/* 200 */       return integer.intValue();
/*     */ 
/*     */     }
/*     */     catch (Throwable throwable)
/*     */     {
/* 205 */       handleException(throwable, null, p_callInt_0_, p_callInt_1_); }
/* 206 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public static float callFloat(ReflectorMethod p_callFloat_0_, Object... p_callFloat_1_)
/*     */   {
/*     */     try
/*     */     {
/* 214 */       Method method = p_callFloat_0_.getTargetMethod();
/*     */       
/* 216 */       if (method == null)
/*     */       {
/* 218 */         return 0.0F;
/*     */       }
/*     */       
/*     */ 
/* 222 */       Float f = (Float)method.invoke(null, p_callFloat_1_);
/* 223 */       return f.floatValue();
/*     */ 
/*     */     }
/*     */     catch (Throwable throwable)
/*     */     {
/* 228 */       handleException(throwable, null, p_callFloat_0_, p_callFloat_1_); }
/* 229 */     return 0.0F;
/*     */   }
/*     */   
/*     */ 
/*     */   public static String callString(ReflectorMethod p_callString_0_, Object... p_callString_1_)
/*     */   {
/*     */     try
/*     */     {
/* 237 */       Method method = p_callString_0_.getTargetMethod();
/*     */       
/* 239 */       if (method == null)
/*     */       {
/* 241 */         return null;
/*     */       }
/*     */       
/*     */ 
/* 245 */       return (String)method.invoke(null, p_callString_1_);
/*     */ 
/*     */     }
/*     */     catch (Throwable throwable)
/*     */     {
/*     */ 
/* 251 */       handleException(throwable, null, p_callString_0_, p_callString_1_); }
/* 252 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public static Object call(ReflectorMethod p_call_0_, Object... p_call_1_)
/*     */   {
/*     */     try
/*     */     {
/* 260 */       Method method = p_call_0_.getTargetMethod();
/*     */       
/* 262 */       if (method == null)
/*     */       {
/* 264 */         return null;
/*     */       }
/*     */       
/*     */ 
/* 268 */       return method.invoke(null, p_call_1_);
/*     */ 
/*     */     }
/*     */     catch (Throwable throwable)
/*     */     {
/*     */ 
/* 274 */       handleException(throwable, null, p_call_0_, p_call_1_); }
/* 275 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public static void callVoid(Object p_callVoid_0_, ReflectorMethod p_callVoid_1_, Object... p_callVoid_2_)
/*     */   {
/*     */     try
/*     */     {
/* 283 */       if (p_callVoid_0_ == null)
/*     */       {
/* 285 */         return;
/*     */       }
/*     */       
/* 288 */       Method method = p_callVoid_1_.getTargetMethod();
/*     */       
/* 290 */       if (method == null)
/*     */       {
/* 292 */         return;
/*     */       }
/*     */       
/* 295 */       method.invoke(p_callVoid_0_, p_callVoid_2_);
/*     */     }
/*     */     catch (Throwable throwable)
/*     */     {
/* 299 */       handleException(throwable, p_callVoid_0_, p_callVoid_1_, p_callVoid_2_);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean callBoolean(Object p_callBoolean_0_, ReflectorMethod p_callBoolean_1_, Object... p_callBoolean_2_)
/*     */   {
/*     */     try
/*     */     {
/* 307 */       Method method = p_callBoolean_1_.getTargetMethod();
/*     */       
/* 309 */       if (method == null)
/*     */       {
/* 311 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 315 */       Boolean obool = (Boolean)method.invoke(p_callBoolean_0_, p_callBoolean_2_);
/* 316 */       return obool.booleanValue();
/*     */ 
/*     */     }
/*     */     catch (Throwable throwable)
/*     */     {
/* 321 */       handleException(throwable, p_callBoolean_0_, p_callBoolean_1_, p_callBoolean_2_); }
/* 322 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public static int callInt(Object p_callInt_0_, ReflectorMethod p_callInt_1_, Object... p_callInt_2_)
/*     */   {
/*     */     try
/*     */     {
/* 330 */       Method method = p_callInt_1_.getTargetMethod();
/*     */       
/* 332 */       if (method == null)
/*     */       {
/* 334 */         return 0;
/*     */       }
/*     */       
/*     */ 
/* 338 */       Integer integer = (Integer)method.invoke(p_callInt_0_, p_callInt_2_);
/* 339 */       return integer.intValue();
/*     */ 
/*     */     }
/*     */     catch (Throwable throwable)
/*     */     {
/* 344 */       handleException(throwable, p_callInt_0_, p_callInt_1_, p_callInt_2_); }
/* 345 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public static float callFloat(Object p_callFloat_0_, ReflectorMethod p_callFloat_1_, Object... p_callFloat_2_)
/*     */   {
/*     */     try
/*     */     {
/* 353 */       Method method = p_callFloat_1_.getTargetMethod();
/*     */       
/* 355 */       if (method == null)
/*     */       {
/* 357 */         return 0.0F;
/*     */       }
/*     */       
/*     */ 
/* 361 */       Float f = (Float)method.invoke(p_callFloat_0_, p_callFloat_2_);
/* 362 */       return f.floatValue();
/*     */ 
/*     */     }
/*     */     catch (Throwable throwable)
/*     */     {
/* 367 */       handleException(throwable, p_callFloat_0_, p_callFloat_1_, p_callFloat_2_); }
/* 368 */     return 0.0F;
/*     */   }
/*     */   
/*     */ 
/*     */   public static String callString(Object p_callString_0_, ReflectorMethod p_callString_1_, Object... p_callString_2_)
/*     */   {
/*     */     try
/*     */     {
/* 376 */       Method method = p_callString_1_.getTargetMethod();
/*     */       
/* 378 */       if (method == null)
/*     */       {
/* 380 */         return null;
/*     */       }
/*     */       
/*     */ 
/* 384 */       return (String)method.invoke(p_callString_0_, p_callString_2_);
/*     */ 
/*     */     }
/*     */     catch (Throwable throwable)
/*     */     {
/*     */ 
/* 390 */       handleException(throwable, p_callString_0_, p_callString_1_, p_callString_2_); }
/* 391 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public static Object call(Object p_call_0_, ReflectorMethod p_call_1_, Object... p_call_2_)
/*     */   {
/*     */     try
/*     */     {
/* 399 */       Method method = p_call_1_.getTargetMethod();
/*     */       
/* 401 */       if (method == null)
/*     */       {
/* 403 */         return null;
/*     */       }
/*     */       
/*     */ 
/* 407 */       return method.invoke(p_call_0_, p_call_2_);
/*     */ 
/*     */     }
/*     */     catch (Throwable throwable)
/*     */     {
/*     */ 
/* 413 */       handleException(throwable, p_call_0_, p_call_1_, p_call_2_); }
/* 414 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public static Object getFieldValue(ReflectorField p_getFieldValue_0_)
/*     */   {
/* 420 */     return getFieldValue(null, p_getFieldValue_0_);
/*     */   }
/*     */   
/*     */   public static Object getFieldValue(Object p_getFieldValue_0_, ReflectorField p_getFieldValue_1_)
/*     */   {
/*     */     try
/*     */     {
/* 427 */       Field field = p_getFieldValue_1_.getTargetField();
/*     */       
/* 429 */       if (field == null)
/*     */       {
/* 431 */         return null;
/*     */       }
/*     */       
/*     */ 
/* 435 */       return field.get(p_getFieldValue_0_);
/*     */ 
/*     */     }
/*     */     catch (Throwable throwable)
/*     */     {
/*     */ 
/* 441 */       throwable.printStackTrace(); }
/* 442 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public static float getFieldValueFloat(Object p_getFieldValueFloat_0_, ReflectorField p_getFieldValueFloat_1_, float p_getFieldValueFloat_2_)
/*     */   {
/* 448 */     Object object = getFieldValue(p_getFieldValueFloat_0_, p_getFieldValueFloat_1_);
/*     */     
/* 450 */     if (!(object instanceof Float))
/*     */     {
/* 452 */       return p_getFieldValueFloat_2_;
/*     */     }
/*     */     
/*     */ 
/* 456 */     Float f = (Float)object;
/* 457 */     return f.floatValue();
/*     */   }
/*     */   
/*     */ 
/*     */   public static void setFieldValue(ReflectorField p_setFieldValue_0_, Object p_setFieldValue_1_)
/*     */   {
/* 463 */     setFieldValue(null, p_setFieldValue_0_, p_setFieldValue_1_);
/*     */   }
/*     */   
/*     */   public static void setFieldValue(Object p_setFieldValue_0_, ReflectorField p_setFieldValue_1_, Object p_setFieldValue_2_)
/*     */   {
/*     */     try
/*     */     {
/* 470 */       Field field = p_setFieldValue_1_.getTargetField();
/*     */       
/* 472 */       if (field == null)
/*     */       {
/* 474 */         return;
/*     */       }
/*     */       
/* 477 */       field.set(p_setFieldValue_0_, p_setFieldValue_2_);
/*     */     }
/*     */     catch (Throwable throwable)
/*     */     {
/* 481 */       throwable.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean postForgeBusEvent(ReflectorConstructor p_postForgeBusEvent_0_, Object... p_postForgeBusEvent_1_)
/*     */   {
/* 487 */     Object object = newInstance(p_postForgeBusEvent_0_, p_postForgeBusEvent_1_);
/* 488 */     return object == null ? false : postForgeBusEvent(object);
/*     */   }
/*     */   
/*     */   public static boolean postForgeBusEvent(Object p_postForgeBusEvent_0_)
/*     */   {
/* 493 */     if (p_postForgeBusEvent_0_ == null)
/*     */     {
/* 495 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 499 */     Object object = getFieldValue(MinecraftForge_EVENT_BUS);
/*     */     
/* 501 */     if (object == null)
/*     */     {
/* 503 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 507 */     Object object1 = call(object, EventBus_post, new Object[] { p_postForgeBusEvent_0_ });
/*     */     
/* 509 */     if (!(object1 instanceof Boolean))
/*     */     {
/* 511 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 515 */     Boolean obool = (Boolean)object1;
/* 516 */     return obool.booleanValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Object newInstance(ReflectorConstructor p_newInstance_0_, Object... p_newInstance_1_)
/*     */   {
/* 524 */     Constructor constructor = p_newInstance_0_.getTargetConstructor();
/*     */     
/* 526 */     if (constructor == null)
/*     */     {
/* 528 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 534 */       return constructor.newInstance(p_newInstance_1_);
/*     */ 
/*     */     }
/*     */     catch (Throwable throwable)
/*     */     {
/* 539 */       handleException(throwable, p_newInstance_0_, p_newInstance_1_); }
/* 540 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean matchesTypes(Class[] p_matchesTypes_0_, Class[] p_matchesTypes_1_)
/*     */   {
/* 547 */     if (p_matchesTypes_0_.length != p_matchesTypes_1_.length)
/*     */     {
/* 549 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 553 */     for (int i = 0; i < p_matchesTypes_1_.length; i++)
/*     */     {
/* 555 */       Class oclass = p_matchesTypes_0_[i];
/* 556 */       Class oclass1 = p_matchesTypes_1_[i];
/*     */       
/* 558 */       if (oclass != oclass1)
/*     */       {
/* 560 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 564 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   private static void dbgCall(boolean p_dbgCall_0_, String p_dbgCall_1_, ReflectorMethod p_dbgCall_2_, Object[] p_dbgCall_3_, Object p_dbgCall_4_)
/*     */   {
/* 570 */     String s = p_dbgCall_2_.getTargetMethod().getDeclaringClass().getName();
/* 571 */     String s1 = p_dbgCall_2_.getTargetMethod().getName();
/* 572 */     String s2 = "";
/*     */     
/* 574 */     if (p_dbgCall_0_)
/*     */     {
/* 576 */       s2 = " static";
/*     */     }
/*     */     
/* 579 */     Config.dbg(p_dbgCall_1_ + s2 + " " + s + "." + s1 + "(" + Config.arrayToString(p_dbgCall_3_) + ") => " + p_dbgCall_4_);
/*     */   }
/*     */   
/*     */   private static void dbgCallVoid(boolean p_dbgCallVoid_0_, String p_dbgCallVoid_1_, ReflectorMethod p_dbgCallVoid_2_, Object[] p_dbgCallVoid_3_)
/*     */   {
/* 584 */     String s = p_dbgCallVoid_2_.getTargetMethod().getDeclaringClass().getName();
/* 585 */     String s1 = p_dbgCallVoid_2_.getTargetMethod().getName();
/* 586 */     String s2 = "";
/*     */     
/* 588 */     if (p_dbgCallVoid_0_)
/*     */     {
/* 590 */       s2 = " static";
/*     */     }
/*     */     
/* 593 */     Config.dbg(p_dbgCallVoid_1_ + s2 + " " + s + "." + s1 + "(" + Config.arrayToString(p_dbgCallVoid_3_) + ")");
/*     */   }
/*     */   
/*     */   private static void dbgFieldValue(boolean p_dbgFieldValue_0_, String p_dbgFieldValue_1_, ReflectorField p_dbgFieldValue_2_, Object p_dbgFieldValue_3_)
/*     */   {
/* 598 */     String s = p_dbgFieldValue_2_.getTargetField().getDeclaringClass().getName();
/* 599 */     String s1 = p_dbgFieldValue_2_.getTargetField().getName();
/* 600 */     String s2 = "";
/*     */     
/* 602 */     if (p_dbgFieldValue_0_)
/*     */     {
/* 604 */       s2 = " static";
/*     */     }
/*     */     
/* 607 */     Config.dbg(p_dbgFieldValue_1_ + s2 + " " + s + "." + s1 + " => " + p_dbgFieldValue_3_);
/*     */   }
/*     */   
/*     */   private static void handleException(Throwable p_handleException_0_, Object p_handleException_1_, ReflectorMethod p_handleException_2_, Object[] p_handleException_3_)
/*     */   {
/* 612 */     if ((p_handleException_0_ instanceof InvocationTargetException))
/*     */     {
/* 614 */       p_handleException_0_.printStackTrace();
/*     */     }
/*     */     else
/*     */     {
/* 618 */       if ((p_handleException_0_ instanceof IllegalArgumentException))
/*     */       {
/* 620 */         Config.warn("*** IllegalArgumentException ***");
/* 621 */         Config.warn("Method: " + p_handleException_2_.getTargetMethod());
/* 622 */         Config.warn("Object: " + p_handleException_1_);
/* 623 */         Config.warn("Parameter classes: " + Config.arrayToString(getClasses(p_handleException_3_)));
/* 624 */         Config.warn("Parameters: " + Config.arrayToString(p_handleException_3_));
/*     */       }
/*     */       
/* 627 */       Config.warn("*** Exception outside of method ***");
/* 628 */       Config.warn("Method deactivated: " + p_handleException_2_.getTargetMethod());
/* 629 */       p_handleException_2_.deactivate();
/* 630 */       p_handleException_0_.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   private static void handleException(Throwable p_handleException_0_, ReflectorConstructor p_handleException_1_, Object[] p_handleException_2_)
/*     */   {
/* 636 */     if ((p_handleException_0_ instanceof InvocationTargetException))
/*     */     {
/* 638 */       p_handleException_0_.printStackTrace();
/*     */     }
/*     */     else
/*     */     {
/* 642 */       if ((p_handleException_0_ instanceof IllegalArgumentException))
/*     */       {
/* 644 */         Config.warn("*** IllegalArgumentException ***");
/* 645 */         Config.warn("Constructor: " + p_handleException_1_.getTargetConstructor());
/* 646 */         Config.warn("Parameter classes: " + Config.arrayToString(getClasses(p_handleException_2_)));
/* 647 */         Config.warn("Parameters: " + Config.arrayToString(p_handleException_2_));
/*     */       }
/*     */       
/* 650 */       Config.warn("*** Exception outside of constructor ***");
/* 651 */       Config.warn("Constructor deactivated: " + p_handleException_1_.getTargetConstructor());
/* 652 */       p_handleException_1_.deactivate();
/* 653 */       p_handleException_0_.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   private static Object[] getClasses(Object[] p_getClasses_0_)
/*     */   {
/* 659 */     if (p_getClasses_0_ == null)
/*     */     {
/* 661 */       return new Class[0];
/*     */     }
/*     */     
/*     */ 
/* 665 */     Class[] aclass = new Class[p_getClasses_0_.length];
/*     */     
/* 667 */     for (int i = 0; i < aclass.length; i++)
/*     */     {
/* 669 */       Object object = p_getClasses_0_[i];
/*     */       
/* 671 */       if (object != null)
/*     */       {
/* 673 */         aclass[i] = object.getClass();
/*     */       }
/*     */     }
/*     */     
/* 677 */     return aclass;
/*     */   }
/*     */   
/*     */ 
/*     */   public static Field getField(Class p_getField_0_, Class p_getField_1_)
/*     */   {
/*     */     try
/*     */     {
/* 685 */       Field[] afield = p_getField_0_.getDeclaredFields();
/*     */       
/* 687 */       for (int i = 0; i < afield.length; i++)
/*     */       {
/* 689 */         Field field = afield[i];
/*     */         
/* 691 */         if (field.getType() == p_getField_1_)
/*     */         {
/* 693 */           field.setAccessible(true);
/* 694 */           return field;
/*     */         }
/*     */       }
/*     */       
/* 698 */       return null;
/*     */     }
/*     */     catch (Exception var5) {}
/*     */     
/* 702 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public static Field[] getFields(Class p_getFields_0_, Class p_getFields_1_)
/*     */   {
/* 708 */     List list = new ArrayList();
/*     */     
/*     */     try
/*     */     {
/* 712 */       Field[] afield = p_getFields_0_.getDeclaredFields();
/*     */       
/* 714 */       for (int i = 0; i < afield.length; i++)
/*     */       {
/* 716 */         Field field = afield[i];
/*     */         
/* 718 */         if (field.getType() == p_getFields_1_)
/*     */         {
/* 720 */           field.setAccessible(true);
/* 721 */           list.add(field);
/*     */         }
/*     */       }
/*     */       
/* 725 */       return (Field[])list.toArray(new Field[list.size()]);
/*     */     }
/*     */     catch (Exception var6) {}
/*     */     
/*     */ 
/* 730 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\Reflector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */