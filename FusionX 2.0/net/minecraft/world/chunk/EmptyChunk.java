package net.minecraft.world.chunk;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class EmptyChunk extends Chunk
{
    private static final String __OBFID = "CL_00000372";

    public EmptyChunk(World worldIn, int x, int z)
    {
        super(worldIn, x, z);
    }

    /**
     * Checks whether the chunk is at the X/Z location specified
     */
    public boolean isAtLocation(int x, int z)
    {
        return x == this.xPosition && z == this.zPosition;
    }

    /**
     * Returns the value in the height map at this x, z coordinate in the chunk
     */
    public int getHeight(int x, int z)
    {
        return 0;
    }

    /**
     * Generates the height map for a chunk from scratch
     */
    public void generateHeightMap() {}

    /**
     * Generates the initial skylight map for the chunk upon generation or load.
     */
    public void generateSkylightMap() {}

    public Block getBlock(BlockPos pos)
    {
        return Blocks.air;
    }

    public int getBlockLightOpacity(BlockPos pos)
    {
        return 255;
    }

    public int getBlockMetadata(BlockPos pos)
    {
        return 0;
    }

    public int getLightFor(EnumSkyBlock p_177413_1_, BlockPos p_177413_2_)
    {
        return p_177413_1_.defaultLightValue;
    }

    public void setLightFor(EnumSkyBlock p_177431_1_, BlockPos p_177431_2_, int p_177431_3_) {}

    public int setLight(BlockPos p_177443_1_, int p_177443_2_)
    {
        return 0;
    }

    /**
     * Adds an entity to the chunk. Args: entity
     */
    public void addEntity(Entity entityIn) {}

    /**
     * removes entity using its y chunk coordinate as its index
     */
    public void removeEntity(Entity p_76622_1_) {}

    /**
     * Removes entity at the specified index from the entity array.
     */
    public void removeEntityAtIndex(Entity p_76608_1_, int p_76608_2_) {}

    public boolean canSeeSky(BlockPos pos)
    {
        return false;
    }

    public TileEntity func_177424_a(BlockPos p_177424_1_, Chunk.EnumCreateEntityType p_177424_2_)
    {
        return null;
    }

    public void addTileEntity(TileEntity tileEntityIn) {}

    public void addTileEntity(BlockPos pos, TileEntity tileEntityIn) {}

    public void removeTileEntity(BlockPos pos) {}

    /**
     * Called when this Chunk is loaded by the ChunkProvider
     */
    public void onChunkLoad() {}

    /**
     * Called when this Chunk is unloaded by the ChunkProvider
     */
    public void onChunkUnload() {}

    /**
     * Sets the isModified flag for this Chunk
     */
    public void setChunkModified() {}

    public void func_177414_a(Entity p_177414_1_, AxisAlignedBB p_177414_2_, List p_177414_3_, Predicate p_177414_4_) {}

    public void func_177430_a(Class p_177430_1_, AxisAlignedBB p_177430_2_, List p_177430_3_, Predicate p_177430_4_) {}

    /**
     * Returns true if this Chunk needs to be saved
     */
    public boolean needsSaving(boolean p_76601_1_)
    {
        return false;
    }

    public Random getRandomWithSeed(long seed)
    {
        return new Random(this.getWorld().getSeed() + (long)(this.xPosition * this.xPosition * 4987142) + (long)(this.xPosition * 5947611) + (long)(this.zPosition * this.zPosition) * 4392871L + (long)(this.zPosition * 389711) ^ seed);
    }

    public boolean isEmpty()
    {
        return true;
    }

    /**
     * Returns whether the ExtendedBlockStorages containing levels (in blocks) from arg 1 to arg 2 are fully empty
     * (true) or not (false).
     */
    public boolean getAreLevelsEmpty(int p_76606_1_, int p_76606_2_)
    {
        return true;
    }
}
