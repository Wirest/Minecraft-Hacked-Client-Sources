package net.minecraft.world.storage;

import net.minecraft.world.WorldSavedData;

public class SaveDataMemoryStorage extends MapStorage {
   private static final String __OBFID = "CL_00001963";

   public SaveDataMemoryStorage() {
      super((ISaveHandler)null);
   }

   public WorldSavedData loadData(Class p_75742_1_, String p_75742_2_) {
      return (WorldSavedData)this.loadedDataMap.get(p_75742_2_);
   }

   public void setData(String p_75745_1_, WorldSavedData p_75745_2_) {
      this.loadedDataMap.put(p_75745_1_, p_75745_2_);
   }

   public void saveAllData() {
   }

   public int getUniqueDataId(String p_75743_1_) {
      return 0;
   }
}
