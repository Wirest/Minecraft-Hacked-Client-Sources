package optfine;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
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
   public static ReflectorMethod ForgeHooksClient_dispatchRenderLast;
   public static ReflectorMethod ForgeHooksClient_setRenderPass;
   public static ReflectorMethod ForgeHooksClient_onTextureStitchedPre;
   public static ReflectorMethod ForgeHooksClient_onTextureStitchedPost;
   public static ReflectorMethod ForgeHooksClient_renderFirstPersonHand;
   public static ReflectorMethod ForgeHooksClient_getOffsetFOV;
   public static ReflectorMethod ForgeHooksClient_drawScreen;
   public static ReflectorMethod ForgeHooksClient_onFogRender;
   public static ReflectorMethod ForgeHooksClient_setRenderLayer;
   public static ReflectorClass FMLCommonHandler;
   public static ReflectorMethod FMLCommonHandler_instance;
   public static ReflectorMethod FMLCommonHandler_handleServerStarting;
   public static ReflectorMethod FMLCommonHandler_handleServerAboutToStart;
   public static ReflectorMethod FMLCommonHandler_enhanceCrashReport;
   public static ReflectorMethod FMLCommonHandler_getBrandings;
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
   public static ReflectorMethod ForgeWorld_getPerWorldStorage;
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
   public static ReflectorMethod ForgeBlock_hasTileEntity;
   public static ReflectorMethod ForgeBlock_canCreatureSpawn;
   public static ReflectorMethod ForgeBlock_addHitEffects;
   public static ReflectorMethod ForgeBlock_addDestroyEffects;
   public static ReflectorMethod ForgeBlock_isAir;
   public static ReflectorMethod ForgeBlock_canRenderInLayer;
   public static ReflectorClass ForgeEntity;
   public static ReflectorField ForgeEntity_captureDrops;
   public static ReflectorField ForgeEntity_capturedDrops;
   public static ReflectorMethod ForgeEntity_shouldRenderInPass;
   public static ReflectorMethod ForgeEntity_canRiderInteract;
   public static ReflectorClass ForgeTileEntity;
   public static ReflectorMethod ForgeTileEntity_shouldRenderInPass;
   public static ReflectorMethod ForgeTileEntity_getRenderBoundingBox;
   public static ReflectorMethod ForgeTileEntity_canRenderBreaking;
   public static ReflectorClass ForgeItem;
   public static ReflectorMethod ForgeItem_onEntitySwing;
   public static ReflectorClass ForgePotionEffect;
   public static ReflectorMethod ForgePotionEffect_isCurativeItem;
   public static ReflectorClass ForgeItemRecord;
   public static ReflectorMethod ForgeItemRecord_getRecordResource;
   public static ReflectorClass ForgeVertexFormatElementEnumUseage;
   public static ReflectorMethod ForgeVertexFormatElementEnumUseage_preDraw;
   public static ReflectorMethod ForgeVertexFormatElementEnumUseage_postDraw;

   public static void callVoid(ReflectorMethod p_callVoid_0_, Object... p_callVoid_1_) {
      try {
         Method method = p_callVoid_0_.getTargetMethod();
         if (method == null) {
            return;
         }

         method.invoke((Object)null, p_callVoid_1_);
      } catch (Throwable var3) {
         handleException(var3, (Object)null, p_callVoid_0_, p_callVoid_1_);
      }

   }

   public static boolean callBoolean(ReflectorMethod p_callBoolean_0_, Object... p_callBoolean_1_) {
      try {
         Method method = p_callBoolean_0_.getTargetMethod();
         if (method == null) {
            return false;
         } else {
            Boolean obool = (Boolean)method.invoke((Object)null, p_callBoolean_1_);
            return obool;
         }
      } catch (Throwable var4) {
         handleException(var4, (Object)null, p_callBoolean_0_, p_callBoolean_1_);
         return false;
      }
   }

   public static int callInt(ReflectorMethod p_callInt_0_, Object... p_callInt_1_) {
      try {
         Method method = p_callInt_0_.getTargetMethod();
         if (method == null) {
            return 0;
         } else {
            Integer integer = (Integer)method.invoke((Object)null, p_callInt_1_);
            return integer;
         }
      } catch (Throwable var4) {
         handleException(var4, (Object)null, p_callInt_0_, p_callInt_1_);
         return 0;
      }
   }

   public static float callFloat(ReflectorMethod p_callFloat_0_, Object... p_callFloat_1_) {
      try {
         Method method = p_callFloat_0_.getTargetMethod();
         if (method == null) {
            return 0.0F;
         } else {
            Float f = (Float)method.invoke((Object)null, p_callFloat_1_);
            return f;
         }
      } catch (Throwable var4) {
         handleException(var4, (Object)null, p_callFloat_0_, p_callFloat_1_);
         return 0.0F;
      }
   }

   public static String callString(ReflectorMethod p_callString_0_, Object... p_callString_1_) {
      try {
         Method method = p_callString_0_.getTargetMethod();
         if (method == null) {
            return null;
         } else {
            String s = (String)method.invoke((Object)null, p_callString_1_);
            return s;
         }
      } catch (Throwable var4) {
         handleException(var4, (Object)null, p_callString_0_, p_callString_1_);
         return null;
      }
   }

   public static Object call(ReflectorMethod p_call_0_, Object... p_call_1_) {
      try {
         Method method = p_call_0_.getTargetMethod();
         if (method == null) {
            return null;
         } else {
            Object object = method.invoke((Object)null, p_call_1_);
            return object;
         }
      } catch (Throwable var4) {
         handleException(var4, (Object)null, p_call_0_, p_call_1_);
         return null;
      }
   }

   public static void callVoid(Object p_callVoid_0_, ReflectorMethod p_callVoid_1_, Object... p_callVoid_2_) {
      try {
         if (p_callVoid_0_ == null) {
            return;
         }

         Method method = p_callVoid_1_.getTargetMethod();
         if (method == null) {
            return;
         }

         method.invoke(p_callVoid_0_, p_callVoid_2_);
      } catch (Throwable var4) {
         handleException(var4, p_callVoid_0_, p_callVoid_1_, p_callVoid_2_);
      }

   }

   public static boolean callBoolean(Object p_callBoolean_0_, ReflectorMethod p_callBoolean_1_, Object... p_callBoolean_2_) {
      try {
         Method method = p_callBoolean_1_.getTargetMethod();
         if (method == null) {
            return false;
         } else {
            Boolean obool = (Boolean)method.invoke(p_callBoolean_0_, p_callBoolean_2_);
            return obool;
         }
      } catch (Throwable var5) {
         handleException(var5, p_callBoolean_0_, p_callBoolean_1_, p_callBoolean_2_);
         return false;
      }
   }

   public static int callInt(Object p_callInt_0_, ReflectorMethod p_callInt_1_, Object... p_callInt_2_) {
      try {
         Method method = p_callInt_1_.getTargetMethod();
         if (method == null) {
            return 0;
         } else {
            Integer integer = (Integer)method.invoke(p_callInt_0_, p_callInt_2_);
            return integer;
         }
      } catch (Throwable var5) {
         handleException(var5, p_callInt_0_, p_callInt_1_, p_callInt_2_);
         return 0;
      }
   }

   public static float callFloat(Object p_callFloat_0_, ReflectorMethod p_callFloat_1_, Object... p_callFloat_2_) {
      try {
         Method method = p_callFloat_1_.getTargetMethod();
         if (method == null) {
            return 0.0F;
         } else {
            Float f = (Float)method.invoke(p_callFloat_0_, p_callFloat_2_);
            return f;
         }
      } catch (Throwable var5) {
         handleException(var5, p_callFloat_0_, p_callFloat_1_, p_callFloat_2_);
         return 0.0F;
      }
   }

   public static String callString(Object p_callString_0_, ReflectorMethod p_callString_1_, Object... p_callString_2_) {
      try {
         Method method = p_callString_1_.getTargetMethod();
         if (method == null) {
            return null;
         } else {
            String s = (String)method.invoke(p_callString_0_, p_callString_2_);
            return s;
         }
      } catch (Throwable var5) {
         handleException(var5, p_callString_0_, p_callString_1_, p_callString_2_);
         return null;
      }
   }

   public static Object call(Object p_call_0_, ReflectorMethod p_call_1_, Object... p_call_2_) {
      try {
         Method method = p_call_1_.getTargetMethod();
         if (method == null) {
            return null;
         } else {
            Object object = method.invoke(p_call_0_, p_call_2_);
            return object;
         }
      } catch (Throwable var5) {
         handleException(var5, p_call_0_, p_call_1_, p_call_2_);
         return null;
      }
   }

   public static Object getFieldValue(ReflectorField p_getFieldValue_0_) {
      return getFieldValue((Object)null, p_getFieldValue_0_);
   }

   public static Object getFieldValue(Object p_getFieldValue_0_, ReflectorField p_getFieldValue_1_) {
      try {
         Field field = p_getFieldValue_1_.getTargetField();
         if (field == null) {
            return null;
         } else {
            Object object = field.get(p_getFieldValue_0_);
            return object;
         }
      } catch (Throwable var4) {
         var4.printStackTrace();
         return null;
      }
   }

   public static float getFieldValueFloat(Object p_getFieldValueFloat_0_, ReflectorField p_getFieldValueFloat_1_, float p_getFieldValueFloat_2_) {
      Object object = getFieldValue(p_getFieldValueFloat_0_, p_getFieldValueFloat_1_);
      if (!(object instanceof Float)) {
         return p_getFieldValueFloat_2_;
      } else {
         Float f = (Float)object;
         return f;
      }
   }

   public static void setFieldValue(ReflectorField p_setFieldValue_0_, Object p_setFieldValue_1_) {
      setFieldValue((Object)null, p_setFieldValue_0_, p_setFieldValue_1_);
   }

   public static void setFieldValue(Object p_setFieldValue_0_, ReflectorField p_setFieldValue_1_, Object p_setFieldValue_2_) {
      try {
         Field field = p_setFieldValue_1_.getTargetField();
         if (field == null) {
            return;
         }

         field.set(p_setFieldValue_0_, p_setFieldValue_2_);
      } catch (Throwable var4) {
         var4.printStackTrace();
      }

   }

   public static boolean postForgeBusEvent(ReflectorConstructor p_postForgeBusEvent_0_, Object... p_postForgeBusEvent_1_) {
      Object object = newInstance(p_postForgeBusEvent_0_, p_postForgeBusEvent_1_);
      return object == null ? false : postForgeBusEvent(object);
   }

   public static boolean postForgeBusEvent(Object p_postForgeBusEvent_0_) {
      if (p_postForgeBusEvent_0_ == null) {
         return false;
      } else {
         Object object = getFieldValue(MinecraftForge_EVENT_BUS);
         if (object == null) {
            return false;
         } else {
            Object object1 = call(object, EventBus_post, p_postForgeBusEvent_0_);
            if (!(object1 instanceof Boolean)) {
               return false;
            } else {
               Boolean obool = (Boolean)object1;
               return obool;
            }
         }
      }
   }

   public static Object newInstance(ReflectorConstructor p_newInstance_0_, Object... p_newInstance_1_) {
      Constructor constructor = p_newInstance_0_.getTargetConstructor();
      if (constructor == null) {
         return null;
      } else {
         try {
            Object object = constructor.newInstance(p_newInstance_1_);
            return object;
         } catch (Throwable var4) {
            handleException(var4, p_newInstance_0_, p_newInstance_1_);
            return null;
         }
      }
   }

   public static boolean matchesTypes(Class[] p_matchesTypes_0_, Class[] p_matchesTypes_1_) {
      if (p_matchesTypes_0_.length != p_matchesTypes_1_.length) {
         return false;
      } else {
         for(int i = 0; i < p_matchesTypes_1_.length; ++i) {
            Class oclass = p_matchesTypes_0_[i];
            Class oclass1 = p_matchesTypes_1_[i];
            if (oclass != oclass1) {
               return false;
            }
         }

         return true;
      }
   }

   private static void dbgCall(boolean p_dbgCall_0_, String p_dbgCall_1_, ReflectorMethod p_dbgCall_2_, Object[] p_dbgCall_3_, Object p_dbgCall_4_) {
      String s = p_dbgCall_2_.getTargetMethod().getDeclaringClass().getName();
      String s1 = p_dbgCall_2_.getTargetMethod().getName();
      String s2 = "";
      if (p_dbgCall_0_) {
         s2 = " static";
      }

      Config.dbg(p_dbgCall_1_ + s2 + " " + s + "." + s1 + "(" + Config.arrayToString(p_dbgCall_3_) + ") => " + p_dbgCall_4_);
   }

   private static void dbgCallVoid(boolean p_dbgCallVoid_0_, String p_dbgCallVoid_1_, ReflectorMethod p_dbgCallVoid_2_, Object[] p_dbgCallVoid_3_) {
      String s = p_dbgCallVoid_2_.getTargetMethod().getDeclaringClass().getName();
      String s1 = p_dbgCallVoid_2_.getTargetMethod().getName();
      String s2 = "";
      if (p_dbgCallVoid_0_) {
         s2 = " static";
      }

      Config.dbg(p_dbgCallVoid_1_ + s2 + " " + s + "." + s1 + "(" + Config.arrayToString(p_dbgCallVoid_3_) + ")");
   }

   private static void dbgFieldValue(boolean p_dbgFieldValue_0_, String p_dbgFieldValue_1_, ReflectorField p_dbgFieldValue_2_, Object p_dbgFieldValue_3_) {
      String s = p_dbgFieldValue_2_.getTargetField().getDeclaringClass().getName();
      String s1 = p_dbgFieldValue_2_.getTargetField().getName();
      String s2 = "";
      if (p_dbgFieldValue_0_) {
         s2 = " static";
      }

      Config.dbg(p_dbgFieldValue_1_ + s2 + " " + s + "." + s1 + " => " + p_dbgFieldValue_3_);
   }

   private static void handleException(Throwable p_handleException_0_, Object p_handleException_1_, ReflectorMethod p_handleException_2_, Object[] p_handleException_3_) {
      if (p_handleException_0_ instanceof InvocationTargetException) {
         p_handleException_0_.printStackTrace();
      } else {
         if (p_handleException_0_ instanceof IllegalArgumentException) {
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

   private static void handleException(Throwable p_handleException_0_, ReflectorConstructor p_handleException_1_, Object[] p_handleException_2_) {
      if (p_handleException_0_ instanceof InvocationTargetException) {
         p_handleException_0_.printStackTrace();
      } else {
         if (p_handleException_0_ instanceof IllegalArgumentException) {
            Config.warn("*** IllegalArgumentException ***");
            Config.warn("Constructor: " + p_handleException_1_.getTargetConstructor());
            Config.warn("Parameter classes: " + Config.arrayToString(getClasses(p_handleException_2_)));
            Config.warn("Parameters: " + Config.arrayToString(p_handleException_2_));
         }

         Config.warn("*** Exception outside of constructor ***");
         Config.warn("Constructor deactivated: " + p_handleException_1_.getTargetConstructor());
         p_handleException_1_.deactivate();
         p_handleException_0_.printStackTrace();
      }

   }

   private static Object[] getClasses(Object[] p_getClasses_0_) {
      if (p_getClasses_0_ == null) {
         return new Class[0];
      } else {
         Class[] aclass = new Class[p_getClasses_0_.length];

         for(int i = 0; i < aclass.length; ++i) {
            Object object = p_getClasses_0_[i];
            if (object != null) {
               aclass[i] = object.getClass();
            }
         }

         return aclass;
      }
   }

   public static Field getField(Class p_getField_0_, Class p_getField_1_) {
      try {
         Field[] afield = p_getField_0_.getDeclaredFields();

         for(int i = 0; i < afield.length; ++i) {
            Field field = afield[i];
            if (field.getType() == p_getField_1_) {
               field.setAccessible(true);
               return field;
            }
         }

         return null;
      } catch (Exception var5) {
         return null;
      }
   }

   public static Field[] getFields(Class p_getFields_0_, Class p_getFields_1_) {
      ArrayList list = new ArrayList();

      try {
         Field[] afield = p_getFields_0_.getDeclaredFields();

         for(int i = 0; i < afield.length; ++i) {
            Field field = afield[i];
            if (field.getType() == p_getFields_1_) {
               field.setAccessible(true);
               list.add(field);
            }
         }

         Field[] afield1 = (Field[])((Field[])((Field[])list.toArray(new Field[list.size()])));
         return afield1;
      } catch (Exception var6) {
         return null;
      }
   }

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
      ForgeHooksClient_dispatchRenderLast = new ReflectorMethod(ForgeHooksClient, "dispatchRenderLast");
      ForgeHooksClient_setRenderPass = new ReflectorMethod(ForgeHooksClient, "setRenderPass");
      ForgeHooksClient_onTextureStitchedPre = new ReflectorMethod(ForgeHooksClient, "onTextureStitchedPre");
      ForgeHooksClient_onTextureStitchedPost = new ReflectorMethod(ForgeHooksClient, "onTextureStitchedPost");
      ForgeHooksClient_renderFirstPersonHand = new ReflectorMethod(ForgeHooksClient, "renderFirstPersonHand");
      ForgeHooksClient_getOffsetFOV = new ReflectorMethod(ForgeHooksClient, "getOffsetFOV");
      ForgeHooksClient_drawScreen = new ReflectorMethod(ForgeHooksClient, "drawScreen");
      ForgeHooksClient_onFogRender = new ReflectorMethod(ForgeHooksClient, "onFogRender");
      ForgeHooksClient_setRenderLayer = new ReflectorMethod(ForgeHooksClient, "setRenderLayer");
      FMLCommonHandler = new ReflectorClass("net.minecraftforge.fml.common.FMLCommonHandler");
      FMLCommonHandler_instance = new ReflectorMethod(FMLCommonHandler, "instance");
      FMLCommonHandler_handleServerStarting = new ReflectorMethod(FMLCommonHandler, "handleServerStarting");
      FMLCommonHandler_handleServerAboutToStart = new ReflectorMethod(FMLCommonHandler, "handleServerAboutToStart");
      FMLCommonHandler_enhanceCrashReport = new ReflectorMethod(FMLCommonHandler, "enhanceCrashReport");
      FMLCommonHandler_getBrandings = new ReflectorMethod(FMLCommonHandler, "getBrandings");
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
      ForgeWorld_getPerWorldStorage = new ReflectorMethod(ForgeWorld, "getPerWorldStorage");
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
      EntityViewRenderEvent_FogColors_Constructor = new ReflectorConstructor(EntityViewRenderEvent_FogColors, new Class[]{EntityRenderer.class, Entity.class, Block.class, Double.TYPE, Float.TYPE, Float.TYPE, Float.TYPE});
      EntityViewRenderEvent_FogColors_red = new ReflectorField(EntityViewRenderEvent_FogColors, "red");
      EntityViewRenderEvent_FogColors_green = new ReflectorField(EntityViewRenderEvent_FogColors, "green");
      EntityViewRenderEvent_FogColors_blue = new ReflectorField(EntityViewRenderEvent_FogColors, "blue");
      EntityViewRenderEvent_FogDensity = new ReflectorClass("net.minecraftforge.client.event.EntityViewRenderEvent$FogDensity");
      EntityViewRenderEvent_FogDensity_Constructor = new ReflectorConstructor(EntityViewRenderEvent_FogDensity, new Class[]{EntityRenderer.class, Entity.class, Block.class, Double.TYPE, Float.TYPE});
      EntityViewRenderEvent_FogDensity_density = new ReflectorField(EntityViewRenderEvent_FogDensity, "density");
      EntityViewRenderEvent_RenderFogEvent = new ReflectorClass("net.minecraftforge.client.event.EntityViewRenderEvent$RenderFogEvent");
      EntityViewRenderEvent_RenderFogEvent_Constructor = new ReflectorConstructor(EntityViewRenderEvent_RenderFogEvent, new Class[]{EntityRenderer.class, Entity.class, Block.class, Double.TYPE, Integer.TYPE, Float.TYPE});
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
      ForgeBlock_hasTileEntity = new ReflectorMethod(ForgeBlock, "hasTileEntity", new Class[]{IBlockState.class});
      ForgeBlock_canCreatureSpawn = new ReflectorMethod(ForgeBlock, "canCreatureSpawn");
      ForgeBlock_addHitEffects = new ReflectorMethod(ForgeBlock, "addHitEffects");
      ForgeBlock_addDestroyEffects = new ReflectorMethod(ForgeBlock, "addDestroyEffects");
      ForgeBlock_isAir = new ReflectorMethod(ForgeBlock, "isAir");
      ForgeBlock_canRenderInLayer = new ReflectorMethod(ForgeBlock, "canRenderInLayer");
      ForgeEntity = new ReflectorClass(Entity.class);
      ForgeEntity_captureDrops = new ReflectorField(ForgeEntity, "captureDrops");
      ForgeEntity_capturedDrops = new ReflectorField(ForgeEntity, "capturedDrops");
      ForgeEntity_shouldRenderInPass = new ReflectorMethod(ForgeEntity, "shouldRenderInPass");
      ForgeEntity_canRiderInteract = new ReflectorMethod(ForgeEntity, "canRiderInteract");
      ForgeTileEntity = new ReflectorClass(TileEntity.class);
      ForgeTileEntity_shouldRenderInPass = new ReflectorMethod(ForgeTileEntity, "shouldRenderInPass");
      ForgeTileEntity_getRenderBoundingBox = new ReflectorMethod(ForgeTileEntity, "getRenderBoundingBox");
      ForgeTileEntity_canRenderBreaking = new ReflectorMethod(ForgeTileEntity, "canRenderBreaking");
      ForgeItem = new ReflectorClass(Item.class);
      ForgeItem_onEntitySwing = new ReflectorMethod(ForgeItem, "onEntitySwing");
      ForgePotionEffect = new ReflectorClass(PotionEffect.class);
      ForgePotionEffect_isCurativeItem = new ReflectorMethod(ForgePotionEffect, "isCurativeItem");
      ForgeItemRecord = new ReflectorClass(ItemRecord.class);
      ForgeItemRecord_getRecordResource = new ReflectorMethod(ForgeItemRecord, "getRecordResource", new Class[]{String.class});
      ForgeVertexFormatElementEnumUseage = new ReflectorClass(VertexFormatElement.EnumUsage.class);
      ForgeVertexFormatElementEnumUseage_preDraw = new ReflectorMethod(ForgeVertexFormatElementEnumUseage, "preDraw");
      ForgeVertexFormatElementEnumUseage_postDraw = new ReflectorMethod(ForgeVertexFormatElementEnumUseage, "postDraw");
   }
}
