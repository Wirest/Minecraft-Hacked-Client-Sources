// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockWorldState;
import com.google.common.cache.LoadingCache;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.BlockState;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.world.IBlockAccess;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.PropertyEnum;

public class BlockPortal extends BlockBreakable
{
    public static final PropertyEnum<EnumFacing.Axis> AXIS;
    
    static {
        AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class, EnumFacing.Axis.X, EnumFacing.Axis.Z);
    }
    
    public BlockPortal() {
        super(Material.portal, false);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockPortal.AXIS, EnumFacing.Axis.X));
        this.setTickRandomly(true);
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        super.updateTick(worldIn, pos, state, rand);
        if (worldIn.provider.isSurfaceWorld() && worldIn.getGameRules().getBoolean("doMobSpawning") && rand.nextInt(2000) < worldIn.getDifficulty().getDifficultyId()) {
            final int i = pos.getY();
            BlockPos blockpos;
            for (blockpos = pos; !World.doesBlockHaveSolidTopSurface(worldIn, blockpos) && blockpos.getY() > 0; blockpos = blockpos.down()) {}
            if (i > 0 && !worldIn.getBlockState(blockpos.up()).getBlock().isNormalCube()) {
                final Entity entity = ItemMonsterPlacer.spawnCreature(worldIn, 57, blockpos.getX() + 0.5, blockpos.getY() + 1.1, blockpos.getZ() + 0.5);
                if (entity != null) {
                    entity.timeUntilPortal = entity.getPortalCooldown();
                }
            }
        }
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        return null;
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        final EnumFacing.Axis enumfacing$axis = worldIn.getBlockState(pos).getValue(BlockPortal.AXIS);
        float f = 0.125f;
        float f2 = 0.125f;
        if (enumfacing$axis == EnumFacing.Axis.X) {
            f = 0.5f;
        }
        if (enumfacing$axis == EnumFacing.Axis.Z) {
            f2 = 0.5f;
        }
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f2, 0.5f + f, 1.0f, 0.5f + f2);
    }
    
    public static int getMetaForAxis(final EnumFacing.Axis axis) {
        return (axis == EnumFacing.Axis.X) ? 1 : ((axis == EnumFacing.Axis.Z) ? 2 : 0);
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    public boolean func_176548_d(final World worldIn, final BlockPos p_176548_2_) {
        final Size blockportal$size = new Size(worldIn, p_176548_2_, EnumFacing.Axis.X);
        if (blockportal$size.func_150860_b() && blockportal$size.field_150864_e == 0) {
            blockportal$size.func_150859_c();
            return true;
        }
        final Size blockportal$size2 = new Size(worldIn, p_176548_2_, EnumFacing.Axis.Z);
        if (blockportal$size2.func_150860_b() && blockportal$size2.field_150864_e == 0) {
            blockportal$size2.func_150859_c();
            return true;
        }
        return false;
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        final EnumFacing.Axis enumfacing$axis = state.getValue(BlockPortal.AXIS);
        if (enumfacing$axis == EnumFacing.Axis.X) {
            final Size blockportal$size = new Size(worldIn, pos, EnumFacing.Axis.X);
            if (!blockportal$size.func_150860_b() || blockportal$size.field_150864_e < blockportal$size.field_150868_h * blockportal$size.field_150862_g) {
                worldIn.setBlockState(pos, Blocks.air.getDefaultState());
            }
        }
        else if (enumfacing$axis == EnumFacing.Axis.Z) {
            final Size blockportal$size2 = new Size(worldIn, pos, EnumFacing.Axis.Z);
            if (!blockportal$size2.func_150860_b() || blockportal$size2.field_150864_e < blockportal$size2.field_150868_h * blockportal$size2.field_150862_g) {
                worldIn.setBlockState(pos, Blocks.air.getDefaultState());
            }
        }
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        EnumFacing.Axis enumfacing$axis = null;
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        if (worldIn.getBlockState(pos).getBlock() == this) {
            enumfacing$axis = iblockstate.getValue(BlockPortal.AXIS);
            if (enumfacing$axis == null) {
                return false;
            }
            if (enumfacing$axis == EnumFacing.Axis.Z && side != EnumFacing.EAST && side != EnumFacing.WEST) {
                return false;
            }
            if (enumfacing$axis == EnumFacing.Axis.X && side != EnumFacing.SOUTH && side != EnumFacing.NORTH) {
                return false;
            }
        }
        final boolean flag = worldIn.getBlockState(pos.west()).getBlock() == this && worldIn.getBlockState(pos.west(2)).getBlock() != this;
        final boolean flag2 = worldIn.getBlockState(pos.east()).getBlock() == this && worldIn.getBlockState(pos.east(2)).getBlock() != this;
        final boolean flag3 = worldIn.getBlockState(pos.north()).getBlock() == this && worldIn.getBlockState(pos.north(2)).getBlock() != this;
        final boolean flag4 = worldIn.getBlockState(pos.south()).getBlock() == this && worldIn.getBlockState(pos.south(2)).getBlock() != this;
        final boolean flag5 = flag || flag2 || enumfacing$axis == EnumFacing.Axis.X;
        final boolean flag6 = flag3 || flag4 || enumfacing$axis == EnumFacing.Axis.Z;
        return (flag5 && side == EnumFacing.WEST) || (flag5 && side == EnumFacing.EAST) || (flag6 && side == EnumFacing.NORTH) || (flag6 && side == EnumFacing.SOUTH);
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }
    
    @Override
    public void onEntityCollidedWithBlock(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
        if (entityIn.ridingEntity == null && entityIn.riddenByEntity == null) {
            entityIn.func_181015_d(pos);
        }
    }
    
    @Override
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (rand.nextInt(100) == 0) {
            worldIn.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "portal.portal", 0.5f, rand.nextFloat() * 0.4f + 0.8f, false);
        }
        for (int i = 0; i < 4; ++i) {
            double d0 = pos.getX() + rand.nextFloat();
            final double d2 = pos.getY() + rand.nextFloat();
            double d3 = pos.getZ() + rand.nextFloat();
            double d4 = (rand.nextFloat() - 0.5) * 0.5;
            final double d5 = (rand.nextFloat() - 0.5) * 0.5;
            double d6 = (rand.nextFloat() - 0.5) * 0.5;
            final int j = rand.nextInt(2) * 2 - 1;
            if (worldIn.getBlockState(pos.west()).getBlock() != this && worldIn.getBlockState(pos.east()).getBlock() != this) {
                d0 = pos.getX() + 0.5 + 0.25 * j;
                d4 = rand.nextFloat() * 2.0f * j;
            }
            else {
                d3 = pos.getZ() + 0.5 + 0.25 * j;
                d6 = rand.nextFloat() * 2.0f * j;
            }
            worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d2, d3, d4, d5, d6, new int[0]);
        }
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return null;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockPortal.AXIS, ((meta & 0x3) == 0x2) ? EnumFacing.Axis.Z : EnumFacing.Axis.X);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return getMetaForAxis(state.getValue(BlockPortal.AXIS));
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockPortal.AXIS });
    }
    
    public BlockPattern.PatternHelper func_181089_f(final World p_181089_1_, final BlockPos p_181089_2_) {
        EnumFacing.Axis enumfacing$axis = EnumFacing.Axis.Z;
        Size blockportal$size = new Size(p_181089_1_, p_181089_2_, EnumFacing.Axis.X);
        final LoadingCache<BlockPos, BlockWorldState> loadingcache = BlockPattern.func_181627_a(p_181089_1_, true);
        if (!blockportal$size.func_150860_b()) {
            enumfacing$axis = EnumFacing.Axis.X;
            blockportal$size = new Size(p_181089_1_, p_181089_2_, EnumFacing.Axis.Z);
        }
        if (!blockportal$size.func_150860_b()) {
            return new BlockPattern.PatternHelper(p_181089_2_, EnumFacing.NORTH, EnumFacing.UP, loadingcache, 1, 1, 1);
        }
        final int[] aint = new int[EnumFacing.AxisDirection.values().length];
        final EnumFacing enumfacing = blockportal$size.field_150866_c.rotateYCCW();
        final BlockPos blockpos = blockportal$size.field_150861_f.up(blockportal$size.func_181100_a() - 1);
        EnumFacing.AxisDirection[] values;
        for (int length = (values = EnumFacing.AxisDirection.values()).length, k = 0; k < length; ++k) {
            final EnumFacing.AxisDirection enumfacing$axisdirection = values[k];
            final BlockPattern.PatternHelper blockpattern$patternhelper = new BlockPattern.PatternHelper((enumfacing.getAxisDirection() == enumfacing$axisdirection) ? blockpos : blockpos.offset(blockportal$size.field_150866_c, blockportal$size.func_181101_b() - 1), EnumFacing.func_181076_a(enumfacing$axisdirection, enumfacing$axis), EnumFacing.UP, loadingcache, blockportal$size.func_181101_b(), blockportal$size.func_181100_a(), 1);
            for (int i = 0; i < blockportal$size.func_181101_b(); ++i) {
                for (int j = 0; j < blockportal$size.func_181100_a(); ++j) {
                    final BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(i, j, 1);
                    if (blockworldstate.getBlockState() != null && blockworldstate.getBlockState().getBlock().getMaterial() != Material.air) {
                        final int[] array = aint;
                        final int ordinal = enumfacing$axisdirection.ordinal();
                        ++array[ordinal];
                    }
                }
            }
        }
        EnumFacing.AxisDirection enumfacing$axisdirection2 = EnumFacing.AxisDirection.POSITIVE;
        EnumFacing.AxisDirection[] values2;
        for (int length2 = (values2 = EnumFacing.AxisDirection.values()).length, l = 0; l < length2; ++l) {
            final EnumFacing.AxisDirection enumfacing$axisdirection3 = values2[l];
            if (aint[enumfacing$axisdirection3.ordinal()] < aint[enumfacing$axisdirection2.ordinal()]) {
                enumfacing$axisdirection2 = enumfacing$axisdirection3;
            }
        }
        return new BlockPattern.PatternHelper((enumfacing.getAxisDirection() == enumfacing$axisdirection2) ? blockpos : blockpos.offset(blockportal$size.field_150866_c, blockportal$size.func_181101_b() - 1), EnumFacing.func_181076_a(enumfacing$axisdirection2, enumfacing$axis), EnumFacing.UP, loadingcache, blockportal$size.func_181101_b(), blockportal$size.func_181100_a(), 1);
    }
    
    public static class Size
    {
        private final World world;
        private final EnumFacing.Axis axis;
        private final EnumFacing field_150866_c;
        private final EnumFacing field_150863_d;
        private int field_150864_e;
        private BlockPos field_150861_f;
        private int field_150862_g;
        private int field_150868_h;
        
        public Size(final World worldIn, BlockPos p_i45694_2_, final EnumFacing.Axis p_i45694_3_) {
            this.field_150864_e = 0;
            this.world = worldIn;
            this.axis = p_i45694_3_;
            if (p_i45694_3_ == EnumFacing.Axis.X) {
                this.field_150863_d = EnumFacing.EAST;
                this.field_150866_c = EnumFacing.WEST;
            }
            else {
                this.field_150863_d = EnumFacing.NORTH;
                this.field_150866_c = EnumFacing.SOUTH;
            }
            for (BlockPos blockpos = p_i45694_2_; p_i45694_2_.getY() > blockpos.getY() - 21 && p_i45694_2_.getY() > 0 && this.func_150857_a(worldIn.getBlockState(p_i45694_2_.down()).getBlock()); p_i45694_2_ = p_i45694_2_.down()) {}
            final int i = this.func_180120_a(p_i45694_2_, this.field_150863_d) - 1;
            if (i >= 0) {
                this.field_150861_f = p_i45694_2_.offset(this.field_150863_d, i);
                this.field_150868_h = this.func_180120_a(this.field_150861_f, this.field_150866_c);
                if (this.field_150868_h < 2 || this.field_150868_h > 21) {
                    this.field_150861_f = null;
                    this.field_150868_h = 0;
                }
            }
            if (this.field_150861_f != null) {
                this.field_150862_g = this.func_150858_a();
            }
        }
        
        protected int func_180120_a(final BlockPos p_180120_1_, final EnumFacing p_180120_2_) {
            int i;
            for (i = 0; i < 22; ++i) {
                final BlockPos blockpos = p_180120_1_.offset(p_180120_2_, i);
                if (!this.func_150857_a(this.world.getBlockState(blockpos).getBlock())) {
                    break;
                }
                if (this.world.getBlockState(blockpos.down()).getBlock() != Blocks.obsidian) {
                    break;
                }
            }
            final Block block = this.world.getBlockState(p_180120_1_.offset(p_180120_2_, i)).getBlock();
            return (block == Blocks.obsidian) ? i : 0;
        }
        
        public int func_181100_a() {
            return this.field_150862_g;
        }
        
        public int func_181101_b() {
            return this.field_150868_h;
        }
        
        protected int func_150858_a() {
            this.field_150862_g = 0;
        Label_0181:
            while (this.field_150862_g < 21) {
                for (int i = 0; i < this.field_150868_h; ++i) {
                    final BlockPos blockpos = this.field_150861_f.offset(this.field_150866_c, i).up(this.field_150862_g);
                    Block block = this.world.getBlockState(blockpos).getBlock();
                    if (!this.func_150857_a(block)) {
                        break Label_0181;
                    }
                    if (block == Blocks.portal) {
                        ++this.field_150864_e;
                    }
                    if (i == 0) {
                        block = this.world.getBlockState(blockpos.offset(this.field_150863_d)).getBlock();
                        if (block != Blocks.obsidian) {
                            break Label_0181;
                        }
                    }
                    else if (i == this.field_150868_h - 1) {
                        block = this.world.getBlockState(blockpos.offset(this.field_150866_c)).getBlock();
                        if (block != Blocks.obsidian) {
                            break Label_0181;
                        }
                    }
                }
                ++this.field_150862_g;
            }
            for (int j = 0; j < this.field_150868_h; ++j) {
                if (this.world.getBlockState(this.field_150861_f.offset(this.field_150866_c, j).up(this.field_150862_g)).getBlock() != Blocks.obsidian) {
                    this.field_150862_g = 0;
                    break;
                }
            }
            if (this.field_150862_g <= 21 && this.field_150862_g >= 3) {
                return this.field_150862_g;
            }
            this.field_150861_f = null;
            this.field_150868_h = 0;
            return this.field_150862_g = 0;
        }
        
        protected boolean func_150857_a(final Block p_150857_1_) {
            return p_150857_1_.blockMaterial == Material.air || p_150857_1_ == Blocks.fire || p_150857_1_ == Blocks.portal;
        }
        
        public boolean func_150860_b() {
            return this.field_150861_f != null && this.field_150868_h >= 2 && this.field_150868_h <= 21 && this.field_150862_g >= 3 && this.field_150862_g <= 21;
        }
        
        public void func_150859_c() {
            for (int i = 0; i < this.field_150868_h; ++i) {
                final BlockPos blockpos = this.field_150861_f.offset(this.field_150866_c, i);
                for (int j = 0; j < this.field_150862_g; ++j) {
                    this.world.setBlockState(blockpos.up(j), Blocks.portal.getDefaultState().withProperty(BlockPortal.AXIS, this.axis), 2);
                }
            }
        }
    }
}
