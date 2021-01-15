package net.minecraft.src;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;

public class Reflector {
   public static ReflectorClass ModLoader = new ReflectorClass("ModLoader");
   public static ReflectorMethod ModLoader_renderWorldBlock;
   public static ReflectorMethod ModLoader_renderInvBlock;
   public static ReflectorMethod ModLoader_renderBlockIsItemFull3D;
   public static ReflectorMethod ModLoader_registerServer;
   public static ReflectorMethod ModLoader_getCustomAnimationLogic;
   public static ReflectorClass FMLRenderAccessLibrary;
   public static ReflectorMethod FMLRenderAccessLibrary_renderWorldBlock;
   public static ReflectorMethod FMLRenderAccessLibrary_renderInventoryBlock;
   public static ReflectorMethod FMLRenderAccessLibrary_renderItemAsFull3DBlock;
   public static ReflectorClass LightCache;
   public static ReflectorField LightCache_cache;
   public static ReflectorMethod LightCache_clear;
   public static ReflectorClass BlockCoord;
   public static ReflectorMethod BlockCoord_resetPool;
   public static ReflectorClass MinecraftForge;
   public static ReflectorField MinecraftForge_EVENT_BUS;
   public static ReflectorClass ForgeHooks;
   public static ReflectorMethod ForgeHooks_onLivingSetAttackTarget;
   public static ReflectorMethod ForgeHooks_onLivingUpdate;
   public static ReflectorMethod ForgeHooks_onLivingAttack;
   public static ReflectorMethod ForgeHooks_onLivingHurt;
   public static ReflectorMethod ForgeHooks_onLivingDeath;
   public static ReflectorMethod ForgeHooks_onLivingDrops;
   public static ReflectorMethod ForgeHooks_onLivingFall;
   public static ReflectorMethod ForgeHooks_onLivingJump;
   public static ReflectorClass MinecraftForgeClient;
   public static ReflectorMethod MinecraftForgeClient_getRenderPass;
   public static ReflectorMethod MinecraftForgeClient_getItemRenderer;
   public static ReflectorClass ForgeHooksClient;
   public static ReflectorMethod ForgeHooksClient_onDrawBlockHighlight;
   public static ReflectorMethod ForgeHooksClient_orientBedCamera;
   public static ReflectorMethod ForgeHooksClient_renderEquippedItem;
   public static ReflectorMethod ForgeHooksClient_dispatchRenderLast;
   public static ReflectorMethod ForgeHooksClient_onTextureLoadPre;
   public static ReflectorMethod ForgeHooksClient_setRenderPass;
   public static ReflectorMethod ForgeHooksClient_onTextureStitchedPre;
   public static ReflectorMethod ForgeHooksClient_onTextureStitchedPost;
   public static ReflectorMethod ForgeHooksClient_renderFirstPersonHand;
   public static ReflectorMethod ForgeHooksClient_setWorldRendererRB;
   public static ReflectorMethod ForgeHooksClient_onPreRenderWorld;
   public static ReflectorMethod ForgeHooksClient_onPostRenderWorld;
   public static ReflectorClass FMLCommonHandler;
   public static ReflectorMethod FMLCommonHandler_instance;
   public static ReflectorMethod FMLCommonHandler_handleServerStarting;
   public static ReflectorMethod FMLCommonHandler_handleServerAboutToStart;
   public static ReflectorMethod FMLCommonHandler_enhanceCrashReport;
   public static ReflectorClass FMLClientHandler;
   public static ReflectorMethod FMLClientHandler_instance;
   public static ReflectorMethod FMLClientHandler_isLoading;
   public static ReflectorClass ItemRenderType;
   public static ReflectorField ItemRenderType_EQUIPPED;
   public static ReflectorClass ForgeWorldProvider;
   public static ReflectorMethod ForgeWorldProvider_getSkyRenderer;
   public static ReflectorMethod ForgeWorldProvider_getCloudRenderer;
   public static ReflectorMethod ForgeWorldProvider_getWeatherRenderer;
   public static ReflectorClass ForgeWorld;
   public static ReflectorMethod ForgeWorld_countEntities;
   public static ReflectorClass IRenderHandler;
   public static ReflectorMethod IRenderHandler_render;
   public static ReflectorClass DimensionManager;
   public static ReflectorMethod DimensionManager_getStaticDimensionIDs;
   public static ReflectorClass WorldEvent_Load;
   public static ReflectorConstructor WorldEvent_Load_Constructor;
   public static ReflectorClass DrawScreenEvent_Pre;
   public static ReflectorConstructor DrawScreenEvent_Pre_Constructor;
   public static ReflectorClass DrawScreenEvent_Post;
   public static ReflectorConstructor DrawScreenEvent_Post_Constructor;
   public static ReflectorClass EntityViewRenderEvent_FogColors;
   public static ReflectorConstructor EntityViewRenderEvent_FogColors_Constructor;
   public static ReflectorField EntityViewRenderEvent_FogColors_red;
   public static ReflectorField EntityViewRenderEvent_FogColors_green;
   public static ReflectorField EntityViewRenderEvent_FogColors_blue;
   public static ReflectorClass EntityViewRenderEvent_FogDensity;
   public static ReflectorConstructor EntityViewRenderEvent_FogDensity_Constructor;
   public static ReflectorField EntityViewRenderEvent_FogDensity_density;
   public static ReflectorClass EntityViewRenderEvent_RenderFogEvent;
   public static ReflectorConstructor EntityViewRenderEvent_RenderFogEvent_Constructor;
   public static ReflectorClass EventBus;
   public static ReflectorMethod EventBus_post;
   public static ReflectorClass Event_Result;
   public static ReflectorField Event_Result_DENY;
   public static ReflectorField Event_Result_ALLOW;
   public static ReflectorField Event_Result_DEFAULT;
   public static ReflectorClass ForgeEventFactory;
   public static ReflectorMethod ForgeEventFactory_canEntitySpawn;
   public static ReflectorMethod ForgeEventFactory_canEntityDespawn;
   public static ReflectorClass ChunkWatchEvent_UnWatch;
   public static ReflectorConstructor ChunkWatchEvent_UnWatch_Constructor;
   public static ReflectorClass ForgeBlock;
   public static ReflectorMethod ForgeBlock_getBedDirection;
   public static ReflectorMethod ForgeBlock_isBedFoot;
   public static ReflectorMethod ForgeBlock_canRenderInPass;
   public static ReflectorMethod ForgeBlock_hasTileEntity;
   public static ReflectorMethod ForgeBlock_canCreatureSpawn;
   public static ReflectorClass ForgeEntity;
   public static ReflectorField ForgeEntity_captureDrops;
   public static ReflectorField ForgeEntity_capturedDrops;
   public static ReflectorMethod ForgeEntity_shouldRenderInPass;
   public static ReflectorMethod ForgeEntity_canRiderInteract;
   public static ReflectorClass ForgeTileEntity;
   public static ReflectorMethod ForgeTileEntity_shouldRenderInPass;
   public static ReflectorMethod ForgeTileEntity_getRenderBoundingBox;
   public static ReflectorClass ForgeItem;
   public static ReflectorMethod ForgeItem_onEntitySwing;
   public static ReflectorClass ForgePotionEffect;
   public static ReflectorMethod ForgePotionEffect_isCurativeItem;
   public static ReflectorClass ForgeItemStack;
   public static ReflectorMethod ForgeItemStack_hasEffect;
   public static ReflectorClass ForgeItemRecord;
   public static ReflectorMethod ForgeItemRecord_getRecordResource;

