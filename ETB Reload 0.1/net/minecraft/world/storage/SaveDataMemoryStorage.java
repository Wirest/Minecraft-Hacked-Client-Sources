package net.minecraft.world.storage;

import net.minecraft.world.WorldSavedData;

public class SaveDataMemoryStorage extends MapStorage
{
    public SaveDataMemoryStorage()
    {
        super((ISaveHandler)null);
    }

    /**
     * Loads an existing MapDataBase corresponding to the given String id from disk, instantiating the given Class, or
     * returns null if none such file exists. args: Class to instantiate, String dataid
     */
    @Override
	public WorldSavedData loadData(Class <? extends WorldSavedData > clazz, String dataIdentifier)
    {
        return this.loadedDataMap.get(dataIdentifier);
    }

    /**
     * Assigns the given String id to the given MapDataBase, removing any existing ones of the same id.
     */
    @Override
	public void setData(String dataIdentifier, WorldSavedData data)
    {
        this.loadedDataMap.put(dataIdentifier, data);
    }

    /**
     * Saves all dirty loaded MapDataBases to disk.
     */
    @Override
	public void saveAllData()
    {
    }

    /**
     * Returns an unique new data id for the given prefix and saves the idCounts map to the 'idcounts' file.
     */
    @Override
	public int getUniqueDataId(String key)
    {
        return 0;
    }
}
