package net.minecraft.block.state.pattern;

import com.google.common.base.Predicate;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import java.util.Iterator;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

public class BlockPattern
{
    private final Predicate[][][] field_177689_a;
    private final int field_177687_b;
    private final int field_177688_c;
    private final int field_177686_d;
    private static final String __OBFID = "CL_00002024";

    public BlockPattern(Predicate[][][] p_i45657_1_)
    {
        this.field_177689_a = p_i45657_1_;
        this.field_177687_b = p_i45657_1_.length;

        if (this.field_177687_b > 0)
        {
            this.field_177688_c = p_i45657_1_[0].length;

            if (this.field_177688_c > 0)
            {
                this.field_177686_d = p_i45657_1_[0][0].length;
            }
            else
            {
                this.field_177686_d = 0;
            }
        }
        else
        {
            this.field_177688_c = 0;
            this.field_177686_d = 0;
        }
    }

    public int func_177685_b()
    {
        return this.field_177688_c;
    }

    public int func_177684_c()
    {
        return this.field_177686_d;
    }

    private BlockPattern.PatternHelper func_177682_a(BlockPos p_177682_1_, EnumFacing p_177682_2_, EnumFacing p_177682_3_, LoadingCache p_177682_4_)
    {
        for (int var5 = 0; var5 < this.field_177686_d; ++var5)
        {
            for (int var6 = 0; var6 < this.field_177688_c; ++var6)
            {
                for (int var7 = 0; var7 < this.field_177687_b; ++var7)
                {
                    if (!this.field_177689_a[var7][var6][var5].apply(p_177682_4_.getUnchecked(func_177683_a(p_177682_1_, p_177682_2_, p_177682_3_, var5, var6, var7))))
                    {
                        return null;
                    }
                }
            }
        }

        return new BlockPattern.PatternHelper(p_177682_1_, p_177682_2_, p_177682_3_, p_177682_4_);
    }

    public BlockPattern.PatternHelper func_177681_a(World worldIn, BlockPos p_177681_2_)
    {
        LoadingCache var3 = CacheBuilder.newBuilder().build(new BlockPattern.CacheLoader(worldIn));
        int var4 = Math.max(Math.max(this.field_177686_d, this.field_177688_c), this.field_177687_b);
        Iterator var5 = BlockPos.getAllInBox(p_177681_2_, p_177681_2_.add(var4 - 1, var4 - 1, var4 - 1)).iterator();

        while (var5.hasNext())
        {
            BlockPos var6 = (BlockPos)var5.next();
            EnumFacing[] var7 = EnumFacing.values();
            int var8 = var7.length;

            for (int var9 = 0; var9 < var8; ++var9)
            {
                EnumFacing var10 = var7[var9];
                EnumFacing[] var11 = EnumFacing.values();
                int var12 = var11.length;

                for (int var13 = 0; var13 < var12; ++var13)
                {
                    EnumFacing var14 = var11[var13];

                    if (var14 != var10 && var14 != var10.getOpposite())
                    {
                        BlockPattern.PatternHelper var15 = this.func_177682_a(var6, var10, var14, var3);

                        if (var15 != null)
                        {
                            return var15;
                        }
                    }
                }
            }
        }

        return null;
    }

    protected static BlockPos func_177683_a(BlockPos p_177683_0_, EnumFacing p_177683_1_, EnumFacing p_177683_2_, int p_177683_3_, int p_177683_4_, int p_177683_5_)
    {
        if (p_177683_1_ != p_177683_2_ && p_177683_1_ != p_177683_2_.getOpposite())
        {
            Vec3i var6 = new Vec3i(p_177683_1_.getFrontOffsetX(), p_177683_1_.getFrontOffsetY(), p_177683_1_.getFrontOffsetZ());
            Vec3i var7 = new Vec3i(p_177683_2_.getFrontOffsetX(), p_177683_2_.getFrontOffsetY(), p_177683_2_.getFrontOffsetZ());
            Vec3i var8 = var6.crossProduct(var7);
            return p_177683_0_.add(var7.getX() * -p_177683_4_ + var8.getX() * p_177683_3_ + var6.getX() * p_177683_5_, var7.getY() * -p_177683_4_ + var8.getY() * p_177683_3_ + var6.getY() * p_177683_5_, var7.getZ() * -p_177683_4_ + var8.getZ() * p_177683_3_ + var6.getZ() * p_177683_5_);
        }
        else
        {
            throw new IllegalArgumentException("Invalid forwards & up combination");
        }
    }

    static class CacheLoader extends com.google.common.cache.CacheLoader
    {
        private final World field_177680_a;
        private static final String __OBFID = "CL_00002023";

        public CacheLoader(World worldIn)
        {
            this.field_177680_a = worldIn;
        }

        public BlockWorldState func_177679_a(BlockPos p_177679_1_)
        {
            return new BlockWorldState(this.field_177680_a, p_177679_1_);
        }

        public Object load(Object p_load_1_)
        {
            return this.func_177679_a((BlockPos)p_load_1_);
        }
    }

    public static class PatternHelper
    {
        private final BlockPos field_177674_a;
        private final EnumFacing field_177672_b;
        private final EnumFacing field_177673_c;
        private final LoadingCache field_177671_d;
        private static final String __OBFID = "CL_00002022";

        public PatternHelper(BlockPos p_i45655_1_, EnumFacing p_i45655_2_, EnumFacing p_i45655_3_, LoadingCache p_i45655_4_)
        {
            this.field_177674_a = p_i45655_1_;
            this.field_177672_b = p_i45655_2_;
            this.field_177673_c = p_i45655_3_;
            this.field_177671_d = p_i45655_4_;
        }

        public EnumFacing func_177669_b()
        {
            return this.field_177672_b;
        }

        public EnumFacing func_177668_c()
        {
            return this.field_177673_c;
        }

        public BlockWorldState func_177670_a(int p_177670_1_, int p_177670_2_, int p_177670_3_)
        {
            return (BlockWorldState)this.field_177671_d.getUnchecked(BlockPattern.func_177683_a(this.field_177674_a, this.func_177669_b(), this.func_177668_c(), p_177670_1_, p_177670_2_, p_177670_3_));
        }
    }
}
