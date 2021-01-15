package shadersmod.client;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class SVertexBuilder
{
    int vertexSize;
    int offsetNormal;
    int offsetUV;
    int offsetUVCenter;
    boolean hasNormal;
    boolean hasTangent;
    boolean hasUV;
    boolean hasUVCenter;
    long[] entityData = new long[10];
    int entityDataIndex = 0;

    public SVertexBuilder()
    {
        this.entityData[this.entityDataIndex] = 0L;
    }

    public static void initVertexBuilder(WorldRenderer wrr)
    {
        wrr.sVertexBuilder = new SVertexBuilder();
    }

    public void pushEntity(long data)
    {
        ++this.entityDataIndex;
        this.entityData[this.entityDataIndex] = data;
    }

    public void popEntity()
    {
        this.entityData[this.entityDataIndex] = 0L;
        --this.entityDataIndex;
    }

    public static void pushEntity(IBlockState blockState, BlockPos blockPos, IBlockAccess blockAccess, WorldRenderer wrr)
    {
        Block block = blockState.getBlock();
        int blockID = Block.getIdFromBlock(block);
        int renderType = block.getRenderType();
        int meta = block.getMetaFromState(blockState);
        int dataLo = ((renderType & 65535) << 16) + (blockID & 65535);
        int dataHi = meta & 65535;
        wrr.sVertexBuilder.pushEntity(((long)dataHi << 32) + (long)dataLo);
    }

    public static void popEntity(WorldRenderer wrr)
    {
        wrr.sVertexBuilder.popEntity();
    }

    public static boolean popEntity(boolean value, WorldRenderer wrr)
    {
        wrr.sVertexBuilder.popEntity();
        return value;
    }

    public static void endSetVertexFormat(WorldRenderer wrr)
    {
        SVertexBuilder svb = wrr.sVertexBuilder;
        VertexFormat vf = wrr.func_178973_g();
        svb.vertexSize = vf.func_177338_f() / 4;
        svb.hasNormal = vf.func_177350_b();
        svb.hasTangent = svb.hasNormal;
        svb.hasUV = vf.func_177347_a(0);
        svb.offsetNormal = svb.hasNormal ? vf.func_177342_c() / 4 : 0;
        svb.offsetUV = svb.hasUV ? vf.func_177344_b(0) / 4 : 0;
        svb.offsetUVCenter = 8;
    }

    public static void beginAddVertex(WorldRenderer wrr)
    {
        if (wrr.vertexCount == 0)
        {
            endSetVertexFormat(wrr);
        }
    }

    public static void endAddVertex(WorldRenderer wrr)
    {
        SVertexBuilder svb = wrr.sVertexBuilder;

        if (svb.vertexSize == 14)
        {
            if (wrr.drawMode == 7 && wrr.vertexCount % 4 == 0)
            {
                svb.calcNormal(wrr, wrr.rawBufferIndex - 4 * svb.vertexSize);
            }

            long eData = svb.entityData[svb.entityDataIndex];
            int pos = wrr.rawBufferIndex - 14 + 12;
            wrr.rawIntBuffer.put(pos, (int)eData);
            wrr.rawIntBuffer.put(pos + 1, (int)(eData >> 32));
        }
    }

    public static void beginAddVertexData(WorldRenderer wrr, int[] data)
    {
        if (wrr.vertexCount == 0)
        {
            endSetVertexFormat(wrr);
        }

        SVertexBuilder svb = wrr.sVertexBuilder;

        if (svb.vertexSize == 14)
        {
            long eData = svb.entityData[svb.entityDataIndex];

            for (int pos = 12; pos + 1 < data.length; pos += 14)
            {
                data[pos] = (int)eData;
                data[pos + 1] = (int)(eData >> 32);
            }
        }
    }

    public static void endAddVertexData(WorldRenderer wrr)
    {
        SVertexBuilder svb = wrr.sVertexBuilder;

        if (svb.vertexSize == 14 && wrr.drawMode == 7 && wrr.vertexCount % 4 == 0)
        {
            svb.calcNormal(wrr, wrr.rawBufferIndex - 4 * svb.vertexSize);
        }
    }

    public void calcNormal(WorldRenderer wrr, int baseIndex)
    {
        FloatBuffer floatBuffer = wrr.rawFloatBuffer;
        IntBuffer intBuffer = wrr.rawIntBuffer;
        int rbi = wrr.rawBufferIndex;
        float v0x = floatBuffer.get(baseIndex + 0 * this.vertexSize);
        float v0y = floatBuffer.get(baseIndex + 0 * this.vertexSize + 1);
        float v0z = floatBuffer.get(baseIndex + 0 * this.vertexSize + 2);
        float v0u = floatBuffer.get(baseIndex + 0 * this.vertexSize + this.offsetUV);
        float v0v = floatBuffer.get(baseIndex + 0 * this.vertexSize + this.offsetUV + 1);
        float v1x = floatBuffer.get(baseIndex + 1 * this.vertexSize);
        float v1y = floatBuffer.get(baseIndex + 1 * this.vertexSize + 1);
        float v1z = floatBuffer.get(baseIndex + 1 * this.vertexSize + 2);
        float v1u = floatBuffer.get(baseIndex + 1 * this.vertexSize + this.offsetUV);
        float v1v = floatBuffer.get(baseIndex + 1 * this.vertexSize + this.offsetUV + 1);
        float v2x = floatBuffer.get(baseIndex + 2 * this.vertexSize);
        float v2y = floatBuffer.get(baseIndex + 2 * this.vertexSize + 1);
        float v2z = floatBuffer.get(baseIndex + 2 * this.vertexSize + 2);
        float v2u = floatBuffer.get(baseIndex + 2 * this.vertexSize + this.offsetUV);
        float v2v = floatBuffer.get(baseIndex + 2 * this.vertexSize + this.offsetUV + 1);
        float v3x = floatBuffer.get(baseIndex + 3 * this.vertexSize);
        float v3y = floatBuffer.get(baseIndex + 3 * this.vertexSize + 1);
        float v3z = floatBuffer.get(baseIndex + 3 * this.vertexSize + 2);
        float v3u = floatBuffer.get(baseIndex + 3 * this.vertexSize + this.offsetUV);
        float v3v = floatBuffer.get(baseIndex + 3 * this.vertexSize + this.offsetUV + 1);
        float x1 = v2x - v0x;
        float y1 = v2y - v0y;
        float z1 = v2z - v0z;
        float x2 = v3x - v1x;
        float y2 = v3y - v1y;
        float z2 = v3z - v1z;
        float vnx = y1 * z2 - y2 * z1;
        float vny = z1 * x2 - z2 * x1;
        float vnz = x1 * y2 - x2 * y1;
        float lensq = vnx * vnx + vny * vny + vnz * vnz;
        float mult = (double)lensq != 0.0D ? (float)(1.0D / Math.sqrt((double)lensq)) : 1.0F;
        vnx *= mult;
        vny *= mult;
        vnz *= mult;
        x1 = v1x - v0x;
        y1 = v1y - v0y;
        z1 = v1z - v0z;
        float u1 = v1u - v0u;
        float v1 = v1v - v0v;
        x2 = v2x - v0x;
        y2 = v2y - v0y;
        z2 = v2z - v0z;
        float u2 = v2u - v0u;
        float v2 = v2v - v0v;
        float d = u1 * v2 - u2 * v1;
        float r = d != 0.0F ? 1.0F / d : 1.0F;
        float tan1x = (v2 * x1 - v1 * x2) * r;
        float tan1y = (v2 * y1 - v1 * y2) * r;
        float tan1z = (v2 * z1 - v1 * z2) * r;
        float tan2x = (u1 * x2 - u2 * x1) * r;
        float tan2y = (u1 * y2 - u2 * y1) * r;
        float tan2z = (u1 * z2 - u2 * z1) * r;
        lensq = tan1x * tan1x + tan1y * tan1y + tan1z * tan1z;
        mult = (double)lensq != 0.0D ? (float)(1.0D / Math.sqrt((double)lensq)) : 1.0F;
        tan1x *= mult;
        tan1y *= mult;
        tan1z *= mult;
        lensq = tan2x * tan2x + tan2y * tan2y + tan2z * tan2z;
        mult = (double)lensq != 0.0D ? (float)(1.0D / Math.sqrt((double)lensq)) : 1.0F;
        tan2x *= mult;
        tan2y *= mult;
        tan2z *= mult;
        float tan3x = vnz * tan1y - vny * tan1z;
        float tan3y = vnx * tan1z - vnz * tan1x;
        float tan3z = vny * tan1x - vnx * tan1y;
        float tan1w = tan2x * tan3x + tan2y * tan3y + tan2z * tan3z < 0.0F ? -1.0F : 1.0F;
        int bnx = (int)(vnx * 127.0F) & 255;
        int bny = (int)(vny * 127.0F) & 255;
        int bnz = (int)(vnz * 127.0F) & 255;
        int packedNormal = (bnz << 16) + (bny << 8) + bnx;
        intBuffer.put(baseIndex + 0 * this.vertexSize + this.offsetNormal, packedNormal);
        intBuffer.put(baseIndex + 1 * this.vertexSize + this.offsetNormal, packedNormal);
        intBuffer.put(baseIndex + 2 * this.vertexSize + this.offsetNormal, packedNormal);
        intBuffer.put(baseIndex + 3 * this.vertexSize + this.offsetNormal, packedNormal);
        int packedTan1xy = ((int)(tan1x * 32767.0F) & 65535) + (((int)(tan1y * 32767.0F) & 65535) << 16);
        int packedTan1zw = ((int)(tan1z * 32767.0F) & 65535) + (((int)(tan1w * 32767.0F) & 65535) << 16);
        intBuffer.put(baseIndex + 0 * this.vertexSize + 10, packedTan1xy);
        intBuffer.put(baseIndex + 0 * this.vertexSize + 10 + 1, packedTan1zw);
        intBuffer.put(baseIndex + 1 * this.vertexSize + 10, packedTan1xy);
        intBuffer.put(baseIndex + 1 * this.vertexSize + 10 + 1, packedTan1zw);
        intBuffer.put(baseIndex + 2 * this.vertexSize + 10, packedTan1xy);
        intBuffer.put(baseIndex + 2 * this.vertexSize + 10 + 1, packedTan1zw);
        intBuffer.put(baseIndex + 3 * this.vertexSize + 10, packedTan1xy);
        intBuffer.put(baseIndex + 3 * this.vertexSize + 10 + 1, packedTan1zw);
        float midU = (v0u + v1u + v2u + v3u) / 4.0F;
        float midV = (v0v + v1v + v2v + v3v) / 4.0F;
        floatBuffer.put(baseIndex + 0 * this.vertexSize + 8, midU);
        floatBuffer.put(baseIndex + 0 * this.vertexSize + 8 + 1, midV);
        floatBuffer.put(baseIndex + 1 * this.vertexSize + 8, midU);
        floatBuffer.put(baseIndex + 1 * this.vertexSize + 8 + 1, midV);
        floatBuffer.put(baseIndex + 2 * this.vertexSize + 8, midU);
        floatBuffer.put(baseIndex + 2 * this.vertexSize + 8 + 1, midV);
        floatBuffer.put(baseIndex + 3 * this.vertexSize + 8, midU);
        floatBuffer.put(baseIndex + 3 * this.vertexSize + 8 + 1, midV);
    }

    public static void calcNormalChunkLayer(WorldRenderer wrr)
    {
        if (wrr.func_178973_g().func_177350_b() && wrr.drawMode == 7 && wrr.vertexCount % 4 == 0)
        {
            SVertexBuilder svb = wrr.sVertexBuilder;
            endSetVertexFormat(wrr);
            int indexEnd = wrr.vertexCount * svb.vertexSize;

            for (int index = 0; index < indexEnd; index += svb.vertexSize * 4)
            {
                svb.calcNormal(wrr, index);
            }
        }
    }

    public static void drawArrays(int drawMode, int first, int count, WorldRenderer wrr)
    {
        if (count != 0)
        {
            VertexFormat vf = wrr.func_178973_g();
            int vertexSizeByte = vf.func_177338_f();

            if (vertexSizeByte == 56)
            {
                ByteBuffer bb = wrr.func_178966_f();
                bb.position(32);
                GL20.glVertexAttribPointer(Shaders.midTexCoordAttrib, 2, GL11.GL_FLOAT, false, vertexSizeByte, bb);
                bb.position(40);
                GL20.glVertexAttribPointer(Shaders.tangentAttrib, 4, GL11.GL_SHORT, false, vertexSizeByte, bb);
                bb.position(48);
                GL20.glVertexAttribPointer(Shaders.entityAttrib, 3, GL11.GL_SHORT, false, vertexSizeByte, bb);
                bb.position(0);
                GL20.glEnableVertexAttribArray(Shaders.midTexCoordAttrib);
                GL20.glEnableVertexAttribArray(Shaders.tangentAttrib);
                GL20.glEnableVertexAttribArray(Shaders.entityAttrib);
                GL11.glDrawArrays(drawMode, first, count);
                GL20.glDisableVertexAttribArray(Shaders.midTexCoordAttrib);
                GL20.glDisableVertexAttribArray(Shaders.tangentAttrib);
                GL20.glDisableVertexAttribArray(Shaders.entityAttrib);
            }
            else
            {
                GL11.glDrawArrays(drawMode, first, count);
            }
        }
    }

    public static void startTexturedQuad(WorldRenderer wrr)
    {
        wrr.setVertexFormat(SVertexFormat.defVertexFormatTextured);
    }
}
