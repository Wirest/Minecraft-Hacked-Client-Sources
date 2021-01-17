// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import com.google.common.base.Predicate;
import net.minecraft.block.properties.PropertyEnum;

public class BlockOldLog extends BlockLog
{
    public static final PropertyEnum<BlockPlanks.EnumType> VARIANT;
    
    static {
        VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, new Predicate<BlockPlanks.EnumType>() {
            @Override
            public boolean apply(final BlockPlanks.EnumType p_apply_1_) {
                return p_apply_1_.getMetadata() < 4;
            }
        });
    }
    
    public BlockOldLog() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockOldLog.LOG_AXIS, EnumAxis.Y));
    }
    
    @Override
    public MapColor getMapColor(final IBlockState state) {
        final BlockPlanks.EnumType blockplanks$enumtype = state.getValue(BlockOldLog.VARIANT);
        switch (state.getValue(BlockOldLog.LOG_AXIS)) {
            default: {
                switch (blockplanks$enumtype) {
                    default: {
                        return BlockPlanks.EnumType.SPRUCE.func_181070_c();
                    }
                    case SPRUCE: {
                        return BlockPlanks.EnumType.DARK_OAK.func_181070_c();
                    }
                    case BIRCH: {
                        return MapColor.quartzColor;
                    }
                    case JUNGLE: {
                        return BlockPlanks.EnumType.SPRUCE.func_181070_c();
                    }
                }
                break;
            }
            case Y: {
                return blockplanks$enumtype.func_181070_c();
            }
        }
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List<ItemStack> list) {
        list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.OAK.getMetadata()));
        list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()));
        list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.BIRCH.getMetadata()));
        list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()));
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        IBlockState iblockstate = this.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.byMetadata((meta & 0x3) % 4));
        switch (meta & 0xC) {
            case 0: {
                iblockstate = iblockstate.withProperty(BlockOldLog.LOG_AXIS, EnumAxis.Y);
                break;
            }
            case 4: {
                iblockstate = iblockstate.withProperty(BlockOldLog.LOG_AXIS, EnumAxis.X);
                break;
            }
            case 8: {
                iblockstate = iblockstate.withProperty(BlockOldLog.LOG_AXIS, EnumAxis.Z);
                break;
            }
            default: {
                iblockstate = iblockstate.withProperty(BlockOldLog.LOG_AXIS, EnumAxis.NONE);
                break;
            }
        }
        return iblockstate;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue(BlockOldLog.VARIANT).getMetadata();
        switch (state.getValue(BlockOldLog.LOG_AXIS)) {
            case X: {
                i |= 0x4;
                break;
            }
            case Z: {
                i |= 0x8;
                break;
            }
            case NONE: {
                i |= 0xC;
                break;
            }
        }
        return i;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockOldLog.VARIANT, BlockOldLog.LOG_AXIS });
    }
    
    @Override
    protected ItemStack createStackedBlock(final IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this), 1, state.getValue(BlockOldLog.VARIANT).getMetadata());
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(BlockOldLog.VARIANT).getMetadata();
    }
}
