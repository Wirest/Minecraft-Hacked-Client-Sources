// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import java.util.Random;
import java.util.Iterator;
import net.minecraft.util.MathHelper;
import java.util.Map;
import java.util.Arrays;
import net.minecraft.world.biome.BiomeGenBase;
import java.util.List;

public class MapGenVillage extends MapGenStructure
{
    public static final List<BiomeGenBase> villageSpawnBiomes;
    private int terrainType;
    private int field_82665_g;
    private int field_82666_h;
    
    static {
        villageSpawnBiomes = Arrays.asList(BiomeGenBase.plains, BiomeGenBase.desert, BiomeGenBase.savanna);
    }
    
    public MapGenVillage() {
        this.field_82665_g = 32;
        this.field_82666_h = 8;
    }
    
    public MapGenVillage(final Map<String, String> p_i2093_1_) {
        this();
        for (final Map.Entry<String, String> entry : p_i2093_1_.entrySet()) {
            if (entry.getKey().equals("size")) {
                this.terrainType = MathHelper.parseIntWithDefaultAndMax(entry.getValue(), this.terrainType, 0);
            }
            else {
                if (!entry.getKey().equals("distance")) {
                    continue;
                }
                this.field_82665_g = MathHelper.parseIntWithDefaultAndMax(entry.getValue(), this.field_82665_g, this.field_82666_h + 1);
            }
        }
    }
    
    @Override
    public String getStructureName() {
        return "Village";
    }
    
    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        final int i = chunkX;
        final int j = chunkZ;
        if (chunkX < 0) {
            chunkX -= this.field_82665_g - 1;
        }
        if (chunkZ < 0) {
            chunkZ -= this.field_82665_g - 1;
        }
        int k = chunkX / this.field_82665_g;
        int l = chunkZ / this.field_82665_g;
        final Random random = this.worldObj.setRandomSeed(k, l, 10387312);
        k *= this.field_82665_g;
        l *= this.field_82665_g;
        k += random.nextInt(this.field_82665_g - this.field_82666_h);
        l += random.nextInt(this.field_82665_g - this.field_82666_h);
        if (i == k && j == l) {
            final boolean flag = this.worldObj.getWorldChunkManager().areBiomesViable(i * 16 + 8, j * 16 + 8, 0, MapGenVillage.villageSpawnBiomes);
            if (flag) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    protected StructureStart getStructureStart(final int chunkX, final int chunkZ) {
        return new Start(this.worldObj, this.rand, chunkX, chunkZ, this.terrainType);
    }
    
    public static class Start extends StructureStart
    {
        private boolean hasMoreThanTwoComponents;
        
        public Start() {
        }
        
        public Start(final World worldIn, final Random rand, final int x, final int z, final int p_i2092_5_) {
            super(x, z);
            final List<StructureVillagePieces.PieceWeight> list = StructureVillagePieces.getStructureVillageWeightedPieceList(rand, p_i2092_5_);
            final StructureVillagePieces.Start structurevillagepieces$start = new StructureVillagePieces.Start(worldIn.getWorldChunkManager(), 0, rand, (x << 4) + 2, (z << 4) + 2, list, p_i2092_5_);
            this.components.add(structurevillagepieces$start);
            structurevillagepieces$start.buildComponent(structurevillagepieces$start, this.components, rand);
            final List<StructureComponent> list2 = structurevillagepieces$start.field_74930_j;
            final List<StructureComponent> list3 = structurevillagepieces$start.field_74932_i;
            while (!list2.isEmpty() || !list3.isEmpty()) {
                if (list2.isEmpty()) {
                    final int i = rand.nextInt(list3.size());
                    final StructureComponent structurecomponent = list3.remove(i);
                    structurecomponent.buildComponent(structurevillagepieces$start, this.components, rand);
                }
                else {
                    final int j = rand.nextInt(list2.size());
                    final StructureComponent structurecomponent2 = list2.remove(j);
                    structurecomponent2.buildComponent(structurevillagepieces$start, this.components, rand);
                }
            }
            this.updateBoundingBox();
            int k = 0;
            for (final StructureComponent structurecomponent3 : this.components) {
                if (!(structurecomponent3 instanceof StructureVillagePieces.Road)) {
                    ++k;
                }
            }
            this.hasMoreThanTwoComponents = (k > 2);
        }
        
        @Override
        public boolean isSizeableStructure() {
            return this.hasMoreThanTwoComponents;
        }
        
        @Override
        public void writeToNBT(final NBTTagCompound tagCompound) {
            super.writeToNBT(tagCompound);
            tagCompound.setBoolean("Valid", this.hasMoreThanTwoComponents);
        }
        
        @Override
        public void readFromNBT(final NBTTagCompound tagCompound) {
            super.readFromNBT(tagCompound);
            this.hasMoreThanTwoComponents = tagCompound.getBoolean("Valid");
        }
    }
}
