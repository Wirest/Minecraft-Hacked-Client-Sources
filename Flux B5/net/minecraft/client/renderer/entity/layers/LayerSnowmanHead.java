package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderSnowMan;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class LayerSnowmanHead implements LayerRenderer
{
    private final RenderSnowMan field_177152_a;
    private static final String __OBFID = "CL_00002411";

    public LayerSnowmanHead(RenderSnowMan p_i46110_1_)
    {
        this.field_177152_a = p_i46110_1_;
    }

    public void func_177151_a(EntitySnowman p_177151_1_, float p_177151_2_, float p_177151_3_, float p_177151_4_, float p_177151_5_, float p_177151_6_, float p_177151_7_, float p_177151_8_)
    {
        if (!p_177151_1_.isInvisible())
        {
            GlStateManager.pushMatrix();
            this.field_177152_a.func_177123_g().head.postRender(0.0625F);
            float var9 = 0.625F;
            GlStateManager.translate(0.0F, -0.34375F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.scale(var9, -var9, -var9);
            Minecraft.getMinecraft().getItemRenderer().renderItem(p_177151_1_, new ItemStack(Blocks.pumpkin, 1), ItemCameraTransforms.TransformType.HEAD);
            GlStateManager.popMatrix();
        }
    }

    public boolean shouldCombineTextures()
    {
        return true;
    }

    public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_)
    {
        this.func_177151_a((EntitySnowman)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
    }
}
