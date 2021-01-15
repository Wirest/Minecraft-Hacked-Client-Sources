/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class WorldSavedData
/*    */ {
/*    */   public final String mapName;
/*    */   private boolean dirty;
/*    */   
/*    */   public WorldSavedData(String name)
/*    */   {
/* 15 */     this.mapName = name;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract void readFromNBT(NBTTagCompound paramNBTTagCompound);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract void writeToNBT(NBTTagCompound paramNBTTagCompound);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void markDirty()
/*    */   {
/* 33 */     setDirty(true);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setDirty(boolean isDirty)
/*    */   {
/* 41 */     this.dirty = isDirty;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isDirty()
/*    */   {
/* 49 */     return this.dirty;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\WorldSavedData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */