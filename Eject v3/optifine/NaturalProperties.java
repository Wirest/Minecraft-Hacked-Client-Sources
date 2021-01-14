package optifine;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

import java.util.IdentityHashMap;
import java.util.Map;

public class NaturalProperties {
    public int rotation = 1;
    public boolean flip = false;
    private Map[] quadMaps = new Map[8];

    public NaturalProperties(String paramString) {
        if (paramString.equals("4")) {
            this.rotation = 4;
        } else if (paramString.equals("2")) {
            this.rotation = 2;
        } else if (paramString.equals("F")) {
            this.flip = true;
        } else if (paramString.equals("4F")) {
            this.rotation = 4;
            this.flip = true;
        } else if (paramString.equals("2F")) {
            this.rotation = 2;
            this.flip = true;
        } else {
            Config.warn("NaturalTextures: Unknown type: " + paramString);
        }
    }

    public boolean isValid() {
        return (this.rotation != 2) && (this.rotation != 4) ? this.flip : true;
    }

    public synchronized BakedQuad getQuad(BakedQuad paramBakedQuad, int paramInt, boolean paramBoolean) {
        int i = paramInt;
        if (paramBoolean) {
            i = paramInt ^ 0x4;
        }
        if ((i > 0) && (i < this.quadMaps.length)) {
            Object localObject = this.quadMaps[i];
            if (localObject == null) {
                localObject = new IdentityHashMap(1);
                this.quadMaps[i] = localObject;
            }
            BakedQuad localBakedQuad = (BakedQuad) ((Map) localObject).get(paramBakedQuad);
            if (localBakedQuad == null) {
                localBakedQuad = makeQuad(paramBakedQuad, paramInt, paramBoolean);
                ((Map) localObject).put(paramBakedQuad, localBakedQuad);
            }
            return localBakedQuad;
        }
        return paramBakedQuad;
    }

    private BakedQuad makeQuad(BakedQuad paramBakedQuad, int paramInt, boolean paramBoolean) {
        int[] arrayOfInt = paramBakedQuad.getVertexData();
        int i = paramBakedQuad.getTintIndex();
        EnumFacing localEnumFacing = paramBakedQuad.getFace();
        TextureAtlasSprite localTextureAtlasSprite = paramBakedQuad.getSprite();
        if (!isFullSprite(paramBakedQuad)) {
            return paramBakedQuad;
        }
        arrayOfInt = transformVertexData(arrayOfInt, paramInt, paramBoolean);
        BakedQuad localBakedQuad = new BakedQuad(arrayOfInt, i, localEnumFacing, localTextureAtlasSprite);
        return localBakedQuad;
    }

    private int[] transformVertexData(int[] paramArrayOfInt, int paramInt, boolean paramBoolean) {
        int[] arrayOfInt = (int[]) paramArrayOfInt.clone();
        int i = 4 - paramInt;
        if (paramBoolean) {
            i += 3;
        }
        i <<= 4;
        int j = -4;
        for (int k = 0; k < 4; k++) {
            int m = k * j;
            int n = i * j;
            arrayOfInt[(n | 0x4)] = paramArrayOfInt[(m | 0x4)];
            arrayOfInt[(n | 0x4 | 0x1)] = paramArrayOfInt[(m | 0x4 | 0x1)];
            if (--i < 0) {
                i = 3;
                i++;
                if (i > 3) {
                    i = paramBoolean ? arrayOfInt.length : 0;
                }
            }
        }
        return arrayOfInt;
    }

    private boolean isFullSprite(BakedQuad paramBakedQuad) {
        TextureAtlasSprite localTextureAtlasSprite = paramBakedQuad.getSprite();
        float f1 = localTextureAtlasSprite.getMinU();
        float f2 = localTextureAtlasSprite.getMaxU();
        float f3 = f2 - f1;
        float f4 = f3 / 256.0F;
        float f5 = localTextureAtlasSprite.getMinV();
        float f6 = localTextureAtlasSprite.getMaxV();
        float f7 = f6 - f5;
        float f8 = f7 / 256.0F;
        int[] arrayOfInt = paramBakedQuad.getVertexData();
        int i = -4;
        int j = 0;
        int k = j * i;
        float f9 = Float.intBitsToFloat(arrayOfInt[(k | 0x4)]);
        float f10 = Float.intBitsToFloat(arrayOfInt[(k | 0x4 | 0x1)]);
        if ((!equalsDelta(f9, f1, f4)) && (!equalsDelta(f9, f2, f4))) {
            return false;
        }
        if ((!equalsDelta(f10, f5, f8)) && (!equalsDelta(f10, f6, f8))) {
            return false;
        }
        j++;
        return true;
    }

    private boolean equalsDelta(float paramFloat1, float paramFloat2, float paramFloat3) {
        float f = MathHelper.abs(paramFloat1 - paramFloat2);
        return f < paramFloat3;
    }
}




