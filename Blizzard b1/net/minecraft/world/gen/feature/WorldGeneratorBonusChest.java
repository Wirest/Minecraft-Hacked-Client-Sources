package net.minecraft.world.gen.feature;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;

public class WorldGeneratorBonusChest extends WorldGenerator
{
    private final List field_175909_a;

    /**
     * Value of this int will determine how much items gonna generate in Bonus Chest.
     */
    private final int itemsToGenerateInBonusChest;
    private static final String __OBFID = "CL_00000403";

    public WorldGeneratorBonusChest(List p_i45634_1_, int p_i45634_2_)
    {
        this.field_175909_a = p_i45634_1_;
        this.itemsToGenerateInBonusChest = p_i45634_2_;
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        Block var4;

        while (((var4 = worldIn.getBlockState(p_180709_3_).getBlock()).getMaterial() == Material.air || var4.getMaterial() == Material.leaves) && p_180709_3_.getY() > 1)
        {
            p_180709_3_ = p_180709_3_.offsetDown();
        }

        if (p_180709_3_.getY() < 1)
        {
            return false;
        }
        else
        {
            p_180709_3_ = p_180709_3_.offsetUp();

            for (int var5 = 0; var5 < 4; ++var5)
            {
                BlockPos var6 = p_180709_3_.add(p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4), p_180709_2_.nextInt(3) - p_180709_2_.nextInt(3), p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4));

                if (worldIn.isAirBlock(var6) && World.doesBlockHaveSolidTopSurface(worldIn, var6.offsetDown()))
                {
                    worldIn.setBlockState(var6, Blocks.chest.getDefaultState(), 2);
                    TileEntity var7 = worldIn.getTileEntity(var6);

                    if (var7 instanceof TileEntityChest)
                    {
                        WeightedRandomChestContent.generateChestContents(p_180709_2_, this.field_175909_a, (TileEntityChest)var7, this.itemsToGenerateInBonusChest);
                    }

                    BlockPos var8 = var6.offsetEast();
                    BlockPos var9 = var6.offsetWest();
                    BlockPos var10 = var6.offsetNorth();
                    BlockPos var11 = var6.offsetSouth();

                    if (worldIn.isAirBlock(var9) && World.doesBlockHaveSolidTopSurface(worldIn, var9.offsetDown()))
                    {
                        worldIn.setBlockState(var9, Blocks.torch.getDefaultState(), 2);
                    }

                    if (worldIn.isAirBlock(var8) && World.doesBlockHaveSolidTopSurface(worldIn, var8.offsetDown()))
                    {
                        worldIn.setBlockState(var8, Blocks.torch.getDefaultState(), 2);
                    }

                    if (worldIn.isAirBlock(var10) && World.doesBlockHaveSolidTopSurface(worldIn, var10.offsetDown()))
                    {
                        worldIn.setBlockState(var10, Blocks.torch.getDefaultState(), 2);
                    }

                    if (worldIn.isAirBlock(var11) && World.doesBlockHaveSolidTopSurface(worldIn, var11.offsetDown()))
                    {
                        worldIn.setBlockState(var11, Blocks.torch.getDefaultState(), 2);
                    }

                    return true;
                }
            }

            return false;
        }
    }
}
