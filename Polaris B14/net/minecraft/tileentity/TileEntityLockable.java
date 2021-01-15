/*    */ package net.minecraft.tileentity;
/*    */ 
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.util.ChatComponentText;
/*    */ import net.minecraft.util.ChatComponentTranslation;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import net.minecraft.world.IInteractionObject;
/*    */ import net.minecraft.world.ILockableContainer;
/*    */ import net.minecraft.world.LockCode;
/*    */ 
/*    */ public abstract class TileEntityLockable extends TileEntity implements IInteractionObject, ILockableContainer
/*    */ {
/* 13 */   private LockCode code = LockCode.EMPTY_CODE;
/*    */   
/*    */   public void readFromNBT(NBTTagCompound compound)
/*    */   {
/* 17 */     super.readFromNBT(compound);
/* 18 */     this.code = LockCode.fromNBT(compound);
/*    */   }
/*    */   
/*    */   public void writeToNBT(NBTTagCompound compound)
/*    */   {
/* 23 */     super.writeToNBT(compound);
/*    */     
/* 25 */     if (this.code != null)
/*    */     {
/* 27 */       this.code.toNBT(compound);
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean isLocked()
/*    */   {
/* 33 */     return (this.code != null) && (!this.code.isEmpty());
/*    */   }
/*    */   
/*    */   public LockCode getLockCode()
/*    */   {
/* 38 */     return this.code;
/*    */   }
/*    */   
/*    */   public void setLockCode(LockCode code)
/*    */   {
/* 43 */     this.code = code;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public IChatComponent getDisplayName()
/*    */   {
/* 51 */     return hasCustomName() ? new ChatComponentText(getName()) : new ChatComponentTranslation(getName(), new Object[0]);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\tileentity\TileEntityLockable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */