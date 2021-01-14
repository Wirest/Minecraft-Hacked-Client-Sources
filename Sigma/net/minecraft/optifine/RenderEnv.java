package net.minecraft.optifine;

import java.util.BitSet;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BreakingFour;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class RenderEnv {
    private IBlockAccess blockAccess;
    private IBlockState blockState;
    private BlockPos blockPos;
    private GameSettings gameSettings;
    private int blockId = -1;
    private int metadata = -1;
    private int breakingAnimation = -1;
    private float[] quadBounds;
    private BitSet boundsFlags;
    private BlockModelRenderer.AmbientOcclusionFace aoFace;
    private BlockPosM colorizerBlockPos;
    private boolean[] borderFlags;
    private static ThreadLocal threadLocalInstance = new ThreadLocal();

    private RenderEnv(IBlockAccess blockAccess, IBlockState blockState, BlockPos blockPos) {
        quadBounds = new float[EnumFacing.VALUES.length * 2];
        boundsFlags = new BitSet(3);
        aoFace = new BlockModelRenderer.AmbientOcclusionFace();
        colorizerBlockPos = null;
        borderFlags = null;
        this.blockAccess = blockAccess;
        this.blockState = blockState;
        this.blockPos = blockPos;
        gameSettings = Config.getGameSettings();
    }

    public static RenderEnv getInstance(IBlockAccess blockAccessIn, IBlockState blockStateIn, BlockPos blockPosIn) {
        RenderEnv re = (RenderEnv) RenderEnv.threadLocalInstance.get();

        if (re == null) {
            re = new RenderEnv(blockAccessIn, blockStateIn, blockPosIn);
            RenderEnv.threadLocalInstance.set(re);
            return re;
        } else {
            re.reset(blockAccessIn, blockStateIn, blockPosIn);
            return re;
        }
    }

    private void reset(IBlockAccess blockAccessIn, IBlockState blockStateIn, BlockPos blockPosIn) {
        blockAccess = blockAccessIn;
        blockState = blockStateIn;
        blockPos = blockPosIn;
        blockId = -1;
        metadata = -1;
        breakingAnimation = -1;
        boundsFlags.clear();
    }

    public int getBlockId() {
        if (blockId < 0) {
            blockId = Block.getIdFromBlock(blockState.getBlock());
        }

        return blockId;
    }

    public int getMetadata() {
        if (metadata < 0) {
            metadata = blockState.getBlock().getMetaFromState(blockState);
        }

        return metadata;
    }

    public float[] getQuadBounds() {
        return quadBounds;
    }

    public BitSet getBoundsFlags() {
        return boundsFlags;
    }

    public BlockModelRenderer.AmbientOcclusionFace getAoFace() {
        return aoFace;
    }

    public boolean isBreakingAnimation(List listQuads) {
        if (breakingAnimation < 0 && listQuads.size() > 0) {
            if (listQuads.get(0) instanceof BreakingFour) {
                breakingAnimation = 1;
            } else {
                breakingAnimation = 0;
            }
        }

        return breakingAnimation == 1;
    }

    public boolean isBreakingAnimation(BakedQuad quad) {
        if (breakingAnimation < 0) {
            if (quad instanceof BreakingFour) {
                breakingAnimation = 1;
            } else {
                breakingAnimation = 0;
            }
        }

        return breakingAnimation == 1;
    }

    public boolean isBreakingAnimation() {
        return breakingAnimation == 1;
    }

    public IBlockState getBlockState() {
        return blockState;
    }

    public BlockPosM getColorizerBlockPos() {
        if (colorizerBlockPos == null) {
            colorizerBlockPos = new BlockPosM(0, 0, 0);
        }

        return colorizerBlockPos;
    }

    public boolean[] getBorderFlags() {
        if (borderFlags == null) {
            borderFlags = new boolean[4];
        }

        return borderFlags;
    }
}
