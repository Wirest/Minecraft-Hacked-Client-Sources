/*    */ package net.minecraft.util;
/*    */ 
/*    */ import net.minecraft.nbt.NBTTagFloat;
/*    */ import net.minecraft.nbt.NBTTagList;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Rotations
/*    */ {
/*    */   protected final float x;
/*    */   protected final float y;
/*    */   protected final float z;
/*    */   
/*    */   public Rotations(float x, float y, float z)
/*    */   {
/* 19 */     this.x = x;
/* 20 */     this.y = y;
/* 21 */     this.z = z;
/*    */   }
/*    */   
/*    */   public Rotations(NBTTagList nbt)
/*    */   {
/* 26 */     this.x = nbt.getFloatAt(0);
/* 27 */     this.y = nbt.getFloatAt(1);
/* 28 */     this.z = nbt.getFloatAt(2);
/*    */   }
/*    */   
/*    */   public NBTTagList writeToNBT()
/*    */   {
/* 33 */     NBTTagList nbttaglist = new NBTTagList();
/* 34 */     nbttaglist.appendTag(new NBTTagFloat(this.x));
/* 35 */     nbttaglist.appendTag(new NBTTagFloat(this.y));
/* 36 */     nbttaglist.appendTag(new NBTTagFloat(this.z));
/* 37 */     return nbttaglist;
/*    */   }
/*    */   
/*    */   public boolean equals(Object p_equals_1_)
/*    */   {
/* 42 */     if (!(p_equals_1_ instanceof Rotations))
/*    */     {
/* 44 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 48 */     Rotations rotations = (Rotations)p_equals_1_;
/* 49 */     return (this.x == rotations.x) && (this.y == rotations.y) && (this.z == rotations.z);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public float getX()
/*    */   {
/* 58 */     return this.x;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public float getY()
/*    */   {
/* 66 */     return this.y;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public float getZ()
/*    */   {
/* 74 */     return this.z;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\Rotations.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */