package optifine;

import java.util.BitSet;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateBase;
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
    private BlockPosM colorizerBlockPosM;
    private boolean[] borderFlags;
    private static ThreadLocal threadLocalInstance = new ThreadLocal();

    private RenderEnv(IBlockAccess blockAccess, IBlockState blockState, BlockPos blockPos) {
        this.quadBounds = new float[EnumFacing.VALUES.length * 2];
        this.boundsFlags = new BitSet(3);
        this.aoFace = new BlockModelRenderer.AmbientOcclusionFace();
        this.colorizerBlockPosM = null;
        this.borderFlags = null;
        this.blockAccess = blockAccess;
        this.blockState = blockState;
        this.blockPos = blockPos;
        this.gameSettings = Config.getGameSettings();
    }

    public static RenderEnv getInstance(IBlockAccess blockAccessIn, IBlockState blockStateIn, BlockPos blockPosIn) {
        RenderEnv re = (RenderEnv) threadLocalInstance.get();

        if (re == null) {
            re = new RenderEnv(blockAccessIn, blockStateIn, blockPosIn);
            threadLocalInstance.set(re);
            return re;
        } else {
            re.reset(blockAccessIn, blockStateIn, blockPosIn);
            return re;
        }
    }

    private void reset(IBlockAccess blockAccessIn, IBlockState blockStateIn, BlockPos blockPosIn) {
        this.blockAccess = blockAccessIn;
        this.blockState = blockStateIn;
        this.blockPos = blockPosIn;
        this.blockId = -1;
        this.metadata = -1;
        this.breakingAnimation = -1;
        this.boundsFlags.clear();
    }

    public int getBlockId() {
        if (this.blockId < 0) {
            if (this.blockState instanceof BlockStateBase) {
                BlockStateBase bsb = (BlockStateBase) this.blockState;
                this.blockId = bsb.getBlockId();
            } else {
                this.blockId = Block.getIdFromBlock(this.blockState.getBlock());
            }
        }

        return this.blockId;
    }

    public int getMetadata() {
        if (this.metadata < 0) {
            if (this.blockState instanceof BlockStateBase) {
                BlockStateBase bsb = (BlockStateBase) this.blockState;
                this.metadata = bsb.getMetadata();
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

    public boolean isBreakingAnimation(List listQuads) {
        if (this.breakingAnimation < 0 && listQuads.size() > 0) {
            if (listQuads.get(0) instanceof BreakingFour) {
                this.breakingAnimation = 1;
            } else {
                this.breakingAnimation = 0;
            }
        }

        return this.breakingAnimation == 1;
    }

    public boolean isBreakingAnimation(BakedQuad quad) {
        if (this.breakingAnimation < 0) {
            if (quad instanceof BreakingFour) {
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
