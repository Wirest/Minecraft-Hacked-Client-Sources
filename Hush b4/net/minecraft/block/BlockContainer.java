// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public abstract class BlockContainer extends Block implements ITileEntityProvider
{
    protected BlockContainer(final Material materialIn) {
        this(materialIn, materialIn.getMaterialMapColor());
    }
    
    protected BlockContainer(final Material p_i46402_1_, final MapColor p_i46402_2_) {
        super(p_i46402_1_, p_i46402_2_);
        this.isBlockContainer = true;
    }
    
    protected boolean func_181086_a(final World p_181086_1_, final BlockPos p_181086_2_, final EnumFacing p_181086_3_) {
        return p_181086_1_.getBlockState(p_181086_2_.offset(p_181086_3_)).getBlock().getMaterial() == Material.cactus;
    }
    
    protected boolean func_181087_e(final World p_181087_1_, final BlockPos p_181087_2_) {
        return this.func_181086_a(p_181087_1_, p_181087_2_, EnumFacing.NORTH) || this.func_181086_a(p_181087_1_, p_181087_2_, EnumFacing.SOUTH) || this.func_181086_a(p_181087_1_, p_181087_2_, EnumFacing.WEST) || this.func_181086_a(p_181087_1_, p_181087_2_, EnumFacing.EAST);
    }
    
    @Override
    public int getRenderType() {
        return -1;
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }
    
    @Override
    public boolean onBlockEventReceived(final World worldIn, final BlockPos pos, final IBlockState state, final int eventID, final int eventParam) {
        super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(eventID, eventParam);
    }
}
