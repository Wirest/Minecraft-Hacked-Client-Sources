package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

public class BlockHugeMushroom extends Block
{
    public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockHugeMushroom.EnumType.class);
    private final Block smallBlock;

    public BlockHugeMushroom(Material materialIn, Block smallBlock)
    {
        super(materialIn);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockHugeMushroom.EnumType.ALL_OUTSIDE));
        this.smallBlock = smallBlock;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
	public int quantityDropped(Random random)
    {
        return Math.max(0, random.nextInt(10) - 7);
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *  
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    @Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(this.smallBlock);
    }

    @Override
	public Item getItem(World worldIn, BlockPos pos)
    {
        return Item.getItemFromBlock(this.smallBlock);
    }

    @Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState();
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(VARIANT, BlockHugeMushroom.EnumType.byMetadata(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
        return ((BlockHugeMushroom.EnumType)state.getValue(VARIANT)).getMetadata();
    }

    @Override
	protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {VARIANT});
    }

    public static enum EnumType implements IStringSerializable
    {
        NORTH_WEST("NORTH_WEST", 0, 1, "north_west"),
        NORTH("NORTH", 1, 2, "north"),
        NORTH_EAST("NORTH_EAST", 2, 3, "north_east"),
        WEST("WEST", 3, 4, "west"),
        CENTER("CENTER", 4, 5, "center"),
        EAST("EAST", 5, 6, "east"),
        SOUTH_WEST("SOUTH_WEST", 6, 7, "south_west"),
        SOUTH("SOUTH", 7, 8, "south"),
        SOUTH_EAST("SOUTH_EAST", 8, 9, "south_east"),
        STEM("STEM", 9, 10, "stem"),
        ALL_INSIDE("ALL_INSIDE", 10, 0, "all_inside"),
        ALL_OUTSIDE("ALL_OUTSIDE", 11, 14, "all_outside"),
        ALL_STEM("ALL_STEM", 12, 15, "all_stem");
        private static final BlockHugeMushroom.EnumType[] META_LOOKUP = new BlockHugeMushroom.EnumType[16];
        private final int meta;
        private final String name; 

        private EnumType(String p_i45710_1_, int p_i45710_2_, int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMetadata()
        {
            return this.meta;
        }

        @Override
		public String toString()
        {
            return this.name;
        }

        public static BlockHugeMushroom.EnumType byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            BlockHugeMushroom.EnumType var1 = META_LOOKUP[meta];
            return var1 == null ? META_LOOKUP[0] : var1;
        }

        @Override
		public String getName()
        {
            return this.name;
        }

        static {
            BlockHugeMushroom.EnumType[] var0 = values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2)
            {
                BlockHugeMushroom.EnumType var3 = var0[var2];
                META_LOOKUP[var3.getMetadata()] = var3;
            }
        }
    }
}
