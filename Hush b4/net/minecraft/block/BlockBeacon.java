// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.WorldServer;
import net.minecraft.init.Blocks;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.stats.StatList;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockBeacon extends BlockContainer
{
    public BlockBeacon() {
        super(Material.glass, MapColor.diamondColor);
        this.setHardness(3.0f);
        this.setCreativeTab(CreativeTabs.tabMisc);
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityBeacon();
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityBeacon) {
            playerIn.displayGUIChest((IInventory)tileentity);
            playerIn.triggerAchievement(StatList.field_181730_N);
        }
        return true;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public int getRenderType() {
        return 3;
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (stack.hasDisplayName()) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntityBeacon) {
                ((TileEntityBeacon)tileentity).setName(stack.getDisplayName());
            }
        }
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityBeacon) {
            ((TileEntityBeacon)tileentity).updateBeacon();
            worldIn.addBlockEvent(pos, this, 1, 0);
        }
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    public static void updateColorAsync(final World worldIn, final BlockPos glassPos) {
        HttpUtil.field_180193_a.submit((Runnable)new Runnable() {
            @Override
            public void run() {
                final Chunk chunk = worldIn.getChunkFromBlockCoords(glassPos);
                for (int i = glassPos.getY() - 1; i >= 0; --i) {
                    final BlockPos blockpos = new BlockPos(glassPos.getX(), i, glassPos.getZ());
                    if (!chunk.canSeeSky(blockpos)) {
                        break;
                    }
                    final IBlockState iblockstate = worldIn.getBlockState(blockpos);
                    if (iblockstate.getBlock() == Blocks.beacon) {
                        ((WorldServer)worldIn).addScheduledTask(new Runnable() {
                            @Override
                            public void run() {
                                final TileEntity tileentity = worldIn.getTileEntity(blockpos);
                                if (tileentity instanceof TileEntityBeacon) {
                                    ((TileEntityBeacon)tileentity).updateBeacon();
                                    worldIn.addBlockEvent(blockpos, Blocks.beacon, 1, 0);
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}
