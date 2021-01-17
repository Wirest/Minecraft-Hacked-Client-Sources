// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.BlockState;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import com.google.common.base.Predicate;
import net.minecraft.block.properties.PropertyEnum;

public class BlockNewLeaf extends BlockLeaves
{
    public static final PropertyEnum<BlockPlanks.EnumType> VARIANT;
    
    static {
        VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, new Predicate<BlockPlanks.EnumType>() {
            @Override
            public boolean apply(final BlockPlanks.EnumType p_apply_1_) {
                return p_apply_1_.getMetadata() >= 4;
            }
        });
    }
    
    public BlockNewLeaf() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty((IProperty<Comparable>)BlockNewLeaf.CHECK_DECAY, true).withProperty((IProperty<Comparable>)BlockNewLeaf.DECAYABLE, true));
    }
    
    @Override
    protected void dropApple(final World worldIn, final BlockPos pos, final IBlockState state, final int chance) {
        if (state.getValue(BlockNewLeaf.VARIANT) == BlockPlanks.EnumType.DARK_OAK && worldIn.rand.nextInt(chance) == 0) {
            Block.spawnAsEntity(worldIn, pos, new ItemStack(Items.apple, 1, 0));
        }
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(BlockNewLeaf.VARIANT).getMetadata();
    }
    
    @Override
    public int getDamageValue(final World worldIn, final BlockPos pos) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        return iblockstate.getBlock().getMetaFromState(iblockstate) & 0x3;
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List<ItemStack> list) {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
    }
    
    @Override
    protected ItemStack createStackedBlock(final IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this), 1, state.getValue(BlockNewLeaf.VARIANT).getMetadata() - 4);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockNewLeaf.VARIANT, this.getWoodType(meta)).withProperty((IProperty<Comparable>)BlockNewLeaf.DECAYABLE, (meta & 0x4) == 0x0).withProperty((IProperty<Comparable>)BlockNewLeaf.CHECK_DECAY, (meta & 0x8) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue(BlockNewLeaf.VARIANT).getMetadata() - 4;
        if (!state.getValue((IProperty<Boolean>)BlockNewLeaf.DECAYABLE)) {
            i |= 0x4;
        }
        if (state.getValue((IProperty<Boolean>)BlockNewLeaf.CHECK_DECAY)) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    public BlockPlanks.EnumType getWoodType(final int meta) {
        return BlockPlanks.EnumType.byMetadata((meta & 0x3) + 4);
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockNewLeaf.VARIANT, BlockNewLeaf.CHECK_DECAY, BlockNewLeaf.DECAYABLE });
    }
    
    @Override
    public void harvestBlock(final World worldIn, final EntityPlayer player, final BlockPos pos, final IBlockState state, final TileEntity te) {
        if (!worldIn.isRemote && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears) {
            player.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
            Block.spawnAsEntity(worldIn, pos, new ItemStack(Item.getItemFromBlock(this), 1, state.getValue(BlockNewLeaf.VARIANT).getMetadata() - 4));
        }
        else {
            super.harvestBlock(worldIn, player, pos, state, te);
        }
    }
}
