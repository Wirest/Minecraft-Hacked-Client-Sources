// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model;

import optifine.Config;
import net.minecraft.client.Minecraft;
import optifine.Reflector;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.IVertexProducer;

public class BakedQuad implements IVertexProducer
{
    protected int[] vertexData;
    protected final int tintIndex;
    protected final EnumFacing face;
    private static final String __OBFID = "CL_00002512";
    private TextureAtlasSprite sprite;
    private int[] vertexDataSingle;
    
    public BakedQuad(final int[] p_i9_1_, final int p_i9_2_, final EnumFacing p_i9_3_, final TextureAtlasSprite p_i9_4_) {
        this.sprite = null;
        this.vertexDataSingle = null;
        this.vertexData = p_i9_1_;
        this.tintIndex = p_i9_2_;
        this.face = p_i9_3_;
        this.sprite = p_i9_4_;
        this.fixVertexData();
    }
    
    public TextureAtlasSprite getSprite() {
        if (this.sprite == null) {
            this.sprite = getSpriteByUv(this.getVertexData());
        }
        return this.sprite;
    }
    
    @Override
    public String toString() {
        return "vertex: " + this.vertexData.length / 7 + ", tint: " + this.tintIndex + ", facing: " + this.face + ", sprite: " + this.sprite;
    }
    
    public BakedQuad(final int[] vertexDataIn, final int tintIndexIn, final EnumFacing faceIn) {
        this.sprite = null;
        this.vertexDataSingle = null;
        this.vertexData = vertexDataIn;
        this.tintIndex = tintIndexIn;
        this.face = faceIn;
        this.fixVertexData();
    }
    
    public int[] getVertexData() {
        this.fixVertexData();
        return this.vertexData;
    }
    
    public boolean hasTintIndex() {
        return this.tintIndex != -1;
    }
    
    public int getTintIndex() {
        return this.tintIndex;
    }
    
    public EnumFacing getFace() {
        return this.face;
    }
    
    public int[] getVertexDataSingle() {
        if (this.vertexDataSingle == null) {
            this.vertexDataSingle = makeVertexDataSingle(this.getVertexData(), this.getSprite());
        }
        return this.vertexDataSingle;
    }
    
    private static int[] makeVertexDataSingle(final int[] p_makeVertexDataSingle_0_, final TextureAtlasSprite p_makeVertexDataSingle_1_) {
        final int[] aint = p_makeVertexDataSingle_0_.clone();
        final int i = p_makeVertexDataSingle_1_.sheetWidth / p_makeVertexDataSingle_1_.getIconWidth();
        final int j = p_makeVertexDataSingle_1_.sheetHeight / p_makeVertexDataSingle_1_.getIconHeight();
        final int k = aint.length / 4;
        for (int l = 0; l < 4; ++l) {
            final int i2 = l * k;
            final float f = Float.intBitsToFloat(aint[i2 + 4]);
            final float f2 = Float.intBitsToFloat(aint[i2 + 4 + 1]);
            final float f3 = p_makeVertexDataSingle_1_.toSingleU(f);
            final float f4 = p_makeVertexDataSingle_1_.toSingleV(f2);
            aint[i2 + 4] = Float.floatToRawIntBits(f3);
            aint[i2 + 4 + 1] = Float.floatToRawIntBits(f4);
        }
        return aint;
    }
    
    @Override
    public void pipe(final IVertexConsumer p_pipe_1_) {
        Reflector.callVoid(Reflector.LightUtil_putBakedQuad, p_pipe_1_, this);
    }
    
    private static TextureAtlasSprite getSpriteByUv(final int[] p_getSpriteByUv_0_) {
        float f = 1.0f;
        float f2 = 1.0f;
        float f3 = 0.0f;
        float f4 = 0.0f;
        final int i = p_getSpriteByUv_0_.length / 4;
        for (int j = 0; j < 4; ++j) {
            final int k = j * i;
            final float f5 = Float.intBitsToFloat(p_getSpriteByUv_0_[k + 4]);
            final float f6 = Float.intBitsToFloat(p_getSpriteByUv_0_[k + 4 + 1]);
            f = Math.min(f, f5);
            f2 = Math.min(f2, f6);
            f3 = Math.max(f3, f5);
            f4 = Math.max(f4, f6);
        }
        final float f7 = (f + f3) / 2.0f;
        final float f8 = (f2 + f4) / 2.0f;
        final TextureAtlasSprite textureatlassprite = Minecraft.getMinecraft().getTextureMapBlocks().getIconByUV(f7, f8);
        return textureatlassprite;
    }
    
    private void fixVertexData() {
        if (Config.isShaders()) {
            if (this.vertexData.length == 28) {
                this.vertexData = expandVertexData(this.vertexData);
            }
        }
        else if (this.vertexData.length == 56) {
            this.vertexData = compactVertexData(this.vertexData);
        }
    }
    
    private static int[] expandVertexData(final int[] p_expandVertexData_0_) {
        final int i = p_expandVertexData_0_.length / 4;
        final int j = i * 2;
        final int[] aint = new int[j * 4];
        for (int k = 0; k < 4; ++k) {
            System.arraycopy(p_expandVertexData_0_, k * i, aint, k * j, i);
        }
        return aint;
    }
    
    private static int[] compactVertexData(final int[] p_compactVertexData_0_) {
        final int i = p_compactVertexData_0_.length / 4;
        final int j = i / 2;
        final int[] aint = new int[j * 4];
        for (int k = 0; k < 4; ++k) {
            System.arraycopy(p_compactVertexData_0_, k * i, aint, k * j, j);
        }
        return aint;
    }
}
