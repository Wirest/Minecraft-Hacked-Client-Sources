package optifine;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

public class ReflectorForge {
    public static void FMLClientHandler_trackBrokenTexture(ResourceLocation paramResourceLocation, String paramString) {
        if (!Reflector.FMLClientHandler_trackBrokenTexture.exists()) {
            Object localObject = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
            Reflector.call(localObject, Reflector.FMLClientHandler_trackBrokenTexture, new Object[]{paramResourceLocation, paramString});
        }
    }

    public static void FMLClientHandler_trackMissingTexture(ResourceLocation paramResourceLocation) {
        if (!Reflector.FMLClientHandler_trackMissingTexture.exists()) {
            Object localObject = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
            Reflector.call(localObject, Reflector.FMLClientHandler_trackMissingTexture, new Object[]{paramResourceLocation});
        }
    }

    public static void putLaunchBlackboard(String paramString, Object paramObject) {
        Map localMap = (Map) Reflector.getFieldValue(Reflector.Launch_blackboard);
        if (localMap != null) {
            localMap.put(paramString, paramObject);
        }
    }

    public static boolean renderFirstPersonHand(RenderGlobal paramRenderGlobal, float paramFloat, int paramInt) {
        return !Reflector.ForgeHooksClient_renderFirstPersonHand.exists() ? false : Reflector.callBoolean(Reflector.ForgeHooksClient_renderFirstPersonHand, new Object[]{paramRenderGlobal, Float.valueOf(paramFloat), Integer.valueOf(paramInt)});
    }

    public static InputStream getOptiFineResourceStream(String paramString) {
        if (!Reflector.OptiFineClassTransformer_instance.exists()) {
            return null;
        }
        Object localObject = Reflector.getFieldValue(Reflector.OptiFineClassTransformer_instance);
        if (localObject == null) {
            return null;
        }
        if (paramString.startsWith("/")) {
            paramString = paramString.substring(1);
        }
        byte[] arrayOfByte = (byte[]) (byte[]) Reflector.call(localObject, Reflector.OptiFineClassTransformer_getOptiFineResource, new Object[]{paramString});
        if (arrayOfByte == null) {
            return null;
        }
        ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
        return localByteArrayInputStream;
    }

    public static boolean blockHasTileEntity(IBlockState paramIBlockState) {
        Block localBlock = paramIBlockState.getBlock();
        return !Reflector.ForgeBlock_hasTileEntity.exists() ? localBlock.hasTileEntity() : Reflector.callBoolean(localBlock, Reflector.ForgeBlock_hasTileEntity, new Object[]{paramIBlockState});
    }

    public static boolean isItemDamaged(ItemStack paramItemStack) {
        return !Reflector.ForgeItem_showDurabilityBar.exists() ? paramItemStack.isItemDamaged() : Reflector.callBoolean(paramItemStack.getItem(), Reflector.ForgeItem_showDurabilityBar, new Object[]{paramItemStack});
    }

    public static boolean armorHasOverlay(ItemArmor paramItemArmor, ItemStack paramItemStack) {
        int i = paramItemArmor.getColor(paramItemStack);
        return i != 16777215;
    }
}




