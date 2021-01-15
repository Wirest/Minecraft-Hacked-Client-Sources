package net.minecraft.src;

import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class NaturalProperties
{
    public int rotation = 1;
    public boolean flip = false;
    private Map[] quadMaps = new Map[8];

    public NaturalProperties(String type)
    {
        if (type.equals("4"))
        {
            this.rotation = 4;
        }
        else if (type.equals("2"))
        {
            this.rotation = 2;
        }
        else if (type.equals("F"))
        {
            this.flip = true;
        }
        else if (type.equals("4F"))
        {
            this.rotation = 4;
            this.flip = true;
        }
        else if (type.equals("2F"))
        {
            this.rotation = 2;
            this.flip = true;
        }
        else
        {
            Config.warn("NaturalTextures: Unknown type: " + type);
        }
    }

    public boolean isValid()
    {
        return this.rotation != 2 && this.rotation != 4 ? this.flip : true;
    }

    public synchronized BakedQuad getQuad(BakedQuad quadIn, int rotate, boolean flipU)
    {
        int index = rotate;

        if (flipU)
        {
            index = rotate | 4;
        }

        if (index > 0 && index < this.quadMaps.length)
        {
            Object map = this.quadMaps[index];

            if (map == null)
            {
                map = new IdentityHashMap(1);
                this.quadMaps[index] = (Map)map;
            }

            BakedQuad quad = (BakedQuad)((Map)map).get(quadIn);

            if (quad == null)
            {
                quad = this.makeQuad(quadIn, rotate, flipU);
                ((Map)map).put(quadIn, quad);
            }

            return quad;
        }
        else
        {
            return quadIn;
        }
    }

    private BakedQuad makeQuad(BakedQuad quad, int rotate, boolean flipU)
    {
        int[] vertexData = quad.func_178209_a();
        int tintIndex = quad.func_178211_c();
        EnumFacing face = quad.getFace();
        TextureAtlasSprite sprite = quad.getSprite();
        vertexData = this.fixVertexData(vertexData, rotate, flipU);
        BakedQuad bq = new BakedQuad(vertexData, tintIndex, face, sprite);
        return bq;
    }

    private int[] fixVertexData(int[] vertexData, int rotate, boolean flipU)
    {
        int[] vertexData2 = new int[vertexData.length];
        int v2;

        for (v2 = 0; v2 < vertexData.length; ++v2)
        {
            vertexData2[v2] = vertexData[v2];
        }

        v2 = 4 - rotate;

        if (flipU)
        {
            v2 += 3;
        }

        v2 %= 4;

        for (int v = 0; v < 4; ++v)
        {
            int pos = v * 7;
            int pos2 = v2 * 7;
            vertexData2[pos2 + 4] = vertexData[pos + 4];
            vertexData2[pos2 + 4 + 1] = vertexData[pos + 4 + 1];

            if (flipU)
            {
                --v2;

                if (v2 < 0)
                {
                    v2 = 3;
                }
            }
            else
            {
                ++v2;

                if (v2 > 3)
                {
                    v2 = 0;
                }
            }
        }

        return vertexData2;
    }
}
