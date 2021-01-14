package net.minecraft.block;

import java.util.List;

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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDirt extends Block {
    public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockDirt.DirtType.class);
    public static final PropertyBool SNOWY = PropertyBool.create("snowy");
    private static final String __OBFID = "CL_00000228";

    protected BlockDirt() {
        super(Material.ground);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockDirt.DirtType.DIRT).withProperty(SNOWY, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Get the actual Block state of this Block at the given position. This applies properties not visible in the
     * metadata, such as fence connections.
     */
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (state.getValue(VARIANT) == BlockDirt.DirtType.PODZOL) {
            Block var4 = worldIn.getBlockState(pos.offsetUp()).getBlock();
            state = state.withProperty(SNOWY, Boolean.valueOf(var4 == Blocks.snow || var4 == Blocks.snow_layer));
        }

        return state;
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        list.add(new ItemStack(this, 1, BlockDirt.DirtType.DIRT.getMetadata()));
        list.add(new ItemStack(this, 1, BlockDirt.DirtType.COARSE_DIRT.getMetadata()));
        list.add(new ItemStack(this, 1, BlockDirt.DirtType.PODZOL.getMetadata()));
    }

    public int getDamageValue(World worldIn, BlockPos pos) {
        IBlockState var3 = worldIn.getBlockState(pos);
        return var3.getBlock() != this ? 0 : ((BlockDirt.DirtType) var3.getValue(VARIANT)).getMetadata();
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, BlockDirt.DirtType.byMetadata(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        return ((BlockDirt.DirtType) state.getValue(VARIANT)).getMetadata();
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{VARIANT, SNOWY});
    }

    /**
     * Get the damage value that this Block should drop
     */
    public int damageDropped(IBlockState state) {
        BlockDirt.DirtType var2 = (BlockDirt.DirtType) state.getValue(VARIANT);

        if (var2 == BlockDirt.DirtType.PODZOL) {
            var2 = BlockDirt.DirtType.DIRT;
        }

        return var2.getMetadata();
    }

    public static enum DirtType implements IStringSerializable {
        DIRT("DIRT", 0, 0, "dirt", "default"),
        COARSE_DIRT("COARSE_DIRT", 1, 1, "coarse_dirt", "coarse"),
        PODZOL("PODZOL", 2, 2, "podzol");
        private static final BlockDirt.DirtType[] METADATA_LOOKUP = new BlockDirt.DirtType[values().length];
        private final int metadata;
        private final String name;
        private final String unlocalizedName;

        private static final BlockDirt.DirtType[] $VALUES = new BlockDirt.DirtType[]{DIRT, COARSE_DIRT, PODZOL};
        private static final String __OBFID = "CL_00002125";

        private DirtType(String p_i45727_1_, int p_i45727_2_, int metadata, String name) {
            this(p_i45727_1_, p_i45727_2_, metadata, name, name);
        }

        private DirtType(String p_i45728_1_, int p_i45728_2_, int metadata, String name, String unlocalizedName) {
            this.metadata = metadata;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }

        public int getMetadata() {
            return this.metadata;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }

        public String toString() {
            return this.name;
        }

        public static BlockDirt.DirtType byMetadata(int metadata) {
            if (metadata < 0 || metadata >= METADATA_LOOKUP.length) {
                metadata = 0;
            }

            return METADATA_LOOKUP[metadata];
        }

        public String getName() {
            return this.name;
        }

        static {
            BlockDirt.DirtType[] var0 = values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2) {
                BlockDirt.DirtType var3 = var0[var2];
                METADATA_LOOKUP[var3.getMetadata()] = var3;
            }
        }
    }
}
