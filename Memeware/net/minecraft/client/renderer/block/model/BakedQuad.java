package net.minecraft.client.renderer.block.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.IVertexProducer;
import optifine.Config;
import optifine.Reflector;

public class BakedQuad implements IVertexProducer {
    protected int[] field_178215_a;
    protected final int field_178213_b;
    protected final EnumFacing face;
    private static final String __OBFID = "CL_00002512";
    private TextureAtlasSprite sprite = null;
    private int[] vertexDataSingle = null;

    public BakedQuad(int[] p_i46232_1_, int p_i46232_2_, EnumFacing p_i46232_3_, TextureAtlasSprite sprite) {
        this.field_178215_a = p_i46232_1_;
        this.field_178213_b = p_i46232_2_;
        this.face = p_i46232_3_;
        this.sprite = sprite;
        this.fixVertexData();
    }

    public TextureAtlasSprite getSprite() {
        if (this.sprite == null) {
            this.sprite = getSpriteByUv(this.func_178209_a());
        }

        return this.sprite;
    }

    public String toString() {
        return "vertex: " + this.field_178215_a.length / 7 + ", tint: " + this.field_178213_b + ", facing: " + this.face + ", sprite: " + this.sprite;
    }

    public BakedQuad(int[] p_i46232_1_, int p_i46232_2_, EnumFacing p_i46232_3_) {
        this.field_178215_a = p_i46232_1_;
        this.field_178213_b = p_i46232_2_;
        this.face = p_i46232_3_;
        this.fixVertexData();
    }

    public int[] func_178209_a() {
        this.fixVertexData();
        return this.field_178215_a;
    }

    public boolean func_178212_b() {
        return this.field_178213_b != -1;
    }

    public int func_178211_c() {
        return this.field_178213_b;
    }

    public EnumFacing getFace() {
        return this.face;
    }

    public int[] getVertexDataSingle() {
        if (this.vertexDataSingle == null) {
            this.vertexDataSingle = makeVertexDataSingle(this.func_178209_a(), this.getSprite());
        }

        return this.vertexDataSingle;
    }

    private static int[] makeVertexDataSingle(int[] vd, TextureAtlasSprite sprite) {
        int[] vdSingle = (int[]) vd.clone();
        int ku = sprite.sheetWidth / sprite.getIconWidth();
        int kv = sprite.sheetHeight / sprite.getIconHeight();
        int step = vdSingle.length / 4;

        for (int i = 0; i < 4; ++i) {
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

    public void pipe(IVertexConsumer consumer) {
        Reflector.callVoid(Reflector.LightUtil_putBakedQuad, new Object[]{consumer, this});
    }

    private static TextureAtlasSprite getSpriteByUv(int[] vertexData) {
        float uMin = 1.0F;
        float vMin = 1.0F;
        float uMax = 0.0F;
        float vMax = 0.0F;
        int step = vertexData.length / 4;

        for (int uMid = 0; uMid < 4; ++uMid) {
            int vMid = uMid * step;
            float spriteUv = Float.intBitsToFloat(vertexData[vMid + 4]);
            float tv = Float.intBitsToFloat(vertexData[vMid + 4 + 1]);
            uMin = Math.min(uMin, spriteUv);
            vMin = Math.min(vMin, tv);
            uMax = Math.max(uMax, spriteUv);
            vMax = Math.max(vMax, tv);
        }

        float var10 = (uMin + uMax) / 2.0F;
        float var11 = (vMin + vMax) / 2.0F;
        TextureAtlasSprite var12 = Minecraft.getMinecraft().getTextureMapBlocks().getIconByUV((double) var10, (double) var11);
        return var12;
    }

    private void fixVertexData() {
        if (Config.isShaders()) {
            if (this.field_178215_a.length == 28) {
                this.field_178215_a = expandVertexData(this.field_178215_a);
            }
        } else if (this.field_178215_a.length == 56) {
            this.field_178215_a = compactVertexData(this.field_178215_a);
        }
    }

    private static int[] expandVertexData(int[] vd) {
        int step = vd.length / 4;
        int stepNew = step * 2;
        int[] vdNew = new int[stepNew * 4];

        for (int i = 0; i < 4; ++i) {
            System.arraycopy(vd, i * step, vdNew, i * stepNew, step);
        }

        return vdNew;
    }

    private static int[] compactVertexData(int[] vd) {
        int step = vd.length / 4;
        int stepNew = step / 2;
        int[] vdNew = new int[stepNew * 4];

        for (int i = 0; i < 4; ++i) {
            System.arraycopy(vd, i * step, vdNew, i * stepNew, stepNew);
        }

        return vdNew;
    }
}
