package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPortal extends BlockBreakable
{
    public static final PropertyEnum AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class, new EnumFacing.Axis[] {EnumFacing.Axis.X, EnumFacing.Axis.Z});

    public BlockPortal()
    {
        super(Material.portal, false);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.X));
        this.setTickRandomly(true);
    }

    @Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        super.updateTick(worldIn, pos, state, rand);

        if (worldIn.provider.isSurfaceWorld() && worldIn.getGameRules().getGameRuleBooleanValue("doMobSpawning") && rand.nextInt(2000) < worldIn.getDifficulty().getDifficultyId())
        {
            int var5 = pos.getY();
            BlockPos var6;

            for (var6 = pos; !World.doesBlockHaveSolidTopSurface(worldIn, var6) && var6.getY() > 0; var6 = var6.down())
            {
                ;
            }

            if (var5 > 0 && !worldIn.getBlockState(var6.up()).getBlock().isNormalCube())
            {
                Entity var7 = ItemMonsterPlacer.spawnCreature(worldIn, 57, var6.getX() + 0.5D, var6.getY() + 1.1D, var6.getZ() + 0.5D);

                if (var7 != null)
                {
                    var7.timeUntilPortal = var7.getPortalCooldown();
                }
            }
        }
    }

    @Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }

    @Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        EnumFacing.Axis var3 = (EnumFacing.Axis)worldIn.getBlockState(pos).getValue(AXIS);
        float var4 = 0.125F;
        float var5 = 0.125F;

        if (var3 == EnumFacing.Axis.X)
        {
            var4 = 0.5F;
        }

        if (var3 == EnumFacing.Axis.Z)
        {
            var5 = 0.5F;
        }

        this.setBlockBounds(0.5F - var4, 0.0F, 0.5F - var5, 0.5F + var4, 1.0F, 0.5F + var5);
    }

    public static int getMetaForAxis(EnumFacing.Axis axis)
    {
        return axis == EnumFacing.Axis.X ? 1 : (axis == EnumFacing.Axis.Z ? 2 : 0);
    }

    @Override
	public boolean isFullCube()
    {
        return false;
    }

    public boolean func_176548_d(World worldIn, BlockPos p_176548_2_)
    {
        BlockPortal.Size var3 = new BlockPortal.Size(worldIn, p_176548_2_, EnumFacing.Axis.X);

        if (var3.func_150860_b() && var3.field_150864_e == 0)
        {
            var3.func_150859_c();
            return true;
        }
        else
        {
            BlockPortal.Size var4 = new BlockPortal.Size(worldIn, p_176548_2_, EnumFacing.Axis.Z);

            if (var4.func_150860_b() && var4.field_150864_e == 0)
            {
                var4.func_150859_c();
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    /**
     * Called when a neighboring block changes.
     */
    @Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        EnumFacing.Axis var5 = (EnumFacing.Axis)state.getValue(AXIS);
        BlockPortal.Size var6;

        if (var5 == EnumFacing.Axis.X)
        {
            var6 = new BlockPortal.Size(worldIn, pos, EnumFacing.Axis.X);

            if (!var6.func_150860_b() || var6.field_150864_e < var6.field_150868_h * var6.field_150862_g)
            {
                worldIn.setBlockState(pos, Blocks.air.getDefaultState());
            }
        }
        else if (var5 == EnumFacing.Axis.Z)
        {
            var6 = new BlockPortal.Size(worldIn, pos, EnumFacing.Axis.Z);

            if (!var6.func_150860_b() || var6.field_150864_e < var6.field_150868_h * var6.field_150862_g)
            {
                worldIn.setBlockState(pos, Blocks.air.getDefaultState());
            }
        }
    }

    @Override
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        EnumFacing.Axis var4 = null;
        IBlockState var5 = worldIn.getBlockState(pos);

        if (worldIn.getBlockState(pos).getBlock() == this)
        {
            var4 = (EnumFacing.Axis)var5.getValue(AXIS);

            if (var4 == null)
            {
                return false;
            }

            if (var4 == EnumFacing.Axis.Z && side != EnumFacing.EAST && side != EnumFacing.WEST)
            {
                return false;
            }

            if (var4 == EnumFacing.Axis.X && side != EnumFacing.SOUTH && side != EnumFacing.NORTH)
            {
                return false;
            }
        }

        boolean var6 = worldIn.getBlockState(pos.west()).getBlock() == this && worldIn.getBlockState(pos.west(2)).getBlock() != this;
        boolean var7 = worldIn.getBlockState(pos.east()).getBlock() == this && worldIn.getBlockState(pos.east(2)).getBlock() != this;
        boolean var8 = worldIn.getBlockState(pos.north()).getBlock() == this && worldIn.getBlockState(pos.north(2)).getBlock() != this;
        boolean var9 = worldIn.getBlockState(pos.south()).getBlock() == this && worldIn.getBlockState(pos.south(2)).getBlock() != this;
        boolean var10 = var6 || var7 || var4 == EnumFacing.Axis.X;
        boolean var11 = var8 || var9 || var4 == EnumFacing.Axis.Z;
        return var10 && side == EnumFacing.WEST ? true : (var10 && side == EnumFacing.EAST ? true : (var11 && side == EnumFacing.NORTH ? true : var11 && side == EnumFacing.SOUTH));
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
	public int quantityDropped(Random random)
    {
        return 0;
    }

    @Override
	public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    /**
     * Called When an Entity Collided with the Block
     */
    @Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (entityIn.ridingEntity == null && entityIn.riddenByEntity == null)
        {
            entityIn.setInPortal();
        }
    }

    @Override
	public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (rand.nextInt(100) == 0)
        {
            worldIn.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "portal.portal", 0.5F, rand.nextFloat() * 0.4F + 0.8F, false);
        }

        for (int var5 = 0; var5 < 4; ++var5)
        {
            double var6 = pos.getX() + rand.nextFloat();
            double var8 = pos.getY() + rand.nextFloat();
            double var10 = pos.getZ() + rand.nextFloat();
            double var12 = (rand.nextFloat() - 0.5D) * 0.5D;
            double var14 = (rand.nextFloat() - 0.5D) * 0.5D;
            double var16 = (rand.nextFloat() - 0.5D) * 0.5D;
            int var18 = rand.nextInt(2) * 2 - 1;

            if (worldIn.getBlockState(pos.west()).getBlock() != this && worldIn.getBlockState(pos.east()).getBlock() != this)
            {
                var6 = pos.getX() + 0.5D + 0.25D * var18;
                var12 = rand.nextFloat() * 2.0F * var18;
            }
            else
            {
                var10 = pos.getZ() + 0.5D + 0.25D * var18;
                var16 = rand.nextFloat() * 2.0F * var18;
            }

            worldIn.spawnParticle(EnumParticleTypes.PORTAL, var6, var8, var10, var12, var14, var16, new int[0]);
        }
    }

    @Override
	public Item getItem(World worldIn, BlockPos pos)
    {
        return null;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(AXIS, (meta & 3) == 2 ? EnumFacing.Axis.Z : EnumFacing.Axis.X);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
        return getMetaForAxis((EnumFacing.Axis)state.getValue(AXIS));
    }

    @Override
	protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {AXIS});
    }

    public static class Size
    {
        private final World world;
        private final EnumFacing.Axis axis;
        private final EnumFacing field_150866_c;
        private final EnumFacing field_150863_d;
        private int field_150864_e = 0;
        private BlockPos field_150861_f;
        private int field_150862_g;
        private int field_150868_h;

        public Size(World worldIn, BlockPos p_i45694_2_, EnumFacing.Axis p_i45694_3_)
        {
            this.world = worldIn;
            this.axis = p_i45694_3_;

            if (p_i45694_3_ == EnumFacing.Axis.X)
            {
                this.field_150863_d = EnumFacing.EAST;
                this.field_150866_c = EnumFacing.WEST;
            }
            else
            {
                this.field_150863_d = EnumFacing.NORTH;
                this.field_150866_c = EnumFacing.SOUTH;
            }

            for (BlockPos var4 = p_i45694_2_; p_i45694_2_.getY() > var4.getY() - 21 && p_i45694_2_.getY() > 0 && this.func_150857_a(worldIn.getBlockState(p_i45694_2_.down()).getBlock()); p_i45694_2_ = p_i45694_2_.down())
            {
                ;
            }

            int var5 = this.func_180120_a(p_i45694_2_, this.field_150863_d) - 1;

            if (var5 >= 0)
            {
                this.field_150861_f = p_i45694_2_.offset(this.field_150863_d, var5);
                this.field_150868_h = this.func_180120_a(this.field_150861_f, this.field_150866_c);

                if (this.field_150868_h < 2 || this.field_150868_h > 21)
                {
                    this.field_150861_f = null;
                    this.field_150868_h = 0;
                }
            }

            if (this.field_150861_f != null)
            {
                this.field_150862_g = this.func_150858_a();
            }
        }

        protected int func_180120_a(BlockPos p_180120_1_, EnumFacing p_180120_2_)
        {
            int var3;

            for (var3 = 0; var3 < 22; ++var3)
            {
                BlockPos var4 = p_180120_1_.offset(p_180120_2_, var3);

                if (!this.func_150857_a(this.world.getBlockState(var4).getBlock()) || this.world.getBlockState(var4.down()).getBlock() != Blocks.obsidian)
                {
                    break;
                }
            }

            Block var5 = this.world.getBlockState(p_180120_1_.offset(p_180120_2_, var3)).getBlock();
            return var5 == Blocks.obsidian ? var3 : 0;
        }

        protected int func_150858_a()
        {
            int var1;
            label56:

            for (this.field_150862_g = 0; this.field_150862_g < 21; ++this.field_150862_g)
            {
                for (var1 = 0; var1 < this.field_150868_h; ++var1)
                {
                    BlockPos var2 = this.field_150861_f.offset(this.field_150866_c, var1).up(this.field_150862_g);
                    Block var3 = this.world.getBlockState(var2).getBlock();

                    if (!this.func_150857_a(var3))
                    {
                        break label56;
                    }

                    if (var3 == Blocks.portal)
                    {
                        ++this.field_150864_e;
                    }

                    if (var1 == 0)
                    {
                        var3 = this.world.getBlockState(var2.offset(this.field_150863_d)).getBlock();

                        if (var3 != Blocks.obsidian)
                        {
                            break label56;
                        }
                    }
                    else if (var1 == this.field_150868_h - 1)
                    {
                        var3 = this.world.getBlockState(var2.offset(this.field_150866_c)).getBlock();

                        if (var3 != Blocks.obsidian)
                        {
                            break label56;
                        }
                    }
                }
            }

            for (var1 = 0; var1 < this.field_150868_h; ++var1)
            {
                if (this.world.getBlockState(this.field_150861_f.offset(this.field_150866_c, var1).up(this.field_150862_g)).getBlock() != Blocks.obsidian)
                {
                    this.field_150862_g = 0;
                    break;
                }
            }

            if (this.field_150862_g <= 21 && this.field_150862_g >= 3)
            {
                return this.field_150862_g;
            }
            else
            {
                this.field_150861_f = null;
                this.field_150868_h = 0;
                this.field_150862_g = 0;
                return 0;
            }
        }

        protected boolean func_150857_a(Block p_150857_1_)
        {
            return p_150857_1_.blockMaterial == Material.air || p_150857_1_ == Blocks.fire || p_150857_1_ == Blocks.portal;
        }

        public boolean func_150860_b()
        {
            return this.field_150861_f != null && this.field_150868_h >= 2 && this.field_150868_h <= 21 && this.field_150862_g >= 3 && this.field_150862_g <= 21;
        }

        public void func_150859_c()
        {
            for (int var1 = 0; var1 < this.field_150868_h; ++var1)
            {
                BlockPos var2 = this.field_150861_f.offset(this.field_150866_c, var1);

                for (int var3 = 0; var3 < this.field_150862_g; ++var3)
                {
                    this.world.setBlockState(var2.up(var3), Blocks.portal.getDefaultState().withProperty(BlockPortal.AXIS, this.axis), 2);
                }
            }
        }
    }
}
