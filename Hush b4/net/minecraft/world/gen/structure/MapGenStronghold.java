// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure;

import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import java.util.Random;
import java.util.Iterator;
import net.minecraft.util.MathHelper;
import java.util.Map;
import com.google.common.collect.Lists;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.biome.BiomeGenBase;
import java.util.List;

public class MapGenStronghold extends MapGenStructure
{
    private List<BiomeGenBase> field_151546_e;
    private boolean ranBiomeCheck;
    private ChunkCoordIntPair[] structureCoords;
    private double field_82671_h;
    private int field_82672_i;
    
    public MapGenStronghold() {
        this.structureCoords = new ChunkCoordIntPair[3];
        this.field_82671_h = 32.0;
        this.field_82672_i = 3;
        this.field_151546_e = (List<BiomeGenBase>)Lists.newArrayList();
        BiomeGenBase[] biomeGenArray;
        for (int length = (biomeGenArray = BiomeGenBase.getBiomeGenArray()).length, i = 0; i < length; ++i) {
            final BiomeGenBase biomegenbase = biomeGenArray[i];
            if (biomegenbase != null && biomegenbase.minHeight > 0.0f) {
                this.field_151546_e.add(biomegenbase);
            }
        }
    }
    
    public MapGenStronghold(final Map<String, String> p_i2068_1_) {
        this();
        for (final Map.Entry<String, String> entry : p_i2068_1_.entrySet()) {
            if (entry.getKey().equals("distance")) {
                this.field_82671_h = MathHelper.parseDoubleWithDefaultAndMax(entry.getValue(), this.field_82671_h, 1.0);
            }
            else if (entry.getKey().equals("count")) {
                this.structureCoords = new ChunkCoordIntPair[MathHelper.parseIntWithDefaultAndMax(entry.getValue(), this.structureCoords.length, 1)];
            }
            else {
                if (!entry.getKey().equals("spread")) {
                    continue;
                }
                this.field_82672_i = MathHelper.parseIntWithDefaultAndMax(entry.getValue(), this.field_82672_i, 1);
            }
        }
    }
    
    @Override
    public String getStructureName() {
        return "Stronghold";
    }
    
    @Override
    protected boolean canSpawnStructureAtCoords(final int chunkX, final int chunkZ) {
        if (!this.ranBiomeCheck) {
            final Random random = new Random();
            random.setSeed(this.worldObj.getSeed());
            double d0 = random.nextDouble() * 3.141592653589793 * 2.0;
            int i = 1;
            for (int j = 0; j < this.structureCoords.length; ++j) {
                final double d2 = (1.25 * i + random.nextDouble()) * this.field_82671_h * i;
                int k = (int)Math.round(Math.cos(d0) * d2);
                int l = (int)Math.round(Math.sin(d0) * d2);
                final BlockPos blockpos = this.worldObj.getWorldChunkManager().findBiomePosition((k << 4) + 8, (l << 4) + 8, 112, this.field_151546_e, random);
                if (blockpos != null) {
                    k = blockpos.getX() >> 4;
                    l = blockpos.getZ() >> 4;
                }
                this.structureCoords[j] = new ChunkCoordIntPair(k, l);
                d0 += 6.283185307179586 * i / this.field_82672_i;
                if (j == this.field_82672_i) {
                    i += 2 + random.nextInt(5);
                    this.field_82672_i += 1 + random.nextInt(2);
                }
            }
            this.ranBiomeCheck = true;
        }
        ChunkCoordIntPair[] structureCoords;
        for (int length = (structureCoords = this.structureCoords).length, n = 0; n < length; ++n) {
            final ChunkCoordIntPair chunkcoordintpair = structureCoords[n];
            if (chunkX == chunkcoordintpair.chunkXPos && chunkZ == chunkcoordintpair.chunkZPos) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    protected List<BlockPos> getCoordList() {
        final List<BlockPos> list = (List<BlockPos>)Lists.newArrayList();
        ChunkCoordIntPair[] structureCoords;
        for (int length = (structureCoords = this.structureCoords).length, i = 0; i < length; ++i) {
            final ChunkCoordIntPair chunkcoordintpair = structureCoords[i];
            if (chunkcoordintpair != null) {
                list.add(chunkcoordintpair.getCenterBlock(64));
            }
        }
        return list;
    }
    
    @Override
    protected StructureStart getStructureStart(final int chunkX, final int chunkZ) {
        Start mapgenstronghold$start;
        for (mapgenstronghold$start = new Start(this.worldObj, this.rand, chunkX, chunkZ); mapgenstronghold$start.getComponents().isEmpty() || mapgenstronghold$start.getComponents().get(0).strongholdPortalRoom == null; mapgenstronghold$start = new Start(this.worldObj, this.rand, chunkX, chunkZ)) {}
        return mapgenstronghold$start;
    }
    
    public static class Start extends StructureStart
    {
        public Start() {
        }
        
        public Start(final World worldIn, final Random p_i2067_2_, final int p_i2067_3_, final int p_i2067_4_) {
            super(p_i2067_3_, p_i2067_4_);
            StructureStrongholdPieces.prepareStructurePieces();
            final StructureStrongholdPieces.Stairs2 structurestrongholdpieces$stairs2 = new StructureStrongholdPieces.Stairs2(0, p_i2067_2_, (p_i2067_3_ << 4) + 2, (p_i2067_4_ << 4) + 2);
            this.components.add(structurestrongholdpieces$stairs2);
            structurestrongholdpieces$stairs2.buildComponent(structurestrongholdpieces$stairs2, this.components, p_i2067_2_);
            final List<StructureComponent> list = structurestrongholdpieces$stairs2.field_75026_c;
            while (!list.isEmpty()) {
                final int i = p_i2067_2_.nextInt(list.size());
                final StructureComponent structurecomponent = list.remove(i);
                structurecomponent.buildComponent(structurestrongholdpieces$stairs2, this.components, p_i2067_2_);
            }
            this.updateBoundingBox();
            this.markAvailableHeight(worldIn, p_i2067_2_, 10);
        }
    }
}
