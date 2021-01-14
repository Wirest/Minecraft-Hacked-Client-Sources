package splash.client.modules.visual;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;

public class ItemPhysics extends Module {

	public static Minecraft mc;
    public static long tick;
    public static double rotation = 0;
    public static Random random;
    
    public ItemPhysics() {
        super("ItemPhysics", "Item physics for items on the ground.", ModuleCategory.VISUALS);
    }
    
    public static ResourceLocation getEntityTexture() {
        return TextureMap.locationBlocksTexture;
    }
    
    public static void setPositionAndRotation2(final EntityItem item, final double x, final double y, final double z, final float yaw, final float pitch, final int posRotationIncrements, final boolean p_180426_10_) {
        item.setPosition(x, y, z);
    }
    
    public static void doRender(final RenderEntityItem renderer, final Entity entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        rotation = (System.nanoTime() - tick) / 2500000.0 * 0.006f;
        if (!mc.inGameHasFocus) {
            rotation = 0.0;
        }
        final EntityItem item = (EntityItem)entity;
        final ItemStack itemstack = item.getEntityItem();
        int i;
        if (itemstack != null && itemstack.getItem() != null) {
            i = Item.getIdFromItem(itemstack.getItem()) + itemstack.getMetadata();
        }
        else {
            i = 187;
        }
        random.setSeed(i);
        final boolean flag = true;
        renderer.bindTexture(getEntityTexture());
        renderer.getRenderManager().renderEngine.getTexture(getEntityTexture()).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.pushMatrix();
        IBakedModel ibakedmodel = mc.getRenderItem().getItemModelMesher().getItemModel(itemstack);
        final boolean flag2 = ibakedmodel.isGui3d();
        final boolean is3D = ibakedmodel.isGui3d();
        final int j = getModelCount(itemstack);
        final float f = 0.25f;
        final float f2 = 0.0f;
        final float f3 = ibakedmodel.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;
        GlStateManager.translate((float)x, (float)y, (float)z);
        if (ibakedmodel.isGui3d()) {
            GlStateManager.scale(0.5f, 0.5f, 0.5f);
        }
        GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(item.rotationYaw, 0.0f, 0.0f, 1.0f);
        if (is3D) {
            GlStateManager.translate(0.0, 0.0, -0.08);
        }
        else {
            GlStateManager.translate(0.0, 0.0, -0.04);
        }
        if (is3D || mc.getRenderManager().options != null) {
            if (is3D) {
                if (!item.onGround) {
                	rotation *= 2.0;
                    final EntityItem entityItem = item;
                    entityItem.rotationPitch += (float)rotation;
                }
            }
            else if (item != null && !Double.isNaN(item.posX) && !Double.isNaN(item.posY) && !Double.isNaN(item.posZ) && item.worldObj != null) {
                if (item.onGround) {
                    item.rotationPitch = 0.0f;
                }
                else {
                	rotation *= 2.0;
                    final EntityItem entityItem2 = item;
                    entityItem2.rotationPitch += (float)rotation;
                }
            }
            GlStateManager.rotate(item.rotationPitch, 1.0f, 0.0f, 0.0f);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        for (int k = 0; k < j; ++k) {
            if (flag2) {
                GlStateManager.pushMatrix();
                if (k > 0) {
                    final float f4 = (random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    final float f5 = (random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    final float f6 = (random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    GlStateManager.translate(f4, f5, f6);
                }
                ibakedmodel = handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND);
                mc.getRenderItem().renderItem(itemstack, ibakedmodel);
                GlStateManager.popMatrix();
            }
            else {
                GlStateManager.pushMatrix();
                if (k > 0) {}
                ibakedmodel = handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND);
                mc.getRenderItem().renderItem(itemstack, ibakedmodel);
                GlStateManager.popMatrix();
                GlStateManager.translate(0.0f, 0.0f, 0.05375f);
            }
        }
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        renderer.bindTexture(getEntityTexture());
        if (flag) {
            renderer.getRenderManager().renderEngine.getTexture(getEntityTexture()).restoreLastBlurMipmap();
        }
    }
    
    public static int getModelCount(final ItemStack stack) {
        int i = 1;
        if (stack.stackSize > 48) {
            i = 5;
        }
        else if (stack.stackSize > 32) {
            i = 4;
        }
        else if (stack.stackSize > 16) {
            i = 3;
        }
        else if (stack.stackSize > 1) {
            i = 2;
        }
        return i;
    }
    
    public static IBakedModel handleCameraTransforms(final IBakedModel model, final ItemCameraTransforms.TransformType cameraTransformType) {
        model.getItemCameraTransforms().applyTransform(cameraTransformType);
        return model;
    }
    
    static {
        mc = Minecraft.getMinecraft();
        random = new Random();
    }
}
