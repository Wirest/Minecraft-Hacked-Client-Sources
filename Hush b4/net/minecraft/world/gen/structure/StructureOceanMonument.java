// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import com.google.common.collect.Sets;
import net.minecraft.world.ChunkCoordIntPair;
import java.util.Set;
import java.util.Random;
import net.minecraft.util.BlockPos;
import java.util.Iterator;
import net.minecraft.util.MathHelper;
import java.util.Map;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityGuardian;
import com.google.common.collect.Lists;
import java.util.Arrays;
import net.minecraft.world.biome.BiomeGenBase;
import java.util.List;

public class StructureOceanMonument extends MapGenStructure
{
    private int field_175800_f;
    private int field_175801_g;
    public static final List<BiomeGenBase> field_175802_d;
    private static final List<BiomeGenBase.SpawnListEntry> field_175803_h;
    
    static {
        field_175802_d = Arrays.asList(BiomeGenBase.ocean, BiomeGenBase.deepOcean, BiomeGenBase.river, BiomeGenBase.frozenOcean, BiomeGenBase.frozenRiver);
        (field_175803_h = Lists.newArrayList()).add(new BiomeGenBase.SpawnListEntry(EntityGuardian.class, 1, 2, 4));
    }
    
    public StructureOceanMonument() {
        this.field_175800_f = 32;
        this.field_175801_g = 5;
    }
    
    public StructureOceanMonument(final Map<String, String> p_i45608_1_) {
        this();
        for (final Map.Entry<String, String> entry : p_i45608_1_.entrySet()) {
            if (entry.getKey().equals("spacing")) {
                this.field_175800_f = MathHelper.parseIntWithDefaultAndMax(entry.getValue(), this.field_175800_f, 1);
            }
            else {
                if (!entry.getKey().equals("separation")) {
                    continue;
                }
                this.field_175801_g = MathHelper.parseIntWithDefaultAndMax(entry.getValue(), this.field_175801_g, 1);
            }
        }
    }
    
    @Override
    public String getStructureName() {
        return "Monument";
    }
    
    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        final int i = chunkX;
        final int j = chunkZ;
        if (chunkX < 0) {
            chunkX -= this.field_175800_f - 1;
        }
        if (chunkZ < 0) {
            chunkZ -= this.field_175800_f - 1;
        }
        int k = chunkX / this.field_175800_f;
        int l = chunkZ / this.field_175800_f;
        final Random random = this.worldObj.setRandomSeed(k, l, 10387313);
        k *= this.field_175800_f;
        l *= this.field_175800_f;
        k += (random.nextInt(this.field_175800_f - this.field_175801_g) + random.nextInt(this.field_175800_f - this.field_175801_g)) / 2;
        l += (random.nextInt(this.field_175800_f - this.field_175801_g) + random.nextInt(this.field_175800_f - this.field_175801_g)) / 2;
        if (i == k && j == l) {
            if (this.worldObj.getWorldChunkManager().getBiomeGenerator(new BlockPos(i * 16 + 8, 64, j * 16 + 8), null) != BiomeGenBase.deepOcean) {
                return false;
            }
            final boolean flag = this.worldObj.getWorldChunkManager().areBiomesViable(i * 16 + 8, j * 16 + 8, 29, StructureOceanMonument.field_175802_d);
            if (flag) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    protected StructureStart getStructureStart(final int chunkX, final int chunkZ) {
        return new StartMonument(this.worldObj, this.rand, chunkX, chunkZ);
    }
    
    public List<BiomeGenBase.SpawnListEntry> func_175799_b() {
        return StructureOceanMonument.field_175803_h;
    }
    
    public static class StartMonument extends StructureStart
    {
        private Set<ChunkCoordIntPair> field_175791_c;
        private boolean field_175790_d;
        
        public StartMonument() {
            this.field_175791_c = (Set<ChunkCoordIntPair>)Sets.newHashSet();
        }
        
        public StartMonument(final World worldIn, final Random p_i45607_2_, final int p_i45607_3_, final int p_i45607_4_) {
            super(p_i45607_3_, p_i45607_4_);
            this.field_175791_c = (Set<ChunkCoordIntPair>)Sets.newHashSet();
            this.func_175789_b(worldIn, p_i45607_2_, p_i45607_3_, p_i45607_4_);
        }
        
        private void func_175789_b(final World worldIn, final Random p_175789_2_, final int p_175789_3_, final int p_175789_4_) {
            p_175789_2_.setSeed(worldIn.getSeed());
            final long i = p_175789_2_.nextLong();
            final long j = p_175789_2_.nextLong();
            final long k = p_175789_3_ * i;
            final long l = p_175789_4_ * j;
            p_175789_2_.setSeed(k ^ l ^ worldIn.getSeed());
            final int i2 = p_175789_3_ * 16 + 8 - 29;
            final int j2 = p_175789_4_ * 16 + 8 - 29;
            final EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(p_175789_2_);
            this.components.add(new StructureOceanMonumentPieces.MonumentBuilding(p_175789_2_, i2, j2, enumfacing));
            this.updateBoundingBox();
            this.field_175790_d = true;
        }
        
        @Override
        public void generateStructure(final World worldIn, final Random rand, final StructureBoundingBox structurebb) {
            if (!this.field_175790_d) {
                this.components.clear();
                this.func_175789_b(worldIn, rand, this.getChunkPosX(), this.getChunkPosZ());
            }
            super.generateStructure(worldIn, rand, structurebb);
        }
        
        @Override
        public boolean func_175788_a(final ChunkCoordIntPair pair) {
            return !this.field_175791_c.contains(pair) && super.func_175788_a(pair);
        }
        
        @Override
        public void func_175787_b(final ChunkCoordIntPair pair) {
            super.func_175787_b(pair);
            this.field_175791_c.add(pair);
        }
        
        @Override
        public void writeToNBT(final NBTTagCompound tagCompound) {
            super.writeToNBT(tagCompound);
            final NBTTagList nbttaglist = new NBTTagList();
            for (final ChunkCoordIntPair chunkcoordintpair : this.field_175791_c) {
                final NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setInteger("X", chunkcoordintpair.chunkXPos);
                nbttagcompound.setInteger("Z", chunkcoordintpair.chunkZPos);
                nbttaglist.appendTag(nbttagcompound);
            }
            tagCompound.setTag("Processed", nbttaglist);
        }
        
        @Override
        public void readFromNBT(final NBTTagCompound tagCompound) {
            super.readFromNBT(tagCompound);
            if (tagCompound.hasKey("Processed", 9)) {
                final NBTTagList nbttaglist = tagCompound.getTagList("Processed", 10);
                for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                    final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                    this.field_175791_c.add(new ChunkCoordIntPair(nbttagcompound.getInteger("X"), nbttagcompound.getInteger("Z")));
                }
            }
        }
    }
}