   static {
      ModLoader_renderWorldBlock = new ReflectorMethod(ModLoader, "renderWorldBlock");
      ModLoader_renderInvBlock = new ReflectorMethod(ModLoader, "renderInvBlock");
      ModLoader_renderBlockIsItemFull3D = new ReflectorMethod(ModLoader, "renderBlockIsItemFull3D");
      ModLoader_registerServer = new ReflectorMethod(ModLoader, "registerServer");
      ModLoader_getCustomAnimationLogic = new ReflectorMethod(ModLoader, "getCustomAnimationLogic");
      FMLRenderAccessLibrary = new ReflectorClass("net.minecraft.src.FMLRenderAccessLibrary");
      FMLRenderAccessLibrary_renderWorldBlock = new ReflectorMethod(FMLRenderAccessLibrary, "renderWorldBlock");
      FMLRenderAccessLibrary_renderInventoryBlock = new ReflectorMethod(FMLRenderAccessLibrary, "renderInventoryBlock");
      FMLRenderAccessLibrary_renderItemAsFull3DBlock = new ReflectorMethod(FMLRenderAccessLibrary, "renderItemAsFull3DBlock");
      LightCache = new ReflectorClass("LightCache");
      LightCache_cache = new ReflectorField(LightCache, "cache");
      LightCache_clear = new ReflectorMethod(LightCache, "clear");
      BlockCoord = new ReflectorClass("BlockCoord");
      BlockCoord_resetPool = new ReflectorMethod(BlockCoord, "resetPool");
      MinecraftForge = new ReflectorClass("net.minecraftforge.common.MinecraftForge");
      MinecraftForge_EVENT_BUS = new ReflectorField(MinecraftForge, "EVENT_BUS");
      ForgeHooks = new ReflectorClass("net.minecraftforge.common.ForgeHooks");
      ForgeHooks_onLivingSetAttackTarget = new ReflectorMethod(ForgeHooks, "onLivingSetAttackTarget");
      ForgeHooks_onLivingUpdate = new ReflectorMethod(ForgeHooks, "onLivingUpdate");
      ForgeHooks_onLivingAttack = new ReflectorMethod(ForgeHooks, "onLivingAttack");
      ForgeHooks_onLivingHurt = new ReflectorMethod(ForgeHooks, "onLivingHurt");
      ForgeHooks_onLivingDeath = new ReflectorMethod(ForgeHooks, "onLivingDeath");
      ForgeHooks_onLivingDrops = new ReflectorMethod(ForgeHooks, "onLivingDrops");
      ForgeHooks_onLivingFall = new ReflectorMethod(ForgeHooks, "onLivingFall");
      ForgeHooks_onLivingJump = new ReflectorMethod(ForgeHooks, "onLivingJump");
      MinecraftForgeClient = new ReflectorClass("net.minecraftforge.client.MinecraftForgeClient");
      MinecraftForgeClient_getRenderPass = new ReflectorMethod(MinecraftForgeClient, "getRenderPass");
      MinecraftForgeClient_getItemRenderer = new ReflectorMethod(MinecraftForgeClient, "getItemRenderer");
      ForgeHooksClient = new ReflectorClass("net.minecraftforge.client.ForgeHooksClient");
      ForgeHooksClient_onDrawBlockHighlight = new ReflectorMethod(ForgeHooksClient, "onDrawBlockHighlight");
      ForgeHooksClient_orientBedCamera = new ReflectorMethod(ForgeHooksClient, "orientBedCamera");
      ForgeHooksClient_renderEquippedItem = new ReflectorMethod(ForgeHooksClient, "renderEquippedItem");
      ForgeHooksClient_dispatchRenderLast = new ReflectorMethod(ForgeHooksClient, "dispatchRenderLast");
      ForgeHooksClient_onTextureLoadPre = new ReflectorMethod(ForgeHooksClient, "onTextureLoadPre");
      ForgeHooksClient_setRenderPass = new ReflectorMethod(ForgeHooksClient, "setRenderPass");
      ForgeHooksClient_onTextureStitchedPre = new ReflectorMethod(ForgeHooksClient, "onTextureStitchedPre");
      ForgeHooksClient_onTextureStitchedPost = new ReflectorMethod(ForgeHooksClient, "onTextureStitchedPost");
      ForgeHooksClient_renderFirstPersonHand = new ReflectorMethod(ForgeHooksClient, "renderFirstPersonHand");
      ForgeHooksClient_setWorldRendererRB = new ReflectorMethod(ForgeHooksClient, "setWorldRendererRB");
      ForgeHooksClient_onPreRenderWorld = new ReflectorMethod(ForgeHooksClient, "onPreRenderWorld");
      ForgeHooksClient_onPostRenderWorld = new ReflectorMethod(ForgeHooksClient, "onPostRenderWorld");
      FMLCommonHandler = new ReflectorClass("net.minecraftforge.fml.common.FMLCommonHandler");
      FMLCommonHandler_instance = new ReflectorMethod(FMLCommonHandler, "instance");
      FMLCommonHandler_handleServerStarting = new ReflectorMethod(FMLCommonHandler, "handleServerStarting");
      FMLCommonHandler_handleServerAboutToStart = new ReflectorMethod(FMLCommonHandler, "handleServerAboutToStart");
      FMLCommonHandler_enhanceCrashReport = new ReflectorMethod(FMLCommonHandler, "enhanceCrashReport");
      FMLClientHandler = new ReflectorClass("net.minecraftforge.fml.client.FMLClientHandler");
      FMLClientHandler_instance = new ReflectorMethod(FMLClientHandler, "instance");
      FMLClientHandler_isLoading = new ReflectorMethod(FMLClientHandler, "isLoading");
      ItemRenderType = new ReflectorClass("net.minecraftforge.client.IItemRenderer$ItemRenderType");
      ItemRenderType_EQUIPPED = new ReflectorField(ItemRenderType, "EQUIPPED");
      ForgeWorldProvider = new ReflectorClass(WorldProvider.class);
      ForgeWorldProvider_getSkyRenderer = new ReflectorMethod(ForgeWorldProvider, "getSkyRenderer");
      ForgeWorldProvider_getCloudRenderer = new ReflectorMethod(ForgeWorldProvider, "getCloudRenderer");
      ForgeWorldProvider_getWeatherRenderer = new ReflectorMethod(ForgeWorldProvider, "getWeatherRenderer");
      ForgeWorld = new ReflectorClass(World.class);
      ForgeWorld_countEntities = new ReflectorMethod(ForgeWorld, "countEntities", new Class[]{EnumCreatureType.class, Boolean.TYPE});
      IRenderHandler = new ReflectorClass("net.minecraftforge.client.IRenderHandler");
      IRenderHandler_render = new ReflectorMethod(IRenderHandler, "render");
      DimensionManager = new ReflectorClass("net.minecraftforge.common.DimensionManager");
      DimensionManager_getStaticDimensionIDs = new ReflectorMethod(DimensionManager, "getStaticDimensionIDs");
      WorldEvent_Load = new ReflectorClass("net.minecraftforge.event.world.WorldEvent$Load");
      WorldEvent_Load_Constructor = new ReflectorConstructor(WorldEvent_Load, new Class[]{World.class});
      DrawScreenEvent_Pre = new ReflectorClass("net.minecraftforge.client.event.GuiScreenEvent$DrawScreenEvent$Pre");
      DrawScreenEvent_Pre_Constructor = new ReflectorConstructor(DrawScreenEvent_Pre, new Class[]{GuiScreen.class, Integer.TYPE, Integer.TYPE, Float.TYPE});
      DrawScreenEvent_Post = new ReflectorClass("net.minecraftforge.client.event.GuiScreenEvent$DrawScreenEvent$Post");
      DrawScreenEvent_Post_Constructor = new ReflectorConstructor(DrawScreenEvent_Post, new Class[]{GuiScreen.class, Integer.TYPE, Integer.TYPE, Float.TYPE});
      EntityViewRenderEvent_FogColors = new ReflectorClass("net.minecraftforge.client.event.EntityViewRenderEvent$FogColors");
      EntityViewRenderEvent_FogColors_Constructor = new ReflectorConstructor(EntityViewRenderEvent_FogColors, new Class[]{EntityRenderer.class, EntityLivingBase.class, Block.class, Double.TYPE, Float.TYPE, Float.TYPE, Float.TYPE});
      EntityViewRenderEvent_FogColors_red = new ReflectorField(EntityViewRenderEvent_FogColors, "red");
      EntityViewRenderEvent_FogColors_green = new ReflectorField(EntityViewRenderEvent_FogColors, "green");
      EntityViewRenderEvent_FogColors_blue = new ReflectorField(EntityViewRenderEvent_FogColors, "blue");
      EntityViewRenderEvent_FogDensity = new ReflectorClass("net.minecraftforge.client.event.EntityViewRenderEvent$FogDensity");
      EntityViewRenderEvent_FogDensity_Constructor = new ReflectorConstructor(EntityViewRenderEvent_FogDensity, new Class[]{EntityRenderer.class, EntityLivingBase.class, Block.class, Double.TYPE, Float.TYPE});
      EntityViewRenderEvent_FogDensity_density = new ReflectorField(EntityViewRenderEvent_FogDensity, "density");
      EntityViewRenderEvent_RenderFogEvent = new ReflectorClass("net.minecraftforge.client.event.EntityViewRenderEvent$RenderFogEvent");
      EntityViewRenderEvent_RenderFogEvent_Constructor = new ReflectorConstructor(EntityViewRenderEvent_RenderFogEvent, new Class[]{EntityRenderer.class, EntityLivingBase.class, Block.class, Double.TYPE, Integer.TYPE, Float.TYPE});
      EventBus = new ReflectorClass("net.minecraftforge.fml.common.eventhandler.EventBus");
      EventBus_post = new ReflectorMethod(EventBus, "post");
      Event_Result = new ReflectorClass("net.minecraftforge.fml.common.eventhandler.Event$Result");
      Event_Result_DENY = new ReflectorField(Event_Result, "DENY");
      Event_Result_ALLOW = new ReflectorField(Event_Result, "ALLOW");
      Event_Result_DEFAULT = new ReflectorField(Event_Result, "DEFAULT");
      ForgeEventFactory = new ReflectorClass("net.minecraftforge.event.ForgeEventFactory");
      ForgeEventFactory_canEntitySpawn = new ReflectorMethod(ForgeEventFactory, "canEntitySpawn");
      ForgeEventFactory_canEntityDespawn = new ReflectorMethod(ForgeEventFactory, "canEntityDespawn");
      ChunkWatchEvent_UnWatch = new ReflectorClass("net.minecraftforge.event.world.ChunkWatchEvent$UnWatch");
      ChunkWatchEvent_UnWatch_Constructor = new ReflectorConstructor(ChunkWatchEvent_UnWatch, new Class[]{ChunkCoordIntPair.class, EntityPlayerMP.class});
      ForgeBlock = new ReflectorClass(Block.class);
      ForgeBlock_getBedDirection = new ReflectorMethod(ForgeBlock, "getBedDirection");
      ForgeBlock_isBedFoot = new ReflectorMethod(ForgeBlock, "isBedFoot");
      ForgeBlock_canRenderInPass = new ReflectorMethod(ForgeBlock, "canRenderInPass");
      ForgeBlock_hasTileEntity = new ReflectorMethod(ForgeBlock, "hasTileEntity", new Class[]{Integer.TYPE});
      ForgeBlock_canCreatureSpawn = new ReflectorMethod(ForgeBlock, "canCreatureSpawn");
      ForgeEntity = new ReflectorClass(Entity.class);
      ForgeEntity_captureDrops = new ReflectorField(ForgeEntity, "captureDrops");
      ForgeEntity_capturedDrops = new ReflectorField(ForgeEntity, "capturedDrops");
      ForgeEntity_shouldRenderInPass = new ReflectorMethod(ForgeEntity, "shouldRenderInPass");
      ForgeEntity_canRiderInteract = new ReflectorMethod(ForgeEntity, "canRiderInteract");
      ForgeTileEntity = new ReflectorClass(TileEntity.class);
      ForgeTileEntity_shouldRenderInPass = new ReflectorMethod(ForgeTileEntity, "shouldRenderInPass");
      ForgeTileEntity_getRenderBoundingBox = new ReflectorMethod(ForgeTileEntity, "getRenderBoundingBox");
      ForgeItem = new ReflectorClass(Item.class);
      ForgeItem_onEntitySwing = new ReflectorMethod(ForgeItem, "onEntitySwing");
      ForgePotionEffect = new ReflectorClass(PotionEffect.class);
      ForgePotionEffect_isCurativeItem = new ReflectorMethod(ForgePotionEffect, "isCurativeItem");
      ForgeItemStack = new ReflectorClass(ItemStack.class);
      ForgeItemStack_hasEffect = new ReflectorMethod(ForgeItemStack, "hasEffect", new Class[]{Integer.TYPE});
      ForgeItemRecord = new ReflectorClass(ItemRecord.class);
      ForgeItemRecord_getRecordResource = new ReflectorMethod(ForgeItemRecord, "getRecordResource", new Class[]{String.class});
   }

   public static void callVoid(ReflectorMethod refMethod, Object... params) {
      try {
         Method e = refMethod.getTargetMethod();
         if (e == null) {
            return;
         }

         e.invoke((Object)null, params);
      } catch (Throwable var3) {
         handleException(var3, (Object)null, refMethod, params);
      }

   }

   public static boolean callBoolean(ReflectorMethod refMethod, Object... params) {
      try {
         Method e = refMethod.getTargetMethod();
         if (e == null) {
            return false;
         } else {
            Boolean retVal = (Boolean)e.invoke((Object)null, params);
            return retVal;
         }
      } catch (Throwable var4) {
         handleException(var4, (Object)null, refMethod, params);
         return false;
      }
   }

   public static int callInt(ReflectorMethod refMethod, Object... params) {
      try {
         Method e = refMethod.getTargetMethod();
         if (e == null) {
            return 0;
         } else {
            Integer retVal = (Integer)e.invoke((Object)null, params);
            return retVal;
         }
      } catch (Throwable var4) {
         handleException(var4, (Object)null, refMethod, params);
         return 0;
      }
   }

   public static float callFloat(ReflectorMethod refMethod, Object... params) {
      try {
         Method e = refMethod.getTargetMethod();
         if (e == null) {
            return 0.0F;
         } else {
            Float retVal = (Float)e.invoke((Object)null, params);
            return retVal;
         }
      } catch (Throwable var4) {
         handleException(var4, (Object)null, refMethod, params);
         return 0.0F;
      }
   }

   public static String callString(ReflectorMethod refMethod, Object... params) {
      try {
         Method e = refMethod.getTargetMethod();
         if (e == null) {
            return null;
         } else {
            String retVal = (String)e.invoke((Object)null, params);
            return retVal;
         }
      } catch (Throwable var4) {
         handleException(var4, (Object)null, refMethod, params);
         return null;
      }
   }

   public static Object call(ReflectorMethod refMethod, Object... params) {
      try {
         Method e = refMethod.getTargetMethod();
         if (e == null) {
            return null;
         } else {
            Object retVal = e.invoke((Object)null, params);
            return retVal;
         }
      } catch (Throwable var4) {
         handleException(var4, (Object)null, refMethod, params);
         return null;
      }
   }

   public static void callVoid(Object obj, ReflectorMethod refMethod, Object... params) {
      try {
         if (obj == null) {
            return;
         }

         Method e = refMethod.getTargetMethod();
         if (e == null) {
            return;
         }

         e.invoke(obj, params);
      } catch (Throwable var4) {
         handleException(var4, obj, refMethod, params);
      }

   }

   public static boolean callBoolean(Object obj, ReflectorMethod refMethod, Object... params) {
      try {
         Method e = refMethod.getTargetMethod();
         if (e == null) {
            return false;
         } else {
            Boolean retVal = (Boolean)e.invoke(obj, params);
            return retVal;
         }
      } catch (Throwable var5) {
         handleException(var5, obj, refMethod, params);
         return false;
      }
   }

