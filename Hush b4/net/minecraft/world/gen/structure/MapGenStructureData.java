// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

public class MapGenStructureData extends WorldSavedData
{
    private NBTTagCompound tagCompound;
    
    public MapGenStructureData(final String name) {
        super(name);
        this.tagCompound = new NBTTagCompound();
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        this.tagCompound = nbt.getCompoundTag("Features");
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound nbt) {
        nbt.setTag("Features", this.tagCompound);
    }
    
    public void writeInstance(final NBTTagCompound tagCompoundIn, final int chunkX, final int chunkZ) {
        this.tagCompound.setTag(formatChunkCoords(chunkX, chunkZ), tagCompoundIn);
    }
    
    public static String formatChunkCoords(final int chunkX, final int chunkZ) {
        return "[" + chunkX + "," + chunkZ + "]";
    }
    
    public NBTTagCompound getTagCompound() {
        return this.tagCompound;
    }
}
