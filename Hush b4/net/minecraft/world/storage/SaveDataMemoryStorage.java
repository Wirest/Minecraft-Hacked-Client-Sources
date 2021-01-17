// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage;

import net.minecraft.world.WorldSavedData;

public class SaveDataMemoryStorage extends MapStorage
{
    public SaveDataMemoryStorage() {
        super(null);
    }
    
    @Override
    public WorldSavedData loadData(final Class<? extends WorldSavedData> clazz, final String dataIdentifier) {
        return this.loadedDataMap.get(dataIdentifier);
    }
    
    @Override
    public void setData(final String dataIdentifier, final WorldSavedData data) {
        this.loadedDataMap.put(dataIdentifier, data);
    }
    
    @Override
    public void saveAllData() {
    }
    
    @Override
    public int getUniqueDataId(final String key) {
        return 0;
    }
}
