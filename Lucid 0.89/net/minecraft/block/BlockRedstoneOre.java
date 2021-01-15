package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class BlockRedstoneOre extends Block
{
    private final boolean isOn;

    public BlockRedstoneOre(boolean isOn)
    {
        super(Material.rock);

        if (isOn)
        {
            this.setTickRandomly(true);
        }

        this.isOn = isOn;
    }

    /**
     * How many world ticks before ticking
     */
    @Override
	public int tickRate(World worldIn)
    {
        return 30;
    }

    @Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
    {
        this.activate(worldIn, pos);
        super.onBlockClicked(worldIn, pos, playerIn);
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the block)
     */
    @Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn)
    {
        this.activate(worldIn, pos);
        super.onEntityCollidedWithBlock(worldIn, pos, entityIn);
    }

    @Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        this.activate(worldIn, pos);
        return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
    }

    private void activate(World worldIn, BlockPos pos)
    {
        this.spawnParticles(worldIn, pos);

        if (this == Blocks.redstone_ore)
        {
            worldIn.setBlockState(pos, Blocks.lit_redstone_ore.getDefaultState());
        }
    }

    @Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (this == Blocks.lit_redstone_ore)
        {
            worldIn.setBlockState(pos, Blocks.redstone_ore.getDefaultState());
        }
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *  
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    @Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.redstone;
    }

    /**
     * Get the quantity dropped based on the given fortune level
     */
    @Override
	public int quantityDroppedWithBonus(int fortune, Random random)
    {
        return this.quantityDropped(random) + random.nextInt(fortune + 1);
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
	public int quantityDropped(Random random)
    {
        return 4 + random.nextInt(2);
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     *  
     * @param chance The chance that each Item is actually spawned (1.0 = always, 0.0 = never)
     * @param fortune The player's fortune level
     */
    @Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);

        if (this.getItemDropped(state, worldIn.rand, fortune) != Item.getItemFromBlock(this))
        {
            int var6 = 1 + worldIn.rand.nextInt(5);
            this.dropXpOnBlockBreak(worldIn, pos, var6);
        }
    }

    @Override
	public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (this.isOn)
        {
            this.spawnParticles(worldIn, pos);
        }
    }

    private void spawnParticles(World worldIn, BlockPos pos)
    {
        Random var3 = worldIn.rand;
        double var4 = 0.0625D;

        for (int var6 = 0; var6 < 6; ++var6)
        {
            double var7 = pos.getX() + var3.nextFloat();
            double var9 = pos.getY() + var3.nextFloat();
            double var11 = pos.getZ() + var3.nextFloat();

            if (var6 == 0 && !worldIn.getBlockState(pos.up()).getBlock().isOpaqueCube())
            {
                var9 = pos.getY() + var4 + 1.0D;
            }

            if (var6 == 1 && !worldIn.getBlockState(pos.down()).getBlock().isOpaqueCube())
            {
                var9 = pos.getY() - var4;
            }

            if (var6 == 2 && !worldIn.getBlockState(pos.south()).getBlock().isOpaqueCube())
            {
                var11 = pos.getZ() + var4 + 1.0D;
            }

            if (var6 == 3 && !worldIn.getBlockState(pos.north()).getBlock().isOpaqueCube())
            {
                var11 = pos.getZ() - var4;
            }

            if (var6 == 4 && !worldIn.getBlockState(pos.east()).getBlock().isOpaqueCube())
            {
                var7 = pos.getX() + var4 + 1.0D;
            }

            if (var6 == 5 && !worldIn.getBlockState(pos.west()).getBlock().isOpaqueCube())
            {
                var7 = pos.getX() - var4;
            }

            if (var7 < pos.getX() || var7 > pos.getX() + 1 || var9 < 0.0D || var9 > pos.getY() + 1 || var11 < pos.getZ() || var11 > pos.getZ() + 1)
            {
                worldIn.spawnParticle(EnumParticleTypes.REDSTONE, var7, var9, var11, 0.0D, 0.0D, 0.0D, new int[0]);
            }
        }
    }

    @Override
	protected ItemStack createStackedBlock(IBlockState state)
    {
        return new ItemStack(Blocks.redstone_ore);
    }
}
