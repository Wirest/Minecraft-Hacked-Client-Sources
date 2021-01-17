// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BreakingFour;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.renderer.BlockModelRenderer;
import java.util.BitSet;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.IBlockAccess;

public class RenderEnv
{
    private IBlockAccess blockAccess;
    private IBlockState blockState;
    private BlockPos blockPos;
    private GameSettings gameSettings;
    private int blockId;
    private int metadata;
    private int breakingAnimation;
    private float[] quadBounds;
    private BitSet boundsFlags;
    private BlockModelRenderer.AmbientOcclusionFace aoFace;
    private BlockPosM colorizerBlockPosM;
    private boolean[] borderFlags;
    private static ThreadLocal threadLocalInstance;
    
    static {
        RenderEnv.threadLocalInstance = new ThreadLocal();
    }
    
    private RenderEnv(final IBlockAccess p_i94_1_, final IBlockState p_i94_2_, final BlockPos p_i94_3_) {
        this.blockId = -1;
        this.metadata = -1;
        this.breakingAnimation = -1;
        this.quadBounds = new float[EnumFacing.VALUES.length * 2];
        this.boundsFlags = new BitSet(3);
        this.aoFace = new BlockModelRenderer.AmbientOcclusionFace();
        this.colorizerBlockPosM = null;
        this.borderFlags = null;
        this.blockAccess = p_i94_1_;
        this.blockState = p_i94_2_;
        this.blockPos = p_i94_3_;
        this.gameSettings = Config.getGameSettings();
    }
    
    public static RenderEnv getInstance(final IBlockAccess p_getInstance_0_, final IBlockState p_getInstance_1_, final BlockPos p_getInstance_2_) {
        RenderEnv renderenv = RenderEnv.threadLocalInstance.get();
        if (renderenv == null) {
            renderenv = new RenderEnv(p_getInstance_0_, p_getInstance_1_, p_getInstance_2_);
            RenderEnv.threadLocalInstance.set(renderenv);
            return renderenv;
        }
        renderenv.reset(p_getInstance_0_, p_getInstance_1_, p_getInstance_2_);
        return renderenv;
    }
    
    private void reset(final IBlockAccess p_reset_1_, final IBlockState p_reset_2_, final BlockPos p_reset_3_) {
        this.blockAccess = p_reset_1_;
        this.blockState = p_reset_2_;
        this.blockPos = p_reset_3_;
        this.blockId = -1;
        this.metadata = -1;
        this.breakingAnimation = -1;
        this.boundsFlags.clear();
    }
    
    public int getBlockId() {
        if (this.blockId < 0) {
            if (this.blockState instanceof BlockStateBase) {
                final BlockStateBase blockstatebase = (BlockStateBase)this.blockState;
                this.blockId = blockstatebase.getBlockId();
            }
            else {
                this.blockId = Block.getIdFromBlock(this.blockState.getBlock());
            }
        }
        return this.blockId;
    }
    
    public int getMetadata() {
        if (this.metadata < 0) {
            if (this.blockState instanceof BlockStateBase) {
                final BlockStateBase blockstatebase = (BlockStateBase)this.blockState;
                this.metadata = blockstatebase.getMetadata();
            }
            else {
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
    
    public boolean isBreakingAnimation(final List p_isBreakingAnimation_1_) {
        if (this.breakingAnimation < 0 && p_isBreakingAnimation_1_.size() > 0) {
            if (p_isBreakingAnimation_1_.get(0) instanceof BreakingFour) {
                this.breakingAnimation = 1;
            }
            else {
                this.breakingAnimation = 0;
            }
        }
        return this.breakingAnimation == 1;
    }
    
    public boolean isBreakingAnimation(final BakedQuad p_isBreakingAnimation_1_) {
        if (this.breakingAnimation < 0) {
            if (p_isBreakingAnimation_1_ instanceof BreakingFour) {
                this.breakingAnimation = 1;
            }
            else {
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
