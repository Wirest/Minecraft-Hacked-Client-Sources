/*    */ package net.minecraft.world.gen.structure;
/*    */ 
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.world.WorldSavedData;
/*    */ 
/*    */ public class MapGenStructureData extends WorldSavedData
/*    */ {
/*  8 */   private NBTTagCompound tagCompound = new NBTTagCompound();
/*    */   
/*    */   public MapGenStructureData(String name)
/*    */   {
/* 12 */     super(name);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void readFromNBT(NBTTagCompound nbt)
/*    */   {
/* 20 */     this.tagCompound = nbt.getCompoundTag("Features");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void writeToNBT(NBTTagCompound nbt)
/*    */   {
/* 28 */     nbt.setTag("Features", this.tagCompound);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void writeInstance(NBTTagCompound tagCompoundIn, int chunkX, int chunkZ)
/*    */   {
/* 37 */     this.tagCompound.setTag(formatChunkCoords(chunkX, chunkZ), tagCompoundIn);
/*    */   }
/*    */   
/*    */   public static String formatChunkCoords(int chunkX, int chunkZ)
/*    */   {
/* 42 */     return "[" + chunkX + "," + chunkZ + "]";
/*    */   }
/*    */   
/*    */   public NBTTagCompound getTagCompound()
/*    */   {
/* 47 */     return this.tagCompound;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\structure\MapGenStructureData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */