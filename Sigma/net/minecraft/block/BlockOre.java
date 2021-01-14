package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockOre extends Block {
    private static final String __OBFID = "CL_00000282";

    public BlockOre() {
        super(Material.rock);
        setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return this == Blocks.coal_ore ? Items.coal : (this == Blocks.diamond_ore ? Items.diamond : (this == Blocks.lapis_ore ? Items.dye : (this == Blocks.emerald_ore ? Items.emerald : (this == Blocks.quartz_ore ? Items.quartz : Item.getItemFromBlock(this)))));
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random random) {
        return this == Blocks.lapis_ore ? 4 + random.nextInt(5) : 1;
    }

    /**
     * Get the quantity dropped based on the given fortune level
     */
    @Override
    public int quantityDroppedWithBonus(int fortune, Random random) {
        if (fortune > 0 && Item.getItemFromBlock(this) != getItemDropped((IBlockState) getBlockState().getValidStates().iterator().next(), random, fortune)) {
            int var3 = random.nextInt(fortune + 2) - 1;

            if (var3 < 0) {
                var3 = 0;
            }

            return quantityDropped(random) * (var3 + 1);
        } else {
            return quantityDropped(random);
        }
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     *
     * @param chance  The chance that each Item is actually spawned (1.0 = always,
     *                0.0 = never)
     * @param fortune The player's fortune level
     */
    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);

        if (getItemDropped(state, worldIn.rand, fortune) != Item.getItemFromBlock(this)) {
            int var6 = 0;

            if (this == Blocks.coal_ore) {
                var6 = MathHelper.getRandomIntegerInRange(worldIn.rand, 0, 2);
            } else if (this == Blocks.diamond_ore) {
                var6 = MathHelper.getRandomIntegerInRange(worldIn.rand, 3, 7);
            } else if (this == Blocks.emerald_ore) {
                var6 = MathHelper.getRandomIntegerInRange(worldIn.rand, 3, 7);
            } else if (this == Blocks.lapis_ore) {
                var6 = MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);
            } else if (this == Blocks.quartz_ore) {
                var6 = MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);
            }

            dropXpOnBlockBreak(worldIn, pos, var6);
        }
    }

    @Override
    public int getDamageValue(World worldIn, BlockPos pos) {
        return 0;
    }

    /**
     * Get the damage value that this Block should drop
     */
    @Override
    public int damageDropped(IBlockState state) {
        return this == Blocks.lapis_ore ? EnumDyeColor.BLUE.getDyeColorDamage() : 0;
    }
}
