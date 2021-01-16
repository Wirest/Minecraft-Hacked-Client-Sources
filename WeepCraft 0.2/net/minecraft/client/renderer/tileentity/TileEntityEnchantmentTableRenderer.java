package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class TileEntityEnchantmentTableRenderer extends TileEntitySpecialRenderer<TileEntityEnchantmentTable>
{
    /** The texture for the book above the enchantment table. */
    private static final ResourceLocation TEXTURE_BOOK = new ResourceLocation("textures/entity/enchanting_table_book.png");
    private final ModelBook modelBook = new ModelBook();

    public void func_192841_a(TileEntityEnchantmentTable p_192841_1_, double p_192841_2_, double p_192841_4_, double p_192841_6_, float p_192841_8_, int p_192841_9_, float p_192841_10_)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_192841_2_ + 0.5F, (float)p_192841_4_ + 0.75F, (float)p_192841_6_ + 0.5F);
        float f = (float)p_192841_1_.tickCount + p_192841_8_;
        GlStateManager.translate(0.0F, 0.1F + MathHelper.sin(f * 0.1F) * 0.01F, 0.0F);
        float f1;

        for (f1 = p_192841_1_.bookRotation - p_192841_1_.bookRotationPrev; f1 >= (float)Math.PI; f1 -= ((float)Math.PI * 2F))
        {
            ;
        }

        while (f1 < -(float)Math.PI)
        {
            f1 += ((float)Math.PI * 2F);
        }

        float f2 = p_192841_1_.bookRotationPrev + f1 * p_192841_8_;
        GlStateManager.rotate(-f2 * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(80.0F, 0.0F, 0.0F, 1.0F);
        this.bindTexture(TEXTURE_BOOK);
        float f3 = p_192841_1_.pageFlipPrev + (p_192841_1_.pageFlip - p_192841_1_.pageFlipPrev) * p_192841_8_ + 0.25F;
        float f4 = p_192841_1_.pageFlipPrev + (p_192841_1_.pageFlip - p_192841_1_.pageFlipPrev) * p_192841_8_ + 0.75F;
        f3 = (f3 - (float)MathHelper.fastFloor((double)f3)) * 1.6F - 0.3F;
        f4 = (f4 - (float)MathHelper.fastFloor((double)f4)) * 1.6F - 0.3F;

        if (f3 < 0.0F)
        {
            f3 = 0.0F;
        }

        if (f4 < 0.0F)
        {
            f4 = 0.0F;
        }

        if (f3 > 1.0F)
        {
            f3 = 1.0F;
        }

        if (f4 > 1.0F)
        {
            f4 = 1.0F;
        }

        float f5 = p_192841_1_.bookSpreadPrev + (p_192841_1_.bookSpread - p_192841_1_.bookSpreadPrev) * p_192841_8_;
        GlStateManager.enableCull();
        this.modelBook.render((Entity)null, f, f3, f4, f5, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
    }
}
