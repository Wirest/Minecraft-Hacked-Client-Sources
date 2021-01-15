package net.minecraft.block.state;

import com.google.common.base.Predicate;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockWorldState
{
    private final World world;
    private final BlockPos pos;
    private IBlockState state;
    private TileEntity tileEntity;
    private boolean tileEntityInitialized;

    public BlockWorldState(World worldIn, BlockPos posIn)
    {
        this.world = worldIn;
        this.pos = posIn;
    }

    public IBlockState getBlockState()
    {
        if (this.state == null && this.world.isBlockLoaded(this.pos))
        {
            this.state = this.world.getBlockState(this.pos);
        }

        return this.state;
    }

    public TileEntity getTileEntity()
    {
        if (this.tileEntity == null && !this.tileEntityInitialized)
        {
            this.tileEntity = this.world.getTileEntity(this.pos);
            this.tileEntityInitialized = true;
        }

        return this.tileEntity;
    }

    public BlockPos getPos()
    {
        return this.pos;
    }

    public static Predicate hasState(final Predicate p_177510_0_)
    {
        return new Predicate()
        {
            public boolean func_177503_a(BlockWorldState p_177503_1_)
            {
                return p_177503_1_ != null && p_177510_0_.apply(p_177503_1_.getBlockState());
            }
            @Override
			public boolean apply(Object p_apply_1_)
            {
                return this.func_177503_a((BlockWorldState)p_apply_1_);
            }
        };
    }
}
