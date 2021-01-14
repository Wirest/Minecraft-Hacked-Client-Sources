package net.minecraft.world.storage;

import net.minecraft.world.WorldSavedData;

public class SaveDataMemoryStorage extends MapStorage {
   public SaveDataMemoryStorage() {
      super((ISaveHandler)null);
   }

   public WorldSavedData loadData(Class clazz, String dataIdentifier) {
      return (WorldSavedData)this.loadedDataMap.get(dataIdentifier);
   }

   public void setData(String dataIdentifier, WorldSavedData data) {
      this.loadedDataMap.put(dataIdentifier, data);
   }

   public void saveAllData() {
   }

   public int getUniqueDataId(String key) {
      return 0;
   }
}
