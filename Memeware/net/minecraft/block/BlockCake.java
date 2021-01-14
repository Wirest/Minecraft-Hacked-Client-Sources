package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCake extends Block {
    public static final PropertyInteger BITES = PropertyInteger.create("bites", 0, 6);
    private static final String __OBFID = "CL_00000211";

    protected BlockCake() {
        super(Material.cake);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BITES, Integer.valueOf(0)));
        this.setTickRandomly(true);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        float var3 = 0.0625F;
        float var4 = (float) (1 + ((Integer) access.getBlockState(pos).getValue(BITES)).intValue() * 2) / 16.0F;
        float var5 = 0.5F;
        this.setBlockBounds(var4, 0.0F, var3, 1.0F - var3, var5, 1.0F - var3);
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender() {
        float var1 = 0.0625F;
        float var2 = 0.5F;
        this.setBlockBounds(var1, 0.0F, var1, 1.0F - var1, var2, 1.0F - var1);
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        float var4 = 0.0625F;
        float var5 = (float) (1 + ((Integer) state.getValue(BITES)).intValue() * 2) / 16.0F;
        float var6 = 0.5F;
        return new AxisAlignedBB((double) ((float) pos.getX() + var5), (double) pos.getY(), (double) ((float) pos.getZ() + var4), (double) ((float) (pos.getX() + 1) - var4), (double) ((float) pos.getY() + var6), (double) ((float) (pos.getZ() + 1) - var4));
    }

    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        return this.getCollisionBoundingBox(worldIn, pos, worldIn.getBlockState(pos));
    }

    public boolean isFullCube() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        this.eatCake(worldIn, pos, state, playerIn);
        return true;
    }

    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        this.eatCake(worldIn, pos, worldIn.getBlockState(pos), playerIn);
    }

    private void eatCake(World worldIn, BlockPos p_180682_2_, IBlockState p_180682_3_, EntityPlayer p_180682_4_) {
        if (p_180682_4_.canEat(false)) {
            p_180682_4_.getFoodStats().addStats(2, 0.1F);
            int var5 = ((Integer) p_180682_3_.getValue(BITES)).intValue();

            if (var5 < 6) {
                worldIn.setBlockState(p_180682_2_, p_180682_3_.withProperty(BITES, Integer.valueOf(var5 + 1)), 3);
            } else {
                worldIn.setBlockToAir(p_180682_2_);
            }
        }
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) ? this.canBlockStay(worldIn, pos) : false;
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!this.canBlockStay(worldIn, pos)) {
            worldIn.setBlockToAir(pos);
        }
    }

    private boolean canBlockStay(World worldIn, BlockPos p_176588_2_) {
        return worldIn.getBlockState(p_176588_2_.offsetDown()).getBlock().getMaterial().isSolid();
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random) {
        return 0;
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    public Item getItem(World worldIn, BlockPos pos) {
        return Items.cake;
    }

    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(BITES, Integer.valueOf(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        return ((Integer) state.getValue(BITES)).intValue();
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{BITES});
    }

    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        return (7 - ((Integer) worldIn.getBlockState(pos).getValue(BITES)).intValue()) * 2;
    }

    public boolean hasComparatorInputOverride() {
        return true;
    }
}
