/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ 
/*    */ public class LockCode
/*    */ {
/*  7 */   public static final LockCode EMPTY_CODE = new LockCode("");
/*    */   private final String lock;
/*    */   
/*    */   public LockCode(String code)
/*    */   {
/* 12 */     this.lock = code;
/*    */   }
/*    */   
/*    */   public boolean isEmpty()
/*    */   {
/* 17 */     return (this.lock == null) || (this.lock.isEmpty());
/*    */   }
/*    */   
/*    */   public String getLock()
/*    */   {
/* 22 */     return this.lock;
/*    */   }
/*    */   
/*    */   public void toNBT(NBTTagCompound nbt)
/*    */   {
/* 27 */     nbt.setString("Lock", this.lock);
/*    */   }
/*    */   
/*    */   public static LockCode fromNBT(NBTTagCompound nbt)
/*    */   {
/* 32 */     if (nbt.hasKey("Lock", 8))
/*    */     {
/* 34 */       String s = nbt.getString("Lock");
/* 35 */       return new LockCode(s);
/*    */     }
/*    */     
/*    */ 
/* 39 */     return EMPTY_CODE;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\LockCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */