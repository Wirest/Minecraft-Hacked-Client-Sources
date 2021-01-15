package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class BlockPumpkin extends BlockDirectional
{
    private BlockPattern snowmanBasePattern;
    private BlockPattern snowmanPattern;
    private BlockPattern golemBasePattern;
    private BlockPattern golemPattern;

    protected BlockPumpkin()
    {
        super(Material.gourd);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(worldIn, pos, state);
        this.trySpawnGolem(worldIn, pos);
    }

    public boolean canDispenserPlace(World worldIn, BlockPos pos)
    {
        return this.getSnowmanBasePattern().match(worldIn, pos) != null || this.getGolemBasePattern().match(worldIn, pos) != null;
    }

    private void trySpawnGolem(World worldIn, BlockPos pos)
    {
        BlockPattern.PatternHelper var3;
        int var4;
        int var6;

        if ((var3 = this.getSnowmanPattern().match(worldIn, pos)) != null)
        {
            for (var4 = 0; var4 < this.getSnowmanPattern().getThumbLength(); ++var4)
            {
                BlockWorldState var5 = var3.translateOffset(0, var4, 0);
                worldIn.setBlockState(var5.getPos(), Blocks.air.getDefaultState(), 2);
            }

            EntitySnowman var9 = new EntitySnowman(worldIn);
            BlockPos var11 = var3.translateOffset(0, 2, 0).getPos();
            var9.setLocationAndAngles(var11.getX() + 0.5D, var11.getY() + 0.05D, var11.getZ() + 0.5D, 0.0F, 0.0F);
            worldIn.spawnEntityInWorld(var9);

            for (var6 = 0; var6 < 120; ++var6)
            {
                worldIn.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, var11.getX() + worldIn.rand.nextDouble(), var11.getY() + worldIn.rand.nextDouble() * 2.5D, var11.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
            }

            for (var6 = 0; var6 < this.getSnowmanPattern().getThumbLength(); ++var6)
            {
                BlockWorldState var7 = var3.translateOffset(0, var6, 0);
                worldIn.notifyNeighborsRespectDebug(var7.getPos(), Blocks.air);
            }
        }
        else if ((var3 = this.getGolemPattern().match(worldIn, pos)) != null)
        {
            for (var4 = 0; var4 < this.getGolemPattern().getPalmLength(); ++var4)
            {
                for (int var12 = 0; var12 < this.getGolemPattern().getThumbLength(); ++var12)
                {
                    worldIn.setBlockState(var3.translateOffset(var4, var12, 0).getPos(), Blocks.air.getDefaultState(), 2);
                }
            }

            BlockPos var10 = var3.translateOffset(1, 2, 0).getPos();
            EntityIronGolem var13 = new EntityIronGolem(worldIn);
            var13.setPlayerCreated(true);
            var13.setLocationAndAngles(var10.getX() + 0.5D, var10.getY() + 0.05D, var10.getZ() + 0.5D, 0.0F, 0.0F);
            worldIn.spawnEntityInWorld(var13);

            for (var6 = 0; var6 < 120; ++var6)
            {
                worldIn.spawnParticle(EnumParticleTypes.SNOWBALL, var10.getX() + worldIn.rand.nextDouble(), var10.getY() + worldIn.rand.nextDouble() * 3.9D, var10.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
            }

            for (var6 = 0; var6 < this.getGolemPattern().getPalmLength(); ++var6)
            {
                for (int var14 = 0; var14 < this.getGolemPattern().getThumbLength(); ++var14)
                {
                    BlockWorldState var8 = var3.translateOffset(var6, var14, 0);
                    worldIn.notifyNeighborsRespectDebug(var8.getPos(), Blocks.air);
                }
            }
        }
    }

    @Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos).getBlock().blockMaterial.isReplaceable() && World.doesBlockHaveSolidTopSurface(worldIn, pos.down());
    }

    @Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
    }

    @Override
	protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {FACING});
    }

    protected BlockPattern getSnowmanBasePattern()
    {
        if (this.snowmanBasePattern == null)
        {
            this.snowmanBasePattern = FactoryBlockPattern.start().aisle(new String[] {" ", "#", "#"}).where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.snow))).build();
        }

        return this.snowmanBasePattern;
    }

    protected BlockPattern getSnowmanPattern()
    {
        if (this.snowmanPattern == null)
        {
            this.snowmanPattern = FactoryBlockPattern.start().aisle(new String[] {"^", "#", "#"}).where('^', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.pumpkin))).where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.snow))).build();
        }

        return this.snowmanPattern;
    }

    protected BlockPattern getGolemBasePattern()
    {
        if (this.golemBasePattern == null)
        {
            this.golemBasePattern = FactoryBlockPattern.start().aisle(new String[] {"~ ~", "###", "~#~"}).where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.iron_block))).where('~', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.air))).build();
        }

        return this.golemBasePattern;
    }

    protected BlockPattern getGolemPattern()
    {
        if (this.golemPattern == null)
        {
            this.golemPattern = FactoryBlockPattern.start().aisle(new String[] {"~^~", "###", "~#~"}).where('^', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.pumpkin))).where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.iron_block))).where('~', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.air))).build();
        }

        return this.golemPattern;
    }
}
