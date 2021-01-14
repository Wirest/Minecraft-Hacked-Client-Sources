package net.minecraft.client.renderer.entity.layers;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class LayerHeldItem implements LayerRenderer {
    private final RendererLivingEntity field_177206_a;
    private static final String __OBFID = "CL_00002416";

    public LayerHeldItem(RendererLivingEntity p_i46115_1_) {
        this.field_177206_a = p_i46115_1_;
    }

    public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_) {
        ItemStack var9 = p_177141_1_.getHeldItem();

        if (var9 != null) {
            GlStateManager.pushMatrix();

            if (this.field_177206_a.getMainModel().isChild) {
                float var10 = 0.5F;
                GlStateManager.translate(0.0F, 0.625F, 0.0F);
                GlStateManager.rotate(-20.0F, -1.0F, 0.0F, 0.0F);
                GlStateManager.scale(var10, var10, var10);
            }

            ((ModelBiped) this.field_177206_a.getMainModel()).postRenderHiddenArm(0.0625F);
            GlStateManager.translate(-0.0625F, 0.4375F, 0.0625F);

            if (p_177141_1_ instanceof EntityPlayer && ((EntityPlayer) p_177141_1_).fishEntity != null) {
                var9 = new ItemStack(Items.fishing_rod, 0);
            }

            Item var13 = var9.getItem();
            Minecraft var11 = Minecraft.getMinecraft();

            if (var13 instanceof ItemBlock && Block.getBlockFromItem(var13).getRenderType() == 2) {
                GlStateManager.translate(0.0F, 0.1875F, -0.3125F);
                GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                float var12 = 0.375F;
                GlStateManager.scale(-var12, -var12, var12);
            }

            var11.getItemRenderer().renderItem(p_177141_1_, var9, ItemCameraTransforms.TransformType.THIRD_PERSON);
            GlStateManager.popMatrix();
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
