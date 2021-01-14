package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneRepeater extends BlockRedstoneDiode {
    public static final PropertyBool field_176411_a = PropertyBool.create("locked");
    public static final PropertyInteger field_176410_b = PropertyInteger.create("delay", 1, 4);
    private static final String __OBFID = "CL_00000301";

    protected BlockRedstoneRepeater(boolean p_i45424_1_) {
        super(p_i45424_1_);
        setDefaultState(blockState.getBaseState().withProperty(BlockDirectional.AGE, EnumFacing.NORTH).withProperty(BlockRedstoneRepeater.field_176410_b, Integer.valueOf(1)).withProperty(BlockRedstoneRepeater.field_176411_a, Boolean.valueOf(false)));
    }

    /**
     * Get the actual Block state of this Block at the given position. This
     * applies properties not visible in the metadata, such as fence
     * connections.
     */
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(BlockRedstoneRepeater.field_176411_a, Boolean.valueOf(func_176405_b(worldIn, pos, state)));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!playerIn.capabilities.allowEdit) {
            return false;
        } else {
            worldIn.setBlockState(pos, state.cycleProperty(BlockRedstoneRepeater.field_176410_b), 3);
            return true;
        }
    }

    @Override
    protected int func_176403_d(IBlockState p_176403_1_) {
        return ((Integer) p_176403_1_.getValue(BlockRedstoneRepeater.field_176410_b)).intValue() * 2;
    }

    @Override
    protected IBlockState func_180674_e(IBlockState p_180674_1_) {
        Integer var2 = (Integer) p_180674_1_.getValue(BlockRedstoneRepeater.field_176410_b);
        Boolean var3 = (Boolean) p_180674_1_.getValue(BlockRedstoneRepeater.field_176411_a);
        EnumFacing var4 = (EnumFacing) p_180674_1_.getValue(BlockDirectional.AGE);
        return Blocks.powered_repeater.getDefaultState().withProperty(BlockDirectional.AGE, var4).withProperty(BlockRedstoneRepeater.field_176410_b, var2).withProperty(BlockRedstoneRepeater.field_176411_a, var3);
    }

    @Override
    protected IBlockState func_180675_k(IBlockState p_180675_1_) {
        Integer var2 = (Integer) p_180675_1_.getValue(BlockRedstoneRepeater.field_176410_b);
        Boolean var3 = (Boolean) p_180675_1_.getValue(BlockRedstoneRepeater.field_176411_a);
        EnumFacing var4 = (EnumFacing) p_180675_1_.getValue(BlockDirectional.AGE);
        return Blocks.unpowered_repeater.getDefaultState().withProperty(BlockDirectional.AGE, var4).withProperty(BlockRedstoneRepeater.field_176410_b, var2).withProperty(BlockRedstoneRepeater.field_176411_a, var3);
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.repeater;
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return Items.repeater;
    }

    @Override
    public boolean func_176405_b(IBlockAccess p_176405_1_, BlockPos p_176405_2_, IBlockState p_176405_3_) {
        return func_176407_c(p_176405_1_, p_176405_2_, p_176405_3_) > 0;
    }

    @Override
    protected boolean func_149908_a(Block p_149908_1_) {
        return BlockRedstoneDiode.isRedstoneRepeaterBlockID(p_149908_1_);
    }

    @Override
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (isRepeaterPowered) {
            EnumFacing var5 = (EnumFacing) state.getValue(BlockDirectional.AGE);
            double var6 = pos.getX() + 0.5F + (rand.nextFloat() - 0.5F) * 0.2D;
            double var8 = pos.getY() + 0.4F + (rand.nextFloat() - 0.5F) * 0.2D;
            double var10 = pos.getZ() + 0.5F + (rand.nextFloat() - 0.5F) * 0.2D;
            float var12 = -5.0F;

            if (rand.nextBoolean()) {
                var12 = ((Integer) state.getValue(BlockRedstoneRepeater.field_176410_b)).intValue() * 2 - 1;
            }

            var12 /= 16.0F;
            double var13 = var12 * var5.getFrontOffsetX();
            double var15 = var12 * var5.getFrontOffsetZ();
            worldIn.spawnParticle(EnumParticleTypes.REDSTONE, var6 + var13, var8, var10 + var15, 0.0D, 0.0D, 0.0D, new int[0]);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        func_176400_h(worldIn, pos, state);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BlockDirectional.AGE, EnumFacing.getHorizontal(meta)).withProperty(BlockRedstoneRepeater.field_176411_a, Boolean.valueOf(false)).withProperty(BlockRedstoneRepeater.field_176410_b, Integer.valueOf(1 + (meta >> 2)));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        byte var2 = 0;
        int var3 = var2 | ((EnumFacing) state.getValue(BlockDirectional.AGE)).getHorizontalIndex();
        var3 |= ((Integer) state.getValue(BlockRedstoneRepeater.field_176410_b)).intValue() - 1 << 2;
        return var3;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{BlockDirectional.AGE, BlockRedstoneRepeater.field_176410_b, BlockRedstoneRepeater.field_176411_a});
    }
}
