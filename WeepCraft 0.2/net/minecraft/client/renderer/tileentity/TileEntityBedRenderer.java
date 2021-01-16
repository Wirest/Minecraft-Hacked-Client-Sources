package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.model.ModelBed;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TileEntityBedRenderer extends TileEntitySpecialRenderer<TileEntityBed>
{
    private static final ResourceLocation[] field_193848_a;
    private ModelBed field_193849_d = new ModelBed();
    private int field_193850_e;

    public TileEntityBedRenderer()
    {
        this.field_193850_e = this.field_193849_d.func_193770_a();
    }

    public void func_192841_a(TileEntityBed p_192841_1_, double p_192841_2_, double p_192841_4_, double p_192841_6_, float p_192841_8_, int p_192841_9_, float p_192841_10_)
    {
        if (this.field_193850_e != this.field_193849_d.func_193770_a())
        {
            this.field_193849_d = new ModelBed();
            this.field_193850_e = this.field_193849_d.func_193770_a();
        }

        boolean flag = p_192841_1_.getWorld() != null;
        boolean flag1 = flag ? p_192841_1_.func_193050_e() : true;
        EnumDyeColor enumdyecolor = p_192841_1_ != null ? p_192841_1_.func_193048_a() : EnumDyeColor.RED;
        int i = flag ? p_192841_1_.getBlockMetadata() & 3 : 0;

        if (p_192841_9_ >= 0)
        {
            this.bindTexture(DESTROY_STAGES[p_192841_9_]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        }
        else
        {
            ResourceLocation resourcelocation = field_193848_a[enumdyecolor.getMetadata()];

            if (resourcelocation != null)
            {
                this.bindTexture(resourcelocation);
            }
        }

        if (flag)
        {
            this.func_193847_a(flag1, p_192841_2_, p_192841_4_, p_192841_6_, i, p_192841_10_);
        }
        else
        {
            GlStateManager.pushMatrix();
            this.func_193847_a(true, p_192841_2_, p_192841_4_, p_192841_6_, i, p_192841_10_);
            this.func_193847_a(false, p_192841_2_, p_192841_4_, p_192841_6_ - 1.0D, i, p_192841_10_);
            GlStateManager.popMatrix();
        }

        if (p_192841_9_ >= 0)
        {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }

    private void func_193847_a(boolean p_193847_1_, double p_193847_2_, double p_193847_4_, double p_193847_6_, int p_193847_8_, float p_193847_9_)
    {
        this.field_193849_d.func_193769_a(p_193847_1_);
        GlStateManager.pushMatrix();
        float f = 0.0F;
        float f1 = 0.0F;
        float f2 = 0.0F;

        if (p_193847_8_ == EnumFacing.NORTH.getHorizontalIndex())
        {
            f = 0.0F;
        }
        else if (p_193847_8_ == EnumFacing.SOUTH.getHorizontalIndex())
        {
            f = 180.0F;
            f1 = 1.0F;
            f2 = 1.0F;
        }
        else if (p_193847_8_ == EnumFacing.WEST.getHorizontalIndex())
        {
            f = -90.0F;
            f2 = 1.0F;
        }
        else if (p_193847_8_ == EnumFacing.EAST.getHorizontalIndex())
        {
            f = 90.0F;
            f1 = 1.0F;
        }

        GlStateManager.translate((float)p_193847_2_ + f1, (float)p_193847_4_ + 0.5625F, (float)p_193847_6_ + f2);
        GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f, 0.0F, 0.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        this.field_193849_d.func_193771_b();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, p_193847_9_);
        GlStateManager.popMatrix();
    }

    static
    {
        EnumDyeColor[] aenumdyecolor = EnumDyeColor.values();
        field_193848_a = new ResourceLocation[aenumdyecolor.length];

        for (EnumDyeColor enumdyecolor : aenumdyecolor)
        {
            field_193848_a[enumdyecolor.getMetadata()] = new ResourceLocation("textures/entity/bed/" + enumdyecolor.func_192396_c() + ".png");
        }
    }
}
