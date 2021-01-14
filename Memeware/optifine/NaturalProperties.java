package optifine;

import java.util.IdentityHashMap;
import java.util.Map;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class NaturalProperties {
    public int rotation = 1;
    public boolean flip = false;
    private Map[] quadMaps = new Map[8];

    public NaturalProperties(String type) {
        if (type.equals("4")) {
            this.rotation = 4;
        } else if (type.equals("2")) {
            this.rotation = 2;
        } else if (type.equals("F")) {
            this.flip = true;
        } else if (type.equals("4F")) {
            this.rotation = 4;
            this.flip = true;
        } else if (type.equals("2F")) {
            this.rotation = 2;
            this.flip = true;
        } else {
            Config.warn("NaturalTextures: Unknown type: " + type);
        }
    }

    public boolean isValid() {
        return this.rotation != 2 && this.rotation != 4 ? this.flip : true;
    }

    public synchronized BakedQuad getQuad(BakedQuad quadIn, int rotate, boolean flipU) {
        int index = rotate;

        if (flipU) {
            index = rotate | 4;
        }

        if (index > 0 && index < this.quadMaps.length) {
            Object map = this.quadMaps[index];

            if (map == null) {
                map = new IdentityHashMap(1);
                this.quadMaps[index] = (Map) map;
            }

            BakedQuad quad = (BakedQuad) ((Map) map).get(quadIn);

            if (quad == null) {
                quad = this.makeQuad(quadIn, rotate, flipU);
                ((Map) map).put(quadIn, quad);
            }

            return quad;
        } else {
            return quadIn;
        }
    }

    private BakedQuad makeQuad(BakedQuad quad, int rotate, boolean flipU) {
        int[] vertexData = quad.func_178209_a();
        int tintIndex = quad.func_178211_c();
        EnumFacing face = quad.getFace();
        TextureAtlasSprite sprite = quad.getSprite();

        if (!this.isFullSprite(quad)) {
            return quad;
        } else {
            vertexData = this.transformVertexData(vertexData, rotate, flipU);
            BakedQuad bq = new BakedQuad(vertexData, tintIndex, face, sprite);
            return bq;
        }
    }

    private int[] transformVertexData(int[] vertexData, int rotate, boolean flipU) {
        int[] vertexData2 = (int[]) vertexData.clone();
        int v2 = 4 - rotate;

        if (flipU) {
            v2 += 3;
        }

        v2 %= 4;
        int step = vertexData2.length / 4;

        for (int v = 0; v < 4; ++v) {
            int pos = v * step;
            int pos2 = v2 * step;
            vertexData2[pos2 + 4] = vertexData[pos + 4];
            vertexData2[pos2 + 4 + 1] = vertexData[pos + 4 + 1];

            if (flipU) {
                --v2;

                if (v2 < 0) {
                    v2 = 3;
                }
            } else {
                ++v2;

                if (v2 > 3) {
                    v2 = 0;
                }
            }
        }

        return vertexData2;
    }

    private boolean isFullSprite(BakedQuad quad) {
        TextureAtlasSprite sprite = quad.getSprite();
        float uMin = sprite.getMinU();
        float uMax = sprite.getMaxU();
        float uSize = uMax - uMin;
        float uDelta = uSize / 256.0F;
        float vMin = sprite.getMinV();
        float vMax = sprite.getMaxV();
        float vSize = vMax - vMin;
        float vDelta = vSize / 256.0F;
        int[] vertexData = quad.func_178209_a();
        int step = vertexData.length / 4;

        for (int i = 0; i < 4; ++i) {
            int pos = i * step;
            float u = Float.intBitsToFloat(vertexData[pos + 4]);
            float v = Float.intBitsToFloat(vertexData[pos + 4 + 1]);

            if (!this.equalsDelta(u, uMin, uDelta) && !this.equalsDelta(u, uMax, uDelta)) {
                return false;
            }

            if (!this.equalsDelta(v, vMin, vDelta) && !this.equalsDelta(v, vMax, vDelta)) {
                return false;
            }
        }

        return true;
    }

    private boolean equalsDelta(float x1, float x2, float deltaMax) {
        float deltaAbs = MathHelper.abs(x1 - x2);
        return deltaAbs < deltaMax;
    }
}
