package net.minecraft.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.client.resources.model.WeightedBakedModel;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;

public class BlockRendererDispatcher implements IResourceManagerReloadListener
{
    private BlockModelShapes blockModelShapes;
    private final GameSettings gameSettings;
    private final BlockModelRenderer blockModelRenderer = new BlockModelRenderer();
    private final ChestRenderer chestRenderer = new ChestRenderer();
    private final BlockFluidRenderer fluidRenderer = new BlockFluidRenderer();

    public BlockRendererDispatcher(BlockModelShapes blockModelShapesIn, GameSettings gameSettingsIn)
    {
        this.blockModelShapes = blockModelShapesIn;
        this.gameSettings = gameSettingsIn;
    }

    public BlockModelShapes getBlockModelShapes()
    {
        return this.blockModelShapes;
    }

    public void renderBlockDamage(IBlockState state, BlockPos pos, TextureAtlasSprite texture, IBlockAccess blockAccess)
    {
        Block var5 = state.getBlock();
        int var6 = var5.getRenderType();

        if (var6 == 3)
        {
            state = var5.getActualState(state, blockAccess, pos);
            IBakedModel var7 = this.blockModelShapes.getModelForState(state);
            IBakedModel var8 = (new SimpleBakedModel.Builder(var7, texture)).makeBakedModel();
            this.blockModelRenderer.renderModel(blockAccess, var8, state, pos, Tessellator.getInstance().getWorldRenderer());
        }
    }

    public boolean renderBlock(IBlockState state, BlockPos pos, IBlockAccess blockAccess, WorldRenderer worldRendererIn)
    {
        try
        {
            int var5 = state.getBlock().getRenderType();

            if (var5 == -1)
            {
                return false;
            }
            else
            {
                switch (var5)
                {
                    case 1:
                        return this.fluidRenderer.renderFluid(blockAccess, state, pos, worldRendererIn);

                    case 2:
                        return false;

                    case 3:
                        IBakedModel var9 = this.getModelFromBlockState(state, blockAccess, pos);
                        return this.blockModelRenderer.renderModel(blockAccess, var9, state, pos, worldRendererIn);

                    default:
                        return false;
                }
            }
        }
        catch (Throwable var8)
        {
            CrashReport var6 = CrashReport.makeCrashReport(var8, "Tesselating block in world");
            CrashReportCategory var7 = var6.makeCategory("Block being tesselated");
            CrashReportCategory.addBlockInfo(var7, pos, state.getBlock(), state.getBlock().getMetaFromState(state));
            throw new ReportedException(var6);
        }
    }

    public BlockModelRenderer getBlockModelRenderer()
    {
        return this.blockModelRenderer;
    }

    private IBakedModel getBakedModel(IBlockState state, BlockPos pos)
    {
        IBakedModel var3 = this.blockModelShapes.getModelForState(state);

        if (pos != null && this.gameSettings.allowBlockAlternatives && var3 instanceof WeightedBakedModel)
        {
            var3 = ((WeightedBakedModel)var3).getAlternativeModel(MathHelper.getPositionRandom(pos));
        }

        return var3;
    }

    public IBakedModel getModelFromBlockState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        Block var4 = state.getBlock();

        if (worldIn.getWorldType() != WorldType.DEBUG_WORLD)
        {
            try
            {
                state = var4.getActualState(state, worldIn, pos);
            }
            catch (Exception var6)
            {
                ;
            }
        }

        IBakedModel var5 = this.blockModelShapes.getModelForState(state);

        if (pos != null && this.gameSettings.allowBlockAlternatives && var5 instanceof WeightedBakedModel)
        {
            var5 = ((WeightedBakedModel)var5).getAlternativeModel(MathHelper.getPositionRandom(pos));
        }

        return var5;
    }

    public void renderBlockBrightness(IBlockState state, float brightness)
    {
        int var3 = state.getBlock().getRenderType();

        if (var3 != -1)
        {
            switch (var3)
            {
                case 1:
                default:
                    break;

                case 2:
                    this.chestRenderer.renderChestBrightness(state.getBlock(), brightness);
                    break;

                case 3:
                    IBakedModel var4 = this.getBakedModel(state, (BlockPos)null);
                    this.blockModelRenderer.renderModelBrightness(var4, state, brightness, true);
            }
        }
    }

    public boolean isRenderTypeChest(Block p_175021_1_, int p_175021_2_)
    {
        if (p_175021_1_ == null)
        {
            return false;
        }
        else
        {
            int var3 = p_175021_1_.getRenderType();
            return var3 == 3 ? false : var3 == 2;
        }
    }

    @Override
	public void onResourceManagerReload(IResourceManager resourceManager)
    {
        this.fluidRenderer.initAtlasSprites();
    }
}
