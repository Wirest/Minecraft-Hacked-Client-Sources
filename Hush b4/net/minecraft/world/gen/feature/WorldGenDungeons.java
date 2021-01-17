// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.tileentity.TileEntity;
import java.util.Iterator;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import com.google.common.collect.Lists;
import net.minecraft.init.Items;
import org.apache.logging.log4j.LogManager;
import net.minecraft.util.WeightedRandomChestContent;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class WorldGenDungeons extends WorldGenerator
{
    private static final Logger field_175918_a;
    private static final String[] SPAWNERTYPES;
    private static final List<WeightedRandomChestContent> CHESTCONTENT;
    
    static {
        field_175918_a = LogManager.getLogger();
        SPAWNERTYPES = new String[] { "Skeleton", "Zombie", "Zombie", "Spider" };
        CHESTCONTENT = Lists.newArrayList(new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 10), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 4, 10), new WeightedRandomChestContent(Items.bread, 0, 1, 1, 10), new WeightedRandomChestContent(Items.wheat, 0, 1, 4, 10), new WeightedRandomChestContent(Items.gunpowder, 0, 1, 4, 10), new WeightedRandomChestContent(Items.string, 0, 1, 4, 10), new WeightedRandomChestContent(Items.bucket, 0, 1, 1, 10), new WeightedRandomChestContent(Items.golden_apple, 0, 1, 1, 1), new WeightedRandomChestContent(Items.redstone, 0, 1, 4, 10), new WeightedRandomChestContent(Items.record_13, 0, 1, 1, 4), new WeightedRandomChestContent(Items.record_cat, 0, 1, 1, 4), new WeightedRandomChestContent(Items.name_tag, 0, 1, 1, 10), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 2), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 5), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1));
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        final int i = 3;
        final int j = rand.nextInt(2) + 2;
        final int k = -j - 1;
        final int l = j + 1;
        final int i2 = -1;
        final int j2 = 4;
        final int k2 = rand.nextInt(2) + 2;
        final int l2 = -k2 - 1;
        final int i3 = k2 + 1;
        int j3 = 0;
        for (int k3 = k; k3 <= l; ++k3) {
            for (int l3 = -1; l3 <= 4; ++l3) {
                for (int i4 = l2; i4 <= i3; ++i4) {
                    final BlockPos blockpos = position.add(k3, l3, i4);
                    final Material material = worldIn.getBlockState(blockpos).getBlock().getMaterial();
                    final boolean flag = material.isSolid();
                    if (l3 == -1 && !flag) {
                        return false;
                    }
                    if (l3 == 4 && !flag) {
                        return false;
                    }
                    if ((k3 == k || k3 == l || i4 == l2 || i4 == i3) && l3 == 0 && worldIn.isAirBlock(blockpos) && worldIn.isAirBlock(blockpos.up())) {
                        ++j3;
                    }
                }
            }
        }
        if (j3 >= 1 && j3 <= 5) {
            for (int k4 = k; k4 <= l; ++k4) {
                for (int i5 = 3; i5 >= -1; --i5) {
                    for (int k5 = l2; k5 <= i3; ++k5) {
                        final BlockPos blockpos2 = position.add(k4, i5, k5);
                        if (k4 != k && i5 != -1 && k5 != l2 && k4 != l && i5 != 4 && k5 != i3) {
                            if (worldIn.getBlockState(blockpos2).getBlock() != Blocks.chest) {
                                worldIn.setBlockToAir(blockpos2);
                            }
                        }
                        else if (blockpos2.getY() >= 0 && !worldIn.getBlockState(blockpos2.down()).getBlock().getMaterial().isSolid()) {
                            worldIn.setBlockToAir(blockpos2);
                        }
                        else if (worldIn.getBlockState(blockpos2).getBlock().getMaterial().isSolid() && worldIn.getBlockState(blockpos2).getBlock() != Blocks.chest) {
                            if (i5 == -1 && rand.nextInt(4) != 0) {
                                worldIn.setBlockState(blockpos2, Blocks.mossy_cobblestone.getDefaultState(), 2);
                            }
                            else {
                                worldIn.setBlockState(blockpos2, Blocks.cobblestone.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }
            for (int l4 = 0; l4 < 2; ++l4) {
                for (int j4 = 0; j4 < 3; ++j4) {
                    final int l5 = position.getX() + rand.nextInt(j * 2 + 1) - j;
                    final int i6 = position.getY();
                    final int j5 = position.getZ() + rand.nextInt(k2 * 2 + 1) - k2;
                    final BlockPos blockpos3 = new BlockPos(l5, i6, j5);
                    if (worldIn.isAirBlock(blockpos3)) {
                        int j6 = 0;
                        for (final Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
                            if (worldIn.getBlockState(blockpos3.offset((EnumFacing)enumfacing)).getBlock().getMaterial().isSolid()) {
                                ++j6;
                            }
                        }
                        if (j6 == 1) {
                            worldIn.setBlockState(blockpos3, Blocks.chest.correctFacing(worldIn, blockpos3, Blocks.chest.getDefaultState()), 2);
                            final List<WeightedRandomChestContent> list = WeightedRandomChestContent.func_177629_a(WorldGenDungeons.CHESTCONTENT, Items.enchanted_book.getRandom(rand));
                            final TileEntity tileentity1 = worldIn.getTileEntity(blockpos3);
                            if (tileentity1 instanceof TileEntityChest) {
                                WeightedRandomChestContent.generateChestContents(rand, list, (IInventory)tileentity1, 8);
                                break;
                            }
                            break;
                        }
                    }
                }
            }
            worldIn.setBlockState(position, Blocks.mob_spawner.getDefaultState(), 2);
            final TileEntity tileentity2 = worldIn.getTileEntity(position);
            if (tileentity2 instanceof TileEntityMobSpawner) {
                ((TileEntityMobSpawner)tileentity2).getSpawnerBaseLogic().setEntityName(this.pickMobSpawner(rand));
            }
            else {
                WorldGenDungeons.field_175918_a.error("Failed to fetch mob spawner entity at (" + position.getX() + ", " + position.getY() + ", " + position.getZ() + ")");
            }
            return true;
        }
        return false;
    }
    
    private String pickMobSpawner(final Random p_76543_1_) {
        return WorldGenDungeons.SPAWNERTYPES[p_76543_1_.nextInt(WorldGenDungeons.SPAWNERTYPES.length)];
    }
}
