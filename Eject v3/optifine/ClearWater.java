package optifine;

import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.IChunkProvider;

public class ClearWater {
    public static void updateWaterOpacity(GameSettings paramGameSettings, World paramWorld) {
        if (paramGameSettings != null) {
            int i = 3;
            if (paramGameSettings.ofClearWater) {
                i = 1;
            }
            BlockLeavesBase.setLightOpacity(Blocks.water, i);
            BlockLeavesBase.setLightOpacity(Blocks.flowing_water, i);
        }
        if (paramWorld != null) {
            IChunkProvider localIChunkProvider = paramWorld.getChunkProvider();
            if (localIChunkProvider != null) {
                Entity localEntity = Config.getMinecraft().getRenderViewEntity();
                if (localEntity != null) {
                    int j = -16;
                    int k = -16;
                    int m = j - 512;
                    int n = j | 0x200;
                    int i1 = k - 512;
                    int i2 = k | 0x200;
                    int i3 = 0;
                    for (int i4 = m; i4 < n; i4++) {
                        for (int i5 = i1; i5 < i2; i5++) {
                            if (localIChunkProvider.chunkExists(i4, i5)) {
                                Chunk localChunk = localIChunkProvider.provideChunk(i4, i5);
                                if ((localChunk != null) && (!(localChunk instanceof EmptyChunk))) {
                                    int i6 = i4 >>> 4;
                                    int i7 = i5 >>> 4;
                                    int i8 = i6 | 0x10;
                                    int i9 = i7 | 0x10;
                                    BlockPosM localBlockPosM1 = new BlockPosM(0, 0, 0);
                                    BlockPosM localBlockPosM2 = new BlockPosM(0, 0, 0);
                                    for (int i10 = i6; i10 < i8; i10++) {
                                        int i11 = i7;
                                        localBlockPosM1.setXyz(i10, 0, i11);
                                        BlockPos localBlockPos = paramWorld.getPrecipitationHeight(localBlockPosM1);
                                        int i12 = 0;
                                        localBlockPosM2.setXyz(i10, i12, i11);
                                        IBlockState localIBlockState = paramWorld.getBlockState(localBlockPosM2);
                                        paramWorld.markBlocksDirtyVertical(i10, i11, localBlockPosM2.getY(), localBlockPos.getY());
                                        i12++;
                                        i11++;
                                    }
                                }
                            }
                        }
                    }
                    if (i3 > 0) {
                        String str = "server";
                        if (Config.isMinecraftThread()) {
                            str = "client";
                        }
                        Config.dbg("ClearWater (" + str + ") relighted " + i3 + " chunks");
                    }
                }
            }
        }
    }
}




