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
    public static final PropertyBool field_176555_b = PropertyBool.create("seamless");
    public static final PropertyEnum field_176556_M = PropertyEnum.create("variant", BlockStoneSlab.EnumType.class);
    private static final String __OBFID = "CL_00000320";

    public BlockStoneSlab()
    {
        super(Material.rock);
        IBlockState var1 = this.blockState.getBaseState();

        if (this.isDouble())
        {
            var1 = var1.withProperty(field_176555_b, Boolean.valueOf(false));
        }
        else
        {
            var1 = var1.withProperty(HALF_PROP, BlockSlab.EnumBlockHalf.BOTTOM);
        }

        this.setDefaultState(var1.withProperty(field_176556_M, BlockStoneSlab.EnumType.STONE));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *  
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(Blocks.stone_slab);
    }

    public Item getItem(World worldIn, BlockPos pos)
    {
        return Item.getItemFromBlock(Blocks.stone_slab);
    }

    /**
     * Returns the slab block name with the type associated with it
     */
    public String getFullSlabName(int p_150002_1_)
    {
        return super.getUnlocalizedName() + "." + BlockStoneSlab.EnumType.func_176625_a(p_150002_1_).func_176627_c();
    }

    public IProperty func_176551_l()
    {
        return field_176556_M;
    }

    public Object func_176553_a(ItemStack p_176553_1_)
    {
        return BlockStoneSlab.EnumType.func_176625_a(p_176553_1_.getMetadata() & 7);
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
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
                    list.add(new ItemStack(itemIn, 1, var7.func_176624_a()));
                }
            }
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState var2 = this.getDefaultState().withProperty(field_176556_M, BlockStoneSlab.EnumType.func_176625_a(meta & 7));

        if (this.isDouble())
        {
            var2 = var2.withProperty(field_176555_b, Boolean.valueOf((meta & 8) != 0));
        }
        else
        {
            var2 = var2.withProperty(HALF_PROP, (meta & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
        }

        return var2;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        byte var2 = 0;
        int var3 = var2 | ((BlockStoneSlab.EnumType)state.getValue(field_176556_M)).func_176624_a();

        if (this.isDouble())
        {
            if (((Boolean)state.getValue(field_176555_b)).booleanValue())
            {
                var3 |= 8;
            }
        }
        else if (state.getValue(HALF_PROP) == BlockSlab.EnumBlockHalf.TOP)
        {
            var3 |= 8;
        }

        return var3;
    }

    protected BlockState createBlockState()
    {
        return this.isDouble() ? new BlockState(this, new IProperty[] {field_176555_b, field_176556_M}): new BlockState(this, new IProperty[] {HALF_PROP, field_176556_M});
    }

    /**
     * Get the damage value that this Block should drop
     */
    public int damageDropped(IBlockState state)
    {
        return ((BlockStoneSlab.EnumType)state.getValue(field_176556_M)).func_176624_a();
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
        private static final BlockStoneSlab.EnumType[] field_176640_i = new BlockStoneSlab.EnumType[values().length];
        private final int field_176637_j;
        private final String field_176638_k;
        private final String field_176635_l;

        private static final BlockStoneSlab.EnumType[] $VALUES = new BlockStoneSlab.EnumType[]{STONE, SAND, WOOD, COBBLESTONE, BRICK, SMOOTHBRICK, NETHERBRICK, QUARTZ};
        private static final String __OBFID = "CL_00002056";

        private EnumType(String p_i45677_1_, int p_i45677_2_, int p_i45677_3_, String p_i45677_4_)
        {
            this(p_i45677_1_, p_i45677_2_, p_i45677_3_, p_i45677_4_, p_i45677_4_);
        }

        private EnumType(String p_i45678_1_, int p_i45678_2_, int p_i45678_3_, String p_i45678_4_, String p_i45678_5_)
        {
            this.field_176637_j = p_i45678_3_;
            this.field_176638_k = p_i45678_4_;
            this.field_176635_l = p_i45678_5_;
        }

        public int func_176624_a()
        {
            return this.field_176637_j;
        }

        public String toString()
        {
            return this.field_176638_k;
        }

        public static BlockStoneSlab.EnumType func_176625_a(int p_176625_0_)
        {
            if (p_176625_0_ < 0 || p_176625_0_ >= field_176640_i.length)
            {
                p_176625_0_ = 0;
            }

            return field_176640_i[p_176625_0_];
        }

        public String getName()
        {
            return this.field_176638_k;
        }

        public String func_176627_c()
        {
            return this.field_176635_l;
        }

        static {
            BlockStoneSlab.EnumType[] var0 = values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2)
            {
                BlockStoneSlab.EnumType var3 = var0[var2];
                field_176640_i[var3.func_176624_a()] = var3;
            }
        }
    }
}
