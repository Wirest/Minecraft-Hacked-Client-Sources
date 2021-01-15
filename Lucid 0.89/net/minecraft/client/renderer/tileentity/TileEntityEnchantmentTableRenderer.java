package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class TileEntityEnchantmentTableRenderer extends TileEntitySpecialRenderer
{
    /** The texture for the book above the enchantment table. */
    private static final ResourceLocation TEXTURE_BOOK = new ResourceLocation("textures/entity/enchanting_table_book.png");
    private ModelBook field_147541_c = new ModelBook();

    public void func_180537_a(TileEntityEnchantmentTable enchantmentTable, double p_180537_2_, double p_180537_4_, double p_180537_6_, float p_180537_8_, int p_180537_9_)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_180537_2_ + 0.5F, (float)p_180537_4_ + 0.75F, (float)p_180537_6_ + 0.5F);
        float var10 = enchantmentTable.tickCount + p_180537_8_;
        GlStateManager.translate(0.0F, 0.1F + MathHelper.sin(var10 * 0.1F) * 0.01F, 0.0F);
        float var11;

        for (var11 = enchantmentTable.bookRotation - enchantmentTable.bookRotationPrev; var11 >= (float)Math.PI; var11 -= ((float)Math.PI * 2F))
        {
            ;
        }

        while (var11 < -(float)Math.PI)
        {
            var11 += ((float)Math.PI * 2F);
        }

        float var12 = enchantmentTable.bookRotationPrev + var11 * p_180537_8_;
        GlStateManager.rotate(-var12 * 180.0F / (float)Math.PI, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(80.0F, 0.0F, 0.0F, 1.0F);
        this.bindTexture(TEXTURE_BOOK);
        float var13 = enchantmentTable.pageFlipPrev + (enchantmentTable.pageFlip - enchantmentTable.pageFlipPrev) * p_180537_8_ + 0.25F;
        float var14 = enchantmentTable.pageFlipPrev + (enchantmentTable.pageFlip - enchantmentTable.pageFlipPrev) * p_180537_8_ + 0.75F;
        var13 = (var13 - MathHelper.truncateDoubleToInt(var13)) * 1.6F - 0.3F;
        var14 = (var14 - MathHelper.truncateDoubleToInt(var14)) * 1.6F - 0.3F;

        if (var13 < 0.0F)
        {
            var13 = 0.0F;
        }

        if (var14 < 0.0F)
        {
            var14 = 0.0F;
        }

        if (var13 > 1.0F)
        {
            var13 = 1.0F;
        }

        if (var14 > 1.0F)
        {
            var14 = 1.0F;
        }

        float var15 = enchantmentTable.bookSpreadPrev + (enchantmentTable.bookSpread - enchantmentTable.bookSpreadPrev) * p_180537_8_;
        GlStateManager.enableCull();
        this.field_147541_c.render((Entity)null, var10, var13, var14, var15, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
    }

    @Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        this.func_180537_a((TileEntityEnchantmentTable)te, x, y, z, partialTicks, destroyStage);
    }
}