   public static int callInt(Object obj, ReflectorMethod refMethod, Object... params) {
      try {
         Method e = refMethod.getTargetMethod();
         if (e == null) {
            return 0;
         } else {
            Integer retVal = (Integer)e.invoke(obj, params);
            return retVal;
         }
      } catch (Throwable var5) {
         handleException(var5, obj, refMethod, params);
         return 0;
      }
   }

   public static float callFloat(Object obj, ReflectorMethod refMethod, Object... params) {
      try {
         Method e = refMethod.getTargetMethod();
         if (e == null) {
            return 0.0F;
         } else {
            Float retVal = (Float)e.invoke(obj, params);
            return retVal;
         }
      } catch (Throwable var5) {
         handleException(var5, obj, refMethod, params);
         return 0.0F;
      }
   }

   public static String callString(Object obj, ReflectorMethod refMethod, Object... params) {
      try {
         Method e = refMethod.getTargetMethod();
         if (e == null) {
            return null;
         } else {
            String retVal = (String)e.invoke(obj, params);
            return retVal;
         }
      } catch (Throwable var5) {
         handleException(var5, obj, refMethod, params);
         return null;
      }
   }

   public static Object call(Object obj, ReflectorMethod refMethod, Object... params) {
      try {
         Method e = refMethod.getTargetMethod();
         if (e == null) {
            return null;
         } else {
            Object retVal = e.invoke(obj, params);
            return retVal;
         }
      } catch (Throwable var5) {
         handleException(var5, obj, refMethod, params);
         return null;
      }
   }

