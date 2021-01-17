// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import net.minecraft.client.renderer.RenderGlobal;
import java.util.Map;
import net.minecraft.util.ResourceLocation;

public class ReflectorForge
{
    public static void FMLClientHandler_trackBrokenTexture(final ResourceLocation p_FMLClientHandler_trackBrokenTexture_0_, final String p_FMLClientHandler_trackBrokenTexture_1_) {
        if (!Reflector.FMLClientHandler_trackBrokenTexture.exists()) {
            final Object object = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
            Reflector.call(object, Reflector.FMLClientHandler_trackBrokenTexture, p_FMLClientHandler_trackBrokenTexture_0_, p_FMLClientHandler_trackBrokenTexture_1_);
        }
    }
    
    public static void FMLClientHandler_trackMissingTexture(final ResourceLocation p_FMLClientHandler_trackMissingTexture_0_) {
        if (!Reflector.FMLClientHandler_trackMissingTexture.exists()) {
            final Object object = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
            Reflector.call(object, Reflector.FMLClientHandler_trackMissingTexture, p_FMLClientHandler_trackMissingTexture_0_);
        }
    }
    
    public static void putLaunchBlackboard(final String p_putLaunchBlackboard_0_, final Object p_putLaunchBlackboard_1_) {
        final Map map = (Map)Reflector.getFieldValue(Reflector.Launch_blackboard);
        if (map != null) {
            map.put(p_putLaunchBlackboard_0_, p_putLaunchBlackboard_1_);
        }
    }
    
    public static boolean renderFirstPersonHand(final RenderGlobal p_renderFirstPersonHand_0_, final float p_renderFirstPersonHand_1_, final int p_renderFirstPersonHand_2_) {
        return Reflector.ForgeHooksClient_renderFirstPersonHand.exists() && Reflector.callBoolean(Reflector.ForgeHooksClient_renderFirstPersonHand, p_renderFirstPersonHand_0_, p_renderFirstPersonHand_1_, p_renderFirstPersonHand_2_);
    }
    
    public static InputStream getOptiFineResourceStream(String p_getOptiFineResourceStream_0_) {
        if (!Reflector.OptiFineClassTransformer_instance.exists()) {
            return null;
        }
        final Object object = Reflector.getFieldValue(Reflector.OptiFineClassTransformer_instance);
        if (object == null) {
            return null;
        }
        if (p_getOptiFineResourceStream_0_.startsWith("/")) {
            p_getOptiFineResourceStream_0_ = p_getOptiFineResourceStream_0_.substring(1);
        }
        final byte[] abyte = (byte[])Reflector.call(object, Reflector.OptiFineClassTransformer_getOptiFineResource, p_getOptiFineResourceStream_0_);
        if (abyte == null) {
            return null;
        }
        final InputStream inputstream = new ByteArrayInputStream(abyte);
        return inputstream;
    }
    
    public static boolean blockHasTileEntity(final IBlockState p_blockHasTileEntity_0_) {
        final Block block = p_blockHasTileEntity_0_.getBlock();
        return Reflector.ForgeBlock_hasTileEntity.exists() ? Reflector.callBoolean(block, Reflector.ForgeBlock_hasTileEntity, p_blockHasTileEntity_0_) : block.hasTileEntity();
    }
    
    public static boolean isItemDamaged(final ItemStack p_isItemDamaged_0_) {
        return Reflector.ForgeItem_showDurabilityBar.exists() ? Reflector.callBoolean(p_isItemDamaged_0_.getItem(), Reflector.ForgeItem_showDurabilityBar, p_isItemDamaged_0_) : p_isItemDamaged_0_.isItemDamaged();
    }
    
    public static boolean armorHasOverlay(final ItemArmor p_armorHasOverlay_0_, final ItemStack p_armorHasOverlay_1_) {
        final int i = p_armorHasOverlay_0_.getColor(p_armorHasOverlay_1_);
        return i != 16777215;
    }
}
