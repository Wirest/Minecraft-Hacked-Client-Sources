package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class BlockEnchantmentTable extends BlockContainer {
    private static final String __OBFID = "CL_00000235";

    protected BlockEnchantmentTable() {
        super(Material.rock);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
        this.setLightOpacity(0);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public boolean isFullCube() {
        return false;
    }

    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.randomDisplayTick(worldIn, pos, state, rand);

        for (int var5 = -2; var5 <= 2; ++var5) {
            for (int var6 = -2; var6 <= 2; ++var6) {
                if (var5 > -2 && var5 < 2 && var6 == -1) {
                    var6 = 2;
                }

                if (rand.nextInt(16) == 0) {
                    for (int var7 = 0; var7 <= 1; ++var7) {
                        BlockPos var8 = pos.add(var5, var7, var6);

                        if (worldIn.getBlockState(var8).getBlock() == Blocks.bookshelf) {
                            if (!worldIn.isAirBlock(pos.add(var5 / 2, 0, var6 / 2))) {
                                break;
                            }

                            worldIn.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, (double) pos.getX() + 0.5D, (double) pos.getY() + 2.0D, (double) pos.getZ() + 0.5D, (double) ((float) var5 + rand.nextFloat()) - 0.5D, (double) ((float) var7 - rand.nextFloat() - 1.0F), (double) ((float) var6 + rand.nextFloat()) - 0.5D, new int[0]);
                        }
                    }
                }
            }
        }
    }

    public boolean isOpaqueCube() {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType() {
        return 3;
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityEnchantmentTable();
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        } else {
            TileEntity var9 = worldIn.getTileEntity(pos);

            if (var9 instanceof TileEntityEnchantmentTable) {
                playerIn.displayGui((TileEntityEnchantmentTable) var9);
            }

            return true;
        }
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        if (stack.hasDisplayName()) {
            TileEntity var6 = worldIn.getTileEntity(pos);

            if (var6 instanceof TileEntityEnchantmentTable) {
                ((TileEntityEnchantmentTable) var6).func_145920_a(stack.getDisplayName());
            }
        }
    }
}
