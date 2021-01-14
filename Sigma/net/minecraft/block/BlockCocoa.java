package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCocoa extends BlockDirectional implements IGrowable {
    public static final PropertyInteger field_176501_a = PropertyInteger.create("age", 0, 2);
    private static final String __OBFID = "CL_00000216";

    public BlockCocoa() {
        super(Material.plants);
        setDefaultState(blockState.getBaseState().withProperty(BlockDirectional.AGE, EnumFacing.NORTH).withProperty(BlockCocoa.field_176501_a, Integer.valueOf(0)));
        setTickRandomly(true);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!canBlockStay(worldIn, pos, state)) {
            dropBlock(worldIn, pos, state);
        } else if (worldIn.rand.nextInt(5) == 0) {
            int var5 = ((Integer) state.getValue(BlockCocoa.field_176501_a)).intValue();

            if (var5 < 2) {
                worldIn.setBlockState(pos, state.withProperty(BlockCocoa.field_176501_a, Integer.valueOf(var5 + 1)), 2);
            }
        }
    }

    public boolean canBlockStay(World worldIn, BlockPos p_176499_2_, IBlockState p_176499_3_) {
        p_176499_2_ = p_176499_2_.offset((EnumFacing) p_176499_3_.getValue(BlockDirectional.AGE));
        IBlockState var4 = worldIn.getBlockState(p_176499_2_);
        return var4.getBlock() == Blocks.log && var4.getValue(BlockPlanks.VARIANT_PROP) == BlockPlanks.EnumType.JUNGLE;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        IBlockState var3 = access.getBlockState(pos);
        EnumFacing var4 = (EnumFacing) var3.getValue(BlockDirectional.AGE);
        int var5 = ((Integer) var3.getValue(BlockCocoa.field_176501_a)).intValue();
        int var6 = 4 + var5 * 2;
        int var7 = 5 + var5 * 2;
        float var8 = var6 / 2.0F;

        switch (BlockCocoa.SwitchEnumFacing.FACINGARRAY[var4.ordinal()]) {
            case 1:
                setBlockBounds((8.0F - var8) / 16.0F, (12.0F - var7) / 16.0F, (15.0F - var6) / 16.0F, (8.0F + var8) / 16.0F, 0.75F, 0.9375F);
                break;

            case 2:
                setBlockBounds((8.0F - var8) / 16.0F, (12.0F - var7) / 16.0F, 0.0625F, (8.0F + var8) / 16.0F, 0.75F, (1.0F + var6) / 16.0F);
                break;

            case 3:
                setBlockBounds(0.0625F, (12.0F - var7) / 16.0F, (8.0F - var8) / 16.0F, (1.0F + var6) / 16.0F, 0.75F, (8.0F + var8) / 16.0F);
                break;

            case 4:
                setBlockBounds((15.0F - var6) / 16.0F, (12.0F - var7) / 16.0F, (8.0F - var8) / 16.0F, 0.9375F, 0.75F, (8.0F + var8) / 16.0F);
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        EnumFacing var6 = EnumFacing.fromAngle(placer.rotationYaw);
        worldIn.setBlockState(pos, state.withProperty(BlockDirectional.AGE, var6), 2);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        if (!facing.getAxis().isHorizontal()) {
            facing = EnumFacing.NORTH;
        }

        return getDefaultState().withProperty(BlockDirectional.AGE, facing.getOpposite()).withProperty(BlockCocoa.field_176501_a, Integer.valueOf(0));
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!canBlockStay(worldIn, pos, state)) {
            dropBlock(worldIn, pos, state);
        }
    }

    private void dropBlock(World worldIn, BlockPos p_176500_2_, IBlockState p_176500_3_) {
        worldIn.setBlockState(p_176500_2_, Blocks.air.getDefaultState(), 3);
        dropBlockAsItem(worldIn, p_176500_2_, p_176500_3_, 0);
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     *
     * @param chance  The chance that each Item is actually spawned (1.0 = always,
     *                0.0 = never)
     * @param fortune The player's fortune level
     */
    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        int var6 = ((Integer) state.getValue(BlockCocoa.field_176501_a)).intValue();
        byte var7 = 1;

        if (var6 >= 2) {
            var7 = 3;
        }

        for (int var8 = 0; var8 < var7; ++var8) {
            Block.spawnAsEntity(worldIn, pos, new ItemStack(Items.dye, 1, EnumDyeColor.BROWN.getDyeColorDamage()));
        }
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return Items.dye;
    }

    @Override
    public int getDamageValue(World worldIn, BlockPos pos) {
        return EnumDyeColor.BROWN.getDyeColorDamage();
    }

    @Override
    public boolean isStillGrowing(World worldIn, BlockPos p_176473_2_, IBlockState p_176473_3_, boolean p_176473_4_) {
        return ((Integer) p_176473_3_.getValue(BlockCocoa.field_176501_a)).intValue() < 2;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random p_180670_2_, BlockPos p_180670_3_, IBlockState p_180670_4_) {
        return true;
    }

    @Override
    public void grow(World worldIn, Random p_176474_2_, BlockPos p_176474_3_, IBlockState p_176474_4_) {
        worldIn.setBlockState(p_176474_3_, p_176474_4_.withProperty(BlockCocoa.field_176501_a, Integer.valueOf(((Integer) p_176474_4_.getValue(BlockCocoa.field_176501_a)).intValue() + 1)), 2);
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BlockDirectional.AGE, EnumFacing.getHorizontal(meta)).withProperty(BlockCocoa.field_176501_a, Integer.valueOf((meta & 15) >> 2));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        byte var2 = 0;
        int var3 = var2 | ((EnumFacing) state.getValue(BlockDirectional.AGE)).getHorizontalIndex();
        var3 |= ((Integer) state.getValue(BlockCocoa.field_176501_a)).intValue() << 2;
        return var3;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{BlockDirectional.AGE, BlockCocoa.field_176501_a});
    }

    static final class SwitchEnumFacing {
        static final int[] FACINGARRAY = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00002130";

        static {
            try {
                SwitchEnumFacing.FACINGARRAY[EnumFacing.SOUTH.ordinal()] = 1;
            } catch (NoSuchFieldError var4) {
                ;
            }

            try {
                SwitchEnumFacing.FACINGARRAY[EnumFacing.NORTH.ordinal()] = 2;
            } catch (NoSuchFieldError var3) {
                ;
            }

            try {
                SwitchEnumFacing.FACINGARRAY[EnumFacing.WEST.ordinal()] = 3;
            } catch (NoSuchFieldError var2) {
                ;
            }

            try {
                SwitchEnumFacing.FACINGARRAY[EnumFacing.EAST.ordinal()] = 4;
            } catch (NoSuchFieldError var1) {
                ;
            }
        }
    }
}
