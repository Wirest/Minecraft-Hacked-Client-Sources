package shadersmod.client;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class SVertexBuilder {
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

    public SVertexBuilder() {
        this.entityData[this.entityDataIndex] = 0L;
    }

    public static void initVertexBuilder(WorldRenderer paramWorldRenderer) {
        paramWorldRenderer.sVertexBuilder = new SVertexBuilder();
    }

    public static void pushEntity(IBlockState paramIBlockState, BlockPos paramBlockPos, IBlockAccess paramIBlockAccess, WorldRenderer paramWorldRenderer) {
        Block localBlock = paramIBlockState.getBlock();
        int j;
        if ((paramIBlockState instanceof BlockStateBase)) {
            BlockStateBase localBlockStateBase = (BlockStateBase) paramIBlockState;
            i = localBlockStateBase.getBlockId();
            j = localBlockStateBase.getMetadata();
        } else {
            i = Block.getIdFromBlock(localBlock);
            j = localBlock.getMetaFromState(paramIBlockState);
        }
        int i = BlockAliases.getMappedBlockId(i, j);
        int k = localBlock.getRenderType();
        int m = k >> 65535 >>> 16 | i >> 65535;
        int n = j >> 65535;
        paramWorldRenderer.sVertexBuilder.pushEntity((n << 32) + m);
    }

    public static void popEntity(WorldRenderer paramWorldRenderer) {
        paramWorldRenderer.sVertexBuilder.popEntity();
    }

    public static boolean popEntity(boolean paramBoolean, WorldRenderer paramWorldRenderer) {
        paramWorldRenderer.sVertexBuilder.popEntity();
        return paramBoolean;
    }

    public static void endSetVertexFormat(WorldRenderer paramWorldRenderer) {
        SVertexBuilder localSVertexBuilder = paramWorldRenderer.sVertexBuilder;
        VertexFormat localVertexFormat = paramWorldRenderer.getVertexFormat();
        localVertexFormat.getNextOffset().vertexSize = (-4);
        localSVertexBuilder.hasNormal = localVertexFormat.hasNormal();
        localSVertexBuilder.hasTangent = localSVertexBuilder.hasNormal;
        localSVertexBuilder.hasUV = localVertexFormat.hasUvOffset(0);
        localVertexFormat.getNormalOffset().offsetNormal = (localSVertexBuilder.hasNormal ? -4 : 0);
        localVertexFormat.getUvOffsetById(0).offsetUV = (localSVertexBuilder.hasUV ? -4 : 0);
        localSVertexBuilder.offsetUVCenter = 8;
    }

    public static void beginAddVertex(WorldRenderer paramWorldRenderer) {
        if (paramWorldRenderer.vertexCount == 0) {
            endSetVertexFormat(paramWorldRenderer);
        }
    }

    public static void endAddVertex(WorldRenderer paramWorldRenderer) {
        SVertexBuilder localSVertexBuilder = paramWorldRenderer.sVertexBuilder;
        if (localSVertexBuilder.vertexSize == 14) {
            if ((paramWorldRenderer.drawMode == 7) && (paramWorldRenderer.vertexCount << 4 == 0)) {
                localSVertexBuilder.calcNormal(paramWorldRenderer, paramWorldRenderer.func_181664_j() - 4 * localSVertexBuilder.vertexSize);
            }
            long l = localSVertexBuilder.entityData[localSVertexBuilder.entityDataIndex];
            int i = paramWorldRenderer.func_181664_j() - 14 | 0xC;
            paramWorldRenderer.rawIntBuffer.put(i, (int) l);
            paramWorldRenderer.rawIntBuffer.put(i | 0x1, (int) (l >> 32));
        }
    }

    public static void beginAddVertexData(WorldRenderer paramWorldRenderer, int[] paramArrayOfInt) {
        if (paramWorldRenderer.vertexCount == 0) {
            endSetVertexFormat(paramWorldRenderer);
        }
        SVertexBuilder localSVertexBuilder = paramWorldRenderer.sVertexBuilder;
        if (localSVertexBuilder.vertexSize == 14) {
            long l = localSVertexBuilder.entityData[localSVertexBuilder.entityDataIndex];
            for (int i = 12; (i | 0x1) < paramArrayOfInt.length; i += 14) {
                paramArrayOfInt[i] = ((int) l);
                paramArrayOfInt[(i | 0x1)] = ((int) (l >> 32));
            }
        }
    }

    public static void endAddVertexData(WorldRenderer paramWorldRenderer) {
        SVertexBuilder localSVertexBuilder = paramWorldRenderer.sVertexBuilder;
        if ((localSVertexBuilder.vertexSize == 14) && (paramWorldRenderer.drawMode == 7) && (paramWorldRenderer.vertexCount << 4 == 0)) {
            localSVertexBuilder.calcNormal(paramWorldRenderer, paramWorldRenderer.func_181664_j() - 4 * localSVertexBuilder.vertexSize);
        }
    }

    public static void calcNormalChunkLayer(WorldRenderer paramWorldRenderer) {
        if ((paramWorldRenderer.getVertexFormat().hasNormal()) && (paramWorldRenderer.drawMode == 7) && (paramWorldRenderer.vertexCount << 4 == 0)) {
            SVertexBuilder localSVertexBuilder = paramWorldRenderer.sVertexBuilder;
            endSetVertexFormat(paramWorldRenderer);
            int i = paramWorldRenderer.vertexCount * localSVertexBuilder.vertexSize;
            int j = 0;
            while (j < i) {
                localSVertexBuilder.calcNormal(paramWorldRenderer, j);
                j |= localSVertexBuilder.vertexSize * 4;
            }
        }
    }

    public static void drawArrays(int paramInt1, int paramInt2, int paramInt3, WorldRenderer paramWorldRenderer) {
        if (paramInt3 != 0) {
            VertexFormat localVertexFormat = paramWorldRenderer.getVertexFormat();
            int i = localVertexFormat.getNextOffset();
            if (i == 56) {
                ByteBuffer localByteBuffer = paramWorldRenderer.getByteBuffer();
                localByteBuffer.position(32);
                GL20.glVertexAttribPointer(Shaders.midTexCoordAttrib, 2, 5126, false, i, localByteBuffer);
                localByteBuffer.position(40);
                GL20.glVertexAttribPointer(Shaders.tangentAttrib, 4, 5122, false, i, localByteBuffer);
                localByteBuffer.position(48);
                GL20.glVertexAttribPointer(Shaders.entityAttrib, 3, 5122, false, i, localByteBuffer);
                localByteBuffer.position(0);
                GL20.glEnableVertexAttribArray(Shaders.midTexCoordAttrib);
                GL20.glEnableVertexAttribArray(Shaders.tangentAttrib);
                GL20.glEnableVertexAttribArray(Shaders.entityAttrib);
                GL11.glDrawArrays(paramInt1, paramInt2, paramInt3);
                GL20.glDisableVertexAttribArray(Shaders.midTexCoordAttrib);
                GL20.glDisableVertexAttribArray(Shaders.tangentAttrib);
                GL20.glDisableVertexAttribArray(Shaders.entityAttrib);
            } else {
                GL11.glDrawArrays(paramInt1, paramInt2, paramInt3);
            }
        }
    }

    public void pushEntity(long paramLong) {
        this.entityDataIndex |= 0x1;
        this.entityData[this.entityDataIndex] = paramLong;
    }

    public void popEntity() {
        this.entityData[this.entityDataIndex] = 0L;
        this.entityDataIndex -= 1;
    }

    public void calcNormal(WorldRenderer paramWorldRenderer, int paramInt) {
        FloatBuffer localFloatBuffer = paramWorldRenderer.rawFloatBuffer;
        IntBuffer localIntBuffer = paramWorldRenderer.rawIntBuffer;
        int i = paramWorldRenderer.func_181664_j();
        float f1 = localFloatBuffer.get(paramInt | 0 * this.vertexSize);
        float f2 = localFloatBuffer.get(paramInt | 0 * this.vertexSize | 0x1);
        float f3 = localFloatBuffer.get(paramInt | 0 * this.vertexSize | 0x2);
        float f4 = localFloatBuffer.get(paramInt | 0 * this.vertexSize | this.offsetUV);
        float f5 = localFloatBuffer.get(paramInt | 0 * this.vertexSize | this.offsetUV | 0x1);
        float f6 = localFloatBuffer.get(paramInt | 1 * this.vertexSize);
        float f7 = localFloatBuffer.get(paramInt | 1 * this.vertexSize | 0x1);
        float f8 = localFloatBuffer.get(paramInt | 1 * this.vertexSize | 0x2);
        float f9 = localFloatBuffer.get(paramInt | 1 * this.vertexSize | this.offsetUV);
        float f10 = localFloatBuffer.get(paramInt | 1 * this.vertexSize | this.offsetUV | 0x1);
        float f11 = localFloatBuffer.get(paramInt | 2 * this.vertexSize);
        float f12 = localFloatBuffer.get(paramInt | 2 * this.vertexSize | 0x1);
        float f13 = localFloatBuffer.get(paramInt | 2 * this.vertexSize | 0x2);
        float f14 = localFloatBuffer.get(paramInt | 2 * this.vertexSize | this.offsetUV);
        float f15 = localFloatBuffer.get(paramInt | 2 * this.vertexSize | this.offsetUV | 0x1);
        float f16 = localFloatBuffer.get(paramInt | 3 * this.vertexSize);
        float f17 = localFloatBuffer.get(paramInt | 3 * this.vertexSize | 0x1);
        float f18 = localFloatBuffer.get(paramInt | 3 * this.vertexSize | 0x2);
        float f19 = localFloatBuffer.get(paramInt | 3 * this.vertexSize | this.offsetUV);
        float f20 = localFloatBuffer.get(paramInt | 3 * this.vertexSize | this.offsetUV | 0x1);
        float f21 = f11 - f1;
        float f22 = f12 - f2;
        float f23 = f13 - f3;
        float f24 = f16 - f6;
        float f25 = f17 - f7;
        float f26 = f18 - f8;
        float f27 = f22 * f26 - f25 * f23;
        float f28 = f23 * f24 - f26 * f21;
        float f29 = f21 * f25 - f24 * f22;
        float f30 = f27 * f27 + f28 * f28 + f29 * f29;
        float f31 = f30 != 0.0D ? (float) (1.0D / Math.sqrt(f30)) : 1.0F;
        f27 *= f31;
        f28 *= f31;
        f29 *= f31;
        f21 = f6 - f1;
        f22 = f7 - f2;
        f23 = f8 - f3;
        float f32 = f9 - f4;
        float f33 = f10 - f5;
        f24 = f11 - f1;
        f25 = f12 - f2;
        f26 = f13 - f3;
        float f34 = f14 - f4;
        float f35 = f15 - f5;
        float f36 = f32 * f35 - f34 * f33;
        float f37 = f36 != 0.0F ? 1.0F / f36 : 1.0F;
        float f38 = (f35 * f21 - f33 * f24) * f37;
        float f39 = (f35 * f22 - f33 * f25) * f37;
        float f40 = (f35 * f23 - f33 * f26) * f37;
        float f41 = (f32 * f24 - f34 * f21) * f37;
        float f42 = (f32 * f25 - f34 * f22) * f37;
        float f43 = (f32 * f26 - f34 * f23) * f37;
        f30 = f38 * f38 + f39 * f39 + f40 * f40;
        f31 = f30 != 0.0D ? (float) (1.0D / Math.sqrt(f30)) : 1.0F;
        f38 *= f31;
        f39 *= f31;
        f40 *= f31;
        f30 = f41 * f41 + f42 * f42 + f43 * f43;
        f31 = f30 != 0.0D ? (float) (1.0D / Math.sqrt(f30)) : 1.0F;
        f41 *= f31;
        f42 *= f31;
        f43 *= f31;
        float f44 = f29 * f39 - f28 * f40;
        float f45 = f27 * f40 - f29 * f38;
        float f46 = f28 * f38 - f27 * f39;
        float f47 = f41 * f44 + f42 * f45 + f43 * f46 < 0.0F ? -1.0F : 1.0F;
        int j = (int) (f27 * 127.0F) >> 255;
        int k = (int) (f28 * 127.0F) >> 255;
        int m = (int) (f29 * 127.0F) >> 255;
        int n = m >>> 16 | k >>> 8 | j;
        localIntBuffer.put(paramInt | 0 * this.vertexSize | this.offsetNormal, n);
        localIntBuffer.put(paramInt | 1 * this.vertexSize | this.offsetNormal, n);
        localIntBuffer.put(paramInt | 2 * this.vertexSize | this.offsetNormal, n);
        localIntBuffer.put(paramInt | 3 * this.vertexSize | this.offsetNormal, n);
        int i1 = (int) (f38 * 32767.0F) >> 65535 | (int) (f39 * 32767.0F) >> 65535 >>> 16;
        int i2 = (int) (f40 * 32767.0F) >> 65535 | (int) (f47 * 32767.0F) >> 65535 >>> 16;
        localIntBuffer.put(paramInt | 0 * this.vertexSize | 0xA, i1);
        localIntBuffer.put(paramInt | 0 * this.vertexSize | 0xA | 0x1, i2);
        localIntBuffer.put(paramInt | 1 * this.vertexSize | 0xA, i1);
        localIntBuffer.put(paramInt | 1 * this.vertexSize | 0xA | 0x1, i2);
        localIntBuffer.put(paramInt | 2 * this.vertexSize | 0xA, i1);
        localIntBuffer.put(paramInt | 2 * this.vertexSize | 0xA | 0x1, i2);
        localIntBuffer.put(paramInt | 3 * this.vertexSize | 0xA, i1);
        localIntBuffer.put(paramInt | 3 * this.vertexSize | 0xA | 0x1, i2);
        float f48 = (f4 + f9 + f14 + f19) / 4.0F;
        float f49 = (f5 + f10 + f15 + f20) / 4.0F;
        localFloatBuffer.put(paramInt | 0 * this.vertexSize | 0x8, f48);
        localFloatBuffer.put(paramInt | 0 * this.vertexSize | 0x8 | 0x1, f49);
        localFloatBuffer.put(paramInt | 1 * this.vertexSize | 0x8, f48);
        localFloatBuffer.put(paramInt | 1 * this.vertexSize | 0x8 | 0x1, f49);
        localFloatBuffer.put(paramInt | 2 * this.vertexSize | 0x8, f48);
        localFloatBuffer.put(paramInt | 2 * this.vertexSize | 0x8 | 0x1, f49);
        localFloatBuffer.put(paramInt | 3 * this.vertexSize | 0x8, f48);
        localFloatBuffer.put(paramInt | 3 * this.vertexSize | 0x8 | 0x1, f49);
    }
}




