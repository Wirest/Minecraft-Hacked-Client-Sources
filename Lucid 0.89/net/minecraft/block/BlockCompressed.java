package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;

public class BlockCompressed extends Block
{
    private final MapColor mapColor;

    public BlockCompressed(MapColor mapColorIn)
    {
        super(Material.iron);
        this.mapColor = mapColorIn;
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     */
    @Override
	public MapColor getMapColor(IBlockState state)
    {
        return this.mapColor;
    }
}
