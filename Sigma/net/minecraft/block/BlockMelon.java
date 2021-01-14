package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class BlockMelon extends Block {
    private static final String __OBFID = "CL_00000267";

    protected BlockMelon() {
        super(Material.gourd);
        setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.melon;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random random) {
        return 3 + random.nextInt(5);
    }

    /**
     * Get the quantity dropped based on the given fortune level
     */
    @Override
    public int quantityDroppedWithBonus(int fortune, Random random) {
        return Math.min(9, quantityDropped(random) + random.nextInt(1 + fortune));
    }
}
