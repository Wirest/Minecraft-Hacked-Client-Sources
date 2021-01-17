// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.WorldRenderer;

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
    long[] entityData;
    int entityDataIndex;
    
    public SVertexBuilder() {
        this.entityData = new long[10];
        this.entityDataIndex = 0;
        this.entityData[this.entityDataIndex] = 0L;
    }
    
    public static void initVertexBuilder(final WorldRenderer wrr) {
        wrr.sVertexBuilder = new SVertexBuilder();
    }
    
    public void pushEntity(final long data) {
        ++this.entityDataIndex;
        this.entityData[this.entityDataIndex] = data;
    }
    
    public void popEntity() {
        this.entityData[this.entityDataIndex] = 0L;
        --this.entityDataIndex;
    }
    
    public static void pushEntity(final IBlockState blockState, final BlockPos blockPos, final IBlockAccess blockAccess, final WorldRenderer wrr) {
        final Block block = blockState.getBlock();
        int i;
        int j;
        if (blockState instanceof BlockStateBase) {
            final BlockStateBase blockstatebase = (BlockStateBase)blockState;
            i = blockstatebase.getBlockId();
            j = blockstatebase.getMetadata();
        }
        else {
            i = Block.getIdFromBlock(block);
            j = block.getMetaFromState(blockState);
        }
        i = BlockAliases.getMappedBlockId(i, j);
        final int i2 = block.getRenderType();
        final int k = ((i2 & 0xFFFF) << 16) + (i & 0xFFFF);
        final int l = j & 0xFFFF;
        wrr.sVertexBuilder.pushEntity(((long)l << 32) + k);
    }
    
    public static void popEntity(final WorldRenderer wrr) {
        wrr.sVertexBuilder.popEntity();
    }
    
    public static boolean popEntity(final boolean value, final WorldRenderer wrr) {
        wrr.sVertexBuilder.popEntity();
        return value;
    }
    
    public static void endSetVertexFormat(final WorldRenderer wrr) {
        final SVertexBuilder svertexbuilder = wrr.sVertexBuilder;
        final VertexFormat vertexformat = wrr.getVertexFormat();
        svertexbuilder.vertexSize = vertexformat.getNextOffset() / 4;
        svertexbuilder.hasNormal = vertexformat.hasNormal();
        svertexbuilder.hasTangent = svertexbuilder.hasNormal;
        svertexbuilder.hasUV = vertexformat.hasUvOffset(0);
        svertexbuilder.offsetNormal = (svertexbuilder.hasNormal ? (vertexformat.getNormalOffset() / 4) : 0);
        svertexbuilder.offsetUV = (svertexbuilder.hasUV ? (vertexformat.getUvOffsetById(0) / 4) : 0);
        svertexbuilder.offsetUVCenter = 8;
    }
    
    public static void beginAddVertex(final WorldRenderer wrr) {
        if (wrr.vertexCount == 0) {
            endSetVertexFormat(wrr);
        }
    }
    
    public static void endAddVertex(final WorldRenderer wrr) {
        final SVertexBuilder svertexbuilder = wrr.sVertexBuilder;
        if (svertexbuilder.vertexSize == 14) {
            if (wrr.drawMode == 7 && wrr.vertexCount % 4 == 0) {
                svertexbuilder.calcNormal(wrr, wrr.func_181664_j() - 4 * svertexbuilder.vertexSize);
            }
            final long i = svertexbuilder.entityData[svertexbuilder.entityDataIndex];
            final int j = wrr.func_181664_j() - 14 + 12;
            wrr.rawIntBuffer.put(j, (int)i);
            wrr.rawIntBuffer.put(j + 1, (int)(i >> 32));
        }
    }
    
    public static void beginAddVertexData(final WorldRenderer wrr, final int[] data) {
        if (wrr.vertexCount == 0) {
            endSetVertexFormat(wrr);
        }
        final SVertexBuilder svertexbuilder = wrr.sVertexBuilder;
        if (svertexbuilder.vertexSize == 14) {
            final long i = svertexbuilder.entityData[svertexbuilder.entityDataIndex];
            for (int j = 12; j + 1 < data.length; j += 14) {
                data[j] = (int)i;
                data[j + 1] = (int)(i >> 32);
            }
        }
    }
    
    public static void endAddVertexData(final WorldRenderer wrr) {
        final SVertexBuilder svertexbuilder = wrr.sVertexBuilder;
        if (svertexbuilder.vertexSize == 14 && wrr.drawMode == 7 && wrr.vertexCount % 4 == 0) {
            svertexbuilder.calcNormal(wrr, wrr.func_181664_j() - 4 * svertexbuilder.vertexSize);
        }
    }
    
    public void calcNormal(final WorldRenderer wrr, final int baseIndex) {
        final FloatBuffer floatbuffer = wrr.rawFloatBuffer;
        final IntBuffer intbuffer = wrr.rawIntBuffer;
        final int i = wrr.func_181664_j();
        final float f = floatbuffer.get(baseIndex + 0 * this.vertexSize);
        final float f2 = floatbuffer.get(baseIndex + 0 * this.vertexSize + 1);
        final float f3 = floatbuffer.get(baseIndex + 0 * this.vertexSize + 2);
        final float f4 = floatbuffer.get(baseIndex + 0 * this.vertexSize + this.offsetUV);
        final float f5 = floatbuffer.get(baseIndex + 0 * this.vertexSize + this.offsetUV + 1);
        final float f6 = floatbuffer.get(baseIndex + 1 * this.vertexSize);
        final float f7 = floatbuffer.get(baseIndex + 1 * this.vertexSize + 1);
        final float f8 = floatbuffer.get(baseIndex + 1 * this.vertexSize + 2);
        final float f9 = floatbuffer.get(baseIndex + 1 * this.vertexSize + this.offsetUV);
        final float f10 = floatbuffer.get(baseIndex + 1 * this.vertexSize + this.offsetUV + 1);
        final float f11 = floatbuffer.get(baseIndex + 2 * this.vertexSize);
        final float f12 = floatbuffer.get(baseIndex + 2 * this.vertexSize + 1);
        final float f13 = floatbuffer.get(baseIndex + 2 * this.vertexSize + 2);
        final float f14 = floatbuffer.get(baseIndex + 2 * this.vertexSize + this.offsetUV);
        final float f15 = floatbuffer.get(baseIndex + 2 * this.vertexSize + this.offsetUV + 1);
        final float f16 = floatbuffer.get(baseIndex + 3 * this.vertexSize);
        final float f17 = floatbuffer.get(baseIndex + 3 * this.vertexSize + 1);
        final float f18 = floatbuffer.get(baseIndex + 3 * this.vertexSize + 2);
        final float f19 = floatbuffer.get(baseIndex + 3 * this.vertexSize + this.offsetUV);
        final float f20 = floatbuffer.get(baseIndex + 3 * this.vertexSize + this.offsetUV + 1);
        float f21 = f11 - f;
        float f22 = f12 - f2;
        float f23 = f13 - f3;
        float f24 = f16 - f6;
        float f25 = f17 - f7;
        float f26 = f18 - f8;
        float f27 = f22 * f26 - f25 * f23;
        float f28 = f23 * f24 - f26 * f21;
        float f29 = f21 * f25 - f24 * f22;
        float f30 = f27 * f27 + f28 * f28 + f29 * f29;
        float f31 = (f30 != 0.0) ? ((float)(1.0 / Math.sqrt(f30))) : 1.0f;
        f27 *= f31;
        f28 *= f31;
        f29 *= f31;
        f21 = f6 - f;
        f22 = f7 - f2;
        f23 = f8 - f3;
        final float f32 = f9 - f4;
        final float f33 = f10 - f5;
        f24 = f11 - f;
        f25 = f12 - f2;
        f26 = f13 - f3;
        final float f34 = f14 - f4;
        final float f35 = f15 - f5;
        final float f36 = f32 * f35 - f34 * f33;
        final float f37 = (f36 != 0.0f) ? (1.0f / f36) : 1.0f;
        float f38 = (f35 * f21 - f33 * f24) * f37;
        float f39 = (f35 * f22 - f33 * f25) * f37;
        float f40 = (f35 * f23 - f33 * f26) * f37;
        float f41 = (f32 * f24 - f34 * f21) * f37;
        float f42 = (f32 * f25 - f34 * f22) * f37;
        float f43 = (f32 * f26 - f34 * f23) * f37;
        f30 = f38 * f38 + f39 * f39 + f40 * f40;
        f31 = ((f30 != 0.0) ? ((float)(1.0 / Math.sqrt(f30))) : 1.0f);
        f38 *= f31;
        f39 *= f31;
        f40 *= f31;
        f30 = f41 * f41 + f42 * f42 + f43 * f43;
        f31 = ((f30 != 0.0) ? ((float)(1.0 / Math.sqrt(f30))) : 1.0f);
        f41 *= f31;
        f42 *= f31;
        f43 *= f31;
        final float f44 = f29 * f39 - f28 * f40;
        final float f45 = f27 * f40 - f29 * f38;
        final float f46 = f28 * f38 - f27 * f39;
        final float f47 = (f41 * f44 + f42 * f45 + f43 * f46 < 0.0f) ? -1.0f : 1.0f;
        final int j = (int)(f27 * 127.0f) & 0xFF;
        final int k = (int)(f28 * 127.0f) & 0xFF;
        final int l = (int)(f29 * 127.0f) & 0xFF;
        final int i2 = (l << 16) + (k << 8) + j;
        intbuffer.put(baseIndex + 0 * this.vertexSize + this.offsetNormal, i2);
        intbuffer.put(baseIndex + 1 * this.vertexSize + this.offsetNormal, i2);
        intbuffer.put(baseIndex + 2 * this.vertexSize + this.offsetNormal, i2);
        intbuffer.put(baseIndex + 3 * this.vertexSize + this.offsetNormal, i2);
        final int j2 = ((int)(f38 * 32767.0f) & 0xFFFF) + (((int)(f39 * 32767.0f) & 0xFFFF) << 16);
        final int k2 = ((int)(f40 * 32767.0f) & 0xFFFF) + (((int)(f47 * 32767.0f) & 0xFFFF) << 16);
        intbuffer.put(baseIndex + 0 * this.vertexSize + 10, j2);
        intbuffer.put(baseIndex + 0 * this.vertexSize + 10 + 1, k2);
        intbuffer.put(baseIndex + 1 * this.vertexSize + 10, j2);
        intbuffer.put(baseIndex + 1 * this.vertexSize + 10 + 1, k2);
        intbuffer.put(baseIndex + 2 * this.vertexSize + 10, j2);
        intbuffer.put(baseIndex + 2 * this.vertexSize + 10 + 1, k2);
        intbuffer.put(baseIndex + 3 * this.vertexSize + 10, j2);
        intbuffer.put(baseIndex + 3 * this.vertexSize + 10 + 1, k2);
        final float f48 = (f4 + f9 + f14 + f19) / 4.0f;
        final float f49 = (f5 + f10 + f15 + f20) / 4.0f;
        floatbuffer.put(baseIndex + 0 * this.vertexSize + 8, f48);
        floatbuffer.put(baseIndex + 0 * this.vertexSize + 8 + 1, f49);
        floatbuffer.put(baseIndex + 1 * this.vertexSize + 8, f48);
        floatbuffer.put(baseIndex + 1 * this.vertexSize + 8 + 1, f49);
        floatbuffer.put(baseIndex + 2 * this.vertexSize + 8, f48);
        floatbuffer.put(baseIndex + 2 * this.vertexSize + 8 + 1, f49);
        floatbuffer.put(baseIndex + 3 * this.vertexSize + 8, f48);
        floatbuffer.put(baseIndex + 3 * this.vertexSize + 8 + 1, f49);
    }
    
    public static void calcNormalChunkLayer(final WorldRenderer wrr) {
        if (wrr.getVertexFormat().hasNormal() && wrr.drawMode == 7 && wrr.vertexCount % 4 == 0) {
            final SVertexBuilder svertexbuilder = wrr.sVertexBuilder;
            endSetVertexFormat(wrr);
            for (int i = wrr.vertexCount * svertexbuilder.vertexSize, j = 0; j < i; j += svertexbuilder.vertexSize * 4) {
                svertexbuilder.calcNormal(wrr, j);
            }
        }
    }
    
    public static void drawArrays(final int drawMode, final int first, final int count, final WorldRenderer wrr) {
        if (count != 0) {
            final VertexFormat vertexformat = wrr.getVertexFormat();
            final int i = vertexformat.getNextOffset();
            if (i == 56) {
                final ByteBuffer bytebuffer = wrr.getByteBuffer();
                bytebuffer.position(32);
                GL20.glVertexAttribPointer(Shaders.midTexCoordAttrib, 2, 5126, false, i, bytebuffer);
                bytebuffer.position(40);
                GL20.glVertexAttribPointer(Shaders.tangentAttrib, 4, 5122, false, i, bytebuffer);
                bytebuffer.position(48);
                GL20.glVertexAttribPointer(Shaders.entityAttrib, 3, 5122, false, i, bytebuffer);
                bytebuffer.position(0);
                GL20.glEnableVertexAttribArray(Shaders.midTexCoordAttrib);
                GL20.glEnableVertexAttribArray(Shaders.tangentAttrib);
                GL20.glEnableVertexAttribArray(Shaders.entityAttrib);
                GL11.glDrawArrays(drawMode, first, count);
                GL20.glDisableVertexAttribArray(Shaders.midTexCoordAttrib);
                GL20.glDisableVertexAttribArray(Shaders.tangentAttrib);
                GL20.glDisableVertexAttribArray(Shaders.entityAttrib);
            }
            else {
                GL11.glDrawArrays(drawMode, first, count);
            }
        }
    }
}
