package net.minecraft.block.state;

import com.google.common.base.Predicate;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockWorldState
{
    private final World world;
    private final BlockPos pos;
    private IBlockState field_177514_c;
    private TileEntity field_177511_d;
    private boolean field_177512_e;
    private static final String __OBFID = "CL_00002026";

    public BlockWorldState(World worldIn, BlockPos p_i45659_2_)
    {
        this.world = worldIn;
        this.pos = p_i45659_2_;
    }

    public IBlockState func_177509_a()
    {
        if (this.field_177514_c == null && this.world.isBlockLoaded(this.pos))
        {
            this.field_177514_c = this.world.getBlockState(this.pos);
        }

        return this.field_177514_c;
    }

    public TileEntity func_177507_b()
    {
        if (this.field_177511_d == null && !this.field_177512_e)
        {
            this.field_177511_d = this.world.getTileEntity(this.pos);
            this.field_177512_e = true;
        }

        return this.field_177511_d;
    }

    public BlockPos getPos()
    {
        return this.pos;
    }

    public static Predicate hasState(final Predicate p_177510_0_)
    {
        return new Predicate()
        {
            private static final String __OBFID = "CL_00002025";
            public boolean func_177503_a(BlockWorldState p_177503_1_)
            {
                return p_177503_1_ != null && p_177510_0_.apply(p_177503_1_.func_177509_a());
            }
            public boolean apply(Object p_apply_1_)
            {
                return this.func_177503_a((BlockWorldState)p_apply_1_);
            }
        };
    }
}
