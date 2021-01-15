package net.minecraft.block;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public abstract class BlockFlower extends BlockBush
{
    protected PropertyEnum type;

    protected BlockFlower()
    {
        super(Material.plants);
        this.setDefaultState(this.blockState.getBaseState().withProperty(this.getTypeProperty(), this.getBlockType() == BlockFlower.EnumFlowerColor.RED ? BlockFlower.EnumFlowerType.POPPY : BlockFlower.EnumFlowerType.DANDELION));
    }

    /**
     * Get the damage value that this Block should drop
     */
    @Override
	public int damageDropped(IBlockState state)
    {
        return ((BlockFlower.EnumFlowerType)state.getValue(this.getTypeProperty())).getMeta();
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        BlockFlower.EnumFlowerType[] var4 = BlockFlower.EnumFlowerType.getTypes(this.getBlockType());
        int var5 = var4.length;

        for (int var6 = 0; var6 < var5; ++var6)
        {
            BlockFlower.EnumFlowerType var7 = var4[var6];
            list.add(new ItemStack(itemIn, 1, var7.getMeta()));
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(this.getTypeProperty(), BlockFlower.EnumFlowerType.getType(this.getBlockType(), meta));
    }

    /**
     * Get the Type of this flower (Yellow/Red)
     */
    public abstract BlockFlower.EnumFlowerColor getBlockType();

    public IProperty getTypeProperty()
    {
        if (this.type == null)
        {
            this.type = PropertyEnum.create("type", BlockFlower.EnumFlowerType.class, new Predicate()
            {
                public boolean apply(BlockFlower.EnumFlowerType type)
                {
                    return type.getBlockType() == BlockFlower.this.getBlockType();
                }
                @Override
				public boolean apply(Object p_apply_1_)
                {
                    return this.apply((BlockFlower.EnumFlowerType)p_apply_1_);
                }
            });
        }

        return this.type;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
        return ((BlockFlower.EnumFlowerType)state.getValue(this.getTypeProperty())).getMeta();
    }

    @Override
	protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {this.getTypeProperty()});
    }

    /**
     * Get the OffsetType for this Block. Determines if the model is rendered slightly offset.
     */
    @Override
	public Block.EnumOffsetType getOffsetType()
    {
        return Block.EnumOffsetType.XZ;
    }

    public static enum EnumFlowerColor
    {
        YELLOW("YELLOW", 0),
        RED("RED", 1); 

        private EnumFlowerColor(String p_i45716_1_, int p_i45716_2_) {}

        public BlockFlower getBlock()
        {
            return this == YELLOW ? Blocks.yellow_flower : Blocks.red_flower;
        }
    }

    public static enum EnumFlowerType implements IStringSerializable
    {
        DANDELION("DANDELION", 0, BlockFlower.EnumFlowerColor.YELLOW, 0, "dandelion"),
        POPPY("POPPY", 1, BlockFlower.EnumFlowerColor.RED, 0, "poppy"),
        BLUE_ORCHID("BLUE_ORCHID", 2, BlockFlower.EnumFlowerColor.RED, 1, "blue_orchid", "blueOrchid"),
        ALLIUM("ALLIUM", 3, BlockFlower.EnumFlowerColor.RED, 2, "allium"),
        HOUSTONIA("HOUSTONIA", 4, BlockFlower.EnumFlowerColor.RED, 3, "houstonia"),
        RED_TULIP("RED_TULIP", 5, BlockFlower.EnumFlowerColor.RED, 4, "red_tulip", "tulipRed"),
        ORANGE_TULIP("ORANGE_TULIP", 6, BlockFlower.EnumFlowerColor.RED, 5, "orange_tulip", "tulipOrange"),
        WHITE_TULIP("WHITE_TULIP", 7, BlockFlower.EnumFlowerColor.RED, 6, "white_tulip", "tulipWhite"),
        PINK_TULIP("PINK_TULIP", 8, BlockFlower.EnumFlowerColor.RED, 7, "pink_tulip", "tulipPink"),
        OXEYE_DAISY("OXEYE_DAISY", 9, BlockFlower.EnumFlowerColor.RED, 8, "oxeye_daisy", "oxeyeDaisy");
        private static final BlockFlower.EnumFlowerType[][] TYPES_FOR_BLOCK = new BlockFlower.EnumFlowerType[BlockFlower.EnumFlowerColor.values().length][];
        private final BlockFlower.EnumFlowerColor blockType;
        private final int meta;
        private final String name;
        private final String unlocalizedName; 

        private EnumFlowerType(String p_i45718_1_, int p_i45718_2_, BlockFlower.EnumFlowerColor blockType, int meta, String name)
        {
            this(p_i45718_1_, p_i45718_2_, blockType, meta, name, name);
        }

        private EnumFlowerType(String p_i45719_1_, int p_i45719_2_, BlockFlower.EnumFlowerColor blockType, int meta, String name, String unlocalizedName)
        {
            this.blockType = blockType;
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }

        public BlockFlower.EnumFlowerColor getBlockType()
        {
            return this.blockType;
        }

        public int getMeta()
        {
            return this.meta;
        }

        public static BlockFlower.EnumFlowerType getType(BlockFlower.EnumFlowerColor blockType, int meta)
        {
            BlockFlower.EnumFlowerType[] var2 = TYPES_FOR_BLOCK[blockType.ordinal()];

            if (meta < 0 || meta >= var2.length)
            {
                meta = 0;
            }

            return var2[meta];
        }

        public static BlockFlower.EnumFlowerType[] getTypes(BlockFlower.EnumFlowerColor flowerColor)
        {
            return TYPES_FOR_BLOCK[flowerColor.ordinal()];
        }

        @Override
		public String toString()
        {
            return this.name;
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
            BlockFlower.EnumFlowerColor[] var0 = BlockFlower.EnumFlowerColor.values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2)
            {
                final BlockFlower.EnumFlowerColor var3 = var0[var2];
                Collection var4 = Collections2.filter(Lists.newArrayList(values()), new Predicate()
                {
                    public boolean apply(BlockFlower.EnumFlowerType type)
                    {
                        return type.getBlockType() == var3;
                    }
                    @Override
					public boolean apply(Object p_apply_1_)
                    {
                        return this.apply((BlockFlower.EnumFlowerType)p_apply_1_);
                    }
                });
                TYPES_FOR_BLOCK[var3.ordinal()] = (BlockFlower.EnumFlowerType[])var4.toArray(new BlockFlower.EnumFlowerType[var4.size()]);
            }
        }
    }
}