   public static Object getFieldValue(ReflectorField refField) {
      return getFieldValue((Object)null, refField);
   }

   public static Object getFieldValue(Object obj, ReflectorField refField) {
      try {
         Field e = refField.getTargetField();
         if (e == null) {
            return null;
         } else {
            Object value = e.get(obj);
            return value;
         }
      } catch (Throwable var4) {
         var4.printStackTrace();
         return null;
      }
   }

   public static float getFieldValueFloat(Object obj, ReflectorField refField, float def) {
      Object val = getFieldValue(obj, refField);
      if (!(val instanceof Float)) {
         return def;
      } else {
         Float valFloat = (Float)val;
         return valFloat;
      }
   }

   public static void setFieldValue(ReflectorField refField, Object value) {
      setFieldValue((Object)null, refField, value);
   }

   public static void setFieldValue(Object obj, ReflectorField refField, Object value) {
      try {
         Field e = refField.getTargetField();
         if (e == null) {
            return;
         }

         e.set(obj, value);
      } catch (Throwable var4) {
         var4.printStackTrace();
      }

   }

   public static boolean postForgeBusEvent(ReflectorConstructor constr, Object... params) {
      Object event = newInstance(constr, params);
      return event == null ? false : postForgeBusEvent(event);
   }

   public static boolean postForgeBusEvent(Object event) {
      if (event == null) {
         return false;
      } else {
         Object eventBus = getFieldValue(MinecraftForge_EVENT_BUS);
         if (eventBus == null) {
            return false;
         } else {
            Object ret = call(eventBus, EventBus_post, event);
            if (!(ret instanceof Boolean)) {
               return false;
            } else {
               Boolean retBool = (Boolean)ret;
               return retBool;
            }
         }
      }
   }

