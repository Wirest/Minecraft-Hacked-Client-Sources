// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import java.util.IdentityHashMap;
import net.minecraft.client.renderer.block.model.BakedQuad;
import java.util.Map;

public class NaturalProperties
{
    public int rotation;
    public boolean flip;
    private Map[] quadMaps;
    
    public NaturalProperties(final String p_i68_1_) {
        this.rotation = 1;
        this.flip = false;
        this.quadMaps = new Map[8];
        if (p_i68_1_.equals("4")) {
            this.rotation = 4;
        }
        else if (p_i68_1_.equals("2")) {
            this.rotation = 2;
        }
        else if (p_i68_1_.equals("F")) {
            this.flip = true;
        }
        else if (p_i68_1_.equals("4F")) {
            this.rotation = 4;
            this.flip = true;
        }
        else if (p_i68_1_.equals("2F")) {
            this.rotation = 2;
            this.flip = true;
        }
        else {
            Config.warn("NaturalTextures: Unknown type: " + p_i68_1_);
        }
    }
    
    public boolean isValid() {
        return this.rotation == 2 || this.rotation == 4 || this.flip;
    }
    
    public synchronized BakedQuad getQuad(final BakedQuad p_getQuad_1_, final int p_getQuad_2_, final boolean p_getQuad_3_) {
        int i = p_getQuad_2_;
        if (p_getQuad_3_) {
            i = (p_getQuad_2_ | 0x4);
        }
        if (i > 0 && i < this.quadMaps.length) {
            Map map = this.quadMaps[i];
            if (map == null) {
                map = new IdentityHashMap(1);
                this.quadMaps[i] = map;
            }
            BakedQuad bakedquad = map.get(p_getQuad_1_);
            if (bakedquad == null) {
                bakedquad = this.makeQuad(p_getQuad_1_, p_getQuad_2_, p_getQuad_3_);
                map.put(p_getQuad_1_, bakedquad);
            }
            return bakedquad;
        }
        return p_getQuad_1_;
    }
    
    private BakedQuad makeQuad(final BakedQuad p_makeQuad_1_, final int p_makeQuad_2_, final boolean p_makeQuad_3_) {
        int[] aint = p_makeQuad_1_.getVertexData();
        final int i = p_makeQuad_1_.getTintIndex();
        final EnumFacing enumfacing = p_makeQuad_1_.getFace();
        final TextureAtlasSprite textureatlassprite = p_makeQuad_1_.getSprite();
        if (!this.isFullSprite(p_makeQuad_1_)) {
            return p_makeQuad_1_;
        }
        aint = this.transformVertexData(aint, p_makeQuad_2_, p_makeQuad_3_);
        final BakedQuad bakedquad = new BakedQuad(aint, i, enumfacing, textureatlassprite);
        return bakedquad;
    }
    
    private int[] transformVertexData(final int[] p_transformVertexData_1_, final int p_transformVertexData_2_, final boolean p_transformVertexData_3_) {
        final int[] aint = p_transformVertexData_1_.clone();
        int i = 4 - p_transformVertexData_2_;
        if (p_transformVertexData_3_) {
            i += 3;
        }
        i %= 4;
        final int j = aint.length / 4;
        for (int k = 0; k < 4; ++k) {
            final int l = k * j;
            final int i2 = i * j;
            aint[i2 + 4] = p_transformVertexData_1_[l + 4];
            aint[i2 + 4 + 1] = p_transformVertexData_1_[l + 4 + 1];
            if (p_transformVertexData_3_) {
                if (--i < 0) {
                    i = 3;
                }
            }
            else if (++i > 3) {
                i = 0;
            }
        }
        return aint;
    }
    
    private boolean isFullSprite(final BakedQuad p_isFullSprite_1_) {
        final TextureAtlasSprite textureatlassprite = p_isFullSprite_1_.getSprite();
        final float f = textureatlassprite.getMinU();
        final float f2 = textureatlassprite.getMaxU();
        final float f3 = f2 - f;
        final float f4 = f3 / 256.0f;
        final float f5 = textureatlassprite.getMinV();
        final float f6 = textureatlassprite.getMaxV();
        final float f7 = f6 - f5;
        final float f8 = f7 / 256.0f;
        final int[] aint = p_isFullSprite_1_.getVertexData();
        final int i = aint.length / 4;
        for (int j = 0; j < 4; ++j) {
            final int k = j * i;
            final float f9 = Float.intBitsToFloat(aint[k + 4]);
            final float f10 = Float.intBitsToFloat(aint[k + 4 + 1]);
            if (!this.equalsDelta(f9, f, f4) && !this.equalsDelta(f9, f2, f4)) {
                return false;
            }
            if (!this.equalsDelta(f10, f5, f8) && !this.equalsDelta(f10, f6, f8)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean equalsDelta(final float p_equalsDelta_1_, final float p_equalsDelta_2_, final float p_equalsDelta_3_) {
        final float f = MathHelper.abs(p_equalsDelta_1_ - p_equalsDelta_2_);
        return f < p_equalsDelta_3_;
    }
}
