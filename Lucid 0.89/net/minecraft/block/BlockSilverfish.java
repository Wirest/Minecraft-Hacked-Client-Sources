package net.minecraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

public class BlockSilverfish extends Block
{
    public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockSilverfish.EnumType.class);

    public BlockSilverfish()
    {
        super(Material.clay);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockSilverfish.EnumType.STONE));
        this.setHardness(0.0F);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
	public int quantityDropped(Random random)
    {
        return 0;
    }

    public static boolean canContainSilverfish(IBlockState blockState)
    {
        Block var1 = blockState.getBlock();
        return blockState == Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE) || var1 == Blocks.cobblestone || var1 == Blocks.stonebrick;
    }

    @Override
	protected ItemStack createStackedBlock(IBlockState state)
    {
        switch (BlockSilverfish.SwitchEnumType.TYPE_LOOKUP[((BlockSilverfish.EnumType)state.getValue(VARIANT)).ordinal()])
        {
            case 1:
                return new ItemStack(Blocks.cobblestone);

            case 2:
                return new ItemStack(Blocks.stonebrick);

            case 3:
                return new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.EnumType.MOSSY.getMetadata());

            case 4:
                return new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.EnumType.CRACKED.getMetadata());

            case 5:
                return new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.EnumType.CHISELED.getMetadata());

            default:
                return new ItemStack(Blocks.stone);
        }
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
        if (!worldIn.isRemote && worldIn.getGameRules().getGameRuleBooleanValue("doTileDrops"))
        {
            EntitySilverfish var6 = new EntitySilverfish(worldIn);
            var6.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
            worldIn.spawnEntityInWorld(var6);
            var6.spawnExplosionParticle();
        }
    }

    @Override
	public int getDamageValue(World worldIn, BlockPos pos)
    {
        IBlockState var3 = worldIn.getBlockState(pos);
        return var3.getBlock().getMetaFromState(var3);
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        BlockSilverfish.EnumType[] var4 = BlockSilverfish.EnumType.values();
        int var5 = var4.length;

        for (int var6 = 0; var6 < var5; ++var6)
        {
            BlockSilverfish.EnumType var7 = var4[var6];
            list.add(new ItemStack(itemIn, 1, var7.getMetadata()));
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(VARIANT, BlockSilverfish.EnumType.byMetadata(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
        return ((BlockSilverfish.EnumType)state.getValue(VARIANT)).getMetadata();
    }

    @Override
	protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {VARIANT});
    }

    public static enum EnumType implements IStringSerializable
    {
        STONE("STONE", 0, 0, "stone", (BlockSilverfish.SwitchEnumType)null)
        {
            @Override
			public IBlockState getModelBlock()
            {
                return Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE);
            }
        },
        COBBLESTONE("COBBLESTONE", 1, 1, "cobblestone", "cobble", (BlockSilverfish.SwitchEnumType)null)
        {
            @Override
			public IBlockState getModelBlock()
            {
                return Blocks.cobblestone.getDefaultState();
            }
        },
        STONEBRICK("STONEBRICK", 2, 2, "stone_brick", "brick", (BlockSilverfish.SwitchEnumType)null)
        {
            @Override
			public IBlockState getModelBlock()
            {
                return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.DEFAULT);
            }
        },
        MOSSY_STONEBRICK("MOSSY_STONEBRICK", 3, 3, "mossy_brick", "mossybrick", (BlockSilverfish.SwitchEnumType)null)
        {
            @Override
			public IBlockState getModelBlock()
            {
                return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY);
            }
        },
        CRACKED_STONEBRICK("CRACKED_STONEBRICK", 4, 4, "cracked_brick", "crackedbrick", (BlockSilverfish.SwitchEnumType)null)
        {
            @Override
			public IBlockState getModelBlock()
            {
                return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED);
            }
        },
        CHISELED_STONEBRICK("CHISELED_STONEBRICK", 5, 5, "chiseled_brick", "chiseledbrick", (BlockSilverfish.SwitchEnumType)null)
        {
            @Override
			public IBlockState getModelBlock()
            {
                return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED);
            }
        };
        private static final BlockSilverfish.EnumType[] META_LOOKUP = new BlockSilverfish.EnumType[values().length];
        private final int meta;
        private final String name;
        private final String unlocalizedName; 

        private EnumType(String p_i45704_1_, int p_i45704_2_, int meta, String name)
        {
            this(p_i45704_1_, p_i45704_2_, meta, name, name);
        }

        private EnumType(String p_i45705_1_, int p_i45705_2_, int meta, String name, String unlocalizedName)
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

        public static BlockSilverfish.EnumType byMetadata(int meta)
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

        public abstract IBlockState getModelBlock();

        public static BlockSilverfish.EnumType forModelBlock(IBlockState model)
        {
            BlockSilverfish.EnumType[] var1 = values();
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3)
            {
                BlockSilverfish.EnumType var4 = var1[var3];

                if (model == var4.getModelBlock())
                {
                    return var4;
                }
            }

            return STONE;
        }

        EnumType(String p_i45706_1_, int p_i45706_2_, int meta, String name, BlockSilverfish.SwitchEnumType dummy)
        {
            this(p_i45706_1_, p_i45706_2_, meta, name);
        }

        EnumType(String p_i45707_1_, int p_i45707_2_, int meta, String name, String unlocalizedName, BlockSilverfish.SwitchEnumType dummy)
        {
            this(p_i45707_1_, p_i45707_2_, meta, name, unlocalizedName);
        }

        static {
            BlockSilverfish.EnumType[] var0 = values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2)
            {
                BlockSilverfish.EnumType var3 = var0[var2];
                META_LOOKUP[var3.getMetadata()] = var3;
            }
        }
    }

    static final class SwitchEnumType
    {
        static final int[] TYPE_LOOKUP = new int[BlockSilverfish.EnumType.values().length];

        static
        {
            try
            {
                TYPE_LOOKUP[BlockSilverfish.EnumType.COBBLESTONE.ordinal()] = 1;
            }
            catch (NoSuchFieldError var5)
            {
                ;
            }

            try
            {
                TYPE_LOOKUP[BlockSilverfish.EnumType.STONEBRICK.ordinal()] = 2;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                TYPE_LOOKUP[BlockSilverfish.EnumType.MOSSY_STONEBRICK.ordinal()] = 3;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                TYPE_LOOKUP[BlockSilverfish.EnumType.CRACKED_STONEBRICK.ordinal()] = 4;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                TYPE_LOOKUP[BlockSilverfish.EnumType.CHISELED_STONEBRICK.ordinal()] = 5;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
