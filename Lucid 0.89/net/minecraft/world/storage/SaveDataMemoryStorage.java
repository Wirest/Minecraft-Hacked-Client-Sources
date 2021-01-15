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
	public WorldSavedData loadData(Class p_75742_1_, String p_75742_2_)
    {
        return (WorldSavedData)this.loadedDataMap.get(p_75742_2_);
    }

    /**
     * Assigns the given String id to the given MapDataBase, removing any existing ones of the same id.
     */
    @Override
	public void setData(String p_75745_1_, WorldSavedData p_75745_2_)
    {
        this.loadedDataMap.put(p_75745_1_, p_75745_2_);
    }

    /**
     * Saves all dirty loaded MapDataBases to disk.
     */
    @Override
	public void saveAllData() {}

    /**
     * Returns an unique new data id for the given prefix and saves the idCounts map to the 'idcounts' file.
     */
    @Override
	public int getUniqueDataId(String key)
    {
        return 0;
    }
}