   public static Object newInstance(ReflectorConstructor constr, Object... params) {
      Constructor c = constr.getTargetConstructor();
      if (c == null) {
         return null;
      } else {
         try {
            Object e = c.newInstance(params);
            return e;
         } catch (Throwable var4) {
            handleException(var4, constr, params);
            return null;
         }
      }
   }

   public static boolean matchesTypes(Class[] pTypes, Class[] cTypes) {
      if (pTypes.length != cTypes.length) {
         return false;
      } else {
         for(int i = 0; i < cTypes.length; ++i) {
            Class pType = pTypes[i];
            Class cType = cTypes[i];
            if (pType != cType) {
               return false;
            }
         }

         return true;
      }
   }

   private static void dbgCall(boolean isStatic, String callType, ReflectorMethod refMethod, Object[] params, Object retVal) {
      String className = refMethod.getTargetMethod().getDeclaringClass().getName();
      String methodName = refMethod.getTargetMethod().getName();
      String staticStr = "";
      if (isStatic) {
         staticStr = " static";
      }

      Config.dbg(callType + staticStr + " " + className + "." + methodName + "(" + Config.arrayToString(params) + ") => " + retVal);
   }

   private static void dbgCallVoid(boolean isStatic, String callType, ReflectorMethod refMethod, Object[] params) {
      String className = refMethod.getTargetMethod().getDeclaringClass().getName();
      String methodName = refMethod.getTargetMethod().getName();
      String staticStr = "";
      if (isStatic) {
         staticStr = " static";
      }

      Config.dbg(callType + staticStr + " " + className + "." + methodName + "(" + Config.arrayToString(params) + ")");
   }

