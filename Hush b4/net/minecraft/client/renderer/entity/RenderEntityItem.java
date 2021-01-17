// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.util.MathHelper;
import net.minecraft.client.resources.model.IBakedModel;
import java.util.Random;
import net.minecraft.entity.item.EntityItem;

public class RenderEntityItem extends Render<EntityItem>
{
    private final RenderItem itemRenderer;
    private Random field_177079_e;
    
    public RenderEntityItem(final RenderManager renderManagerIn, final RenderItem p_i46167_2_) {
        super(renderManagerIn);
        this.field_177079_e = new Random();
        this.itemRenderer = p_i46167_2_;
        this.shadowSize = 0.15f;
        this.shadowOpaque = 0.75f;
    }
    
    private int func_177077_a(final EntityItem itemIn, final double p_177077_2_, final double p_177077_4_, final double p_177077_6_, final float p_177077_8_, final IBakedModel p_177077_9_) {
        final ItemStack itemstack = itemIn.getEntityItem();
        final Item item = itemstack.getItem();
        if (item == null) {
            return 0;
        }
        final boolean flag = p_177077_9_.isGui3d();
        final int i = this.func_177078_a(itemstack);
        final float f = 0.25f;
        final float f2 = MathHelper.sin((itemIn.getAge() + p_177077_8_) / 10.0f + itemIn.hoverStart) * 0.1f + 0.1f;
        final float f3 = p_177077_9_.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;
        GlStateManager.translate((float)p_177077_2_, (float)p_177077_4_ + f2 + 0.25f * f3, (float)p_177077_6_);
        if (flag || this.renderManager.options != null) {
            final float f4 = ((itemIn.getAge() + p_177077_8_) / 20.0f + itemIn.hoverStart) * 57.295776f;
            GlStateManager.rotate(f4, 0.0f, 1.0f, 0.0f);
        }
        if (!flag) {
            final float f5 = -0.0f * (i - 1) * 0.5f;
            final float f6 = -0.0f * (i - 1) * 0.5f;
            final float f7 = -0.046875f * (i - 1) * 0.5f;
            GlStateManager.translate(f5, f6, f7);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        return i;
    }
    
    private int func_177078_a(final ItemStack stack) {
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
    
    @Override
    public void doRender(final EntityItem entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        final ItemStack itemstack = entity.getEntityItem();
        this.field_177079_e.setSeed(187L);
        boolean flag = false;
        if (this.bindEntityTexture(entity)) {
            this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity)).setBlurMipmap(false, false);
            flag = true;
        }
        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.pushMatrix();
        final IBakedModel ibakedmodel = this.itemRenderer.getItemModelMesher().getItemModel(itemstack);
        for (int i = this.func_177077_a(entity, x, y, z, partialTicks, ibakedmodel), j = 0; j < i; ++j) {
            if (ibakedmodel.isGui3d()) {
                GlStateManager.pushMatrix();
                if (j > 0) {
                    final float f = (this.field_177079_e.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    final float f2 = (this.field_177079_e.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    final float f3 = (this.field_177079_e.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    GlStateManager.translate(f, f2, f3);
                }
                GlStateManager.scale(0.5f, 0.5f, 0.5f);
                ibakedmodel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GROUND);
                this.itemRenderer.renderItem(itemstack, ibakedmodel);
                GlStateManager.popMatrix();
            }
            else {
                GlStateManager.pushMatrix();
                ibakedmodel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GROUND);
                this.itemRenderer.renderItem(itemstack, ibakedmodel);
                GlStateManager.popMatrix();
                final float f4 = ibakedmodel.getItemCameraTransforms().ground.scale.x;
                final float f5 = ibakedmodel.getItemCameraTransforms().ground.scale.y;
                final float f6 = ibakedmodel.getItemCameraTransforms().ground.scale.z;
                GlStateManager.translate(0.0f * f4, 0.0f * f5, 0.046875f * f6);
            }
        }
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        this.bindEntityTexture(entity);
        if (flag) {
            this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity)).restoreLastBlurMipmap();
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityItem entity) {
        return TextureMap.locationBlocksTexture;
    }
}
