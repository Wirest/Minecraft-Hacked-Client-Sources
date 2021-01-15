package net.minecraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

public abstract class BlockStoneSlab extends BlockSlab
{
    public static final PropertyBool SEAMLESS = PropertyBool.create("seamless");
    public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockStoneSlab.EnumType.class);

    public BlockStoneSlab()
    {
        super(Material.rock);
        IBlockState var1 = this.blockState.getBaseState();

        if (this.isDouble())
        {
            var1 = var1.withProperty(SEAMLESS, Boolean.valueOf(false));
        }
        else
        {
            var1 = var1.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        }

        this.setDefaultState(var1.withProperty(VARIANT, BlockStoneSlab.EnumType.STONE));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *  
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    @Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(Blocks.stone_slab);
    }

    @Override
	public Item getItem(World worldIn, BlockPos pos)
    {
        return Item.getItemFromBlock(Blocks.stone_slab);
    }

    /**
     * Returns the slab block name with the type associated with it
     */
    @Override
	public String getUnlocalizedName(int meta)
    {
        return super.getUnlocalizedName() + "." + BlockStoneSlab.EnumType.byMetadata(meta).getUnlocalizedName();
    }

    @Override
	public IProperty getVariantProperty()
    {
        return VARIANT;
    }

    @Override
	public Object getVariant(ItemStack stack)
    {
        return BlockStoneSlab.EnumType.byMetadata(stack.getMetadata() & 7);
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        if (itemIn != Item.getItemFromBlock(Blocks.double_stone_slab))
        {
            BlockStoneSlab.EnumType[] var4 = BlockStoneSlab.EnumType.values();
            int var5 = var4.length;

            for (int var6 = 0; var6 < var5; ++var6)
            {
                BlockStoneSlab.EnumType var7 = var4[var6];

                if (var7 != BlockStoneSlab.EnumType.WOOD)
                {
                    list.add(new ItemStack(itemIn, 1, var7.getMetadata()));
                }
            }
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
	public IBlockState getStateFromMeta(int meta)
    {
        IBlockState var2 = this.getDefaultState().withProperty(VARIANT, BlockStoneSlab.EnumType.byMetadata(meta & 7));

        if (this.isDouble())
        {
            var2 = var2.withProperty(SEAMLESS, Boolean.valueOf((meta & 8) != 0));
        }
        else
        {
            var2 = var2.withProperty(HALF, (meta & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
        }

        return var2;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
        byte var2 = 0;
        int var3 = var2 | ((BlockStoneSlab.EnumType)state.getValue(VARIANT)).getMetadata();

        if (this.isDouble())
        {
            if (((Boolean)state.getValue(SEAMLESS)).booleanValue())
            {
                var3 |= 8;
            }
        }
        else if (state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP)
        {
            var3 |= 8;
        }

        return var3;
    }

    @Override
	protected BlockState createBlockState()
    {
        return this.isDouble() ? new BlockState(this, new IProperty[] {SEAMLESS, VARIANT}): new BlockState(this, new IProperty[] {HALF, VARIANT});
    }

    /**
     * Get the damage value that this Block should drop
     */
    @Override
	public int damageDropped(IBlockState state)
    {
        return ((BlockStoneSlab.EnumType)state.getValue(VARIANT)).getMetadata();
    }

    public static enum EnumType implements IStringSerializable
    {
        STONE("STONE", 0, 0, "stone"),
        SAND("SAND", 1, 1, "sandstone", "sand"),
        WOOD("WOOD", 2, 2, "wood_old", "wood"),
        COBBLESTONE("COBBLESTONE", 3, 3, "cobblestone", "cobble"),
        BRICK("BRICK", 4, 4, "brick"),
        SMOOTHBRICK("SMOOTHBRICK", 5, 5, "stone_brick", "smoothStoneBrick"),
        NETHERBRICK("NETHERBRICK", 6, 6, "nether_brick", "netherBrick"),
        QUARTZ("QUARTZ", 7, 7, "quartz");
        private static final BlockStoneSlab.EnumType[] META_LOOKUP = new BlockStoneSlab.EnumType[values().length];
        private final int meta;
        private final String name;
        private final String unlocalizedName; 

        private EnumType(String p_i45677_1_, int p_i45677_2_, int meta, String name)
        {
            this(p_i45677_1_, p_i45677_2_, meta, name, name);
        }

        private EnumType(String p_i45678_1_, int p_i45678_2_, int meta, String name, String unlocalizedName)
        {
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
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

        public static BlockStoneSlab.EnumType byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        @Override
		public String getName()
        {
            return this.name;
        }

        public String getUnlocalizedName()
        {
            return this.unlocalizedName;
        }

        static {
            BlockStoneSlab.EnumType[] var0 = values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2)
            {
                BlockStoneSlab.EnumType var3 = var0[var2];
                META_LOOKUP[var3.getMetadata()] = var3;
            }
        }
    }
}
