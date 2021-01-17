// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import com.google.common.base.Predicate;
import net.minecraft.block.state.pattern.BlockPattern;

public class BlockPumpkin extends BlockDirectional
{
    private BlockPattern snowmanBasePattern;
    private BlockPattern snowmanPattern;
    private BlockPattern golemBasePattern;
    private BlockPattern golemPattern;
    private static final Predicate<IBlockState> field_181085_Q;
    
    static {
        field_181085_Q = new Predicate<IBlockState>() {
            @Override
            public boolean apply(final IBlockState p_apply_1_) {
                return p_apply_1_ != null && (p_apply_1_.getBlock() == Blocks.pumpkin || p_apply_1_.getBlock() == Blocks.lit_pumpkin);
            }
        };
    }
    
    protected BlockPumpkin() {
        super(Material.gourd, MapColor.adobeColor);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockPumpkin.FACING, EnumFacing.NORTH));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        this.trySpawnGolem(worldIn, pos);
    }
    
    public boolean canDispenserPlace(final World worldIn, final BlockPos pos) {
        return this.getSnowmanBasePattern().match(worldIn, pos) != null || this.getGolemBasePattern().match(worldIn, pos) != null;
    }
    
    private void trySpawnGolem(final World worldIn, final BlockPos pos) {
        BlockPattern.PatternHelper blockpattern$patternhelper;
        if ((blockpattern$patternhelper = this.getSnowmanPattern().match(worldIn, pos)) != null) {
            for (int i = 0; i < this.getSnowmanPattern().getThumbLength(); ++i) {
                final BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(0, i, 0);
                worldIn.setBlockState(blockworldstate.getPos(), Blocks.air.getDefaultState(), 2);
            }
            final EntitySnowman entitysnowman = new EntitySnowman(worldIn);
            final BlockPos blockpos1 = blockpattern$patternhelper.translateOffset(0, 2, 0).getPos();
            entitysnowman.setLocationAndAngles(blockpos1.getX() + 0.5, blockpos1.getY() + 0.05, blockpos1.getZ() + 0.5, 0.0f, 0.0f);
            worldIn.spawnEntityInWorld(entitysnowman);
            for (int j = 0; j < 120; ++j) {
                worldIn.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, blockpos1.getX() + worldIn.rand.nextDouble(), blockpos1.getY() + worldIn.rand.nextDouble() * 2.5, blockpos1.getZ() + worldIn.rand.nextDouble(), 0.0, 0.0, 0.0, new int[0]);
            }
            for (int i2 = 0; i2 < this.getSnowmanPattern().getThumbLength(); ++i2) {
                final BlockWorldState blockworldstate2 = blockpattern$patternhelper.translateOffset(0, i2, 0);
                worldIn.notifyNeighborsRespectDebug(blockworldstate2.getPos(), Blocks.air);
            }
        }
        else if ((blockpattern$patternhelper = this.getGolemPattern().match(worldIn, pos)) != null) {
            for (int k = 0; k < this.getGolemPattern().getPalmLength(); ++k) {
                for (int l = 0; l < this.getGolemPattern().getThumbLength(); ++l) {
                    worldIn.setBlockState(blockpattern$patternhelper.translateOffset(k, l, 0).getPos(), Blocks.air.getDefaultState(), 2);
                }
            }
            final BlockPos blockpos2 = blockpattern$patternhelper.translateOffset(1, 2, 0).getPos();
            final EntityIronGolem entityirongolem = new EntityIronGolem(worldIn);
            entityirongolem.setPlayerCreated(true);
            entityirongolem.setLocationAndAngles(blockpos2.getX() + 0.5, blockpos2.getY() + 0.05, blockpos2.getZ() + 0.5, 0.0f, 0.0f);
            worldIn.spawnEntityInWorld(entityirongolem);
            for (int j2 = 0; j2 < 120; ++j2) {
                worldIn.spawnParticle(EnumParticleTypes.SNOWBALL, blockpos2.getX() + worldIn.rand.nextDouble(), blockpos2.getY() + worldIn.rand.nextDouble() * 3.9, blockpos2.getZ() + worldIn.rand.nextDouble(), 0.0, 0.0, 0.0, new int[0]);
            }
            for (int k2 = 0; k2 < this.getGolemPattern().getPalmLength(); ++k2) {
                for (int l2 = 0; l2 < this.getGolemPattern().getThumbLength(); ++l2) {
                    final BlockWorldState blockworldstate3 = blockpattern$patternhelper.translateOffset(k2, l2, 0);
                    worldIn.notifyNeighborsRespectDebug(blockworldstate3.getPos(), Blocks.air);
                }
            }
        }
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos).getBlock().blockMaterial.isReplaceable() && World.doesBlockHaveSolidTopSurface(worldIn, pos.down());
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockPumpkin.FACING, placer.getHorizontalFacing().getOpposite());
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockPumpkin.FACING, EnumFacing.getHorizontal(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<EnumFacing>)BlockPumpkin.FACING).getHorizontalIndex();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockPumpkin.FACING });
    }
    
    protected BlockPattern getSnowmanBasePattern() {
        if (this.snowmanBasePattern == null) {
            this.snowmanBasePattern = FactoryBlockPattern.start().aisle(" ", "#", "#").where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.snow))).build();
        }
        return this.snowmanBasePattern;
    }
    
    protected BlockPattern getSnowmanPattern() {
        if (this.snowmanPattern == null) {
            this.snowmanPattern = FactoryBlockPattern.start().aisle("^", "#", "#").where('^', BlockWorldState.hasState(BlockPumpkin.field_181085_Q)).where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.snow))).build();
        }
        return this.snowmanPattern;
    }
    
    protected BlockPattern getGolemBasePattern() {
        if (this.golemBasePattern == null) {
            this.golemBasePattern = FactoryBlockPattern.start().aisle("~ ~", "###", "~#~").where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.iron_block))).where('~', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.air))).build();
        }
        return this.golemBasePattern;
    }
    
    protected BlockPattern getGolemPattern() {
        if (this.golemPattern == null) {
            this.golemPattern = FactoryBlockPattern.start().aisle("~^~", "###", "~#~").where('^', BlockWorldState.hasState(BlockPumpkin.field_181085_Q)).where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.iron_block))).where('~', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.air))).build();
        }
        return this.golemPattern;
    }
}
