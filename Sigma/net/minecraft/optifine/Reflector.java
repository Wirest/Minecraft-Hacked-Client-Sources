package net.minecraft.optifine;

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
    public static ReflectorMethod ModLoader_renderWorldBlock = new ReflectorMethod(Reflector.ModLoader, "renderWorldBlock");
    public static ReflectorMethod ModLoader_renderInvBlock = new ReflectorMethod(Reflector.ModLoader, "renderInvBlock");
    public static ReflectorMethod ModLoader_renderBlockIsItemFull3D = new ReflectorMethod(Reflector.ModLoader, "renderBlockIsItemFull3D");
    public static ReflectorMethod ModLoader_registerServer = new ReflectorMethod(Reflector.ModLoader, "registerServer");
    public static ReflectorMethod ModLoader_getCustomAnimationLogic = new ReflectorMethod(Reflector.ModLoader, "getCustomAnimationLogic");
    public static ReflectorClass FMLRenderAccessLibrary = new ReflectorClass("net.minecraft.src.FMLRenderAccessLibrary");
    public static ReflectorMethod FMLRenderAccessLibrary_renderWorldBlock = new ReflectorMethod(Reflector.FMLRenderAccessLibrary, "renderWorldBlock");
    public static ReflectorMethod FMLRenderAccessLibrary_renderInventoryBlock = new ReflectorMethod(Reflector.FMLRenderAccessLibrary, "renderInventoryBlock");
    public static ReflectorMethod FMLRenderAccessLibrary_renderItemAsFull3DBlock = new ReflectorMethod(Reflector.FMLRenderAccessLibrary, "renderItemAsFull3DBlock");
    public static ReflectorClass LightCache = new ReflectorClass("LightCache");
    public static ReflectorField LightCache_cache = new ReflectorField(Reflector.LightCache, "cache");
    public static ReflectorMethod LightCache_clear = new ReflectorMethod(Reflector.LightCache, "clear");
    public static ReflectorClass BlockCoord = new ReflectorClass("BlockCoord");
    public static ReflectorMethod BlockCoord_resetPool = new ReflectorMethod(Reflector.BlockCoord, "resetPool");
    public static ReflectorClass MinecraftForge = new ReflectorClass("net.minecraftforge.common.MinecraftForge");
    public static ReflectorField MinecraftForge_EVENT_BUS = new ReflectorField(Reflector.MinecraftForge, "EVENT_BUS");
    public static ReflectorClass ForgeHooks = new ReflectorClass("net.minecraftforge.common.ForgeHooks");
    public static ReflectorMethod ForgeHooks_onLivingSetAttackTarget = new ReflectorMethod(Reflector.ForgeHooks, "onLivingSetAttackTarget");
    public static ReflectorMethod ForgeHooks_onLivingUpdate = new ReflectorMethod(Reflector.ForgeHooks, "onLivingUpdate");
    public static ReflectorMethod ForgeHooks_onLivingAttack = new ReflectorMethod(Reflector.ForgeHooks, "onLivingAttack");
    public static ReflectorMethod ForgeHooks_onLivingHurt = new ReflectorMethod(Reflector.ForgeHooks, "onLivingHurt");
    public static ReflectorMethod ForgeHooks_onLivingDeath = new ReflectorMethod(Reflector.ForgeHooks, "onLivingDeath");
    public static ReflectorMethod ForgeHooks_onLivingDrops = new ReflectorMethod(Reflector.ForgeHooks, "onLivingDrops");
    public static ReflectorMethod ForgeHooks_onLivingFall = new ReflectorMethod(Reflector.ForgeHooks, "onLivingFall");
    public static ReflectorMethod ForgeHooks_onLivingJump = new ReflectorMethod(Reflector.ForgeHooks, "onLivingJump");
    public static ReflectorClass MinecraftForgeClient = new ReflectorClass("net.minecraftforge.client.MinecraftForgeClient");
    public static ReflectorMethod MinecraftForgeClient_getRenderPass = new ReflectorMethod(Reflector.MinecraftForgeClient, "getRenderPass");
    public static ReflectorMethod MinecraftForgeClient_getItemRenderer = new ReflectorMethod(Reflector.MinecraftForgeClient, "getItemRenderer");
    public static ReflectorClass ForgeHooksClient = new ReflectorClass("net.minecraftforge.client.ForgeHooksClient");
    public static ReflectorMethod ForgeHooksClient_onDrawBlockHighlight = new ReflectorMethod(Reflector.ForgeHooksClient, "onDrawBlockHighlight");
    public static ReflectorMethod ForgeHooksClient_orientBedCamera = new ReflectorMethod(Reflector.ForgeHooksClient, "orientBedCamera");
    public static ReflectorMethod ForgeHooksClient_renderEquippedItem = new ReflectorMethod(Reflector.ForgeHooksClient, "renderEquippedItem");
    public static ReflectorMethod ForgeHooksClient_dispatchRenderLast = new ReflectorMethod(Reflector.ForgeHooksClient, "dispatchRenderLast");
    public static ReflectorMethod ForgeHooksClient_onTextureLoadPre = new ReflectorMethod(Reflector.ForgeHooksClient, "onTextureLoadPre");
    public static ReflectorMethod ForgeHooksClient_setRenderPass = new ReflectorMethod(Reflector.ForgeHooksClient, "setRenderPass");
    public static ReflectorMethod ForgeHooksClient_onTextureStitchedPre = new ReflectorMethod(Reflector.ForgeHooksClient, "onTextureStitchedPre");
    public static ReflectorMethod ForgeHooksClient_onTextureStitchedPost = new ReflectorMethod(Reflector.ForgeHooksClient, "onTextureStitchedPost");
    public static ReflectorMethod ForgeHooksClient_renderFirstPersonHand = new ReflectorMethod(Reflector.ForgeHooksClient, "renderFirstPersonHand");
    public static ReflectorMethod ForgeHooksClient_setWorldRendererRB = new ReflectorMethod(Reflector.ForgeHooksClient, "setWorldRendererRB");
    public static ReflectorMethod ForgeHooksClient_onPreRenderWorld = new ReflectorMethod(Reflector.ForgeHooksClient, "onPreRenderWorld");
    public static ReflectorMethod ForgeHooksClient_onPostRenderWorld = new ReflectorMethod(Reflector.ForgeHooksClient, "onPostRenderWorld");
    public static ReflectorClass FMLCommonHandler = new ReflectorClass("net.minecraftforge.fml.common.FMLCommonHandler");
    public static ReflectorMethod FMLCommonHandler_instance = new ReflectorMethod(Reflector.FMLCommonHandler, "instance");
    public static ReflectorMethod FMLCommonHandler_handleServerStarting = new ReflectorMethod(Reflector.FMLCommonHandler, "handleServerStarting");
    public static ReflectorMethod FMLCommonHandler_handleServerAboutToStart = new ReflectorMethod(Reflector.FMLCommonHandler, "handleServerAboutToStart");
    public static ReflectorMethod FMLCommonHandler_enhanceCrashReport = new ReflectorMethod(Reflector.FMLCommonHandler, "enhanceCrashReport");
    public static ReflectorClass FMLClientHandler = new ReflectorClass("net.minecraftforge.fml.client.FMLClientHandler");
    public static ReflectorMethod FMLClientHandler_instance = new ReflectorMethod(Reflector.FMLClientHandler, "instance");
    public static ReflectorMethod FMLClientHandler_isLoading = new ReflectorMethod(Reflector.FMLClientHandler, "isLoading");
    public static ReflectorClass ItemRenderType = new ReflectorClass("net.minecraftforge.client.IItemRenderer$ItemRenderType");
    public static ReflectorField ItemRenderType_EQUIPPED = new ReflectorField(Reflector.ItemRenderType, "EQUIPPED");
    public static ReflectorClass ForgeWorldProvider = new ReflectorClass(WorldProvider.class);
    public static ReflectorMethod ForgeWorldProvider_getSkyRenderer = new ReflectorMethod(Reflector.ForgeWorldProvider, "getSkyRenderer");
    public static ReflectorMethod ForgeWorldProvider_getCloudRenderer = new ReflectorMethod(Reflector.ForgeWorldProvider, "getCloudRenderer");
    public static ReflectorMethod ForgeWorldProvider_getWeatherRenderer = new ReflectorMethod(Reflector.ForgeWorldProvider, "getWeatherRenderer");
    public static ReflectorClass ForgeWorld = new ReflectorClass(World.class);
    public static ReflectorMethod ForgeWorld_countEntities = new ReflectorMethod(Reflector.ForgeWorld, "countEntities", new Class[]{EnumCreatureType.class, Boolean.TYPE});
    public static ReflectorClass IRenderHandler = new ReflectorClass("net.minecraftforge.client.IRenderHandler");
    public static ReflectorMethod IRenderHandler_render = new ReflectorMethod(Reflector.IRenderHandler, "render");
    public static ReflectorClass DimensionManager = new ReflectorClass("net.minecraftforge.common.DimensionManager");
    public static ReflectorMethod DimensionManager_getStaticDimensionIDs = new ReflectorMethod(Reflector.DimensionManager, "getStaticDimensionIDs");
    public static ReflectorClass WorldEvent_Load = new ReflectorClass("net.minecraftforge.event.world.WorldEvent$Load");
    public static ReflectorConstructor WorldEvent_Load_Constructor = new ReflectorConstructor(Reflector.WorldEvent_Load, new Class[]{World.class});
    public static ReflectorClass DrawScreenEvent_Pre = new ReflectorClass("net.minecraftforge.client.event.GuiScreenEvent$DrawScreenEvent$Pre");
    public static ReflectorConstructor DrawScreenEvent_Pre_Constructor = new ReflectorConstructor(Reflector.DrawScreenEvent_Pre, new Class[]{GuiScreen.class, Integer.TYPE, Integer.TYPE, Float.TYPE});
    public static ReflectorClass DrawScreenEvent_Post = new ReflectorClass("net.minecraftforge.client.event.GuiScreenEvent$DrawScreenEvent$Post");
    public static ReflectorConstructor DrawScreenEvent_Post_Constructor = new ReflectorConstructor(Reflector.DrawScreenEvent_Post, new Class[]{GuiScreen.class, Integer.TYPE, Integer.TYPE, Float.TYPE});
    public static ReflectorClass EntityViewRenderEvent_FogColors = new ReflectorClass("net.minecraftforge.client.event.EntityViewRenderEvent$FogColors");
    public static ReflectorConstructor EntityViewRenderEvent_FogColors_Constructor = new ReflectorConstructor(Reflector.EntityViewRenderEvent_FogColors, new Class[]{EntityRenderer.class, EntityLivingBase.class, Block.class, Double.TYPE, Float.TYPE, Float.TYPE, Float.TYPE});
    public static ReflectorField EntityViewRenderEvent_FogColors_red = new ReflectorField(Reflector.EntityViewRenderEvent_FogColors, "red");
    public static ReflectorField EntityViewRenderEvent_FogColors_green = new ReflectorField(Reflector.EntityViewRenderEvent_FogColors, "green");
    public static ReflectorField EntityViewRenderEvent_FogColors_blue = new ReflectorField(Reflector.EntityViewRenderEvent_FogColors, "blue");
    public static ReflectorClass EntityViewRenderEvent_FogDensity = new ReflectorClass("net.minecraftforge.client.event.EntityViewRenderEvent$FogDensity");
    public static ReflectorConstructor EntityViewRenderEvent_FogDensity_Constructor = new ReflectorConstructor(Reflector.EntityViewRenderEvent_FogDensity, new Class[]{EntityRenderer.class, EntityLivingBase.class, Block.class, Double.TYPE, Float.TYPE});
    public static ReflectorField EntityViewRenderEvent_FogDensity_density = new ReflectorField(Reflector.EntityViewRenderEvent_FogDensity, "density");
    public static ReflectorClass EntityViewRenderEvent_RenderFogEvent = new ReflectorClass("net.minecraftforge.client.event.EntityViewRenderEvent$RenderFogEvent");
    public static ReflectorConstructor EntityViewRenderEvent_RenderFogEvent_Constructor = new ReflectorConstructor(Reflector.EntityViewRenderEvent_RenderFogEvent, new Class[]{EntityRenderer.class, EntityLivingBase.class, Block.class, Double.TYPE, Integer.TYPE, Float.TYPE});
    public static ReflectorClass EventBus = new ReflectorClass("net.minecraftforge.fml.common.eventhandler.EventBus");
    public static ReflectorMethod EventBus_post = new ReflectorMethod(Reflector.EventBus, "post");
    public static ReflectorClass Event_Result = new ReflectorClass("net.minecraftforge.fml.common.eventhandler.Event$Result");
    public static ReflectorField Event_Result_DENY = new ReflectorField(Reflector.Event_Result, "DENY");
    public static ReflectorField Event_Result_ALLOW = new ReflectorField(Reflector.Event_Result, "ALLOW");
    public static ReflectorField Event_Result_DEFAULT = new ReflectorField(Reflector.Event_Result, "DEFAULT");
    public static ReflectorClass ForgeEventFactory = new ReflectorClass("net.minecraftforge.event.ForgeEventFactory");
    public static ReflectorMethod ForgeEventFactory_canEntitySpawn = new ReflectorMethod(Reflector.ForgeEventFactory, "canEntitySpawn");
    public static ReflectorMethod ForgeEventFactory_canEntityDespawn = new ReflectorMethod(Reflector.ForgeEventFactory, "canEntityDespawn");
    public static ReflectorClass ChunkWatchEvent_UnWatch = new ReflectorClass("net.minecraftforge.event.world.ChunkWatchEvent$UnWatch");
    public static ReflectorConstructor ChunkWatchEvent_UnWatch_Constructor = new ReflectorConstructor(Reflector.ChunkWatchEvent_UnWatch, new Class[]{ChunkCoordIntPair.class, EntityPlayerMP.class});
    public static ReflectorClass ForgeBlock = new ReflectorClass(Block.class);
    public static ReflectorMethod ForgeBlock_getBedDirection = new ReflectorMethod(Reflector.ForgeBlock, "getBedDirection");
    public static ReflectorMethod ForgeBlock_isBedFoot = new ReflectorMethod(Reflector.ForgeBlock, "isBedFoot");
    public static ReflectorMethod ForgeBlock_canRenderInPass = new ReflectorMethod(Reflector.ForgeBlock, "canRenderInPass");
    public static ReflectorMethod ForgeBlock_hasTileEntity = new ReflectorMethod(Reflector.ForgeBlock, "hasTileEntity", new Class[]{Integer.TYPE});
    public static ReflectorMethod ForgeBlock_canCreatureSpawn = new ReflectorMethod(Reflector.ForgeBlock, "canCreatureSpawn");
    public static ReflectorClass ForgeEntity = new ReflectorClass(Entity.class);
    public static ReflectorField ForgeEntity_captureDrops = new ReflectorField(Reflector.ForgeEntity, "captureDrops");
    public static ReflectorField ForgeEntity_capturedDrops = new ReflectorField(Reflector.ForgeEntity, "capturedDrops");
    public static ReflectorMethod ForgeEntity_shouldRenderInPass = new ReflectorMethod(Reflector.ForgeEntity, "shouldRenderInPass");
    public static ReflectorMethod ForgeEntity_canRiderInteract = new ReflectorMethod(Reflector.ForgeEntity, "canRiderInteract");
    public static ReflectorClass ForgeTileEntity = new ReflectorClass(TileEntity.class);
    public static ReflectorMethod ForgeTileEntity_shouldRenderInPass = new ReflectorMethod(Reflector.ForgeTileEntity, "shouldRenderInPass");
    public static ReflectorMethod ForgeTileEntity_getRenderBoundingBox = new ReflectorMethod(Reflector.ForgeTileEntity, "getRenderBoundingBox");
    public static ReflectorClass ForgeItem = new ReflectorClass(Item.class);
    public static ReflectorMethod ForgeItem_onEntitySwing = new ReflectorMethod(Reflector.ForgeItem, "onEntitySwing");
    public static ReflectorClass ForgePotionEffect = new ReflectorClass(PotionEffect.class);
    public static ReflectorMethod ForgePotionEffect_isCurativeItem = new ReflectorMethod(Reflector.ForgePotionEffect, "isCurativeItem");
    public static ReflectorClass ForgeItemStack = new ReflectorClass(ItemStack.class);
    public static ReflectorMethod ForgeItemStack_hasEffect = new ReflectorMethod(Reflector.ForgeItemStack, "hasEffect", new Class[]{Integer.TYPE});
    public static ReflectorClass ForgeItemRecord = new ReflectorClass(ItemRecord.class);
    public static ReflectorMethod ForgeItemRecord_getRecordResource = new ReflectorMethod(Reflector.ForgeItemRecord, "getRecordResource", new Class[]{String.class});

    public static void callVoid(ReflectorMethod refMethod, Object... params) {
        try {
            Method e = refMethod.getTargetMethod();

            if (e == null) {
                return;
            }

            e.invoke((Object) null, params);
        } catch (Throwable var3) {
            Reflector.handleException(var3, (Object) null, refMethod, params);
        }
    }

    public static boolean callBoolean(ReflectorMethod refMethod, Object... params) {
        try {
            Method e = refMethod.getTargetMethod();

            if (e == null) {
                return false;
            } else {
                Boolean retVal = (Boolean) e.invoke((Object) null, params);
                return retVal.booleanValue();
            }
        } catch (Throwable var4) {
            Reflector.handleException(var4, (Object) null, refMethod, params);
            return false;
        }
    }

    public static int callInt(ReflectorMethod refMethod, Object... params) {
        try {
            Method e = refMethod.getTargetMethod();

            if (e == null) {
                return 0;
            } else {
                Integer retVal = (Integer) e.invoke((Object) null, params);
                return retVal.intValue();
            }
        } catch (Throwable var4) {
            Reflector.handleException(var4, (Object) null, refMethod, params);
            return 0;
        }
    }

    public static float callFloat(ReflectorMethod refMethod, Object... params) {
        try {
            Method e = refMethod.getTargetMethod();

            if (e == null) {
                return 0.0F;
            } else {
                Float retVal = (Float) e.invoke((Object) null, params);
                return retVal.floatValue();
            }
        } catch (Throwable var4) {
            Reflector.handleException(var4, (Object) null, refMethod, params);
            return 0.0F;
        }
    }

    public static String callString(ReflectorMethod refMethod, Object... params) {
        try {
            Method e = refMethod.getTargetMethod();

            if (e == null) {
                return null;
            } else {
                String retVal = (String) e.invoke((Object) null, params);
                return retVal;
            }
        } catch (Throwable var4) {
            Reflector.handleException(var4, (Object) null, refMethod, params);
            return null;
        }
    }

    public static Object call(ReflectorMethod refMethod, Object... params) {
        try {
            Method e = refMethod.getTargetMethod();

            if (e == null) {
                return null;
            } else {
                Object retVal = e.invoke((Object) null, params);
                return retVal;
            }
        } catch (Throwable var4) {
            Reflector.handleException(var4, (Object) null, refMethod, params);
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
            Reflector.handleException(var4, obj, refMethod, params);
        }
    }

    public static boolean callBoolean(Object obj, ReflectorMethod refMethod, Object... params) {
        try {
            Method e = refMethod.getTargetMethod();

            if (e == null) {
                return false;
            } else {
                Boolean retVal = (Boolean) e.invoke(obj, params);
                return retVal.booleanValue();
            }
        } catch (Throwable var5) {
            Reflector.handleException(var5, obj, refMethod, params);
            return false;
        }
    }

    public static int callInt(Object obj, ReflectorMethod refMethod, Object... params) {
        try {
            Method e = refMethod.getTargetMethod();

            if (e == null) {
                return 0;
            } else {
                Integer retVal = (Integer) e.invoke(obj, params);
                return retVal.intValue();
            }
        } catch (Throwable var5) {
            Reflector.handleException(var5, obj, refMethod, params);
            return 0;
        }
    }

    public static float callFloat(Object obj, ReflectorMethod refMethod, Object... params) {
        try {
            Method e = refMethod.getTargetMethod();

            if (e == null) {
                return 0.0F;
            } else {
                Float retVal = (Float) e.invoke(obj, params);
                return retVal.floatValue();
            }
        } catch (Throwable var5) {
            Reflector.handleException(var5, obj, refMethod, params);
            return 0.0F;
        }
    }

    public static String callString(Object obj, ReflectorMethod refMethod, Object... params) {
        try {
            Method e = refMethod.getTargetMethod();

            if (e == null) {
                return null;
            } else {
                String retVal = (String) e.invoke(obj, params);
                return retVal;
            }
        } catch (Throwable var5) {
            Reflector.handleException(var5, obj, refMethod, params);
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
            Reflector.handleException(var5, obj, refMethod, params);
            return null;
        }
    }

    public static Object getFieldValue(ReflectorField refField) {
        return Reflector.getFieldValue((Object) null, refField);
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
        Object val = Reflector.getFieldValue(obj, refField);

        if (!(val instanceof Float)) {
            return def;
        } else {
            Float valFloat = (Float) val;
            return valFloat.floatValue();
        }
    }

    public static void setFieldValue(ReflectorField refField, Object value) {
        Reflector.setFieldValue((Object) null, refField, value);
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
        Object event = Reflector.newInstance(constr, params);
        return event == null ? false : Reflector.postForgeBusEvent(event);
    }

    public static boolean postForgeBusEvent(Object event) {
        if (event == null) {
            return false;
        } else {
            Object eventBus = Reflector.getFieldValue(Reflector.MinecraftForge_EVENT_BUS);

            if (eventBus == null) {
                return false;
            } else {
                Object ret = Reflector.call(eventBus, Reflector.EventBus_post, new Object[]{event});

                if (!(ret instanceof Boolean)) {
                    return false;
                } else {
                    Boolean retBool = (Boolean) ret;
                    return retBool.booleanValue();
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
                Reflector.handleException(var4, constr, params);
                return null;
            }
        }
    }

    public static boolean matchesTypes(Class[] pTypes, Class[] cTypes) {
        if (pTypes.length != cTypes.length) {
            return false;
        } else {
            for (int i = 0; i < cTypes.length; ++i) {
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
                Config.warn("Parameter classes: " + Config.arrayToString(Reflector.getClasses(params)));
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
                Config.warn("Parameter classes: " + Config.arrayToString(Reflector.getClasses(params)));
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

            for (int i = 0; i < classes.length; ++i) {
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

            for (Field field : e) {
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

            for (Field field : e) {
                if (field.getType() == fieldType) {
                    field.setAccessible(true);
                    list.add(field);
                }
            }

            Field[] var7 = ((Field[]) list.toArray(new Field[list.size()]));
            return var7;
        } catch (Exception var6) {
            return null;
        }
    }
}
