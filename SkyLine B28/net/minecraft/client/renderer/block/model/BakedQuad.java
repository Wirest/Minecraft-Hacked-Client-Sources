package net.minecraft.client.renderer.block.model;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.optifine.Reflector;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.IVertexProducer;

public class BakedQuad implements IVertexProducer
{
    protected final int[] field_178215_a;
    protected final int field_178213_b;
    protected final EnumFacing face;
    private TextureAtlasSprite sprite = null;
    private int[] vertexDataSingle = null;

    public BakedQuad(int[] p_i46232_1_, int p_i46232_2_, EnumFacing p_i46232_3_, TextureAtlasSprite sprite)
    {
        this.field_178215_a = p_i46232_1_;
        this.field_178213_b = p_i46232_2_;
        this.face = p_i46232_3_;
        this.sprite = sprite;
    }

    public TextureAtlasSprite getSprite()
    {
        return this.sprite;
    }

    public String toString()
    {
        return "vertex: " + this.field_178215_a.length / 7 + ", tint: " + this.field_178213_b + ", facing: " + this.face + ", sprite: " + this.sprite;
    }

    public BakedQuad(int[] p_i46232_1_, int p_i46232_2_, EnumFacing p_i46232_3_)
    {
        this.field_178215_a = p_i46232_1_;
        this.field_178213_b = p_i46232_2_;
        this.face = p_i46232_3_;
    }

    public int[] func_178209_a()
    {
        return this.field_178215_a;
    }

    public boolean func_178212_b()
    {
        return this.field_178213_b != -1;
    }

    public int func_178211_c()
    {
        return this.field_178213_b;
    }

    public EnumFacing getFace()
    {
        return this.face;
    }

    public int[] getVertexDataSingle()
    {
        if (this.vertexDataSingle == null)
        {
            this.vertexDataSingle = makeVertexDataSingle(this.field_178215_a, this.sprite);
        }

        return this.vertexDataSingle;
    }

    private static int[] makeVertexDataSingle(int[] vd, TextureAtlasSprite sprite)
    {
        int[] vdSingle = (int[])vd.clone();
        int ku = sprite.sheetWidth / sprite.getIconWidth();
        int kv = sprite.sheetHeight / sprite.getIconHeight();
        int step = vdSingle.length / 4;

        for (int i = 0; i < 4; ++i)
        {
            int pos = i * step;
            float tu = Float.intBitsToFloat(vdSingle[pos + 4]);
            float tv = Float.intBitsToFloat(vdSingle[pos + 4 + 1]);
            float u = sprite.toSingleU(tu);
            float v = sprite.toSingleV(tv);
            vdSingle[pos + 4] = Float.floatToRawIntBits(u);
            vdSingle[pos + 4 + 1] = Float.floatToRawIntBits(v);
        }

        return vdSingle;
    }

    public void pipe(IVertexConsumer consumer)
    {
        Reflector.callVoid(Reflector.LightUtil_putBakedQuad, new Object[] {consumer, this});
    }
}
