package optifine;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer.AmbientOcclusionFace;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BreakingFour;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

import java.util.BitSet;
import java.util.List;

public class RenderEnv {
    private static ThreadLocal threadLocalInstance = new ThreadLocal();
    private IBlockAccess blockAccess;
    private IBlockState blockState;
    private BlockPos blockPos;
    private GameSettings gameSettings;
    private int blockId = -1;
    private int metadata = -1;
    private int breakingAnimation = -1;
    private float[] quadBounds = new float[EnumFacing.VALUES.length * 2];
    private BitSet boundsFlags = new BitSet(3);
    private BlockModelRenderer.AmbientOcclusionFace aoFace = new BlockModelRenderer.AmbientOcclusionFace();
    private BlockPosM colorizerBlockPosM = null;
    private boolean[] borderFlags = null;

    private RenderEnv(IBlockAccess paramIBlockAccess, IBlockState paramIBlockState, BlockPos paramBlockPos) {
        this.blockAccess = paramIBlockAccess;
        this.blockState = paramIBlockState;
        this.blockPos = paramBlockPos;
        this.gameSettings = Config.getGameSettings();
    }

    public static RenderEnv getInstance(IBlockAccess paramIBlockAccess, IBlockState paramIBlockState, BlockPos paramBlockPos) {
        RenderEnv localRenderEnv = (RenderEnv) threadLocalInstance.get();
        if (localRenderEnv == null) {
            localRenderEnv = new RenderEnv(paramIBlockAccess, paramIBlockState, paramBlockPos);
            threadLocalInstance.set(localRenderEnv);
            return localRenderEnv;
        }
        localRenderEnv.reset(paramIBlockAccess, paramIBlockState, paramBlockPos);
        return localRenderEnv;
    }

    private void reset(IBlockAccess paramIBlockAccess, IBlockState paramIBlockState, BlockPos paramBlockPos) {
        this.blockAccess = paramIBlockAccess;
        this.blockState = paramIBlockState;
        this.blockPos = paramBlockPos;
        this.blockId = -1;
        this.metadata = -1;
        this.breakingAnimation = -1;
        this.boundsFlags.clear();
    }

    public int getBlockId() {
        if (this.blockId < 0) {
            if ((this.blockState instanceof BlockStateBase)) {
                BlockStateBase localBlockStateBase = (BlockStateBase) this.blockState;
                this.blockId = localBlockStateBase.getBlockId();
            } else {
                this.blockId = Block.getIdFromBlock(this.blockState.getBlock());
            }
        }
        return this.blockId;
    }

    public int getMetadata() {
        if (this.metadata < 0) {
            if ((this.blockState instanceof BlockStateBase)) {
                BlockStateBase localBlockStateBase = (BlockStateBase) this.blockState;
                this.metadata = localBlockStateBase.getMetadata();
            } else {
                this.metadata = this.blockState.getBlock().getMetaFromState(this.blockState);
            }
        }
        return this.metadata;
    }

    public float[] getQuadBounds() {
        return this.quadBounds;
    }

    public BitSet getBoundsFlags() {
        return this.boundsFlags;
    }

    public BlockModelRenderer.AmbientOcclusionFace getAoFace() {
        return this.aoFace;
    }

    public boolean isBreakingAnimation(List paramList) {
        if ((this.breakingAnimation < 0) && (paramList.size() > 0)) {
            if ((paramList.get(0) instanceof BreakingFour)) {
                this.breakingAnimation = 1;
            } else {
                this.breakingAnimation = 0;
            }
        }
        return this.breakingAnimation == 1;
    }

    public boolean isBreakingAnimation(BakedQuad paramBakedQuad) {
        if (this.breakingAnimation < 0) {
            if ((paramBakedQuad instanceof BreakingFour)) {
                this.breakingAnimation = 1;
            } else {
                this.breakingAnimation = 0;
            }
        }
        return this.breakingAnimation == 1;
    }

    public boolean isBreakingAnimation() {
        return this.breakingAnimation == 1;
    }

    public IBlockState getBlockState() {
        return this.blockState;
    }

    public BlockPosM getColorizerBlockPosM() {
        if (this.colorizerBlockPosM == null) {
            this.colorizerBlockPosM = new BlockPosM(0, 0, 0);
        }
        return this.colorizerBlockPosM;
    }

    public boolean[] getBorderFlags() {
        if (this.borderFlags == null) {
            this.borderFlags = new boolean[4];
        }
        return this.borderFlags;
    }
}




