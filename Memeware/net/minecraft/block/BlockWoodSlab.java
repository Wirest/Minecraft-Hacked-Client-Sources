package net.minecraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class BlockWoodSlab extends BlockSlab {
    public static final PropertyEnum field_176557_b = PropertyEnum.create("variant", BlockPlanks.EnumType.class);
    private static final String __OBFID = "CL_00000337";

    public BlockWoodSlab() {
        super(Material.wood);
        IBlockState var1 = this.blockState.getBaseState();

        if (!this.isDouble()) {
            var1 = var1.withProperty(HALF_PROP, BlockSlab.EnumBlockHalf.BOTTOM);
        }

        this.setDefaultState(var1.withProperty(field_176557_b, BlockPlanks.EnumType.OAK));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(Blocks.wooden_slab);
    }

    public Item getItem(World worldIn, BlockPos pos) {
        return Item.getItemFromBlock(Blocks.wooden_slab);
    }

    /**
     * Returns the slab block name with the type associated with it
     */
    public String getFullSlabName(int p_150002_1_) {
        return super.getUnlocalizedName() + "." + BlockPlanks.EnumType.func_176837_a(p_150002_1_).func_176840_c();
    }

    public IProperty func_176551_l() {
        return field_176557_b;
    }

    public Object func_176553_a(ItemStack p_176553_1_) {
        return BlockPlanks.EnumType.func_176837_a(p_176553_1_.getMetadata() & 7);
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        if (itemIn != Item.getItemFromBlock(Blocks.double_wooden_slab)) {
            BlockPlanks.EnumType[] var4 = BlockPlanks.EnumType.values();
            int var5 = var4.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                BlockPlanks.EnumType var7 = var4[var6];
                list.add(new ItemStack(itemIn, 1, var7.func_176839_a()));
            }
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        IBlockState var2 = this.getDefaultState().withProperty(field_176557_b, BlockPlanks.EnumType.func_176837_a(meta & 7));

        if (!this.isDouble()) {
            var2 = var2.withProperty(HALF_PROP, (meta & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
        }

        return var2;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        byte var2 = 0;
        int var3 = var2 | ((BlockPlanks.EnumType) state.getValue(field_176557_b)).func_176839_a();

        if (!this.isDouble() && state.getValue(HALF_PROP) == BlockSlab.EnumBlockHalf.TOP) {
            var3 |= 8;
        }

        return var3;
    }

    protected BlockState createBlockState() {
        return this.isDouble() ? new BlockState(this, new IProperty[]{field_176557_b}) : new BlockState(this, new IProperty[]{HALF_PROP, field_176557_b});
    }

    /**
     * Get the damage value that this Block should drop
     */
    public int damageDropped(IBlockState state) {
        return ((BlockPlanks.EnumType) state.getValue(field_176557_b)).func_176839_a();
    }
}
