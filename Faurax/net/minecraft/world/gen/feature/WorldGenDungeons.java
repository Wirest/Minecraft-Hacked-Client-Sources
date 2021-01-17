package net.minecraft.world.gen.feature;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldGenDungeons extends WorldGenerator
{
    private static final Logger field_175918_a = LogManager.getLogger();
    private static final String[] SPAWNERTYPES = new String[] {"Skeleton", "Zombie", "Zombie", "Spider"};
    private static final List CHESTCONTENT = Lists.newArrayList(new WeightedRandomChestContent[] {new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 10), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 4, 10), new WeightedRandomChestContent(Items.bread, 0, 1, 1, 10), new WeightedRandomChestContent(Items.wheat, 0, 1, 4, 10), new WeightedRandomChestContent(Items.gunpowder, 0, 1, 4, 10), new WeightedRandomChestContent(Items.string, 0, 1, 4, 10), new WeightedRandomChestContent(Items.bucket, 0, 1, 1, 10), new WeightedRandomChestContent(Items.golden_apple, 0, 1, 1, 1), new WeightedRandomChestContent(Items.redstone, 0, 1, 4, 10), new WeightedRandomChestContent(Items.record_13, 0, 1, 1, 4), new WeightedRandomChestContent(Items.record_cat, 0, 1, 1, 4), new WeightedRandomChestContent(Items.name_tag, 0, 1, 1, 10), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 2), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 5), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1)});
    private static final String __OBFID = "CL_00000425";

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        boolean var4 = true;
        int var5 = p_180709_2_.nextInt(2) + 2;
        int var6 = -var5 - 1;
        int var7 = var5 + 1;
        boolean var8 = true;
        boolean var9 = true;
        int var10 = p_180709_2_.nextInt(2) + 2;
        int var11 = -var10 - 1;
        int var12 = var10 + 1;
        int var13 = 0;
        int var14;
        int var15;
        int var16;
        BlockPos var17;

        for (var14 = var6; var14 <= var7; ++var14)
        {
            for (var15 = -1; var15 <= 4; ++var15)
            {
                for (var16 = var11; var16 <= var12; ++var16)
                {
                    var17 = p_180709_3_.add(var14, var15, var16);
                    Material var18 = worldIn.getBlockState(var17).getBlock().getMaterial();
                    boolean var19 = var18.isSolid();

                    if (var15 == -1 && !var19)
                    {
                        return false;
                    }

                    if (var15 == 4 && !var19)
                    {
                        return false;
                    }

                    if ((var14 == var6 || var14 == var7 || var16 == var11 || var16 == var12) && var15 == 0 && worldIn.isAirBlock(var17) && worldIn.isAirBlock(var17.offsetUp()))
                    {
                        ++var13;
                    }
                }
            }
        }

        if (var13 >= 1 && var13 <= 5)
        {
            for (var14 = var6; var14 <= var7; ++var14)
            {
                for (var15 = 3; var15 >= -1; --var15)
                {
                    for (var16 = var11; var16 <= var12; ++var16)
                    {
                        var17 = p_180709_3_.add(var14, var15, var16);

                        if (var14 != var6 && var15 != -1 && var16 != var11 && var14 != var7 && var15 != 4 && var16 != var12)
                        {
                            if (worldIn.getBlockState(var17).getBlock() != Blocks.chest)
                            {
                                worldIn.setBlockToAir(var17);
                            }
                        }
                        else if (var17.getY() >= 0 && !worldIn.getBlockState(var17.offsetDown()).getBlock().getMaterial().isSolid())
                        {
                            worldIn.setBlockToAir(var17);
                        }
                        else if (worldIn.getBlockState(var17).getBlock().getMaterial().isSolid() && worldIn.getBlockState(var17).getBlock() != Blocks.chest)
                        {
                            if (var15 == -1 && p_180709_2_.nextInt(4) != 0)
                            {
                                worldIn.setBlockState(var17, Blocks.mossy_cobblestone.getDefaultState(), 2);
                            }
                            else
                            {
                                worldIn.setBlockState(var17, Blocks.cobblestone.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }

            var14 = 0;

            while (var14 < 2)
            {
                var15 = 0;

                while (true)
                {
                    if (var15 < 3)
                    {
                        label197:
                        {
                            var16 = p_180709_3_.getX() + p_180709_2_.nextInt(var5 * 2 + 1) - var5;
                            int var24 = p_180709_3_.getY();
                            int var25 = p_180709_3_.getZ() + p_180709_2_.nextInt(var10 * 2 + 1) - var10;
                            BlockPos var26 = new BlockPos(var16, var24, var25);

                            if (worldIn.isAirBlock(var26))
                            {
                                int var20 = 0;
                                Iterator var21 = EnumFacing.Plane.HORIZONTAL.iterator();

                                while (var21.hasNext())
                                {
                                    EnumFacing var22 = (EnumFacing)var21.next();

                                    if (worldIn.getBlockState(var26.offset(var22)).getBlock().getMaterial().isSolid())
                                    {
                                        ++var20;
                                    }
                                }

                                if (var20 == 1)
                                {
                                    worldIn.setBlockState(var26, Blocks.chest.func_176458_f(worldIn, var26, Blocks.chest.getDefaultState()), 2);
                                    List var27 = WeightedRandomChestContent.func_177629_a(CHESTCONTENT, new WeightedRandomChestContent[] {Items.enchanted_book.getRandomEnchantedBook(p_180709_2_)});
                                    TileEntity var28 = worldIn.getTileEntity(var26);

                                    if (var28 instanceof TileEntityChest)
                                    {
                                        WeightedRandomChestContent.generateChestContents(p_180709_2_, var27, (TileEntityChest)var28, 8);
                                    }

                                    break label197;
                                }
                            }

                            ++var15;
                            continue;
                        }
                    }

                    ++var14;
                    break;
                }
            }

            worldIn.setBlockState(p_180709_3_, Blocks.mob_spawner.getDefaultState(), 2);
            TileEntity var23 = worldIn.getTileEntity(p_180709_3_);

            if (var23 instanceof TileEntityMobSpawner)
            {
                ((TileEntityMobSpawner)var23).getSpawnerBaseLogic().setEntityName(this.pickMobSpawner(p_180709_2_));
            }
            else
            {
                field_175918_a.error("Failed to fetch mob spawner entity at (" + p_180709_3_.getX() + ", " + p_180709_3_.getY() + ", " + p_180709_3_.getZ() + ")");
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Randomly decides which spawner to use in a dungeon
     */
    private String pickMobSpawner(Random p_76543_1_)
    {
        return SPAWNERTYPES[p_76543_1_.nextInt(SPAWNERTYPES.length)];
    }
}
