package net.minecraft.block;

import com.google.common.base.Predicate;

import java.util.List;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockNewLeaf extends BlockLeaves {
    public static final PropertyEnum field_176240_P = PropertyEnum.create("variant", BlockPlanks.EnumType.class, new Predicate() {
        private static final String __OBFID = "CL_00002090";

        public boolean func_180195_a(BlockPlanks.EnumType p_180195_1_) {
            return p_180195_1_.func_176839_a() >= 4;
        }

        @Override
        public boolean apply(Object p_apply_1_) {
            return func_180195_a((BlockPlanks.EnumType) p_apply_1_);
        }
    });
    private static final String __OBFID = "CL_00000276";

    public BlockNewLeaf() {
        setDefaultState(blockState.getBaseState().withProperty(BlockNewLeaf.field_176240_P, BlockPlanks.EnumType.ACACIA).withProperty(BlockLeaves.field_176236_b, Boolean.valueOf(true)).withProperty(BlockLeaves.field_176237_a, Boolean.valueOf(true)));
    }

    @Override
    protected void func_176234_a(World worldIn, BlockPos p_176234_2_, IBlockState p_176234_3_, int p_176234_4_) {
        if (p_176234_3_.getValue(BlockNewLeaf.field_176240_P) == BlockPlanks.EnumType.DARK_OAK && worldIn.rand.nextInt(p_176234_4_) == 0) {
            Block.spawnAsEntity(worldIn, p_176234_2_, new ItemStack(Items.apple, 1, 0));
        }
    }

    /**
     * Get the damage value that this Block should drop
     */
    @Override
    public int damageDropped(IBlockState state) {
        return ((BlockPlanks.EnumType) state.getValue(BlockNewLeaf.field_176240_P)).func_176839_a();
    }

    @Override
    public int getDamageValue(World worldIn, BlockPos pos) {
        IBlockState var3 = worldIn.getBlockState(pos);
        return var3.getBlock().getMetaFromState(var3) & 3;
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood
     * returns 4 blocks)
     */
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
    }

    @Override
    protected ItemStack createStackedBlock(IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this), 1, ((BlockPlanks.EnumType) state.getValue(BlockNewLeaf.field_176240_P)).func_176839_a() - 4);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BlockNewLeaf.field_176240_P, func_176233_b(meta)).withProperty(BlockLeaves.field_176237_a, Boolean.valueOf((meta & 4) == 0)).withProperty(BlockLeaves.field_176236_b, Boolean.valueOf((meta & 8) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        byte var2 = 0;
        int var3 = var2 | ((BlockPlanks.EnumType) state.getValue(BlockNewLeaf.field_176240_P)).func_176839_a() - 4;

        if (!((Boolean) state.getValue(BlockLeaves.field_176237_a)).booleanValue()) {
            var3 |= 4;
        }

        if (((Boolean) state.getValue(BlockLeaves.field_176236_b)).booleanValue()) {
            var3 |= 8;
        }

        return var3;
    }

    @Override
    public BlockPlanks.EnumType func_176233_b(int p_176233_1_) {
        return BlockPlanks.EnumType.func_176837_a((p_176233_1_ & 3) + 4);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{BlockNewLeaf.field_176240_P, BlockLeaves.field_176236_b, BlockLeaves.field_176237_a});
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer playerIn, BlockPos pos, IBlockState state, TileEntity te) {
        if (!worldIn.isRemote && playerIn.getCurrentEquippedItem() != null && playerIn.getCurrentEquippedItem().getItem() == Items.shears) {
            playerIn.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
            Block.spawnAsEntity(worldIn, pos, new ItemStack(Item.getItemFromBlock(this), 1, ((BlockPlanks.EnumType) state.getValue(BlockNewLeaf.field_176240_P)).func_176839_a() - 4));
        } else {
            super.harvestBlock(worldIn, playerIn, pos, state, te);
        }
    }
}
