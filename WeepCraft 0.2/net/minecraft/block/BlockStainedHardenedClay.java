package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockStainedHardenedClay extends BlockColored
{
    private static final MapColor[] field_193389_b = new MapColor[] {MapColor.field_193561_M, MapColor.field_193562_N, MapColor.field_193563_O, MapColor.field_193564_P, MapColor.field_193565_Q, MapColor.field_193566_R, MapColor.field_193567_S, MapColor.field_193568_T, MapColor.field_193569_U, MapColor.field_193570_V, MapColor.field_193571_W, MapColor.field_193572_X, MapColor.field_193573_Y, MapColor.field_193574_Z, MapColor.field_193559_aa, MapColor.field_193560_ab};

    public BlockStainedHardenedClay()
    {
        super(Material.ROCK);
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     */
    public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_)
    {
        return field_193389_b[((EnumDyeColor)state.getValue(COLOR)).getMetadata()];
    }
}
