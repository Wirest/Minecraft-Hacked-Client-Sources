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

public class BlockSilverfish extends Block {
    public static final PropertyEnum VARIANT_PROP = PropertyEnum.create("variant", BlockSilverfish.EnumType.class);
    private static final String __OBFID = "CL_00000271";

    public BlockSilverfish() {
        super(Material.clay);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT_PROP, BlockSilverfish.EnumType.STONE));
        this.setHardness(0.0F);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random) {
        return 0;
    }

    public static boolean func_176377_d(IBlockState p_176377_0_) {
        Block var1 = p_176377_0_.getBlock();
        return p_176377_0_ == Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT_PROP, BlockStone.EnumType.STONE) || var1 == Blocks.cobblestone || var1 == Blocks.stonebrick;
    }

    protected ItemStack createStackedBlock(IBlockState state) {
        switch (BlockSilverfish.SwitchEnumType.field_180178_a[((BlockSilverfish.EnumType) state.getValue(VARIANT_PROP)).ordinal()]) {
            case 1:
                return new ItemStack(Blocks.cobblestone);

            case 2:
                return new ItemStack(Blocks.stonebrick);

            case 3:
                return new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.EnumType.MOSSY.getMetaFromState());

            case 4:
                return new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.EnumType.CRACKED.getMetaFromState());

            case 5:
                return new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.EnumType.CHISELED.getMetaFromState());

            default:
                return new ItemStack(Blocks.stone);
        }
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     *
     * @param chance  The chance that each Item is actually spawned (1.0 = always, 0.0 = never)
     * @param fortune The player's fortune level
     */
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (!worldIn.isRemote && worldIn.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
            EntitySilverfish var6 = new EntitySilverfish(worldIn);
            var6.setLocationAndAngles((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, 0.0F, 0.0F);
            worldIn.spawnEntityInWorld(var6);
            var6.spawnExplosionParticle();
        }
    }

    public int getDamageValue(World worldIn, BlockPos pos) {
        IBlockState var3 = worldIn.getBlockState(pos);
        return var3.getBlock().getMetaFromState(var3);
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        BlockSilverfish.EnumType[] var4 = BlockSilverfish.EnumType.values();
        int var5 = var4.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            BlockSilverfish.EnumType var7 = var4[var6];
            list.add(new ItemStack(itemIn, 1, var7.func_176881_a()));
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT_PROP, BlockSilverfish.EnumType.func_176879_a(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        return ((BlockSilverfish.EnumType) state.getValue(VARIANT_PROP)).func_176881_a();
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{VARIANT_PROP});
    }

    public static enum EnumType implements IStringSerializable {
        STONE("STONE", 0, 0, "stone", (BlockSilverfish.SwitchEnumType) null) {
            private static final String __OBFID = "CL_00002097";

            public IBlockState func_176883_d() {
                return Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT_PROP, BlockStone.EnumType.STONE);
            }
        },
        COBBLESTONE("COBBLESTONE", 1, 1, "cobblestone", "cobble", (BlockSilverfish.SwitchEnumType) null) {
            private static final String __OBFID = "CL_00002096";

            public IBlockState func_176883_d() {
                return Blocks.cobblestone.getDefaultState();
            }
        },
        STONEBRICK("STONEBRICK", 2, 2, "stone_brick", "brick", (BlockSilverfish.SwitchEnumType) null) {
            private static final String __OBFID = "CL_00002095";

            public IBlockState func_176883_d() {
                return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT_PROP, BlockStoneBrick.EnumType.DEFAULT);
            }
        },
        MOSSY_STONEBRICK("MOSSY_STONEBRICK", 3, 3, "mossy_brick", "mossybrick", (BlockSilverfish.SwitchEnumType) null) {
            private static final String __OBFID = "CL_00002094";

            public IBlockState func_176883_d() {
                return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT_PROP, BlockStoneBrick.EnumType.MOSSY);
            }
        },
        CRACKED_STONEBRICK("CRACKED_STONEBRICK", 4, 4, "cracked_brick", "crackedbrick", (BlockSilverfish.SwitchEnumType) null) {
            private static final String __OBFID = "CL_00002093";

            public IBlockState func_176883_d() {
                return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT_PROP, BlockStoneBrick.EnumType.CRACKED);
            }
        },
        CHISELED_STONEBRICK("CHISELED_STONEBRICK", 5, 5, "chiseled_brick", "chiseledbrick", (BlockSilverfish.SwitchEnumType) null) {
            private static final String __OBFID = "CL_00002092";

            public IBlockState func_176883_d() {
                return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT_PROP, BlockStoneBrick.EnumType.CHISELED);
            }
        };
        private static final BlockSilverfish.EnumType[] field_176885_g = new BlockSilverfish.EnumType[values().length];
        private final int field_176893_h;
        private final String field_176894_i;
        private final String field_176891_j;

        private static final BlockSilverfish.EnumType[] $VALUES = new BlockSilverfish.EnumType[]{STONE, COBBLESTONE, STONEBRICK, MOSSY_STONEBRICK, CRACKED_STONEBRICK, CHISELED_STONEBRICK};
        private static final String __OBFID = "CL_00002098";

        private EnumType(String p_i45704_1_, int p_i45704_2_, int p_i45704_3_, String p_i45704_4_) {
            this(p_i45704_1_, p_i45704_2_, p_i45704_3_, p_i45704_4_, p_i45704_4_);
        }

        private EnumType(String p_i45705_1_, int p_i45705_2_, int p_i45705_3_, String p_i45705_4_, String p_i45705_5_) {
            this.field_176893_h = p_i45705_3_;
            this.field_176894_i = p_i45705_4_;
            this.field_176891_j = p_i45705_5_;
        }

        public int func_176881_a() {
            return this.field_176893_h;
        }

        public String toString() {
            return this.field_176894_i;
        }

        public static BlockSilverfish.EnumType func_176879_a(int p_176879_0_) {
            if (p_176879_0_ < 0 || p_176879_0_ >= field_176885_g.length) {
                p_176879_0_ = 0;
            }

            return field_176885_g[p_176879_0_];
        }

        public String getName() {
            return this.field_176894_i;
        }

        public String func_176882_c() {
            return this.field_176891_j;
        }

        public abstract IBlockState func_176883_d();

        public static BlockSilverfish.EnumType func_176878_a(IBlockState p_176878_0_) {
            BlockSilverfish.EnumType[] var1 = values();
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                BlockSilverfish.EnumType var4 = var1[var3];

                if (p_176878_0_ == var4.func_176883_d()) {
                    return var4;
                }
            }

            return STONE;
        }

        EnumType(String p_i45706_1_, int p_i45706_2_, int p_i45706_3_, String p_i45706_4_, BlockSilverfish.SwitchEnumType p_i45706_5_) {
            this(p_i45706_1_, p_i45706_2_, p_i45706_3_, p_i45706_4_);
        }

        EnumType(String p_i45707_1_, int p_i45707_2_, int p_i45707_3_, String p_i45707_4_, String p_i45707_5_, BlockSilverfish.SwitchEnumType p_i45707_6_) {
            this(p_i45707_1_, p_i45707_2_, p_i45707_3_, p_i45707_4_, p_i45707_5_);
        }

        static {
            BlockSilverfish.EnumType[] var0 = values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2) {
                BlockSilverfish.EnumType var3 = var0[var2];
                field_176885_g[var3.func_176881_a()] = var3;
            }
        }
    }

    static final class SwitchEnumType {
        static final int[] field_180178_a = new int[BlockSilverfish.EnumType.values().length];
        private static final String __OBFID = "CL_00002099";

        static {
            try {
                field_180178_a[BlockSilverfish.EnumType.COBBLESTONE.ordinal()] = 1;
            } catch (NoSuchFieldError var5) {
                ;
            }

            try {
                field_180178_a[BlockSilverfish.EnumType.STONEBRICK.ordinal()] = 2;
            } catch (NoSuchFieldError var4) {
                ;
            }

            try {
                field_180178_a[BlockSilverfish.EnumType.MOSSY_STONEBRICK.ordinal()] = 3;
            } catch (NoSuchFieldError var3) {
                ;
            }

            try {
                field_180178_a[BlockSilverfish.EnumType.CRACKED_STONEBRICK.ordinal()] = 4;
            } catch (NoSuchFieldError var2) {
                ;
            }

            try {
                field_180178_a[BlockSilverfish.EnumType.CHISELED_STONEBRICK.ordinal()] = 5;
            } catch (NoSuchFieldError var1) {
                ;
            }
        }
    }
}
