// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model;

import net.minecraft.util.EnumFacing;
import java.util.Arrays;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class BreakingFour extends BakedQuad
{
    private final TextureAtlasSprite texture;
    private static final String __OBFID = "CL_00002492";
    
    public BreakingFour(final BakedQuad p_i46217_1_, final TextureAtlasSprite textureIn) {
        super(Arrays.copyOf(p_i46217_1_.getVertexData(), p_i46217_1_.getVertexData().length), p_i46217_1_.tintIndex, FaceBakery.getFacingFromVertexData(p_i46217_1_.getVertexData()));
        this.texture = textureIn;
        this.func_178217_e();
    }
    
    private void func_178217_e() {
        for (int i = 0; i < 4; ++i) {
            this.func_178216_a(i);
        }
    }
    
    private void func_178216_a(final int p_178216_1_) {
        final int i = this.vertexData.length / 4;
        final int j = i * p_178216_1_;
        final float f = Float.intBitsToFloat(this.vertexData[j]);
        final float f2 = Float.intBitsToFloat(this.vertexData[j + 1]);
        final float f3 = Float.intBitsToFloat(this.vertexData[j + 2]);
        float f4 = 0.0f;
        float f5 = 0.0f;
        switch (BreakingFour$1.field_178419_a[this.face.ordinal()]) {
            case 1: {
                f4 = f * 16.0f;
                f5 = (1.0f - f3) * 16.0f;
                break;
            }
            case 2: {
                f4 = f * 16.0f;
                f5 = f3 * 16.0f;
                break;
            }
            case 3: {
                f4 = (1.0f - f) * 16.0f;
                f5 = (1.0f - f2) * 16.0f;
                break;
            }
            case 4: {
                f4 = f * 16.0f;
                f5 = (1.0f - f2) * 16.0f;
                break;
            }
            case 5: {
                f4 = f3 * 16.0f;
                f5 = (1.0f - f2) * 16.0f;
                break;
            }
            case 6: {
                f4 = (1.0f - f3) * 16.0f;
                f5 = (1.0f - f2) * 16.0f;
                break;
            }
        }
        this.vertexData[j + 4] = Float.floatToRawIntBits(this.texture.getInterpolatedU(f4));
        this.vertexData[j + 4 + 1] = Float.floatToRawIntBits(this.texture.getInterpolatedV(f5));
    }
    
    static final class BreakingFour$1
    {
        static final int[] field_178419_a;
        private static final String __OBFID = "CL_00002491";
        
        static {
            field_178419_a = new int[EnumFacing.values().length];
            try {
                BreakingFour$1.field_178419_a[EnumFacing.DOWN.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                BreakingFour$1.field_178419_a[EnumFacing.UP.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                BreakingFour$1.field_178419_a[EnumFacing.NORTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                BreakingFour$1.field_178419_a[EnumFacing.SOUTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            try {
                BreakingFour$1.field_178419_a[EnumFacing.WEST.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
            try {
                BreakingFour$1.field_178419_a[EnumFacing.EAST.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError6) {}
        }
    }
}
