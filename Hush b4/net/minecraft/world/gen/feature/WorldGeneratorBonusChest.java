// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.util.WeightedRandomChestContent;
import java.util.List;

public class WorldGeneratorBonusChest extends WorldGenerator
{
    private final List<WeightedRandomChestContent> chestItems;
    private final int itemsToGenerateInBonusChest;
    
    public WorldGeneratorBonusChest(final List<WeightedRandomChestContent> p_i45634_1_, final int p_i45634_2_) {
        this.chestItems = p_i45634_1_;
        this.itemsToGenerateInBonusChest = p_i45634_2_;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, BlockPos position) {
        Block block;
        while (((block = worldIn.getBlockState(position).getBlock()).getMaterial() == Material.air || block.getMaterial() == Material.leaves) && position.getY() > 1) {
            position = position.down();
        }
        if (position.getY() < 1) {
            return false;
        }
        position = position.up();
        for (int i = 0; i < 4; ++i) {
            final BlockPos blockpos = position.add(rand.nextInt(4) - rand.nextInt(4), rand.nextInt(3) - rand.nextInt(3), rand.nextInt(4) - rand.nextInt(4));
            if (worldIn.isAirBlock(blockpos) && World.doesBlockHaveSolidTopSurface(worldIn, blockpos.down())) {
                worldIn.setBlockState(blockpos, Blocks.chest.getDefaultState(), 2);
                final TileEntity tileentity = worldIn.getTileEntity(blockpos);
                if (tileentity instanceof TileEntityChest) {
                    WeightedRandomChestContent.generateChestContents(rand, this.chestItems, (IInventory)tileentity, this.itemsToGenerateInBonusChest);
                }
                final BlockPos blockpos2 = blockpos.east();
                final BlockPos blockpos3 = blockpos.west();
                final BlockPos blockpos4 = blockpos.north();
                final BlockPos blockpos5 = blockpos.south();
                if (worldIn.isAirBlock(blockpos3) && World.doesBlockHaveSolidTopSurface(worldIn, blockpos3.down())) {
                    worldIn.setBlockState(blockpos3, Blocks.torch.getDefaultState(), 2);
                }
                if (worldIn.isAirBlock(blockpos2) && World.doesBlockHaveSolidTopSurface(worldIn, blockpos2.down())) {
                    worldIn.setBlockState(blockpos2, Blocks.torch.getDefaultState(), 2);
                }
                if (worldIn.isAirBlock(blockpos4) && World.doesBlockHaveSolidTopSurface(worldIn, blockpos4.down())) {
                    worldIn.setBlockState(blockpos4, Blocks.torch.getDefaultState(), 2);
                }
                if (worldIn.isAirBlock(blockpos5) && World.doesBlockHaveSolidTopSurface(worldIn, blockpos5.down())) {
                    worldIn.setBlockState(blockpos5, Blocks.torch.getDefaultState(), 2);
                }
                return true;
            }
        }
        return false;
    }
}
