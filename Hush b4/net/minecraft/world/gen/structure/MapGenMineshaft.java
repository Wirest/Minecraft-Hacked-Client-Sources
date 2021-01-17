// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure;

import java.util.Iterator;
import net.minecraft.util.MathHelper;
import java.util.Map;

public class MapGenMineshaft extends MapGenStructure
{
    private double field_82673_e;
    
    public MapGenMineshaft() {
        this.field_82673_e = 0.004;
    }
    
    @Override
    public String getStructureName() {
        return "Mineshaft";
    }
    
    public MapGenMineshaft(final Map<String, String> p_i2034_1_) {
        this.field_82673_e = 0.004;
        for (final Map.Entry<String, String> entry : p_i2034_1_.entrySet()) {
            if (entry.getKey().equals("chance")) {
                this.field_82673_e = MathHelper.parseDoubleWithDefault(entry.getValue(), this.field_82673_e);
            }
        }
    }
    
    @Override
    protected boolean canSpawnStructureAtCoords(final int chunkX, final int chunkZ) {
        return this.rand.nextDouble() < this.field_82673_e && this.rand.nextInt(80) < Math.max(Math.abs(chunkX), Math.abs(chunkZ));
    }
    
    @Override
    protected StructureStart getStructureStart(final int chunkX, final int chunkZ) {
        return new StructureMineshaftStart(this.worldObj, this.rand, chunkX, chunkZ);
    }
}
