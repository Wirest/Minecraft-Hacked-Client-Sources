// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public class BlockCommandBlock extends BlockContainer
{
    public static final PropertyBool TRIGGERED;
    
    static {
        TRIGGERED = PropertyBool.create("triggered");
    }
    
    public BlockCommandBlock() {
        super(Material.iron, MapColor.adobeColor);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockCommandBlock.TRIGGERED, false));
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityCommandBlock();
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!worldIn.isRemote) {
            final boolean flag = worldIn.isBlockPowered(pos);
            final boolean flag2 = state.getValue((IProperty<Boolean>)BlockCommandBlock.TRIGGERED);
            if (flag && !flag2) {
                worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockCommandBlock.TRIGGERED, true), 4);
                worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
            }
            else if (!flag && flag2) {
                worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockCommandBlock.TRIGGERED, false), 4);
            }
        }
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityCommandBlock) {
            ((TileEntityCommandBlock)tileentity).getCommandBlockLogic().trigger(worldIn);
            worldIn.updateComparatorOutputLevel(pos, this);
        }
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return 1;
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity instanceof TileEntityCommandBlock && ((TileEntityCommandBlock)tileentity).getCommandBlockLogic().tryOpenEditCommandBlock(playerIn);
    }
    
    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }
    
    @Override
    public int getComparatorInputOverride(final World worldIn, final BlockPos pos) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        return (tileentity instanceof TileEntityCommandBlock) ? ((TileEntityCommandBlock)tileentity).getCommandBlockLogic().getSuccessCount() : 0;
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityCommandBlock) {
            final CommandBlockLogic commandblocklogic = ((TileEntityCommandBlock)tileentity).getCommandBlockLogic();
            if (stack.hasDisplayName()) {
                commandblocklogic.setName(stack.getDisplayName());
            }
            if (!worldIn.isRemote) {
                commandblocklogic.setTrackOutput(worldIn.getGameRules().getBoolean("sendCommandFeedback"));
            }
        }
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    public int getRenderType() {
        return 3;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockCommandBlock.TRIGGERED, (meta & 0x1) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        if (state.getValue((IProperty<Boolean>)BlockCommandBlock.TRIGGERED)) {
            i |= 0x1;
        }
        return i;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockCommandBlock.TRIGGERED });
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockCommandBlock.TRIGGERED, false);
    }
}
