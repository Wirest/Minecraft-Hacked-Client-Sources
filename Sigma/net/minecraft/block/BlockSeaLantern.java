package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;

public class BlockSeaLantern extends Block {
    private static final String __OBFID = "CL_00002066";

    public BlockSeaLantern(Material p_i45685_1_) {
        super(p_i45685_1_);
        setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random random) {
        return 2 + random.nextInt(2);
    }

    /**
     * Get the quantity dropped based on the given fortune level
     */
    @Override
    public int quantityDroppedWithBonus(int fortune, Random random) {
        return MathHelper.clamp_int(quantityDropped(random) + random.nextInt(fortune + 1), 1, 5);
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.prismarine_crystals;
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     */
    @Override
    public MapColor getMapColor(IBlockState state) {
        return MapColor.quartzColor;
    }

    @Override
    protected boolean canSilkHarvest() {
        return true;
    }
}
