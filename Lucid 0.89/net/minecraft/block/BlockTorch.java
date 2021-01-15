package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;

import com.google.common.base.Predicate;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class BlockTorch extends Block
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", new Predicate()
    {
        public boolean apply(EnumFacing facing)
        {
            return facing != EnumFacing.DOWN;
        }
        @Override
		public boolean apply(Object p_apply_1_)
        {
            return this.apply((EnumFacing)p_apply_1_);
        }
    });

    protected BlockTorch()
    {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }

    @Override
	public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
	public boolean isFullCube()
    {
        return false;
    }

    private boolean canPlaceOn(World worldIn, BlockPos pos)
    {
        if (World.doesBlockHaveSolidTopSurface(worldIn, pos))
        {
            return true;
        }
        else
        {
            Block var3 = worldIn.getBlockState(pos).getBlock();
            return var3 instanceof BlockFence || var3 == Blocks.glass || var3 == Blocks.cobblestone_wall || var3 == Blocks.stained_glass;
        }
    }

    @Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        Iterator var3 = FACING.getAllowedValues().iterator();
        EnumFacing var4;

        do
        {
            if (!var3.hasNext())
            {
                return false;
            }

            var4 = (EnumFacing)var3.next();
        }
        while (!this.canPlaceAt(worldIn, pos, var4));

        return true;
    }

    private boolean canPlaceAt(World worldIn, BlockPos pos, EnumFacing facing)
    {
        BlockPos var4 = pos.offset(facing.getOpposite());
        boolean var5 = facing.getAxis().isHorizontal();
        return var5 && worldIn.isBlockNormalCube(var4, true) || facing.equals(EnumFacing.UP) && this.canPlaceOn(worldIn, var4);
    }

    @Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        if (this.canPlaceAt(worldIn, pos, facing))
        {
            return this.getDefaultState().withProperty(FACING, facing);
        }
        else
        {
            Iterator var9 = EnumFacing.Plane.HORIZONTAL.iterator();
            EnumFacing var10;

            do
            {
                if (!var9.hasNext())
                {
                    return this.getDefaultState();
                }

                var10 = (EnumFacing)var9.next();
            }
            while (!worldIn.isBlockNormalCube(pos.offset(var10.getOpposite()), true));

            return this.getDefaultState().withProperty(FACING, var10);
        }
    }

    @Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        this.checkForDrop(worldIn, pos, state);
    }

    /**
     * Called when a neighboring block changes.
     */
    @Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        this.onNeighborChangeInternal(worldIn, pos, state);
    }

    protected boolean onNeighborChangeInternal(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!this.checkForDrop(worldIn, pos, state))
        {
            return true;
        }
        else
        {
            EnumFacing var4 = (EnumFacing)state.getValue(FACING);
            EnumFacing.Axis var5 = var4.getAxis();
            EnumFacing var6 = var4.getOpposite();
            boolean var7 = false;

            if (var5.isHorizontal() && !worldIn.isBlockNormalCube(pos.offset(var6), true))
            {
                var7 = true;
            }
            else if (var5.isVertical() && !this.canPlaceOn(worldIn, pos.offset(var6)))
            {
                var7 = true;
            }

            if (var7)
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    protected boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state)
    {
        if (state.getBlock() == this && this.canPlaceAt(worldIn, pos, (EnumFacing)state.getValue(FACING)))
        {
            return true;
        }
        else
        {
            if (worldIn.getBlockState(pos).getBlock() == this)
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }

            return false;
        }
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit.
     *  
     * @param start The start vector
     * @param end The end vector
     */
    @Override
	public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end)
    {
        EnumFacing var5 = (EnumFacing)worldIn.getBlockState(pos).getValue(FACING);
        float var6 = 0.15F;

        if (var5 == EnumFacing.EAST)
        {
            this.setBlockBounds(0.0F, 0.2F, 0.5F - var6, var6 * 2.0F, 0.8F, 0.5F + var6);
        }
        else if (var5 == EnumFacing.WEST)
        {
            this.setBlockBounds(1.0F - var6 * 2.0F, 0.2F, 0.5F - var6, 1.0F, 0.8F, 0.5F + var6);
        }
        else if (var5 == EnumFacing.SOUTH)
        {
            this.setBlockBounds(0.5F - var6, 0.2F, 0.0F, 0.5F + var6, 0.8F, var6 * 2.0F);
        }
        else if (var5 == EnumFacing.NORTH)
        {
            this.setBlockBounds(0.5F - var6, 0.2F, 1.0F - var6 * 2.0F, 0.5F + var6, 0.8F, 1.0F);
        }
        else
        {
            var6 = 0.1F;
            this.setBlockBounds(0.5F - var6, 0.0F, 0.5F - var6, 0.5F + var6, 0.6F, 0.5F + var6);
        }

        return super.collisionRayTrace(worldIn, pos, start, end);
    }

    @Override
	public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        EnumFacing var5 = (EnumFacing)state.getValue(FACING);
        double var6 = pos.getX() + 0.5D;
        double var8 = pos.getY() + 0.7D;
        double var10 = pos.getZ() + 0.5D;
        double var12 = 0.22D;
        double var14 = 0.27D;

        if (var5.getAxis().isHorizontal())
        {
            EnumFacing var16 = var5.getOpposite();
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var6 + var14 * var16.getFrontOffsetX(), var8 + var12, var10 + var14 * var16.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D, new int[0]);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, var6 + var14 * var16.getFrontOffsetX(), var8 + var12, var10 + var14 * var16.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D, new int[0]);
        }
        else
        {
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var6, var8, var10, 0.0D, 0.0D, 0.0D, new int[0]);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, var6, var8, var10, 0.0D, 0.0D, 0.0D, new int[0]);
        }
    }

    @Override
	public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
	public IBlockState getStateFromMeta(int meta)
    {
        IBlockState var2 = this.getDefaultState();

        switch (meta)
        {
            case 1:
                var2 = var2.withProperty(FACING, EnumFacing.EAST);
                break;

            case 2:
                var2 = var2.withProperty(FACING, EnumFacing.WEST);
                break;

            case 3:
                var2 = var2.withProperty(FACING, EnumFacing.SOUTH);
                break;

            case 4:
                var2 = var2.withProperty(FACING, EnumFacing.NORTH);
                break;

            case 5:
            default:
                var2 = var2.withProperty(FACING, EnumFacing.UP);
        }

        return var2;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
        byte var2 = 0;
        int var3;

        switch (BlockTorch.SwitchEnumFacing.FACING_LOOKUP[((EnumFacing)state.getValue(FACING)).ordinal()])
        {
            case 1:
                var3 = var2 | 1;
                break;

            case 2:
                var3 = var2 | 2;
                break;

            case 3:
                var3 = var2 | 3;
                break;

            case 4:
                var3 = var2 | 4;
                break;

            case 5:
            case 6:
            default:
                var3 = var2 | 5;
        }

        return var3;
    }

    @Override
	protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {FACING});
    }

    static final class SwitchEnumFacing
    {
        static final int[] FACING_LOOKUP = new int[EnumFacing.values().length];

        static
        {
            try
            {
                FACING_LOOKUP[EnumFacing.EAST.ordinal()] = 1;
            }
            catch (NoSuchFieldError var6)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.WEST.ordinal()] = 2;
            }
            catch (NoSuchFieldError var5)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.SOUTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.NORTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.DOWN.ordinal()] = 5;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.UP.ordinal()] = 6;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