   private static void dbgFieldValue(boolean isStatic, String accessType, ReflectorField refField, Object val) {
      String className = refField.getTargetField().getDeclaringClass().getName();
      String fieldName = refField.getTargetField().getName();
      String staticStr = "";
      if (isStatic) {
         staticStr = " static";
      }

      Config.dbg(accessType + staticStr + " " + className + "." + fieldName + " => " + val);
   }

   private static void handleException(Throwable e, Object obj, ReflectorMethod refMethod, Object[] params) {
      if (e instanceof InvocationTargetException) {
         e.printStackTrace();
      } else {
         if (e instanceof IllegalArgumentException) {
            Config.warn("*** IllegalArgumentException ***");
            Config.warn("Method: " + refMethod.getTargetMethod());
            Config.warn("Object: " + obj);
            Config.warn("Parameter classes: " + Config.arrayToString(getClasses(params)));
            Config.warn("Parameters: " + Config.arrayToString(params));
         }

         Config.warn("*** Exception outside of method ***");
         Config.warn("Method deactivated: " + refMethod.getTargetMethod());
         refMethod.deactivate();
         e.printStackTrace();
      }

   }

   private static void handleException(Throwable e, ReflectorConstructor refConstr, Object[] params) {
      if (e instanceof InvocationTargetException) {
         e.printStackTrace();
      } else {
         if (e instanceof IllegalArgumentException) {
            Config.warn("*** IllegalArgumentException ***");
            Config.warn("Constructor: " + refConstr.getTargetConstructor());
            Config.warn("Parameter classes: " + Config.arrayToString(getClasses(params)));
            Config.warn("Parameters: " + Config.arrayToString(params));
         }

         Config.warn("*** Exception outside of constructor ***");
         Config.warn("Constructor deactivated: " + refConstr.getTargetConstructor());
         refConstr.deactivate();
         e.printStackTrace();
      }

   }

   private static Object[] getClasses(Object[] objs) {
      if (objs == null) {
         return new Class[0];
      } else {
         Class[] classes = new Class[objs.length];

         for(int i = 0; i < classes.length; ++i) {
            Object obj = objs[i];
            if (obj != null) {
               classes[i] = obj.getClass();
            }
         }

         return classes;
      }
   }

   public static Field getField(Class cls, Class fieldType) {
      try {
         Field[] e = cls.getDeclaredFields();

         for(int i = 0; i < e.length; ++i) {
            Field field = e[i];
            if (field.getType() == fieldType) {
               field.setAccessible(true);
               return field;
            }
         }

         return null;
      } catch (Exception var5) {
         return null;
      }
   }

   public static Field[] getFields(Class cls, Class fieldType) {
      ArrayList list = new ArrayList();

      try {
         Field[] e = cls.getDeclaredFields();

         for(int fields = 0; fields < e.length; ++fields) {
            Field field = e[fields];
            if (field.getType() == fieldType) {
               field.setAccessible(true);
               list.add(field);
            }
         }

         Field[] var7 = (Field[])list.toArray(new Field[list.size()]);
         return var7;
      } catch (Exception var6) {
         return null;
      }
   }
}
