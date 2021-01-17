// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import java.util.List;
import java.util.Collection;
import net.minecraft.util.WeightedRandom;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.block.Block;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import com.google.common.collect.Sets;
import java.util.Set;

public final class SpawnerAnimals
{
    private static final int MOB_COUNT_DIV;
    private final Set<ChunkCoordIntPair> eligibleChunksForSpawning;
    
    static {
        MOB_COUNT_DIV = (int)Math.pow(17.0, 2.0);
    }
    
    public SpawnerAnimals() {
        this.eligibleChunksForSpawning = (Set<ChunkCoordIntPair>)Sets.newHashSet();
    }
    
    public int findChunksForSpawning(final WorldServer p_77192_1_, final boolean spawnHostileMobs, final boolean spawnPeacefulMobs, final boolean p_77192_4_) {
        if (!spawnHostileMobs && !spawnPeacefulMobs) {
            return 0;
        }
        this.eligibleChunksForSpawning.clear();
        int i = 0;
        for (final EntityPlayer entityplayer : p_77192_1_.playerEntities) {
            if (!entityplayer.isSpectator()) {
                final int j = MathHelper.floor_double(entityplayer.posX / 16.0);
                final int k = MathHelper.floor_double(entityplayer.posZ / 16.0);
                for (int l = 8, i2 = -l; i2 <= l; ++i2) {
                    for (int j2 = -l; j2 <= l; ++j2) {
                        final boolean flag = i2 == -l || i2 == l || j2 == -l || j2 == l;
                        final ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(i2 + j, j2 + k);
                        if (!this.eligibleChunksForSpawning.contains(chunkcoordintpair)) {
                            ++i;
                            if (!flag && p_77192_1_.getWorldBorder().contains(chunkcoordintpair)) {
                                this.eligibleChunksForSpawning.add(chunkcoordintpair);
                            }
                        }
                    }
                }
            }
        }
        int i3 = 0;
        final BlockPos blockpos2 = p_77192_1_.getSpawnPoint();
        EnumCreatureType[] values;
        for (int length = (values = EnumCreatureType.values()).length, n = 0; n < length; ++n) {
            final EnumCreatureType enumcreaturetype = values[n];
            if ((!enumcreaturetype.getPeacefulCreature() || spawnPeacefulMobs) && (enumcreaturetype.getPeacefulCreature() || spawnHostileMobs) && (!enumcreaturetype.getAnimal() || p_77192_4_)) {
                final int j3 = p_77192_1_.countEntities(enumcreaturetype.getCreatureClass());
                final int k2 = enumcreaturetype.getMaxNumberOfCreature() * i / SpawnerAnimals.MOB_COUNT_DIV;
                if (j3 <= k2) {
                Label_0816:
                    while (true) {
                        for (final ChunkCoordIntPair chunkcoordintpair2 : this.eligibleChunksForSpawning) {
                            final BlockPos blockpos3 = getRandomChunkPosition(p_77192_1_, chunkcoordintpair2.chunkXPos, chunkcoordintpair2.chunkZPos);
                            final int k3 = blockpos3.getX();
                            final int l2 = blockpos3.getY();
                            final int i4 = blockpos3.getZ();
                            final Block block = p_77192_1_.getBlockState(blockpos3).getBlock();
                            if (!block.isNormalCube()) {
                                int j4 = 0;
                                for (int k4 = 0; k4 < 3; ++k4) {
                                    int l3 = k3;
                                    int i5 = l2;
                                    int j5 = i4;
                                    final int k5 = 6;
                                    BiomeGenBase.SpawnListEntry biomegenbase$spawnlistentry = null;
                                    IEntityLivingData ientitylivingdata = null;
                                    for (int l4 = 0; l4 < 4; ++l4) {
                                        l3 += p_77192_1_.rand.nextInt(k5) - p_77192_1_.rand.nextInt(k5);
                                        i5 += p_77192_1_.rand.nextInt(1) - p_77192_1_.rand.nextInt(1);
                                        j5 += p_77192_1_.rand.nextInt(k5) - p_77192_1_.rand.nextInt(k5);
                                        final BlockPos blockpos4 = new BlockPos(l3, i5, j5);
                                        final float f = l3 + 0.5f;
                                        final float f2 = j5 + 0.5f;
                                        if (!p_77192_1_.isAnyPlayerWithinRangeAt(f, i5, f2, 24.0) && blockpos2.distanceSq(f, i5, f2) >= 576.0) {
                                            if (biomegenbase$spawnlistentry == null) {
                                                biomegenbase$spawnlistentry = p_77192_1_.getSpawnListEntryForTypeAt(enumcreaturetype, blockpos4);
                                                if (biomegenbase$spawnlistentry == null) {
                                                    break;
                                                }
                                            }
                                            if (p_77192_1_.canCreatureTypeSpawnHere(enumcreaturetype, biomegenbase$spawnlistentry, blockpos4) && canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.getPlacementForEntity(biomegenbase$spawnlistentry.entityClass), p_77192_1_, blockpos4)) {
                                                EntityLiving entityliving;
                                                try {
                                                    entityliving = (EntityLiving)biomegenbase$spawnlistentry.entityClass.getConstructor(World.class).newInstance(p_77192_1_);
                                                }
                                                catch (Exception exception) {
                                                    exception.printStackTrace();
                                                    return i3;
                                                }
                                                entityliving.setLocationAndAngles(f, i5, f2, p_77192_1_.rand.nextFloat() * 360.0f, 0.0f);
                                                if (entityliving.getCanSpawnHere() && entityliving.isNotColliding()) {
                                                    ientitylivingdata = entityliving.onInitialSpawn(p_77192_1_.getDifficultyForLocation(new BlockPos(entityliving)), ientitylivingdata);
                                                    if (entityliving.isNotColliding()) {
                                                        ++j4;
                                                        p_77192_1_.spawnEntityInWorld(entityliving);
                                                    }
                                                    if (j4 >= entityliving.getMaxSpawnedInChunk()) {
                                                        continue Label_0816;
                                                    }
                                                }
                                                i3 += j4;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
        return i3;
    }
    
    protected static BlockPos getRandomChunkPosition(final World worldIn, final int x, final int z) {
        final Chunk chunk = worldIn.getChunkFromChunkCoords(x, z);
        final int i = x * 16 + worldIn.rand.nextInt(16);
        final int j = z * 16 + worldIn.rand.nextInt(16);
        final int k = MathHelper.func_154354_b(chunk.getHeight(new BlockPos(i, 0, j)) + 1, 16);
        final int l = worldIn.rand.nextInt((k > 0) ? k : (chunk.getTopFilledSegment() + 16 - 1));
        return new BlockPos(i, l, j);
    }
    
    public static boolean canCreatureTypeSpawnAtLocation(final EntityLiving.SpawnPlacementType p_180267_0_, final World worldIn, final BlockPos pos) {
        if (!worldIn.getWorldBorder().contains(pos)) {
            return false;
        }
        final Block block = worldIn.getBlockState(pos).getBlock();
        if (p_180267_0_ == EntityLiving.SpawnPlacementType.IN_WATER) {
            return block.getMaterial().isLiquid() && worldIn.getBlockState(pos.down()).getBlock().getMaterial().isLiquid() && !worldIn.getBlockState(pos.up()).getBlock().isNormalCube();
        }
        final BlockPos blockpos = pos.down();
        if (!World.doesBlockHaveSolidTopSurface(worldIn, blockpos)) {
            return false;
        }
        final Block block2 = worldIn.getBlockState(blockpos).getBlock();
        final boolean flag = block2 != Blocks.bedrock && block2 != Blocks.barrier;
        return flag && !block.isNormalCube() && !block.getMaterial().isLiquid() && !worldIn.getBlockState(pos.up()).getBlock().isNormalCube();
    }
    
    public static void performWorldGenSpawning(final World worldIn, final BiomeGenBase p_77191_1_, final int p_77191_2_, final int p_77191_3_, final int p_77191_4_, final int p_77191_5_, final Random p_77191_6_) {
        final List<BiomeGenBase.SpawnListEntry> list = p_77191_1_.getSpawnableList(EnumCreatureType.CREATURE);
        if (!list.isEmpty()) {
            while (p_77191_6_.nextFloat() < p_77191_1_.getSpawningChance()) {
                final BiomeGenBase.SpawnListEntry biomegenbase$spawnlistentry = WeightedRandom.getRandomItem(worldIn.rand, list);
                final int i = biomegenbase$spawnlistentry.minGroupCount + p_77191_6_.nextInt(1 + biomegenbase$spawnlistentry.maxGroupCount - biomegenbase$spawnlistentry.minGroupCount);
                IEntityLivingData ientitylivingdata = null;
                int j = p_77191_2_ + p_77191_6_.nextInt(p_77191_4_);
                int k = p_77191_3_ + p_77191_6_.nextInt(p_77191_5_);
                final int l = j;
                final int i2 = k;
                for (int j2 = 0; j2 < i; ++j2) {
                    boolean flag = false;
                    for (int k2 = 0; !flag && k2 < 4; ++k2) {
                        final BlockPos blockpos = worldIn.getTopSolidOrLiquidBlock(new BlockPos(j, 0, k));
                        if (canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, worldIn, blockpos)) {
                            EntityLiving entityliving;
                            try {
                                entityliving = (EntityLiving)biomegenbase$spawnlistentry.entityClass.getConstructor(World.class).newInstance(worldIn);
                            }
                            catch (Exception exception) {
                                exception.printStackTrace();
                                continue;
                            }
                            entityliving.setLocationAndAngles(j + 0.5f, blockpos.getY(), k + 0.5f, p_77191_6_.nextFloat() * 360.0f, 0.0f);
                            worldIn.spawnEntityInWorld(entityliving);
                            ientitylivingdata = entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), ientitylivingdata);
                            flag = true;
                        }
                        for (j += p_77191_6_.nextInt(5) - p_77191_6_.nextInt(5), k += p_77191_6_.nextInt(5) - p_77191_6_.nextInt(5); j < p_77191_2_ || j >= p_77191_2_ + p_77191_4_ || k < p_77191_3_ || k >= p_77191_3_ + p_77191_4_; j = l + p_77191_6_.nextInt(5) - p_77191_6_.nextInt(5), k = i2 + p_77191_6_.nextInt(5) - p_77191_6_.nextInt(5)) {}
                    }
                }
            }
        }
    }
}
