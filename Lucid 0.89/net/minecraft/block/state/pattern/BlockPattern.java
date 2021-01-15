package net.minecraft.block.state.pattern;

import java.util.Iterator;

import com.google.common.base.Predicate;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;

import net.minecraft.block.state.BlockWorldState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

public class BlockPattern
{
    private final Predicate[][][] blockMatches;
    private final int fingerLength;
    private final int thumbLength;
    private final int palmLength;

    public BlockPattern(Predicate[][][] predicatesIn)
    {
        this.blockMatches = predicatesIn;
        this.fingerLength = predicatesIn.length;

        if (this.fingerLength > 0)
        {
            this.thumbLength = predicatesIn[0].length;

            if (this.thumbLength > 0)
            {
                this.palmLength = predicatesIn[0][0].length;
            }
            else
            {
                this.palmLength = 0;
            }
        }
        else
        {
            this.thumbLength = 0;
            this.palmLength = 0;
        }
    }

    public int getThumbLength()
    {
        return this.thumbLength;
    }

    public int getPalmLength()
    {
        return this.palmLength;
    }

    /**
     * checks that the given pattern & rotation is at the block co-ordinates.
     */
    private BlockPattern.PatternHelper checkPatternAt(BlockPos pos, EnumFacing finger, EnumFacing thumb, LoadingCache lcache)
    {
        for (int var5 = 0; var5 < this.palmLength; ++var5)
        {
            for (int var6 = 0; var6 < this.thumbLength; ++var6)
            {
                for (int var7 = 0; var7 < this.fingerLength; ++var7)
                {
                    if (!this.blockMatches[var7][var6][var5].apply(lcache.getUnchecked(translateOffset(pos, finger, thumb, var5, var6, var7))))
                    {
                        return null;
                    }
                }
            }
        }

        return new BlockPattern.PatternHelper(pos, finger, thumb, lcache);
    }

    /**
     * Calculates whether the given world position matches the pattern. Warning, fairly heavy function. @return a
     * BlockPattern.PatternHelper if found, null otherwise.
     */
    public BlockPattern.PatternHelper match(World worldIn, BlockPos pos)
    {
        LoadingCache var3 = CacheBuilder.newBuilder().build(new BlockPattern.CacheLoader(worldIn));
        int var4 = Math.max(Math.max(this.palmLength, this.thumbLength), this.fingerLength);
        Iterator var5 = BlockPos.getAllInBox(pos, pos.add(var4 - 1, var4 - 1, var4 - 1)).iterator();

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
                        BlockPattern.PatternHelper var15 = this.checkPatternAt(var6, var10, var14, var3);

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

    /**
     * Offsets the position of pos in the direction of finger and thumb facing by offset amounts, follows the right-hand
     * rule for cross products (finger, thumb, palm) @return A new BlockPos offset in the facing directions
     *  
     * @param pos The original block position
     * @param finger Finger direction
     * @param thumb Thumb direction
     * @param palmOffset An amount to offset in the palm direction. Palm is the direction of the result of the cross-
     * product between finger and thumb
     * @param thumbOffset An amount to offset in the thumb direction
     * @param fingerOffset An amount to offset in the finger direction
     */
    protected static BlockPos translateOffset(BlockPos pos, EnumFacing finger, EnumFacing thumb, int palmOffset, int thumbOffset, int fingerOffset)
    {
        if (finger != thumb && finger != thumb.getOpposite())
        {
            Vec3i var6 = new Vec3i(finger.getFrontOffsetX(), finger.getFrontOffsetY(), finger.getFrontOffsetZ());
            Vec3i var7 = new Vec3i(thumb.getFrontOffsetX(), thumb.getFrontOffsetY(), thumb.getFrontOffsetZ());
            Vec3i var8 = var6.crossProduct(var7);
            return pos.add(var7.getX() * -thumbOffset + var8.getX() * palmOffset + var6.getX() * fingerOffset, var7.getY() * -thumbOffset + var8.getY() * palmOffset + var6.getY() * fingerOffset, var7.getZ() * -thumbOffset + var8.getZ() * palmOffset + var6.getZ() * fingerOffset);
        }
        else
        {
            throw new IllegalArgumentException("Invalid forwards & up combination");
        }
    }

    static class CacheLoader extends com.google.common.cache.CacheLoader
    {
        private final World world;

        public CacheLoader(World worldIn)
        {
            this.world = worldIn;
        }

        public BlockWorldState loadState(BlockPos pos)
        {
            return new BlockWorldState(this.world, pos);
        }

        @Override
		public Object load(Object p_load_1_)
        {
            return this.loadState((BlockPos)p_load_1_);
        }
    }

    public static class PatternHelper
    {
        private final BlockPos pos;
        private final EnumFacing finger;
        private final EnumFacing thumb;
        private final LoadingCache lcache;

        public PatternHelper(BlockPos pos, EnumFacing fingerIn, EnumFacing thumbIn, LoadingCache loadingCache)
        {
            this.pos = pos;
            this.finger = fingerIn;
            this.thumb = thumbIn;
            this.lcache = loadingCache;
        }

        public EnumFacing getFinger()
        {
            return this.finger;
        }

        public EnumFacing getThumb()
        {
            return this.thumb;
        }

        public BlockWorldState translateOffset(int palmOffset, int thumbOffset, int fingerOffset)
        {
            return (BlockWorldState)this.lcache.getUnchecked(BlockPattern.translateOffset(this.pos, this.getFinger(), this.getThumb(), palmOffset, thumbOffset, fingerOffset));
        }
    }
}
