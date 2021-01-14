package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockPackedIce extends Block {
    private static final String __OBFID = "CL_00000283";

    public BlockPackedIce() {
        super(Material.packedIce);
        slipperiness = 0.98F;
        setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random random) {
        return 0;
    }
}
