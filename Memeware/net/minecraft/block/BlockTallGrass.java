package net.minecraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTallGrass extends BlockBush implements IGrowable {
    public static final PropertyEnum field_176497_a = PropertyEnum.create("type", BlockTallGrass.EnumType.class);
    private static final String __OBFID = "CL_00000321";

    protected BlockTallGrass() {
        super(Material.vine);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176497_a, BlockTallGrass.EnumType.DEAD_BUSH));
        float var1 = 0.4F;
        this.setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, 0.8F, 0.5F + var1);
    }

    public int getBlockColor() {
        return ColorizerGrass.getGrassColor(0.5D, 1.0D);
    }

    public boolean canBlockStay(World worldIn, BlockPos p_180671_2_, IBlockState p_180671_3_) {
        return this.canPlaceBlockOn(worldIn.getBlockState(p_180671_2_.offsetDown()).getBlock());
    }

    /**
     * Whether this Block can be replaced directly by other blocks (true for e.g. tall grass)
     */
    public boolean isReplaceable(World worldIn, BlockPos pos) {
        return true;
    }

    public int getRenderColor(IBlockState state) {
        if (state.getBlock() != this) {
            return super.getRenderColor(state);
        } else {
            BlockTallGrass.EnumType var2 = (BlockTallGrass.EnumType) state.getValue(field_176497_a);
            return var2 == BlockTallGrass.EnumType.DEAD_BUSH ? 16777215 : ColorizerGrass.getGrassColor(0.5D, 1.0D);
        }
    }

    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        return worldIn.getBiomeGenForCoords(pos).func_180627_b(pos);
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return rand.nextInt(8) == 0 ? Items.wheat_seeds : null;
    }

    /**
     * Get the quantity dropped based on the given fortune level
     */
    public int quantityDroppedWithBonus(int fortune, Random random) {
        return 1 + random.nextInt(fortune * 2 + 1);
    }

    public void harvestBlock(World worldIn, EntityPlayer playerIn, BlockPos pos, IBlockState state, TileEntity te) {
        if (!worldIn.isRemote && playerIn.getCurrentEquippedItem() != null && playerIn.getCurrentEquippedItem().getItem() == Items.shears) {
            playerIn.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
            spawnAsEntity(worldIn, pos, new ItemStack(Blocks.tallgrass, 1, ((BlockTallGrass.EnumType) state.getValue(field_176497_a)).func_177044_a()));
        } else {
            super.harvestBlock(worldIn, playerIn, pos, state, te);
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
        for (int var4 = 1; var4 < 3; ++var4) {
            list.add(new ItemStack(itemIn, 1, var4));
        }
    }

    public boolean isStillGrowing(World worldIn, BlockPos p_176473_2_, IBlockState p_176473_3_, boolean p_176473_4_) {
        return p_176473_3_.getValue(field_176497_a) != BlockTallGrass.EnumType.DEAD_BUSH;
    }

    public boolean canUseBonemeal(World worldIn, Random p_180670_2_, BlockPos p_180670_3_, IBlockState p_180670_4_) {
        return true;
    }

    public void grow(World worldIn, Random p_176474_2_, BlockPos p_176474_3_, IBlockState p_176474_4_) {
        BlockDoublePlant.EnumPlantType var5 = BlockDoublePlant.EnumPlantType.GRASS;

        if (p_176474_4_.getValue(field_176497_a) == BlockTallGrass.EnumType.FERN) {
            var5 = BlockDoublePlant.EnumPlantType.FERN;
        }

        if (Blocks.double_plant.canPlaceBlockAt(worldIn, p_176474_3_)) {
            Blocks.double_plant.func_176491_a(worldIn, p_176474_3_, var5, 2);
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(field_176497_a, BlockTallGrass.EnumType.func_177045_a(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        return ((BlockTallGrass.EnumType) state.getValue(field_176497_a)).func_177044_a();
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{field_176497_a});
    }

    /**
     * Get the OffsetType for this Block. Determines if the model is rendered slightly offset.
     */
    public Block.EnumOffsetType getOffsetType() {
        return Block.EnumOffsetType.XYZ;
    }

    public static enum EnumType implements IStringSerializable {
        DEAD_BUSH("DEAD_BUSH", 0, 0, "dead_bush"),
        GRASS("GRASS", 1, 1, "tall_grass"),
        FERN("FERN", 2, 2, "fern");
        private static final BlockTallGrass.EnumType[] field_177048_d = new BlockTallGrass.EnumType[values().length];
        private final int field_177049_e;
        private final String field_177046_f;

        private static final BlockTallGrass.EnumType[] $VALUES = new BlockTallGrass.EnumType[]{DEAD_BUSH, GRASS, FERN};
        private static final String __OBFID = "CL_00002055";

        private EnumType(String p_i45676_1_, int p_i45676_2_, int p_i45676_3_, String p_i45676_4_) {
            this.field_177049_e = p_i45676_3_;
            this.field_177046_f = p_i45676_4_;
        }

        public int func_177044_a() {
            return this.field_177049_e;
        }

        public String toString() {
            return this.field_177046_f;
        }

        public static BlockTallGrass.EnumType func_177045_a(int p_177045_0_) {
            if (p_177045_0_ < 0 || p_177045_0_ >= field_177048_d.length) {
                p_177045_0_ = 0;
            }

            return field_177048_d[p_177045_0_];
        }

        public String getName() {
            return this.field_177046_f;
        }

        static {
            BlockTallGrass.EnumType[] var0 = values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2) {
                BlockTallGrass.EnumType var3 = var0[var2];
                field_177048_d[var3.func_177044_a()] = var3;
            }
        }
    }
}
