package net.minecraft.block;

import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockAir extends Block
{
    private static Map mapOriginalOpacity = new IdentityHashMap();

    protected BlockAir()
    {
        super(Material.air);
    }

    /**
     * The type of render function called. 3 for standard block models, 2 for TESR's, 1 for liquids, -1 is no render
     */
    public int getRenderType()
    {
        return -1;
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid)
    {
        return false;
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     *  
     * @param chance The chance that each Item is actually spawned (1.0 = always, 0.0 = never)
     * @param fortune The player's fortune level
     */
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
    }

    /**
     * Whether this Block can be replaced directly by other blocks (true for e.g. tall grass)
     */
    public boolean isReplaceable(World worldIn, BlockPos pos)
    {
        return true;
    }

    public static void setLightOpacity(Block p_setLightOpacity_0_, int p_setLightOpacity_1_)
    {
        if (!mapOriginalOpacity.containsKey(p_setLightOpacity_0_))
        {
            mapOriginalOpacity.put(p_setLightOpacity_0_, p_setLightOpacity_0_.lightOpacity);
        }

        p_setLightOpacity_0_.lightOpacity = p_setLightOpacity_1_;
    }

    public static void restoreLightOpacity(Block p_restoreLightOpacity_0_)
    {
        if (mapOriginalOpacity.containsKey(p_restoreLightOpacity_0_))
        {
            int i = (Integer) mapOriginalOpacity.get(p_restoreLightOpacity_0_);
            setLightOpacity(p_restoreLightOpacity_0_, i);
        }
    }
}
