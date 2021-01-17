// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityBlaze;
import com.google.common.collect.Lists;
import net.minecraft.world.biome.BiomeGenBase;
import java.util.List;

public class MapGenNetherBridge extends MapGenStructure
{
    private List<BiomeGenBase.SpawnListEntry> spawnList;
    
    public MapGenNetherBridge() {
        (this.spawnList = (List<BiomeGenBase.SpawnListEntry>)Lists.newArrayList()).add(new BiomeGenBase.SpawnListEntry(EntityBlaze.class, 10, 2, 3));
        this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntityPigZombie.class, 5, 4, 4));
        this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntitySkeleton.class, 10, 4, 4));
        this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntityMagmaCube.class, 3, 4, 4));
    }
    
    @Override
    public String getStructureName() {
        return "Fortress";
    }
    
    public List<BiomeGenBase.SpawnListEntry> getSpawnList() {
        return this.spawnList;
    }
    
    @Override
    protected boolean canSpawnStructureAtCoords(final int chunkX, final int chunkZ) {
        final int i = chunkX >> 4;
        final int j = chunkZ >> 4;
        this.rand.setSeed((long)(i ^ j << 4) ^ this.worldObj.getSeed());
        this.rand.nextInt();
        return this.rand.nextInt(3) == 0 && chunkX == (i << 4) + 4 + this.rand.nextInt(8) && chunkZ == (j << 4) + 4 + this.rand.nextInt(8);
    }
    
    @Override
    protected StructureStart getStructureStart(final int chunkX, final int chunkZ) {
        return new Start(this.worldObj, this.rand, chunkX, chunkZ);
    }
    
    public static class Start extends StructureStart
    {
        public Start() {
        }
        
        public Start(final World worldIn, final Random p_i2040_2_, final int p_i2040_3_, final int p_i2040_4_) {
            super(p_i2040_3_, p_i2040_4_);
            final StructureNetherBridgePieces.Start structurenetherbridgepieces$start = new StructureNetherBridgePieces.Start(p_i2040_2_, (p_i2040_3_ << 4) + 2, (p_i2040_4_ << 4) + 2);
            this.components.add(structurenetherbridgepieces$start);
            structurenetherbridgepieces$start.buildComponent(structurenetherbridgepieces$start, this.components, p_i2040_2_);
            final List<StructureComponent> list = structurenetherbridgepieces$start.field_74967_d;
            while (!list.isEmpty()) {
                final int i = p_i2040_2_.nextInt(list.size());
                final StructureComponent structurecomponent = list.remove(i);
                structurecomponent.buildComponent(structurenetherbridgepieces$start, this.components, p_i2040_2_);
            }
            this.updateBoundingBox();
            this.setRandomHeight(worldIn, p_i2040_2_, 48, 70);
        }
    }
}
