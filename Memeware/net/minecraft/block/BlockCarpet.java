package net.minecraft.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCarpet extends Block {
    public static final PropertyEnum field_176330_a = PropertyEnum.create("color", EnumDyeColor.class);
    private static final String __OBFID = "CL_00000338";

    protected BlockCarpet() {
        super(Material.carpet);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176330_a, EnumDyeColor.WHITE));
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setBlockBoundsFromMeta(0);
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender() {
        this.setBlockBoundsFromMeta(0);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        this.setBlockBoundsFromMeta(0);
    }

    protected void setBlockBoundsFromMeta(int meta) {
        byte var2 = 0;
        float var3 = (float) (1 * (1 + var2)) / 16.0F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, var3, 1.0F);
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos);
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        this.checkAndDropBlock(worldIn, pos, state);
    }

    private boolean checkAndDropBlock(World worldIn, BlockPos p_176328_2_, IBlockState p_176328_3_) {
        if (!this.canBlockStay(worldIn, p_176328_2_)) {
            this.dropBlockAsItem(worldIn, p_176328_2_, p_176328_3_, 0);
            worldIn.setBlockToAir(p_176328_2_);
            return false;
        } else {
            return true;
        }
    }

    private boolean canBlockStay(World worldIn, BlockPos p_176329_2_) {
        return !worldIn.isAirBlock(p_176329_2_.offsetDown());
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.UP ? true : super.shouldSideBeRendered(worldIn, pos, side);
    }

    /**
     * Get the damage value that this Block should drop
     */
    public int damageDropped(IBlockState state) {
        return ((EnumDyeColor) state.getValue(field_176330_a)).func_176765_a();
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        for (int var4 = 0; var4 < 16; ++var4) {
            list.add(new ItemStack(itemIn, 1, var4));
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(field_176330_a, EnumDyeColor.func_176764_b(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        return ((EnumDyeColor) state.getValue(field_176330_a)).func_176765_a();
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{field_176330_a});
    }
}
